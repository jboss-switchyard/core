package org.switchyard.transform;

import java.beans.IntrospectionException;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;

import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.switchyard.exception.SwitchYardException;

import org.xml.sax.SAXException;


/**
 * <p/>
 * This file is using the subset 16800-16999 for logger messages.
 * <p/>
 *
 */
@MessageBundle(projectCode = "SWITCHYARD")
public interface TransformMessages {
    /**
     * Default messages.
     */
    TransformMessages MESSAGES = Messages.getBundle(TransformMessages.class);

    /**
     * errorDuringXsltTransformation method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16800, value = "Error during xslt transformation")
    SwitchYardException errorDuringXsltTransformation(@Cause Exception e);

    /**
     * noXSLFileDefined method definition.
     * @return SwitchYardException
     */
    @Message(id=16801, value = "No xsl file has been defined. Check your transformer configuration.")
    SwitchYardException noXSLFileDefined();

    /**
     * unexpectedErrorOcurred method definition.
     * @param tce tce
     * @return SwitchYardException
     */
    @Message(id=16802, value = "An unexpected error ocurred while creating the xslt transformer")
    SwitchYardException unexpectedErrorOcurred(@Cause TransformerConfigurationException tce);

    /**
     * unableToLocateXSLTFile method definition.
     * @param fileName fileName
     * @param ioe ioe
     * @return SwitchYardException
     */
    @Message(id=16803, value = "Unable to locate the xslt file %s")
    SwitchYardException unableToLocateXSLTFile(String fileName, @Cause IOException ioe);

    /**
     * failedToLoadXSLFile method definition.
     * @param xsltFileUri xsltFileUri
     * @return SwitchYardException
     */
    @Message(id=16804, value = "Failed to load xsl file '%s' from classpath.")
    SwitchYardException failedToLoadXSLFile(String xsltFileUri);

    /**
     * exceptionTransformingFromXML method definition.
     * @param beanClassName beanClassName
     * @param ioe ioe
     * @return SwitchYardException
     */
    @Message(id=16805, value = "Exception while transforming from XML to '%s'.")
    SwitchYardException exceptionTransformingFromXML(String beanClassName, @Cause IOException ioe);

    /**
     * cannotTransformToXML method definition.
     * @param inputType inputType
     * @param expectedType expectedType
     * @return SwitchYardException
     */
    @Message(id=16806, value = "Cannot transform to XML.  Input type is '%s' but should be '%s'.")
    SwitchYardException cannotTransformToXML(String inputType, String expectedType);

    /**
     * invalidSmooksConfigurationModelNullType method definition.
     * @return SwitchYardException
     */
    @Message(id=16807, value = "Invalid Smooks configuration model.  null or empty 'type' specification.")
    SwitchYardException invalidSmooksConfigurationModelNullType();

    /**
     * invalidSmooksConfigurationModelNullConfig method definition.
     * @return SwitchYardException
     */
    @Message(id=16808, value = "Invalid Smooks configuration model.  null or empty 'config' specification.")
    SwitchYardException invalidSmooksConfigurationModelNullConfig();

    /**
     * invalidSmooksConfigurationModelNullFrom method definition.
     * @return SwitchYardException
     */
    @Message(id=16809, value = "Invalid Smooks configuration model.  null or 'from' specification.")
    SwitchYardException invalidSmooksConfigurationModelNullFrom();

    /**
     * invalidSmooksConfigurationModelNullTo method definition.
     * @return SwitchYardException
     */
    @Message(id=16810, value = "Invalid Smooks configuration model.  null or 'to' specification.")
    SwitchYardException invalidSmooksConfigurationModelNullTo();

    /**
     * failedToCreateSmooksInstance method definition.
     * @param config config
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16811, value = "Failed to create Smooks instance for config '%s'.")
    SwitchYardException failedToCreateSmooksInstance(String config, @Cause Exception e);

    /**
     * invalidBindingConfiguration method definition.
     * @param direction direction
     * @return SwitchYardException
     */
    @Message(id=16812, value = "Invalid %s binding configuration.  No <jb:bean> configurations found.")
    SwitchYardException invalidBindingConfiguration(String direction);

