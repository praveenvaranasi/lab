package com.fiorano.esb.junit.scenario;

import com.fiorano.stf.test.core.RMITestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.esb.rtl.application.FioranoApplicationController;

import javax.management.*;

import junit.framework.Test;
import junit.framework.TestSuite;

import junit.framework.Assert;

import java.io.File;
import java.util.*;


import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.aps.ApplicationStateDetails;


/**
 * Created by IntelliJ IDEA.
 * User: Nagesh V H
 * Date: Aug 12, 2008
 * Time: 3:32:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class FPSControllerMBeanTest extends RMITestCase {

    private ObjectName objName;
    private ObjectName objName2;

    private FioranoApplicationController m_fioranoApplicationController;

    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;

    private String m_appFile;
    private String m_servinstance1;
    private String m_servinstance2;

    private String FPSPEER = "fps_test";

    public FPSControllerMBeanTest(String name) {
        super(name);
    }


    public FPSControllerMBeanTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }


    public void setUp() throws Exception {
        super.setUp();
        System.out.println("Started the Execution of the TestCase::" + getName());

        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "FPSControllerMBean";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        init();

        try {
            objName = new ObjectName("Fiorano.Esb.Fps.Controller:ServiceType=FPSController,Name=FPSController");
            objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");

            JMXClient.connect("admin", "passwd");
        }
        catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }

    public void init() {
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile");

        m_servinstance1 = testCaseProperties.getProperty("ServiceInstance1");
        m_servinstance2 = testCaseProperties.getProperty("ServiceInstance2");
        printInitParams();
    }

    public static Test suite() {
        return new TestSuite(FPSControllerMBeanTest.class);
    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Service Instances::      " + m_servinstance1 + " and " + m_servinstance2);
        System.out.println("..................................................................");
    }

    public void teststartApplication() throws Exception {

        try {


            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            Thread.sleep(5000);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);
            m_fioranoApplicationController.launchApplication(m_appGUID, m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
            Thread.sleep(5000);
        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

    }

    public void testGetFPSQueueInformation() throws Exception {
        try {


            Application application = ApplicationParser.readApplication(new File(m_appFile));
            boolean value = application.getServiceInstance(m_servinstance1).getPortInstance("IN_PORT").isEnabled();
            Assert.assertTrue(value);

            System.out.println("The Queue instance of " + m_appGUID + " " + m_servinstance1 + " exists.. ");

        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testGetFPSQueues() throws Exception {
        try {


            Object[] params = {FPSPEER};
            String[] signatures = {String.class.getName()};

            List list = (List) jmxClient.invoke(objName, "getFPSQueues", params, signatures);

            Assert.assertTrue(list.size() > 0);
            Iterator iter = list.iterator();

            System.out.println("Printing the FPS Queues list...");
            System.out.println("..............................................................");

            while (iter.hasNext()) {
                System.out.println(iter.next());
            }

            System.out.println("..............................................................");


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testGetFPSTopicInformation() throws Exception {
        try {


            Application application = ApplicationParser.readApplication(new File(m_appFile));
            boolean value = application.getServiceInstance(m_servinstance1).getPortInstance("OUT_PORT").isEnabled();
            Assert.assertTrue(value);

            System.out.println("The Topic instance of " + m_appGUID + " " + m_servinstance1 + " exists.. ");

        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testGetFPSTopics() throws Exception {
        try {


            Object[] params = {FPSPEER};
            String[] signatures = {String.class.getName()};

            List list = (List) jmxClient.invoke(objName, "getFPSTopics", params, signatures);
            Assert.assertTrue(list.size() > 0);
            Iterator iter = list.iterator();

            System.out.println("Printing the FPS Topics list...");
            System.out.println("..............................................................");

            while (iter.hasNext()) {
                System.out.println(iter.next());
            }

            System.out.println("..............................................................");


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testStopAllServices() {
        try {

            ApplicationStateDetails asd = m_fioranoApplicationController.stopAllServices(m_appGUID,m_version);
            if (!(m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version, m_servinstance1).getKillTime() < new Date().getTime()))
                throw new Exception("Stop All Services Failed");
        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testCleanUp() {
        try {

            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            m_fioranoApplicationController.close();
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

    }


    private boolean isApplicationRunningUtil(String appGUID) throws Exception {
        Object[] params = {appGUID};
        String[] signatures = {String.class.getName()};
        Boolean appStatus = (Boolean) jmxClient.invoke(objName2, "isRunningApplication", params, signatures);

        return appStatus.booleanValue();
    }


}
