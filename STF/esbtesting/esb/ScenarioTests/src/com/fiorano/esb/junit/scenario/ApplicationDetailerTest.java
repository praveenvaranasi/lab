/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.esb.junit.scenario;

/*import com.fiorano.dashboard.server.JMXClient;*/

import com.fiorano.stf.test.core.RMITestCase;
import com.fiorano.stf.test.core.TestCaseElement;

import com.fiorano.stf.remote.JMXClient;
import com.fiorano.esb.application.controller.ApplicationControllerMBean;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import fiorano.tifosi.dmi.application.ApplicationParser;

import javax.management.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;
import java.io.IOException;
import java.io.File;

import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.common.TifosiException;

/**
 * User: aditya
 * Date: Oct 15, 2007
 * Time: 2:55:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationDetailerTest extends RMITestCase {
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


    public ApplicationDetailerTest(String name) {
        super(name);
    }

    public ApplicationDetailerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {

        System.out.println("Started the Execution of the TestCase::" + getName());
        if (isReady == true) return;
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "ApplicationDetailer";
        m_applicationcontroller = rtlClient.getFioranoApplicationController();
        init();

        try {
            objName = new ObjectName("Fiorano.Esb.Webconsole.Controller:ServiceType=WebconsoleController,Name=WebconsoleController");
            objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            isReady = true;
            JMXClient.connect("admin", "passwd");
        }
        catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        isReady = true;

    }

    private void init() {
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_appGUID1 = testCaseProperties.getProperty("ApplicationGuid1");
        m_appGUID2 = testCaseProperties.getProperty("ApplicationGuid2");
        m_appGUID3 = testCaseProperties.getProperty("ApplicationGuid3");

        m_routeGUID = testCaseProperties.getProperty("RouteGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));

        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile");
        m_appFile1 = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile1");
        m_appFile2 = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile2");

        m_servinst1 = testCaseProperties.getProperty("InstanceName1");
        m_servinst2 = testCaseProperties.getProperty("InstanceName2");
        m_servinst3 = testCaseProperties.getProperty("InstanceName3");
        printInitParams();
    }

    private void printInitParams() {

        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    public static Test suite() {
        return new TestSuite(ApplicationDetailerTest.class);

    }


    public void testGetApplicationDetails() {

        try {
            ArrayList appDataList = (ArrayList) jmxClient.invoke(objName, "getAppInfo", new Object[]{}, new String[]{});
            Assert.assertNotNull(appDataList);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetAllApplicationNames() {

        try {
            ArrayList appNames = (ArrayList) jmxClient.invoke(objName, "getAllApplicationNames", new Object[]{}, new String[]{});
            Assert.assertNotNull(appNames);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetAllServiceInstanceNames() {

        try {
            Object[] params = {m_appGUID};
            String[] signatures = {String.class.getName()};
            ArrayList servInstNames = (ArrayList) jmxClient.invoke(objName, "getAllServiceInstanceNames", params, signatures);
            Assert.assertNotNull("servInstNames");
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
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

            Thread.sleep(10000);
        } catch (TifosiException e) {
            System.out.println("error launching the application");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
        }

    }


    public void testlaunchApplication() {

        saveApplication();
        String appName = m_appGUID;
        float version = m_version;
        String handleID = getHandleID();

        try {

            Object[] params = {appName, version, handleID};
            String[] signatures = {String.class.getName(), float.class.getName(), String.class.getName()};

            jmxClient.invoke(objName2, "launchAndStartAllServices", params, signatures);

        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }

    }

    public void testkillApplication() {

        String appName = m_appGUID;

        String handleID = getHandleID();
        try {
            Object[] params = {appName, handleID};
            String[] signatures = {String.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "killApplication", params, signatures);
        }


        catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }


    public void testgetDebugRoutes() throws Exception {

        String appName = m_appGUID2;
        float version = m_version;


        try {
            Application application = ApplicationParser.readApplication(new File(m_appFile1));
            m_applicationcontroller.saveApplication(application);
            m_applicationcontroller.compileApplication(appName, version);
            m_applicationcontroller.prepareLaunch(appName, version);

            m_applicationcontroller.launchApplication(appName, version);
            m_applicationcontroller.startAllServices(appName,version);


            Object[] params = {appName, version};
            String[] signatures = {String.class.getName(), float.class.getName()};

            Vector vec = (Vector) jmxClient.invoke(objName2, "getDebugRoutes", params, signatures);
            Assert.assertNotNull(vec);


        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
        catch (TifosiException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }

    }

    public void testgetSBWPortsForApp() {

        String appName = m_appGUID2;
        float version = m_version;
        try {

            Object[] params = {appName, version};
            String[] signatures = {String.class.getName(), float.class.getName()};

            HashMap SBWPorts = (HashMap) jmxClient.invoke(objName2, "getSBWPortsForApp", params, signatures);
            Assert.assertNotNull(SBWPorts);

        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testgetUserDefinedDestinationPortsForApp() {

        String appName = m_appGUID2;
        float version = m_version;
        try {

            Object[] params = {appName, version};
            String[] signatures = {String.class.getName(), float.class.getName()};

            HashMap DestPorts = (HashMap) jmxClient.invoke(objName2, "getSBWPortsForApp", params, signatures);
            Assert.assertNotNull(DestPorts);

        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }


    public void testgetRunningComponents() {

        String FPS = "fps_test";

        try {

            Object[] params = {FPS};
            String[] signatures = {String.class.getName()};

            HashMap runcomps = (HashMap) jmxClient.invoke(objName2, "getRunningComponents", params, signatures);
            Assert.assertNotNull(runcomps);

        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }


    public void testrestrtApplication() {

        String appName = m_appGUID;

        String handleID = getHandleID();
        try {
            Object[] params = {appName, handleID};
            String[] signatures = {String.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "restartEventProcess", params, signatures);
        }

        catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testgetLastOutTrace() {
        System.out.println("started the e4xecution of the testcase::" + getName());
        int nol = 4;
        String sername = m_servinst2;
        String appGUID = m_appGUID;
        float appver = m_version;

        try {
            Object[] params = {nol, sername, appGUID, appver};
            String[] signatures = {int.class.getName(), String.class.getName(), String.class.getName(), float.class.getName()};
            String result = (String) jmxClient.invoke(objName2, "getLastOutTrace", params, signatures);
            Assert.assertNotNull(result);
        }
        catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testgetLastErrorTrace() {
        int nol = 8;
        String sername = m_servinst2;
        String appGUID = m_appGUID;
        float appver = m_version;
        try {
            Object[] params = {nol, sername, appGUID, appver};
            String[] signatures = {int.class.getName(), String.class.getName(), String.class.getName(), float.class.getName()};
            String result = (String) jmxClient.invoke(objName2, "getLastErrorTrace", params, signatures);
            Assert.assertNotNull(result);
        }

        catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void teststopservice() {

        String appName = m_appGUID;
        String serinstname = m_servinst1;
        String handleID = getHandleID();
        try {
            Object[] params = {appName, serinstname, handleID};
            String[] signatures = {String.class.getName(), String.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "stopService", params, signatures);
        }
        catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testlaunchservice() {
        System.out.println("Started the execution of the test case::" + getName());
        String appName = m_appGUID;
        String serinstname = m_servinst1;
        String handleID = getHandleID();
        try {
            Object[] params = {appName, serinstname, handleID};
            String[] signatures = {String.class.getName(), String.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "startService", params, signatures);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testclearserviceoutlogs() {
        String appName = m_appGUID;
        String serinstname = m_servinst1;
        float version = m_version;

        try {
            Object[] params = {serinstname, appName, version};
            String[] signatures = {String.class.getName(), String.class.getName(), float.class.getName()};
            jmxClient.invoke(objName2, "clearServiceOutLogs", params, signatures);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testclearServiceErrorLogs() {
        String appName = m_appGUID;
        String serinstname = m_servinst1;
        float version = m_version;
        try {
            Object[] params = {serinstname, appName, version};
            String[] signatures = {String.class.getName(), String.class.getName(), float.class.getName()};
            jmxClient.invoke(objName2, "clearServiceErrLogs", params, signatures);

        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }

    }

    public void testgetPortInstancesForApp() {
        String appGUID = m_appGUID;
        try {
            Object[] params = {appGUID};
            String[] signatures = {String.class.getName()};
            ArrayList listappprtinst = (ArrayList) jmxClient.invoke(objName2, "getPortInstancesForApp", params, signatures);
            Assert.assertNotNull(listappprtinst);
        }
        catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testgetPOrtInstancesForService() {
        String appGUID = m_appGUID;
        String serinstname = m_servinst1;
        try {
            Object[] params = {appGUID, serinstname};
            String[] signatures = {String.class.getName(), String.class.getName()};
            ArrayList listserprtinst = (ArrayList) jmxClient.invoke(objName2, "getPortInstancesForService", params, signatures);
            Assert.assertNotNull(listserprtinst);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }

    }

    public void testgetResourcePropertiesForApp() {
        String appGUID = m_appGUID;
        try {
            Object[] params = {appGUID};
            String[] signatures = {String.class.getName()};
            HashMap resproplst = (HashMap) jmxClient.invoke(objName2, "getResourcePropertiesForApp", params, signatures);
            Assert.assertNotNull(resproplst);

        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }

    }


    public void testgetAllSOAResourceNames() {
        try {
            ArrayList SOAresnams = (ArrayList) jmxClient.invoke(objName2, "getAllSOAResourceNames", new Object[]{}, new String[]{});
            Assert.assertNotNull(SOAresnams);

        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testgetResourceProperties() {

        try {

            ApplicationControllerMBean.ResourceListType resourcelisttype = ApplicationControllerMBean.ResourceListType.APPLICATION_LIST;

            ArrayList resourcelist = new ArrayList();
            resourcelist.add(m_appGUID);
            resourcelist.add(m_appGUID1);
            Object[] params = {resourcelist, resourcelisttype};
            String[] signatures = {ArrayList.class.getName(), ApplicationControllerMBean.ResourceListType.class.getName()};
            HashMap resprop = (HashMap) jmxClient.invoke(objName2, "getResourceProperties", params, signatures);
            Assert.assertNotNull(resprop);

        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testgetcomponentProperties() {


        try {

            ApplicationControllerMBean.ResourceListType resourcelisttype = ApplicationControllerMBean.ResourceListType.COMPONENT_LIST;

            ArrayList compGUIDlist = new ArrayList();
            compGUIDlist.add("chat");

            Object[] params = {compGUIDlist, resourcelisttype};
            String[] signatures = {ArrayList.class.getName(), ApplicationControllerMBean.ResourceListType.class.getName()};

            HashMap comprops = (HashMap) jmxClient.invoke(objName2, "getResourceProperties", params, signatures);
            Assert.assertNotNull(comprops);

        } catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

    }

    public void testStopApplication() throws Exception {
        try {

            if (m_applicationcontroller.isApplicationRunning(m_appGUID2))
                m_applicationcontroller.killApplication(m_appGUID2,m_version);
            m_applicationcontroller.deleteApplication(m_appGUID2, m_version);
            m_applicationcontroller.close();
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testviewHttpContext() throws Exception {

        try {

            System.out.println("Invoking the component with HTTP Stub");

            Application application = ApplicationParser.readApplication(new File(m_appFile2));
            m_applicationcontroller.saveApplication(application);
            m_applicationcontroller.compileApplication(m_appGUID3, m_version);
            m_applicationcontroller.prepareLaunch(m_appGUID3, m_version);

            m_applicationcontroller.launchApplication(m_appGUID3, m_version);
            m_applicationcontroller.startAllServices(m_appGUID3,m_version);


            Object[] params = {m_appGUID3, m_servinst3};
            String[] signatures = {String.class.getName(), String.class.getName()};
            String HttpContext = (String) jmxClient.invoke(objName2, "viewHttpContext", params, signatures);

            System.out.println("The HTTP Context is " + HttpContext);

            System.out.println("Stopping the application " + m_appGUID3);

            if (m_applicationcontroller.isApplicationRunning(m_appGUID3))
                m_applicationcontroller.killApplication(m_appGUID3,m_version);
            m_applicationcontroller.deleteApplication(m_appGUID3, m_version);
            m_applicationcontroller.close();

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

}
