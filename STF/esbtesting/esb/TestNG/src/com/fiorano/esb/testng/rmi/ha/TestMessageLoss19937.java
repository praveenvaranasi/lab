package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.server.api.IEventProcessManager;
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
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.Attribute;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 2/22/11
 * Time: 4:06 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestMessageLoss19937 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IServiceProviderManager serviceprovidermanager;
    private ExecutionController executioncontroller;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private static TestEnvironmentConfig testenvconfig = ESBTestHarness.getTestEnvConfig();
    private static String Fiorano_home = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
    private static File fioranoServerfile = new File(Fiorano_home + "/esb/server/bin");
    private MachineElement machine1;
    private ServerElement serverElement;
    private ProfileElement profileElement;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private float appVersion = 1.0f;
    private String appName1 = "EVENTPROCESS19937";
    private String appName2 = "EVENTPROCESSTIMERDIS19937";
    private String appName4 = "EVENTPROCESSTIMER_DISPLAY_INMEM_LPC19937";
    private String appName3 = "EVENTPROCESSTIMER_DISPLAY_INMEMORY_19937";
    public int compareresult, result;

    @Test(groups = {"TestMessageLoss19937"}, alwaysRun = true)
    public void startSetup() {
        this.serverstatus = ServerStatusController.getInstance();
        this.testenv = serverstatus.getTestEnvironment();
        this.executioncontroller = ExecutionController.getInstance();
        this.serviceprovidermanager = rmiClient.getServiceProviderManager();
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group AppRepoSync. as eventProcessManager is not set.");
        }
    }



    @Test(groups = {"TestMessageLoss19937"}, description = "bug 19937", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testMessageLoss_1() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testMessageLoss_1", "TestMessageLoss19937"));

        try {
            SampleEventProcessListener eplistener;
            eplistener = new SampleEventProcessListener(appName1, eventStore);
            eventProcessManager.addEventProcessListener(eplistener);

            deployEventProcess("eventprocess19937-1.0@EnterpriseServer.zip");
            System.out.println("EP Delpoyed in MessageLoss");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testMessageLoss_1", "TestMessageLoss19937"), e);
            Assert.fail("failed to save the application");
        }

        try {
            //1st test case (Non-transacted session)
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            //eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(15000);
            //eventProcessManager.stopServiceInstance(appName1,appVersion, "Display1");
            // eventProcessManager.stopServiceInstance(appName1, "Feeder1");
            Receiver receiverForApp1 = testMessageListerForReceiver("EVENTPROCESS19937__1_0__DISPLAY1__IN_PORT");
            new PublishMessagesThread().start();
            testActiveToStandaloneServer();
            result = receiverForApp1.getCount();
            Thread.sleep(10000);
            compareresult = receiverForApp1.getCount();
            while (compareresult != result) {
                Thread.sleep(20000);
                result = receiverForApp1.getCount();
                if (result == 100000) {
                    break;
                } else {
                    Thread.sleep(50000);
                    compareresult = receiverForApp1.getCount();
                }
            }
            if (result != 100000) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testMessageLoss", "TestMessageLoss19937"));
                Assert.fail("Failed as message loss occurs in first testcase. No.of messages received:" + result);
            }
            testStopDeleteApplication(appName1);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testMessageLoss_1", "TestMessageLoss19937"), e);
            //Assert.fail("failed to start the first application ", e);
        }

    }

    @Test(groups = {"TestMessageLoss19937"}, description = "bug 19937", dependsOnMethods = "testMessageLoss_1", alwaysRun = true)
    public void testMessageLoss_2() {
        try {
            SampleEventProcessListener eplistener;
            eplistener = new SampleEventProcessListener(appName2, eventStore);
            eventProcessManager.addEventProcessListener(eplistener);
            deployEventProcess("eventprocesstimerdis19937-1.0@EnterpriseServer.zip");
            Logger.LogMethodOrder(Logger.getOrderMessage("testMessageLoss_2", "TestMessageLoss19937"));
            //2nd test case (Transacted Session, Separate Process):
            eventProcessManager.checkResourcesAndConnectivity(appName2, appVersion);
            eventProcessManager.startEventProcess(appName2, appVersion, false);
            //eventProcessManager.startServiceInstance(appName2, appVersion,"Display1");
            Thread.sleep(10000);
            eventProcessManager.stopServiceInstance(appName2,appVersion, "Display1");
            Receiver receiverForApp2 = testMessageListerForReceiver("EVENTPROCESSTIMERDIS19937__1_0__DISPLAY1__IN_PORT");
            eventProcessManager.startServiceInstance(appName2,appVersion, "Timer1");
            testActiveToStandaloneServer();
            result = receiverForApp2.getCount();
            Thread.sleep(10000);
            compareresult = receiverForApp2.getCount();
            while (compareresult != result) {
                Thread.sleep(15000);
                result = receiverForApp2.getCount();
                if (result == 100000) {
                    break;
                } else {
                    Thread.sleep(50000);
                    compareresult = receiverForApp2.getCount();
                }
            }
            if (result != 100000) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testMessageLoss", "TestMessageLoss19937"));
                Assert.fail("Failed as message loss occurs in second testcase. No.of messages received:" + result);
            }
            testStopDeleteApplication(appName2);


        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testMessageLoss_2", "TestMessageLoss19937"), e);
            //Assert.fail("failed to start the second application ", e);
        }
    }

    Attribute attribute = null;
    MBeanServerConnection mbsc = null;
    ObjectName mem = null;

    @Test(groups = "TestMessageLoss19937", description = "bug 19937", dependsOnMethods = "testMessageLoss_2", alwaysRun = true)
    public void testMessageLoss_3() {

        Logger.LogMethodOrder(Logger.getOrderMessage("testMessageLoss_3", "TestMessageLoss19937"));
        //3rd test case (Transacted Session, InMemory, Non-LPC):
        //int result = 0, compareresult = 0;
        try {
            SampleEventProcessListener eplistener;
            eplistener = new SampleEventProcessListener(appName3, eventStore);
            eventProcessManager.addEventProcessListener(eplistener);
            deployEventProcess("eventprocesstimer_display_inmemory_19937-1.0@EnterpriseServer.zip");
            int port;
            String url = rmiClient.getFpsManager().getConnectURLForPeer("hafps");
            if (url.endsWith("1867")) {   // if url ends with 1867 , it means primary is active ,so shutdown secondary server & vice-versa
                port = 2067;
            } else port = 2068;
            jmxClient.cleanUp();
            jmxClient = JMXClient.getInstance("admin", "passwd", port);

            mem = new ObjectName(
                    "Fiorano.Esb.Peer.Launch:ServiceType=InMemoryJavaLauncher,Name=InMemoryJavaLauncher,type=config");
            mbsc = jmxClient.getMBeanServerConnection();

            attribute = new Attribute("UseLPCForInMemoryComponents", false);
            mbsc.setAttribute(mem, attribute);
            //System.out.println(mbsc.getAttribute(mem, "UseLPCForInMemoryComponents"));
            eventProcessManager.checkResourcesAndConnectivity(appName3, appVersion);
            eventProcessManager.startEventProcess(appName3, appVersion, false);
            //eventProcessManager.startServiceInstance(appName3,appVersion, "Display1");
            Thread.sleep(10000);
            eventProcessManager.stopServiceInstance(appName3,appVersion, "Display1");
            Receiver receiverForApp3 = testMessageListerForReceiver("EVENTPROCESSTIMER_DISPLAY_INMEMORY_19937__1_0__DISPLAY1__IN_PORT");
            eventProcessManager.startServiceInstance(appName3,appVersion, "Timer1");
            testActiveToStandaloneServer();

            result = receiverForApp3.getCount();
            Thread.sleep(10000);
            compareresult = receiverForApp3.getCount();
            while (compareresult != result) {
                Thread.sleep(15000);
                result = receiverForApp3.getCount();
                if (result == 100000) {
                    break;
                } else {
                    Thread.sleep(50000);
                    compareresult = receiverForApp3.getCount();
                }
            }
            if (result != 100000) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testMessageLoss", "TestMessageLoss19937"));
                Assert.fail("Failed as message loss occurs in third testcase. No.of messages received:" + result);
            }
            testStopDeleteApplication(appName3);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testMessageLoss_3", "TestMessageLoss19937"), e);
            Assert.fail("failed to start the third application ", e);
        }
    }

    @Test(groups = "TestMessageLoss19937", description = "bug 19937", dependsOnMethods = "testMessageLoss_3", alwaysRun = true)
    public void testMessageLoss_4() {
        try {

            Logger.LogMethodOrder(Logger.getOrderMessage("testMessageLoss_4", "TestMessageLoss19937"));
            //4th test case (Transacted Session, InMemory, LPC):
            attribute = new Attribute("UseLPCForInMemoryComponents", true);
            mbsc.setAttribute(mem, attribute);
            jmxClient.cleanUp();
            jmxClient = JMXClient.getInstance();
            SampleEventProcessListener epListener = new SampleEventProcessListener(appName4, eventStore);
            eventProcessManager.addEventProcessListener(epListener);
            deployEventProcess("eventprocesstimer_display_inmem_lpc19937-1.0@EnterpriseServer.zip");
            eventProcessManager.checkResourcesAndConnectivity(appName4, appVersion);
            eventProcessManager.startEventProcess(appName4, appVersion, false);
            //eventProcessManager.startServiceInstance(appName4,appVersion, "Display1");
            Thread.sleep(10000);
            eventProcessManager.stopServiceInstance(appName4,appVersion, "Display1");
            Receiver receiverForApp4 = testMessageListerForReceiver("EVENTPROCESSTIMER_DISPLAY_INMEM_LPC19937__1_0__DISPLAY1__IN_PORT");
            eventProcessManager.startServiceInstance(appName4,appVersion, "Timer1");
            testActiveToStandaloneServer();

            result = receiverForApp4.getCount();
            Thread.sleep(10000);
            compareresult = receiverForApp4.getCount();
            while (compareresult != result) {
                Thread.sleep(15000);
                result = receiverForApp4.getCount();
                if (result == 100000) {
                    break;
                } else {
                    Thread.sleep(40000);
                    compareresult = receiverForApp4.getCount();
                }
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName4), "1");
            testStopDeleteApplication(appName4);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testMessageLoss_4", "TestMessageLoss19937"), e);
            //Assert.fail("failed to start the fourth application ", e);
        }
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" + File.separator + "resources" + File.separator + zipName));
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


    public class PublishMessagesThread extends Thread {

        Publisher pub = new Publisher();

        public PublishMessagesThread() {
            super();
            pub.setDestinationName("EVENTPROCESS19937__1_0__FEEDER1__OUT_PORT");
            pub.setCf("PRIMARYTCF");
            pub.setUser("anonymous");
            pub.setPasswd("anonymous");
            try {
                pub.initialize("hafps");
            } catch (Exception e) {
                Assert.fail("Failed to do create publisher to outport", e);
            }
        }

        public void run() {
            String messageSent = "Hello Fiorano";
            try {
                for (int i = 0; i < 100000; i++) {
                    pub.sendMessage(messageSent);
                }
            } catch (Exception e) {
                Assert.fail("Failed to do publish message on outport", e);
            } finally {
                pub.close();
            }
        }
    }


    @Test(enabled = false)
    public void testStopDeleteApplication(String appGUID) {
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, appVersion, true, false);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testStopDeleteApplication", "TestJapanese"));

    }

    @Test(enabled = false)
    public void testActiveToStandaloneServer() {
        String profileType = null;
        Map<String, ProfileElement> profileElements = null;
        for (int i = 0; i <= 2; i++) {
            try {
                String url = rmiClient.getFpsManager().getConnectURLForPeer("hafps");
                if (url.endsWith("1867")) {   // if url ends with 1867 , it means primary is active ,so shutdown secondary server & vice-versa
                    profileType = "secondary";
                } else profileType = "primary";
                String serverName = "hafps";
                serverElement = testenv.getServer(serverName);
                profileElements = serverElement.getProfileElements();
                executioncontroller.shutdownServerOnRemote(testenv.getMachine(profileElements.get(profileType).getMachineName()).getAddress(), serverElement.getMode(), "haprofile1/" + profileType, profileElements.get(profileType).isWrapper(), testenv.getServer("hafps").isHA());
                Thread.sleep(5000);
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testActiveToStandaloneServer", "TestMessageLoss19937"), e);
                Assert.fail("Failed in testActiveToStandaloneServer method", e);
            }
            executioncontroller.startServerOnRemote(testenv.getMachine(profileElements.get(profileType).getMachineName()).getAddress(), serverElement.getMode(), "haprofile1/" + profileType, profileElements.get(profileType).isWrapper(), testenv.getServer("hafps").isHA());

            try {
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testActiveToStandaloneServer", "TestMessageLoss19937"), e);
                Assert.fail("Failed in testSendMessages method", e);
            }
        }
    }

    @Test(enabled = false)
    public Receiver testMessageListerForReceiver(String destinationName) {
        Receiver rec = new Receiver(true, false);
        rec.setDestinationName(destinationName);
        rec.setCf("PRIMARYQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("hafps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testMessageListerForReceiver", "TestMessageLoss19937"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        return rec;
    }
}

