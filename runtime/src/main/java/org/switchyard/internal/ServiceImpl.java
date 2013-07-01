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

package org.switchyard.internal;

import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.switchyard.ExchangeHandler;
import org.switchyard.Service;
import org.switchyard.ServiceDomain;
import org.switchyard.ServiceSecurity;
import org.switchyard.event.ServiceUnregistrationEvent;
import org.switchyard.metadata.Registrant;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.policy.Policy;

/**
 * A service registered in a SwitchYard domain.  This is an instance of the 
 * registered service itself and not a service reference (which is used to
 * invoke a service).
 */
public class ServiceImpl implements Service {

    private QName _name;
    private ServiceInterface _interface;
    private DomainImpl _domain;
    private ExchangeHandler _providerHandler;
    private List<Policy> _requires;
    private String _securityName;
    private Registrant _providerMetadata;
    
    /**
     * Creates a new Service instance representing a service provider.
     * @param name name of the service reference
     * @param serviceInterface the service interface
     * @param domain domain in which the service is used 
     * @param providerHandler the exchange handler representing the provider
     */
    public ServiceImpl(QName name,
            ServiceInterface serviceInterface,
            DomainImpl domain,
            ExchangeHandler providerHandler) {
        this(name, serviceInterface, domain, providerHandler, null, null, null);
    }

    /**
     * Creates a new Service instance representing a service provider.
     * @param name name of the service reference
     * @param serviceInterface the service interface
     * @param domain domain in which the service is used 
     * @param providerHandler the exchange handler representing the provider
     * @param requires list of policies required for this reference
     * @param securityName the security name
     * @param providerMetadata information related to the provider
     */
    public ServiceImpl(QName name,
            ServiceInterface serviceInterface,
            DomainImpl domain,
            ExchangeHandler providerHandler,
            List<Policy> requires,
            String securityName,
            Registrant providerMetadata) {
        
        _name = name;
        _interface = serviceInterface;
        _domain = domain;
        _providerHandler = providerHandler;
        _providerMetadata = providerMetadata;
        if (requires != null) {
            _requires = requires;
        } else {
            _requires = Collections.emptyList();
        }
        _securityName = securityName;
    }

    @Override
    public ServiceInterface getInterface() {
        return _interface;
    }

    @Override
    public QName getName() {
        return _name;
    }
    
    @Override
    public ServiceDomain getDomain() {
        return _domain;
    }
    
    @Override
    public ServiceSecurity getSecurity() {
        return _domain != null ? _domain.getServiceSecurity(_securityName) : null;
    }
    
    @Override
    public void unregister() {
        _domain.getServiceRegistry().unregisterService(this);
        _domain.getEventPublisher().publish(new ServiceUnregistrationEvent(this));
    }
    
    @Override
    public List<Policy> getRequiredPolicies() {
        return Collections.unmodifiableList(_requires);
    }

    @Override
    public ExchangeHandler getProviderHandler() {
        return _providerHandler;
    }

    @Override
    public Registrant getProviderMetadata() {
        return _providerMetadata;
    }
    
    /**
     * Sets the list of required policies for this service.
     * @param requires list of policies required
     * @return this ServiceImpl instance
     */
    public ServiceImpl setRequires(List<Policy> requires) {
        _requires = requires;
        return this;
    }
    
    /**
     * Specifies the provider metadata associated with this service.
     * @param provider provider metadata
     * @return this ServiceImpl instance
     */
    public ServiceImpl setProviderMetadata(Registrant provider) {
        _providerMetadata = provider;
        return this;
    }

    @Override
    public String toString() {
        return "Service [name=" + _name + ", interface=" + _interface
                + ", domain=" + _domain + ", requires=" + _requires + "]";
    }

}
