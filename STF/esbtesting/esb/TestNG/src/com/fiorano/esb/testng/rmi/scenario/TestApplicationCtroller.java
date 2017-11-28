package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.ApplicationReference;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.aps.ApplicationStateDetails;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import java.io.File;
import java.util.*;
import java.util.logging.Level;

/**
* Created by IntelliJ IDEA.
* User: ranu
* Date: 10/19/11
* Time: 11:22 AM
* To change this template use File | Settings | File Templates.
*/
public class TestApplicationCtroller extends AbstractTestNG{
    private ObjectName objName;
    private FioranoApplicationController FAC;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String m_startInstance;
    private String m_endInstance;
    private float m_version2;
    private String m_appFile2;
    private String m_appFile3;

    public void init() {
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_version2 = Float.parseFloat(testProperties.getProperty("ApplicationVersion2"));
        m_appFile2 = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile2", "2.0.xml");
        m_appFile3 = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile3", "3.0.xml");
        m_startInstance = testProperties.getProperty("WorkFlowStartInstance");
        m_endInstance = testProperties.getProperty("WorkFlowEndInstance");
    }

    private void printInitParams() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    @BeforeClass(groups = "ApplicationCtrollerTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "ApplicationController" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc +"tests" +  fsc + "scenario" + fsc + "ApplicationController";
        FAC=rtlClient.getFioranoApplicationController();
        try {
              init();
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "ApplicationCtrollerTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testCreateApplication() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        System.out.println("The App File is::" + m_appFile);
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateApplication", "TestApplicationCtroller"));
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            String handleId = rtlClient.getHandleID();
            Object[] params = {application, handleId};
            String[] signatures = {Application.class.getName(), String.class.getName()};
            //jmxClient.invoke(objName, "saveApplication", params, signatures);
            FAC.saveApplication(application);

            if (FAC.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");

            startApplication(m_appGUID, m_version);
            Assert.assertTrue(FAC.isApplicationRunning(m_appGUID));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateApplication", "TestApplicationCtroller"));
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateApplication", "TestApplicationCtroller"), e);
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationCtrollerTest", alwaysRun = true, dependsOnMethods = "testCreateApplication")
    public void testGetAllApplicationRoutes(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetAllApplicationRoutes", "TestApplicationCtroller"));
                System.out.println("Started the Execution of the TestCase::" + getName());


//            List list = (List)(jmxClient.invoke(objName, "getRoutesOfApplication", params, signatures));
                List list=FAC.getRoutesOfApplication(m_appGUID,m_version);
                Assert.assertNotNull(list);
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetAllApplicationRoutes", "TestApplicationCtroller"));

            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetAllApplicationRoutes", "TestApplicationCtroller"), e);
                   Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }

    @Test(groups = "ApplicationCtrollerTest", alwaysRun = true, dependsOnMethods = "testGetAllApplicationRoutes")
    public void  testGetDeliverableMessageCount(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetDeliverableMessageCount", "TestApplicationCtroller"));
                System.out.println("xxxStarted the Execution of the TestCase::" + getName());
                Hashtable pendingMsgs=new Hashtable();
                pendingMsgs=FAC.getDeliverableMessageCount(m_appGUID,m_version);
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetDeliverableMessageCount", "TestApplicationCtroller"));
            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetDeliverableMessageCount", "TestApplicationCtroller"), e);
                   Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }

    @Test(groups = "ApplicationCtrollerTest", alwaysRun = true, dependsOnMethods = "testGetDeliverableMessageCount")
    public void testGetApplicationHeaders() {
            try {
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationHeaders", "TestApplicationCtroller"));
                ApplicationReference AR1;
                System.out.println("Started the Execution of the TestCase::"+getName());
                ApplicationReference[] refArray1 = FAC.getApplicationHeaders(m_appGUID);
                ApplicationStateDetails ASD;
                System.out.println("Finished fetching applicationheaders");
                Enumeration en1 = FAC.getHeadersOfRunningApplications();
                while(en1.hasMoreElements())
                {
                    AR1= (ApplicationReference)en1.nextElement();
                    Assert.assertTrue(FAC.isApplicationRunning(AR1.getGUID()));

                }
                Enumeration en2 = FAC.getHeadersOfSavedApplications();
                while(en2.hasMoreElements())
                {
                    AR1= (ApplicationReference)en2.nextElement();
                  FAC.getApplication(AR1.getGUID(),AR1.getVersion());      //if its present, it must be gettable, insome pieces at least
                }
                System.out.println("Finished fetching saved application headers");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetApplicationHeaders", "TestApplicationCtroller"));
            }
            catch (Exception ex) {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationHeaders", "TestApplicationCtroller"), ex);
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }
        }

    @Test(groups = "ApplicationCtrollerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationHeaders")
    public void testGetApplicationList(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationList", "TestApplicationCtroller"));
                System.out.println("Started the Execution of the TestCase::" + getName());
                String appName;
                Enumeration en1 =FAC.getListOfRunningApplications();
                while(en1.hasMoreElements())
                {
                    appName=en1.nextElement().toString();

                    String appnew=appName.substring(0,appName.length()-5);
                    Assert.assertTrue(FAC.isApplicationRunning(appnew,m_version));

                    String[] appNameAndVersion = appName.split("__");
                    String version = appNameAndVersion[1];
                    float verssion=Float.parseFloat(version);
                    Assert.assertTrue(FAC.isApplicationRunning(appnew,verssion));

                }
                System.out.println("Finished fetching list of running application");
                Enumeration en2 =FAC.getListOfSavedApplications();
                while(en2.hasMoreElements())
                {
                    appName=en2.nextElement().toString();
                    String[] appNameAndVersion = appName.split("__");
                    String appnew = appNameAndVersion[0];
                    String version = appNameAndVersion[1];
                    float verssion=Float.parseFloat(version);
                    FAC.getApplication(appnew,verssion);
                }
                System.out.println("Finished fetching list of saved application");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetApplicationList", "TestApplicationCtroller"));
            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationList", "TestApplicationCtroller"), e);
                   Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }

    @Test(groups = "ApplicationCtrollerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationList")
    public void testGetApplicationsStateDetails(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationsStateDetails", "TestApplicationCtroller"));
                System.out.println("Started the Execution of the TestCase::" + getName());

                Object[] params = {m_appGUID};
                String[] signatures = {String.class.getName()};

                ApplicationStateDetails asd1 =(ApplicationStateDetails)FAC.getCurrentStateOfApplication(m_appGUID,m_version);

                    Assert.assertEquals(m_appGUID,asd1.getAppGUID());
                    Enumeration list=asd1.getAllServiceNames();
                    ApplicationStateDetails asd2 =FAC.getCurrentStateOfApplicationFromFPSs(m_appGUID,m_version);
                    Assert.assertTrue(asd2.getAppGUID().equalsIgnoreCase(m_appGUID));
                    Enumeration list2=asd2.getAllServiceNames();
                    ArrayList<String> aList=new ArrayList<String>();
                     while(list.hasMoreElements())
                    {
                        aList.add((String)list.nextElement());
                    }
                    while(list2.hasMoreElements())
                    {
                        Assert.assertTrue(aList.contains(list2.nextElement()));
                    }
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetApplicationsStateDetails", "TestApplicationCtroller"));

           }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationsStateDetails", "TestApplicationCtroller"), e);
                   Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }

    @Test(groups = "ApplicationCtrollerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationsStateDetails")
    public void testGetServiceInstanceStateDetails(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetServiceInstanceStateDetails", "TestApplicationCtroller"));
                System.out.println("Started the Execution of the TestCase::"+getName());

                Application application = ApplicationParser.readApplication(new File(m_appFile));
                ServiceInstance startService = application.getServiceInstance(m_startInstance);
                String srvName = startService.getName();

                ServiceInstanceStateDetails sisd1 = FAC.getCurrentStateOfService(m_appGUID,m_version,m_startInstance);
                Assert.assertTrue(sisd1.getServiceInstanceName().equalsIgnoreCase(srvName));
                System.out.println("Finished fetching current state of service");
                ServiceInstanceStateDetails sisd2 = FAC.getCurrentStateOfServiceFromFPSs(m_appGUID,m_version,m_startInstance);
                Assert.assertTrue(sisd2.getServiceInstanceName().equalsIgnoreCase(srvName));
                System.out.println("Finished fetching current state of service from fps");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetServiceInstanceStateDetails", "TestApplicationCtroller"));
            }
            catch (Exception ex) {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServiceInstanceStateDetails", "TestApplicationCtroller"), ex);
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }
        }

    @Test(groups = "ApplicationCtrollerTest", alwaysRun = true, dependsOnMethods = "testGetServiceInstanceStateDetails")
    public void testStopAllServices(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testStopAllServices", "TestApplicationCtroller"));
                System.out.println("Started the Execution of the TestCase::" + getName());
                  if(!(((FAC.getCurrentStateOfService(m_appGUID,m_version,m_startInstance))).getKillTime() < new Date().getTime()))
                    throw new Exception("Stop All Services Failed");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAllServices", "TestApplicationCtroller"));
            }catch(Exception ex){
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopAllServices", "TestApplicationCtroller"), ex);
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }
        }

