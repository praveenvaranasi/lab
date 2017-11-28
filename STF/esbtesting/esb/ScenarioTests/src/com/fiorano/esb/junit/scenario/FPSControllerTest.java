package com.fiorano.esb.junit.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.ServicesRemovalStatus;
import fiorano.tifosi.dmi.TraceConfiguration;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.events.SystemEventInfo;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Nagesh V H
 * Date: Aug 7, 2008
 * Time: 2:40:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class FPSControllerTest extends DRTTestCase {


    private FioranoFPSManager m_fioranoFPSManager;
    private FioranoApplicationController m_fioranoApplicationController;


    private String resourceFilePath;

    private String m_appGUID;
    private float m_version;

    private String m_appFile;
    private String m_servinstance1;
    private String m_servinstance2;
    boolean isModifiedOnceHA = false;//to check the files overwriting
    boolean isModifiedOnce = false;
    private FilenameFilter xmlFilter;//file extracter
    private String FPSPEER = "fps";

    public FPSControllerTest(String name) {
        super(name);
    }


    public FPSControllerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }


    private void setXmlFilter(String ext)//picking up the xml extension files
    {
        xmlFilter = new OnlyExt(ext);//creating filters

    }


    // the function modifies the xml files and replace any "search" string with "replace" string

    void modifyXmlFiles(String path, String search, String replace) throws IOException {
        File fl = new File(path);
        File[] filearr = fl.listFiles(xmlFilter);
        FileReader fr = null;
        FileWriter fw = null;
        boolean change = false;
        BufferedReader br;
        String s;
        String result = "";


        int i = 0;
        while (i < filearr.length - 1) {
            try {
                fr = new FileReader(filearr[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fw = new FileWriter("temp.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            br = null;
            br = new BufferedReader(fr);


            while ((s = br.readLine()) != null) {

                int j = s.indexOf(search);
                if (j != -1) {
                    change = true;
                    int k = search.length() + j;
                    //System.out.println("The Index start is "+j+" and index last is "+ k);
                    result = s.substring(0, j);
                    result = result + replace;
                    result = result + s.substring(k);
                    s = result;

                }
                //System.out.print(s);

                fw.write(s);
                fw.write('\n');
            }
            fr.close();
            fw.close();

            if (change) {
                fr = new FileReader("temp.xml");
                fw = new FileWriter(filearr[i]);
                br = new BufferedReader(fr);
                while ((s = br.readLine()) != null) {
                    fw.write(s);
                    fw.write('\n');
                }
                fr.close();
                fw.close();
                change = false;
            }

            i = i + 1;

        }
    }
    //changed function


    public void setUp() throws Exception {
        super.setUp();
        m_fioranoFPSManager = rtlClient.getFioranoFPSManager();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "FPSController";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();

        //new change
        ServerStatusController stc;
        stc = ServerStatusController.getInstance();
        boolean isFPSHA = stc.getFPSHA();
        setXmlFilter("xml");
        if (isFPSHA && !isModifiedOnceHA) {
            isModifiedOnceHA = true;

            modifyXmlFiles(resourceFilePath, "fps", "fps_ha");

        } else if (!isFPSHA && !isModifiedOnce) {
            isModifiedOnce = true;
            modifyXmlFiles(resourceFilePath, "fps_ha", "fps");

        }

        init();
    }

    private void init() throws Exception {
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile");

        m_servinstance1 = testCaseProperties.getProperty("ServiceInstance1");
        m_servinstance2 = testCaseProperties.getProperty("ServiceInstance2");
        printInitParams();
    }

    private void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("FPS Name:: " + FPSPEER);
        System.out.println("...........................................................................");
    }


    public static Test suite() {
        return new TestSuite(FPSControllerTest.class);
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    public void teststartApplication() throws Exception {

        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("teststartApplication", "FPSControllerTest"));
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);
            m_fioranoApplicationController.launchApplication(m_appGUID, m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("teststartApplication", "FPSControllerTest"));
        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("teststartApplication", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

    }


    public void testClearTPSErrLogs() throws Exception {
        try {

            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearTpsErrLogs", "FPSControlllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String TestingHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            String location = TestingHome + File.separator + TestEnvironmentConstants.REPORTS_DIR + File.separator + DRTTestCase.DRTTest_Logs
                    + File.separator + "FPSlogs.zip";
            m_fioranoFPSManager.exportFPSLogs(FPSPEER, location);
            String dir = TestingHome + File.separator + TestEnvironmentConstants.REPORTS_DIR + File.separator + DRTTestCase.DRTTest_Logs;
            String location1 = TestingHome + File.separator + TestEnvironmentConstants.REPORTS_DIR + File.separator + DRTTestCase.DRTTest_Logs
                    + File.separator + "FESlogs.zip";
            rtlClient.getFioranoServiceProvider().exportFESLogs(location1, dir);
            Thread.sleep(10000);
            m_fioranoFPSManager.clearTPSErrLogs(FPSPEER);
            Assert.assertNull(m_fioranoFPSManager.getTPSLastErrLogs(1, FPSPEER));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearTpsErrLogs", "FPSControlllerTest"));


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearTpsErrLogs", "FPSControlllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

    }

    public void testClearTPSOutLogs() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearTPSOutLogs", "FPSControllerTest"));

            int a, b;
            a = m_fioranoFPSManager.getTPSLastOutLogs(100000, FPSPEER).length();

            m_fioranoFPSManager.clearTPSOutLogs(FPSPEER);
            b = m_fioranoFPSManager.getTPSLastOutLogs(100000, FPSPEER).length();
            //Assert.assertNull(m_fioranoFPSManager.getTPSLastOutLogs(1, FPSPEER));
            if (b > a) {
                throw new Exception();
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearTPSOutLogs", "FPSControllerTest"));


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearTPSOutLogs", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

    }


    public void testGetTPSAlertModules() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetTPSAlertModules", "FPSControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Vector vec = m_fioranoFPSManager.getTPSAlertModules(FPSPEER);
            Assert.assertNotNull(vec);
            for (int i = 0; i < vec.size(); i++) {
                SystemEventInfo sei = (SystemEventInfo) vec.elementAt(i);
                String[] list = sei.getAlertHandlers();
                for (int j = 0; j < list.length; j++) {
                    System.out.println(list[j]);
                }

            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetTPSAlertModules", "FPSControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetTPSAlertModules", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testSetTPSAlertModules() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetTPSAlertModules", "FPSControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Vector vec = m_fioranoFPSManager.getTPSAlertModules(FPSPEER);
            m_fioranoFPSManager.setTPSAlertModules(FPSPEER, vec);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetTPSAlertModules", "FPSControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetTPSAlertModules", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testGetTPSTraceConfiguration() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetTPSTraceConfiguration", "FPSControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            TraceConfiguration tc = m_fioranoFPSManager.getTPSTraceConfiguration(FPSPEER);
            Enumeration en = tc.getTraceValues();
            while (en.hasMoreElements()) {
                System.out.println(en.getClass());
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetTPSTraceConfiguration", "FPSControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetTPSTraceConfiguration", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testSetTPSTraceConfiguration() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetTPSTraceConfiguration", "FPSControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            TraceConfiguration tc = m_fioranoFPSManager.getTPSTraceConfiguration(FPSPEER);
            m_fioranoFPSManager.setTPSTraceConfiguration(FPSPEER, tc);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetTPSTraceConfiguration", "FPSControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetTPSTraceConfiguration", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testRemoveAllInstalledServices() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveAllInstalledServices", "FPSControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            ServicesRemovalStatus sr = m_fioranoFPSManager.removeAllInstalledServices(FPSPEER, true);
            Assert.assertTrue(sr != null);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveAllInstalledServices", "FPSControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveAllInstalledServices", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testRemoveInstalledService() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveInstalledService", "FPSControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String s = m_fioranoFPSManager.removeInstalledService(FPSPEER, "chat", "4.0", true);
            Assert.assertTrue(ServicesRemovalStatus.SERVICE_SUCCESSFULLY_REMOVED.equalsIgnoreCase(s) ||
                    ServicesRemovalStatus.SERVICE_NOT_PRESENT.equalsIgnoreCase(s));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveInstalledService", "FPSControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveInstalledService", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testRebootFPS() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRebootFPS", "FPSControllerTest"));
            Assert.assertTrue(m_fioranoFPSManager.isTPSRunning(FPSPEER));
            m_fioranoFPSManager.restartTPS(FPSPEER);
            System.out.println("Rebooting FPS..");
            Thread.sleep(10000);

            while (!m_fioranoFPSManager.isTPSRunning(FPSPEER)) {
                Thread.sleep(10000);
                System.out.println("Still rebooting...");
            }

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRebootFPS", "FPSControllerTest"));
        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRebootFPS", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

    }

    public void testKillFPS() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testkillFPS", "FPSControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Assert.assertTrue(m_fioranoFPSManager.isTPSRunning(FPSPEER));
            m_fioranoFPSManager.shutdownTPS(FPSPEER);
            System.out.println("Shutting down FPS..");
            Thread.sleep(10000);
            Assert.assertFalse(m_fioranoFPSManager.isTPSRunning(FPSPEER));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillFPS", "FPSControllerTest"));


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillFPS", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

    }

    public void testCleanUp() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCleanUp", "FPSControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            m_fioranoApplicationController.close();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCleanUp", "FPSControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCleanUp", "FPSControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

    }


    public static ArrayList getOrder() {
        ArrayList methodsOrder = new ArrayList();
        methodsOrder.add("teststartApplication");
        methodsOrder.add("testClearTPSErrLogs");
        methodsOrder.add("testClearTPSOutLogs");
        methodsOrder.add("testGetTPSAlertModules");
        methodsOrder.add("testGetTPSTraceConfiguration");
        methodsOrder.add("testSetTPSTraceConfiguration");
        methodsOrder.add("testRemoveAllInstalledServices");
        methodsOrder.add("testRemoveInstalledService");
        methodsOrder.add("testRebootFPS");
        methodsOrder.add("testCleanUp");


        return methodsOrder;
    }


}