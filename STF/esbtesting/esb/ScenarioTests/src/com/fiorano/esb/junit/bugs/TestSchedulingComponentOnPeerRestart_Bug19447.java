package com.fiorano.esb.junit.bugs;

import com.fiorano.stf.jms.STFQueueSender;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.TestCaseElement;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.*;
import fiorano.tifosi.dmi.aps.*;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: lokesh2
 * Date: Jan 20, 2011
 * Time: 12:02:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSchedulingComponentOnPeerRestart_Bug19447 extends DRTTestCase {

    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private String m_appFile;
    private float m_version;
    private String m_smtpInstance;
    private String m_pop3Instance;
    private String smtpQueue;

    public TestSchedulingComponentOnPeerRestart_Bug19447(String testcaseName) {
        super(testcaseName);
    }

    public TestSchedulingComponentOnPeerRestart_Bug19447(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {
        super.setUp();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "bugs"
                + File.separator + "TestSchedulingComponentOnPeerRestart_Bug19447";

        init();

        Application application = ApplicationParser.readApplication(new File(m_appFile));
        m_fioranoApplicationController.saveApplication(application);
        if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
            throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
        startApplication(m_appGUID, m_version);
        Thread.sleep(10000);
        if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
            throw new Exception("The Application is not started.");
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
        m_smtpInstance = testCaseProperties.getProperty("SMTPInstance");
        m_pop3Instance = testCaseProperties.getProperty("POP3Instance");
        smtpQueue = m_appGUID + "__" + m_smtpInstance + "__IN_PORT";
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
        return new TestSuite(TestSchedulingComponentOnPeerRestart_Bug19447.class);
    }


    /**
     * ************************** Tests *****************************************
     */

    //TODO: Add test to forcefully kill some components
    public void testSchedulingComponent() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSchedulingComponent", "TestSchedulingComponentOnPeerRestart_Bug19447"));

            // SMTP dies
            STFQueueSender STFQueueSender = new STFQueueSender();
            STFQueueSender.initialize(smtpQueue, null, getURL());
            STFQueueSender.send("Hi", 1);
            Thread.sleep(2000);
            STFQueueSender.cleanup();

            rtlClient.getFioranoFPSManager().restartTPS(getPeerName(m_smtpInstance));
            Thread.sleep(30000);
            ServiceInstanceStateDetails smtpDetails = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_smtpInstance);
            String statusSmtp = smtpDetails.getStatusString();
            System.out.println("$$$ smtp status :" + statusSmtp + " $$$");
            ServiceInstanceStateDetails pop3Details = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_pop3Instance);
            String statusPop3 = pop3Details.getStatusString();
            System.out.println("$$$ pop3 status :" + statusPop3 + " $$$");

            if (statusSmtp.equals(statusPop3) && statusPop3.equals("SERVICE_HANDLE_CREATED"))
                Assert.assertTrue(true);
            else Assert.assertTrue("Components are not retaining their states", false);

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSchedulingComponent", "TestSchedulingComponentOnPeerRestart_Bug19447"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSchedulingComponent", "TestSchedulingComponentOnPeerRestart_Bug19447"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
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


    private String getURL() throws Exception {
        try {
            String fpsName = getPeerName(m_smtpInstance);
            String url;
            if ((fpsName.equals("fps") || fpsName.equals("fps1"))) {
                url = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
                if (url.endsWith("2010"))
                    url = "http://0.0.0.0:1877";
            } else {
                ArrayList urls = new ArrayList(2);//string followed by int.
                String s = ServerStatusController.getInstance().getURLOnFESActive();
                String rtlPort = s.substring(s.lastIndexOf(":") + 1);
                urls.add(s.substring(s.lastIndexOf("//") + 2, s.lastIndexOf(":")));
                if (rtlPort.equals("1947")) {
                    urls.add(2047);
                } else
                    urls.add(2048);

                int port = 1847;
                if (urls.get(1).equals("2047")) {
                    port = 1847;
                }
                url = "http://" + urls.get(0) + ":" + port;
            }
            return url;
        } catch (STFException e) {
            throw new Exception(e);
        }
    }

    private String getPeerName(String serviceName) throws Exception {
        fiorano.tifosi.dmi.application.ServiceInstance smtpInstance = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstance(serviceName);
        String fpsName = smtpInstance.getNodes()[0];
        return fpsName;
    }

    /***************************** End Of Auxiliary Methods ******************************************/


}
