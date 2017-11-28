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
 * Date: 8/3/12
 * Time: 11:18 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestRestartPeerStopEP21883 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private SampleEventProcessListener epListener;
    private Hashtable<String,String> eventStore = new Hashtable<String, String>();
    private String appName = "FEDDISP21883";
    private float appVersion = 1.0f;
    @Test(groups = "Bugs",alwaysRun = true)
    public void initNewSetup(){
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.fpsManager = rmiClient.getFpsManager();

    }

    @Test(groups = "Bugs",description = "launching EP & restarting peer",dependsOnMethods = "initNewSetup",alwaysRun = true)
    public void TestRestartPeer(){
        try {
            epListener = new SampleEventProcessListener(appName,eventStore);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestRestartPeer","TestRestartPeerStopEP21883"),e);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestRestartPeer","TestRestartPeerStopEP21883"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestRestartPeerStopEP21883"),e);
        }

        try {
            deployEventProcess("FEDDISP21883.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestRestartPeerStopEP21883"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestRestartPeerStopEP21883"),e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName,appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestRestartPeerStopEP21883"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestRestartPeerStopEP21883"),e);
        }

        try {
            eventProcessManager.startEventProcess(appName,appVersion,false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestRestartPeerStopEP21883"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestRestartPeerStopEP21883"),e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        try {
            fpsManager.restartPeer("fps");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestRestartPeerStopEP21883"));
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartPeer", "TestRestartPeerStopEP21883"));
        }

        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
        }

        stopAndDeleteEP(eventProcessManager , appName, appVersion);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }

        Logger.Log(Level.INFO,Logger.getFinishMessage("TestRestartPeer","TestRestartPeerStopEP21883"));
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
