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
package org.switchyard.adapter;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.switchyard.adapter.config.model.adapters.AnnoV1toV2Adapter;
import org.switchyard.adapter.config.model.adapters.MockServiceContract;
import org.switchyard.adapter.config.model.adapters.NoAnnotationAdapter;
import org.switchyard.adapter.config.model.adapters.V1toV2Adapter;
import org.switchyard.adapter.config.model.v1.V1JavaAdapterModel;
import org.switchyard.config.model.composite.InterfaceModel;
import org.switchyard.config.model.composite.v1.V1InterfaceModel;
import org.switchyard.exception.SwitchYardException;


/**
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class AdapterUtilTest {
	
	private V1JavaAdapterModel model;

	@Before
	public void before() {
		model = new V1JavaAdapterModel();
		InterfaceModel interfaceModel = new V1InterfaceModel(InterfaceModel.JAVA);
		interfaceModel.setInterface(MockServiceContract.class.getName());
		model.setInterfaceModel(interfaceModel);
	}
	
	@Test
	public void testAnnotationBasedAdapter() {
		model.setClazz(AnnoV1toV2Adapter.class.getName());
		
		Adapter adapter = AdapterUtil.newAdapter(model);
		assertNotNull(adapter);
		assertNotNull(adapter.getServiceInterface());
	}
	
	@Test
	public void testImplementationBasedAdapter() {
		model.setClazz(V1toV2Adapter.class.getName());
		
		Adapter adapter = AdapterUtil.newAdapter(model);
		assertNotNull(adapter);
		assertEquals(V1toV2Adapter.class, adapter.getClass());
		assertNotNull(adapter.getServiceInterface());
	}
	
	@Test
	public void testNoAnnotationFoundAdapter() { 
		model.setClazz(NoAnnotationAdapter.class.getName());
		
		try {
			AdapterUtil.newAdapter(model);
			fail();
		} catch (SwitchYardException e) {
			Assert.assertEquals("Invalid Adapter class 'class org.switchyard.adapter.config.model.adapters.NoAnnotationAdapter'.", e.getMessage());
		}
	}
}
