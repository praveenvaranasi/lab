package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: ishita
 * Date: 8/3/12
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */

public class RouteNotRemoved_21910 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "BUG_21910_ROUTE";
    private SampleEventProcessListener epListener = null;
   // private List<RouteMetaData> rList;
   // private RouteMetaData RMData;
    private JMXClient esb = null;
    private ObjectName objName;
    private String handleID;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initBug_21910Setup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            // Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if (fpsManager == null) {
            // Assert.fail("Cannot run Group Bugs as fpsManager is not set.");
        }
    }
    @Test(groups = "Bugs", description = "Route not removed in special scenario - bug 21910 ", dependsOnMethods = "initBug_21910Setup", alwaysRun = true)
    public void TestBug_21910() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestBug_21910", "RouteNotRemoved_21910"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestBug_21910", "RouteNotRemoved_21910"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);


        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_21910_ROUTE.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
        try {
            eventProcessManager.startServiceInstance(appName,appVersion, "Feeder1");
            // Assert.fail("Failed to stop service instance!");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            // Assert.fail("Failed to stop service instance!", e);
        } catch (ServiceException e) {
            //Test Passed Successfully
        }
        try {
            eventProcessManager.startServiceInstance(appName,appVersion, "Display1");
            // Assert.fail("Failed to stop service instance!");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21910", "RouteNotRemoved_21910"), e);
            // Assert.fail("Failed to stop service instance!", e);
        } catch (ServiceException e) {
            //Test Passed Successfully
        }
    }

    @Test(groups="Bugs",dependsOnMethods ="TestBug_21910",alwaysRun=true)
    public void TestKill(){
        Logger.LogMethodOrder(Logger.getOrderMessage("TestKill","TestBug_21910"));

        try {
            eventProcessManager.stopEventProcess(appName,appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestKill","TestBug_21910"));
            Assert.fail("Failed to stop event process",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestKill","TestBug_21910"));
        }
        try {
            esb = JMXClient.getInstance();
            objName = new ObjectName("Fiorano.Etc:ServiceType=RouteManager,Name=RouteManager");
            handleID = esb.getHandleId();
            String[] signatures = {Vector.class.getName()};
            esb.invoke(objName, "listAllRoutes",null, signatures);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKill", "TestBug_21910"), e);
            // Assert.fail("Failed assign permission create or edit and delete a principal to user1", e);
        }
        String str = "Bug_21910_Route";
        String route = objName.toString();
        int i=str.length();
        int j=route.length();
        boolean found = false;
        for (int k = 0; k <= (j-i);k++)
        {
                if (route.regionMatches(i, str, 0, i)) {
                    found = true;
                    break;
                }
            }
        if (found)
            Assert.fail("Routes are found even though the process is killed!");
        stopAndDeleteEP(eventProcessManager, appName, appVersion);
     }

  /*  @Test(groups = "Bugs", dependsOnMethods = "TestRoutes", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "RouteNotRemoved_21910"));
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "RouteNotRemoved_21910"), e);
            // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "RouteNotRemoved_21910"), e);
            // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "RouteNotRemoved_21910"));
    }
    */
     @Test(enabled=false)
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
