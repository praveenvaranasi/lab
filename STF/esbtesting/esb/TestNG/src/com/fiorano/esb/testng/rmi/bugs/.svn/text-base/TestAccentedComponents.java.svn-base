package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import junit.framework.Assert;
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
 * Date: 1/17/11
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestAccentedComponents extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "COMPONENTWITHACCENTEDNAMES";
    private float appVersion = 1.0f;
    @Test(groups = "AccentedComponent", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Accented component. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "AccentedComponent", description = "bug:19088:Components with accented characters in display names are not fetched when peer is restarted ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchAccentedComponent() {

        Logger.LogMethodOrder(Logger.getOrderMessage("Accentedcomponent", "TestLaunchAccentedComponent"));
        SampleEventProcessListener sepl = null;
        if (System.getProperty("os.name").startsWith("Windows")) {
            try {
                sepl = new SampleEventProcessListener(appGUID, eventStore);
                eventProcessManager.addEventProcessListener(sepl);
                Thread.sleep(5000);
                deployEventProcess("componentwithaccentednames-1.0@EnterpriseServer.zip");
                Thread.sleep(4000);
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
                Thread.sleep(5000);
                eventProcessManager.startEventProcess(appGUID, appVersion, false);

                Thread.sleep(5000);
                eventStore.clear();
                eventProcessManager.startAllServices(appGUID, appVersion);
                Thread.sleep(5000);
                // // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "2");
                rmiClient.getFpsManager().restartPeer("fps");
                eventStore.clear();
                Thread.sleep(20000);
                // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "2");
                eventProcessManager.stopServiceInstance(appGUID,appVersion, "Display1");
                String accentedstr = new String("F" + "\u00cd" + "DER");
                //F\u00cdDER
                createPub("COMPONENTWITHACCENTEDNAMES__1_0__" + accentedstr + "__OUT_PORT");
                createQueueSub("COMPONENTWITHACCENTEDNAMES__1_0__DISPLAY1__IN_PORT", "fps");
                testStopDeleteApplication();

            } catch (Exception e) {
                Assert.fail("Failed while launching Accented component");
            }
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

    @Test(enabled = false)
    public void createPub(String topicOrQueueName) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicOrQueueName);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do create publisher to outport", e);
        }
        try {
            for (int i = 0; i < 5; i++) {
                pub.sendMessage("hello Fiorano");
            }
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

    }


    @Test(enabled = false)
    public void testStopDeleteApplication() {
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchAccentedComponent", "TestAccentedComponent"), e);
            org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchAccentedComponent", "TestAccentedComponent"), e);
            org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
    }

    @Test(enabled = false)
    public void createQueueSub(String queueName, String serverName) {

        Receiver rec = new Receiver();
        rec.setDestinationName(queueName);
        rec.setCf("primaryQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize(serverName);
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            for (int i = 0; i < 5; i++) {
                messageOnDestination = rec.getMessageOnDestination();
                if (!messageOnDestination.equalsIgnoreCase("hello Fiorano")) {
                    Assert.fail("Failed while comparing the received messages");
                }
            }
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }
    }
}
