package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.server.api.IUserSecurityManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.remote.JMXClient;
import fiorano.tifosi.dmi.security.SecurityDatastoreResetStateDetails;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 8/22/12
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDataStoreResetByNonAdmin22275 extends AbstractTestNG {
    private SecurityDatastoreResetStateDetails resetStateDetails;
    private MBeanServerConnection mbsc;
    private String userName = "san";
    private String password = "passwd";
    private ObjectName name1 = null;
    private IUserSecurityManager userSecurityManager;
    private IRmiManager rmiManager;
    private String handleID;

    @Test(groups = "Bugs",alwaysRun = true)
    public void initNewSetup() throws ServiceException, RemoteException, MalformedObjectNameException {
        this.rmiManager = rmiClient.getRmiManager();
        this.userSecurityManager = rmiManager.getUserSecurityManager(rmiClient.getHandleID());
        this.name1 = new ObjectName("Fiorano.Esb.Security.User:ServiceType=UserSecurityManager,Name=UserSecurityManager");

    }

    @Test(groups = "Bugs",description = "creating New User",dependsOnMethods = "initNewSetup",alwaysRun = true)
    public void TestAddUser(){
        try {
            userSecurityManager.createUser(userName,password);
            Vector<String> vec = userSecurityManager.getUserNames();
            //String p = vec.elementAt(1);
            //System.out.println("hi");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddUser","TestDataStoreResetByNonAdmin22275"),e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddUser", "TestDataStoreResetByNonAdmin22275"),e);
        }

    }

    @Test(groups = "Bugs",description = "reseting Data Store",dependsOnMethods = "TestAddUser",alwaysRun = true)
    public void TestDataStoreResetByNonAdmin() throws InstanceNotFoundException, IOException, ReflectionException, MBeanException, MalformedObjectNameException {
        jmxClient = JMXClient.getInstance(userName, password, 2047);
        this.handleID = jmxClient.getHandleId();
        Object[] params = {handleID};
        String[] signatures = {String.class.getName()};

        this.mbsc = JMXClient.getMBeanServerConnection();
        this.resetStateDetails = (SecurityDatastoreResetStateDetails) mbsc.invoke(name1, "resetSecurityStore", params, signatures);
        String status = resetStateDetails.getComment();
        if(status.equalsIgnoreCase("Enterprise Security Store Reset succeeded.")){
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestDataStoreResetByNonAdmin","TestDataStoreResetByNonAdmin22275"));
        }
        else{
            Logger.Log(Level.INFO,Logger.getFinishMessage("TestDataStoreResetByNonAdmin","TestDataStoreResetByNonAdmin22275"));
        }
       // System.out.println("hi");

    }

}

