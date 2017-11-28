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

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.jms.STFTopicPublisher;
import com.fiorano.stf.jms.STFTopicSubscriber;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.misc.JUnitDebuger;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.OutputPortInstance;
import fiorano.tifosi.dmi.application.ServiceInstance;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.jms.Message;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Sep 8, 2006
 * Time: 11:55:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventInterceptorTest extends DRTTestCase {

    private FioranoApplicationController m_fioranoApplicationController;
    private FioranoFPSManager m_fioranoFPSManager;
    private boolean m_initialised = false;
    private float m_version;
    private String m_appGUID;
    private String m_routeGUID;
    private String resourceFilePath;
    private String m_inputFileName = null;
    private boolean applicatioStarted = false;
    private STFTopicSubscriber m_listener;
    private String m_appFile;

    private String m_appGUID2;
    private String m_routeGUID2;
    private float m_version2;
    private String m_appFile2;

    static boolean isModifiedOnceHA = false;//to check the files overwriting
    static boolean isModifiedOnce = false;
    private FilenameFilter xmlFilter;//xml file filter

    public EventInterceptorTest(String name) {
        super(name);
    }


    public EventInterceptorTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    private void setXmlFilter(String ext)//creating the xml extension files
    {
        /* xmlFilter=new OnlyExt(ext); */
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
        if (!m_initialised) {
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "EventInterceptor";
            m_fioranoFPSManager = rtlClient.getFioranoFPSManager();
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();

            //new changes
            ServerStatusController stc;
            stc = ServerStatusController.getInstance();
            /* boolean isFPSHA=stc.getFPSHA();
             setXmlFilter("xml");
          if(isFPSHA && !isModifiedOnceHA)
         {
             isModifiedOnceHA=true;

             modifyXmlFiles(resourceFilePath,"fps_test","fps_ha");

         }
         else if(!isFPSHA && !isModifiedOnce)
         {
             isModifiedOnce=true;
             modifyXmlFiles(resourceFilePath,"fps_ha","fps_test");

         }   */
            init();
            m_initialised = true;
        }
    }

    private void init() throws Exception {
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_routeGUID = testCaseProperties.getProperty("RouteGUID");
        m_inputFileName = resourceFilePath + File.separator + testCaseProperties.getProperty("InputFile");
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationXML", "1.0.xml");

        m_appGUID2 = testCaseProperties.getProperty("ApplicationGUID2");
        m_version2 = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion2"));
        m_routeGUID2 = testCaseProperties.getProperty("RouteGUID2");
        m_appFile2 = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationXML2", "2.0.xml");
        printInitParams();
    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("..................................................................");
    }

    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * trying to import an application with Transformation.
     */
    public void testImportApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testImportApplication", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testImportApplication", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testImportApplication", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    private void startApplication() throws Exception {
        if (applicatioStarted)
            return;
        try {
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);
            m_fioranoApplicationController.launchApplication(m_appGUID, m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
            applicatioStarted = true;
        } catch (TifosiException e) {
            System.out.println("Application Already Started");
        }
    }

    public void testSetDebugger() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetDebugger", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication();
            Thread.sleep(10000);
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, interceptor);
            System.out.println("Debugger Set");
            //postCondition check whether the debugger is properly set.
            // i.e remove debugger should not throw an Exception.
            Thread.sleep(10000);
            try {
                m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, false, false, 10000L);
                Thread.sleep(10000);
            } catch (Exception ex) {
                System.err.println("Exception Caught" + ex.getMessage());
                ex.printStackTrace();
                throw new Exception("Event Interceptor not set properly.. ");
            }
            //execution of the above statement is expected to throw an Exception
            Assert.assertTrue(true);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetDebugger", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testRemoveDebugger() throws Exception {
        //The Test case is expected to fail. As the debugger is completely handled in the RTL Side -- 11th Sep. 2006
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveDebugger", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            //String routeName=m_fioranoApplicationController.getRoute(m_appGUID,m_routeGUID).getRouteName();
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, true, false, 10000L);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            //an exception is expected here as the debugger is already removed from testSetDebugger().
            //if exception with proper message is caught then pass the test.

            if ((ex instanceof TifosiException) && ((ex.getMessage()).indexOf("Debugger doesnt Exist on the route") != -1)) {
                Assert.assertTrue(true);
            } else {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveDebugger", "EventInterceptorTest"), ex);
                Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
            }
        }
    }

