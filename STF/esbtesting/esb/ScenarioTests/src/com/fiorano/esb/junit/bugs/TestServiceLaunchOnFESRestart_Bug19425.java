package com.fiorano.esb.junit.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.dmi.application.*;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: lokesh2
 * Date: Jan 18, 2011
 * Time: 7:34:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceLaunchOnFESRestart_Bug19425 extends DRTTestCase {

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

    public TestServiceLaunchOnFESRestart_Bug19425(String testcaseName) {
        super(testcaseName);
        proxy = ExecutionController.getInstance();
    }

    public TestServiceLaunchOnFESRestart_Bug19425(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
        proxy = ExecutionController.getInstance();
    }

    public void setUp() throws Exception {
        super.setUp();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        m_fioranoFPSManager = rtlClient.getFioranoFPSManager();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "bugs"
                + File.separator + "TestServiceLaunchOnFESRestart_Bug19425";

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

    }

    public void tearDown() throws Exception {
        m_fioranoApplicationController.killApplication(m_appGUID,m_version);
        Thread.sleep(3000);
        if (m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
            throw new Exception("The Application is still running.");
    }

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile", "1.0.xml");
        m_chatInstanceOne = testCaseProperties.getProperty("ChatInstanceOne");
        m_chatInstanceTwo = testCaseProperties.getProperty("ChatInstanceTwo");
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

    public static Test suite() {
        return new TestSuite(TestServiceLaunchOnFESRestart_Bug19425.class);
    }

    /**
     * ************************** Tests *****************************************
     */

    public void testPeerOneKill() {
        String peerURL, peerAddress = null;
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            peerURL = m_fioranoFPSManager.getConnectURLForFPS(peerOne);
            peerAddress = peerURL.substring(peerURL.lastIndexOf("//") + 2, peerURL.lastIndexOf(":"));
            m_fioranoFPSManager.shutdownTPS(peerOne);
            Thread.sleep(10000);

            String chat1Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_chatInstanceOne).getStatusString();
            String chat2Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_chatInstanceTwo).getStatusString();

            if (chat1Status.equals(chat2Status))
                Assert.assertTrue(true);
            else Assert.assertTrue("Chat 1 is not up, its status is :" + chat1Status, false);

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
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

    public void testFESAndPeerOneKill() {
        String peerURL, peerAddress = null;
        String fesURL, fesAddress = null;
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testFESAndPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            peerURL = m_fioranoFPSManager.getConnectURLForFPS(peerOne);
            peerAddress = peerURL.substring(peerURL.lastIndexOf("//") + 2, peerURL.lastIndexOf(":"));
            fesURL = ServerStatusController.getInstance().getURLOnFESActive(); //tsp_tcp
            fesAddress = fesURL.substring(fesURL.lastIndexOf("//") + 2, fesURL.lastIndexOf(":"));

            rtlClient.shutdownEnterpriseServer();
            proxy.stopFPS(peerAddress, false);            
            Thread.sleep(5000);

            proxy.startServerOnRemote(fesAddress, "fes", "profile1", false, false);
            Thread.sleep(10000);

            this.suite.updateRemoteClients();
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            m_fioranoFPSManager = rtlClient.getFioranoFPSManager();

            String chat1Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_chatInstanceOne).getStatusString();
            String chat2Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_chatInstanceTwo).getStatusString();

            if (chat1Status.equals(chat2Status))
                Assert.assertTrue(true);
            else Assert.assertTrue("Chat 1 is not up, its status is :" + chat1Status, false);

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testFESAndPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testFESAndPeerOneKill", "TestServiceLaunchOnFESRestart_Bug19425"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
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


    public void testFESAndPeerTwoKill() {
        String peerURL, peerAddress = null;
        String fesURL, fesAddress = null;
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testFESAndPeerTwoKill", "TestServiceLaunchOnFESRestart_Bug19425"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            peerURL = m_fioranoFPSManager.getConnectURLForFPS(peerTwo);
            peerAddress = peerURL.substring(peerURL.lastIndexOf("//") + 2, peerURL.lastIndexOf(":"));
            fesURL = ServerStatusController.getInstance().getURLOnFESActive();
            fesAddress = fesURL.substring(fesURL.lastIndexOf("//") + 2, fesURL.lastIndexOf(":")); //tsp_tcp

            rtlClient.shutdownEnterpriseServer();
            proxy.shutdownServerOnRemote(peerAddress, "fps", "profile2", false, false);
            Thread.sleep(5000);

            proxy.startServerOnRemote(fesAddress, "fes", "profile1", false, false);
            Thread.sleep(10000);

            this.suite.updateRemoteClients();
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            m_fioranoFPSManager = rtlClient.getFioranoFPSManager();

            String chat1Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_chatInstanceOne).getStatusString();
            String chat2Status = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_chatInstanceTwo).getStatusString();

            if (chat1Status.equals(chat2Status))
                Assert.assertTrue(true);
            else Assert.assertTrue("Chat 2 is not up, its status is :" + chat2Status, false);

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testFESAndPeerTwoKill", "TestServiceLaunchOnFESRestart_Bug19425"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testFESAndPeerTwoKill", "TestServiceLaunchOnFESRestart_Bug19425"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
        finally {
            proxy.startServerOnRemote(peerAddress, "fps", "profile2", false, false);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

/***************************** End Of Tests ******************************************/


    /**
     * ************************** Auxiliary Methods *****************************************
     */

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


    /***************************** End Of Auxiliary Methods ******************************************/

}
