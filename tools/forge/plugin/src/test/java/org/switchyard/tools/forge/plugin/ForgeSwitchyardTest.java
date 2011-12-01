/*
 * JBoss, by Red Hat.
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.switchyard.tools.forge.plugin;

import org.jboss.arquillian.api.Deployment;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Assert;
import org.junit.Test;
import org.switchyard.tools.forge.GenericTestForge;

/**
 * Test for {@link SwitchyardFacet}.
 *
 * @author Mario Antollini
 */
public class ForgeSwitchyardTest extends GenericTestForge {

    private static final String SERVICE_TEST = "ForgeServiceTest";

    private static final String _enableMessageTracingSuccessMsg = "Message tracing has been enabled";

    private static final String _disableMessageTracingSuccessMsg = "Message tracing has been disabled";

    private static final String _getVersionSuccessMsg = "SwitchYard version " + switchyardVersion;

    private static final String _showConfigSuccessMsg = "[Public]" + System.getProperty("line.separator") + 
                                        System.getProperty("line.separator") + "[Private]";

    
    @Deployment
    public static JavaArchive getDeployment() {
        // The deployment method is where references to classes, packages, 
        // and configuration files are added via Arquillian.
        JavaArchive archive = AbstractShellTest.getDeployment();
        archive.addPackages(true, SwitchYardFacet.class.getPackage());
        return archive;
    }
    
    @Test
    public void runTest(){
        try {
        
            createTestService();
            testTraceMessages();
            testGetVersion();
            testShowConfig();
        
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public void createTestService() throws Exception {
        resetOutputStream();
        getShell().execute("switchyard create-service-test --serviceName " + SERVICE_TEST);
        System.out.println(outputStream);
        Assert.assertTrue(outputStream.toString().contains("Created unit test " + SERVICE_TEST + "Test.java"));
    }
    
    public void testTraceMessages() throws Exception{
        resetOutputStream();
        queueInputLines("Y");
        getShell().execute("switchyard trace-messages");
        Assert.assertTrue(outputStream.toString().contains(_enableMessageTracingSuccessMsg));
        queueInputLines("n");
        getShell().execute("switchyard trace-messages");
        System.out.println(outputStream);
        Assert.assertTrue(outputStream.toString().contains(_disableMessageTracingSuccessMsg));
    }

    public void testGetVersion() throws Exception {
        resetOutputStream();
    	getShell().execute("switchyard get-version");
        System.out.println(outputStream);
    	Assert.assertTrue(outputStream.toString().contains(_getVersionSuccessMsg));
    }

    public void testShowConfig() throws Exception {
        resetOutputStream();
    	getShell().execute("switchyard show-config");
    	System.out.println(outputStream);
        Assert.assertTrue(outputStream.toString().contains(_showConfigSuccessMsg));
    }
    
}
