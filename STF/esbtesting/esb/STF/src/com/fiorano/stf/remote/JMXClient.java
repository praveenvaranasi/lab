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

import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.dmi.LoginResponse;

import javax.management.*;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lokesh
 */
public class JMXClient implements RemoteClient {
    private static JMXClient jmxClient = null;
    private static JMXConnector jmxc;
    private static MBeanServerConnection mbsc = null;

    private static String userName = "admin";
    private static String password = "passwd";
    private static String rmiAddress = "localhost";
    private ObjectName fesServer;


    public static JMXClient getInstance() {
        if (jmxClient == null) {
            jmxClient = new JMXClient();
        }
        return jmxClient;
    }

    public static JMXClient getInstance(String username, String passwd, int port) {
        if (jmxClient == null) {
            jmxClient = new JMXClient(username, passwd, port);
        }
        return jmxClient;
    }

    private JMXClient() {
        try {
            rmiAddress = ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer();
            boolean connected = connect(userName, password, rmiAddress, 2047);
            if (!connected)
                connected = connect(userName, password, rmiAddress, 2048);
            if (!connected) throw new STFException("JMXClient couldn't connect to server");
        } catch (STFException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private JMXClient(String username, String passwd, int port) {
        try {
            rmiAddress = ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer();
            boolean connected = connect(username, passwd, rmiAddress, port);
            if (!connected)
                connected = connect(username, passwd, rmiAddress, port + 1);
            if (!connected) throw new STFException("JMXClient couldn't connect to server");
        } catch (STFException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static void setClientToNull() {
        jmxClient = null;
    }


    public void shutdownEnterpriseServer() {
        invokeOnFESServer("shutdown", new Object[]{}, new String[]{});
        setClientToNull();
        RTLClient.setClientToNull();
        RMIClient.setClientToNull();
    }

    public void restartEnterpriseServer() {
        invokeOnFESServer("restart", new Object[]{}, new String[]{});
        setClientToNull();
        RTLClient.setClientToNull();
        RMIClient.setClientToNull();
    }

    public void cleanUp() {
        if (jmxc == null) return;
        try {
            jmxc.close();
        } catch (IOException e) {
            System.out.println("COULDN'T CLEANUP JMXCLIENT DUE TO :" + e.getMessage());
        }
        setClientToNull();
    }

    public static void clean() {
        if (jmxc == null) return;
        try {
            jmxc.close();
        } catch (IOException e) {
            System.out.println("COULDN'T CLEANUP JMXCLIENT DUE TO :" + e.getMessage());
        }
        setClientToNull();
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


    public Object invoke(ObjectName obj, String operation, Object[] params, String[] signatures) throws ReflectionException, IOException, InstanceNotFoundException, MBeanException {

        return mbsc.invoke(obj, operation, params, signatures);
    }


    public static MBeanServerConnection getMBeanServerConnection() {
        return mbsc;
    }

    public String getHandleId() {
        try {
            Object[] params = {userName, password, false};
            String[] signatures = {String.class.getName(), String.class.getName(), boolean.class.getName()};
            ObjectName name1 = new ObjectName("Fiorano.Esb.Login:ServiceType=ConnectionHandleManager,Name=ESBConnectionHandleManager");
            LoginResponse loginResponse = (LoginResponse) mbsc.invoke(name1, "login", params, signatures);
            String handleID = loginResponse.getHandleID();
            return handleID;
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;

    }

    public String getHandleId(String userName, String password) {
        try {
            Object[] params = {userName, password, false};
            String[] signatures = {String.class.getName(), String.class.getName(), boolean.class.getName()};
            ObjectName name1 = new ObjectName("Fiorano.Esb.Login:ServiceType=ConnectionHandleManager,Name=ESBConnectionHandleManager");
            LoginResponse loginResponse = (LoginResponse) mbsc.invoke(name1, "login", params, signatures);
            String handleID = loginResponse.getHandleID();
            return handleID;
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;

    }


    /**
     * ********************** Auxiliary methods *************************
     */

    private void invokeOnFESServer(String methodName, Object[] objects, String[] strings) {
        try {
            fesServer = new ObjectName("Fiorano.Esb.Server:ServiceType=FESServer,Name=FESServer");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            invoke(fesServer, methodName, objects, strings);
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static boolean connect(String user, String password, String rmiAddress, int rmiPort) {
        // Create an RMI connector client and connect it to the RMI connector server
        Map env = new HashMap();
        env.put("jmx.remote.credentials", new String[]{user, password});
        env.put(Context.PROVIDER_URL, "rmi://" + rmiAddress + ":" + rmiPort);
        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.rmi.registry.RegistryContextFactory");
        env.put(Context.SECURITY_CREDENTIALS, password);

        try {
            JMXServiceURL url = new JMXServiceURL("service:jmx:rmi://" + rmiAddress + ":" + rmiPort + "/jndi/fmq");
            jmxc = JMXConnectorFactory.connect(url, env);
            mbsc = jmxc.getMBeanServerConnection();     // Get an MBeanServerConnection
        }
        catch (IOException ex) {
//            System.out.println("Couldn't connect to server");
            return false;
        }
        return true;
    }


    /**
     * ******************* TO BE REMOVED IN FINAL CLEANUP ****************************************
     */
    // make rmiaddress, user,passwd, main connect method non-static
    public static boolean connect() {
        return connect(userName, password);
    }

    public static boolean connect(String user, String password) {
        return connect(user, password, rmiAddress, 2047);
    }

    public static boolean connect(String rmiAddress, int port) {
        return connect(userName, password, rmiAddress, port);
    }


}
