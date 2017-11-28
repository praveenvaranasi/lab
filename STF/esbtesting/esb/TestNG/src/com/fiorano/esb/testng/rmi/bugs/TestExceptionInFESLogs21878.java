package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import com.fiorano.stf.test.core.TestNGTestCase;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: malarvizhi
 * Date: 14/8/12
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestExceptionInFESLogs21878 extends AbstractTestNG {

     private IEventProcessManager eventProcessManager;
     private IServiceProviderManager serviceProviderManager;
     private IFPSManager fpsManager;
     private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
     private String appName = "BUG21878";
     private String componentName="Feeder1";
     private SampleEventProcessListener epListener = null;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initDisplayLaunchSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
             Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        this.serviceProviderManager=rmiClient.getServiceProviderManager();
        if (serviceProviderManager == null) {
             Assert.fail("Cannot run Group Bugs as serviceProviderManager is not set.");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if (fpsManager == null) {
             Assert.fail("Cannot run Group Bugs as fpsManager is not set.");
        }
    }

    @Test(groups = "Bugs", dependsOnMethods = "initDisplayLaunchSetup", alwaysRun = true)
    public void testClearFESErrorLogs() {
        try {
            serviceProviderManager.clearFESErrLogs();
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearFESErrorLogs", "TestExceptionInFESLogs21878"), e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearFESErrorLogs", "TestExceptionInFESLogs21878"), e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearFESErrorLogs", "TestExceptionInFESLogs21878"), e);
        }

    }

    @Test(groups = "Bugs", description = "Exception in FES logs when delete the killed components - bug 21878", dependsOnMethods = "testClearFESErrorLogs", alwaysRun = true)
    public void testEventProcessLaunch() {

        Logger.LogMethodOrder(Logger.getOrderMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e1);
           Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("bug21878.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
             Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
             Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
             Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
              Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
              Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
              Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
//        try {
//            eventProcessManager.startAllServices(appName, appVersion);
//            try {
//                Thread.sleep(15000);
//            } catch (InterruptedException e) {
//            }
//            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "1");
//        } catch (RemoteException e) {
//            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
//            Assert.fail("Failed to do operation on service instance!", e);
//        } catch (ServiceException e) {
//            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
//             Assert.fail("Failed to do operation on service instance!", e);
//        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"));
    }

    @Test(groups = "Bugs", dependsOnMethods = "testEventProcessLaunch", alwaysRun = true)
    public void stopAndDeleteComponent() {
        try {
            eventProcessManager.stopServiceInstance(appName,appVersion,componentName) ;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        }  catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteComponent", "TestExceptionInFESLogs21878"), e);
             Assert.fail("Failed to do stop service Instance", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteComponent", "TestExceptionInFESLogs21878"), e);
              Assert.fail("Failed to do stop service Instance", e);
        }

        try {
            //delete a service by deploying deleted ep
            deployEventProcess("bug21878New.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEventProcessLaunch", "TestExceptionInFESLogs21878"), e);
            Assert.fail("Failed to do SAVE!", e);
        }
    }

      String Result;
      Writer output = null;
    @Test(groups = "Bugs", dependsOnMethods = "stopAndDeleteComponent", alwaysRun = true)
    public void viewFESErrorLogs() {
        try {
            Result=serviceProviderManager.getLastErrLogs(Integer.MAX_VALUE);
             Thread.sleep(10000);
        }
        catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("getLastErrLogs", "TestExceptionInFESLogs21878"), e);
                Assert.fail("Failed to view FES Error logs");
        }
        if(Result.isEmpty())
        {
           System.out.println("\n"+"FES ERROR LOG IS EMPTY Test passed :TestExceptionInFESLogs21878"+"\n");
        }
        else {
             System.out.println("Exception Thrown in FES Error Log :Test Failed"+"\n");
             TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
             String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
             String logDir = home + File.separator + TestEnvironmentConstants.REPORTS_DIR + File.separator
                        + TestNGTestCase.testNG_ReportsDir + File.separator + "logs";

             String file=logDir+"/FES_ErrorLogs_21878.txt";

             File outputFile = new File(file);
            try{
              if(!outputFile.exists())
              {
               outputFile.createNewFile();
              }
              output = new BufferedWriter(new FileWriter(outputFile));
              output.write(Result);
              output.close();
              System.out.println("FES Error LOG file Path:"+file);
              Assert.fail("Exception Thrown in FES Error Log");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

   @Test(groups = "Bugs", dependsOnMethods = "viewFESErrorLogs", alwaysRun = true)
    public void stopAndDeleteApplication() {
       Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestDisplayLaunch"));
       try {
           eventProcessManager.stopEventProcess(appName, appVersion);
           try {
               Thread.sleep(5000);
           } catch (InterruptedException e) {
           }
       } catch (RemoteException e) {
           Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestDisplayLaunch"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
       } catch (ServiceException e) {
           Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestDisplayLaunch"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
       }
       Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestDisplayLaunch"));
        try {
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
            Thread.sleep(5000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestDisplayLaunch"), e);
              Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestDisplayLaunch"), e);
              Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
       Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestDisplayLaunch"));
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
