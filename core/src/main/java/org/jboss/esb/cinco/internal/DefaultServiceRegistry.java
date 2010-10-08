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

package org.jboss.esb.cinco.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jboss.esb.cinco.HandlerChain;
import org.jboss.esb.cinco.Service;
import org.jboss.esb.cinco.ServiceDomain;
import org.jboss.esb.cinco.spi.ServiceRegistry;

public class DefaultServiceRegistry implements ServiceRegistry {
	
	private Map<QName, List<ServiceRegistration>> _services = 
		new HashMap<QName, List<ServiceRegistration>>();

	

	@Override
	public synchronized List<Service> getServices() {
		LinkedList<Service> serviceList = new LinkedList<Service>();
		for (List<ServiceRegistration> services : _services.values()) {
			serviceList.addAll(services);
		}
		
		return serviceList;
	}

	@Override
    public synchronized List<Service> getServices(QName serviceName) {
        List<ServiceRegistration> services = _services.get(serviceName);
        if (services == null) {
            return Collections.emptyList();
        }
        
        return new LinkedList<Service>(services);
    }
	
	@Override
	public synchronized Service registerService(QName serviceName,
			org.jboss.esb.cinco.spi.Endpoint endpoint, HandlerChain handlers,
			ServiceDomain domain) {
		
		ServiceRegistration sr = new ServiceRegistration(
				serviceName, endpoint, handlers, this, domain);
		
		List<ServiceRegistration> serviceList = _services.get(serviceName);
		if (serviceList == null) {
			serviceList = new LinkedList<ServiceRegistration>();
			 _services.put(serviceName, serviceList);
		}
		
		serviceList.add(sr);
		return sr;
	}

	@Override
	public synchronized void unregisterService(Service service) {
		List<ServiceRegistration> serviceList = _services.get(service);
		if (serviceList != null) {
			serviceList.remove(service);
		}
	}

}
