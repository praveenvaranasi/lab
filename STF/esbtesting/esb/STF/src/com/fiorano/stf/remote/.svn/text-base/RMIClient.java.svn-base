/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.stf.remote;

import com.fiorano.esb.server.api.*;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import org.testng.annotations.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * @author lokesh
 */
public class RMIClient implements RemoteClient {

    private static RMIClient rmiClient;

    private IRmiManager rmiManager;
    private IEventProcessManager eventProcessManager;
    private IServiceManager serviceManager;
    private IFPSManager fpsManager;
    private IServiceProviderManager serviceProviderManager;
    private IDebugger debugger;
    private IConfigurationManager configurationManager;
    private IUserSecurityManager userSecurityManager;

    private String userName = "admin";
    private String password = "passwd";

    private String handleID;

    public static RMIClient getInstance() {
        if (rmiClient == null) {
            rmiClient = new RMIClient();
        }
        return rmiClient;
    }

    public static RMIClient getInstance(String username, String password) {
        if (rmiClient == null) {
            rmiClient = new RMIClient(username, password);
        }
        return rmiClient;
    }

    public String getHandleID() {
        return handleID;
    }

    private RMIClient() {
        try {
            ArrayList urlDetails = getActiveFESUrl();
            Registry registry = LocateRegistry.getRegistry((String) urlDetails.get(0), (Integer) urlDetails.get(1));
            try {
                rmiManager = (IRmiManager) registry.lookup(IRmiManager.class.toString());
            }
            catch (java.rmi.ConnectException e) {
                e.printStackTrace();
            }

            handleID = rmiManager.login(userName, password);

            eventProcessManager = rmiManager.getEventProcessManager(handleID);
            serviceManager = rmiManager.getServiceManager(handleID);
            fpsManager = rmiManager.getFPSManager(handleID);
            debugger = rmiManager.getBreakpointManager(handleID);
            configurationManager = rmiManager.getConfigurationManager(handleID);
            serviceProviderManager = rmiManager.getServiceProviderManager(handleID);
            userSecurityManager = rmiManager.getUserSecurityManager(handleID);

        } catch (ServiceException e3) {
            e3.printStackTrace();
        } catch (RemoteException e4) {
            e4.printStackTrace();
        } catch (NotBoundException e4) {
            e4.printStackTrace();
        } catch (STFException e4) {
            e4.printStackTrace();
        }
    }

    private RMIClient(String username, String passwd) {
        try {
            ArrayList urlDetails = getActiveFESUrl();
            Registry registry = LocateRegistry.getRegistry((String) urlDetails.get(0), (Integer) urlDetails.get(1));
            try {
                rmiManager = (IRmiManager) registry.lookup(IRmiManager.class.toString());
            }
            catch (java.rmi.ConnectException e) {
                e.printStackTrace();
            }
            handleID = rmiManager.login(username, passwd);

            eventProcessManager = rmiManager.getEventProcessManager(handleID);
            serviceManager = rmiManager.getServiceManager(handleID);
            fpsManager = rmiManager.getFPSManager(handleID);
            debugger = rmiManager.getBreakpointManager(handleID);
            configurationManager = rmiManager.getConfigurationManager(handleID);
            serviceProviderManager = rmiManager.getServiceProviderManager(handleID);

        } catch (ServiceException e3) {
            e3.printStackTrace();
        } catch (RemoteException e4) {
            e4.printStackTrace();
        } catch (NotBoundException e4) {
            e4.printStackTrace();
        } catch (STFException e4) {
            e4.printStackTrace();
        }
    }

    public static void setClientToNull() {
        rmiClient = null;
    }

    public void shutdownEnterpriseServer() {
        try {
            serviceProviderManager.shutdownServer();
        } catch (Exception e) {
            if (e.getMessage().startsWith("Connection refused to host"))
                System.out.println("The Provider is already down");
            else
                e.printStackTrace();
        }
        setClientToNull();
        RTLClient.setClientToNull();
        JMXClient.setClientToNull();
    }

    public void restartEnterpriseServer() {
        try {
            serviceProviderManager.restartServer();
        } catch (RemoteException e) {
            if (e.getMessage().startsWith("Connection refused to host"))
                System.out.println("The Provider is already down");
            else e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        setClientToNull();
        RTLClient.setClientToNull();
        JMXClient.setClientToNull();
    }

    public void cleanUp() {
        // this.rmiManager gives us the rmimanager of the Object calling this method, it is being compared with rmiClient.getRmiManager which gives us the
        // rmiManager of latest active RMIClient instance. This check ensures that logout needs to be done only if we have latest rmimanager with us or else calling
        // setClienttoNull is enough. For eg - In STFtestsuite's teardown method rmiManager which it maintains would be obsolete once we restart a server & retreive a new RMICLient instance
        // in any of our test-case   &  hence logout need not be done in that case.
        if (this.rmiManager == rmiClient.getRmiManager()) {
            try {
                rmiManager.logout(handleID);
            } catch (RemoteException e) {
                if (e.getMessage().startsWith("Connection refused to host"))
                    System.out.println("The Provider is already down");
                //ignore
                //else e.printStackTrace();
            } catch (Exception e) {
                //e.printStackTrace();  //ignore
            }
        }
        setClientToNull();
    }


    public IEventProcessManager getEventProcessManager() {
        return eventProcessManager;
    }

    public IServiceManager getServiceManager() {
        return serviceManager;
    }

    public IFPSManager getFpsManager() {
        return fpsManager;
    }

    public IServiceProviderManager getServiceProviderManager() {
        return serviceProviderManager;
    }
    public IUserSecurityManager getUserSecurityManager() {
        return userSecurityManager;
    }

    public IDebugger getDebugger() {
        return debugger;
    }

    public IConfigurationManager getConfigurationManager() {
        return configurationManager;
    }

    public IRmiManager getRmiManager() {
        return rmiManager;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * returns a arraylist of size 2.
     * which contains a string(ip of fes) followed by a int(rmi port of fes)
     */
    @Test(enabled = false)
    public static ArrayList getActiveFESUrl() throws STFException {
        ArrayList url = new ArrayList(2);//string followed by int.
        try {
            String s = ServerStatusController.getInstance().getURLOnFESActive();
            String rtlPort = s.substring(s.lastIndexOf(":") + 1);
            url.add(s.substring(s.lastIndexOf("//") + 2, s.lastIndexOf(":")));
            if (rtlPort.equals("1947")) {
                url.add(2047);
            } else
                url.add(2048);
        } catch (STFException e) {
            e.printStackTrace();
        }
        return url;
    }

}