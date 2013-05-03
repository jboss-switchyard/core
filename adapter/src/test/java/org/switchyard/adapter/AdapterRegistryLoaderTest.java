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
package org.switchyard.adapter;

import java.util.Collection;

import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.switchyard.adapter.config.model.adapters.V1toV2Adapter;
import org.switchyard.adapter.config.model.v1.V1JavaAdaptModel;
import org.switchyard.config.model.adapter.AdaptersModel;
import org.switchyard.config.model.adapter.v1.V1AdaptersModel;
import org.switchyard.exception.DuplicateAdapterException;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

/**
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class AdapterRegistryLoaderTest {
	private AdapterRegistryLoader loader;
	private V1JavaAdaptModel adaptModel;
	private AdapterRegistry registy;

	@Before
	public void setup() {
		registy = Mockito.mock(AdapterRegistry.class);
		loader = new AdapterRegistryLoader(registy);

		adaptModel = new V1JavaAdaptModel();
		adaptModel.setFrom(V1toV2Adapter.FROM);
		adaptModel.setTo(V1toV2Adapter.TO);
		adaptModel.setClazz(V1toV2Adapter.class.getName());
	}

	@Test
	public void testNewAdapters() {
		Collection<Adapter> adapters = loader.newAdapters(adaptModel);
		Assert.assertEquals(1, adapters.size());
	}

	@Test
	public void testRegisterAdapters() {
		AdaptersModel adaptersModel = new V1AdaptersModel();
		adaptersModel.addAdapt(adaptModel);

		loader.registerAdapters(adaptersModel);
		Assert.assertEquals(1, loader.getAdapters().size());
	}

	@Test
	public void testRegisterAdaptersDuplicateFrom() {
		AdaptersModel adaptersModel = new V1AdaptersModel();
		adaptersModel.addAdapt(adaptModel);

		loader.registerAdapters(adaptersModel);

		Mockito.when(registy.hasAdapter(adaptModel.getFrom())).thenReturn(true);
		Mockito.when(registy.getAdapter(Mockito.any(QName.class))).thenReturn(
				new TestAdapter(adaptModel.getFrom(), adaptModel.getTo()));

		try {
			loader.registerAdapters(adaptersModel);
			Assert.fail();
		} catch (DuplicateAdapterException e) {
			Assert.assertEquals(
					"Failed to register Adapter " +
					"'org.switchyard.adapter.AdapterUtil$1({urn:org.switchyard.adapter.config.model}TestServiceV1, {urn:org.switchyard.adapter.config.model}TestServiceV2)'.  " +
					"An Adapter for these services is already registered: " +
					"'org.switchyard.adapter.AdapterRegistryLoaderTest$TestAdapter({urn:org.switchyard.adapter.config.model}TestServiceV1, {urn:org.switchyard.adapter.config.model}TestServiceV2)'.", 
					e.getMessage());
		}
	}

	public static class TestAdapter extends BaseAdapter {
		public TestAdapter(QName from, QName to) {
			super(from, to);
		}

		@Override
		public ServiceOperation lookup(String consumerOperation,
				ServiceInterface targetInterface) {
			return null;
		}
	}
}
