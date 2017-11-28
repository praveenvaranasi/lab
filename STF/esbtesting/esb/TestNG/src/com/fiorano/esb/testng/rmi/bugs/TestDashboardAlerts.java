package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.bam.alert.notification.INotification;
import com.fiorano.esb.bam.alert.notification.smtp.EmailNotification;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
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
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import fiorano.tifosi.dmi.events.emailAlerts.*;
import fiorano.tifosi.dmi.events.emailAlerts.AbstractEventAlert.*;
import fiorano.tifosi.dmi.events.emailAlerts.FPSStartStopAlert.*;
import fiorano.tifosi.dmi.rule.FioranoPolicy;
import fiorano.tifosi.dmi.rule.IFioranoPolicy;
import fiorano.tifosi.dmi.rule.bmr.BacklogMonPolicy;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jms.TextMessage;
import javax.management.Attribute;
import javax.management.ObjectName;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ramesh
 * Date: 2/7/11
 * Time: 6:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDashboardAlerts extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName1 = "GMAILTEST";
    private String appName2 = "SIMPLECHAT";
    private SampleEventProcessListener epListener = null;
    private TestEnvironmentConfig testEnvConfig;
    private ObjectName EventEmailNotifier;
    private ObjectName policyManagerObjName;
    private ObjectName alertManagerObjName;
    private TextMessage textMessage;
    private String fioranoHome = null;
    private String jmsnotif_file_path = null;
    private float appVersion = 1.0f;
    private String backlog_file_path = null;
    private FioranoApplicationController m_fioFioranoApplicationController;
    Publisher pub = new Publisher();
    Receiver sub = new Receiver();


    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.testEnvConfig = ESBTestHarness.getTestEnvConfig();
        fioranoHome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        jmsnotif_file_path = fioranoHome + File.separator + "runtimedata" + File.separator + "repository" + File.separator + "Notifications";
        backlog_file_path = fioranoHome + File.separator + "runtimedata" + File.separator + "repository" + File.separator + "Policies" + File.separator + "Backlog";
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        try {
            m_fioFioranoApplicationController = ServerStatusController.getInstance().getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestDashboardAlerts"), e);
            //Assert.fail("Failed to run test,since we could not fioranoapplicationcontroller!", e);
        }
        try {
            String certsHome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME) + File.separator + "esb" + File.separator + "server" +
                    File.separator + "profiles" + File.separator + "certs" + File.separator + "jssecacerts";
            String TestingHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME) + File.separator + "esb" + File.separator + "TestNG" +
                    File.separator + "resources" + File.separator + "Certs" + File.separator;
            File f1 = new File(TestingHome + "jssecacerts");
            File f2 = new File(certsHome);
            f2.delete();
            f2 = new File(certsHome);
            InputStream in = new FileInputStream(f1);

            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();

            System.out.println("Running TestDashboardAlerts STF testcase:");
            String osname = System.getProperty("os.name");
            if (osname.startsWith("Windows")) {
                certsHome = "\"" + certsHome + "\"";
            }
            Process p1 = null;
            p1 = Runtime.getRuntime().exec("java -classpath " + TestingHome + " InstallCert smtp.gmail.com:465 passphrase " + certsHome);
            Thread.sleep(1000);

            BufferedReader bin = new BufferedReader(
                    new InputStreamReader(p1.getInputStream()));
            String line1 = null;

            while ((line1 = bin.readLine()) != null) {
                System.out.println(line1);
            }

            bin.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(groups = "Bugs", description = "bug 19687 - Dashboard SMTP alerts for ssl server", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestAlertsWithSSL() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestAlertsWithSSL", "TestDashboardAlerts"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestAlertsWithSSL", "TestDashboardAlerts"));

        try {
            epListener = new SampleEventProcessListener(appName1, eventStore);
            eventProcessManager.addEventProcessListener(epListener);
            deployEventProcess("GmailTest.zip");
            Thread.sleep(4000);
            eventProcessManager.checkResourcesAndConnectivity(appName1, 1.0f);
            eventProcessManager.startEventProcess(appName1, 1.0f, false);
            Thread.sleep(5000);
            eventStore.clear();
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAlertsWithSSL", "TestDashboardAlerts"), e);
            e.printStackTrace();
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAlertsWithSSL", "TestDashboardAlerts"), e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAlertsWithSSL", "TestDashboardAlerts"), e);
            e.printStackTrace();
        }

        try {
            eventProcessManager.startAllServices(appName1, 1.0f);
            Thread.sleep(40000);
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "3");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAlertsWithSSL", "TestDashboardAlerts"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAlertsWithSSL", "TestDashboardAlerts"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAlertsWithSSL", "TestDashboardAlerts"), e);
            e.printStackTrace();
        }

        topicPublisher("fps", appName1 + "__"+ "1_0 " + "__" + "FEEDER1" + "__OUT_PORT");
        queueReceiver("fps", appName1 + "__"+ "1_0 "+ "__" + "DISPLAY1" + "__IN_PORT");

        popAllMails();
        setSMTPSettings();
        createSMTPAlert();

        try {
            rmiClient.getFpsManager().restartPeer("fps");
            Thread.sleep(60000);
            System.out.println("Peer fps has been restarted");
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAlertsWithSSL", "TestDashboardAlerts"), e);
            e.printStackTrace();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAlertsWithSSL", "TestDashboardAlerts"), e);
            //Assert.fail("Failed to restart the peer", e);
        }

        try {
            pub.sendMessage(textMessage);
            Thread.sleep(2000);
            String msg = sub.getMessageOnDestination();
            if (msg.contains("TIFOSI_SERVER_SHUTDOWN"))
                System.out.println("Mail alert for FPS shutdown event has been successfully popped");
            else {
               // Assert.fail("Mail Alert for FPS shutdown event has not been received");
            }
            deleteSMTPAlert();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAlertsWithSSL", "TestDashboardAlerts"), e);
            //Assert.fail("Failed to do publish message on Out port", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestAlertsWithSSL", "TestDashboardAlerts"));

    }

    @Test(groups = "Bugs", description = "bug 19687 - Dashboard Email alerts for StartTLS server", dependsOnMethods = "TestAlertsWithSSL", alwaysRun = true)
    public void TestStartTLSAlerts() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStartTLSAlerts", "TestDashboardAlerts"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestStartTLSAlerts", "TestDashboardAlerts"));

        try {
            Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
            SampleEventProcessListener epListener1 = new SampleEventProcessListener(appName2, eventStore1);
            eventProcessManager.addEventProcessListener(epListener1);
            Thread.sleep(4000);

            eventProcessManager.checkResourcesAndConnectivity(appName2, 1.0f);
            eventProcessManager.startEventProcess(appName2, 1.0f, false);
            Thread.sleep(5000);

            eventProcessManager.startAllServices(appName2, 1.0f);
            Thread.sleep(20000);
           // Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName2), "2");

            eventProcessManager.stopServiceInstance(appName2, appVersion, "chat2");
            Thread.sleep(2000);
            //Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName2), "1");

        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartTLSAlerts", "TestDashboardAlerts"), e);
            e.printStackTrace();
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartTLSAlerts", "TestDashboardAlerts"), e);
            e.printStackTrace();
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartTLSAlerts", "TestDashboardAlerts"), e);
            e.printStackTrace();
        }

        try {
            eventStore.clear();
            eventProcessManager.restartEventProcess(appName1, appVersion);
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartTLSAlerts", "TestDashboardAlerts"), e);
            e.printStackTrace();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartTLSAlerts", "TestDashboardAlerts"), e);
            //Assert.fail("Failed to restart the EP GmailTest", e);
        }

        try {
            ServiceInstanceStateDetails service = m_fioFioranoApplicationController.getCurrentStateOfService(appName1, appVersion, "Feeder1");
            ServiceInstanceStateDetails service1 = m_fioFioranoApplicationController.getCurrentStateOfService(appName1, appVersion, "Display1");
            ServiceInstanceStateDetails service2 = m_fioFioranoApplicationController.getCurrentStateOfService(appName1, appVersion, "POP31");
            if (service.getStatusString().equals("SERVICE_HANDLE_UNBOUND")){
                //Assert.fail("Feeder1 is not running after EP restart");
            }
            if (service1.getStatusString().equals("SERVICE_HANDLE_UNBOUND")){
               // Assert.fail("Display1 is not running after EP restart");
            }
            if (service2.getStatusString().equals("SERVICE_HANDLE_UNBOUND")){
                //Assert.fail("POP31 is not running after EP restart");
            }
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartTLSAlerts", "TestDashboardAlerts"), e);
           // Assert.fail("not able to execute getWSContexts.", e);
        }

        addSMTPEmailNotification();
        addBacklogPolicy();
        popAllMails();

        try {
            Publisher pub2 = new Publisher();
            pub2.setDestinationName(appName2 + "__"+"1_0"+"__" + "CHAT1" + "__OUT_PORT");
            pub2.setCf(appName2 + "__" + "1_0" + "__" + "CHAT1");
            pub2.setUser("anonymous");
            pub2.setPasswd("anonymous");
            pub2.initialize("fps");
            for (int i = 0; i < 8; i++)
                pub2.sendMessage("STF Fiorano");
            pub2.close();
            System.out.println("Messages req. to reach the policy threshold value have been sent");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartTLSAlerts", "TestDashboardAlerts"), e);
            //Assert.fail("Exception occured while sending message from publisher", e);
        }

        try {
            Thread.sleep(50000);
            pub.sendMessage(textMessage);
            String msg = sub.getMessageOnDestination();
            if (msg.contains("Email Notification for BacklogMonPolicy"))
                System.out.println("SMTP Email Notification for backlog policy has been successfully popped");
            else {
               // Assert.fail("Email Notification for backlog policy event has not been received");
            }
            BacklogCleanup();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartTLSAlerts", "TestDashboardAlerts"), e);
            //Assert.fail("Failed to do publish message on Out port", e);
        }

        closePubSub();
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestStartTLSAlerts", "TestDashboardAlerts"));

    }

    @Test(enabled = false)
    private void setParentParams(FPSStartStopAlert eventAlert, String alertId, EventCategory eventCategory, ArrayList<String> emailDetails) {
        eventAlert.setAlertId(alertId);
        eventAlert.setReceipientAddress(emailDetails.get(0));
        eventAlert.setSenderName(emailDetails.get(1));
        eventAlert.setSenderAddress(emailDetails.get(2));
        eventAlert.setEventCategory(eventCategory);
    }

    @Test(enabled = false)
    public void createSMTPAlert() {
        Logger.LogMethodOrder(Logger.getOrderMessage("createSMTPAlert", "TestDashboardAlerts"));
        try {
            String eventType = "FPS_LAUNCH_KILL";
            EventCategory eventCategory = EventCategory.WARNING;
            String params = "fps";
            ArrayList<String> emailDetails = new ArrayList<String>(3);
            emailDetails.add(0, "stf.fiorano@gmail.com");
            emailDetails.add(1, "Fiorano STF");
            emailDetails.add(2, "stf.fiorano@gmail.com");
            EventEmailNotifier = new ObjectName("Fiorano.Esb.Events:ServiceType=EventEmailNotifier,Name=ESBEventEmailNotifier");
            //Object[] parameters = {emailDetails, eventType, eventCategory, params};

            FPSStartStopAlert fpsStartStopAlert = new FPSStartStopAlert();
            setParentParams(fpsStartStopAlert, "STFAlert", eventCategory, emailDetails);
            fpsStartStopAlert.setFpsName(params);
            AbstractEventAlert objabsEventAlert = (AbstractEventAlert)fpsStartStopAlert;
            Object[] parameters = new Object[]{objabsEventAlert, eventType, eventCategory+""};


            String[] signatures = {AbstractEventAlert.class.getName(), String.class.getName(), String.class.getName()};

            jmxClient.invoke(EventEmailNotifier, "addEmailID", parameters, signatures);
            System.out.println("New SMTP alert for peer server shutdown has been registered ::TestDashboardAlerts");

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createSMTPAlert", "TestDashboardAlerts"), e);
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public void setSMTPSettings() {
        Logger.LogMethodOrder(Logger.getOrderMessage("setSMTPSettings", "TestDashboardAlerts"));
        try {
            String mailServerName = "smtp.gmail.com";
            int mailServerPort = 465;
            String secureConnectionType = "SSL/TLS";
            boolean authenticationRequired = true;
            String userName = "stf.fiorano";
            String password = "fiorano12";
            ObjectName objName1 = new ObjectName("Fiorano.Esb.Events:ServiceType=EventEmailNotifier,Name=ESBEventEmailNotifier,type=config");
            Attribute attr1 = new Attribute("MailServerName", mailServerName);
            Attribute attr2 = new Attribute("MailServerPort", mailServerPort);
            Attribute attr3 = new Attribute("AuthenticationRequired", authenticationRequired);
            Attribute attr4 = new Attribute("SecureConnectionType", secureConnectionType);
            Attribute attr5 = new Attribute("UserName", userName);
            Attribute attr6 = new Attribute("Password", password);
            JMXClient.getMBeanServerConnection().setAttribute(objName1, attr1);
            JMXClient.getMBeanServerConnection().setAttribute(objName1, attr2);
            JMXClient.getMBeanServerConnection().setAttribute(objName1, attr3);
            JMXClient.getMBeanServerConnection().setAttribute(objName1, attr4);
            JMXClient.getMBeanServerConnection().setAttribute(objName1, attr5);
            JMXClient.getMBeanServerConnection().setAttribute(objName1, attr6);
            System.out.println("SMTP server settings have been configured ::TestDashboardAlerts");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("setSMTPSettings", "TestDashboardAlerts"), e);
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public void deleteSMTPAlert() {
        Logger.LogMethodOrder(Logger.getOrderMessage("deleteSMTPAlert", "TestDashboardAlerts"));
        try {
            String eventType = "FPS_LAUNCH_KILL";
            String eventCategory = "Warning";
            String params = "fps";
            ArrayList<String> emailDetails = new ArrayList<String>(3);
            emailDetails.add(0, "stf.fiorano@gmail.com");
            emailDetails.add(1, "Fiorano STF");
            emailDetails.add(2, "stf.fiorano@gmail.com");
            Object[] parameters = {emailDetails, eventType, eventCategory, params};
            String[] signatures = {ArrayList.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()};
            jmxClient.invoke(EventEmailNotifier, "removeEmailID", parameters, signatures);
            System.out.println("newly created SMTP alert has been deRegistered");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteSMTPAlert", "TestDashboardAlerts"), e);
            e.printStackTrace();
        }

    }

    @Test(enabled = false)
    public void addSMTPEmailNotification() {
        Logger.LogMethodOrder(Logger.getOrderMessage("addSMTPEmailNotification", "TestDashboardAlerts"));
        EmailNotification emailNotification = new EmailNotification();
        emailNotification.setSmtp_host("smtp.gmail.com");
        emailNotification.setSmtp_port(587);
        emailNotification.setSecureConnectionType("STARTTLS");
        emailNotification.setSmtp_username("stf.fiorano@gmail.com");
        emailNotification.setSmtp_password("fiorano12");
        emailNotification.setFromAddress("stf.fiorano@gmail.com");
        emailNotification.setFromName("Fiorano STF");
        emailNotification.setTo("stf.fiorano@gmail.com");
        emailNotification.setUseTransport(true);
        emailNotification.setNotificationID("test");

        Object[] params = {rmiClient.getHandleID(), emailNotification, false};
        String[] signatures = {String.class.getName(), INotification.class.getName(), "boolean"};

        try {
            File jmsnotif = new File(jmsnotif_file_path, "test.xml");
            if (jmsnotif.exists())
                jmsnotif.delete();
            alertManagerObjName = new ObjectName("Fiorano.Esb.Bam.Alert.Manager:ServiceType=AlertManager,Name=AlertManager");
            jmxClient.invoke(alertManagerObjName, "persistNotification", params, signatures);
            System.out.println("Email Notification SMTP alert for Backlog Policy has been created");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("addSMTPEmailNotification", "TestDashboardAlerts"), e);
            e.printStackTrace();
        }

    }

    @Test(enabled = false)
    public void addBacklogPolicy() {
        Logger.LogMethodOrder(Logger.getOrderMessage("addBacklogPolicy", "TestDashboardAlerts"));
        BacklogMonPolicy policy = new BacklogMonPolicy();
        policy.setRuleID("test");
        policy.setType(IFioranoPolicy.BACKLOG_MONITOR_POLICY);
        policy.setApplicationGUID(appName2);
        policy.setApplicationVersion(1.0f);
        policy.setServiceInstanceGUID("chat2");
        policy.setPortName("IN_PORT");
        ArrayList<String> alerts = new ArrayList<String>(1);
        alerts.add(0, "test");
        policy.setAlerts(alerts);
        policy.setBacklogValue("6");
        policy.setDirection(BacklogMonPolicy.Direction.ANY_DIRECTION);
        policy.setSubject("Email Notification for BacklogMonPolicy");
        policy.setMessageBody("Email Notification via SMTP Transport");
        Object[] params = {policy, false};
        String[] signatures = {FioranoPolicy.class.getName(), "boolean"};

        try {
            File bklog = new File(backlog_file_path, "test.xml");
            if (bklog.exists())
                bklog.delete();
            policyManagerObjName = new ObjectName("Fiorano.Esb.Bam.Policy.Manager:ServiceType=PolicyManager,Name=PolicyManager");
            jmxClient.invoke(policyManagerObjName, "addPolicy", params, signatures);
            //apply/activate backlog policy

            Object[] params1 = {"test"};
            String[] signatures1 = {String.class.getName()};
            jmxClient.invoke(policyManagerObjName, "applyBacklogMonitoringPolicy", params1, signatures1);
            System.out.println("New Backlog Monitor Policy has been activated ::TestDashboardAlerts");

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("addBacklogPolicy", "TestDashboardAlerts"), e);
            org.testng.Assert.fail("Failed while creating policyManagerObjectName", e);
        }
    }

    @Test(enabled = false)
    public void BacklogCleanup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("BacklogCleanup", "TestDashboardAlerts"));
        try {
            Object[] params = {"test", IFioranoPolicy.BACKLOG_MONITOR_POLICY};
            String[] signatures = {String.class.getName(), String.class.getName()};
            jmxClient.invoke(policyManagerObjName, "deletePolicy", params, signatures);
            Thread.sleep(2000);
            System.out.println("Backlog policy in use has been deActivated");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("BacklogCleanup", "TestDashboardAlerts"), e);
            org.testng.Assert.fail("Fail to delete backlog policy the service", e);
        }
        try {
            Object[] params = {"test"};
            String[] signatures = {String.class.getName()};
            jmxClient.invoke(alertManagerObjName, "removeNotification", params, signatures);
            Thread.sleep(2000);
            System.out.println("new SMTP Email notification for backlog policy has been removed ::TestDashboardAlerts");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("BacklogCleanup", "TestDashboardAlerts"), e);
            org.testng.Assert.fail("Fail to remove SMTP Email notification the service", e);
        }
    }

    @Test(enabled = false)
    public void topicPublisher(String serverName, String topicName) {

        pub.setDestinationName(topicName);
        pub.setCf(appName1 + "__"+ "1_0" +"__" + "FEEDER1");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");

        try {
            pub.initialize(serverName);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicPublisher", "TestDashboardAlerts"), e);
           // Assert.fail("Failed to do create publisher to Out port", e);
        }
    }

    @Test(enabled = false)
    public void queueReceiver(String serverName, String queueName) {

        sub.setDestinationName(queueName);
        sub.setCf(appName1 + "__" + "1_0" + "__" + "DISPLAY1");
        sub.setUser("anonymous");
        sub.setPasswd("anonymous");

        try {
            sub.initialize(serverName);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queueReceiver", "TestDashboardAlerts"), e);
           // Assert.fail("Failed to do create receiver to INport", e);
        }
    }

    @Test(enabled = false)
    public void closePubSub() {
        pub.close();
        sub.close();
    }

    @Test(enabled = false)
    public void popAllMails() {

        try {
            textMessage = pub.getSession().createTextMessage();
            textMessage.setText("<ns1:Input xmlns:ns1=\"http://www.fiorano.com/fesb/activity/POP31/In\">\n" +
                    "<MessageCount>1</MessageCount>\n" + "</ns1:Input>");

            String blankmsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<ns1:Email xmlns:ns1=\"http://www.fiorano.com/fesb/activity/POP31/Out\" xmlns:ns2=\"http://www.w3.org/2001/XMLSchema\">\n" +
                    "  <ns1:To/>\n" + "  <ns1:From/>\n" + "  <ns1:Subject/>\n" + "</ns1:Email>\n";

            boolean flag = true;

            String osname = System.getProperty("os.name");
            if (osname.startsWith("Windows")) {
                blankmsg = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                        "<ns1:Email xmlns:ns1=\"http://www.fiorano.com/fesb/activity/POP31/Out\" xmlns:ns2=\"http://www.w3.org/2001/XMLSchema\">\r\n" +
                        "  <ns1:To/>\r\n" + "  <ns1:From/>\r\n" + "  <ns1:Subject/>\r\n" + "</ns1:Email>\r\n";
            }

            while (flag) {
                pub.sendMessage(textMessage);
                Thread.sleep(1500);
                String msg = sub.getMessageOnDestination();
                if (msg.compareTo(blankmsg) == 0) {
                    System.out.println("All previous mails in inbox have been popped ::TestDashboardAlerts");
                    System.out.println("Ready to pop new mail alerts from dashboard ::TestDashboardAlerts");
                    return;
                }
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("popAllMails", "TestDashboardAlerts"), e);
           // Assert.fail("Failed to do publish message on Out port", e);
        }
    }

    @Test(groups = "Bugs", dependsOnMethods = "TestStartTLSAlerts", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestDashboardAlerts"));
        try {
            eventProcessManager.stopEventProcess(appName2, 1.0f);
            eventProcessManager.stopEventProcess(appName1, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName1, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestDashboardAlerts"), e);
           // Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestDashboardAlerts"), e);
           // Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestDashboardAlerts"));
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
