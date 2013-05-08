/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
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
package org.switchyard.handlers;

import java.util.List;

import javax.xml.namespace.QName;

import org.switchyard.BaseHandler;
import org.switchyard.Exchange;
import org.switchyard.ExchangePhase;
import org.switchyard.HandlerException;
import org.switchyard.Service;
import org.switchyard.ServiceDomain;
import org.switchyard.ServiceReference;
import org.switchyard.adapter.AdapterRegistry;
import org.switchyard.addressing.ServiceResolver;
import org.switchyard.addressing.resolvers.AdapterServiceResolver;
import org.switchyard.addressing.resolvers.DefaultServiceResolver;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.metadata.ExchangeContract;
import org.switchyard.metadata.ServiceOperation;
import org.switchyard.policy.Policy;
import org.switchyard.policy.PolicyUtil;

/**
 * The AddressingHandler resolves service instances based on a service reference.
 */
public class AddressingHandler extends BaseHandler {
    
    private ServiceDomain _domain;
    private AdapterRegistry _adapterRegistry;
    private DefaultServiceResolver _defaultResolver;
    private AdapterServiceResolver _adapterResolver;
    
    /**
     * Create a new AddressingHandler for the specified domain.
     * @param domain services available for routing
     */
    public AddressingHandler(ServiceDomain domain) {
        _domain = domain;
        _adapterRegistry = domain.getAdapterRegistry();
        _defaultResolver = new DefaultServiceResolver();
        _adapterResolver = new AdapterServiceResolver(domain.getAdapterRegistry());
    }

    @Override
    public void handleMessage(Exchange exchange) throws HandlerException {
        // only set the provider on the 'IN' phase
        if (ExchangePhase.IN != exchange.getPhase()) {
            return;
        }
        
        // is a provider already set?
        if (exchange.getProvider() != null) {
            return;
        }
        
        List<Service> services = _domain.getServices(exchange.getConsumer().getTargetServiceName());
        if (services == null || services.isEmpty()) {
            throw new SwitchYardException("No registered service found for " + exchange.getConsumer().getName());
        }

        ServiceReference consumerService = exchange.getConsumer();
        ExchangeContract exchangeContract = exchange.getContract();
        ServiceResolver resolver = selectServiceResolver(consumerService.getName());
        Service service = resolver.serviceLookup(_domain, consumerService);
        ServiceOperation providerOp = resolver.getProviderOp(consumerService.getName(), service.getName(), exchangeContract.getConsumerOperation(), service.getInterface());
        
        // set provider contract and details on exchange
        exchange.provider(service, providerOp);
        for (Policy policy : service.getRequiredPolicies()) {
            PolicyUtil.require(exchange, policy);
        }
    }

    private ServiceResolver selectServiceResolver(QName name) {
        if (_adapterRegistry.hasAdapter(name)) {
            return _adapterResolver;
        }
        return _defaultResolver;
    }

}
