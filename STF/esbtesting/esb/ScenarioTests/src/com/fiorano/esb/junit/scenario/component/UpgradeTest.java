package com.fiorano.esb.junit.scenario.component;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
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
 * This File contains code for upgrading a componet version and testing it
 */
public class UpgradeTest extends DRTTestCase {
    private FioranoServiceRepository m_fioranoServiceRepository;
    private FioranoApplicationController m_fioranoApplicationController;
    private String resourceFilePath;
    private String m_appGUID;
    private Float m_version;
    private String m_appFile;

    public UpgradeTest(String name) {
        super(name);
    }

    public UpgradeTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }


    public void init() throws Exception {

        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));


        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile");
        printInitParams();

    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: " + m_appGUID + "\tVersion:: " + m_version);
        System.out.println("The Resource File Path:: " + resourceFilePath);
        System.out.println("...........................................................................");
    }


    public void setUp() throws Exception {
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Component" + File.separator + "Upgrade";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
        init();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * trying to import an application .
     */
    public void testImportApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "UpgradeTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "UpgradeTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application .
     */
    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "UpgradeTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "UpgradeTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Upgrades a component version and tests the change
     *
     * @throws Exception
     */

    public void testUpgrade() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testUpgrade", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String highest_version = m_fioranoServiceRepository.getHighestVersionOfService("SMTP");
            System.out.println("version before change is::" + highest_version);
            Float highestversion = Float.parseFloat(highest_version);
            Float newhighestversion;
            newhighestversion = highestversion + 0.1f;
            String newhighest_version = newhighestversion.toString();
            m_fioranoServiceRepository.copyServiceVersion("SMTP", highest_version, "SMTP", newhighest_version);
            m_fioranoServiceRepository.commitService("SMTP", newhighest_version);
            System.out.println("version after upgrade ::" + m_fioranoServiceRepository.getHighestVersionOfService("SMTP"));

            if (m_fioranoServiceRepository.getHighestVersionOfService("SMTP") == highest_version)
                throw new Exception("Version of the component is not upgraded");

            //remove the last upgraded version so that in the next test run , service
            // could be again upgraded to same version , or else it gives version
            // already exists error.
            m_fioranoServiceRepository.removeService("SMTP", newhighest_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testUpgrade", "UpgradeTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testUpgrade", "UpgradeTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    /**
     * Stops the running application.
     */
    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "UpgradeTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "UpgradeTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes the stopped application.
     */
    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "UpgradeTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "UpgradeTest"), ex);
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
            System.out.println("stoping the application");
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);

        } catch (TifosiException e) {
            System.err.println("Error stoping the application");
            e.printStackTrace();
        }
    }


    public static Test suite() {
        return new TestSuite(UpgradeTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();
        methodsOrder.add("testImportApplication");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testUpgrade");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }

}