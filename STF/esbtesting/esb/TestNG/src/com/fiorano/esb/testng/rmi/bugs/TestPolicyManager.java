package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.bam.alert.notification.INotification;
import com.fiorano.esb.bam.alert.notification.jms.JMSNotification;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.rule.FioranoPolicy;
import fiorano.tifosi.dmi.rule.IFioranoPolicy;
import fiorano.tifosi.dmi.rule.bmr.BacklogMonPolicy;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 1/24/11
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestPolicyManager extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "SIMPLECHAT";
    private TestEnvironmentConfig testEnvConfig = null;
    private ObjectName policyManagerObjName = null;
    private String jmsAlert_file_path = null;
    private String backlog_file_path = null;
    private String fioranoHome = null;
    private JMXClient esb = JMXClient.getInstance();
    private ObjectName alertManagerObjName = null;
    private float appVersion = 1.0f;

    @Test(groups = "PolicyManager", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            //Assert.fail("Cannot run Group Accented component. as eventProcessManager is not set.");
        }
        testEnvConfig = ESBTestHarness.getTestEnvConfig();
        fioranoHome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        jmsAlert_file_path = fioranoHome + File.separator + "runtimedata" + File.separator + "repository" + File.separator + "Notifications";
        backlog_file_path = fioranoHome + File.separator + "runtimedata" + File.separator + "repository" + File.separator + "Policies" + File.separator + "Backlog";
    }

    @Test(groups = "PolicyManager", description = "bug17950:Additional add new policy options", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testPolicyManager() {

        Logger.LogMethodOrder(Logger.getOrderMessage("testPolicyManager", "TestPolicyManager"));
        SampleEventProcessListener sepl = null;

        try {
            sepl = new SampleEventProcessListener(appGUID, eventStore);
            eventProcessManager.addEventProcessListener(sepl);
            //Thread.sleep(5000);
        } catch (Exception e) {
           // Assert.fail("Fail to add sample event process listener");
        }
        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
            //Thread.sleep(2000);
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
            Thread.sleep(1000);
            eventStore.clear();
            eventProcessManager.startAllServices(appGUID, appVersion);
            Thread.sleep(10000);
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "2");
            eventProcessManager.stopServiceInstance(appGUID,appVersion, "chat2");
            //TODO:add jms alert and later policy to jms alert.
            persistJms();
            addBacklog();

            //create publisher to chat1 and publish  more that 8 messages to verify the policy levels for 5,6,7
            createPub("SIMPLECHAT__1_0__CHAT1__OUT_PORT");
            Thread.sleep(3000);
            createTopicSub("JMS_POLICY_ALERT", "fes");
            Thread.sleep(2000);
            eventProcessManager.startServiceInstance(appGUID,appVersion, "chat2");
            onMessage();

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testPolicyManager", "TestPolicyManager"), e);
            //org.testng.Assert.fail("Fail to start application", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testPolicyManager", "TestPolicyManager"));

    }


    @Test(groups = "PolicyManager", description = "bug17950:Additional add new policy options", dependsOnMethods = "testPolicyManager", alwaysRun = true)
    public void stopApplicationAndDeleteAlerts() {
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopApplicationAndDeleteAlerts", "TestPolicyManager"), e1);
            org.testng.Assert.fail("Fail to stop the service", e1);
        } catch (ServiceException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopApplicationAndDeleteAlerts", "TestPolicyManager"), e1);
            org.testng.Assert.fail("Fail to stop the service", e1);
        }
        //delete policy and alert

        try {
            Thread.sleep(2000);
            Object[] params = {"test", IFioranoPolicy.BACKLOG_MONITOR_POLICY};
            String[] signatures = {String.class.getName(), String.class.getName()};
            esb.invoke(policyManagerObjName, "deletePolicy", params, signatures);
            Thread.sleep(2000);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopApplicationAndDeleteAlerts", "TestPolicyManager"), e);
            //org.testng.Assert.fail("Fail to delete backlog policy the service", e);
        }
        try {
            Object[] params = {"test"};
            String[] signatures = {String.class.getName()};
            esb.invoke(alertManagerObjName, "removeNotification", params, signatures);
            Thread.sleep(2000);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopApplicationAndDeleteAlerts", "TestPolicyManager"), e);
            //org.testng.Assert.fail("Fail to remove JMS alert notification the service", e);

        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopApplicationAndDeleteAlerts", "TestPolicyManager"));

    }

    @Test(enabled = false)
    public void persistJms() {
        HashMap serverdetails = null;
        try {
            serverdetails = rmiClient.getServiceProviderManager().getServerDetails();
        } catch (RemoteException e) {
           // Assert.fail("Fail to get server details");
        } catch (ServiceException e) {
           // Assert.fail("Fail to get server details");
        }
        String url = (String) serverdetails.get("Server URL");


        JMSNotification jmsNotification = new JMSNotification();
        jmsNotification.setNotificationID("test");
        jmsNotification.setProviderURL(url);
//        String url1 = "http://localhost:1847";
        jmsNotification.setUseTransport(false);
        url = (String) serverdetails.get("Backup URLs");
        jmsNotification.setBackupConnectURLs(url);
        jmsNotification.setInitialContextFactory("fiorano.jms.runtime.naming.FioranoInitialContextFactory");
        jmsNotification.setConnectionFactory("PrimaryCF");
        jmsNotification.setSecurityPrincipal("anonymous");
        jmsNotification.setSecurityCredentials("anonymous");
        jmsNotification.setDestinationName("JMS_POLICY_ALERT");
        jmsNotification.setDestinationType("Topic");
        jmsNotification.setDeliveryMode(2);
        jmsNotification.setAutoCreateDestination(true);
        jmsNotification.setPriority(4);
        jmsNotification.setTimeToLive(0);

        Object[] params = {jmsNotification, false};
        String[] signatures = {INotification.class.getName(), "boolean"};
        //url1 = url1.split(":")[0] + ":" +url1.split(":")[1] + ":" +
        ArrayList urlDetails = null;
        try {
            File jmsnotif = new File(jmsAlert_file_path, "test.xml");
            if (jmsnotif.exists())
                jmsnotif.delete();
            urlDetails = AbstractTestNG.getActiveFESUrl();
            alertManagerObjName = new ObjectName("Fiorano.Esb.Bam.Alert.Manager:ServiceType=AlertManager,Name=AlertManager");
            JMXClient.connect("admin", "passwd", urlDetails.get(0).toString(), Integer.parseInt(urlDetails.get(1).toString()));
            esb.invoke(alertManagerObjName, "persistNotification", params, signatures);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("persistjms", "TestPolicyManager"), e);
           // org.testng.Assert.fail("failed while getting the jmsTransportObjName", e);
        }

    }

    @Test(enabled = false)
    public void addBacklog() {

        BacklogMonPolicy policy = new BacklogMonPolicy();
        policy.setRuleID("test");
        policy.setType(IFioranoPolicy.BACKLOG_MONITOR_POLICY);
        policy.setApplicationGUID(appGUID);
        policy.setApplicationVersion(-appVersion);
        policy.setServiceInstanceGUID("chat2");
        policy.setPortName("IN_PORT");
        ArrayList<String> alerts = new ArrayList<String>(2);
        alerts.add(0, "test");
        policy.setAlerts(alerts);
        policy.setBacklogValue("5,6,7");
        policy.setDirection(BacklogMonPolicy.Direction.ANY_DIRECTION);
        policy.setSubject("FIORANO");
        Object[] params = {policy, false};
        String[] signatures = {FioranoPolicy.class.getName(), "boolean"};
        try {
            File bklog = new File(backlog_file_path, "test.xml");
            if (bklog.exists())
                bklog.delete();
            policyManagerObjName = new ObjectName("Fiorano.Esb.Bam.Policy.Manager:ServiceType=PolicyManager,Name=PolicyManager");
            esb.invoke(policyManagerObjName, "addPolicy", params, signatures);
            //apply/activate backlog policy

            Object[] params1 = {"test"};
            String[] signatures1 = {String.class.getName()};
            /*Collection<BacklogPolicyStateDetails> serverPolicyStateDetails = (Collection<BacklogPolicyStateDetails>) */
            esb.invoke(policyManagerObjName, "applyBacklogMonitoringPolicy", params1, signatures1);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("addBacklog", "TestPolicyManager"), e);
            //org.testng.Assert.fail("Failed while creating policymanagerObjectName", e);
        }
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
           // org.testng.Assert.fail("Failed to do create publisher to outport", e);
        }
        try {
            for (int i = 0; i < 9; i++) {
                pub.sendMessage("hello fiorano");
            }
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            //org.testng.Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

    }

    Receiver topicRecieve = new Receiver();

    @Test(enabled = false)
    public void createTopicSub(String topicName, String serverName) {


        topicRecieve.setDestinationName(topicName);
        topicRecieve.setCf("PRIMARYTCF");
        topicRecieve.setUser("anonymous");
        topicRecieve.setPasswd("anonymous");
        try {
            topicRecieve.initialize(serverName);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createTopicSub", "TestPolicyManager"), e);
            //org.testng.Assert.fail("Failed to do create receiver to alert topic", e);
        }


    }

    @Test(enabled = false)
    public void onMessage() {
        String messageOnDestination = null;
        try {
            for (int i = 0; i < 3; i++) {
                messageOnDestination = topicRecieve.getMessageOnDestination();
                if (!messageOnDestination.equalsIgnoreCase("FIORANO")) {
                  //  Assert.fail("failed to get jms alert");
                }
            }
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
           // org.testng.Assert.fail("Failed to do get message from inport", e);
        } finally {
            topicRecieve.close();
        }
    }

}
