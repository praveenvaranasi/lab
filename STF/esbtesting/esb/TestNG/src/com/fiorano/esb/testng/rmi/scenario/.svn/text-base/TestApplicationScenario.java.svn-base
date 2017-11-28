package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationContext;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.service.Execution;
import com.fiorano.xml.ClarkName;
import fiorano.tifosi.dmi.service.Schema;
import fiorano.tifosi.events.EventStateConstants;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/19/11
 * Time: 12:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestApplicationScenario extends AbstractTestNG{
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised =false;
    private String m_appGUID;
    private float m_version;
    private String m_applicationContextFile;
    private String m_routeElement;
    private String resourceFilePath;
    private String m_instance;
    private String m_routeElementNamespace;

    String m_appGUID2;
    String m_appFile2;
    float m_version2;
    String m_serviceInstance2;

    static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter

        //changed function
    private void init() throws Exception{
        if(m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_applicationContextFile = resourceFilePath+ fsc+testProperties.getProperty("ApplicationContext");
        m_routeElement = testProperties.getProperty("RouteElement");
        m_routeElementNamespace  = testProperties.getProperty("RouteElementNamespace");
        m_instance = resourceFilePath+fsc+testProperties.getProperty("ApplicationContextValue");

        m_appGUID2 = testProperties.getProperty("ApplicationGUID2");
        m_appFile2 = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile2");
        m_version2 = Float.parseFloat(testProperties.getProperty("ApplicationVersion2"));
        m_serviceInstance2 = testProperties.getProperty("ServiceToBeChanged");
        m_initialised = true;
    }

    private void printInitParams(){
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       "+m_appGUID+"  Version Number::"+m_version);
        System.out.println("_____________________________________________________________________________");
   }

    @BeforeClass(groups = "ApplicationScenarioTest", alwaysRun = true)
    public void startSetUp(){
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "TestApplicationScenario"));
        System.out.println("Started the Execution of the TestCase::"+getName());
        initializeProperties("scenario" + fsc + "ApplicationScenario" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "ApplicationScenario";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();

        ServerStatusController stc;
        stc=ServerStatusController .getInstance();
        boolean isFPSHA=stc.getFPSHA();
        try {

               setXmlFilter("xml");
            if(isFPSHA && !isModifiedOnceHA)
           {
               isModifiedOnceHA=true;

               modifyXmlFiles(resourceFilePath,"fps","fps_ha");

           }
           else if(!isFPSHA && !isModifiedOnce)
           {
               isModifiedOnce=true;
               modifyXmlFiles(resourceFilePath,"fps_ha","fps");

           }
           init();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "ApplicationScenarioTest", alwaysRun = true,dependsOnMethods = "startSetUp", description = "Creates an application")
    public void testCreateApplication() {
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateApplication", "TestApplicationScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Application application = new Application();//m_fioranoApplicationController.getApplication(BASE_APPLICATION,1.0f);
            application.setServiceInstances(new ArrayList());
            application.setRoutes(new ArrayList());
            application.setGUID(m_appGUID);
            application.setDisplayName(m_appGUID);
            application.setVersion(m_version);
            ArrayList al = new ArrayList();
            al.add("CoreScenarioTests");
            application.setCategories(al);
            application.setCreationDate(new Date());
            ApplicationContext appContext = getAppContext();
            setDefaultInstance(appContext);
            application.setApplicationContext(appContext);
            m_fioranoApplicationController.saveApplication(application);
   //         System.out.println("Creating the application with version no::"+m_fioranoApplicationController.createApplication(application,m_version));
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }

    @Test(groups = "ApplicationScenarioTest", alwaysRun = true,dependsOnMethods = "testCreateApplication")
    public void testCheckApplicationContextValues(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testCheckApplicationContextValues", "TestApplicationScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            System.out.println("The Application Is::\n"+m_fioranoApplicationController.getApplication(m_appGUID,m_version));
            System.out.println("AND The APP Context is \n"+m_fioranoApplicationController.getApplication(m_appGUID,m_version).getApplicationContext().getContent());
            System.out.println("AND The APP Context Default instance  is  \n" + m_fioranoApplicationController.getApplication(m_appGUID, m_version).getApplicationContext().getValue());
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }

    @Test(groups = "ApplicationScenarioTest", alwaysRun = true,dependsOnMethods = "testCheckApplicationContextValues", description = "written for the sake of bug 11788 STARTS")
    public void testChangeLaunchType(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testChangeLaunchType", "TestApplicationScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            /* create the application with a file reader and a display */
            Application application = ApplicationParser.readApplication(new File(m_appFile2));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID2, m_version2) == null)
                throw new Exception("Application with GUID::" + m_appGUID2 + ", version::" + m_version2 + " not created");

            /* launch the application */
            startApplication(m_appGUID2,m_version2);

            /* kill the service and change the launch type */
            application = m_fioranoApplicationController.getApplication(m_appGUID2, m_version2);
            m_fioranoApplicationController.killService(m_appGUID2,m_version,m_serviceInstance2);
            application.getServiceInstance(m_serviceInstance2).setLaunchType(Execution.LAUNCH_TYPE_IN_MEMORY);
            m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID2,m_version);
            m_fioranoApplicationController.startService(m_appGUID2,m_version, m_serviceInstance2);

            /* kill the service and changed the launch type */
            application = m_fioranoApplicationController.getApplication(m_appGUID2, m_version2);
            m_fioranoApplicationController.killService(m_appGUID2,m_version,m_serviceInstance2);
            application.getServiceInstance(m_serviceInstance2).setLaunchType(Execution.LAUNCH_TYPE_SEPARATE_PROCESS);
            m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID2,m_version);
            m_fioranoApplicationController.startService(m_appGUID2,m_version, m_serviceInstance2);

            /* kill the application */
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID2))
                m_fioranoApplicationController.killApplication(m_appGUID2,m_version);
            /*delete the application in next test */
            //m_fioranoApplicationController.deleteApplication(m_appGUID2,m_version2);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testChangeLaunchType", "TestApplicationScenario"));
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangeLaunchType", "TestApplicationScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

            /*kill the application if it wasn't killed previously due to some exceptions */
            try{
                if(m_fioranoApplicationController.isApplicationRunning(m_appGUID2))
                    m_fioranoApplicationController.killApplication(m_appGUID2,m_version);
                m_fioranoApplicationController.deleteApplication(m_appGUID2,m_version2);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Test(groups = "ApplicationScenarioTest", alwaysRun = true,dependsOnMethods = "testChangeLaunchType", description = "written for the sake of bug 12397 STARTS")
    public void testLaunchTypeNone(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchTypeNone", "TestApplicationScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            /* application was already created in the previous test */
            /* change the application's launch type to none */
            Application application = m_fioranoApplicationController.getApplication(m_appGUID2, m_version2);
            application.getServiceInstance(m_serviceInstance2).setLaunchType(Execution.LAUNCH_TYPE_NONE);

            /* launch the application and check whether the component in running. if yes, raise exception so that the testcase fails */
            startApplication(m_appGUID2, m_version2);
            String componentStatus = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID2,m_version,m_serviceInstance2).getStatusString();
            Assert.assertNotNull(componentStatus);
            Assert.assertFalse(componentStatus.equals(EventStateConstants.SERVICE_HANDLE_BOUND));

            /*kill and delete the application */
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID2))
                m_fioranoApplicationController.killApplication(m_appGUID2,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID2,m_version2);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchTypeNone", "TestApplicationScenario"));
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchTypeNone", "TestApplicationScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

            /*kill and delete the application if it wasn't deleted previously due to some exceptions */
            try{
                if(m_fioranoApplicationController.isApplicationRunning(m_appGUID2))
                    m_fioranoApplicationController.killApplication(m_appGUID2,m_version);
                m_fioranoApplicationController.deleteApplication(m_appGUID2,m_version2);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    @Test(groups = "ApplicationScenarioTest", alwaysRun = true,dependsOnMethods = "testLaunchTypeNone", description = "Deletes the Application")
    public void testDeleteApplication(){
        try{
            System.out.println("Started the Execution of the TestCase::"+getName());
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID,m_version);
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,m_version);
        } catch (TifosiException e) {
            if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1) {
                //never mind if the application is already running.
                return;
            }
            System.err.println("Error launching the application");
            e.printStackTrace();
            throw new FioranoException("Error launching the application");
        }
    }
    private ApplicationContext getAppContext() {
        ApplicationContext appContext = new ApplicationContext();
        String structure;
        StringBuffer buffer = new StringBuffer("");
        FileReader reader=null;
        BufferedReader bufferedReader=null;
        try{
            reader = new FileReader(m_applicationContextFile);
            bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            structure = "";
            while(line!=null){
                buffer.append(line);
                line = bufferedReader.readLine();
            }
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("Unable To Read the message from the file. Sending the default message::testMessage");
            structure = "testMessage";
        }
        finally{
            try{
                bufferedReader.close();
                reader.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        structure = buffer.toString();
        appContext.setContent(structure);
        appContext.setRootElement(ClarkName.toClarkName(m_routeElementNamespace, m_routeElement));
        appContext.setType(Schema.TYPE_XSD);
        return appContext;
    }
    private void setDefaultInstance(ApplicationContext appContext) {
        StringBuffer appContextValue = new StringBuffer("");
        FileReader valueReader=null;
        BufferedReader valueBufferedReader=null;
        try{
            valueReader = new FileReader(m_instance);
            valueBufferedReader = new BufferedReader(valueReader);
            String value = valueBufferedReader.readLine();
            while(value!=null){
                appContextValue.append(value);
                value = valueBufferedReader.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                valueBufferedReader.close();
                valueReader.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        System.out.println("The Default Instance being set is::"+appContextValue.toString());
        appContext.setValue(appContextValue.toString());
    }

    //picking up the xml extension files
    private void setXmlFilter(String ext){
               xmlFilter=new OnlyExt(ext);
           }

    //private static ModifyXML m_modifyFiles: the function modifies the xml files and replace any "search" string with "replace" string
    private void modifyXmlFiles(String path,String search,String replace) throws IOException {
            File fl=new File(path);
            File[]filearr=fl.listFiles(xmlFilter);
            FileReader fr=null;
            FileWriter fw=null;
           boolean change=false;
            BufferedReader br;
            String s;
            String result="";


            int i=0;
            while(i<filearr.length-1)
            {
                try{
                    fr=new FileReader(filearr[i]) ;
                } catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                try{
                    fw=new FileWriter("temp.xml");
                } catch(IOException e){
                    e.printStackTrace();
                }
                br=null;
                br=new BufferedReader(fr);


             while((s=br.readLine())!=null)
             {

                 int j=s.indexOf(search);
                 if(j!=-1)
                 {
                     change=true;
                     int k=search.length()+j;
                     //System.out.println("The Index start is "+j+" and index last is "+ k);
                     result=s.substring(0,j);
                     result=result+replace;
                     result=result+ s.substring(k);
                     s=result;

                  }
                    //System.out.print(s);

                    fw.write(s);
                   fw.write('\n');
            }
                fr.close();
                fw.close();

                if(change)
                {
                    fr=new FileReader("temp.xml");
                    fw=new FileWriter(filearr[i]);
                    br=new BufferedReader(fr);
                    while((s=br.readLine())!=null)
                    {
                        fw.write(s);
                        fw.write('\n');
                    }
                    fr.close();
                    fw.close();
                    change=false;
                }

                i=i+1;

            }
        }
}
