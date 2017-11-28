package com.fiorano.esb.testng.rmi.scenario.application;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/12/11
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestWorkFlow extends AbstractTestNG{

    private String resourceFilePath;
    private boolean m_initialised;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private FioranoApplicationController m_fioranoApplicationController;

    public void init() throws Exception {
        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + getProperty("ApplicationFile");
        printInitParams();
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Application GUID:: " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("...........................................................................");
    }

    @BeforeClass(alwaysRun = true)
    public void startSetUp(){
        if (!m_initialised) {
            initializeProperties("scenario"+ fsc +"Application" + fsc + "Workflow" + fsc + "tests.properties");
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Application" + fsc + "Workflow";
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            m_initialised = true;
        }
    }

    @Test(groups = "WorkFlowTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "WorkFlowTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "WorkFlowTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "WorkFlowTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "WorkFlowTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "trying to perform CRC.")
    public void testCRC() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCRC", "WorkFlowTest"));
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("The Application with GUID " + m_appGUID + " is already running.");
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);

            //post condition
            //todo complete this

            Assert.assertTrue(true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCRC", "WorkFlowTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRC", "WorkFlowTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "WorkFlowTest", alwaysRun = true, dependsOnMethods = "testCRC", description = "Runs the application with transformation set.")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "WorkFlowTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "WorkFlowTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "WorkFlowTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "WorkFlowTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "tests the workflow properties of a port.")
    public void testWorkFlow() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testWorkflow", "WorkFlowTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            application.getServiceInstance("Feeder1").getOutputPortInstance("OUT_PORT").setWorkflow(1);
            application.getServiceInstance("Display1").getInputPortInstance("IN_PORT").setWorkflow(2);
            int one = application.getServiceInstance("Feeder1").getOutputPortInstance("OUT_PORT").getWorkflow();
            int two = application.getServiceInstance("Display1").getInputPortInstance("IN_PORT").getWorkflow();
            if (one != 1 && two != 2)
                throw new Exception("Workflow properties not set during run time");
            System.out.println("workflow type of feeder output port::" + one);
            System.out.println("workflow type of display input port::" + two);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testWorkflow", "WorkFlowTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWorkflow", "WorkFlowTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "WorkFlowTest", alwaysRun = true, dependsOnMethods = "testWorkFlow", description = "trying to import an application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "WorkFlowTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "WorkFlowTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "WorkFlowTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "WorkFlowTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "trying to import an application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "WorkFlowTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "WorkFlowTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "WorkFlowTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
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
}
