package org.switchyard.bus.camel;

import java.util.List;
import java.util.Set;
import javax.xml.namespace.QName;
import org.apache.camel.Processor;

import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Cause;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
import org.switchyard.ExchangeHandler;
import org.switchyard.exception.SwitchYardException;

/**
 * <p/>
 * This file is using the subset 10800-10999 for logger messages.
 * <p/>
 *
 */
@MessageBundle(projectCode = "SWITCHYARD")
public interface BusMessages {
    /**
     * Default messages.
     */
    BusMessages MESSAGES = Messages.getBundle(BusMessages.class);

    /**
     * maxOneExceptionHandler method definition.
     * @param set set
     * @return SwitchYardException
     */
    @Message(id = 10800, value = "Only one exception handler can be defined. Found %s")
    SwitchYardException maxOneExceptionHandler(Set set);

    /**
     * failedToStartBus method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id = 10801, value = "Failed to start Camel Exchange Bus")
    SwitchYardException failedToStartBus(@Cause Exception e);

    /**
     * failedToStopBus method definition.
     * @param e e
     * @return SwitchYardException
     */
    @Message(id = 10802, value = "Failed to stop Camel Exchange Bus")
    SwitchYardException failedToStopBus(@Cause Exception e);

    /**
     * failedToCreateRoute method definition.
     * @param name name
     * @param e e
     * @return SwitchYardException
     */
    @Message(id = 10803, value = "Failed to create Camel route for service %s")
    SwitchYardException failedToCreateRoute(QName name, @Cause Exception e);

    /**
     * faultProcessorString method definition.
     * @param processor processor
     * @return String
     */
    @Message(id = 10804, value = "FaultProcessor [%s]")
    String faultProcessorString(Processor processor);

    /**
     * handlerProcessorString method definition.
     * @param handlers handlers
     * @return String
     */
    @Message(id = 10805, value = "HandlerProcessor [%s]")
    String handlerProcessorString(List<ExchangeHandler> handlers);
 
    /**
     * providerProcessorString method definition.
     * @param hashCode hashCode
     * @return String
     */
    @Message(id = 10806, value = "ProviderProcessor@ %d")
    String providerProcessorString(int hashCode);

    /**
     * methodMustBeOverridden method definition.
     * @return AbstractMethodError
     */
    @Message(id = 10807, value = "Method must be overriden")
    AbstractMethodError methodMustBeOverridden();
}
