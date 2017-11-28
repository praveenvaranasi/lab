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
import fiorano.tifosi.dmi.service.ServiceRef;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * The objective of this class is to add a service dependency for a service and test the change
 */
public class ServiceDependancyTest extends DRTTestCase {
    private FioranoServiceRepository m_fioranoServiceRepository;
    private FioranoApplicationController m_fioranoApplicationController;
    private String resourceFilePath;
    private String m_appGUID;
    private Float m_version;
    private String m_appFile;


    public ServiceDependancyTest(String name) {
        super(name);
    }

    public ServiceDependancyTest(TestCaseElement testCaseConfig) {
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
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Component" + File.separator + "ServiceDependancy";
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
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "ServiceDependancyTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));

            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "ServiceDependancyTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "ServiceDependancyTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Resources and connectivity check
     */

    public void testCRC() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCRC", "ServiceDependancyTest"));
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

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCRC", "ServiceDependancyTest"));


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCRC", "ServiceDependancyTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application.
     */
    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "ServiceDependancyTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);

            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "ServiceDependancyTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "ServiceDependancyTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Adds service dependency for a component and checks for the change
     */
    public void testServiceDependancy() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testServiceDependancy", "ServiceDependancyTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            int i = 0;
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


            Iterator ir = service.getDeployment().getServiceRefs().iterator();
            while (ir.hasNext()) {
                ServiceRef ref = (ServiceRef) ir.next();
                System.out.println("Service reference name::" + ref.getName());
            }
            ServiceRef ref = new ServiceRef();
            ref.setGUID("perl");
            ref.setVersion(Float.parseFloat("4.0"));
            service1.getDeployment().addServiceRef(ref);
            m_fioranoServiceRepository.editRegisteredService(service.getGUID(), Float.toString(service.getVersion()), service1);
            Iterator ir1 = service.getDeployment().getServiceRefs().iterator();
            while (ir1.hasNext()) {
                ServiceRef ref1 = (ServiceRef) ir1.next();
                if (ref1.getName().matches("perl:4.0"))
                    i = 1;
                System.out.println("Service reference name::" + ref1.getName());
            }

            if (i == 0)
                throw new Exception("couldnt add service dependency");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testServiceDependancy", "ServiceDependancyTest"));


        }


        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testServiceDependancy", "ServiceDependancyTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Stops a running application
     */

    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "ServiceDependancyTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "ServiceDependancyTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "ServiceDependancyTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes an application which is stopped
     */

    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "ServiceDependancyTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "ServiceDependancyTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "ServiceDependancyTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            System.out.println("launching the application");
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
        return new TestSuite(ServiceDependancyTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();
        methodsOrder.add("testImportApplication");
        methodsOrder.add("testCRC");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testServiceDependancy");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }

}
