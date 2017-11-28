package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.BreakpointMetaData;
import com.fiorano.esb.server.api.IDebugger;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: srikanth
 * Date: Nov 25, 2010
 * Time: 5:14:03 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestBreakpointReappears extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IDebugger debugger;
    private ExecutionController executioncontroller;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private TestEnvironmentConfig testenvconfig;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "SIMPLECHAT";
    private float appVersion = 1.0f;
    @Test(groups = "BreakpointReappears", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.serverstatus = ServerStatusController.getInstance();
        this.testenv = serverstatus.getTestEnvironment();
        this.debugger = rmiClient.getDebugger();
        this.executioncontroller = ExecutionController.getInstance();
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group BreakpointReapears. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfSIMPLECHAT() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"));
        try {
            deployEventProcess("SIMPLECHAT.zip");

        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"), e);
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfSIMPLECHAT", "TestBreakpointReappears"));
    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestLaunchOfSIMPLECHAT", alwaysRun = true)
    public void TestSetBreakPOint() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestSetBreakPOint", "TestBreakpointReappears"));
        try {
           // BreakpointMetaData bpmd1 = debugger.addBreakpoint("SIMPLECHAT", "route1");
            BreakpointMetaData bpmd1 = debugger.addBreakpoint("SIMPLECHAT",appVersion, "route1");
            Assert.assertNotNull(bpmd1.getSourceQueueName());

            String[] route = debugger.getRoutesWithDebugger(appName,appVersion);
            Assert.assertEquals(route.length, 1);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSetBreakPOint", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSetBreakPOint", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestSetBreakPOint", "TestBreakpointReappears"));
    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestSetBreakPOint", alwaysRun = true)
    public void TestStopandStart() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopandStart", "TestBreakpointReappears"));
        try {
            eventProcessManager.stopServiceInstance("SIMPLECHAT",appVersion, "chat2");
            //      Thread.sleep(Thread_Sleep_Time);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandStart", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandStart", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }


        try {
            eventProcessManager.startServiceInstance("SIMPLECHAT",appVersion, "chat2");
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandStart", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandStart", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandStart", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopandStart", "TestBreakpointReappears"));
    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestStopandStart", alwaysRun = true)
    public void TestRemoveBreakPOint() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestRemoveBreakPOint", "TestBreakpointReappears"));
        try {
            debugger.removeBreakpoint("SIMPLECHAT",appVersion, "route1");
            Thread.sleep(10000);
            String[] route = debugger.getRoutesWithDebugger(appName,appVersion);
            Assert.assertEquals(route.length, 0);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSetBreakPOint", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSetBreakPOint", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSetBreakPOint", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestSetBreakPOint", "TestBreakpointReappears"));
    }


    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestRemoveBreakPOint", alwaysRun = true)
    public void TestSendMessage() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestSendMessage", "TestBreakpointReappears"));
        try {

            eventProcessManager.stopServiceInstance(appName,appVersion, "chat2");
            Thread.sleep(6000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        String messageSent = topicpublisher("fps", appName +"__" + "1_0" + "__" + "chat1" + "__OUT_PORT");
        queuereceiver("fps", appName + "__" + "1_0" + "__" + "chat2" + "__IN_PORT", messageSent);
        try {
            eventProcessManager.startServiceInstance(appName,appVersion, "chat2");
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestSendMessage", "TestBreakpointReappears"));
    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestSendMessage", alwaysRun = true)
    public void TestKillFES() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestKillFES", "TestBreakpointReappears"));
        String FioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        File th = new File(FioranoHome);
        String location = th.getAbsolutePath() + File.separator + "esb/server/profiles/profile1/FES/pid.txt";
        String pid = null;
        String line = null;
        try {
            FileReader fr = new FileReader(location);
            BufferedReader br = new BufferedReader(fr);

            while ((line = br.readLine()) != null) {
                pid = line.trim();
            }
            fr.close();
            br.close();
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillFES", "TestBreakpointReappears"), e);
            Assert.fail("Failed to kill FES", e);
        }
        String os_name = System.getProperty("os.name");
        try {
            if (os_name.startsWith("Windows")) {
                Runtime.getRuntime().exec("taskkill /f /PID " + pid);
            } else {
                Runtime.getRuntime().exec("kill -9 " + pid);
            }
            rmiClient.cleanUp();
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillFES", "TestBreakpointReappears"), e);
            Assert.fail("Failed to kill FES", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestKillFES", "TestBreakpointReappears"));
    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestKillFES", alwaysRun = true)
    public void TestStartFES() {
        startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
        startSetup();
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStartFES", "TestBreakpointReappears"));
    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestStartFES", alwaysRun = true)
    public void TestVerifyBreakPoint() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestVerifyBreakPoint", "TestBreakpointReappears"));
        try {
            String[] route = debugger.getRoutesWithDebugger(appName,appVersion);
            if (route.length == 0)
                Assert.assertEquals(route.length, 0);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifyBreakPoint", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifyBreakPoint", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        TestSendMessage();
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestVerifyBreakPoint", "TestBreakpointReappears"));
    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestVerifyBreakPoint", alwaysRun = true)
    public void TestVerifySuspendedMessages() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"));
        TestSetBreakPOint();

        String messageSent = topicpublisher("fps", appName + "__" + "1_0" + "__" + "chat1" + "__OUT_PORT");

        TestKillFES();

        TestStartFES();

        try {
            String[] route = debugger.getRoutesWithDebugger(appName,appVersion);
            if (route.length == 1)
                Assert.assertEquals(route.length, 1);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }

        try {
            eventProcessManager.stopServiceInstance(appName,appVersion, "chat2");
            Thread.sleep(6000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

        queuereceiver("fes", appName + "__" + "1_0" + "__ROUTE1__C", messageSent);
        queuesender("fes", appName + "__" + "1_0" + "__ROUTE1__D", messageSent);
        queuereceiver("fps", appName + "__" + "1_0" + "__" + "chat2" + "__IN_PORT", messageSent);

        try {
            //eventProcessManager.startServiceInstance(appName, "chat2");
            eventProcessManager.startServiceInstance(appName,appVersion, "chat2");
            Thread.sleep(6000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        TestRemoveBreakPOint();
        TestSendMessage();
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestVerifySuspendedMessages", "TestBreakpointReappears"));
    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestVerifySuspendedMessages", alwaysRun = true)
    public void TestVerifyQueueCandD() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestVerifyQueueCandD", "TestBreakpointReappears"));
        Hashtable env = new Hashtable();

        env.put(Context.SECURITY_PRINCIPAL, "anonymous");
        env.put(Context.SECURITY_CREDENTIALS, "anonymous");
        String url = null;
        ArrayList arrlist;
        String qcf = "primaryQCF";
        InitialContext ic;
        try {
            arrlist = AbstractTestNG.getActiveFESUrl();
            int port = 1847;
            if (arrlist.get(1).equals("2047")) {
                port = 1847;
            }
            url = "http://" + arrlist.get(0) + ":" + port;
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.jms.runtime.naming.FioranoInitialContextFactory");
            env.put("BackupConnectURLs", "http://localhost:1956");
            ic = new InitialContext(env);
            try {
                javax.jms.Queue queue = (javax.jms.Queue) ic.lookup(appName + "__" + "1_0" + "__" + "ROUTE1__" + "C");
                Assert.fail(appName +"__1_0__" + "ROUTE1__" + "C" + "is still exists");
            } catch (Exception e) {
                boolean b = true;
                Assert.assertEquals(b, true);
            }

            try {
                javax.jms.Queue queue = (javax.jms.Queue) ic.lookup(appName + "__" + "1_0" + "__" + "ROUTE1__" + "D");
                Assert.fail(appName + "__1_0__" + "ROUTE1__" + "D" + "is still exists");
            } catch (Exception e) {
                boolean b = true;
                Assert.assertEquals(b, true);
            }

        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifyQueueCandD", "TestBreakpointReappears"), e);
            Assert.fail("Failed to get the FES url", e);
        } catch (NamingException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestVerifyQueueCandD", "TestBreakpointReappears"), e);
            Assert.fail("Failed to get the InitialContext Object", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestVerifyQueueCandD", "TestBreakpointReappears"));

    }

    @Test(groups = "BreakpointReappears", description = "bug 19947 ", dependsOnMethods = "TestVerifyQueueCandD", alwaysRun = true)
    public void TestStopEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopEventProcess", "TestBreakpointReappears"));
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopEventProcess", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopEventProcess", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopEventProcess", "TestBreakpointReappears"));

        try {
            deployEventProcess("SIMPLECHAT.zip");

        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDestinationsNotGettingDeleted22424Launch", "TestDestinationsNotGettingDeleted22424"), e);
            Assert.fail("Failed to do SAVE!", e);
        }
    }

    /**
     * Method to create FMQ pub/sub
     */
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {

            pub.close();
        }
        return messageSent;
    }

    @Test(enabled = false)
    public void queuereceiver(String servername, String queuename, String messageSent) {
        Receiver rec = new Receiver();
        rec.setDestinationName(queuename);
        rec.setCf("primaryQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        Assert.assertEquals(messageSent, messageOnDestination);

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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuesender", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        try {
            qpub.sendMessage(messageSent);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuesender", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            qpub.close();
        }
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
}


