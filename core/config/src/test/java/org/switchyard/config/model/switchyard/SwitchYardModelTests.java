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
package org.switchyard.config.model.switchyard;

import java.io.StringReader;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.switchyard.config.model.Model;
import org.switchyard.config.model.ModelResource;
import org.switchyard.config.model.Models;
import org.switchyard.config.model.composite.CompositeModel;
import org.switchyard.config.model.composite.ExternalServiceModel;
import org.switchyard.config.model.composite.test.soap.PortModel;
import org.switchyard.config.model.composite.test.soap.SOAPBindingModel;
import org.switchyard.config.util.StringResource;

/**
 * SwitchYardModelTests.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public class SwitchYardModelTests {

    private static final String INCOMPLETE_XML = "/org/switchyard/config/model/switchyard/SwitchYardModelTests-Incomplete.xml";
    private static final String FRAGMENT_XML = "/org/switchyard/config/model/switchyard/SwitchYardModelTests-Fragment.xml";
    private static final String COMPLETE_XML = "/org/switchyard/config/model/switchyard/SwitchYardModelTests-Complete.xml";

    private ModelResource _res;

    @Before
    public void before() throws Exception {
        _res = new ModelResource();
    }

    @Test
    public void testCreateEmptyModel() throws Exception {
        String namespace = SwitchYardModel.DEFAULT_NAMESPACE;
        String name = SwitchYardModel.SWITCHYARD;
        Model model = Models.create(namespace, name);
        Assert.assertTrue(model instanceof SwitchYardModel);
        Assert.assertEquals(name, model.getModelConfiguration().getName());
        Assert.assertEquals(new QName(namespace, name), model.getModelConfiguration().getQName());
    }

    @Test
    public void testMerge() throws Exception {
        SwitchYardModel incomplete_switchyard = (SwitchYardModel)_res.pull(INCOMPLETE_XML);
        SwitchYardModel fragment_switchyard = (SwitchYardModel)_res.pull(FRAGMENT_XML);
        SwitchYardModel merged_switchyard = (SwitchYardModel)Models.merge(fragment_switchyard, incomplete_switchyard);
        XMLUnit.setIgnoreWhitespace(true);
        SwitchYardModel complete_switchyard = (SwitchYardModel)_res.pull(COMPLETE_XML);
        Diff diff = XMLUnit.compareXML(complete_switchyard.toString(), merged_switchyard.toString());
        Assert.assertTrue(diff.identical());
    }

    @Test
    public void testReadComplete() throws Exception {
        SwitchYardModel switchyard = (SwitchYardModel)_res.pull(COMPLETE_XML);
        CompositeModel composite = switchyard.getComposite();
        ExternalServiceModel service = composite.getServices().get(0);
        SOAPBindingModel binding = (SOAPBindingModel)service.getBindings().get(0);
        PortModel port = binding.getPort();
        Assert.assertEquals("MyWebService/SOAPPort", port.getPort());
        String name = port.getBinding().getService().getComposite().getComponents().get(0).getName();
        Assert.assertEquals("SimpleService", name);
    }

    @Test
    public void testWriteComplete() throws Exception {
        String old_xml = new StringResource().pull(COMPLETE_XML);
        SwitchYardModel switchyard = (SwitchYardModel)_res.pull(new StringReader(old_xml));
        String new_xml = switchyard.toString();
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(old_xml, new_xml);
        Assert.assertTrue(diff.identical());
    }

}
