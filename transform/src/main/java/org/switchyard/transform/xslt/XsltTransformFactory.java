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
import java.io.InputStream;
import javax.xml.namespace.QName;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.Templates;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import org.switchyard.common.type.Classes;
import org.switchyard.transform.TransformLogger;
import org.switchyard.transform.TransformMessages;
import org.switchyard.transform.Transformer;
import org.switchyard.transform.TransformerFactory;
import org.switchyard.transform.config.model.XsltTransformModel;

/**
 * @author Alejandro Montenegro <a href="mailto:aamonten@gmail.com">aamonten@gmail.com</a>
 */
public final class XsltTransformFactory implements TransformerFactory<XsltTransformModel>{
    
    /**
     * Create a {@link Transformer} instance from the supplied {@link XsltTransformModel}.
     * @param model the JSON transformer model. 
     * @return the Transformer instance.
     */
    public Transformer newTransformer(XsltTransformModel model) {

        String xsltFileUri = model.getXsltFile();
        boolean failOnWarning = model.failOnWarning();
        QName to = model.getTo();
        QName from = model.getFrom();

        if (xsltFileUri == null || xsltFileUri.equals("")) {
            throw TransformMessages.MESSAGES.noXSLFileDefined();
        }

        try {
            InputStream stylesheetStream = Classes.getResourceAsStream(xsltFileUri);
            
            if (stylesheetStream == null) {
                TransformMessages.MESSAGES.failedToLoadXSLFile(xsltFileUri);
            }
            javax.xml.transform.TransformerFactory tFactory = javax.xml.transform.TransformerFactory.newInstance();
            tFactory.setErrorListener(new XsltTransformFactoryErrorListener(failOnWarning));
            Templates templates = tFactory.newTemplates(new StreamSource(stylesheetStream));
            
            return new XsltTransformer(from, to, templates, failOnWarning);
        } catch (TransformerConfigurationException e) {
            throw TransformMessages.MESSAGES.unexpectedErrorOcurred(e);
        } catch (IOException e) {
            throw TransformMessages.MESSAGES.unableToLocateXSLTFile(model.getXsltFile().toString(), e);
        }
    }
    
    private class XsltTransformFactoryErrorListener implements ErrorListener {
        private boolean _failOnWarning;

        public XsltTransformFactoryErrorListener(boolean failOnWarning) {
             this._failOnWarning = failOnWarning;
        }
        
        @Override
        public void warning(TransformerException ex) throws TransformerException {
            if (_failOnWarning) {
                throw ex;
           } else {
               TransformLogger.ROOT_LOGGER.warningDuringCompilation(ex);
             }
        }

        @Override
        public void error(TransformerException ex) throws TransformerException {
            throw ex;
        }

        @Override
        public void fatalError(TransformerException ex) throws TransformerException {
            throw ex;
        }
    }
}
