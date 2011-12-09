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

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

import junit.framework.Assert;

import org.codehaus.plexus.util.FileUtils;
import org.jboss.arquillian.api.Deployment;
import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.project.dependencies.DependencyResolver;
import org.jboss.forge.test.AbstractShellTest;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.switchyard.tools.forge.bean.BeanFacet;

/**
 * Test for {@link SwitchyardFacet}.
 *
 * @author Mario Antollini
 */
public class ForgeSwitchyardTest extends AbstractShellTest {

    @Inject
    private DependencyResolver resolver;
    
    private static OutputStream outputStream;
    
    //Ideally we should be obtaining the version from the SwitchyardFacet.
    //However, during test time, this facet is not packaged yet, so we just get
    //the version from Beanfacet.
    private static final String switchyardVersion = BeanFacet.class.getPackage().getSpecificationVersion();
    
    private static final String switchyardFacetVersion = " - [org.switchyard:switchyard-api:::" + switchyardVersion;
    
    private static final String FORGE_APP = "ForgeTestApp";
    
    private static final String BEAN_SERVICE = "ForgeBeanService";
    
    private static final String BEAN_SERVICE_REFERENCEABLE = "ForgeBeanServiceReferenceable";
    
    private static final String SERVICE_TEST = "ForgeServiceTest";
    
    private static final String _switchyardSuccessMsg = "SwitchYard version " + switchyardVersion;
    
    private static final String _servicePromotedSuccessMsg = "Promoted service " + BEAN_SERVICE;

    private static final String _serviceTestCreatedSuccessMsg = "Created unit test " + SERVICE_TEST + "Test.java";
    
    private static final String _referencePromotedSuccessMsg = "Promoted reference " + BEAN_SERVICE_REFERENCEABLE;
    
    private static final String _enableMessageTracingSuccessMsg = "Message tracing has been enabled";
    
    private static final String _disableMessageTracingSuccessMsg = "Message tracing has been disabled";
    
    private static final String _getVersionSuccessMsg = "SwitchYard version " + switchyardVersion;
    
    private static final String _showConfigSuccessMsg = "[Public]" + System.getProperty("line.separator") + 
    "service: ForgeBeanService" + System.getProperty("line.separator") + "   interface: " +
    "inherited" + System.getProperty("line.separator") + "reference: ForgeBeanServiceReferenceable" +
    System.getProperty("line.separator") + "   interface: inherited";
    
    @Deployment
    public static JavaArchive getDeployment() {
        
    	// The deployment method is where references to classes, packages, 
        // and configuration files are added via Arquillian.
        JavaArchive archive = AbstractShellTest.getDeployment();
        archive.addPackage(SwitchYardFacet.class.getPackage());
        archive.addPackage(BeanFacet.class.getPackage());
        return archive;
    }

    @Test
    public void test() throws Exception{
    	prepareSwitchyardForge();
        //switchyard promote-service
    	testPromoteService();
    	//switchyard promote-reference
    	testPromoteReference();
    	//switchyard create-service-test
    	testCreateServiceTest();
    	//switchyard trace-messages
    	testTraceMessages();
    	//switchyard get-version
    	testGetVersion();
    	//switchyard show-config
    	testShowConfig();
    }
    
    private void testPromoteService() throws Exception {
    	initializeOutputStream();
        getShell().execute("project install-facet switchyard.bean");
        getShell().execute("bean-service create --serviceName " + BEAN_SERVICE);
        String[] mvnCommand = new String[]{"package", "-Dmaven.test.skip=true"};
        getProject().getFacet(MavenCoreFacet.class).executeMaven(mvnCommand);
        getShell().execute("switchyard promote-service --serviceName " + BEAN_SERVICE);
        System.out.println(outputStream);
        Assert.assertTrue(outputStream.toString().contains(_servicePromotedSuccessMsg));
        Assert.assertNotNull(resolver);
    }
    
