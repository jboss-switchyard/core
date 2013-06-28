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

package org.switchyard;

import javax.xml.namespace.QName;

import org.switchyard.metadata.ServiceInterface;

/**
 * A service registered with the SwitchYard runtime.
 */
public interface ServiceReference {
    /**
     * Qualified name of the service.
     * @return service name
     */
    QName getName();
    
    /**
     * The domain in which this service reference is registered.
     * @return service domain which created this service reference
     */
    ServiceDomain getDomain();
    
    /**
    * Interface metadata for the registered service.
    * @return the service interface
    */
    ServiceInterface getInterface();
    
    /**
     * Creates a new Exchange to invoke this service.  Since this method does
     * not accept an operation name, it should only be used when the service
     * interface contains a single operation.
     * @return a new Exchange instance
     */
    Exchange createExchange();
    
    /**
     * Creates a new Exchange to invoke this service, with replies handled by 
     * the specified ExchangeHandler.  Since this method does
     * not accept an operation name, it should only be used when the service
     * interface contains a single operation.
     * @param handler used to process reply messages
     * @return a new Exchange instance
     */
    Exchange createExchange(ExchangeHandler handler);
    
    /**
     * Creates a new Exchange to invoke this service with the specified exchange
     * pattern.
     * @param operation the operation to invoke
     * @return a new Exchange instance
     */
    Exchange createExchange(String operation);
    
    /**
     * Creates a new Exchange to invoke this service with the specified exchange
     * pattern.  The supplied ExchangeHandler is used to handle any faults or
     * reply messages that are generated as part of the message exchange.
     * @param operation the operation to invoke
     * @param handler used to process response and fault messages
     * @return a new Exchange instance
     */
    Exchange createExchange(String operation, ExchangeHandler handler);
    
     /**
      * Unregisters this service reference from the domain it's registered in.
      */
     void unregister();

     /**
     * Wire this service reference to a registered service.  The default wiring
     * of a reference maps it to a service with the same name.  This method
     * can be used to map references to services with a different name.
     * @param serviceName service name to wire
      */
     void wire(QName serviceName);
     
     /**
      * Returns the name of a service which is wired from this reference.
      * @return wired service name
      */
     QName getTargetServiceName();
     
     /**
      * Return runtime metadata associated with this service reference.
      * @return runtime service reference metadata
      */
     ServiceMetadata getServiceMetadata();
     
}
