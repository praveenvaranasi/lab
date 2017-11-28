package com.fiorano.esb.testng.rmi.sanity;

import com.fiorano.esb.server.api.BreakpointMetaData;
import com.fiorano.esb.server.api.IDebugger;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.bugs.TestConstants;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Feb 23, 2010
 * Time: 5:40:18 PM
 * To change this template use File | Settings | File Templates.
 */
//@Test(groups = {"IDebug"}, dependsOnGroups = {"IServiceProvider"})
@Test(dependsOnGroups = "TWO_IServiceManager.*")
public class TestIDebugger extends AbstractTestNG {
    public IDebugger debugger;
    public IEventProcessManager eventProcessManager;
    public static Hashtable<String, String> resultMap = new Hashtable<String, String>();
    private String appGUID = "SIMPLECHAT2";
    private String messageSent;
    private float appVersion = 1.0f;
    @Test(groups = {"IDebugger"}, alwaysRun = true)
    public void start_IDebugger() {
        Logger.LogMethodOrder(Logger.getOrderMessage("start_IDebugger", "TestIDebugger"));
        debugger = TestIRmiManager.getdebugger();
        eventProcessManager = TestIRmiManager.geteventProcessManager();
        Logger.Log(Level.INFO, Logger.getFinishMessage("start_IDebugger", "TestIDebugger"));
    }

    /**
     * Method to create FMQ pub
     */
    @Test(enabled = false)
    public void createPub(String topicOrQueueName) {

        Publisher pub = new Publisher();
        pub.setDestinationName(topicOrQueueName);
        pub.setCf("primaryTCF");
        pub.setUser("anonymous");
        pub.setPasswd("anonymous");
        try {
            pub.initialize("fps");
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do create publisher to outport", e);
        }
        try {
            pub.sendMessage(messageSent);
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do publish message on outport", e);
        } finally {
            pub.close();
        }

    }

    /**
     * Method to create FMQ sub
     */
    @Test(enabled = false)
    public void createQueueSub(String queueName, String serverName) {

        Receiver rec = new Receiver();
        rec.setDestinationName(queueName);
        rec.setCf("primaryQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize(serverName);
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }
        String messageOnDestination = null;
        try {
            messageOnDestination = rec.getMessageOnDestination();
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do get message from inport", e);
        } finally {
            rec.close();
        }

        Assert.assertEquals(messageSent, messageOnDestination);
        messageSent = messageOnDestination;
    }

    /**
     * Method to create FMQ sub
     */
    @Test(enabled = false)
    public void createQueuePub(String queueName, String serverName) {

        Publisher rec = new Publisher();
        rec.setDestinationName(queueName);
        rec.setCf("primaryQCF");
        rec.setUser("anonymous");
        rec.setPasswd("anonymous");
        try {
            rec.initialize(serverName);
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to create sender to destination " + queueName, e);
        }
        String messageOnDestination = null;
        try {
            rec.sendMessage(messageSent);
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do send message to destination " + queueName, e);
        } finally {
            rec.close();
        }
    }


    @Test(groups = {"IDebugger"}, dependsOnMethods = "start_IDebugger", alwaysRun = true)
    public void TestaddBreakpoint() {
        try {
            // adds a break point to a given application and a given route in it
            Logger.LogMethodOrder(Logger.getOrderMessage("TestaddBreakpoint", "TestIDebugger"));

            TestIEventProcessManagerListener epListener = null;

            try {
                epListener = new TestIEventProcessManagerListener(appGUID);
            } catch (RemoteException e1) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e1);
                Assert.fail("Failed to create ep listener!", e1);
            }

