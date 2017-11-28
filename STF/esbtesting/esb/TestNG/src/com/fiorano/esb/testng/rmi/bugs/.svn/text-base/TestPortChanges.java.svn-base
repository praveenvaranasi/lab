package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ServiceInstance;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 1/25/11
 * Time: 4:12 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestPortChanges extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "CHATPORTCHANGE";
    private ServerStatusController serverStatusController;
    private FioranoApplicationController m_fioranoApplicationController;
    private float appVersion = 1.0f;
    private String m_appGUID;
    private float m_version;
    Receiver sub;
    @Test(groups = "PortChangeForChatComponents", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.serverStatusController = ServerStatusController.getInstance();
        try {
            this.m_fioranoApplicationController = serverStatusController.getServiceProvider().getApplicationController();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to create ep listener!", e);
        }
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Unexpected component shutdown. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "PortChangeForChatComponents", description = "bug18604:Unable to Launch Flow when Port destination type is changed ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfChatComponentsAfterPortChange() {

        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"));
        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
            //eventProcessManager.addEventProcessListener(epListener, appGUID,appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("chatportchange1-1.0@EnterpriseServer.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do SAVE!", e);
        }
        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do stop application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do stop application!", e);
        }

        try {

            Application application = m_fioranoApplicationController.getApplication(appGUID, appVersion);
            List<ServiceInstance> serviceInstanceList = application.getServiceInstances();
            serviceInstanceList.get(0).getOutputPortInstance("OUT_PORT").setDestinationType(0);
            serviceInstanceList.get(1).getInputPortInstance("IN_PORT").setDestinationType(1);
            application.setServiceInstances(serviceInstanceList);
            m_fioranoApplicationController.saveApplication(application);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to to get service information!", e);
        }


        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {

            eventProcessManager.startEventProcess(appGUID, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        createTopicSubscriber("CHATPORTCHANGE__1_0__CHAT2__IN_PORT");
        createQueuePub("CHATPORTCHANGE__1_0__CHAT1__OUT_PORT", "fps");
        onMessage();
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
        } catch (Exception e) {
            Assert.fail("Failed to stop the event process", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testLaunchOfChatComponentsAfterPortChange", "PortChangeForChatComponents"));

    }

    @Test(groups = "PortChangeForChatComponents", description = "bug18604:Unable to Launch Flow when Port destination type is changed ", dependsOnMethods = "testLaunchOfChatComponentsAfterPortChange", alwaysRun = true)
    public void testStopDeleteApplication() {
        stopAndDeleteEP(eventProcessManager,appGUID,appVersion);

        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testStopDeleteApplication", "PortChangeForChatComponents"));

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

    @Test(enabled = false)
    public void createQueuePub(String queueName, String serverName) {

        Publisher rec = new Publisher();
        rec.setDestinationName(queueName);
        rec.setCf("primaryQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize(serverName);
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to create sender to destination " + queueName, e);
        }
        try {
            rec.sendMessage("Fiorano");
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do send message to destination " + queueName, e);
        } finally {
            rec.close();
        }
    }



    @Test(enabled = false)
    public void createTopicSubscriber(String topicName) {
        sub = new Receiver();
        sub.setDestinationName(topicName);
        sub.setCf("primaryTCF");
        sub.setUser("anonymous");
        sub.setPasswd("anonymous");
        try {
            sub.initialize("fps");
        } catch (Exception e) {
            Assert.fail("Failed to subscribe to topic", e);
        }

    }

    @Test(enabled = false)
    public void onMessage() {
        String message = null;
        try {
            message = sub.getMessageOnDestination();
        } catch (Exception e) {
            Assert.fail("Failed to fetch the message on topic", e);
        } finally {
            sub.close();
        }
        if (!message.equalsIgnoreCase("Fiorano")) {
            Assert.fail("Failed while comparing messages");
        }
    }


}