//     added for the sake of the bug 12996 STARTS
//    public void testLaunchEPAfterAddingBreakPoint(){
//        try{
//            System.out.println("Started the Execution of the TestCase::"+getName());
//            m_fioranoApplicationController.killApplication(m_appGUID);
//
//            /* set the debugger and stop the process */
//            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID,m_routeGUID,enterpriseServiceProvider);
//            m_fioranoApplicationController.setDebugger(m_routeGUID,m_appGUID,interceptor);
//
//            /* launch the event process with debugger */
//            m_fioranoApplicationController.prepareLaunch(m_appGUID,m_version);
//            m_fioranoApplicationController.launchApplication(m_appGUID,m_version);
//            m_fioranoApplicationController.startAllServices(m_appGUID);
//
//            /* remove debugger for the sake of other tests */
//            m_fioranoApplicationController.removeDebugger(m_routeGUID,m_appGUID,true,false,10000L);
//        }
//        catch(Exception ex){
//            System.err.println("Exception in the Execution of test case::"+getName());
//            ex.printStackTrace();
//            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);
//        }
//    }
    /* added for the sake of the bug 12996 ENDS */

    private void send() throws Exception {
        String srcInstance = m_fioranoApplicationController.getRoute(m_appGUID,m_version, m_routeGUID).getSourceServiceInstance();
        String srcPort = m_fioranoApplicationController.getRoute(m_appGUID, m_version,m_routeGUID).getSourcePortInstance();
        String trgInstance = m_fioranoApplicationController.getRoute(m_appGUID,m_version, m_routeGUID).getTargetServiceInstance();
        String destination = m_appGUID + "__" + srcInstance + "__" + srcPort;
        String fpsNames[] = {"FPS"};
        Iterator itr = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstances().iterator();
        while (itr.hasNext()) {
            ServiceInstance service = (ServiceInstance) itr.next();
            if (service.getName().equals(srcInstance)) {
                fpsNames = service.getNodes();
                System.out.println("THE NODE USED::" + fpsNames);
                break;
            }
        }
        createListener(trgInstance, fpsNames[0]);
        publishMessage(destination, fpsNames[0]);
        Thread.sleep(500L);
    }

    public void testForwardMessagesOnDebugger() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testForwardMessagesOnDebugger", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            //startApplication();
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, interceptor);
            Thread.sleep(10000);
            send();
            if (interceptor.getMessages().size() == 0)
                throw new Exception("Messages not received in the listener");
            interceptor.forwardAll();
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, true, true, 1000L);
            Thread.sleep(5000L);
            System.out.println("MESSAGES IN THE LISTENER::" + m_listener.getMessages());
            if (m_listener.getMessages().size() != 0)
                throw new Exception("All Messages not Sent");
            //m_fioranoApplicationController.forwardDebugMessage(m_appGUID,m_routeGUID,m_interceptor.getMessage(0));
            Assert.assertTrue(true);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testForwardMessagesOnDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testForwardMessagesOnDebugger", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testForwardDebugMessage2() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testForwardDebugMessage2", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, interceptor);
            Thread.sleep(10000);
            send();
            if (interceptor.getMessages().size() == 0)
                throw new Exception("Messages not received in the listener");
            Vector msgs = interceptor.getMessages();
            Message[] msgsArray = new Message[msgs.size()];
            for (int i = 0; i < msgs.size(); i++)
                msgsArray[i] = (Message) msgs.get(i);
            m_fioranoApplicationController.forwardDebugMessage(m_appGUID,m_version, m_routeGUID, msgsArray);
            interceptor.forwardAll();
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, true, true, 1000L);
            Thread.sleep(500L);
            System.out.println("MESSAGES IN THE LISTENER::" + m_listener.getMessages());
            if (m_listener.getMessages().size() != 0)
                throw new Exception("All Messages not Sent");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testForwardDebugMessage2", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testForwardDebugMessage2", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testForwardDebugMessage() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testForwardDebugMessage", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, interceptor);
            Thread.sleep(10000);
            send();
            if (interceptor.getMessages().size() == 0)
                throw new Exception("Messages not received in the listener");
            //Message msg = interceptor.getMessage(0);
            //Assert.assertNotNull(msg);
            //The following works keeping the above to highlight the bug
            Vector msgs = interceptor.getMessages();
            Message msg = (Message) msgs.get(0);
            m_fioranoApplicationController.forwardDebugMessage(m_appGUID,m_version, m_routeGUID, msg);
            interceptor.forwardAll();
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, true, true, 1000L);
            Thread.sleep(500L);
            System.out.println("MESSAGES IN THE LISTENER::" + m_listener.getMessages());
            if (m_listener.getMessages().size() != 0)
                throw new Exception("All Messages not Sent");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testForwardDebugMessage", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testForwardDebugMessage", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testSetMessageOnRoute() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetMessageOnRoute", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID, m_version,interceptor);
            Thread.sleep(10000);
            send();
            if (interceptor.getMessages().size() == 0)
                throw new Exception("Messages not received in the listener");
            Message msg = interceptor.getMessage(0);
            Assert.assertNotNull(msg);
            m_fioranoApplicationController.setMessageOnRoute(m_appGUID,m_version, m_routeGUID, msg);
            interceptor.forwardAll();
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, true, true, 1000L);
            Thread.sleep(500L);
            System.out.println("MESSAGES IN THE LISTENER::" + m_listener.getMessages());
            if (m_listener.getMessages().size() != 0)
                throw new Exception("All Messages not Sent");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetMessageOnRoute", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetMessageOnRoute", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testPauseDebugger() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testPauseDebugger", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            send();
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, interceptor);
            Thread.sleep(10000);
            m_fioranoApplicationController.pauseMessageConsumptionInDebugger(m_routeGUID, m_appGUID,m_version);
            send();
            interceptor.forwardAll();
            m_fioranoApplicationController.resumeMessageConsumptionInDebugger(m_routeGUID, m_appGUID,m_version);
            interceptor.forwardAll();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testPauseDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testPauseDebugger", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
        finally {
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, true, true, 1000L);
            Thread.sleep(500L);
        }
    }

    public void testDebugger() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDebugger", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            send();
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, interceptor);
            m_fioranoApplicationController.pauseMessageConsumptionInDebugger(m_routeGUID, m_appGUID,m_version);
            send();
            interceptor.forwardAll();
            m_fioranoApplicationController.resumeMessageConsumptionInDebugger(m_routeGUID, m_appGUID,m_version);
            send();
            //         m_fioranoApplicationController.forwardAllPendingDebugMessages(m_appGUID,m_routeGUID);

            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDebugger", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
        finally {
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, false, false, 1000L);
            Thread.sleep(30000);
        }
    }

