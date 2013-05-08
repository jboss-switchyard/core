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
package org.switchyard.adapter.config.model;

import java.io.StringReader;

import javax.xml.namespace.QName;

import junit.framework.Assert;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Before;
import org.junit.Test;
import org.switchyard.adapter.config.model.adapters.V1toV2Adapter;
import org.switchyard.adapter.config.model.v1.V1JavaAdaptModel;
import org.switchyard.common.io.pull.StringPuller;
import org.switchyard.common.xml.XMLHelper;
import org.switchyard.config.model.Model;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.adapter.AdaptModel;
import org.switchyard.config.model.adapter.AdaptersModel;
import org.switchyard.config.model.adapter.v1.V1AdaptersModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;
import org.switchyard.config.model.switchyard.v1.V1SwitchYardModel;

/**
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class AdapterModelTests {

	private static final String XML = "/org/switchyard/adapter/config/model/AdapterModelTests.xml";
	
	private ModelPuller<SwitchYardModel> _puller;
	
	@Before
    public void before() throws Exception {
        _puller = new ModelPuller<SwitchYardModel>();
    }
	
	@Test
    public void testCreateEmptyModel() throws Exception {
		String namespace = AdaptModel.DEFAULT_NAMESPACE;
        String name = AdaptModel.ADAPT + '.' + JavaAdaptModel.JAVA;
        Model model = new ModelPuller<Model>().pull(XMLHelper.createQName(namespace, name));
        Assert.assertTrue(model instanceof JavaAdaptModel);
        Assert.assertEquals(name, model.getModelConfiguration().getName());
        Assert.assertEquals(new QName(namespace, name), model.getModelConfiguration().getQName());
    }
	
	@Test
    public void testCreate() throws Exception {
        SwitchYardModel switchyard = new V1SwitchYardModel();
        AdaptersModel adapters = new V1AdaptersModel();
        JavaAdaptModel javaAdapters = new V1JavaAdaptModel();
        javaAdapters.setFrom(new QName("{urn:org.switchyard.adapter.config.model}TestServiceV1"));
        javaAdapters.setTo(new QName("{urn:org.switchyard.adapter.config.model}TestServiceV2"));
        javaAdapters.setClazz("org.switchyard.adapter.config.model.adapters.V1toV2Adapter");  
        adapters.addAdapt(javaAdapters);

        switchyard.setAdapters(adapters);
        String new_xml = switchyard.toString();        
        String old_xml = new ModelPuller<SwitchYardModel>().pull(XML, getClass()).toString();
        
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(old_xml, new_xml);
        Assert.assertTrue(diff.toString(), diff.identical());
    }
	
	@Test
    public void testRead() throws Exception {
        SwitchYardModel switchyard = _puller.pull(XML, getClass());
        AdaptersModel adapters = switchyard.getAdapters();

        JavaAdaptModel java_adapt = (JavaAdaptModel)adapters.getAdapts().get(0);
        Assert.assertEquals("TestServiceV1", java_adapt.getFrom().getLocalPart());
        Assert.assertEquals("TestServiceV2", java_adapt.getTo().getLocalPart());
        Assert.assertEquals(V1toV2Adapter.class.getName(), java_adapt.getClazz());
    }
	 
	@Test
    public void testWrite() throws Exception {
        String old_xml = new StringPuller().pull(XML, getClass());
        SwitchYardModel switchyard = _puller.pull(new StringReader(old_xml));
        String new_xml = switchyard.toString();
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(old_xml, new_xml);
        Assert.assertTrue(diff.toString(), diff.identical());
    }

    @Test
    public void testParenthood() throws Exception {
        SwitchYardModel switchyard_1 = _puller.pull(XML, getClass());
        AdaptersModel transforms_1 = switchyard_1.getAdapters();
        AdaptModel transform = transforms_1.getAdapts().get(0);
        AdaptersModel transforms_2 = transform.getAdapters();
        
        SwitchYardModel switchyard_2 = transforms_2.getSwitchYard();
        Assert.assertEquals(transforms_1, transforms_2);
        Assert.assertEquals(switchyard_1, switchyard_2);
    }

    @Test
    public void testValidation() throws Exception {
        SwitchYardModel switchyard = _puller.pull(XML, getClass());
        switchyard.assertModelValid();
    }
}
