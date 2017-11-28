package com.fiorano.esb.testng.rmi.bugs;


import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
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
import java.util.List;
import java.util.logging.Level;


public class TestStopEPafterMsgSent21544 extends AbstractTestNG{

    private IRmiManager rmiManager;
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "EVENT_PROCESS2";
    private String appName1 = "EVENT_PROCESS1";
    private SampleEventProcessListener epListener = null;
    private TestEnvironment testenv;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initNewSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        testenv = ServerStatusController.getInstance().getTestEnvironment();
        testEnvConfig = ESBTestHarness.getTestEnvConfig();
        if (eventProcessManager == null) {
           // Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if (fpsManager == null) {
           // Assert.fail("Cannot run Group Bugs as fpsManager is not set.");
        }
    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "initNewSetup", alwaysRun = true)
    public void TestNewNone() throws Exception, RemoteException {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestNewNone", "TestStopEPafterMsgSent21544"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestNewNone", "TestStopEPafterMsgSent21544"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestStopEPafterMsgSent21544"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestStopEPafterMsgSent21544"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestStopEPafterMsgSent21544"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }
        try {

            deployEventProcess("EVENT_PROCESS1.zip");
            deployEventProcess("EVENT_PROCESS2.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestStopEPafterMsgSent21544"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestStopEPafterMsgSent21544"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

         rmiManager = rmiClient.getRmiManager();

        List<RouteMetaData> rList = eventProcessManager.getRoutesOfEventProcesses(appName,appVersion);
        RouteMetaData RMData = rList.get(0);

        List<RouteMetaData> rList1 = eventProcessManager.getRoutesOfEventProcesses(appName1,appVersion);
        RouteMetaData RMData1 = rList1.get(0);

        IDebugger DeBug = rmiClient.getDebugger();
        IDebugger DeBug1 = rmiClient.getDebugger();




        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestStopEPafterMsgSent21544"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestStopEPafterMsgSent21544"), e);
           // Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestStopEPafterMsgSent21544"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestStopEPafterMsgSent21544"), e);
           // Assert.fail("Failed to do start application!", e);
        }


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        BreakpointMetaData BMData = DeBug.addBreakpoint(appName,appVersion,RMData.getRouteName());

        String BPortSource = BMData.getSourceQueueName();
        String BPortTarget = BMData.getTargetQueueName();
        System.out.println(BPortSource + "  " + BPortTarget);

        BreakpointMetaData BMData1 = DeBug1.addBreakpoint(appName1,appVersion,RMData1.getRouteName());

        String BPortSource1 = BMData1.getSourceQueueName();
        String BPortTarget1 = BMData1.getTargetQueueName();
        System.out.println(BPortSource1 + "  " + BPortTarget1);

        //ports of EP2
        List <PortInstanceMetaData> PortList = eventProcessManager.getPortsForEventProcesses(appName,appVersion);
        System.out.println(PortList.size());
        PortInstanceMetaData[] Port = new PortInstanceMetaData[6];
        int i;
        for(i = 0;i<6;i++){
            Port[i] = PortList.get(i);
        }
        String[] name = new String[6];
        for (i = 0;i<6;i++){
            name[i] = Port[i].getActualDestinationName();
            System.out.println(name[i]);
        }

        //ports of EP1
        List <PortInstanceMetaData> PortList1 = eventProcessManager.getPortsForEventProcesses(appName1,appVersion);
        System.out.println(PortList1.size());
        int k = PortList1.size();
        PortInstanceMetaData[] Port1 = new PortInstanceMetaData[k];
        for(i = 0;i<k;i++){
            Port1[i] = PortList1.get(i);
        }
        String[] name1 = new String[k];
        for (i = 0;i<k;i++){
            name1[i] = Port1[i].getActualDestinationName();
            System.out.println(name1[i]);
        }

        //sending msgs for EP2
        String messageSent = topicpublisher("fps",name[1]);
        String IntermMsg = queuereceiver("fes", BPortSource);

        queuesender("fes", BPortTarget, IntermMsg);

        //sending msgs for EP1
        String messageSent1 = topicpublisher("fps",name1[1]);
        String IntermMsg1 = queuereceiver("fes", BPortSource1);

        queuesender("fes", BPortTarget1, IntermMsg1);

    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "TestNewNone", alwaysRun = true)
    public void ShutDownPeer() {

        try {
            fpsManager.shutdownPeer("fps");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("ShutDownPeer", "TestStopEPafterMsgSent21544"), e);
        } catch (ServiceException e) {
           Logger.Log(Level.SEVERE, Logger.getErrMessage("ShutDownPeer", "TestStopEPafterMsgSent21544"), e);
        }
    }

    /*@Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "ShutDownPeer", alwaysRun = true)
    public void FESLogOutnLogIn(){
        //rmiManager = rmiClient.getRmiManager();
        try {
            rmiManager.logout(rmiClient.getHandleID());
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("FESLogoutnLogIn", "TestStopEPafterMsgSent21544"), e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("FESLogoutnLogIn", "TestStopEPafterMsgSent21544"), e);
        }

    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "ShutDownPeer", alwaysRun = true)
    public void FESLogin(){
        String handle = null;
        try {
            handle  = rmiManager.login(rmiClient.getUserName(),rmiClient.getPassword());

        } catch (RemoteException e) {
           Logger.Log(Level.SEVERE, Logger.getErrMessage("FESLogoutnLogIn", "TestStopEPafterMsgSent21544"), e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("FESLogoutnLogIn", "TestStopEPafterMsgSent21544"), e);
        }
        try {
            rmiClient.setHandleID(handle);
        } catch (Exception e) {
             Logger.Log(Level.SEVERE, Logger.getErrMessage("FESLogoutnLogIn", "TestStopEPafterMsgSent21544"), e);
        }
    }*/

    @Test(groups = "Bugs", dependsOnMethods = "ShutDownPeer", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestStopEPafterMsgSent21544"));
        stopAndDeleteEP(eventProcessManager, appName1, appVersion);
        Logger.Log(Level.INFO, Logger.getFinishMessage("StopandDeleteApplication", "TestStopEPafterMsgSent21544"));
    }
    @Test(groups = "Bugs", dependsOnMethods = "stopAndDeleteApplication", alwaysRun = true)
    public void startPeerServer() {
        startServer(testenv, testEnvConfig, "fps", SERVER_TYPE.FPS);
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestStopEPafterMsgSent21544"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestStopEPafterMsgSent21544"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {

            //pub.close();
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "TestStopEPafterMsgSent21544"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "TestStopEPafterMsgSent21544"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        return messageOnDestination;
        //Assert.assertEquals(messageSent, messageOnDestination);

    }

    @Test(enabled = false)
    public void queuesender(String servername, String queuename, String messageSent) {

        Publisher qpub = new Publisher();
        qpub.setDestinationName(queuename);
        qpub.setCf("primaryQCF");
        qpub.setUser("anonymous");
        qpub.setPasswd("anonymous");
        try {
            qpub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuesender", "TestStopEPafterMsgSent21544"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        try {
            qpub.sendMessage(messageSent);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuesender", "TestStopEPafterMsgSent21544"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            qpub.close();
        }
    }
}

