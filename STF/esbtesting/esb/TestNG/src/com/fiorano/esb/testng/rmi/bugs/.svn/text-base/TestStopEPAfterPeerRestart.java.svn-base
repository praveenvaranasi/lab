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
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import fiorano.tifosi.dmi.events.TPSEvent;
import fiorano.tifosi.dmi.events.TifosiEvent;
import fiorano.tifosi.events.IEventListener;
import fiorano.tifosi.events.TifosiEventIDs;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: srikanth
 * Date: Jan 25, 2011
 * Time: 11:33:43 AM
 * To change this template use File | Settings | File Templates.
 */

public class TestStopEPAfterPeerRestart extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsmanager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "STOPEPAFTERPEERRESTART";
    private FioranoServiceProvider fsp = null;
    private EventListener eventlistener = null;
    private FioranoApplicationController m_fioFioranoApplicationController;
    private float appVersion = 1.0f;
    @Test(groups = "StopEPAfterPeerRestart", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group StopEPAfterPeerRestart. as eventProcessManager is not set.");
        }
        this.fpsmanager = rmiClient.getFpsManager();
        try {
            m_fioFioranoApplicationController = ServerStatusController.getInstance().getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to run test,since we could not fioranoapplicationcontroller!", e);
        }
    }

    @Test(groups = "StopEPAfterPeerRestart", description = "bug 18917 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"));
        SampleEventProcessListener epListener = null;
        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("STOPEPAFTERPEERRESTART.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }


        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appGUID, appVersion);
            try {
                Thread.sleep(45000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "2");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"));

    }

    @Test(groups = "StopEPAfterPeerRestart", description = "bug 18917 ", dependsOnMethods = "TestLaunchOfApplication", alwaysRun = true)
    public void TestRestartPeer() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestRestartPeer", "TestStopEPAfterPeerRestart"));

        try {
            fpsmanager.restartPeer("fps");
            TestRegisterPeerEventListener();
            Thread.sleep(40000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to restart fps", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to restart fps", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to restart fps", e);
        }
        try {
            fsp.getEventsManager().unRegisterFPSEventListener(eventlistener);
        } catch (TifosiException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestRestartPeer", "TestStopEPAfterPeerRestart"));
    }

    @Test(groups = "StopEPAfterPeerRestart", description = "bug 18917 ", dependsOnMethods = "TestRestartPeer", alwaysRun = true)
    public void TestStopandDeleteEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopandDeleteEventProcess", "TestStopEPAfterPeerRestart"));
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandDeleteEventProcess", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandDeleteEventProcess", "TestStopEPAfterPeerRestart"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopandDeleteEventProcess", "TestStopEPAfterPeerRestart"));

    }

    private void TestRegisterPeerEventListener() {
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
            fsp.getEventsManager().registerFPSEventListener(eventlistener);

        } catch (NamingException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to register listeners for application events", e);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to register listeners for application events", e);
        }
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

    private class EventListener implements IEventListener {
        public void onEvent(TifosiEvent event) throws TifosiException {
            if (event instanceof TPSEvent) {
                TPSEvent fpsEvent = (TPSEvent) event;
                if (fpsEvent.getEventID() == TifosiEventIDs.TIFOSI_SERVER_LAUNCHED) {
                    try {
                        eventProcessManager.stopEventProcess(appGUID, appVersion);

                        //TODO - execute jps and check for Feeder and Display processes
                        Process p = null;
                        try {
                            p = Runtime.getRuntime().exec("jps");

                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(p.getInputStream()));
                            String line = null;
                            String a[] = null;
                            while ((line = in.readLine()) != null) {
                                //System.out.println(line);
                                a = line.split(" ");
                                String serviceName = a[1];
                                if (serviceName.equals("FeederServiceLauncher") || serviceName.equals("DisplayServiceLauncher")) {
                                    Assert.fail("Feeder/Display components are still running");
                                }

                            }
                        } catch (IOException e) {
                            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }

                        eventProcessManager.startEventProcess(appGUID, appVersion, false);
                        Thread.sleep(10000);
                        eventProcessManager.startAllServices(appGUID, appVersion);
                        Thread.sleep(60000);

                        try {
                            ServiceInstanceStateDetails service = m_fioFioranoApplicationController.getCurrentStateOfService(appGUID,appVersion, "Feeder1");
                            ServiceInstanceStateDetails service1 = m_fioFioranoApplicationController.getCurrentStateOfService(appGUID,appVersion, "Display1");
                            if (service.getStatusString().equals("SERVICE_HANDLE_UNBOUND"))
                                Assert.fail("Feeder1 is not running after peer events");
                            if (service1.getStatusString().equals("SERVICE_HANDLE_UNBOUND"))
                                Assert.fail("Display1 is not running after peer events");
                        } catch (TifosiException e) {
                            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
                            Assert.fail("not able to execute getWSContexts.", e);
                        }


                    } catch (RemoteException e) {
                        Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
                        Assert.fail("Failed to do operation on service instance!", e);
                    } catch (ServiceException e) {
                        Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestStopEPAfterPeerRestart"), e);
                        Assert.fail("Failed to do operation on service instance!", e);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }


}