            try {
                eventProcessManager.addEventProcessListener(epListener);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
                Assert.fail("Failed to add ep listener!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
                Assert.fail("Failed to do ep listener!", e);
            }


            try {
                deployEventProcess("simplechat2-1.0@EnterpriseServer.zip");
            } catch (IOException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
                Assert.fail("Failed to do SAVE!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
                Assert.fail("Failed to do SAVE!", e);
            }

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
            }

            try {
                eventProcessManager.checkResourcesAndConnectivity(appGUID, 1.0f);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
                Assert.fail("Failed to do crc!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
                Assert.fail("Failed to do crc!", e);
            }
            resultMap.clear();
            eventProcessManager.startEventProcess(appGUID, 1.0f, false);
            Thread.sleep(Thread_Sleep_Time);

            eventProcessManager.startAllServices(appGUID, 1.0f);
            Thread.sleep(Thread_Sleep_Time);

            BreakpointMetaData bpmd1 = debugger.addBreakpoint(appGUID,appVersion, "route1");
            Assert.assertNotNull(bpmd1.getSourceQueueName());
            messageSent = "Fiorano" + System.currentTimeMillis();
            createPub("SIMPLECHAT2__1_0__CHAT1__OUT_PORT");

            eventProcessManager.stopServiceInstance(appGUID, appVersion ,"chat2");
            Thread.sleep(3000);
            //debugger.removeBreakpoint("SIMPLECHAT", "route1");
            BreakpointMetaData bpmd2 = debugger.addBreakpoint(appGUID,appVersion, "route2");
            Assert.assertNotNull(bpmd2.getSourceQueueName());

            createQueueSub("SIMPLECHAT2__1_0__ROUTE1__C", "fes");
            Thread.sleep(3000);

            createQueuePub("SIMPLECHAT2__1_0__ROUTE1__D", "fes");
            Thread.sleep(3000);

            createQueueSub("SIMPLECHAT2__1_0__CHAT2__IN_PORT", "fps");
            Thread.sleep(3000);

            eventProcessManager.startServiceInstance(appGUID,appVersion, "chat2");
            Thread.sleep(3000);

            //Thread.sleep(4000);
            int count = Integer.parseInt(resultMap.get(TestConstants.ROUTEBREAKPOINT_ADD_SUCCESSFUL));
            Assert.assertEquals(count, 2);
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestaddBreakpoint", "TestIDebugger"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddBreakpoint", "TestIDebugger"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddBreakpoint", "TestIDebugger"), e);
            Assert.fail();

        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddBreakpoint", "TestIDebugger"), e);
            Assert.fail();

        }
    }

    @Test(groups = {"IDebugger"}, dependsOnMethods = {"TestaddBreakpoint"}, alwaysRun = true)
    public void TestgetBreakpointMetaData() {
        try {
            // gets the breakpoint meta data of a given application and of a given route. It returns breakpointmetadata reference
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetBreakpointMetaData", "TestIDebugger"));
            BreakpointMetaData bpmd3 = debugger.getBreakpointMetaData(appGUID,appVersion, "route1");
            Assert.assertNotNull(bpmd3.getSourceQueueName());
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetBreakpointMetaData", "TestIDebugger"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetBreakpointMetaData", "TestIDebugger"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetBreakpointMetaData", "TestIDebugger"), e);
            Assert.fail();

        }
    }

