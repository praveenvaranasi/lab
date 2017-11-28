package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.sbw.FioranoSBWManager;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.server.api.IConfigurationManager;
import com.fiorano.esb.server.api.IConfigurationRepositoryListener;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.SBWSearchContext;
import fiorano.tifosi.dmi.configuration.data.*;
import fiorano.tifosi.dmi.ste.DocumentInfo;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Feb 25, 2011
 * Time: 4:36:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestNamedWorkflowConfig extends AbstractTestNG {
    private IConfigurationRepositoryListener configRepoListener;
    private IConfigurationManager configManager;
    private IEventProcessManager eventProcessManager;
    private ServerStatusController serverStatusController;
    private FioranoServiceProvider fsp;
    private FioranoSBWManager sbwManager;
    private SBWSearchContext sbwSearchContext = new SBWSearchContext();
    private DataObject dataObject1 = new DataObject();
    private DataObject dataObject2 = new DataObject();
    private DataObject dataObject3 = new DataObject();
    private NamedObject namedObject1;
    private NamedObject namedObject2;
    private NamedObject namedObject3;
    private String appName1 = "CHAT";
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private Publisher pub;
    private float appVersion = 1.0f;
    @Test(groups = "TestNamedWorkflowConfig", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestNamedWorkflowConfig"));
        configManager = rmiClient.getConfigurationManager();
        eventProcessManager = rmiClient.getEventProcessManager();
        serverStatusController = ServerStatusController.getInstance();
        try {
            fsp = serverStatusController.getServiceProvider();
            sbwManager = fsp.getSBWManager();
            sbwSearchContext.setAppGUID(appName1);
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestNamedWorkflowConfig"), e);
            //Assert.fail("Failed in startSetup method!", e);
        }

        dataObject1.setObjectCategory(ObjectCategory.WORKFLOW);
        dataObject1.setDataType(DataType.TEXT);
        dataObject1.setName("item");
        dataObject1.setLabel(Label.DEVELOPMENT);
        String data1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<workflow datatype=\"0\" type=\"1\"/>";
        dataObject1.setData(data1.getBytes());
        namedObject1 = new NamedObject("item", Label.DEVELOPMENT, ObjectCategory.WORKFLOW);

        dataObject2.setObjectCategory(ObjectCategory.WORKFLOW);
        dataObject2.setDataType(DataType.TEXT);
        dataObject2.setName("end");
        dataObject2.setLabel(Label.DEVELOPMENT);
        String data2 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<workflow datatype=\"0\" type=\"2\"/>";
        dataObject2.setData(data2.getBytes());
        namedObject2 = new NamedObject("end", Label.DEVELOPMENT, ObjectCategory.WORKFLOW);

        dataObject3.setObjectCategory(ObjectCategory.WORKFLOW);
        dataObject3.setDataType(DataType.TEXT);
        dataObject3.setName("none");
        dataObject3.setLabel(Label.DEVELOPMENT);
        String data3 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<workflow type=\"0\"/>";
        dataObject3.setData(data3.getBytes());
        namedObject3 = new NamedObject("none", Label.DEVELOPMENT, ObjectCategory.WORKFLOW);

        try {
            configManager.persistConfiguration(dataObject1, true);
            configManager.persistConfiguration(dataObject2, true);
            configManager.persistConfiguration(dataObject3, true);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestNamedWorkflowConfig"), e);
            //Assert.fail("Failed in startSetup method!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestNamedWorkflowConfig"), e);
            //Assert.fail("Failed in startSetup method!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestNamedWorkflowConfig"));
    }


    @Test(groups = "TestNamedWorkflowConfig", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testLaunchOfEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"));
        SampleEventProcessListener epListener1 = null;
        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("chat-1.0.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
            //Assert.fail("Failed to do start application!", e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to do start application!", e);
        }
        //clear map
        eventStore1.clear();
        try {
            eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(10000);

           // Assert.assertEquals(eventStore1.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName1), "2");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
          //  Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfEventProcess", "TestNamedWorkflowConfig"), e);
          //  Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfApplication", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Error with thread sleep!", e);
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOfApplication", "TestNamedWorkflowConfig"));

    }

    @Test(groups = "TestNamedWorkflowConfig", dependsOnMethods = "testLaunchOfEventProcess", alwaysRun = true)
    public void testWorkflowConfiguration() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testWorkflowConfiguration", "TestNamedWorkflowConfig"));
            eventProcessManager.changeSBWConfiguration("chat1", appName1,appVersion, "OUT_PORT", "item");
            createPubSub();
            Thread.sleep(5000);
            Enumeration enum1 = sbwManager.searchDocumentInfos(sbwSearchContext, 0, 10);
            //Assert.assertEquals(((DocumentInfo) enum1.nextElement()).getWorkflowStatus().equals("EXECUTING"), true);
            sbwManager.clearWorkflowInfo(appName1,"appVersion");
            eventProcessManager.changeSBWConfiguration("chat2", appName1,IEventProcessManager.ANY_VERSION, "IN_PORT", "end");
            createPubSub();
            Thread.sleep(5000);
            Enumeration enum2 = sbwManager.searchDocumentInfos(sbwSearchContext, 0, 10);
            while (enum2.hasMoreElements()) {
                DocumentInfo doc = ((DocumentInfo) enum2.nextElement());
                if (doc.getServInstName().equals("chat1"))
                    Assert.assertEquals(doc.getWorkflowStatus().equals("EXECUTING"), true);
                else
                    Assert.assertEquals(doc.getWorkflowStatus().equals("EXECUTED"), true);
            }
            sbwManager.clearWorkflowInfo(appName1,"appVersion");
            eventProcessManager.changeSBWConfiguration("chat2", appName1,appVersion, "IN_PORT", "none");
            eventProcessManager.changeSBWConfiguration("chat1", appName1,appVersion, "OUT_PORT", "none");
            createPubSub();
            Thread.sleep(5000);
            Enumeration enum3 = sbwManager.searchDocumentInfos(sbwSearchContext, 0, 10);
           // Assert.assertEquals(enum3.hasMoreElements(), false);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testWorkflowConfiguration", "TestNamedWorkflowConfig"));
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWorkflowConfiguration", "TestNamedWorkflowConfig"), e);
            //Assert.fail("Failed in testWorkflowConfiguration method", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWorkflowConfiguration", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed in testWorkflowConfiguration method", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWorkflowConfiguration", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed in testWorkflowConfiguration method ", e);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testWorkflowConfiguration", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed in testWorkflowConfiguration method", e);
        } finally {
            pub.close();
        }


    }


    @Test(groups = "TestNamedWorkflowConfig", dependsOnMethods = "testWorkflowConfiguration", alwaysRun = true)
    public void testStopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStopAndDeleteApplication", "TestNamedWorkflowConfig"));
        try {
            eventProcessManager.stopEventProcess(appName1, appVersion);
            Thread.sleep(5000);
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            Thread.sleep(5000);
            configManager.deleteConfiguration(namedObject1);
            configManager.deleteConfiguration(namedObject2);
            configManager.deleteConfiguration(namedObject3);
            Thread.sleep(5000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAndDeleteApplication", "TestNamedWorkflowConfig"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopAndDeleteApplication", "TestNamedWorkflowConfig"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
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
    public void createPubSub() {

        pub = new Publisher();
        pub.setDestinationName(appName1 + "1_0" + "__" + "CHAT1" + "__OUT_PORT");
        pub.setCf(appName1 + "1_0" + "__" + "CHAT1");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
           // Assert.fail("Failed to do publish message on outport", e);
        }


    }
}
