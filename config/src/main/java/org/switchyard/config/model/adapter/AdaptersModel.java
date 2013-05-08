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
package org.switchyard.config.model.adapter;

import java.util.List;

import org.switchyard.config.model.Model;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * Collection model for adapters.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public interface AdaptersModel extends Model {
    
    /** The "adapters" name. */
    public static final String ADAPTERS = "adapters";
    
    /**
     * Gets the parent switchyard model.
     * 
     * @return the parent switchyard model
     */
    public SwitchYardModel getSwitchYard();
    
    /**
     * Gets the child adapt models.
     * 
     * @return the child adapt models
     */
    public List<AdaptModel> getAdapts();
    
    /**
     * Adds a child adapt model.
     * 
     * @param adapt the child adapt model to add
     * @return this AdaptersModel (useful for chaining)
     */
    public AdaptersModel addAdapt(AdaptModel adapt);

}
