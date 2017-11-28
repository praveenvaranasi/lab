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
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
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
 * Date: 3/5/12
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDebugTwoStudios19596 extends AbstractTestNG{

    private IEventProcessManager eventProcessManager1;
    private IEventProcessManager eventProcessManager2;
    private IRmiManager rmiManager;
    private TestEnvironmentConfig testenvconfig;
    private IFPSManager fpsManager;
    private FioranoApplicationController m_fioranoappcontroller;
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
    private ExecutionController executioncontroller;
    private TestEnvironment testenv;
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
        handleID2 = rmiManager.login(userName, password);
        }catch (Exception e){
            e.printStackTrace();
        }
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        this.testenv = ServerStatusController.getInstance().getTestEnvironment();
        this.fpsManager = rmiClient.getFpsManager();
        eventProcessManager1 = rmiManager.getEventProcessManager(handleID1);
        //eventProcessManager2 = rmiManager.getEventProcessManager(handleID2);
        Debug = rmiManager.getBreakpointManager(handleID1);
        Debug2= rmiManager.getBreakpointManager(handleID2);

    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "initNewSetUp", alwaysRun = true)
    public void TestNewNone() throws ServiceException, RemoteException{

        Logger.LogMethodOrder(Logger.getOrderMessage("TestNewNone", "TestDebugTwoStudios19596"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestNewNone", "TestDebugTwoStudios19596"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugTwoStudios19596"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager1.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugTwoStudios19596"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugTwoStudios19596"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }



        try {
            try{//cleanup
                eventProcessManager1.stopEventProcess(appName, appVersion);
                eventProcessManager1.deleteEventProcess(appName, appVersion, true, true);
            } catch (Exception e ){}
            deployEventProcess("ONE.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugTwoStudios19596"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugTwoStudios19596"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        List<RouteMetaData> rList = eventProcessManager1.getRoutesOfEventProcesses(appName,appVersion);
        RMData = rList.get(0);

        try {
            eventProcessManager1.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugTwoStudios19596"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugTwoStudios19596"), e);
           // Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager1.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugTwoStudios19596"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestDebugTwoStudios19596"), e);
           // Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        BMData = Debug.addBreakpoint(appName,appVersion,RMData.getRouteName());

    }


    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "TestNewNone", alwaysRun = true)
    public void TestAnotherInstance() {
        //eStudio1 crashes
        eventProcessManager1=null;
        handleID1=null;

        try {
            Debug2.removeBreakpoint(appName,appVersion, RMData.getRouteName());
        } catch (RemoteException e) {
             Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAnotherInstance", "TestDebugTwoStudios19596"), e);
        } catch (ServiceException e) {
             Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAnotherInstance", "TestDebugTwoStudios19596"), e);
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestAnotherInstance", "TestDebugTwoStudios19596"));
    }


     @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "TestAnotherInstance", alwaysRun = true)
     public void TestExcptOnSecondBreakPoint(){
         int count = 0;

         try {
             BMData = Debug2.addBreakpoint(appName,appVersion,RMData.getRouteName());
         } catch (RemoteException e) {
             Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestExcptOnSecondBreakPoint", "TestDebugTwoStudios19596"), e);
         } catch (ServiceException e) {
             Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestExcptOnSecondBreakPoint", "TestDebugTwoStudios19596"), e);
         }
     }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "TestExcptOnSecondBreakPoint", alwaysRun = true)
    public void TestClntIDExistsExcpt() throws RemoteException,ServiceException{
        eventProcessManager1 = rmiManager.getEventProcessManager(handleID2);
        epListener = new SampleEventProcessListener(appName2, eventStore);
        eventProcessManager1.addEventProcessListener(epListener);
        try {
            deployEventProcess("TWO.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClntIDExistsExcpt", "TestDebugTwoStudios19596"), e);
        }
        eventProcessManager1.checkResourcesAndConnectivity(appName2, appVersion);
        eventProcessManager1.startEventProcess(appName2, appVersion, false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        List<RouteMetaData> rList = eventProcessManager1.getRoutesOfEventProcesses(appName2,appVersion);
        RMData = rList.get(0);

        BMData = Debug2.addBreakpoint(appName2,appVersion,RMData.getRouteName());

        fpsManager.shutdownPeer("fps");

        try{
           Debug2.removeBreakpoint(appName2,appVersion,RMData.getRouteName());

           BMData1 = Debug2.addBreakpoint(appName2,appVersion,RMData.getRouteName());

        }catch(Exception e){
             Logger.Log(Level.SEVERE, Logger.getErrMessage("TestClntIDExistsExcpt", "TestDebugTwoStudios19596"), e);
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestClntIDExistsExcpt", "TestDebugTwoStudios19596"));

    }

    @Test(groups = "bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "TestClntIDExistsExcpt", alwaysRun = true)
    public void testCleanUp() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testCleanUp", "TestSpaceInFioranoHome"));
        startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
        try {

            eventProcessManager1.stopEventProcess(appName, appVersion);
            Thread.sleep(5000);
            eventProcessManager1.deleteEventProcess(appName,appVersion, true, false);
            Thread.sleep(5000);
            eventProcessManager1.stopEventProcess(appName2, appVersion);
            Thread.sleep(5000);
            eventProcessManager1.deleteEventProcess(appName2,appVersion, true, false);
            Thread.sleep(5000);
        } catch (Exception e) {
            //ignore
        }
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

     @Test(enabled = false)
    public void TestStartFPS() throws RemoteException,ServiceException{
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStartFPS1", "TestStopRemoteInstance"));

            if (!fpsManager.isPeerRunning("fps")) {
                startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
            }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStartFPS", "TestDebugTwoStudios19596"));
    }
}
