package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.RTLClient;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.ServiceInstance;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/10/11
 * Time: 5:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceLaunchOnFESRestart extends AbstractTestNG{

    private FioranoApplicationController m_fioranoApplicationController;
    private FioranoFPSManager m_fioranoFPSManager;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String peerOne;
    private String peerTwo;
    private String m_chatInstanceOne; //fps,fps1
    private String m_chatInstanceTwo; //fps1,fps
    private String m_appFile;
    private ExecutionController proxy;
    private float appVersion = 1.0f;
    private TestEnvironmentConfig testenvconfig;
    private TestEnvironment testenv;
    //private STFTestSuite suite;

    @BeforeClass(groups = "ServiceLaunchAfterFESRestart", description = "Bug 19425")
    public void startSetUp(){
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "TestServiceLaunchOnFESRestart_Bug19425"));
        initializeProperties("bugs" + fsc + "TestServiceLaunchOnFESRestart_Bug19425" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc +"tests" +  fsc + "bugs" + fsc + "TestServiceLaunchOnFESRestart_Bug19425";
        rtlClient = RTLClient.getInstance();
        proxy = ExecutionController.getInstance();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        m_fioranoFPSManager = rtlClient.getFioranoFPSManager();
        testenv = ServerStatusController.getInstance().getTestEnvironment();
        testenvconfig = ESBTestHarness.getTestEnvConfig();

    }

    @Test(groups = "ServiceLaunchAfterFESRestart", dependsOnMethods = "startSetUp", alwaysRun = true, description = "Bug 19425")
    public void testPeerOneKill() {
        String peerURL, peerAddress = null;
        try {
            init();
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            startApplication(m_appGUID, m_version);
            Thread.sleep(10000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            List<ServiceInstance> instances = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstances();
            for (ServiceInstance instance : instances) {
                if (instance.isPreferLaunchOnFirstAvailableNode())
                    throw new Exception("Application with GUID::" + m_appGUID + "has PreferLaunchOnFirstNode set to true");
            }


            Logger.LogMethodOrder(Logger.getOrderMessage("testPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            peerURL = m_fioranoFPSManager.getConnectURLForFPS(peerOne);
            peerAddress = peerURL.substring(peerURL.lastIndexOf("//") + 2, peerURL.lastIndexOf(":"));
            m_fioranoFPSManager.shutdownTPS(peerOne);
            Thread.sleep(25000);

            String chat1Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,appVersion, m_chatInstanceOne).getStatusString();
            String chat2Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID, appVersion,m_chatInstanceTwo).getStatusString();

            if (chat1Status.equals(chat2Status))
                Assert.assertTrue(true);
            else Assert.assertTrue(false, "Chat 1 is not up, its status is :" + chat1Status);

            Logger.Log(Level.INFO, Logger.getFinishMessage("testPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
        finally {
            proxy.startServerOnRemote(peerAddress, "fps", "profile1", false, false);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test(groups = "ServiceLaunchAfterFESRestart", dependsOnMethods = "testPeerOneKill", alwaysRun = true, description = "Bug 19425")
    public void testFESAndPeerOneKill() {
        String peerURL, peerAddress = null;
        String fesURL, fesAddress = null;
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testFESAndPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            peerURL = m_fioranoFPSManager.getConnectURLForFPS(peerOne);
            peerAddress = peerURL.substring(peerURL.lastIndexOf("//") + 2, peerURL.lastIndexOf(":"));
            fesURL = ServerStatusController.getInstance().getURLOnFESActive(); //tsp_tcp
            fesAddress = fesURL.substring(fesURL.lastIndexOf("//") + 2, fesURL.lastIndexOf(":"));

            rtlClient.shutdownEnterpriseServer();
            proxy.stopFPS(peerAddress, false);
            Thread.sleep(10000);

            startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);

            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            m_fioranoFPSManager = rtlClient.getFioranoFPSManager();

            String chat1Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,appVersion, m_chatInstanceOne).getStatusString();
            String chat2Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID, appVersion,m_chatInstanceTwo).getStatusString();

            if (chat1Status.equals(chat2Status))
                Assert.assertTrue(true);
            else Assert.assertTrue(false, "Chat 1 is not up, its status is :" + chat1Status);

            Logger.Log(Level.INFO, Logger.getFinishMessage("testFESAndPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testFESAndPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
        finally {
            proxy.startServerOnRemote(peerAddress, "fps", "profile1", false, false);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Test(groups = "ServiceLaunchAfterFESRestart", dependsOnMethods = "testFESAndPeerOneKill", alwaysRun = true, description = "Bug 19425")
    public void testFESAndPeerTwoKill() {
        String peerURL, peerAddress = null;
        String fesURL, fesAddress = null;
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testFESAndPeerTwoKill", "TestServiceLaunchOnFESRestart_Bug19425"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            peerURL = m_fioranoFPSManager.getConnectURLForFPS(peerTwo);
            peerAddress = peerURL.substring(peerURL.lastIndexOf("//") + 2, peerURL.lastIndexOf(":"));
            fesURL = ServerStatusController.getInstance().getURLOnFESActive();
            fesAddress = fesURL.substring(fesURL.lastIndexOf("//") + 2, fesURL.lastIndexOf(":")); //tsp_tcp

            rtlClient.shutdownEnterpriseServer();
            proxy.shutdownServerOnRemote(peerAddress, "fps1", "profile2", false, false);


            startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);

            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            m_fioranoFPSManager = rtlClient.getFioranoFPSManager();

            String chat1Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,appVersion, m_chatInstanceOne).getStatusString();
            String chat2Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,appVersion, m_chatInstanceTwo).getStatusString();

            if (chat1Status.equals(chat2Status))
                Assert.assertTrue(true);
            else Assert.assertTrue(false, "Chat 2 is not up, its status is :" + chat2Status);

            Logger.Log(Level.INFO, Logger.getFinishMessage("testFESAndPeerTwoKill", "TestServiceLaunchOnFESRestart_Bug19425"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testFESAndPeerTwoKill", "TestServiceLaunchOnFESRestart_Bug19425"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
        finally {
            proxy.startServerOnRemote(peerAddress, "fps1", "profile2", false, false);
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @AfterClass(groups = "ServiceLaunchAfterFESRestart", alwaysRun = true, description = "Bug 19425")
    public void tearDown() throws Exception {
        m_fioranoApplicationController.killApplication(m_appGUID,appVersion);
        m_fioranoApplicationController.deleteApplication(m_appGUID,appVersion);
        Thread.sleep(10000);
        if (m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
            throw new Exception("The Application is still running.");
    }

    /*Auxiliary functions*/

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_chatInstanceOne = testProperties.getProperty("ChatInstanceOne");
        m_chatInstanceTwo = testProperties.getProperty("ChatInstanceTwo");
        peerOne = "fps";
        peerTwo = "fps1";
        printInitParams();
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID:: " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("The Application File is::" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);
            Thread.sleep(10000);
        }
        catch (Exception e) {
            System.err.println("Error launching the application");
            e.printStackTrace();
            throw new FioranoException("Error launching the application");
        }
    }

}
