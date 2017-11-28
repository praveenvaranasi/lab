package com.fiorano.esb.testng.rmi;

import com.fiorano.esb.server.api.IEventProcessManagerListener;
import com.fiorano.esb.testng.rmi.bugs.TestConstants;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: Nov 18, 2010
 * Time: 4:45:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class SampleEventProcessListener extends UnicastRemoteObject implements IEventProcessManagerListener {

    private String appName;
    private Hashtable eventStore;

    public SampleEventProcessListener(String appName, Hashtable eventStore) throws RemoteException {
        super();
        this.appName = appName;
        this.eventStore = eventStore;
    }

    public void eventProcessDeleted(float v) throws RemoteException {

    }

    public void eventProcessDeployed(float v) throws RemoteException {

    }

    public void serviceInstanceStarted(String s, String s1, String s2, float v, String s3) throws RemoteException {
        if (eventStore.containsKey(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName)) {
            int count = Integer.parseInt(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName).toString());
            eventStore.put(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName, String.valueOf(++count));
        } else {
            eventStore.put(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName, "1");
        }
    }

    public void serviceInstanceStopped(String s, String s1, String s2, float v, String s3) throws RemoteException {
        if (eventStore.containsKey(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName)) {
            int count = Integer.parseInt(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName).toString());
            eventStore.put(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName, String.valueOf(--count));
        } else {
            eventStore.put(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName, "0");
        }
    }

    public void routeBreakPointAdded(String s, float v, String s1) throws RemoteException {

    }

    public void routeBreakPointRemoved(String s, float v, String s1) throws RemoteException {

    }

    public void eventProcessStarted(String s, float v) throws RemoteException {
        eventStore.put(TestConstants.EVENTPROCESS_LAUNCH_SUCCESSFUL, appName);
    }

    public void eventProcessStarting(String s, float v) throws RemoteException {

    }

    public void eventProcessStopped(String s,float v) throws RemoteException {
        eventStore.put(TestConstants.EVENTPROCESS_STOP_SUCCESSFUL, appName);
    }

    public void serviceInstanceStarting(String s, String s1, String s2, float v, String s3) throws java.rmi.RemoteException {

    }

    public void eventProcessSynchronized(String s, float v) throws RemoteException{

    }
}
