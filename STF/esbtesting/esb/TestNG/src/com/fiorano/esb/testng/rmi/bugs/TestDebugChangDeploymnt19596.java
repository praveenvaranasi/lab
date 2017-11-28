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
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 3/9/12
 * Time: 11:41 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestDebugChangDeploymnt19596 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager1;
    private IEventProcessManager eventProcessManager2;
    private IRmiManager rmiManager;
    private IFPSManager fpsManager;
    private FioranoApplicationController m_fioranoappcontroller;
    private ServerStatusController serverStatus;
    private BreakpointMetaData BMData,BMData1;
    private IDebugger Debug;
    private IDebugger Debug2;
    private RouteMetaData RMData;
    private String handleID1;
    private String handleID2;
    private String userName = "admin";
    private String password = "passwd";
    private SampleEventProcessListener epListener = null;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "ONE";
    private String appName2= "TWO";
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initNewSetUp() throws ServiceException, RemoteException {

        try{
            ArrayList urlDetails = getActiveFESUrl();
            Registry registry = LocateRegistry.getRegistry((String) urlDetails.get(0), (Integer) urlDetails.get(1));

        try{
            rmiManager = (IRmiManager) registry.lookup(IRmiManager.class.toString());
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        handleID1 = rmiManager.login(userName, password);
        //handleID2 = rmiManager.login(userName, password);
        }catch (Exception e){
            e.printStackTrace();
        }

        this.fpsManager = rmiClient.getFpsManager();
        eventProcessManager1 = rmiManager.getEventProcessManager(handleID1);
        //eventProcessManager2 = rmiManager.getEventProcessManager(handleID2);
        Debug = rmiManager.getBreakpointManager(handleID1);
        //Debug2= rmiManager.getBreakpointManager(handleID2);
        this.serverStatus = ServerStatusController.getInstance();

         try {
            this.m_fioranoappcontroller = serverStatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("initNewSetUp", "TestDebugChangDeploymnt19596"), e);
        }

    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "initNewSetUp", alwaysRun = true)
    public void TestAlrdyExstsExcpt() throws RemoteException,ServiceException{
        epListener = new SampleEventProcessListener(appName, eventStore);
        eventProcessManager1.addEventProcessListener(epListener);
        try {
            deployEventProcess("ONE.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestAlrdyExstsExcpt", "TestDebugChangDeploymnt19596"), e);
        }
        eventProcessManager1.checkResourcesAndConnectivity(appName, appVersion);
        eventProcessManager1.startEventProcess(appName, appVersion, false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        List<RouteMetaData> rList = eventProcessManager1.getRoutesOfEventProcesses(appName,appVersion);
        RMData = rList.get(0);

        BMData = Debug.addBreakpoint(appName,appVersion,RMData.getRouteName());

        eventProcessManager1.stopServiceInstance(appName,appVersion,"chat1");

        String[] nodes1 = {"fps1"};
        String[] nodes = {"fps"};
        try {
            Application application = m_fioranoappcontroller.getApplication(appName, appVersion);
            application.getServiceInstance("chat1").setNodes(nodes1);

            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestAlrdyExstsExcpt", "TestDebugChangDeploymnt19596"), e);
        }

        try{
           eventProcessManager1.synchronizeEventProcess(appName,appVersion);
        }catch(Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestAlrdyExstsExcpt", "TestDebugChangDeploymnt19596"), e);
        }
        //eventProcessManager1.stopServiceInstance(appName,"chat1");

        try {
            Application application = m_fioranoappcontroller.getApplication(appName, appVersion);
            application.getServiceInstance("chat1").setNodes(nodes);

            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestAlrdyExstsExcpt", "TestDebugChangDeploymnt19596"), e);
        }

        try{
           eventProcessManager1.synchronizeEventProcess(appName,appVersion);
        }catch(Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestAlrdyExstsExcpt", "TestDebugChangDeploymnt19596"), e);
        }

         Logger.Log(Level.INFO, Logger.getFinishMessage(" TestAlrdyExstsExcpt", "TestDebugChangDeploymnt19596"));

    }

    @Test(enabled = false)
    public static ArrayList getActiveFESUrl() throws STFException {
        ArrayList url = new ArrayList(2);//string followed by int.
        try {
            String s = ServerStatusController.getInstance().getURLOnFESActive();
            String rtlPort = s.substring(s.lastIndexOf(":") + 1);
            url.add(s.substring(s.lastIndexOf("//") + 2, s.lastIndexOf(":")));
            if (rtlPort.equals("1947")) {
                url.add(2047);
            } else
                url.add(2048);
        } catch (STFException e) {
            e.printStackTrace();
        }
        return url;
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
            this.eventProcessManager1.deployEventProcess(result, completed);

        }
    }

}
