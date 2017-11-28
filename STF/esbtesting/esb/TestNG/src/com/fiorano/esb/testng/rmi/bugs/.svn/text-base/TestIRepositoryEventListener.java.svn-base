package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IRepositoryEventListener;
import com.fiorano.esb.testng.rmi.sanity.TestIEventProcessManager;
import com.fiorano.esb.testng.rmi.sanity.TestIServiceManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Feb 23, 2010
 * Time: 1:47:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestIRepositoryEventListener extends UnicastRemoteObject implements IRepositoryEventListener {
    public void serviceDeleted(java.lang.String s, float v) throws java.rmi.RemoteException {
        TestIServiceManager.insertIntoResultMap(TestConstants.SERVICEINSTANCE_DELETE_SUCCESSFUL, s);
    }

    public void serviceDeployed(java.lang.String s, float v) throws java.rmi.RemoteException {
        TestIServiceManager.insertIntoResultMap(TestConstants.SERVICEINSTANCE_DEPLOY_SUCCESSFUL, s);
    }

    public void eventProcessDeleted(java.lang.String s, float v, java.lang.String s1) throws java.rmi.RemoteException {
        TestIEventProcessManager.insertIntoResultMap(TestConstants.EVENTPROCESS_DELETE_SUCCESSFUL, s);
    }

    public void eventProcessDeployed(java.lang.String s, float v, java.lang.String s1) throws java.rmi.RemoteException {
        TestIEventProcessManager.insertIntoResultMap(TestConstants.EVENTPROCESS_DEPLOYED_SUCCESSFUL, s);
    }

    public void resourceDeployed(java.lang.String s, java.lang.String s1, float v) throws java.rmi.RemoteException {
    }

    public void resourceDeleted(java.lang.String s, java.lang.String s1, float v) throws java.rmi.RemoteException {
    }

    public TestIRepositoryEventListener() throws RemoteException {
    }

    public void descriptorModified(java.lang.String s, float v) throws java.rmi.RemoteException {
    }

}
