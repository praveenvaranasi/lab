package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
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
 * Date: Nov 18, 2010
 * Time: 3:26:27 PM
 */

public class TestJapanese extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = new String("\u30dd" + "\u30ea" + "\u30b9");
    private float appVersion = 1.0f;
    @Test(groups = "Japanese", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Japanese. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "Japanese", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfJapaneseApplication() {


        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfJapaneseApplication", "TestJapanese"));
        SampleEventProcessListener epListener = null;
        try {
            appGUID = new String(appGUID.getBytes("UTF8"), "UTF8");
        } catch (Exception e) {
            Assert.fail("Failed to get string!", e);
        }
        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            //eventProcessManager.addEventProcessListener(epListener, appGUID);
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("police.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            //eventProcessManager.startEventProcess(appGUID, appVersion, false);
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            //eventProcessManager.startAllServices(appGUID, appVersion);
            eventProcessManager.startAllServices(appGUID,appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "1");
            //we stop the component, so we can creat a recevier to the queue(inport),else 2 recievers are present for a qeueu.
            eventProcessManager.stopServiceInstance(appGUID,appVersion, appGUID);
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "0");

        //create a sub/publisher to those
        createPubSub();
        // Test passed Successfully
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestLaunchOfJapaneseApplication", "TestJapanese"));


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
        pub.setDestinationName(appGUID + "__" + "1_0" + "__" + appGUID + "__OUT_PORT");
        pub.setCf("PRIMARYTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

        Receiver rec = new Receiver();
        rec.setDestinationName(appGUID + "__" + "1_0" +"__" + appGUID + "__IN_PORT");
        rec.setCf("PRIMARYQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        Assert.assertEquals(messageSent, messageOnDestination);
    }

    @Test(groups = "Japanese", description = "Japanese component Launch on peer restart bug 19057 ", dependsOnMethods = "TestLaunchOfJapaneseApplication", alwaysRun = true)
    public void testComponentLaunchOnPeerRestartBug19057() {
        //clear map
        //https://bugzilla.fstpl.com/show_bug.cgi?id=19057
        Logger.LogMethodOrder(Logger.getOrderMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"));

        try {
            appGUID = new String(appGUID.getBytes("UTF8"), "UTF8");
        } catch (Exception e) {
            Assert.fail("Failed to get string!", e);
        }
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appGUID, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "1");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }


        try {
            rmiClient.getFpsManager().restartPeer("fps");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            Assert.fail("Failed to restart the peer");
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            Assert.fail("Failed to restart the peer");
        }

        // // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + "ポリス"), "0");
        eventStore.clear();
        try {
            Thread.sleep(50000);
        } catch (InterruptedException ignore) {

        }

        // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "1");

        //create pub/sub and send messages

        try {//we stop the component, so we can creat a recevier to the queue(inport),else 2 recievers are present for a qeueu.
            eventProcessManager.stopServiceInstance(appGUID,appVersion, appGUID);
        } catch (RemoteException e) {

        } catch (ServiceException e) {

        }
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
        }

        createPubSub();
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"));

    }

    @Test(groups = "Japanese", description = "Unable to get application headers for EP with japanese name ", dependsOnMethods = "testComponentLaunchOnPeerRestartBug19057", alwaysRun = true)
    public void testGetApplicationHeaders() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationHeaders", "TestJapanese"));
        try {

            FioranoServiceProvider sp = ServerStatusController.getInstance().getServiceProvider();

            FioranoApplicationController fac = sp.getApplicationController();
            fac.getApplicationHeaders(appGUID);
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationHeaders", "TestJapanese"), e);
            Assert.fail("Failed to get Application Headers", e);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationHeaders", "TestJapanese"), e);
            Assert.fail("Failed to get Application Headers", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testGetApplicationHeaders", "TestJapanese"));

    }

    @Test(groups = "Japanese", description = "stop & delete a japanese application", dependsOnMethods = "testGetApplicationHeaders", alwaysRun = true)
    public void testStopDeleteApplication() {
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, appVersion, true, false);

        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testStopDeleteApplication", "TestJapanese"));

    }


}
