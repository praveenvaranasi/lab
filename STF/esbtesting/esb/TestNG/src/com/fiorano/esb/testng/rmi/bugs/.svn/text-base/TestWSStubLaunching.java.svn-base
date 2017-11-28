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
 * User: sudharshan
 * Date: Nov 24, 2010
 * Time: 9:42:03 AM
 * To change this template use File | Settings | File Templates.
 */

public class TestWSStubLaunching extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "EVENT_PROCESS1";
    private float appVersion = 1.0f;

    @Test(groups = "WSStubLaunching", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Japanese. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "WSStubLaunching", description = "WSStub launch bug 20095 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfWSStubApplication() {


        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfWSStubApplication", "TestWSStubLaunching"));

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("event_process1-1.0@EnterpriseServer.zip");
            eventProcessManager.clearApplicationLogs(appName, appVersion);
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do start application!", e);
        }


        try {

            Thread.sleep(5000);

        } catch (InterruptedException e) {
        }


        try {
            eventProcessManager.synchronizeEventProcess(appName, appVersion);


        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to synchronize application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to synchronize application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();

        String errStr = null;
        try {
            errStr = eventProcessManager.getLastErrTrace(20, "WSStub1", appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        }

        if (errStr.contains("Error in starting application")) {
            Assert.fail("Failed to start WSStubLaunching service instance an Exception occured!");
        }

        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"));

    }

    @Test(groups = "WSStubLaunching", dependsOnMethods = "TestLaunchOfWSStubApplication", alwaysRun = true)
    public void stopAndDeleteApplication() {
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestWSStubLaunching"));

    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + "/esb/TestNG/resources/" + zipName));
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
