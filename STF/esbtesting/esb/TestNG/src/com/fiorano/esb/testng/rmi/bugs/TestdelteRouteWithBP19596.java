package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.Route;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 3/9/12
 * Time: 6:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestdelteRouteWithBP19596 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IRmiManager rmiManager;
    private IFPSManager fpsManager;
    private IDebugManager debugManager;
    private SampleEventProcessListener epListener = null;
    private ServerStatusController serverstatus;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private IDebugger Debug;
    private RouteMetaData RMData,RMData1;
    private BreakpointMetaData BMData;
    private String appName1 = "EVENT_PROCESS1";
    private String appName2 = "EVENT_PROCESS3";
    private List<RouteMetaData> rList,rList1;
    private FioranoApplicationController m_fioranoappcontroller;
    private float appVersion = 1.0f;

    @Test(groups = "Bugs", alwaysRun = true)
    public void initNewSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
           // Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if (fpsManager == null) {
           // Assert.fail("Cannot run Group Bugs as fpsManager is not set.");
        }
        this.serverstatus = ServerStatusController.getInstance();
        try {
            this.m_fioranoappcontroller = serverstatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
             Logger.Log(Level.SEVERE, Logger.getErrMessage("initNewSetup", "TestdelteRouteWithBP19596"), e);
        }

        this.Debug = rmiClient.getDebugger();
        rmiManager = rmiClient.getRmiManager();
    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "initNewSetup", alwaysRun = true)
    public void TestNewNone() throws ServiceException, RemoteException{
        epListener = new SampleEventProcessListener(appName2, eventStore);
        eventProcessManager.addEventProcessListener(epListener);
        try {
            deployEventProcess("EVENT_PROCESS1.zip");
            deployEventProcess("EVENT_PROCESS3.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestdelteRouteWithBP19596"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        rList = eventProcessManager.getRoutesOfEventProcesses(appName2,appVersion);
        RMData = rList.get(0);

        rList1 = eventProcessManager.getRoutesOfEventProcesses(appName1,appVersion);
        RMData1 = rList1.get(0);

        eventProcessManager.checkResourcesAndConnectivity(appName2, appVersion);
        eventProcessManager.startEventProcess(appName2, appVersion, false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        this.Debug = rmiClient.getDebugger();
        BMData = Debug.addBreakpoint(appName2,appVersion,RMData.getRouteName());

        try{
           deleteRoute(RMData1.getRouteName(), "EVENT_PROCESS1__1_0__CHAT1__OUT_PORT", "EVENT_PROCESS1__1_0__CHAT2__IN_PORT");
        }catch(Exception e){
           Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestdelteRouteWithBP19596"), e);
        }

        Logger.Log(Level.SEVERE,Logger.getFinishMessage("TestNewNone","TestdelteRouteWithBP19596"));
    }
    @Test(groups = "Bugs", dependsOnMethods = "TestNewNone", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestdelteRouteWithBP19596"));
        stopAndDeleteEP(eventProcessManager, appName2, appVersion);
        stopAndDeleteEP(eventProcessManager, appName1, appVersion);
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestdelteRouteWithBP19596"));
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

    @Test(enabled = false)
    private void deleteRoute(String RName,String SPName,String DPName){

        Route route = new Route();
        route.setName(RName);
        route.setSourcePortInstance(SPName);
        route.setTargetPortInstance(DPName);

        try {
            Application application = m_fioranoappcontroller.getApplication(appName1, appVersion);
            application.removeRoute(route);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteRoute", "TestdelteRouteWithBP19596"), e);
        }


    }
}
