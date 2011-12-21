package org.switchyard.tools.forge;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import org.jboss.forge.maven.MavenCoreFacet;
import org.jboss.forge.resources.DirectoryResource;
import org.jboss.forge.resources.FileResource;
import org.jboss.forge.test.AbstractShellTest;
import org.junit.Assert;
import org.junit.Before;
import org.switchyard.config.model.switchyard.SwitchYardModel;

/**
 * Generic Test Class to be used for Forge testing.
 *
 * @author Mario Antollini
 */
public abstract class GenericTestForge extends AbstractShellTest {

    //Ideally we should be obtaining the version from the SwitchyardFacet.
    //However, during test time, this facet is not packaged yet, so we just get
    //the version from SwitchyardFacet.
    protected static final String switchyardVersion = SwitchYardModel.class.getPackage().getSpecificationVersion();

    protected static OutputStream outputStream;



    private static final String switchyardFacetVersion = " - [org.switchyard:switchyard-api:::" + switchyardVersion;

    private static final String FORGE_APP_NAME = "ForgeTestApp";

    private static final String _switchyardVersionSuccessMsg = "SwitchYard version " + switchyardVersion;

    public GenericTestForge(){
    }

    @Before
    public void prepareSwitchyardForge() throws IOException {
        try {
            initializeJavaProject();
            resetOutputStream();
            resetInputQueue();

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

            queueInputLines(FORGE_APP_NAME);
            getShell().execute("project install-facet switchyard");
            getShell().execute("switchyard get-version");
            Assert.assertTrue(outputStream.toString().contains(_switchyardVersionSuccessMsg));
            System.out.println(outputStream);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(outputStream);
            resetOutputStream();
        }
    }

    protected void addReferenceToService(String serviceName, String referenceServiceName){

        FileResource<?> serviceFile = null;
        DirectoryResource srcDir = getProject().getProjectRoot().getChildDirectory(
                "src" 
                + File.separator + "main"
                + File.separator + "java"
                + File.separator + "com"
                + File.separator + "test" );
        
        if (srcDir != null && srcDir.exists()) {
            
            serviceFile = (FileResource<?>) srcDir.getChild( serviceName + "Bean.java" );
            
            if(serviceFile != null && serviceFile.exists()){
                StringBuffer output = new StringBuffer();
                InputStream in = serviceFile.getResourceInputStream();

                try{

                    BufferedReader bis = new BufferedReader(new InputStreamReader(in));
                    String strLine;
                    while ((strLine = bis.readLine()) != null)   {
                        output.append(strLine + "\n");
                        if(strLine.contains("import ")){
                            output.append("import javax.inject.Inject;\n");
                            output.append("import org.switchyard.component.bean.Reference;\n");
                            break;
                        }
                    }

                    while ((strLine = bis.readLine()) != null) {
                        output.append(strLine + "\n");

                        if(strLine.contains( "implements com.test." + serviceName + " {")){
                            output.append("\n");
                            output.append("    @Inject @Reference\n");
                            output.append("    public " + referenceServiceName + " referenceableService;\n");
                            break;
                        }
                    }
                    
                    while ((strLine = bis.readLine()) != null) {
                        output.append(strLine + "\n");
                    }

                    //Close the input stream
                    in.close();
                }catch (Exception e){//Catch exception if any
                    System.err.println("Error: " + e.getMessage());
                }
                
                serviceFile.setContents(output.toString());
            }
            
        }

    }

    protected void buildForgeProject(){
        //getShell().execute("build");
        String[] mvnCommand = new String[]{"package", "-e", "-Dmaven.test.skip=true"};
        getProject().getFacet(MavenCoreFacet.class).executeMaven(mvnCommand);
    }

    protected void resetOutputStream()  throws IOException {
        outputStream = new OutputStream()
        {
            private StringBuilder stringBuilder = new StringBuilder();
            @Override
            public void write(int b) throws IOException {
                this.stringBuilder.append((char) b );
            }
            public String toString(){
                return this.stringBuilder.toString();
            }
        };
        getShell().setOutputStream(outputStream);
    }

}
