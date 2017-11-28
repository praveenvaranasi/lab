package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.security.FioranoSecurityManager;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.rtl.server.FioranoServiceProviderFactory;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import fiorano.tifosi.common.TifosiException;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 2/15/11
 * Time: 10:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestChangePasswdSecurity19625 {
    private FioranoSecurityManager fioranoSecurityManager;
    private ServerStatusController serverstatus;
    private IRmiManager rmiManager;
    private JMXClient esb = null;
    private ObjectName objName;
    private String handleID;

    @Test(groups = "TestChangePasswdSecurity19625", alwaysRun = true)
    public void startSetUp() throws RemoteException {
        serverstatus = ServerStatusController.getInstance();
        try {
            fioranoSecurityManager = serverstatus.getServiceProvider().getSecurityManager();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestChangePasswdSecurity19625"), e);
           // Assert.fail("Couldnt retrieve fioranosecuritymanager", e);
        }
        rmiManager = RMIClient.getInstance().getRmiManager();
        if (fioranoSecurityManager == null && rmiManager == null) {
            //Assert.fail("Cannot run, as fioranosecuritymanager and rmiManger is not set.");
        }
    }

    @Test(groups = "TestChangePasswdSecurity19625", description = "bug19625:FioranoSecurityManager.changePassword" +
            "(String,String,String) API doesn't work properly", dependsOnMethods = "startSetUp", alwaysRun = true)
    public void testChangePasswdSecurity() {
        //Logger.LogMethodOrder(Logger.getOrderMessage("testChangePasswdSecurity", "TestChangePasswdSecurity19625"));
        try {
            if (!(fioranoSecurityManager.authenticateUser("user1", "user1")))
                fioranoSecurityManager.createUser("user1", "user1");
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangePasswdSecurity", "TestChangePasswdSecurity19625"), e);
           // Assert.fail("Failed create the new user", e);
        }
        try {
            esb = JMXClient.getInstance();
            objName = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            handleID = esb.getHandleId();
            ArrayList<String> userNames = new ArrayList<String>();
            userNames.add("user1");
            Object[] params = {"PERMISSION TO CREATE OR EDIT AND DELETE A PRINCIPAL", userNames, handleID};
            String[] signatures = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            esb.invoke(objName, "addUsersToSystemPermission", params, signatures);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangePasswdSecurity", "TestChangePasswdSecurity19625"), e);
           // Assert.fail("Failed assign permission create or edit and delete a principal to user1", e);
        }

        //logout as admin and login in as user1
        try {
            userName = "user1";
            password = "user1";
            login();
            handleID = fioranoServiceProvider.getHandleID();
            Thread.sleep(2000);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangePasswdSecurity", "TestChangePasswdSecurity19625"), e);
           // Assert.fail("Failed to logout as admin", e);
        }
        try {
            fioranoSecurityManager.changePassword("user1", "aaaa", "bbbb");
        } catch (TifosiException e) {
            if (!(e.getErrorCode().equals("INVALID_PASSWORD"))) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangePasswdSecurity", "TestChangePasswdSecurity19625"));
                //Assert.fail("Failed as user1 password is changed ... which should not to be happened");
            }
        }

        //logout as user1 and login as admin
        try {
            userName = "admin";
            password = "passwd";
            login();
            handleID = fioranoServiceProvider.getHandleID();
            Thread.sleep(2000);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangePasswdSecurity", "TestChangePasswdSecurity19625"), e);
           // Assert.fail("Failed to logout as admin", e);
        }
        try {
            fioranoSecurityManager.changePassword("user1", "aaaa", "aaaa");
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangePasswdSecurity", "TestChangePasswdSecurity19625"));
            //Assert.fail("Failed as user1 password is not changed ... which to be happened successfully");
        }

        //remove the permission principal and user1
        try {
            ArrayList<String> userNames = new ArrayList<String>();
            userNames.add("USER1");
            Object[] params = {"PERMISSION TO CREATE OR EDIT AND DELETE A PRINCIPAL", userNames, handleID};
            String[] signatures = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            boolean removestatus = (Boolean) esb.invoke(objName, "removeUsersFromSystemPermission", params, signatures);
            fioranoSecurityManager.deleteUser("user1");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangePasswdSecurity", "TestChangePasswdSecurity19625"), e);
           // Assert.fail("Failed remove permission create or edit and delete a principal for user1", e);
        }
       // Logger.Log(Level.SEVERE, Logger.getFinishMessage("testChangePasswdSecurity", "TestChangePasswdSecurity19625"));

    }

    FioranoServiceProvider fioranoServiceProvider;
    public String userName;
    public String password;

    @Test(enabled = false)
    public void login() {
        String providerURL = "tsp_tcp://" + ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer() + ":1947";
        FioranoServiceProviderFactory fioranoFactory = setupEnvironment(providerURL);
        try {
            fioranoServiceProvider = fioranoFactory.createServiceProvider(userName, password);
        } catch (TifosiException tifosiException) {
            if (tifosiException.getErrorCode().equals("UNABLE_TO_CONNECT")) {
                try {
                    providerURL = "tsp_tcp://" + ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer() + ":1948";
                    fioranoFactory = setupEnvironment(providerURL);
                    fioranoServiceProvider = fioranoFactory.createServiceProvider(userName, password);
                } catch (TifosiException tifosiException1) {
                    tifosiException1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        fioranoSecurityManager = fioranoServiceProvider.getSecurityManager();
    }

    private String contextFactory = "fiorano.tifosi.jndi.InitialContextFactory";
    private String serviceProvider = "TifosiServiceProvider";

    @Test(enabled = false)
    private FioranoServiceProviderFactory setupEnvironment(String providerURL) {
        Hashtable env = new Hashtable();
        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.PROVIDER_URL, providerURL);
        env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);

        InitialContext ic = null;
        try {
            ic = new InitialContext(env);
        } catch (NamingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // Lookup for Provider Factory
        FioranoServiceProviderFactory fioranoFactory = null;
        try {
            fioranoFactory = (FioranoServiceProviderFactory) ic.lookup(serviceProvider);
        } catch (NamingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return fioranoFactory;
    }
}
