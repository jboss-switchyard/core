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
package org.switchyard.integration.rules.config.model;

import org.switchyard.config.model.Model;
import org.switchyard.integration.rules.audit.AuditType;

/**
 * RulesAuditModel.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public interface AuditModel extends Model {

    /**
     * The audit XML element.
     */
    public static final String AUDIT = "audit";

    /**
     * Gets the interval of the audit.
     * @return the interval of the audit
     */
    public Integer getInterval();

    /**
     * Sets the interval of the audit.
     * @param interval the interval of the audit
     * @return this AuditModel (useful for chaining)
     */
    public AuditModel setInterval(Integer interval);

    /**
     * Gets the log of the audit.
     * @return the log of the audit
     */
    public String getLog();

    /**
     * Sets the log of the audit.
     * @param log the log of the audit
     * @return this AuditModel (useful for chaining)
     */
    public AuditModel setLog(String log);
 
    /**
     * Gets the type of the audit.
     * @return the type of the audit
     */
    public AuditType getType();
 
    /**
     * Sets the type of the audit.
     * @param type the type of the audit
     * @return this AuditModel (useful for chaining)
     */
    public AuditModel setType(AuditType type);

}
