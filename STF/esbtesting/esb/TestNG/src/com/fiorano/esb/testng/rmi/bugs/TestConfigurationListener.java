package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IConfigurationManager;
import com.fiorano.esb.server.api.IConfigurationRepositoryListener;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import fiorano.tifosi.dmi.configuration.data.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Feb 1, 2011
 * Time: 3:33:29 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestConfigurationListener extends AbstractTestNG {

    private IConfigurationRepositoryListener configRepoListener;
    private IConfigurationManager configManager;
    PortConfigurationDataObject dataObject = new PortConfigurationDataObject();
    PortConfigurationNamedObject pcNameObject;

    @Test(groups = "ConfigurationListener", description = "Listener for  named configuration changes in Server:Bug-20217", alwaysRun = true)
    public void testListener() throws RemoteException {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testListener", "TestConfigurationListener"));
            configManager = rmiClient.getConfigurationManager();
            configRepoListener = new ConfigurationListener();
            configManager.addConfigurationRepositoryListener(configRepoListener);
            dataObject.setDestinationType(DestinationType.QUEUE);
            dataObject.setObjectCategory(ObjectCategory.PORT_CONFIGURATION);
            dataObject.setPortType(PortType.INPUT);
            dataObject.setDataType(DataType.TEXT);
            dataObject.setName("new_config");
            dataObject.setLabel(Label.DEVELOPMENT);
            String data = "<sample/>";
            dataObject.setData(data.getBytes());
            configManager.persistConfiguration(dataObject, true);
            Thread.sleep(5000);
            pcNameObject = new PortConfigurationNamedObject(dataObject.getName(), dataObject.getLabel(), dataObject.getObjectCategory(), dataObject.getDestinationType(), dataObject.getPortType());
            configManager.deleteConfiguration(pcNameObject);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testListener", "TestConfigurationListener"));
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testListener", "TestConfigurationListener"), e);
            Assert.fail("Failed in testListener method ", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testListener", "TestConfigurationListener"), e);
            Assert.fail("Failed in testListener method ", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testListener", "TestConfigurationListener"), e);
            Assert.fail("Failed in testListener method ", e);
        }
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
