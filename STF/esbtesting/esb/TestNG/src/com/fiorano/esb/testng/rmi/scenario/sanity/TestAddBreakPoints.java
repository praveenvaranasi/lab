package com.fiorano.esb.testng.rmi.scenario.sanity;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.EventProcessHandle;
import com.fiorano.stf.misc.JUnitDebuger;
import com.fiorano.stf.misc.OutputPortMessageListener;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/17/11
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestAddBreakPoints extends AbstractTestNG{
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_routeGUID;
    private String m_appFile;
    private EventProcessHandle eventProcessHandle;

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_routeGUID = getProperty("RouteGUID");
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_initialised = true;

    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Route GUID:: " + m_routeGUID);
        System.out.println("The Application File is::" + m_appFile);
        System.out.println("..................................................................");
    }

    @BeforeClass(groups = "AddBreakPointsTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Sanity" + fsc + "AddBreakPoints" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Sanity" + fsc + "AddBreakPoints";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "AddBreakPointsTest", alwaysRun = true, description = "trying to import an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestAddBreakPoints"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            eventProcessHandle = new EventProcessHandle(m_appGUID, m_version, m_appFile);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestAddBreakPoints"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + ex.getMessage());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestSenderSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "AddBreakPointsTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestAddBreakPoints"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestAddBreakPoints"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestSenderSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "AddBreakPointsTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "Ads the breakpoints and check.")
    public void testAddBreakPoints() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddbreakpoints", "TestAddBreakPoints"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitDebuger callback = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, callback);
            System.out.println("Added debugger");
            Thread.sleep(10000);
            OutputPortMessageListener listener = new OutputPortMessageListener(120000);
            eventProcessHandle.addListener("Display1", "IN_PORT", listener);
            eventProcessHandle.sendMessage("Feeder1", "OUT_PORT", "testing");
            if (!listener.getMessageReceived())
                System.out.println("Message not received at display , hence Debugger is set");
            else throw new Exception();

            System.out.println("Forward all pending msgs");
            m_fioranoApplicationController.forwardAllPendingDebugMessages(m_appGUID,m_version, m_routeGUID);
            Thread.sleep(5000);
            if (listener.getMessageReceived() == true)
                System.out.println("Message received at display now.");
            else throw new Exception();
            //m_fioranoApplicationController.
            // m_fioranoApplicationController.removeDebugger(m_routeGUID,m_appGUID,false,false,10000L);
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, false, false, 10000L);
            Thread.sleep(5000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testAddBreakpoints", "TestAddBreakPoints"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddbreakpoints", "AddBreakPointsTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "AddBreakPointsTest", alwaysRun = true, dependsOnMethods = "testAddBreakPoints", description = "Stops the running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestAddBreakPoints"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "TestAddBreakPoints"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestSenderSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "AddBreakPointsTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the stopped application.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestAddBreakPoints"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestAddBreakPoints"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestSenderSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);
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
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);
        } catch (TifosiException e) {
            System.err.println("Error stoping the application");
            e.printStackTrace();
        }
    }

}
