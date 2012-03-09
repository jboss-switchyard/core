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

package org.switchyard.config.model.switchyard.v1;

import javax.xml.namespace.QName;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.composite.v1.V1InterfaceModel;
import org.switchyard.config.model.switchyard.EsbInterfaceModel;

/**
 * An "esb" implementation of InterfaceModel.
 */
public class V1EsbInterfaceModel extends V1InterfaceModel implements EsbInterfaceModel {

    /**
     * Default constructor for application use.
     */
    public V1EsbInterfaceModel() {
        super(ESB);
    }

    /**
     * Constructor for Marshaller use.
     *
     * @param config the Configuration
     * @param desc the Descriptor
     */
    public V1EsbInterfaceModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

    @Override
    public QName getInputType() {
        return getQName(getModelAttribute(INPUT_TYPE));
    }

    @Override
    public QName getOutputType() {
        return getQName(getModelAttribute(OUTPUT_TYPE));
    }

    @Override
    public QName getFaultType() {
        return getQName(getModelAttribute(FAULT_TYPE));
    }

    @Override
    public EsbInterfaceModel setInputType(QName input) {
        setModelAttribute(INPUT_TYPE, input.toString());
        return this;
    }

    @Override
    public EsbInterfaceModel setOutputType(QName output) {
        setModelAttribute(OUTPUT_TYPE, output.toString());
        return this;
    }

    @Override
    public EsbInterfaceModel setFaultType(QName fault) {
        setModelAttribute(FAULT_TYPE, fault.toString());
        return this;
    }
    
    private QName getQName(String name) {
        return name == null ? null : QName.valueOf(name);
    }

}
