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
package org.switchyard.addressing.resolvers;

import java.util.List;

import javax.xml.namespace.QName;

import org.switchyard.HandlerException;
import org.switchyard.Service;
import org.switchyard.ServiceDomain;
import org.switchyard.ServiceReference;
import org.switchyard.adapter.Adapter;
import org.switchyard.adapter.AdapterRegistry;
import org.switchyard.addressing.ServiceResolver;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

/**
 * Service and operation selection based on user defined adapters.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class AdapterServiceResolver implements ServiceResolver {

    private AdapterRegistry _adapterRegistry;

    /**
     * Public constructor.
     * 
     * @param adapterRegistry The registry instance.
     */
    public AdapterServiceResolver(AdapterRegistry adapterRegistry) {
        _adapterRegistry = adapterRegistry;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceOperation getProviderOp(QName fromServiceName, QName toServiceName, ServiceOperation consumerOperation, ServiceInterface serviceInterface) throws HandlerException {
        Adapter adapter = _adapterRegistry.getAdapter(fromServiceName);        
        if (adapter.getTo().equals(toServiceName)) {
            return adapter.lookup(consumerOperation.getName(), serviceInterface);
        }
        throw new HandlerException(String.format("No adapter available ('%s' -> '%s')", fromServiceName, toServiceName));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Service serviceLookup(ServiceDomain domain, ServiceReference consumerReference) throws HandlerException {
        QName from = consumerReference.getName();
        Adapter adapter = _adapterRegistry.getAdapter(from);
        
        List<Service> services = domain.getServices(adapter.getTo());
        if (services.size() >= 1) {
            return services.get(0);
        }
        throw new HandlerException("Target service not found: " + adapter.getTo());
    }
}