    private void testPromoteReference() throws Exception {
    	initializeOutputStream();
        getShell().execute("bean-service create --serviceName " + BEAN_SERVICE_REFERENCEABLE);

        //Let's overwrite the ForgeBeanServiceBean.java file with the one in test/resources, which
        //is referencing ForgeBeanServiceReferenceable
        File from = new File("." + File.separator + "src" + File.separator + "test" + File.separator + 
        		"resources" + File.separator + "com" + File.separator + "test" + File.separator + "ForgeBeanServiceBean.java");
        File to = new File(getShell().getCurrentDirectory().getFullyQualifiedName() + File.separator + "src" + 
        		File.separator + "main" + File.separator + "java" + File.separator + 
        		"com" + File.separator + "test");
        FileUtils.copyFileToDirectory(from, to);
        
        String[] mvnCommand = new String[]{"package", "-Dmaven.test.skip=true"};
        getProject().getFacet(MavenCoreFacet.class).executeMaven(mvnCommand);
        getShell().execute("switchyard promote-reference --referenceName " + BEAN_SERVICE_REFERENCEABLE);
        System.out.println(outputStream);
        Assert.assertTrue(outputStream.toString().substring(outputStream.toString().length() - 50).contains(_referencePromotedSuccessMsg));
        Assert.assertNotNull(resolver);
    }
    
    private void testCreateServiceTest() throws Exception {
    	initializeOutputStream();
        getShell().execute("switchyard create-service-test --serviceName " + SERVICE_TEST);
        System.out.println(outputStream);
        Assert.assertTrue(outputStream.toString().contains(_serviceTestCreatedSuccessMsg));
        Assert.assertNotNull(resolver);
    }
    
    private void testTraceMessages() throws Exception {
        initializeOutputStream();
    	queueInputLines("Y");
        getShell().execute("switchyard trace-messages");
        Assert.assertTrue(outputStream.toString().contains(_enableMessageTracingSuccessMsg));
        queueInputLines("n");
        getShell().execute("switchyard trace-messages");
        System.out.println(outputStream);
        Assert.assertTrue(outputStream.toString().contains(_disableMessageTracingSuccessMsg));
        Assert.assertNotNull(resolver);
    }
    
    private void testGetVersion() throws Exception {
        initializeOutputStream();
    	getShell().execute("switchyard get-version");
        System.out.println(outputStream);
    	Assert.assertTrue(outputStream.toString().contains(_getVersionSuccessMsg));
        Assert.assertNotNull(resolver);
    }
    
    private void testShowConfig() throws Exception {
        initializeOutputStream();
    	getShell().execute("switchyard show-config");
    	System.out.println(outputStream);
        Assert.assertTrue(outputStream.toString().contains(_showConfigSuccessMsg));
        Assert.assertNotNull(resolver);
    }
    
    private void prepareSwitchyardForge() throws IOException {
    	try {
			initializeJavaProject();
			initializeOutputStream();
			resetInputQueue();
			queueInputLines("-1");
			
			try{
				getShell().execute("project install-facet switchyard");
			} catch (Exception e) {
				//Let's discard this expected exception
			}

			int index = outputStream.toString().indexOf(switchyardFacetVersion);
			if(index > -1){
			    Integer version = Integer.decode(outputStream.toString().substring(index - 1, index));
			    queueInputLines(version.toString());
			}
			queueInputLines(FORGE_APP);
			getShell().execute("project install-facet switchyard");
			getShell().execute("switchyard get-version");
			Assert.assertTrue(outputStream.toString().contains(_switchyardSuccessMsg));
			System.out.println(outputStream);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(outputStream);
			initializeOutputStream();
			e.printStackTrace();
		}
    }
    
    
    private void initializeOutputStream()  throws IOException {
        outputStream = new OutputStream()
        {
            private StringBuilder string = new StringBuilder();
            @Override
            public void write(int b) throws IOException {
                this.string.append((char) b );
            }

            public String toString(){
                return this.string.toString();
            }
        };
        getShell().setOutputStream(outputStream);
    }
    
}
