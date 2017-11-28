package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.jms.Message;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: janardhan
 * Date: Jan 26, 2011
 * Time: 3:03:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestAppContext extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "APPCONTEXT";
    private SampleEventProcessListener epListener = null;
    public String messageSentfromfeeder;
    public String appContextonDisplay1;
    public String appContextonDisplay2;
    public Message messageonDisplay1;
    public Message messageonDisplay2;
    public String appContext;
    private String m_appGUID;
    private float m_version;
    private float appVersion = 1.0f;
    private FioranoApplicationController m_fioranoApplicationController;
    @Test(groups = "AppContext", alwaysRun = true)
    public void startSetup() {
        System.out.println("running startSetup of TestAppContext");
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group AppContext. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "AppContext", description = "AppContext bug 18941 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestLaunchOfAppContextApplication() {
        System.out.println("running TestLaunchofAppContextApplication");

        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfAppContextApplication", "TestAppContext"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfAppContextApplication", "TestAppContext"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfAppContextApplication", "TestAppContext"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfAppContextApplication", "TestAppContext"), e);
            Assert.fail("Failed to do ep listener!", e);
        }
        // testEnvConfig = ESBTestHarness.getTestEnvConfig();
        // String currentos=testEnvConfig.getProperty(TestEnvironmentConstants.MACHINE);


        try {
            deployEventProcess("appcontext-1.0@EnterpriseServer.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfAppContextApplication", "TestAppContext"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfAppContextApplication", "TestAppContext"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfAppContextApplication", "TestAppContext"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfAppContextApplication", "TestAppContext"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfAppContextApplication", "TestAppContext"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfAppContextApplication", "TestAppContext"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
        }

    }

    @Test(groups = "AppContext", description = "AppContext bug 18941 ", dependsOnMethods = "TestLaunchOfAppContextApplication", alwaysRun = true)
    public void TestSendMessage() {
        System.out.println("running TestSendMessage of TestAppContext");
        try {
            try {
                eventProcessManager.stopServiceInstance(appName,appVersion, "Display1");
                Thread.sleep(6000);
                eventProcessManager.stopServiceInstance(appName,appVersion, "Display2");
                Thread.sleep(6000);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestAppContext"), e);
                Assert.fail("Failed to do operation on service instance!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestAppContext"), e);
                Assert.fail("Failed to do operation on service instance!", e);
            } catch (InterruptedException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestAppContext"), e);
                Assert.fail("Failed to do operation on service instance!", e);
            }
            // messageSentfromfeeder = topicpublisher("fps", appName + "__" + "feeder1" + "__OUT_PORT");
            // System.out.println("messageSentfromfeeder is " + messageSentfromfeeder);
            // appContextonDisplay1=queuereceiver("fps", appName + "__" + "display1" + "__IN_PORT", messageSentfromfeeder);
            //messageonDisplay1=queuereceiver("fps", appName + "__" + "display1" + "__IN_PORT", messageSentfromfeeder);
            createPubSub1();
            System.out.println("messageSentfromfeeder is " + messageSentfromfeeder);
            appContextonDisplay1 = ((fiorano.esb.util.CarryForwardContext) messageonDisplay1.getObjectProperty("ESBX__SYSTEM__CARRY_FORWARD_CONTEXT")).getAppContext();
            try {
                eventProcessManager.startServiceInstance(appName,appVersion, "Display1");
                Thread.sleep(10000);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestAppContext"), e);
                Assert.fail("Failed to do operation on service instance!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestAppContext"), e);
                Assert.fail("Failed to do operation on service instance!", e);
            } catch (InterruptedException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestAppContext"), e);
                Assert.fail("Failed to do operation on service instance!", e);
            }
            // topicpublisher("fps", appName + "__" + "display1" + "__OUT_PORT");
            // messageonDisplay2=queuereceiver("fps", appName + "__" + "display2" + "__IN_PORT", messageSentfromfeeder);
            // appContextonDisplay2=queuereceiver("fps", appName + "__" + "display2" + "__IN_PORT", messageSentfromfeeder);
            createPubSub2();
            appContextonDisplay2 = ((fiorano.esb.util.CarryForwardContext) messageonDisplay2.getObjectProperty("ESBX__SYSTEM__CARRY_FORWARD_CONTEXT")).getAppContext();
        } catch (Exception e) {

            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSendMessage", "TestAppContext"), e);
            Assert.fail("Failed to send message from feeder to display1 and display2", e);

        }


    }

    @Test(groups = "AppContext", description = "AppContext bug 18941 ", dependsOnMethods = "TestSendMessage", alwaysRun = true)
    public void TestAppContextValue() {
        System.out.println("running TestAppContextValue");
        System.out.println("appContextonDisplay1 is " + appContextonDisplay1);
        System.out.println("appContextonDisplay2 is " + appContextonDisplay2);
        try {
            if (appContextonDisplay1 == null && appContextonDisplay2 == null && appContextonDisplay1.equals(appContextonDisplay2)) {
                throw new Exception("application context is same on display1 and display2");
            }
        } catch (Exception e) {

            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAppContextValue", "TestAppContext"), e);
            Assert.fail("Failed because application context is same on display1 and display2", e);
        }

    }

    @Test(groups = "AppContext", description = "AppContext bug 18941 ", dependsOnMethods = "TestAppContextValue", alwaysRun = true)
    public void stopAndDeleteApplication() {
        System.out.println("running stopAndDeleteApplication of TestAppContext");
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestAppContext"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestAppContext"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + "/esb/TestNG/resources/" + zipName));
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

    /**
     * Method to create FMQ pub/sub
     */
    @Test(enabled = false)
    public void createPubSub1() {

        Publisher pub = new Publisher();
        pub.setDestinationName(appName + "__" + "1_0" + "__" + "feeder1" + "__OUT_PORT");
        pub.setCf(appName + "__" + "1_0" + "__" + "feeder1");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        messageSentfromfeeder = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageSentfromfeeder);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

        Receiver rec = new Receiver();
        rec.setDestinationName(appName + "__" + "1_0" + "__" + "Display1" + "__IN_PORT");
        rec.setCf(appName + "__" + "1_0" + "__" + "Display1");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        //String messageOnDestination = null;
        try {
            messageonDisplay1 = rec.getMessage();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        // Assert.assertEquals(messageSent, messageOnDestination);
    }

    @Test(enabled = false)
    public void createPubSub2() {

        Publisher pub = new Publisher();
        pub.setDestinationName(appName + "__" + "1_0" + "__" + "Display1" + "__OUT_PORT");
        pub.setCf(appName + "__" + "1_0" + "__" + "Display1");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        // String messageSent = "Fiorano" + System.currentTimeMillis();
        try {
            pub.sendMessage(messageonDisplay1);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

        Receiver rec = new Receiver();
        rec.setDestinationName(appName + "__" + "1_0" + "__" + "Display2" + "__IN_PORT");
        rec.setCf(appName + "__" + "1_0" + "__" + "Display2");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize("fps");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        //String messageOnDestination = null;
        try {
            messageonDisplay2 = rec.getMessage();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        // Assert.assertEquals(messageSent, messageOnDestination);
    }


}
