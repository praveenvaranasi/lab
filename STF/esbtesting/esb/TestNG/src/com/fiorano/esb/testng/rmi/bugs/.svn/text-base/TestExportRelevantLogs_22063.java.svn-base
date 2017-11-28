package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import com.fiorano.stf.test.core.TestNGTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;


/**
 * Created by IntelliJ IDEA.
 * User: Anubhav
 * Date: 8/23/12
 * Time: 15:04 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestExportRelevantLogs_22063 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "BUG_22063";
    private SampleEventProcessListener epListener = null;
    private IDebugger debugger;
    private BreakpointMetaData BMData;
    public IServiceProviderManager serverlogs;
    public static IRmiManager rmiManager;
    private String HandleID;
    private float appVersion = 1.0f;


    @Test(groups = "Bugs", alwaysRun = true)
    public void initBug_Setup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            // Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if (fpsManager == null) {
            // Assert.fail("Cannot run Group Bugs as fpsManager is not set.");
        }
        this.debugger = rmiClient.getDebugger();
    }

    @Test(groups = "Bugs", description = "Export logs for Servers should export all relevant logs. check reports/TestNG_Reports__testNG_Rmi_Cases/logs/fes_logs.zip for all logs - bug 22063 ", dependsOnMethods = "initBug_Setup", alwaysRun = true)
    public void TestBUG_22063() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestBUG_22063", "TestExportRelevantLogs_22063"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestBUG_22063", "TestExportRelevantLogs_22063"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);


        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_22063.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_22063", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","TestExportRelevantLogs_22063"),e);
            Assert.fail("Failed to stop EP",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","TestExportRelevantLogs_22063"),e);
            Assert.fail("Failed to stop EP",e);
        }

    }

    @Test(groups="Bugs", dependsOnMethods="TestBUG_22063", alwaysRun = true)
    private void TestCheckExportLogs(){
        try {
            deployEventProcess("BUG_22063.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to start all services!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to start all services!", e);
        }


        try{
            topicpublisher("fps", appName + "__" + "1_0" + "__" + "chat1" + "__OUT_PORT");
        }
        catch (Exception e)
        {
            Assert.fail("Failed to send message", e);
        }
        try{

            ArrayList urlDetails = getActiveFESUrl();
            Registry reg = LocateRegistry.getRegistry((String) urlDetails.get(0), (Integer) urlDetails.get(1));
            rmiManager = (IRmiManager) reg.lookup(IRmiManager.class.toString());
            HandleID= rmiManager.login("admin", "passwd");
            serverlogs = rmiManager.getServiceProviderManager(HandleID);
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            String logDir = home + File.separator + TestEnvironmentConstants.REPORTS_DIR + File.separator
                    + TestNGTestCase.testNG_ReportsDir + File.separator + "logs";
            new File(logDir).mkdir();
            File destination = new File(logDir + File.separator + "fes_logs.zip");
            destination.createNewFile();
            long index = 0;
            FileOutputStream fos = new FileOutputStream(destination);
            byte[] data = serverlogs.exportLogs(index);
            while (data != null) {
                fos.write(data);
                index = destination.length();
                data = serverlogs.exportLogs(index);
            }
            fos.close();

         } catch (IOException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to export logs", e);
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to export logs", e);
        }   catch (STFException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to export logs", e);
        }   catch (NotBoundException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to export logs", e);
        }

        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs","TestExportRelevantLogs_22063"),e);
            Assert.fail("Failed to stop and delete EP",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCheckExportLogs","TestExportRelevantLogs_22063"),e);
            Assert.fail("Failed to stop and delete EP",e);
        }
    }

    @Test(enabled=false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" +
                File.separator + "resources" + File.separator + zipName));
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

    @Test(enabled = false)
    public String topicpublisher(String servername, String topicname) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicname);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestExportRelevantLogs_22063"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {

            // pub.close();
        }
        return messageSent;
    }

   
}
