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
 * Date: 8/1/12
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestStopComponentRevertPeer21769 extends AbstractTestNG{

    private IRmiManager rmiManager;
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private IDebugger debugger;
    private static Hashtable <String,String> eventStore = new Hashtable<String, String>();
    private SampleEventProcessListener epListener = null;
    private String appName = "CHAT_21769";
    private RouteMetaData RMData;
    private BreakpointMetaData BMData;
    private ServerStatusController serverStatus;
    private FioranoApplicationController appController;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs" ,alwaysRun = true)
    public void initNewSetup(){
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.fpsManager = rmiClient.getFpsManager();
        this.debugger = rmiClient.getDebugger();
        this.serverStatus = ServerStatusController.getInstance();
        try {
            this.appController = serverStatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangDeploy", "TestStopComponentRevertPeer21769"),e);
        }
    }

    @Test(groups = "Bugs", description = "deploying on diff fps",dependsOnMethods = "initNewSetup",alwaysRun = true)
    public void TestChangDeploy() throws ServiceException, RemoteException {
        try {
            epListener = new SampleEventProcessListener(appName,eventStore);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestChangDeploy1","TestStopComponentRevertPeer21769"),e);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestChangDeploy2","TestStopComponentRevertPeer21769"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestChangDeploy3","TestStopComponentRevertPeer21769"),e);
        }

        try {
            deployEventProcess("CHAT_21769.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestChangDeploy4","TestStopComponentRevertPeer21769"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangDeploy5", "TestStopComponentRevertPeer21769"),e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName,appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangDeploy6", "TestStopComponentRevertPeer21769"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangDeploy7", "TestStopComponentRevertPeer21769"),e);
        }

        try {
            eventProcessManager.startEventProcess(appName,appVersion,false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangDeploy8", "TestStopComponentRevertPeer21769"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangDeploy9", "TestStopComponentRevertPeer21769"),e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestChangDeploy10","TestStopComponentRevertPeer21769"),e);
        }

        List<RouteMetaData> rList = eventProcessManager.getRoutesOfEventProcesses(appName,appVersion);
        RMData = rList.get(0);

        BMData = debugger.addBreakpoint(appName,appVersion, RMData.getRouteName());

        eventProcessManager.stopServiceInstance(appName,appVersion,"chat1");

        String[] nodes = {"fps"};
        String[] nodes1 = {"fps1"};
        try {
            Application application = appController.getApplication(appName, appVersion);
            application.getServiceInstance("chat1").setNodes(nodes1);

            appController.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestChangDeploy11", "TestStopComponentRevertPeer21769"), e);
        }

        try{
            eventProcessManager.synchronizeEventProcess(appName,appVersion);
        }catch(Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestChangDeploy12", "TestStopComponentRevertPeer21769"), e);
        }

        try {
            Application application = appController.getApplication(appName, appVersion);
            application.getServiceInstance("chat1").setNodes(nodes);

            appController.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestChangDeploy13", "TestStopComponentRevertPeer21769"), e);
        }

        try{
           eventProcessManager.synchronizeEventProcess(appName,appVersion);
        }catch(Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestChangDeploy14", "TestStopComponentRevertPeer21769"), e);
        }

        Logger.Log(Level.SEVERE, Logger.getFinishMessage(" TestChangDeploy", "TestStopComponentRevertPeer21769"));


    }

    @Test(groups = "Bugs", description = "Deploying of diff fps ", dependsOnMethods = "TestChangDeploy", alwaysRun = true)
    public void stopAndDeleteEP() throws RemoteException,ServiceException{
        stopAndDeleteEP(eventProcessManager, appName, appVersion);
    }

    @Test(enabled = false)
    public void deployEventProcess(String zipName) throws IOException,ServiceException{
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String testHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(testHome);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator +
                                      "TestNG" + File.separator +"resources" + File.separator + zipName  ));
        boolean completed = false;
        byte[] result;
        while(!completed){
            byte[] temp = new byte[1024];
            int count = bis.read(temp);

            if(count < 0){
                completed = true;
                count = 0;
            }

            result = new byte[count];
            System.arraycopy(temp,0,result,0,count);

            this.eventProcessManager.deployEventProcess(result,completed);
        }

    }

}
