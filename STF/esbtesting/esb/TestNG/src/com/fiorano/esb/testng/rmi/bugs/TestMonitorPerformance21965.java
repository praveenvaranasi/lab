package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: malarvizhi
 * Date: 8/16/12
 * Time: 11:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestMonitorPerformance21965 extends AbstractTestNG {

    private JMXClient esb;
    private IEventProcessManager eventProcessManager;
    private String appName = "BUG21965";
    private String serviceInstanceName = "JMSIn1";
    private String fpsName="fps1";
    private String handleID;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private SampleEventProcessListener epListener = null;
    private String eventCategory="ALL";
    private int fromIndex=Integer.MIN_VALUE;
    private int toIndex=Integer.MAX_VALUE;
    private Date from=null;
    private Date to=null;
    private float appVersion = 1.0f;
    private ObjectName objName1 =null;
    private ObjectName objName2=null;

    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestPermission"));
        esb = JMXClient.getInstance();
        handleID = esb.getHandleId();
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group getWSContexts. as eventProcessManager is not set.");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestRemoteServiceInstance"));
    }

    @Test(groups = "Bugs", description = "bug 21965-Moniter Performance when Application is Launched in FPS1", dependsOnMethods = "initSetup", alwaysRun = true)
    public void testSetListenerForUserEvents() {
      try {
           objName1 = new ObjectName("Fiorano.Esb.Events:ServiceType=EventsManager,Name=FESEventsManager,type=config");
           JMXClient.connect("admin", "passwd");
           jmxClient.invoke(objName1, "setListenForUserEvents", new Object[]{true}, new String[]{"boolean"});
           System.out.println("ListenForUserEvents is set to true");

      } catch (Exception e) {
           Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetListenerForUserEvents", "TestMonitorPerformance21965"), e);
           Assert.fail("Failed to set ListenForUser to True", e);
      }
      try {
           objName2 = new ObjectName("Container.UserComponents:Name=ConfigurationProvider,ServiceType=ConfigProvider");
           esb.invoke(objName2, "save", new Object[]{}, new String[]{});

      } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetListenerForUserEvents", "TestMonitorPerformance21965"), e);
      }
      try {
           rmiClient.restartEnterpriseServer();
           System.out.println("\n"+"Restarting FES Server");
           Thread.sleep(50000);
           dupConstructor();
           initSetup();
      } catch (Exception e) {
           System.out.println("problem in restarting FES Server");
           e.printStackTrace();
      }
    }

    @Test(groups = "Bugs", dependsOnMethods = "testSetListenerForUserEvents", alwaysRun = true)
    public void testLaunchApplication() throws RemoteException, ServiceException {

        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchApplication", "TestMonitorPerformance21965"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("testLaunchApplication", "TestMonitorPerformance21965"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchApplication", "TestMonitorPerformance21965"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("bug21965.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testLaunchApplication", "TestMonitorPerformance21965"));

        try{
            topicpublisher("fps1", appName + "__" + "1_0" + "__" + "Feeder1" + "__OUT_PORT");
            Thread.sleep(5000);
        }
        catch (Exception e)
        {
            Logger.Log(Level.SEVERE, Logger.getFinishMessage("testLaunchApplication", "TestMonitorPerformance21965"),e);

        }
    }
      int size=-1;
      ArrayList eventList=null;
      boolean test=true;
    @Test(groups = "Bugs", dependsOnMethods = "testLaunchApplication", alwaysRun = true)
    public void testSearchUserEvents() throws InstanceNotFoundException, IOException, ReflectionException, MBeanException {
         try {
            objName1 = new ObjectName("Fiorano.Esb.Events:ServiceType=EventReciever,Name=ESBEventReciever");
             Object[] params1 = {handleID, appName, String.valueOf(appVersion), serviceInstanceName,fpsName, eventCategory, from, to, fromIndex,toIndex};
             String[] signatures1 = {String.class.getName(),String.class.getName(), String.class.getName(),String.class.getName(), String.class.getName(), String.class.getName(), Date.class.getName(), Date.class.getName(),int.class.getName(),int.class.getName()};
            eventList =  (ArrayList)esb.invoke(objName1,"searchUserEvents", params1, signatures1);
         }catch (Exception e)
         {
             e.printStackTrace();
             Logger.Log(Level.SEVERE,Logger.getErrMessage("testSearchUserEvents","TestMonitorPerformance21965"),e);
         }
          size= eventList.size();
          System.out.println("\n"+"No of Records in Monitoring Data :"+size+"\n");
          for(int loop=0;loop<size;loop++)
          {
             String userData= eventList.get(loop).toString();
             if(!(userData.contains("FPS Name = FPS1")))
             test=false;
          }
          if(!test)
          Assert.fail("peer server name shown is wrong");
          if((size==-1)||(size==0))
          Assert.fail("No Records found in Monitoring data");
    }

    @Test(groups = "Bugs", dependsOnMethods = "testSearchUserEvents", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestMonitorPerformance21965"));
        try {
            eventProcessManager.stopEventProcess(appName, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestMonitorPerformance21965"));
    }

    @Test(enabled = false)
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
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisherReceiver", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }

        String messageSent;
        try {
            int loop=0;
            System.out.println("\n"+"Sending Messages to"+serviceInstanceName);
             while(loop<=10){
                messageSent = "Fiorano" + System.currentTimeMillis();
                pub.sendMessage(messageSent);
                loop++;
             }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisherReceiver", "TestMonitorPerformance21965"), e);
            Assert.fail("Failed to do publish message on outport", e);
        }

    }

}
