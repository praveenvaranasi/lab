package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Sagar
 * Date: Nov 24, 2010
 * Time: 9:42:03 AM
 * To change this template use File | Settings | File Templates.
 */

public class TestGetWSContexts extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private FioranoApplicationController m_fioFioranoApplicationController;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private static Hashtable<String, String> eventStore2 = new Hashtable<String, String>();
    private String appName1 = "EVENT_PROCESS1";
    private String appName2 = "EVENT_PROCESS2";
    private float appVersion = 1.0f;
    @Test(groups = "getWSContexts", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestGetWSContexts"));
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            //Assert.fail("Cannot run Group getWSContexts. as eventProcessManager is not set.");
        }
        FioranoServiceProvider sp = null;
        try {
            sp = ServerStatusController.getInstance().getServiceProvider();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestGetWSContexts"), e);
           // Assert.fail("Failed to run test,since we could not getServiceProvider!", e);
        }

        FioranoApplicationController fac = sp.getApplicationController();
        m_fioFioranoApplicationController = fac;
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestGetWSContexts"));
    }

    @Test(groups = "getWSContexts", description = "getWSContexts method bug 19994 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfWSStubApplication() {


        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"));

        SampleEventProcessListener epListener1 = null;
        SampleEventProcessListener epListener2 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);
            epListener2 = new SampleEventProcessListener(appName2, eventStore2);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
            eventProcessManager.addEventProcessListener(epListener2);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
            //Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
           // Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("event_process1-1.0@EnterpriseServer.zip");
            deployEventProcess("event_process2-1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
            eventProcessManager.checkResourcesAndConnectivity(appName2, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
           // Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
          //  Assert.fail("Failed to do crc!", e);
        }
        try {
            //eventProcessManager.startEventProcess(appName1, appVersion, false);
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            Thread.sleep(10000);
            eventProcessManager.startEventProcess(appName2, appVersion, false);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
           // Assert.fail("Error in thread sleep", e);
        }

        //clear map
        eventStore1.clear();
        eventStore2.clear();
        try {
            eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(10000);

            eventProcessManager.startAllServices(appName2, appVersion);
            Thread.sleep(10000);

           // Assert.assertEquals(eventStore2.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName2), "2");
           // Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "1");


        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
          //  Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
           // Assert.fail("Error with thread sleep!", e);
        }

        String errStr1 = null;
        String errStr2 = null;
        try {
            errStr1 = eventProcessManager.getLastErrTrace(20, "WSStub1", "EVENT_PROCESS1", appVersion);
            errStr2 = eventProcessManager.getLastErrTrace(20, "WSStub1", "EVENT_PROCESS2", appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexs"), e);
           // Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfWSStubApplication", "TestGetWSContexts"), e);
          //  Assert.fail("Failed to do getLastErrTrace on service instance!", e);
        }

        if (errStr1.contains("Exception")) {
           // Assert.fail("Failed to start WSStubLaunching service instance an Exception occured!");
        }
        if (errStr2.contains("Exception")) {
           // Assert.fail("Failed to start WSStubLaunching service instance an Exception occured!");

        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOFWSStubApplication", "TestGetWSContexts"));

    }

    @Test(groups = "getWSContexts", dependsOnMethods = "testLaunchOfWSStubApplication", alwaysRun = true)
    public void testGetWSContexts() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetWSContexts", "TestGetWSContexts"));
        try {
            Map<String,List<String>> list1 = m_fioFioranoApplicationController.getWSContexts();
           // List list1 = m_fioFioranoApplicationController.getWSContexts();
            //List list1 = (List) m_fioFioranoApplicationController.getWSContexts();
            //System.out.println("list1=" + list1.toString());
           // Assert.assertNotNull(list1);
           // Assert.assertEquals(list1.size(), 2);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetWSContexts", "TestGetWSContexts"), e);
           // Assert.fail("not able to execute getWSContexts.", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testGetWSContexts", "TestGetWSContexts"));
    }

    @Test(groups = "getWSContexts", dependsOnMethods = "testGetWSContexts", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestGetWSContexts"));
        try {
            eventProcessManager.stopEventProcess(appName1, appVersion);
            eventProcessManager.stopEventProcess(appName2, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName2, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestGetWSContexts"), e);
            //Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestGetWSContexts"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopAndDeleteApplication", "TestGetWSContexts"));
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
