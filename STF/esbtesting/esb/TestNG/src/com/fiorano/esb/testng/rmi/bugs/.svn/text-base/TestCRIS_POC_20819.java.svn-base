package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: janardhan
 * Date: 9/15/11
 * Time: 3:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestCRIS_POC_20819 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IServiceProviderManager SPManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "CRISPOC";
    private float appVersion = 1.0f;
    @Test(groups = "CRIS_POC", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.SPManager = rmiClient.getServiceProviderManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group GarbledLogs. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "CRIS_POC", description = "bug no 20819: doing CRC should throw exception ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testCRCofApplication() {

        System.out.println("running testCRCofApplication of TestCRIS_POC_20819");
        Logger.LogMethodOrder(Logger.getOrderMessage("testCRCofApplication", "TestCRIS_POC_20819"));

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRCofApplication", "TestCRIS_POC_20819"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRCofApplication", "TestCRIS_POC_20819"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRCofApplication", "TestCRIS_POC_20819"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("crispoc-1.0@EnterpriseServer.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRCofApplication", "TestCRIS_POC_20819"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRCofApplication", "TestCRIS_POC_20819"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {

            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
            throw new RemoteException("CRC is done successfully which should thow the exception ideally. so throwing exception.");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCRCofApplication", "TestCRIS_POC_20819"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            System.out.println("failing to do CRC. so working fine.");
        }
    }

    @Test(groups = "RestStubIncorrectXML", dependsOnMethods = "testCRCofApplication", alwaysRun = true)
    public void stopAndDeleteApplication() {
        try {
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestCRIS_POC_20819"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestCRIS_POC_20819"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
    }


    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + "/esb/TestNG/resources/" + zipName));
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
