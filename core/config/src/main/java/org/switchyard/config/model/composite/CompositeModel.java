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

import java.util.List;

import org.switchyard.config.model.NamedModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * CompositeModel.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public interface CompositeModel extends NamedModel {

    public static final String DEFAULT_NAMESPACE = "http://docs.oasis-open.org/ns/opencsa/sca/200912";
    public static final String COMPOSITE = "composite";

    public SwitchYardModel getSwitchYard();

    public List<ExternalServiceModel> getServices();

    public CompositeModel addService(ExternalServiceModel service);

    public List<ComponentModel> getComponents();

    public CompositeModel addComponent(ComponentModel component);

}
