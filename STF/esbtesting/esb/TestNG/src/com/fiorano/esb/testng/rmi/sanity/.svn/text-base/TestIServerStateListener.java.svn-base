package com.fiorano.esb.testng.rmi.sanity;

import com.fiorano.esb.server.api.IServerStateListener;
import com.fiorano.esb.testng.rmi.bugs.TestConstants;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Feb 23, 2010
 * Time: 3:53:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestIServerStateListener extends UnicastRemoteObject implements IServerStateListener {

    public void peerUnavailable(java.lang.String s) throws java.rmi.RemoteException {
        TestIFPSManager.insertintoResultMap(TestConstants.PEER_STATUS_UNAVALIABILITY, "UNAVAILABLE");
    }

    public void peerAvailable(java.lang.String s) throws java.rmi.RemoteException {
        TestIFPSManager.insertintoResultMap(TestConstants.PEER_STATUS_AVALIABILITY, "AVAILABLE");
    }

    public TestIServerStateListener() throws RemoteException {
    }
}
