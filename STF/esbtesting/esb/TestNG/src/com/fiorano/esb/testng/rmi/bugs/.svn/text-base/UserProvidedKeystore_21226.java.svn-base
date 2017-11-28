package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import junit.framework.Assert;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: ishita
 * Date: 8/16/12
 * Time: 5:52 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserProvidedKeystore_21226 extends AbstractTestNG{

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private  IServiceProviderManager serviceProviderManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "WSSTUB";
    private String appName1 = "EVENT_PROCESS1";
    private SampleEventProcessListener epListener = null;
    private JMXClient esb = null;
    private ObjectName objName1,objName2,objName3;
    private String handleID;
    private float appVersion = 1.0f;


    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if(eventProcessManager == null) {
            Assert.fail("Failed to set eventProcessManager");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if(fpsManager == null) {
            Assert.fail("Failed to set FPSManager");
        }
        this.serviceProviderManager=rmiClient.getServiceProviderManager();
        if (serviceProviderManager == null) {
            // Assert.fail("Cannot run Group Bugs as serviceProviderManager is not set.");
        }

    }
 @Test(groups = "Bugs", description = "Bug - 21226 ", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestBug_21226WS() throws MalformedObjectNameException {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestBug_21226WS", "UserProvidedKeystore_21226"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestBug_21226WS", "UserProvidedKeystore_21226"));
        String req;
        try {
            serviceProviderManager.clearFESErrLogs();
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        }
        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_21226.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();
        System.out.println("Checking for WS with stub config");
        Map<String,Map<String,ArrayList<String>>> maps = null;
        ArrayList<String> operation = new ArrayList();
       try {
            esb = JMXClient.getInstance();
            objName1 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            handleID = esb.getHandleId();
            Object[] args = {appName,appVersion, "WSStub1", false, false, "", "", true, true, true, true, "","", "", "", "", "", "", "", "", "", "", handleID};
            String[] sigs = {String.class.getName(), float.class.getName(),String.class.getName(), "boolean", "boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                    String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                    String.class.getName()};
             maps = (Map<String,Map<String,ArrayList<String>>>) esb.invoke(objName1, "getOps", args, sigs);
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed assign permission create or edit and delete a principal to user1", e);
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
       try {
           esb = JMXClient.getInstance();
           objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
           handleID = esb.getHandleId();
           String serviceName = null;
           String portName = null;
           String operationName = null;

           for (String service : maps.keySet()) {
               serviceName = service;
               break;
           }

           for (String port : maps.get(serviceName).keySet()) {
               portName = port;
               break;
           }
           for (String opName : maps.get(serviceName).get(portName)) {
               operationName = opName;
               break;
           }
           operation.add(0, serviceName);
           operation.add(1, portName);
           operation.add(2, operationName);

           Object[] args2 = {appName, appVersion, "WSStub1", false, false, "", "", operation, true, true, true, true,  "","", "","", "", "", "", "", "", "", "", handleID};
           String[] sigs2 = {String.class.getName(), float.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName(), String.class.getName(), ArrayList.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                   String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                   String.class.getName()};
           req= (String) esb.invoke(objName2, "getReqSOAPMsg", args2, sigs2);
           System.out.println("getReqSOAPMsg executed!");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed assign permission create or edit and delete a principal to user1", e);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
       try {
           // esb = JMXClient.getInstance();
            objName3 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
           // handleID = esb.getHandleId();
            String res;
            //List<String> operation1 = new ArrayList<String>(maps.keySet());
           // ArrayList operation1 = new ArrayList(maps.keySet());
            Object[] args3 = {"req","", appName, appVersion, "WSStub1",false, "", "",operation, true, true, true, true, "","", "","", "","", "", "", "", "", "", handleID};
            String[] sigs3 = {String.class.getName(), String.class.getName(), float.class.getName(), String.class.getName(), String.class.getName(), "boolean", String.class.getName(), String.class.getName(), ArrayList.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                    String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                    String.class.getName()};
            res = (String) esb.invoke(objName3, "getResponseSOAPMsg", args3, sigs3);
            System.out.println("getResponseSOAPMsg executed!");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed assign permission create or edit and delete a principal to user1", e);
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            System.out.println("I am here!");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS","UserProvidedKeystore_21226"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS","UserProvidedKeystore_21226"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        }
        try {
            String logs;
            logs =serviceProviderManager.getLastErrLogs(Integer.MAX_VALUE);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            if(logs.isEmpty())
            {
               // System.out.println("FES error logs are empty!");
            }
            else {
                Assert.fail("FES error log is not empty");
            }
        }  catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226WS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        }
    }

     @Test(groups = "Bugs", description = "Bug - 21226 ", dependsOnMethods = "TestBug_21226WS", alwaysRun = true)
    public void TestDisableStubConfigWS() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"));
        String req;
        try {
            serviceProviderManager.clearFESErrLogs();
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_21226.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
        System.out.println("Checking for WS without enabling stub config");
        eventStore.clear();
        Map<String,Map<String,ArrayList<String>>> maps=null;
        ArrayList operation = new ArrayList();
        try {
            esb = JMXClient.getInstance();
            objName1 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            handleID = esb.getHandleId();
            Object[] args = {appName,"WSStub1", false, false, "", "", true, false, true, true,  "/home/ishita/fiorano.ks",
                    "fiorano","/home/ishita/fiorano.ks", "fiorano", "JKS", "JKS", "SunX509", "SunX509", "com.sun.net.ssl.internal.ssl.Provider", "TLS", "hellos", handleID};
            String[] sigs = {String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                    String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                    String.class.getName()};
            maps = (Map<String,Map<String,ArrayList<String>>>) esb.invoke(objName1, "getOps", args, sigs);
            System.out.println("get ops!!");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      try {
            objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
           String serviceName = null;
           String portName = null;
           String operationName = null;

           for (String service : maps.keySet()) {
               serviceName = service;
               break;
           }

           for (String port : maps.get(serviceName).keySet()) {
               portName = port;
               break;
           }
           for (String opName : maps.get(serviceName).get(portName)) {
               operationName = opName;
               break;
           }
           operation.add(0, serviceName);
           operation.add(1, portName);
           operation.add(2, operationName);

          Object[] args1 = {appName,"WSStub1", false, false,"","", operation, true, false, true, true, "/home/ishita/fiorano.ks",
                  "fiorano","/home/ishita/fiorano.ks", "fiorano","JKS", "JKS", "SunX509", "SunX509", "com.sun.net.ssl.internal.ssl.Provider", "TLS", "hellos", handleID};
          String[] sigs1 = {String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName(), String.class.getName(), ArrayList.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                  String.class.getName()};
            req = (String) esb.invoke(objName2, "getReqSOAPmsg",args1,sigs1);
           System.out.println("getReqSOAPmsg executed!");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
      try {
            // esb = JMXClient.getInstance();
            objName3 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            // handleID = esb.getHandleId();
          Object[] args3 = {"req","", appName,"WSStub1",false, "", "",operation, true, false,true,true, "/home/ishita/fiorano.ks","fiorano.ks",
                  "/home/ishita/fiorano.ks", "fiorano","JKS", "JKS", "SunX509","SunX509", "com.sun.net.ssl.internal.ssl.Provider", "TLS", "hellos", handleID};
          String[] sigs3 = {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), "boolean", String.class.getName(), String.class.getName(), ArrayList.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                  String.class.getName()};
            String res;
            res = (String) esb.invoke(objName3, "getResponseSOAPMsg", args3, sigs3);
            System.out.println("getResponseSOAPmsg executed!");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            System.out.println("I am here!");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS","UserProvidedKeystore_21226"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS","UserProvidedKeystore_21226"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        }
        try {
            String logs;
            logs =serviceProviderManager.getLastErrLogs(Integer.MAX_VALUE);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            if(logs.isEmpty())
            {
                System.out.println("FES error logs are empty!");
            }
            else {
                Assert.fail("FES error log is not empty");
            }
        }  catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigWS", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        }
    }
   @Test(groups = "Bugs", description = "Bug - 21226 ", dependsOnMethods = "TestDisableStubConfigWS", alwaysRun = true)
    public void TestBug_21226REST() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestBug_21226REST", "UserProvidedKeystore_21226"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestBug_21226REST", "UserProvidedKeystore_21226"));
        String req=null;
        try {
            serviceProviderManager.clearFESErrLogs();
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        }
        try {
            epListener = new SampleEventProcessListener(appName1, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_21226REST.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();
        Map<String, Vector<String>> maps=null;
        try {
            esb = JMXClient.getInstance();
            objName1 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            handleID = esb.getHandleId();
            Object[] args1 = {appName1, "RESTStub1", false, "","", true, true, true, true, "","","", "", "","", "", "","","", "", handleID};
            String[] sigs1 = {String.class.getName(), String.class.getName(), "boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                    String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                    String.class.getName()};
            maps = (Map<String, Vector<String>>) esb.invoke(objName1, "getRESTServiceMethodDetails", args1, sigs1);
        }  catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String resourceName = null;
        String methodName = null;
       try {
            //  esb = JMXClient.getInstance();
            objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");


            for (String resource : maps.keySet()) {
                resourceName=resource;
                break;
            }
           for (String method : maps.get(resourceName)) {
               methodName =method;
               break;
           }

           Object[] args2 = {appName1, "RESTStub1",resourceName, methodName,false, "", "", true, true, true, true, "","","","","","","","","", "","", handleID};
           String[] sigs2 = {String.class.getName(), String.class.getName(), String.class.getName(),String.class.getName(),"boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                   String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                   String.class.getName()};
            req = (String) esb.invoke(objName2, "getRESTRequestMsg",args2,sigs2);
           System.out.println("getRESTRequestMsg executed!");
        }  catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            // esb = JMXClient.getInstance();
            objName3 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            Object[] args3 = {appName1,"RESTStub1", req, false, "", "",resourceName, methodName, true,true , true, true, "","","","", "","", "", "", ""
                    , "", "",handleID};
            String[] sigs3 = {String.class.getName(), String.class.getName(),String.class.getName(), "boolean", String.class.getName(), String.class.getName(), String.class.getName(),String.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                    String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                    String.class.getName()};
            String res;
            res = (String) esb.invoke(objName3, "getRESTResponseMsg", args3, sigs3);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            System.out.println("getRESTResponseMsg executed!");
        }  catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            eventProcessManager.stopEventProcess(appName1, appVersion);
            System.out.println("I am here!");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST","UserProvidedKeystore_21226"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST","UserProvidedKeystore_21226"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        }
        try {
            String logs;
            logs =serviceProviderManager.getLastErrLogs(Integer.MAX_VALUE);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            if(logs.isEmpty())
            {
                System.out.println("FES error logs are empty!");
            }
            else {
                Assert.fail("FES error log is not empty");
            }
        }  catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_21226REST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        }
    }
    @Test(groups = "Bugs", description = "Bug - 21226 ", dependsOnMethods = "TestBug_21226REST", alwaysRun = true)
    public void TestDisableStubConfigREST() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"));
        String req=null;
        try {
            serviceProviderManager.clearFESErrLogs();
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        }
        try {
            epListener = new SampleEventProcessListener(appName1, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_21226REST.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();
        Map<String, Vector<String>> maps=null;
        try {
            esb = JMXClient.getInstance();
            objName1 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            handleID = esb.getHandleId();
            Object[] args1 = {appName1, "RESTStub1", false, "", "", true, false, true, true, "/home/ishita/fiorano.ks","fiorano", "/home/ishita/fiorano.ks",
                    "fiorano", "JKS", "JKS", "SunX509", "SunX509", "com.sun.net.ssl.internal.ssl.Provider","TLS" , "hellos", handleID};
            String[] sigs1 = {String.class.getName(), String.class.getName(), "boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                    String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                    String.class.getName()};
           maps = (Map<String, Vector<String>>) esb.invoke(objName1, "getRESTServiceMethodDetails", args1, sigs1);
        }  catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String resourceName = null;
        String methodName = null;
        try {

            objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            for (String resource : maps.keySet()) {
                resourceName=resource;
                break;
            }
            for (String method : maps.get(resourceName)) {
                methodName =method;
                break;
            }
            Object[] args2 = {appName1, "RESTStub1",resourceName, methodName,false, "", "",true, false, true, true, "/home/ishita/fiorano.ks",
                    "fiorano", "/home/ishita/fiorano.ks", "fiorano","JKS", "JKS", "SunX509","SunX509", "com.sun.net.ssl.internal.ssl.Provider", "TLS","hellos", handleID};
            String[] sigs2 = {String.class.getName(), String.class.getName(), String.class.getName(),String.class.getName(),"boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                    String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                    String.class.getName()};
            req = (String) esb.invoke(objName2, "getRESTRequestMsg",args2,sigs2);
        }  catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            objName3 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            Object[] args3 = {appName1, "RESTStub1", req, false, "", "",resourceName, methodName, true, false, true, true, "/home/ishita/fiorano.ks",
                    "fiorano", "/home/ishita/fiorano.ks","fiorano", "JKS","JKS", "SunX509", "SunX509", "com.sun.net.ssl.internal.ssl.Provider", "TLS","hellos",handleID};
            String[] sigs3 = {String.class.getName(), String.class.getName(),String.class.getName(), "boolean", String.class.getName(), String.class.getName(), String.class.getName(),String.class.getName(), "boolean", "boolean", "boolean", "boolean", String.class.getName(), String.class.getName(),
                    String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(),  String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),
                    String.class.getName()};
            String res;
            res = (String) esb.invoke(objName3, "getRESTResponseMsg", args3, sigs3);
        }  catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try {
            eventProcessManager.stopEventProcess(appName1, appVersion);
            System.out.println("I am here!");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST","UserProvidedKeystore_21226"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST","UserProvidedKeystore_21226"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        }
        try {
            String logs;
            logs =serviceProviderManager.getLastErrLogs(Integer.MAX_VALUE);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            if(logs.isEmpty())
            {
                System.out.println("FES error logs are empty");
            }
            else {
                Assert.fail("FES error log is not empty");
            }
        }  catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDisableStubConfigREST", "UserProvidedKeystore_21226"), e);
            org.testng.Assert.fail("Failed to see the logs!", e);
        }
    }
    @Test(enabled=false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" +
                File.separator + "resources" + File.separator + zipName));
        boolean completed = false;
        byte result[];
        while (!completed) {
            byte[] temp = new byte[1024];
            int readcount = 0;
            readcount = bis.read(temp);

            if (readcount < 0) {
                completed = true;
                readcount = 0;
            }
            result = new byte[readcount];
            System.arraycopy(temp, 0, result, 0, readcount);

            // it will deploy the event process if the correct byte array is send; else gives an error
            this.eventProcessManager.deployEventProcess(result, completed);
        }
    }
}
