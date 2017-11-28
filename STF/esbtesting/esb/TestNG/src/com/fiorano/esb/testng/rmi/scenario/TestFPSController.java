package com.fiorano.esb.testng.rmi.scenario;


import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.*;
import fiorano.tifosi.dmi.ServicesRemovalStatus;
import fiorano.tifosi.dmi.TraceConfiguration;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.events.SystemEventInfo;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Enumeration;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;

/**
* Created by IntelliJ IDEA.
* User: ranu
* Date: 10/20/11
* Time: 11:14 AM
* To change this template use File | Settings | File Templates.
*/
public class TestFPSController extends AbstractTestNG {
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
    
    private void init() throws Exception {
            m_appGUID = testProperties.getProperty("ApplicationGUID");
            m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
            m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile");
            m_servinstance1 = testProperties.getProperty("ServiceInstance1");
            m_servinstance2 = testProperties.getProperty("ServiceInstance2");
        }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Service Instances::      " + m_servinstance1 + " and " + m_servinstance2);
        System.out.println("FPS Name:: " + FPSPEER);
        System.out.println("..................................................................");
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

    //picking up the xml extension files
    private void setXmlFilter(String ext){
        xmlFilter = new OnlyExt(ext);//creating filters
    }
                           
    @BeforeClass(groups = "FPSControllerTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "FPSController" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "FPSController";
        m_fioranoFPSManager = rtlClient.getFioranoFPSManager();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        //new change
        ServerStatusController stc;
        stc = ServerStatusController.getInstance();
        boolean isFPSHA = stc.getFPSHA();
        setXmlFilter("xml");
        try {
            if (isFPSHA && !isModifiedOnceHA) {
                isModifiedOnceHA = true;
                modifyXmlFiles(resourceFilePath, "fps", "fps_ha");
            }
            else if (!isFPSHA && !isModifiedOnce) {
                isModifiedOnce = true;
                modifyXmlFiles(resourceFilePath, "fps_ha", "fps");
            }
        init();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            Assert.fail("Could not initialise properties");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testStartApplication() throws Exception {

        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Logger.LogMethodOrder(Logger.getOrderMessage("teststartApplication", "TestFPSController"));
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);
            m_fioranoApplicationController.launchApplication(m_appGUID, m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("teststartApplication", "TestFPSController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("teststartApplication", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }

    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testStartApplication")
    public void testClearTPSErrLogs() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearTpsErrLogs", "TestFPSController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String TestingHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            String dir = TestingHome + fsc + TestEnvironmentConstants.REPORTS_DIR + fsc + TestNGTestCase.testNG_ReportsDir + fsc + "logs";
            String location = dir + fsc + "FPSlogs.zip";
            m_fioranoFPSManager.exportFPSLogs(FPSPEER, location);
            String location1 = dir + fsc + "FESlogs.zip";
            rtlClient.getFioranoServiceProvider().exportFESLogs(location1, dir);
            Thread.sleep(10000);
            m_fioranoFPSManager.clearTPSErrLogs(FPSPEER);
            Assert.assertNull(m_fioranoFPSManager.getTPSLastErrLogs(1, FPSPEER));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearTpsErrLogs", "TestFPSController"));

        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearTpsErrLogs", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }

    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testClearTPSErrLogs")
    public void testClearTPSOutLogs() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearTPSOutLogs", "TestFPSController"));

            int a, b;
            a = m_fioranoFPSManager.getTPSLastOutLogs(100000, FPSPEER).length();

            m_fioranoFPSManager.clearTPSOutLogs(FPSPEER);
            b = m_fioranoFPSManager.getTPSLastOutLogs(100000, FPSPEER).length();
            //Assert.assertNull(m_fioranoFPSManager.getTPSLastOutLogs(1, FPSPEER));
            if (b > a) {
                throw new Exception();
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearTPSOutLogs", "TestFPSController"));


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearTPSOutLogs", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }

    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testClearTPSOutLogs")
    public void testGetTPSAlertModules() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetTPSAlertModules", "TestFPSController"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetTPSAlertModules", "TestFPSController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTPSAlertModules", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testGetTPSAlertModules")
    public void testSetTPSAlertModules() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetTPSAlertModules", "TestFPSController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Vector vec = m_fioranoFPSManager.getTPSAlertModules(FPSPEER);
            m_fioranoFPSManager.setTPSAlertModules(FPSPEER, vec);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetTPSAlertModules", "TestFPSController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetTPSAlertModules", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testSetTPSAlertModules")
    public void testGetTPSTraceConfiguration() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetTPSTraceConfiguration", "TestFPSController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            TraceConfiguration tc = m_fioranoFPSManager.getTPSTraceConfiguration(FPSPEER);
            Enumeration en = tc.getTraceValues();
            while (en.hasMoreElements()) {
                System.out.println(en.getClass());
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetTPSTraceConfiguration", "TestFPSController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTPSTraceConfiguration", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testGetTPSTraceConfiguration")
    public void testSetTPSTraceConfiguration() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetTPSTraceConfiguration", "TestFPSController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            TraceConfiguration tc = m_fioranoFPSManager.getTPSTraceConfiguration(FPSPEER);
            m_fioranoFPSManager.setTPSTraceConfiguration(FPSPEER, tc);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetTPSTraceConfiguration", "TestFPSController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetTPSTraceConfiguration", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testSetTPSTraceConfiguration")
    public void testRemoveAllInstalledServices() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveAllInstalledServices", "TestFPSController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            ServicesRemovalStatus sr = m_fioranoFPSManager.removeAllInstalledServices(FPSPEER, true);
            Assert.assertTrue(sr != null);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveAllInstalledServices", "TestFPSController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveAllInstalledServices", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testRemoveAllInstalledServices")
    public void testRemoveInstalledService() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveInstalledService", "TestFPSController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String s = m_fioranoFPSManager.removeInstalledService(FPSPEER, "chat", "4.0", true);
            Assert.assertTrue(ServicesRemovalStatus.SERVICE_SUCCESSFULLY_REMOVED.equalsIgnoreCase(s) ||
                    ServicesRemovalStatus.SERVICE_NOT_PRESENT.equalsIgnoreCase(s));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveInstalledService", "TestFPSController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveInstalledService", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testRemoveInstalledService")
    public void testRebootFPS() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Logger.LogMethodOrder(Logger.getOrderMessage("testRebootFPS", "TestFPSController"));
            Assert.assertTrue(m_fioranoFPSManager.isTPSRunning(FPSPEER));
            m_fioranoFPSManager.restartTPS(FPSPEER);
            System.out.println("Rebooting FPS..");
            Thread.sleep(10000);

            while (!m_fioranoFPSManager.isTPSRunning(FPSPEER)) {
                Thread.sleep(10000);
                System.out.println("Still rebooting...");
            }

            Logger.Log(Level.INFO, Logger.getFinishMessage("testRebootFPS", "TestFPSController"));
        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRebootFPS", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }

    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testRebootFPS")
    public void testKillFPS() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testkillFPS", "TestFPSController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Assert.assertTrue(m_fioranoFPSManager.isTPSRunning(FPSPEER));
            m_fioranoFPSManager.shutdownTPS(FPSPEER);
            System.out.println("Shutting down FPS..");
            Thread.sleep(10000);
            Assert.assertFalse(m_fioranoFPSManager.isTPSRunning(FPSPEER));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillFPS", "TestFPSController"));


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillFPS", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }

    }

