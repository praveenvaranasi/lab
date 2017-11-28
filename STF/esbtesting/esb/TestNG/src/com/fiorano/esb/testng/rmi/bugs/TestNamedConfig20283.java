package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IConfigurationManager;
import com.fiorano.esb.server.api.IConfigurationRepositoryListener;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.configuration.data.*;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 2/2/11
 * Time: 2:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestNamedConfig20283 extends AbstractTestNG {

    private IConfigurationRepositoryListener configRepoListener;
    private IConfigurationManager configManager;
    PortConfigurationDataObject dataObject = new PortConfigurationDataObject();
    //NamedObject nameObject ;
    PortConfigurationNamedObject pcNameObject;
    private IEventProcessManager eventProcessManager;
    private float appVersion = 1.0f;
    @Test(groups = "TestNamedConfig20283", alwaysRun = true)
    public void startSetUp() throws RemoteException {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.serverStatusController = ServerStatusController.getInstance();
        try {
            this.m_fioranoappcontroller = serverStatusController.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestNamedConfig20283"), e);
            //org.testng.Assert.fail("Failed get the application controller!", e);
        }

        if (eventProcessManager == null) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetUp", "TestNamedConfig20283"));
            //Assert.fail("failed to create port Named configuration");
        }
    }

    TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
    String fioranohome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
    File namedConfigPathFile = new File(fioranohome + "/runtimedata/repository/configurations/ports/output/");

    @Test(groups = "TestNamedConfig20283", dependsOnMethods = "startSetUp", alwaysRun = true)
    public void createNamedConfigForPort() {
        configManager = rmiClient.getConfigurationManager();
        try {
            configRepoListener = new ConfigurationListener();
            configManager.addConfigurationRepositoryListener(configRepoListener);
            dataObject.setDestinationType(DestinationType.TOPIC);
            dataObject.setObjectCategory(ObjectCategory.PORT_CONFIGURATION);
            dataObject.setPortType(PortType.OUTPUT);
            dataObject.setDataType(DataType.TEXT);
            dataObject.setName("TestNamedConfiguration1");
            dataObject.setLabel(Label.TESTING);
            String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<publisher>\n" +
                    "  <messages persistent=\"true\"/>\n" +
                    "</publisher>";
            dataObject.setData(data.getBytes());
            //create instance of PortConfigNamedObject for deletion of named config
            pcNameObject = new PortConfigurationNamedObject(dataObject.getName(), dataObject.getLabel(), dataObject.getObjectCategory(), dataObject.getDestinationType(), dataObject.getPortType());

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createNamedConfigForPort", "TestNamedConfig20283"), e);
            //Assert.fail("failed to create port Named configuration", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createNamedConfigForPort", "TestNamedConfig20283"), e);
            //Assert.fail("failed to create port Named configuration", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("createNamedConfigForPort", "TestNamedConfig20283"));

    }

    private ServerStatusController serverStatusController;
    private FioranoApplicationController m_fioranoappcontroller;
    private Application application;
    List<ServiceInstance> serviceInstanceList;
    private String appGUID = "NAMEDCONF";
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private boolean exceptionWhileSaveAndLaunchStatus = false;

    @Test(groups = "TestNamedConfig20283", description = "Check for existence of named configurations while saving and compiling applications", dependsOnMethods = "createNamedConfigForPort", alwaysRun = true)
    public void chkNamedConfigurationWhileSavingCompilingApp() {
        Logger.LogMethodOrder(Logger.getOrderMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
        SampleEventProcessListener epListener = null;
        try {
            epListener = new SampleEventProcessListener(appGUID, eventStore);
            eventProcessManager.addEventProcessListener(epListener);
            try {
                deployEventProcess("namedconf-1.0@EnterpriseServer.zip");
            } catch (Exception e) {
                if (((ServiceException) e).getErrorCode().equalsIgnoreCase("INTERNAL_ERROR"))
                    exceptionWhileSaveAndLaunchStatus = true;
            }
            Thread.sleep(3000);
            if (!exceptionWhileSaveAndLaunchStatus) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
                //Assert.fail("failed to raise exception while saving application after named configuration deletion!");
            }
            configManager.persistConfiguration(dataObject, true);
            Thread.sleep(2000);
            exceptionWhileSaveAndLaunchStatus = false;
            try {
                deployEventProcess("namedconf-1.0@EnterpriseServer.zip");
            } catch (Exception e) {
                exceptionWhileSaveAndLaunchStatus = true;
            }
            Thread.sleep(3000);
            if (exceptionWhileSaveAndLaunchStatus) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
                //Assert.fail("failed while saving application!");
            }

            //checks while doing CRC application.
            if (namedConfigPathFile.exists()) {
                TestInMemory17763.deleteDirectory(namedConfigPathFile);
            }

            try {
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
            } catch (Exception e) {
                if (((ServiceException) e).getErrorCode().equalsIgnoreCase("INTERNAL_ERROR"))
                    exceptionWhileSaveAndLaunchStatus = true;
            }
            //checking the exceptionWhileSaveAndLaunchStatus to true as we are deleting the Named Configuration before CRC
            if (!exceptionWhileSaveAndLaunchStatus) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
               // Assert.fail("test case failed as it not raises exception while CRC application!");
            }

            try {
                configManager.persistConfiguration(dataObject, true);
                Thread.sleep(5000);
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
               // Assert.fail("failed to create named configuration third time!");
            }
            if (!namedConfigPathFile.exists()) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
                //Assert.fail("failed to create port Named configuration third time after crc exception");
            }
            exceptionWhileSaveAndLaunchStatus = false;
            try {
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
            } catch (Exception e) {
                exceptionWhileSaveAndLaunchStatus = true;
            }
            //checking the exceptionWhileSaveAndLaunchStatus to true as we are deleting the Named Configuration before CRC
            if (exceptionWhileSaveAndLaunchStatus) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
               // Assert.fail("failed as it raises exception while saving application!");
            }

            //checks while staring the application.
            if (namedConfigPathFile.exists()) {
                TestInMemory17763.deleteDirectory(namedConfigPathFile);
            }
            try {
                eventProcessManager.startEventProcess(appGUID, appVersion, false);
            } catch (Exception e) {
                if (((ServiceException) e).getErrorCode().equalsIgnoreCase("INTERNAL_ERROR"))
                    exceptionWhileSaveAndLaunchStatus = true;
            }

            if (!exceptionWhileSaveAndLaunchStatus) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
                //Assert.fail("failed as it not raises exception while starting application!");
            }

            try {
                configManager.persistConfiguration(dataObject, true);
                Thread.sleep(5000);
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
               // Assert.fail("failed to create named configuration third time!");
            }
            if (!namedConfigPathFile.exists()) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
              //  Assert.fail("failed to create port Named configuration fourth time after start ep check!");
            }
            exceptionWhileSaveAndLaunchStatus = false;
            try {
                //eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
                //Thread.sleep(1000);
                eventProcessManager.startEventProcess(appGUID, appVersion, false);
                Thread.sleep(2000);
                eventProcessManager.startAllServices(appGUID, appVersion);
                Thread.sleep(3000);
            } catch (Exception e) {
                exceptionWhileSaveAndLaunchStatus = true;
            }

            //checking the exceptionWhileSaveAndLaunchStatus to true as we are deleting the Named Configuration before CRC
            if (exceptionWhileSaveAndLaunchStatus) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));
                //Assert.fail("failed as it raises exception while saving application!");
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"), e);
           // Assert.fail("failed to start ep!", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("chkNamedConfigurationWhileSavingCompilingApp", "TestNamedConfig20283"));

    }

    File TestingHome = new File(testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME));

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + "/esb/TestNG/resources/" + zipName));
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

    @Test(groups = "TestNamedConfig20283", dependsOnMethods = "chkNamedConfigurationWhileSavingCompilingApp", alwaysRun = true)
    public void testStopDeleteApplication() {
        try {
            eventProcessManager.stopEventProcess(appGUID, appVersion);

            try {
                if (namedConfigPathFile.exists()) {
                    configManager.deleteConfiguration(pcNameObject);
                }
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, appVersion, true, false);
        } catch (RemoteException e) {
            //Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
           // org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            //Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
           // org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testStopDeleteApplication", "TestNamedConfig20283"));

    }

    private class ConfigurationListener extends UnicastRemoteObject implements IConfigurationRepositoryListener {
        private ConfigurationListener() throws RemoteException {
        }

        public void configurationPersisted(NamedObject namedObject) throws RemoteException {
           // Assert.assertEquals(namedObject, dataObject);
        }

        public void configurationDeleted(NamedObject namedObject) throws RemoteException {
           // Assert.assertEquals(namedObject, pcNameObject);
        }
    }
}
