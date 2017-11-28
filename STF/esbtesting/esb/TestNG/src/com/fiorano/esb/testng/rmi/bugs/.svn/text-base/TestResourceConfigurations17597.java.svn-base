package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.RTLServiceEventListener;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.*;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.service.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.naming.NamingException;
import java.io.File;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 1/28/11
 * Time: 3:33 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestResourceConfigurations17597 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "SIMPLECHAT";
    private ServerStatusController serverstatus;
    private FioranoServiceProvider fsp;
    private TestEnvironment testenv;
    private float appVersion = 1.0f;
    TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
    String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
    String fioranohome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
    File TestingHome = new File(home);
    File fiorano_home = new File(fioranohome);

    @Test(groups = "ResouceConfiguration", alwaysRun = true)
    public void startSetup() {
        try {
            this.eventProcessManager = rmiClient.getEventProcessManager();
            serverstatus = ServerStatusController.getInstance();
            fsp = serverstatus.getServiceProvider();
            testenv = serverstatus.getTestEnvironment();
            testEnvConfig = ESBTestHarness.getTestEnvConfig();
            if (eventProcessManager == null) {
                Assert.fail("Cannot run Group Unexpected component shutdown. as eventProcessManager is not set.");
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestResourceConfiguration17597"), e);
            Assert.fail("fail to get the eventprocess manager", e);
        }
    }

    @Test(groups = "ResouceConfiguration", description = "bug17597 CRC fails when resource that is not marked as required for configuration", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testResourceConfiguration() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testResourceConfiguration", "TestResourceConfiguration17597"));
        SampleEventProcessListener epListener = null;
        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
            eventProcessManager.addEventProcessListener(epListener);
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
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testResourceConfiguration", "TestResourceConfiguration17597"), e);
                org.testng.Assert.fail("NamingException while creating rtlServiceEventSubscriber");
            } catch (TifosiException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testResourceConfiguration", "TestResourceConfiguration17597"), e);
                org.testng.Assert.fail("TifosiException while creating rtlServiceEventSubcriber");
            }
            Resource resource = new Resource();
            resource.setName("jms.jar");
            //System.out.println("configuration" +resource.isRequiredForConfiguration() + "execution" +resource.isRequiredForExecution());
            resource.setRequiredForConfiguration(false);
            resource.setRequiredForExecution(false);
            String resourceLocation = TestingHome.getAbsolutePath() + "/esb/TestNG/resources/jms.jar";
            //TODO: resource to be added here
            try {
                fsr.addResource("jms", "4.0", resource, resourceLocation);
            } catch (TifosiException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testResourceConfiguration", "TestResourceConfiguration17597"), e);
                junit.framework.Assert.fail("Fail to add resource to service");
            }
            //new JMXClient.shutdownEnterpriseServer();
            //rmiClient.getFpsManager().shutdownPeer();

            /* try {
                fsp.getFPSManager().shutdownTPS("fps");
                fsp.shutdownTES();
                Thread.sleep(10000);
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testResourceConfiguration", "TestResourceConfiguration17597"), e);
                junit.framework.Assert.fail("Fail to shutdown the servers service");
            }*/
            String resource_path = fiorano_home.getAbsolutePath() + "/runtimedata/repository/components/jms/4.0/jms.jar";
            File jmsresourcefile = new File(resource_path);
            if (jmsresourcefile.exists()) {
                jmsresourcefile.delete();
            }
            //testStartServers();
            try {
                Thread.sleep(2000);
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testResourceConfiguration", "TestResourceConfiguration17597"), e);
                Assert.fail("Failed to do crc! test case fails as it checking for resource", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testResourceConfiguration", "TestResourceConfiguration17597"), e);
                Assert.fail("Failed to do crc!", e);
            }

            fsr.removeResource("jms", "4.0", resource);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testResourceConfiguration", "TestResourceConfiguration17597"), e);
            Assert.fail("failed to add event listener", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testResourceConfiguration", "TestResourceConfiguration17597"));

    }

    @Test(groups = "ResouceConfiguration", enabled = false)
    public void testStartServers() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStartServers", "TestResourceConfiguration17597"));
        startServer(testenv, testEnvConfig, "fes", SERVER_TYPE.FES);
        startServer(testenv, testEnvConfig, "fps", SERVER_TYPE.FPS);
        Logger.Log(Level.INFO, Logger.getFinishMessage("testStartServers", "TestResourceConfiguration17597"));
    }
}

