package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.rtl.server.FioranoServiceProviderFactory;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.*;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import fiorano.tifosi.dmi.events.ServiceEvent;
import fiorano.tifosi.dmi.events.TPSEvent;
import fiorano.tifosi.dmi.events.TifosiEvent;
import fiorano.tifosi.events.IEventListener;
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
 * Date: Jan 18, 2011
 * Time: 3:01:17 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestSender_Reciever extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsmanager;
    private TestEnvironment testenv;
    private FioranoApplicationController m_fioFioranoApplicationController;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private String appName1 = "SENDER_RECIEVER";
    private FioranoServiceProvider fsp = null;
    private TestEnvironmentConfig testenvconfig;
    private ExecutionController executioncontroller;
    private EventListener eventlistener = null;
    private EventListener1 eventlistener1 = null;
    private ServerStatusController serverstatus;
    private ArrayList<TifosiEvent> events = new ArrayList<TifosiEvent>();
    private String processid = null;
    private float appVersion = 1.0f;
    @Test(groups = "Sender_Reciever", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
           // Assert.fail("Cannot run Group Sender_Reciever. as eventProcessManager is not set.");
        }
        try {
            m_fioFioranoApplicationController = ServerStatusController.getInstance().getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestGetWSContexts"), e);
            //Assert.fail("Failed to run test,since we could not fioranoapplicationcontroller!", e);
        }
        this.serverstatus = ServerStatusController.getInstance();
        this.testenv = serverstatus.getTestEnvironment();
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        this.executioncontroller = ExecutionController.getInstance();
        this.fpsmanager = rmiClient.getFpsManager();

    }

    @Test(groups = "Sender_Reciever", description = "bug 19848 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfSender_Reciever() {


        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"));
        SampleEventProcessListener epListener = null;
        try {
            epListener = new SampleEventProcessListener(appName1, eventStore1);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e1);
            //Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("SENDER_RECIEVER.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
            //Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
            //Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
           // Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
           // Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore1.clear();
        try {
            eventProcessManager.startAllServices(appName1, appVersion);
            try {
                Thread.sleep(120000);
            } catch (InterruptedException e) {
            }
            //we stop the component, so we can creat a recevier to the queue(inport),else 2 recievers are present for a qeueu.
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfSender_Reciever", "TestSender_Reciever"));

    }

    @Test(groups = "Sender_Reciever", description = "bug 19848 ", dependsOnMethods = "TestLaunchOfSender_Reciever", alwaysRun = true)
    public void TestGetlogs_and_Stop() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestGetlogs_and_Stop", "TestSender_Reciever"));
        try {
            String sender_logs = eventProcessManager.getLastOutTrace(200, "Sender1", appName1, appVersion);
            if (sender_logs.contains("Sent all 10,000 messages.")) {
                try {
                    ServiceInstanceStateDetails service = m_fioFioranoApplicationController.getCurrentStateOfService("SENDER_RECIEVER",appVersion, "Sender1");
                    if (service.getStatusString().equals("SERVICE_HANDLE_UNBOUND")) {
                        eventProcessManager.stopEventProcess("SENDER_RECIEVER", appVersion);
                        Thread.sleep(5000);
                        eventProcessManager.startEventProcess("SENDER_RECIEVER", appVersion, false);
                        Thread.sleep(5000);
                        registerServiceeventlistener();

                        eventProcessManager.startAllServices(appName1, appVersion);
                        Thread.sleep(120000);
                        //     Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "2");
                        if (!(events.size() == 4))
                           // Assert.fail("Unable to start the sender component");
                        fsp.getEventsManager().unRegisterApplicationEventListener(eventlistener);
                        events.clear();

                    }// else
                        //Assert.fail("Sender1 is still running");
                } catch (TifosiException e) {
                    Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetlogs_and_Stop", "TestSender_Reciever"), e);
                   // Assert.fail("not able to execute getWSContexts.", e);
                }
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetlogs_and_Stop", "TestSender_Reciever"), e);
          //  Assert.fail();
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetlogs_and_Stop", "TestSender_Reciever"), e);
            //Assert.fail();
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestGetlogs_and_Stop", "TestSender_Reciever"), e);
            //Assert.fail();
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestGetlogs_and_Stop", "TestSender_Reciever"));
    }

    @Test(groups = "Sender_Reciever", description = "bug 19848 ", dependsOnMethods = "TestGetlogs_and_Stop", alwaysRun = true)
    public void TestKillFPS() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestKillFPS", "TestSender_Reciever"));
        String FioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        File th = new File(FioranoHome);
        String location = th.getAbsolutePath() + File.separator + "esb/server/profiles/profile1/FPS/pid.txt";
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
            processid = pid;
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillFES", "TestSender_Reciever"), e);
           // Assert.fail("Unable to get fps process id", e);
        }
        registerTPSeventlistener();
        try {
            fpsmanager.shutdownPeer("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillFES", "TestSender_Reciever"), e);
            //Assert.fail("Failed to kill FES", e);
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!(events.size() == 1))
           // Assert.fail("Unable to kill the Peer Server");
        try {
            fsp.getEventsManager().unRegisterFPSEventListener(eventlistener1);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillFPS", "TestSender_Reciever"), e);
           // Assert.fail("not able to execute getWSContexts.", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestKillFPS", "TestSender_Reciever"));
    }

    @Test(groups = "Sender_Reciever", description = "bug 19848 ", dependsOnMethods = "TestKillFPS", alwaysRun = true)
    public void TestFPSIsRunning() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestFPSIsRunning", "TestSender_Reciever"));
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("jps");
            InputStreamReader ins = new InputStreamReader(p.getInputStream());
            BufferedReader bbr = new BufferedReader(ins);
            String pid = null;
            String a[] = null;
            String id = null;
            while ((id = bbr.readLine()) != null) {
                a = id.split(" ");
                if (a[0].equals(processid)) {
                   // Assert.fail("Peer is still running in System Monitor");
                }
            }
            bbr.close();
            ins.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.destroy();
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestFPSIsRunning", "TestSender_Reciever"));
    }

    @Test(groups = "Sender_Reciever", description = "bug 19848 ", dependsOnMethods = "TestFPSIsRunning", alwaysRun = true)
    public void TestRestarFPS() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestRestarFPS", "TestSender_Reciever"));

        startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestRestarFPS", "TestSender_Reciever"));
    }

    @Test(groups = "Sender_Reciever", description = "bug 19947 ", dependsOnMethods = "TestRestarFPS", alwaysRun = true)
    public void TestStopandDeleteEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopandDeleteEventProcess", "TestSender_Reciever"));

        try {
            eventProcessManager.stopEventProcess(appName1, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandDeleteEventProcess", "TestSender_Reciever"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandDeleteEventProcess", "TestSender_Reciever"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopandDeleteEventProcess", "TestSender_Reciever"));
    }

    @Test(enabled = false)
    private void registerServiceeventlistener() {
        try {
            Hashtable env = new Hashtable();
            String providerURL = "tsp_tcp://localhost:1947";
            env.put(Context.SECURITY_PRINCIPAL, "admin");
            env.put(Context.SECURITY_CREDENTIALS, "passwd");
            env.put(Context.PROVIDER_URL, providerURL);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.tifosi.jndi.InitialContextFactory");

            InitialContext ic = new InitialContext(env);

            // Lookup for Provider Factory
            FioranoServiceProviderFactory fioranoFactory = (FioranoServiceProviderFactory) ic.lookup("TifosiServiceProvider");

            // Create Fiorano Service Provider
            fsp = fioranoFactory.createServiceProvider("admin", "passwd");

            // Register an Event Listener
            eventlistener = new EventListener();
            fsp.getEventsManager().registerApplicationEventListener(eventlistener);

        } catch (NamingException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestSender_Reciever"), e);
           // Assert.fail("Failed to register listeners for application events", e);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestSender_Reciever"), e);
           // Assert.fail("Failed to register listeners for application events", e);
        }
    }

    @Test(enabled = false)
    private void registerTPSeventlistener() {
        try {
            Hashtable env = new Hashtable();
            String providerURL = "tsp_tcp://localhost:1947";
            env.put(Context.SECURITY_PRINCIPAL, "admin");
            env.put(Context.SECURITY_CREDENTIALS, "passwd");
            env.put(Context.PROVIDER_URL, providerURL);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.tifosi.jndi.InitialContextFactory");

            InitialContext ic = new InitialContext(env);

            // Lookup for Provider Factory
            FioranoServiceProviderFactory fioranoFactory = (FioranoServiceProviderFactory) ic.lookup("TifosiServiceProvider");

            // Create Fiorano Service Provider
            fsp = fioranoFactory.createServiceProvider("admin", "passwd");

            // Register an Event Listener
            eventlistener1 = new EventListener1();
            fsp.getEventsManager().registerFPSEventListener(eventlistener1);

        } catch (NamingException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestSender_Reciever"), e);
           // Assert.fail("Failed to register listeners for application events", e);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestSender_Reciever"), e);
           // Assert.fail("Failed to register listeners for application events", e);
        }
    }

    /**
     * deploys an event process which is located under $testing_home/esb/TestNG/resources/
     *
     * @param zipName - name of the event process zip located under $testing_home/esb/TestNG/resources/
     * @throws java.io.IOException - if file is not found or for any other IO error
     * @throws com.fiorano.esb.server.api.ServiceException
     *
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


    private class EventListener implements IEventListener {

        public void onEvent(TifosiEvent event) throws TifosiException {

            if (event instanceof ServiceEvent) {
                ServiceEvent serviceEvent = (ServiceEvent) event;
                System.out.println("ServiceEvent Details\n--------------------------------");
                System.out.println("\tService GUID : " + serviceEvent.getServiceGUID());
                System.out.println("\tService Version : " + serviceEvent.getServiceVersion());
                System.out.println("\tService Instance : " + serviceEvent.getServiceInstance());
                System.out.println("\tPeer Name : " + serviceEvent.getPeerName());
                System.out.println("\tApplication GUID : " + serviceEvent.getApplicationGUID());
                events.add(event);
            }// else
               // Assert.fail("not able to start the application");
        }
    }

    private class EventListener1 implements IEventListener {

        public void onEvent(TifosiEvent event) throws TifosiException {

            if (event instanceof TPSEvent) {
                TPSEvent tpsEvent = (TPSEvent) event;
                System.out.println("FPSEvent Details\n--------------------------------");
                System.out.println("\tEvent Category : " + tpsEvent.getEventCategory());
                System.out.println("\tEvent Description : " + tpsEvent.getEventDescription());
                System.out.println("\tEvent Type : " + tpsEvent.getEventType());
                System.out.println("\tPeer Name : " + tpsEvent.getTPSName());
                events.add(event);
            }// else
              //  Assert.fail("not able kill the peer server");
        }
    }
}
