package com.fiorano.esb.testng.rmi.scenario.component;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.service.Service;
import fiorano.tifosi.dmi.service.ServiceRef;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/14/11
 * Time: 12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceDependency extends AbstractTestNG{
    private FioranoServiceRepository m_fioranoServiceRepository;
    private FioranoApplicationController m_fioranoApplicationController;
    private String resourceFilePath;
    private String m_appGUID;
    private Float m_version;
    private String m_appFile;

    public void init() throws Exception {
        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + getProperty("ApplicationFile");
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: " + m_appGUID + "\tVersion:: " + m_version);
        System.out.println("The Resource File Path:: " + resourceFilePath);
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "ServiceDependencyTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Component" + fsc + "ServiceDependency" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Component" + fsc + "ServiceDependency";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        printInitParams();
    }

    @Test(groups = "ServiceDependencyTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestServiceDependency"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestServiceDependency"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestServiceDependency"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceDependencyTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Resources and Connectivity Check")
    public void testCRC() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCRC", "TestServiceDependency"));
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("The Application with GUID " + m_appGUID + " is already running.");
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);

            //post condition
            //todo complete this

            Assert.assertTrue(true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCRC", "TestServiceDependency"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRC", "TestServiceDependency"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceDependencyTest", alwaysRun = true, dependsOnMethods = "testCRC", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestServiceDependency"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestServiceDependency"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestServiceDependency"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceDependencyTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "Adds service dependency for a component and checks for the change.")
    public void testServiceDependency() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testServiceDependency", "ServiceDependencyTest"));
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
                throw new Exception("couldn't add service dependency");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testServiceDependency", "ServiceDependencyTest"));


        }


        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testServiceDependency", "ServiceDependencyTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceDependencyTest", alwaysRun = true, dependsOnMethods = "testServiceDependency", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestServiceDependency"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "TestServiceDependency"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestServiceDependency"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceDependencyTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestServiceDependency"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestServiceDependency"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestServiceDependency"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

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

}