//    public void testSetMessageOnRoute() throws Exception {
//        try {
//            System.out.println("Started the Execution of the TestCase::" + getName());
//
//            String srcInstance=m_fioranoApplicationController.getRoute(m_appGUID,m_routeGUID).getSourceServiceInstance();
//            String srcPort=m_fioranoApplicationController.getRoute(m_appGUID,m_routeGUID).getSourcePortInstance();
//            String destination = m_appGUID+"__"+srcInstance+"__"+srcPort;
//
//             m_fioranoApplicationController.sendMessageOnTopicPort(m_appGUID,m_routeGUID,);
//        }
//        catch (Exception ex) {
//            System.err.println("Exception in the Execution of test case::" + getName());
//            ex.printStackTrace();
//            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
//        }
//    }

    private void createListener(String serviceInstance, String fpsName) throws Exception {
        String url = m_fioranoFPSManager.getConnectURLForFPS(fpsName);
        Iterator services = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstances().iterator();
        String topicName = m_appGUID;
        while (services.hasNext()) {
            ServiceInstance service = (ServiceInstance) services.next();
            if (service.getName().equals(serviceInstance)) {
                System.out.println(service.getOutputPortInstances().get(0).getClass());
                topicName = topicName + "__" + serviceInstance + "__" +
                        ((OutputPortInstance) service.getOutputPortInstances().get(0)).getName();
            }
        }
        System.out.println("THE TOPIC NAME " + topicName);
        m_listener = new STFTopicSubscriber(topicName, null, url);
    }

    public void testSetDebuggerDuplicate() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetDebuggerDuplicate", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitDebuger abcd = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, abcd);
            Thread.sleep(10000);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetDebuggerDuplicate", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            if (ex.getMessage().indexOf("ERROR_SET_DEBUGGER :") != -1) {
                Assert.assertTrue(true);
                return;
            }
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetDebuggerDuplicate", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
        finally {
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, false, false, 1000L);
            Thread.sleep(500L);
        }

    }

    private void publishMessage(String destination, String fpsName) throws Exception {
        RandomAccessFile file = new RandomAccessFile(m_inputFileName, "r");
        String line = file.readLine();
        StringBuffer buffer = new StringBuffer();
        while (line != null) {
            buffer.append(line);
            line = file.readLine();
        }
        line = buffer.toString();
        //System.out.println("THE MESSAGE Being SENT IS::"+line);
        //create a  publisher to the destination
        String url = m_fioranoFPSManager.getConnectURLForFPS(fpsName);
        STFTopicPublisher publisher = new STFTopicPublisher();
        publisher.publish(destination, null, url, line);
    }

    public static Test suite() {
        return new TestSuite(EventInterceptorTest.class);
    }

    /* added for the sake of the bug 12480 STARTS */

    public void testDebuggerOnRouteReferencingExternalService() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDebuggerOnRouteReferencingExternalService", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            /* create the application 2 which contains chat1 component of the previous event process as an external service */
            Application application = ApplicationParser.readApplication(new File(m_appFile2));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID2, m_version2) == null)
                throw new Exception("Application with GUID::" + m_appGUID2 + ", version::" + m_version2 + " not created");

            /* change the route name */

            /* launch the eventprocess */
            m_fioranoApplicationController.compileApplication(m_appGUID2, m_version2);
            m_fioranoApplicationController.prepareLaunch(m_appGUID2, m_version2);
            m_fioranoApplicationController.launchApplication(m_appGUID2, m_version2);
            m_fioranoApplicationController.startAllServices(m_appGUID2,m_version);

            /* set debugger on route */
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID2, m_routeGUID2);
            m_fioranoApplicationController.setDebugger(m_routeGUID2, m_appGUID2,m_version, interceptor);
            Thread.sleep(10000);
            try {
                m_fioranoApplicationController.removeDebugger(m_routeGUID2, m_appGUID2,m_version, false, false, 10000L);
                Thread.sleep(10000);
            } catch (Exception ex) {
                System.err.println("Exception Caught" + ex.getMessage());
                ex.printStackTrace();
                throw new Exception("Event Interceptor not set properly.. ");
            }

            /* kill the eventprocess */
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID2))
                m_fioranoApplicationController.killApplication(m_appGUID2,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID2, m_version2);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDebuggerOnRouteReferencingExternalService", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDebuggerOnRouteReferencingExternalService", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

            /* kill the eventprocess */
            try {
                if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                    m_fioranoApplicationController.killApplication(m_appGUID,m_version);
                m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            } catch (TifosiException e) {
                e.printStackTrace();
            }
        }
    }
    /* added for the sake of the bug 12480 ENDS */


    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID)) {
                //      m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,true,true,1000L);
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            }
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "EventInterceptorTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testImportApplication");
        methodsOrder.add("testSetDebugger");
        methodsOrder.add("testRemoveDebugger");
//    methodsOrder.add("testLaunchEPAfterAddingBreakPoint");
        methodsOrder.add("testForwardMessagesOnDebugger");
        methodsOrder.add("testForwardDebugMessage2");
        methodsOrder.add("testForwardDebugMessage");
        methodsOrder.add("testSetMessageOnRoute");
        methodsOrder.add("testPauseDebugger");
        methodsOrder.add("testDebugger");
        methodsOrder.add("testSetDebuggerDuplicate");
        methodsOrder.add("testDebuggerOnRouteReferencingExternalService");
        methodsOrder.add("testDeleteApplication");

        return methodsOrder;
    }
}
