package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ramesh
 * Date: 2/22/11
 * Time: 3:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPurchaseSystem extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "PURCHASING_SYSTEM";
    private SampleEventProcessListener epListener = null;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
    }

    @Test(groups = "Bugs", description = "bug 20015 ", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestPurchasingSystemLaunch() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {

            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
            System.out.println(appName+ " Started");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"), e);
            Assert.fail("Failed to do start application!", e);
        }


        //clear map

        String ErrStr = null;
        try {
            ErrStr = eventProcessManager.getLastErrTrace(100, "Purchase_Request_Transformation", appName, appVersion);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"), e);
            Assert.fail("Failed to do getLastPeerErrLogs on peer fps!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"), e);
            Assert.fail("Failed to do getLastPeerErrLogs on peer fps!", e);
        }

        if (ErrStr.contains("Exception") || ErrStr.contains("Prefix must resolve to a namespace")) {
            Assert.fail("Purchase_System application failed to launch properly:Exception occurred");
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestPurchasingSystemLaunch", "TestPurchaseSystem"));
    }

    @Test(groups = "Bugs", dependsOnMethods = "TestPurchasingSystemLaunch", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestPurchaseSystem"));
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestPurchaseSystem"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestPurchaseSystem"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestPurchaseSystem"));
    }

}