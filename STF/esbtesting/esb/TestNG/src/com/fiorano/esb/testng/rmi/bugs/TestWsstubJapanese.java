package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
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
 * User: ramesh
 * Date: Dec 2, 2010
 * Time: 4:22:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestWsstubJapanese extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = new String("\u30a4" + "\u30d9" + "\u30f3" + "\u30c8");
    private SampleEventProcessListener epListener;
    private FioranoApplicationController m_fioFioranoApplicationController;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Japanese. as eventProcessManager is not set.");
        }
        try {
            m_fioFioranoApplicationController = ServerStatusController.getInstance().getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestWsstubJapanese"), e);
            Assert.fail("Failed to run test,since we could not fioranoapplicationcontroller!", e);
        }
    }

    @Test(groups = "Bugs", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestLaunchJapaneseEP() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"));

        epListener = null;
        try {
            appGUID = new String(appGUID.getBytes("UTF8"), "UTF8");
        } catch (Exception e) {
            Assert.fail("Failed to get string!", e);
        }
        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            //eventProcessManager.addEventProcessListener(epListener, appGUID);
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("testWsStubJap.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestLaunchJapaneseEP", "TestWsstubJapanese"));

    }

    /**
     * deploys an event process which is located under $testing_home/esb/TestNG/resources/
     *
     * @param zipName - name of the event process zip located under $testing_home/esb/TestNG/resources/
     * @throws java.io.IOException - if file is not found or for any other IO error
     * @throws ServiceException
     */
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

    @Test(groups = "Bugs", description = "WSStub Launch on fps restart for EP with japanese name bug 19714 ", dependsOnMethods = "TestLaunchJapaneseEP", alwaysRun = true)
    public void testWsStubLaunchOnPeerRestartBug19714() {
        //clear map

        Logger.LogMethodOrder(Logger.getOrderMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"));

        try {
            appGUID = new String(appGUID.getBytes("UTF8"), "UTF8");
        } catch (Exception e) {
            Assert.fail("Failed to get string!", e);
        }
        eventStore.clear();
        try {
            //eventProcessManager.startAllServices(appGUID, appVersion);
            eventProcessManager.startAllServices(appGUID, appVersion);
            try {
                Thread.sleep(65000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "1");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }


        try {
            rmiClient.getFpsManager().restartPeer("fps");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"), e);
            Assert.fail("Failed to restart the peer");
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"), e);
            Assert.fail("Failed to restart the peer");
        }

        try {
            Thread.sleep(75000);
        } catch (InterruptedException ignore) {

        }

        try {
            ServiceInstanceStateDetails service = m_fioFioranoApplicationController.getCurrentStateOfService(appGUID,appVersion,"WSStub1");
            if (service.getStatusString().equals("SERVICE_HANDLE_UNBOUND"))
                Assert.fail("WSStub is not running after peer restart");
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"), e);
            Assert.fail("not able to execute getWSContexts.", e);
        }


        try {
            eventProcessManager.restartEventProcess(appGUID,appVersion);
            try {
                Thread.sleep(75000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"), e);
            Assert.fail("Failed to restart EP", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"), e);
            Assert.fail("Failed to restart EP", e);
        }

        try {
            ServiceInstanceStateDetails service = m_fioFioranoApplicationController.getCurrentStateOfService(appGUID,appVersion, "WSStub1");
            if (service.getStatusString().equals("SERVICE_HANDLE_UNBOUND"))
                Assert.fail("WSStub is not running after EP restart");
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"), e);
            Assert.fail("not able to execute getWSContexts.", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testWsStubLaunchOnPeerRestartBug19714", "TestWsstubJapanese"));

    }

    @Test(groups = "Bugs", description = "Stop & Delete Japanese Wsstub Application", dependsOnMethods = "testWsStubLaunchOnPeerRestartBug19714", alwaysRun = true)
    public void testDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestWsstubJapanese"));
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestWsstubJapanese"), e);
            Assert.fail("Failed to delete EP", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestWsstubJapanese"), e);
            Assert.fail("Failed to delete EP", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testDeleteApplication", "TestWsstubJapanese"));
    }
}