    @Test(groups = "FPSControllerTest", alwaysRun = true, dependsOnMethods = "testKillFPS")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestFPSController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            m_fioranoApplicationController.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestFPSController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestFPSController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }

    }

    @AfterClass(groups = "FPSControllerTest", alwaysRun = true)
    public void testCleanUp() {
         try {
             Logger.LogMethodOrder(Logger.getOrderMessage("testCleanUp", "TestFPSController"));
             System.out.println("Started the Execution of the TestCase::" + getName());

             //below is the code to start fps again so that other tests dont get failed
             ServerStatusController serverstatus = ServerStatusController.getInstance();
             TestEnvironment testenv = serverstatus.getTestEnvironment();
             ExecutionController remoteProxy = ExecutionController.getInstance();
             TestEnvironmentConfig testenvconfig = ESBTestHarness.getTestEnvConfig();
             ServerElement se1 = testenv.getServer("fps");
             Map<String, ProfileElement> profileElements1 = se1.getProfileElements();
             String str = new String("standalone");
             ProfileElement pm1 = profileElements1.get(str);
             String machine_fps = pm1.getMachineName();
             MachineElement machine2 = testenv.getMachine(machine_fps);
             String profileType1 = se1.getMode();
             String profileName1 = pm1.getProfileName();
             Boolean isWrapper1 = pm1.isWrapper();
             Boolean isHA1 = se1.isHA();
             remoteProxy.startServerOnRemote(machine2.getAddress(), profileType1, profileName1, isWrapper1, isHA1, testenvconfig);
             Thread.sleep(50000);
             rmiClient = RMIClient.getInstance();
             Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanUp", "TestFPSController"));
         }
         catch (Exception ex) {
             System.err.println("Exception in the Execution of test case::" + getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanUp", "TestFPSController"), ex);
             Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
         }

     }

}
