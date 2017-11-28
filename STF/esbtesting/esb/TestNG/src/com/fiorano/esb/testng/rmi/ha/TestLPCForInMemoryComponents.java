package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.server.api.IConfigurationManager;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.bugs.TestInMemory17763;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Feb 15, 2011
 * Time: 10:23:08 AM
 * To change this template use File | Settings | File Templates.
 */

public class TestLPCForInMemoryComponents extends AbstractTestNG {
    private IConfigurationManager configmanager;
    private ExecutionController executioncontroller;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private TestEnvironmentConfig testenvconfig;
    private MachineElement machine1;
    private ServerElement serverElement;
    private ProfileElement profileElement;
    private String customComponentName = "testLPC";
    private String fioranoHome = null;
    private String testingHome = null;
    private String customComponentPath = null;
    private String customComponentBuildPath = null;
    private String customComponentDeployPath = null;
    private String esbToComponent = null;
    private String outputLog = "output.log";

    private String appName1 = "TESTLPC";
    private String appName2 = "TESTLPC1";
    private static Hashtable<String, String> eventStore1 = new Hashtable<String, String>();
    private static Hashtable<String, String> eventStore2 = new Hashtable<String, String>();
    private IEventProcessManager eventProcessManager;
    private Receiver rec1, rec2;
    private int peerPort;
    private String os_name;
    private float appVersion = 1.0f;
    @Test(groups = "LPCForInMemoryComponents", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestLPCForInMemoryComponents"));
        serverstatus = ServerStatusController.getInstance();
        testenv = serverstatus.getTestEnvironment();
        executioncontroller = ExecutionController.getInstance();
        testenvconfig = ESBTestHarness.getTestEnvConfig();
        configmanager = rmiClient.getConfigurationManager();
        fioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        testingHome = testenvconfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        esbToComponent = "esb" + File.separator + "tools" + File.separator + "templates" + File.separator + customComponentName;
        customComponentPath = testingHome + File.separator + "esb" + File.separator + "TestNG" + File.separator + "resources" + File.separator + customComponentName;
        customComponentBuildPath = fioranoHome + File.separator + "esb" + File.separator + "tools" + File.separator + "templates" + File.separator + customComponentName;
        customComponentDeployPath = fioranoHome + File.separator + "runtimedata" + File.separator + "repository" + File.separator + "components" + File.separator + customComponentName;
        os_name = System.getProperty("os.name");
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetup", "TestLPCForInMemoryComponents"));
    }

