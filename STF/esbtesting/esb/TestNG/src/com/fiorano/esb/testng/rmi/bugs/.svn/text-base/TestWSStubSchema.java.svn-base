package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.jms.STFQueueReceiver;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestArtifact;
import com.fiorano.stf.test.core.TestElement;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.dashboard.WebServiceDTO;
import org.custommonkey.xmlunit.XMLTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.jms.TextMessage;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
* Created by IntelliJ IDEA.
* User: ranu
* Date: 10/10/11
* Time: 5:52 PM
* To change this template use File | Settings | File Templates.
*/
public class TestWSStubSchema extends AbstractTestNG{

    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_stubInstance;
    private String m_appFile;
    private String m_defaultSchemaFile;
    private String m_modifiedSchemaFile;
    private ObjectName objName;
    private TestElement testCaseConfig;
    private float appVersion = 1.0f;
    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_stubInstance = testProperties.getProperty("StubInstance");
        m_appFile = resourceFilePath + File.separator + "WSSTUB_1.0.xml";//testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_defaultSchemaFile = resourceFilePath + File.separator + testProperties.getProperty("DefaultSchemaFile", null);
        m_modifiedSchemaFile = resourceFilePath + File.separator + testProperties.getProperty("ModifiedSchemaFile", null);
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.print("Application GUID:: " + m_appGUID + "  Version Number::" + m_version);
        System.out.print("\nThe Application File is::" + m_appFile);
        System.out.println("\n_____________________________________________________________________________");
    }

    @BeforeClass(groups = "WSStubSchemaTest", description = "Bug 19529")
    public void startSetUp() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "WSSchemaTest"));
        initializeProperties("bugs" + fsc + "TestWSStubSchema_Bug19529" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc +"tests" +  fsc + "bugs" + fsc + "TestWSStubSchema_Bug19529";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        try {
            init();
        } catch (Exception e) {
            System.out.println("Error in initialisation");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
            printInitParams();
        try {
            objName = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            JMXClient.connect("admin", "passwd");
        }
        catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        Application application = ApplicationParser.readApplication(new File(m_appFile));
        m_fioranoApplicationController.saveApplication(application);
        if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
            throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
        startApplication(m_appGUID, m_version);
        Thread.sleep(10000);
        if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
            throw new Exception("The Application is not started.");
    }

    @Test(groups = "WSStubSchemaTest", alwaysRun = true, dependsOnMethods = "startSetUp",description = "Bug 19529")
    public void testSchemaValidity() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSchemaValidity", "TestWSStubSchema_Bug19529"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String schema = getRequestSchema();
            compare(new File(m_defaultSchemaFile),schema,"xml");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSchemaValidity", "TestWSStubSchema_Bug19529"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSchemaValidity", "TestWSStubSchema_Bug19529"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "WSStubSchemaTest", alwaysRun = true, description = "Bug 19529", dependsOnMethods = "testSchemaValidity")
    public void testSendDefaultSchema() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSendDefaultSchema", "TestWSStubSchema_Bug19529"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            STFQueueReceiver m_receiver = createReceiver("primaryQueue");

            simulateDashboard(null);

            TextMessage msg = null;
            if (m_receiver != null) {
                Vector messages = m_receiver.getMessages();
                msg = (TextMessage) messages.get(0);
                System.out.println("RECIEVED " + messages.size() + " Messages");
                System.out.println("==== Message:: " + msg.getText() + " ====");
            }

            Thread.sleep(5000);
            System.out.println("THE THREAD STATUS IS::Alive: " + m_receiver.isAlive() + "; Daemon: " + m_receiver.isDaemon());
            m_receiver.cleanup();
            m_receiver.join();
            Thread.sleep(5000);
            if (msg.getText() == null)
                Assert.assertTrue(true);
            else
                Assert.assertTrue(false, "Expected null; getting this " + msg.getText());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSendDefaultSchema", "TestWSStubSchema_Bug19529"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendDefaultSchema", "TestWSStubSchema_Bug19529"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }

    }

    @Test(groups = "WSStubSchemaTest", alwaysRun = true, description = "Bug 19529", dependsOnMethods = "testSendDefaultSchema")
    public void testSendModifiedSchema() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSendModifiedSchema", "TestWSStubSchema_Bug19529"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            STFQueueReceiver m_receiver = createReceiver("primaryQueue");

            FileReader fr = new FileReader(new File(m_modifiedSchemaFile));
            BufferedReader br = new BufferedReader(fr);
            String s = null;
            StringBuilder sb = new StringBuilder();
            while ((s = br.readLine()) != null)
                sb.append(s);

            simulateDashboard(sb.toString());

            TextMessage msg = null;
            if (m_receiver != null) {
                Vector messages = m_receiver.getMessages();
                if(messages.size()==0)
                    Assert.assertTrue(false, "No message received on queue");
                msg = (TextMessage) messages.get(0);
                System.out.println("RECEIVED " + messages.size() + " Messages");
                System.out.println("==== Message:: " + msg.getText() + " ====");
            }
            Thread.sleep(5000);
            System.out.println("THE THREAD STATUS IS::Alive" + m_receiver.isAlive() + " " + m_receiver.isDaemon());
            m_receiver.cleanup();
            m_receiver.join();
            Thread.sleep(5000);
            if (msg.getText().equals("<to>dag</to>"))
                Assert.assertTrue(true);
            else Assert.assertTrue(false, "Expected : <to>dag</to> \tbut got " + msg.getText());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSendModifiedSchema", "TestWSStubSchema_Bug19529"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendModifiedSchema", "TestWSStubSchema_Bug19529"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }

    }



    /***************************** End Of Tests ******************************************/

    /**
     * ************************** Auxiliary Methods *****************************************
     */

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid, appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);
            Thread.sleep(10000);
        }
        catch (Exception e) {
            System.err.println("Error launching the application");
            e.printStackTrace();
            throw new FioranoException("Error launching the application");
        }
    }

    private void compare(File expectedSchema, String schema, String messageType) throws IOException {

        /*if (!schema.exists())
            throw new AssertionError("!!!! NO FILE TO COMPARE !!!");*/

        if (messageType.equals(TestArtifact.XML_MESSAGE)) {
            //TODO : do an xml comparision. more gracefuly
            //TODO: chk for comparisons where the element values can be different
            XMLTestCase xmlTestCase = new XMLTestCase(){};
            //xmlTestCase.setIgnoreWhitespace(true);
      //      xmlTestCase.setIgnoreWhitespace(true);
            try {
                if (!((xmlTestCase).compareXML(new FileReader(expectedSchema), new StringReader(schema))).similar())
                    throw new IOException("TestResultValidation Failed: Expected Schema file: " +
                            expectedSchema.getCanonicalPath() + "Output : " + schema);
            } catch (Exception e) {
                if (e.getMessage() != null) e.printStackTrace();
                throw new AssertionError("Failed to compare test results: " + getName());
            }
        } else {
            String expectedOutputMessage = getFileAsString(expectedSchema);
            String outputMessage = schema;

            String exop = removeESC(expectedOutputMessage);
            String op = removeESC(outputMessage);


            if (!exop.equals(op))
                throw new IOException("TestResultValidation Failed: Expected Output: \n" + exop + "Output: \n" + op);
        }
    }

    private void simulateDashboard(String requestSchema) throws Exception {
        String handleID = getHandleID();
        Object[] params = {m_appGUID, m_stubInstance, false, false, null, null, false, false, handleID};
        String[] signatures = {String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName()};
        Map<String, Map<String, ArrayList<String>>> maps = (Map<String, Map<String, ArrayList<String>>>) jmxClient.invoke(objName, "getOps", params, signatures);
        Thread.sleep(2000);
        ArrayList<String> operation = new ArrayList<String>(3);
        for (String s : maps.keySet()) {
            operation.add(s);
            Map<String, ArrayList<String>> msp = maps.get(s);
            for (String ss : msp.keySet()) {
                operation.add(ss);
                ArrayList<String> list = msp.get(ss);
                operation.add(list.get(0));
                break;
            }
            break;
        }

        if (requestSchema == null) {
            Object[] params2 = {m_appGUID, m_stubInstance, false, false, null, null, operation, false, false, handleID};
            String[] signatures2 = {String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName(), String.class.getName(), ArrayList.class.getName(),"boolean", "boolean", String.class.getName()};
            requestSchema = (String) jmxClient.invoke(objName, "getReqSOAPMsg", params2, signatures2);
            Thread.sleep(2000);
        }

        Object[] params3 = {requestSchema, getEndPointURI(), m_appGUID, m_stubInstance, false, null, null, operation, false, false, handleID};
        String[] signatures3 = {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), "boolean", String.class.getName(), String.class.getName(), ArrayList.class.getName(), "boolean", "boolean", String.class.getName()};
        jmxClient.invoke(objName, "getResponseSOAPMsg", params3, signatures3);
        Thread.sleep(2000);
    }

    private String getRequestSchema() throws Exception {
        String handleID = getHandleID();
        Object[] params = {m_appGUID, m_stubInstance, false, false, null, null, false, false, handleID};
        //String[] sigs = {java.lang.String s, java.lang.String s1, boolean b, boolean b1, java.lang.String s2, java.lang.String s3, java.util.ArrayList arrayList, boolean b2, boolean b3, java.lang.String s4};
        String[] signatures = {String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName()};
        Map<String, Map<String, ArrayList<String>>> maps = (Map<String, Map<String, ArrayList<String>>>) jmxClient.invoke(objName, "getOps", params, signatures);
        Thread.sleep(2000);
        ArrayList<String> operation = new ArrayList<String>(3);
        for (String s : maps.keySet()) {
            operation.add(s);
            Map<String, ArrayList<String>> msp = maps.get(s);
            for (String ss : msp.keySet()) {
                operation.add(ss);
                ArrayList<String> list = msp.get(ss);
                operation.add(list.get(0));
                break;
            }
            break;
        }

        Object[] params2 = {m_appGUID, m_stubInstance, false, false, null, null, operation, false, false, handleID};
        String[] signatures2 = {String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName(), String.class.getName(), ArrayList.class.getName(), "boolean", "boolean", String.class.getName()};
        return (String) jmxClient.invoke(objName, "getReqSOAPMsg", params2, signatures2);
    }

    private STFQueueReceiver createReceiver(String queueName) throws TifosiException {
        try {
            List serviceInstances = m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstances();
            String fpsName = null;
            for (int i = 0; i < serviceInstances.size(); i++) {
                ServiceInstance instance = (ServiceInstance) serviceInstances.get(i);
//                if (instance.getName().equalsIgnoreCase(trgInstance)) {
                fpsName = instance.getNodes()[0];
                break;
//                }
            }

            String url = null;
            if ((fpsName.equals("fps") || fpsName.equals("fps1"))) {
                url = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
                if (url.endsWith("2010"))
                    url = "http://0.0.0.0:1877";
            } else {
                ArrayList urls = new ArrayList(2);//string followed by int.
                String s = ServerStatusController.getInstance().getURLOnFESActive();
                String rtlPort = s.substring(s.lastIndexOf(":") + 1);
                urls.add(s.substring(s.lastIndexOf("//") + 2, s.lastIndexOf(":")));
                if (rtlPort.equals("1947")) {
                    urls.add(2047);
                } else
                    urls.add(2048);

                int port = 1847;
                if (urls.get(1).equals("2047")) {
                    port = 1847;
                }
                url = "http://" + urls.get(0) + ":" + port;
            }


            STFQueueReceiver m_receiver = new STFQueueReceiver();
            m_receiver.initialize(queueName, null, url);
            m_receiver.runTest();
            return m_receiver;
        } catch (STFException e) {
            throw new TifosiException(e);
        }
    }

    private String removeESC(String exM) {
        String c = exM.replaceAll("\r", "");
        return c;

    }

    private String getEndPointURI() throws Exception {
        String handleID = getHandleID();
        Object[] args = {1, handleID};
        String[] sigs = {"int", String.class.getName()};
        Vector list = (Vector) jmxClient.invoke(objName, "getWebEventProcessList", args, sigs);

        Iterator itr = list.iterator();
        String endPURI = null;
        while (itr.hasNext()) {
            WebServiceDTO obj = (WebServiceDTO) itr.next();
            endPURI = obj.getEndPointURITest();
            break;
        }
        return endPURI;
    }

    private String getFileAsString(File output) throws IOException {
        return new String(getBytesFromFile(output));
    }

    private byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);

        // Get the size of the file
        long length = file.length();

        // You cannot create an array using a long type.
        // It needs to be an int type.
        // Before converting to an int type, check
        // to ensure that file is not larger than Integer.MAX_VALUE.
        if (length > Integer.MAX_VALUE) {
            // File is too large
        }

        // Create the byte array to hold the data
        byte[] bytes = new byte[(int) length];

        // Read in the bytes
        int offset = 0;
        int numRead;
        while (offset < bytes.length
                && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
            offset += numRead;
        }

        if (offset < bytes.length) {
            throw new IOException("Could not completely read file " + file.getName());
        }

        // Close the input stream and return bytes
        is.close();
        return bytes;
    }

    private String getHandleID() {
        return rtlClient.getHandleID();
    }
}
