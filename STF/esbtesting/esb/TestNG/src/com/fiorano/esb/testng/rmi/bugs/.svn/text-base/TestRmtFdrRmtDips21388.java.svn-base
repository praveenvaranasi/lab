package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
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
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: aravind
 * Date: 8/21/12
 * Time: 10:24 AM
 * To change this template use File | Settings | File Templates.
 */

public class TestRmtFdrRmtDips21388 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private static Hashtable<String, String> eventStore2 = new Hashtable<String, String>();
    private String appName1 = "EVENT_PROCESS1";
    private String appName2 = "EVENT_PROCESS2";
    private FioranoApplicationController m_fioranoappcontroller;
    private ServerStatusController serverstatus;
    private String m_appGUID;
    private float m_version;
    private IServiceProviderManager ispm;
    private FioranoApplicationController m_fioranoApplicationController;
    private float appVersion = 1.0f;

    @Test(groups = "RemoteServiceInstance", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestRmtFdrRmtDips21388"));
        this.eventProcessManager = rmiClient.getEventProcessManager();
        ispm=rmiClient.getServiceProviderManager();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        if (eventProcessManager == null) {
            // Assert.fail("Cannot run Group RemoteServiceInstance as eventProcessManager is not set.");

        }
        this.serverstatus = ServerStatusController.getInstance();
        try {

            this.m_fioranoappcontroller = serverstatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("initNewSetup", "TestRmtFdrRmtDips21388"), e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestRmtFdrRmtDips21388"));
    }

    @Test(groups = "RemoteServiceInstance", description = "check remote-service-instance output port bug ::21388 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfApplication(){
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"));
        SampleEventProcessListener epListener1 = null;
        SampleEventProcessListener epListener2 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);
            epListener2 = new SampleEventProcessListener(appName2, eventStore2);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"), e1);
            //  Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
            eventProcessManager.addEventProcessListener(epListener2);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"), e);
            //  Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"), e);
            // Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("fdrdisp_1.0.zip");
            deployEventProcess("fdrrmtdisprmt_1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"), e);
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"), e);
            //  Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"), e);
            //  Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);

            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"), e);
            // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRmtFdrRmtDips21388"), e);
            // Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        //strat EP2
        try
        {
            eventProcessManager.startEventProcess(appName2, appVersion, false);
            Thread.sleep(10000);

        }
        catch (Exception e)
        {
            Assert.fail("EP2 launch should not fail with multiple routes  exception not allowed.");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOfApplication", "TestRmtFdrRmtDips21388"));
    }

    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "testLaunchOfApplication", alwaysRun = true)
    void testDuplicateMessage()
    {
        Logger.LogMethodOrder(Logger.getOrderMessage("testDuplicateMessage", "TestRmtFdrRmtDips21388"));


        try {
            eventProcessManager.stopServiceInstance(appName1,appVersion,"Display1");
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        boolean duplicate=false;
        try{
            duplicate=topicpublisherReceiver("fps", appName1 + "__" + "1_0" + "__" + "feeder1" + "__OUT_PORT", "fps1", appName1 +"__" + "1_0" + "__" + "display1" + "__IN_PORT");
        }
        catch (Exception e)
        {
            System.out.println("Error in sending and receiving the message:"+e);

        }

        if(!duplicate)
            Assert.fail("Not Receiving duplicate messages on the destination");
        else
            System.out.println("Receiving duplicate messages on the destination");

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("testDuplicateMessage", "TestRmtFdrRmtDips21388"));
    }

    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "testDuplicateMessage", alwaysRun = true)
    public void stopApplication() throws ServiceException, RemoteException {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestRmtFdrRmtDips21388"));
        try {

            eventProcessManager.stopEventProcess(appName2, appVersion);
            eventProcessManager.stopEventProcess(appName1, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Assert.fail("Thread Exception ", e);
                Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestRmtFdrRmtDips21388"), e);
            }

            eventProcessManager.deleteEventProcess(appName2, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestRmtFdrRmtDips21388"), e);
            //  Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestRmtFdrRmtDips21388"), e);
            // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopAndDeleteApplication", "TestRmtFdrRmtDips21388"));

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


    /**
     * Method to create FMQ pub/sub
     */
    @Test(enabled = false)
    public boolean topicpublisherReceiver(String servername, String topicname,String servername1,String queuename)
    {
        boolean duplicate=true;
        Publisher pub = new Publisher();
        pub.setDestinationName(topicname);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");

        try {
            pub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisherReceiver", "TestRmtFdrRmtDips21388"), e);
            //Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "TestBreakpointReappears"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        //    return messageSent;
        //}
        //@Test(enabled = false)
        //public void queuereceiver(String servername, String queuename, String messageSent) {
        Receiver rec = new Receiver();
        rec.setDestinationName(queuename);
        rec.setCf("primaryQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize(servername1);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiverReceiver", "TestRmtFdrRmtDips21388"), e);

            Assert.fail("Failed to do create receiver to inport", e);
        }
          try {

            pub.sendMessage(messageSent);
            //System.out.println("Message sent:"+messageSent);
        } catch (Exception e) {
            //System.out.println("Message not sent");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisherReceiver", "TestRmtFdrRmtDips21388"), e);
            Assert.fail("Failed to do publish message on outport", e);
        }

        String messageOnDestination = null;

        try {

            messageOnDestination = rec.getMessageOnDestination(5000);

            // System.out.println("Received 1:"+messageOnDestination);
        } catch (Exception e) {
            //System.out.println(" not Received 1");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiverReceiver", "TestRmtFdrRmtDips21388"), e);
            Assert.fail("Failed to do get message from inport", e);
            rec.close();
        }

        String messageOnDestination1 = null;
        try {
            messageOnDestination1 = rec.getMessageOnDestination(5000);
            System.out.println("Received 2:"+messageOnDestination1);
        } catch (Exception e) {
            System.out.println(" not Received 2");
            //Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiverReceiver", "TestRemoteFeederDisplay"), e);
            //Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
            pub.close();
        }

        if(messageSent.equals(messageOnDestination)&&messageSent.equals(messageOnDestination1))
            return true;
        else
            return false;
    }

}