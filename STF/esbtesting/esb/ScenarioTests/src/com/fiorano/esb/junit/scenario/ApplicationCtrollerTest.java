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

import com.fiorano.stf.test.core.RMITestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.server.FioranoServiceProviderFactory;


import javax.management.*;
import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.io.File;
import java.util.*;

import fiorano.tifosi.dmi.aps.ApplicationStateDetails;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import fiorano.tifosi.dmi.application.*;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Oct 16, 2007
 * Time: 3:16:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationCtrollerTest extends RMITestCase {

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

    public ApplicationCtrollerTest(String name) {
        super(name);
    }

    public ApplicationCtrollerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {
        super.setUp();

        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "ApplicationController";
        super.setUp();
        FAC = rtlClient.getFioranoApplicationController();
        init();

    }

    public void init() {
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile", "1.0.xml");
        m_version2 = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion2"));
        m_appFile2 = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile2", "2.0.xml");
        m_appFile3 = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile3", "3.0.xml");
        m_startInstance = testCaseProperties.getProperty("WorkFlowStartInstance");
        m_endInstance = testCaseProperties.getProperty("WorkFlowEndInstance");
        printInitParams();
    }

    private void printInitParams() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    public static Test suite() {
        return new TestSuite(ApplicationCtrollerTest.class);
    }

    public void testCreateApplication() {

        System.out.println("The App File is::" + m_appFile);
        try {
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
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    private void startApplication(String appGUID, float version) {
        try {
            String handleId = rtlClient.getHandleID();

            Object params[] = {appGUID, new Float(version), handleId};
            String signatures[] = {String.class.getName(), float.class.getName(), String.class.getName()};

            FAC.prepareLaunch(appGUID, version);
            FAC.launchApplication(appGUID, version);

            FAC.startAllServices(appGUID,version);

            /* wait for 20 sec so that the application gets started */
            Thread.sleep(20000);
        }
        catch (Exception e) {
            if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1)
                //never mind if the application is already running.
                return;
        }
    }


    public void testGetAllApplicationRoutes() {
        try {


//            List list = (List)(jmxClient.invoke(objName, "getRoutesOfApplication", params, signatures));
            List list = FAC.getRoutesOfApplication(m_appGUID, m_version);
            Assert.assertNotNull(list);

        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testModifyRoutes() {
        {
            //deprecated method
        }
    }

    public void testGetDeliverableMessageCount() {
        try {

            Hashtable pendingMsgs = new Hashtable();
            pendingMsgs = FAC.getDeliverableMessageCount(m_appGUID,m_version);
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }


    public void testGetApplicationHeaders() {
        try {
            ApplicationReference AR1;
            System.out.println("Started the Execution of the TestCase::" + getName());
            ApplicationReference[] refArray1 = FAC.getApplicationHeaders(m_appGUID);
            ApplicationStateDetails ASD;
            System.out.println("Finished fetching applicationheaders");
            Enumeration en1 = FAC.getHeadersOfRunningApplications();
            while (en1.hasMoreElements()) {
                AR1 = (ApplicationReference) en1.nextElement();
                Assert.assertTrue(FAC.isApplicationRunning(AR1.getGUID()));

            }
            Enumeration en2 = FAC.getHeadersOfSavedApplications();
            while (en2.hasMoreElements()) {
                AR1 = (ApplicationReference) en2.nextElement();
                FAC.getApplication(AR1.getGUID(), AR1.getVersion());      //if its present, it must be gettable, insome pieces at least
            }
            System.out.println("Finished fetching saved applicationheaders");
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testGetApplicatonList() {
        try {

            String appName;
            Enumeration en1 = FAC.getListOfRunningApplications();
            while (en1.hasMoreElements()) {
                appName = en1.nextElement().toString();
                Assert.assertTrue(FAC.isApplicationRunning(appName));

            }
            System.out.println("Finished fetching list of running application");
            Enumeration en2 = FAC.getListOfSavedApplications();
            while (en2.hasMoreElements()) {
                appName = en2.nextElement().toString();
                FAC.getApplication(appName, 1.0f);
            }
            System.out.println("Finished fetching list of saved application");
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetApplicatonStateDetails() {
        try {


            Object[] params = {m_appGUID};
            String[] signatures = {String.class.getName()};

            ApplicationStateDetails asd1 = (ApplicationStateDetails) FAC.getCurrentStateOfApplication(m_appGUID,m_version);

            Assert.assertEquals(m_appGUID, asd1.getAppGUID());
            Enumeration list = asd1.getAllServiceNames();
            ApplicationStateDetails asd2 = FAC.getCurrentStateOfApplicationFromFPSs(m_appGUID,m_version);
            Assert.assertTrue(asd2.getAppGUID().equalsIgnoreCase(m_appGUID));
            Enumeration list2 = asd2.getAllServiceNames();
            ArrayList<String> aList = new ArrayList<String>();
            while (list.hasMoreElements()) {
                aList.add((String) list.nextElement());
            }
            while (list2.hasMoreElements()) {
                Assert.assertTrue(aList.contains(list2.nextElement()));
            }

        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    //
    public void testGetServiceInstanceStateDetails() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application application = ApplicationParser.readApplication(new File(m_appFile));
            ServiceInstance startService = application.getServiceInstance(m_startInstance);
            String srvName = startService.getName();

            ServiceInstanceStateDetails sisd1 = FAC.getCurrentStateOfService(m_appGUID,m_version, m_startInstance);
            Assert.assertTrue(sisd1.getServiceInstanceName().equalsIgnoreCase(srvName));
            System.out.println("Finished fetching current state of service");
            ServiceInstanceStateDetails sisd2 = FAC.getCurrentStateOfServiceFromFPSs(m_appGUID,m_version, m_startInstance);
            Assert.assertTrue(sisd2.getServiceInstanceName().equalsIgnoreCase(srvName));
            System.out.println("Finished fetching current state of service from fps");
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testStopAllServices() {
        try {

            if (!(((FAC.getCurrentStateOfService(m_appGUID,m_version, m_startInstance))).getKillTime() < new Date().getTime()))
                throw new Exception("Stop All Services Failed");
        } catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }
//
//

    //

    public void testCleanUp() {
        try {


            String handleId = rtlClient.getHandleID();
            Object[] params1 = {m_appGUID, handleId};
            String[] signatures1 = {String.class.getName(), String.class.getName()};

            if (FAC.isApplicationRunning(m_appGUID))
                FAC.killApplication(m_appGUID,m_version);

            /* wait for some time so that the application gets killed */
            Thread.sleep(10000);
            FAC.deleteApplication(m_appGUID, m_version);
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }
}

