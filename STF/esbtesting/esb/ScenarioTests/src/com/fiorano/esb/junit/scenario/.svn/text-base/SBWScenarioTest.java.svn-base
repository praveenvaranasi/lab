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

import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.util.Enumeration;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

import com.fiorano.stf.jms.STFQueueSender;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.sbw.FioranoSBWManager;
import com.fiorano.esb.rtl.dm.FioranoDeploymentManager;
import com.fiorano.esb.sbw.handler.SBWManager;
import com.fiorano.esb.server.impl.FESServer;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestCaseElement;

import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.*;
import fiorano.tifosi.dmi.ste.*;
import fiorano.tifosi.dmi.ServiceInfo;
import fiorano.tifosi.dmi.SBWSearchContext;
import fiorano.tifosi.dmi.dm.DeploymentRule;
import fiorano.tifosi.tps.rtl.TifosiDocument;

/**
 * @author Sandeep M
 * @date 26th Dec 2006
 */
public class SBWScenarioTest extends DRTTestCase {
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private String m_appGUID1;

    private float m_version;
    private FioranoSBWManager m_fioranoSBWManager;
    private FESServer m_fesmanager;
    private String m_appFile;
    private String m_startInstance;
    private String m_endInstance;

    private String m_appFile1;
    private String m_startInstance1;
    private String m_endInstance1;

    private SBWManager m_manager ;
    static boolean isModifiedOnceHA = false;//to check the files overwriting
    static boolean isModifiedOnce = false;
    private FilenameFilter xmlFilter;//file extracter

    // This is set in STFQueueSender as a StringProperty
    // Dont think there is a more direct way to set doc ids
    private String m_UserDocID = "siren";

    public SBWScenarioTest(String name) {
        super(name);
    }

    public SBWScenarioTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    private void setXmlFilter(String ext)//creating the xml files filter
    {
        xmlFilter = new OnlyExt(ext);
    }

