package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.sbw.handler.SBWManager;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
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
 * Date: 8/21/12
 * Time: 11:26 AM
 * To change this template use File | Settings | File Templates.
 */

public class NoOptionToReinject_22060 extends AbstractTestNG{

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private IServiceProviderManager serviceProviderManager;
    private String serviceInstanceName = "Display1";
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "BUG_22060";
    private SampleEventProcessListener epListener = null;
    private JMXClient esb = null;
    private ObjectName objName1,objName2, objName3,objName4, objName5;
    private String handleID;
    private SBWManager m_manager;
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

    @Test(groups = "Bugs", description = "Bug - 22060 ", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestBug_22060() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestBug_22060", "NoOptionToReinject_22060"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestBug_22060", "NoOptionToReinject_22060"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_22060.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
            org.testng.Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
            org.testng.Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try{
            topicpublisher("fps", appName +"__"+ "1_0" +"__" +"Feeder1" + "__OUT_PORT");
        }
        catch (Exception e)
        {

        }
        ArrayList docsList = new ArrayList();
        int numDocs =0 ;
        HashMap map1 = null;
        try {
            esb = JMXClient.getInstance();
            handleID = esb.getHandleId();
            objName1 = new ObjectName("Fiorano.Esb.Sbw:ServiceType=SBWManager,Name=SBWManager");
            Object[] params = {handleID, appName, "", "", "", "","","", "", "", "", "","",null, null};
            String[] signatures = {String.class.getName(),String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),String.class.getName(), Date.class.getName(), Date.class.getName()};
            numDocs = (Integer) esb.invoke(objName1, "countSearchedDocumentsWithText", params, signatures);
            System.out.println("numDocs evaluated");

        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
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

        ArrayList workFlowList=null;
        try {
            esb = JMXClient.getInstance();
            handleID = esb.getHandleId();
            objName2 = new ObjectName("Fiorano.Esb.Sbw:ServiceType=SBWManager,Name=SBWManager");
            Object[] params = {appName, "1.0",1, 5};
            String[] signatures = {String.class.getName(),String.class.getName(), int.class.getName(), int.class.getName()};
            numDocs = (Integer) esb.invoke(objName2, "countAllWorkFlowInstancesForApp", new Object[]{appName,"1.0"}, new String[]{String.class.getName(), String.class.getName()});
            workFlowList = (ArrayList) esb.invoke(objName2, "getAllWorkFlowInstancesForApp", params, signatures);
            System.out.println("Work flow instances for apps!");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
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
        ArrayList documentInfoList = new ArrayList();
        try {
            objName3 = new ObjectName("Fiorano.Esb.Sbw:ServiceType=SBWManager,Name=SBWManager");
            Object[] params3 = {appName};
            String[] signatures3 = {String.class.getName()};
            documentInfoList = (ArrayList) esb.invoke(objName3, "getDocumentInfosList", params3, signatures3);
            System.out.println("Document infolist obtained!");
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
           objName4 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
           Object[] params4 = {handleID,documentInfoList};
           String[] signatures4 = {String.class.getName(), ArrayList.class.getName()};
           esb.invoke(objName4, "reInjectMultipleDocuments", params4, signatures4);
            System.out.println("Re-injection done!");
       } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
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

        int newNumDocs = 0;
        try {
            objName5 = new ObjectName("Fiorano.Esb.Sbw:ServiceType=SBWManager,Name=SBWManager");
            Object[] params5 = {handleID, appName,"", "", "", "", "","", "", "", "", "","", null,null};
            String[] signatures5 = {String.class.getName(), String.class.getName(),String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),String.class.getName(), Date.class.getName(), Date.class.getName()};
            newNumDocs = (Integer) esb.invoke(objName5, "countSearchedDocumentsWithText", params5, signatures5);
            System.out.println("New number of documents obtained!");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060", "NoOptionToReinject_22060"), e);
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
        System.out.println("newNumDocs evaluated!");
        if(newNumDocs==numDocs)
        {
            org.testng.Assert.fail("Re-injection unsuccessful!");
        } else      {
            System.out.println("Re-injection successful!");
        }
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060","NoOptionToReinject_22060"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBug_22060","NoOptionToReinject_22060"),e);
            org.testng.Assert.fail("Failed to stop EP", e);
        }
    }
    @Test(enabled=false)
    public void topicpublisher(String servername, String topicname) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicname);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "NoOptionToReinject_22060"), e);
          //  Assert.fail("Failed to do create publisher to outport", e);
        }
        int loop=0;
        String messageSent;
        System.out.println("\n"+"Sending Messages to "+ serviceInstanceName);
        try {

            while(loop<=10){
                messageSent = "Fiorano" + System.currentTimeMillis();
                pub.sendMessage(messageSent);
                loop++;
            }

        } catch (Exception e) {
            System.out.println("Message not sent");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "NoOptionToReinject_22060"), e);
          //  Assert.fail("Failed to do publish message on outport", e);
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
