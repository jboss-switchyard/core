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

import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

/**
 * Handles changes (operation names) between service interfaces/contracts.
 * Adapters are directly attached to a composite service.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public interface Adapter {
    
    /**
     * Set the service interface to adapt.
     * @param serviceInterface service interface to adapts
     * @return this Adapter (useful for chaining)
     */
    public Adapter setServiceInterface(ServiceInterface serviceInterface);
    
    /**
     * Get the adapted service interface.
     * @return the adapted service interface
     */
    public ServiceInterface getServiceInterface();
    
    /**
     * Lookup for a service operation in the targetInterface.
     * @param consumerOperation The operation's name invoked by the consumer
     * @param targetInterface The interface containing all available service operation
     * @return service operation selected out of the target interface based on the cunsumerOperation.
     */
    public ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface);
}
