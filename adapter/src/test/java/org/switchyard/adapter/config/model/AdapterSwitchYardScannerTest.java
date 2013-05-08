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

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.switchyard.adapter.config.model.adapters.AnnoV1toV2Adapter;
import org.switchyard.config.model.ScannerInput;
import org.switchyard.config.model.switchyard.SwitchYardModel;
import org.switchyard.exception.DuplicateAdapterException;

/**
 * @author Christoph Gostner &lt;<a href="mailto:christoph.gostner@objectbay.com">christoph.gostner@objectbay.com</a>&gt; &copy; 2013 Objectbay
 */
public class AdapterSwitchYardScannerTest {

	private AdapterSwitchYardScanner scanner;
	private ArrayList<URL> testUrls;

	@Before
	public void setUp() throws MalformedURLException {
		this.scanner = new AdapterSwitchYardScanner();

		testUrls = new ArrayList<URL>();
        // If running this test inside your IDE... you need to set the cwd to be the
        // root of the transform module !!
        testUrls.add(new File("./target/test-classes").toURI().toURL());
	}
	
	@Test
	public void testAnnotatedAdapter() {
		List<?> adapterTypes = scanner.adapterTypesFromAdapterAnnotation(AnnoV1toV2Adapter.class);
		
		Assert.assertEquals(2, adapterTypes.size());
	}
	
	@Test
	public void testScanDuplicateAdapters() throws IOException {
		ScannerInput<SwitchYardModel> input = new ScannerInput<SwitchYardModel>().setURLs(testUrls);
		try {
			scanner.scan(input).getModel();
        	Assert.fail();
		} catch (DuplicateAdapterException e) {
			Assert.assertEquals("An Adapter for the service is already registered: '{urn:org.switchyard.adapter.config.model}TestServiceV1'.", e.getMessage());
		}
	}
}
