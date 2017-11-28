package com.fiorano.esb.testng.rmi.scenario.application;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;

/**
* Created by IntelliJ IDEA.
* User: ranu
* Date: 11/24/11
* Time: 1:09 PM
* To change this template use File | Settings | File Templates.
*/
public class TestApplicationCache  extends AbstractTestNG {
    private String resourceFilePath;
    private boolean m_initialised;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private IEventProcessManager eventProcessManager;
    private FioranoApplicationController m_fioranoApplicationController;

    public void init() throws Exception {
        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + getProperty("ApplicationFile");
    }

    @BeforeClass(groups = "ApplicationCacheTest", alwaysRun = true)
    public void startSetUp() throws Exception {
        if (!m_initialised) {
            initializeProperties("scenario"+ fsc +"Application" + fsc + "ApplicationCache" + fsc + "tests.properties");
            resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Application" + fsc + "ApplicationCache";
            eventProcessManager = rmiClient.getEventProcessManager();
            m_fioranoApplicationController = ServerStatusController.getInstance().getServiceProvider().getApplicationController();
            init();
            m_initialised = true;
        }
        printInitParams();
    }

    @Test(groups = "ApplicationCacheTest", alwaysRun = true, description = "Imports an application.")
    public void testImportApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestApplicationCache"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            deployEventProcess(m_appFile);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestApplicationCache"));
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::"+getName());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestApplicationCache"), e);
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ApplicationCacheTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "changes the cache property of an application and tests the change.")
    public void testApplicationCache() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testApplicationCache", "ApplicationCacheTest"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            Application application = ApplicationParser.readApplication(new File(resourceFilePath+fsc+"CACHE_1.0.xml"));
            System.out.println("cache status before change::" + application.isComponentCached());
            application.setComponentCached(false);
            System.out.println("cache status after change::" + application.isComponentCached());
            m_fioranoApplicationController.saveApplication(application);
            //m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID);
            if (application.isComponentCached() == true)
                throw new Exception("Couldnt change the cache of application");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testApplicationCache", "ApplicationCacheTest"));

        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testApplicationCache", "ApplicationCacheTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ApplicationCacheTest", alwaysRun = true, dependsOnMethods = "testApplicationCache", description = "Resource and connectivity check is done.")
    public void testCRC() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testCRC", "TestApplicationCache"));
        System.out.println("Started the Execution of the TestCase::"+getName());
            //                     an Exception if the application is not present.
        try{
            eventProcessManager.checkResourcesAndConnectivity(m_appGUID, 1.0f);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCRC", "TestApplicationCache"));
            Assert.assertTrue(true);
        } catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRC", "TestApplicationCache"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ApplicationCacheTest", alwaysRun = true, dependsOnMethods = "testCRC", description = "Runs the application.")
    public void testRunApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestApplicationCache"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            eventProcessManager.startEventProcess(m_appGUID, 1.0f, false);
            //eventProcessManager.startAllServices(m_appGUID, 1.0f);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestApplicationCache"));
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestApplicationCache"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestApplicationCache"), e);
            Assert.fail("Failed to do start application!", e);
        } catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ApplicationCacheTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "stops a running application.")
    public void testKillApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestApplicationCache"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            eventProcessManager.stopEventProcess(m_appGUID, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestApplicationCache"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestApplicationCache"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch(Exception ex) {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ApplicationCacheTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the application which is stopped.")
    public void testDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestApplicationCache"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            eventProcessManager.deleteEventProcess(m_appGUID, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteApplication", "TestApplicationCache"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteApplication", "TestApplicationCache"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }

           /*Auxiliary Methods*/
    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Application GUID:: " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("...........................................................................");
    }
     @Test(enabled = false)
    private void deployEventProcess(String zipFile) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        bis = new BufferedInputStream(new FileInputStream(zipFile));
        boolean completed = false;
        byte result[];
        while (!completed) {
            byte[] temp = new byte[1024];
            int readcount = 0;
            readcount = bis.read(temp);

            if (readcount < 0) {
                completed = true;
                readcount = 0;
            }
            result = new byte[readcount];
            System.arraycopy(temp, 0, result, 0, readcount);

            // it will deploy the event process if the correct byte array is send; else gives an error
            this.eventProcessManager.deployEventProcess(result, completed);
        }
    }
}
