package com.fiorano.esb.junit.scenario.application;

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
import java.util.List;
import java.util.logging.Level;

/**
 * The objective if this test file is to create an application with a name of an existing application and checks for the exception
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Mar 3, 2008
 * Time: 3:32:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class SameApplicationTest extends DRTTestCase {

    private String resourceFilePath;
    private boolean m_initialised;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private FioranoApplicationController m_fioranoApplicationController;

    public SameApplicationTest(String name) {
        super(name);
    }

    public SameApplicationTest(TestCaseElement testCaseConfig) {
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
        System.out.println("Application GUID:: " + m_appGUID + "  Version Number::" + m_version);


        System.out.println("Application File" + m_appFile);
        System.out.println("...........................................................................");
    }

    public void setUp() throws Exception {
        super.setUp();
        if (!m_initialised) {
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Application" + File.separator + "SameApplication";
            init();
            m_initialised = true;
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Imports and application
     */
    public void testImportApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "SameApplicationTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "SameApplicationTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application.
     */


    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "SameApplicationTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "SameApplicationTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Creates a new application with the same name of an existing application
     */

    public void testCreateSameApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCreateSameApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = new Application();//m_fioranoApplicationController.getApplication(BASE_APPLICATION,1.0f);
            application.setServiceInstances(new ArrayList());
            application.setRoutes(new ArrayList());
            application.setGUID(m_appGUID);
            List categories = new ArrayList();
            categories.add("CoreScenarioTests");
            application.setCategories(categories);
            application.setDisplayName(m_appGUID);
            application.setVersion(m_version);
            System.out.println("Saving application with same name and same version no");
            m_fioranoApplicationController.saveApplication(application);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCreateSameApplication", "SameApplicationTest"));
        }
        catch (Exception ex) {
            if (ex.getMessage().indexOf("APPLICATION_SAVE_FAILURE_ERROR") != -1)
                Assert.assertTrue(true);
            else {
                System.out.println("Exception in the Execution of test case::" + ex.getMessage());
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCreateSameApplication", "SameApplicationTest"), ex);
                Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
            }
        }
    }

    /**
     * Stops a running application
     */
    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "SameApplicationTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "SameApplicationTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes the application which is stopped
     */

    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "SameApplicationTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "SameApplicationTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);
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
        return new TestSuite(SameApplicationTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testCreateSameApplication");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }

}
