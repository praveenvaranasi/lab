package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.*;
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
import com.fiorano.stf.test.core.*;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 3/9/12
 * Time: 11:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestMsgNotIntrceptedOnBrkPont19596 extends AbstractTestNG{
    private IEventProcessManager eventProcessManager1;
    private IEventProcessManager eventProcessManager2;
    private IRmiManager rmiManager;
    private IFPSManager fpsManager;
    private FioranoApplicationController m_fioranoappcontroller;
    private BreakpointMetaData BMData,BMData1;
    private IDebugger Debug;
    private IDebugger Debug2;
    private RouteMetaData RMData;
    private String handleID1;
    private String handleID2;
    private String userName = "admin";
    private String password = "passwd";
    private SampleEventProcessListener epListener = null;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "FEDDISP";
    private String appName2= "TWO";
    private ExecutionController executioncontroller;
    private TestEnvironment testenv;
    private ServerStatusController serverStatus;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initNewSetUp() throws ServiceException, RemoteException {

        try{
            ArrayList urlDetails = getActiveFESUrl();
            Registry registry = LocateRegistry.getRegistry((String) urlDetails.get(0), (Integer) urlDetails.get(1));

        try{
            rmiManager = (IRmiManager) registry.lookup(IRmiManager.class.toString());
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NotBoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        handleID1 = rmiManager.login(userName, password);
       // handleID2 = rmiManager.login(userName, password);
        }catch (Exception e){
            e.printStackTrace();
        }

        this.serverStatus = ServerStatusController.getInstance();
        this.testenv = serverStatus.getTestEnvironment();
        testEnvConfig = ESBTestHarness.getTestEnvConfig();
        this.executioncontroller = ExecutionController.getInstance();
        try {
            this.m_fioranoappcontroller = serverStatus.getServiceProvider().getApplicationController();
        } catch (STFException e) {
             Logger.Log(Level.SEVERE, Logger.getErrMessage("initNewSetup", "TestMsgNotIntrceptedOnBrkPont19596"), e);
        }

       this.fpsManager = rmiClient.getFpsManager();
       this. eventProcessManager1 = rmiManager.getEventProcessManager(handleID1);
        //eventProcessManager2 = rmiManager.getEventProcessManager(handleID2);
        Debug = rmiManager.getBreakpointManager(handleID1);
       // Debug2= rmiManager.getBreakpointManager(handleID2);

    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "initNewSetUp", alwaysRun = true)
    public void TestMsgNotIntrcptBP() throws RemoteException,ServiceException{

        Logger.LogMethodOrder(Logger.getOrderMessage("TestMsgNotIntrcptBP", "TestMsgNotIntrceptedOnBrkPont19596"));
        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestMsgNotIntrcptBP", "TestMsgNotIntrceptedOnBrkPont19596"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager1.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestMsgNotIntrcptBP", "TestMsgNotIntrceptedOnBrkPont19596"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestMsgNotIntrcptBP", "TestMsgNotIntrceptedOnBrkPont19596"), e);
           // Assert.fail("Failed to do ep listener!", e);
        }

        try {
            stopAndDeleteEP(eventProcessManager1, appName, appVersion);

            deployEventProcess("FEDDISP.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestMsgNotIntrcptBP", "TestMsgNotIntrceptedOnBrkPont19596"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        eventProcessManager1.checkResourcesAndConnectivity(appName, appVersion);
        eventProcessManager1.startEventProcess(appName, appVersion, false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String[] nodes1 = {"fps1"};
        String[] nodes = {"fps"};
        try {
            Application application = m_fioranoappcontroller.getApplication(appName, appVersion);
            application.getServiceInstance("Display1").setNodes(nodes1);

            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestMsgNotIntrcptBP", "TestMsgNotIntrceptedOnBrkPont19596"), e);
        }

        try {
            Application application = m_fioranoappcontroller.getApplication(appName, appVersion);
            application.getServiceInstance("Feeder1").setNodes(nodes);

            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestMsgNotIntrcptBP", "TestMsgNotIntrceptedOnBrkPont19596"), e);
        }

        List<RouteMetaData> rList = eventProcessManager1.getRoutesOfEventProcesses(appName,appVersion);
        RMData = rList.get(0);

        BMData = Debug.addBreakpoint(appName,appVersion,RMData.getRouteName());
        String BPortSource = BMData.getSourceQueueName();
        String BPortTarget = BMData.getTargetQueueName();

        fpsManager.shutdownPeer("fps1");
        eventProcessManager1.stopEventProcess(appName, appVersion);
        TestStartFPS1();
        eventProcessManager1.checkResourcesAndConnectivity(appName, appVersion);
        eventProcessManager1.startEventProcess(appName, appVersion, false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        List<PortInstanceMetaData> PList = eventProcessManager1.getPortsForEventProcesses(appName,appVersion);
        String[] name = new String[PList.size()];
        System.out.println(PList.size());
        for(int i=0;i<PList.size();i++){
           name[i] = PList.get(i).getActualDestinationName();
           System.out.println(name[i]);
        }

        String IntermMsg = null;
        String messageSent = topicpublisher("fps","FEDDISP__1_0__FEEDER1__OUT_PORT");
        IntermMsg = queuereceiver("fes", BPortSource);
        if(IntermMsg ==null){
            try{
               throw new Exception();
            }catch(Exception e){
                 Logger.Log(Level.SEVERE, Logger.getErrMessage(" TestMsgNotIntrcptBP", "TestMsgNotIntrceptedOnBrkPont19596"), e);
            }
        }else{
             Logger.Log(Level.INFO, Logger.getFinishMessage("TestMsgNotIntrcptBP", "TestMsgNotIntrceptedOnBrkPont19596"));
        }

        queuesender("fes", BPortTarget, IntermMsg);
    }

    @Test(groups = "Bugs", description = "Display with launch type none - bug 19623 ", dependsOnMethods = "TestMsgNotIntrcptBP", alwaysRun = true)
    public void stopAndDeleteEP() throws RemoteException,ServiceException{
        try {
            eventProcessManager1.stopEventProcess(appName, appVersion);
            eventProcessManager1.deleteEventProcess(appName, appVersion, true, true);
        } catch (Exception e){
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestKill","TestBug_21910"), e);
        }
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
            this.eventProcessManager1.deployEventProcess(result, completed);

        }
    }

    @Test(enabled = false)
    public void TestStartFPS1() throws RemoteException,ServiceException{
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStartFPS1", "TestMsgNotIntrceptedOnBrkPont19596"));

            if (!fpsManager.isPeerRunning("fps1")) {
                startServer(testenv, testEnvConfig, "fps1", SERVER_TYPE.FPS);
            }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStartFPS1", "TestMsgNotIntrceptedOnBrkPont19596"));
    }

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

     @Test(enabled = false)
    public String topicpublisher(String servername, String topicname) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicname);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestMsgNotIntrceptedOnBrkPont19596"), e);
           // Assert.fail("Failed to do create publisher to outport", e);
        }
        String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "TestMsgNotIntrceptedOnBrkPont19596"), e);
            //Assert.fail("Failed to do publish message on outport", e);
        } finally {

            //pub.close();
        }
        return messageSent;
    }

    @Test(enabled = false)
    public String queuereceiver(String servername, String queuename) {
        Receiver rec = new Receiver();
        rec.setDestinationName(queuename);
        rec.setCf("primaryQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "TestMsgNotIntrceptedOnBrkPont19596"), e);
            //Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "TestMsgNotIntrceptedOnBrkPont19596"), e);
            //Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        return messageOnDestination;
        //Assert.assertEquals(messageSent, messageOnDestination);

    }

    @Test(enabled = false)
    public void queuesender(String servername, String queuename, String messageSent) {

        Publisher qpub = new Publisher();
        qpub.setDestinationName(queuename);
        qpub.setCf("primaryQCF");
        qpub.setUser("anonymous");
        qpub.setPasswd("anonymous");
        try {
            qpub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuesender", "TestMsgNotIntrceptedOnBrkPont19596"), e);
            //Assert.fail("Failed to do create receiver to inport", e);
        }
        try {
            qpub.sendMessage(messageSent);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuesender", "TestMsgNotIntrceptedOnBrkPont19596"), e);
            //Assert.fail("Failed to do get message from inport", e);
        } finally {
            qpub.close();
        }
    }


}