package com.fiorano.esb.testng.rmi.sanity;

import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.IServerStateListener;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.server.api.SystemInfoReference;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.bugs.TestConstants;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import com.fiorano.stf.test.core.TestNGTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Feb 23, 2010
 * Time: 3:10:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Test(dependsOnGroups = "TWO_IDebugger.*")
public class TestIFPSManager extends AbstractTestNG {
    public IFPSManager fpsManager;
    public static Hashtable<String, String> resultMap = new Hashtable<String, String>();

    @Test(groups = {"IFPSManager"}, alwaysRun = true)
    public void start_fpsManager() {
        Logger.LogMethodOrder(Logger.getOrderMessage("start_fpsManager", "TestIFPSManager"));
        fpsManager = TestIRmiManager.getfpsManager();
        Logger.Log(Level.INFO, Logger.getFinishMessage("start_fpsManager", "final_fpsManagerManager"));
    }


    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestaddListener() {
        try {
            // adds the ServerStatelistener to the fpsmanager
            Logger.LogMethodOrder(Logger.getOrderMessage("TestaddListener", "TestIFPSManager"));
            IServerStateListener issl = new TestIServerStateListener();
            resultMap.clear();
            fpsManager.addListener(issl);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            fpsManager.restartPeer("fps");
            try {
                Thread.sleep(Thread_Sleep_Time);
                //adding sleep for dealing with delayed fps restart on solaris
                Thread.sleep(50000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (resultMap.get(TestConstants.PEER_STATUS_UNAVALIABILITY).equals("UNAVAILABLE") && resultMap.get(TestConstants.PEER_STATUS_AVALIABILITY).equals("AVAILABLE")) {
                // testpassed Successfully
            } else {
                Assert.fail();
            }
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestaddListener", "final_fpsManagerManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddListener", "final_fpsManagerManager"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddListener", "final_fpsManagerManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestgetURLforFPS() {
        try {
            // gets the url for fps in a string
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetURLforFPS", "TestIFPSManager"));
            Assert.assertTrue(fpsManager.getURLForFPS("fps").contains("http://192"));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetURLforFPS", "TestIFPSManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetURLforFPS", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetURLforFPS", "final_fpsManagerManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestgetConnectProtocolForPeer() {
        try {
            // gets the connect protocol for the peer into a string
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetConnectProtocolForPeer", "TestIFPSManager"));
            Assert.assertTrue(fpsManager.getConnectProtocolForPeer("fps").contains("TCP"));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetConnectProtocolForPeer", "TestIFPSManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetConnectProtocolForPeer", "final_fpsManagerManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestgetLastPeerErrLogs() {
        try {
            // gets the last peer error logs of a given peer and given number of lines . It returns a string
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetLastPeerErrLogs", "TestIFPSManager"));
            String errorlogs = fpsManager.getLastPeerErrLogs("fps", 7);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetLastPeerErrLogs", "TestIFPSManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastPeerErrLogs", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastPeerErrLogs", "final_fpsManagerManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestgetLastPeerOutLogs() {
        try {
            // gets the last peer out logs of the given peer and number of lines. It returns a string
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetLastPeerOutLogs", "TestIFPSManager"));
            String outlogs = fpsManager.getLastPeerOutLogs("fps", 7);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetLastPeerOutLogs", "TestIFPSManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastPeerOutLogs", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastPeerOutLogs", "final_fpsManagerManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestExportFPSLogs() {
        try {
            // exports the fps logs of a given peer and long index into a byte array
            Logger.LogMethodOrder(Logger.getOrderMessage("TestExportFPSLogs", "TestIFPSManager"));

            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            String logDir = home + File.separator + TestEnvironmentConstants.REPORTS_DIR + File.separator
                    + TestNGTestCase.testNG_ReportsDir + File.separator + "logs";
            new File(logDir).mkdir();
            File destination = new File(logDir + File.separator + "fps_logs.zip");
            long index = 0;
            FileOutputStream fos = new FileOutputStream(destination);
            try {
                byte[] data = fpsManager.exportFPSLogs("fps", index);
                while (data != null) {
                    fos.write(data);
                    index = destination.length();
                    data = fpsManager.exportFPSLogs("fps", index);
                }
            } finally {
                try {
                    fos.close();
                } catch (IOException ignore) {/*tried my best to cleanup.*/}
            }
            Assert.assertTrue(destination.length() > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestExportFPSLogs", "TestIFPSManager"));

        } catch (IOException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestExportFPSLogs", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestExportFPSLogs", "final_fpsManagerManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestSystemInfoReference() {
        try {
            // gets a peer system infomation of the given peer . It returns a systeminforeference
            Logger.LogMethodOrder(Logger.getOrderMessage("TestSystemInfoReference", "TestIFPSManager"));
            // gives an systemInfoReference of the given peer
            SystemInfoReference sifrf = fpsManager.getPeerSystemInfo("fps");
            Assert.assertEquals(sifrf.getOpSysName(), "Linux");
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestSystemInfoReference", "TestIFPSManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSystemInfoReference", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSystemInfoReference", "final_fpsManagerManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestgetAllPeersInRepository() {
        try {
            // gets all the peers in the repository. It returns a vector
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetAllPeersInRepository", "TestIFPSManager"));
            // gives all peers avaliable in the repository
            Vector<String> v1 = fpsManager.getAllPeersInRepository();
            Assert.assertTrue(v1.size() > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetAllPeersInRepository", "final_fpsManagerManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetAllPeersInRepository", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetAllPeersInRepository", "final_fpsManagerManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestgetFPSAliases() {
        try {
            // gets Fps aliases for a given peer. It returns a vector
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetFPSAliases", "TestIFPSManager"));
            // gives the Aliases peer for the given peer;
            Vector<String> v2 = fpsManager.getFPSAliases("fps");
            Assert.assertTrue(v2.size() > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetFPSAliases", "TestIFPSManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetFPSAliases", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetFPSAliases", "final_fpsManagerManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = {"IFPSManager"}, dependsOnMethods = "start_fpsManager", alwaysRun = true)
    public void TestgetBackupURLsForPeer() {
        try {
            // gets the backup URL for a given peer . It returns a vector
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetBackupURLsForPeer", "TestIFPSManager"));
            // gives backup urls for the given fps
            Vector v3 = fpsManager.getBackupURLsForFPS("fps");
            Assert.assertTrue(v3.size() > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetBackupURLsForPeer", "TestIFPSManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetBackupURLsForPeer", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetBackupURLsForPeer", "final_fpsManagerManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = "TWO_IFPSManager", dependsOnGroups = "IFPSManager.*", alwaysRun = true)
    public void TestrestartPeer() {
        try {
            // restarts the given peer
            Logger.LogMethodOrder(Logger.getOrderMessage("TestrestartPeer", "TestIFPSManager"));
            resultMap.clear();
            //restarts the server
            fpsManager.restartPeer("fps");
            Thread.sleep(Thread_Sleep_Time);

            if (resultMap.get(TestConstants.PEER_STATUS_UNAVALIABILITY).equals("UNAVAILABLE") && resultMap.get(TestConstants.PEER_STATUS_AVALIABILITY).equals("AVAILABLE")) {

                Assert.assertTrue(true);

            } else {
                Assert.fail();
            }
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestrestartPeer", "TestIFPSManager"));

        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestrestartPeer", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestrestartPeer", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestrestartPeer", "final_fpsManagerManager"), e);
            Assert.fail();
        }
    }


    @Test(groups = {"TWO_IFPSManager"}, dependsOnMethods = "TestrestartPeer", alwaysRun = true)
    public void TestremoveListener() {
        try {
            // removes the ServerStateListener already added
            resultMap.clear();
            Logger.LogMethodOrder(Logger.getOrderMessage("TestremoveListener", "TestIFPSManager"));
            fpsManager.removeListener();

            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            fpsManager.restartPeer("fps");
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (resultMap.size() == 0) {
                // testpassed Successfully
            } else {
                Assert.fail();
            }
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestremoveListener", "TestIFPSManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveListener", "final_fpsManagerManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveListener", "final_fpsManagerManager"), e);
            Assert.fail();
        }
    }

    @Test(enabled = false)
    public static void insertintoResultMap(String key, String value) {
        resultMap.put(key, value);
    }

}
