package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import junit.framework.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/*
*
 * Created by IntelliJ IDEA.
 * User: aravind
 * Date: AUG 8, 2012
 * Time: 6:18:10 PM
 * To change this template use File | Settings | File Templates.
*/
// Automated test case for the bug 21905

public class TestExtraMQLogs extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private String appName1 = "EVENT_PROCESS1";
    private IServiceProviderManager ispm;
    private float appVersion = 1.0f;
    @Test(groups = "RemoteServiceInstance", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestExtraMQLogs"));
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            // Assert.fail("Cannot run Group RemoteServiceInstance as eventProcessManager is not set.");

        }
        ispm=rmiClient.getServiceProviderManager();
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestExtraMQLogs"));
    }


    @Test(groups = "RemoteServiceInstance", description = "check remote-service-instance output port bug ::21388 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfApplication() throws ServiceException, RemoteException {
        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOfApplication", "TestExtraMQLogs"));
        SampleEventProcessListener epListener1 = null;
        SampleEventProcessListener epListener2 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfApplication", "TestExtraMQLogs"), e1);
            //  Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfApplication", "TestExtraMQLogs"), e);
            //  Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
            // Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("chat_single_1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
            //  Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
            //  Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
            // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
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
            Thread.sleep(10000);

            //Assert.assertEquals(eventStore2.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName2), "2");
            //  Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
            // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
            // Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfApplication", "TestExtraMQLogs"), e);
            //  Assert.fail("Error with thread sleep!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestLaunchOfApplication", "TestExtraMQLogs"));
    }

    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "testLaunchOfApplication", alwaysRun = true)
    public void stopApplication() {

        Logger.LogMethodOrder(Logger.getOrderMessage("stopApplication", "TestExtraMQLogs"));
        try {

            //eventProcessManager.stopEventProcess(appName1, appVersion);
            eventProcessManager.stopEventProcess(appName1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopApplication", "TestExtraMQLogs"), e);
            //  Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopApplication", "TestExtraMQLogs"), e);
            // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopApplication", "TestExtraMQLogs"));
    }

    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "stopApplication", alwaysRun = true)
    public void testExtraLogs()
    {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestExtraLogs", "TestExtraMQLogs"));
        String service_name="chat1";
        String component_logs_out=null,component_logs_err=null,errorLog=null,out_log=null;
        try {
            //adapters logs have appeared in the component logs
            component_logs_out=this.eventProcessManager.getLastOutTrace(-1,service_name,appName1,appVersion);
            component_logs_err=eventProcessManager.getLastErrTrace(-1,service_name,appName1,appVersion);
            //errorLog=ispm.getLastErrLogs(-1);
            //out_log=ispm.getLastOutLogs(-1);

        } catch (Exception e) {
            System.out.println(e);
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestExtraLogs", "TestExtraMQLogs"), e);
            //e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        //if(logs_out.contains("Looking up this object name:: ")||logs_err.contains("Looking up this object name:: ")|| errorLog.contains("Looking up this object name:: ")|| out_log.contains("Looking up this object name:: "))
        if(component_logs_out.contains("Looking up this object name:: ")||component_logs_err.contains("Looking up this object name:: "))
        {
            System.out.println("Test failed :: TestExtraMQLogs");
            Assert.fail("Extra logging info coming in adapter logs 21905");
        }
        else
            System.out.println("Test passed :: TestExtraMQLogs");
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestExtraLogs", "TestExtraMQLogs"));
    }

    @Test(groups = "RemoteServiceInstance", dependsOnMethods = "testExtraLogs", alwaysRun = true)
    public void deleteApplication() {

        Logger.LogMethodOrder(Logger.getOrderMessage("deleteApplication", "TestExtraMQLogs"));
        try {

            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteApplication", "TestExtraMQLogs"), e);
            //  Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deleteApplication", "TestExtraMQLogs"), e);
            // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("deleteApplication", "TestExtraMQLogs"));
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