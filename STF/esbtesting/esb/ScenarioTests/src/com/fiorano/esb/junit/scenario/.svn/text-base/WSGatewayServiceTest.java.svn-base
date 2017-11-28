/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.esb.junit.scenario;

import com.fiorano.stf.test.core.RMITestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.application.controller.handler.ApplicationHandle;

import javax.management.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.Vector;
import java.util.ArrayList;
import java.util.Enumeration;
import java.io.IOException;
import java.io.File;

import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.ApplicationReference;
import fiorano.tifosi.common.TifosiException;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Oct 15, 2007
 * Time: 5:19:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class WSGatewayServiceTest extends RMITestCase {
    ObjectName appController;
    private ObjectName objName;

    boolean isReady = false;

    private String resourceFilePath;
    private FioranoApplicationController m_fioranoApplicationController;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String handleId ;

    public WSGatewayServiceTest(String name) {
        super(name);
    }

    public WSGatewayServiceTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {
        if (isReady == true) return;
        super.setUp();
        System.out.println("Started the Execution of the TestCase::" + getName());

        //for launching the event process STARTS
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "WSGatewayService";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        init();
        //for launching the event process ENDS

        try {
            appController = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            objName = new ObjectName("Fiorano.Esb.Webconsole.Controller:ServiceType=WebconsoleController,Name=WebconsoleController");
            JMXClient client =JMXClient.getInstance();
            handleId = client.getHandleId();
            isReady = true;
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        isReady = true;
    }

    private void init() {
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile");
    }

    private void launchApplication() {
        try {
            System.out.println("The App File is::" + m_appFile);
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);
            m_fioranoApplicationController.launchApplication(m_appGUID, m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
            Thread.sleep(10000);
        } catch (TifosiException e) {
            System.out.println("error launching the application");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
        }

        //kill the application in the end of the testcase
    }

    public static Test suite() {
        return new TestSuite(WSGatewayServiceTest.class);
    }

    public void testGetWebEventProcessList() {
//        Thread.sleep(10000);

        launchApplication();
        try {
            Thread.sleep(10000);
        } catch(InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        int webinterfaceType = 1;
        Object[] args = {webinterfaceType,handleId};
        String[] sigs = {int.class.getName(),String.class.getName()};
        try {
            Vector list = (Vector) jmxClient.invoke(appController, "getWebEventProcessList", args, sigs);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execpasswdution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGenerateWSDL() {

        String appGUID = "WSGATEWAYSERVICE";
        String servInstName = "WSStub1";
        Object[] args = {appGUID, servInstName,handleId};
        String[] sigs = {String.class.getName(), String.class.getName(),String.class.getName()};
        try {
            String wsdl = (String) jmxClient.invoke(appController, "showWSDL", args, sigs);
            Assert.assertNotNull(wsdl);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetReqSOAPmsg() {

        String appGUID = "WSGATEWAYSERVICE";
        String servinstnme = "WSStub1";
        boolean addrflag = true;
        boolean basicAuth = true;
        String user = "admin";
        String passwd = "passwd";

        Object[] args = {appGUID, servinstnme, addrflag, basicAuth, user, passwd,handleId};
        String[] sigs = {String.class.getName(), String.class.getName(), boolean.class.getName(), boolean.class.getName(), String.class.getName(), String.class.getName(),String.class.getName()};

        try {
            String reqSOAPmsg = (String) jmxClient.invoke(appController, "getReqSOAPMsg", args, sigs);
            Assert.assertNotNull(reqSOAPmsg);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetResponseSOAPMsg() {

        String soapStr = "";
        String endPointURL = "FAdminService";
        String operationName = "TEST";
        String username = "admin";
        String password = "passwd";
        boolean basicAuth = true;
        String servInstName = "";
        String appGUID="";

        Object[] args = {soapStr, endPointURL, operationName, appGUID,servInstName, basicAuth, username, password,handleId};
        String[] sigs = {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),String.class.getName(), boolean.class.getName(),String.class.getName(), String.class.getName(),String.class.getName()};
        try {
            String responseSOAPmsg = (String) jmxClient.invoke(appController, "getResponseSOAPMsg", args, sigs);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testStartWebInstance() {

        String appGUID = "WSGATEWAYSERVICE";
        String servinstnme = "WSStub1";
        String user = null;
        Object[] args = {appGUID, servinstnme, handleId };
        String[] sigs = {String.class.getName(), String.class.getName(),String.class.getName()};
        try {
            jmxClient.invoke(appController, "startWebInstance", args, sigs);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }finally {
            System.out.println("going to kill the application");
            stopApplication();
        }

    }

    public void testGetListOfApplications() throws Exception {


        try {
            Enumeration applist = m_fioranoApplicationController.getListOfApplications(ApplicationReference.WEB_SERVICE);
            Assert.assertNotNull(applist);

        } catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testGetHeadersOfApplications() throws Exception {

        try {
            Enumeration headerlist = m_fioranoApplicationController.getHeadersOfApplications(ApplicationReference.WEB_SERVICE);
            Assert.assertNotNull(headerlist);

        } catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testStopWebInstance() {

        String appGUID = "WSGATEWAYSERVICE";
        String servinstnme = "WSStub1";
        String user = null;
        Object[] args = {appGUID, servinstnme,handleId};
        String[] sigs = {String.class.getName(), String.class.getName(),String.class.getName()};
        try {
            jmxClient.invoke(appController, "stopWebInstance", args, sigs);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }

    }

    private void stopApplication() {
        try {
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Thread.sleep(10000);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static ArrayList getOrder() {
        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testGetWebEventProcessList");
        methodsOrder.add("testGenerateWSDL");
        methodsOrder.add("testGetReqSOAPmsg");
        methodsOrder.add("testGetResponseSOAPMsg");
        methodsOrder.add("testGetListOfApplications");
//        methodsOrder.add("testGetHeadersOfApplications");           // method is throwing OOM
        methodsOrder.add("testStopWebInstance");
        methodsOrder.add("testStartWebInstance");

        return methodsOrder;
    }
}

