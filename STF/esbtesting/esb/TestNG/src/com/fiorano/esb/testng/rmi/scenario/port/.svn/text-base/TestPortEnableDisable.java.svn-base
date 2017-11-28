package com.fiorano.esb.testng.rmi.scenario.port;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.logging.Level;

/**
* Created by IntelliJ IDEA.
* User: ranu
* Date: 10/14/11
* Time: 12:31 PM
* To change this template use File | Settings | File Templates.
*/
public class TestPortEnableDisable extends AbstractTestNG{
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
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

    @BeforeClass(groups = "PortEnableDisableTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Port" + fsc + "EnableDisablePort" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Port" + fsc + "EnableDisablePort";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        printInitParams();
    }

    @Test(groups = "PortEnableDisableTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestPoetEnableDisable"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestPoetEnableDisable"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestPoetEnableDisable"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortEnableDisableTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestPoetEnableDisable"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestPoetEnableDisable"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestPoetEnableDisable"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortEnableDisableTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "Disables a port and tests the change.")
    public void testDisablePort() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDisablePort", "EnableDisablePortTest"));
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

            Logger.Log(Level.INFO, Logger.getFinishMessage("testDisablePort", "EnableDisablePortTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDisablePort", "EnableDisablePortTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortEnableDisableTest", alwaysRun = true, dependsOnMethods = "testDisablePort", description = "Enables a port and tests the change.")
    public void testEnablePort() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testEnablePort", "EnableDisablePortTest"));
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

            Logger.Log(Level.INFO, Logger.getFinishMessage("testEnablePort", "EnableDisablePortTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEnablePort", "EnableDisablePortTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortEnableDisableTest", alwaysRun = true, dependsOnMethods = "testEnablePort", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestPoetEnableDisable"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "TestPoetEnableDisable"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestPoetEnableDisable"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortEnableDisableTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestPoetEnableDisable"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestPoetEnableDisable"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestPoetEnableDisable"), ex);
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
