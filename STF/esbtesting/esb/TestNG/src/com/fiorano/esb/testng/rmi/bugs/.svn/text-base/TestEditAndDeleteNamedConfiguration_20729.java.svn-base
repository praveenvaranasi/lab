package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IConfigurationManager;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.configuration.data.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: janardhan
 * Date: 9/22/11
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestEditAndDeleteNamedConfiguration_20729 extends AbstractTestNG {

    private IConfigurationManager configManager;

    TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
    String fioranohome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);

    //NamedObject nameObject ;
    PortConfigurationNamedObject pcNameObject;
    PortConfigurationDataObject dataObject;

    @Test(groups = "EditAndDeleteNamedConfiguration_20729", alwaysRun = true)
    public void startSetUp() throws RemoteException {
        configManager = rmiClient.getConfigurationManager();
    }

    @Test(groups = "EditAndDeleteNamedConfiguration_20729", dependsOnMethods = "startSetUp", alwaysRun = true)
    public void testCreateNamedConfigForPort() {

        try {
            dataObject = new PortConfigurationDataObject();
            dataObject.setDestinationType(DestinationType.TOPIC);
            dataObject.setObjectCategory(ObjectCategory.PORT_CONFIGURATION);
            dataObject.setPortType(PortType.OUTPUT);
            dataObject.setDataType(DataType.TEXT);
            dataObject.setName("NamedConfiguration1");
            dataObject.setLabel(Label.TESTING);
            String data = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<publisher>\n" +
                    "  <messages persistent=\"true\"/>\n" +
                    "</publisher>";
            dataObject.setData(data.getBytes());
            //create instance of PortConfigNamedObject for deletion of named config
            pcNameObject = new PortConfigurationNamedObject(dataObject.getName(), dataObject.getLabel(), dataObject.getObjectCategory(), dataObject.getDestinationType(), dataObject.getPortType());
            configManager.persistConfiguration(dataObject, true);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createNamedConfigForPort", "TestEditAndDeleteNamedConfiguration_20729"), e);
            Assert.fail("failed to create port Named configuration", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("createNamedConfigForPort", "TestEditAndDeleteNamedConfiguration_20729"));

    }

    @Test(groups = "EditAndDeleteNamedConfiguration_20729", dependsOnMethods = "testCreateNamedConfigForPort", alwaysRun = true)
    public void testEditNamedConfigForPort() {
            ArrayList list = new ArrayList();
        try {
            list = configManager.getConfigurations(pcNameObject, true);
            if(list.isEmpty())
                throw new Exception("list is empty");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEditNamedConfigForPort", "TestEditAndDeleteNamedConfiguration_20729"), e);
            Assert.fail("failed to edit port Named configuration", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEditNamedConfigForPort", "TestEditAndDeleteNamedConfiguration_20729"), e);
            Assert.fail("failed to edit port Named configuration", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEditNamedConfigForPort", "TestEditAndDeleteNamedConfiguration_20729"), e);
            Assert.fail("failed to edit port Named configuration", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testEditNamedConfigForPort", "TestEditAndDeleteNamedConfiguration_20729"));

    }

    @Test(groups = "EditAndDeleteNamedConfiguration_20729", dependsOnMethods = "testEditNamedConfigForPort", alwaysRun = true)
    public void testDeleteNamedConfigForPort() {

        try {
            configManager.deleteConfiguration(pcNameObject);
            String fileSep = System.getProperty("file.separator");
            String namedConfigPathFile = fioranohome + "/runtimedata" + fileSep + "repository" + fileSep + "configurations"
                    + fileSep + "ports" + fileSep + "output" + fileSep + "NamedConfiguration1";
            File f1 = new File(namedConfigPathFile);
            if (f1.exists())
                throw new Exception("the named configuration file exists after deletion");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteNamedConfigForPort", "TestEditAndDeleteNamedConfiguration_20729"), e);
            Assert.fail("failed to delete port Named configuration", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteNamedConfigForPort", "TestNaTestEditAndDeleteNamedConfiguration_20729medConfig20283"), e);
            Assert.fail("failed to delete port Named configuration", e);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteNamedConfigForPort", "TestEditAndDeleteNamedConfiguration_20729"), e);
            Assert.fail("failed to delete port Named configuration", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testDeleteNamedConfigForPort", "TestEditAndDeleteNamedConfiguration_20729"));

    }
}
