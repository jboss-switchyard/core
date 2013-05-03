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
package org.switchyard.event;

import java.util.EventObject;

import org.switchyard.adapter.Adapter;

/**
 * Fired when an adapter is removed from the domain.
 */
public class AdapterRemovedEvent extends EventObject {

    private static final long serialVersionUID = -5796235997474241451L;

    /**
     * Creates a new AdapterRemovedEvent.
     * @param adapter the adapter that was removed
     */
    public AdapterRemovedEvent(Adapter adapter) {
        super(adapter);
    }
    
    /**
     * Get the removed adapter.
     * @return removed adapter
     */
    public Adapter getTransformer() {
        return (Adapter) getSource();
    }
}
