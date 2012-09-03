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

package org.switchyard.deploy;

import static org.junit.Assert.assertSame;

import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Test;
import org.switchyard.ServiceDomain;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.switchyard.SwitchYardModel;
import org.switchyard.config.model.switchyard.v1.V1SwitchYardModel;

/**
 * @author <a href="mailto:tom.fennelly@gmail.com">tom.fennelly@gmail.com</a>
 */
public class ServiceDomainManagerTest {

    @Test
    public void testHandlerRegistration() throws Exception {
        SwitchYardModel switchyard = new ModelPuller<SwitchYardModel>().pull(
                "/switchyard-config-handler-01.xml", getClass());
        
        ServiceDomain domain = new ServiceDomainManager().createDomain(
                new QName("test"), switchyard);
        
        Assert.assertEquals(2, domain.getHandlers().size());
    }

    @Test
    public void testSharedDomain() throws Exception {
        ServiceDomainManager domainManager = new ServiceDomainManager();

        QName domainName = new QName("urn:switchyard:test", "test-domain");

        SwitchYardModel switchyardConfig = new V1SwitchYardModel();
        ServiceDomain domain = domainManager.createDomain(domainName, switchyardConfig);

        assertSame(domain, domainManager.createDomain(domainName, switchyardConfig));
    }
}
