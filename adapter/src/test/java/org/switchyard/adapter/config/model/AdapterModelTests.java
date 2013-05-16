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
import java.util.List;

import javax.xml.namespace.QName;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.switchyard.adapter.config.model.adapters.V1toV2Adapter;
import org.switchyard.adapter.config.model.v1.V1JavaAdapterModel;
import org.switchyard.common.io.pull.StringPuller;
import org.switchyard.common.xml.XMLHelper;
import org.switchyard.config.model.Model;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.composite.CompositeServiceModel;
import org.switchyard.config.model.composite.InterfaceModel;
import org.switchyard.config.model.composite.v1.V1InterfaceModel;
import org.switchyard.config.model.extensions.ExtensionsModel;
import org.switchyard.config.model.extensions.adapter.AdapterModel;
import org.switchyard.config.model.extensions.v1.V1ExtensionsModel;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class AdapterModelTests {

	private static final String XML = "/org/switchyard/adapter/config/model/AdapterModelTests.xml";
	private static final String CREATE_XML = "/org/switchyard/adapter/config/model/CreateAdapterModelTests.xml";
	
	private ModelPuller<SwitchYardModel> _puller;
	
	@Before
    public void before() throws Exception {
        _puller = new ModelPuller<SwitchYardModel>();
    }
	
	@Test
    public void testCreateInvalidEmptyModel() throws Exception {
		String namespace = AdapterModel.DEFAULT_NAMESPACE;
        String name = AdapterModel.ADAPTER + '.' + JavaAdapterModel.JAVA;
        Model model = new ModelPuller<Model>().pull(XMLHelper.createQName(namespace, name));
        Assert.assertTrue(model instanceof JavaAdapterModel);
        Assert.assertEquals(name, model.getModelConfiguration().getName());
        Assert.assertEquals(new QName(namespace, name), model.getModelConfiguration().getQName());
        Assert.assertFalse(model.isModelValid());
    }
	
//	@Test
//    public void testCreate() throws Exception {
//		SwitchYardModel switchyard = _puller.pull(CREATE_XML, getClass());
//		Assert.assertTrue(switchyard.isModelValid());
//		
//		InterfaceModel interfaceModel = new V1InterfaceModel(InterfaceModel.JAVA);
//		interfaceModel.setInterface("org.switchyard.adapter.config.model.ServiceContractV1");
//		JavaAdapterModel javaAdapter = new V1JavaAdapterModel();
//		javaAdapter.setInterfaceModel(interfaceModel);
//		javaAdapter.setClazz(V1toV2Adapter.class.getName());
//		ExtensionsModel extensionsModel = new V1ExtensionsModel();
//        extensionsModel.setAdapterModel(javaAdapter);
//        switchyard.getComposite().getServices().get(0).setExtensions(extensionsModel);
//
//        String new_xml = switchyard.toString();
//        String old_xml = new ModelPuller<SwitchYardModel>().pull(XML, getClass()).toString();
//       	XMLUnit.setIgnoreWhitespace(true);
//       	Diff diff = XMLUnit.compareXML(old_xml, new_xml);
//       	Assert.assertTrue(diff.toString(), diff.identical());
//    }
	
	@Test
    public void testRead() throws Exception {
        SwitchYardModel switchyard = _puller.pull(XML, getClass());
        List<CompositeServiceModel> serviceModels = switchyard.getComposite().getServices();
        Assert.assertEquals(1, serviceModels.size());
        
        CompositeServiceModel compositeServiceModel = serviceModels.get(0);
        ExtensionsModel extensionsModel = compositeServiceModel.getExtensionsModel();
        Assert.assertNotNull(extensionsModel);
        AdapterModel adapterModel = extensionsModel.getAdapterModel();
        Assert.assertNotNull(adapterModel);
        Assert.assertTrue(adapterModel instanceof JavaAdapterModel);
        InterfaceModel interfaceModel = adapterModel.getInterfaceModel();
        Assert.assertEquals(InterfaceModel.JAVA, interfaceModel.getType());
        Assert.assertEquals("org.switchyard.adapter.config.model.ServiceContractV1", interfaceModel.getInterface());
        JavaAdapterModel javaAdapterModel = (JavaAdapterModel) adapterModel;
        Assert.assertEquals(V1toV2Adapter.class.getName(), javaAdapterModel.getClazz());
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
    public void testValidation() throws Exception {
    	SwitchYardModel switchyard = _puller.pull(XML, getClass());
        switchyard.assertModelValid();
    }
}
