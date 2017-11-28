package com.fiorano.esb.testng.rmi.scenario.sanity;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.service.Execution;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/17/11
 * Time: 3:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestChangeLaunch extends AbstractTestNG{
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String componentName = "SMTP1";

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("The Application File is::" + m_appFile);
        System.out.println("..................................................................");
    }

    @BeforeClass(groups = "ChangeLaunchTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Sanity" + fsc + "ChangeLaunch" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Sanity" + fsc + "ChangeLaunch";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        printInitParams();
    }

    @Test(groups = "ChangeLaunchTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestChangeLaunch"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestChangeLaunch"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestFESRestart"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ChangeLaunchTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Resources and Connectivity Check")
    public void testCRC() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCRC", "TestChangeLaunch"));
            //precondition checking whether the Aplication is there in the repository and not running
            //not checking whether the application is present in the repository as isApplicationRunnning() will throw
            //                      an Exception if the application is not present.
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("The Application with GUID " + m_appGUID + " is already running.");
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);

            //post condition
            //todo complete this

            Assert.assertTrue(true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCRC", "TestChangeLaunch"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRC", "ChangeLaunchTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ChangeLaunchTest", alwaysRun = true, dependsOnMethods = "testCRC", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestChangeLaunch"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestChangeLaunch"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestFESRestart"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ChangeLaunchTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "change the launch type during run time and tests the change.")
    public void testChangeLaunch() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testChangeLaunch", "TestChangeLaunch"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("service guid::" + m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstance(componentName).getGUID());
            System.out.println("service name::" + m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstance(componentName).getName());
            System.out.println("launch type before change is" + " " + m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstance(componentName).getLaunchType());

            m_fioranoApplicationController.stopService(m_appGUID,m_version, "SMTP1");

            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            application.getServiceInstance(componentName).setLaunchType(Execution.LAUNCH_TYPE_SEPARATE_PROCESS);
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID,m_version);
            m_fioranoApplicationController.startService(m_appGUID,m_version, "SMTP1");
            Thread.sleep(5000);
            System.out.println("Launch type after change is::" + application.getServiceInstance(componentName).getLaunchType());


            if (application.getServiceInstance(componentName).getLaunchType() == 2) {
                System.out.println("launch type after change is" + " " + application.getServiceInstance(componentName).getLaunchType());
                throw new Exception("couldnt change launch type");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testChangeLaunch", "TestChangeLaunch"));


        }
        catch (Exception ex) {

            System.out.println("Exception message::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangeLaunch", "ChangeLaunchTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of \n SCHEMA CONTENT NOT GETTING SAVED\n" + ex.getMessage());

        }
    }

    @Test(groups = "ChangeLaunchTest", alwaysRun = true, dependsOnMethods = "testChangeLaunch", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestChangeLaunch"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "TestChangeLaunch"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestFESRestart"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ChangeLaunchTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestChangeLaunch"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestChangeLaunch"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestFESRestart"), ex);
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

