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

import javax.xml.namespace.QName;

import org.switchyard.HandlerException;
import org.switchyard.addressing.ServiceResolver;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

/**
 * Default implementation for the service resolver interface.
 * 
 * @see ServiceResolver
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class DefaultServiceResolver extends BaseServiceResolver implements ServiceResolver {

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceOperation getProviderOp(QName fromServiceName, QName toServiceName, ServiceOperation consumerOperation, ServiceInterface serviceInterface) throws HandlerException {
        ServiceOperation providerOp = serviceInterface.getOperation(consumerOperation.getName());
        
        if (providerOp == null) {
            // try for a default operation
            if (serviceInterface.getOperations().size() == 1) {
                providerOp = serviceInterface.getOperations().iterator().next();
            } else {
                throw new HandlerException("Operation " + consumerOperation.getName() 
                    + " is not included in interface for service: " + toServiceName);
            }
        }
        return providerOp;
    }

}
