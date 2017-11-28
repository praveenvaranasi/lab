/* Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */

package com.fiorano.esb.junit.scenario.sanity;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * The objective of this file is to check for the availabilty of inmemory logs info after the flow is launched
 */
public class LogsInfoTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;


    public LogsInfoTest(String name) {
        super(name);
    }

    public LogsInfoTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Sanity" + File.separator + "LogsInfo";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        init();
    }

    private void init() throws Exception {
        if (m_initialised)
            return;

        m_appGUID = (testCaseProperties.getProperty("ApplicationGUID"));
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));

        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile", "1.0.xml");

        printInitParams();
        m_initialised = true;


    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::" + m_appGUID + "  Version Number::" + m_version);

        System.out.println("The Application File is::" + m_appFile);

        System.out.println("..................................................................");

    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * trying to import an application.
     */
    public void testImportApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "LogsInfoTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "LogsInfoTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "LogsInfoTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application.
     */
    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "LogsInfoTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "LogsInfoTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "LogsInfoTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Tests for the inmemory logs info
     */
    public void testLogInfo() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testLogInfo", "LogsInfoTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String str = m_fioranoApplicationController.getLastOutTrace(100, "SMTP1", m_appGUID);
            System.out.println("Out InMemory logs are::" + str);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testLogInfo", "LogsInfoTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testLogInfo", "LogsInfoTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Stops an application which is running
     */

    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "LogsInfoTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "LogsInfoTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "LogsInfoTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes an application which is stopped above
     */

    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "LogsInfoTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "LogsInfoTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "LogsInfoTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);
            System.out.println("started application");
        } catch (TifosiException e) {
            if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1) {
                //never mind if the application is already running.
                return;
            }
            System.err.println("Error launching the application");
            e.printStackTrace();
            throw new FioranoException("Error launching the application", e);
        }
    }

    private void stopApplication(String appGUID) {
        try {
            System.out.println("stopped the application");
            Thread.sleep(100000);
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            Thread.sleep(100000);
            m_fioranoApplicationController.killApplication(appGUID,m_version);


        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (TifosiException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static Test suite() {
        return new TestSuite(LogsInfoTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testRunApplication");

        methodsOrder.add("testLogInfo");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }
}