    /**
     * unhandledSmooksTransformationType method definition.
     * @param type type
     * @return SwitchYardException
     */
    @Message(id=16813, value = "Unhandled Smooks transformation type '%s'.")
    SwitchYardException unhandledSmooksTransformationType(String type);

    /**
     * smooksConfigurationNoExports method definition.
     * @return SwitchYardException
     */
    @Message(id=16814, value = "Invalid Smooks configuration file.  Must define an <core:exports> section with "
            + "a single <core:export>.  See Smooks User Guide.")
    SwitchYardException smooksConfigurationNoExports();

    /**
     * unsupportedSmooksExport method definition.
     * @param type type
     * @return SwitchYardException
     */
    @Message(id=16815, value = "Unsupported Smooks <core:export> type '%s'.  Only supports StringResult or "
            + "JavaResult.")
    SwitchYardException unsupportedSmooksExport(String type);

    /**
     * unsupportedExceptionCreatingResult method definition.
     * @param type type
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16816, value = "Unexpected exception while creating an instance of Result type '%s'.")
    SwitchYardException unsupportedExceptionCreatingResult(String type, @Cause Exception e);

    /**
     * unexpectedDOMParserConfigException method definition.
     * @param e e 
     * @return SwitchYardException
     */
    @Message(id=16817, value = "Unexpected DOM parser configuration exception.")
    SwitchYardException unexpectedDOMParserConfigException(@Cause ParserConfigurationException e);

    /**
     * errorSerializingDOMNode method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16818, value = "Error serializing DOM node.")
    SwitchYardException errorSerializingDOMNode(@Cause TransformerException e);

    /**
     * unexpectedExceptionCreatingJDKTransformer method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16819, value = "Unexpected exception creating JDK Transformer instance.")
    SwitchYardException unexpectedExceptionCreatingJDKTransformer(@Cause TransformerConfigurationException e);

    /**
     * errorReadingDOMSourceSAX method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16820, value = "Error reading DOM source.")
    SwitchYardException errorReadingDOMSourceSAX(@Cause SAXException e);

    /**
     * errorReadingDOMSourceIO method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16821, value = "Error reading DOM source.")
    SwitchYardException errorReadingDOMSourceIO(@Cause IOException e);

    /**
     * invalidToTypeNotJavaObject method definition.
     * @param type type 
     * @return SwitchYardException
     */
    @Message(id=16822, value = "Invalid 'to' type '%s'.  Must be a Java Object type.")
    SwitchYardException invalidToTypeNotJavaObject(String type);

    /**
     * invalidToTypeClassNotFound method definition.
     * @param type type
     * @return SwitchYardException
     */
    @Message(id=16823, value = "Invalid 'to' type '%s'.  Class Not Found.")
    SwitchYardException invalidToTypeClassNotFound(String type);

    /**
     * failedToExtractBeanInfo method definition.
     * @param type type
     * @param ioe ioe
     * @return SwitchYardException
     */
    @Message(id=16824, value = "Failed to extract bean information from bean type '%s'.")
    SwitchYardException failedToExtractBeanInfo(String type, @Cause IntrospectionException ioe);

    /**
     * noSetterMethodForProperty method definition.
     * @param property property
     * @param className className
     * @return SwitchYardException
     */
    @Message(id=16825, value = "No setter method for property '%s' on class '%s'.")
    SwitchYardException noSetterMethodForProperty(String property, String className);

    /**
     * unableToCreateInstance method definition.
     * @param type type
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16826, value = "Unable to create instance of type '%s'.")
    SwitchYardException unableToCreateInstance(String type, @Cause Exception e);

    /**
     * errorInvokingSetter method definition.
     * @param methodName methodName
     * @param typeName typeName
     * @param e e 
     * @return SwitchYardException
     */
    @Message(id=16827, value = "Error invoking setter method '%s' on type '%s'.")
    SwitchYardException errorInvokingSetter(String methodName, String typeName, @Cause Exception e);

