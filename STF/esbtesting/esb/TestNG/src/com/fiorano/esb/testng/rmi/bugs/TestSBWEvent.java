package com.fiorano.esb.testng.rmi.bugs;


import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.*;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.events.TifosiEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.*;
import javax.naming.NamingException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;


/**
 * Created by IntelliJ IDEA.
 * User: srikanth
 * Date: Nov 30, 2010
 * Time: 2:32:58 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestSBWEvent extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private TestEnvironmentConfig testenvconfig;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "SBWEVENT";
    private ObjectName objName1;
    private JMXClient jmxClient;
    private String Instance1 = "chat1";
    private String Instance2 = "chat2";
    private float appVersion = 1.0f;

    @Test(groups = "SBWEvents", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        this.jmxClient = JMXClient.getInstance();
        try {
            objName1 = new ObjectName("Fiorano.Esb.Sbw:ServiceType=SBWManager,Name=SBWManager,type=config");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Japanese. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "SBWEvents", description = "bug 19934 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfSBWEventApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"));
        SampleEventProcessListener epListener = null;
        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("sbwevent.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUID, 1.0f, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appGUID, 1.0f);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "2");
            //we stop the component, so we can creat a recevier to the queue(inport),else 2 recievers are present for a qeueu.
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfSBWEventApplication", "TestSBWEvent"));
    }

    @Test(groups = "SBWEvents", description = "bug - 19934,start RTLEventSubscriber sample", dependsOnMethods = "TestLaunchOfSBWEventApplication", alwaysRun = true)
    public void testStartRTLEventSubscriber() throws IOException {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStartRTLEventSubscriber", "TestSBWEvent"));
        try {
            JMXClient.connect("admin", "passwd");
            jmxClient.invoke(objName1, "setRTLToReceiveSBWEvents", new Object[]{false}, new String[]{"boolean"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            eventProcessManager.stopServiceInstance(appGUID,appVersion, Instance2);
            Thread.sleep(6000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        HashMap serverdetails = null;
        try {
            serverdetails = rmiClient.getServiceProviderManager().getServerDetails();
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("Failed to get Server details!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("Failed to get Server details!", e);
        }
        String url = (String) serverdetails.get("Server URL");
        RTLEventSubscriber rtlEventSubscriber = new RTLEventSubscriber(url);

        try {
            rtlEventSubscriber.startup();
        } catch (NamingException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("NamingException while creating rtlEventSubcriber");
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("TifosiException while creating rtlEventSubcriber");
        }
        createPubSub();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ArrayList<TifosiEvent> events = rtlEventSubscriber.getEvents();
        int Numberofevents = events.size();
        if (Numberofevents == 0)
            System.out.println("Test Passed Successfully");
        else
            Assert.fail("RTL is receiving sbw events even property is disabled");
        try {
            eventProcessManager.startServiceInstance(appGUID,appVersion, Instance2);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRTLEventSubscriber", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testStartRTLEventSubscriber", "TestSBWEvent"));

    }


    @Test(groups = "SBWEvents", description = "bug 19934 - Update SBW export property ", dependsOnMethods = "testStartRTLEventSubscriber", alwaysRun = true)
    public void testupdateSBWProperty() throws IOException {
        Logger.LogMethodOrder(Logger.getOrderMessage("testupdateSBWProperty", "TestSBWEvent"));
        try {
            jmxClient.invoke(objName1, "setRTLToReceiveSBWEvents", new Object[]{true}, new String[]{"boolean"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            eventProcessManager.stopServiceInstance(appGUID,appVersion, Instance2);
            Thread.sleep(6000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        HashMap serverdetails = null;
        try {
            serverdetails = rmiClient.getServiceProviderManager().getServerDetails();
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("Failed to get Server details!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("Failed to get Server details!", e);
        }
        String url = (String) serverdetails.get("Server URL");
        RTLEventSubscriber rtlEventSubscriber = new RTLEventSubscriber(url);

        try {
            rtlEventSubscriber.startup();
        } catch (NamingException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("NamingException while creating rtlEventSubcriber");
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("TifosiException while creating rtlEventSubcriber");
        }
        createPubSub();
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        ArrayList<TifosiEvent> events = rtlEventSubscriber.getEvents();
        int Numberofevents = events.size();
        if (Numberofevents == 0)
            Assert.fail("RTL is not receiving sbw events even property is enabled");
        else if (Numberofevents == 2)
            System.out.println("Test Passed Successfully");
        try {
            eventProcessManager.startServiceInstance(appGUID,appVersion, Instance2);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testupdateSBWProperty", "TestSBWEvent"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        try {
            jmxClient.invoke(objName1, "setRTLToReceiveSBWEvents", new Object[]{false}, new String[]{"boolean"});
        } catch (ReflectionException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (InstanceNotFoundException e) {
            e.printStackTrace();

        } catch (MBeanException e) {
            e.printStackTrace();

        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testupdateSBWProperty", "TestSBWEvent"));
    }

    @Test(groups = "SBWEvents", description = "bug 19934 - Stop and Delete Application", dependsOnMethods = "testupdateSBWProperty", alwaysRun = true)
    public void testStopDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStopDeleteApplication", "TestSBWEvent"));
        try {
            jmxClient.cleanUp();
            eventProcessManager.stopEventProcess(appGUID, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopDeleteApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopDeleteApplication", "TestSBWEvent"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testStopDeleteApplication", "TestSBWEvent"));
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

    /**
     * Method to create FMQ pub/sub
     */
    @Test(enabled = false)
    public void createPubSub() {

        Publisher pub = new Publisher();
        pub.setDestinationName(appGUID + "__"+"1_0"+"__" + Instance1 + "__OUT_PORT");
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestSBWEvent"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestSBWEvent"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

        Receiver rec = new Receiver();
        rec.setDestinationName(appGUID + "__" + "1_0" + "__" + Instance2 + "__IN_PORT");
        rec.setCf("primaryQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestSBWEvent"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestSBWEvent"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        Assert.assertEquals(messageSent, messageOnDestination);
    }
}