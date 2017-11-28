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
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.esb.rtl.sbw.FioranoSBWManager;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.sbw.handler.SBWManager;
import com.fiorano.stf.jms.STFQueueSender;
import fiorano.tifosi.dmi.SBWSearchContext;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.application.InputPortInstance;
import fiorano.tifosi.dmi.application.ApplicationParser;

import javax.management.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.HashMap;
import java.util.*;
import java.io.IOException;
import java.io.File;

import fiorano.tifosi.dmi.dashboard.WorkFlowStatusForAppData;
import fiorano.tifosi.dmi.ste.DocumentInfo;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.common.FioranoException;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Oct 15, 2007
 * Time: 3:56:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class SBWDetailerTest extends RMITestCase {
    private ObjectName objName1;
    private ObjectName objName2;
    private ObjectName objName3;
    private ObjectName objName4;

    private String resourceFilePath;
    private String handleId;


    private String m_appGUID;

    private float m_version;

    private String m_appFile;

    private String m_startInstance;

    private String m_endInstance;


    private boolean isReady = false;
    private FioranoSBWManager m_fioranoSBWManager;
    private FioranoApplicationController m_applicationcontroller;
    private SBWManager m_manager;

    public SBWDetailerTest(String name) {
        super(name);
    }

    public SBWDetailerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {

        if (isReady == true) return;
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "SBWDetailer";
        m_applicationcontroller = rtlClient.getFioranoApplicationController();
        m_fioranoSBWManager = rtlClient.getFioranoSBWManager();
        init();
        try {

            objName1 = new ObjectName("Fiorano.Esb.Sbw:ServiceType=SBWManager,Name=SBWManager");
            objName2 = new ObjectName("Fiorano.Esb.Webconsole.Controller:ServiceType=WebconsoleController,Name=WebconsoleController");
            objName3 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            objName4 = new ObjectName("Fiorano.Esb.Server:ServiceType=FESServer,Name=FESServer");
            isReady = true;
            JMXClient.connect("admin", "passwd");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        isReady = true;


    }

    private void init() {

        m_appGUID = testCaseProperties.getProperty("ApplicationGUID1");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile1", "SBWCHAT_1.0.xml");
        m_startInstance = testCaseProperties.getProperty("Instance1");
        m_endInstance = testCaseProperties.getProperty("Instance2");

    }

    public static Test suite() {
        return new TestSuite(SBWDetailerTest.class);
    }




    public void testGetSBWStatusForAllApps() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            JMXClient client =JMXClient.getInstance();
            handleId = client.getHandleId();
            ArrayList appNames = (ArrayList) jmxClient.invoke(objName2, "getAllApplicationNames", new Object[]{handleId}, new String[]{String.class.getName()});

            // This is method is commented in ( SBWManager implements ISBWManager ), so here also commenting ..
/*
            if (appNames != null) {
                for (int i = 0; i < appNames.size(); i++) {
                    Object[] params = {(String) appNames.get(i)};
                    String[] signatures = {String.class.getName()};
                    WorkFlowStatusForAppData sbwAppStatus = (WorkFlowStatusForAppData) jmxClient.invoke(objName1, "getSBWWorkFlowStatusForApp", params, signatures);
                }
            }
*/

        } catch (ReflectionException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }


    private void testastartApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_applicationcontroller.prepareLaunch("SIMPLECHAT1.0", 1);
            m_applicationcontroller.launchApplication(appGuid, appVersion);
            m_applicationcontroller.startAllServices(appGuid,appVersion);
        } catch (TifosiException e) {
            if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1) {
                //never mind if the application is already running.
                return;
            }
            System.err.println("Error launching the application");
            e.printStackTrace();
            throw new FioranoException("Error launching the application");
        }
    }


    public void testcountAllWorkFlowInstancesForApp() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            Object[] params = {m_appGUID};
            String[] signatures = {String.class.getName()};
            jmxClient.invoke(objName1, "countAllWorkFlowInstancesForApp", params, signatures);


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testgetGeneralInfo() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_applicationcontroller.saveApplication(application);
            m_applicationcontroller.compileApplication(m_appGUID, m_version);
            m_applicationcontroller.prepareLaunch(m_appGUID, m_version);
            m_applicationcontroller.launchApplication(m_appGUID, m_version);
            m_applicationcontroller.startAllServices(m_appGUID,m_version);


            Application appn = m_applicationcontroller.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) appn.getServiceInstance(m_endInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);


            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi test the track", 5);
            SBWSearchContext ctx = makeSBWSearchContext(m_endInstance);
            Enumeration docs = m_fioranoSBWManager.searchDocumentInfos(ctx, 0, -1);
            Assert.assertNotNull("document was supposed to be tracked at workflow item chat1 but was not tracked", docs);

            DocumentInfo di = (DocumentInfo) docs.nextElement();
            Assert.assertNotNull(di);

            Object[] params = {di.getDocumentID()};
            String[] signatures = {String.class.getName()};
            HashMap geninfo = (HashMap) jmxClient.invoke(objName1, "getSerializedTifosiDocument", params, signatures);
            assertNotNull(geninfo);


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    private SBWSearchContext makeSBWSearchContext(String m_startInst) {
        String fpsName = "fps_test";
        SBWSearchContext ctx = new SBWSearchContext();
        ctx.setAppGUID(m_appGUID);
        ctx.setPeerName(fpsName);
        ctx.setServiceInstance(m_startInst);
        return ctx;
    }

    private void sendMessageOnInputPort(String m_appGUID, String serviceInstName, String portName, String message, int count) throws Exception {
        STFQueueSender publisher = null;

        System.out.println("Sending the message \n" + message);
        publisher = new STFQueueSender();

        String fpsName = (String) m_applicationcontroller.getApplication(m_appGUID, m_version).getServiceInstance(serviceInstName).getNodes()[0];
        if (fpsName == null || fpsName.trim().equals(""))
            fpsName = "fps_test";
        String url = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);

        String queueName = m_appGUID + "__" + serviceInstName + "__" + portName;
        publisher.initialize(queueName, null, url);
        publisher.send(message, count);


        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            //ignore
        }

        if (publisher != null)
            publisher.cleanup();
        System.out.println("Messages Published");
    }

    public void testpurgeDocumentsByName() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application appn = m_applicationcontroller.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) appn.getServiceInstance(m_endInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);


            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi test the track again", 2);
            SBWSearchContext ctx = makeSBWSearchContext(m_endInstance);

            Enumeration docs = m_fioranoSBWManager.searchDocumentInfos(ctx, 0, -1);
            Assert.assertNotNull("document was supposed to be tracked at workflow item chat1 but was not tracked", docs);


            ArrayList docIdlist = new ArrayList();
            while (docs.hasMoreElements()) {
                DocumentInfo di = (DocumentInfo) docs.nextElement();
                docIdlist.add(di.getDocumentID());
            }

            Object[] params = {docIdlist};
            String[] signatures = {ArrayList.class.getName()};
            jmxClient.invoke(objName1, "purgeDocumentsByName", params, signatures);


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testcountSearchedDocuments() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application appn = m_applicationcontroller.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) appn.getServiceInstance(m_endInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);


            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi test the track again", 2);
            SBWSearchContext ctx = makeSBWSearchContext(m_endInstance);

            Object[] params = {ctx};
            String[] signatures = {SBWSearchContext.class.getName()};
            jmxClient.invoke(objName1, "countSearchedDocuments", params, signatures);


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    /*  public void testsearchDocumentFromEnd() throws Exception {
try {
   System.out.println("Started the Execution of the TestCase::" + getName());

   Application appn = m_applicationcontroller.getApplication(m_appGUID, m_version);
   ServiceInstance service = (ServiceInstance) appn.getServiceInstance(m_endInstance);
   InputPortInstance inPort = (InputPortInstance) service.getInputPortInstances().get(0);


   sendMessageOnInputPort(m_appGUID, service.getName(), inPort.getName(), "hi test the track again", 3);
   SBWSearchContext ctx = makeSBWSearchContext(m_endInstance);

   Date from_date = new Date();

   Object[] params = {m_appGUID,service.getName(),inPort.getName(),"fps_test","EXECUTING",1,5,from_date,to_date};
   String[] signatures = {String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),int.class.getName(),int.class.getName(), Date.class.getName(),Date.class.getName()};
   Enumeration docinfo = (Enumeration) jmxClient.invoke(objName3, "searchDocumentInfosFromEnd", params, signatures);
   Assert.assertNotNull(docinfo);

}
catch (Exception ex) {
   System.err.println("Exception in the Execution of test case::" + getName());
   ex.printStackTrace();
   Assert.assertTrue("TestCase Failed because of
    " + ex.getMessage(), false);
}
}

    */

  // This is method is nolonger(i.e commented)  in source code (look into history ), so commenting here also (Raja Gopal)
