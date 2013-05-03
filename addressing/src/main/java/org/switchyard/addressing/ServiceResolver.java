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
package org.switchyard.addressing;

import javax.xml.namespace.QName;

import org.switchyard.HandlerException;
import org.switchyard.Service;
import org.switchyard.ServiceDomain;
import org.switchyard.ServiceReference;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

/**
 * Resolves the provider service and operation.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public interface ServiceResolver {

    /**
     * Resolves the service provider.
     * 
     * @param domain Service Domain
     * @param consumerReference Consumer's service reference.
     * @return Provider's service reference.
     * @throws HandlerException If no unique service is found. 
     */
    Service serviceLookup(ServiceDomain domain, ServiceReference consumerReference) throws HandlerException;

    /**
     * Resolve the provider's service operation.
     * 
     * @param fromServiceName The service's name requested by the consumer.
     * @param toServiceName The service's name resolved by <i>getProviderService</i>.
     * @param consumerOperation The consumer's service operation.
     * @param serviceInterface The consumer's service interface.
     * @return The provider's service interface.
     * @throws HandlerException If no service operation could be resolved.
     */
    ServiceOperation getProviderOp(QName fromServiceName, QName toServiceName, ServiceOperation consumerOperation, ServiceInterface serviceInterface) throws HandlerException;
}
