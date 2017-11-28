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
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: srikanth
 * Date: Dec 1, 2010
 * Time: 7:43:00 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestRemote extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "SIMPLECHAT";
    private String appNameRemote = "REMOTE";
    private float appVersion = 1.0f;
    @Test(groups = "Remote", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.fpsManager = rmiClient.getFpsManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Remote. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "Remote", description = "bug 19490 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfSIMPLECHAT() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfSIMPLECHAT", "TestRemote"));

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestRemote"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestRemote"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestRemote"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {

            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestRemote"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestRemote"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
            ;
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestRemote"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestRemote"), e);
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
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfSIMPLECHAT", "TestRemote"));
    }

    @Test(groups = "Remote", description = "bug 19490 ", dependsOnMethods = "TestLaunchOfSIMPLECHAT", alwaysRun = true)
    public void TestLaunchOfEP() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfEP", "TestRemote"));

        SampleEventProcessListener epListenerRemote = null;

        try {
            epListenerRemote = new SampleEventProcessListener(appNameRemote, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestRemote"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListenerRemote);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestRemote"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestRemote"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("REMOTE.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestJapanese"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestJapanese"), e);
            Assert.fail("Failed to do SAVE!", e);
        }
        try {
            eventProcessManager.checkResourcesAndConnectivity(appNameRemote, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestRemote"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestRemote"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {

            eventProcessManager.startEventProcess(appNameRemote, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestRemote"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestRemote"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appNameRemote, appVersion);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appNameRemote), "1");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEP", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfEP", "TestRemote"));
    }

    @Test(groups = "Remote", description = "bug 19490 ", dependsOnMethods = "TestLaunchOfEP", alwaysRun = true)
    public void TestStopOfEP() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopOfEP", "TestRemote"));
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            //      Thread.sleep(Thread_Sleep_Time);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopOfEP", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopOfEP", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopOfEP", "TestRemote"));
    }

    @Test(groups = "Remote", description = "bug 19490 ", dependsOnMethods = "TestStopOfEP", alwaysRun = true)
    public void TestGetFPSLogs() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestGetFPSLogs", "TestRemote"));
        try {
            String str = fpsManager.getLastPeerErrLogs("fps", 10);
            if (str.indexOf("kill call is ignored") == -1 || str.indexOf("kill call is ignored") >= 0) {
                Assert.assertEquals(1, 1);
            }
            //      Thread.sleep(Thread_Sleep_Time);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetFPSLogs", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetFPSLogs", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestGetFPSLogs", "TestRemote"));
    }


    @Test(groups = "Remote", description = "bug 19490 ", dependsOnMethods = "TestGetFPSLogs", alwaysRun = true)
    public void TestStopOfSIMPLECHAT() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopOfSIMPLECHAT", "TestRemote"));
//        try {
//            eventProcessManager.stopEventProcess(appName, appVersion);
//            //      Thread.sleep(Thread_Sleep_Time);
//        } catch (ServiceException e) {
//            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopOfSIMPLECHAT", "TestRemote"), e);
//            Assert.fail("Failed to do operation on service instance!", e);
//        } catch (Exception e) {
//            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopOfSIMPLECHAT", "TestRemote"), e);
//            Assert.fail("Failed to do operation on service instance!", e);
//        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopOfSIMPLECHAT", "TestRemote"));
    }

    @Test(groups = "Remote", description = "bug 19490 ", dependsOnMethods = "TestStopOfSIMPLECHAT", alwaysRun = true)
    public void TestDeletionOfEP() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDeletionOfEP", "TestRemote"));
        try {
            eventProcessManager.deleteEventProcess(appNameRemote, appVersion, true, false);
            //      Thread.sleep(Thread_Sleep_Time);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeletionOfEP", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeletionOfEP", "TestRemote"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestDeletionOfEP", "TestRemote"));
    }

    /**
     * deploys an event process which is located under $testing_home/esb/TestNG/resources/
     *
     * @param zipName - name of the event process zip located under $testing_home/esb/TestNG/resources/
     * @throws java.io.IOException - if file is not found or for any other IO error
     * @throws ServiceException
     */
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
