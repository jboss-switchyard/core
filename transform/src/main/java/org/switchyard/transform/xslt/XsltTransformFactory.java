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

import java.io.IOException;

import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.switchyard.common.type.Classes;
import org.switchyard.transform.Transformer;
import org.switchyard.transform.config.model.XsltTransformModel;

/**
 * @author Alejandro Montenegro <a href="mailto:aamonten@gmail.com">aamonten@gmail.com</a>
 */
public class XsltTransformFactory {
	
	public static Transformer newTransformer(XsltTransformModel model) {
		
		String xsltFileUri = model.getXsltFile();
		if(xsltFileUri == null || xsltFileUri.equals("")){
			throw new RuntimeException("no xslt file has beed defined, check your transformer configuration");
		}
		if (model.getFrom() == null || model.getFrom().equals("")) {
            throw new RuntimeException("Invalid xslt configuration model.  null or 'from' specification.");
        }
        if (model.getTo() == null || model.getTo().equals("")) {
            throw new RuntimeException("Invalid xslt configuration model.  null or 'to' specification.");
        }
		
		javax.xml.transform.Transformer transformer = null;
		try{
			StreamSource styleSheet = new StreamSource(Classes.getResourceAsStream(xsltFileUri));
			TransformerFactory tFactory = TransformerFactory.newInstance();
			transformer = tFactory.newTransformer(styleSheet);
		}
		catch(TransformerConfigurationException e){
			throw new RuntimeException("An unexpected error ocurred while creating the xslt transformer", e);
		} catch (IOException e) {
			throw new RuntimeException("Unable to locate the xslt file " + model.getXsltFile(), e);
		}
		return new XsltTransformer(transformer, model);
	}

}
