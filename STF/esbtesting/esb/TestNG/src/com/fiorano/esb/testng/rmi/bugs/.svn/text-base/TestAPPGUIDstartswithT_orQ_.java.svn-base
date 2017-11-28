package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.rtl.server.FioranoServiceProviderFactory;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.events.ServiceEvent;
import fiorano.tifosi.dmi.events.TifosiEvent;
import fiorano.tifosi.events.IEventListener;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: srikanth
 * Date: Jan 10, 2011
 * Time: 7:42:49 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestAPPGUIDstartswithT_orQ_ extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private static Hashtable<String, String> eventStore2 = new Hashtable<String, String>();
    private String appName1 = "T_AAAA";
    private String appName2 = "Q_AAAA";
    private FioranoServiceProvider fsp = null;
    private EventListener eventlistener = null;
    private float appVersion = 1.0f;
    @Test(groups = "APPGUIDstartswithT_orQ_", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
           // Assert.fail("Cannot run Group Remote. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "APPGUIDstartswithT_orQ_", description = "bug 19882 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfApplications() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"));

        SampleEventProcessListener epListener1 = null;
        SampleEventProcessListener epListener2 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);
            epListener2 = new SampleEventProcessListener(appName2, eventStore2);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
            eventProcessManager.addEventProcessListener(epListener2);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("t_aaaa.zip");
            deployEventProcess("q_aaaa.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
            eventProcessManager.checkResourcesAndConnectivity(appName2, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            eventProcessManager.startEventProcess(appName2, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore1.clear();
        eventStore2.clear();
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to register listeners for application events", e);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            Assert.fail("Failed to register listeners for application events", e);
        }
        try {

            eventProcessManager.startAllServices(appName1, appVersion);
            try {
                Thread.sleep(15000);
               // Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "2");
            } catch (InterruptedException e) {
            }
            eventProcessManager.startAllServices(appName2, appVersion);
            try {
                Thread.sleep(15000);
                //Assert.assertEquals(eventStore2.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName2), "2");
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        }
        try {
            fsp.getEventsManager().unRegisterApplicationEventListener(eventlistener);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"), e);
           // Assert.fail("Failed to unregister listeners for application events", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfApplications", "TestAPPGUIDstartswithT_orQ_"));

    }

    @Test(groups = "APPGUIDstartswithT_orQ_", description = "bug 19882 ", dependsOnMethods = "TestLaunchOfApplications", alwaysRun = true)
    public void testStopDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testStopDeleteApplication", "TestAPPGUIDstartswithT_orQ_"));

            eventProcessManager.stopEventProcess(appName1, appVersion);
            Thread.sleep(5000);
            eventProcessManager.stopEventProcess(appName2, appVersion);
            Thread.sleep(5000);
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            Thread.sleep(5000);
            eventProcessManager.deleteEventProcess(appName2, appVersion, true, false);
            Thread.sleep(5000);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopDeleteApplication", "TestAPPGUIDstartswithT_orQ_"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopDeleteApplication", "TestAPPGUIDstartswithT_orQ_"), e);
            //Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopDeleteApplication", "TestAPPGUIDstartswithT_orQ_"), e);
            //Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testStopDeleteApplication", "TestAPPGUIDstartswithT_orQ_"));
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

            if (event instanceof ServiceEvent) {
                ServiceEvent serviceEvent = (ServiceEvent) event;
                System.out.println("ServiceEvent Details\n--------------------------------");
                System.out.println("\tService GUID : " + serviceEvent.getServiceGUID());
                System.out.println("\tService Version : " + serviceEvent.getServiceVersion());
                System.out.println("\tService Instance : " + serviceEvent.getServiceInstance());
                System.out.println("\tPeer Name : " + serviceEvent.getPeerName());
                System.out.println("\tApplication GUID : " + serviceEvent.getApplicationGUID());
            }// else
               // Assert.fail("not able to start the application");
        }
    }
}



