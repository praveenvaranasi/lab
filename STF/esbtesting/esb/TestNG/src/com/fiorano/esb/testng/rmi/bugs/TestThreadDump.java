package com.fiorano.esb.testng.rmi.bugs;

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

import java.io.*;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: janardhan
 * Date: Nov 25, 2010
 * Time: 5:09:04 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestThreadDump extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "THREADDUMP";
    private float appVersion = 1.0f;

    @Test(groups = "ThreadDump", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group ThreadDump. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "ThreadDump", description = "ThreadDump bug 19916 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfThreadDumpApplication() {


        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"));

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do ep listener!", e);
        }
        // testEnvConfig = ESBTestHarness.getTestEnvConfig();
        // String currentos=testEnvConfig.getProperty(TestEnvironmentConstants.MACHINE);
        try {
            String osname = System.getProperty("os.name");
            if (osname.startsWith("Windows")) {
                throw new Exception("testcase written  only for linux machine not for winodws so failed");
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("testcase written  only for linux machine not for winodws so failed", e);
            return;
        }


        try {
            deployEventProcess("threaddump-1.0@EnterpriseServer.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
            ;
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appName, 1.0f);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

        /* String errStr = null;
        try {
            errStr = eventProcessManager.getLastErrTrace(20, "WSStub1"  ,"EVENT_PROCESS1" , 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLanunchOfWSStubApplication", "TestWSStubLaunching"), e);
            Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        }

        if (errStr.contains("Exception")) {
            Assert.fail("Failed to start WSStubLaunching service instance an Exception occured!");
        }*/
        /* try {
           // eventProcessManager.getEventProcessIds();

        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }*/

        Process p = null;
        try {
            p = Runtime.getRuntime().exec("jps");

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line = null;
            String a[] = null;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                a = line.split(" ");
                if (a.length > 1 && a[1].equals("ChatService")) {
                    System.out.println("Thread dump on " + a[1]);
                    Runtime.getRuntime().exec("kill -3 " + a[0]);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String outStr = null;
        try {
            outStr = eventProcessManager.getLastOutTrace(200, "chat1", appName, 1.0f);
            // System.out.println(outStr);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do getLastOutTrace on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do getLastOutTrace on service instance!", e);
        }

        if (!outStr.contains("VM Periodic Task Thread")) {
            Assert.fail("Failed to do ThreadDump on chat service an Exception occured!");
        }

    }

    @Test(groups = "ThreadDump", dependsOnMethods = "TestLaunchOfThreadDumpApplication", alwaysRun = true)
    public void stopAndDeleteApplication() {
        try {
            eventProcessManager.stopEventProcess(appName, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfThreadDumpApplication", "TestThreadDump"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + "/esb/TestNG/resources/" + zipName));
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


