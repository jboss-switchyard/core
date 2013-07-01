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

package org.switchyard.bus.camel;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.switchyard.Exchange;
import org.switchyard.ExchangeHandler;
import org.switchyard.ExchangePattern;
import org.switchyard.ExchangePhase;
import org.switchyard.ServiceReference;
import org.switchyard.spi.Dispatcher;

/**
 * Creates a Dispatcher instance for handling message exchange for a SwitchYard
 * service.
 */
public class ExchangeDispatcher implements Dispatcher {

    private final ModelCamelContext _context;
    private ServiceReference _reference;
    private ProducerTemplate _producer;

    /**
     * Create a new Dispatcher instance.
     * @param context Camel context instance
     * @param reference dispatch for this reference
     */
    public ExchangeDispatcher(ModelCamelContext context, ServiceReference reference) {
        _context = context;
        _reference = reference;
        _producer = context.createProducerTemplate();
    }

    @Override
    public ServiceReference getServiceReference() {
        return _reference;
    }

    @Override
    public Exchange createExchange(ExchangeHandler replyHandler, ExchangePattern pattern) {
        DefaultExchange exchange = new DefaultExchange(_context, translate(pattern));
        exchange.setIn(new CamelMessage(exchange));
        return new CamelExchange(this, exchange, replyHandler);
    }

    private org.apache.camel.ExchangePattern translate(ExchangePattern pattern) {
        return ExchangePattern.IN_OUT == pattern ? org.apache.camel.ExchangePattern.InOut : org.apache.camel.ExchangePattern.InOnly;
    }

    @Override
    public void dispatch(final Exchange exchange) {
        if (exchange instanceof CamelExchange) {
            if (exchange.getPhase().equals(ExchangePhase.IN)) {
                _producer.send("direct:" + exchange.getConsumer().getName(), ((CamelExchange) exchange).getExchange());
            }
        } else {
            throw new IllegalArgumentException("Camel Bus accepts only CamelExchanges");
        }
    }

}
