package com.fiorano.esb.testng.rmi.rmi;

import com.fiorano.esb.application.controller.ApplicationControllerErrorCodes;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.server.api.SystemInfoReference;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.stf.test.core.TestElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/10/11
 * Time: 6:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceProviderManager extends AbstractTestNG{

    private IServiceProviderManager SPManager;
    private IRmiManager rmiManager;
    private String resourceFilePath;
    private String m_ComponentGUID;
    private float m_version;
    private String m_serviceDir;
    private String rmiHandleID;
    private TestElement testCaseConfig;

    @BeforeClass(groups = "ServiceProviderManagerTest", alwaysRun = true)
    public void startSetUp(){

        try {
            rmiManager = rmiClient.getRmiManager();
            rmiHandleID = rmiManager.login("admin", "passwd");
            SPManager = rmiManager.getServiceProviderManager(rmiHandleID);
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "ServiceProviderManager";
            System.out.println("Started the Execution of the TestCase::" + getName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test(groups = "ServiceProviderManagerTest", alwaysRun = true)
    public void testGetLastOutLogs() {
        try {

            Assert.assertNotNull(SPManager.getLastErrLogs(50));
        } catch (Exception e) {
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT) != -1;
            if (!check) {
                System.err.println("Exception in the Execution of test case::" + getName());
                e.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            }
        }
    }

    @Test(groups = "ServiceProviderManagerTest", alwaysRun = true, dependsOnMethods = "testGetLastOutLogs")
    public void testGetLastErrLogs() {
        try {

            Assert.assertNotNull(SPManager.getLastErrLogs(50));

        } catch (Exception e) {
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT) != -1;
            if (!check) {
                System.err.println("Exception in the Execution of test case::" + getName());
                e.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            }
        }
    }

    @Test(groups = "ServiceProviderManagerTest", alwaysRun = true, dependsOnMethods = "testGetLastErrLogs")
    public void testGetServerName() {
        try {
            Assert.assertEquals(SPManager.getServerName(), "fes");
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceProviderManagerTest", alwaysRun = true, dependsOnMethods = "testGetServerName")
    public void testGetServerDetails() {
        try {
            HashMap deets = SPManager.getServerDetails();
            Assert.assertEquals(deets.get("Name"), SPManager.getServerName());
            deets.get("Server URL");
            deets.get("Backup URL");
            deets.get("ProcessCount");
            deets.get("ThreadCount");
            Assert.assertTrue(((String) deets.get("OS")).toLowerCase().contains(System.getProperty("os.name").toLowerCase()));
        } catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceProviderManagerTest", alwaysRun = true, dependsOnMethods = "testGetServerDetails")
    public void testShutdownServer() {
    }

    @Test(groups = "ServiceProviderManagerTest", alwaysRun = true, dependsOnMethods = "testShutdownServer")
    public void testGetServerSystemInfo() {
        try {
            SystemInfoReference info = SPManager.getServerSystemInfo();
            Assert.assertEquals(info.getOpSysName(), System.getProperty("os.name"));
        }
        catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
        catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceProviderManagerTest", alwaysRun = true, dependsOnMethods = "testGetServerSystemInfo")
    public void testClearFESOutLogs() {
        try {
            SPManager.clearFESOutLogs();
            Assert.assertTrue(SPManager.getLastOutLogs(50).length() == 0);
        }
        catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
        catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceProviderManagerTest", alwaysRun = true, dependsOnMethods = "testClearFESOutLogs")
    public void testClearFESErrLogs() {
        try {
            SPManager.clearFESErrLogs();
            Assert.assertTrue(SPManager.getLastErrLogs(50).length() == 0);
        }
        catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
        catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

}
