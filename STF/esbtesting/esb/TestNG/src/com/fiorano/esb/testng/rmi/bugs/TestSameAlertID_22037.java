package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.events.emailAlerts.AbstractEventAlert;
import fiorano.tifosi.dmi.events.emailAlerts.FESStartStopAlert;
import org.testng.Assert;
import org.testng.annotations.Test;
import javax.management.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Anubhav
 * Date: 8/23/12
 * Time: 15:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSameAlertID_22037 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();


    private TestEnvironmentConfig testEnvConfig;
    private ObjectName EventEmailNotifier;
    private String fioranoHome = null;
    private String jmsnotif_file_path = null;
    private String backlog_file_path = null;
    private JMXClient esb = null;
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestSameAlertID_22037"), e);
            //Assert.fail("Failed to run test,since we could not fioranoapplicationcontroller!", e);
        }

    }

    @Test(groups = "Bugs", description = "[ESB Dashboard] should not allow the user to add email alert with already existing alert id- BUG 22037", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestSameAlertId() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestSameAlertid", "TestSameAlertID_22037"));

        try {
            esb = JMXClient.getInstance();
            String eventType = "FES";
            String AlertId="Smtp123";
            String eventCategory = "ALL";
            String params = "FES";
            ArrayList<String> emailDetails = new ArrayList<String>(3);
            ArrayList<String> emailDetails1 = new ArrayList<String>(3);
            emailDetails.add(0, "stf.fiorano@gmail.com");
            emailDetails.add(1, "Fiorano STF");
            emailDetails.add(2, "stf.fiorano@gmail.com");
            emailDetails1.add(0, "stf.fiorano@gmail.com");
            emailDetails1.add(1, "Fiorano STF");
            emailDetails1.add(2, "stf.fiorano213@gmail.com");
            EventEmailNotifier = new ObjectName("Fiorano.Esb.Events:ServiceType=EventEmailNotifier,Name=ESBEventEmailNotifier");
            FESStartStopAlert fesStartStopAlert = new FESStartStopAlert();
            fesStartStopAlert.setAlertId(AlertId);
            fesStartStopAlert.setReceipientAddress(emailDetails.get(0));
            fesStartStopAlert.setSenderName(emailDetails.get(1));
            fesStartStopAlert.setSenderAddress(emailDetails.get(2));
            fesStartStopAlert.setEventCategory(AbstractEventAlert.EventCategory.valueOf(eventCategory));

            FESStartStopAlert fesStartStopAlert1 = new FESStartStopAlert();
            fesStartStopAlert1.setAlertId(AlertId);
            fesStartStopAlert1.setReceipientAddress(emailDetails1.get(0));
            fesStartStopAlert1.setSenderName(emailDetails1.get(1));
            fesStartStopAlert1.setSenderAddress(emailDetails1.get(2));
            fesStartStopAlert1.setEventCategory(AbstractEventAlert.EventCategory.valueOf(eventCategory));



            Object[] parameters = new Object[]{fesStartStopAlert, eventType, eventCategory};
            Object[] parameters1 = new Object[]{fesStartStopAlert1, eventType, eventCategory};
            String[] signatures = {AbstractEventAlert.class.getName(), String.class.getName(), String.class.getName()};
            esb.invoke(EventEmailNotifier, "addEmailID", parameters, signatures);
            esb.invoke(EventEmailNotifier, "addEmailID", parameters1, signatures);
           } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSameAlertId", "TestSameAlertID_22037"), e);
            e.printStackTrace();
            Assert.fail("Failed to Add AlertID ",e);

        }catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSameAlertId", "TestSameAlertID_22037"), e);
            e.printStackTrace();
            Assert.fail("Failed to Add AlertID ",e);
        }catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSameAlertId", "TestSameAlertID_22037"), e);
            e.printStackTrace();
            Assert.fail("Failed to Add AlertID ",e);
        }catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSameAlertId", "TestSameAlertID_22037"), e);
            e.printStackTrace();
            Assert.fail("Failed to Add AlertID ",e);
        }catch (MalformedObjectNameException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSameAlertId", "TestSameAlertID_22037"), e);
            e.printStackTrace();
            Assert.fail("Failed to Add AlertID ",e);
        }

    }
}

