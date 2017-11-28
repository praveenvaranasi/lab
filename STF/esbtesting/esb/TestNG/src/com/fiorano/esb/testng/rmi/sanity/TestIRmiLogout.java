package com.fiorano.esb.testng.rmi.sanity;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import com.fiorano.stf.test.core.TestNGTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Mar 10, 2010
 * Time: 5:17:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Test(dependsOnGroups = "TWO_IServiceProviderManager.*")
public class TestIRmiLogout extends AbstractTestNG {
    public IRmiManager rmiManager;
    public IServiceProviderManager serviceProviderManager;
    public IFPSManager fpsManager;
    public IEventProcessManager eventProcessManager;

    String handleID;

    @BeforeClass
    @Test(groups = "IRmiLogout", alwaysRun = true)
    public void start_IRmiLogout() {
        Logger.LogMethodOrder(Logger.getOrderMessage("start_IRmiLogout", "TestIRmiLogout"));
        handleID = TestIRmiManager.getHandleID();
        rmiManager = TestIRmiManager.getrmiManager();
        Assert.assertNotNull(rmiManager);
        Assert.assertNotNull(handleID);
        Logger.Log(Level.INFO, Logger.getFinishMessage("start_IRmiLogout", "TestIRmiLogout"));

    }

    @Test(groups = {"IRmiLogout"}, dependsOnMethods = {"start_IRmiLogout"}, alwaysRun = true)
    public void Testlogout() {
        try {
            //logs out the rmiManager
            Logger.LogMethodOrder(Logger.getOrderMessage("Testlogout", "TestIRmiLogout"));
            Assert.assertNotNull(rmiManager);
            Assert.assertNotNull(handleID);
            // will logout successfully if correct string is passed ; else gives an error;
            rmiManager.logout(handleID);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                //trying to fetch EventProcessManager after logout. It should show an exception
                rmiManager.getEventProcessManager(handleID);

            } catch (ServiceException e) {
                //Ignore
                Logger.Log(Level.INFO, Logger.getFinishMessage("Testlogout", "TestIRmiLogout"));

            } catch (RemoteException e) {
                e.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogout", "TestIRmiLogout"), e);
                Assert.fail();
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogout", "TestIRmiLogout"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogout", "TestIRmiLogout"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IRmiLogout"}, dependsOnMethods = "Testlogout", alwaysRun = true)
    public void Testlogin_case2() {
        try {
            // Testing two times consecutive login
            Logger.LogMethodOrder(Logger.getOrderMessage("Testlogin_case2", "TestIRmiLogout"));

            String tempID = rmiManager.login("admin", "passwd");
            handleID = rmiManager.login("admin", "passwd");
            Assert.assertNotSame(tempID, handleID);

            fpsManager = rmiManager.getFPSManager(handleID);
            serviceProviderManager = rmiManager.getServiceProviderManager(handleID);
            eventProcessManager = rmiManager.getEventProcessManager(handleID);

            Assert.assertNotNull(fpsManager);
            Assert.assertNotNull(serviceProviderManager);

            Logger.Log(Level.INFO, Logger.getFinishMessage("Testlogin_case2", "TestIRmiLogout"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogin_case2", "TestIRmiLogout"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogin_case2", "TestIRmiLogout"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IRmiLogout", dependsOnMethods = "Testlogin_case2", alwaysRun = true)
    public void CreateBackupLogs() {
        try {
            FileWriter writer;
            String logmessage;

            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            File TestingHome = new File(home);
            String reportsDirectory = TestingHome.getAbsolutePath() + File.separator + TestEnvironmentConstants.REPORTS_DIR
                    + File.separator + TestNGTestCase.testNG_ReportsDir;
            String serverLogs = reportsDirectory + File.separator + "ServerLogs";
            new File(serverLogs).mkdir();
            // creating a backup file for peer error logs
            logmessage = fpsManager.getLastPeerErrLogs("fps", -1);
            File log = new File(serverLogs + File.separator + "FpsErrorLogs.log");
            log.createNewFile();
            writer = new FileWriter(log);
            writer.write(logmessage);
            writer.close();

            //creating a backup file for peer out logs
            logmessage = fpsManager.getLastPeerOutLogs("fps", -1);
            log = new File(serverLogs + File.separator + "FpsOutLogs.log");
            log.createNewFile();
            writer = new FileWriter(log);
            writer.write(logmessage);
            writer.close();

            // creating a backup file for fes error logs
            logmessage = serviceProviderManager.getLastErrLogs(-1);
            log = new File(serverLogs + File.separator + "FesErrorLogs.log");
            log.createNewFile();
            writer = new FileWriter(log);
            writer.write(logmessage);
            writer.close();

            // creating a backup file for the fes out logs
            logmessage = serviceProviderManager.getLastOutLogs(-1);
            log = new File(serverLogs + File.separator + "FesOutLogs.log");
            log.createNewFile();
            writer = new FileWriter(log);
            writer.write(logmessage);
            writer.close();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("CreateBackupLogs", "TestIRmiLogout"), e);
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("CreateBackupLogs", "TestIRmiLogout"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IRmiLogout"}, dependsOnMethods = {"CreateBackupLogs"}, alwaysRun = true)
    public void TestClearPeerErrLogs() {
        try {
            //clears peer error logs
            Logger.LogMethodOrder(Logger.getOrderMessage("TestClearPeerErrLogs", "TestIRmiLogout"));
            String templogs = fpsManager.getLastPeerErrLogs("fps", 7);
            if (templogs != null && templogs.length() > 0) {
                fpsManager.clearPeerErrLogs("fps");
                try {
                    Thread.sleep(Thread_Sleep_Time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Assert.fail();
                }
                Assert.assertTrue(fpsManager.getLastPeerErrLogs("fps", 7).length() == 0);
            }
            fpsManager.clearPeerErrLogs("fps");
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestClearPeerErrLogs", "IRmiLogout"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClearPeerErrLogs", "IRmiLogout"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClearPeerErrLogs", "IRmiLogout"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IRmiLogout"}, dependsOnMethods = {"TestClearPeerErrLogs"}, alwaysRun = true)
    public void TestClearPeerOutLogs() {
        try {
            // clears peer outlogs
            Logger.LogMethodOrder(Logger.getOrderMessage("TestClearPeerOutLogs", "TestIRmiLogout"));
            String templogs = fpsManager.getLastPeerOutLogs("fps", 10000);
            int a, b;
            a = templogs.length();
            if (templogs != null && templogs.length() > 0) {
                fpsManager.clearPeerOutLogs("fps");

            }

            b = fpsManager.getLastPeerOutLogs("fps", 10000).length();
            if (b > a) {
                throw new Exception();
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestClearPeerOutLogs", "IRmiLogout"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClearPeerOutLogs", "IRmiLogout"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClearPeerOutLogs", "IRmiLogout"), e);
            Assert.fail();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClearPeerOutLogs", "IRmiLogout"), e);
            Assert.fail();
        }

    }

    @Test(groups = "IRmiLogout", dependsOnMethods = {"TestClearPeerOutLogs"}, alwaysRun = true)
    public void TestClearFESErrLogs() {
        try {
            // clears FES error logs
            Logger.LogMethodOrder(Logger.getOrderMessage("TestClearFESErrLogs", "TestIRmiLogout"));
            String logs1 = serviceProviderManager.getLastErrLogs(7);
            if (logs1 != null && logs1.length() > 0) {
                serviceProviderManager.clearFESErrLogs();
                Assert.assertTrue(serviceProviderManager.getLastErrLogs(7).length() == 0);
            }
            serviceProviderManager.clearFESErrLogs();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestClearFESErrLogs", "IRmiLogout"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClearFESErrLogs", "IRmiLogout"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClearFESErrLogs", "IRmiLogout"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IRmiLogout", dependsOnMethods = {"TestClearFESErrLogs"}, alwaysRun = true)
    public void TestClearFESOutLogs() {
        try {
            // clears FES OutLogs
            Logger.LogMethodOrder(Logger.getOrderMessage("TestClearFESOutLogs", "TestIRmiLogout"));
            String logs2 = serviceProviderManager.getLastOutLogs(7);
            if (logs2 != null && logs2.length() > 0) {
                serviceProviderManager.clearFESOutLogs();
                Assert.assertTrue(serviceProviderManager.getLastOutLogs(7).length() == 0);
            }
            serviceProviderManager.clearFESOutLogs();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestClearFESOutLogs", "IRmiLogout"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClearFESOutLogs", "IRmiLogout"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClearFESOutLogs", "IRmiLogout"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IRmiLogout"}, dependsOnMethods = {"TestClearFESOutLogs"}, alwaysRun = true)
    public void Testlogout_case2() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("Testlogout_case2", "TestIRmiLogout"));
            // since logout method is excuted before this method. So we are doing a login
            handleID = rmiManager.login("admin", "passwd");
            //testing twice logout for checking NPE
            rmiManager.logout(handleID);
            System.out.println("Logout successfully 1st time");
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rmiManager.logout(handleID);
            System.out.println("Logout successfully twice without NPE");
            Logger.Log(Level.INFO, Logger.getErrMessage("Testlogout_case2", "TestIRmiLogout"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogout_case2", "TestIRmiLogout"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogout_case2", "TestIRmiLogout"), e);
            Assert.fail();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogout_case2", "TestIRmiLogout"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IRmiLogout", dependsOnMethods = "Testlogout_case2", alwaysRun = true)
    public void TestshutdownPeer() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("TestshutdownPeer", "TestIRmiLogout"));
            handleID = rmiManager.login("admin", "passwd");
            fpsManager = rmiManager.getFPSManager(handleID);
            //shuts down the peer
            fpsManager.shutdownPeer("fps");
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.Log(Level.INFO, Logger.getErrMessage("TestshutdownPeer", "TestIRmiLogout"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestshutdownPeer", "TestIRmiLogout"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestshutdownPeer", "TestIRmiLogout"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IRmiLogout", dependsOnMethods = "TestshutdownPeer", alwaysRun = true)
    public void TestshutdownServer() {
        try {
            // shuts down the Server
            Logger.LogMethodOrder(Logger.getOrderMessage("TestshutdownServer", "TestIRmiLogout"));
            handleID = rmiManager.login("admin", "passwd");
            serviceProviderManager = rmiManager.getServiceProviderManager(handleID);
            serviceProviderManager.shutdownServer();
            Logger.Log(Level.INFO, Logger.getErrMessage("TestshutdownServer", "TestIRmiLogout"));
        } catch (RemoteException e) {
            //Ignore
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestshutdownServer", "TestIRmiLogout"), e);
            Assert.fail();
        }
    }
}
