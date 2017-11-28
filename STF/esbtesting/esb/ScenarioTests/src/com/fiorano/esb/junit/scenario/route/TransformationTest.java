package com.fiorano.esb.junit.scenario.route;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Mar 14, 2008
 * Time: 1:17:51 PM
 * To change this template use File | Settings | File Templates.
 * The main objective of this file is to test the transformation provided.
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

public class TransformationTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String testfile;
    private String outfile;
    private String expectedoutput;
    private EventProcessHandle eventProcessHandle;
    private TestEnvironmentConfig testenvconfig;


    public TransformationTest(String name) {
        super(name);
    }

    public TransformationTest(TestCaseElement testCaseConfig) {
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
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Route" + File.separator + "Transformation";
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
        testfile = resourceFilePath + File.separator + testCaseProperties.getProperty("Test", null);
        outfile = resourceFilePath + File.separator + testCaseProperties.getProperty("Output", null);
        expectedoutput = resourceFilePath + File.separator + testCaseProperties.getProperty("Expectedoutput", null);
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
     * trying to import an application
     */
    public void testImportApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "TransformationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));

            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "TransformationTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "TransformationTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    /**
     * Runs the application.
     */
    public void testRunApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunApplication", "TransformationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(20000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunApplication", "TransformationTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunApplication", "TransformationTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Runs the application.
     */
    public void testSendMessage() {
        boolean check;
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSendMessage", "TransformationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            File file = new File(outfile);
            if (file.exists())
                file.delete();
            eventProcessHandle.sendMessageOnTopic("chat1", "OUT_PORT", new File(testfile));
            Thread.sleep(20000);
            check = checkFiles(outfile, expectedoutput);
            Thread.sleep(10000);
            if (check)
                System.out.println("test passed as expected message is recieved at chat2");
            else {
                System.out.println("test failed as expected message is not recieved at chat2");
                throw new Exception("test failed as expected message is not recieved at chat2");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSendMessage", "TransformationTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSendMessage", "TransformationTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    /**
     * Stops an application which one is running
     */

    public void testKillApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testKillApplication", "TransformationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testKillApplication", "TransformationTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testKillApplication", "TransformationTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * Deletes an application which is stopped above
     */

    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "TransformationTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            File file = new File(m_appFile);
            if (file.exists())
                file.delete();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "TransformationTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "TransformationTest"), ex);
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
        return new TestSuite(TransformationTest.class);
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testRunApplication");
        methodsOrder.add("testSendMessage");
        methodsOrder.add("testKillApplication");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }

}



