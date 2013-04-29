package org.switchyard;

import org.jboss.logging.Messages;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageBundle;
/**
 * <p/>
 * This file is using the subset 12200-12399 for logger messages.
 * <p/>
 *
 */
@MessageBundle(projectCode = "SWITCHYARD")
public interface BaseDeployMessages {
    /**
     * Default messages.
     */
    BaseDeployMessages MESSAGES = Messages.getBundle(BaseDeployMessages.class);

    /**
     * usagePath method definition.
     * @param path path
     * @return Localized string
     */
    @Message(id = 12200, value = "Usage: %s path-to-switchyard-config")
    String usagePath(String path);

    /**
     * notValidConfigFile method definition.
     * @param arg arg
     * @return Localized string
     */
    @Message(id = 12201, value = "'%s' is not a valid SwitchYard configuration file.")
    String notValidConfigFile(String arg);
}
