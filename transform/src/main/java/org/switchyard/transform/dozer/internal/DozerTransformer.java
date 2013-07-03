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

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.dozer.metadata.ClassMappingMetadata;
import org.dozer.metadata.MappingMetadata;
import org.switchyard.common.xml.QNameUtil;
import org.switchyard.config.model.Scannable;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.transform.BaseTransformer;

import javax.xml.namespace.QName;
import java.util.List;

/**
 * Dozer {@link org.switchyard.transform.Transformer}.
 * @author <a href="mailto:uros.krivec@parsek.net">uros.krivec@parsek.net</a>
 */
@Scannable(false)
public class DozerTransformer extends BaseTransformer {

    private static Logger _log = Logger.getLogger(DozerTransformer.class);

    private DozerBeanMapper _mapper;

    /**
     * Constructor.
     * @param from From type.
     * @param to To type.
     * @param mapper Dozer instance.
     */
    protected DozerTransformer(final QName from, final QName to, DozerBeanMapper mapper) {
        super(from, to);
        _mapper = mapper;
        init();
    }

    @Override
    public Class getToType() {
        Class clazz = QNameUtil.toJavaMessageType(getTo());
        if (clazz == null) {
            throw new SwitchYardException("Not able to find class definition " + getTo());
        }
        return clazz;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object transform(Object from) {
        if (from == null) {
            _log.debug("Null from payload passed to DozerTransformer.  Returning null.");
            return null;
        }
        return _mapper.map(from, getToType());
    }

    private void init() {
        MappingMetadata metadata = _mapper.getMappingMetadata();

        Class fromClass = QNameUtil.toJavaMessageType(getFrom());
        Class toClass = QNameUtil.toJavaMessageType(getTo());

        List<ClassMappingMetadata> fromMappings = metadata.getClassMappingsBySource(fromClass);
        List<ClassMappingMetadata> toMappings = metadata.getClassMappingsBySource(toClass);

        if(fromMappings.isEmpty() || toMappings.isEmpty()) {
            throw new SwitchYardException("Invalid Dozer configuration file. No mappings defined for "+fromClass+" or "+toClass+". See Dozer User Guide.");
        }
    }

}
