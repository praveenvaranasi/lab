package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.MachineElement;
import com.fiorano.stf.test.core.ProfileElement;
import com.fiorano.stf.test.core.TestElement;
import com.fiorano.stf.test.core.TestEnvironment;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/6/11
 * Time: 5:53 PM
 * Sample Test Class to show basic HA test scenarios,
        Assumptions : Machine1 & Machine2 are as in testenv.xml
                      Machine3 is the gateway machine as in testenv.xml;
                      Servers have started & become active/passive


    test1 : Machine1 goes down & comes back
    test2 : Machine2 goes down & comes back
    test3 : hafes/active goes down & comes back
    test4 : hafes/passive goes down & comes back
    test5 : hafps/active goes down & comes back
    test6 : hafps/passive goes down & comes back
    test7 : All actives down & come back
    test8 : All passives down & come back
    test9 : Machine3 goes down & comes back
    test10 : Total network break and comes back
 */
public class TestRMIHA extends AbstractTestNG{
    private static IEventProcessManager eventProcessManager;
    private static IServiceProviderManager serviceProviderManager;
    private ServerStatusController serverStatusController;
    public static IFPSManager ifpsManager;
    private static final int FES_PRIMARY_RMI_PORT = 2047;
    private static final int FES_SECONDARY_RMI_PORT = 2048;
    private static final int FPS_PRIMARY_RMI_PORT = 2067;
    private static final int FPS_SECONDARY_RMI_PORT = 2068;

    private final int FES_HA_PRIMARY_PORT = 1747;
    private final int FES_HA_SECONDARY_PORT = 1748;
    private final int FPS_HA_PRIMARY_PORT = 1767;
    private final int FPS_HA_SECONDARY_PORT = 1768;

    private final int FES_PRIMARY_PORT = 1847;
    private final int FES_SECONDARY_PORT = 1848;
    private final int FPS_PRIMARY_PORT = 1867;
    private final int FPS_SECONDARY_PORT = 1868;

    private final static String HAFES = "hafes";
    private final static String HAFPS = "hafps";
    private final static String A = "ACTIVE";
    private final static String P = "PASSIVE";
    private final static String S = "STANDALONE";
    private final static String W = "WAITING";
    private String appname = "HATESTEVENT";
    private float appversion = 1.0f;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();


    IRmiManager rmiManager;
    String handleID;
    String resourceFilePath;
    ExecutionController executionController;

    String primaryFESAddress;
    String secondaryFESAddress;
    String primaryFPSAddress;
    String secondaryFPSAddress;

    static volatile boolean active;
    private TestElement testCaseConfig;


    @Test(groups = "RMIHATests", alwaysRun = true)
    void startSetUp() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        initializeProperties("HA" + fsc + "RMIHATest" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "HA" + fsc + "RMIHATest";

        try {
            serverStatusController = ServerStatusController.getInstance();
            executionController = ExecutionController.getInstance();
            primaryFESAddress = serverStatusController.getPrimaryFESAddress();
            secondaryFESAddress = serverStatusController.getSecondaryFESAddress();
            primaryFPSAddress = serverStatusController.getPrimaryFPSAddress();
            secondaryFPSAddress = serverStatusController.getSecondaryFPSAddress();

            waitTillServersUp();
            getManagers();
            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
        }
    }

