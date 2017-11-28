package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.*;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 3/14/12
 * Time: 5:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDebugStopPeerRemovBrkPont19596 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private IDebugger Debug;
    private SampleEventProcessListener epListener;
    private TestEnvironmentConfig testenvconfig;
    private FioranoApplicationController m_fioranoappcontroller;
    private ServerStatusController serverStatus;
    private Hashtable<String,String> eventStore = new Hashtable<String, String>();
    private BreakpointMetaData BMData;
    private RouteMetaData RMData;
    private String appName = "EVENT_PROCESS1";
    private float appVersion = 1.0f;
    private TestEnvironment testenv;

    @Test(groups = "bugs",alwaysRun = true)
    public void initNewSetup(){
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.fpsManager = rmiClient.getFpsManager();
        this.Debug = rmiClient.getDebugger();
        this.serverStatus = ServerStatusController.getInstance();
        try {
            this.m_fioranoappcontroller = serverStatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("initNewSetup", "TestDebugStopPeerRemovBrkPont19596"), e);
        }
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        this.testenv = ServerStatusController.getInstance().getTestEnvironment();
    }

    @Test(groups = "bugs",description = "remove BP after peer shutdown",dependsOnMethods = "initNewSetup",alwaysRun=true)
    public void TestNewNone() throws ServiceException,RemoteException{

        epListener = new SampleEventProcessListener(appName,eventStore);

        eventProcessManager.addEventProcessListener(epListener);

        try {
            deployEventProcess("EVENT_PROCESS1.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugStopPeerRemovBrkPont19596"), e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugStopPeerRemovBrkPont19596"), e);
        }

        eventProcessManager.checkResourcesAndConnectivity(appName,appVersion);
        eventProcessManager.startEventProcess(appName,appVersion,false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        String[] nodes1 = {"fps1"};
        String[] nodes = {"fps"};
        try {
            Application application = m_fioranoappcontroller.getApplication(appName, appVersion);
            application.getServiceInstance("chat2").setNodes(nodes1);

            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestNewNone", "TestDebugStopPeerRemovBrkPont19596"), e);
        }

        try {
            Application application = m_fioranoappcontroller.getApplication(appName, appVersion);
            application.getServiceInstance("chat1").setNodes(nodes);

            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestNewNone", "TestDebugStopPeerRemovBrkPont19596"), e);
        }

        List<RouteMetaData> rList = eventProcessManager.getRoutesOfEventProcesses(appName,appVersion);
        RMData = rList.get(0);

        BMData = Debug.addBreakpoint(appName,appVersion,RMData.getRouteName());

        fpsManager.shutdownPeer("fps1");

        try{
            Debug.removeBreakpoint(appName,appVersion,RMData.getRouteName());
        }catch(Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestNewNone", "TestDebugStopPeerRemovBrkPont19596"), e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage(" TestNewNone", "TestDebugStopPeerRemovBrkPont19596"));

    }

    @Test(groups = "bugs", description = "remove BP after peer shutdown", dependsOnMethods = "TestNewNone", alwaysRun = true)
    public void testCleanUp() {
        startServer(testenv, testenvconfig, "fps1", SERVER_TYPE.FPS);
        stopAndDeleteEP(eventProcessManager, appName, appVersion);
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipname) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath()+File.separator+"esb"+File.separator+
                "TestNG"+File.separator+"resources"+File.separator+zipname));

        boolean completed = false;
        byte result[];
        while(!completed){
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
