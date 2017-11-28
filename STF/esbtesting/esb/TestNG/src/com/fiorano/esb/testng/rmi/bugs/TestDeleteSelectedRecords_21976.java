package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.services.common.monitor.ExecutionTimeDetailsConstants;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.remote.RTLClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.events.UserEvent;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Anubhav
 * Date: 8/23/12
 * Time: 15:04 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestDeleteSelectedRecords_21976 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "BUG_21976";
    private SampleEventProcessListener epListener = null;
    private IDebugger debugger;
    private BreakpointMetaData BMData;
    public IServiceProviderManager serverlogs;
    public static IRmiManager rmiManager;
    private String HandleID;
    private ObjectName objName;
    private String fioranoHome = null;
    private String jmsnotif_file_path = null;
    private String backlog_file_path = null;
    private JMXClient esb = null;
    private FioranoApplicationController m_fioFioranoApplicationController;
    public ArrayList<UserEvent> eventList;
    private float appVersion = 1.0f;

    @Test(groups = "Bugs", alwaysRun = true)
    public void initBug_Setup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            // Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        this.fpsManager = rmiClient.getFpsManager();
        if (fpsManager == null) {
            // Assert.fail("Cannot run Group Bugs as fpsManager is not set.");
        }
        this.debugger = rmiClient.getDebugger();
    }

    @Test(groups = "Bugs", description = "[ESB DashBoard]Delete Selected Records option is not working in specific case - bug 21976 ", dependsOnMethods = "initBug_Setup", alwaysRun = true)
    public void TestBUG_21976() {

        Logger.LogMethodOrder(Logger.getOrderMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"));

        ObjectName objName = null;
        ObjectName objName2=null;
        try {
            objName = new ObjectName("Fiorano.Esb.Events:ServiceType=EventsManager,Name=FESEventsManager,type=config");
        } catch (MalformedObjectNameException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetListenerForUserEvents", "TestDeleteSelectedRecords_21976"), e);
        }
        Logger.LogMethodOrder(Logger.getOrderMessage("testStartRTLEventSubscriber", "TestSBWEvent"));
        try {
            JMXClient.connect("admin", "passwd");
            jmxClient.invoke(objName, "setListenForUserEvents", new Object[]{true}, new String[]{"boolean"});
            System.out.println("ListenForUserEvents is set to true");

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetListenerForUserEvents", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to set ListenForUser to True", e);
        }
        try {
            objName2 = new ObjectName("Container.UserComponents:Name=ConfigurationProvider,ServiceType=ConfigProvider");
        } catch (MalformedObjectNameException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetListenerForUserEvents", "TestDeleteSelectedRecords_21976"), e);
        }

        try {

            esb.invoke(objName2, "save", new Object[]{}, new String[]{});

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetListenerForUserEvents", "TestDeleteSelectedRecords_21976"), e);
        }
        try {
            rtlClient.restartEnterpriseServer();
            System.out.println("\n"+"Restarting FES Server");
            Thread.sleep(100000);
            //initializing
            rmiClient = RMIClient.getInstance();
            rtlClient = RTLClient.getInstance();
            jmxClient = JMXClient.getInstance();
            initBug_Setup();
            
        } catch (Exception e) {
            System.out.println("problem in restarting FES Server");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        
        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);


        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("BUG_21976.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestBUG_21976", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","TestDeleteSelectedRecords_21976"),e);
            Assert.fail("Failed to stop EP",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddBP","TestDeleteSelectedRecords_21976"),e);
            Assert.fail("Failed to stop EP",e);
        }

    }
    //ADD BP AND SEND MSG
    @Test(groups="Bugs", dependsOnMethods="TestBUG_21976", alwaysRun = true)
    private void TestPurgeSelected(){
        try {
            deployEventProcess("BUG_21976.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected", "TestDeleteSelectedRecords_21976"), e);
            // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected", "TestDeleteSelectedRecords_21976"), e);
            // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        eventStore.clear();

        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to start all services!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected", "TestDeleteSelectedRecords_21976"), e);
            Assert.fail("Failed to start all services!", e);
        }
        try{
            topicpublisher("fps", appName + "__" + "1_0" + "__" + "Feeder1" + "__OUT_PORT");
        }
        catch (Exception e)
        {

        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.testEnvConfig = ESBTestHarness.getTestEnvConfig();
        fioranoHome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        jmsnotif_file_path = fioranoHome + File.separator + "runtimedata" + File.separator + "repository" + File.separator + "Notifications";
        backlog_file_path = fioranoHome + File.separator + "runtimedata" + File.separator + "repository" + File.separator + "Policies" + File.separator + "Backlog";
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Bugs as eventProcessManager is not set.");
        }
        try {
            m_fioFioranoApplicationController = ServerStatusController.getInstance().getServiceProvider().getApplicationController();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestDeleteSelectedRecords_21976"), e);
            //Assert.fail("Failed to run test,since we could not fioranoapplicationcontroller!", e);
        }

        try{
            esb = JMXClient.getInstance();
            objName = new ObjectName("Fiorano.Esb.Events:ServiceType=EventReciever,Name=ESBEventReciever");
            String eventCategory="ALL";
            String appInstName="BUG_21976";
            String servInstName="JMSIN1";
            String fpsName="fps";
            //String source=null;
            Object[] params = {esb.getHandleId(),appInstName,"1.0",servInstName, fpsName, eventCategory, null, null, 1, 20};
            String[] signatures = {String.class.getName(),String.class.getName(),String.class.getName(),String.class.getName(), String.class.getName(), String.class.getName(), Date.class.getName(), Date.class.getName(),int.class.getName(),int.class.getName()};
            eventList= (ArrayList)esb.invoke(objName,"searchUserEvents", params, signatures);
        }
        catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to get details ",e);
        }catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to get details",e);
        }catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to get details",e);
        }catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to get details",e);
        }catch (MalformedObjectNameException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to get details",e);
        }

        try{
            esb = JMXClient.getInstance();
            objName = new ObjectName("Fiorano.Esb.Events:ServiceType=EventReciever,Name=ESBEventReciever");
            int eventType=217;
            ArrayList records = new ArrayList();
            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
            ArrayList events = new ArrayList();
            for(int i=0;i<eventList.size();i++){
                UserEvent userEvent = (UserEvent)eventList.get(i);
                Hashtable properties = userEvent.getPropertiesHash();
                records.add(new Date(Long.parseLong((String)properties.get(ExecutionTimeDetailsConstants.TIMESTAMP))));
            }

            Object[] params1 = {eventType, records};
            String[] signatures1= {int.class.getName(), ArrayList.class.getName()};
            esb.invoke(objName,"purgeSelectedEvents", params1, signatures1);
        }catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to Purge Selected Records ",e);
        }catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to Purge Selected Records",e);
        }catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to Purge Selected Records",e) ;
        }catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to Purge Selected Records",e)  ;
        }catch (MalformedObjectNameException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPerformanceMonitor", "TestDeleteSelectedRecords_21976"), e);
            e.printStackTrace();
            Assert.fail("Failed to Purge Selected Records",e)   ;
        }

        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected","TestDeleteSelectedRecords_21976"),e);
            Assert.fail("Failed to stop and delete EP",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestPurgeSelected","TestDeleteSelectedRecords_21976"),e);
            Assert.fail("Failed to stop and delete EP",e);
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

    @Test(enabled = false)
    public void topicpublisher(String servername, String topicname) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicname);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestDeleteSelectedRecords_21976"), e);
            //  Assert.fail("Failed to do create publisher to outport", e);
        }
        int loop=0;
        String messageSent;
        System.out.println("\n"+"Sending Messages to "+ "JMSIn1");
        try {

            while(loop<=10){
                messageSent = "Fiorano" + System.currentTimeMillis();
                pub.sendMessage(messageSent);
                loop++;
            }

        } catch (Exception e) {
            System.out.println("Message not sent");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestDeleteSelectedRecords_21976"), e);
            //  Assert.fail("Failed to do publish message on outport", e);
        }
    }
}
