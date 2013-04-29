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

package org.switchyard.validate.xml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.log4j.Logger;
import org.apache.xml.resolver.CatalogManager;
import org.apache.xml.resolver.tools.CatalogResolver;
import org.switchyard.Message;
import org.switchyard.common.type.Classes;
import org.switchyard.config.model.Scannable;
import org.switchyard.exception.SwitchYardException;
import org.switchyard.validate.BaseValidator;
import org.switchyard.validate.ValidateLogger;
import org.switchyard.validate.ValidateMessages;
import org.switchyard.validate.ValidationResult;
import org.switchyard.validate.config.model.FileEntryModel;
import org.switchyard.validate.config.model.XmlSchemaType;
import org.switchyard.validate.config.model.XmlValidateModel;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/**
 * XML Validator {@link org.switchyard.validate.Validator}.
 * 
 * @author <a href="mailto:tm.igarashi@gmail.com">Tomohisa Igarashi</a>
 */
@Scannable(false)
public class XmlValidator extends BaseValidator<Message> {

    private static final Logger LOGGER = Logger.getLogger(XmlValidator.class);
    private XmlSchemaType _schemaType;
    private String _schemaTypeUri;
    private boolean _failOnWarning;
    private boolean _isNamespaceAware;
    private List<FileEntryModel> _schemaConfig;
    private List<FileEntryModel> _catalogConfig;
    private XMLReader _validatingParser;
    private List<String> _schemaFileNames = new ArrayList<String>();
    private List<String> _catalogFileNames = new ArrayList<String>();
    
    /**
     * constructor.
     * @param name name
     * @param model model
     */
    public XmlValidator(QName name, XmlValidateModel model) {
        super(name);

        _schemaType = model.getSchemaType();
        if (_schemaType == null) {
            throw ValidateMessages.MESSAGES.couldNotInstantiateXmlValidator();
        }
        
        switch(_schemaType) {
        case DTD:
            _schemaTypeUri = XMLConstants.XML_DTD_NS_URI;
            break;
        case XML_SCHEMA:
            _schemaTypeUri = XMLConstants.W3C_XML_SCHEMA_NS_URI;
            break;
        case RELAX_NG:
            _schemaTypeUri = XMLConstants.RELAXNG_NS_URI;
            break;
        default:
            throw ValidateMessages.MESSAGES.couldNotInstantiateXmlValidatorBadSchemaType(_schemaType.toString(),
                    XmlSchemaType.values().toString());
        }
        
        _failOnWarning = model.failOnWarning();
        _isNamespaceAware = model.namespaceAware();
        if (model.getSchemaFiles() != null) {
            _schemaConfig = model.getSchemaFiles().getEntries();
        }
        if (model.getSchemaCatalogs() != null) {
            _catalogConfig = model.getSchemaCatalogs().getEntries();
        }
        
        setupValidatingParser();
    }

    protected void setupValidatingParser() {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(new StringBuffer("Setting up XmlValidator:[").append(formatUnparsedConfigs()).append("]"));
        }

        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setXIncludeAware(true);
        parserFactory.setNamespaceAware(_isNamespaceAware);

        XmlValidatorCatalogResolver catalogResolver = null;
        if (_catalogConfig != null) {
            List<String> foundCatalogs = new ArrayList<String>();
            for (FileEntryModel entry : _catalogConfig) {
                URL located = locateFile(entry.getFile());
                if (located != null) {
                    foundCatalogs.add(located.toString());
                } else {
                    ValidateLogger.ROOT_LOGGER.schemaCatalogNotLocated(entry.getFile());
                }
            }
            if (foundCatalogs.size() > 0) {
                _catalogFileNames = foundCatalogs;
                CatalogManager manager = new CatalogManager();
                manager.setIgnoreMissingProperties(true);
                manager.setAllowOasisXMLCatalogPI(true);
                manager.setPreferPublic(true);
                manager.setRelativeCatalogs(false);
                manager.setUseStaticCatalog(false);
                manager.setVerbosity(0);
                StringBuilder buf = new StringBuilder();
                buf.append(foundCatalogs.get(0));
                for (int i=1; i<foundCatalogs.size(); i++) {
                    buf.append(";").append(foundCatalogs.get(i));
                }
                manager.setCatalogFiles(buf.toString());
                catalogResolver = new XmlValidatorCatalogResolver(manager);
                catalogResolver.namespaceAware = _isNamespaceAware;
            }
        }
        
