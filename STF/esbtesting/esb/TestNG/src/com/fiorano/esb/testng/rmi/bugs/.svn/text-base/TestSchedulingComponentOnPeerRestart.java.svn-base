package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.jms.STFQueueSender;
import com.fiorano.stf.remote.RTLClient;
import com.fiorano.stf.test.core.TestCaseElement;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/10/11
 * Time: 5:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSchedulingComponentOnPeerRestart extends AbstractTestNG{

    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private String m_appFile;
    private float m_version;
    private String m_smtpInstance;
    private String m_pop3Instance;
    private String smtpQueue;
    private float appVersion = 1.0f;
    @BeforeClass(groups = "schedulingComponentOnPeerRestart", description = "Bug 19447", alwaysRun = true)
    public void startSetUp(){

        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "ResourceFetchForCacheEnabled"));
        initializeProperties("bugs" + fsc + "TestSchedulingComponentOnPeerRestart_Bug19447" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc +"tests" +  fsc + "bugs" + fsc + "TestSchedulingComponentOnPeerRestart_Bug19447";
        rtlClient = RTLClient.getInstance();

    }

    @Test(groups = "schedulingComponentOnPeerRestart", description = "Bug 19447", alwaysRun = true, dependsOnMethods = "startSetup")
    public void testSchedulingComponent() {
            try {
                init();

                m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
                Application application = ApplicationParser.readApplication(new File(m_appFile));
                m_fioranoApplicationController.saveApplication(application);
                if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                    throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
                Logger.LogMethodOrder(Logger.getOrderMessage("testSchedulingComponent", "TestSchedulingComponentOnPeerRestart_Bug19447"));
                startApplication(m_appGUID, m_version);
                Thread.sleep(10000);
                if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                    throw new Exception("The Application is not started.");
                // SMTP dies
                STFQueueSender STFQueueSender = new STFQueueSender();
                STFQueueSender.initialize(smtpQueue, null, getURL());
                STFQueueSender.send("Hi", 1);
                Thread.sleep(2000);
                STFQueueSender.cleanup();
                Thread.sleep(3000);

                ServiceInstanceStateDetails pop3Details = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,appVersion, m_pop3Instance);
                String statusPop3 = pop3Details.getStatusString();
                System.out.println("$$$ before restart pop3 status :" + statusPop3 + " $$$");
                rtlClient.getFioranoFPSManager().restartTPS(getPeerName(m_smtpInstance));
                Thread.sleep(30000);
                ServiceInstanceStateDetails smtpDetails = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,appVersion, m_smtpInstance);
                String statusSmtp = smtpDetails.getStatusString();
                int i=0;
                while (i++ <=10 && !statusSmtp.equals("SERVICE_HANDLE_CREATED")) {
                    smtpDetails = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,appVersion, m_smtpInstance);
                    statusSmtp = smtpDetails.getStatusString();
                    Thread.sleep(30000);
                }
                System.out.println("$$$ smtp status :" + statusSmtp + " $$$");
                pop3Details = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,appVersion, m_pop3Instance);
                statusPop3 = pop3Details.getStatusString();
                System.out.println("$$$ pop3 status :" + statusPop3 + " $$$");

                if (statusSmtp.equals(statusPop3) && statusPop3.equals("SERVICE_HANDLE_CREATED"))
                    Assert.assertTrue(true);
                else Assert.assertTrue(false, "Components are not retaining their states");

                Logger.Log(Level.INFO, Logger.getFinishMessage("testSchedulingComponent", "TestSchedulingComponentOnPeerRestart_Bug19447"));
            }
            catch (Exception ex) {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testSchedulingComponent", "TestSchedulingComponentOnPeerRestart_Bug19447"), ex);
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }

        }

    @AfterClass(groups = "schedulingComponentOnPeerRestart", description = "Bug 19447", alwaysRun = true)
    public void tearDown() throws Exception {
        m_fioranoApplicationController.killApplication(m_appGUID,appVersion);
        Thread.sleep(3000);
        if (m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
            throw new Exception("The Application is still running.");
    }


    /***************************** End Of Tests ******************************************/


    /**
     * ************************** Auxiliary Methods *****************************************
     */
    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_smtpInstance = testProperties.getProperty("SMTPInstance");
        m_pop3Instance = testProperties.getProperty("POP3Instance");
        smtpQueue = m_appGUID  + "__" + "1_0" + "__" + m_smtpInstance + "__IN_PORT";
        printInitParams();
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID:: " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("The Application File is::" + m_appFile);
        System.out.println("_____________________________________________________________________________");
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
