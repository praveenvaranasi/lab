package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.server.api.IConfigurationManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.*;
import fiorano.tifosi.dmi.configuration.data.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: srikanth
 * Date: Jan 19, 2011
 * Time: 7:21:32 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestaddNewConfiguration extends AbstractTestNG {
    private IConfigurationManager configmanager;
    private ExecutionController executioncontroller;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private TestEnvironmentConfig testenvconfig;
    private IFPSManager fpsManager;

    @Test(groups = "Add_NewConfiguration", alwaysRun = true)
    public void startSetup() {
        this.serverstatus = ServerStatusController.getInstance();
        this.testenv = serverstatus.getTestEnvironment();
        this.executioncontroller = ExecutionController.getInstance();
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        this.configmanager = rmiClient.getConfigurationManager();
        this.fpsManager = rmiClient.getFpsManager();
    }

    @Test(groups = "Add_NewConfiguration", description = "bug 20161 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestAddRouteConfiguration() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestAddRouteConfiguration", "TestaddNewConfiguration"));
        addRouteConfiguration("route_newport0");
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestAddRouteConfiguration", "TestaddNewConfiguration"));
    }

    @Test(groups = "Add_NewConfiguration", description = "bug 20161 ", dependsOnMethods = "TestAddRouteConfiguration", alwaysRun = true)
    public void TestAddPortConfiguration() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestAddPortConfiguration", "TestaddNewConfiguration"));
        addPortConfiguration("port_newport0");
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestAddPortConfiguration", "TestaddNewConfiguration"));
    }

    @Test(groups = "Add_NewConfiguration", description = "bug 20161 ", dependsOnMethods = "TestAddPortConfiguration", alwaysRun = true)
    public void TestKillActiveFES() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestKillActiveFES", "TestaddNewConfiguration"));
        ArrayList arrlist = null;
        String profile = null;
        try {
            arrlist = AbstractTestNG.getActiveFESUrl();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillActiveFES", "TestaddNewConfiguration"), e);
            Assert.fail("Failed to get active FES url", e);
        }
        if (arrlist.get(1).toString().equals("2047"))
            profile = "primary";
        else
            profile = "secondary";
        ServerElement serverElement = testenv.getServer("hafes");
        Map<String, ProfileElement> profileElements = serverElement.getProfileElements();
        String str = new String(profile);
        ProfileElement profileElement = profileElements.get(str);
        String machine = profileElement.getMachineName();
        MachineElement machine1 = testenv.getMachine(machine);
        Boolean isWrapper = profileElement.isWrapper();
        Boolean isHA = serverElement.isHA();
        try {
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(60000);
            executioncontroller.startServerOnRemote(machine1.getAddress(), serverElement.getMode(), "haprofile1/" + profile, isWrapper, isHA);
            Thread.sleep(60000);
            rmiClient = RMIClient.getInstance();
            startSetup();

        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillActiveFES", "TestaddNewConfiguration"), e);
            Assert.fail("Failed to Restart Primary FES", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestKillActiveFES", "TestaddNewConfiguration"));
    }

    @Test(groups = "Add_NewConfiguration", description = "bug 20161 ", dependsOnMethods = "TestKillActiveFES", alwaysRun = true)
    public void TestAddRouteConfiguration1() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestAddRouteConfiguration1", "TestaddNewConfiguration"));
        addRouteConfiguration("route_newport0");
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestAddRouteConfiguration1", "TestaddNewConfiguration"));
    }

    @Test(groups = "Add_NewConfiguration", description = "bug 20161 ", dependsOnMethods = "TestAddRouteConfiguration1", alwaysRun = true)
    public void TestAddPortConfiguration1() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestAddPortConfiguration1", "TestaddNewConfiguration"));
        addPortConfiguration("port_newport0");
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestAddPortConfiguration1", "TestaddNewConfiguration"));
    }

    private void addPortConfiguration(String configName) {
        String configstring = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<subscriber acknowledgement=\"3\" messageSelector=\"shrikanth\" sessions=\"1\">\n" +
                "  <transaction enabled=\"false\"/>\n" +
                "  <subscription durable=\"false\"/>\n" +
                "</subscriber>";
        byte data[] = configstring.getBytes();
        PortConfigurationDataObject portData = new PortConfigurationDataObject(configName, Label.DEVELOPMENT, ObjectCategory.PORT_CONFIGURATION, DataType.TEXT, data, DestinationType.TOPIC, PortType.OUTPUT);

        try {
            configmanager.persistConfiguration(portData, true);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("addPortConfiguration", "TestaddNewConfiguration"), e);
            Assert.fail("Failed to create new port configuration!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("addPortConfiguration", "TestaddNewConfiguration"), e);
            Assert.fail("Failed to create new port configuration!", e);
        }
    }

    private void addRouteConfiguration(String configName) {
        String configstring = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<messages compress=\"true\" durable=\"true\" encrypt=\"true\"/>";
        byte data[] = configstring.getBytes();
        DataObject routeData = new DataObject(configName, Label.DEVELOPMENT, ObjectCategory.ROUTE, DataType.TEXT, data);
        try {
            configmanager.persistConfiguration(routeData, true);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("addRouteConfiguration", "TestaddNewConfiguration"), e);
            Assert.fail("Failed to create new route configuration!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("addRouteConfiguration", "TestaddNewConfiguration"), e);
            Assert.fail("Failed to create new route configuration!", e);
        }
    }
}
