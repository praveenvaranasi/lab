package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
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
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ramesh
 * Date: Dec 10, 2010
 * Time: 12:48:42 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestDisplayLaunch extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "DISPLAYLAUNCH";
    private SampleEventProcessListener epListener = null;
    private float appVersion = 1.0f;

    @Test(groups = "Bugs", alwaysRun = true)
    public void initDisplayLaunchSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
           // Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if (fpsManager == null) {
           // Assert.fail("Cannot run Group Bugs as fpsManager is not set.");
        }

    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "initDisplayLaunchSetup", alwaysRun = true)
    public void TestDisplayLaunchtypeNone() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"));
        //clear peer logs
        try {
            fpsManager.clearPeerErrLogs("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
        }
        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
			System.out.println(map);


        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("DisplayLaunch.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
           // Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
           // Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }

        String ErrStr = null;
        try {
            ErrStr = fpsManager.getLastPeerErrLogs("fps", 50);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
           // Assert.fail("Failed to do getLastPeerErrLogs on peer fps!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"), e);
           // Assert.fail("Failed to do getLastPeerErrLogs on peer fps!", e);
        }

        if (ErrStr.contains("java.lang.NullPointerException")) {
           Assert.fail("Display component failed to work properly as NPE occurred");
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestDisplayLaunchtypeNone", "TestDisplayLaunch"));
    }

    @Test(groups = "Bugs", dependsOnMethods = "TestDisplayLaunchtypeNone", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestDisplayLaunch"));
        stopAndDeleteEP(eventProcessManager, appName, appVersion);
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestDisplayLaunch"));
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
