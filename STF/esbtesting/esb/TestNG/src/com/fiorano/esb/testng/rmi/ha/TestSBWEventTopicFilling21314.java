package com.fiorano.esb.testng.rmi.ha;



import com.fiorano.esb.common.CommonConstants;
import com.fiorano.esb.log.impl.IESBLogManager;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.sbw.handler.services.SBWGetJdbcService;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.*;
import fiorano.tifosi.cfg.ConfigurationManagerHome;
import fiorano.tifosi.cfg.IConfigurationManager;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.log.ILog;
import fiorano.tifosi.log.ILogManager;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: anurag
 * Date: 3/7/12
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSBWEventTopicFilling21314  extends AbstractTestNG  {
    private IEventProcessManager eventProcessManager;
    private String appName = "BUG_21314";
    private TestEnvironmentConfig testEnvConfig;
    private ServerStatusController serverStatusController;
    private FioranoApplicationController m_fioranoappcontroller;
    private SampleEventProcessListener epListener = null;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
      // SBW config Manager. This loads up the SBW configuration file (sbwdb.cfg) and loads up in the configuration manager.
    IConfigurationManager m_sbwdbConfigManager;

    // SBW SQL Manager. Loads up all the SQL statements needed for querying the SBW Database.
    IConfigurationManager m_sbwdbsqlConfigManager;
    private SBWGetJdbcService getJdbcService;
    char fsc = File.separatorChar;
    private ServerElement serverElement;
    private TestEnvironment testenv;
    private ExecutionController executioncontroller;
    private String profile;
    private ProfileElement profileElement;
    private Map<String, ProfileElement> profileElements = null;
    private MachineElement machine1;
    private  String machine = null;
    private boolean isWrapper = false;
    private boolean isHA = true;
    private IServiceProviderManager serviceprovidermanager;
    private IFPSManager fpsManager;
    private float appVersion = 1.0f;
    private String appVersion1 = "1.0f";
    @Test(groups="Bugs" ,alwaysRun = true)
    public void InitSetup() throws InterruptedException {

        this.serverStatusController=ServerStatusController.getInstance();
        this.fpsManager = rmiClient.getFpsManager();
        this.testenv=serverStatusController.getTestEnvironment();
        this.executioncontroller=ExecutionController.getInstance();

        this.serviceprovidermanager = rmiClient.getServiceProviderManager();
        this.testEnvConfig= ESBTestHarness.getTestEnvConfig();
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run  as eventProcessManager is not set.");
        }

    }


     @Test(groups="Bugs",description="JMSX_PM_TIDFES_SBW_EVENTS_TOPIC data is filling in case of HA--bug 21314", dependsOnMethods="InitSetup", alwaysRun = true)
    public void Test_SBW_EVENT_TOPIC_FillingLaunch()
     {
        Logger.LogMethodOrder(Logger.getOrderMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"));
         TestKillActiveHAFES();
         try {
             Thread.sleep(15000);
         } catch (InterruptedException e) {
             e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
         try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("Bug_21314.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e);
            Assert.fail("Failed to do CRC!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appName, 1.0f);

            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_SBW_EVENT_TOPIC_FillingLaunch", "Test_SBW_EVENT_TOPIC_Filling"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
     }

    @Test(groups="BUGS", dependsOnMethods="Test_SBW_EVENT_TOPIC_FillingLaunch",alwaysRun = true)
    public void TestCase_21314() throws  TifosiException, IOException{

        String messageSent1=topicPublisher("fps", appName + "__" + "FEEDER1" + "__OUT_PORT");
         String Path_To_SBW = null;
        String sbwSqlFilePath = null;
       // createTopicSubscriber(appName + "__" + "Display1" + "__IN_PORT");
       // onMessage();
        try{

            HashMap serverdetails = null;
            serverdetails=rmiClient.getServiceProviderManager().getServerDetails();
            String url = (String) serverdetails.get("Server URL");
                if(url.endsWith("1947"))
                {
                    Path_To_SBW=  System.getProperty("FIORANO_HOME")+fsc+"esb"+fsc+"server"+fsc+"profiles"+fsc+"haprofile1"+fsc+"primary"+fsc+"FES"+fsc+"conf";
                }else{
                        Path_To_SBW=  System.getProperty("FIORANO_HOME")+fsc+"esb"+fsc+"server"+fsc+"profiles"+fsc+"haprofile1"+fsc+"secondary"+fsc+"FES"+fsc+"conf";
                     }
        }catch (Exception e)
        {
            e.printStackTrace();
        }


        ConfigurationManagerHome cfgHome = new ConfigurationManagerHome();
        try{
        m_sbwdbConfigManager = cfgHome.createConfigurationManager(CommonConstants.CONFMANAGER_IMPL_CLASS);
        m_sbwdbsqlConfigManager = cfgHome.createConfigurationManager(CommonConstants.CONFMANAGER_IMPL_CLASS);
        }catch(Exception e)
        {e.printStackTrace();
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestCase_21314","Test_SBW_EVENT_TOPIC_Filling"));
        }

        // Start the SBW Config Manager with the config file specified
          try{
            m_sbwdbConfigManager.startup(new File(Path_To_SBW+fsc+"sbwdb.cfg").getCanonicalPath());
            // Load all the SBW Sql statements with the config file specified
             sbwSqlFilePath = m_sbwdbConfigManager.get(Path_To_SBW+fsc+"sbwdml.sql");
            } catch (TifosiException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        try{
        if(sbwSqlFilePath != null){
            if(!new File(sbwSqlFilePath).exists()){
                System.out.println("\nFILE " + sbwSqlFilePath + " DOES NOT EXIST. PLEASE SPECIFY CORRECT VALUE FOR 'SBW_SQL_FILE_PATH' PROPERTY IN connection.properties FILE");
                System.exit(0);
            }
            m_sbwdbsqlConfigManager.startup(sbwSqlFilePath);
        }else
        {  System.out.println("\nPLEASE SPECIFY CORRECT VALUE FOR 'SBW_SQL_FILE_PATH' PROPERTY IN connection.properties FILE");
          m_sbwdbsqlConfigManager.startup(Path_To_SBW+fsc+"sbwdml.sql");
        }
        getJdbcService = new SBWGetJdbcService(new LogManager(), false ,false);
        getJdbcService = new SBWGetJdbcService(new LogManager(), false, false);

            getJdbcService.startup(m_sbwdbConfigManager,appName , m_sbwdbsqlConfigManager);

            int count = getJdbcService.countAllWorkFlowInstancesForApp(appName,appVersion1);
            }catch (Exception e)
                {
                e.printStackTrace();
                }
        jmxClient = JMXClient.getInstance();

        ObjectName objName = null;
        try {
            objName = new ObjectName("Fiorano.etc:ServiceType=AdminService,Name=AdminService");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        Object[] args = {"FES_SBW_EVENTS_TOPIC"};
        Object[] params= {"true"};
        String[] sigs = {String.class.getName()};
        try {
            Integer count = (Integer) jmxClient.invoke(objName, "", args, sigs);
        } catch (ReflectionException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (MBeanException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

@Test(groups = "Bugs", dependsOnMethods = "TestCase_21314", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "Test_ComponentPortChange"));
        try {
            eventProcessManager.stopEventProcess(appName, 1.0f);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, 1.0f, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication","Test_ComponentPortChange"), e);
            Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "Test_ComponentPortChange"));
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



    public void TestKillActiveHAFES() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestKillActiveHAFES", "TestAppRepoSync"));
        for(int i=0;i<2;i++)
        {
        ArrayList arrlist = null;
        try {
            arrlist = AbstractTestNG.getActiveFESUrl();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillActiveHAFES", "TestAppRepoSync"), e);
            Assert.fail("Failed to get active FES url", e);
        }
        if (arrlist.get(1).toString().equals("2047"))
            profile = "primary";
        else
            profile = "secondary";
        serverElement = testenv.getServer("hafes");
        profileElements = serverElement.getProfileElements();


        profileElement = profileElements.get(new String(profile));
        machine = profileElement.getMachineName();
        machine1 = testenv.getMachine(machine);
        isWrapper = profileElement.isWrapper();
        isHA = serverElement.isHA();
        try {
            rmiClient.shutdownEnterpriseServer();


            Thread.sleep(60000);
            executioncontroller.startServerOnRemote(machine1.getAddress(), serverElement.getMode(), "haprofile1/" + profile, isWrapper, isHA);
            Thread.sleep(60000);
            rmiClient = RMIClient.getInstance();
            InitSetup();

        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillActiveHAFES", "TestAppRepoSync"), e);
            Assert.fail("Failed to Restart Primary FES", e);
        }
       }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestKillActiveHAFES", "TestAppRepoSync"));
    }

    /**
     * Method to create FMQ pub/sub
     */
    @Test(enabled = false)
    public String topicPublisher(String servername, String topicname) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicname);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");

        try {
            pub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }


        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            for(int i=0;i<10;i++)
            {
                pub.sendMessage(messageSent);
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do publish message on outport", e);
        }
        finally {

            pub.close();
        }
        return messageSent;
    }

    Receiver sub = new Receiver();
    @Test(enabled = false)
    public void createTopicSubscriber(String topicName) {

        sub.setDestinationName(topicName);
        sub.setCf("primaryTCF");
        sub.setUser("anonymous");
        sub.setPasswd("anonymous");
        try {
            sub.initialize("fps");
        } catch (Exception e) {
            Assert.fail("Failed to subscribe to topic", e);
        }

    }

    @Test(enabled = false)
    public void onMessage() {
        String message = null;
        try {
            for(int i=0;i<10;i++)
            {
                message = sub.getMessageOnDestination();
            }
        } catch (Exception e) {
            Assert.fail("Failed to fetch the message on topic", e);
        } finally {
            sub.close();
        }

    }


    private  class LogManager implements IESBLogManager {

        public ILogManager getLogManager() {
            return null;
        }

        public ILog getErrorLog() {
            return null;
        }

        public ILog getOutLog() {
            return null;
        }

        public String getLogManagerImpl() {
            return null;
        }

        public String getTESLastErrLogs(int i) throws TifosiException {
            return null;
        }

        public String getTESLastOutLogs(int i) throws TifosiException {
            return null;
        }

        public String getMQLastErrLogs(int i) throws TifosiException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getMQLastOutLogs(int i) throws TifosiException {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        public void exportFESLogs(String s, String s1) throws TifosiException {

        }

        public void clearTESOutLogs() throws TifosiException {

        }

        public void clearTESMQOutLogs() throws TifosiException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void clearTESErrLogs() throws TifosiException {

        }

        public void clearTESMQErrLogs() throws TifosiException {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public String getLogPath() {
            return null;
        }

        public void clearAMSOutLogs() throws TifosiException{

        }

        public void clearAMSErrLogs() throws TifosiException{

        }
    }

}