    /**
     * objectToTransformWrongType method definition.
     * @param type type
     * @return SwitchYardException
     */
    @Message(id=16828, value = "The object to transform is of wrong instance type %s")
    SwitchYardException objectToTransformWrongType(String type);

    /**
     * unexpectedJSONProcessingException method definition.
     * @param e e 
     * @return SwitchYardException
     */
    @Message(id=16829, value = "Unexpected JSON processing exception, check your transformer configuration")
    SwitchYardException unexpectedJSONProcessingException(@Cause JsonProcessingException e);

    /**
     * unexpectedIOException method definition.
     * @param ioe ioe
     * @return SwitchYardException
     */
    @Message(id=16830, value = "Unexpected I/O exception, check your transformer configuration")
    SwitchYardException unexpectedIOException(@Cause IOException ioe);

    /**
     * transformationResultWrongType method definition.
     * @param typeName typeName
     * @return SwitchYardException
     */
    @Message(id=16831, value = "Result of transformation has wrong instance type %s")
    SwitchYardException transformationResultWrongType(String typeName);

    /**
     * unexpectedJSONParseException method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16832, value = "Unexpected JSON parse exception, check your transformer configuration")
    SwitchYardException unexpectedJSONParseException(@Cause JsonParseException e);

    /**
     * unexpectedJSONMappingException method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16833, value = "Unexpected JSON mapping exception, check your transformer configuration")
    SwitchYardException unexpectedJSONMappingException(@Cause JsonMappingException e);
   
    /**
     * unexpectedIOExceptionCheckTransformer method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16834, value = "Unexpected I/O exception, check your transformer configuration")
    SwitchYardException unexpectedIOExceptionCheckTransformer(@Cause IOException e);

    /**
     * notAbleToFindClassDefinition method definition.
     * @param className className
     * @return SwitchYardException
     */
    @Message(id=16835, value = "Not able to find class definition %s")
    SwitchYardException notAbleToFindClassDefinition(String className);

    /**
     * onlyOneJavaType method definition.
     * @return SwitchYardException
     */
    @Message(id=16836, value = "Invalid JSON Transformer configuration.  One (and only one) of the "
            + "specified 'to' and 'from' transform types must be a Java type.")
    SwitchYardException onlyOneJavaType();

    /**
     * failedToCreateJAXBContext method definition. 
     * @param from from
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16837, value = "Failed to create JAXBContext for '%s'.")
    SwitchYardException failedToCreateJAXBContext(String from, @Cause JAXBException e);

    /**
     * failedToCreateMarshaller method definition.
     * @param type type
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16838, value = "Failed to create Marshaller for type '%s'.")
    SwitchYardException failedToCreateMarshaller(String type, @Cause JAXBException e);

    /**
     * failedToUnmarshallForType method definition.
     * @param type type
     * @param e e
     * @return SwitchYardException
     */
    @Message(id=16839, value = "Failed to unmarshall for type '%s'.")
    SwitchYardException failedToUnmarshallForType(String type, @Cause JAXBException e);

    /**
     * noJAXBElementFactoryDefined method definition.
     * @param typeName typeName
     * @param factoryName factoryName
     * @return String
     */
    @Message(id=16840, value = "JAXB Type '%s' does not have a JAXBElement factory method defined in %s"
            + ".  The supported JAXBElement factory methods are for types:")
    String noJAXBElementFactoryDefined(String typeName, String factoryName);

    /**
     * bothJavaTypes method definition.
     * @return SwitchYardException
     */
    @Message(id=16841, value = "Invalid JAXB Transformer configuration.  The 'from' and 'to' "
            + "transformation types are both Java types.  Exactly one must be a Java type.")
    SwitchYardException bothJavaTypes();

    /**
     * neitherJavaType method definition.
     * @return SwitchYardException
     */
    @Message(id=16842, value = "Invalid JAXB Transformer configuration.  Neither 'from' or 'to' "
            + "transformation types is a Java type.  Exactly one must be a Java type.")
    SwitchYardException neitherJavaType();

}
