package com.fiorano.esb.testng.rmi.sanity;

import com.fiorano.esb.server.api.IEventProcessManagerListener;
import com.fiorano.esb.testng.rmi.bugs.TestConstants;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Feb 22, 2010
 * Time: 2:48:38 PM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings(value = {"deprecation"})
public class TestIEventProcessManagerListener extends UnicastRemoteObject implements IEventProcessManagerListener {

    private String appName;

    public TestIEventProcessManagerListener(String appName) throws RemoteException {
        super();
        this.appName = appName;
    }

    public void eventProcessDeleted(float v) throws java.rmi.RemoteException {
    }

    public void eventProcessDeployed(float v) throws java.rmi.RemoteException {
    }

    public void serviceInstanceStarted(String s,String s1, String s2, float v, String s3) throws java.rmi.RemoteException {
        if (TestIEventProcessManager.containsKey(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName)) {
            int count = Integer.parseInt(TestIEventProcessManager.getValue(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName));
            TestIEventProcessManager.insertIntoResultMap(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName, String.valueOf(++count));
        } else {
            TestIEventProcessManager.insertIntoResultMap(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName, "1");
        }

    }

    public void serviceInstanceStopped(String s,String s1, String s2, float v, String s3) throws java.rmi.RemoteException {
        if (TestIEventProcessManager.containsKey(TestConstants.SERVICEINSTANCE_STOP_SUCCESSFUL + "SIMPLECHAT")) {
            int count = Integer.parseInt(TestIEventProcessManager.getValue(TestConstants.SERVICEINSTANCE_STOP_SUCCESSFUL + "SIMPLECHAT"));
            TestIEventProcessManager.insertIntoResultMap(TestConstants.SERVICEINSTANCE_STOP_SUCCESSFUL + "SIMPLECHAT", String.valueOf(++count));
        } else {
            TestIEventProcessManager.insertIntoResultMap(TestConstants.SERVICEINSTANCE_STOP_SUCCESSFUL + "SIMPLECHAT", "1");
        }

    }

    public void routeBreakPointAdded(String s,float v,String s1) throws java.rmi.RemoteException {

        if (TestIDebugger.containsKey(TestConstants.ROUTEBREAKPOINT_ADD_SUCCESSFUL)) {
            int count = Integer.parseInt(TestIDebugger.getValue(TestConstants.ROUTEBREAKPOINT_ADD_SUCCESSFUL));
            TestIDebugger.insertIntoResultMap(TestConstants.ROUTEBREAKPOINT_ADD_SUCCESSFUL, String.valueOf(++count));
        } else {
            TestIDebugger.insertIntoResultMap(TestConstants.ROUTEBREAKPOINT_ADD_SUCCESSFUL, "1");
        }

    }

    public void routeBreakPointRemoved(String s, float v, String s1) throws java.rmi.RemoteException {
        if (TestIDebugger.containsKey(TestConstants.ROUTEBREAKPOINT_REMOVE_SUCCESSFUL)) {
            int count = Integer.parseInt(TestIDebugger.getValue(TestConstants.ROUTEBREAKPOINT_REMOVE_SUCCESSFUL));
            TestIDebugger.insertIntoResultMap(TestConstants.ROUTEBREAKPOINT_REMOVE_SUCCESSFUL, String.valueOf(++count));
        } else {
            TestIDebugger.insertIntoResultMap(TestConstants.ROUTEBREAKPOINT_REMOVE_SUCCESSFUL, "1");
        }
    }

    public void eventProcessStarted(String s, float v) throws java.rmi.RemoteException {
        TestIEventProcessManager.insertIntoResultMap(TestConstants.EVENTPROCESS_LAUNCH_SUCCESSFUL, appName);
    }

    public void eventProcessStarting(String s, float v) throws java.rmi.RemoteException {
    }

    public void eventProcessStopped(String s, float v) throws java.rmi.RemoteException {
        TestIEventProcessManager.insertIntoResultMap(TestConstants.EVENTPROCESS_STOP_SUCCESSFUL, appName);
    }

    public void serviceInstanceStarting(String s, String s1, String s2, float v, String s3) throws java.rmi.RemoteException {

    }

    public void eventProcessSynchronized(String s, float v) throws RemoteException{

    }
}
