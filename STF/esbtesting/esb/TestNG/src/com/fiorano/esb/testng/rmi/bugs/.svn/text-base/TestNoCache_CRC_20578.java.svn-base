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
 * User: janardhan
 * Date: 9/19/11
 * Time: 4:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestNoCache_CRC_20578 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "CACHE_CRC";
    private float appVersion = 1.0f;

    @Test(groups = "NoCache_CRC", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group ThreadDump. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "NoCache_CRC", description = "bug 19916: CRC should not clear entire user directory when cache is set to no ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfApplication() {


        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"));

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("cache_crc-1.0@EnterpriseServer.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
       /* try {
            eventProcessManager.startAllServices(appName, 1.0f);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }  */

        try {
            eventProcessManager.stopEventProcess(appName, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do crc!", e);
        }

        try {
            testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String fileSep = System.getProperty("file.separator");
            String resDir1 = testEnvConfig.getProperty("FIORANO_HOME") + fileSep + "runtimedata" + fileSep + "PeerServers" + fileSep + "profile1" +
                    fileSep + "FPS" + fileSep + "run" + fileSep + "components" + fileSep + "FileTransmitter" + fileSep + "4.0" + fileSep + "CACHE_CRC__1_0__FileTransmitter1";
            String resDir2 = testEnvConfig.getProperty("FIORANO_HOME") + fileSep + "runtimedata" + fileSep + "PeerServers" + fileSep + "profile1" +
                    fileSep + "FPS" + fileSep + "run" + fileSep + "components" + fileSep + "FileReceiver" + fileSep + "4.0" + fileSep + "CACHE_CRC__1_0__FileReceiver1";
            File f1 = new File(resDir1);
            File f2 = new File(resDir2);
            if (!f1.exists() || !f2.exists()) {
                //System.out.println("$FIORANO_HOME/runtimedata/repository/applications/applications_temp folder exists");
                throw new Exception();
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("CRC clearing entire user directory when cache is set to no");
        }


    }

    @Test(groups = "NoCache_CRC", dependsOnMethods = "TestLaunchOfApplication", alwaysRun = true)
    public void deleteApplication() {
        try {
            eventProcessManager.deleteEventProcess(appName, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteApplication", "TestNoCache_CRC_20578"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
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
