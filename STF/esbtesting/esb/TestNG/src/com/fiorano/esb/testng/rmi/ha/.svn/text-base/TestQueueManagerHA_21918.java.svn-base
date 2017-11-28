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
import java.util.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Anubhav
 * Date: 8/23/12
 * Time: 15:04 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestQueueManagerHA_21918 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "BUG_21918";
    private SampleEventProcessListener epListener = null;
    private IDebugger debugger;
    private BreakpointMetaData BMData;
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

    @Test(groups = "Bugs", description = "Queue Manager feature is not working in HA - bug 21918 ", dependsOnMethods = "initBug_Setup", alwaysRun = true)
    public void TestBug_21918() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestBug_21918", "TestQueueManagerHA_21918"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestBug_21918", "TestQueueManagerHA_21918"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e1);

        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);


        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);

        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);

        }

        try {
            deployEventProcess("BUG_21918.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);

        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);

        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do start application!", e);
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

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21918", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

        try {
            eventProcessManager.stopEventProcess(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","TestQueueManagerHA_21918"),e);
            Assert.fail("Failed to stop EP",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","TestQueueManagerHA_21918"),e);
            Assert.fail("Failed to stop EP",e);
        }

    }
    //ADD BP AND SEND MSG
    @Test(groups="Bugs", dependsOnMethods="TestBug_21918", alwaysRun = true)
    private void TestGetPort(){
        try {
            deployEventProcess("BUG_21918.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort", "TestQueueManagerHA_21918"), e);

        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort", "TestQueueManagerHA_21918"), e);

        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do start application!", e);
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

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to start all services!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to start all services!", e);
        }




        try{
            topicpublisher("fps",appName + "__" + "Feeder1" + "__OUT_PORT");
        }
        catch (Exception e)
        {
            Assert.fail("Failed to send message", e);
        }


    try{
            List<PortInstanceMetaData> portInstMetaData1 = new ArrayList<PortInstanceMetaData>();
            portInstMetaData1= eventProcessManager.getPortsForEventProcesses(appName, 1.0f);
            try{
                int a= portInstMetaData1.size();
                if(a!=5)
                {
                    Assert.fail("Failed to get all instances");
                }
            }
            catch(OutOfMemoryError e)
            {

            }

        }   catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort","TestQueueManagerHA_21918"),e);
            Assert.fail("Failed to get all Port Instances",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort","TestQueueManagerHA_21918"),e);
            Assert.fail("Failed to get all Port Instances",e);
        }

        try {
            eventProcessManager.stopEventProcess(appName, 1.0f);
            eventProcessManager.deleteEventProcess(appName, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort","TestQueueManagerHA_21918"),e);
            Assert.fail("Failed to stop and delete EP",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetPort","TestQueueManagerHA_21918"),e);
            Assert.fail("Failed to stop and delete EP",e);
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestQueueManagerHA_21918"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {

           // pub.close();
        }
        return messageSent;
    }


}
