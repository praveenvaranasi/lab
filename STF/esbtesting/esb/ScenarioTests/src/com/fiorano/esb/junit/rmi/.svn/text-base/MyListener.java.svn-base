package com.fiorano.esb.junit.rmi;

import com.fiorano.esb.server.api.IEventProcessManagerListener;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.io.Serializable;
import java.io.File;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by IntelliJ IDEA.
 * User: lokesh2
 * Date: Jan 28, 2011
 * Time: 11:41:12 AM
 * To change this template use File | Settings | File Templates.
 */
class MyListener extends UnicastRemoteObject implements IEventProcessManagerListener, Serializable {

    File file;
    BufferedWriter bwriter;

    public MyListener() throws RemoteException {
        super();
        file = new File(EventProcessManagerTest.listenerFilePath);
        if (file.exists()) file.delete();

    }

    public void eventProcessDeployed(float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void eventProcessDeleted(float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void serviceInstanceStarted(String s, String s1, String s2, float v, String s3) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void serviceInstanceStopped(String s, String s1, String s2, float v, String s3) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void routeBreakPointAdded(String s, float v, String s1) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void routeBreakPointRemoved(String s, float v, String s1) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void eventProcessStarted(String s, float v) throws RemoteException {
        try {
            file.createNewFile();
            bwriter = new BufferedWriter(new FileWriter(file));
            bwriter.write("ProcessStarted");
            bwriter.close();
        }
        catch (Exception e) {
        }
    }

    public void eventProcessStarting(String s, float v) throws RemoteException {
    }

    public void eventProcessStopped(String s, float v) throws RemoteException {
    }


    public void serviceInstanceStarting(String s, String s1, String s2, float v, String s3) throws java.rmi.RemoteException {

    }

    public void eventProcessSynchronized(String s, float v) throws RemoteException{

    }
}
