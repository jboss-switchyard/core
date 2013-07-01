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

import java.util.EventObject;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.switchyard.event.EventObserver;
import org.switchyard.event.EventPublisher;
import org.switchyard.metadata.Registrant;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.policy.Policy;
import org.switchyard.transform.TransformerRegistry;
import org.switchyard.validate.ValidatorRegistry;

/**
 * A ServiceDomain represents a collection of services with a shared set of
 * resources, configuration, and policy definitions.  The ServiceDomain
 * interface is used by software components to provide and/or consume
 * services.  These software components include protocol gateways, service
 * containers, translation engines, adapters, orchestration and routing
 * engines.
 */
public interface ServiceDomain {

    /**
     * Name of the ServiceDomain. This is unique within a runtime.
     * @return name name of the ServiceDomain.
     */
    QName getName();
    
    /**
     * Register a service with the domain.
     * @param serviceName the name of the service
     * @param handler the handler to use to process exchanges directed at this
     * service
     * @param metadata service interface details
     * @return the registered service
     */
    Service registerService(QName serviceName,
            ServiceInterface metadata,
            ExchangeHandler handler);
    
    /**
     * Register a service with the domain.
     * @param serviceName the name of the service
     * @param handler the handler to use to process exchanges directed at this
     * service
     * @param contract service interface details
     * @param requires policy requirements for the service
     * @param securityName the security name
     * @param owner metadata related to the entity registering the service
     * @return the registered service
     */
    Service registerService(QName serviceName,
            ServiceInterface contract,
            ExchangeHandler handler,
            List<Policy> requires,
            String securityName,
            Registrant owner);
    
    /**
     * Register a service reference with the domain.
     * @param serviceName the name of the reference
     * @param metadata service consumer contract
     * @return a reference to the registered service that can be used to
     * unregister when required
     */
    ServiceReference registerServiceReference(QName serviceName, ServiceInterface metadata);
    
    /**
     * Register a service reference with the domain.
     * @param serviceName the name of the reference
     * @param metadata service consumer contract
     * @param handler the handler to use to process replies from the service
     * @return a reference to the registered service that can be used to
     * unregister when required
     */
    ServiceReference registerServiceReference(QName serviceName,
            ServiceInterface metadata,
            ExchangeHandler handler);
    
    /**
     * Register a service reference with the domain.
     * @param serviceName the name of the reference
     * @param contract service consumer contract
     * @param handler the handler to use to process replies from the service
     * @param provides policies provided by the reference
     * @param requires policies required for the reference
     * @param securityName the security name
     * @param owner metadata related to the entity registering the reference
     * @return a reference to the registered service that can be used to
     * unregister when required
     */
    ServiceReference registerServiceReference(QName serviceName,
            ServiceInterface contract,
            ExchangeHandler handler,
            List<Policy> provides,
            List<Policy> requires,
            String securityName,
            Registrant owner);
    
    /**
     * Fetches a registered service reference for the specified name.
     * @param serviceName name of the service reference
     * @return registered service reference, or null if no references have
     * been registered with the specified name.
     */
    ServiceReference getServiceReference(QName serviceName);
    
    /**
     * Return the list of services.
     * @return services
     */
    List<Service> getServices();
      
    /**
     * Get the list of services for the specified service name.
     * @param serviceName service name
     * @return services
     */
    List<Service> getServices(QName serviceName);
    
    /**
     * Returns a reference to the transformer registry for this domain.
     * @return transformer registry instance
     */
    TransformerRegistry getTransformerRegistry();
    
    /**
     * Returns a reference to the validator registry for this domain.
     * @return validator registry instance
     */
    ValidatorRegistry getValidatorRegistry();
    
    /**
     * Returns the list of domain-level handlers that execute on every 
     * message exchange.
     * @return list of exchange handlers
     */
    List<ExchangeHandler> getHandlers();
    
    /**
     * Add an EventObserver for the specified event type.  A single instance
     * of EventObserver can be registered to receive multiple event types.
     * @param observer the event observer to add
     * @param eventType the event type to receive notifications on
     * @return a reference to this ServiceDomain for chaining multiple addEventObserver() calls
     */
    ServiceDomain addEventObserver(EventObserver observer, Class<? extends EventObject> eventType);
    
    /**
     * Returns the EventPublisher for this domain, which can be used to notify
     * event observers of activity.
     * @return event publisher
     */
    EventPublisher getEventPublisher();

    /**
     * Gets a ServiceSecurity by name.
     * @param name the name
     * @return the ServiceSecurity
     */
    ServiceSecurity getServiceSecurity(String name);

    /**
     * Shared properties/POJOs which are not part of switchyard core-api.
     * 
     * @return Bag with objects.
     */
    Map<String, Object> getProperties();

    /**
     * Cleans up all resources associated with a ServiceDomain instance including
     * the transformer/validator/service registry and exchange bus.  ServiceDomain
     * instances can not be used after they are destroyed.
     */
    void destroy();


}