/*

    public void testsetSBWBoundCount() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            Object[] params = {7};
            String[] signatures = {int.class.getName()};
            jmxClient.invoke(objName1, "setSBWBoundCount", params, signatures);


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }
*/


    public void testpurgeDocuments() throws Exception {
        String userDocID = "";
        String wrkflwInstID = "";
        String documentID = "";
        String port= "";
        String status = "";
        String peer = "";
        Date from = null;
        Date to = null;


        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application appn = m_applicationcontroller.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) appn.getServiceInstance(m_endInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);


            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi test the track again", 2);
            SBWSearchContext ctx = makeSBWSearchContext(m_endInstance);

            Object[] params = {handleId, m_appGUID, userDocID,wrkflwInstID,documentID, startService.getName(), inPort.getName(),peer, status, from, to};
            String[] signatures = {String.class.getName(), String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),Date.class.getName(),Date.class.getName()};
            jmxClient.invoke(objName1, "purgeDocuments", params, signatures);


        }
        catch(Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testgetClientJars() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());


            String fiorano_home = System.getProperty("FIORANO_HOME");

            String sourceResourcePath = fiorano_home + File.separator + "runtimedata";
            Object[] params = {sourceResourcePath, false};
            String[] signatures = {String.class.getName(), boolean.class.getName()};
            Object clientjar = (Object) jmxClient.invoke(objName4, "getClientJars", params, signatures);
            Assert.assertNotNull(clientjar);

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void DeleteApplication() {
        try {
            if (m_applicationcontroller.isApplicationRunning(m_appGUID))
                m_applicationcontroller.killApplication(m_appGUID,m_version);
            m_applicationcontroller.deleteApplication(m_appGUID, m_version);

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }

    }


    public void testGetAllWorkFlowInstancesForApp() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String appGUID = "SIMPLECHAT1.0";
        int fromind = 0;
        int toind = 1;
        Object[] params = {appGUID, fromind, toind};
        String[] signatures = {String.class.getName(), int.class.getName(), int.class.getName()};
        try {
            ArrayList workFlowList = (ArrayList) jmxClient.invoke(objName1, "getAllWorkFlowInstancesForApp", params, signatures);
            Assert.assertNotNull(workFlowList);
        } catch (ReflectionException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetTrackedDocumentsForWorkFlow() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        String workFlowID = "";
        Object[] params = {workFlowID};
        String[] signatures = {String.class.getName()};
        try {
            ArrayList docsList = (ArrayList) jmxClient.invoke(objName1, "getDocumentInfosList", params, signatures);
            Assert.assertNotNull(docsList);

            DeleteApplication();

        } catch (ReflectionException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }
}
