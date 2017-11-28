package com.fiorano.esb.testng.rmi.scenario.route;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.EventProcessHandle;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/14/11
 * Time: 5:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestMessageBodyXPath extends AbstractTestNG{
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private EventProcessHandle eventProcessHandle;
    private String m_testFile1;
    private String m_testFile2;
    private String m_outFile;
    private String m_expecoutFile1;
    private String m_expecoutFile2;
    private TestEnvironmentConfig testenvconfig; 

    private void init() throws Exception {
        if (m_initialised)
            return;

        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile","1.0.xml");
        modifyXmlFiles(m_appFile, "/home/ranu/Fiorano/Testing_home", testingHome);
        m_appFile = resourceFilePath + fsc + "temp.xml";
        m_testFile1 = resourceFilePath + fsc + testProperties.getProperty("Test1", null);
        m_testFile2 = resourceFilePath + fsc + testProperties.getProperty("Test2", null);
        m_outFile = resourceFilePath + fsc + testProperties.getProperty("Output", null);
        m_expecoutFile1 = resourceFilePath + fsc + testProperties.getProperty("ExpectedOutput1", null);
        m_expecoutFile2 = resourceFilePath + fsc + testProperties.getProperty("ExpectedOutput2", null);

        m_initialised = true;
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: " + m_appGUID + "\tVersion:: " + m_version);
        System.out.println("The Resource File Path:: " + resourceFilePath);
        System.out.println("...........................................................................");
    }  

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
            fw = new FileWriter(resourceFilePath + fsc + "temp.xml");
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

    @BeforeClass(groups = "MessageBodyXPathTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Route" + fsc + "MessageBodyXpath" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Route" + fsc + "MessageBodyXpath";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "MessageBodyXPathTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestMessageBodyXPath"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestMessageBodyXPath"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestMessageBodyXPath"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "MessageBodyXPathTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestMessageBodyXPath"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestMessageBodyXPath"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestMessageBodyXPath"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "MessageBodyXPathTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "sets the output port of chat1 to topic and input port of chat2 and chat3 to queue and tests the change")
    public void testXpathNegative() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testXPathNegative", "MesssageBodyXPathTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            eventProcessHandle = new EventProcessHandle(m_appGUID, m_version, m_appFile);
            File file = new File(m_outFile);
            if (file.exists())
                file.delete();
            eventProcessHandle.sendMessageOnTopic("Feeder1", "OUT_PORT", new File(m_testFile1));    //todo exception here
            Thread.sleep(20000);
            if (!file.exists())
                System.out.println("test passed as message is not recieved at Display");
            else {
                System.out.println("test failed as message recieved at Display");
                throw new Exception("test failed as message recieved at Display");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testXPathNegative", "MesssageBodyXPathTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testXPathNegative", "MesssageBodyXPathTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "MessageBodyXPathTest", alwaysRun = true, dependsOnMethods = "testXpathNegative", description = "sets the output port of chat1 to Queue and input port of chat2 and chat3 to topic and tests the change")
    public void testXpathPositive() {
        boolean check = false;
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testXPathPositive", "MesssageBodyXPathTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            File file = new File(m_outFile);
            if (file.exists())
                file.delete();
            eventProcessHandle.sendMessageOnTopic("Feeder1", "OUT_PORT", new File(m_testFile2));       //todo exception here
            Thread.sleep(20000);
            check = checkFiles(m_outFile, m_expecoutFile2);
            Thread.sleep(10000);
            if (check)
                System.out.println("test passed as expected message is recieved at Display");
            else {
                System.out.println("test failed as expected message is not recieved at Display");
                throw new Exception("test failed as expected message is not recieved at Display");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testXPathPositive", "MesssageBodyXPathTest"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testXPathPositive", "MesssageBodyXPathTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "MessageBodyXPathTest", alwaysRun = true, dependsOnMethods = "testXpathPositive", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestMessageBodyXPath"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "TestMessageBodyXPath"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestMessageBodyXPath"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "MessageBodyXPathTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestMessageBodyXPath"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestMessageBodyXPath"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestMessageBodyXPath"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);
            System.out.println("started application");
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
            System.out.println("stoping the application");
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
}
