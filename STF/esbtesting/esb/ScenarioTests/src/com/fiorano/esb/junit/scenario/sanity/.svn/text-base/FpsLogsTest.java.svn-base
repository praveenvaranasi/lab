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
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * The objective of this file is to test the export of fps logs
 */
public class FpsLogsTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;


    public FpsLogsTest(String name) {
        super(name);
    }

    public FpsLogsTest(TestCaseElement testCaseConfig) {
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
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Sanity" + File.separator + "FpsLogs";

            init();
            m_initialised = true;
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }


    /**
     * tests for the export of fps logs
     */
    public void testFpslogs() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testFpslogs", "FpsLogsTest"));
            Boolean found = false;
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.getFioranoFPSManager().exportFPSLogs("fps", (System.getProperty("user.home") + "/Desktop/fps.zip"));
            Thread.sleep(5000);
            System.out.println("exported the zip file");
            found = new File(System.getProperty("user.home") + "/Desktop/fps.zip").exists();
            if (found)
                System.out.println("zip file exported successfully");
            else throw new Exception();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testFpslogs", "FpsLogsTest"));
        }
        catch (Exception ex) {   //DRTTestLogger.initializeLogger();
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testFpslogs", "FpsLogsTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public static Test suite() {
        return new TestSuite(FpsLogsTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();
        methodsOrder.add("testFpslogs");


        return methodsOrder;
    }
}




