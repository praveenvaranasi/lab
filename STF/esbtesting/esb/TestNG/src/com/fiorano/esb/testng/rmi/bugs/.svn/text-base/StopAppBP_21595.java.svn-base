package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
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

public class StopAppBP_21595 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "BUG_21595";
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

    @Test(groups = "Bugs", description = "Should stop application irrespective of BP - bug 21595 ", dependsOnMethods = "initBug_Setup", alwaysRun = true)
    public void TestBug_21595() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestBug_21595", "StopAppBP_21595"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestBug_21595", "StopAppBP_21595"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);


        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_21595.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21595", "StopAppBP_21595"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        }
    }

    //ADD BP
    @Test(groups="Bugs", dependsOnMethods="TestBug_21595", alwaysRun = true)
    private void TestAddBP() {
        try {
            BreakpointMetaData bpmd1 = debugger.addBreakpoint("BUG_21595", appVersion, "route1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to do the operation!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to do the operation!", e);
        }
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","StopAppBP_21595"),e);
            Assert.fail("Failed to stop EP",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","StopAppBP_21595"),e);
            Assert.fail("Failed to stop EP",e);
        }
    }

   //REMOVE BP
   @Test(groups="Bugs", dependsOnMethods="TestAddBP", alwaysRun = true)
    private void TestRemBP() {
        try {
            deployEventProcess("BUG_21595.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to start application!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to start all services!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to start all services!", e);
        }
        try {
            BreakpointMetaData bpmd1 = debugger.addBreakpoint("BUG_21595",appVersion, "route1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to do the operation!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to do the operation!", e);
        }
        try {
            debugger.removeBreakpoint("BUG_21595",appVersion, "route1");
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to remove BP!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to remove BP!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP", "StopAppBP_21595"), e);
            Assert.fail("Failed to remove BP!", e);
        }
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP","StopAppBP_21595"),e);
            Assert.fail("Failed to stop EP",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemBP","StopAppBP_21595"),e);
            Assert.fail("Failed to stop EP",e);
        }
    }

    //ADD BP AND SEND MSG
    @Test(groups="Bugs", dependsOnMethods="TestRemBP", alwaysRun = true)
    private void TestSendMsgAdd(){
        try {
            deployEventProcess("BUG_21595.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to start all services!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to start all services!", e);
        }
        try {
            BMData = debugger.addBreakpoint("BUG_21595",appVersion, "route1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to do the operation!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to do the operation!", e);
        }
        String IntermMsg = null;

        String BPortSource = BMData.getSourceQueueName();
        String messageSent = topicpublisher("fps",appName + "__" + "1_0" + "__" + "chat1" + "__OUT_PORT");
        IntermMsg = queuereceiver("fes", BPortSource);
        if(IntermMsg ==null){
            try{
                throw new Exception();
            }catch(Exception e){
                Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestSendMsgAdd", "StopAppBP_21595"), e);
            }
        }else{
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestSendMsgAdd", "StopAppBP_21595"));
        }
        try {
            debugger.removeBreakpoint("BUG_21595",appVersion, "route1");
            Thread.sleep(5000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to remove BP!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to remove BP!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd", "StopAppBP_21595"), e);
            Assert.fail("Failed to remove BP!", e);
        }
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd","StopAppBP_21595"),e);
            Assert.fail("Failed to stop EP",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMsgAdd","StopAppBP_21595"),e);
            Assert.fail("Failed to stop EP",e);
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "StopAppBP_21595"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "StopAppBP_21595"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {

           // pub.close();
        }
        return messageSent;
    }

    @Test(enabled = false)
    public String queuereceiver(String servername, String queuename) {
        Receiver rec = new Receiver();
        rec.setDestinationName(queuename);
        rec.setCf("primaryQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "StopAppBP_21595"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "StopAppBP_21595"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        return messageOnDestination;
       // Assert.assertEquals(messageSent, messageOnDestination);

    }
}