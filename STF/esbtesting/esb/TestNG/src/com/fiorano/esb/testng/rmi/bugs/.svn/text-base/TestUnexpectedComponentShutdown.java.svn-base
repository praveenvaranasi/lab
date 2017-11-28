package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.RTLEventSubscriber;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.events.ServiceEvent;
import fiorano.tifosi.dmi.events.TifosiEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.naming.NamingException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: Jan 11, 2011
 * Time: 10:46:17 AM
 * To change this template use File | Settings | File Templates.
 */

public class TestUnexpectedComponentShutdown extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "EP1";
    private float appVerison = 1.0f;
    @Test(groups = "Unexpected Component shutdown", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            //Assert.fail("Cannot run Group Unexpected component shutdown. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "Unexpected Component shutdown", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfSMTPEventProcess() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"));
        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e1);
            //Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
            //Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
           // Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("ep1-1.0@EnterpriseServer.1.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        RTLEventSubscriber rtlEventSubscriber = null;

        HashMap serverdetails = null;
        try {
            serverdetails = rmiClient.getServiceProviderManager().getServerDetails();
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String url = (String) serverdetails.get("Server URL");
        rtlEventSubscriber = new RTLEventSubscriber(url);

        try {
            rtlEventSubscriber.startup();
        } catch (NamingException e) {
            //Assert.fail("NamingException while creating rtlEventSubcriber");
        } catch (TifosiException e) {
            //Assert.fail("TifosiException while creating rtlEventSubcriber");
        }
        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVerison);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
            //Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUID, appVerison, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
            //Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
            //Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }

        createPub("EP1__1_0__FEEDER1__OUT_PORT");
       /* try {
            eventProcessManager.stopServiceInstance("EP1",appVerison, "Display1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
            //Assert.fail("Failed to stop service Dispaly1!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"), e);
            //Assert.fail("Failed to  stop  Display1!", e);
        }*/
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        String os = System.getProperty("os.name");
        Process process = null;
        long pid = 0l;
        try {
            process = Runtime.getRuntime().exec("jps");
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line = null;
            String a[] = null;

            while ((line = in.readLine()) != null) {
                a = line.split(" ");
                if (a.length > 1 && a[1].equals("DisplayServiceLauncher")) {
                    pid = Long.valueOf(a[0]);
                    break;
                }
            }
        } catch (IOException e) {
            //Assert.fail("Failed to execute jps");
        }
        if (os.startsWith("windows")) {
            //kill the display window in windows box
            try {
                Runtime.getRuntime().exec("taskkill /f /PID " + pid);
            } catch (IOException e) {
               // Assert.fail("Failed to kill DisplayServiceLauncher");
            }

        } else {

            try {
                Runtime.getRuntime().exec("kill -9 " + pid);
            } catch (IOException e) {
                //Assert.fail("Failed to kill DisplayServiceLauncher");
            }
        }
        //TODO

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }


        ArrayList<TifosiEvent> events = rtlEventSubscriber.getEvents();
        int Numberofevents = events.size();
        boolean warningcheck = false;
        for (int i = 0; i < Numberofevents; i++) {
            String eventcategory = events.get(i).getEventCategory();
            String servicetype = ((ServiceEvent) events.get(i)).getServiceGUID();
            String servicestatus=events.get(i).getEventStatus();
            if (servicetype.equalsIgnoreCase("Display") && (servicestatus.equalsIgnoreCase("SERVICE_HANDLE_UNBOUND"))) {
                if (!eventcategory.equalsIgnoreCase("Warning")) {
                    Assert.fail("Unexpected component shutdown failed as event category is not warning for Display!");
                } else {
                    warningcheck = true;
                }

            }
        }

        if (!warningcheck) {
            //Assert.fail("Unexpected component shutdown failed as event category is not warning for Display!");
        }
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestLaunchOfSMTPEventProcess", "Unexpected Component shutdown"));

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
            Assert.fail("Failed to do create publisher to outport", e);
        }
        try {
            pub.sendMessage("message with out schema");
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
           // Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
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

    @Test(groups = "Unexpected Component shutdown", dependsOnMethods = "TestLaunchOfSMTPEventProcess", alwaysRun = true)
    public void testStopDeleteApplication() {
        try {
            eventProcessManager.stopEventProcess(appGUID, appVerison);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, appVerison, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopDeleteApplication", "Unexpected Component shutdown"), e);
            //Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopDeleteApplication", "Unexpected Component shutdown"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testStopDeleteApplication", "Unexpected Component shutdown"));

    }

}
