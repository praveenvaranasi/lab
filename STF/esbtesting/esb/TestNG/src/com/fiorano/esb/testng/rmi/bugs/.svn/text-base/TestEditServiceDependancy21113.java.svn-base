package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.RTLServiceEventListener;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.service.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.naming.NamingException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 9/20/11
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestEditServiceDependancy21113 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "TESTEDITSERVICEDEPENDANCY21113";
    private float appVersion = 1.0f;

    @Test(groups = "TestEditServiceDependancy21113", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group TestEditServiceDependancy21113. as eventProcessManager is not set.");
        }
    }

    TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
    String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
    File TestingHome = new File(home);

    @Test(groups = "TestEditServiceDependancy21113", description = "bug 21113", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestEditServiceDependancy21113() {


        Logger.LogMethodOrder(Logger.getOrderMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"));

        SampleEventProcessListener epListener = null;
        HashMap serverdetails = null;
        try {
            serverdetails = rmiClient.getServiceProviderManager().getServerDetails();
        } catch (RemoteException e) {
            junit.framework.Assert.fail("Fail to get server details");
        } catch (ServiceException e) {
            junit.framework.Assert.fail("Fail to get server details");
        }
        String url = (String) serverdetails.get("Server URL");

        RTLServiceEventListener rtlServiceEventListener = new RTLServiceEventListener(url);
        FioranoServiceRepository fsr = null;
        try {
            fsr = rtlServiceEventListener.startup();
        } catch (NamingException e) {
            org.testng.Assert.fail("NamingException while creating rtlServiceEventSubscriber");
        } catch (TifosiException e) {
            org.testng.Assert.fail("TifosiException while creating rtlServiceEventSubcriber");
        }

        Resource resource = new Resource();
        resource.setName("ojdbc14.jar");

        String resourceLocation = TestingHome.getAbsolutePath() + "/esb/TestNG/resources/ojdbc14.jar";
        try {
            fsr.addResource("jdbc", "4.0", resource, resourceLocation);
        } catch (TifosiException e) {
            junit.framework.Assert.fail("Fail to add resource to service");
        }

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("testeditservicedependancy21113-1.0@EnterpriseServer.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
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
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "1");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

        try {
            rmiClient.getFpsManager().restartPeer("fps");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to shutdown fps!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to shutdown fps!", e);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        try {
            fsr.removeResource("jdbc", "4.0", resource);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditServiceDependancy21113", "TestEditServiceDependancy21113"), e);
            Assert.fail("Failed to remove jdbc resource !", e);
        }
        testStopDeleteApplication();

    }

    @Test(enabled = false)
    public void testStopDeleteApplication() {
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            //Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            //Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testStopDeleteApplication", "ResourceFetch"));

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
