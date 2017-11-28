package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.server.api.IEventProcessManagerListener;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/11/11
 * Time: 12:27 PM
 * To change this template use File | Settings | File Templates.
 */
//public class RMIHAListener extends AbstractTestNG{

class RMIHAListener extends UnicastRemoteObject implements IEventProcessManagerListener, Serializable {

    protected static ArrayList<String> launchedComponents;

    public RMIHAListener(ArrayList<String> launchedComponents) throws RemoteException {
        super();
        this.launchedComponents = launchedComponents;
    }


    public void serviceInstanceStarted(String s, String s1, String serviceInstanceName, float v, String s3) {

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

    public void serviceInstanceStopped(String s, String s1, String serviceInstanceName, float v, String s3) {
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
