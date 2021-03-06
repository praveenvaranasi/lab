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
import fiorano.tifosi.dmi.application.ServiceInstance;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Mar 3, 2008
 * Time: 3:32:38 PM
 * To change this template use File | Settings | File Templates.
 * The objective of this file is to change the cache property of a component and test the change
 */
public class ComponentCacheTest extends DRTTestCase {

    private String resourceFilePath;
    private boolean m_initialised;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private FioranoApplicationController m_fioranoApplicationController;

    public ComponentCacheTest(String name) {
        super(name);
    }

    public ComponentCacheTest(TestCaseElement testCaseConfig) {
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
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Application" + File.separator + "ComponentCache";
            init();
            m_initialised = true;
        }
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Imports an application
     */
    public void testImportApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "ComponentCacheTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "ComponentCacheTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "ComponentCacheTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    /**
     * Resource and connectivity check is done
     */

    public void testCRC() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCRC", "ComponentCacheTest"));
            //System.out.println("Started the Execution of the TestCase::"+getName());
            //                     an Exception if the application is not present.
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("The Application with GUID " + m_appGUID + " is already running.");
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);

            //post condition
            //todo complete this

            Assert.assertTrue(true);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCRC", "ComponentCacheTest"));


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCRC", "ComponentCacheTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * changes the cache property of a component and tests the change
     */

    public void testComponentCache() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testComponentCache", "ComponentCacheTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance instance = application.getServiceInstance("SMTP1");
            System.out.println("cache status::" + instance.isComponentCached());
            instance.setComponentCached(false);
            System.out.println("cache status::" + instance.isComponentCached());
            m_fioranoApplicationController.saveApplication(application);
            // m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID);
            if (instance.isComponentCached() == true)
                throw new Exception("Couldnt change the cache of component");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testComponentCache", "ComponentCacheTest"));

        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testComponentCache", "ComponentCacheTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application
     */

    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "ComponentCacheTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "ComponentCacheTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "ComponentCacheTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * stops a running application
     */

    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "ComponentCacheTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "ComponentCacheTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "ComponentCacheTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes the appication which is stopped
     */
    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "ComponentCacheTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "ComponentCacheTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "ComponentCacheTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
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
            //ignore.
        }
    }

    public static Test suite() {
        return new TestSuite(ComponentCacheTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testComponentCache");
        methodsOrder.add("testCRC");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }

}
