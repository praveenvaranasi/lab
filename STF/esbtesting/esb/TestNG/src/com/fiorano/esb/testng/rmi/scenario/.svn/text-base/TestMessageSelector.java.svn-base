package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.jms.STFQueueReceiver;
import com.fiorano.stf.jms.STFTopicPublisher;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.*;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/20/11
 * Time: 6:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestMessageSelector extends AbstractTestNG{
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_routeGUID;
    private String m_appFile;
    private String m_messageFile;
    private String m_route1;
    private String m_route2;
    private String m_route3;

    private String m_messageFileWrong;

    static boolean isModifiedOnceHA = false;//to check the files overwriting
    static boolean isModifiedOnce = false;
    private FilenameFilter xmlFilter;//file extracter

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_routeGUID = testProperties.getProperty("SelectorRouteGUID");
        m_route2 = testProperties.getProperty("RouteOfComponent2");
        m_route1 = testProperties.getProperty("RouteOfComponent1");
        m_route3 = testProperties.getProperty("RouteOfComponent3");
        m_appFile = resourceFilePath + File.separator + testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_messageFile = resourceFilePath + File.separator + testProperties.getProperty("MessageFile", null);
        m_messageFileWrong = resourceFilePath + File.separator + testProperties.getProperty("MessageFileWrong", null);
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.print("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.print("\tSelector Route GUID:: " + m_routeGUID);
        System.out.print("\tRoute1 GUID:: " + m_route1);
        System.out.print("\tRoute2 GUID:: " + m_route2);
        System.out.print("\tRoute3 GUID:: " + m_route3);
        System.out.print("\tThe Application File is::" + m_appFile);
        System.out.println("\tThe Message File::" + m_messageFile);
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

    @BeforeClass(groups = "MessageSelectorTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "MessageSelector" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "MessageSelector";
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
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            Assert.fail("could not initialise properties");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "startSetUp", description = "trying to import an application")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application with selector set.")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(10000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testRunApplication")
    public void testSendMessagePositive() {
        /*
        The Flow is designed to have a message selector on route with GUID m_routeGIUD, and 3 components sending messages to the source component of the route
        The messages from the component3 will be received with all the selector set.
        So the test case shall be considered as passed if the messages that are sent from are received.
        */
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSendMessgePositive", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("Sending the messages on the route ::" + m_route3 + " And Receiving on ::" + m_routeGUID + ".");
            int count = 5;
            Thread.sleep(10000);
            m_fioranoApplicationController.stopService(m_appGUID,m_version, "ExitPoint");
            STFQueueReceiver m_reciever = sendMessageOnRoutes(new File(m_messageFile), m_route3, m_routeGUID, count);
            Thread.sleep(2000L);
            m_reciever.stop = true;
            System.out.println("........................................");
            //notifyAll();
            System.out.println("The values of Stop::" + m_reciever.stop);
            System.out.println("The Messages Recieved are ( " + m_reciever + " )");
            if (m_reciever != null) {
                Vector messages = m_reciever.getMessages();
                System.out.println("RECIEVED " + messages.size() + " Messages");
                if (messages.size() != count)
                    throw new Exception("ALL MESSAGE NOT RECIEVED. Recieved only " + messages.size() + " messages of " + count);
                for (int i = 0; i < messages.size(); i++)
                    System.out.println("Message::\n" + messages.get(i));
            }
            m_reciever.stop = true;
            m_fioranoApplicationController.startService(m_appGUID,m_version, "ExitPoint");
            Thread.sleep(10000);
            System.out.println("THE THREAD STATUS IS::Alive" + m_reciever.isAlive() + " " + m_reciever.isDaemon());
            m_reciever.interrupt();
            m_reciever.interrupt();
            m_reciever.cleanup();
            m_reciever.join();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSendMessgePositive", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessgePositive", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testSendMessagePositive")
    public void testSendMessageNegative() {
        /*
        The Flow is designed to have a messaege selector on route with GUID m_routeGIUD, and 3 components sending messages to the source component of the route
        The messages from the component3 will be recieved with all the selector set.
        So the test case shall be considered as passed if the messages that are sent from component1 are not recieved.
        */
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSendMessgeNegativ", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("Sending the messages on the route ::" + m_route1 + " And Recieving on ::" + m_routeGUID + ".");
            int count = 5;
            STFQueueReceiver m_reciever = sendMessageOnRoutes(new File(m_messageFile), m_route1, m_routeGUID, count);
            Thread.sleep(2000L);
            System.out.println("The Messages Recieved are ( " + m_reciever + " )");
            m_reciever.stop = true;
            if (m_reciever != null) {
                Vector messages = m_reciever.getMessages();
                System.out.println("RECIEVED " + messages.size() + " Messages");
                if (messages.size() > 0)
                    throw new Exception("Selectors Not working. Messages from component1 not supposed to reach the reciever but recieved " + messages.size() + " messages.");
            }
            m_reciever.stop = true;
            System.out.println("THE THREAD STATUS IS::Alive" + m_reciever.isAlive() + " " + m_reciever.isDaemon());
            m_reciever.interrupt();
            m_reciever.interrupt();
            m_reciever.cleanup();
            m_reciever.join();
            //m_reciever.stop=true;
            //m_reciever.join();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSendMessgeNegativ", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessgeNegativ", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testSendMessageNegative")
    public void testRemoveSenderSelectorRuntime() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveSenderSelectorRuntime", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Iterator routes = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getRoutes().iterator();
            while (routes.hasNext()) {
                Route route = (Route) routes.next();
                if (!route.getName().equalsIgnoreCase(m_routeGUID))
                    continue;
                HashMap selectors = new HashMap(route.getSelectors());
                System.out.println("Before Removing any of the Selectors, the selectors are::" + selectors.toString());
                String senderSelector = (String) selectors.remove(Route.SELECTOR_SENDER);
                if (senderSelector == null)
                    throw new Exception("Sender Selector Not found on the route");
                System.out.println("The selectors after deleting the Sender Selector" + selectors);
                System.out.println("The Sender Selector Removed in::" + senderSelector);
                senderSelector = (String) selectors.remove(Route.SELECTOR_SENDER);
                if (senderSelector != null)
                    throw new Exception("Expected null for Sender Selector but found" + senderSelector);
                m_fioranoApplicationController.changeRouteSelector(m_appGUID,m_version, m_routeGUID, selectors);
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveSenderSelectorRuntime", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveSenderSelectorRuntime", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testRemoveSenderSelectorRuntime")
    public void testRemoveJMSSelectorRuntime() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveJMSSelectorRuntime", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Iterator routes = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getRoutes().iterator();
            while (routes.hasNext()) {
                Route route = (Route) routes.next();
                if (!route.getName().equalsIgnoreCase(m_routeGUID))
                    continue;
                HashMap selectors = new HashMap(route.getSelectors());
                System.out.println("Before Removing JMS Selectors, the selectors are::" + selectors.toString());
                String jmsSelector = (String) selectors.remove(Route.SELECTOR_JMS);
                if (jmsSelector == null)
                    throw new Exception("JMS Selector Not found on the route");
                System.out.println("The selectors after deleting the JMS Selector" + selectors);
                System.out.println("THE SELECTOR REMOVED IS::" + jmsSelector);
                jmsSelector = (String) selectors.remove(Route.SELECTOR_JMS);
                if (jmsSelector != null)
                    throw new Exception("Expected null for JMS Selector but found" + jmsSelector);
                m_fioranoApplicationController.changeRouteSelector(m_appGUID,m_version, m_routeGUID, selectors);
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveJMSSelectorRuntime", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveJMSSelectorRuntime", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testRemoveJMSSelectorRuntime")
    public void testForwardMessage() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testForwardMessage", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            // try sending from component1
            System.out.println("Sending the messages on the route ::" + m_route1 + " And Recieving on ::" + m_routeGUID + ".");
            int count = 6;
            m_fioranoApplicationController.stopService(m_appGUID,m_version, "ExitPoint");
            STFQueueReceiver m_reciever = sendMessageOnRoutes(new File(m_messageFile), m_route1, m_routeGUID, count);
            Thread.sleep(5000L);
            m_reciever.stop = true;
            System.out.println("The Messages Recieved are ( " + m_reciever + " )");
            if (m_reciever != null) {
                Vector messages = m_reciever.getMessages();
                System.out.println("RECIEVED " + messages.size() + " Messages");
                if (messages.size() != count)
                    throw new Exception("ALL MESSAGE NOT RECIEVED. Recieved only " + messages.size() + " messages of " + count);
                for (int i = 0; i < messages.size(); i++)
                    System.out.println("Message::\n" + messages.get(i));
            }
            m_reciever.stop = true;
            m_fioranoApplicationController.startService(m_appGUID,m_version, "ExitPoint");
            Thread.sleep(10000);
            System.out.println("THE THREAD STATUS IS::Alive" + m_reciever.isAlive() + " " + m_reciever.isDaemon());
            m_reciever.interrupt();
            m_reciever.interrupt();
            m_reciever.cleanup();
            m_reciever.join();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testForwardMessage", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testForwardMessage", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testForwardMessage")
    public void testSetBodyXPathSelector() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetBodyXPathSelector", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Properties namespace = new Properties();
            namespace.put("ns1", "");
            namespace.put("ns2", "");
            XPathSelector xpath;
            String XpathValue = "(/ChatMessage/Sender/Name = 'Sandeep')";
            xpath = new XPathSelector(XpathValue, namespace);
            HashMap selectors = new HashMap();
            selectors.put(Route.SELECTOR_BODY, xpath);
            m_fioranoApplicationController.changeRouteSelector(m_appGUID, m_version, m_routeGUID, selectors);
            XPathSelector bodySelector = (XPathSelector) m_fioranoApplicationController.getRoute(m_appGUID,m_version, m_routeGUID).getBodySelector();
            if (!bodySelector.getXPath().equalsIgnoreCase(XpathValue))
                throw new Exception("MESSAGE Xpath Selector Not Set.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetBodyXPathSelector", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetBodyXPathSelector", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testSetBodyXPathSelector")
    public void testForwardMessageBodySelectorPositive() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testForwardMessageBodySelectorPositive", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            // try sending from component1
            System.out.println("Sending the messages on the route ::" + m_route1 + " And Recieving on ::" + m_routeGUID + ".");
            m_fioranoApplicationController.stopService(m_appGUID,m_version, "ExitPoint");
            int count = 6;
            STFQueueReceiver m_reciever = sendMessageOnRoutes(new File(m_messageFile), m_route1, m_routeGUID, count);
            Thread.sleep(5000L);
            m_reciever.stop = true;
            System.out.println("The Messages Recieved are ( " + m_reciever + " )");
            if (m_reciever != null) {
                Vector messages = m_reciever.getMessages();
                System.out.println("RECIEVED " + messages.size() + " Messages");
                if (messages.size() != count)
                    throw new Exception("ALL MESSAGE NOT RECIEVED. Recieved only " + messages.size() + " messages of " + count);
                for (int i = 0; i < messages.size(); i++)
                    System.out.println("Message::\n" + messages.get(i));
            }
            m_reciever.stop = true;
            m_fioranoApplicationController.startService(m_appGUID,m_version, "ExitPoint");
            Thread.sleep(10000);
            System.out.println("THE THREAD STATUS IS::Alive" + m_reciever.isAlive() + " " + m_reciever.isDaemon());
            m_reciever.interrupt();
            m_reciever.interrupt();
            m_reciever.cleanup();
            m_reciever.join();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testForwardMessageBodySelectorPositive", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testForwardMessageBodySelectorPositive", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testForwardMessageBodySelectorPositive")
    public void testForwardMessageBodySelectorNegative() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testForwardMessageBodySelectorNegative", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            // try sending from component1
            System.out.println("Sending the messages on the route ::" + m_route1 + " And Recieving on ::" + m_routeGUID + ".");
            int count = 6;
            STFQueueReceiver m_reciever = sendMessageOnRoutes(new File(m_messageFileWrong), m_route1, m_routeGUID, count);
            Thread.sleep(5000L);
            m_reciever.stop = true;
            System.out.println("The Messages Recieved are ( " + m_reciever + " )");
            if (m_reciever != null) {
                Vector messages = m_reciever.getMessages();
                System.out.println("RECIEVED " + messages.size() + " Messages");
                if (messages.size() != 0)
                    throw new Exception("Selectors Not working. Messages from component1 not supposed to reach the reciever but recieved " + messages.size() + " messages.");
            }
            m_reciever.stop = true;
            System.out.println("THE THREAD STATUS IS::Alive" + m_reciever.isAlive() + " " + m_reciever.isDaemon());
            m_reciever.interrupt();
            m_reciever.interrupt();
            m_reciever.cleanup();
            m_reciever.join();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testForwardMessageBodySelectorNegative", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testForwardMessageBodySelectorNegative", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testForwardMessageBodySelectorNegative")
    public void testSetAppContextXPathSelector() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetAppContextXPathSelector", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Properties namespace = new Properties();
            namespace.put("ns1", "");
            namespace.put("ns2", "");
            XPathSelector xpath;
            String XpathValue = "(/ChatMessage/Sender/Name = 'Sandeep')";
            xpath = new XPathSelector(XpathValue, namespace);
            HashMap selectors = new HashMap();
            selectors.put(Route.SELECTOR_APPLICATION_CONTEXT, xpath);
            m_fioranoApplicationController.changeRouteSelector(m_appGUID,m_version, m_routeGUID, selectors);
            XPathSelector bodySelector = (XPathSelector) m_fioranoApplicationController.getRoute(m_appGUID,m_version, m_routeGUID).getApplicationContextSelector();
            if (!bodySelector.getXPath().equalsIgnoreCase(XpathValue))
                throw new Exception("MESSAGE Xpath Selector Not Set.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetAppContextXPathSelector", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetAppContextXPathSelector", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testSetAppContextXPathSelector")
    public void testForwardMessageAppContextSelectorPositive() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testForwardMessageAppContextSelectorPositive", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            // try sending from component1
            System.out.println("Sending the messages on the route ::" + m_route1 + " And Recieving on ::" + m_routeGUID + ".");
            int count = 6;
            m_fioranoApplicationController.stopService(m_appGUID,m_version, "ExitPoint");
            STFQueueReceiver m_reciever = sendMessageOnRoutes(new File(m_messageFile), m_route1, m_routeGUID, count);

            Thread.sleep(5000L);
            m_reciever.stop = true;
            System.out.println("The Messages Recieved are ( " + m_reciever + " )");
            if (m_reciever != null) {
                Vector messages = m_reciever.getMessages();
                System.out.println("RECIEVED " + messages.size() + " Messages");
                if (messages.size() != count)
                    throw new Exception("ALL MESSAGE NOT RECIEVED. Recieved only " + messages.size() + " messages of " + count);
                for (int i = 0; i < messages.size(); i++)
                    System.out.println("Message::\n" + messages.get(i));
            }
            m_reciever.stop = true;
            m_fioranoApplicationController.startService(m_appGUID,m_version, "ExitPoint");
            Thread.sleep(10000);
            System.out.println("THE THREAD STATUS IS::Alive" + m_reciever.isAlive() + " " + m_reciever.isDaemon());
            m_reciever.interrupt();
            m_reciever.interrupt();
            m_reciever.cleanup();
            m_reciever.join();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testForwardMessageAppContextSelectorPositive", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testForwardMessageAppContextSelectorPositive", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testForwardMessageAppContextSelectorPositive")
    public void testForwardMessageAppContextSelectorNegative() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testForwardMessageAppContextSelectorNegitive", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            // try sending from component1
            System.out.println("Sending the messages on the route ::" + m_route2 + " And Recieving on ::" + m_routeGUID + ".");
            int count = 6;
            STFQueueReceiver m_reciever = sendMessageOnRoutes(new File(m_messageFileWrong), m_route2, m_routeGUID, count);
            Thread.sleep(5000L);
            m_reciever.stop = true;
            System.out.println("The Messages Recieved are ( " + m_reciever + " )");
            if (m_reciever != null) {
                Vector messages = m_reciever.getMessages();
                System.out.println("RECIEVED " + messages.size() + " Messages");
                if (messages.size() != 0)
                    throw new Exception("Selectors Not working. Messages from component1 not supposed to reach the reciever but recieved " + messages.size() + " messages.");
            }
            m_reciever.stop = true;
            System.out.println("THE THREAD STATUS IS::Alive" + m_reciever.isAlive() + " " + m_reciever.isDaemon());
            m_reciever.interrupt();
            m_reciever.interrupt();
            m_reciever.cleanup();
            m_reciever.join();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testForwardMessageAppContextSelectorNegitive", "TestMessageSelector"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testForwardMessageAppContextSelectorNegitive", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "MessageSelectorTest", alwaysRun = true, dependsOnMethods = "testForwardMessageAppContextSelectorNegative", description = "trying to delete application with a Selectors.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestMessageSelector"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            try {
                stopApplication(m_appGUID);
            } catch (Exception e) {
                //ignore comfortably ..
            }
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);

            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null) {
                Assert.assertTrue(true);
            } else {
                throw new Exception("Failed to delete the application.");
            }
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestMessageSelector"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }


    /*Auxiliary methods*/
    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,m_version);
            Thread.sleep(10000);
        }
        catch (Exception e) {
            if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1) {
                //never mind if the application is already running.
                return;
            }
            System.err.println("Error launching the application");
            e.printStackTrace();
            throw new FioranoException("Error launching the application");
        }
    }

    private void stopApplication(String appGUID) {
        try {
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);
            Thread.sleep(2000);
        }
        catch (Exception e) {
            //ignore.
        }
    }

    private STFQueueReceiver sendMessageOnRoutes(File file, String sendingRoute, String receivingRoute, int count) throws TifosiException {
        String message;
        StringBuffer buffer = new StringBuffer("");
        FileReader reader = null;
        BufferedReader bufferedReader = null;
        try {
            reader = new FileReader(file);
            bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            message = "";
            while (line != null) {
                buffer.append(line);
                line = bufferedReader.readLine();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Unable To Read the message from the file. Sending the default message::testMessage");
            message = "testMessage";
        }
        finally {
            try {
                bufferedReader.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        message = buffer.toString();
        return sendMessageOnRoutes(message, sendingRoute, receivingRoute, count);
    }

    private STFQueueReceiver sendMessageOnRoutes(String message, String sendingRoute, String receivingRoute, int count) throws TifosiException {
        STFQueueReceiver reciever = createReciever(receivingRoute);
        System.out.println("Reciever created...");
        STFTopicPublisher publisher = null;
        for (int i = 0; i < count; i++) {
            System.out.println("Sending the message \n" + message);
            publisher = publishMessage(message, sendingRoute);
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                //ignore
            }
        }
        if (publisher != null)
            publisher.cleanup();
        System.out.println("Messages Published");
        return reciever;
    }

    private STFQueueReceiver createReciever(String route) throws TifosiException {
        String trgInstance = m_fioranoApplicationController.getRoute(m_appGUID, m_version,route).getTargetServiceInstance();
        String trgPort = m_fioranoApplicationController.getRoute(m_appGUID,m_version, route).getTargetPortInstance();
        String queueName = m_appGUID +"__1_0"+ "__" + trgInstance + "__" + trgPort;
        List serviceInstances = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstances();
        String fpsName = null;
        for (int i = 0; i < serviceInstances.size(); i++) {
            ServiceInstance instance = (ServiceInstance) serviceInstances.get(i);
            if (instance.getName().equalsIgnoreCase(trgInstance)) {
                fpsName = instance.getNodes()[0];
                break;
            }
        }
        if (fpsName == null || fpsName.trim().equals(""))
            fpsName = "fps";
        String url = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
        System.out.println("Recieving messages  on ::" + queueName + " And URL::" + url);
        STFQueueReceiver m_reciever = new STFQueueReceiver();
        m_reciever.initialize(queueName, null, url);
        m_reciever.runTest();
        return m_reciever;
    }

    private STFTopicPublisher publishMessage(String message, String route) throws TifosiException {
        STFTopicPublisher publisher = new STFTopicPublisher();
        String srcInstance = m_fioranoApplicationController.getRoute(m_appGUID,m_version, route).getSourceServiceInstance();
        String srcPort = m_fioranoApplicationController.getRoute(m_appGUID,m_version, route).getSourcePortInstance();
        List serviceInstances = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstances();
        String fpsName = null;
        for (int i = 0; i < serviceInstances.size(); i++) {
            ServiceInstance instance = (ServiceInstance) serviceInstances.get(i);
            if (instance.getName().equalsIgnoreCase(srcInstance)) {
                fpsName = instance.getNodes()[0];
                break;
            }
        }
        if (fpsName == null || fpsName.trim().equals(""))
            fpsName = "fps";
        String url = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
        String topicName = m_appGUID +"__1_0"+ "__" + srcInstance + "__" + srcPort;
        publisher.initialize(topicName, null, url, message);
        publisher.runTest();
        return publisher;
    }
}
