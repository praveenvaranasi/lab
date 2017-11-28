package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.EventProcessHandle;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.RemoteServiceInstance;
import fiorano.tifosi.dmi.application.Route;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/*
*
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Dec 1, 2010
 * Time: 6:18:10 PM
 * To change this template use File | Settings | File Templates.
*/


public class TestRemoteServiceInstance extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private FioranoApplicationController m_FioranoApplicationController;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private static Hashtable<String, String> eventStore2 = new Hashtable<String, String>();
    private EventProcessHandle eventProcessHandle;
    private String appName1 = "CHAT";
    private String appName2 = "FEEDER_DISPLAY";
    private float appVersion = 1.0f;

    @Test(groups = "RemoteServiceInstance", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestRemoteServiceInstance"));
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
           // Assert.fail("Cannot run Group RemoteServiceInstance as eventProcessManager is not set.");

        }
        FioranoServiceProvider sp = null;
        try {
            sp = ServerStatusController.getInstance().getServiceProvider();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to run test,since we could not getServiceProvider!", e);
        }

        FioranoApplicationController fac = sp.getApplicationController();
        m_FioranoApplicationController = fac;
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestRemoteServiceInstance"));
    }


    @Test(groups = "RemoteServiceInstance", description = "check remote-service-instance output port bug ::9468 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"));
        SampleEventProcessListener epListener1 = null;
        SampleEventProcessListener epListener2 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);
            epListener2 = new SampleEventProcessListener(appName2, eventStore2);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e1);
          //  Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
            eventProcessManager.addEventProcessListener(epListener2);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
          //  Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("feeder_display-1.0.zip");
            deployEventProcess("chat-1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);

            eventProcessManager.checkResourcesAndConnectivity(appName2, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
          //  Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
          //  Assert.fail("Failed to do crc!", e);
        }
        try {

            eventProcessManager.startEventProcess(appName1, appVersion, false);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore1.clear();
        try {
            eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(10000);


            //Assert.assertEquals(eventStore2.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName2), "2");
          //  Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "2");


        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
          //  Assert.fail("Error with thread sleep!", e);
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"));

    }

    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "testLaunchOfApplication", alwaysRun = true)
    public void addRemoteServiceInstance() {
        Logger.LogMethodOrder(Logger.getOrderMessage("addRemoteServiceInstance", "TestRemoteServiceInstance"));
        Application application = null;

        try {
            application = m_FioranoApplicationController.getApplication(appName2, appVersion);
        } catch (TifosiException e) {
           // Assert.fail("Could not retrieve Chat Application", e);
            e.printStackTrace();
        }
        RemoteServiceInstance remote = new RemoteServiceInstance();
        remote.setApplicationGUID(appName1);
        Float version = (float) 1.0;
        remote.setApplicationVersion(version);
        remote.setRemoteName("chat1");
        remote.setName("remote_chat1");
        application.addRemoteServiceInstance(remote);
        Route route = new Route();
        route.setName("route3");
        route.setSourceServiceInstance("remote_chat1");
        route.setSourcePortInstance("OUT_PORT");
        route.setTargetServiceInstance("Display1");
        route.setTargetPortInstance("IN_PORT");
        application.addRoute(route);


        try {
            m_FioranoApplicationController.saveApplication(application);
        } catch (TifosiException e) {
           // Assert.fail("Could not save application ", e);
            Logger.Log(Level.SEVERE, Logger.getErrMessage("addRemoteServiceInstance", "TestRemoteServiceInstance"), e);

        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("addRemoteServiceInstance", "TestRemoteServiceInstance"));

    }

    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "addRemoteServiceInstance", alwaysRun = true)
    public void testRemoteServiceInstance() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testRemoteServiceInstance", "TestRemoteServiceInstance"));
        try {
            eventProcessManager.checkResourcesAndConnectivity(appName2, appVersion);

            eventProcessManager.startEventProcess(appName2, appVersion, false);
            Thread.sleep(5000);

            eventProcessManager.startAllServices(appName2, appVersion);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoteServiceInstance", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do operation on service instance", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoteServiceInstance", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do operation on service instance", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoteServiceInstance", "TestRemoteServiceInstance"), e);
           // Assert.fail("Exception with thread sleep", e);
        }


        try {
            eventProcessManager.stopServiceInstance(appName1,appVersion, "chat1");

            eventProcessManager.stopServiceInstance(appName2, appVersion,"Display1");
            createPubSub();
            eventProcessManager.startServiceInstance(appName1,appVersion, "chat1");
            eventProcessManager.startServiceInstance(appName2,appVersion, "Display1");
            Thread.sleep(4000);
        } catch (RemoteException e) {
           // Assert.fail("Failed to do operation on service instance", e);
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoteServiceInstance", "TestRemoteServiceInstance"), e);
        } catch (ServiceException e) {
           // Assert.fail("Failed to do operation on service instance", e);
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoteServiceInstance", "TestRemoteServiceInstance"), e);
        } catch (InterruptedException e) {
           // Assert.fail("Exception with thread sleep", e);
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoteServiceInstance", "TestRemoteServiceInstance"), e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoteServiceInstance", "TestRemoteServiceInstance"));

    }

    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "testRemoteServiceInstance", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestRemoteServiceInstance"));
        stopAndDeleteEP(eventProcessManager, appName2, appVersion);
        stopAndDeleteEP(eventProcessManager, appName1, appVersion);
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopAndDeleteApplication", "TestRemoteServiceInstance"));
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

    @Test(enabled = false)
    public void createPubSub() {

        Publisher pub = new Publisher();
        pub.setDestinationName(appName1 + "__" + "1_0" + "__" + "CHAT1" + "__OUT_PORT");
        pub.setCf(appName1 + "__" + "1_0" + "__" + "CHAT1");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
          //  Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

        Receiver rec = new Receiver();
        rec.setDestinationName(appName2 + "__" + "1_0" + "__" + "Display1" + "__IN_PORT");
        rec.setCf(appName2 + "__" + "1_0" + "__" + "Display1");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
          //  Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
          //  Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

       // Assert.assertEquals(messageSent, messageOnDestination);
    }

}