    // the function modifies the xml files and replace any search string with replace string

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
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        m_fioranoSBWManager = rtlClient.getFioranoSBWManager();
        m_manager = new SBWManager();
        m_fesmanager = new FESServer();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "SBWScenario";

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
        if (m_initialised)
            return;

        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_appGUID1 = testCaseProperties.getProperty("ApplicationGUID1");

        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile", "1.0.xml");
        m_appFile1 = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile1", "SBWCHAT_1.0.xml");
        m_startInstance = testCaseProperties.getProperty("WorkFlowStartInstance");
        m_endInstance = testCaseProperties.getProperty("WorkFlowEndInstance");
        m_startInstance1 = testCaseProperties.getProperty("Instance1");
        m_endInstance1 = testCaseProperties.getProperty("Instance2");
        printInitParams();
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");

    }

    public static Test suite() {
        return new TestSuite(SBWScenarioTest.class);
    }

    public void testCreateSBWApplication() {
        // This application is created form the SIMPLECHAT Application, and then setting the Workflow on ports. etc.
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCreateSBWApplication", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("The App File is::" + m_appFile);
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCreateSBWApplication", "SBWScenarioTest"));


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCreateSBWApplication", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    public void testTrackingType() {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testTrackingType", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            //start the application
            startApplication(m_appGUID, m_version);

            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) application.getServiceInstance(m_startInstance);
            ServiceInstance endService = (ServiceInstance) application.getServiceInstance(m_endInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);
            OutputPortInstance outPort = (OutputPortInstance) endService.getOutputPortInstances().get(0);

            clearSBWDocuments();

            System.out.println("Enabling The SBW for " + m_appGUID + m_version);
            System.out.println("Tracking type for the port ::" + inPort.getName() + " Of Service:: " + startService.getName() + " is WORKFLOW_DATA_MESSAGE");
            System.out.println("Tracking type for the port ::" + outPort.getName() + " Of Service:: " + endService.getName() + " is WORKFLOW_DATA_MESSAGE");

            m_fioranoApplicationController.enableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false, PortInstance.WORKFLOW_DATA_MESSAGE);
            m_fioranoApplicationController.enableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true, PortInstance.WORKFLOW_DATA_MESSAGE);
            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi, test the track", 1);
            waitTillExecuted();

            checkTracks(m_startInstance, PortInstance.WORKFLOW_DATA_MESSAGE);
            checkTracks(m_endInstance, PortInstance.WORKFLOW_DATA_MESSAGE);

            m_fioranoApplicationController.disableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false);
            m_fioranoApplicationController.disableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true);

            clearSBWDocuments();

            System.out.println("Enabling The SBW for " + m_appGUID + m_version);
            System.out.println("Tracking type for the port ::" + inPort.getName() + " Of Service:: " + startService.getName() + " is WORKFLOW_DATA_MESSAGE_HEADER");
            System.out.println("Tracking type for the port ::" + outPort.getName() + " Of Service:: " + endService.getName() + " is WORKFLOW_DATA_MESSAGE_HEADER");

            m_fioranoApplicationController.enableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false, PortInstance.WORKFLOW_DATA_MESSAGE_HEADER);
            m_fioranoApplicationController.enableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true, PortInstance.WORKFLOW_DATA_MESSAGE_HEADER);
            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi, test the track", 1);
            waitTillExecuted();

            checkTracks(m_startInstance, PortInstance.WORKFLOW_DATA_MESSAGE_HEADER);
            checkTracks(m_endInstance, PortInstance.WORKFLOW_DATA_MESSAGE_HEADER);

            m_fioranoApplicationController.disableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false);
            m_fioranoApplicationController.disableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true);

            clearSBWDocuments();

            System.out.println("Enabling The SBW for " + m_appGUID + m_version);
            System.out.println("Tracking type for the port ::" + inPort.getName() + " Of Service:: " + startService.getName() + " is WORKFLOW_DATA_MESSAGE_BODY");
            System.out.println("Tracking type for the port ::" + outPort.getName() + " Of Service:: " + endService.getName() + " is WORKFLOW_DATA_MESSAGE_BODY");

            m_fioranoApplicationController.enableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false, PortInstance.WORKFLOW_DATA_MESSAGE_BODY);
            m_fioranoApplicationController.enableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true, PortInstance.WORKFLOW_DATA_MESSAGE_BODY);
            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi, test the track", 1);
            waitTillExecuted();

            checkTracks(m_startInstance, PortInstance.WORKFLOW_DATA_MESSAGE_BODY);
            checkTracks(m_endInstance, PortInstance.WORKFLOW_DATA_MESSAGE_BODY);

            m_fioranoApplicationController.disableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false);
            m_fioranoApplicationController.disableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true);

            clearSBWDocuments();

            System.out.println("Enabling The SBW for " + m_appGUID + m_version);
            System.out.println("Tracking type for the port ::" + inPort.getName() + " Of Service:: " + startService.getName() + " is WORKFLOW_DATA_MESSAGE");
            System.out.println("Tracking type for the port ::" + outPort.getName() + " Of Service:: " + endService.getName() + " is WORKFLOW_DATA_APP_CONTEXT");

            m_fioranoApplicationController.enableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false, PortInstance.WORKFLOW_DATA_MESSAGE);
            m_fioranoApplicationController.enableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true, PortInstance.WORKFLOW_DATA_APP_CONTEXT);
            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi, test the track", 1);
            waitTillExecuted();

            checkTracks(m_startInstance, PortInstance.WORKFLOW_DATA_APP_CONTEXT);
            checkTracks(m_endInstance, PortInstance.WORKFLOW_DATA_APP_CONTEXT);

            m_fioranoApplicationController.disableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false);
            m_fioranoApplicationController.disableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testTrackingType", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testTrackingType", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    private void checkTracks(String serviceInstanceName, int trackingType) throws TifosiException {
        Enumeration docs = m_fioranoSBWManager.searchDocumentInfos(makeSBWSearchContext(serviceInstanceName), 0, -1);
        Assert.assertNotNull("document was supposed to be tracked at workflow item " + serviceInstanceName + "but was not tracked", docs);

        DocumentInfo di = (DocumentInfo) docs.nextElement();
        Assert.assertNotNull(di);
        TifosiDocument doc = m_fioranoSBWManager.getDocument(di.getDocumentID());

        if (trackingType == PortInstance.WORKFLOW_DATA_MESSAGE) {
            Assert.assertNotNull(doc.getText());
            Assert.assertNotNull(doc.getCompleteHeaderInformation());
            if (!serviceInstanceName.equals(m_startInstance))    //since the component1 doesn't not carry any app_context
                Assert.assertNotNull(doc.getCarryFwdContext());
        } else if (trackingType == PortInstance.WORKFLOW_DATA_MESSAGE_BODY) {
            Assert.assertNotNull(doc.getText());
            Assert.assertNotNull(doc.getCompleteHeaderInformation());
            if (serviceInstanceName.equals(m_startInstance))
                Assert.assertNull(doc.getCarryFwdContext());
        } else if (trackingType == PortInstance.WORKFLOW_DATA_MESSAGE_HEADER) {
            Assert.assertNull(doc.getText());
            Assert.assertNotNull(doc.getCompleteHeaderInformation());
            if (serviceInstanceName.equals(m_startInstance))
                Assert.assertNull(doc.getCarryFwdContext());
        } else if (trackingType == PortInstance.WORKFLOW_DATA_APP_CONTEXT) {
            if (serviceInstanceName.equals(m_startInstance))
                Assert.assertNotNull(doc.getText());
            Assert.assertNotNull(doc.getCompleteHeaderInformation());
            if (!serviceInstanceName.equals(m_startInstance))    //since the component1 doesn't not carry any app_context
                Assert.assertNotNull(doc.getCarryFwdContext());
        }

    }

    private SBWSearchContext makeSBWSearchContext(String serviceInstanceName) throws TifosiException {
        Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
        ServiceInstance startService = (ServiceInstance) application.getServiceInstance(serviceInstanceName);
        String serviceInstName = startService.getName();

        String fpsName = (String) m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstance(serviceInstName).getNodes()[0];
        if (fpsName == null || fpsName.trim().equals(""))
            fpsName = "fps";

        SBWSearchContext ctx = new SBWSearchContext();
        ctx.setAppGUID(m_appGUID);
        ctx.setPeerName(fpsName);
        ctx.setServiceInstance(serviceInstName);
        return ctx;
    }


    private void waitTillExecuted() throws TifosiException {
        int i = 0;
        Enumeration workflows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID,"EXECUTED", 0, -1);
        while (workflows.hasMoreElements()) {
            WorkflowInstanceInfo wfii = m_fioranoSBWManager.getWorkflowInstanceInfo((String) workflows.nextElement());

            if (wfii.getWorkflowStatus().equals(SBWConstants.STATE_EXECUTING)) {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                }
                workflows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID,"EXECUTED", 0, -1);
                if (++i == 61) return;
            }
            System.out.println(wfii.getWorflowInstID() + wfii.getWorkflowStatus());
        }
    }


    public void testSetSBWOnline() throws Exception {
        int count = 5;

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetSBWOnline", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            //get the Instance of the Application Set its Inport and outport as the workflow Items.
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) application.getServiceInstance(m_startInstance);
            ServiceInstance endService = (ServiceInstance) application.getServiceInstance(m_endInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);
            OutputPortInstance outPort = (OutputPortInstance) endService.getOutputPortInstances().get(0);

            System.out.println("Enabling The SBW for " + m_appGUID + m_version + " and the port ::" + inPort.getName() + " Of Service::" + startService.getName());
            m_fioranoApplicationController.enableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false);
            m_fioranoApplicationController.enableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true);

            clearSBWDocuments();
            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), getMessage(), count);
            waitTillExecuted();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetSBWOnline", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetSBWOnline", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testGetDestinationNameOfPort() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetDestinationNameOfPort", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) application.getServiceInstance(m_startInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);
            String destPort = m_fioranoApplicationController.getDestinationNameOfPort(m_appGUID,m_version, m_startInstance, inPort.getName());
            Assert.assertNotNull(destPort);
            System.out.println("dest Port name:" + destPort);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetDestinationNameOfPort", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetDestinationNameOfPort", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testSendMessageOnTopicPort() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSendMessageOnTopicPort", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) application.getServiceInstance(m_startInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);

            m_fioranoApplicationController.sendMessageOnTopicPort(m_appGUID,m_version, m_startInstance, inPort.getName());
            System.out.println("sent message to " + inPort.getName());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSendMessageOnTopicPort", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSendMessageOnTopicPort", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testRunService() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRunService", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            m_fioranoApplicationController.stopService(m_appGUID,m_version, m_startInstance);

            m_fioranoApplicationController.startService(m_appGUID,m_version, m_startInstance);
            Thread.sleep(3000);

            m_fioranoApplicationController.killService(m_appGUID,m_version, m_startInstance);

            m_fioranoApplicationController.synchronizeApplicationToLatestVersion(m_appGUID,m_version);
            Thread.sleep(5000);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRunService", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRunService", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }

    }


    public void testGetWorkflowInfos() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetWorkflowInfos", "SBWScenarioTest"));
            System.out.println("started the Execution of the TestCas::" + getName());

            //  int noOfEvents = m_fioranoSBWManager.getTotalNumberOfSBWEvents(m_appGUID);
            Enumeration workflowInstanceIds = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID,"EXECUTED", 1, -1);
            if (workflowInstanceIds == null)
                throw new Exception("No workflow instances captured.");

            Enumeration workflowInfos = m_fioranoSBWManager.getWorkflowInfos(workflowInstanceIds);
            if (workflowInfos == null)
                throw new Exception("No Workflow infos captured.");

            for (; workflowInfos.hasMoreElements();) {
                WorkflowInstanceInfo wfii = (WorkflowInstanceInfo) workflowInfos.nextElement();
                Assert.assertNotNull(wfii);
                System.out.println("Fetched workflow with id::" + wfii.getWorflowID() + "   &  workflow instance with id::" + wfii.getWorflowInstID());
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetWorkflowInfos", "SBWScenarioTest"));
            }

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetWorkflowInfos", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of" + ex.getMessage(), false);
        }
    }


    public void testGetDocumentInfo() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetDocumentInfo", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            int noOfEvents = m_fioranoSBWManager.getTotalNumberOfDocuments(m_appGUID,"m_version");
            Enumeration workFlows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID,"m_version", 1, noOfEvents);
            if (workFlows == null)
                throw new Exception("No Workflow instances captures.");
            int count = 0;
            for (; workFlows.hasMoreElements(); count++) {
                String workflowInstId = (String) workFlows.nextElement();
                WorkflowInstanceInfo wfii = m_fioranoSBWManager.getWorkflowInstanceInfo(workflowInstId);
                Assert.assertNotNull(wfii);
                System.out.println("Fetched workflowinstanceinfo with id::" + workflowInstId);

                Enumeration docs = m_fioranoSBWManager.getDocumentInfos(workflowInstId);
                while (docs.hasMoreElements()) {
                    DocumentInfo di = (DocumentInfo) docs.nextElement();
                    Assert.assertNotNull(di);

                    TifosiDocument docByName = m_fioranoSBWManager.getDocument(di.getDocumentID());
                    Assert.assertNotNull(docByName);
                    System.out.println("Fetched document with id::" + di.getDocumentID());

                    Enumeration docs2 = m_fioranoSBWManager.getDocumentInfos(m_appGUID, di.getOriginatingState());
                    Assert.assertNotNull(docs2);
                    System.out.println("Fetched document with state::" + di.getOriginatingState());
                }

                Enumeration trackedstates = m_fioranoSBWManager.getTrackedStatesOfWorkflowInst(workflowInstId);
                while (trackedstates.hasMoreElements()) {
                    StateInstance si = (StateInstance) trackedstates.nextElement();
                    Assert.assertNotNull(si);
                    System.out.println("Fetched state instance with id::" + si.getObjectID());
                }

                System.out.println("");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetDocumentInfo", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetDocumentInfo", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testSearchDocumentInfos() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSearchDocumentInfos", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            System.out.println("Searching the document tracked at workflow start item:");
            System.out.println("------------------------------------------------------");
            SBWSearchContext ctx = makeSBWSearchContext(m_startInstance);
            Enumeration docs = m_fioranoSBWManager.searchDocumentInfos(ctx, 0, -1);
            Assert.assertNotNull("for workflow start item document was not tracked", docs);
            while (docs.hasMoreElements()) {
                DocumentInfo di = (DocumentInfo) docs.nextElement();
                Assert.assertNotNull(di);

                System.out.println("Fetched document with id::" + di.getDocumentID());
                System.out.println("Fetched document with state::" + di.getOriginatingState());
            }

            System.out.println("Searching the document tracked at workflow end item:");
            System.out.println("------------------------------------------------------");
            ctx = makeSBWSearchContext(m_endInstance);
            docs = m_fioranoSBWManager.searchDocumentInfos(ctx, 0, -1);
            Assert.assertNotNull("for workflow end item document was not tracked", docs);
            while (docs.hasMoreElements()) {
                DocumentInfo di = (DocumentInfo) docs.nextElement();
                Assert.assertNotNull(di);

                System.out.println("Fetched document with id::" + di.getDocumentID());
                System.out.println("Fetched document with state::" + di.getOriginatingState());
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSearchDocumentInfos", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Exceution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSearchDocumentInfos", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase failed because of " + ex.getMessage(), false);
        }
    }


    public void testGetDocumentEventInfo() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetDocumentEventInfo", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            String servInstanceName = ((ServiceInstance) application.getServiceInstance(m_startInstance)).getName();
            Enumeration en = m_fioranoSBWManager.getDocumentEventInfo(m_appGUID,"m_version", servInstanceName);
            while (en.hasMoreElements()) {
                EventInfo ei = (EventInfo) en.nextElement();
                Assert.assertNotNull(ei);
                System.out.println("Fetched event with id::" + ei.getEventID());
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetDocumentEventInfo", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetDocumentEventInfo", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testGetActiveWorkflowInstances() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetActiveWorkflowInstances", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            int noOfEvents = m_fioranoSBWManager.getTotalNumberOfDocuments(m_appGUID,"m_version");
            Enumeration en = m_fioranoSBWManager.getActiveWorkflowInstances(m_appGUID,"m_version", SBWConstants.STATE_EXECUTING, 0, noOfEvents);
            while (en.hasMoreElements()) {
                String workflowInstId = (String) en.nextElement();
                WorkflowInstanceInfo wfii = m_fioranoSBWManager.getWorkflowInstanceInfo(workflowInstId);
                Assert.assertNotNull(wfii);
                System.out.println("Fetched active workflowinstanceinfo with id::" + workflowInstId);
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetActiveWorkflowInstances", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetActiveWorkflowInstances", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testFetchByUserDocID() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testFetchByUserDocID", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Enumeration en1 = m_fioranoSBWManager.getDocumentInfosByUsrDocID(m_UserDocID, m_appGUID,"m_version");
            Assert.assertTrue(en1.hasMoreElements());
            System.out.println("Fetched DocumentInfo with UsrDocId::" + m_UserDocID);

            //       Enumeration en2 = m_fioranoSBWManager.getDocumentInfosForStateByUsrDocID(m_UserDocID, SBWConstants.STATE_EXECUTING,m_appGUID);
            //        Assert.assertTrue(en2.hasMoreElements());
            System.out.println("Fetched DocumentInfo with UsrDocId::" + m_UserDocID + " and state::IN_PORT");
            WorkflowInstanceInfo wfii = m_fioranoSBWManager.getWorkflowInstanceInfoByUsrDocID(m_UserDocID, m_appGUID,"m_version");
            Assert.assertNotNull(wfii);
            System.out.println("Fetched WorkflowInstanceInfo with UsrDocId::" + m_UserDocID);
            Enumeration en3 = m_fioranoSBWManager.getTrackedStatesOfWorkflowInstByUsrDocID(m_UserDocID, m_appGUID,"m_version");
            Assert.assertTrue(en3.hasMoreElements());
            System.out.println("Fetched TrackedStatesofWorkflowInstance with UsrDocId::" + m_UserDocID);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testFetchByUserDocID", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testFetchByUserDocID", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testFetchByPartialDocID() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testFetchByPartialDocID", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Enumeration en = m_fioranoSBWManager.getDocumentInfosByUsrDocID("sir%", m_appGUID,"m_version");
            Assert.assertNotNull(en);
            Assert.assertTrue(en.hasMoreElements());
            System.out.println("Fetched DocumentInfo with UsrDocId::" + m_UserDocID);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testFetchByPartialDocID", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testFetchByPartialDocID", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testgetDocumentInfosForStateByUsrDocID() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testgetDocumentInfosForStateByUsrDocID", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            sendMessageOnInputPort(m_appGUID, "Component2", "IN_PORT", "hi, test the track again", 1);
            waitTillExecuted();

            Enumeration en = m_fioranoSBWManager.getDocumentInfosForStateByUsrDocID(m_UserDocID, SBWConstants.STATE_EXECUTING, m_appGUID,"m_version");
            Assert.assertNotNull(en);
            //  Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testgetDocumentInfosForStateByUsrDocID", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testgetDocumentInfosForStateByUsrDocID", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testGetTotalNumberOfSBWEvents() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetTotalNumberOfSBWEvents", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            int sbwEventCount = m_fioranoSBWManager.getTotalNumberOfSBWEvents(m_appGUID,"m_version");
            Assert.assertTrue(sbwEventCount > 0);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetTotalNumberOfSBWEvents", "SBWScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetTotalNumberOfSBWEvents", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    private void clearSBWDocuments() throws Exception {
        m_fioranoSBWManager.clearWorkflowInfo(m_appGUID,"m_version");
    }

    public void testGetSBWEventsOnlinePositive() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetSBWEventsOnlinePositive", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            int noOfEvents = m_fioranoSBWManager.getTotalNumberOfDocuments(m_appGUID,"m_version");
            Enumeration workFlows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID,"m_version", 1, noOfEvents);
            if (workFlows == null)
                throw new Exception("No Workflow instances captures.");
            int count = 0;
            for (; workFlows.hasMoreElements(); count++) {
                System.out.println("WORK FLOW INSTANCE::" + count + 1);
                Object obj = workFlows.nextElement();
                System.out.println("workFlow" + obj);
            }
            if (count != noOfEvents)
                throw new Exception("Received only " + count + " messges ,expecting " + noOfEvents + " messages.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetSBWEventsOnlinePositive", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetSBWEventsOnlinePositive", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testUnsetSBWOnline() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testUnsetSBWOnline", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            //get the Instance of the Application Set its Inport and outport as the workflow Items.
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) application.getServiceInstance(m_startInstance);
            ServiceInstance endService = (ServiceInstance) application.getServiceInstance(m_endInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);
            OutputPortInstance outPort = (OutputPortInstance) endService.getOutputPortInstances().get(0);

            System.out.println("Enabling The SBW for " + m_appGUID + m_version + " and the port ::" + inPort.getName() + " Of Service::" + startService.getName());
            //todo remove the hardcoding of the workflow end port
            m_fioranoApplicationController.disableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false);
            m_fioranoApplicationController.disableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testUnsetSBWOnline", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testUnsetSBWOnline", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    public void testGetSBWEventsOnlineNegative() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetSBWEventsOnlineNegative", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            //clear the SBWEvents

            clearSBWDocuments();
            //send the some messages
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) application.getServiceInstance(m_startInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);
            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), getMessage(), 5);
            int noOfEvents = m_fioranoSBWManager.getTotalNumberOfDocuments(m_appGUID,"m_version");
            Enumeration workFlows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID,"m_version", 1, noOfEvents);
            if (workFlows == null)
                throw new Exception("No Workflow instances captures.");
            int count = 0;
            for (; workFlows.hasMoreElements(); count++) {
                System.out.println("WORK FLOW INSTANCE::" + count + 1);
                Object obj = workFlows.nextElement();
                System.out.println("workFlow" + obj);
            }
            if (count > 0)
                throw new Exception("Recieved " + count + " messges ,No messages are to be recieved after Unsetting SBW.");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetSBWEventsOnlineNegative", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetSBWEventsOnlineNegative", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testSetSBWOffline() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetSBWOffline", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            //Application application = m_fioranoApplicationController.getApplication(m_appGUID,m_version);
            //todo complete the test
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetSBWOffline", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetSBWOffline", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    private String getMessage() {
        return "Test Message";
    }

    private void sendMessageOnInputPort(String m_appGUID, String serviceInstName, String portName, String message, int count) throws Exception {
        STFQueueSender publisher = null;

        System.out.println("Sending the message \n" + message);
        publisher = new STFQueueSender();

        String fpsName = (String) m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstance(serviceInstName).getNodes()[0];
        if (fpsName == null || fpsName.trim().equals(""))
            fpsName = "fps";
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
            throw new FioranoException("Error launching the application");
        }
    }

    public void testCheckPermissions() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCheckPermissions", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            FioranoDeploymentManager deploymentManager = rtlClient.getFioranoDeploymentManager();
            Assert.assertNotNull(deploymentManager);

            String peername = "fps";
            Enumeration en = rtlClient.getFioranoFPSManager().getAllInstalledServices(peername);
            Assert.assertTrue(en.hasMoreElements());
            ServiceInfo si = (ServiceInfo) en.nextElement();
            String serviceGUID = si.getServiceHeader().getGUID();
            String serviceVersion = String.valueOf(si.getServiceHeader().getVersion());
            String serviceLabel = si.getServiceHeader().getLabel();
            String appVersion = String.valueOf(m_version);
            String appLabel = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getLabel();

            //check. there is no getLabel!!
            String peerlabel = rtlClient.getFioranoFPSManager().getTPSProperties(peername).getTPSName();

            int result = -1;

            result = deploymentManager.checkCompositionPermission(serviceGUID, serviceVersion, serviceLabel
                    , m_appGUID, appVersion, appLabel);
            Assert.assertTrue(result == 0 || result == 1 || result == 2);

            result = -1;
            result = deploymentManager.checkDeploymentPermission(serviceGUID, serviceVersion, serviceLabel, m_appGUID,
                    appVersion, appLabel, peername, peerlabel);
            Assert.assertTrue(result == 0 || result == 1 || result == 2);

            DeploymentRule dr1 = new DeploymentRule();
            dr1.setRuleID("test");
            dr1.setType(true);
            deploymentManager.addDeploymentRule(dr1);

            DeploymentRule[] drList1 = deploymentManager.getDeploymentRules();
            Assert.assertTrue(drList1.length > 0);
            DeploymentRule dr = drList1[0];

            result = -1;
            result = deploymentManager.checkCompositionPermissionForRule(dr.getRuleID(), serviceGUID, serviceVersion,
                    serviceLabel, m_appGUID, appVersion, appLabel);
            Assert.assertTrue(result == 0 || result == 1 || result == 2);

            result = -1;
            result = deploymentManager.checkDeploymentPermissionForRule(dr.getRuleID(), serviceGUID, serviceVersion,
                    serviceLabel, m_appGUID, appVersion, appLabel, peername, peerlabel);
            Assert.assertTrue(result == 0 || result == 1 || result == 2);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCheckPermissions", "SBWScenarioTest"));

            //what to verify?
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCheckPermissions", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testChangeLoggingParameters() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testChangeLoggingParameters", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            LogManager lm = new LogManager();
            lm.setLoggerClass("NewLogger");
            m_fioranoApplicationController.changeLoggingParameters(m_startInstance, m_appGUID,m_version, lm);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testChangeLoggingParameters", "SBWScenarioTest"));

            //To verify
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testChangeLoggingParameters", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    /*public void testgetErrorWorkflowCount() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            m_manager.getErrorWorkflowCount(m_appGUID);

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }

    }       */

    public void testgetTESVersion() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testgetTESVersion", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Hashtable version = m_fesmanager.getTESVersion();
            System.out.println("The Version is " + version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testgetTESVersion", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testgetTESVersion", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testgetProviderMajorVersion() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testgetProviderMajorVersion", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            String Mversion = m_fesmanager.getProviderMajorVersion();
            System.out.println("The Version is " + Mversion);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testgetProviderMajorVersion", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testgetProviderMajorVersion", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testgetProviderMinorVersion() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testgetProviderMinorVersion", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            String Minorversion = m_fesmanager.getProviderMinorVersion();
            System.out.println("The Version is " + Minorversion);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testgetProviderMinorVersion", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testgetProviderMinorVersion", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "SBWScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID)) {
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
                Thread.sleep(2000);
            }
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
             m_fioranoApplicationController.close();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "SBWScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "SBWScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of TESTCHANGELOGGINGPARAMETERS" + ex.getMessage(), false);

        }

    }


    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testCreateSBWApplication");
        methodsOrder.add("testTrackingType");
        methodsOrder.add("testSetSBWOnline");
        methodsOrder.add("testGetDestinationNameOfPort");
        methodsOrder.add("testSendMessageOnTopicPort");
        methodsOrder.add("testRunService");
        methodsOrder.add("testGetWorkflowInfos");
        methodsOrder.add("testGetDocumentInfo");
        methodsOrder.add("testSearchDocumentInfos");
        methodsOrder.add("testGetDocumentEventInfo");
        methodsOrder.add("testGetActiveWorkflowInstances");
        methodsOrder.add("testFetchByUserDocID");
       methodsOrder.add("testFetchByPartialDocID");
        methodsOrder.add("testgetDocumentInfosForStateByUsrDocID");
        methodsOrder.add("testGetTotalNumberOfSBWEvents");
        methodsOrder.add("testGetSBWEventsOnlinePositive");
        methodsOrder.add("testUnsetSBWOnline");
        methodsOrder.add("testGetSBWEventsOnlineNegative");
        methodsOrder.add("testSetSBWOffline");
       methodsOrder.add("testCheckPermissions");
        methodsOrder.add("testChangeLoggingParameters");
        //methodsOrder.add("testgetErrorWorkflowCount");
       methodsOrder.add("testgetTESVersion");
        methodsOrder.add("testgetProviderMajorVersion");
        methodsOrder.add("testgetProviderMinorVersion");
        methodsOrder.add("testDeleteApplication");


        return methodsOrder;
    }
}
