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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.Set;

import org.apache.log4j.Logger;
import org.switchyard.Exchange;
import org.switchyard.ExchangeHandler;
import org.switchyard.HandlerException;
import org.switchyard.Message;
import org.switchyard.Property;
import org.switchyard.Scope;
import org.switchyard.common.lang.Strings;

/**
 * Half-baked message tracing implementation, used primarily as an example
 * at this point.
 */
public class MessageTrace implements ExchangeHandler {
    
    private static final String INDENT = System.getProperty("line.separator");
    private static Logger _log = Logger.getLogger(MessageTrace.class);

    @Override
    public void handleFault(Exchange exchange) {
        if (_log.isInfoEnabled()) {
            _log.info(createTrace(exchange));
        }
    }

    @Override
    public void handleMessage(Exchange exchange) throws HandlerException {
        if (_log.isInfoEnabled()) {
            _log.info(createTrace(exchange));
        }
    }

    String createTrace(Exchange exchange) {        
        StringBuilder summary = new StringBuilder()
            .append(indent(0) + "------- Begin Message Trace -------")
            .append(indent(0) + "Consumer -> " + exchange.getConsumer().getName())
            .append(indent(0) + "Provider -> " + ((exchange.getProvider() == null) ? "[unassigned]" : exchange.getProvider().getName()))
            .append(indent(0) + "Operation -> " + exchange.getContract().getConsumerOperation().getName())
            .append(indent(0) + "MEP -> " 
                + ((exchange.getContract().getConsumerOperation().getExchangePattern() == null) ? "[unassigned]" : exchange.getContract().getConsumerOperation().getExchangePattern()))
            .append(indent(0) + "Phase -> " + exchange.getPhase())
            .append(indent(0) + "State -> " + exchange.getState());
        
        // Add context properties
        summary.append(indent(0) + "Exchange Context -> ");
        dumpContext(summary, exchange.getContext().getProperties(Scope.EXCHANGE));

        summary.append(indent(0) + "Message Context -> ");
        dumpContext(summary, exchange.getContext().getProperties(Scope.MESSAGE));

        // Add message content
        String content = null;
        try {
            // try to convert the payload to a string
            Message msg = exchange.getMessage();
            content = msg.getContent(String.class);
            
            // check to see if we have to put content back into the message 
            // after the conversion to string
            if (InputStream.class.isAssignableFrom(msg.getContent().getClass())) {
                msg.setContent(new ByteArrayInputStream(content.getBytes()));
            } else if (Reader.class.isAssignableFrom(msg.getContent().getClass())) {
                msg.setContent(new StringReader(content));
            }
        } catch (Exception ex) {
            // conversion failed, fall back on toString()
            if (exchange.getMessage().getContent() != null) {
                content = exchange.getMessage().getContent().toString();
            }
        }
        summary.append(indent(0) + "Message Content -> ")
            .append(indent(0) + content);
        
        summary.append(indent(0) + "------ End Message Trace -------");
        return summary.toString();
    }

    private void dumpContext(StringBuilder summary, Set<Property> properties) {
        for (Property property : properties) {
            summary.append(indent(1) + property.getName() + " : " + property.getValue());
        }
    }

    private String indent(int column) {
        return INDENT + Strings.repeat("\t", column);
    }
}
