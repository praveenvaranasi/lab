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
import com.fiorano.stf.framework.EventProcessHandle;
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

/*
*
 * Created by IntelliJ IDEA.
 * User: aravind
 * Date:  AUG 7, 2012
 * Time: 6:18:10 PM
 * To change this template use File | Settings | File Templates.
*/


public class TestRemoteFeederDisplay extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private static Hashtable<String, String> eventStore2 = new Hashtable<String, String>();
    private EventProcessHandle eventProcessHandle;
    private String appName1 = "EVENT_PROCESS2";
    private String appName2 = "EVENT_PROCESS3";
    private FioranoApplicationController m_fioranoappcontroller1;
    private ServerStatusController serverstatus;
    private FioranoApplicationController m_fioranoApplicationController;
    private IServiceProviderManager ispm;
    private String m_appGUID;
    private float m_version;
    private float appVersion = 1.0f;
    @Test(groups = "RemoteServiceInstance", alwaysRun = true)

    public void startSetup() {
        //Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestRemoteServiceInstance"));
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestRemoteFeederDisplay"));
        this.eventProcessManager = rmiClient.getEventProcessManager();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        ispm=rmiClient.getServiceProviderManager();
        if (eventProcessManager == null) {
            // Assert.fail("Cannot run Group RemoteServiceInstance as eventProcessManager is not set.");

        }
        this.serverstatus = ServerStatusController.getInstance();
        try {

            this.m_fioranoappcontroller1 = serverstatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("initNewSetup", "TestdelteRouteWithBP19596"), e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestRemoteServiceInstance"));
    }

    @Test(groups = "RemoteServiceInstance", description = "check remote-service-instance output port bug ::21388 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfApplication() throws ServiceException, RemoteException{
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"));
        SampleEventProcessListener epListener1 = null;
        SampleEventProcessListener epListener2 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);
            epListener2 = new SampleEventProcessListener(appName2, eventStore2);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e1);
            //  Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
            eventProcessManager.addEventProcessListener(epListener2);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e);
            //  Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e);
            // Assert.fail("Failed to do ep listener!", e);
        }


        try {
            stopAndDeleteEP(eventProcessManager, appName1, appVersion);
            stopAndDeleteEP(eventProcessManager, appName2, appVersion);
            deployEventProcess("FeederDisplay_1.0.zip");
            deployEventProcess("FeederRemoteDisplay_1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e);
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e);
            //  Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e);
            //  Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            eventProcessManager.startEventProcess(appName2, appVersion, false);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e);
            // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e);
            // Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }


        //delete route between remote feeder and display in EVENT PROCESS2 by redeploying another EVENT PROCESS's runtime data with all the properties except the route b/w the feeder and display
        try {
            deployEventProcess("FeederRemoteDisplay1_1.0.zip");
            Thread.sleep(5000);

        } catch (Exception e) {
            System.out.println("Problem in restarting app2");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"), e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("All are Restarted");
        eventProcessManager.synchronizeEventProcess(appName2,appVersion);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        eventProcessManager.stopAllServiceInstances(appName1,appVersion);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }
        eventProcessManager.stopServiceInstance(appName2, appVersion, "Display1");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e.toString());
        }

        try{
        topicpublisherReceiver("fps", appName1 + "__" + "1_0" + "__" + "feeder1" + "__OUT_PORT", "fps1", appName1 + "__" + "1_0" + "__" + "display1" + "__IN_PORT",appName2+"__"+"1_0" + "__" + "display1"+"__IN_PORT");
        }
        catch (Exception e)
        {
           System.out.println("Error in sending and receiving the message:"+e);

        }

            try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }

            Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfApplication", "TestRemoteFeederDisplay"));
    }

    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "testLaunchOfApplication", alwaysRun = true)
    public void stopApplication() throws ServiceException, RemoteException {
        System.out.println("Stop and delete");
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestRemoteFeederDisplay"));
        stopAndDeleteEP(eventProcessManager,appName2, appVersion);
        stopAndDeleteEP(eventProcessManager,appName1, appVersion);
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopAndDeleteApplication", "TestRemoteFeederDisplay"));
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
    public String topicpublisherReceiver(String servername, String topicname,String servername1,String queuename,String queuename1) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicname);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisherReceiver", "TestRemoteFeederDisplay"), e);
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiverReceiver", "TestRemoteFeederDisplay"), e);

            Assert.fail("Failed to do create receiver to inport", e);
        }

        Receiver rec1=new Receiver();
        rec1.setDestinationName(queuename1);
        rec1.setCf("primaryQCF");
        rec1.setUser("anonymous");
        rec1.setPasswd("anonymous");
        try {
            rec1.initialize(servername1);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiverReceiver", "TestRemoteFeederDisplay"), e);

            Assert.fail("Failed to do create receiver to inport", e);
        }

        try {

            pub.sendMessage(messageSent);
            //System.out.println("Message sent:"+messageSent);
        } catch (Exception e) {
            //System.out.println("Message not sent");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisherReceiver", "TestRemoteFeederDisplay"), e);
            Assert.fail("Failed to do publish message on outport", e);
        }

        String messageOnDestination = null;

        try {

            messageOnDestination = rec.getMessageOnDestination(2000);

           // System.out.println("Received 1:"+messageOnDestination);
        } catch (Exception e) {
            //System.out.println(" not Received 1");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiverReceiver", "TestRemoteFeederDisplay"), e);
            Assert.fail("Failed to do get message from inport", e);
            rec.close();
        }

        String messageOnDestination1 = null;
        try {
            messageOnDestination1 = rec1.getMessageOnDestination(2000);
            System.out.println("Received 2:"+messageOnDestination1);
        } catch (Exception e) {
            System.out.println(" not Received 2");
            //Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiverReceiver", "TestRemoteFeederDisplay"), e);
            //Assert.fail("Failed to do get message from inport", e);
        } finally {
            Assert.assertEquals(messageSent, messageOnDestination);
            if(messageOnDestination.equals(messageSent)&&!messageSent.equals(messageOnDestination1))
                System.out.println("Test passed :: TestRemoteFeederDisplay");
            else
            {
                System.out.println("Test failed :: TestRemoteFeederDisplay");
                Assert.fail("Test failed :: TestRemoteFeederDisplay");
            }
            rec1.close();
            rec.close();
            pub.close();
        }




     return messageSent;
    }



    /*
            *********** end  ***********

        @Test(enabled = false)
    public void createPubSub() {

        Publisher pub = new Publisher();
        pub.setDestinationName(appName1 + "__" + "FEEDER1" + "__OUT_PORT");
        pub.setCf(appName1 + "__" + "FEEDER1");
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
            System.out.println("senet:"+messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            //  Assert.fail("Failed to do publish message on outport", e);
        } finally {
          //  pub.close();
        }

        Receiver rec = new Receiver();
        rec.setDestinationName(appName1 + "__" + "DISPLAY1" + "__IN_PORT");
        rec.setCf(appName1 + "__" + "Display1");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("fps1");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            //  Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            System.out.println("before reciving");
            messageOnDestination = rec.getMessageOnDestination();
            System.out.println("recieved:"+messageOnDestination);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            System.out.println("EEEEERR:"+e);
            //  Assert.fail("Failed to do get message from inport", e);
        } finally {
            //rec.close();
        }
        if(messageSent.equals(messageOnDestination))
           System.out.println("Success");
        else
            System.out.println("Test fail");
        try{
        pub.close();
        rec.close();
        }
        catch (Exception e)
        {
            System.out.println("erroe man"+e);
        }
        System.out.println("Closed");
        // Assert.assertEquals(messageSent, messageOnDestination);
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
    //private void deleteRoute(String RName,String SPName,String DPName){
    private void deleteRoute(Route route){

        /*
        Route route = new Route();

        route.setName(RName);
        route.setSourcePortInstance(SPName);
        route.setTargetPortInstance(DPName);
        */ /*
    Application application = null;
    try{
        try {
            application = m_fioranoappcontroller.getApplication(appName2, appVersion);
        } catch (TifosiException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteRoute", "TestRemoteServiceInstance"), e1);
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("Source :"+route.getSourceServiceInstance());
        System.out.println("Target :"+route.getTargetServiceInstance());
        application.removeRoute(route);
    }
    catch(Exception e)
    {
        Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteRoute", "TestRemoteServiceInstance"), e);
        System.out.println("Error while deleting");
    }
}
     */
}