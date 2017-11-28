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
import org.testng.annotations.Test;
import org.testng.Assert;

import javax.jms.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ramesh
 * Date: Jan 26, 2011
 * Time: 3:53:17 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestFeederAttachment extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "FEEDERATTACHMENT";
    private TestEnvironmentConfig testEnvConfig;
    private SampleEventProcessListener epListener = null;
    private byte[] sentFile;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.testEnvConfig = ESBTestHarness.getTestEnvConfig();
        if (eventProcessManager == null) {
            //Assert.fail("Cannot run Group Bugs. as eventProcessManager is not set.");
        }
    }


    @Test(groups = "Bugs", description = "Send Large Message from Feeder - bug 11503 ", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestFeederAttachmentLaunch() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e1);
            //Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            //eventProcessManager.addEventProcessListener(epListener, appName);
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("feederattachment-1.0@EnterpriseServer.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
            //Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
            //Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
            //Assert.fail("Failed to do CRC!", e);
        }
        try {
            //eventProcessManager.startEventProcess(appName, appVersion, false);
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        //using FileInputStream for adding attachment to msg
        try {
            String Filename = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME) + File.separator + "esb" + File.separator +
                    "TestNG" + File.separator + "resources" + File.separator + "send.zip";
            FileInputStream fis = new FileInputStream(Filename);
            int bytesAvailable = fis.available();
            sentFile = new byte[bytesAvailable];
            fis.read(sentFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            eventProcessManager.stopServiceInstance(appName,appVersion, "Display1");
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        }

        topicPublisher("fps", appName + "__" + "1_0" + "__" + "FEEDER1" + "__OUT_PORT");
        queueReceiver("fps", appName +"__" + "1_0" + "__" + "DISPLAY1" + "__IN_PORT");

        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestFeederAttachmentLaunch", "TestFeederAttachment"));
    }

    /**
     * Method to create FMQ pub/sub
     */
    @Test(enabled = false)
    public void topicPublisher(String serverName, String topicName) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicName);
        pub.setCf(appName + "__" + "1_0" + "__" + "FEEDER1");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");

        try {
            pub.initialize(serverName);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicPublisher", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do create publisher to Out port", e);
        }

        try {
            ObjectMessage textMessage = pub.getSession().createObjectMessage();
            //textMessage.setText("Msg from Feeder");
            textMessage.setObject(sentFile);
            Message msg = textMessage;
            pub.sendMessage(msg);
            System.out.println("Msg sent from Feeder Out port ::TestFeederAttachment");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicPublisher", "TestFeederAttachment"), e);
            Assert.fail("Failed to do publish message on Out port", e);
        } finally {
            pub.close();
        }

    }

    @Test(enabled = false)
    public void queueReceiver(String serverName, String queueName) {
        Receiver sub = new Receiver();

        sub.setDestinationName(queueName);
        sub.setCf(appName + "__" + "1_0" + "__" + "DISPLAY1");
        sub.setUser("anonymous");
        sub.setPasswd("anonymous");

        try {
            sub.initialize(serverName);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queueReceiver", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do create receiver to INport", e);
        }
        //checking the sent attachment with the received one
        try {
            Object ob = sub.getMsgObj(new Object());
            Assert.assertEquals(ob, sentFile);
            System.out.println("Msg with 30MB attachment has been received to Display IN port ::TestFeederAttachment");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queueReceiver", "TestFeederAttachment"), e);
            Assert.fail("Failed to do get message from In port", e);
        } finally {
            sub.close();
        }

    }

    @Test(groups = "Bugs", dependsOnMethods = "TestFeederAttachmentLaunch", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestFeederAttachment"));
        try {
            //eventProcessManager.stopEventProcess(appName, appVersion);
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestFeederAttachment"), e);
           // Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestFeederAttachment"));
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" +
                File.separator + "resources" + File.separator + zipName));
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

}
