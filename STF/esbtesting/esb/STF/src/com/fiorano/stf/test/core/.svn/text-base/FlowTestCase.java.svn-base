package com.fiorano.stf.test.core;


import com.fiorano.stf.framework.EventProcessHandle;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.misc.OutputPortMessageListener;
import com.fiorano.stf.util.StringHashtable;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.aps.ApplicationStateDetails;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import junit.framework.ComparisonFailure;
import org.custommonkey.xmlunit.XMLTestCase;

import java.io.*;
import java.util.*;


/**
 * @author Sandeep M
 * @Date:
 */
public class FlowTestCase extends STFTestCase {

    private EventProcessHandle processHandle;
    private String testOutputPath;
    //todo configure this
    private static final long TIMEOUT = 20000l;
    private String testCasePath;
    private FlowTestCaseElement flowTestCaseConfig;
    private boolean applicationImported;

    private Map<String, OutputPortMessageListener> listenerMap = new HashMap<String, OutputPortMessageListener>();

    public FlowTestCase(String testCaseName) {
        super(testCaseName);
    }

    public FlowTestCase(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
        flowTestCaseConfig = (FlowTestCaseElement) testCaseConfig;
    }

    public FlowTestCaseElement getFlowTestCaseConfig() {
        return flowTestCaseConfig;
    }

    protected void runTest() throws Exception {
        Vector scenarios = flowTestCaseConfig.getScenarios();
        System.out.println("Running FlowTestCase for" + flowTestCaseConfig.getAppGUID());
        for (int i = 0; i < scenarios.size(); i++) {
            TestScenario scenario = (TestScenario) scenarios.get(i);
            runScenario(scenario);
            Thread.sleep(10000);
        }
        validate(flowTestCaseConfig);
    }

    protected void setUp() throws Exception {

        //create the Handle for the Applciation
        processHandle = new EventProcessHandle(flowTestCaseConfig.getAppGUID(), Float.parseFloat(flowTestCaseConfig.getAppVersion()),
                flowTestCaseConfig.getApplicationFilePath());

        //importApplication(...)
        if (flowTestCaseConfig.getApplicationFilePath() != null)
            applicationImported = processHandle.importApplication(flowTestCaseConfig.isOverWrite());

        processHandle.startApplication();

        testCasePath = flowTestCaseConfig.getTestCasepath();
        testOutputPath = flowTestCaseConfig.getOutputFile() != null ? flowTestCaseConfig.getOutputFile() : testCasePath + File.separator + "output";

        Vector scenarios = flowTestCaseConfig.getScenarios();
        for (int i = 0; i < scenarios.size(); i++) {
            TestScenario scenario = (TestScenario) scenarios.get(i);
            addListeners(scenario);
        }

        if ((flowTestCaseConfig.getStartupCmd()) != null) {
            String[] nodes = getPeerMachineAddress();
            ExecutionController remoteProxy = ExecutionController.getInstance();
            for (String node : nodes) {
                remoteProxy.initStartUpOnRemote(node, flowTestCaseConfig.getStartupDir(), flowTestCaseConfig.getStartupCmd());
            }
        }

    }

    protected void tearDown() throws java.lang.Exception {
        // Delete the application

        if (flowTestCaseConfig.getCleanupCmd() != null) {
            String[] nodes = getPeerMachineAddress();
            ExecutionController remoteProxy = ExecutionController.getInstance();
            for (String node : nodes) {
                remoteProxy.initCleanUpOnRemote(node, flowTestCaseConfig.getCleanupDir(), flowTestCaseConfig.getCleanupCmd());
            }
        }
        processHandle.killApplication();
        if (applicationImported)
            processHandle.deleteApplication();

    }

    private void validate(FlowTestCaseElement testCase) throws IOException {

        File outputDir = new File(testOutputPath);
        Vector scenarios = testCase.getScenarios();

        for (int i = 0; i < scenarios.size(); i++) {
            TestScenario scenario = (TestScenario) scenarios.get(i);

            Hashtable expectedOutputs = scenario.getExpectedOutputs();
            Enumeration outputKeys = expectedOutputs.keys();

            while (outputKeys.hasMoreElements()) {
                String key = ((TestArtifact) expectedOutputs.get(outputKeys.nextElement())).getId();

                TestArtifact expectedOutput = (TestArtifact) expectedOutputs.get(key);
                if (expectedOutput.getContent() == null) {
                    compare(new File(testCasePath, expectedOutput.getMessageFile()), new File(outputDir, key + "." + expectedOutput.getMessageType()), expectedOutput.getMessageType());
                } else
                    compare(expectedOutput.getContent(), new File(outputDir, key + "." + expectedOutput.getMessageType()));
            }
        }
    }

