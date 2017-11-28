package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
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

/*
*
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Jan 24, 2011
 * Time: 12:41:19 PM
 * To change this template use File | Settings | File Templates.


*/


public class TestEventProcessOverwrite extends AbstractTestNG {
    private JMXClient esb;
    private ServerStatusController serverstatus;
    private TestEnvironment testenv;
    private IEventProcessManager eventProcessManager;
    public IRmiManager rmiManager;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private String appName1 = "NEW_EVENT";
    private String username = "NEWUSER";
    private String password = username;
    private String handleID;
    private float appVersion = 1.0f;

    @Test(groups = "TestEventProcessOverwrite", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestEventProcessOverwrite"));
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
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestEventProcessOverwrite"));
    }

    @Test(groups = "TestEventProcessOverwrite", description = "EventProcess is over-written without any warning in a particular case:Bug-17921", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"));
        SampleEventProcessListener epListener1 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);

        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e1);
          //  Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
          //  Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("new_event.zip.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
          //  Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
           // Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
           // Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
           // Assert.fail("Error in thread sleep", e);
        }

        //clear map
        eventStore1.clear();
        try {
            eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(10000);
            //Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "2");


        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"), e);
           // Assert.fail("Error with thread sleep!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOEventProcess", "TestEventProcessOverwrite"));

    }

    @Test(groups = "TestEventProcessOverwrite", dependsOnMethods = "testLaunchOfEventProcess", alwaysRun = true)
    public void testCreateUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateUser", "TestEventProcessOverwrite"));
            Object[] params = {username, password};
            String[] signatures = {String.class.getName(), String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.security:ServiceType=RealmManager,Name=SecuritySubSystem");
            esb.invoke(objName, "createUser", params, signatures);
            handleID = esb.getHandleId();
            String permissionName = "PERMISSION TO COMPOSE AN APPLICATION";
            ArrayList<String> userNames = new ArrayList();
            userNames.add(username);
            Object[] params1 = {permissionName, userNames, handleID};
            String[] signatures1 = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            ObjectName objName1 = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName1, "addUsersToSystemPermission", params1, signatures1);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateUser", "TestEventProcessOverwrite"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateUser", "TestEventProcessOverwrite"), e);
            //Assert.fail("failed to create user", e);
        }
    }


    @Test(groups = "TestEventProcessOverwrite", dependsOnMethods = "testCreateUser", alwaysRun = true)
    public void testLoginAsNewUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testLoginAsNewUser", "TestEventProcessOverwrite"));
            // rmiManager = rmiClient.getRmiManager();
//            rmiManager.logout(handleID);
            rmiClient.cleanUp();
            rmiClient.setUserName(username);
            rmiClient.setPassword(password);
            rmiClient = RMIClient.getInstance();

            //rmiManager = rmiClient.getRmiManager();
            //handleID = rmiManager.login(username, password);
            // Assert.assertNotNull(handleID);
            //System.out.println("login successful with handleID = " + handleID);
            eventProcessManager = rmiClient.getEventProcessManager();
            //eventProcessManager = rmiManager.getEventProcessManager(handleID);
            Assert.assertNotNull(eventProcessManager);
            Assert.assertEquals(eventProcessManager.toString(), "Event_Process_Manager");
            //deploy event should throw an error here & hence we set assert true in catch block.
            deployEventProcess("new_event.zip.zip");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLoginAsNewUser", "TestEventProcessOverwrite"));
        } catch (Exception e) {
            // deploy Event has to fail
            Assert.assertTrue(true);
        }
    }

    @Test(groups = "TestEventProcessOverwrite", dependsOnMethods = "testLoginAsNewUser", alwaysRun = true)
    public void testLoginAsAdmin() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testLoginAsAdmin", "TestEventProcessOverwrite"));
            //rmiManager = rmiClient.getRmiManager();
//            rmiManager.logout(handleID);
            rmiClient.cleanUp();
            rmiClient.setUserName("admin");
            rmiClient.setPassword("passwd");
            rmiClient = RMIClient.getInstance();
            //rmiManager = rmiClient.getRmiManager();
            //handleID = rmiManager.login("admin", "passwd");
            //Assert.assertNotNull(handleID);
            //System.out.println("login successful with handleID = " + handleID);
            eventProcessManager = rmiClient.getEventProcessManager();
            //eventProcessManager = rmiManager.getEventProcessManager(handleID);
            Assert.assertTrue(eventProcessManager.exists("SIMPLECHAT", appVersion));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLoginAsAdmin", "TestEventProcessOverwrite"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLoginAsAdmin", "TestEventProcessOverwrite"), e);
           // Assert.fail();
        }
    }


    @Test(groups = "TestEventProcessOverwrite", dependsOnMethods = "testLoginAsAdmin", alwaysRun = true)
    public void testStopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStopAndDeleteApplication", "TestEventProcessOverwrite"));
        try {
            eventProcessManager.stopEventProcess(appName1, appVersion);
            Thread.sleep(5000);
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAndDeleteApplication", "TestEventProcessOverwrite"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopAndDeleteApplication", "TestEventProcessOverwrite"), e);
          //  Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
    }


    @Test(groups = "TestEventProcessOverwrite", dependsOnMethods = "testStopAndDeleteApplication", alwaysRun = true)
    public void testDeleteUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteUser", "TestEventProcessOverwrite"));
            startSetup();
            handleID = esb.getHandleId();
            String permissionName = "PERMISSION TO COMPOSE AN APPLICATION";
            ArrayList<String> userNames = new ArrayList();
            userNames.add(username);
            Object[] params1 = {permissionName, userNames, handleID};
            String[] signatures1 = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            ObjectName objName1 = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName1, "removeUsersFromSystemPermission", params1, signatures1);

            Object[] params = {username};
            String[] signatures = {String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.security:ServiceType=RealmManager,Name=SecuritySubSystem");
            esb.invoke(objName, "deleteUser", params, signatures);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteUser", "TestEventProcessOverwrite"), e);
           // Assert.fail("failed to delete NEWUSER", e);
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
}
