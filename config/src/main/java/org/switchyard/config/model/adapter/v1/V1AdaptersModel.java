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
package org.switchyard.config.model.adapter.v1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.adapter.AdaptModel;
import org.switchyard.config.model.adapter.AdaptersModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * A version 1 AdaptersModel.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class V1AdaptersModel extends BaseModel implements AdaptersModel {
    
    private List<AdaptModel> _adapts = new ArrayList<AdaptModel>();
    
    /**
     * Constructs a new V1AdaptersModel.
     */
    public V1AdaptersModel() {
        super(new QName(SwitchYardModel.DEFAULT_NAMESPACE, AdaptersModel.ADAPTERS));
        setModelChildrenOrder(AdaptModel.ADAPT);
    }

    /**
     * Constructs a new V1AdaptersModel with the specified Configuration and Descriptor.
     * 
     * @param config the Configuration
     * @param desc the Descriptor
     */
    public V1AdaptersModel(Configuration config, Descriptor desc) {
        super(config, desc);
        for (Configuration adapt_config : config.getChildrenStartsWith(AdaptModel.ADAPT)) {
            AdaptModel adapt = (AdaptModel)readModel(adapt_config);
            if (adapt != null) {
                _adapts.add(adapt);
            }
        }
        setModelChildrenOrder(AdaptModel.ADAPT);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SwitchYardModel getSwitchYard() {
        return (SwitchYardModel)getModelParent();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<AdaptModel> getAdapts() {
        return Collections.unmodifiableList(_adapts);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized AdaptersModel addAdapt(AdaptModel adapt) {
        addChildModel(adapt);
        _adapts.add(adapt);
        return this;
    }

}
