package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IConfigurationManager;
import com.fiorano.esb.server.api.IConfigurationRepositoryListener;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.configuration.data.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 2/11/11
 * Time: 11:27 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestMaintainInternalVersionB20059 extends  AbstractTestNG {
    private IEventProcessManager eventProcessManager;

    @Test(groups = "TestMaintainIntervalVersion", alwaysRun = true)
    public void startSetUp() throws RemoteException {
        eventProcessManager = rmiClient.getEventProcessManager();
        if(eventProcessManager == null) {

            Assert.fail("Cannot run, as eventProcessManager is not set.");
        }
    }
    @Test(groups = "TestMaintainIntervalVersion", description = "b20059:Maintain an internal version number " +
            "for application structure to support....", dependsOnMethods = "startSetUp", alwaysRun = true)
    public void testDeployEP() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testDeployEP", "TestMaintainInternalVersionB20059"));
        try {
            createNamedConfigForPort();
            configManager.persistConfiguration(dataObject, true);
            Thread.sleep(2000);
            deployEventProcess("maintaininginternalversion_b20059-1.0@EnterpriseServer.zip");
            Thread.sleep(1000);
            deployEventProcess("maintaininginternalversion2_b20059-1.0@EnterpriseServer.zip");
            Thread.sleep(1000);
            deployEventProcess("maintaininginternalversion3_b20059-1.0@EnterpriseServer.zip");
            Thread.sleep(1000);
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeployEP", "TestMaintainIntervalVersion"), e);
            Assert.fail("Failed to do SAVE! event process", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeployEP", "TestMaintainIntervalVersion"), e);
            Assert.fail("Failed to do SAVE! event process", e);
        } catch (InterruptedException e){

        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testDeployEP", "TestMaintainInternalVersionB20059"));
    }

    TestEnvironmentConfig testEnvironmentConfig = ESBTestHarness.getTestEnvConfig();
    String firoanoHome = testEnvironmentConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);

    @Test(groups = "TestMaintainIntervalVersion", description = "b20059:Maintain an internal version number " +
            "for application structure to support....", dependsOnMethods = "testDeployEP", alwaysRun = true)
    public void testEPXmlVersion() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testEPXmlVersion", "TestMaintainInternalVersionB20059"));
        try {
            File file = new File(firoanoHome+"/runtimedata/repository/applications/MAINTAININGINTERNALVERSION_B20059/1.0/EventProcess.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = dbFactory.newDocumentBuilder();
            Document doc = documentBuilder.parse(file);
            String version = doc.getElementsByTagName("creation").item(0).getAttributes().getNamedItem("schema-version").getNodeValue();
            if(!version.equals("2.0")) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestMaintainInternalVersionB20059", "TestMaintainIntervalVersion"));
                Assert.fail("Failed as MAINTAININGINTERNALVERSION_B20059 EventProcess.xml schema-version !=2.0");
            }
            file = new File(firoanoHome + "/runtimedata/repository/applications/MAINTAININGINTERNALVERSION2_B20059/1.0/EventProcess.xml" );
            doc = documentBuilder.parse(file);
            version = doc.getElementsByTagName("remote-inst").item(0).getAttributes().getNamedItem("application-schema-version").getNodeValue();
            if(!version.equals("2.0")) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestMaintainInternalVersionB20059", "TestMaintainIntervalVersion"));
                Assert.fail("Failed as MAINTAININGINTERNALVERSION2_B20059 EventProcess.xml application-schema-version != 2.0");
            }
            testDeleteApplication("MAINTAININGINTERNALVERSION3_B20059");
            Thread.sleep(1000);
            testDeleteApplication("MAINTAININGINTERNALVERSION2_B20059");
            Thread.sleep(1000);
            testDeleteApplication("MAINTAININGINTERNALVERSION_B20059");
            configManager.deleteConfiguration(pcNameObject);
        }catch (Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestMaintainInternalVersionB20059", "TestMaintainIntervalVersion"), e);
            Assert.fail("Failed find the specified path!!!", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testEPXmlVersion", "TestMaintainInternalVersionB20059"));

    }

    private IConfigurationRepositoryListener configRepoListener;
    private IConfigurationManager configManager;
    public PortConfigurationDataObject dataObject = new PortConfigurationDataObject();
    //NamedObject nameObject ;
    public static PortConfigurationNamedObject pcNameObject;


    @Test(enabled = false)
    public  void createNamedConfigForPort() {
        configManager = rmiClient.getConfigurationManager();
        try {
            configRepoListener = new ConfigurationListener();
            configManager.addConfigurationRepositoryListener(configRepoListener);
            dataObject.setDestinationType(DestinationType.TOPIC);
            dataObject.setObjectCategory(ObjectCategory.PORT_CONFIGURATION);
            dataObject.setPortType(PortType.OUTPUT);
            dataObject.setDataType(DataType.TEXT);
            dataObject.setName("TestPortNamedConfig");
            dataObject.setLabel(Label.TESTING);
            String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<publisher>\n" +
                    "  <messages persistent=\"true\"/>\n" +
                    "</publisher>";
            dataObject.setData(data.getBytes());
            //create instance of PortConfigNamedObject for deletion of named config
            pcNameObject = new PortConfigurationNamedObject(dataObject.getName(), dataObject.getLabel(), dataObject.getObjectCategory(), dataObject.getDestinationType(), dataObject.getPortType());

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createNamedConfigForPort", "TestMaintainInternalVersionB20059"), e);
            Assert.fail("failed to create port Named configuration", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createNamedConfigForPort", "TestMaintainInternalVersionB20059"), e);
            Assert.fail("failed to create port Named configuration", e);
        }
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
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

    @Test(enabled = false)
    public void testDeleteApplication(String appGUID) {
        try {
            eventProcessManager.deleteEventProcess(appGUID, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestMaintainInternalVersionB20059"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestMaintainInternalVersionB20059"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testDeleteApplication", "TestMaintainInternalVersionB20059"));
    }

    private class ConfigurationListener extends UnicastRemoteObject implements IConfigurationRepositoryListener {
        private ConfigurationListener() throws RemoteException {
        }

        public void configurationPersisted(NamedObject namedObject) throws RemoteException {
            Assert.assertEquals(namedObject, dataObject);
        }

        public void configurationDeleted(NamedObject namedObject) throws RemoteException {
            Assert.assertEquals(namedObject, pcNameObject);
        }
    }
}
