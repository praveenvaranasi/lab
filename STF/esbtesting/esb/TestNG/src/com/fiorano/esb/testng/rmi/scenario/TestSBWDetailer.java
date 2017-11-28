package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.sbw.FioranoSBWManager;
import com.fiorano.esb.sbw.handler.SBWManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.stf.jms.STFQueueSender;
import com.fiorano.stf.remote.JMXClient;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.SBWSearchContext;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.InputPortInstance;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.ste.DocumentInfo;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/24/11
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestSBWDetailer extends AbstractTestNG{
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

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID1::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }    
    
    private void init() {
        m_appGUID = testProperties.getProperty("ApplicationGUID1");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile1", "SBWCHAT_1.0.xml");
        m_startInstance = testProperties.getProperty("Instance1");
        m_endInstance = testProperties.getProperty("Instance2");
    }
    
    @BeforeClass(groups = "SBWDetailerTest", alwaysRun = true)
    public void startSetUp(){
        if (isReady == true) return;
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "SBWDetailer" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "SBWDetailer";
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
        printInitParams();
    }

    @Test(groups = "SBWDetailerTest", alwaysRun = true, dependsOnMethods = "startSetUp")
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
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "SBWDetailerTest", alwaysRun = true, dependsOnMethods = "testGetSBWStatusForAllApps")
    public void testGetGeneralInfo() throws Exception {
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
            Assert.assertNotNull(docs, "document was supposed to be tracked at workflow item chat1 but was not tracked");
            DocumentInfo di = null;
            if(docs!=null && docs.hasMoreElements()){
                di = (DocumentInfo) docs.nextElement();
                Assert.assertNotNull(di);
            }
            else{
                Assert.fail("Docs has no elementS");
            }

            Object[] params = {di.getDocumentID()};
            String[] signatures = {String.class.getName()};
            HashMap geninfo = (HashMap) jmxClient.invoke(objName1, "getSerializedTifosiDocument", params, signatures);
            Assert.assertNotNull(geninfo);
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SBWDetailerTest", alwaysRun = true, dependsOnMethods = "testGetGeneralInfo")
    public void testCountAllWorkFlowInstancesForApp() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            Object[] params = {m_appGUID,String.valueOf(m_version)};
            String[] signatures = {String.class.getName(),String.class.getName()};
            jmxClient.invoke(objName1, "countAllWorkFlowInstancesForApp", params, signatures);


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SBWDetailerTest", alwaysRun = true, dependsOnMethods = "testCountAllWorkFlowInstancesForApp")
    public void testPurgeDocumentsByName() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application appn = m_applicationcontroller.getApplication(m_appGUID, m_version);
            ServiceInstance startService = (ServiceInstance) appn.getServiceInstance(m_endInstance);
            InputPortInstance inPort = (InputPortInstance) startService.getInputPortInstances().get(0);


            sendMessageOnInputPort(m_appGUID, startService.getName(), inPort.getName(), "hi test the track again", 2);
            SBWSearchContext ctx = makeSBWSearchContext(m_endInstance);

            Enumeration docs = m_fioranoSBWManager.searchDocumentInfos(ctx, 0, -1);
            Assert.assertNotNull(docs, "document was supposed to be tracked at workflow item chat1 but was not tracked");
            
            ArrayList docIdlist = new ArrayList();
            while (docs.hasMoreElements()) {
                DocumentInfo di = (DocumentInfo) docs.nextElement();
                docIdlist.add(di.getDocumentID());
            }

            Object[] params = {handleId, docIdlist};
            String[] signatures = {String.class.getName(), ArrayList.class.getName()};
            jmxClient.invoke(objName1, "purgeDocumentsByName", params, signatures);


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SBWDetailerTest", alwaysRun = true, dependsOnMethods = "testPurgeDocumentsByName")
    public void testCountSearchedDocuments() throws Exception {
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
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SBWDetailerTest", alwaysRun = true, dependsOnMethods = "testCountSearchedDocuments")
    public void testPurgeDocuments() throws Exception {
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

            Object[] params = {handleId, m_appGUID,String.valueOf(m_version), userDocID,wrkflwInstID,documentID, startService.getName(), inPort.getName(),peer, status, from, to};
            String[] signatures = {String.class.getName(),String.class.getName(), String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(),Date.class.getName(),Date.class.getName()};
            jmxClient.invoke(objName1, "purgeDocuments", params, signatures);


        }
        catch(Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SBWDetailerTest", alwaysRun = true, dependsOnMethods = "testPurgeDocuments")
    public void testGetClientJars() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());


            String fiorano_home = System.getProperty("FIORANO_HOME");

            String sourceResourcePath = fiorano_home + fsc + "clientJars";
            Object[] params = {sourceResourcePath, false};
            String[] signatures = {String.class.getName(), boolean.class.getName()};
            Object clientjar = jmxClient.invoke(objName4, "getClientJars", params, signatures);
            Assert.assertNotNull(clientjar);

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "SBWDetailerTest", alwaysRun = true, dependsOnMethods = "testGetClientJars")
    public void testGetAllWorkFlowInstancesForApp() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        int fromind = 0;
        int toind = 1;
        Object[] params = {m_appGUID, String.valueOf(m_version), fromind, toind};
        String[] signatures = {String.class.getName(),String.class.getName(), int.class.getName(), int.class.getName()};
        try {
            ArrayList workFlowList = (ArrayList) jmxClient.invoke(objName1, "getAllWorkFlowInstancesForApp", params, signatures);
            Assert.assertNotNull(workFlowList);
        } catch (ReflectionException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "SBWDetailerTest", alwaysRun = true, dependsOnMethods = "testGetAllWorkFlowInstancesForApp")
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
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
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
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }

    }

    private SBWSearchContext makeSBWSearchContext(String m_startInst) {
        SBWSearchContext ctx = new SBWSearchContext();
        ctx.setAppGUID(m_appGUID);
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

        String queueName = m_appGUID + "__"+"1_0" +"__" + serviceInstName + "__" + portName;
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
}
