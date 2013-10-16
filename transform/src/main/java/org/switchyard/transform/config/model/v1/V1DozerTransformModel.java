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
package org.switchyard.transform.config.model.v1;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.transform.TransformModel;
import org.switchyard.config.model.transform.v1.V1BaseTransformModel;
import org.switchyard.transform.config.model.DozerTransformModel;
import org.switchyard.transform.dozer.internal.DozerTransformFactory;
import org.switchyard.transform.internal.TransformerFactoryClass;

import javax.xml.namespace.QName;

/**
 * A version 1 DozerTransformModel.
 *
 * @author <a href="mailto:uros.krivec@parsek.net">uros.krivec@parsek.net</a>
 */
@TransformerFactoryClass(DozerTransformFactory.class)
public class V1DozerTransformModel extends V1BaseTransformModel implements DozerTransformModel {

    /**
     * Constructs a new V1DozerTransformModel.
     */
    public V1DozerTransformModel() {
        super(new QName(TransformModel.DEFAULT_NAMESPACE, TransformModel.TRANSFORM + '.' + DOZER));
    }

    /**
     * Constructs a new V1DozerTransformModel with the specified Configuration and Descriptor.
     * @param config the Configuration
     * @param desc the Descriptor
     */
    public V1DozerTransformModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getConfig() {
        return getModelAttribute(CONFIG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V1DozerTransformModel setConfig(String config) {
        setModelAttribute(CONFIG, config);
        return this;
    }

}
