package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.BreakpointMetaData;
import com.fiorano.esb.server.api.IDebugger;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ServiceInstance;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: anurag
 * Date: 2/28/12
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestComponentPortChange21238 extends AbstractTestNG{


    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "TEST_BUG21238";
    private TestEnvironmentConfig testEnvConfig;
    private SampleEventProcessListener epListener = null;
    private byte[] sentFile;
    private IDebugger debugger;
    private ServerStatusController serverStatusController;
    private float appVersion = 1.0f;
    private FioranoApplicationController m_fioranoappcontroller;
    Receiver sub = new Receiver();

    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.testEnvConfig = ESBTestHarness.getTestEnvConfig();
        this.debugger=rmiClient.getDebugger();
        this.serverStatusController = ServerStatusController.getInstance();
        try {
            this.m_fioranoappcontroller = serverStatusController.getServiceProvider().getApplicationController();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "PortChangeForChatComponents"), e);
            Assert.fail("Failed to create ep listener!", e);
        }
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Bugs. as eventProcessManager is not set.");
        }
    }


    @Test(groups = "Bugs", description = "debugger route is not updated when component port destinations are changed - bug 21238 ", dependsOnMethods = "initSetup", alwaysRun = true)
    public void Test_ComponentPortChangeLaunch() {

        Logger.LogMethodOrder(Logger.getOrderMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("Test_Bug21238.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do CRC!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, 1.0f, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        BreakpointMetaData bpmd1 = null;
        try {
            String bpoints[] = debugger.getRoutesWithDebugger(appName, appVersion);
            if(bpoints.length > 0)
                debugger.removeAllBreakpoints(appName,appVersion);
            bpmd1 = debugger.addBreakpoint(appName, appVersion, "route1");
            Assert.assertNotNull(bpmd1.getSourceQueueName());

            String[] route = debugger.getRoutesWithDebugger(appName, appVersion);
            Assert.assertEquals(route.length, 1);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChangeLaunch", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

    }



    @Test(groups = "Bugs",dependsOnMethods = "Test_ComponentPortChangeLaunch", alwaysRun = true)
    public void Test_ComponentPortChange_case1()
    {
        String messageSent1=topicPublisher("fps", appName + "__" + "1_0" + "__" + "FEEDER1" + "__OUT_PORT");
        try {
            eventProcessManager.stopServiceInstance(appName, appVersion, "Display1");
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case1", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case1", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

        try {
            Application application = m_fioranoappcontroller.getApplication(appName, 1.0f);
            List<ServiceInstance> serviceInstanceList = application.getServiceInstances();

            for(ServiceInstance serviceInstanc: serviceInstanceList){

                if(serviceInstanc.getName().equalsIgnoreCase("Display1")){
                    serviceInstanc.getInputPortInstance("IN_PORT").setDestinationType(1);

                }
            }


            application.setServiceInstances(serviceInstanceList);
            m_fioranoappcontroller.saveApplication(application);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case2", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to to get service information!", e);
        }
        try {
            eventProcessManager.synchronizeEventProcess(appName, 1.0f);
            eventProcessManager.startServiceInstance(appName, appVersion, "Display1");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case1", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case1", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do CRC!", e);
        }

        createSubscriber("fps", appName + "__"+ "1_0" +"__" + "Display1" + "__IN_PORT");
        queueReceiver("fes", appName + "__" + "1_0" + "__ROUTE1__C", messageSent1);
        queueSender("fes", appName + "__" + "1_0" + "__ROUTE1__D",messageSent1);
        onMessage();
        //queueReceiver("fps", appName + "__" + "Display1" + "__IN_PORT",messageSent1);
        stopAndDeleteEP(eventProcessManager, appName, appVersion);
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("Test_ComponentPortChange_case1", "Test_ComponentPortChange"));
    }


      @Test(groups = "Bugs",dependsOnMethods = "Test_ComponentPortChange_case1", alwaysRun = true)
        public void Test_ComponentPortChange_case2()
        {   System.out.print("case 1 over ...now launching case 2");
            Test_ComponentPortChangeLaunch();
         String messageSent=topicPublisher("fps", appName + "__" + "1_0" + "__" + "FEEDER1" + "__OUT_PORT");

       try {
            eventProcessManager.stopServiceInstance(appName, appVersion, "Display1");

            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case2", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case2", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }

       try {
        Application application = m_fioranoappcontroller.getApplication(appName, 1.0f);
        List<ServiceInstance> serviceInstanceList = application.getServiceInstances();
        serviceInstanceList.get(0).setNodes(new String[]{"fps1"});


        application.setServiceInstances(serviceInstanceList);
        m_fioranoappcontroller.saveApplication(application);
    } catch (TifosiException e) {
        Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case2", "Test_ComponentPortChange"), e);
        Assert.fail("Failed to to get service information!", e);
    }

          try {
            eventProcessManager.checkResourcesAndConnectivity(appName, 1.0f);

              eventProcessManager.startServiceInstance(appName, appVersion, "Display1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case2", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_ComponentPortChange_case2", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do CRC!", e);
        }


            queueReceiver("fes", appName + "__" + "1_0" + "__ROUTE1__C",messageSent);
            createSubscriber("fps1", appName + "__1_0__" + "Feeder" + "__IN_PORT");
            queueSender("fes", appName + "__" + "1_0" + "__ROUTE1__D",messageSent);
            String msg = onMessage();
            Assert.assertEquals(messageSent, msg);


        stopAndDeleteEP(eventProcessManager, appName, appVersion);
     Logger.Log(Level.SEVERE, Logger.getFinishMessage("Test_ComponentPortChange_case2", "Test_ComponentPortChange"));
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
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("topicpublisher", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {

            pub.close();
        }
        return messageSent;
    }

    @Test(enabled = false)
    public String queueReceiver(String servername, String queuename, String messageSent) {
        Receiver rec = new Receiver();
        rec.setDestinationName(queuename);
        rec.setCf("primaryCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuereceiver", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        Assert.assertEquals(messageSent, messageOnDestination);
        return messageSent;
    }


    @Test(enabled = false)
    public void queueSender(String servername, String queuename, String messageSent) {

        Publisher qpub = new Publisher();
        qpub.setDestinationName(queuename);
        qpub.setCf("primaryCF");
        qpub.setUser("anonymous");
        qpub.setPasswd("anonymous");
        try {
            qpub.initialize(servername);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuesender", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        try {
            qpub.sendMessage(messageSent);

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("queuesender", "Test_ComponentPortChange"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            qpub.close();
        }
    }

    @Test(enabled = false)
    public void createSubscriber(String serversName,String topicName) {

        sub.setDestinationName(topicName);
        sub.setCf("primaryCF");
        sub.setUser("anonymous");
        sub.setPasswd("anonymous");
        try {
            sub.initialize(serversName);
        } catch (Exception e) {
            Assert.fail("Failed to subscribe to topic", e);
        }

    }

    @Test(enabled = false)
    public String onMessage() {
        String message = null;
        try {
            message = sub.getMessageOnDestination();
        } catch (Exception e) {
            Assert.fail("Failed to fetch the message on topic", e);
        } finally {
            sub.close();
        }
        return message;
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



}
