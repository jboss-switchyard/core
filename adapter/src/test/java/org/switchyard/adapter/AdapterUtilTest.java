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

import java.util.List;

import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Test;
import org.switchyard.metadata.ServiceInterface;
import org.switchyard.metadata.ServiceOperation;

/**
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class AdapterUtilTest {
	private static final QName TEST_SERVICE_V1 = QName.valueOf("{urn:org.switchyard.adapter.config.model}TestServiceV1");
	private static final QName TEST_SERVICE_V2 = QName.valueOf("{urn:org.switchyard.adapter.config.model}TestServiceV2");
	
	@Test
	public void testIsAdapter() {
		Assert.assertTrue(AdapterUtil.isAdapter(TestAdapter.class));
		Assert.assertTrue(AdapterUtil.isAdapter(AnnoTestAdapter.class));
		
		Assert.assertFalse(AdapterUtil.isAdapter(Adapter.class));		
		Assert.assertFalse(AdapterUtil.isAdapter(org.switchyard.annotations.Adapter.class));
		Assert.assertFalse(AdapterUtil.isAdapter(AbstractTestAdapter.class));
		Assert.assertFalse(AdapterUtil.isAdapter(Object.class));
		Assert.assertFalse(AdapterUtil.isAdapter(ConstructerTestAdapter.class));
	}
	
	@Test
	public void testNewAdapter() {
		List<Adapter> adapters = AdapterUtil.newAdapter(TestAdapter.class, TEST_SERVICE_V1, TEST_SERVICE_V2);
		Assert.assertEquals(1, adapters.size());
		Adapter adapter = adapters.get(0);
		Assert.assertEquals(TEST_SERVICE_V1, adapter.getFrom());
		Assert.assertEquals(TEST_SERVICE_V2, adapter.getTo());
		
		adapters = AdapterUtil.newAdapter(AnnoTestAdapter.class, TEST_SERVICE_V1, TEST_SERVICE_V2);
		Assert.assertEquals(1, adapters.size());
		adapter = adapters.get(0);
		Assert.assertEquals(TEST_SERVICE_V1, adapter.getFrom());
		Assert.assertEquals(TEST_SERVICE_V2, adapter.getTo());
	}
	
	public static class TestAdapter extends BaseAdapter {
		public TestAdapter() {
			super(TEST_SERVICE_V1, TEST_SERVICE_V2);
		}

		@Override
		public ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface) {
			return null;
		}
	}

	public static class AnnoTestAdapter {
		
		@org.switchyard.annotations.Adapter(from = "{urn:org.switchyard.adapter.config.model}TestServiceV1", to = "{urn:org.switchyard.adapter.config.model}TestServiceV2")
		public ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface) {
			return null;
		}
	}
	
	public abstract static class AbstractTestAdapter extends BaseAdapter {
		public AbstractTestAdapter() {
			super(null, null);
		}
	}
	
	public static class ConstructerTestAdapter extends BaseAdapter {
		public ConstructerTestAdapter(QName from, QName to) {
			super(from, to);
		}

		@Override
		public ServiceOperation lookup(String consumerOperation, ServiceInterface targetInterface) {
			return null;
		}
	}
}
