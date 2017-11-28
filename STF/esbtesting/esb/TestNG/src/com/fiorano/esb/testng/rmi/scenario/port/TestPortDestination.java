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
* Time: 1:53 PM
* To change this template use File | Settings | File Templates.
*/
public class TestPortDestination extends AbstractTestNG{
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

    @BeforeClass(groups = "PortDestinationTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Port" + fsc + "PortDestination" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Port" + fsc + "PortDestination";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        printInitParams();
    }

    @Test(groups = "PortDestinationTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestPortDestination"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestPortDestination"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestPortDestination"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortDestinationTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestPortDestination"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestPortDestination"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestPortDestination"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortDestinationTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "sets the output port of chat1 to topic and input port of chat2 and chat3 to queue and tests the change")
    public void testChange1() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testChange1", "PortDestinationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").setDestinationType(0);
            m_fioranoApplicationController.saveApplication(application);
            startApplication(m_appGUID, m_version);
            System.out.println("Destination type of chat1::" + application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").destinationType);
            if (!(application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").destinationType == 0))
                throw new Exception("Unable to set chat1 outport desinationtype");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testChange1", "PortDestinationTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChange1", "PortDestinationTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortDestinationTest", alwaysRun = true, dependsOnMethods = "testChange1", description = "sets the output port of chat1 to Queue and input port of chat2 and chat3 to topic and tests the change")
    public void testChange2() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testChange2", "PortDestinationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").setDestinationType(0);
            application.getServiceInstance("chat2").getInputPortInstance("IN_PORT").setDestinationType(1);
            application.getServiceInstance("chat3").getInputPortInstance("IN_PORT").setDestinationType(1);
            m_fioranoApplicationController.saveApplication(application);
            application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            startApplication(m_appGUID, m_version);
            System.out.println("Destination type of chat1::" + application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").destinationType);
            System.out.println("Destination type of chat2::" + application.getServiceInstance("chat2").getInputPortInstance("IN_PORT").destinationType);
            System.out.println("Destination type of chat3::" + application.getServiceInstance("chat3").getInputPortInstance("IN_PORT").destinationType);
            if (!(application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").getDestinationType() == 0))
                throw new Exception("Unable to set chat1 outport desinationtype");
            if (!(application.getServiceInstance("chat2").getInputPortInstance("IN_PORT").getDestinationType() == 1))
                throw new Exception("Unable to set chat2 inport desinationtype");
            if (!(application.getServiceInstance("chat3").getInputPortInstance("IN_PORT").getDestinationType() == 1))
                throw new Exception("Unable to set chat3 inport desinationtype");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testChange2", "PortDestinationTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChange2", "PortDestinationTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortDestinationTest", alwaysRun = true, dependsOnMethods = "testChange2", description = "sets the output port of chat1 to Topic and input port of chat2 and chat3 to topic and tests the change")
    public void testChange3() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testChange3", "PortDestinationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            stopApplication(m_appGUID);
            System.out.println("Here1");
            application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").setDestinationType(1);
            application.getServiceInstance("chat2").getInputPortInstance("IN_PORT").setDestinationType(0);
            application.getServiceInstance("chat3").getInputPortInstance("IN_PORT").setDestinationType(0);
            m_fioranoApplicationController.saveApplication(application);
            application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            startApplication(m_appGUID, m_version);
            System.out.println("Destination type of chat1::" + application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").destinationType);
            System.out.println("Destination type of chat2::" + application.getServiceInstance("chat2").getInputPortInstance("IN_PORT").destinationType);
            System.out.println("Destination type of chat3::" + application.getServiceInstance("chat3").getInputPortInstance("IN_PORT").destinationType);
            if (!(application.getServiceInstance("chat1").getOutputPortInstance("OUT_PORT").getDestinationType() == 1))
                throw new Exception("Unable to set chat1 outport desinationtype");
            if (!(application.getServiceInstance("chat2").getInputPortInstance("IN_PORT").getDestinationType() == 0))
                throw new Exception("Unable to set chat2 inport desinationtype");
            if (!(application.getServiceInstance("chat3").getInputPortInstance("IN_PORT").getDestinationType() == 0))
                throw new Exception("Unable to set chat3 inport desinationtype");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testChange3", "PortDestinationTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChange3", "PortDestinationTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortDestinationTest", alwaysRun = true, dependsOnMethods = "testChange3", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestPortDestination"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "TestPortDestination"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestPortDestination"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "PortDestinationTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestPortDestination"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestPortDestination"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestPortDestination"), ex);
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
