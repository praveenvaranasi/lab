package com.fiorano.esb.testng.rmi.bugs;

//import com.fiorano.esb.junit.scenario.sanity.LaunchTypeNoneTest;
import com.fiorano.esb.server.api.BreakpointMetaData;
import com.fiorano.esb.server.api.IDebugger;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
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
 * User: anurag
 * Date: 2/21/12
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestChangeDebuggerLaunchType21394 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IDebugger debugger;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "EVENT_PROCESS3";
    private String appName1 = "EVENT_PROCESS1";
    private TestEnvironmentConfig testEnvConfig;
    private SampleEventProcessListener epListener = null;
    private byte[] sentFile;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
      public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.debugger=rmiClient.getDebugger();
        this.testEnvConfig = ESBTestHarness.getTestEnvConfig();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Bugs. as eventProcessManager is not set.");
        }
    }


    @Test(groups = "Bugs", description = "Exception thrown while setting debugger in none launch type case - bug 21394 ", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestEvent_Process3Launch() {
         Logger.LogMethodOrder(Logger.getOrderMessage("TestEvent_Process3Launch", "TestEvent_Process3"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestEvent_Process5Launch", "TestEvent_Process5"));


        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
           // e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }


     try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("Bug_21394_3.zip");

            deployEventProcess("Bug_21394_4.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            Assert.fail("Failed to do CRC!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
       // eventStore.clear();
        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        }
     }

    @Test(groups = "Bug", description = "bug 21394 ", dependsOnMethods = "TestEvent_Process3Launch", alwaysRun = true)
       public void TestSetBreakPoint() {
           Logger.LogMethodOrder(Logger.getOrderMessage("TestEvent_Process3Launch", "TestEvent_Process3"));
           try {
               BreakpointMetaData bpmd1 = debugger.addBreakpoint(appName,appVersion, "route1");
               Assert.assertNotNull(bpmd1.getSourceQueueName());


               String[] route = debugger.getRoutesWithDebugger(appName,appVersion);
               Assert.assertEquals(route.length, 1);
           } catch (RemoteException e) {
               Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
               Assert.fail("Failed to do operation on service instance!", e);
           } catch (ServiceException e) {
               Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
               Assert.fail("Failed to do operation on service instance!", e);
           }
           Logger.Log(Level.INFO, Logger.getFinishMessage("TestEvent_Process3Launch", "TestEvent_Process3"));
       }


@Test(groups = "Bugs", dependsOnMethods = "TestSetBreakPoint", alwaysRun = true)
       public void stopAndDeleteApplication() {
            Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestEvent_Process3"));
        try{ //stop chat2
            eventProcessManager.stopServiceInstance(appName1, appVersion, "chat2");
            try{
                Thread.sleep(4000);
            }catch (InterruptedException e){}
        }catch (RemoteException e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopComponent", "TestEvent_Process3"), e);
            Assert.fail("Failed to do stop/delete service instance", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopComponent", "TestEvent_Process3"), e);
            Assert.fail("Failed to do stop/delete service instance ", e);
        }
            try{//change launch type to none , by deploying new ep
                deployEventProcess("Bug_21394_1.zip");
                deployEventProcess("Bug_21394_2.zip");
            } catch (IOException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
                Assert.fail("Failed to do SAVE!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEvent_Process3Launch", "TestEvent_Process3"), e);
                Assert.fail("Failed to do SAVE!", e);
            }

            stopAndDeleteEP(eventProcessManager, appName, appVersion);
            stopAndDeleteEP(eventProcessManager, appName1, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

            Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestEvent_Process3"));
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



