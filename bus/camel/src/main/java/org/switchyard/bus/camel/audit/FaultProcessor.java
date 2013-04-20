/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.switchyard.bus.camel.audit;

import org.apache.camel.AsyncCallback;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.processor.DelegateAsyncProcessor;
import org.apache.log4j.Logger;
import org.switchyard.bus.camel.CamelExchange;
<<<<<<< HEAD
=======
import org.switchyard.bus.camel.BusLogger;
import org.switchyard.bus.camel.BusMessages;
import org.switchyard.bus.camel.ErrorListener;
import org.switchyard.common.lang.Strings;
>>>>>>> SWITCHYARD-1320

/**
 * This processor catches exceptions from camel thrown during handling fault replies
 * from switchyard.
 * 
 * If exception occurs during fault handling it will be ignored.
 */
public class FaultProcessor extends DelegateAsyncProcessor {

    private Logger _logger = Logger.getLogger(FaultProcessor.class);

    /**
     * Creates new fault processor.
     * 
     * @param target Wrapped processor.
     */
    public FaultProcessor(Processor target) {
        super(target);
    }

    @Override
    public boolean process(final Exchange exchange, final AsyncCallback callback) {
        return super.process(exchange, new AsyncCallback() {
            @Override
            public void done(boolean doneSync) {
                if (doneSync) { // verify exchange only if processing is done
                    if (CamelExchange.isFault(exchange) && exchange.getException() != null) {
                        handle(exchange.getException(), exchange);
                        exchange.setException(null);
                    }
                }
                callback.done(doneSync);
            }
        });
    }

    /**
     * Method which tries to send FAULT if there is exception reported by processor
     * and exchange state is still OK.
     * 
     * @param throwable Exception thrown by target processor.
     * @param camel Camel exchange. 
     * @param exchange SwitchYard exchange related to exception.
     */
    protected void handle(Throwable throwable, Exchange exchange) {
      
        // exception thrown during handling FAULT state cannot be forwarded
        // anywhere, because we already have problem to handle
        BusLogger.ROOT_LOGGER.exceptionDuringFaultResponse(throwable);
    }

    protected void dumpExceptionContents(Throwable throwable) {
        if (_logger.isDebugEnabled()) {
            String message = "Caught exception of type %s with message: %s";
            String causeTrace = "";

            if (throwable.getCause() != null) {
                String causedBy = "\n%sCaused by exception of type %s, message: %s";
                Throwable cause = throwable.getCause();
                int depth = 0;
                while (cause != null) {
                    causeTrace += String.format(causedBy, Strings.repeat("  ", ++depth), cause.getClass().getName(), cause.getMessage());
                    cause = cause.getCause();
                }
            }

            _logger.debug(String.format(message, throwable.getClass().getName(), throwable.getMessage()) + causeTrace, throwable);
        }

    }

    protected void notifyListeners(CamelContext context, org.switchyard.Exchange exchange, Throwable exception) {
        Map<String, ErrorListener> listeners = context.getRegistry().lookupByType(ErrorListener.class);
        if (listeners != null && listeners.size() > 0) {
            for (Entry<String, ErrorListener> entry : listeners.entrySet()) {
                try {
                    entry.getValue().notify(exchange, exception);
                } catch (Exception e) {
                    BusLogger.ROOT_LOGGER.listenerFailedHandleException(entry.getKey(), exception.getClass());
                }
            }
        }
    }

    private Throwable detectHandlerException(Throwable throwable) {
        if (throwable instanceof HandlerException) {
            return (HandlerException) throwable;
        }
        return new HandlerException(throwable);
>>>>>>> SWITCHYARD-1320
    }

    @Override
    public String toString() {
        return BusMessages.MESSAGES.faultProcessorString(getProcessor());
    }

}
