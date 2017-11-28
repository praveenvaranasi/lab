package com.fiorano.esb.junit.scenario.route;

/**
 * Created by IntelliJ IDEA.
 * User: root
 *The objective of this file is to change the sender selector configuration of a route and to test the change.
 * Time: 1:17:51 PM
 * To change this template use File | Settings | File Templates.
 */

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.EventProcessHandle;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * The objective of this file is to check the sender selector functionality of a route.
 */
public class SenderSelectorTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String m_testFile1;
    private String m_testFile2;
    private String m_outFile;
    private String m_expecoutFile1;
    private String m_expecoutFile2;
    private EventProcessHandle eventProcessHandle;
    private TestEnvironmentConfig testenvconfig;


    public SenderSelectorTest(String name) {
        super(name);
    }

    public SenderSelectorTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    // the function modifies the xml files and replace any "search" string with "replace" string

    void modifyXmlFiles(String path, String search, String replace) throws IOException {
        File fl = new File(path);
        FileReader fr = null;
        FileWriter fw = null;
        boolean change = false;
        BufferedReader br;
        String s;
        String result = "";

        try {
            fr = new FileReader(fl);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            fw = new FileWriter(resourceFilePath + File.separator + "temp.xml");
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
            int l = s.indexOf(search);
            if (l != -1 && l != j) {
                change = true;
                int k = search.length() + l;
                //System.out.println("The Index start is "+j+" and index last is "+ k);
                result = s.substring(0, l);
                result = result + replace;
                result = result + s.substring(k);
                s = result;
            }

            fw.write(s);
            fw.write('\n');
        }
        fr.close();
        fw.close();
    }//changed function


    public void setUp() throws Exception {
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Route" + File.separator + "SenderSelector";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        init();
    }

    private void init() throws Exception {
        if (m_initialised)
            return;

        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile", "1.0.xml");
        String testingHome = testenvconfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        modifyXmlFiles(m_appFile, "/home/srikanth/esbtesting", testingHome);
        m_appFile = resourceFilePath + File.separator + "temp.xml";
        m_testFile1 = resourceFilePath + File.separator + testCaseProperties.getProperty("Test1", null);
        m_testFile2 = resourceFilePath + File.separator + testCaseProperties.getProperty("Test2", null);
        m_outFile = resourceFilePath + File.separator + testCaseProperties.getProperty("Output", null);
        m_expecoutFile1 = resourceFilePath + File.separator + testCaseProperties.getProperty("ExpectedOutput1", null);
        m_expecoutFile2 = resourceFilePath + File.separator + testCaseProperties.getProperty("ExpectedOutput2", null);
        eventProcessHandle = new EventProcessHandle(m_appGUID, m_version, m_appFile);
        printInitParams();
        m_initialised = true;


    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("The Application File is::" + m_appFile);
        System.out.println("..................................................................");

    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * trying to import an application.
     */
    public void testImportApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "SenderSelectorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));

            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "SenderSelectorTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "SenderSelectorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application .
     */
    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "SenderSelectorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(30000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "SenderSelectorTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "SenderSelectorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * This method changes the sender selector for a route and tests the change
     * So the test case shall be considered as passed if the messages that are sent from are recieved.
     */

    public void testSenderSelectorPositive() {
        boolean check = false;
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSenderSelectorPositive", "SenderSelectorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            File file = new File(m_outFile);
            if (file.exists())
                file.delete();
            eventProcessHandle.sendMessageOnTopic("Feeder1", "OUT_PORT", new File(m_testFile2));
            Thread.sleep(20000);
            check = checkFiles(m_outFile, m_expecoutFile2);
            Thread.sleep(10000);
            if (check)
                System.out.println("test passed as expected message is recieved at Display2");
            else {
                System.out.println("test failed as expected message is not recieved at Display2");
                throw new Exception("test failed as expected message is not recieved at Display2");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSenderSelectorPositive", "SenderSelectorTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSenderSelectorPositive", "SenderSelectorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    /**
     * This method changes the sender selector for a route and tests the change
     */
    public void testSenderSelectorNegative() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSenderSelectorNegative", "SenderSelectorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            File file = new File(m_outFile);
            if (file.exists())
                file.delete();
            eventProcessHandle.sendMessageOnTopic("chat1", "OUT_PORT", new File(m_testFile1));
            Thread.sleep(20000);
            if (!file.exists())
                System.out.println("test passed as message is not recieved at Display2");
            else {
                System.out.println("test failed as message recieved at Display2");
                throw new Exception("test failed as message recieved at Display2");
            }

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSenderSelectorNegative", "SenderSelectorTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSenderSelectorNegative", "SenderSelectorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    /**
     * Stops a running application
     */

    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "SenderSelectorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "SenderSelectorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "SenderSelectorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes a application which is stopped above
     */

    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "SenderSelectorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            File file = new File(m_appFile);
            if (file.exists())
                file.delete();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "SenderSelectorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "SenderSelectorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            System.out.println("starting application");
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);

            return;
        } catch (TifosiException e) {
            if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1) {
                //never mind if the application is already running.
                return;
            }
            System.err.println("Error launching the application");
            e.printStackTrace();
            throw new FioranoException("Error launching the application", e);
        }
    }

    private void stopApplication(String appGUID) {
        try {
            System.out.println("Stoping the application");
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);

        } catch (TifosiException e) {
            System.err.println("Error stoping the application");
            e.printStackTrace();
        }
    }

    private boolean checkFiles(String file1, String file2) {
        int i = 0;
        boolean check1 = false;
        System.out.println("in checkfiles");
        try {
            BufferedReader f1 = new BufferedReader(new FileReader(file1));

            BufferedReader f2 = new BufferedReader(new FileReader(file2));
            String s;
            String y;
            while ((s = f1.readLine()) != null) {
                y = f2.readLine();
                if (y.equals(s))
                    i++;
                else {
                    i = 0;
                    break;
                }
            }

            if (i != 0) {
                f1.close();
                f2.close();
                check1 = true;

            } else {

                f1.close();
                f2.close();

            }

        }

        catch (Exception e) {
            e.printStackTrace();
        }
        return check1;
    }


    public static Test suite() {
        return new TestSuite(SenderSelectorTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testSenderSelectorPositive");
        methodsOrder.add("testSenderSelectorNegative");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }
}

