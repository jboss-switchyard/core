/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved. 
 * See the copyright.txt in the distribution for a 
 * full listing of individual contributors.
 *
 * This copyrighted material is made available to anyone wishing to use, 
 * modify, copy, or redistribute it subject to the terms and conditions 
 * of the GNU Lesser General Public License, v. 2.1. 
 * This program is distributed in the hope that it will be useful, but WITHOUT A 
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details. 
 * You should have received a copy of the GNU Lesser General Public License, 
 * v.2.1 along with this distribution; if not, write to the Free Software 
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */

package org.switchyard.internal;

import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

import org.switchyard.Exchange;
import org.switchyard.HandlerChain;
import org.switchyard.Service;
import org.switchyard.spi.Dispatcher;
import org.switchyard.spi.ExchangeBus;
import org.switchyard.transform.TransformerRegistry;

/**
 * Default endpoint provider.
 */
public class LocalExchangeBus implements ExchangeBus {

    private ConcurrentHashMap<QName, Dispatcher> _dispatchers = 
        new ConcurrentHashMap<QName, Dispatcher>();

    @Override
    public synchronized Dispatcher createDispatcher(
            Service service, HandlerChain handlerChain, TransformerRegistry transformerRegistry) {
        Dispatcher dispatcher = new LocalDispatcher(service, handlerChain);
        _dispatchers.put(service.getName(), dispatcher);
        return dispatcher;
    }

    @Override
    public Dispatcher getDispatcher(Service service) {
        return _dispatchers.get(service.getName());
    }
}

class LocalDispatcher implements Dispatcher {
    private HandlerChain _handlerChain;
    private Service _service;

    /**
     * Constructor.
     * @param handlerChain handler chain
     */
    LocalDispatcher(final Service service, final HandlerChain handlerChain) {
        _service = service;
        _handlerChain = handlerChain;
    }

    @Override
    public void dispatch(final Exchange exchange) {
        switch (exchange.getPhase()) {
        case IN:
            _handlerChain.handle(exchange);
            break;
        case OUT:
            ((ExchangeImpl)exchange).getReplyChain().handle(exchange);
            break;
        default:
            throw new IllegalStateException("Invalid phase for dispatch: " + exchange.getPhase());
        }
    }

    @Override
    public Service getService() {
        return _service;
    }

    @Override
    public void start() {
        // NOP
    }

    @Override
    public void stop() {
        // NOP
    }
}
