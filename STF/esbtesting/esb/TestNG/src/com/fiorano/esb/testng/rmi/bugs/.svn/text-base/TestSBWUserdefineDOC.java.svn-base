package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
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
 * Date: Feb 2, 2011
 * Time: 2:22:07 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestSBWUserdefineDOC extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "SBWUSERDEFINEDOC";
    private float appVersion = 1.0f;
    @Test(groups = "SBWUSERDEFINEDOC", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group SBWUSERDEFINEDOC. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "SBWUSERDEFINEDOC", description = "bug 19128 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"));

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("SBWUSERDEFINEDOCID.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }


        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfApplication", "TestSBWUserdefineDOC"));
    }

    @Test(groups = {"BreakpointReappears"}, description = "bug 19128 ", dependsOnMethods = "TestLaunchOfApplication", alwaysRun = true)
    public void TestSendMessage() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestSendMessage", "TestSBWUserdefineDOC"));
        try {
            eventProcessManager.stopServiceInstance(appGUID,appVersion, "chat2");
            Thread.sleep(6000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        Message message = null;
        try {
            message = topicsubscriberandpublisher("fps", appGUID + "__" + "1_0" + "__" + "chat1" + "__OUT_PORT");
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        TextMessage txtMessage = null;
        if(message != null) {
             txtMessage = (TextMessage) message;
        } else
            Assert.fail("Error: Message Not received from "+appGUID + "__" + "1_0" + "__" + "chat1" + "__OUT_PORT");
        try {
            if (txtMessage != null && !(txtMessage.getStringProperty("ESBX__SYSTEM__USER_DEFINED_DOC_ID").equals("some_value")))   {
                Assert.fail("unable to set user_defined_doc_id");
            }
            else
                System.out.println("Testcase Passeed Successfully");
        } catch (JMSException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCreateListenertoSBWtopic", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to get user_defined doc_id value!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestSendMessage", "TestSBWUserdefineDOC"));
    }

    @Test(groups = "StopEPAfterPeerRestart", description = "bug 18917 ", dependsOnMethods = "TestSendMessage", alwaysRun = true)
    public void TestStopandDeleteEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopandDeleteEventProcess", "TestSBWUserdefineDOC"));
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandDeleteEventProcess", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopandDeleteEventProcess", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopandDeleteEventProcess", "TestSBWUserdefineDOC"));

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
    public Message topicsubscriberandpublisher(String servername, String topicname) throws Exception {
        Receiver rec = new Receiver(false, false);
        Message msg = null;
        rec.setDestinationName("FES_SBW_EVENTS_TOPIC");
        rec.setCf("primaryTCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("fes");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicsubscriberandpublisher", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do create Receiver to outport", e);
        }
        Publisher pub = new Publisher();
        pub.setDestinationName(topicname);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicsubscriberandpublisher", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
            Thread.sleep(10000);
            msg = rec.getMessage();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicsubscriberandpublisher", "TestSBWUserdefineDOC"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {

            pub.close();
            rec.close();
        }
        return msg;
    }


}
