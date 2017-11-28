package com.fiorano.esb.junit.scenario.port;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Mar 11, 2008
 * Time: 2:20:43 PM
 * To change this template use File | Settings | File Templates.
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

/**
 * The objective of this file is to test the enable and disable functionality of a port during run time.
 */
public class EnableDisablePortTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;

    public EnableDisablePortTest(String name) {
        super(name);
    }

    public EnableDisablePortTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Port" + File.separator + "EnableDisablePort";
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
     * trying to import an application with Transformation.
     */
    public void testImportApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "EnableDisablePortTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));

            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "EnableDisablePortTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "EnableDisablePortTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Disables a port and tests the change.
     */
    public void testDisablePort() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDisablePort", "EnableDisablePortTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            System.out.println("Here");
            Boolean value = application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").isEnabled();
            System.out.println("Instance status::" + value);
            application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").setEnabled(false);
            m_fioranoApplicationController.saveApplication(application);

            Boolean value1 = application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").isEnabled();
            System.out.println("Instance status::" + value1);
            if (!(value1 == false))
                throw new Exception("Not able to disable the chat1 outport");

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDisablePort", "EnableDisablePortTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDisablePort", "EnableDisablePortTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Enables a port and tests the change.
     */
    public void testEnablePort() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testEnablePort", "EnableDisablePortTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            Boolean value = application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").isEnabled();
            System.out.println("Instance status::" + value);
            application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").setEnabled(true);
            m_fioranoApplicationController.saveApplication(application);
            Boolean value1 = application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").isEnabled();
            System.out.println("Instance status::" + value1);
            if (!(value1 == true))
                throw new Exception("Not able to enable the chat1 outport");

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testEnablePort", "EnableDisablePortTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testEnablePort", "EnableDisablePortTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application.
     */
    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "EnableDisablePortTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(25000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "EnableDisablePortTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "EnableDisablePortTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Stops an application which is running.
     */
    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "EnableDisablePortTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "EnableDisablePortTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "EnableDisablePortTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes an application which is stopped above.
     */
    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "EnableDisablePortTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "EnableDisablePortTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "EnableDisablePortTest"), ex);
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
            System.out.println("StopIng the application");
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);

        } catch (TifosiException e) {
            System.err.println("Error stoping the application");
            e.printStackTrace();
        }
    }

    public static Test suite() {
        return new TestSuite(EnableDisablePortTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testDisablePort");
        methodsOrder.add("testEnablePort");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }
}


