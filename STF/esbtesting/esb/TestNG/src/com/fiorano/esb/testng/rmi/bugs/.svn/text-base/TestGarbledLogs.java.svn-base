package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
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
 * User: janardhan
 * Date: Dec 2, 2010
 * Time: 3:44:37 PM
 * To change this template use File | Settings | File Templates.
 */
//
public class TestGarbledLogs extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IServiceProviderManager SPManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName;
    private float appVersion = 1.0f;
    public TestGarbledLogs() {
        appName = new String("\u30dd" + "\u30ea" + "\u30b9");
        try {
            appName = new String(appName.getBytes("UTF8"), "UTF8");
        } catch (Exception e) {
            Assert.fail("Failed to get string! for the japanese event process", e);
        }
    }

    @Test(groups = "GarbledLogs", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.SPManager = rmiClient.getServiceProviderManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group GarbledLogs. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "GarbledLogs", description = "GarbledLogs bug 18518 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testGarbledLogs() {


        Logger.LogMethodOrder(Logger.getOrderMessage("testGarbledLogs", "TestGarbledLogs"));

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("police.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            //eventProcessManager.startEventProcess(appName, appVersion, false);
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "1");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

        String errStr = null;
        String outStr = null;
        try {
            errStr = SPManager.getLastErrLogs(1000);
            outStr = SPManager.getLastOutLogs(1000);
            //  System.out.println(errStr);
            //  System.out.println(outStr);
            System.out.println("TestGarbledLogs: the index of japaneese appName is " + outStr.indexOf(appName));
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGarbledLogs", "TestGarbledLogs"), e);
            Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        }
        if (outStr.indexOf(appName) == -1) {

            Assert.fail("Grabled Log");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testGarbledLogs", "TestGarbledLogs"));
    }

    @Test(groups = "GarbledLogs", dependsOnMethods = "testGarbledLogs", alwaysRun = true)
    public void stopAndDeleteApplication() {
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestGarbledLogs"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestGarbledLogs"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopAndDeleteApplication", "TestGarbledLogs"));
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
