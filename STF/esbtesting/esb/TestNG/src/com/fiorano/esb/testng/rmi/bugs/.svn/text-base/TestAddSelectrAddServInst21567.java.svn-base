package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import org.testng.annotations.Test;

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
 * Date: 3/2/12
 * Time: 3:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestAddSelectrAddServInst21567 extends AbstractTestNG{

    private IRmiManager rmiManager;
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "SELECTOR2";
    private String appName1 = "SELECTOR1";
    private SampleEventProcessListener epListener = null;
    private FioranoApplicationController m_fioranoappcontroller;
    private ServerStatusController serverstatus;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initNewSetup() {
        this.serverstatus = ServerStatusController.getInstance();
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
           // Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if (fpsManager == null) {
           // Assert.fail("Cannot run Group Bugs as fpsManager is not set.");
        }

        try {
            this.m_fioranoappcontroller = serverstatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("startSetup", "TestAddSelectrAddServInst"), e);
        }
    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "initNewSetup", alwaysRun = true)
    public void TestNewNone() throws Exception, RemoteException {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestNewNone", "TestAddSelectrAddServInst"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestNewNone", "TestAddSelectrAddServInst"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestAddSelectrAddServInst"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestAddSelectrAddServInst"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestAddSelectrAddServInst"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("SELECTOR1.zip");
            deployEventProcess("SELECTOR2.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestAddSelectrAddServInst"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestAddSelectrAddServInst"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }


        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestAddSelectrAddServInst"), e);
            //Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestAddSelectrAddServInst"), e);
           // Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestAddSelectrAddServInst"), e);
           // Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestNewNone", "TestAddSelectrAddServInst"), e);
           // Assert.fail("Failed to do start application!", e);
        }

        try{
            Thread.sleep(5000);

        }catch (InterruptedException e){

        }
    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "TestNewNone", alwaysRun = true)
    public void TestSave(){

        Application application = null;
        try {
            application = m_fioranoappcontroller.getApplication(appName1, appVersion);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testsave", "TestAddSelectrAddServInst"), e);
        }
        try {
            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testsave", "TestAddSelectrAddServInst"), e);
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            eventProcessManager.deleteEventProcess(appName,appVersion, true, true);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            eventProcessManager.deleteEventProcess(appName1,appVersion, true, true);
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestSave", "TestAddSelectrAddServInst"));
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
