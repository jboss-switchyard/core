/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
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

package org.switchyard.config.model.composite.v1;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.composite.JavaComponentReferenceInterfaceModel;

/**
 * A "java" implementation of the ComponentReferenceInterfaceModel.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public class V1JavaComponentReferenceInterfaceModel extends V1ComponentReferenceInterfaceModel implements JavaComponentReferenceInterfaceModel {

    /**
     * Default constructor for application use.
     */
    public V1JavaComponentReferenceInterfaceModel() {
        super(JAVA);
    }

    /**
     * Constructor for Marshaller use (ie: V1BeanMarshaller).
     *
     * @param config the Configuration
     * @param desc the Descriptor
     */
    public V1JavaComponentReferenceInterfaceModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

}
