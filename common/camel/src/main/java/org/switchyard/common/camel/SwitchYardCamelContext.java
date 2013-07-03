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

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.camel.component.cdi.CdiBeanRegistry;
import org.apache.camel.component.cdi.CdiInjector;
import org.apache.camel.impl.CompositeRegistry;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.impl.SimpleRegistry;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.spi.EventNotifier;
import org.apache.camel.spi.PackageScanClassResolver;
import org.apache.camel.spi.Registry;
import org.apache.log4j.Logger;
import org.switchyard.ServiceDomain;
import org.switchyard.common.camel.event.CamelEventBridge;
import org.switchyard.common.cdi.CDIUtil;

/**
 * Extension of default camel context. Supports access to mutable registry and
 * provides integration with SwitchYard eventing model.
 */
public interface SwitchYardCamelContext extends ModelCamelContext {

    /**
     * Context property name used to store camel context as service domain property.
     */
    String CAMEL_CONTEXT_PROPERTY = "CamelContextProperty";

    /**
     * Gets SwitchYard domain associated with this context.
     *
     * @return SwitchYard domain.
     */
    ServiceDomain getServiceDomain();

    SimpleRegistry getWritebleRegistry();
}
