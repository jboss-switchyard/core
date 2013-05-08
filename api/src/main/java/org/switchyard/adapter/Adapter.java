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
package org.switchyard.adapter;

import javax.xml.namespace.QName;

import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

/**
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public interface Adapter {
    
    /**
     * Set the name of the from, or source, message type.
     * 
     * @param fromType From type.
     * @return a reference to the current Adapter.
     */
    Adapter setFrom(QName fromType);
    
    /**
     * Set the name of the to, or source, message type.
     * 
     * @param toType To type.
     * @return a reference to the current Adapter.
     */
    Adapter setTo(QName toType);
    
    /**
     * The name of the from, or source, message.
     * @return from message
     */
    QName getFrom();

    /**
     * The name of the to, or target, message.
     * @return to message
     */
    QName getTo();
    
    /**
     * Adapter operation.
     * 
     * @param consumerOperation The invoked consumers service operation.
     * @param targetInterface The re-targeted service interface. 
     * @return The {@link ServiceOperation} to invoke. 
     */
    ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface);
}
