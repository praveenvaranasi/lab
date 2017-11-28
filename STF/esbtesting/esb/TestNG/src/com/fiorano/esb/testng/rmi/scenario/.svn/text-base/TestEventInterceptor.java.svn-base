package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.jms.STFTopicPublisher;
import com.fiorano.stf.jms.STFTopicSubscriber;
import com.fiorano.stf.misc.JUnitDebuger;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.OutputPortInstance;
import fiorano.tifosi.dmi.application.ServiceInstance;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.jms.Message;
import java.io.File;
import java.io.FilenameFilter;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;

/**
* Created by IntelliJ IDEA.
* User: ranu
* Date: 10/19/11
* Time: 4:57 PM
* To change this template use File | Settings | File Templates.
*/
public class TestEventInterceptor extends AbstractTestNG{
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

    private void init() throws Exception {
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_routeGUID = testProperties.getProperty("RouteGUID");
        m_inputFileName = resourceFilePath + File.separator + testProperties.getProperty("InputFile");
        m_appFile = resourceFilePath + File.separator + testProperties.getProperty("ApplicationXML", "1.0.xml");

        m_appGUID2 = testProperties.getProperty("ApplicationGUID2");
        m_version2 = Float.parseFloat(testProperties.getProperty("ApplicationVersion2"));
        m_routeGUID2 = testProperties.getProperty("RouteGUID2");
        m_appFile2 = resourceFilePath + File.separator + testProperties.getProperty("ApplicationXML2", "2.0.xml");
        printInitParams();
    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("..................................................................");
    }

