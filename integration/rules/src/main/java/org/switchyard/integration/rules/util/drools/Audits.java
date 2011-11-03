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
package org.switchyard.integration.rules.util.drools;

import org.drools.event.KnowledgeRuntimeEventManager;
import org.drools.logger.KnowledgeRuntimeLogger;
import org.drools.logger.KnowledgeRuntimeLoggerFactory;
import org.switchyard.common.lang.Strings;
import org.switchyard.integration.rules.audit.AuditType;
import org.switchyard.integration.rules.config.model.AuditModel;

/**
 * Drools Audit Utilities.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public final class Audits {

    /**
     * Gets a Drools audit logger given an audit model and a session.
     * @param audit the audit model
     * @param ksession the session
     * @return the logger
     */
    public static KnowledgeRuntimeLogger getLogger(AuditModel audit, KnowledgeRuntimeEventManager ksession) {
        if (audit != null) {
            AuditType type = audit.getType();
            if (type == null) {
                type = AuditType.THREADED_FILE;
            }
            String log = Strings.trimToNull(audit.getLog());
            String fileName = log != null ? log : "event";
            Integer interval = audit.getInterval();
            if (interval == null) {
                interval = Integer.valueOf(1000);
            }
            switch (type) {
                case CONSOLE:
                    return KnowledgeRuntimeLoggerFactory.newConsoleLogger(ksession);
                case FILE:
                    return KnowledgeRuntimeLoggerFactory.newFileLogger(ksession, fileName);
                case THREADED_FILE:
                    return KnowledgeRuntimeLoggerFactory.newThreadedFileLogger(ksession, fileName, interval.intValue());
            }
        }
        return null;
    }

    private Audits() {}

}
