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
package org.switchyard.common.camel.event;

import java.util.EventObject;

import org.switchyard.common.camel.SwitchYardCamelContext;

/**
 * Event fired when switchyard camel context is created.
 */
public class CamelContextCreatedEvent extends EventObject {

    private static final long serialVersionUID = -7554266335427219483L;

    /**
     * Creates new event.
     * 
     * @param source Related camel context.
     */
    public CamelContextCreatedEvent(SwitchYardCamelContext source) {
        super(source);
    }

    /**
     * Gets camel context associated with event.
     * 
     * @return Camel context.
     */
    public SwitchYardCamelContext getContext() {
        return (SwitchYardCamelContext) getSource();
    }

}
