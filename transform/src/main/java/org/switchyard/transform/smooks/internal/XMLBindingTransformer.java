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

package org.switchyard.transform.smooks.internal;

import org.apache.log4j.Logger;
import org.milyn.Smooks;
import org.milyn.container.plugin.SourceFactory;
import org.milyn.javabean.binding.model.Bean;
import org.milyn.javabean.binding.model.ModelSet;
import org.milyn.javabean.binding.xml.XMLBinding;
import org.switchyard.SwitchYardException;
import org.switchyard.config.model.Scannable;
import org.switchyard.transform.BaseTransformer;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;

import java.io.IOException;

/**
 * Smooks XMLBinding {@link org.switchyard.transform.Transformer}.
 *
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
@Scannable(false)
public class XMLBindingTransformer extends BaseTransformer {

    private static Logger _log = Logger.getLogger(XMLBindingTransformer.class);

    private XMLBinding _xmlBinding;
    private Bean _bean;
    private BindingDirection _direction;

    /**
     * Binding direction.
     */
    public static enum BindingDirection {
        /** The xml to java binding direction. */
        XML2JAVA,
        /** The java to xml binding directionx. */
        JAVA2XML
    }

    /**
     * Constructor.
     * @param from From type.
     * @param to To type.
     * @param smooks Smooks instance.
     * @param beanModel Bean model.
     * @param direction Binding direction.
     */
    protected XMLBindingTransformer(final QName from, final QName to, Smooks smooks, ModelSet beanModel, BindingDirection direction) {
        super(from, to);
        _xmlBinding = new XMLBinding(smooks);
        _xmlBinding.setOmitXMLDeclaration(true); // XML decl causes problems for StAX code used by SOAP handler
        _xmlBinding.intiailize();
        _bean = beanModel.getModels().values().iterator().next();
        _direction = direction;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object transform(Object from) {
        if (from == null) {
            _log.debug("Null from payload passed to XMLBindingTransformer.  Returning null.");
            return null;
        }

        if (_direction == BindingDirection.XML2JAVA) {
            Source source = SourceFactory.getInstance().createSource(from);
            try {
                return _xmlBinding.fromXML(source, _bean.getBeanClass());
            } catch (IOException e) {
                throw new SwitchYardException("Exception while transforming from XML to '" + _bean.getBeanClass().getName() + "'.", e);
            }
        } else {
            if (!_bean.getBeanClass().isInstance(from)) {
                throw new SwitchYardException("Cannot transform to XML.  Input type is '" + from.getClass().getName() + "' but should be '" + _bean.getBeanClass().getName() + "'.");
            }

            return _xmlBinding.toXML(from);
        }
    }
}
