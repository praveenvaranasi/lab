package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironment;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.rmi.RemoteException;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: saumil
 * Date: Nov 23, 2010
 * Time: 2:00:36 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * @author saumil
 *         1. Assign fpsMgr the object returned by rmiClient.getFpsManager().
 *         2. Use fpsMgr functions to get the peer protocol and peer URL
 *         3. Check if the fps port required to run using SUN_SSL protocol is 2010.
 *         4. Check if the fps is running by using SUN_SSL protocol.
 *         5. If both the above criterions are met then test case is passed.
 */


public class SSLPeerTest extends AbstractTestNG {

    private IFPSManager fpsMgr;
    private String peerUrl;
    private String protocol;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private String peerName = "fps1";

    @Test(groups = {"SSL_Peer"}, alwaysRun = true)
    public void startSetup() {

        serverstatus = ServerStatusController.getInstance();
        testenv = serverstatus.getTestEnvironment();
        testEnvConfig = ESBTestHarness.getTestEnvConfig();
        this.fpsMgr = rmiClient.getFpsManager();
        if (fpsMgr == null) {
            Assert.fail("fpsMgr is not initialized");
        }
    }

    @Test(groups = {"SSL_PEER"}, description = "starts a peer which accepts ssl connection on port 2010", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testStartFPS1() throws RemoteException, ServiceException{
        if (!fpsMgr.isPeerRunning(peerName)) {
            Assert.fail(peerName+ "should be in running state");
        }
    }

    @Test(groups = {"SSL_Peer"}, description = "starts a peer which accepts ssl connection on port 2010", dependsOnMethods = "testStartFPS1", alwaysRun = true)
    public void testSslPeer() {
        try {

            Logger.LogMethodOrder(Logger.getOrderMessage("testSslPeer", "SSLPeerTest"));
            if (!fpsMgr.isPeerRunning(peerName)) {
                Assert.fail("Peer " + peerName + " failed to start up with SUN_SSL on port 2010.It seems the peer is not up at all.");
            }
            peerUrl = fpsMgr.getURLForFPS(peerName);
            protocol = fpsMgr.getConnectProtocolForPeer(peerName);
            int port = Integer.parseInt(peerUrl.substring(peerUrl.lastIndexOf(":") + 1));
            System.out.println("ssl port = " + port);
            //we start the ssl transport on port 2010. for a ssl peer.
            if (port != 2010 || !protocol.equals("SUN_SSL")) {
                Assert.fail("Peer" + peerName + " failed to start up with SUN_SSL on port 2010. Peer started up with protocol:" + protocol + " and on port:" + port);
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSslPeer", "SSLPeerTest"));
        }
        catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSslPeer", "SSLPeerTest"), e);
            Assert.fail("failed since a remote exception occured", e);
        }

    }
}
