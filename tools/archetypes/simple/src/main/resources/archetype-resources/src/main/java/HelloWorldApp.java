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
package ${groupId};

import javax.xml.namespace.QName;

import org.switchyard.Exchange;
import org.switchyard.ExchangePattern;
import org.switchyard.Message;
import org.switchyard.MessageBuilder;
import org.switchyard.MockHandler;
import org.switchyard.ServiceDomain;
import org.switchyard.internal.ServiceDomains;

import org.switchyard.component.bean.BeanServiceMetadata;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.jboss.weld.environment.se.events.ContainerInitialized;

import ${groupId}.model.HelloRequest;
import ${groupId}.model.HelloResponse;
import ${groupId}.service.HelloWorldManagementService;

public class HelloWorldApp {

    private WeldContainer _weld;
    public static final String MESSAGE = "Hello, World!";

    public void init() {
        _weld = new Weld().initialize();
        _weld.event().select(ContainerInitialized.class).fire(new ContainerInitialized());
    }

    public HelloResponse send() {
        ServiceDomain domain = ServiceDomains.getDomain();

        // Consume the OM model...
        MockHandler responseConsumer = new MockHandler();
        Exchange exchange = domain.createExchange(new QName("HelloWorldManagementService"), ExchangePattern.IN_OUT, responseConsumer);
        BeanServiceMetadata.setOperationName(exchange, "createHello");

        Message inMessage = MessageBuilder.newInstance().buildMessage();
        inMessage.setContent(new HelloRequest(MESSAGE));

        exchange.send(inMessage);

        // wait, since this is async
        responseConsumer.waitForOKMessage();

        HelloResponse response = (HelloResponse) responseConsumer.getMessages().poll().getMessage().getContent();
        return response;
    }

    public void main (String args[]) {
        HelloWorldApp hwa = new HelloWorldApp();
        hwa.init();
        HelloResponse response = hwa.send();
        System.out.println(response.getMessage());
    }
}