    @Test(groups = "RMIHATests", dependsOnMethods = "startSetUp", alwaysRun = true)
    public void test1_Machine1DownAndBack() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        String machine1Address = serverStatusController.getPrimaryFESAddress();
        try {
            machineDownAndBack(machine1Address);
            System.out.println("Machine1DownAndBack Passed!!!");
            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));
        } catch (Exception e) {
            System.out.println("Machine1DownAndBack Failed");
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test1_Machine1DownAndBack")
    public void test2_Machine2DownAndBack() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        String machine2Address = serverStatusController.getSecondaryFESAddress();
        try {
            machineDownAndBack(machine2Address);
            System.out.println("Machine2DownAndBack Passed!!!");
            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));
        } catch (Exception e) {
            System.out.println("Machine2DownAndBack Failed");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
        }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test2_Machine2DownAndBack")
    public void test3_FESFailOverFindFPSAndBack() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        try {
            ServerFactory initialActiveFES = ServerFactory.getServer("fes", "active", HAFES);

            Vector<String> fpslist = ifpsManager.getAllPeersInRepository();
            serviceProviderManager.shutdownServer();
            getManagers();
            boolean pass = true;
            Thread.sleep(30000);
            Vector fpslist2 = ifpsManager.getAllPeersInRepository();
            for (String piece : fpslist) {
                if (fpslist2.contains(piece))
                    pass = true;
                else {
                    pass = false;
                    break;
                }
            }
            Assert.assertEquals(pass, true);

            executionController.startServerOnRemote(initialActiveFES.getAddress(), "fes", initialActiveFES.getProfileName(), initialActiveFES.isWrapper(), true);
            System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" passed!! going in sleep of 6 min");
            Thread.sleep(360000);

            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));

        }
        
        catch (Exception e) {
            Assert.fail("TestCase Failed because of " + e.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
        }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test3_FESFailOverFindFPSAndBack")
    public void test3_FESFailOverDeployAndBack() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        try {
            SampleEventProcessListener ep = new SampleEventProcessListener(appname,eventStore);
            eventProcessManager.addEventProcessListener(ep);
            ServerFactory initialActiveFES = ServerFactory.getServer("fes", "active", HAFES);

            String zipFile = resourceFilePath + File.separator + testProperties.getProperty("zipFile2");
            String guid = testProperties.getProperty("guid2");
            deployEventProcess(new File(zipFile));
            serviceProviderManager.shutdownServer();
            getManagers();
            Thread.sleep(10000);
            Assert.assertTrue(eventProcessManager.exists(guid, 1.0f));

            executionController.startServerOnRemote(initialActiveFES.getAddress(), "fes", initialActiveFES.getProfileName(), initialActiveFES.isWrapper(), true);
            System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" passed!! going in sleep of 6 min");
            Thread.sleep(360000);
            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
            Assert.fail("TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test3_FESFailOverDeployAndBack")
    public void test4_FESPassiveDownAndBack() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        try {
            ServerFactory initialPassiveFES = ServerFactory.getServer("fes", "passive", HAFES);
            ServerFactory initialActiveFES = ServerFactory.getServer("fes", "active", HAFES);

            executionController.stopFESHAProfile(initialPassiveFES.getAddress(), initialPassiveFES.getProfileName(), initialPassiveFES.isWrapper());
            Thread.sleep(30000);
            String activeStatus = serverStatusController.getServerStatus(initialActiveFES.getAddress(), initialActiveFES.getPort());

            executionController.startServerOnRemote(initialPassiveFES.getAddress(), "fes", initialPassiveFES.getProfileName(), initialPassiveFES.isWrapper(), true);
            Thread.sleep(360000);
            String activeStatus2 = serverStatusController.getServerStatus(initialActiveFES.getAddress(), initialActiveFES.getPort());

            if (activeStatus.equalsIgnoreCase(S) && activeStatus2.equalsIgnoreCase(A)){
                Assert.assertTrue(true);
                System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" passed!!");
            }
            else {
                Assert.assertTrue(false);
                System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" FAILED!!");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
            Assert.fail("TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test4_FESPassiveDownAndBack")
    public void test5_FPSFailOver() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        try {

            IServerListener fps_listener = new IServerListener();
            ifpsManager.addListener(fps_listener);
            ServerFactory initialActiveFPS = ServerFactory.getServer("fps", "active", HAFPS);
            ServerFactory initialPassiveFPS = ServerFactory.getServer("fps", "passive", HAFPS);
            System.out.println("Before test:");
            printParams();
            ifpsManager.shutdownPeer(HAFPS);
            Thread.sleep(10000);
            System.setProperty("FIORANO_RMI_CALL_TIMEOUT", "300000");
            while (!active)
                Thread.sleep(60000);
            System.out.println("FPS failover");
            printParams();
            Assert.assertEquals(serverStatusController.getServerStatus(initialPassiveFPS.getAddress(), initialPassiveFPS.getPort()), S);

            //asserts that initially passive server is now standalone
            System.out.println("Start FPS");
            executionController.startServerOnRemote(initialActiveFPS.getAddress(), "fps", initialActiveFPS.getProfileName(), initialActiveFPS.isWrapper(), true);
            Thread.sleep(360000);
            Assert.assertEquals(serverStatusController.getServerStatus(initialActiveFPS.getAddress(), initialActiveFPS.getPort()), P);
            Assert.assertEquals(serverStatusController.getServerStatus(initialPassiveFPS.getAddress(), initialPassiveFPS.getPort()), A);
            printParams();
            System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" passed!!");
            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
            Assert.fail("TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test5_FPSFailOver")
    public void test6_FPSPassiveDownAndBack() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        try {
            ServerFactory initialPassiveFPS = ServerFactory.getServer("fps", "passive", HAFPS);
            ServerFactory initialActiveFPS = ServerFactory.getServer("fps", "active", HAFPS);

            executionController.stopFPSHAProfile(initialPassiveFPS.getAddress(), initialPassiveFPS.getProfileName(), initialPassiveFPS.isWrapper());
            Thread.sleep(30000);
            String activeStatus = serverStatusController.getServerStatus(initialActiveFPS.getAddress(), initialActiveFPS.getPort());

            executionController.startServerOnRemote(initialPassiveFPS.getAddress(), "fps", initialPassiveFPS.getProfileName(), initialPassiveFPS.isWrapper(), true);
            Thread.sleep(360000);
            String activeStatus2 = serverStatusController.getServerStatus(initialActiveFPS.getAddress(), initialActiveFPS.getPort());

            if (activeStatus.equalsIgnoreCase(S) && activeStatus2.equalsIgnoreCase(A)){
                Assert.assertTrue(true);
                System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" passed!!");
            }
            else{
                Assert.assertTrue(false);
                System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" FAILED!!");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
            Assert.fail("TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test6_FPSPassiveDownAndBack")
    public void test7_StopActives() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        try {
            IServerListener fps_listener = new IServerListener();
            ifpsManager.addListener(fps_listener);
            ServerFactory initialPassiveFES = ServerFactory.getServer("fes", "passive", HAFES);
            ServerFactory initialPassiveFPS = ServerFactory.getServer("fps", "passive", HAFPS);
            ServerFactory initialActiveFES = ServerFactory.getServer("fes", "active", HAFES);
            ServerFactory initialActiveFPS = ServerFactory.getServer("fps", "active", HAFPS);
            ifpsManager.shutdownPeer(HAFPS);
            serviceProviderManager.shutdownServer();
            printParams();
            getManagers();
            Thread.sleep(30000);
            Assert.assertEquals(serverStatusController.getServerStatus(initialPassiveFES.getAddress(), initialPassiveFES.getPort()), S);
            Assert.assertEquals(serverStatusController.getServerStatus(initialPassiveFPS.getAddress(), initialPassiveFPS.getPort()), S);

            executionController.startServerOnRemote(initialActiveFPS.getAddress(), "fps", initialActiveFPS.getProfileName(), initialActiveFPS.isWrapper(), true);
            executionController.startServerOnRemote(initialActiveFES.getAddress(), "fes", initialActiveFES.getProfileName(), initialActiveFES.isWrapper(), true);
            Thread.sleep(360000);

            System.out.println("Servers up again");
            printParams();
            Assert.assertEquals(serverStatusController.getServerStatus(initialPassiveFES.getAddress(), initialPassiveFES.getPort()), A);
            Assert.assertEquals(serverStatusController.getServerStatus(initialPassiveFPS.getAddress(), initialPassiveFPS.getPort()), A);
            System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" passed!!");
            //start the servers that were recently shut down

            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
            Assert.fail("TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test7_StopActives")
    public void test8_StopPassives() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        try {

            ServerFactory initialPassiveFES = ServerFactory.getServer("fes", "passive", HAFES);
            ServerFactory initialPassiveFPS = ServerFactory.getServer("fps", "passive", HAFPS);
            ServerFactory initialActiveFES = ServerFactory.getServer("fes", "active", HAFES);
            ServerFactory initialActiveFPS = ServerFactory.getServer("fps", "active", HAFPS);

            executionController.stopFESHAProfile(initialPassiveFES.getAddress(), initialPassiveFES.getProfileName(), initialPassiveFES.isWrapper());
            executionController.stopFPSHAProfile(initialPassiveFPS.getAddress(), initialPassiveFPS.getProfileName(), initialPassiveFPS.isWrapper());
            Thread.sleep(30000);
            printParams();
            Assert.assertEquals(serverStatusController.getServerStatus(initialActiveFES.getAddress(), initialActiveFES.getPort()), S);
            Assert.assertEquals(serverStatusController.getServerStatus(initialActiveFPS.getAddress(), initialActiveFPS.getPort()), S);

            System.out.println("Servers up again");
            //start the servers that were recently shut down
            executionController.startServerOnRemote(initialPassiveFPS.getAddress(), "fps", initialPassiveFPS.getProfileName(), initialPassiveFPS.isWrapper(), true);
            executionController.startServerOnRemote(initialPassiveFES.getAddress(), "fes", initialPassiveFES.getProfileName(), initialPassiveFES.isWrapper(), true);
            System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" passed!! going in sleep of 6 min");
            Thread.sleep(360000);
            printParams();
            Assert.assertEquals(serverStatusController.getServerStatus(initialActiveFES.getAddress(), initialActiveFES.getPort()), A);
            Assert.assertEquals(serverStatusController.getServerStatus(initialActiveFPS.getAddress(), initialActiveFPS.getPort()), A);

            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
            Assert.fail("TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test8_StopPassives")
    public void test9_GatewayDown() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));

        ServerFactory initialPassiveFES = ServerFactory.getServer("fes", "passive", HAFES);
        ServerFactory initialActiveFES = ServerFactory.getServer("fes", "active", HAFES);
        try {
            Map<String, String> addresses = new HashMap<String, String>();
            addresses.put(primaryFESAddress, "add");
            addresses.put(secondaryFESAddress, "add");
            addresses.put(primaryFPSAddress, "add");
            addresses.put(secondaryFPSAddress, "add");
            printParams();
            for (String address : addresses.keySet())
                breakGatewayConnection(address, serverLogic(address), true);

            try {
                ifpsManager.restartPeer(HAFPS);
                serviceProviderManager.restartServer();
            } catch (UnmarshalException ignored) {
            }

            Thread.sleep(360000);      //Thread.sleep 360000
            printParams();
            Assert.assertNull(serverStatusController.getServerStatus(initialActiveFES.getAddress(), initialActiveFES.getPort()));
            Assert.assertEquals(serverStatusController.getServerStatus(initialPassiveFES.getAddress(), initialPassiveFES.getPort()), P);

            flush();

            executionController.startServerOnRemote(initialActiveFES.getAddress(), "fes", initialActiveFES.getProfileName(), initialActiveFES.isWrapper(), true);
            Thread.sleep(360000);
            printParams();
            Assert.assertTrue(serverStatusController.getServerStatus(initialActiveFES.getAddress(), initialActiveFES.getPort()).equals(P) || serverStatusController.getServerStatus(initialActiveFES.getAddress(), initialActiveFES.getPort()).equals(A));
            System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" passed!!");
            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));


        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
            Assert.fail("TestCase Failed because of " + e.getMessage());
        }
        finally {
            flush();
    }
    }

    @Test(groups = "RMIHATests", alwaysRun = true, dependsOnMethods = "test9_GatewayDown")
    public void test10_NetworkDown() {
        System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA"));
        Logger.Log(Level.INFO, Logger.getOrderMessage(getOnlyMethodName(), "testRMIHA"));
        try {
            ServerFactory initialActiveFES = ServerFactory.getServer("fes", "active", HAFES);
            ServerFactory initialActiveFPS = ServerFactory.getServer("fps", "active", HAFPS);

            Map<String, String> addresses = new HashMap<String, String>();
            addresses.put(primaryFESAddress, "add");
            addresses.put(secondaryFESAddress, "add");
            addresses.put(primaryFPSAddress, "add");
            addresses.put(secondaryFPSAddress, "add");
            printParams();
            IServerListener fps_listener = new IServerListener();
            ifpsManager.addListener(fps_listener);

            for (String address : addresses.keySet())
                breakGatewayConnection(address, serverLogic(address), true);
            breakHAFESConnection(true);
            breakHAFPSConnection(true);
            breakConnBWFesAndFps(true);

            Thread.sleep(30000);
            printParams();
            Assert.assertEquals(serverStatusController.getServerStatus(initialActiveFES.getAddress(), initialActiveFES.getPort()), W);
            Assert.assertEquals(serverStatusController.getServerStatus(initialActiveFPS.getAddress(), initialActiveFPS.getPort()), W);

            flush();
            Thread.sleep(60000);
            printParams();
            Assert.assertEquals(serverStatusController.getServerStatus(initialActiveFES.getAddress(), initialActiveFES.getPort()), A);
            Assert.assertEquals(serverStatusController.getServerStatus(initialActiveFPS.getAddress(), initialActiveFPS.getPort()), A);
            System.out.println(Logger.getExecuteMessage(getOnlyMethodName(),"testRMIHA")+" passed!!");


            Logger.Log(Level.INFO, Logger.getFinishMessage(getOnlyMethodName(), "testRMIHA"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage(getOnlyMethodName(), "testRMIHA"));
            Assert.fail("TestCase Failed because of " + e.getMessage());
        }
        finally {
            flush();
        }
    }

    @AfterClass(groups = "RMIHATests", alwaysRun = true)
    void tearDown() throws Exception {

        flush();

    }




    //definitions of auxiliary functions

    private void waitTillServersUp() throws Exception {

        ServerFactory primaryFES = ServerFactory.getServer("fes", "primary", HAFES);
        ServerFactory secondaryFES = ServerFactory.getServer("fes", "secondary", HAFES);
        ServerFactory primaryFPS = ServerFactory.getServer("fps", "primary", HAFPS);
        ServerFactory secondaryFPS = ServerFactory.getServer("fps", "secondary", HAFPS);

        if (serverStatusController.getServerStatus(primaryFES.getAddress(), primaryFES.getPort()) == null) {
            executionController.startServerOnRemote(primaryFES.getAddress(), "fes", primaryFES.getProfileName(), primaryFES.isWrapper(), true);
            Thread.sleep(360000);
        }
        if (serverStatusController.getServerStatus(secondaryFES.getAddress(), secondaryFES.getPort()) == null) {
            executionController.startServerOnRemote(secondaryFES.getAddress(), "fes", secondaryFES.getProfileName(), secondaryFES.isWrapper(), true);
            Thread.sleep(360000);
        }
        if (serverStatusController.getServerStatus(primaryFPS.getAddress(), primaryFPS.getPort()) == null){
            executionController.startServerOnRemote(primaryFPS.getAddress(), "fps", primaryFPS.getProfileName(), primaryFPS.isWrapper(), true);
            Thread.sleep(360000);
        }
        if (serverStatusController.getServerStatus(secondaryFPS.getAddress(), secondaryFPS.getPort()) == null){
            executionController.startServerOnRemote(secondaryFPS.getAddress(), "fps", secondaryFPS.getProfileName(), secondaryFPS.isWrapper(), true);
            Thread.sleep(360000);
        }
        while (!((serverStatusController.getServerStatus(primaryFES.getAddress(), primaryFES.getPort()).equals(P)) || serverStatusController.getServerStatus(primaryFES.getAddress(), primaryFES.getPort()).equals(A))) {
            Thread.sleep(5000);
        }
        while (!((serverStatusController.getServerStatus(primaryFPS.getAddress(), primaryFPS.getPort()).equals(P)) || serverStatusController.getServerStatus(primaryFPS.getAddress(), primaryFPS.getPort()).equals(A))) {
            Thread.sleep(5000);
        }
    }

    private boolean getManagers() {

        try {
            String url = serverStatusController.getURLOnFESActive();
            String add = url.substring(url.lastIndexOf('/') + 1, url.lastIndexOf(':'));
            int port = Integer.parseInt(url.substring(url.lastIndexOf(':') + 1)) + 100;
            Registry registry = LocateRegistry.getRegistry(add, port);

            rmiManager = (IRmiManager) registry.lookup(IRmiManager.class.toString());


            handleID = rmiManager.login("admin", "passwd");
            eventProcessManager = rmiManager.getEventProcessManager(handleID);
            serviceProviderManager = rmiManager.getServiceProviderManager(handleID);
            ifpsManager = rmiManager.getFPSManager(handleID);

            return false;
        } catch (Exception e3) {
            try {
                Thread.sleep(60000);
                getManagers();
            } catch (InterruptedException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        return false;
    }

    private String getMachineAddress(String machineName) {
        TestEnvironment testEnvironment = serverStatusController.getTestEnvironment();
        MachineElement machineElement = testEnvironment.getMachine(machineName);
        return machineElement.getAddress();
    }

    private void machineDownAndBack(String machineAddress) throws Exception {
        ServerFactory initialActiveFES = ServerFactory.getServer("fes", "active", HAFES);
        ServerFactory initialActiveFPS = ServerFactory.getServer("fps", "active", HAFPS);
        ServerFactory initialPassiveFES = ServerFactory.getServer("fes", "passive", HAFES);
        ServerFactory initialPassiveFPS = ServerFactory.getServer("fps", "passive", HAFPS);



        int serverLogic = serverLogic(machineAddress);
        int primarySecondaryActiveLogic = primarySecondaryActiveLogic();
        boolean states;

       /* states = checkBeforeStates(serverLogic, primarySecondaryActiveLogic,
                new ServerFactory[]{initialActiveFES, initialPassiveFES, initialActiveFPS, initialPassiveFPS});
        Assert.assertFalse(states);   */
        breakGatewayConnection(machineAddress, serverLogic, true);

        switch (serverLogic) {
            case 1000:
            case 111: {
                if (primarySecondaryActiveLogic == 10 || primarySecondaryActiveLogic == 11)
                    breakConnBWFesAndFps(true);
                breakHAFESConnection(true);
                break;
            }
            case 1001:
            case 110: {
                if (primarySecondaryActiveLogic == 11)
                    breakConnBWFesAndFps(true);
                breakHAFESConnection(true);
                breakHAFPSConnection(true);
                break;
            }
            case 1010:
            case 101: {
                breakHAFESConnection(true);
                breakHAFPSConnection(true);
                if (primarySecondaryActiveLogic == 1 || primarySecondaryActiveLogic == 10)
                    breakConnBWFesAndFps(true);
                break;
            }
            case 1011:
            case 100: {
                breakHAFESConnection(true);
                if (primarySecondaryActiveLogic == 1 || primarySecondaryActiveLogic == 0)
                    breakConnBWFesAndFps(true);
                break;
            }
            case 1100:
            case 11: {
                breakConnBWFesAndFps(true);
                break;
            }
            case 1101:
            case 10: {
                breakHAFPSConnection(true);
                if (primarySecondaryActiveLogic == 1 || primarySecondaryActiveLogic == 11)
                    breakConnBWFesAndFps(true);
                break;
            }
            case 1110:
            case 1:{
                breakHAFPSConnection(true);
                if (primarySecondaryActiveLogic == 10 || primarySecondaryActiveLogic == 0)
                    breakConnBWFesAndFps(true);
                break;
            }
            case 1111:
            case 0: {
                break;
            }
        }
        System.out.println("ALL connection are broken.");
        Thread.sleep(30000);


        breakGatewayConnection(machineAddress, serverLogic, false);
        switch (serverLogic) {
            case 1000:
            case 111: {
                if (primarySecondaryActiveLogic == 10 || primarySecondaryActiveLogic == 11)
                    breakConnBWFesAndFps(false);
                breakHAFESConnection(false);
                break;
            }
            case 1001:
            case 110: {
                if (primarySecondaryActiveLogic == 11)
                    breakConnBWFesAndFps(false);
                breakHAFESConnection(false);
                breakHAFPSConnection(false);
                break;
            }
            case 1010:
            case 101: {
                breakHAFESConnection(false);
                breakHAFPSConnection(false);
                if (primarySecondaryActiveLogic == 1 || primarySecondaryActiveLogic == 10)
                    breakConnBWFesAndFps(false);
                break;
            }
            case 1011:
            case 100: {
                breakHAFESConnection(false);
                if (primarySecondaryActiveLogic == 1 || primarySecondaryActiveLogic == 0)
                    breakConnBWFesAndFps(false);
                break;
            }
            case 1100:
            case 11: {
                breakConnBWFesAndFps(false);
                break;
            }
            case 1101:
            case 10: {
                breakHAFPSConnection(false);
                if (primarySecondaryActiveLogic == 1 || primarySecondaryActiveLogic == 11)
                    breakConnBWFesAndFps(false);
                break;
            }
            case 1110:
            case 1:{
                breakHAFPSConnection(false);
                if (primarySecondaryActiveLogic == 10 || primarySecondaryActiveLogic == 0)
                    breakConnBWFesAndFps(false);
                break;
            }
            case 1111:
            case 0: {
                break;
            }
        }
        System.out.println("ALL connection are up.");
        Thread.sleep(30000);

        /*states = checkStates(serverLogic, primarySecondaryActiveLogic,
                new ServerFactory[]{initialActiveFES, initialPassiveFES, initialActiveFPS, initialPassiveFPS});
        Assert.assertTrue(states);       */

    }

    private static void deployEventProcess(File eventProcessZipFile) throws IOException {
        boolean complete = false;
        byte[] contents;
        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(eventProcessZipFile));
        } catch (FileNotFoundException e) {
            throw new IOException("Could not find event process file for deployment to fes: " + eventProcessZipFile.getAbsolutePath());
        }

        try {
            /*Keep calling deployEventProcess until all the contents of the event process has been deployed */
            while (!complete) {
                byte[] tempContents = new byte[1024 * 40];
                int readCount = bis.read(tempContents);

                if (readCount < 0) {
                    complete = true;
                    readCount = 0;
                }
                contents = new byte[readCount];
                System.arraycopy(tempContents, 0, contents, 0, readCount);
                eventProcessManager.deployEventProcess(contents, complete);


            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Could not deploy event process file :" + eventProcessZipFile.getAbsolutePath());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new IOException("Could not deploy event process file :" + eventProcessZipFile.getAbsolutePath());
        } finally {
            bis.close();
        }

    }

    private void printParams() throws STFException {
        System.out.println("Primary FES:" + serverStatusController.getServerStatus(primaryFESAddress, FES_PRIMARY_RMI_PORT));
        System.out.println("Secondary FES:" + serverStatusController.getServerStatus(secondaryFESAddress, FES_SECONDARY_RMI_PORT));
        System.out.println("Primary FPS:" + serverStatusController.getServerStatus(primaryFPSAddress, FPS_PRIMARY_RMI_PORT));
        System.out.println("Secondary FPS:" + serverStatusController.getServerStatus(secondaryFPSAddress, FPS_SECONDARY_RMI_PORT));
    }

    private int serverLogic(String machineAddress) {
        int serverLogic = 0;

        if (machineAddress.equals(primaryFESAddress))
            serverLogic += 1000;
        if (machineAddress.equals(secondaryFESAddress))
            serverLogic += 100;
        if (machineAddress.equals(primaryFPSAddress))
            serverLogic += 10;
        if (machineAddress.equals(secondaryFPSAddress))
            serverLogic += 1;

        return serverLogic;
    }

    private void breakGatewayConnection(String machineAddress, int serverLogic, boolean breakOrAdd) throws Exception {

        if (serverLogic == 1000 || serverLogic == 100 || serverLogic == 1100) {
            breakFESGatewayConnection(machineAddress, breakOrAdd);
        } else if (serverLogic == 1 || serverLogic == 10 || serverLogic == 11) {
            breakFPSGatewayConnection(machineAddress, breakOrAdd);
        } else {
            if (serverStatusController.getFesLockFileMachineAddress().equals(serverStatusController.getFpsLockFileMachineAddress())) {
                breakFESGatewayConnection(machineAddress, breakOrAdd);
            } else {
                breakFESGatewayConnection(machineAddress, breakOrAdd);
                breakFPSGatewayConnection(machineAddress, breakOrAdd);
            }
        }
        Thread.sleep(30000);
    }

    private void breakHAFESConnection(boolean breakOrAdd) throws Exception {
        String rule;
        if(breakOrAdd)
            rule = "A";
        else
            rule = "D";
        executionController.modifyFireWallRule(primaryFESAddress, secondaryFESAddress, FES_HA_SECONDARY_PORT, FES_HA_PRIMARY_PORT, rule);
        executionController.modifyFireWallRule(secondaryFESAddress, primaryFESAddress, FES_HA_PRIMARY_PORT, FES_HA_SECONDARY_PORT, rule);
        Thread.sleep(5000);
    }

    private void breakHAFPSConnection(boolean breakOrAdd) throws Exception {
        String rule;
        if(breakOrAdd)
            rule = "A";
        else
            rule = "D";
        executionController.modifyFireWallRule(primaryFPSAddress, secondaryFPSAddress, FPS_HA_SECONDARY_PORT, FPS_HA_PRIMARY_PORT,rule );
        executionController.modifyFireWallRule(secondaryFPSAddress, primaryFPSAddress, FPS_HA_PRIMARY_PORT, FPS_HA_SECONDARY_PORT, rule);
        Thread.sleep(5000);
    }

    private void breakConnBWFesAndFps(boolean breakConn) throws Exception {

        String rule = null;
        if (breakConn)
            rule = "A";
        else rule = "D";

        if (isPrimaryActive(primaryFESAddress, "fes")) {
            if (isPrimaryActive(primaryFPSAddress, "fps")) {
                executionController.modifyFireWallRule(primaryFESAddress, primaryFPSAddress, FPS_PRIMARY_PORT, FES_PRIMARY_PORT, rule);
                executionController.modifyFireWallRule(primaryFPSAddress, primaryFESAddress, FES_PRIMARY_PORT, FPS_PRIMARY_PORT, rule);
            } else {
                executionController.modifyFireWallRule(primaryFESAddress, secondaryFPSAddress, FPS_SECONDARY_PORT, FES_PRIMARY_PORT, rule);
                executionController.modifyFireWallRule(secondaryFPSAddress, primaryFESAddress, FES_PRIMARY_PORT, FPS_SECONDARY_PORT, rule);
            }
        } else {
            if (isPrimaryActive(primaryFPSAddress, "fps")) {
                executionController.modifyFireWallRule(secondaryFESAddress, primaryFPSAddress, FPS_PRIMARY_PORT, FES_SECONDARY_PORT, rule);
                executionController.modifyFireWallRule(primaryFPSAddress, secondaryFESAddress, FES_SECONDARY_PORT, FPS_PRIMARY_PORT, rule);
            } else {
                executionController.modifyFireWallRule(secondaryFESAddress, secondaryFPSAddress, FPS_SECONDARY_PORT, FES_SECONDARY_PORT, rule);
                executionController.modifyFireWallRule(secondaryFPSAddress, secondaryFESAddress, FES_SECONDARY_PORT, FPS_SECONDARY_PORT, rule);
            }
        }
        Thread.sleep(5000);
    }

    private void breakFESGatewayConnection(String machineAddress, boolean breakOrAdd) throws Exception {
        String lockAddress = serverStatusController.getFesLockFileMachineAddress();
        String rule;
        if(breakOrAdd)
            rule = "A";
        else
            rule = "D";
        if (!machineAddress.equals(lockAddress))
            executionController.modifyGateway(machineAddress, lockAddress, rule);
    }

    private void breakFPSGatewayConnection(String machineAddress, boolean breakOrAdd) throws Exception {
        String lockAddress = serverStatusController.getFpsLockFileMachineAddress();
        String rule;
        if(breakOrAdd)
            rule = "A";
        else
            rule = "D";
        if (!machineAddress.equals(lockAddress))
            executionController.modifyGateway(machineAddress, lockAddress, rule);
    }

    private int primarySecondaryActiveLogic() {
        boolean isPrimaryFESActive = isPrimaryActive(primaryFESAddress, "fes");
        boolean isPrimaryFPSActive = isPrimaryActive(primaryFPSAddress, "fps");

        if (isPrimaryFESActive && isPrimaryFPSActive)
            return 11;
        else if (isPrimaryFESActive && !isPrimaryFPSActive)
            return 10;
        else if (!isPrimaryFESActive && isPrimaryFPSActive)
            return 1;
        else return 0;
    }

    private boolean isPrimaryActive(String primaryAddress, String type) {
        ServerFactory server = null;
        if (type.equalsIgnoreCase("FES")) {
            server = ServerFactory.getServer(type, "active", HAFES);
            if (primaryAddress.equals(server.getAddress()))
                return true;
            else return false;
        } else {
            server = ServerFactory.getServer(type, "active", HAFPS);
            if (primaryAddress.equals(server.getAddress()))
                return true;
            else return false;
        }
    }

    private boolean checkBeforeStates(int serverLogic, int activeLogic, ServerFactory[] serverData) throws Exception {
        String[] oldServerStatus = updateStatus(serverData);
        String aS = oldServerStatus[0];
        String pS = oldServerStatus[1];
        String aP = oldServerStatus[2];
        String pP = oldServerStatus[3];

        boolean passOrFail;

        String[] serverState = bitLogic(serverLogic,activeLogic,true);
        String[] peerState = bitLogic(serverLogic,activeLogic,false);

        passOrFail = aS.equalsIgnoreCase(serverState[0]) && pS.equalsIgnoreCase(serverState[1])
                && aP.equalsIgnoreCase(peerState[0]) && pP.equalsIgnoreCase(peerState[1]);


        Thread.sleep(60000);

        String[] newServerStatus = updateStatus(serverData);
        for (int i = 0; i < 4; i++) {
            if (oldServerStatus[i].equalsIgnoreCase(S))
                passOrFail = passOrFail && newServerStatus[i].equalsIgnoreCase(A);
            if (oldServerStatus[i].equalsIgnoreCase(W))
                passOrFail = passOrFail && newServerStatus[i].equalsIgnoreCase(P);
        }

        return passOrFail;
    }

    private String[] updateStatus(ServerFactory[] serverData) throws Exception {
        String[] serverStatuses = new String[4];

        serverStatuses[0] = serverStatusController.getServerStatus(serverData[0].getAddress(), serverData[0].getPort());
        serverStatuses[1] = serverStatusController.getServerStatus(serverData[1].getAddress(), serverData[1].getPort());
        serverStatuses[2] = serverStatusController.getServerStatus(serverData[2].getAddress(), serverData[2].getPort());
        serverStatuses[3] = serverStatusController.getServerStatus(serverData[3].getAddress(), serverData[3].getPort());

        return serverStatuses;

    }

    private String[] bitLogic(int serverLogic, int activeLogic, boolean serverOrPeer) {
        /*
        * 1 0 && 1 --> W S     // The first two bits of serverLogic correspond to fes
        * 0 1 && 0 --> W S     //  and last two correspond to fps
        * 1 0 && 0 --> S P     // First bit of activeLogic corresponds to fes
        * 0 1 && 1 --> S P     //  and last corresponds to fps
        *
        */

        String[] activePassive = new String[2];

        boolean SL1Bit = serverLogic / 1000 != 0;
        boolean SL2Bit = (serverLogic / 100) % 10 != 0;
        boolean SL3Bit = (serverLogic / 10) % 10 != 0;
        boolean SL4Bit = serverLogic % 10 != 0;

        boolean AL1Bit = activeLogic / 10 != 0;
        boolean AL2Bit = activeLogic % 10 != 0;

        boolean SLfirstBit, SLsecondBit, ALBit;
        if (serverOrPeer) {
            SLfirstBit = SL1Bit;
            SLsecondBit = SL2Bit;
            ALBit = AL1Bit;
        } else {
            SLfirstBit = SL3Bit;
            SLsecondBit = SL4Bit;
            ALBit = AL2Bit;
        }
        if ((SLfirstBit && !SLsecondBit && ALBit) || (!SLfirstBit && SLsecondBit && !ALBit)) {
            activePassive[0] = W;
            activePassive[1] = S;
        }
        if ((SLfirstBit && !SLsecondBit && !ALBit) || (!SLfirstBit && SLsecondBit && ALBit)) {
            activePassive[0] = S;
            activePassive[1] = P;
        }

        return activePassive;
    }

    private void flush() {
        executionController.flushFilters(primaryFESAddress);
        executionController.flushFilters(secondaryFESAddress);
        executionController.flushFilters(primaryFPSAddress);
        executionController.flushFilters(secondaryFPSAddress);
    }

    private class IServerListener extends UnicastRemoteObject implements Unreferenced, IServerStateListener {
        private IServerListener() throws RemoteException {
            super();
        }

        public void unreferenced() {
            System.out.println("FES down");
        }

        public void peerUnavailable(String fpsName) throws RemoteException {
            System.out.println("Peer down");
            active = false;
        }

        public void peerAvailable(String fpsName) throws RemoteException {
            System.out.println("Peer back up");
            active = true;

        }
    }

}


class ServerFactory {
    private static final String fps = "fps", fes = "fes";
    private static final String pri = "PRIMARY", sec = "SECONDARY";
    private static final String act = "active", pass = "passive", stand = "standalone";
    private static final int FES_PRIMARY_RMI_PORT = 2047;
    private static final int FES_SECONDARY_RMI_PORT = 2048;
    private static final int FPS_PRIMARY_RMI_PORT = 2067;
    private static final int FPS_SECONDARY_RMI_PORT = 2068;

    private boolean active;
    private String address;
    private int port;

    private boolean isWrapper;

    public boolean isWrapper() {
        return isWrapper;
    }

    public String getProfileName() {
        return profileName;
    }

    private String profileName;


    private String type;
    private static ServerStatusController serverStatusController;

    ServerFactory() {
        serverStatusController = ServerStatusController.getInstance();
    }

    public static ServerFactory getServer(String type, String arg, String serverName)   //arg can be state
    {
        ServerFactory server = new ServerFactory();
        server.type = type;
        if (type.equals(fps)) {
            if (arg.equalsIgnoreCase(pri)) {
                server.port = FPS_PRIMARY_RMI_PORT;
                server.address = serverStatusController.getPrimaryFPSAddress();
                server.profileName = getProfileName(serverName, pri.toLowerCase());
                server.isWrapper = isWrapper(serverName, pri.toLowerCase());

            } else if (arg.equalsIgnoreCase(sec)) {
                server.port = FPS_SECONDARY_RMI_PORT;
                server.address = serverStatusController.getSecondaryFPSAddress();
                server.profileName = getProfileName(serverName, sec.toLowerCase());
                server.isWrapper = isWrapper(serverName, sec.toLowerCase());

            } else if (arg.equalsIgnoreCase(act)) {

                try {
                    if (serverStatusController.getPrimaryFPSStatus(serverName).equalsIgnoreCase(act)
                            || serverStatusController.getPrimaryFPSStatus(serverName).equalsIgnoreCase(stand)) {
                        server.port = FPS_PRIMARY_RMI_PORT;
                        server.address = serverStatusController.getPrimaryFPSAddress();
                        server.profileName = getProfileName(serverName, pri.toLowerCase());
                        server.isWrapper = isWrapper(serverName, pri.toLowerCase());

                    } else {
                        server.port = FPS_SECONDARY_RMI_PORT;
                        server.address = serverStatusController.getSecondaryFPSAddress();
                        server.profileName = getProfileName(serverName, sec.toLowerCase());
                        server.isWrapper = isWrapper(serverName, sec.toLowerCase());
                    }
                } catch (STFException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else if (arg.equalsIgnoreCase(pass)) {
                try {
                    if (serverStatusController.getPrimaryFPSStatus(serverName).equalsIgnoreCase(pass)) {
                        server.port = FPS_PRIMARY_RMI_PORT;
                        server.address = serverStatusController.getPrimaryFPSAddress();
                        server.profileName = getProfileName(serverName, pri.toLowerCase());
                        server.isWrapper = isWrapper(serverName, pri.toLowerCase());

                    } else if (serverStatusController.getSecondaryFPSStatus(serverName).equalsIgnoreCase(pass)) {
                        server.port = FPS_SECONDARY_RMI_PORT;
                        server.address = serverStatusController.getSecondaryFPSAddress();
                        server.profileName = getProfileName(serverName, sec.toLowerCase());
                        server.isWrapper = isWrapper(serverName, sec.toLowerCase());

                    }
                } catch (STFException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        if (type.equalsIgnoreCase(fes)) {
            if (arg.equalsIgnoreCase(pri)) {
                server.port = FES_PRIMARY_RMI_PORT;
                server.address = serverStatusController.getPrimaryFESAddress();
                server.profileName = getProfileName(serverName, pri.toLowerCase());
                server.isWrapper = isWrapper(serverName, pri.toLowerCase());

            } else if (arg.equalsIgnoreCase(sec)) {
                server.port = FES_SECONDARY_RMI_PORT;
                server.address = serverStatusController.getSecondaryFESAddress();
                server.profileName = getProfileName(serverName, sec.toLowerCase());
                server.isWrapper = isWrapper(serverName, sec.toLowerCase());

            } else if (arg.equalsIgnoreCase(act)) {

                try {
                    if (serverStatusController.getPrimaryFESStatus().equalsIgnoreCase(act)
                            || serverStatusController.getPrimaryFESStatus().equalsIgnoreCase(stand)) {
                        server.port = FES_PRIMARY_RMI_PORT;
                        server.address = serverStatusController.getPrimaryFESAddress();
                        server.profileName = getProfileName(serverName, pri.toLowerCase());
                        server.isWrapper = isWrapper(serverName, pri.toLowerCase());

                    } else {
                        server.port = FES_SECONDARY_RMI_PORT;
                        server.address = serverStatusController.getSecondaryFESAddress();
                        server.profileName = getProfileName(serverName, sec.toLowerCase());
                        server.isWrapper = isWrapper(serverName, sec.toLowerCase());

                    }
                } catch (STFException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            } else if (arg.equalsIgnoreCase(pass)) {
                try {
                    if (serverStatusController.getPrimaryFESStatus().equalsIgnoreCase(pass)) {
                        server.port = FES_PRIMARY_RMI_PORT;
                        server.address = serverStatusController.getPrimaryFESAddress();
                        server.profileName = getProfileName(serverName, pri.toLowerCase());
                        server.isWrapper = isWrapper(serverName, pri.toLowerCase());

                    } else if (serverStatusController.getSecondaryFESStatus().equalsIgnoreCase(pass)) {
                        server.port = FES_SECONDARY_RMI_PORT;
                        server.address = serverStatusController.getSecondaryFESAddress();
                        server.profileName = getProfileName(serverName, sec.toLowerCase());
                        server.isWrapper = isWrapper(serverName, sec.toLowerCase());

                    }
                } catch (STFException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return server;

    }

    public boolean isActive() {
        return active;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    private static String getProfileName(String serverName, String profileType) {
        TestEnvironment testEnvironment = serverStatusController.getTestEnvironment();
        Map<String, ProfileElement> profileElementMap = testEnvironment.getServer(serverName).getProfileElements();

        return profileElementMap.get(profileType).getProfileName();
    }

    private static boolean isWrapper(String serverName, String profileType) {
        TestEnvironment testEnvironment = serverStatusController.getTestEnvironment();
        Map<String, ProfileElement> profileElementMap = testEnvironment.getServer(serverName).getProfileElements();

        return profileElementMap.get(profileType).isWrapper();
    }
}
