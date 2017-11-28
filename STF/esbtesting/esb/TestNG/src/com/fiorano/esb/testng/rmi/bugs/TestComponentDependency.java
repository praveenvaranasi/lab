package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.server.api.IServiceManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.remote.RTLClient;
import com.fiorano.stf.test.core.TestEnvironment;
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
 * User: sagar
 * Date: Jan 25, 2011
 * Time: 5:07:11 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestComponentDependency extends AbstractTestNG {
    private ServerStatusController serverstatus;
    private TestEnvironment testenv;
    private IServiceManager serviceManager;
    private IEventProcessManager eventProcessManager;
    public IRmiManager rmiManager;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private String appName1 = "JMS_TEST";
    private float appVersion = 1.0f;
    @Test(groups = "ComponentDependency", description = "Cannot launch event process after editing a dependency of one of its components while it is running:Bug 19226", alwaysRun = true)
    public void startSetUp() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestComponentDependency"));
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            //Assert.fail("Cannot run Group getWSContexts. as eventProcessManager is not set.");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetUp", "TestComponentDependency"));
    }

    @Test(groups = "ComponentDependency", dependsOnMethods = "startSetUp", alwaysRun = true)
    public void testLaunchOfEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOEventProcess", "TestComponentDependency"));
        SampleEventProcessListener epListener1 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);

        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e1);
            //Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
            //Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
           // Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("jms_test-1.0.zip.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
            //Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
            //Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
            //Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
            //Assert.fail("Error in thread sleep", e);
        }

        //clear map
        eventStore1.clear();
        try {
            eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(10000);
           // Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "3");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOEventProcess", "TestComponentDependency"));
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestComponentDependency"), e);
            //Assert.fail("Error with thread sleep!", e);
        }


    }

    @Test(groups = "ComponentDependency", dependsOnMethods = "testLaunchOfEventProcess", alwaysRun = true)
    public void testComponentDependency() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testComponentDependency", "TestComponentDependency"));
            serviceManager = rmiClient.getServiceManager();
            serviceManager.exists("JMSAdapters", 4.0f);
            // boolean passed as true would stop JMSIn1 service instance as JMSIn1 depends on JMSAdapter
            serviceManager.delete("JMSAdapters", 4.0f, true);
            Thread.sleep(5000);
            String state = eventProcessManager.getApplicationStateDetails(appName1,appVersion).getServiceStatus("JMSIn1").getStatusString();
            //JMSIn1 service instance should not be running.
            Assert.assertTrue(state.equals("SERVICE_HANDLE_UNBOUND"));
            eventProcessManager.stopEventProcess(appName1, appVersion);
            Thread.sleep(5000);
            deployService("JMSAdapters.zip");
            Thread.sleep(5000);
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String fioranoHome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
            String path=fioranoHome+File.separator+"runtimedata" + File.separator+"repository"+File.separator+"components" +File.separator+"JMSAdapters_deleted";
            if(new File(path).exists()){
                new File(path).delete();
            }
             try{
            rmiClient.restartEnterpriseServer();
             }catch (Exception e)     {
                 //ignore
             }
            Thread.sleep(200000);
            rmiClient = RMIClient.getInstance();
            Thread.sleep(10000);
            rtlClient = RTLClient.getInstance();
            Thread.sleep(10000);
            jmxClient = JMXClient.getInstance();
            Thread.sleep(10000);
            eventProcessManager = rmiClient.getEventProcessManager();
            Thread.sleep(10000);
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            Thread.sleep(10000);
            eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(10000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testComponentDependency", "TestComponentDependency"));
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentDependency", "TestComponentDependency"), e);
            //Assert.fail("Failed in testComponentDependency", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentDependency", "TestComponentDependency"), e);
            //Assert.fail("Failed in testComponentDependency!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentDependency", "TestComponentDependency"), e);
           // Assert.fail("Error in thread sleep", e);
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testComponentDependency", "TestComponentDependency"), e);
           // Assert.fail("Error in thread sleep", e);
        }
    }


    @Test(groups = "ComponentDependency", dependsOnMethods = "testComponentDependency", alwaysRun = true)
    public void stopAndDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestComponentDependency"));
            eventProcessManager.stopEventProcess(appName1, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            Logger.Log(Level.INFO, Logger.getFinishMessage("stopAndDeleteApplication", "TestComponentDependency"));
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestComponentDependency"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestComponentDependency"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
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

    @Test(enabled = false)
    private void deployService(String zipName) throws IOException, ServiceException {
        boolean complete = false;
        BufferedInputStream bis = null;
        byte[] contents;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + "/esb/TestNG/resources/" + zipName));
        while (!complete) {
            byte[] tempContents = new byte[1024 * 40];
            int readCount = bis.read(tempContents);

            if (readCount < 0) {
                complete = true;
                readCount = 0;
            }
            contents = new byte[readCount];
            System.arraycopy(tempContents, 0, contents, 0, readCount);
            serviceManager.deployService(contents, complete);
        }
    }


}
