package com.fiorano.esb.testng.rmi.scenario.component;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.Route;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.service.Service;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Enumeration;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/14/11
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestOSCompatibility extends AbstractTestNG{
    private FioranoServiceRepository m_fioranoServiceRepository;
    private FioranoApplicationController m_fioranoApplicationController;
    private String resourceFilePath;
    private String m_appGUID;
    private Float m_version;
    private String m_appFile;
    private Float m_version4;

    public void init() throws Exception {
        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_version4 = Float.parseFloat(getProperty("ApplicationVersion4"));
        m_appFile = resourceFilePath + File.separator + getProperty("ApplicationFile");
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: " + m_appGUID + "\tVersion:: " + m_version);
        System.out.println("The Resource File Path:: " + resourceFilePath);
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "OSCompatibilityTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Component" + fsc + "OsCompatability" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Component" + fsc + "OsCompatability";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        printInitParams();
    }

    @Test(groups = "OSCompatibilityTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {

            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                //System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "OsCompatibilityTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "OsCompatibilityTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "OSCompatibilityTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "OsCompatibilityTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "OsCompatibilityTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "OSCompatibilityTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "Changes the component os type to incompatible os type and tests the change.")
    public void testOSCompatibility() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testOSCompatibility", "OsCompatibilityTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testOSCompatibility", "OsCompatibilityTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testOsCompatibility", "OsCompatibilityTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }
                        /*
    @Test(groups = "OSCompatibilityTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "Checks the error logs of fps due to the change of component os to incompatible type")
    public void testFPSlogs() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testFPSlogs", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String str = rtlClient.getFioranoFPSManager().getTPSLastErrLogs(100, "fps");
            System.out.println("error logs" + str);
            String str1 = rtlClient.getFioranoFPSManager().getTPSLastOutLogs(100, "fps");
            System.out.println("out logs::" + str1);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testFPSlogs", "OsCompatibilityTest"));


        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testFPSlogs", "OsCompatibilityTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }*/

    @Test(groups = "OSCompatibilityTest", alwaysRun = true, dependsOnMethods = "testOSCompatibility", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "OsCompatibilityTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log( Level.SEVERE, Logger.getErrMessage("testKillApplication", "OsCompatibilityTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "OSCompatibilityTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "OsCompatibilityTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "OsCompatibilityTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "OsCompatibilityTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

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
}
