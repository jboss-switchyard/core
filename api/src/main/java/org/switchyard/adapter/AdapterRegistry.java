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
package org.switchyard.adapter;

import javax.xml.namespace.QName;

/**
 * Adapter registry.
 * An adapter is identified by the consumer's service name (from).
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public interface AdapterRegistry {
    
    /**
     * Add an adapter.
     * 
     * @param adapter adapter
     * @return {@code this} AdapterRegistry instance.
     */
    AdapterRegistry addAdapter(Adapter adapter);
    
    /**
     * Add an adapter.
     * 
     * @param adapter adapter
     * @param from from
     * @param to to
     * @return {@code this} AdapterRegistry instance.
     */
    AdapterRegistry addAdapter(Adapter adapter, QName from, QName to);

    /**
     * Remove an adapter.
     * 
     * @param adapter adapter
     * @return status of removal
     */
    boolean removeAdapter(Adapter adapter);

    /**
     * Does the registry have an adapter for the specified type.
     * 
     * @param from from
     * @return True if it has an adapter, otherwise false.
     */
    boolean hasAdapter(QName from);
    
    /**
     * Get an adapter.
     * 
     * @param from from
     * @return adapter
     */
    Adapter getAdapter(QName from);
}
