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
 * User: sudharshan
 * Date: 9/19/11
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestNamedConfig20741 extends AbstractTestNG{
    private IConfigurationRepositoryListener configRepoListener;
    private IConfigurationManager configManager;
    ResourceConfigurationDataObject resourceConfigurationDataObject = new ResourceConfigurationDataObject();
    ResourceConfigurationDataObject resourceConfigurationDataObject1 = new ResourceConfigurationDataObject();
    ResourceConfigurationNamedObject resourceConfigurationNamedObject;
    ResourceConfigurationNamedObject resourceConfigurationNamedObject1;

    /*
    * bug20741:Can't save NamedConfig of type Resource
    * */
    @Test(groups = "TestNamedConfig20741", alwaysRun = true)
    public void startSetUp() throws RemoteException {

    }

    @Test(groups = "TestNamedConfig20283", dependsOnMethods = "startSetUp", alwaysRun = true)
    public void createNamedConfigKeyStoreForResource() {
        configManager = rmiClient.getConfigurationManager();
        try {
            configRepoListener = new ConfigurationListener();
            configManager.addConfigurationRepositoryListener(configRepoListener);
            resourceConfigurationDataObject.setObjectCategory(ObjectCategory.RESOURCE_CONFIGURATION);
            resourceConfigurationDataObject.setResourceType("com.fiorano.services.common.security.AuthenticationConfiguration");
            resourceConfigurationDataObject.setName("keystore");
            resourceConfigurationDataObject.setDataType(DataType.TEXT);
            resourceConfigurationDataObject.setLabel(Label.TESTING);
            String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns1:namedConfiguration ns1:CLASS_NAME=\"com.fiorano.services.common.security.AuthenticationConfiguration\" xmlns:ns1=\"uri:com.fiorano.services.common.security\">\n" +
                    "    <ns1:authenticationRequired>false</ns1:authenticationRequired>\n" +
                    "    <ns1:username></ns1:username>\n" +
                    "    <ns1:password></ns1:password>\n" +
                    "</ns1:namedConfiguration>";
            resourceConfigurationDataObject.setData(data.getBytes());
            resourceConfigurationNamedObject = new ResourceConfigurationNamedObject(resourceConfigurationDataObject.getName(), resourceConfigurationDataObject.getLabel(),
                    resourceConfigurationDataObject.getObjectCategory(), resourceConfigurationDataObject.getResourceType());
            configManager.persistConfiguration(resourceConfigurationDataObject, true);
            Thread.sleep(2000);
        }catch (RemoteException e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createNamedConfigKeyStoreForResource", "TestNamedConfig20741"), e);
            Assert.fail("failed to create keystore Resource Named configuration ", e);
        }catch (Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createNamedConfigKeyStoreForResource", "TestNamedConfig20741"), e);
            Assert.fail("failed to create keystore Resource Named configuration ", e);
        }
    }

    @Test(groups = "TestNamedConfig20283", dependsOnMethods = "createNamedConfigKeyStoreForResource", alwaysRun = true)
    public void createNamedConfigHelloForResource() {
        try {
            configRepoListener = new ConfigurationListener();
            configManager.addConfigurationRepositoryListener(configRepoListener);
            resourceConfigurationDataObject1.setObjectCategory(ObjectCategory.RESOURCE_CONFIGURATION);
            resourceConfigurationDataObject1.setResourceType("com.fiorano.services.common.security.AuthenticationConfiguration");
            resourceConfigurationDataObject1.setName("Hello");
            resourceConfigurationDataObject1.setDataType(DataType.TEXT);
            resourceConfigurationDataObject1.setLabel(Label.TESTING);
            String data = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<ns1:namedConfiguration ns1:CLASS_NAME=\"com.fiorano.services.common.security.AuthenticationConfiguration\" xmlns:ns1=\"uri:com.fiorano.services.common.security\">\n" +
                    "    <ns1:authenticationRequired>false</ns1:authenticationRequired>\n" +
                    "    <ns1:username></ns1:username>\n" +
                    "    <ns1:password></ns1:password>\n" +
                    "</ns1:namedConfiguration>";
            resourceConfigurationDataObject1.setData(data.getBytes());
            resourceConfigurationNamedObject1 = new ResourceConfigurationNamedObject(resourceConfigurationDataObject1.getName(), resourceConfigurationDataObject1.getLabel(),
                resourceConfigurationDataObject1.getObjectCategory(), resourceConfigurationDataObject1.getResourceType());
            configManager.persistConfiguration(resourceConfigurationDataObject1, true);
            Thread.sleep(2000);
        }catch (RemoteException e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createNamedConfigHelloForResource", "TestNamedConfig20741"), e);
            Assert.fail("failed to create Hello Resource Named configuration ", e);
        }catch (Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createNamedConfigHelloForResource", "TestNamedConfig20741"), e);
            Assert.fail("failed to create Hello Resource Named configuration ", e);
        }
    }

    @Test(groups = "TestNamedConfig20283", dependsOnMethods = "createNamedConfigHelloForResource", alwaysRun = true)
    public void cleanUpNamedConfigsCreated() {
         try {
             configManager.deleteConfiguration(resourceConfigurationNamedObject);
         } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("cleanUpNamedConfigsCreated", "TestNamedConfig20741"), e);
            Assert.fail("failed to cleanup keystore Resource Named configuration ", e);
         } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("cleanUpNamedConfigsCreated", "TestNamedConfig20741"), e);
            Assert.fail("failed to cleanup keystore Resource Named configuration ", e);
         }

         try {
             configManager.deleteConfiguration(resourceConfigurationNamedObject1);
             configManager.removeConfigurationRepositoryListener();
         } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("cleanUpNamedConfigsCreated", "TestNamedConfig20741"), e);
            Assert.fail("failed to cleanup Hello Resource Named configuration ", e);
         } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("cleanUpNamedConfigsCreated", "TestNamedConfig20741"), e);
            Assert.fail("failed to cleanup Hello Resource Named configuration ", e);
         }

     }

    private class ConfigurationListener extends UnicastRemoteObject implements IConfigurationRepositoryListener {
        private ConfigurationListener() throws RemoteException {
        }

        public void configurationPersisted(NamedObject namedObject) throws RemoteException {
            Assert.assertEquals(namedObject, resourceConfigurationDataObject);
        }

        public void configurationDeleted(NamedObject namedObject) throws RemoteException {
            Assert.assertEquals(namedObject, resourceConfigurationNamedObject);
        }
    }
}
