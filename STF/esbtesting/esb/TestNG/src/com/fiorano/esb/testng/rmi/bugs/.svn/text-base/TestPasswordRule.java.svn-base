package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.security.FioranoSecurityManager;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.MachineElement;
import com.fiorano.stf.test.core.ProfileElement;
import com.fiorano.stf.test.core.ServerElement;
import com.fiorano.stf.test.core.TestEnvironment;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.rule.passwd.impl.IValidationRule;
import fiorano.tifosi.dmi.rule.passwd.impl.LengthRule;
import fiorano.tifosi.dmi.rule.passwd.impl.LengthValidationRule;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Jan 18, 2011
 * Time: 7:14:37 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestPasswordRule extends AbstractTestNG {
    private JMXClient esb;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private FioranoSecurityManager fsm;
    private String handleID;
    private String user = "user";
    private String password1 = "user";
    private String password2 = "user1234";
    private IValidationRule rule1;
    private IEventProcessManager eventProcessManager;


    @Test(groups = "PasswordRule", description = "Password Rule Criteria created on Dashboard need not satisy while working with Studio:19845", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestPasswordRule"));
        serverstatus = ServerStatusController.getInstance();
        try {
            fsm = serverstatus.getServiceProvider().getSecurityManager();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestPasswordRule"), e);
            Assert.fail("Couldnt retrieve fioranosecuritymanager", e);
        }
        testenv = serverstatus.getTestEnvironment();
        ServerElement se = testenv.getServer("fes");
        Map<String, ProfileElement> profileElements = se.getProfileElements();
        String str = new String("standalone");
        ProfileElement pm = profileElements.get(str);
        String machine = pm.getMachineName();
        MachineElement machine1 = testenv.getMachine(machine);
        esb = JMXClient.getInstance();
        esb.connect("admin", "passwd", machine1.getAddress(), 2047);
        eventProcessManager = rmiClient.getEventProcessManager();
        //cleanup
        stopAllEPs(eventProcessManager);
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestPasswordRule"));
    }


    @Test(groups = "PasswordRule", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testAddPasswordRule() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testAddPasswordRule", "TestPasswordRule"));
        handleID = esb.getHandleId();
        LengthRule rule = new LengthValidationRule();
        rule.setRuleType(LengthRule.LengthRuleType.GREATER);
        rule.setRuleDesc("Length of password should be greater than 7");
        rule.setLength(7);
        rule1 = (IValidationRule) rule;

        try {

            Object[] params = {rule1, handleID};
            String[] signatures = {IValidationRule.class.getName(), String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName, "addPasswordValidationRule", params, signatures);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddPasswordRule", "TestPasswordRule"), e);
            Assert.fail("testAddGroupfailed ", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testAddPasswordRule", "TestPasswordRule"));
    }


    @Test(groups = "PasswordRule", dependsOnMethods = "testAddPasswordRule", alwaysRun = true)
    public void testAddUser() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testAddUser", "TestPasswordRule"));
        try {
            fsm.createUser(user, password1);

        } catch (TifosiException e) {
            Assert.assertTrue(e.getStackTraceAsString().contains("Invalid password. Reason : String length should be greater than 7"), "user cannot be added as password length is lesser than 7");
        }

        try {
            fsm.createUser(user, password2);
            Assert.assertTrue(true, "user added as password length is greater than 7");
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddUser", "TestPasswordRule"), e);
            Assert.fail("Unable to create a user, even when  password length is greater than 7", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testAddUser", "TestPasswordRule"));
    }

    @Test(groups = "PasswordRule", dependsOnMethods = "testAddUser", alwaysRun = true)
    public void testCleanUp() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testCleanUp", "TestPasswordRule"));
        try {
            fsm.deleteUser(user);
            Object[] params = {rule1, handleID};
            String[] signatures = {IValidationRule.class.getName(), String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName, "removePasswordValidationRule", params, signatures);
        }
        catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanUp", "TestPasswordRule"), e);
            Assert.fail("testCleanUp failed", e);
        }
        finally {
            esb.cleanUp();
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanUp", "TestPasswordRule"));
    }

}
