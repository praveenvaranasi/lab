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
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/14/11
 * Time: 11:04 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestUpgrade extends AbstractTestNG{
    private FioranoServiceRepository m_fioranoServiceRepository;
    private FioranoApplicationController m_fioranoApplicationController;
    private String resourceFilePath;
    private String m_appGUID;
    private Float m_version;
    private String m_appFile;

    public void init() throws Exception {
        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + getProperty("ApplicationFile");
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: " + m_appGUID + "\tVersion:: " + m_version);
        System.out.println("The Resource File Path:: " + resourceFilePath);
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "UpgradeTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Component" + fsc + "Upgrade" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Component" + fsc + "Upgrade";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        printInitParams();
    }

    @Test(groups = "UpgradeTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "UpgradeTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "UpgradeTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "UpgradeTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "UpgradeTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "UpgradeTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    /**
     * Upgrades a component version and tests the change
     *
     * @throws Exception
     */
    @Test(groups = "UpgradeTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "Upgrades a component version and tests the change.")
    public void testUpgrade() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testUpgrade", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String highest_version = m_fioranoServiceRepository.getHighestVersionOfService("SMTP");
            System.out.println("version before change is::" + highest_version);
            Float highestversion = Float.parseFloat(highest_version);
            Float newhighestversion;
            newhighestversion = highestversion + 0.1f;
            String newhighest_version = newhighestversion.toString();
            m_fioranoServiceRepository.copyServiceVersion("SMTP", highest_version, "SMTP", newhighest_version);
            m_fioranoServiceRepository.commitService("SMTP", newhighest_version);
            System.out.println("version after upgrade ::" + m_fioranoServiceRepository.getHighestVersionOfService("SMTP"));

            if (m_fioranoServiceRepository.getHighestVersionOfService("SMTP") == highest_version)
                throw new Exception("Version of the component is not upgraded");

            //remove the last upgraded version so that in the next test run , service
            // could be again upgraded to same version , or else it gives version
            // already exists error.
            m_fioranoServiceRepository.removeService("SMTP", newhighest_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testUpgrade", "UpgradeTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testUpgrade", "UpgradeTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "UpgradeTest", alwaysRun = true, dependsOnMethods = "testUpgrade", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "UpgradeTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "UpgradeTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "UpgradeTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "UpgradeTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "UpgradeTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "UpgradeTest"), ex);
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
