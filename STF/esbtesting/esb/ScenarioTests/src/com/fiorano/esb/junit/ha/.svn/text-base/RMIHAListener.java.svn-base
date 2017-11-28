package com.fiorano.esb.junit.ha;

import com.fiorano.esb.server.api.IEventProcessManagerListener;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: nikhil
 * Date: Nov 10, 2009
 * Time: 11:37:56 AM
 * To change this template use File | Settings | File Templates.
 */

class RMIHAListener extends UnicastRemoteObject implements IEventProcessManagerListener, Serializable {

    protected static ArrayList<String> launchedComponents;

    public RMIHAListener(ArrayList<String> launchedComponents) throws RemoteException {
        super();
        this.launchedComponents = launchedComponents;
    }


    public void serviceInstanceStarted(String s, String s1, String serviceInstanceName, float serviceVersion, String fpsName) {

        launchedComponents.add(serviceInstanceName);

    }

    public void routeBreakPointRemoved(String s, float v, String s1) throws java.rmi.RemoteException {
    }

    public void eventProcessStarted(String s, float v) throws java.rmi.RemoteException {
    }

    public void eventProcessStarting(String s, float v) throws java.rmi.RemoteException {
    }

    public void eventProcessStopped(String s, float v) throws java.rmi.RemoteException {
    }

    public void eventProcessDeleted(float v) throws java.rmi.RemoteException {
    }

    public void serviceInstanceStopped(String s, String s1, String serviceInstanceName, float serviceversion, String fpsName) {
        launchedComponents.remove(serviceInstanceName);
    }

    public void eventProcessDeployed(float v) throws java.rmi.RemoteException {
    }


    public void routeBreakPointAdded(String s, float v, String s1) throws java.rmi.RemoteException {
    }

    public void serviceInstanceStarting(String s, String s1, String s2, float v, String s3) throws java.rmi.RemoteException {

    }

    public void eventProcessSynchronized(String s, float v) throws RemoteException{

    }
}
