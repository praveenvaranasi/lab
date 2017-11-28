package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 3/28/12
 * Time: 4:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestStopEPfromDashBrd21511 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private SampleEventProcessListener epListener = null;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "FLOW";
    private float appVersion = 1.0f;


    @Test(groups = "bugs",alwaysRun = true)
    public void initNewSetup(){
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.fpsManager = rmiClient.getFpsManager();
    }

    @Test(groups = "bugs",dependsOnMethods = "initNewSetup",alwaysRun=true)
    public void TestNewNone() throws com.fiorano.esb.server.api.ServiceException,RemoteException {
        epListener = new SampleEventProcessListener(appName,eventStore);
        eventProcessManager.addEventProcessListener(epListener);
        try {
            deployEventProcess("FLOW.zip");
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        eventProcessManager.checkResourcesAndConnectivity(appName,appVersion);
        eventProcessManager.startEventProcess(appName,appVersion,false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        ObjectName objName2 = null;
        try {
            objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String handleID = rmiClient.getHandleID();
               Object[] params = {appName, appVersion, handleID};
               String[] signatures = {String.class.getName(), float.class.getName(), String.class.getName()};
        try {
            jmxClient.invoke(objName2, "killApplication", params, signatures);
        } catch (ReflectionException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestNewNone","TestStopEPfromDashBrd"),e);
        } catch (IOException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestNewNone","TestStopEPfromDashBrd"),e);
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestNewNone","TestStopEPfromDashBrd"),e);
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestNewNone","TestStopEPfromDashBrd"),e);
        }

        Logger.Log(Level.INFO,Logger.getFinishMessage("TestNewNone","TestStopEPfromDashBrd"));

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
