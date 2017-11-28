package com.fiorano.esb.testng.rmi.bugs;

/**
 * Created with IntelliJ IDEA.
 * User: nibhrit
 * Date: 11/19/12
 * Time: 7:20 PM
 * To change this template use File | Settings | File Templates.
 */

import com.fiorano.esb.server.api.FESPerformanceStats;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.server.api.SystemInfoReference;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.management.ObjectName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.logging.Level;

public class TestFESRestartAfterSavedConfigs_22769 extends AbstractTestNG {

    private IServiceProviderManager serviceProviderManager;
    private JMXClient jmxclient;

    @Test(groups = "Bugs", alwaysRun = true, description = "Initialise testing of bug 22769")
    public void initTestSetup() throws ParserConfigurationException, IOException, SAXException {

        jmxclient = JMXClient.getInstance();
        this.serviceProviderManager = rmiClient.getServiceProviderManager();
        if (serviceProviderManager == null) {
            Assert.fail("Cannot run group 'Bugs' as serviceProviderManager is not set.");
        }

    }

    @Test(groups = "Bugs", description = "Save Configurations", dependsOnMethods = "initTestSetup", alwaysRun = true)
    public void saveConfigs() {

        try{
            ObjectName objName = new ObjectName("Container.UserComponents:Name=ConfigurationProvider,ServiceType=ConfigProvider");
            jmxclient.invoke(objName, "save", new Object[]{}, new String[]{});
        }
        catch(Exception e){
            //
        }

    }

    @Test(groups = "Bugs", description = "Restart FES Server", dependsOnMethods = "saveConfigs", alwaysRun = true)
    public void restartServer() {

        try {
            rmiClient.restartEnterpriseServer();
            Thread.sleep(50000);
            rmiClient = RMIClient.getInstance();
        } catch (InterruptedException e) {
            //ignore
        }

    }

}