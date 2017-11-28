package com.fiorano.esb.testng.rmi.scenario.application;

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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/13/11
 * Time: 10:46 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestSameApplication extends AbstractTestNG{

    private String resourceFilePath;
    private boolean m_initialised;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private FioranoApplicationController m_fioranoApplicationController;

    public void init() throws Exception {
        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + getProperty("ApplicationFile");
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
            initializeProperties("scenario"+ fsc +"Application" + fsc + "SameApplication" + fsc + "tests.properties");
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Application" + fsc + "SameApplication";
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            m_initialised = true;
        }
    }

    @Test(groups = "SameApplicationTest", alwaysRun = true, description = "Imports and application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "SameApplicationTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "SameApplicationTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SameApplicationTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application.")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "SameApplicationTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "SameApplicationTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SameApplicationTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "Creates a new application with the same name of an existing application.")
    public void testCreateSameApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateSameApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = new Application();//m_fioranoApplicationController.getApplication(BASE_APPLICATION,1.0f);
            application.setServiceInstances(new ArrayList());
            application.setRoutes(new ArrayList());
            application.setGUID(m_appGUID);
            List categories = new ArrayList();
            categories.add("CoreScenarioTests");
            application.setCategories(categories);
            application.setDisplayName(m_appGUID);
            application.setVersion(m_version);
            System.out.println("Saving application with same name and same version no");
            m_fioranoApplicationController.saveApplication(application);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateSameApplication", "SameApplicationTest"));
        }
        catch (Exception ex) {
            if (ex.getMessage().indexOf("APPLICATION_SAVE_FAILURE_ERROR") != -1)
                Assert.assertTrue(true);
            else {
                System.out.println("Exception in the Execution of test case::" + ex.getMessage());
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateSameApplication", "SameApplicationTest"), ex);
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }
        }
    }

    @Test(groups = "SameApplicationTest", alwaysRun = true, dependsOnMethods = "testCreateSameApplication", description = "Stops a running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "SameApplicationTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "SameApplicationTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SameApplicationTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the application which is stopped.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "SameApplicationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "SameApplicationTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "SameApplicationTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
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
