package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ramesh
 * Date: Jan 12, 2011
 * Time: 12:47:45 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestImportSSLWSStub extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "WSSTUBSSL";
    private SampleEventProcessListener epListener = null;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            //Assert.fail("Cannot run Group Bugs. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "Bugs", description = "Import SSL enabled WSStub from 9.0.1 - bug 20163 ", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestLaunchSSLWSStub() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        String kcckeys = ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.TESTING_HOME) + File.separator + "configuration" + File.separator +
                "profilerConfigs" + File.separator + "peer_jetty" + File.separator + "kcckeys";
        String wsstubconfig = ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.FIORANO_HOME) + File.separator + "runtimedata" +
                File.separator + "repository" + File.separator + "applications" + File.separator + "WSSTUBSSL" + File.separator + "1.0" + File.separator + "config";

        try {
            deployEventProcess("wsstubssl.zip");
            Thread.sleep(4000);

            File f1 = new File(wsstubconfig + File.separator + "WSStub1.xml");
            File f2 = new File(wsstubconfig + File.separator + "WSStub1_new.xml");
            BufferedReader br;
            BufferedWriter bw;

            br = new BufferedReader(new FileReader(f1));

            bw = new BufferedWriter(new FileWriter(f2));

            String line = "";
            while ((line = br.readLine()) != null) {

                if (line.contains("kcckeys")) {
                    line = "<string>" + kcckeys + "</string>";
                }

                bw.write(line + "\n");

            }
            br.close();
            bw.flush();
            bw.close();

            f1.delete();
            f2.renameTo(new File(wsstubconfig + File.separator + "WSStub1.xml"));

        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
            //Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
            //Assert.fail("Failed to do SAVE!", e);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
            //Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
           // Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startServiceInstance(appName,appVersion, "WSStub1");
            try {
                Thread.sleep(75000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
           // Assert.fail("Failed to start the imported SSL configured WSStub", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
            //Assert.fail("Failed to start the imported SSL configured WSStub", e);
        }

        String ErrStr = null;
        try {
            ErrStr = eventProcessManager.getLastErrTrace(100, "WSStub1", appName, appVersion);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
            //Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"), e);
            //Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        }

        if (ErrStr.contains("Reason (401)Unauthorized")) {
            Assert.fail("Basic Authentication is enabled in peer but auth details not set in WSStub");
        }

        if (ErrStr.contains("Caused by: java.net.ConnectException: Connection refused")) {
            //Assert.fail("SSL might not be enabled in peer jetty configuration causing the error");
        }
        // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "1");

        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestLaunchSSLWSStub", "TestImportSSLWSStub"));

    }

    @Test(groups = "Bugs", dependsOnMethods = "TestLaunchSSLWSStub", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestImportSSLWSStub"));
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestImportSSLWSStub"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestImportSSLWSStub"), e);
            //Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestImportSSLWSStub"));
    }

    @Test(enabled = false)
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
}
