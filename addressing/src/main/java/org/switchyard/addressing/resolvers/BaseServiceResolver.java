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

import org.switchyard.Service;
import org.switchyard.ServiceDomain;
import org.switchyard.ServiceReference;
import org.switchyard.addressing.ServiceResolver;
import org.switchyard.exception.SwitchYardException;

/**
 * Basic service resolver implementation.
 * Provides the implementation for <i>getProviderService</i>
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public abstract class BaseServiceResolver implements ServiceResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public Service serviceLookup(ServiceDomain domain, ServiceReference consumerReference) {
        List<Service> services = domain.getServices(consumerReference.getTargetServiceName());
        if (services == null || services.isEmpty()) {
            throw new SwitchYardException("No registered service found for " + consumerReference.getName());
        }
        // At this stage, just pick the first service implementation we find and go with
        // it.  In the future, it would be nice if we could make this pluggable.
        return services.get(0);
    }
}
