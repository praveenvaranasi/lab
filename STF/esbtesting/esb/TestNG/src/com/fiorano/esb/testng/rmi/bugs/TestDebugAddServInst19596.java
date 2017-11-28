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
import fiorano.tifosi.dmi.application.ServiceInstance;
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
 * Date: 3/15/12
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDebugAddServInst19596 extends AbstractTestNG{

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private IDebugger Debug;
    private SampleEventProcessListener epListener,epListener1;
    private Hashtable<String,String> eventStore = new Hashtable<String, String>();
    private BreakpointMetaData BMData;
    private RouteMetaData RMData;
    private FioranoApplicationController m_fioranoappcontroller;
    private ServerStatusController serverStatus;
    private Application application;
    private String appName1 = "EVENT_PROCESS5";
    private String appName2 = "EVENT_PROCESS6";
    private float appVersion = 1.0f;
    @Test(groups ="bugs",alwaysRun =true)
    public void initNewSetup(){
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.fpsManager = rmiClient.getFpsManager();
        this.Debug = rmiClient.getDebugger();
        this.serverStatus = ServerStatusController.getInstance();

        try {
            this.m_fioranoappcontroller = serverStatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("initNewSetup", "TestDebugAddServInst19596"), e);
        }
    }

    @Test(groups = "bugs",description = "save EP with break point",dependsOnMethods = "initNewSetup",alwaysRun = true)
    public void TestNewNone() throws ServiceException,RemoteException {
        epListener = new SampleEventProcessListener(appName1,eventStore);
        eventProcessManager.addEventProcessListener(epListener);

        try {
            deployEventProcess("EVENT_PROCESS5.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestNewNone","TestDebugAddServInst19596"));
        }

        eventProcessManager.checkResourcesAndConnectivity(appName1,appVersion);
        eventProcessManager.startEventProcess(appName1,appVersion,false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        epListener1 = new SampleEventProcessListener(appName2,eventStore);
        eventProcessManager.addEventProcessListener(epListener1);

        try {
            deployEventProcess("EVENT_PROCESS6.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestNewNone","TestDebugAddServInst19596"));
        }

        eventProcessManager.checkResourcesAndConnectivity(appName2,appVersion);
        eventProcessManager.startEventProcess(appName2,appVersion,false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        List<RouteMetaData> rList = eventProcessManager.getRoutesOfEventProcesses(appName2,appVersion);
        RMData = rList.get(0);

        BMData = Debug.addBreakpoint(appName2,appVersion,RMData.getRouteName());

        ServiceInstance servInst = new ServiceInstance();
        addServiceInstance(appName2,servInst,appVersion);

        Logger.Log(Level.INFO,Logger.getFinishMessage("TestNewNone","TestDebugAddServInst19596"));
    }

    @Test(groups = "Bugs", dependsOnMethods = "save EP with break point", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestDebugAddServInst19596"));

            stopAndDeleteEP(eventProcessManager,appName2,appVersion);
            stopAndDeleteEP(eventProcessManager,appName1,appVersion);

        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestDebugAddServInst19596"));
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

     @Test(enabled = false)
    private void addServiceInstance(String appName,ServiceInstance servInstance,float num){
        String[] nodes = {"fps"};

        servInstance.setGUID("chat");
        servInstance.setVersion(4.0f);
        servInstance.setName("chat3");
        servInstance.setNodes(nodes);

        //Application application = null;
        try {
            this.application = m_fioranoappcontroller.getApplication(appName,num);

            application.addServiceInstance(servInstance);

            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
             Logger.Log(Level.SEVERE, Logger.getErrMessage("addServiceInstance", "TestDebugAddServInst19596"), e);
        } catch(NullPointerException e){
            Logger.Log(Level.SEVERE,Logger.getErrMessage("addServiceInstance","TestDebugAddServInst19596"),e);
        }
    }

}
