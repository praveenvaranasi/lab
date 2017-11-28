package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.MachineElement;
import com.fiorano.stf.test.core.ProfileElement;
import com.fiorano.stf.test.core.TestEnvironment;
import fiorano.jms.runtime.admin.MQAdminConnection;
import fiorano.jms.runtime.admin.MQAdminConnectionFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.rmi.RemoteException;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Jan 11, 2011
 * Time: 11:29:56 AM
 * To change this template use File | Settings | File Templates.
 */


public class TestRemoveRemoteAccess extends AbstractTestNG {
    private JMXClient esb;
    private MQAdminConnection ac;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "SIMPLECHAT";
    private float appVersion = 1.0f;

    @Test(groups = "RemoveRemoteAccess", description = "Permission to remote access not working properly bug :20131", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestRemoveRemoteAccess"));
        eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group RemoteServiceInstance as eventProcessManager is not set.");
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

        Hashtable env = new Hashtable();

        env.put(Context.SECURITY_PRINCIPAL, "admin");
        env.put(Context.SECURITY_CREDENTIALS, "passwd");
        env.put(Context.PROVIDER_URL, "http://" + machine1.getAddress() + ":1867");
        env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.jms.runtime.naming.FioranoInitialContextFactory");
        env.put("BackupConnectURLs", "");

        try {
            InitialContext ic = new InitialContext(env);
            MQAdminConnectionFactory acf = null;
            acf = (MQAdminConnectionFactory) ic.lookup("primaryACF");
            ac = acf.createMQAdminConnection("admin", "passwd");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetUp", "TestRemoveRemoteAccess"), e);
            Assert.fail("startSetUp failed ", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestRemoveRemoteAccess"));

    }

    @Test(groups = "RemoveRemoteAccess", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testAddGroup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testAddGroup", "TestRemoveRemoteAccess"));
        String handleID = esb.getHandleId();
        String permissionName = "PERMISSION TO REMOTELY ADMINISTRATE AN APPLICATION";
        ArrayList<String> userNames = new ArrayList();
        userNames.add("EVERYONE");
        userNames.add("AYRTON");
        try {

            Object[] params = {permissionName, userNames, handleID};
            String[] signatures = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName, "addUsersToSystemPermission", params, signatures);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddGroup", "TestRemoveRemoteAccess"), e);
            Assert.fail("testAddGroupfailed ", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testAddGroup", "TestRemoveRemoteAccess"));
    }


    @Test(groups = "RemoveRemoteAccess", dependsOnMethods = "testAddGroup", alwaysRun = true)
    public void testLaunchOfSimpleChat() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOfSimpleChat", "TestRemoveRemoteAccess"));
        SampleEventProcessListener epListener = null;
        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestRemoveRemoteAccess"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestRemoveRemoteAccess"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestRemoveRemoteAccess"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestRemoveRemoteAccess"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestRemoveRemoteAccess"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestRemoveRemoteAccess"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestRemoveRemoteAccess"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        //clear map

        Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOfSimpleChat", "TestRemoveRemoteAccess"));

    }

    @Test(groups = "RemoveRemoteAccess", dependsOnMethods = "testLaunchOfSimpleChat", alwaysRun = true)
    public void testACL() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testACL", "TestRemoveRemoteAccess"));
        boolean everyone = false;
        boolean ayrton = false;
        try {
            Acl acl = ac.getMQRealmService().getAcl("SIMPLECHAT__1_0__CHAT1__OUT_PORT");
            Enumeration _enum = acl.entries();
            while (_enum.hasMoreElements()) {
                AclEntry entry = (AclEntry) _enum.nextElement();
                if (entry.getPrincipal().getName().equals("EVERYONE"))
                    everyone = true;
                if (entry.getPrincipal().getName().equals("AYRTON"))
                    ayrton = true;
            }
            Assert.assertTrue(everyone && ayrton, "EVERYONE & AYRTON are added & present");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestACL", "TestRemoveRemoteAccess"), e);
            Assert.fail("testACL failed ", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testACL", "TestRemoveRemoteAccess"));

    }

    @Test(groups = "RemoveRemoteAccess", dependsOnMethods = "testACL", alwaysRun = true)
    public void testStopEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStopEventProcess", "TestRemoveRemoteAccess"));
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopEventProcess", "TestRemoveRemoteAccess"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopEventProcess", "TestRemoveRemoteAccess"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testStopEventProcess", "TestRemoveRemoteAccess"));
    }

    @Test(groups = "RemoveRemoteAccess", dependsOnMethods = "testStopEventProcess", alwaysRun = true)
    public void testRemoveGroup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveGroup", "TestRemoveRemoteAccess"));
        String handleID = esb.getHandleId();
        String permissionName = "PERMISSION TO REMOTELY ADMINISTRATE AN APPLICATION";
        ArrayList<String> userNames = new ArrayList();
        userNames.add("EVERYONE");
        userNames.add("AYRTON");
        try {
            Object[] params = {permissionName, userNames, handleID};
            String[] signatures = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName, "removeUsersFromSystemPermission", params, signatures);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRemoveGroup", "TestRemoveRemoteAccess"), e);
            Assert.fail("testRemoveGroup failed ", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveGroup", "TestRemoveRemoteAccess"));
    }

    @Test(groups = "RemoveRemoteAccess", dependsOnMethods = "testRemoveGroup", alwaysRun = true)
    public void testNewACL() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testNewACL", "TestRemoveRemoteAccess"));

        testLaunchOfSimpleChat();
        try {
            Acl acl = ac.getMQRealmService().getAcl("SIMPLECHAT__1_0__CHAT1__OUT_PORT");
            Enumeration _enum = acl.entries();
            while (_enum.hasMoreElements()) {
                AclEntry entry = (AclEntry) _enum.nextElement();
                if ((entry.getPrincipal().getName().equals("EVERYONE")) || (entry.getPrincipal().getName().equals("AYRTON")))
                    Assert.fail("Everyone or AYrton member still present in ACL");
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewACL", "TestRemoveRemoteAccess"), e);
            Assert.fail("testNewAclFailed", e);
        }
        testStopEventProcess();
        Logger.Log(Level.INFO, Logger.getFinishMessage("testNewACL", "TestRemoveRemoteAccess"));

    }

    @Test(groups = "RemoveRemoteAccess", dependsOnMethods = "testNewACL", alwaysRun = true)
    public void testResetChanges() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testResetChanges", "TestRemoveRemoteAccess"));
        boolean everyone = false;
        String handleID = esb.getHandleId();
        String permissionName = "PERMISSION TO REMOTELY ADMINISTRATE AN APPLICATION";
        ArrayList<String> userNames = new ArrayList();
        userNames.add("EVERYONE");
        try {

            Object[] params = {permissionName, userNames, handleID};
            String[] signatures = {String.class.getName(), ArrayList.class.getName(), String.class.getName()};
            ObjectName objName = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");
            esb.invoke(objName, "addUsersToSystemPermission", params, signatures);
            testLaunchOfSimpleChat();
            Acl acl = ac.getMQRealmService().getAcl("SIMPLECHAT__1_0__CHAT1__OUT_PORT");
            Enumeration _enum = acl.entries();
            while (_enum.hasMoreElements()) {
                AclEntry entry = (AclEntry) _enum.nextElement();
                if (entry.getPrincipal().getName().equals("EVERYONE"))
                    everyone = true;
            }
            Assert.assertTrue(true, "EVERYONE group is present");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResetChanges", "TestRemoveRemoteAccess"), e);
            Assert.fail("testResetChanges failed ", e);
        } finally {
            testStopEventProcess();
            esb.cleanUp();
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testResetChanges", "TestRemoveRemoteAccess"));
    }
}
