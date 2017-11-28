package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.jms.services.msg.def.FioranoTextMessage;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.MBeanException;
import javax.management.ObjectName;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: janardhan
 * Date: 9/20/11
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestRestrictedAccessUser_Reinject_20699 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "RESTRICTED_ACCESS_USER_REINJECT";
    private String username = "NEW_USER";
    private String password = username;
    private String handleID;
    private JMXClient esb;
    private float appVersion = 1.0f;
    @Test(groups = "RestrictedAccessUser_Reinject", alwaysRun = true)
    public void startSetup() {
        esb = JMXClient.getInstance();
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group RestrictedAccessUser_Reinject. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "RestrictedAccessUser_Reinject", description = "bug 20699: [SI#4366] Restricted access user is able to re-inject data in Dashboard ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfApplication() {


        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"));

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("restricted_access_user_reinject-1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        //clear map

    }

    @Test(groups = "RestrictedAccessUser_Reinject", dependsOnMethods = "TestLaunchOfApplication", alwaysRun = true)
    public void testCreateNewUser() {
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateUser", "TestRestrictedAccessUser_Reinject_20699"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateUser", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("failed to create user", e);
        }
    }

    @Test(groups = "RestrictedAccessUser_Reinject", dependsOnMethods = "testCreateNewUser", alwaysRun = true)
    public void testLoignAsNewUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testLoginAsNewUser", "TestRestrictedAccessUser_Reinject_20699"));
            esb.cleanUp();
            esb = JMXClient.getInstance(username, password, 2047);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLoginAsNewUser", "TestRestrictedAccessUser_Reinject_20699"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateUser", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("failed to login as new user", e);
        }
    }

    @Test(groups = "RestrictedAccessUser_Reinject", dependsOnMethods = "testLoignAsNewUser", alwaysRun = true)
    public void testDeleteSBWDocument() {
        try {

            handleID = esb.getHandleId(username, password);
            ArrayList<String> list = new ArrayList();
            list.add("");
            Object[] params = {handleID, list};
            String[] signatures = {String.class.getName(), ArrayList.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.Esb.Sbw:ServiceType=SBWManager,Name=SBWManager");
            esb.invoke(objName, "purgeDocumentsByName", params, signatures);
            Assert.fail("deleted sbw document succesfully with restricted user ");
        } catch (MBeanException e) {
            if (e.getTargetException().getMessage().contains("Permission is denied for user: NEW_USER"))
                Assert.assertTrue(true);
            else {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
                Assert.fail("Failed to delete SBW documents", e);
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to delete SBW documents", e);
        }

    }


    @Test(groups = "RestrictedAccessUser_Reinject", dependsOnMethods = "testDeleteSBWDocument", alwaysRun = true)
    public void testReinjectDocument() {

        try {
            FioranoTextMessage ftm = new FioranoTextMessage(true);
            Object[] params = {handleID, ftm, appName,Float.toString(appVersion), "", "", "", "anonymous", "anonymous"};
            String[] signatures = {String.class.getName(),FioranoTextMessage.class.getName(), String.class.getName(),String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            esb.invoke(objName, "reInjectDocument", params, signatures);
            Assert.fail("reinject document succesfully with restricted user ");
        } catch (MBeanException e) {
            if (e.getTargetException().getMessage().contains("permission denied for User: NEW_USER"))
                Assert.assertTrue(true);

            else {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
                Assert.fail("Failed to reinject documents", e);
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to reinject documents", e);
        }
    }

    @Test(groups = "RestrictedAccessUser_Reinject", dependsOnMethods = "testReinjectDocument", alwaysRun = true)
    public void testLoignAsAdmin() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testLoginAsAdmin", "TestRestrictedAccessUser_Reinject_20699"));
            esb.cleanUp();
            esb = JMXClient.getInstance();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLoginAsAdmin", "TestRestrictedAccessUser_Reinject_20699"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLoginAsAdmin", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail();
        }
    }

    @Test(groups = "RestrictedAccessUser_Reinject", dependsOnMethods = "testLoignAsAdmin", alwaysRun = true)
    public void testDeleteSBWDocumentAsAdmin() {
        try {

            handleID = esb.getHandleId();
            ArrayList<String> list = new ArrayList();
            list.add("");
            Object[] params = {handleID, list};
            String[] signatures = {String.class.getName(), ArrayList.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.Esb.Sbw:ServiceType=SBWManager,Name=SBWManager");
            esb.invoke(objName, "purgeDocumentsByName", params, signatures);
        } catch (MBeanException e) {
            if (!e.getTargetException().getMessage().contains("Permission is denied for user"))
                Assert.assertTrue(true);
            else {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
                Assert.fail("Failed to delete SBW documents", e);
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to delete SBW documents", e);
        }

    }


    @Test(groups = "RestrictedAccessUser_Reinject", dependsOnMethods = "testDeleteSBWDocumentAsAdmin", alwaysRun = true)
    public void testReinjectDocumentAsAdmin() {

        try {
            FioranoTextMessage ftm = new FioranoTextMessage(true);
            Object[] params = {handleID, ftm, appName,Float.toString(appVersion), "Display1", "", "", "anonymous", "anonymous"};
            String[] signatures = {String.class.getName(), FioranoTextMessage.class.getName(), String.class.getName(),String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            esb.invoke(objName, "reInjectDocument", params, signatures);
        } catch (MBeanException e) {
            if (!e.getTargetException().getMessage().contains("permission denied for User"))
                Assert.assertTrue(true);

            else {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
                Assert.fail("Failed to reinject documents", e);
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to reinject documents", e);
        }
    }


    @Test(groups = "RestrictedAccessUser_Reinject", dependsOnMethods = "testReinjectDocumentAsAdmin", alwaysRun = true)
    public void testDeleteNewUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteUser", "TestRestrictedAccessUser_Reinject_20699"));
            startSetup();
            handleID = esb.getHandleId();
            String permissionName1 = "PERMISSION TO VIEW RUNNING AND SAVED APPLICATIONS";
            ArrayList<String> userNames = new ArrayList();
            userNames.add(username);
            Object[] params1 = {permissionName1, userNames, handleID};
            String[] signatures1 = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            ObjectName objName1 = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName1, "removeUsersFromSystemPermission", params1, signatures1);

            Object[] params = {username};
            String[] signatures = {String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.security:ServiceType=RealmManager,Name=SecuritySubSystem");
            esb.invoke(objName, "deleteUser", params, signatures);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteNewUser", "TestRestrictedAccessUser_Reinject_20699"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteNewUser", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("failed to delete NEW_USER", e);
        }
    }


    @Test(groups = "RestrictedAccessUser_Reinject", dependsOnMethods = "testDeleteNewUser", alwaysRun = true)
    public void stopAndDeleteApplication() {
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestRestrictedAccessUser_Reinject_20699"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
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
}
