/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
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
package org.switchyard.deploy;

import org.switchyard.config.model.composite.BindingModel;
import org.switchyard.metadata.Registrant;

/**
 * Represents binding metadata for a service or reference.
 */
public class Binding implements Registrant {
    
    private BindingModel _config;
    
    /**
     * Create a new Binding instance.
     * @param config binding configuration
     */
    public Binding(BindingModel config) {
        _config = config;
    }

    @Override
    public boolean isBinding() {
        return true;
    }

    @Override
    public boolean isImplementation() {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public BindingModel getConfig() {
        return _config;
    }

}
