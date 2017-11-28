package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.dm.FioranoDeploymentManager;
import com.fiorano.esb.rtl.sbw.FioranoSBWManager;
import com.fiorano.esb.sbw.handler.SBWManager;
import com.fiorano.esb.server.impl.FESServer;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.jms.STFQueueSender;
import fiorano.esb.util.CarryForwardContext;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.SBWSearchContext;
import fiorano.tifosi.dmi.ServiceInfo;
import fiorano.tifosi.dmi.application.*;
import fiorano.tifosi.dmi.dm.DeploymentRule;
import fiorano.tifosi.dmi.ste.*;
import fiorano.tifosi.tps.rtl.TifosiDocument;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/20/11
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSWScenario extends AbstractTestNG{
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
    private String m_UserDocID = "siren";

    private SBWManager m_manager ;
    static boolean isModifiedOnceHA = false;//to check the files overwriting
    static boolean isModifiedOnce = false;
    private FilenameFilter xmlFilter;//file extracter

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_appGUID1 = testProperties.getProperty("ApplicationGUID1");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_appFile1 = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile1", "SBWCHAT_1.0.xml");
        m_startInstance = testProperties.getProperty("WorkFlowStartInstance");
        m_endInstance = testProperties.getProperty("WorkFlowEndInstance");
        m_startInstance1 = testProperties.getProperty("Instance1");
        m_endInstance1 = testProperties.getProperty("Instance2");
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID1::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("Application GUID1::       " + m_appGUID1 + "  Version Number::" + m_version);
        System.out.println("Application File1" + m_appFile1);
        System.out.println("_____________________________________________________________________________");
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

    @BeforeClass(groups = "SWScenarioTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "SBWScenario" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "SBWScenario";
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
        m_fioranoSBWManager = rtlClient.getFioranoSBWManager();
        m_fesmanager = new FESServer();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            Assert.fail("could not initialise properties");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testCreateSBWApplication() {
        // This application is created form the SIMPLECHAT Application, and then setting the Workflow on ports. etc.
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateSBWApplication", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("The App File is::" + m_appFile);
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateSBWApplication", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateSBWApplication", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testCreateSBWApplication")
    public void testTrackingType() {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testTrackingType", "TestSBWScenario"));
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

            m_fioranoApplicationController.enableSBW(startService.getName(), m_appGUID, m_version,inPort.getName(), false, PortInstance.WORKFLOW_DATA_MESSAGE);
            m_fioranoApplicationController.enableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true, PortInstance.WORKFLOW_DATA_APP_CONTEXT);
            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi, test the track", 1);
            waitTillExecuted();

            checkTracks(m_startInstance, PortInstance.WORKFLOW_DATA_APP_CONTEXT);
            checkTracks(m_endInstance, PortInstance.WORKFLOW_DATA_APP_CONTEXT);

            m_fioranoApplicationController.disableSBW(startService.getName(), m_appGUID,m_version, inPort.getName(), false);
            m_fioranoApplicationController.disableSBW(endService.getName(), m_appGUID,m_version, outPort.getName(), true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testTrackingType", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testTrackingType", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testTrackingType")
    public void testSetSBWOnline() throws Exception {
        int count = 5;

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetSBWOnline", "TestSBWScenario"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetSBWOnline", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetSBWOnline", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testSetSBWOnline")
    public void testGetDestinationNameOfPort() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetDestinationNameOfPort", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) application.getServiceInstance(m_startInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);
            String destPort = m_fioranoApplicationController.getDestinationNameOfPort(m_appGUID,m_version, m_startInstance, inPort.getName());
            Assert.assertNotNull(destPort);
            System.out.println("dest Port name:" + destPort);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetDestinationNameOfPort", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetDestinationNameOfPort", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetDestinationNameOfPort")
    public void testSendMessageOnTopicPort() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSendMessageOnTopicPort", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) application.getServiceInstance(m_startInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);

            m_fioranoApplicationController.sendMessageOnTopicPort(m_appGUID,m_version, m_startInstance, inPort.getName());
            System.out.println("sent message to " + inPort.getName());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSendMessageOnTopicPort", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessageOnTopicPort", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testSendMessageOnTopicPort")
    public void testRunService() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunService", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            m_fioranoApplicationController.stopService(m_appGUID,m_version,m_startInstance);

            m_fioranoApplicationController.startService(m_appGUID,m_version, m_startInstance);
            Thread.sleep(3000);

            m_fioranoApplicationController.killService(m_appGUID,m_version, m_startInstance);

            m_fioranoApplicationController.synchronizeApplicationToLatestVersion(m_appGUID,m_version);
            Thread.sleep(5000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunService", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunService", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }

    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testRunService")
    public void testGetWorkflowInfos() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetWorkflowInfos", "TestSBWScenario"));
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
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetWorkflowInfos", "TestSBWScenario"));
            }

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetWorkflowInfos", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of" + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetWorkflowInfos")
    public void testGetDocumentInfo() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetDocumentInfo", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            int noOfEvents = m_fioranoSBWManager.getTotalNumberOfDocuments(m_appGUID,String.valueOf(m_version));
            Enumeration workFlows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID,"EXECUTED", 1, noOfEvents);
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetDocumentInfo", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetDocumentInfo", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetDocumentInfo")
    public void testSearchDocumentInfos() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSearchDocumentInfos", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            System.out.println("Searching the document tracked at workflow start item:");
            System.out.println("------------------------------------------------------");
            SBWSearchContext ctx = makeSBWSearchContext(m_startInstance);
            Enumeration docs = m_fioranoSBWManager.searchDocumentInfos(ctx, 0, -1);
            Assert.assertNotNull(docs, "for workflow start item document was not tracked");
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
            Assert.assertNotNull(docs, "for workflow start item document was not tracked");
            while (docs.hasMoreElements()) {
                DocumentInfo di = (DocumentInfo) docs.nextElement();
                Assert.assertNotNull(di);

                System.out.println("Fetched document with id::" + di.getDocumentID());
                System.out.println("Fetched document with state::" + di.getOriginatingState());
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSearchDocumentInfos", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Exceution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSearchDocumentInfos", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testSearchDocumentInfos")
    public void testGetDocumentEventInfo() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetDocumentEventInfo", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            String servInstanceName = ((ServiceInstance) application.getServiceInstance(m_startInstance)).getName();
            Enumeration en = m_fioranoSBWManager.getDocumentEventInfo(m_appGUID,String.valueOf(m_version), servInstanceName);
            while (en.hasMoreElements()) {
                EventInfo ei = (EventInfo) en.nextElement();
                Assert.assertNotNull(ei);
                System.out.println("Fetched event with id::" + ei.getEventID());
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetDocumentEventInfo", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetDocumentEventInfo", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetDocumentEventInfo")
    public void testGetActiveWorkflowInstances() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetActiveWorkflowInstances", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            int noOfEvents = m_fioranoSBWManager.getTotalNumberOfDocuments(m_appGUID,String.valueOf(m_version));
            Enumeration en = m_fioranoSBWManager.getActiveWorkflowInstances(m_appGUID,String.valueOf(m_version), SBWConstants.STATE_EXECUTING, 0, noOfEvents);
            while (en.hasMoreElements()) {
                String workflowInstId = (String) en.nextElement();
                WorkflowInstanceInfo wfii = m_fioranoSBWManager.getWorkflowInstanceInfo(workflowInstId);
                Assert.assertNotNull(wfii);
                System.out.println("Fetched active workflowinstanceinfo with id::" + workflowInstId);
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetActiveWorkflowInstances", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetActiveWorkflowInstances", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetActiveWorkflowInstances")
    public void testFetchByUserDocID() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testFetchByUserDocID", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Enumeration en1 = m_fioranoSBWManager.getDocumentInfosByUsrDocID(m_UserDocID, m_appGUID,String.valueOf(m_version));
            Assert.assertTrue(en1.hasMoreElements());
            System.out.println("Fetched DocumentInfo with UsrDocId::" + m_UserDocID);

            //       Enumeration en2 = m_fioranoSBWManager.getDocumentInfosForStateByUsrDocID(m_UserDocID, SBWConstants.STATE_EXECUTING,m_appGUID);
            //        Assert.assertTrue(en2.hasMoreElements());
            System.out.println("Fetched DocumentInfo with UsrDocId::" + m_UserDocID + " and state::IN_PORT");
            WorkflowInstanceInfo wfii = m_fioranoSBWManager.getWorkflowInstanceInfoByUsrDocID(m_UserDocID, m_appGUID,String.valueOf(m_version));
            Assert.assertNotNull(wfii);
            System.out.println("Fetched WorkflowInstanceInfo with UsrDocId::" + m_UserDocID);
            Enumeration en3 = m_fioranoSBWManager.getTrackedStatesOfWorkflowInstByUsrDocID(m_UserDocID, m_appGUID,String.valueOf(m_version));
            Assert.assertTrue(en3.hasMoreElements());
            System.out.println("Fetched TrackedStatesofWorkflowInstance with UsrDocId::" + m_UserDocID);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testFetchByUserDocID", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testFetchByUserDocID", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testFetchByUserDocID")
    public void testFetchByPartialDocID() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testFetchByPartialDocID", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Enumeration en = m_fioranoSBWManager.getDocumentInfosByUsrDocID("sir%", m_appGUID, String.valueOf(m_version));
            Assert.assertNotNull(en);
            Assert.assertTrue(en.hasMoreElements());
            System.out.println("Fetched DocumentInfo with UsrDocId::" + m_UserDocID);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testFetchByPartialDocID", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testFetchByPartialDocID", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testFetchByPartialDocID")
    public void testGetDocumentInfosForStateByUsrDocID() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testgetDocumentInfosForStateByUsrDocID", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            sendMessageOnInputPort(m_appGUID, "Component2", "IN_PORT", "hi, test the track again", 1);
            waitTillExecuted();

            Enumeration en = m_fioranoSBWManager.getDocumentInfosForStateByUsrDocID(m_UserDocID, SBWConstants.STATE_EXECUTING, m_appGUID,String.valueOf(m_version));
            Assert.assertNotNull(en);
            //  Assert.assertTrue(en.hasMoreElements());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testgetDocumentInfosForStateByUsrDocID", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testgetDocumentInfosForStateByUsrDocID", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetDocumentInfosForStateByUsrDocID")
    public void testGetTotalNumberOfSBWEvents() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetTotalNumberOfSBWEvents", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            int sbwEventCount = m_fioranoSBWManager.getTotalNumberOfSBWEvents(m_appGUID,String.valueOf(m_version));
            Assert.assertTrue(sbwEventCount > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetTotalNumberOfSBWEvents", "TestSBWScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTotalNumberOfSBWEvents", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetTotalNumberOfSBWEvents")
    public void testGetSBWEventsOnlinePositive() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetSBWEventsOnlinePositive", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            int noOfEvents = m_fioranoSBWManager.getTotalNumberOfDocuments(m_appGUID,String.valueOf(m_version));
            Enumeration workFlows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID,String.valueOf(m_version), 1, noOfEvents);
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetSBWEventsOnlinePositive", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetSBWEventsOnlinePositive", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetSBWEventsOnlinePositive")
    public void testUnsetSBWOnline() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testUnsetSBWOnline", "TestSBWScenario"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testUnsetSBWOnline", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testUnsetSBWOnline", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testUnsetSBWOnline")
    public void testGetSBWEventsOnlineNegative() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetSBWEventsOnlineNegative", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            //clear the SBWEvents

            clearSBWDocuments();
            //send the some messages
            Application application = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) application.getServiceInstance(m_startInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);
            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), getMessage(), 5);
            int noOfEvents = m_fioranoSBWManager.getTotalNumberOfDocuments(m_appGUID,String.valueOf(m_version));
            Enumeration workFlows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID,String.valueOf(m_version), 1, noOfEvents);
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetSBWEventsOnlineNegative", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetSBWEventsOnlineNegative", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetSBWEventsOnlineNegative")
    public void testSetSBWOffline() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetSBWOffline", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            //Application application = m_fioranoApplicationController.getApplication(m_appGUID,m_version);
            //todo complete the test
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetSBWOffline", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetSBWOffline", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testSetSBWOffline")
    public void testCheckPermissions() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCheckPermissions", "TestSBWScenario"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCheckPermissions", "TestSBWScenario"));

            //what to verify?
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCheckPermissions", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testCheckPermissions")
    public void testChangeLoggingParameters() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testChangeLoggingParameters", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            LogManager lm = new LogManager();
            lm.setLoggerClass("NewLogger");
            m_fioranoApplicationController.changeLoggingParameters(m_startInstance, m_appGUID,m_version, lm);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testChangeLoggingParameters", "TestSBWScenario"));

            //To verify
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangeLoggingParameters", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
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
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }

    }       */

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testChangeLoggingParameters")
    public void testGetTESVersion() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testgetTESVersion", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Hashtable version = m_fesmanager.getTESVersion();
            System.out.println("The Version is " + version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testgetTESVersion", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testgetTESVersion", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetTESVersion")
    public void testGetProviderMajorVersion() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testgetProviderMajorVersion", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            String Mversion = m_fesmanager.getProviderMajorVersion();
            System.out.println("The Version is " + Mversion);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testgetProviderMajorVersion", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testgetProviderMajorVersion", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetProviderMajorVersion")
    public void testGetProviderMinorVersion() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testgetProviderMinorVersion", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            String Minorversion = m_fesmanager.getProviderMinorVersion();
            System.out.println("The Version is " + Minorversion);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testgetProviderMinorVersion", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testgetProviderMinorVersion", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SWScenarioTest", alwaysRun = true, dependsOnMethods = "testGetProviderMinorVersion")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestSBWScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID)) {
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
                Thread.sleep(2000);
            }
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
             m_fioranoApplicationController.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestSBWScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestSBWScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of TESTCHANGELOGGINGPARAMETERS" + ex.getMessage());

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

        String queueName = m_appGUID + "__" + "1_0" + "__" + serviceInstName + "__" + portName;
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
            m_fioranoApplicationController.startAllServices(appGuid,m_version);
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

    private void clearSBWDocuments() throws Exception {
        m_fioranoSBWManager.clearWorkflowInfo(m_appGUID,String.valueOf(m_version));
    }

    private void checkTracks(String serviceInstanceName, int trackingType) throws TifosiException {
        Enumeration docs = m_fioranoSBWManager.searchDocumentInfos(makeSBWSearchContext(serviceInstanceName), 0, -1);
        Assert.assertNotNull(docs, "document was supposed to be tracked at workflow item " + serviceInstanceName + "but was not tracked");

        DocumentInfo di = (DocumentInfo) docs.nextElement();
        Assert.assertNotNull(di);
        TifosiDocument doc = m_fioranoSBWManager.getDocument(di.getDocumentID());

        if (trackingType == PortInstance.WORKFLOW_DATA_MESSAGE) {
            Assert.assertNotNull(doc.getText());
            Assert.assertNotNull(doc.getCompleteHeaderInformation());
            if (!serviceInstanceName.equals(m_startInstance))    //since the component1 doesn't not carry any app_context
            {
                Assert.assertNotNull(doc.getCarryFwdContext());
            }
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
        Enumeration workflows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID, String.valueOf(m_version), 0, -1);
        while (workflows.hasMoreElements()) {
            WorkflowInstanceInfo wfii = m_fioranoSBWManager.getWorkflowInstanceInfo((String) workflows.nextElement());

            if (wfii.getWorkflowStatus().equals(SBWConstants.STATE_EXECUTING)) {
                try {
                    Thread.sleep(1000);
                }
                catch (Exception e) {
                }
                workflows = m_fioranoSBWManager.getAllWorkflowInstances(m_appGUID, String.valueOf(m_version), 0, -1);
                if (++i == 61) return;
            }
            System.out.println(wfii.getWorflowInstID() + wfii.getWorkflowStatus());
        }
    }
}
