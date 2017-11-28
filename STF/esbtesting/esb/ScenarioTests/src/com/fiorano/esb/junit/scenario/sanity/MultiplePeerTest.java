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

package com.fiorano.esb.junit.scenario.sanity;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.*;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

/**
 *
 *
 *
 *
 */
public class MultiplePeerTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;


    public MultiplePeerTest(String name) {
        super(name);
    }

    public MultiplePeerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Sanity" + File.separator + "MultiplePeers";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        init();
    }

    private void init() throws Exception {
        if (m_initialised)
            return;

        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));

        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile", "1.0.xml");

        printInitParams();
        m_initialised = true;


    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);

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
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "MultiplePeerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "MultiplePeersTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "MultiplePeerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application.
     */
    public void testLaunchPeerServer() {
        DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testLaunchPeerServer", "MultiplePeerTest"));
        try {
            if (!(rtlClient.getFioranoFPSManager().isTPSRunning("fps1"))) {
                ExecutionController remoteProxy = ExecutionController.getInstance();
                ServerStatusController sc = ServerStatusController.getInstance();
                TestEnvironment testEnvironment = sc.getTestEnvironment();
                ServerElement server = testEnvironment.getServer("fps");
                Map<String, ProfileElement> profiles = server.getProfileElements();
                ProfileElement profileElem;
                profileElem = profiles.get("standalone");
                remoteProxy.startServerOnRemote(testEnvironment.getMachine(profileElem.getMachineName()).getAddress(),
                        server.getMode(), "profile2", profileElem.isWrapper(), false);
                Thread.sleep(5000);
                //boolean fps = rtlClient.getFioranoFPSManager().isTPSRunning("fps");
                if (rtlClient.getFioranoFPSManager().isTPSRunning("fps1"))
                    System.out.println("Peer server fps1 launched");
                else throw new Exception();
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testLaunchPeerServer", "MultiplePeersTest"));
            }
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testLaunchPeerServer", "MultiplePeerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "MultiplePeerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            m_fioranoApplicationController.startService(m_appGUID,m_version, "chat1");
            m_fioranoApplicationController.startService(m_appGUID,m_version, "chat2");
            Thread.sleep(10000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            String node1 = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, "chat1").getServiceNodeName();
            String node2 = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, "chat2").getServiceNodeName();
            System.out.println("node1= " + node1 + ", node2= " + node2);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "MultiplePeersTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "MultiplePeerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testStopPeerServer() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testPeerServer", "MultiplePeerTest"));
            rtlClient.getFioranoFPSManager().shutdownTPS("fps1");
            Thread.sleep(5000);
            if (rtlClient.getFioranoFPSManager().isTPSRunning("fps1"))
                throw new Exception();
            else System.out.println("Peer server fps1 stopped ");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testStopPeerServer", "MultiplePeersTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testStopPeerServer", "MultiplePeerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Stops an application which is running.
     */


    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "MultiplePeerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "MultiplePeersTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "MultiplePeerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes an application which is stopped above
     */

    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "MultiplePeerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "MultiplePeersTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "MultiplePeerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            //m_fioranoApplicationController.startAllServices(appGuid);
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
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);
        } catch (TifosiException e) {
            System.err.println("Error stoping the application");
            e.printStackTrace();
        }
    }

    public static Test suite() {
        return new TestSuite(MultiplePeerTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testLaunchPeerServer");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testStopPeerServer");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }
}
