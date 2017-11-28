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
import fiorano.tifosi.dmi.application.ServiceInstance;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Iterator;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/14/11
 * Time: 6:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestTransformation extends AbstractTestNG{
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
    
    private void init() throws Exception {
        if (m_initialised)
            return;

        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile", "1.0.xml");
        modifyXmlFiles(m_appFile, "/home/ranu/Fiorano/Testing_Home", testingHome);
        m_appFile = resourceFilePath + fsc + "temp.xml";
        testfile = resourceFilePath + fsc + testProperties.getProperty("Test", null);
        outfile = resourceFilePath + fsc + testProperties.getProperty("Output", null);
        expectedoutput = resourceFilePath + fsc + testProperties.getProperty("Expectedoutput", null);

        m_initialised = true;
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: " + m_appGUID + "\tVersion:: " + m_version);
        System.out.println("test file:: " + testfile + "\nOut file:: " + outfile);
        System.out.println("The Resource File Path:: " + resourceFilePath);
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "TransformationTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Route" + fsc + "Transformation" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Route" + fsc + "Transformation";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "TransformationTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestTransformation"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestTransformation"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestTransformation"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "TransformationTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestTransformation"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(15000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestTransformation"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestTransformation"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "TransformationTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "sets the output port of chat1 to topic and input port of chat2 and chat3 to queue and tests the change")
    public void testSendMessage() {
        boolean check;
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSendMessage", "TestTransformation"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            File file = new File(outfile);
            if (file.exists())
                file.delete();

            eventProcessHandle = new EventProcessHandle(m_appGUID, m_version, m_appFile);
            //for checking all service instances
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            Iterator ir = application.getServiceInstances().iterator();
            while (ir.hasNext()) {
                ServiceInstance si = (ServiceInstance) ir.next();
                System.out.println("service instance::" + si.getName());
            }
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSendMessage", "TestTransformation"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessage", "TransformationTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "TransformationTest", alwaysRun = true, dependsOnMethods = "testSendMessage", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestTransformation"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "TestTransformation"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestTransformation"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "TransformationTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestTransformation"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestTransformation"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestTransformation"), ex);
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
}
