package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 8/10/11
 * Time: 12:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestClearOutMQLogs20586 extends AbstractTestNG {
    IServiceProviderManager providerManager ;
    @Test(groups = "ClearOutMqLogs", alwaysRun = true)
    public void startSetup() {
        if (rmiClient == null) {
            Assert.fail("Cannot run Group ClearOutMqLogs. as rmiClient is not set.");
        }
    }

    @Test(groups = "ClearOutMqLogs", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testClearOutMQLogs20586() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfJapaneseApplication", "TestJapanese"));

        try {
            rmiClient.getServiceProviderManager().clearFESMQOutLogs();
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
            Assert.fail("Failed clear fes out logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
            Assert.fail("Failed clear fes out logs!", e);
        }
        char fsc = File.separatorChar;
        String pathUptoEnterpriseRun = System.getProperty("FIORANO_HOME") + fsc +
                "runtimedata" + fsc + "EnterpriseServers" + fsc + "profile1" + fsc + "FES" + fsc + "run" + fsc +
                "logs";
        String mqOutLogFilePath = pathUptoEnterpriseRun + fsc + "mqout.log" ;
        File mqOutLogFile = new File(mqOutLogFilePath);
        if(mqOutLogFile.length() != 0) {
            Assert.fail("TestClearOutMQLogs20586 failed to clear fes mqout.log file");
        }
        //test to clear mqerr.logs
        try {
            rmiClient.getServiceProviderManager().clearFESMQErrLogs();
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
            Assert.fail("Failed clear fes Err logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
            Assert.fail("Failed clear fes Err logs!", e);
        }

        String mqErrLogFilePath = pathUptoEnterpriseRun + fsc + "mqerr.log";
        File mqErrLogFile = new File(mqErrLogFilePath);
        if(mqErrLogFile.length() != 0) {
            Assert.fail("TestClearOutMQLogs20586 failed to clear fes mqerr.log file");
        }

        //test case for clearing peers mqout and mqerr logs
        Vector peersVector = null;
        try {
            peersVector = rmiClient.getFpsManager().getAllPeersInRepository();
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
            Assert.fail("Failed to get allPeersInRepository!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
            Assert.fail("Failed to get allPeersInRepository!", e);
        }
        String peerName = null;
        if(peersVector.size() > 0){
            peerName = (String)peersVector.get(0);
        }

        //test to clear peer errlogs
        try {
            rmiClient.getFpsManager().clearPeerMQErrLogs(peerName);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
            Assert.fail("Failed clear fps Err logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
            Assert.fail("Failed clear fps Err logs!", e);
        }

        if (peerName.equalsIgnoreCase("fps")) {
            String pathUptoPeerRun = System.getProperty("FIORANO_HOME") + fsc + "runtimedata" +
                    fsc + "PeerServers" +fsc + "profile1" + fsc + "FPS" + fsc + "run" + fsc + "logs";
            File peerMqErrLogFile = new File(pathUptoPeerRun + fsc + "mqerr.log");

            if(peerMqErrLogFile.length() != 0) {
                Assert.fail("TestClearOutMQLogs20586 failed to clear peer mqerr.log file");
            }

            //test to clear peer outlogs
            try {
                rmiClient.getFpsManager().clearPeerMQOutLogs(peerName);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
                Assert.fail("Failed clear fps Out logs!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearOutMQLogs20586", "TestClearOutMQLogs20586"), e);
                Assert.fail("Failed clear fps Out logs!", e);
            }

            File peerMqOutLogFile = new File(pathUptoPeerRun + fsc + "mqout.log");
            if(peerMqOutLogFile.length() != 0) {
                Assert.fail("TestClearOutMQLogs20586 failed to clear peer mqout.log file");
            }
        }

    }
}