    @Test(groups = "LPCForInMemoryComponents", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testCreateCustomComponent() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("testCreateCustomComponent", "LPCForInMemoryComponents"));
        try{
        File file = new File(customComponentBuildPath);
        if (file.exists()) {
            boolean status = TestInMemory17763.deleteDirectory(file);
            Thread.sleep(10000);
            if (!status) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateCustomComponent", "LPCForInMemoryComponents"));
                Assert.fail("Failed to delete pre existed custom component!");
                }
            TestInMemory17763.copyDirectory(new File(customComponentPath), new File(customComponentBuildPath));
            Thread.sleep(25000);
            }
        else{
            TestInMemory17763.copyDirectory(new File(customComponentPath), new File(customComponentBuildPath));
            Thread.sleep(25000);
        }
        }
        catch (NullPointerException e){
            System.out.println("File doesn't exist. Copying from customComponentPath");
            TestInMemory17763.copyDirectory(new File(customComponentPath), new File(customComponentBuildPath));
            Thread.sleep(25000);
        }

        Properties properties = new Properties();
        properties.load(new FileInputStream(customComponentBuildPath + File.separator + "build.properties"));
        properties.setProperty("installer.dir", fioranoHome);
        properties.setProperty("esb.user.dir", fioranoHome + File.separator + "runtimedata");
        ArrayList url = getActiveFESUrl();
        int rmiport = (Integer) url.get(1);
        int port;
        if (rmiport == 2047)
            port = 1947;
        else
            port = 1948;
        properties.setProperty("server", "tsp_tcp://" + (String) url.get(0) + ":" + port);
        properties.store(new FileOutputStream(customComponentBuildPath + File.separator + "build.properties"), null);
        Properties properties1 = new Properties();
        properties1.load(new FileInputStream(customComponentBuildPath + File.separator + "etc" + File.separator + "resources.properties"));
        properties1.setProperty("icon32", fioranoHome + File.separator + "esb" + File.separator + "samples" + File.separator + "icons" + File.separator + "png_icons" + File.separator + "icon32.png");
        properties1.setProperty("icon", fioranoHome + File.separator + "esb" + File.separator + "samples" + File.separator + "icons" + File.separator + "png_icons" + File.separator + "icon.png");
        properties1.store(new FileOutputStream(customComponentBuildPath + File.separator + "etc" + File.separator + "resources.properties"), null);

        File buildFile = new File(customComponentDeployPath);
        if (buildFile.exists()) {
            executioncontroller.antunRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antunregister", esbToComponent, outputLog);
            Thread.sleep(5000);
        }
        File outLog = new File(outputLog);
        if (outLog.exists())
            outLog.delete();
        executioncontroller.antRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antregister", esbToComponent, outputLog);
        Thread.sleep(5000);
        boolean checkBuild = VerifyBuildProcess();
        if (!checkBuild) {
            Assert.fail("failed to register custom component");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateCustomComponent", "LPCForInMemoryComponents"));

    }

    @Test(groups = "LPCForInMemoryComponents", dependsOnMethods = "testCreateCustomComponent", alwaysRun = true)
    public void testLaunchOfEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"));
        SampleEventProcessListener epListener1 = null;
        SampleEventProcessListener epListener2 = null;
        eventProcessManager = rmiClient.getEventProcessManager();
        try {
            epListener1 = new SampleEventProcessListener(appName1, eventStore1);
            epListener2 = new SampleEventProcessListener(appName2, eventStore2);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener1);
            eventProcessManager.addEventProcessListener(epListener2);

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("testlpc-1.0.zip");
            Thread.sleep(4000);
            deployEventProcess("testlpc1-1.0.zip");
            Thread.sleep(4000);
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName1, appVersion);
            System.out.println("CRC of "+appName1+" DONE!!");
            eventProcessManager.checkResourcesAndConnectivity(appName2, appVersion);
            System.out.println("CRC of "+appName2+" DONE!!");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName1, appVersion, false);
            Thread.sleep(20000);
            eventProcessManager.startEventProcess(appName2, appVersion, false);
            Thread.sleep(20000);
            rec2 = new Receiver(true, false);
            rec2.setDestinationName(appName2 + "__" + "1_0" + "__" + "DISPLAY1" + "__OUT_PORT");
            rec2.setCf("PRIMARYCF");
            rec2.setUser("anonymous");
            rec2.setPasswd("anonymous");
            try {
                rec2.initialize("hafps");
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
                Assert.fail("Failed to do create receiver to inport", e);
            }

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Error in thread sleep", e);
        }

        //clear map
        eventStore1.clear();
        eventStore2.clear();
        try {
            eventProcessManager.startAllServices(appName1, appVersion);
            Thread.sleep(10000);
            eventProcessManager.startAllServices(appName2, appVersion);
            Thread.sleep(10000);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Error with thread sleep!", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOEventProcess", "TestLPCForInMemoryComponents"));

    }

    @Test(groups = "LPCForInMemoryComponents", dependsOnMethods = "testLaunchOfEventProcess", alwaysRun = true)
    public void testSendMessages() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testSendMessages", "TestLPCForInMemoryComponents"));
        rec1 = new Receiver(true, false);
        rec1.setDestinationName(appName1 + "__" + "1_0" + "__" + "DISPLAY1" + "__OUT_PORT");
        rec1.setCf(appName1 + "__" + "1_0" + "__" + "DISPLAY1");
        rec1.setUser("anonymous");
        rec1.setPasswd("anonymous");
        try {
            rec1.initialize("hafps");
            System.out.println("Init done in " + Logger.getExecuteMessage(getOnlyMethodName(), "LPC"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessages", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do create receiver to inport", e);
        }

        Thread publisherThread1 = new PublisherThread1();
        publisherThread1.start();
        Process process = null;
        String profileType = null;
        Map<String, ProfileElement> profileElements = null;
        for (int i = 0; i < 3; i++) {
            try {

                String url = rmiClient.getFpsManager().getConnectURLForPeer("hafps");
                if (url.endsWith("1867")) {   // if url ends with 1867 , it means primary is active ,so shutdown secondary server & vice-versa
                    profileType = "secondary";
                } else profileType = "primary";
                String serverName = "hafps";
                serverElement = testenv.getServer(serverName);
                profileElements = serverElement.getProfileElements();
                executioncontroller.shutdownServerOnRemote(testenv.getMachine(profileElements.get(profileType).getMachineName()).getAddress(), serverElement.getMode(), "haprofile1/" + profileType, profileElements.get(profileType).isWrapper(), testenv.getServer("hafps").isHA());
                Thread.sleep(5000);
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessages", "TestLPCForInMemoryComponents"), e);
                Assert.fail("Failed in testSendMessages method", e);
            }

            executioncontroller.startServerOnRemote(testenv.getMachine(profileElements.get(profileType).getMachineName()).getAddress(), serverElement.getMode(), "haprofile1/" + profileType, profileElements.get(profileType).isWrapper(), testenv.getServer("hafps").isHA());

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessages", "TestLPCForInMemoryComponents"), e);
                Assert.fail("Failed in testSendMessages method", e);
            }

        }
        int msgCount1 = rec1.getCount();
        int msgCount2 = rec2.getCount();
        try {
            Thread.sleep(10000);
            do {
                msgCount1 = rec1.getCount();
                msgCount2 = rec2.getCount();
                Thread.sleep(60000);
            } while (rec1.getCount() < 100000 && rec1.getCount() > msgCount1);

            do {
                msgCount1 = rec1.getCount();
                msgCount2 = rec2.getCount();
                Thread.sleep(60000);
            } while (rec2.getCount() < 100000 && rec2.getCount() > msgCount2);
            System.out.println("msgCount1=" + msgCount1);
            System.out.println("msgCount2=" + msgCount2);
            System.out.println("rec1 : " + rec1.getCount());
            System.out.println("rec2 : " + rec2.getCount());
            Assert.assertEquals(rec1.getCount(), 100000);
            Assert.assertEquals(rec2.getCount(), 100000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestSendMessages", "TestLPCForInMemoryComponents"));
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSendMessages", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed in testSendMessages method", e);
        } finally {
            rec1.close();
            rec2.close();
        }

    }


    @Test(groups = "LPCForInMemoryComponents", dependsOnMethods = "testSendMessages", alwaysRun = true)
    public void testStopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStopAndDeleteApplication", "TestLPCForInMemoryComponents"));
        try {
            eventProcessManager.stopEventProcess(appName1, appVersion);
            Thread.sleep(5000);
            eventProcessManager.stopEventProcess(appName2, appVersion);
            Thread.sleep(5000);
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            Thread.sleep(5000);
            eventProcessManager.deleteEventProcess(appName2, appVersion, true, false);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAndDeleteApplication", "TestLPCForInMemoryComponents"));
            Thread.sleep(5000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAndDeleteApplication", "TestLPCForInMemoryComponents"));
            System.out.println("Stop and delete done in LPC");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopAndDeleteApplication", "TestLPCForInMemoryComponents"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" + File.separator + "resources" + File.separator + zipName));
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
    private boolean VerifyBuildProcess() {
        FileReader outLog = null;
        boolean checkBuild = false;
        try {
            outLog = new FileReader(fioranoHome + File.separator + "output.log");
            BufferedReader br = new BufferedReader(outLog);
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.contains("BUILD FAILED")) {
                    checkBuild = false;
                    break;
                } else if (line.contains("BUILD SUCCESSFUL")) {
                    checkBuild = true;
                    break;
                } else
                    continue;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            new File(fioranoHome + File.separator + "output.log").delete();
        }
        return checkBuild;
    }

    @Test(enabled = false)
    class PublisherThread1 extends Thread {
        public void run() {
            Publisher pub = new Publisher();
            pub.setDestinationName(appName1 + "__" + "1_0" + "__" + "FEEDER1" + "__OUT_PORT");
            pub.setCf(appName1 + "__" + "1_0" + "__" + "FEEDER1");
            pub.setUser("anonymous");
            pub.setPasswd("anonymous");
            try {
                pub.initialize("hafps");
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("createPubSub", "TestRemoteServiceInstance"), e);
                Assert.fail("Failed to do create publisher to outport", e);
            }

            try {
                String messageSent = "Fiorano" + System.currentTimeMillis();
                pub.sendMessage(messageSent);
                //Thread.sleep(200000);
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } finally {
                pub.close();
            }
        }
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
}
