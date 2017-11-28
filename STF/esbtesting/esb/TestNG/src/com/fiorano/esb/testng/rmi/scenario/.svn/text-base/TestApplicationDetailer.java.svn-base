package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.application.controller.ApplicationControllerMBean;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.remote.JMXClient;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/19/11
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestApplicationDetailer extends AbstractTestNG{
    private ObjectName objName;
    private ObjectName objName2;

    private FioranoApplicationController m_applicationcontroller;

    private boolean isReady = false;
    private String resourceFilePath;

    private String m_appGUID;
    private String m_routeGUID;
    private float m_version;

    private String m_appFile;
    private String m_appFile1;
    private String m_appFile2;
    private String m_servinst1;

    private String m_servinst2;
    private String m_servinst3;

    private String m_appGUID1;
    private String m_appGUID2;
    private String m_appGUID3;
    private String m_appGUID4;
    private String m_appGUID5;
    private String handleId;

    private void init() {
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_appGUID1 = testProperties.getProperty("ApplicationGuid1");
        m_appGUID2 = testProperties.getProperty("ApplicationGuid2");
        m_appGUID3 = testProperties.getProperty("ApplicationGuid3");
        m_appGUID4=m_appGUID+"__"+"1.0";
        m_appGUID5=m_appGUID2+"__"+"1.0";

        m_routeGUID = testProperties.getProperty("RouteGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));

        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile");
        m_appFile1 = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile1");
        m_appFile2 = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile2");

        m_servinst1 = testProperties.getProperty("InstanceName1");
        m_servinst2 = testProperties.getProperty("InstanceName2");
        m_servinst3 = testProperties.getProperty("InstanceName3");
    }

    private void printInitParams() {

        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    @BeforeClass(groups = "ApplicationDetailerTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        if (isReady == true) return;
        initializeProperties("scenario" + fsc + "ApplicationDetailerTest" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "ApplicationDetailerTest";
        m_applicationcontroller=rtlClient.getFioranoApplicationController();
        try {
            objName = new ObjectName("Fiorano.Esb.Webconsole.Controller:ServiceType=WebconsoleController,Name=WebconsoleController");
            objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            isReady = true;
            JMXClient.connect("admin", "passwd");
            init();
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testGetApplicationDetails() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            JMXClient client =JMXClient.getInstance();
            handleId = client.getHandleId();
            ArrayList appDataList = (ArrayList) jmxClient.invoke(objName, "getAppInfo", new Object[]{handleId}, new String[]{String.class.getName()});
            Assert.assertNotNull(appDataList);
        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationDetails")
    public void testGetAllApplicationNames() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            ArrayList appNames = (ArrayList) jmxClient.invoke(objName, "getAllApplicationNames", new Object[]{handleId}, new String[]{String.class.getName()});
            Assert.assertNotNull(appNames);
        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetAllApplicationNames")
    public void testGetAllServiceInstanceNames() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            Object[] params = {m_appGUID,m_version,handleId};
            String[] signatures = new String[]{String.class.getName(),float.class.getName(),String.class.getName()};
            ArrayList servInstNames = (ArrayList) jmxClient.invoke(objName, "getAllServiceInstanceNames", params, signatures);
            Assert.assertNotNull("servInstNames");
        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetAllServiceInstanceNames")
    public void testLaunchApplication() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        saveApplication();
        String appName = m_appGUID;
        float version = m_version;
        String handleID = getHandleID();

        try {
            Object[] params = {appName, version, handleId};
            String[] signatures = {String.class.getName(), float.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "launchAndStartAllServices", params, signatures);

        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }

    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testLaunchApplication")
    public void testGetDebugRoutes() throws Exception {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appName = m_appGUID2;
        float version = m_version;
        try {
            Application application = ApplicationParser.readApplication(new File(m_appFile1));
            m_applicationcontroller.saveApplication(application);
            m_applicationcontroller.compileApplication(appName, version);
            m_applicationcontroller.prepareLaunch(appName, version);
            m_applicationcontroller.launchApplication(appName, version);
            m_applicationcontroller.startAllServices(appName,version);

            Object[] params = {appName, version, handleId};
            String[] signatures = {String.class.getName(), float.class.getName(), String.class.getName()};

            Vector vec = (Vector) jmxClient.invoke(objName2, "getDebugRoutes", params, signatures);
            Assert.assertNotNull(vec);


        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
        catch (TifosiException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }

    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetDebugRoutes")
    public void testGetSBWPortsForApp() {
        System.out.println("Started the Execution of the TestCase::" + getName());

        String appName = m_appGUID2;
        float version = m_version;
        try {

            Object[] params = {appName, version, handleId};
            String[] signatures = {String.class.getName(), float.class.getName(), String.class.getName()};

            HashMap SBWPorts = (HashMap) jmxClient.invoke(objName2, "getSBWPortsForApp", params, signatures);
            Assert.assertNotNull(SBWPorts);

        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetSBWPortsForApp")
    public void testGetUserDefinedDestinationPortsForApp() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appName = m_appGUID2;
        float version = m_version;
        try {

            Object[] params = {appName, version, handleId};
            String[] signatures = {String.class.getName(), float.class.getName(), String.class.getName()};

            HashMap DestPorts = (HashMap) jmxClient.invoke(objName2, "getUserDefinedDestinationPortsForApp", params, signatures);
            Assert.assertNotNull(DestPorts);

        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetUserDefinedDestinationPortsForApp")
    public void testGetRunningComponents() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String FPS = "fps";

        try {

            Object[] params = {FPS};
            String[] signatures = {String.class.getName()};

            HashMap runcomps = (HashMap) jmxClient.invoke(objName2, "getRunningComponents", params, signatures);
            Assert.assertNotNull(runcomps);

        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetRunningComponents")
    public void testRestartApplication() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appName = m_appGUID;
        try {
            Object[] params = {appName,m_version, handleId};
            String[] signatures = {String.class.getName(),float.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "restartEventProcess", params, signatures);
            Thread.sleep(10000);
        }

        catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InterruptedException e) {
            ;//ignore
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testRestartApplication")
    public void testGetLastOutTrace() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        int nol = 4;
        String sername = m_servinst2;
        String appGUID = m_appGUID;
        float appver = m_version;

        try {
            Object[] params = {nol, sername, appGUID, appver, handleId};
            String[] signatures = {int.class.getName(), String.class.getName(), String.class.getName(), float.class.getName(), String.class.getName()};
            String result = (String) jmxClient.invoke(objName2, "getLastOutTrace", params, signatures);
            Assert.assertNotNull(result);
        }
        catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetLastOutTrace")
    public void testGetLastErrorTrace() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        int nol = 8;
        String sername = m_servinst2;
        String appGUID = m_appGUID;
        float appver = m_version;
        try {
            Object[] params = {nol, sername, appGUID, appver, handleId};
            String[] signatures = {int.class.getName(), String.class.getName(), String.class.getName(), float.class.getName(), String.class.getName()};
            String result = (String) jmxClient.invoke(objName2, "getLastErrorTrace", params, signatures);
            Assert.assertNotNull(result);
        }

        catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetLastErrorTrace")
    public void testStopService() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appName = m_appGUID;
        String serinstname = m_servinst1;
        try {
            Object[] params = {appName,m_version, serinstname, handleId};
            String[] signatures = {String.class.getName(),float.class.getName(), String.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "stopService", params, signatures);
            Thread.sleep(3000);
        }
        catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InterruptedException e) {
            ;//ignore
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testStopService")
    public void testLaunchService() {
        System.out.println("Started the execution of the test case::" + getName());
        String appName = m_appGUID;
        String serinstname = m_servinst1;
        String handleID = getHandleID();
        try {
            Object[] params = {appName,m_version,serinstname, handleId};
            String[] signatures = {String.class.getName(),float.class.getName(),String.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "startService", params, signatures);
            Thread.sleep(3000);
        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InterruptedException e) {
            ;//ignore
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testLaunchService")
    public void testClearServiceOutLogs() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appName = m_appGUID;
        String serinstname = m_servinst1;
        float version = m_version;

        try {
            Object[] params = {serinstname, appName, version, handleId};
            String[] signatures = {String.class.getName(), String.class.getName(), float.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "clearServiceOutLogs", params, signatures);
        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testClearServiceOutLogs")
    public void testClearServiceErrorLogs() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appName = m_appGUID;
        String serinstname = m_servinst1;
        float version = m_version;
        try {
            Object[] params = {serinstname, appName, version, handleId};
            String[] signatures = {String.class.getName(), String.class.getName(), float.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "clearServiceErrLogs", params, signatures);

        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }

    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testClearServiceErrorLogs")
    public void testKillApplication() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appName = m_appGUID;
        JMXClient client =JMXClient.getInstance();
        handleId = client.getHandleId();
        try {
            Object[] params = {appName,m_version,handleId};
            String[] signatures = {String.class.getName(),float.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "killApplication", params, signatures);
        }catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testKillApplication")
    public void testGetPortInstancesForApp() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appGUID = m_appGUID;
        try {
            Object[] params = {appGUID,m_version,handleId};
            String[] signatures = {String.class.getName(),float.class.getName(), String.class.getName()};
            ArrayList listappprtinst = (ArrayList) jmxClient.invoke(objName2, "getPortInstancesForApp", params, signatures);
            Assert.assertNotNull(listappprtinst);
        }
        catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetPortInstancesForApp")
    public void testGetPortInstancesForService() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appGUID = m_appGUID;
        String serinstname = m_servinst1;
        try {
            Object[] params = {appGUID,m_version, serinstname, handleId};
            String[] signatures = {String.class.getName(),float.class.getName(), String.class.getName(), String.class.getName()};
            ArrayList listserprtinst = (ArrayList) jmxClient.invoke(objName2, "getPortInstancesForService", params, signatures);
            Assert.assertNotNull(listserprtinst);
        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }

    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetPortInstancesForService")
    public void testGetResourcePropertiesForApp() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appGUID = m_appGUID;
        try {
            Object[] params = {appGUID,m_version,handleId};
            String[] signatures = {String.class.getName(),float.class.getName(), String.class.getName()};
            HashMap resproplst = (HashMap) jmxClient.invoke(objName2, "getResourcePropertiesForApp", params, signatures);
            Assert.assertNotNull(resproplst);

        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }

    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetResourcePropertiesForApp")
    public void testGetAllSOAResourceNames() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            ArrayList SOAresnams = (ArrayList) jmxClient.invoke(objName2, "getAllSOAResourceNames", new Object[]{handleId}, new String[]{String.class.getName()});
            Assert.assertNotNull(SOAresnams);

        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetAllSOAResourceNames")
    public void testGetResourceProperties() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {

            ApplicationControllerMBean.ResourceListType resourceListType = ApplicationControllerMBean.ResourceListType.APPLICATION_LIST;

            ArrayList resourceList = new ArrayList();
            resourceList.add(m_appGUID4);
            resourceList.add(m_appGUID5);
            Object[] params = {resourceList, resourceListType,handleId};
            String[] signatures = {ArrayList.class.getName(), ApplicationControllerMBean.ResourceListType.class.getName(),String.class.getName()};
            HashMap resprop = (HashMap) jmxClient.invoke(objName2, "getResourceProperties", params, signatures);
            Assert.assertNotNull(resprop);

        } catch (ReflectionException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest",enabled = false, dependsOnMethods = "testGetResourceProperties", description = "disabled because this method is not exposed.")
    public void testGetComponentProperties() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            ArrayList compGUIDlist = new ArrayList();
            compGUIDlist.add("chat");

            Object[] params = {compGUIDlist, handleId};
            String[] signatures = {ArrayList.class.getName(), String.class.getName()};

            HashMap comprops = (HashMap) jmxClient.invoke(objName2, "getComponentProperties", params, signatures);
            Assert.assertNotNull(comprops);

        } catch (Exception ex) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }

    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testGetResourceProperties")
    public void testStopApplication() throws Exception {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {

            if (m_applicationcontroller.isApplicationRunning(m_appGUID2))
                m_applicationcontroller.killApplication(m_appGUID2,m_version);
            m_applicationcontroller.deleteApplication(m_appGUID2, m_version);
            m_applicationcontroller.close();
        }
        catch (Exception ex) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ApplicationDetailerTest", alwaysRun = true, dependsOnMethods = "testStopApplication")
    public void testViewHttpContext() throws Exception {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {

            System.out.println("Invoking the component with HTTP Stub");
             if(m_applicationcontroller.isApplicationRunning(m_appGUID3,m_version)){
                 m_applicationcontroller.killApplication(m_appGUID3,m_version);
             }
            Application application = ApplicationParser.readApplication(new File(m_appFile2));
            m_applicationcontroller.saveApplication(application);
            m_applicationcontroller.compileApplication(m_appGUID3, m_version);
            m_applicationcontroller.prepareLaunch(m_appGUID3, m_version);

            m_applicationcontroller.launchApplication(m_appGUID3, m_version);
            m_applicationcontroller.startAllServices(m_appGUID3,m_version);


            Object[] params = {m_appGUID3,m_version,m_servinst3, handleId};
            String[] signatures = {String.class.getName(),float.class.getName(), String.class.getName(), String.class.getName()};
            String HttpContext = (String) jmxClient.invoke(objName2, "viewHttpContext", params, signatures);

            System.out.println("The HTTP Context is " + HttpContext);

            System.out.println("Stopping the application " + m_appGUID3);

            if (m_applicationcontroller.isApplicationRunning(m_appGUID3))
                m_applicationcontroller.killApplication(m_appGUID3,m_version);
            m_applicationcontroller.deleteApplication(m_appGUID3, m_version);
            m_applicationcontroller.close();

        }
        catch (Exception ex) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @AfterClass(groups = "ApplicationDetailerTest", alwaysRun = true)
    public void testCleanup() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testCleanup", "ApplicationControllerTest"));
        try {
            if (m_applicationcontroller.isApplicationRunning(m_appGUID,m_version))
                m_applicationcontroller.killApplication(m_appGUID,m_version);

            m_applicationcontroller.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanup", "ApplicationControllerTest"));
        }
        catch (Exception ex) {
            System.err.println(Logger.getErrMessage(getOnlyMethodName(),"TestApplicationDetailer"));
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanup", "ApplicationControllerTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    public String getHandleID() {
        return rtlClient.getHandleID();
    }

    private void saveApplication() {
        try {
            System.out.println("The App File is::" + m_appFile);
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_applicationcontroller.saveApplication(application);
            Thread.sleep(10000);/*
            if(m_applicationcontroller.isApplicationRunning(m_appGUID))
                return;
            m_applicationcontroller.prepareLaunch(m_appGUID,m_version);
            m_applicationcontroller.launchApplication(m_appGUID,m_version);
            m_applicationcontroller.startAllServices(m_appGUID);
            Thread.sleep(10000);*/
        } catch (TifosiException e) {
            System.out.println("error launching the application");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
        }

    }
}
