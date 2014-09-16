/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.switchyard.admin.base;

import javax.xml.namespace.QName;

import org.junit.Assert;
import org.junit.Test;
import org.switchyard.ServiceDomain;
import org.switchyard.config.model.ModelPuller;
import org.switchyard.config.model.switchyard.SwitchYardModel;
import org.switchyard.deploy.ServiceDomainManager;
import org.switchyard.deploy.event.ApplicationDeployedEvent;
import org.switchyard.deploy.event.ApplicationUndeployedEvent;
import org.switchyard.deploy.internal.Deployment;

public class SwitchYardBuilderTest extends SwitchYardBuilderTestBase {

    public SwitchYardBuilderTest() throws Exception {
        super();
    }

	@Test
    public void testApplication() {
        Assert.assertEquals(1, _switchYard.getApplications().size());
    }

    @Test
    public void testService() {
        Assert.assertEquals(1, _switchYard.getApplication(TEST_APP).getServices().size());
    }

    @Test
    public void testComponent() {
        Assert.assertEquals(2, _switchYard.getApplication(TEST_APP).getComponentServices().size());
    }

    @Test
    public void testNoComponentService() throws Exception{
        Deployment testDeployment = new MockDeployment(
                new ModelPuller<SwitchYardModel>().pull("switchyard_multiappweb.xml", getClass()), 
                QName.valueOf("{urn:switchyard-quickstart-demo:multiapp:0.1.0}web"), _domainManager);
        _domainManager.getEventManager().publish(new ApplicationDeployedEvent(testDeployment));
        Assert.assertEquals(2, _builder.getSwitchYard().getApplications().size());
        _domainManager.getEventManager().publish(new ApplicationUndeployedEvent(testDeployment));
        Assert.assertEquals(1, _builder.getSwitchYard().getApplications().size());
    }

}

class MockDeployment extends Deployment {
    private ServiceDomain _domain;
    private QName _name;
    
    MockDeployment(SwitchYardModel config, QName name, ServiceDomainManager sdm) {
        super(config);
        _name = name;
        _domain = sdm.createDomain(name, config);
    }
    
    @Override
    public QName getName() {
        return _name;
    }

    @Override
    public ServiceDomain getDomain() {
        return _domain;
    }
    
}
