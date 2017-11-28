package com.fiorano.esb.junit.scenario.server;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Mar 13, 2008
 * Time: 10:28:36 AM
 * To change this template use File | Settings | File Templates.
 */
/**
 * This class aims at testing how restarting of a FES server effects an application.
 */

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

public class FESRestartTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;

    public FESRestartTest(String name) {
        super(name);
    }

    public FESRestartTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Server" + File.separator + "FESRestart";
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
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "FESRestartTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));

            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "FESRestartTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "FESRestartTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application.
     */
    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "FESRestartTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);

            Thread.sleep(25000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "FESRestartTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "FESRestartTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * restarts FES
     */
    public void testRestartFES() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRestartFES", "FESRestartTest"));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRestartFES", "FESRestartTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.restartEnterpriseServer();

            System.out.println("Restarted FES");
            //todo
            // log saying that we have restarted the servers and hence we are updating fsp
            Thread.sleep(100000);
            this.suite.updateFioranoServiceProvider();
            DRTTestLogger.logInfo("FioranoServiceProvider updated by testRestartFES of FESRestartTest");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRestartFES", "FESRestartTest"));

        }
        catch (Exception ex) {
            System.out.println("Exception in the execution of testcase::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testrestartFES", "FESRestartTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * tests whether the application is running after the restart of the server
     */
    public void testRun() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRun", "FESRestartTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not running.");
            else
                System.out.println("Application is running after the restart of the server");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRun", "FESRestartTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the execution of testcase::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRun", "FESRestartTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Stops an application which is running
     */

    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "FESRestartTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "FESRestartTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "FESRestartTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes an application which is stopped above
     */
    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "FESRestartTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "FESRestartTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "FESRestartTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            System.out.println("starting application");
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);

            return;
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
            System.out.println("Stoping the application");
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);

        } catch (TifosiException e) {
            System.err.println("Error stoping the application");
            e.printStackTrace();
        }
    }

    public static Test suite() {
        return new TestSuite(FESRestartTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testRestartFES");
        methodsOrder.add("testRun");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");
        return methodsOrder;
    }
}

