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
package org.switchyard.transform.xslt;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.switchyard.config.model.Scannable;
import org.switchyard.transform.BaseTransformer;
import org.switchyard.transform.config.model.XsltTransformModel;

/**
 * XSLT Transformer {@link org.switchyard.transform.Transformer}.
 * @author Alejandro Montenegro <a href="mailto:aamonten@gmail.com">aamonten@gmail.com</a>
 */
@Scannable(false)
public class XsltTransformer extends BaseTransformer{

	private static Logger _log = Logger.getLogger(XsltTransformer.class);
	javax.xml.transform.Transformer transformer;
		
	public XsltTransformer(javax.xml.transform.Transformer transformer, XsltTransformModel model) {
		super(model.getFrom(), model.getTo());
		this.transformer = transformer;
	}
	
	@Override
	public Object transform(Object from) {
		
		StreamResult result = null;
		try {
			
			InputStream input = new ByteArrayInputStream(((String)from).getBytes("UTF-8"));
			StreamSource source = new StreamSource(input);
			StringWriter writer = new StringWriter();
			result = new StreamResult(writer);
			transformer.transform(source, result);
			
		} catch (Exception e) {
			throw new RuntimeException("Error during xslt transformation", e);
		}
		return ((StringWriter)result.getWriter()).getBuffer().toString();
	}

}