    @Test(groups = "ApplicationCtrollerTest", alwaysRun = true, dependsOnMethods = "testStopAllServices")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestApplicationCtroller"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (FAC.isApplicationRunning(m_appGUID))
                FAC.killApplication(m_appGUID,m_version);
            FAC.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestApplicationCtroller"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestApplicationCtroller"), ex);
            Assert.assertTrue(
                    false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @AfterClass(groups = "ApplicationCtrollerTest", alwaysRun = true)
    public void testCleanup() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCleanup", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (FAC.isApplicationRunning(m_appGUID))
                FAC.killApplication(m_appGUID,m_version);

            FAC.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanup", "ApplicationControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanup", "ApplicationControllerTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    private void startApplication(String appGUID, float version) {
        try {
            String handleId = rtlClient.getHandleID();

            Object params[] = {appGUID, new Float(version), handleId};
            String signatures[] = {String.class.getName(), float.class.getName(), String.class.getName()};

            FAC.prepareLaunch(appGUID, version);
            FAC.launchApplication(appGUID, version);

            FAC.startAllServices(appGUID,m_version);

            /* wait for 20 sec so that the application gets started */
            Thread.sleep(20000);
        }
        catch (Exception e) {
            if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1)
                //never mind if the application is already running.
                return;
        }
    }
}
