package com.fiorano.esb.testng.rmi.bugs;


import com.fiorano.esb.rtl.security.FioranoSecurityManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Random;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: malarvizhi
 * Date: 9/8/12
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestChangePasswordForNewUser21803 extends AbstractTestNG {

    private JMXClient esb;
    private FioranoSecurityManager fioranoSecurityManager;
    private String username = "BUG_21803_USER";
    private String password = username;
    private String newpassword = "fiorano";
    private String handleID;

    @Test(groups = "Bugs", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestPermission"));
        fioranoSecurityManager = rtlClient.getFioranoSecurityManager();
        esb = JMXClient.getInstance();
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestPermission"));
    }

    Enumeration userNameResult = null;
    ArrayList<String> userList = new ArrayList();

    @Test(groups = "Bugs", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testCheckUserAvailability() {
        try {
            userNameResult = fioranoSecurityManager.getUserNames();

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCheckUserAvailability", "TestPermission"), e);
        }
        while (userNameResult != null && userNameResult.hasMoreElements()) {
            String name = userNameResult.nextElement().toString();
            userList.add(name);
        }
        boolean test = true;
        while (test) {
            if (userList.contains(username)) {
                Random random = new Random();
                username = username + (random.nextInt(25) + 'A');
            } else
                test = false;
        }
        password = username;
    }

    @Test(groups = "Bugs", dependsOnMethods = "testCheckUserAvailability", alwaysRun = true)
    public void testCreateUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateUser", "TestPermission"));
            fioranoSecurityManager.createUser(username, password);
            System.out.println("Created New User :" + username + "\n");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateUser", "TestPermission"), e);
            Assert.fail("failed to create user", e);
        }

    }

    @Test(groups = "Bugs", dependsOnMethods = "testCreateUser", alwaysRun = true)
    public void loginasNewUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testLoginAsNewUser", "TestChangePasswordForNewUser21803"));
            rmiClient.cleanUp();
            rmiClient = RMIClient.getInstance(username, password);
            handleID = esb.getHandleId();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLoginAsNewUser", "TestChangePasswordForNewUser21803"));
            System.out.println("Logging in with New User" + "\n");
        } catch (Exception e) {
            // deploy Event has to fail
            Assert.assertTrue(true);
        }
    }

    @Test(groups = "Bugs", dependsOnMethods = "loginasNewUser", alwaysRun = true)
    public void testChangePasswordForNewUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testChangePasswordForNewUser", "TestChangePasswordForNewUser21803"));
            fioranoSecurityManager.changePassword(username, password, newpassword);
            System.out.println("Password Changed for New User" + "\n");
            System.out.println("Test Passed :: TestChangePasswordForNewUser21803" + "\n");

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateUser", "TestPermission"), e);
            Assert.fail("failed to change password", e);
        }
    }

    @Test(groups = "Bugs", dependsOnMethods = "testChangePasswordForNewUser", alwaysRun = true)
    public void testLoginAsAdmin() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testLoginAsAdmin", "TestPermission"));
            rmiClient.cleanUp();
            rmiClient = RMIClient.getInstance();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLoginAsAdmin", "TestPermission"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLoginAsAdmin", "TestPermission"), e);
            Assert.fail("failed to change password", e);
        }
    }

    @Test(groups = "Bugs", dependsOnMethods = "testLoginAsAdmin", alwaysRun = true)
    public void testDeleteUser() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteUser", "TestPermission"));
            fioranoSecurityManager.deleteUser(username);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteUser", "TestPermission"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteUser", "TestPermission"), e);
        } finally {
            esb.cleanUp();
        }
    }
}


