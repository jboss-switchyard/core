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

package org.switchyard.metadata;

/**
 * Base exchange contract.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class BaseExchangeContract implements ExchangeContract {

    private ServiceOperation _operation;
    private BaseInvocationContract _invokerInvocationMetadata = new BaseInvocationContract();

    /**
     * Public constructor.
     * @param operation The target service operation.
     */
    public BaseExchangeContract(ServiceOperation operation) {
        if (operation == null) {
            throw new IllegalArgumentException("null 'operation' arg.");
        }
        this._operation = operation;
    }

    @Override
    public BaseInvocationContract getInvokerInvocationMetaData() {
        return _invokerInvocationMetadata;
    }

    @Override
    public ServiceOperation getServiceOperation() {
        return _operation;
    }
}
