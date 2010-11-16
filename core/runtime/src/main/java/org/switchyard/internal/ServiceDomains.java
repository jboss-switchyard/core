/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.switchyard.internal;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.switchyard.ServiceDomain;
import org.switchyard.spi.EndpointProvider;
import org.switchyard.spi.ServiceRegistry;
import org.switchyard.util.ClassUtil;

public class ServiceDomains {
    public static final String ROOT_DOMAIN = "org.switchyard.domains.root";

    private static ConcurrentHashMap<String, ServiceDomain> _domains = 
        new ConcurrentHashMap<String, ServiceDomain>();
    
    private static ServiceRegistry _registry = null;
    private static EndpointProvider _endpointProvider = null;
        
    public synchronized static void init() {
    	String registryClassName = System.getProperty(ServiceRegistry.REGISTRY_CLASS_NAME,
    			"org.switchyard.internal.DefaultServiceRegistry");
    	String endpointProviderClassName = System.getProperty(EndpointProvider.ENDPOINT_PROVIDER_CLASS_NAME,
    			"org.switchyard.internal.DefaultEndpointProvider");

    	Class registryClass, endpointProviderClass;
    	try {
			registryClass = ClassUtil.forName(registryClassName);
			endpointProviderClass = ClassUtil.forName(endpointProviderClassName);

    		_registry = (ServiceRegistry) registryClass.newInstance();
	    	_endpointProvider = (EndpointProvider) endpointProviderClass.newInstance();
    	} catch (ClassNotFoundException e) {
    		throw new RuntimeException(e);
    	} catch (InstantiationException e) {
    		throw new RuntimeException(e);
    	} catch (IllegalAccessException e) {
    		throw new RuntimeException(e);
    	}    	
    }
    
    public static boolean isInitialized() {
    	return (_registry != null) && (_endpointProvider != null);
    }
    
    public synchronized static ServiceDomain getDomain() {
    	if (!isInitialized()) {
    		init();
    	}
    	
        if (!_domains.containsKey(ROOT_DOMAIN)) {
            createDomain(ROOT_DOMAIN);
        }
        
        return getDomain(ROOT_DOMAIN);
    }
    
    public synchronized static ServiceDomain createDomain(String name) {
    	if (!isInitialized()) {
    		init();
    	}
    	
    	if (_domains.containsKey(name)) {
            throw new RuntimeException("Domain already exists: " + name);
        }
        
        ServiceDomain domain = new DomainImpl(name, _registry, _endpointProvider);
        _domains.put(name, domain);
        return domain;
    }
    
    public static ServiceDomain getDomain(String domainName) {
        return _domains.get(domainName);
    }
    
    public static Set<String> getDomainNames() {
        return _domains.keySet();
    }
}