    @BeforeClass(groups = "DeploymentManagerTest", alwaysRun = true)
    public void startSetUp(){
        if (m_initialised)
            return;
        initializeProperties("scenario" + fsc + "EventInterceptor" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "EventInterceptor";
        m_fioranoFPSManager = rtlClient.getFioranoFPSManager();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        ServerStatusController stc;
        stc = ServerStatusController.getInstance();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testImportApplication")
    public void testSetDebugger() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetDebugger", "EventInterceptorTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetDebugger", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testSetDebugger")
    public void testRemoveDebugger() throws Exception {
        //The Test case is expected to fail. As the debugger is completely handled in the RTL Side -- 11th Sep. 2006
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveDebugger", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            //String routeName=m_fioranoApplicationController.getRoute(m_appGUID,m_routeGUID).getRouteName();
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, true, false, 10000L);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            //an exception is expected here as the debugger is already removed from testSetDebugger().
            //if exception with proper message is caught then pass the test.

            if ((ex instanceof TifosiException) && ((ex.getMessage()).indexOf("Debugger doesnt Exist on the route") != -1)) {
                Assert.assertTrue(true);
            } else {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveDebugger", "EventInterceptorTest"), ex);
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testRemoveDebugger")
    public void testForwardMessagesOnDebugger() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testForwardMessagesOnDebugger", "EventInterceptorTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testForwardMessagesOnDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testForwardMessagesOnDebugger", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testForwardMessagesOnDebugger")
    public void testForwardDebugMessage2() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testForwardDebugMessage2", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID, m_version,interceptor);
            Thread.sleep(10000);
            send();
            if (interceptor.getMessages().size() == 0)
                throw new Exception("Messages not received in the listener");
            Vector msgs = interceptor.getMessages();
            Message[] msgsArray = new Message[msgs.size()];
            for (int i = 0; i < msgs.size(); i++)
                msgsArray[i] = (Message) msgs.get(i);
            m_fioranoApplicationController.forwardDebugMessage(m_appGUID, m_version,m_routeGUID, msgsArray);
            interceptor.forwardAll();
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID, m_version, true, true, 1000L);
            Thread.sleep(500L);
            System.out.println("MESSAGES IN THE LISTENER::" + m_listener.getMessages());
            if (m_listener.getMessages().size() != 0)
                throw new Exception("All Messages not Sent");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testForwardDebugMessage2", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testForwardDebugMessage2", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testForwardDebugMessage2")
    public void testForwardDebugMessage() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testForwardDebugMessage", "EventInterceptorTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testForwardDebugMessage", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testForwardDebugMessage", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testForwardDebugMessage")
    public void testSetMessageOnRoute() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetMessageOnRoute", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, interceptor);
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetMessageOnRoute", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetMessageOnRoute", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testSetMessageOnRoute")
    public void testPauseDebugger() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testPauseDebugger", "EventInterceptorTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testPauseDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testPauseDebugger", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
        finally {
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, true, true, 1000L);
            Thread.sleep(500L);
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testPauseDebugger")
    public void testDebugger() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDebugger", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            send();
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, interceptor);
            m_fioranoApplicationController.pauseMessageConsumptionInDebugger(m_routeGUID, m_appGUID, m_version);
            send();
            interceptor.forwardAll();
            m_fioranoApplicationController.resumeMessageConsumptionInDebugger(m_routeGUID, m_appGUID,m_version);
            send();
            //         m_fioranoApplicationController.forwardAllPendingDebugMessages(m_appGUID,m_routeGUID);

            Logger.Log(Level.INFO, Logger.getFinishMessage("testDebugger", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDebugger", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
        finally {
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, false, false, 1000L);
            Thread.sleep(30000);
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testDebugger")
    public void testSetDebuggerDuplicate() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetDebuggerDuplicate", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitDebuger abcd = new JUnitDebuger(m_appGUID, m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID, m_appGUID,m_version, abcd);
            Thread.sleep(10000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetDebuggerDuplicate", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            ex.printStackTrace();
            if (ex.getMessage().indexOf("ERROR_SET_DEBUGGER :") != -1) {
                Assert.assertTrue(true);
                return;
            }
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetDebuggerDuplicate", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
        finally {
            m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,m_version, false, false, 1000L);
            Thread.sleep(500L);
        }

    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testSetDebuggerDuplicate", description = "added for the sake of the bug 12480 STARTS")
    public void testDebuggerOnRouteReferencingExternalService() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDebuggerOnRouteReferencingExternalService", "EventInterceptorTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDebuggerOnRouteReferencingExternalService", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDebuggerOnRouteReferencingExternalService", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

            /* kill the eventprocess */
            try {
                if (m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version))
                    m_fioranoApplicationController.killApplication(m_appGUID,m_version);
                m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            } catch (TifosiException e) {
                e.printStackTrace();
            }
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testDebuggerOnRouteReferencingExternalService")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "EventInterceptorTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version)) {
                //      m_fioranoApplicationController.removeDebugger(m_routeGUID, m_appGUID,true,true,1000L);
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            }
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "EventInterceptorTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "EventInterceptorTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
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
    private void send() throws Exception {
        String srcInstance = m_fioranoApplicationController.getRoute(m_appGUID,m_version, m_routeGUID).getSourceServiceInstance();
        String srcPort = m_fioranoApplicationController.getRoute(m_appGUID,m_version, m_routeGUID).getSourcePortInstance();
        String trgInstance = m_fioranoApplicationController.getRoute(m_appGUID, m_version,m_routeGUID).getTargetServiceInstance();
        String destination = m_appGUID +"__"+"1_0"+"__" + srcInstance + "__" + srcPort;
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
    private void createListener(String serviceInstance, String fpsName) throws Exception {
        String url = m_fioranoFPSManager.getConnectURLForFPS(fpsName);
        Iterator services = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstances().iterator();
        String topicName = m_appGUID;
        while (services.hasNext()) {
            ServiceInstance service = (ServiceInstance) services.next();
            if (service.getName().equals(serviceInstance)) {
                System.out.println(service.getOutputPortInstances().get(0).getClass());
                topicName = topicName +"__"+"1_0"+ "__" + serviceInstance + "__" +
                        ((OutputPortInstance) service.getOutputPortInstances().get(0)).getName();
            }
        }
        System.out.println("THE TOPIC NAME " + topicName);
        m_listener = new STFTopicSubscriber(topicName, null, url);
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
}
