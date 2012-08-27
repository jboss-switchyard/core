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
package org.switchyard.common.camel;

import java.util.EventObject;

import javax.xml.namespace.QName;

import org.apache.log4j.Logger;
import org.switchyard.ServiceDomain;
import org.switchyard.event.DomainShutdownEvent;
import org.switchyard.event.DomainStartupEvent;
import org.switchyard.event.EventObserver;

/**
 * Simple class designed to link {@link SwitchYardCamelContext} instances 
 * with service domains.
 */
public class CamelContextManager implements EventObserver {

    /**
     * Name of attachement property set on {@link ServiceDomain}.
     */
    public static final String CAMEL_CONTEXT_PROPERTY = "CamelContext";

    private Logger _logger = Logger.getLogger(CamelContextManager.class);

    @Override
    public void notify(EventObject event) {
        if (event instanceof DomainStartupEvent) {
            createContext(((DomainStartupEvent) event).getDomain());
        } else if (event instanceof DomainShutdownEvent) {
            destroyContext(((DomainShutdownEvent) event).getDomain());
        }
    }

    private void destroyContext(ServiceDomain domain) {
        QName domainName = domain.getName();
        if (_logger.isDebugEnabled()) {
            _logger.debug("Destroying Camel context for " + domainName);
        }

        domain.getProperties().remove(CAMEL_CONTEXT_PROPERTY);
    }

    private void createContext(ServiceDomain domain) {
        QName domainName = domain.getName();
        if (_logger.isDebugEnabled()) {
            _logger.debug("Creating Camel context for " + domainName);
        }

        if (!domain.getProperties().containsKey(CAMEL_CONTEXT_PROPERTY)) {
            SwitchYardCamelContext context = new SwitchYardCamelContext(domain);
            domain.getProperties().put(CAMEL_CONTEXT_PROPERTY, context);
        } else {
            _logger.warn("Camel context already present, skipping creation");
        }


    }

}
