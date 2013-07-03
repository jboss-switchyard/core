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
package org.switchyard.transform.dozer.internal;

import org.dozer.DozerBeanMapper;
import org.switchyard.common.xml.QNameUtil;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.transform.Transformer;
import org.switchyard.transform.internal.TransformerFactory;
import org.switchyard.transform.config.model.DozerTransformModel;

import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;

/**
 * Dozer Transformer factory.
 *
 * @author <a href="mailto:uros.krivec@parsek.net">uros.krivec@parsek.net</a>
 */
public class DozerTransformFactory implements TransformerFactory<DozerTransformModel> {

    /**
     * Create a {@link org.switchyard.transform.Transformer} instance from the supplied {@link org.switchyard.transform.config.model.DozerTransformModel}.
     * @param model The model.
     * @return The Transformer instance.
     */
    public Transformer newTransformer(DozerTransformModel model) {
        String config = model.getConfig();
        QName from = model.getFrom();
        QName to = model.getTo();

        if (config == null || config.trim().length() == 0) {
            throw new SwitchYardException("Invalid Dozer configuration model.  Null or empty 'config' specification.");
        }
        if (from == null) {
            throw new SwitchYardException("Invalid Dozer configuration model.  Null or empty 'from' specification.");
        }
        if (to == null) {
            throw new SwitchYardException("Invalid Dozer configuration model.  Null or empty 'to' specification.");
        }

        assertValidDozerTransformSpec(from, to);

        DozerBeanMapper mapper;
        try {
            List<String> mappings = new ArrayList<String>();
            mappings.add(config);
            mapper = new DozerBeanMapper();
            mapper.setMappingFiles(mappings);
        } catch (Exception e) {
            throw new SwitchYardException("Failed to create Dozer instance for config '" + config + "'.", e);
        }

        Transformer transformer = new DozerTransformer(from, to, mapper);

        transformer.setFrom(model.getFrom());
        transformer.setTo(model.getTo());

        return transformer;
    }

    private static void assertValidDozerTransformSpec(QName from, QName to) {
        if(!(QNameUtil.isJavaMessageType(from) && QNameUtil.isJavaMessageType(to))) {
            throw new SwitchYardException("Invalid Dozer Transformer configuration. Both of the specified 'to' and 'from' transform types must be a Java type.");
        }
    }

}
