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
package org.switchyard.transform.internal.xslt;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.switchyard.common.type.Classes;
import org.switchyard.config.model.ModelResource;
import org.switchyard.config.model.switchyard.SwitchYardModel;
import org.switchyard.config.model.transform.TransformsModel;
import org.switchyard.transform.Transformer;
import org.switchyard.transform.config.model.XsltTransformModel;
import org.switchyard.transform.xslt.XsltTransformFactory;
import org.switchyard.transform.xslt.XsltTransformer;

/**
 * @author Alejandro Montenegro <a href="mailto:aamonten@gmail.com">aamonten@gmail.com</a>
 */
public class XsltTransformerTest {

	private final static String INITIAL = "<?xml version=\"1.0\"?><project><topic><title>Switchyard</title><url>http://www.jboss.org/switchyard</url>"
		  + "</topic><topic><title>Arquillian</title><url>http://www.jboss.org/arquillian</url></topic><topic><title>Drools</title>"
		  + "<url>http://www.jboss.org/drools</url></topic><topic><title>JBoss Tools</title><url>http://www.jboss.org/tools</url>"
		  + "</topic></project>";
	
	private final static String INITIAL_FAIL = "<?xml version=\"1.0\"?><project><topic><title>Switchyard</title><url>http://www.jboss.org/switchyard</url>"
		  + "</topic><topic><title>Arquillian</title><url>http://www.jboss.org/arquillian</url></topic><topic><title>Drools</title>"
		  + "<url>http://www.jboss.org/drools</url></topic><topic><title>JBoss Tools</title><url>http://www.jboss.org/tools</url>";
	
	private final static String EXPECTED = "<html>\r\n<head>\r\n<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\r\n"
			+ "<title>JBoss Project's'</title>\r\n</head>\r\n<body>\r\n<table border=\"1\">\r\n<tr>\r\n<th>Title</th><th>URL</th>\r\n</tr>\r\n<tr>\r\n"
			+ "<td>Switchyard</td><td>http://www.jboss.org/switchyard</td>\r\n</tr>\r\n<tr>\r\n<td>Arquillian</td><td>http://www.jboss.org/arquillian</td>\r\n"
			+ "</tr>\r\n<tr>\r\n<td>Drools</td><td>http://www.jboss.org/drools</td>\r\n</tr>\r\n<tr>\r\n<td>JBoss Tools</td><td>http://www.jboss.org/tools</td>\r\n"
			+ "</tr>\r\n</table>\r\n</body>\r\n</html>\r\n"; 
	
	
	@Test
	public void test_no_validation() throws IOException {
		try {
			getTransformer("xslt-config-01.xml");
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_no_xslt_file() throws IOException {
		try {
			getTransformer("xslt-config-02.xml");
			Assert.fail("the configuration file should be invalid");
		} catch (RuntimeException e) {
			Assert.assertEquals("no xslt file has beed defined, check your transformer configuration", e.getMessage());
		}
	}
	
	@Test
	public void test_local_xslt_file() throws IOException {
		try {
			Transformer transformer = getTransformer("xslt-config-03.xml");
			Object result = transformer.transform(INITIAL);
			Assert.assertEquals(EXPECTED, result);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void test_local_xslt_file_fail() throws IOException {
		try {
			Transformer transformer = getTransformer("xslt-config-03.xml");
			Object result = transformer.transform(INITIAL_FAIL);
			Assert.fail("xml to transform should be invalid");
		} catch (RuntimeException e) {
			Assert.assertEquals("Error during xslt transformation", e.getMessage());
		}
	}

	private Transformer getTransformer(String config) throws IOException {
		InputStream swConfigStream = Classes.getResourceAsStream(config, getClass());

		if (swConfigStream == null) {
			Assert.fail("null config stream.");
		}

		SwitchYardModel switchyardConfig = new ModelResource<SwitchYardModel>().pull(swConfigStream);
		TransformsModel transforms = switchyardConfig.getTransforms();

		XsltTransformModel xsltTransformModel = (XsltTransformModel) transforms.getTransforms().get(0);

		if (xsltTransformModel == null) {
			Assert.fail("No xslt config.");
		}

		Transformer transformer = XsltTransformFactory.newTransformer(xsltTransformModel);

		if (!(transformer instanceof XsltTransformer)) {
			Assert.fail("Not an instance of XsltTransformer.");
		}

		return transformer;
	}
}