    @Test(groups = {"IDebugger"}, dependsOnMethods = {"TestgetBreakpointMetaData"}, alwaysRun = true)
    public void TestgetRoutesWithDebugger() {
        try {
            // returns a string array which contains all the routes with debugger of a given application
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetRoutesWithDebugger", "TestIDebugger"));
            String[] route = debugger.getRoutesWithDebugger(appGUID,appVersion);
            Assert.assertTrue(route.length > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetRoutesWithDebugger", "TestIDebugger"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetRoutesWithDebugger", "TestIDebugger"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetRoutesWithDebugger", "TestIDebugger"), e);
            Assert.fail();

        }
    }

    @Test(groups = {"IDebugger"}, dependsOnMethods = {"TestgetRoutesWithDebugger"}, alwaysRun = true)
    public void TestremoveBreakpoint() {
        try {
            // removes the break point from the given application and of the given route. If the route given is not there it will throw an excpetion
            Logger.LogMethodOrder(Logger.getOrderMessage("TestremoveBreakpoint", "TestIDebugger"));
            resultMap.clear();
            debugger.removeBreakpoint(appGUID,appVersion, "route1");
            debugger.removeBreakpoint(appGUID,appVersion, "route2");
            try {
                Thread.sleep(Thread_Sleep_Time);

            } catch (InterruptedException e) {
                e.printStackTrace();
                Assert.fail();

            }
            int count = Integer.parseInt(resultMap.get(TestConstants.ROUTEBREAKPOINT_REMOVE_SUCCESSFUL));
            Assert.assertEquals(count, 2);
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestremoveBreakpoint", "TestIDebugger"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveBreakpoint", "TestIDebugger"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveBreakpoint", "TestIDebugger"), e);
            Assert.fail();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveBreakpoint", "TestIDebugger"), e);
            Assert.fail();

        }
    }

    @Test(groups = {"IDebugger"}, dependsOnMethods = {"TestremoveBreakpoint"}, alwaysRun = true)
    public void TestremoveAllBreakpoints() {
        try { // removes all the breakpoints for a given application
            Logger.LogMethodOrder(Logger.getOrderMessage("TestremoveAllBreakpoints", "TestIDebugger"));

            // now no breakpoints are there
            resultMap.clear();
            debugger.addBreakpoint(appGUID,appVersion, "route1");
            debugger.addBreakpoint(appGUID,appVersion, "route2");
            try {
                Thread.sleep(Thread_Sleep_Time);

            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            debugger.removeAllBreakpoints(appGUID,appVersion);
            try {
                Thread.sleep(Thread_Sleep_Time);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            String[] route2 = debugger.getRoutesWithDebugger(appGUID,appVersion);
            Assert.assertTrue(route2.length == 0);
            try {
                Thread.sleep(Thread_Sleep_Time);

            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            int count = Integer.parseInt(resultMap.get(TestConstants.ROUTEBREAKPOINT_REMOVE_SUCCESSFUL));

            Assert.assertEquals(count, 2);

            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestremoveAllBreakpoints", "TestIDebugger"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveAllBreakpoints", "TestIDebugger"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveAllBreakpoints", "TestIDebugger"), e);
            Assert.fail();

        }
    }

    @Test(groups = {"TWO_IDebugger"}, dependsOnGroups = {"IDebugger.*"}, alwaysRun = true)
    public void lastoperation_IDebugger() {
        try {
            // this method will stops the event process of a given application
            Logger.LogMethodOrder(Logger.getOrderMessage("lastoperation_IDebugger", "TestIDebugger"));
            eventProcessManager.stopEventProcess(appGUID, 1.0f);
            try {
                Thread.sleep(Thread_Sleep_Time);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eventProcessManager.deleteEventProcess(appGUID, 1.0f, true, false);
            Logger.Log(Level.INFO, Logger.getFinishMessage("lastoperation_IDebugger", "TestIDebugger"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("lastoperation_IDebugger", "TestIDebugger"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("lastoperation_IDebugger", "TestIDebugger"), e);
            Assert.fail();

        }
    }

    @Test(enabled = false)
    public static synchronized void insertIntoResultMap(String key, String value) {
        resultMap.put(key, value);
    }

    @Test(enabled = false)
    public static synchronized boolean containsKey(String key) {
        return resultMap.containsKey(key);
    }

    @Test(enabled = false)
    public static synchronized String getValue(String key) {
        return resultMap.get(key);
    }

    /**
     * deploys an event process which is located under $testing_home/esb/TestNG/resources/
     *
     * @param zipName - name of the event process zip located under $testing_home/esb/TestNG/resources/
     * @throws java.io.IOException - if file is not found or for any other IO error
     * @throws ServiceException
     */
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


}
