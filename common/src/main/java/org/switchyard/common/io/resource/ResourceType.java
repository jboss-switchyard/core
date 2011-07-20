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
package org.switchyard.common.io.resource;

/**
 * Represents the type of a Resource.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2011 Red Hat Inc.
 */
public enum ResourceType {

    /** Business Process Modeling Notation 2. */
    BPMN2,
    /** Business Rule Language. */
    BRL,
    /** Drools Rule Flow. */
    DRF,
    /** Drools Rule Language. */
    DRL,
    /** Domain Specific Language. */
    DSL,
    /** Domain Specific Language Rules. */
    DSLR,
    /** Decision  Table. */
    DTABLE,
    /** Package. */
    PKG,
    /** XML Drools Rule Language. */
    XDRL,
    /** XML Schema Definition. */
    XSD

}
