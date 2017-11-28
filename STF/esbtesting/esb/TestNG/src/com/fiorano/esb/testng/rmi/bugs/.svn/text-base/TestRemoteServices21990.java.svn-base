package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 8/13/12
 * Time: 12:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestRemoteServices21990 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private SampleEventProcessListener epListener;
    private Hashtable<String,String> eventStore = new Hashtable<String, String>();
    private MBeanServerConnection mbsc;
    private String handleID;
    private String appName = "FEDDISP21990";
    private String appName1 = "FEDDISP21990_1";
    private HashMap Map;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs",alwaysRun = true)
    public void initNewSetup(){
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.fpsManager = rmiClient.getFpsManager();
        this.handleID = jmxClient.getHandleId();
    }

    @Test(groups = "Bugs",description = "resouce search in aplication view",dependsOnMethods = "initNewSetup",alwaysRun = true)
    public void TestResourceSearch() throws IOException, IntrospectionException, InstanceNotFoundException, ReflectionException {
        try {
            epListener = new SampleEventProcessListener(appName,eventStore);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceSearch", "TestRemoteServices21990"),e);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestResourceSearch","TestRemoteServices21990"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceSearch", "TestRemoteServices21990"),e);
        }

        try {
            deployEventProcess("FEDDISP21990.zip");
            deployEventProcess("FEDDISP21990_1.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceSearch", "TestRemoteServices21990"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceSearch", "TestRemoteServices21990"),e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1,appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceSearch", "TestRemoteServices21990"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceSearch", "TestRemoteServices21990"),e);
        }

        jmxClient = JMXClient.getInstance("admin","passwd",2047);
        this.mbsc = JMXClient.getMBeanServerConnection();
        ObjectName name1 = null;
        Object[] params = {appName1,appVersion, handleID};
        String[] signatures = {String.class.getName(),"float",String.class.getName()};
        try {
            name1 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
        } catch (MalformedObjectNameException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceSearch", "TestRemoteServices21990"),e);
        }
        int z =  mbsc.getMBeanCount();
        String[] domains = mbsc.getDomains();
        MBeanInfo mBeanInfo = mbsc.getMBeanInfo(name1);
        try {

            this.Map = (HashMap) mbsc.invoke(name1,"getResourcePropertiesForApp",params,signatures);
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceSearch", "TestRemoteServices21990"),e);
        }

        if(Map.containsKey("FEDDISP21990.Feeder11___Feeder___fps")){
            Logger.Log(Level.INFO,Logger.getFinishMessage("TestResourceSearch","TestRemoteServices21990"));
        }
        else{
            Logger.Log(Level.INFO,Logger.getFinishMessage("TestResourceSearch failed","TestRemoteServices21990"));
        }
    }


    @Test(groups = "Bugs",description = "resouce search in aplication view",dependsOnMethods = "TestResourceSearch",alwaysRun = true)
    public void stopAndDeleteEp(){
        stopAndDeleteEP(eventProcessManager, appName1 , appVersion);
        stopAndDeleteEP(eventProcessManager, appName, appVersion);
    }


    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" +
                File.separator + "resources" + File.separator + zipName));
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
}
