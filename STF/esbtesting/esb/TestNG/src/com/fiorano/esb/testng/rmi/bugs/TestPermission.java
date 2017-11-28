package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.MachineElement;
import com.fiorano.stf.test.core.ProfileElement;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Feb 14, 2011
 * Time: 12:56:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPermission extends AbstractTestNG {
    private JMXClient esb;
    private ServerStatusController serverstatus;
    private TestEnvironment testenv;
    private IEventProcessManager eventProcessManager;
    public IRmiManager rmiManager;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private String appName1 = "CHAT";
    private String username = "NEWUSER";
    private String password = username;
    private String handleID;
    private float appVersion = 1.0f;

    @Test(groups = "TestPermission", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestPermission"));
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            //Assert.fail("Cannot run Group getWSContexts. as eventProcessManager is not set.");
        }
        serverstatus = ServerStatusController.getInstance();
        testenv = serverstatus.getTestEnvironment();
        com.fiorano.stf.test.core.ServerElement se = testenv.getServer("fes");
        Map<String, ProfileElement> profileElements = se.getProfileElements();
        String str = new String("standalone");
        ProfileElement pm = profileElements.get(str);
        String machine = pm.getMachineName();
        MachineElement machine1 = testenv.getMachine(machine);
        esb = JMXClient.getInstance();
        esb.connect("admin", "passwd", machine1.getAddress(), 2047);
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestPermission"));
    }

    @Test(groups = "TestPermission", description = "Permission to change properties of an application not showing correct behaviour:Bug-19188", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOEventProcess", "TestPermission"));
        SampleEventProcessListener epListener1 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);

        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
          //  Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("chat-1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
           // Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
            //Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
            //Assert.fail("Error in thread sleep", e);
        }

        //clear map
        eventStore1.clear();
        try {
            eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(10000);
            //Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "2");


        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestPermission"), e);
            //Assert.fail("Error with thread sleep!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOfEventProcess", "TestPermission"));

    }

    @Test(groups = "TestPermission", dependsOnMethods = "testLaunchOfEventProcess", alwaysRun = true)
    public void testCreateUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateUser", "TestPermission"));
            Object[] params = {username, password};
            String[] signatures = {String.class.getName(), String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.security:ServiceType=RealmManager,Name=SecuritySubSystem");
            esb.invoke(objName, "createUser", params, signatures);
            handleID = esb.getHandleId();
            String permissionName1 = "PERMISSION TO VIEW RUNNING AND SAVED APPLICATIONS";
            ArrayList<String> userNames = new ArrayList();
            userNames.add(username);
            Object[] params1 = {permissionName1, userNames, handleID};
            String[] signatures1 = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            ObjectName objName1 = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName1, "addUsersToSystemPermission", params1, signatures1);
            permissionName1 = "PERMISSION TO CHANGE PROPERTIES OF AN APPLICATION";
            esb.invoke(objName1, "addUsersToSystemPermission", params1, signatures1);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateUser", "TestPermission"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateUser", "TestPermission"), e);
           // Assert.fail("failed to create user", e);
        }
    }


    @Test(groups = "TestPermission", dependsOnMethods = "testCreateUser", alwaysRun = true)
    public void testLoginAsNewUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testLoginAsNewUser", "TestPermission"));
            rmiClient.cleanUp();
            rmiClient = RMIClient.getInstance(username, password);
            eventProcessManager = rmiClient.getEventProcessManager();
            //eventProcessManager = rmiManager.getEventProcessManager(handleID);
           // Assert.assertNotNull(eventProcessManager);
           // Assert.assertEquals(eventProcessManager.toString(), "Event_Process_Manager");
            changeRouteTransformation("route1.zip");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLoginAsNewUser", "TestPermission"));
        } catch (Exception e) {
            // deploy Event has to fail
            Assert.assertTrue(true);
        }
    }

    @Test(groups = "TestPermission", dependsOnMethods = "testLoginAsNewUser", alwaysRun = true)
    public void testLoginAsAdmin() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testLoginAsAdmin", "TestPermission"));
            rmiClient.cleanUp();
            rmiClient = RMIClient.getInstance();
            eventProcessManager = rmiClient.getEventProcessManager();
            eventProcessManager.stopServiceInstance(appName1,appVersion, "chat1");
            eventProcessManager.stopServiceInstance(appName1,appVersion, "chat2");
            createPubSub();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLoginAsAdmin", "TestPermission"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLoginAsAdmin", "TestPermission"), e);
           // Assert.fail();
        }
    }

    @Test(groups = "TestPermission", dependsOnMethods = "testLoginAsAdmin", alwaysRun = true)
    public void testStopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStopAndDeleteApplication", "TestPermission"));
        try {
            eventProcessManager.stopEventProcess(appName1, appVersion);
            Thread.sleep(5000);
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAndDeleteApplication", "TestPermission"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopAndDeleteApplication", "TestPermission"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
    }


    @Test(groups = "TestEventProcessOverwrite", dependsOnMethods = "testStopAndDeleteApplication", alwaysRun = true)
    public void testDeleteUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteUser", "TestPermission"));
            startSetup();
            handleID = esb.getHandleId();
            String permissionName1 = "PERMISSION TO VIEW RUNNING AND SAVED APPLICATIONS";
            ArrayList<String> userNames = new ArrayList();
            userNames.add(username);
            Object[] params1 = {permissionName1, userNames, handleID};
            String[] signatures1 = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            ObjectName objName1 = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName1, "removeUsersFromSystemPermission", params1, signatures1);

            permissionName1 = "PERMISSION TO CHANGE PROPERTIES OF AN APPLICATION";
            esb.invoke(objName1, "removeUsersFromSystemPermission", params1, signatures1);

            Object[] params = {username};
            String[] signatures = {String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.security:ServiceType=RealmManager,Name=SecuritySubSystem");
            esb.invoke(objName, "deleteUser", params, signatures);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteUser", "TestPermission"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteUser", "TestPermission"), e);
            //Assert.fail("failed to delete NEWUSER", e);
        } finally {
            esb.cleanUp();
        }
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
    private void changeRouteTransformation(String zipName) throws IOException, ServiceException {
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
            eventProcessManager.changeRouteTransformation("CHAT",appVersion, "route1", "org.apache.xalan.processor.TransformerFactoryImpl", result, completed, "script.xml", null, "project");
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestPermission"), e);
           // Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestPermission"), e);
           // Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

        Receiver rec = new Receiver();
        rec.setDestinationName(appName1 + "__" + "1_0" + "__" + "CHAT2" + "__IN_PORT");
        rec.setCf(appName1 +  "__" + "1_0" + "__" + "CHAT2");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestPermission"), e);
            //Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestPermission"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        Assert.assertEquals(messageSent, messageOnDestination);
    }

}


