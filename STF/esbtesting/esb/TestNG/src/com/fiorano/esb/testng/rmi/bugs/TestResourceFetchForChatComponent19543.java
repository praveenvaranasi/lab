package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.RTLServiceEventListener;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.service.Resource;
import junit.framework.Assert;
import org.testng.annotations.Test;

import javax.naming.NamingException;
import java.io.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 1/18/11
 * Time: 6:06 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestResourceFetchForChatComponent19543 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "RESOURCEFETCHFORCHATBUG19543";
    private ServerStatusController serverStatusController;
    private FioranoApplicationController m_fioranoappcontroller;
    private Application application;
    List<ServiceInstance> serviceInstanceList;
    private float appVersion = 1.0f;
    @Test(groups = "ResourceFetch", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.serverStatusController = ServerStatusController.getInstance();
        try {
            this.m_fioranoappcontroller = serverStatusController.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "PortChangeForChatComponents"), e);
            org.testng.Assert.fail("Failed to create ep listener!", e);
        }
        if (eventProcessManager == null) {
            org.testng.Assert.fail("Cannot run Group Unexpected component shutdown. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "ResourceFetch", description = " Resource not fetched if its present in installation drive", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestResourceFetchForbug19543Comment1_1() {
        if (!System.getProperty("os.name").startsWith("Windows")) {
            Logger.LogMethodOrder(Logger.getOrderMessage("TestResourceFetchForbug19543Comment1_1", "ResourceFetch"));
            SampleEventProcessListener epListener = null;
            try {
                epListener = new SampleEventProcessListener(appGUID, eventStore);
                eventProcessManager.addEventProcessListener(epListener);
                deployEventProcess("resourcefetchforchatbug19543-1.0@EnterpriseServer.zip");
                Thread.sleep(5000);
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
                HashMap serverdetails = null;
                try {
                    serverdetails = rmiClient.getServiceProviderManager().getServerDetails();
                } catch (RemoteException e) {
                    Assert.fail("Fail to get server details");
                } catch (ServiceException e) {
                    Assert.fail("Fail to get server details");
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
                resource.setName("jms.jar");
                //resource.
                //resource.
                String resourceLocation = TestingHome.getAbsolutePath() + "/esb/TestNG/resources/jms.jar";
                //TODO: resource to be added here
                try {
                    fsr.addResource("jms", "4.0", resource, resourceLocation);
                } catch (TifosiException e) {
                    Assert.fail("Fail to add resource to service");
                }
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
                String jmsjarfilepath = fiorano_home.getAbsolutePath() + "/runtimedata/PeerServers/profile1/FPS/run/components/jms/4.0/jms.jar";
                File jmsjarfile = new File(jmsjarfilepath);
                long firstlycreatedjartime = 0l;
                if (jmsjarfile.exists()) {
                    firstlycreatedjartime = jmsjarfile.lastModified();
                } else {
                    Assert.fail("Fail to fetch jms.jar resource");
                }
                try {
                    application = m_fioranoappcontroller.getApplication(appGUID, appVersion);
                    serviceInstanceList = application.getServiceInstances();
                    if (serviceInstanceList.get(0).isComponentCached()) {
                        serviceInstanceList.get(0).setComponentCached(false);
                    } else {
                        serviceInstanceList.get(0).setComponentCached(true);
                    }
                    if (serviceInstanceList.get(1).isComponentCached()) {
                        serviceInstanceList.get(1).setComponentCached(false);
                    } else {
                        serviceInstanceList.get(1).setComponentCached(true);
                    }
                    application.setServiceInstances(serviceInstanceList);
                    m_fioranoappcontroller.saveApplication(application);
                } catch (TifosiException e) {
                    Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetchForbug19543Comment1_1", "ResourceFetch"), e);
                    org.testng.Assert.fail("Failed to to get service information!", e);
                }

                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
                long lastlycreatedjartime = 0l;
                if (jmsjarfile.exists()) {
                    lastlycreatedjartime = jmsjarfile.lastModified();
                } else {
                    Assert.fail("Fail to fetch jms.jar resource");
                }
                /*if (firstlycreatedjartime != 0l && lastlycreatedjartime != 0l && firstlycreatedjartime == lastlycreatedjartime) {
                    Assert.fail("fail to fetch resource");
                }*/
                fsr.removeResource("jms", "4.0", resource);
                //testStopDeleteApplication();

            } catch (Exception e) {
                Assert.fail("Fail to launch the ResourceFetch ep");
            }
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestResourceFetchForbug19543Comment1_1", "ResourceFetch"));

    }

    @Test(groups = "ResourceFetch", description = " Resource not fetched if its present in installation drive", dependsOnMethods = "TestResourceFetchForbug19543Comment1_1", alwaysRun = true)
    public void TestResourceFetchForbug19543Comment1_2() {
        if (!System.getProperty("os.name").startsWith("Windows")) {
            Logger.LogMethodOrder(Logger.getOrderMessage("TestResourceFetchForbug19543Comment1_2", "ResourceFetch"));
            SampleEventProcessListener epListener = null;
            try {
/*                //appGUID = "RESOURCEFETCHFORCHATCOMPONENT4BUG19543";
                eventStore.clear();
                epListener = new SampleEventProcessListener(appGUID, eventStore);
                eventProcessManager.addEventProcessListener(epListener, appGUID);
                deployEventProcess("resourcefetchforchatcomponent4bug19543-1.0@EnterpriseServer.zip");
                Thread.sleep(5000);*/
                String resourcepath = fiorano_home.getAbsolutePath() + "/runtimedata/PeerServers/profile1/FPS/run/components";
                String cmd[] = new String[3];
                cmd[0] = "/bin/sh";
                cmd[1] = "-c";
                cmd[2] = "find " + resourcepath + " -name temp |xargs rm -rf ";
                Runtime.getRuntime().exec(cmd);

                if (application.isComponentCached()) {
                    application.setComponentCached(false);
                } else {
                    application.setComponentCached(true);
                }
                m_fioranoappcontroller.saveApplication(application);
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
                //testStopDeleteApplication();
                /*appGUID = "RESOURCEFETCHFORCHATCOMPONENT3BUG19543";
                deployEventProcess("resourcefetchforchatcomponent3bug19543-1.0@EnterpriseServer.zip");
                Thread.sleep(5000);*/
                if (application.isComponentCached()) {
                    application.setComponentCached(false);
                } else {
                    application.setComponentCached(true);
                }
                serviceInstanceList.get(0).setComponentCached(true);
                serviceInstanceList.get(0).setComponentCached(true);
                application.setServiceInstances(serviceInstanceList);
                m_fioranoappcontroller.saveApplication(application);
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
                testStopDeleteApplication();

                File resourcepathfile = new File(resourcepath);
                Process process = Runtime.getRuntime().exec("find ./ -name temp", null, resourcepathfile);
                InputStreamReader isr = new InputStreamReader(process.getInputStream());
                BufferedReader br = new BufferedReader(isr);
                if (br.readLine() != null) {
                    Assert.fail("Failed as resource directory contains temp folders");
                }
            } catch (Exception e) {
                Assert.fail("Fail to launch the ResourceFetch ep");
            }
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestResourceFetchForbug19543Comment1_2", "ResourceFetch"));

    }

    TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
    String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
    String fioranohome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
    File TestingHome = new File(home);
    File fiorano_home = new File(fioranohome);

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
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

    @Test(groups = "ResourceFetch", description = " Resource not fetched if its present in installation drive", enabled = false)
    public void testStopDeleteApplication() {
        try {
            /*eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }*/
            eventProcessManager.deleteEventProcess(appGUID, appVersion, true, false);
        } catch (RemoteException e) {
            //Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            //Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testStopDeleteApplication", "ResourceFetch"));

    }


}
