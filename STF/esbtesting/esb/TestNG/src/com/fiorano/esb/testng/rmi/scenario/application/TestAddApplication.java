package com.fiorano.esb.testng.rmi.scenario.application;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
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
 * Time: 11:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestAddApplication extends AbstractTestNG{
    private String resourceFilePath;
    private boolean m_initialised;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private IEventProcessManager eventProcessManager;

    @BeforeClass(groups = "AddApplicationTest", alwaysRun = true, description = "performs initial setup")
    public void startSetUp() throws Exception {
        if (!m_initialised) {
            initializeProperties("scenario"+ fsc +"Application" + fsc + "CRC" + fsc + "tests.properties");
            eventProcessManager = rmiClient.getEventProcessManager();
            resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Application" + fsc +  "CRC";
            m_appGUID = getProperty("ApplicationGUID");
            m_version = Float.parseFloat(getProperty("ApplicationVersion"));
            m_appFile = resourceFilePath + File.separator + getProperty("ApplicationFile");
            printInitParams();
            m_initialised = true;
        }
    }

    @Test(groups = "AddApplicationTest", alwaysRun = true, description = "Imports the application.")
    public void testImportApplication() {
          System.out.println("Started the Execution of the TestCase::"+getName());
        try {
            deployEventProcess(m_appFile);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestAddApplication"));
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::"+getName());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestAddApplication"), e);
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "AddApplicationTest", alwaysRun = true, dependsOnMethods = "testImportApplication",description = "Performs CRC")
    public void testCRC(){
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            eventProcessManager.checkResourcesAndConnectivity(m_appGUID, 1.0f);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCRC", "TestAddApplication"));
            Assert.assertTrue(true);
        }
        catch(Exception ex) {
            System.err.println("Exception in the Execution of test case::"+getName());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRC", "TestAddApplication"), ex);
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }

    @Test(groups = "AddApplicationTest", alwaysRun = true, dependsOnMethods = "testCRC",description = "Runs the application with transformation set.")
    public void testRunApplication() {
        System.out.println("Started the Execution of the TestCase::"+getName());
        try {
            eventProcessManager.startEventProcess(m_appGUID, 1.0f, false);
            //eventProcessManager.startAllServices(m_appGUID, 1.0f);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestAddApplication"));
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestAddApplication"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestAddApplication"), e);
            Assert.fail("Failed to do start application!", e);
        } catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "AddApplicationTest", alwaysRun = true, dependsOnMethods = "testRunApplication",description = "Kills the application with transformation set.")
    public void testKillApplication() {
            System.out.println("Started the Execution of the TestCase::"+getName());
        try {
            eventProcessManager.stopEventProcess(m_appGUID, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestAddApplication"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestAddApplication"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch(Exception ex) {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }

    @Test(groups = "AddApplicationTest", alwaysRun = true, dependsOnMethods = "testKillApplication",description = "Runs the application with transformation set.")
    public void testDeleteApplication() {
            System.out.println("Started the Execution of the TestCase::"+getName());
        try {
            eventProcessManager.deleteEventProcess(m_appGUID, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteApplication", "TestAddApplication"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteApplication", "TestAddApplication"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
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
