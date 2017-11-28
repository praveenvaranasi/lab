package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironment;
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
 * Date: Dec 9, 2010
 * Time: 6:28:38 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestStopRemoteInstance extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IServiceProviderManager SPManager;
    private IFPSManager fpsmanager;
    private ExecutionController executioncontroller;
    private ServerStatusController serverstatus;
    private TestEnvironment testenv;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "SIMPLECHAT";
    private String appGUIDRemote = "SIMPLECHATREMOTE";
    private String Instance1 = "CHAT1";
    private String Instance2 = "CHAT3";
    private float appVersion = 1.0f;
    @Test(groups = "Remote", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.fpsmanager = rmiClient.getFpsManager();
        this.serverstatus = ServerStatusController.getInstance();
        this.testenv = serverstatus.getTestEnvironment();
        this.executioncontroller = ExecutionController.getInstance();
        this.SPManager = rmiClient.getServiceProviderManager();
        testEnvConfig = ESBTestHarness.getTestEnvConfig();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Japanese. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "Remote", description = "bug 19173 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfSIMPLECHAT() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"));
        try {
            SPManager.clearFESErrLogs();
            rmiClient.getFpsManager().clearPeerErrLogs("fps");
            rmiClient.getFpsManager().clearPeerErrLogs("fps1");
            Thread.sleep(5000);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"), e);
        }

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        //stopAndDeleteEP(eventProcessManager, appGUIDRemote, appVersion);
        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfSIMPLECHAT", "TestStopRemoteInstance"));
    }

    @Test(groups = "Remote", description = "bug 19173 ", dependsOnMethods = "TestLaunchOfSIMPLECHAT", alwaysRun = true)
    public void TestStartFPS1() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStartFPS1", "TestStopRemoteInstance"));
        try {
            if (!fpsmanager.isPeerRunning("fps1")) {
                startServer(testenv, testEnvConfig, "fps1", SERVER_TYPE.FPS);
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartFPS1", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to check peer running or not", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartFPS1", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to check peer running or not", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStartFPS1", "TestStopRemoteInstance"));
    }

    @Test(groups = "Remote", description = "bug 19173 ", dependsOnMethods = "TestStartFPS1", alwaysRun = true)
    public void TestLaunchOfRemoteEP() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"));

        SampleEventProcessListener epListenerRemote = null;

        try {
            epListenerRemote = new SampleEventProcessListener(appGUIDRemote, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListenerRemote);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("SIMPLECHATREMOTE.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do SAVE!", e);
        }
        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUIDRemote, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUIDRemote, appVersion, false);
            Thread.sleep(20000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfRemoteEP", "TestStopRemoteInstance"));
    }

    @Test(groups = "Remote", description = "bug 19173 ", dependsOnMethods = "TestLaunchOfRemoteEP", alwaysRun = true)
    public void TestSendMessages() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestSendMessages", "TestStopRemoteInstance"));
        try {
            eventProcessManager.stopServiceInstance(appGUIDRemote,appVersion, "chat3");
            Thread.sleep(6000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        createPubSub();
        try {
            eventProcessManager.startServiceInstance(appGUIDRemote,appVersion, "chat3");
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        try {
            rmiClient.getFpsManager().restartPeer("fps1");
            Thread.sleep(20000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to restart the peer fps1");
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to restart the peer fps1");
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to restart the peer fps1", e);
        }

        try {
            eventProcessManager.stopServiceInstance(appGUIDRemote,appVersion, "chat3");
            Thread.sleep(6000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        createPubSub();
        try {
            eventProcessManager.startServiceInstance(appGUIDRemote,appVersion, "chat3");
            Thread.sleep(6000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestSendMessages", "TestStopRemoteInstance"));
    }

    @Test(groups = "Remote", description = "bug 19173 ", dependsOnMethods = "TestSendMessages", alwaysRun = true)
    public void TestStopOfRemoteEP() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopOfRemoteEP", "TestStopRemoteInstance"));
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            Thread.sleep(10000);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopOfRemoteEP", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopOfRemoteEP", "TestStopRemoteInstance"));
    }

    @Test(groups = "Remote", description = "bug 19490 ", dependsOnMethods = "TestStopOfRemoteEP", alwaysRun = true)
    public void TestGetFPSandFESLogs() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestGetFPSandFESLogs", "TestStopRemoteInstance"));
        try {
            String str = rmiClient.getFpsManager().getLastPeerErrLogs("fps", 100);
            if (str.isEmpty())
                Assert.assertEquals(1, 1);
            else {
                if (!str.contains("Error while trying to execute request killApplication")) {
                    Assert.assertEquals(1, 1);
                } else
                    Assert.fail("Getting exception in FPS Logs");
            }
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetFPSandFESLogs", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to get FPS logs", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetFPSandFESLogs", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to get FPS logs", e);
        }

        try {
            String str = rmiClient.getFpsManager().getLastPeerErrLogs("fps1", 100);
            if (str.isEmpty())
                Assert.assertEquals(1, 1);
            else {
                if (!str.contains("Error while trying to execute request killApplication")) {
                    Assert.assertEquals(1, 1);
                } else
                    Assert.fail("Getting exception in FPS1 Logs");
            }
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetFPSandFESLogs", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to get FPS1 logs", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetFPSandFESLogs", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to get FPS1 logs", e);
        }

        try {
            String str;
            str = SPManager.getLastErrLogs(150);
            if (str.trim().isEmpty()) {
                Assert.assertEquals(1, 1);
            }
            else {
                if (!str.contains("Error in stopping Event Process")) {
                    Assert.assertEquals(1, 1);
                } else
                {
                    Assert.fail("Getting exception in FES Logs");
            }
            }
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetFPSandFESLogs", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to get FES logs", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetFPSandFESLogs", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to get FES logs", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestGetFPSandFESLogs", "TestStopRemoteInstance"));
    }

    @Test(groups = "Remote", description = "bug 19173 ", dependsOnMethods = "TestGetFPSandFESLogs", alwaysRun = true)
    public void TestKIllFPS1() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestKIllFPS1", "TestStopRemoteInstance"));
        try {
            rmiClient.getFpsManager().shutdownPeer("fps1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKIllFPS1", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to shutdown the peer fps1");
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKIllFPS1", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to shutdown the peer fps1");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestKIllFPS1", "TestStopRemoteInstance"));
    }

    @Test(groups = "Remote", description = "bug 19490 ", dependsOnMethods = "TestKIllFPS1", alwaysRun = true)
    public void TestDeletionOfEP() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDeletionOfEP", "TestStopRemoteInstance"));


        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKIllFPS1", "TestStopRemoteInstance"), e);
        }
        stopAndDeleteEP(eventProcessManager, appGUIDRemote, appVersion);

        startServer(testenv, testEnvConfig, "fps1", SERVER_TYPE.FPS);
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestDeletionOfEP", "TestStopRemoteInstance"));
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

    /**
     * Method to create FMQ pub/sub
     */
    @Test(enabled = false)
    public void createPubSub() {

        Publisher pub = new Publisher();
        pub.setDestinationName(appGUID + "__" + "1_0" + "__" + Instance1 + "__OUT_PORT");
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

        Receiver rec = new Receiver();
        rec.setDestinationName(appGUIDRemote + "__" + "1_0" + "__" + Instance2 + "__IN_PORT");
        rec.setCf("primaryQCF");
        rec.setUser("admin");
        rec.setPasswd("passwd");
        try {
            rec.initialize("fps1");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestStopRemoteInstance"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        Assert.assertEquals(messageSent, messageOnDestination);
    }
}