    private void compare(File expectedOutput, File output, String messageType) throws IOException {

        if (!output.exists())
            throw new AssertionError("!!!! THE OUTPUT FILE OF THE FLOWTEST WAS NOT CREATED, POSSIBLY DUE TO OUTPUT NOT RECEIVED " +
                    "AT THE REQUIRED PORT !!!");

        if (messageType.equals(TestArtifact.XML_MESSAGE)) {
            //TODO : do an xml comparision. more gracefuly
            //TODO: chk for comparisons where the element values can be different
            XMLTestCase xmlTestCase = new XMLTestCase(){};
            //xmlTestCase.setIgnoreWhitespace(true);
            try {
                if (!((xmlTestCase).compareXML(new FileReader(expectedOutput), new FileReader(output))).similar())
                    throw new ComparisonFailure("TestResultValidation Failed: ", "Expected Output file: " + expectedOutput.getCanonicalPath(), "Output file: " + output.getCanonicalPath());
            } catch (Exception e) {
                if (e.getMessage() != null) e.printStackTrace();
                throw new AssertionError("Failed to compare test results: " + getName());
            }
        } else {
            String expectedOutputMessage = getFileAsString(expectedOutput);
            String outputMessage = getFileAsString(output);

            String exop = removeesc(expectedOutputMessage);
            String op = removeesc(outputMessage);


            if (!exop.equals(op))
                throw new ComparisonFailure("TestResultValidation Failed: ", "Expected Output: \n" + exop, "Output: \n" + op);
        }
    }

    private String removeesc(String exM) {

        String c = exM.replaceAll("\r", "");

        return c;

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

    private void compare(String expectedOutput, File output) throws IOException {
        //cannot be an xml comparision.
        String outputMessage = getFileAsString(output);
        String expM = removeesc(expectedOutput);
        String opM = removeesc(outputMessage);

        if (!opM.equals(expM))
            throw new ComparisonFailure("TestResultValidation Failed: ", "Expected Output: \n" + expM, "Output : \n" + opM);
    }


    private void addListeners(TestScenario scenario) throws Exception {
        Hashtable expectedOutputs = scenario.getExpectedOutputs();
        int numberOfOutPorts = expectedOutputs.size();
        Enumeration outputKeys = expectedOutputs.keys();
        OutputPortMessageListener[] outputMessageListener = new OutputPortMessageListener[numberOfOutPorts];

        for (int i = 0; outputKeys.hasMoreElements(); i++) {
            String key = (String) outputKeys.nextElement();
            TestArtifact output = (TestArtifact) expectedOutputs.get(key);
            outputMessageListener[i] = new OutputPortMessageListener(
                    output.getId() + "." + output.getMessageType(), testOutputPath, output.getTimeout());
            outputMessageListener[i].start();
            processHandle.addListener(output.getComponentName(), output.getPortName(), outputMessageListener[i]);

            listenerMap.put(output.getId() + "." + output.getMessageType(), outputMessageListener[i]);
        }
    }

    private void runScenario(TestScenario scenario) throws Exception {
        Iterator inputs = scenario.getInputs().iterator();
        Hashtable expectedOutputs = scenario.getExpectedOutputs();
        Enumeration outputKeys = expectedOutputs.keys();

        while (inputs.hasNext()) {
            TestArtifact input = (TestArtifact) inputs.next();
            if (input.getContent() == null)
                processHandle.sendMessage(input.getComponentName(), input.getPortName(), new File(testCasePath, input.getMessageFile()));
            else
                processHandle.sendMessage(input.getComponentName(), input.getPortName(), input.getContent());
        }

        for (int i = 0; outputKeys.hasMoreElements(); i++) {
            String key = (String) outputKeys.nextElement();
            TestArtifact output = (TestArtifact) expectedOutputs.get(key);
            OutputPortMessageListener listener = listenerMap.get(output.getId() + "." + output.getMessageType());
            listener.join(listener.getTimeout());
            if (listener.isAlive()) {
                System.out.println("Message Not Received for all components");
                listener.interrupt();
                listener.join(1000);
            }
        }
    }

    private String[] getPeerMachineAddress() throws Exception {

        ArrayList<String> peerMachineAddresses = new ArrayList<String>();
        ServerStatusController ssController = ServerStatusController.getInstance();
        StringHashtable serverElemList = ssController.getCurrentSuiteElement().getServers();

        Application application = rtlClient.getFioranoApplicationController().getApplication(flowTestCaseConfig.getAppGUID(), Float.parseFloat(flowTestCaseConfig.getAppVersion()));
        String[] instanceNames;
        if (flowTestCaseConfig.getInstanceName() == null)
            instanceNames = flowTestCaseConfig.getInstanceNames();
        else {
            instanceNames = new String[1];
            instanceNames[0] = flowTestCaseConfig.getInstanceName();
        }

        for (String instanceName : instanceNames) {
            ServiceInstance serviceInstance = application.getServiceInstance(instanceName);
            String peername = null;
            for (String node : serviceInstance.getNodes()) {
                for (Object server : serverElemList.keySet()) {
                    if (node.equals(server)) {
                        peername = node;
                        break;
                    }
                }
            }
            String peerUrl = rtlClient.getFioranoFPSManager().getConnectURLForFPS(peername);
            if (!peerMachineAddresses.contains(getMachineAddress(peerUrl)))
                peerMachineAddresses.add(getMachineAddress(peerUrl));
        }

        return peerMachineAddresses.toArray(new String[]{});
    }


    private String getMachineAddress(String url) {
        if (url == null) return null;

        int protocolEnd = url.indexOf("//");
        int addBegin;
        if (protocolEnd == -1) addBegin = 0;
        else addBegin = protocolEnd + 2;

        int addEnd = url.indexOf(':', addBegin);
        if (addEnd == -1) addEnd = url.length();

        return url.substring(addBegin, addEnd);
    }

}
