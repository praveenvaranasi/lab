package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
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
 * User: Anubhav
 * Date: 8/23/12
 * Time: 15:04 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestServerStartHA_21887 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "BUG_21887";
    private SampleEventProcessListener epListener = null;
    private IDebugger debugger;
    private BreakpointMetaData BMData;
    public IServiceProviderManager serverlogs;
    public static IRmiManager rmiManager;
    private String HandleID;
    private float appVersion = 1.0f;


    @Test(groups = "Bugs", alwaysRun = true)
    public void initBug_Setup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            // Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if (fpsManager == null) {
            // Assert.fail("Cannot run Group Bugs as fpsManager is not set.");
        }
        this.debugger = rmiClient.getDebugger();
    }

    @Test(groups = "Bugs", description = "[HA]Unable to start the Servers in HA mode - bug 21887 ", dependsOnMethods = "initBug_Setup", alwaysRun = true)
    public void TestBUG_21887() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestBUG_21887", "TestServerStartHA_21887"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestBUG_21887", "TestServerStartHA_21887"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);


        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_21887.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do CRC in HA !", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do CRC in HA !", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do start application in HA !", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do start application in HA !", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, 1.0f);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do operation on service instance in HA !", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21887", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do operation on service instance in HA !", e);
        }

        try {
            eventProcessManager.stopEventProcess(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","TestServerStartHA_21887"),e);
            Assert.fail("Failed to stop EP in HA ",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","TestServerStartHA_21887"),e);
            Assert.fail("Failed to stop EP in HA ",e);
        }

    }
  
    @Test(groups="Bugs", dependsOnMethods="TestBUG_21887", alwaysRun = true)
    private void TestSendMessages(){
        try {
            deployEventProcess("BUG_21887.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestServerStartHA_21887"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestServerStartHA_21887"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do CRC in HA !", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do CRC in HA !", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do start application in HA !", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do start application in HA !", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to start all services in HA !", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to start all services in HA!", e);
        }


        try{
            topicpublisher("hafps", appName + "__" + "chat1" + "__OUT_PORT");
        }
        catch (Exception e)
        {
            Assert.fail("Failed to send message in HA mode", e);
        }
        try {
            eventProcessManager.stopEventProcess(appName, 1.0f);
            eventProcessManager.deleteEventProcess(appName, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages","TestServerStartHA_21887"),e);
            Assert.fail("Failed to stop and delete EP in HA ",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessages","TestServerStartHA_21887"),e);
            Assert.fail("Failed to stop and delete EP in HA ",e);
        }
    }

    @Test(enabled=false)
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

    @Test(enabled = false)
    public String topicpublisher(String servername, String topicname) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicname);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do create publisher to outport in HA ", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestServerStartHA_21887"), e);
            Assert.fail("Failed to do publish message on outport in HA ", e);
        } finally {

            // pub.close();
        }
        return messageSent;
    }

   
}
