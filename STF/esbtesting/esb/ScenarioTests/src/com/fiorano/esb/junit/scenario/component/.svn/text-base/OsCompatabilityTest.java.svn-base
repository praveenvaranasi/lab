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
import fiorano.tifosi.dmi.service.Service;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;

/**
 * The objective of this test class is to change the component os type to an incompatible type and checks whether it produces an error
 */
public class OsCompatabilityTest extends DRTTestCase {
    private FioranoServiceRepository m_fioranoServiceRepository;
    private FioranoApplicationController m_fioranoApplicationController;
    private String resourceFilePath;
    private String m_appGUID;
    private Float m_version;
    private String m_appFile;
    private Float m_version4;


    public OsCompatabilityTest(String name) {
        super(name);
    }

    public OsCompatabilityTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }


    public void init() throws Exception {

        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_version4 = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion4"));


        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile");
        printInitParams();

    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: " + m_appGUID + "\tVersion:: " + m_version);
        //System.out.println("new Version:: "+m_newVersion);

        System.out.println("The Resource File Path:: " + resourceFilePath);
        System.out.println("...........................................................................");
    }


    public void setUp() throws Exception {
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Component" + File.separator + "OsCompatability";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
        init();
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * trying to import an application.
     */
    public void testImportApplication() {
        try {

            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                //System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "OsCompatibilityTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "OsCompatibilityTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application .
     */
    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "OsCompatibilityTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "OsCompatibilityTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Changes the component os type to incompatible os type and tests the change
     */

    public void testOSCompatabilty() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testOSCompatibility", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Enumeration services = m_fioranoServiceRepository.getAllServicesInRepository();
            Service service = new Service();
            Service service1 = new Service();
            while (services.hasMoreElements()) {
                service = (Service) services.nextElement();

                String str = service.getDisplayName();
                if (str.matches("SMTP")) {
                    service1 = service;
                    break;
                }
                System.out.println("Service name::" + str);
            }
            boolean osChanged = false;
            System.out.println("Service name::" + service.getDisplayName());
            int os = service1.getDeployment().getSupportedOperatingSystems();
            System.out.println("Supported operating system b4 change::" + os);
            if (os != 4) {
                service1.getDeployment().setSupportedOperatingSystems(4);
                osChanged = true;
            }
            m_fioranoServiceRepository.editRegisteredService(service.getGUID(), Float.toString(service.getVersion()), service1);
            if (osChanged && m_fioranoServiceRepository.getServiceInfo("SMTP", Float.toString(m_version4)).getDeployment().getSupportedOperatingSystems() == os)
                throw new Exception("Component OS couldnt be changed");

            if (osChanged = true) {
                service1.getDeployment().setSupportedOperatingSystems(0);
                m_fioranoServiceRepository.editRegisteredService(service.getGUID(), Float.toString(service.getVersion()), service1);
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testOSCompatibility", "OsCompatibilityTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testOsCompatibility", "OsCompatibilityTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Checks the error logs of fps due to the change of component os to incompatible type
     */

    public void testFPSlogs() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testFPSlogs", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String str = rtlClient.getFioranoFPSManager().getTPSLastErrLogs(100, "fps");
            System.out.println("error logs" + str);
            String str1 = rtlClient.getFioranoFPSManager().getTPSLastOutLogs(100, "fps");
            System.out.println("out logs::" + str1);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testFPSlogs", "OsCompatibilityTest"));


        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testFPSlogs", "OsCompatibilityTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Stops the running application.
     */

    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "OsCompatibilityTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "OsCompatibilityTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes the stopped application.
     */

    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "OsCompatibilityTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "OsCompatibilityTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            System.out.println("starting the application");
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
        return new TestSuite(OsCompatabilityTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

//        methodsOrder.add("testOSCompatabilty");          // test doesn't make sense as we can change OS
        methodsOrder.add("testImportApplication");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testOSCompatabilty");
        // methodsOrder.add("testFPSlogs");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }

}
