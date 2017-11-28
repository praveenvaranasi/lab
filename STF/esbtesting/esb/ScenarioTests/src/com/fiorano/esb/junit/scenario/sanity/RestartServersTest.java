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
/**
 * The objective of this file is to test the restart functionality of the servers.
 */
package com.fiorano.esb.junit.scenario.sanity;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

public class RestartServersTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;


    public RestartServersTest(String name) {
        super(name);
    }

    public RestartServersTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void init() throws Exception {
        printInitParams();
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("...........................................................................");
    }

    public void setUp() throws Exception {
        super.setUp();
        if (!m_initialised) {
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Sanity" + File.separator + "RestartServers";

            init();
            m_initialised = true;
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Restarts the FPS server
     */

    public void testRestartFPS() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRestartFPS", "RestartServersTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.getFioranoFPSManager().restartTPS("fps");
            Thread.sleep(100000);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRestartFPS", "RestartServersTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRestartFPS", "RestartServersTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Restarts the FES server
     */
    public void testRestartFES() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRestartFES", "RestartServersTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.restartEnterpriseServer();

            System.out.println("Restarted FES");
            //todo
            // log saying that we have restarted the servers and hence we are updating fsp
            Thread.sleep(100000);
            this.suite.updateFioranoServiceProvider();
            DRTTestLogger.logInfo("FioranoServiceProvider updated by testRestartFES of RestartServersTest");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRestartFES", "RestartServersTest"));


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRestartFES", "RestartServersTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Shutdowns the FEs and FPS servers after restart
     */

    public void testShutdown() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testShutdown", "RestartServersTest"));
            Thread.sleep(5000);
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.getFioranoFPSManager().shutdownTPS("fps");
            rtlClient.shutdownEnterpriseServer();
            Thread.sleep(5000);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testShutdown", "RestartServersTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testShutdown", "RestartServersTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public static Test suite() {
        return new TestSuite(RestartServersTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();


        methodsOrder.add("testRestartFPS");
        methodsOrder.add("testRestartFES");


        //todo
        // log saying that we have restarted the servers and hence we are updating fsp
        //this.suite.updateFioranoServiceProvider();

        methodsOrder.add("testShutdown");
        return methodsOrder;
    }
}




