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

import java.util.EventObject;

import org.switchyard.event.EventObserver;

/**
 * MBean definition for the EventManager.
 */
public interface EventManagerMBean {

    /**
     * Add an event observer associated with the supplied event type.
     * @param observer observer instance to add
     * @param events the list of events to register against
     * @return a reference to this EventManger for chaining calls
     */
    public void addObserver(EventObserver observer, java.util.List<Class<? extends EventObject>> events);
    
    /**
     * Remove all event registrations for a given EventObserver instance.
     * @param observer the observer to unregister
     */
    public void removeObserver(EventObserver observer);
    
}
