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

import javax.xml.namespace.QName;

import org.switchyard.common.xml.XMLHelper;
import org.switchyard.config.Configuration;
import org.switchyard.config.model.BaseTypedModel;
import org.switchyard.config.model.Descriptor;
import org.switchyard.config.model.adapter.AdaptModel;
import org.switchyard.config.model.adapter.AdaptersModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * A version 1 AdaptModel.
 * 
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public abstract class V1BaseAdaptModel extends BaseTypedModel implements AdaptModel {

    protected V1BaseAdaptModel(String type) {
        this(new QName(SwitchYardModel.DEFAULT_NAMESPACE, AdaptModel.ADAPT + '.' + type));
    }

    protected V1BaseAdaptModel(QName qname) {
        super(qname);
    }

    protected V1BaseAdaptModel(Configuration config, Descriptor desc) {
        super(config, desc);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdaptersModel getAdapters() {
        return (AdaptersModel) getModelParent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QName getFrom() {
        return XMLHelper.createQName(getModelAttribute(AdaptModel.FROM));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdaptModel setFrom(QName from) {
        setModelAttribute(AdaptModel.FROM, from != null ? from.toString() : null);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public QName getTo() {
        return XMLHelper.createQName(getModelAttribute(AdaptModel.TO));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AdaptModel setTo(QName to) {
        setModelAttribute(AdaptModel.TO, to != null ? to.toString() : null);
        return this;
    }
}
