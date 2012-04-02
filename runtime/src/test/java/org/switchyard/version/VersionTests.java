/* 
 * JBoss, Home of Professional Open Source 
 * Copyright 2012 Red Hat Inc. and/or its affiliates and other contributors
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
package org.switchyard.version;

import junit.framework.Assert;

import org.junit.Test;

/**
 * VersionTests.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; (C) 2012 Red Hat Inc.
 */
public class VersionTests {

    @Test
    public void testPrintWelcomeAndVersions() {
        Versions.printSwitchYardWelcome();
        Versions.printSwitchYardVersions();
    }

    @Test
    public void testVersionEquals() {
        final Version expected = VersionFactory.instance().getVersion(new Query(QueryType.SPECIFICATION_TITLE, "SwitchYard: API"));
        final Version actual = Versions.getSwitchYardAPIVersion();
        Assert.assertNotNull(expected);
        Assert.assertNotNull(actual);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testVersionTitles() {
        final String[] titles = {"SwitchYard: API", "SwitchYard: Common"};
        for (final String title : titles) {
            final Query query = new Query(QueryType.SPECIFICATION_TITLE, title);
            final String actual = VersionFactory.instance().getVersion(query).getSpecification().getTitle();
            Assert.assertEquals(title, actual);
        }
    }

}