        if (XMLConstants.XML_DTD_NS_URI.equals(_schemaTypeUri)) {
            // set up for DTD validation - DTD file is located by DOCTYPE element in the Document itself
            parserFactory.setValidating(true);
            
            try {
                _validatingParser = parserFactory.newSAXParser().getXMLReader();
                if (catalogResolver != null) {
                    _validatingParser.setEntityResolver(catalogResolver);
                }
            } catch (SAXException se) {
                throw new SwitchYardException(se);
            } catch (ParserConfigurationException pce) {
                throw new SwitchYardException(pce);
            }
            
        } else {
            // setup for XML Schema or Relax NG validation
            if (_schemaConfig == null) {
                throw ValidateMessages.MESSAGES.schemaFileMustBeSpecified(_schemaType.toString());
            }
            
            SchemaFactory schemaFactory = SchemaFactory.newInstance(_schemaTypeUri);
            if (catalogResolver != null) {
                schemaFactory.setResourceResolver(catalogResolver);
            }
            
            List<Source> foundSchemas = new ArrayList<Source>();
            for (FileEntryModel entry : _schemaConfig) {
                URL located = locateFile(entry.getFile());
                if (located != null) {
                    _schemaFileNames.add(located.toString());
                    foundSchemas.add(new StreamSource(located.toExternalForm()));
                } else {
                    ValidateLogger.ROOT_LOGGER.schemaFileNotLocated(entry.getFile());
                }
            }

            if (foundSchemas.size() == 0) {
                throw ValidateMessages.MESSAGES.noValidSchemaFileFound();
            }
            
            try {
                Schema schema = schemaFactory.newSchema(foundSchemas.toArray(new Source[0]));
                parserFactory.setSchema(schema);
                _validatingParser = parserFactory.newSAXParser().getXMLReader();
            } catch (SAXException e) {
                throw new SwitchYardException(e);
            } catch (ParserConfigurationException pce) {
                throw new SwitchYardException(pce);
            }
        }
    }
    
    @Override
    public ValidationResult validate(Message msg) {
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(new StringBuffer("Entering XML validation:[")
                        .append(formatUnparsedConfigs()).append(" / ").append(formatParsedConfigs()).append("]"));
        }
        
        try {
            XmlValidationErrorHandler errorHandler = new XmlValidationErrorHandler(_failOnWarning);
            _validatingParser.setErrorHandler(errorHandler);
            _validatingParser.parse(msg.getContent(InputSource.class));
            if (errorHandler.validationFailed()) {
                return invalidResult(formatErrorMessage(errorHandler.getErrors()).toString());
            }
        } catch (SAXException e) {
            throw new SwitchYardException(e);
        } catch (IOException ioe) {
            throw new SwitchYardException(ioe);
        }
        return validResult();
    }

    protected URL locateFile(String path) {
        if (path == null) {
            return null;
        }
        
        if (new File(path).exists()) {
            try {
                return new File(path).toURI().toURL();
            } catch (Exception e) {
                return null;
            }
        } else {
            try {
                URL res = Classes.getResource(path);
                if (res != null) {
                    return res;
                }
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return null;
    }
    
    
    protected StringBuffer formatErrorMessage(List<Exception> errors) {
        String nl = System.getProperty("line.separator");
        StringBuffer buf = new StringBuffer().append(errors.size()).append(" validation error(s): ").append(nl);
        for (Exception e : errors) {
            buf.append(formatRootCause(e)).append(nl);
        }
        return buf;
    }

    protected StringBuffer formatRootCause(Throwable t) {
        Throwable cause = t;
        StringBuffer buf = new StringBuffer(cause.getClass().getName()).append(": ").append(cause.getMessage());
        while ((cause = cause.getCause()) != null) {
            buf.append(" --- Caused by ").append(cause.getClass().getName()).append(": ").append(cause.getMessage());
        }
        return buf;
    }
    
    protected StringBuffer formatUnparsedConfigs() {
        StringBuffer buf = new StringBuffer();
        buf.append("schema type=").append(_schemaType); 
        if (_schemaConfig != null && _schemaConfig.size() > 0) {
            buf.append(", schema files=").append(_schemaConfig.toString());
        }
        if (_catalogConfig != null && _catalogConfig.size() > 0) {
            buf.append(", catalogs=").append(_catalogConfig.toString());
        }
        return buf;
    }
    
    protected StringBuffer formatParsedConfigs() {
        StringBuffer buf = new StringBuffer();
        buf.append("schema type=").append(_schemaType); 
        if (_schemaFileNames.size() > 0) {
            buf.append(", schema files=").append(_schemaFileNames.toString());
        }
        if (_catalogFileNames.size() > 0) {
            buf.append(", catalogs=").append(_catalogFileNames.toString());
        }
        return buf;
    }
    
    protected class XmlValidationErrorHandler extends DefaultHandler {
        private boolean _validationFailed;
        private boolean _failOnWarning;
        private List<Exception> _errors = new ArrayList<Exception>();
        
        public XmlValidationErrorHandler(boolean failOnWarning) {
            _failOnWarning = failOnWarning;
            _validationFailed = false;
        }
        
        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            _validationFailed = true;
            _errors.add(e);
        }
        
        @Override
        public void error(SAXParseException e) throws SAXException {
            _validationFailed = true;
            _errors.add(e);
        }
        
        @Override
        public void warning(SAXParseException e) throws SAXException {
            if (_failOnWarning) {
                _validationFailed = true;
                _errors.add(e);
            } else {
                StringBuffer warning = new StringBuffer();
                warning.append(formatParsedConfigs()).append(": ").append(e.getMessage());
                ValidateLogger.ROOT_LOGGER.warningDuringValidation(warning.toString());
            }
        }
        
        public boolean validationFailed() {
            return _validationFailed;
        }
        
        public List<Exception> getErrors() {
            return Collections.unmodifiableList(_errors);
        }
    }
    
    private class XmlValidatorCatalogResolver extends CatalogResolver implements LSResourceResolver {
        public XmlValidatorCatalogResolver(CatalogManager manager) {
            super(manager);
        }
        
        @Override
        public LSInput resolveResource(String type, String namespaceURI,
                String publicId, String systemId, String baseURI) {
            return new XmlValidatorLSInput(this.resolveEntity(publicId, systemId), publicId, systemId, baseURI);
        }
    }

    private final class XmlValidatorLSInput implements LSInput {

        private Reader _characterStream;
        private InputStream _byteStream;
        private InputSource _inputSource;
        private String _publicId;
        private String _systemId;
        private String _baseURI;
        private String _encoding;
        private boolean _certifiedText;

        private XmlValidatorLSInput(InputSource xsd, String publicId, String systemId, String baseURI) {
            _inputSource = xsd;
            setPublicId(publicId);
            setSystemId(systemId);
            setBaseURI(baseURI);
            setEncoding("UTF-8");
            setCertifiedText(false);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Reader getCharacterStream() {
            if (_inputSource != null) {
                return _inputSource.getCharacterStream();
            } else {
                return _characterStream;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setCharacterStream(Reader characterStream) {
            _characterStream = characterStream;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public InputStream getByteStream() {
            if (_inputSource != null) {
                return _inputSource.getByteStream();
            } else {
                return _byteStream;
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setByteStream(InputStream byteStream) {
            _byteStream = byteStream;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getStringData() {
            Reader r = _inputSource.getCharacterStream();
            int c;
            StringBuilder buf = new StringBuilder();
            try {
                while ((c = r.read()) != -1) {
                buf.append((char)c);
                }
            } catch (Exception e) {
                return null;
            }
            return buf.toString();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setStringData(String stringData) {
            _inputSource = new InputSource(new StringReader(stringData));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getSystemId() {
            return _systemId;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getPublicId() {
            return _publicId;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setPublicId(String publicId) {
            _publicId = publicId;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setSystemId(String systemId) {
            _systemId = systemId;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getBaseURI() {
            return _baseURI;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setBaseURI(String baseURI) {
            _baseURI = baseURI;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getEncoding() {
            return _encoding;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setEncoding(String encoding) {
            _encoding = encoding;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean getCertifiedText() {
            return _certifiedText;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void setCertifiedText(boolean certifiedText) {
            _certifiedText = certifiedText;
        }

    }

}
