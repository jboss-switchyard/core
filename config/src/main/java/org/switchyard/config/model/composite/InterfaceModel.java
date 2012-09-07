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
package org.switchyard.config.model.composite;

import org.switchyard.config.model.TypedModel;

/**
 * An "interface" configuration model.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public interface InterfaceModel extends TypedModel {

    /** The "interface" name. */
    public static final String INTERFACE = "interface";

    /**
     * Name of standard SCA Java interface.
     */
    public static final String JAVA = "java";
    /**
     * Name of standard SCA WSDL interface.
     */
    public static final String WSDL = "wsdl";


    /**
     * Gets the name of the interface.
     * @return the name of the interface
     */
    public String getInterface();

    /**
     * Sets the name of the interface.
     * @param interfaze the name of the interface
     * @return this InterfaceModel (useful for chaining)
     */
    public InterfaceModel setInterface(String interfaze);

}
