package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: anurag
 * Date: 2/23/12
 * Time: 2:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class StopApplicationWithBreakpt_Bug21600 extends AbstractTestNG{
    private IEventProcessManager eventProcessManager,eventProcessManager2;
    private IDebugger debugger;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "TEST_BUG21600";
    private TestEnvironmentConfig testEnvConfig;
    private SampleEventProcessListener epListener = null;
    private byte[] sentFile;
    private static IRmiManager rmiManager;
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "passwd";
    private static String rmiHandleID;
    private static String rmiHandleID2;
    private float appVersion = 1.0f;

       @Test(groups = "Bugs", alwaysRun = true)
         public void initSetup() {
           this.testEnvConfig = ESBTestHarness.getTestEnvConfig();


           rmiManager = rmiClient.getRmiManager();
        try {

            rmiHandleID = rmiManager.login(USERNAME, PASSWORD);
            rmiHandleID2 = rmiManager.login(USERNAME, PASSWORD);
            this.debugger=rmiManager.getBreakpointManager(rmiHandleID);

            this.eventProcessManager = rmiManager.getEventProcessManager(rmiHandleID);
            this.eventProcessManager2 = rmiManager.getEventProcessManager(rmiHandleID2);
            if (eventProcessManager == null) {
               Assert.fail("Cannot run Group Bugs. as eventProcessManager is not set.");
           }
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetUp", "TestBug21600"));

       }
    @Test(groups = "Bugs", description = "Problem with stopping applications having remote breakpoints", dependsOnMethods = "initSetup", alwaysRun = true)
        public void Test_Bug21600Launch() {
             Logger.LogMethodOrder(Logger.getOrderMessage("Test_Bug21600Launch", "Test_Bug21600"));
            Logger.Log(Level.SEVERE, Logger.getExecuteMessage("Test_Bug21600Launch", "Test_Bug21600"));


            try {
                epListener = new SampleEventProcessListener(appName, eventStore);
            } catch (RemoteException e1) {
               // e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e1);
                Assert.fail("Failed to create ep listener!", e1);
            }


         try {
                eventProcessManager.addEventProcessListener(epListener);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                Assert.fail("Failed to add ep listener!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                Assert.fail("Failed to do ep listener!", e);
            }

            try {
                try{// cleanup
                    eventProcessManager.stopEventProcess(appName, appVersion);
                    eventProcessManager.deleteEventProcess(appName, appVersion, true, true);
                } catch (Exception e){}
                deployEventProcess("Test_Bug21600.zip");

            } catch (IOException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                Assert.fail("Failed to do SAVE!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                Assert.fail("Failed to do SAVE!", e);
            }

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }

            try {
                eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                Assert.fail("Failed to do CRC!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                Assert.fail("Failed to do CRC!", e);
            }
            try {
                eventProcessManager.startEventProcess(appName, appVersion, false);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                Assert.fail("Failed to do start application!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                Assert.fail("Failed to do start application!", e);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            //clear map
            eventStore.clear();
            try {
                eventProcessManager.startAllServices(appName, appVersion);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                //Assert.fail("Failed to do operation on service instance!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_Bug21600Launch", "Test_Bug21600"), e);
                //Assert.fail("Failed to do operation on service instance!", e);
            }



         }

    @Test(groups = "Bug", description = "bug 21600 ", dependsOnMethods = "Test_Bug21600Launch", alwaysRun = true)
          public void TestSetBreakPoint() {
              Logger.LogMethodOrder(Logger.getOrderMessage("TestSetBreakPoint", "Test_Bug21600"));
              try {

                //  this.debugger=rmiManager.getBreakpointManager(rmiHandleID);
                  BreakpointMetaData bpmd1 = debugger.addBreakpoint(appName,appVersion, "route1");
                  Assert.assertNotNull(bpmd1.getSourceQueueName());

                  String[] route = debugger.getRoutesWithDebugger(appName,appVersion);
                  Assert.assertEquals(route.length, 1);
              } catch (RemoteException e) {
                  Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSetBreakPoint", "Test_Bug21600"), e);
                  Assert.fail("Failed to do operation on service instance!", e);
              } catch (ServiceException e) {
                  Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSetBreakPoint", "Test_Bug21600"), e);
                  Assert.fail("Failed to do operation on service instance!", e);
              }
              Logger.Log(Level.INFO, Logger.getFinishMessage("TestSetBreakPoint", "Test_Bug21600"));
          }

    @Test(groups = "Bugs", dependsOnMethods = "TestSetBreakPoint", alwaysRun = true)
       public void stopAndDeleteApplication() {
            Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TEST_BUG21600"));
            try {
                eventProcessManager2.stopEventProcess(appName, appVersion);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                eventProcessManager2.deleteEventProcess(appName, appVersion, true, false);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "Test_Bug21600"), e);
                Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "Test_Bug21600"), e);
                Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
            }
            Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "Test_Bug21600"));
    }

    @Test(groups = "Bugs", dependsOnMethods = "stopAndDeleteApplication", alwaysRun = true)
    public void ReStartApplication(){
        Test_Bug21600Launch();
        try {
                eventProcessManager.stopEventProcess(appName, appVersion);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("ReStartApplication", "Test_Bug21600"), e);
                Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("ReStartApplication", "Test_Bug21600"), e);
                Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
            }
           Logger.Log(Level.SEVERE, Logger.getFinishMessage("ReStartApplication", "Test_Bug21600"));


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

}


