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
package org.switchyard.bus.camel.processors;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.switchyard.ExchangeHandler;
import org.switchyard.bus.camel.ExchangeDispatcher;

/**
 * Processor wrapping SwitchYard {@link ExchangeHandler} invocation.
 */
public class HandlerProcessor implements Processor {

    private List<ExchangeHandler> _handlers;

    /**
     * Creates new processor which delegates execution to SwitchYard {@link ExchangeHandler}.
     * 
     * @param handler Handler to wrap.
     */
    public HandlerProcessor(ExchangeHandler handler) {
        this(Arrays.asList(handler));
    }

    /**
     * Creates new processor which delegates execution to collection of exchange handlers.
     * 
     * @param handlers Exchange handlers used to process messages during dispatching.
     */
    public HandlerProcessor(List<ExchangeHandler> handlers) {
        _handlers = handlers;
    }

    @Override
    public void process(Exchange ex) throws Exception {
        for (ExchangeHandler handler : _handlers) {
            handler.handleMessage(ex.getProperty(ExchangeDispatcher.SY_EXCHANGE,
                org.switchyard.Exchange.class));
        }
    }
}
