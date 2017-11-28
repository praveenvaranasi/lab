package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ServerStatusController;
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
 * Created with IntelliJ IDEA.
 * User: aravind
 * Date: 8/13/12
 * Time: 5:03 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestGracefullyKilledComp21667 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private String appName1 = "SMTP_SC";
    private ServerStatusController serverstatus;
    private IFPSManager fpsManager;
    EventProcessStateData eventProcessStateData;
    private float appVersion = 1.0f;
    @Test(groups = "RemoteServiceInstance", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestRemoteServiceInstance"));


        this.eventProcessManager = rmiClient.getEventProcessManager();

        fpsManager=rmiClient.getFpsManager();

        if (eventProcessManager == null) {
            // Assert.fail("Cannot run Group RemoteServiceInstance as eventProcessManager is not set.");

        }
        this.serverstatus = ServerStatusController.getInstance();

        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestRemoteServiceInstance"));
    }


    @Test(groups = "RemoteServiceInstance", description = "check remote-service-instance output port bug ::21388 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfApplication() throws ServiceException, RemoteException {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"));
        SampleEventProcessListener epListener1 = null;


        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);

        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e1);
            //  Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            //eventProcessManager.addEventProcessListener(epListener1, appName1);
            eventProcessManager.addEventProcessListener(epListener1);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            //  Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            // Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("nstd_smpt_disp_1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            //  Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            //  Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);

            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            // Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore1.clear();

        try {
            eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(1000);
            //Assert.assertEquals(eventStore2.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName2), "2");
            //  Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            // Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"), e);
            //  Assert.fail("Error with thread sleep!", e);
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfApplication", "TestRemoteServiceInstance"));
    }


    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "testLaunchOfApplication", alwaysRun = true)
    public void testGracefullyKilled()
    {
        Logger.LogMethodOrder(Logger.getOrderMessage("testGracefullyKilled", "TestRemoteServiceInstance"));

        try {
            eventProcessStateData=rmiClient.getEventProcessManager().getApplicationStateDetails(appName1,appVersion);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            Thread.sleep(10000);

        } catch (Exception e) {
            System.out.println(e);
        }

        ServiceStateData serviceStateData=eventProcessStateData.getServiceStatus("SMTP1");

  //      System.out.println(serviceStateData.getStatusString());

       // waiting till the component got killed
        while(serviceStateData.getStatusString().equals("SERVICE_HANDLE_BOUND"))  {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e);
            }
            try {//get status
                eventProcessStateData=rmiClient.getEventProcessManager().getApplicationStateDetails(appName1,appVersion);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            serviceStateData=eventProcessStateData.getServiceStatus("SMTP1");
        }
//        System.out.println(serviceStateData.getStatusString());

        try {
            System.out.println("restarting fps");
            fpsManager.restartPeer("fps");
            System.out.println("restarted fps");
        } catch (Exception e) {
            System.out.println("Error while restarting the fps"+e);
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        try {    //get status
            eventProcessStateData=rmiClient.getEventProcessManager().getApplicationStateDetails(appName1,appVersion);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        serviceStateData=eventProcessStateData.getServiceStatus("SMTP1");

        //System.out.println(serviceStateData.getStatusString());
        if(serviceStateData.getStatusString().equals("SERVICE_HANDLE_BOUND"))
        {
            System.out.println("Test failed ::: TestGracefullyKilledComp21667");
            Assert.fail("Component should not restarted after restarting the  peer server");
        }
        else
            System.out.println("Test paseed ::: TestGracefullyKilledComp21667");


        //eventProcessManager
/*        try {
            m_fioranoappcontroller.startService(appName1,"SMTP1");
        } catch (TifosiException e) {
            System.out.println("Problem with starting smtp");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        */
        Logger.Log(Level.INFO, Logger.getFinishMessage("testGracefullyKilled", "TestRemoteServiceInstance"));
    }


    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "testGracefullyKilled", alwaysRun = true)
    public void stopAndDeleteApplication() throws ServiceException, RemoteException {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestRemoteServiceInstance"));
        try {

            //eventProcessManager.stopEventProcess(appName1, appVersion);

            eventProcessManager.stopEventProcess(appName1, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // Assert.fail("Thread Exception ", e);
                Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestRemoteServiceInstance"), e);
            }
            //eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestRemoteServiceInstance"), e);
            //  Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestRemoteServiceInstance"), e);
            // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopAndDeleteApplication", "TestRemoteServiceInstance"));
    }


    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" + File.separator + "resources" + File.separator + zipName));
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