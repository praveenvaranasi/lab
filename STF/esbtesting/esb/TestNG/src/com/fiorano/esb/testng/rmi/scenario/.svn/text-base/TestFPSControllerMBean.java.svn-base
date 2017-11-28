package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.remote.JMXClient;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.aps.ApplicationStateDetails;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.File;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/20/11
 * Time: 10:45 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestFPSControllerMBean extends AbstractTestNG{
    private ObjectName objName;
    private ObjectName objName2;

    private FioranoApplicationController m_fioranoApplicationController;

    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;

    private String m_appFile;
    private String m_servinstance1;
    private String m_servinstance2;

    private String FPSPEER = "fps";

    public void init() {
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile");
        m_servinstance1 = testProperties.getProperty("ServiceInstance1");
        m_servinstance2 = testProperties.getProperty("ServiceInstance2");
    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Service Instances::      " + m_servinstance1 + " and " + m_servinstance2);
        System.out.println("..................................................................");
    }

    @BeforeClass(groups = "FPSControllerMBeanTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());

        initializeProperties("scenario" + fsc + "FPSControllerMBean" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc +"tests" +  fsc + "scenario" + fsc + "FPSControllerMBean";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        init();

        try {
            objName = new ObjectName("Fiorano.Esb.Fps.Controller:ServiceType=FPSController,Name=FPSController");
            objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            JMXClient.connect("admin", "passwd");
        }
        catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        printInitParams();
    }

    @Test(groups = "FPSControllerMBeanTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testStartApplication() throws Exception {
            Logger.LogMethodOrder(Logger.getOrderMessage("testStartApplication", "TestFPSControllerMBean"));
        try {
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);
            m_fioranoApplicationController.launchApplication(m_appGUID, m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
            Thread.sleep(5000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStartApplication", "TestFPSControllerMBean"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartApplication", "TestFPSControllerMBean"));
        }
    }

    @Test(groups = "FPSControllerMBeanTest", alwaysRun = true, dependsOnMethods = "testStartApplication")
    public void testGetFPSQueueInformation() throws Exception {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetFPSQueueInformation", "TestFPSControllerMBean"));
        try {
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            boolean value = application.getServiceInstance(m_servinstance1).getPortInstance("IN_PORT").isEnabled();
            Assert.assertTrue(value);
            System.out.println("The Queue instance of " + m_appGUID + " " + m_servinstance1 + " exists.. ");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetFPSQueueInformation", "TestFPSControllerMBean"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSQueueInformation", "TestFPSControllerMBean"));
        }
    }

    @Test(groups = "FPSControllerMBeanTest", alwaysRun = true, dependsOnMethods = "testGetFPSQueueInformation")
    public void testGetFPSQueues() throws Exception {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetFPSQueues", "TestFPSControllerMBean"));
        try {
            Object[] params = {FPSPEER};
            String[] signatures = {String.class.getName()};
            List list = (List) jmxClient.invoke(objName, "getFPSQueues", params, signatures);

            Assert.assertTrue(list.size() > 0);
            Iterator iter = list.iterator();

            System.out.println("Printing the FPS Queues list...");
            System.out.println("..............................................................");
            while (iter.hasNext()) {
                System.out.println(iter.next());
            }
            System.out.println("..............................................................");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetFPSQueues", "TestFPSControllerMBean"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSQueues", "TestFPSControllerMBean"));
        }
    }

    @Test(groups = "FPSControllerMBeanTest", alwaysRun = true, dependsOnMethods = "testGetFPSQueues")
    public void testGetFPSTopicInformation() throws Exception {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetFPSTopicInformation", "TestFPSControllerMBean"));
        try {
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            boolean value = application.getServiceInstance(m_servinstance1).getPortInstance("OUT_PORT").isEnabled();
            Assert.assertTrue(value);
            System.out.println("The Topic instance of " + m_appGUID + " " + m_servinstance1 + " exists.. ");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetFPSTopicInformation", "TestFPSControllerMBean"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSTopicInformation", "TestFPSControllerMBean"));
        }
    }

    @Test(groups = "FPSControllerMBeanTest", alwaysRun = true, dependsOnMethods = "testGetFPSTopicInformation")
    public void testGetFPSTopics() throws Exception {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetFPSTopics", "TestFPSControllerMBean"));
        try {
            Object[] params = {FPSPEER};
            String[] signatures = {String.class.getName()};

            List list = (List) jmxClient.invoke(objName, "getFPSTopics", params, signatures);
            Assert.assertTrue(list.size() > 0);
            Iterator iter = list.iterator();

            System.out.println("Printing the FPS Topics list...");
            System.out.println("..............................................................");
            while (iter.hasNext()) {
                System.out.println(iter.next());
            }
            System.out.println("..............................................................");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetFPSTopics", "TestFPSControllerMBean"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSTopics", "TestFPSControllerMBean"));
        }


    }

    @Test(groups = "FPSControllerMBeanTest", alwaysRun = true, dependsOnMethods = "testGetFPSTopics")
    public void testStopAllServices() {
            Logger.LogMethodOrder(Logger.getOrderMessage("testStopAllServices", "TestFPSControllerMBean"));
        try {
            ApplicationStateDetails asd = m_fioranoApplicationController.stopAllServices(m_appGUID,m_version);
            if (!(m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_servinstance1).getKillTime() < new Date().getTime()))
                throw new Exception("Stop All Services Failed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAllServices", "TestFPSControllerMBean"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopAllServices", "TestFPSControllerMBean"));
        }
    }

    @Test(groups = "FPSControllerMBeanTest", alwaysRun = true, dependsOnMethods = "testStopAllServices")
    public void testCleanUp() {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCleanUp", "TestFPSControllerMBean"));
        try {
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            m_fioranoApplicationController.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanUp", "TestFPSControllerMBean"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanUp", "TestFPSControllerMBean"));
        }
    }

    /*private boolean isApplicationRunningUtil(String appGUID) throws Exception {
        Object[] params = {appGUID};
        String[] signatures = {String.class.getName()};
        Boolean appStatus = (Boolean) jmxClient.invoke(objName2, "isRunningApplication", params, signatures);

        return appStatus.booleanValue();
    }*/
}
