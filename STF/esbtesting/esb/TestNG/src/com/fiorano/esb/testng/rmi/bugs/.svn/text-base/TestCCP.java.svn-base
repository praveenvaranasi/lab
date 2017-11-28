package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.ccp.event.CCPEventType;
import com.fiorano.ccp.event.ControlEvent;
import com.fiorano.ccp.event.EventFactory;
import com.fiorano.ccp.event.common.DataEvent;
import com.fiorano.ccp.event.common.DataRequestEvent;
import com.fiorano.ccp.event.common.LogLevelRequestEvent;
import com.fiorano.ccp.event.common.data.LogLevel;
import com.fiorano.ccp.event.common.data.MemoryUsage;
import com.fiorano.ccp.event.component.HandShakeAckEvent;
import com.fiorano.ccp.event.component.StatusEvent;
import com.fiorano.ccp.event.peer.CommandEvent;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jms.BytesMessage;
import java.io.*;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;


/**
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Feb 23, 2011
 * Time: 2:22:27 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestCCP extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private String appName1 = "TEST_CCP";
    private String customComponentName = "ccp";
    private String fioranoHome = null;
    private String testingHome = null;
    private String customComponentPath = null;
    private String customComponentBuildPath = null;
    private String customComponentDeployPath = null;
    private String esbToComponent = null;
    private TestEnvironmentConfig testenvconfig;
    private ExecutionController remoteProxy;
    private String outputLog = "output.log";
    private float appVersion = 1.0f;

    @Test(groups = "TestCCP", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestCCP"));
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            //Assert.fail("Cannot run Group RemoteServiceInstance as eventProcessManager is not set.");
        }
        System.setProperty("DontSetReadOnly", "false");
        testenvconfig = ESBTestHarness.getTestEnvConfig();
        remoteProxy = ExecutionController.getInstance();
        fioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        testingHome = testenvconfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        esbToComponent = "esb" + File.separator + "tools" + File.separator + "templates" + File.separator + customComponentName;
        customComponentPath = testingHome + File.separator + "esb" + File.separator + "TestNG" + File.separator + "resources" + File.separator + customComponentName;
        customComponentBuildPath = fioranoHome + File.separator + "esb" + File.separator + "tools" + File.separator + "templates" + File.separator + customComponentName;
        customComponentDeployPath = fioranoHome + File.separator + "runtimedata" + File.separator + "repository" + File.separator + "components" + File.separator + customComponentName;
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestCCP"));
    }


    @Test(groups = "TestCCP", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testCreateCustomComponent() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("testCreateCustomComponent", "TestCCP"));
        File file = new File(customComponentBuildPath);
        if (file.exists()) {
            boolean status = TestInMemory17763.deleteDirectory(file);
            if (!status) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateCustomComponent", "TestCCP"));
               // Assert.fail("Failed to delete pre existed custom component!");
            }
            TestInMemory17763.copyDirectory(new File(customComponentPath), new File(customComponentBuildPath));
        } else {
            TestInMemory17763.copyDirectory(new File(customComponentPath), new File(customComponentBuildPath));
        }

        Properties properties = new Properties();
        properties.load(new FileInputStream(customComponentBuildPath + File.separator + "build.properties"));
        properties.setProperty("installer.dir", fioranoHome);
        properties.setProperty("esb.user.dir", fioranoHome + File.separator + "runtimedata");
        properties.setProperty("server", "tsp_tcp://" + ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer() + ":1947");
        properties.store(new FileOutputStream(customComponentBuildPath + File.separator + "build.properties"), null);
        Properties properties1 = new Properties();
        properties1.load(new FileInputStream(customComponentBuildPath + File.separator + "etc" + File.separator + "resources.properties"));
        properties1.setProperty("icon32", fioranoHome + File.separator + "esb" + File.separator + "samples" + File.separator + "icons" + File.separator + "png_icons" + File.separator + "icon32.png");
        properties1.setProperty("icon", fioranoHome + File.separator + "esb" + File.separator + "samples" + File.separator + "icons" + File.separator + "png_icons" + File.separator + "icon.png");
        properties1.store(new FileOutputStream(customComponentBuildPath + File.separator + "etc" + File.separator + "resources.properties"), null);

        File buildFile = new File(customComponentDeployPath);
        if (buildFile.exists()) {
            remoteProxy.antunRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antunregister", esbToComponent, outputLog);
            Thread.sleep(5000);
        }
        File outLog = new File(outputLog);
        if (outLog.exists())
            outLog.delete();
        remoteProxy.antRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antregister", esbToComponent, outputLog);
        Thread.sleep(5000);
        boolean checkBuild = VerifyBuildProcess();
        if (!checkBuild) {
            //Assert.fail("failed to register custom component");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateCustomComponent", "TestCCP"));

    }

    @Test(groups = "TestCCP", dependsOnMethods = "testCreateCustomComponent", alwaysRun = true)
    public void testLaunchOfEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOfEventProcess", "TestCCP"));
        SampleEventProcessListener epListener1 = null;

        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("test_ccp-1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, 1.0f, false);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do start application!", e);
        }
        //clear map
        eventStore1.clear();
        try {
            eventProcessManager.startAllServices(appName1, 1.0f);
            Thread.sleep(10000);


           // Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "1");


        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestCCP"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfApplication", "TestCCP"), e);
            //Assert.fail("Error with thread sleep!", e);
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOfApplication", "TestCCP"));

    }

    @Test(groups = "TestCCP", dependsOnMethods = "testLaunchOfEventProcess", alwaysRun = true)
    public void testSendMessage() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testSendMessage", "TestCCP"));
        Receiver rec = new Receiver(true, true);
        rec.setCf("PrimaryCF");
        rec.setDestinationName("CCP_COMPONENT_TO_PEER_TRANSPORT");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessage", "TestCCP"), e);
            //Assert.fail("Failed to initialize receiver", e);
        }

        Publisher pub = new Publisher();
        pub.setCf("PrimaryCF");
        pub.setDestinationName("CCP_PEER_TO_COMPONENT_TRANSPORT");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessage", "TestCCP"), e);
           // Assert.fail("Failed to initialize publisher", e);
        }
        try {

            BytesMessage bytesMessageSent = pub.getSession().createBytesMessage();
            CommandEvent commandEvent = new CommandEvent();
            commandEvent.setCommand(CommandEvent.Command.REPORT_STATE);
            commandEvent.setReplyNeeded(true);
            commandEvent.setReplyTimeout(10000);
            commandEvent.setCorrelationID(System.currentTimeMillis());
            commandEvent.toMessage(bytesMessageSent);
            String target = "TEST_CCP__1_0__ccp1;";
            bytesMessageSent.setStringProperty(ControlEvent.TARGET_OBJECTS, target);
            bytesMessageSent.setStringProperty(ControlEvent.EVENT_TYPE_HEADER, commandEvent.getEventType().toString());
            pub.sendMessage(bytesMessageSent);
            Thread.sleep(10000);
            ArrayList arr = rec.getMessages();
           // Assert.assertNotNull(arr);
            BytesMessage bytesMessageRec = (BytesMessage) arr.get(0);
            bytesMessageRec.reset();
            String component = bytesMessageRec.getStringProperty(ControlEvent.SOURCE_OBJECT);
            CCPEventType eventType = CCPEventType.valueOf(bytesMessageRec.getStringProperty(ControlEvent.EVENT_TYPE_HEADER));
            ControlEvent event = EventFactory.getEvent(eventType);
            event.fromMessage(bytesMessageRec);
            //Assert.assertEquals((Object) commandEvent.getEventId(), (Object) event.getCorrelationID());
            StatusEvent statusEvent = (StatusEvent) event;
            //Assert.assertEquals(statusEvent.getOperationScope().name(), "COMPONENT_RUNNING");
            System.out.println("Response sent by component instance : " + component);
            System.out.println(eventToString(event));
            rec.getMessages().clear();

            // send another message

            BytesMessage bytesMessageSent1 = pub.getSession().createBytesMessage();
            Collection<DataRequestEvent.DataIdentifier> drIdentifier = new ArrayList<DataRequestEvent.DataIdentifier>();
            drIdentifier.add(DataRequestEvent.DataIdentifier.MEMORY_USAGE);
            DataRequestEvent drEvent = new DataRequestEvent();
            drEvent.setDataIdentifiers(drIdentifier);
            drEvent.setReplyNeeded(true);
            drEvent.setReplyTimeout(30000);
            drEvent.setCorrelationID(System.currentTimeMillis());
            drEvent.setRepetitionCount(1);
            drEvent.setReplyNeeded(true);
            drEvent.toMessage(bytesMessageSent1);
            bytesMessageSent1.setStringProperty(ControlEvent.TARGET_OBJECTS, target);
            bytesMessageSent1.setStringProperty(ControlEvent.EVENT_TYPE_HEADER, drEvent.getEventType().toString());
            pub.sendMessage(bytesMessageSent1);
            Thread.sleep(10000);
            arr = rec.getMessages();
            Assert.assertNotNull(arr);
            BytesMessage bytesMessageRec1 = (BytesMessage) arr.get(0);
            bytesMessageRec1.reset();
            String component1 = bytesMessageRec1.getStringProperty(ControlEvent.SOURCE_OBJECT);
            CCPEventType eventType1 = CCPEventType.valueOf(bytesMessageRec1.getStringProperty(ControlEvent.EVENT_TYPE_HEADER));
            ControlEvent event1 = EventFactory.getEvent(eventType1);
            event1.fromMessage(bytesMessageRec1);
            System.out.println("Response sent by component instance : " + component1);
           // Assert.assertEquals((Object) drEvent.getEventId(), (Object) event1.getCorrelationID());
            System.out.println(eventToString(event1));
            rec.getMessages().clear();

            // send another message

            BytesMessage bytesMessageSent2 = pub.getSession().createBytesMessage();
            ArrayList<String> ar1 = new ArrayList<String>();
            ar1.add("com.fiorano.edbc.ccp.ccp");
            ar1.add("com.fiorano.edbc.framework.service.ccp.jms");
            ar1.add("ERR_HANDLER");
            ar1.add("OUT_HANDLER");
            LogLevelRequestEvent logEvent = new LogLevelRequestEvent(ar1);
            logEvent.setRepetitionCount(1);
            logEvent.setReplyNeeded(true);
            logEvent.setReplyTimeout(10000);
            logEvent.setCorrelationID(System.currentTimeMillis());
            logEvent.toMessage(bytesMessageSent2);
            logEvent.setCorrelationID(System.currentTimeMillis());
            bytesMessageSent2.setStringProperty(ControlEvent.TARGET_OBJECTS, target);
            bytesMessageSent2.setStringProperty(ControlEvent.EVENT_TYPE_HEADER, logEvent.getEventType().toString());
            pub.sendMessage(bytesMessageSent2);
            Thread.sleep(10000);
            arr = rec.getMessages();
           // Assert.assertNotNull(arr);
            BytesMessage bytesMessageRec2 = (BytesMessage) arr.get(0);
            bytesMessageRec2.reset();
            String component2 = bytesMessageRec2.getStringProperty(ControlEvent.SOURCE_OBJECT);
            CCPEventType eventType2 = CCPEventType.valueOf(bytesMessageRec2.getStringProperty(ControlEvent.EVENT_TYPE_HEADER));
            ControlEvent event2 = EventFactory.getEvent(eventType2);
            event2.fromMessage(bytesMessageRec2);
            System.out.println("Response sent by component instance : " + component2);
            DataEvent dataEvent = (DataEvent) event2;
            //Assert.assertEquals(((LogLevel) (dataEvent.getData().get(DataRequestEvent.DataIdentifier.LOG_LEVELS))).getLoggerLevels().get("com.fiorano.edbc.framework.service.ccp.jms").toString().equals("SEVERE"), true);
           // Assert.assertEquals(((LogLevel) (dataEvent.getData().get(DataRequestEvent.DataIdentifier.LOG_LEVELS))).getLoggerLevels().get("com.fiorano.edbc.ccp.ccp").toString().equals("WARNING"), true);
            //Assert.assertEquals(((LogLevel) (dataEvent.getData().get(DataRequestEvent.DataIdentifier.LOG_LEVELS))).getLoggerLevels().get("ERR_HANDLER").toString().equals("WARNING"), true);
            //Assert.assertEquals(((LogLevel) (dataEvent.getData().get(DataRequestEvent.DataIdentifier.LOG_LEVELS))).getLoggerLevels().get("OUT_HANDLER").toString().equals("INFO"), true);
           // Assert.assertEquals((Object) logEvent.getEventId(), (Object) event2.getCorrelationID());
            System.out.println(eventToString(event2));
            rec.getMessages().clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSendMessage", "TestCCP"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessage", "TestCCP"), e);
            //Assert.fail("Failed in testSendMessage Method ", e);
        } finally {
            pub.close();
            rec.close();
        }
    }

    @Test(groups = "TestCCP", dependsOnMethods = "testSendMessage", alwaysRun = true)
    public void testStopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStopAndDeleteApplication", "TestCCP"));
        try {
            System.setProperty("DontSetReadOnly", "true");
            eventProcessManager.stopEventProcess(appName1, 1.0f);
            Thread.sleep(5000);
            eventProcessManager.deleteEventProcess(appName1, 1.0f, true, false);
            Thread.sleep(5000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAndDeleteApplication", "TestCCP"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopAndDeleteApplication", "TestCCP"), e);
            //Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" + File.separator + "resources" + File.separator + zipName));
        boolean completed = false;
        byte result[];
        while (!completed) {
            byte[] temp = new byte[1024];
            int readcount = 0;
            readcount = bis.read(temp);

            if (readcount < 0) {
                completed = true;
                readcount = 0;
            }
            result = new byte[readcount];
            System.arraycopy(temp, 0, result, 0, readcount);

            // it will deploy the event process if the correct byte array is send; else gives an error
            this.eventProcessManager.deployEventProcess(result, completed);
        }
    }

    @Test(enabled = false)
    private String eventToString(ControlEvent event) {
        StringBuilder builder = new StringBuilder();

        if (event instanceof StatusEvent) {
            StatusEvent statusEvent = (StatusEvent) event;
            builder.append("Status Event Properties").append("\n");
            builder.append("-------------------------------------").append("\n");
            builder.append(" Status Type : ").append(statusEvent.getStatusType().name()).append("\n");
            builder.append(" Status : ").append(statusEvent.getStatus().name()).append("\n");
            builder.append(" Operation Scope : ").append(statusEvent.getOperationScope().name()).append("\n");

            String errorMessage = statusEvent.getErrorMessage();
            if (errorMessage != null)
                builder.append(" Error Message : ").append(errorMessage).append("\n");
        } else if (event instanceof DataEvent) {
            DataEvent dataEvent = (DataEvent) event;
            builder.append("Data Event Properties").append("\n");
            builder.append("-------------------------------------").append("\n");
            builder.append(" Data Sent : ").append(dataEvent.getData() != null ? dataEvent.getData().toString() : "").append("\n");
            builder.append("Non Heap Memory Allocated:").append((dataEvent.getData().containsKey(DataRequestEvent.DataIdentifier.MEMORY_USAGE)) == true ? ((MemoryUsage) dataEvent.getData().get(DataRequestEvent.DataIdentifier.MEMORY_USAGE)).getNonHeapMemoryAllocated() : "").append("\n");
            builder.append("Log Levels:").append((dataEvent.getData().containsKey(DataRequestEvent.DataIdentifier.LOG_LEVELS)) == true ? ((LogLevel) dataEvent.getData().get(DataRequestEvent.DataIdentifier.LOG_LEVELS)).getLoggerLevels() : "").append("\n");
        } else if (event instanceof HandShakeAckEvent) {
            HandShakeAckEvent handShakeAckEvent = (HandShakeAckEvent) event;
            builder.append("Hand-shake Acknowledgement Event Properties").append("\n");
            builder.append("-------------------------------------").append("\n");
            builder.append(" CCP Supported : ").append(handShakeAckEvent.isCcpSupported()).append("\n");
            builder.append(" Min CCP Version Supported : ").append(handShakeAckEvent.getMinVersionSupported()).append("\n");
            builder.append(" Max CCP Version Supported : ").append(handShakeAckEvent.getMaxVersionSupported()).append("\n");
            builder.append(" Comment : ").append(handShakeAckEvent.getComment()).append("\n");
        }

        builder.append(" Event ID : ").append(event.getEventId()).append("\n");
        builder.append(" Protocol Version : ").append(event.getProtocolVersion()).append("\n");
        builder.append(" Event Time : ").append(new Date(event.getDateTime())).append("\n");
        builder.append(" Event Description : ").append(event.getDescription()).append("\n");
        builder.append(" Event Priority : ").append(event.getPriority()).append("\n");
        builder.append(" Event Expiry Time : ").append(event.getExpiryTime() != -1 ? new Date(event.getExpiryTime()) : event.getExpiryTime()).append("\n");
        builder.append(" Reply Needed : ").append(event.isReplyNeeded()).append("\n");
        builder.append(" Reply Timeout : ").append(event.getReplyTimeout()).append("\n");
        builder.append(" Correlation ID : ").append(event.getCorrelationID()).append("\n");
        return builder.toString();
    }

    @Test(enabled = false)
    private boolean VerifyBuildProcess() {
        FileReader outLog = null;
        boolean checkBuild = false;
        try {
            outLog = new FileReader(fioranoHome + File.separator + "output.log");
            BufferedReader br = new BufferedReader(outLog);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains("BUILD FAILED")) {
                    checkBuild = false;
                    break;
                } else if (line.contains("BUILD SUCCESSFUL")) {
                    checkBuild = true;
                    break;
                } else
                    continue;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            new File(fioranoHome + File.separator + "output.log").delete();
        }
        return checkBuild;
    }

}
