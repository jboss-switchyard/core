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
package org.switchyard.adapter.config.model.v1;

import javax.xml.namespace.QName;

import org.switchyard.adapter.config.model.JavaAdaptModel;
import org.switchyard.config.Configuration;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.adapter.AdaptModel;
import org.switchyard.config.model.adapter.v1.V1BaseAdaptModel;

/**
 * A version 1 JavaAdaptModel.
 *
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class V1JavaAdaptModel extends V1BaseAdaptModel implements JavaAdaptModel {

    /**
     * Constructs a new V1JavaAdaptModel.
     */
    public V1JavaAdaptModel() {
        super(new QName(AdaptModel.DEFAULT_NAMESPACE, AdaptModel.ADAPT + '.' + JAVA));
    }

    /**
     * Constructs a new V1JavaAdaptModel with the specified Configuration and Descriptor.
     * 
     * @param config the Configuration
     * @param desc the Descriptor
     */
    public V1JavaAdaptModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClazz() {
        return getModelAttribute(CLASS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JavaAdaptModel setClazz(String clazz) {
        setModelAttribute(CLASS, clazz);
        return this;
    }
}
