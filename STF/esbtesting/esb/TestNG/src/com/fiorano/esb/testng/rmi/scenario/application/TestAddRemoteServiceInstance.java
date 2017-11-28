package com.fiorano.esb.testng.rmi.scenario.application;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.RemoteServiceInstance;
import fiorano.tifosi.dmi.application.Route;
import fiorano.tifosi.dmi.events.ApplicationEvent;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/13/11
 * Time: 11:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestAddRemoteServiceInstance extends AbstractTestNG{
    private String resourceFilePath;
    private boolean m_initialised;
    private String m_appGUID1;
    private String m_appGUID;
    private float m_version;
    private float m_version1;
    private String m_appFile;
    private String m_appFile1;
    private FioranoApplicationController m_fioranoApplicationController;
    private float appVersion = 1.0f;

    public void init() throws Exception {
        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + getProperty("ApplicationFile");
        m_appGUID1 = getProperty("ApplicationGUID1");
        m_version1 = Float.parseFloat(getProperty("ApplicationVersion1"));
        m_appFile1 = resourceFilePath + File.separator + getProperty("ApplicationFile1");
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Application GUID:: " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("Application GUID1:: " + m_appGUID1 + "  Version Number1::" + m_version1);
        System.out.println("Application File1" + m_appFile1);
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "AddRemoteServiceInstanceTest", alwaysRun = true)
    public void startSetUp()  throws Exception {
        if (!m_initialised) {
            initializeProperties("scenario"+ fsc +"Application" + fsc + "AddRemoteServiceInstance" + fsc + "tests.properties");
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Application" + fsc + "AddRemoteServiceInstance";
            init();
            m_initialised = true;
        }
        printInitParams();
    }
                               
    @Test(groups = "AddRemoteServiceInstanceTest", alwaysRun = true, dependsOnMethods = "startSetUp", description = "Imports the application.")
    public void testImportApplication(){
        try
        {
            System.out.println("Started the Execution of the TestCase::"+getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            Application application1 = ApplicationParser.readApplication(new File(m_appFile1));

            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.saveApplication(application1);
            if(m_fioranoApplicationController.getApplication(m_appGUID,m_version)==null)//remember//
             {
             System.out.println("i am here");
             throw new Exception("Application with GUID::"+m_appGUID+", version::"+m_version+" not created");
              }
            if(m_fioranoApplicationController.getApplication(m_appGUID1,m_version1)==null)//remember//
             {
             System.out.println("i am here");
             throw new Exception("Application with GUID::"+m_appGUID1+", version::"+m_version1+" not created");
              }
        }
        catch(Exception ex)
        {
            System.out.println("Exception message::"+ex.getMessage());
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }
    
    @Test(groups = "AddRemoteServiceInstanceTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Resource and connectivity check is done.")
    public void testCRC(){
            System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("The Application with GUID "+m_appGUID+" is already running.");
            m_fioranoApplicationController.compileApplication(m_appGUID,m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID,m_version);

            //post condition
            //todo complete this

            Assert.assertTrue(true);


        }
        catch(Exception ex)
        {
            System.out.println("Exception message::"+ex.getMessage());
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
}
    
    @Test(groups = "AddRemoteServiceInstanceTest", alwaysRun = true, dependsOnMethods = "testCRC", description = "Adds a remote service instance")
    public void testAddRemoteServiceInstance(){
        try{
            System.out.println("Started the Execution of the TestCase::"+getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            System.out.println("Got the application");
            RemoteServiceInstance remote=application.getRemoteServiceInstance("FEEDERDISPLAY");
            //System.out.println("Got the instance" + remote.getApplicationGUID());
            Enumeration enumeration = rtlClient.getFioranoEventsManager().getAllApplicationEvents("FEEDERDISPLAY",String.valueOf(appVersion),"Feeder1",-1,0,50);
            ApplicationEvent event=new ApplicationEvent();

            event=(ApplicationEvent)enumeration.nextElement();
            System.out.println("Application event name is ::"+event.getApplicationName());
                 //String guid=event.getApplicationGUID();
            //Float version=Float.parseFloat(event.getApplicationVersion());
            System.out.println("Application event guid is::"+event.getApplicationGUID());

                 remote=new RemoteServiceInstance();
            System.out.println("created instance");
                 remote.setApplicationGUID(event.getApplicationGUID());
            System.out.println("set guid");
            Float version=(float)4.0;
                 remote.setApplicationVersion(version);
            System.out.println("set version");
                 remote.setRemoteName("Display1");
                 remote.setName("Display2");
            System.out.println("set values");
                 application.addRemoteServiceInstance(remote);
            System.out.println("added remote service instance");
                  Route route=new Route();
            System.out.println("created new route");
                 route.setName("route3");
            System.out.println("set name");
                 route.setSourceServiceInstance("SMTP1");
            System.out.println("set source instance");
                 route.setTargetServiceInstance("Display2");
            System.out.println("set target instance");
                 application.addRoute(route);
            System.out.println("added route to application");
                 //m_fioranoApplicationController.synchronizeApplicationToRunningVersion("CHANGELOGMODULE");
                 //application.AddRemoteServiceInstance();



        }
        catch(Exception ex){
            System.out.println("Exception message::"+ex.getMessage());
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }
    
    @Test(groups = "AddRemoteServiceInstanceTest", alwaysRun = true, dependsOnMethods = "testAddRemoteServiceInstance", description = "Runs the application.")
    public void testRunApplication(){
        try
        {
            System.out.println("Started the Execution of the TestCase::"+getName());
            startApplication(m_appGUID,m_version);
            startApplication("FEEDERDISPLAY",(float)1.0);
            if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version))
                throw new Exception("The Application is not started.");

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }
    
    @Test(groups = "AddRemoteServiceInstanceTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "test the App")
    public void testApp(){
        try{
            System.out.println("Started the Execution of the TestCase::"+getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            Iterator ir=application.getRemoteServiceInstances().iterator();
            System.out.println(1);
            while(ir.hasNext())
            {

                System.out.println(2);
                String str1=ir.next().toString();
                System.out.println("Remote instance name::"+str1);
            }
            System.out.println(3);
            Iterator ir1=application.getRoutes().iterator();
            while(ir1.hasNext()){
                System.out.println(4);
                String str=ir1.next().toString();
                System.out.println("route name::"+str);
            }
        }
        catch(Exception ex){
            System.out.println("Exception message::"+ex.getMessage());
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }
        
    @Test(groups = "AddRemoteServiceInstanceTest", alwaysRun = true, dependsOnMethods = "testApp", description = "stops a running application.")
    public void testKillApplication() {
        try
        {
            System.out.println("Started the Execution of the TestCase::"+getName());
            stopApplication(m_appGUID);
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            stopApplication("FEEDERDISPLAY");
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }
     
    @Test(groups = "AddRemoteServiceInstanceTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the application which is stopped.")
    public void testDeleteApplication() {
        try{
            System.out.println("Started the Execution of the TestCase::"+getName());
            stopApplication(m_appGUID);
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            m_fioranoApplicationController.deleteApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication("FEEDERDISPLAY",1.0f);
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException{
        try
        {
            m_fioranoApplicationController.prepareLaunch(appGuid,appVersion);
            m_fioranoApplicationController.launchApplication(appGuid,appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);
        }
        catch(TifosiException e){
            if(e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::")!=-1 && e.getMessage().indexOf("version of this application is already running")!=-1)
            {
                //never mind if the application is already running.
                return;
            }
            System.err.println("Error launching the application");
            e.printStackTrace();
            throw new FioranoException("Error launching the application",e);
        }
    }
    
    private void stopApplication(String appGUID){
        try
        {
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);
        } catch(TifosiException e){
            //ignore.
        }
    }
}
