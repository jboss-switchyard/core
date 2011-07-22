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

package org.switchyard.handlers;

import javax.xml.namespace.QName;

import org.junit.Before;
import org.junit.Test;
import org.switchyard.Exchange;
import org.switchyard.MockDomain;
import org.switchyard.MockHandler;
import org.switchyard.ServiceDomain;
import org.switchyard.ServiceReference;
import org.switchyard.metadata.ExchangeContract;

public class MessageTraceTest {

    private ServiceDomain _domain;
    
    @Before
    public void setUp() throws Exception {
        _domain = new MockDomain();
        _domain.getHandlerChain().addFirst("trace", new MessageTrace());
    }
    
    @Test
    public void testInMessageTrace() {
        ServiceReference service = _domain.registerService(
                new QName("InTrace"), new MockHandler());
        Exchange exchange = _domain.createExchange(service, ExchangeContract.IN_ONLY);
        exchange.send(exchange.createMessage());
    }
    
    @Test
    public void testInOutMessageTrace() throws Exception {
        ServiceReference service = _domain.registerService(
                new QName("InOutTrace"), new MockHandler().forwardInToOut());
        Exchange exchange = _domain.createExchange(service, ExchangeContract.IN_OUT, new MockHandler());
        exchange.send(exchange.createMessage());
    }

    @Test
    public void testInFaultMessageTrace() throws Exception {
        ServiceReference service = _domain.registerService(
                new QName("InFaultTrace"), new MockHandler().forwardInToFault());
        Exchange exchange = _domain.createExchange(service, ExchangeContract.IN_OUT, new MockHandler());
        exchange.send(exchange.createMessage());
    }
}
