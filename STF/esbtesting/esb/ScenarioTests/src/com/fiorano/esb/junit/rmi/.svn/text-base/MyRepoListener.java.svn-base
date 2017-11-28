package com.fiorano.esb.junit.rmi;

import com.fiorano.esb.server.api.IRepositoryEventListener;

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
class MyRepoListener extends UnicastRemoteObject implements IRepositoryEventListener, Serializable {
    File file;
    BufferedWriter bwriter;

    public MyRepoListener() throws RemoteException {
        super();
        file = new File(EventProcessManagerTest.listenerFilePath);
        if (file.exists()) file.delete();
    }

    public void serviceDeleted(String s, float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void serviceDeployed(String s, float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void eventProcessDeleted(String s, float v, String s1) throws RemoteException {
        try {
            file.createNewFile();
            bwriter = new BufferedWriter(new FileWriter(file));
            bwriter.write("ProcessDeleted");
            bwriter.close();
        }
        catch (Exception e) {
        }

    }

    public void eventProcessDeployed(String s, float v, String s1) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void resourceDeployed(String s, String s1, float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void resourceDeleted(String s, String s1, float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void descriptorModified(String s, float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
