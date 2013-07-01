/*
 * 2012 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.switchyard;

import org.apache.log4j.Logger;
import org.switchyard.exception.DeliveryException;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Synchronous IN_OUT exchange handler.
 * <p/>
 * Provides a blocking wait for the OUT exchange message.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class SynchronousInOutHandler implements ExchangeHandler {

    /**
     * Default timeout.
     */
    public static final long DEFAULT_TIMEOUT = 1000 * 60 * 5;

    private static final Logger LOGGER = Logger.getLogger(SynchronousInOutHandler.class);

    private BlockingQueue<Exchange> _responseQueue = new ArrayBlockingQueue<Exchange>(1);

    /**
     * Wait for an OUT Exchange message.
     * <p/>
     * Uses the {@link #DEFAULT_TIMEOUT}.
     *
     * @return The OUT Exchange instance.
     * @throws DeliveryException Timeout or interrupt while waiting on OUT message.
     */
    public Exchange waitForOut() throws DeliveryException {
        return waitForOut(DEFAULT_TIMEOUT);
    }

    /**
     * Wait for an OUT Exchange message.
     *
     * @param timeout The timeout in milliseconds.
     * @return The OUT Exchange instance.
     * @throws DeliveryException Timeout or interrupt while waiting on OUT message.
     */
    public Exchange waitForOut(long timeout) throws DeliveryException {
        try {
            Exchange outExchange;

            try {
                outExchange = _responseQueue.poll(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                synchronized (this) {
                    _responseQueue = null;
                    throw new DeliveryException("Unexpected interrupt while waiting on OUT Exchange message.", e);
                }
            }

            if (outExchange == null) {
                // we've timed out waiting on the OUT message...
                synchronized (this) {
                    if (!_responseQueue.isEmpty()) {
                        // message arrived just after timeout... we're OK...
                        outExchange = _responseQueue.poll();
                    } else {
                        _responseQueue = null;
                        throw new DeliveryException("Timed out waiting on OUT Exchange message.");
                    }
                }
            }

            return outExchange;
        } finally {
            _responseQueue = null;
        }
    }

    @Override
    public synchronized void handleMessage(Exchange exchange) throws HandlerException {
        if (_responseQueue == null) {
            LOGGER.debug("OUT Exchange arrived after timeout has elapsed.");
        } else {
            try {
                _responseQueue.put(exchange);
            } catch (InterruptedException e) {
                throw new HandlerException(e);
            }
        }
    }

    @Override
    public synchronized void handleFault(Exchange exchange) {
        if (_responseQueue == null) {
            LOGGER.debug("OUT Exchange arrived after timeout has elapsed.");
        } else {
            try {
                _responseQueue.put(exchange);
            } catch (InterruptedException e) {
                throw new IllegalStateException("Unexpected Interrupt exception.", e);
            }
        }
    }
}
