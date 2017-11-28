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
 * The objective is to change the log module during run time and test the change
 */

public class ChangeLogModuleTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String m_servinstance1;
    private String m_loglevel1;
    private String m_loglevel2;
    private String m_modulename1;


    public ChangeLogModuleTest(String name) {
        super(name);
    }

    public ChangeLogModuleTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {
        super.setUp();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Sanity" + File.separator + "ChangeLogModule";
        init();
        m_initialised = true;
    }

    private void init() throws Exception {
        if (m_initialised)
            return;

        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));

        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile", "1.0.xml");
        m_servinstance1 = testCaseProperties.getProperty("ServiceInstance1");
        m_modulename1 = testCaseProperties.getProperty("ModuleName1");
        m_loglevel1 = testCaseProperties.getProperty("LogLevel1");
        m_loglevel2 = testCaseProperties.getProperty("LogLevel2");
        printInitParams();


    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        //System.out.println("Route GUID:: "+m_routeGUID);
        System.out.println("The Application File is::" + m_appFile);
        System.out.println("Service Instance::      " + m_servinstance1);
        //System.out.println("Loglevel defined is ::      "+m_loglevel1);
        //System.out.println("The New Transformation File::"+m_newTransformation);
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
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "ChangeLogMOduleTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "ChangeLogModuleTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "ChangeLogMOduleTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application.
     */
    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "ChangeLogMOduleTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "ChangeLogModuleTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "ChangeLogMOduleTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * sets loglevel at application level
     *
     * @throws Exception
     */

    public void testSetLogLevelForApplication() throws Exception {
        DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetLogLevelForApplication", "ChangeLogMOduleTest"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            // Application application = ApplicationParser.readApplication(new File(m_appFile));
            //m_fioranoApplicationController.saveApplication(application);
            //m_fioranoApplicationController.compileApplication(m_appGUID,m_version);
            //m_fioranoApplicationController.prepareLaunch(m_appGUID,m_version);
            //m_fioranoApplicationController.launchApplication(m_appGUID,m_version);
            //m_fioranoApplicationController.startAllServices(m_appGUID);
            m_fioranoApplicationController.setLogLevel(m_appGUID,m_version, m_loglevel1);

            String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version, m_servinstance1, m_modulename1);
            System.out.println("loglevel is::" + loglevel);
            if (!loglevel.equalsIgnoreCase(m_loglevel1))
                throw new Exception("Log Level not set@" + m_loglevel1 + "@" + loglevel);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetLOgLevelForApplication", "ChangeLogModuleTest"));
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetLOgLevelForApplication", "ChangeLogMOduleTest"), e);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + e.getMessage(), false);
        }
    }

    /**
     * sets loglevel for a service instance
     *
     * @throws Exception
     */

    public void testSetLogLevelForServiceInstance() throws Exception {
        DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetLOgLevelForServiceInstance", "ChangeLogMOduleTest"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            m_fioranoApplicationController.setLogLevel(m_appGUID,m_version, m_servinstance1, m_loglevel1);
            String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version, m_servinstance1, m_modulename1);
            System.out.println("loglevel is::" + loglevel);
            if (!loglevel.equalsIgnoreCase(m_loglevel1))
                throw new Exception("Log Level not set@" + m_loglevel1 + "@" + loglevel);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetLogLevelForServiceInstance", "ChangeLogModuleTest"));
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetLOgLevelForServiceInstance", "ChangeLogMOduleTest"), e);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + e.getMessage(), false);
        }
    }

    /**
     * sets loglevel for a module in a service instance
     *
     * @throws Exception
     */

    public void testSetLogLevelForModule() throws Exception {
        DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetLogLevelForMOdule", "ChangeLogMOduleTest"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            m_fioranoApplicationController.setLogLevel(m_appGUID,m_version, m_servinstance1, m_modulename1, m_loglevel2);
            String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version, m_servinstance1, m_modulename1);
            System.out.println("loglevel is::" + loglevel);
            if (!loglevel.equalsIgnoreCase(m_loglevel2))
                throw new Exception("Log Level not set>>" + m_loglevel1 + "<<and>>" + loglevel + "<<");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetLogLevelForModule", "ChangeLogModuleTest"));
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetLOgLevelForModule", "ChangeLogMOduleTest"), e);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + e.getMessage(), false);
        }
    }

    /**
     * Stops an application which is running
     */

    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "ChangeLogMOduleTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "ChangeLogModuleTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "ChangeLogMOduleTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes an application which is stopped above
     */

    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "ChangeLogMOduleTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "ChangeLogModuleTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "ChangeLogMOduleTest"), ex);
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
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);

        } catch (TifosiException e) {
            System.err.println("Error stoping the application");
            e.printStackTrace();
        }
    }

    public static Test suite() {
        return new TestSuite(ChangeLogModuleTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testRunApplication");
//        methodsOrder.add("testSetLogLevelForApplication");         //deprecated
        methodsOrder.add("testSetLogLevelForServiceInstance");
        methodsOrder.add("testSetLogLevelForModule");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }
}




