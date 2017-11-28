package com.fiorano.esb.testng.rmi.sanity;

import com.fiorano.esb.server.api.FESPerformanceStats;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.server.api.SystemInfoReference;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
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
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Feb 23, 2010
 * Time: 4:34:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Test(dependsOnGroups = {"TWO_IFPSManager.*"})
public class TestIServiceProviderManager extends AbstractTestNG {
    public IServiceProviderManager serviceProviderManager;

    @Test(groups = "IServiceProviderManager", alwaysRun = true)
    public void start_IServiceProviderManager() {
        Logger.LogMethodOrder(Logger.getOrderMessage("start_IServiceProviderManager", "TestIServiceProviderManager"));
        serviceProviderManager = TestIRmiManager.getserviceProviderManager();
        Logger.Log(Level.INFO, Logger.getFinishMessage("start_IServiceProviderManager", "TestIServiceProviderManager"));
    }

    @Test(groups = "IServiceProviderManager", dependsOnMethods = "start_IServiceProviderManager", alwaysRun = true)
    public void TestgetServerName() {
        try {
            // returns the server name of the given server provided
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetServerName", "TestIServiceProviderManager"));
            String servername = serviceProviderManager.getServerName();
            Assert.assertTrue(servername.contains("fes"));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetServerName", "TestIServiceProviderManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetServerName", "TestIServiceProviderManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetServerName", "TestIServiceProviderManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceProviderManager", dependsOnMethods = "start_IServiceProviderManager", alwaysRun = true)
    public void TestgetFESActiveSwitchTime() {
        try {
            // gets teh active switch time of the fes. It returns a long value
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetFESActiveSwitchTime", "TestIServiceProviderManager"));
            Assert.assertTrue(serviceProviderManager.getFESActiveSwitchTime() > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetFESActiveSwitchTime", "TestIServiceProviderManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetFESActiveSwitchTime", "TestIServiceProviderManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetFESActiveSwitchTime", "TestIServiceProviderManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceProviderManager", dependsOnMethods = "start_IServiceProviderManager", alwaysRun = true)
    public void TestexportLogs() {
        try {
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            String logDir = home + File.separator + TestEnvironmentConstants.REPORTS_DIR + File.separator
                    + TestNGTestCase.testNG_ReportsDir + File.separator + "logs";
            new File(logDir).mkdir();
            File destination = new File(logDir + File.separator + "fes_logs.zip");
            destination.createNewFile();
            // expots the logs of the server into a bye array with starting index of long value
            Logger.LogMethodOrder(Logger.getOrderMessage("TestexportLogs", "TestIServiceProviderManager"));
            long index = 0;
            FileOutputStream fos = new FileOutputStream(destination);
            try {
                byte[] data = serviceProviderManager.exportLogs(index);
                while (data != null) {
                    fos.write(data);
                    index = destination.length();
                    data = serviceProviderManager.exportLogs(index);
                }
            } finally {
                try {
                    fos.close();
                } catch (IOException ignore) {/*tried my best to cleanup.*/}
            }
            Assert.assertTrue(destination.length() > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestexportLogs", "TestIServiceProviderManager"));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestexportLogs", "TestIServiceProviderManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestexportLogs", "TestIServiceProviderManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceProviderManager", dependsOnMethods = "start_IServiceProviderManager", alwaysRun = true)
    public void TestSystemInfoReferenece() {
        try {
            // gets the server system information of the serviceprovidermanager. It returns a system info reference
            Logger.LogMethodOrder(Logger.getOrderMessage("TestSystemInfoReferenece", "TestIServiceProviderManager"));
            SystemInfoReference sirf = serviceProviderManager.getServerSystemInfo();
            Assert.assertTrue(sirf.getOpSysName().equals("Linux"));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestSystemInfoReferenece", "TestIServiceProviderManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSystemInfoReferenece", "TestIServiceProviderManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSystemInfoReferenece", "TestIServiceProviderManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceProviderManager", dependsOnMethods = "start_IServiceProviderManager", alwaysRun = true)
    public void TestFESPerformanceStats() {
        try {
            // gets the fesperformance stats of the serviceprovider manager object. It returns a FESPerformanceStats object
            Logger.LogMethodOrder(Logger.getOrderMessage("TestFESPerformanceStats", "TestIServiceProviderManager"));
            FESPerformanceStats fesps = serviceProviderManager.getServerPerformanceStats();
            Assert.assertTrue(fesps.getTotalProcessCount() > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestFESPerformanceStats", "TestIServiceProviderManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFESPerformanceStats", "TestIServiceProviderManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFESPerformanceStats", "TestIServiceProviderManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceProviderManager", dependsOnMethods = "start_IServiceProviderManager", alwaysRun = true)
    public void TestgetServerDetails() {
        try {
            // gets the serverdetails of the serviceprovidermanager object. It returns a  hashmap containing all the details of the server
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetServerDetails", "TestIServiceProviderManager"));
            HashMap h1 = serviceProviderManager.getServerDetails();
            Assert.assertTrue(h1.size() > 0);
            Assert.assertTrue(((String) h1.get("OS")).contains("Linux"));
            Assert.assertTrue(((String) h1.get("Name")).contains("fes"));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetServerDetails", "TestIServiceProviderManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetServerDetails", "TestIServiceProviderManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetServerDetails", "TestIServiceProviderManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceProviderManager", dependsOnMethods = "start_IServiceProviderManager", alwaysRun = true)
    public void TestgetLastErrLogs() {
        try {
            // gets the last error logs of the server with a given number of lines to retreive.
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetLastErrLogs", "TestIServiceProviderManager"));
            String log1 = serviceProviderManager.getLastErrLogs(7);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetLastErrLogs", "TestIServiceProviderManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastErrLogs", "TestIServiceProviderManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastErrLogs", "TestIServiceProviderManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceProviderManager", dependsOnMethods = "start_IServiceProviderManager", alwaysRun = true)
    public void TestgetLastOutLogs() {
        try {
            // gets the last outlogs of the  server with a given number of lines. It returns a string
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetLastOutLogs", "TestIServiceProviderManager"));
            String log2 = serviceProviderManager.getLastOutLogs(7);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetLastOutLogs", "TestIServiceProviderManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastOutLogs", "TestIServiceProviderManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastOutLogs", "TestIServiceProviderManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "TWO_IServiceProviderManager", dependsOnGroups = "IServiceProviderManager.*", alwaysRun = true)
    public void TestrestartServer() {
        try {
            // restarts the server which is currently in running state. If the server is not in running state. It will throw an exception
            Logger.LogMethodOrder(Logger.getOrderMessage("TestrestartServer", "TestIServiceProviderManager"));
            serviceProviderManager.restartServer();
        } catch (RemoteException e) {
            //Ignore
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestrestartServer", "TestIServiceProviderManager"), e);
            Assert.fail();
            return;
        }
        try {
            Thread.sleep(Thread_Sleep_Time);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            Assert.fail();
        }

        //We need to update the rmiManager object in TestIRmiManager class
        TestIRmiManager.updateIRmiManager();

        try {
            String servername = TestIRmiManager.getserviceProviderManager().getServerName();
            Assert.assertTrue(servername.contains("fes"));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestrestartServer", "TestIServiceProviderManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestrestartServer", "TestIServiceProviderManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestrestartServer", "TestIServiceProviderManager"), e);
            Assert.fail();
        }


    }
}







