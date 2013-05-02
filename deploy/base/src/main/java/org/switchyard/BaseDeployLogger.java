package org.switchyard;

import org.jboss.logging.Logger;
import org.jboss.logging.annotations.MessageLogger;

/**
 * <p/>
 * This file is using the subset 12000-12199 for logger messages.
 * <p/>
 *
 */
@MessageLogger(projectCode = "SWITCHYARD")
public interface BaseDeployLogger {
    /**
     * Default root logger.
     */
    BaseDeployLogger ROOT_LOGGER = Logger.getMessageLogger(BaseDeployLogger.class, BaseDeployLogger.class.getPackage().getName());
}
