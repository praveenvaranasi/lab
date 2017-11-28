package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.ServiceException;
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
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
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
 * User: srikanth
 * Date: Jan 24, 2011
 * Time: 11:00:11 AM
 * To change this template use File | Settings | File Templates.
 */

public class TestComponentReLaunch extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsmanager;
    private ServerStatusController serverstatus;
    private TestEnvironmentConfig testenvconfig;
    private TestEnvironment testenv;
    private ExecutionController executioncontroller;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private FioranoApplicationController m_fioranoappcontroller;
    private String appGUID = "COMPONENTRELAUNCH";
    private String Instance1 = "chat1";
    private String Instance2 = "chat2";
    private float appVersion = 1.0f;
    @Test(groups = "ComponentReLaunch", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
           // Assert.fail("Cannot run Group ComponentReLaunch. as eventProcessManager is not set.");
        }
        this.fpsmanager = rmiClient.getFpsManager();
        this.serverstatus = ServerStatusController.getInstance();
        this.testenv = serverstatus.getTestEnvironment();
        this.executioncontroller = ExecutionController.getInstance();
        try {
            this.m_fioranoappcontroller = serverstatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestComponentReLaunch"), e);
           // Assert.fail("Cannot run Group ComponentReLaunch. as fioranoapplicationcontroller is not set.", e);
        }
        testenvconfig = ESBTestHarness.getTestEnvConfig();
    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestStartFPS1() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStartFPS1", "TestComponentReLaunch"));
        try {
            if (!fpsmanager.isPeerRunning("fps1")) {
                try {
                    if (!fpsmanager.isPeerRunning("fps1")) {
                        startServer(testenv, testenvconfig, "fps1", SERVER_TYPE.FPS);
                    }
                } catch (RemoteException e) {
                    Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartFPS1", "TestComponentReLaunch"), e);
                   // Assert.fail("Failed to start FPS1", e);
                } catch (ServiceException e) {
                    Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartFPS1", "TestComponentReLaunch"), e);
                   // Assert.fail("Failed to start FPS1", e);
                }
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartFPS1", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to start FPS1", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartFPS1", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to start FPS1", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStartFPS1", "TestComponentReLaunch"));
    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestStartFPS1", alwaysRun = true)
    public void TestShutdownFPS1() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestShutdownFPS1", "TestComponentReLaunch"));
        try {
            fpsmanager.shutdownPeer("fps1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestShutdownFPS1", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to shutdown fps1", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestShutdownFPS1", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to shutdown fps1", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestShutdownFPS1", "TestComponentReLaunch"));
    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestShutdownFPS1", alwaysRun = true)
    public void TestLaunchOfEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"));
        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("COMPONENTRELAUNCH.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }


        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appGUID, appVersion);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "2");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfEventProcess", "TestComponentReLaunch"));

    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestLaunchOfEventProcess", alwaysRun = true)
    public void TestStopComponents() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopComponents", "TestComponentReLaunch"));
        try {
            eventProcessManager.stopServiceInstance(appGUID,appVersion, Instance1);
            eventProcessManager.stopServiceInstance(appGUID,appVersion, Instance2);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopComponents", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to stop service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopComponents", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to stop service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopComponents", "TestComponentReLaunch"));

    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestStopComponents", alwaysRun = true)
    public void TestChangeNodesTofps1ansSave() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestChangeNodesTofps1ansSave", "TestComponentReLaunch"));
        String[] nodes = {"fps1"};
        try {
            Application application = m_fioranoappcontroller.getApplication(appGUID, appVersion);
            application.getServiceInstance(Instance1).setNodes(nodes);
            application.getServiceInstance(Instance2).setNodes(nodes);

            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangeNodesTofps1ansSave", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to change component node fps to fps1", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestChangeNodesTofps1ansSave", "TestComponentReLaunch"));

    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestChangeNodesTofps1ansSave", alwaysRun = true)
    public void TestStartallServices() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStartallServices", "TestComponentReLaunch"));
        try {
            eventProcessManager.startServiceInstance(appGUID,appVersion, Instance1);
           // Assert.fail("Failed to stop service instance!");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartallServices", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to stop service instance!", e);
        } catch (ServiceException e) {
            //Test Passed Successfully
        }
        try {
            eventProcessManager.startServiceInstance(appGUID,appVersion, Instance2);
           // Assert.fail("Failed to stop service instance!");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartallServices", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to stop service instance!", e);
        } catch (ServiceException e) {
            //Test Passed Successfully
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStartallServices", "TestComponentReLaunch"));

    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestStartallServices", alwaysRun = true)
    public void TestChangeNodesTofpsandSave() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestChangeNodesTofpsansSave", "TestComponentReLaunch"));
        String[] nodes = {"fps"};
        try {
            Application application = m_fioranoappcontroller.getApplication(appGUID, appVersion);
            application.getServiceInstance(Instance1).setNodes(nodes);
            application.getServiceInstance(Instance2).setNodes(nodes);
            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangeNodesTofpsansSave", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to change component node fps1 to fps", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestChangeNodesTofpsansSave", "TestComponentReLaunch"));

    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestChangeNodesTofpsandSave", alwaysRun = true)
    public void TestStopEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopEventProcess", "TestComponentReLaunch"));
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopEventProcess", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to do stop ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do stop ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopEventProcess", "TestComponentReLaunch"));

    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestStopEventProcess", alwaysRun = true)
    public void Test2ndLaunchOfEventProcess() {


        Logger.LogMethodOrder(Logger.getOrderMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"));

        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appGUID, appVersion);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appGUID), "2");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"), e);
          //  Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("Test2ndLaunchOfEventProcess", "TestComponentReLaunch"));
    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "Test2ndLaunchOfEventProcess", alwaysRun = true)
    public void Test2ndStopComponents() {
        Logger.LogMethodOrder(Logger.getOrderMessage("Test2ndStopComponents", "TestComponentReLaunch"));
        try {
            eventProcessManager.stopServiceInstance(appGUID,appVersion, Instance1);
            eventProcessManager.stopServiceInstance(appGUID,appVersion, Instance2);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndStopComponents", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to stop service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndStopComponents", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to stop service instance!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("Test2ndStopComponents", "TestComponentReLaunch"));

    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "Test2ndStopComponents", alwaysRun = true)
    public void Test2ndChangeNodesTofps1ansSave() {
        Logger.LogMethodOrder(Logger.getOrderMessage("Test2ndChangeNodesTofps1ansSave", "TestComponentReLaunch"));
        String[] nodes = {"fps1"};
        try {
            Application application = m_fioranoappcontroller.getApplication(appGUID, appVersion);
            application.getServiceInstance(Instance1).setNodes(nodes);
            application.getServiceInstance(Instance2).setNodes(nodes);
            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndChangeNodesTofps1ansSave", "TestComponentReLaunch"), e);
            //Assert.fail("Failed to change component node fps to fps1", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("Test2ndChangeNodesTofps1ansSave", "TestComponentReLaunch"));

    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "Test2ndChangeNodesTofps1ansSave", alwaysRun = true)
    public void TestStopEP() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStopEP", "TestComponentReLaunch"));
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopEP", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do stop ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStopEP", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do stop ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStopEP", "TestComponentReLaunch"));

    }


    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestStopEP", alwaysRun = true)
    public void Test2ndStartallServices() {
        Logger.LogMethodOrder(Logger.getOrderMessage("Test2ndStartallServices", "TestComponentReLaunch"));
        try {
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
           // Assert.fail("EP is starting even if Peer is not running");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test2ndStartallServices", "TestComponentReLaunch"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            //Test Passed Successfully
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("Test2ndStartallServices", "TestComponentReLaunch"));

    }

    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "Test2ndStartallServices", alwaysRun = true)
    public void TestDeleteEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDeleteEventProcess", "TestComponentReLaunch"));
        stopAndDeleteEP(eventProcessManager,appGUID, appVersion);
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestDeleteEventProcess", "TestComponentReLaunch"));

    }
    @Test(groups = "ComponentReLaunch", description = "bug 19424 ", dependsOnMethods = "TestDeleteEventProcess", alwaysRun = true)
    public void testCleanUp() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testCleanUp", "TestSpaceInFioranoHome"));
        startServer(testenv, testenvconfig, "fps1", SERVER_TYPE.FPS);
        Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanUp", "testSpaceInFioranoHome"));
    }

    /**
     * deploys an event process which is located under $testing_home/esb/TestNG/resources/
     *
     * @param zipName - name of the event process zip located under $testing_home/esb/TestNG/resources/
     * @throws java.io.IOException - if file is not found or for any other IO error
     * @throws ServiceException
     */
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
