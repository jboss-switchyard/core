/*
 * 2012 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.switchyard;

import java.util.List;

import javax.xml.namespace.QName;

import org.switchyard.metadata.Registrant;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.policy.Policy;

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
     * Gets the security.
     * @return the security
     */
    ServiceSecurity getSecurity();
    
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
     * Returns a list of required policies for this service reference.
     * @return list of required policy
     */
     List<Policy> getRequiredPolicies();

     /**
     * Returns a list of policies provided by this service reference.
     * @return list of provided policy
     */
     List<Policy> getProvidedPolicies();
     
     /**
      * Unregisters this service reference from the domain it's registered in.
      */
     void unregister();
     
     /**
      * Return the consumer metadata associated with this service.
      * @return consumer metadata
      */
     Registrant getConsumerMetadata();

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
}
