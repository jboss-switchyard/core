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
package org.switchyard.internal.adapter;

import java.util.EventObject;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.namespace.QName;

import org.switchyard.adapter.Adapter;
import org.switchyard.adapter.AdapterRegistry;
import org.switchyard.event.AdapterAddedEvent;
import org.switchyard.event.AdapterRemovedEvent;
import org.switchyard.event.EventPublisher;

/**
 * Adapter registry implementation.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class BaseAdapterRegistry implements AdapterRegistry {
    private EventPublisher _eventPublisher;
    
    private final ConcurrentHashMap<QName, Adapter> _adapter = new ConcurrentHashMap<QName, Adapter>();
    
    /**
     * Constructor.
     */
    public BaseAdapterRegistry() {
    }

    /**
     * Create a new BaseAdapterRegistry instance and add the list of adapters
     * to the registry.
     * 
     * @param adapters set of adapters to add to registry
     */
    public BaseAdapterRegistry(Set<Adapter> adapters) {
        for (Adapter a : adapters) {
            addAdapter(a);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdapterRegistry addAdapter(Adapter adapter) {
        return addAdapter(adapter, adapter.getFrom(), adapter.getTo());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdapterRegistry addAdapter(Adapter adapter, QName from, QName to) {
        _adapter.put(from, adapter);
        publishEvent(new AdapterAddedEvent(adapter));
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAdapter(Adapter adapter) {
        boolean removed = _adapter.remove(adapter.getFrom()) != null;
        if (removed) {
            publishEvent(new AdapterRemovedEvent(adapter));
        }
        
        return removed;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasAdapter(QName from) {
        return _adapter.containsKey(from);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Adapter getAdapter(QName from) {
        return _adapter.get(from);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setEventPublisher(EventPublisher eventPublisher) {
        this._eventPublisher = eventPublisher;
    }
    
    private void publishEvent(EventObject event) {
        if (_eventPublisher != null) {
            _eventPublisher.publish(event);
        }
    }
}
