package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ramesh
 * Date: Dec 9, 2010
 * Time: 2:23:33 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestWSStubCleanup extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "WSSTUBCLEANUP";
    private SampleEventProcessListener epListener = null;
    private float appVersion = 1.0f;


    @Test(groups = "Bugs", alwaysRun = true)
    public void initWSStubcleanupSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Bugs. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "Bugs", description = "WsStub Cleanup bug 19999 ", dependsOnMethods = "initWSStubcleanupSetup", alwaysRun = true)
    public void TestLaunchOfWsStubCleanup() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("WSStubCleanup.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
            ;
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(25000);
        } catch (InterruptedException e) {
        }

        String ErrStr = null;
        try {
            ErrStr = eventProcessManager.getLastErrTrace(50, "WSStub2", appName, 1.0f);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            //Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"), e);
            //Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        }

        if (!ErrStr.contains("is already deployed")) {
           // Assert.fail("WSStub2 failed to reach the expected conditions");
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestLaunchOfWsStubCleanup", "TestWSStubCleanup"));
    }

    @Test(groups = "Bugs", dependsOnMethods = "TestLaunchOfWsStubCleanup", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestWSStubCleanup"));
        try {
            eventProcessManager.stopEventProcess(appName, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestWSStubCleanup"), e);
            //Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestWSStubCleanup"), e);
            //Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestWSStubCleanup"));
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" +
                File.separator + "resources" + File.separator + zipName));
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


