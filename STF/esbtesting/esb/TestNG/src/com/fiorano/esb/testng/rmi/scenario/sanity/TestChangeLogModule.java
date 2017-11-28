package com.fiorano.esb.testng.rmi.scenario.sanity;

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
 * Date: 10/17/11
 * Time: 4:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestChangeLogModule extends AbstractTestNG{
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String m_servinstance1;
    private String m_loglevel1;
    private String m_loglevel2;
    private String m_modulename1;

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_servinstance1 = testProperties.getProperty("ServiceInstance1");
        m_modulename1 = testProperties.getProperty("ModuleName1");
        m_loglevel1 = testProperties.getProperty("LogLevel1");
        m_loglevel2 = testProperties.getProperty("LogLevel2");
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        //System.out.println("Route GUID:: "+m_routeGUID);
        System.out.println("The Application File is::" + m_appFile);
        System.out.println("Service Instance::      " + m_servinstance1);
        //System.out.println("Loglevel defined is ::      "+m_loglevel1);
        //System.out.println("The New Transformation File::"+m_newTransformation);
        System.out.println("..................................................................");
    }

    @BeforeClass(groups = "ChangeLogModuleTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Sanity" + fsc + "ChangeLogModule" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Sanity" + fsc + "ChangeLogModule";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        printInitParams();
    }

    @Test(groups = "ChangeLogModuleTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestChangeLogModule"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestChangeLogModule"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestFESRestart"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ChangeLogModuleTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestChangeLogModule"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestChangeLogModule"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestFESRestart"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ChangeLogModuleTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "sets log level for an application.")
    public void testSetLogLevelForApplication() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("testSetLogLevelForApplication", "TestChangeLogModule"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            // Application application = ApplicationParser.readApplication(new File(m_appFile));
            //m_fioranoApplicationController.saveApplication(application);
            //m_fioranoApplicationController.compileApplication(m_appGUID,m_version);
            //m_fioranoApplicationController.prepareLaunch(m_appGUID,m_version);
            //m_fioranoApplicationController.launchApplication(m_appGUID,m_version);
            //m_fioranoApplicationController.startAllServices(m_appGUID);
            m_fioranoApplicationController.setLogLevel(m_appGUID,m_version, m_loglevel1);

            String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version, m_servinstance1, m_modulename1);
            System.out.println("loglevel is::" + loglevel);
            if (!loglevel.equalsIgnoreCase(m_loglevel1))
                throw new Exception("Log Level not set@" + m_loglevel1 + "@" + loglevel);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetLOgLevelForApplication", "ChangeLogModuleTest"));
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetLOgLevelForApplication", "ChangeLogMOduleTest"), e);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + e.getMessage());
        }
    }

    @Test(groups = "ChangeLogModuleTest", alwaysRun = true, dependsOnMethods = "testSetLogLevelForApplication", description = "sets log level for a service instance.")
    public void testSetLogLevelForServiceInstance() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("testSetLOgLevelForServiceInstance", "TestChangeLogModule"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            m_fioranoApplicationController.setLogLevel(m_appGUID,m_version, m_servinstance1, m_loglevel1);
            String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version, m_servinstance1, m_modulename1);
            System.out.println("loglevel is::" + loglevel);
            if (!loglevel.equalsIgnoreCase(m_loglevel1))
                throw new Exception("Log Level not set@" + m_loglevel1 + "@" + loglevel);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetLogLevelForServiceInstance", "ChangeLogModuleTest"));
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetLOgLevelForServiceInstance", "ChangeLogMOduleTest"), e);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + e.getMessage());
        }
    }

    @Test(groups = "ChangeLogModuleTest", alwaysRun = true, dependsOnMethods = "testSetLogLevelForServiceInstance", description = "sets log level for a module.")
    public void testSetLogLevelForModule() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("testSetLogLevelForMOdule", "TestChangeLogModule"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            m_fioranoApplicationController.setLogLevel(m_appGUID,m_version, m_servinstance1, m_modulename1, m_loglevel2);
            String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version, m_servinstance1, m_modulename1);
            System.out.println("loglevel is::" + loglevel);
            if (!loglevel.equalsIgnoreCase(m_loglevel2))
                throw new Exception("Log Level not set>>" + m_loglevel1 + "<<and>>" + loglevel + "<<");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetLogLevelForModule", "ChangeLogModuleTest"));
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetLOgLevelForModule", "ChangeLogMOduleTest"), e);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + e.getMessage());
        }
    }

    @Test(groups = "ChangeLogModuleTest", alwaysRun = true, dependsOnMethods = "testSetLogLevelForModule", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestChangeLogModule"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "TestChangeLogModule"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestFESRestart"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ChangeLogModuleTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestChangeLogModule"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestChangeLogModule"));
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
