package com.fiorano.esb.testng.rmi.bugs;


import com.fiorano.esb.server.api.FESPerformanceStats;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: janardhan
 * Date: Nov 30, 2010
 * Time: 3:21:16 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestFESMemoryUsage extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IServiceProviderManager SPManager;
    private FESPerformanceStats fesPerformanceStats;
    private FESPerformanceStats fesPerformanceStats1;
    private ExecutionController executioncontroller;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private TestEnvironmentConfig testenvconfig;
    private MachineElement machine1;
    private ServerElement serverElement;
    private ProfileElement profileElement;
    private float appVersion = 1.0f;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "PYV_SF_NOT_IN_MEM_ORIGINAL";
    private String appName1 = "PYV_SF_NOT_IN_MEM_ORIGINAL1";
    private String appName123 = "PYV_SF_NOT_IN_MEM_ORIGINAL123";
    private String appName3 = "PYV_SF_NOT_IN_MEM_ORIGINAL3";
    private String appName4 = "PYV_SF_NOT_IN_MEM_ORIGINAL4";
    private String appName5 = "PYV_SF_NOT_IN_MEM_ORIGINAL5";
    private String appName6 = "PYV_SF_NOT_IN_MEM_ORIGINAL6";
    private String appName7 = "PYV_SF_NOT_IN_MEM_ORIGINAL7";
    private String appName8 = "PYV_SF_NOT_IN_MEM_ORIGINAL8";
    private String appName9 = "PYV_SF_NOT_IN_MEM_ORIGINAL9";
    private String appName10 = "PYV_SF_NOT_IN_MEM_ORIGINAL10";
    private String appName11 = "PYV_SF_NOT_IN_MEM_ORIGINAL11";
    private String appName12 = "PYV_SF_NOT_IN_MEM_ORIGINAL12";
    private String appName13 = "PYV_SF_NOT_IN_MEM_ORIGINAL13";
    private String appName14 = "PYV_SF_NOT_IN_MEM_ORIGINAL14";
    private String appName15 = "PYV_SF_NOT_IN_MEM_ORIGINAL15";
    private String appName16 = "PYV_SF_NOT_IN_MEM_ORIGINAL16";
    private String appName17 = "PYV_SF_NOT_IN_MEM_ORIGINAL17";
    private String appName18 = "PYV_SF_NOT_IN_MEM_ORIGINAL18";
    private String appName19 = "PYV_SF_NOT_IN_MEM_ORIGINAL19";
    private String appName20 = "PYV_SF_NOT_IN_MEM_ORIGINAL20";
    private String appName21 = "PYV_SF_NOT_IN_MEM_ORIGINAL21";
    private String appName22 = "PYV_SF_NOT_IN_MEM_ORIGINAL22";
    private String appName23 = "PYV_SF_NOT_IN_MEM_ORIGINAL23";
    private String appName24 = "PYV_SF_NOT_IN_MEM_ORIGINAL24";
    private String appName25 = "PYV_SF_NOT_IN_MEM_ORIGINAL25";
    private String appName26 = "PYV_SF_NOT_IN_MEM_ORIGINAL26";
    private String appName27 = "PYV_SF_NOT_IN_MEM_ORIGINAL27";
    private String appName28 = "PYV_SF_NOT_IN_MEM_ORIGINAL28";
    private String appName29 = "PYV_SF_NOT_IN_MEM_ORIGINAL29";
    private String appName30 = "PYV_SF_NOT_IN_MEM_ORIGINAL30";


    @Test(groups = "FESMemoryUsage", alwaysRun = true)
    public void startSetup() {
        System.out.println("running startSetup of TestFESMemoryUsage");
        Logger.LogMethodOrder(Logger.getOrderMessage("StartSetup", "TestFESMemoryUsage"));
        this.eventProcessManager = rmiClient.getEventProcessManager();
        serverstatus = ServerStatusController.getInstance();
        testenv = serverstatus.getTestEnvironment();
        testenvconfig = ESBTestHarness.getTestEnvConfig();
        executioncontroller = ExecutionController.getInstance();
        this.SPManager = rmiClient.getServiceProviderManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group FESMemoryUsage. as eventProcessManager is not set.");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("StartSetup", "TestFESMemoryUsage"));
    }

    @Test(groups = "FESMemoryUsage", description = "FESMemoryUsage bug 19787 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestShutdownFESandFPS() {
        System.out.println("running TestShutdownFESandFPS");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestShutdownFESandFPS", "TestFESMemoryUsage"));
        try {
            rmiClient.getFpsManager().shutdownPeer("fps");
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(5000);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestShutdownFESandFPS", "TestFESMemoryUsage"), e);
            Assert.fail("testStopServers failed", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestShutdownFESandFPS", "TestFESMemoryUsage"));

    }

    @Test(groups = "FESMemoryUsage", description = "FESMemoryUsage bug 19787 ", dependsOnMethods = "TestShutdownFESandFPS", alwaysRun = true)
    public void TestModifyFesConf() {
        System.out.println("running ModifyFESConf of TestFESMemoryUsage");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestModifyFesConf", "TestFESMemoryUsage"));
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        String serverConfigsPath = home + System.getProperty("file.separator") + "configuration" + System.getProperty("file.separator") + "serverConfigs" + System.getProperty("file.separator");
        try {


            File f1 = new File(serverConfigsPath + "fes.conf");
            File f2 = new File(serverConfigsPath + "fes1.conf");
            BufferedReader br;
            BufferedWriter bw;

            br = new BufferedReader(new FileReader(f1));

            bw = new BufferedWriter(new FileWriter(f2));


            String line = "";
            /*  StringBuffer newtext = new StringBuffer("");
            while ((line = br.readLine()) != null) {

                if (line.startsWith("FIORANO_HOME=")) {
                    line = "FIORANO_HOME=" + fioranohome;
                }
                if (line.startsWith("JAVA_HOME=")) {
                    line = "JAVA_HOME=" + javahome;
                }

                bw.write(line + "\n");


            }*/
            while ((line = br.readLine()) != null) {
                if (line.startsWith("-XX:+DisableExplicitGC")) {
                    line = "#-XX:+DisableExplicitGC";
                }


                if (line.startsWith("<java.system.props>")) {
                    bw.write(line + "\n");
                    line = "com.sun.management.jmxremote" + "\n" + "com.sun.management.jmxremote.port=7778" + "\n" + "com.sun.management.jmxremote.authenticate=false" + "\n" + "com.sun.management.jmxremote.ssl=false";
                }
                bw.write(line + "\n");

            }
            br.close();
            bw.flush();
            bw.close();

            boolean renamed1 = f1.renameTo(new File(serverConfigsPath + "fes.conf_backup"));
            if (renamed1 == false)      {
                Assert.fail("not able to modify fes.conf file");
            }
            boolean renamed2 = f2.renameTo(new File(serverConfigsPath + "fes.conf"));
            if (renamed2 == false) {
                Assert.fail("not able to modify fes.conf file");
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Assert.fail("Failed to run FioranoMonitoringService", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestModifyFesConf", "TestFESMemoryUsage"));
    }

    @Test(groups = "FESMemoryUsage", description = "FESMemoryUsage bug 19787 ", dependsOnMethods = "TestModifyFesConf", alwaysRun = true)
    public void TestCopyServerConfigs() {
        System.out.println("running TestCopyServerConfigs of TestFESMemoryUsage");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestCopyServerConfigs", "TestFESMemoryUsage"));
        try {
            String serverConfigs = "serverConfigs.zip";
            executioncontroller.sendConfiguration(serverConfigs);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Assert.fail("Failed to run TestCopySeverConfigs", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestCopyServerConfigs", "TestFESMemoryUsage"));
    }

    @Test(groups = "FESMemoryUsage", description = "FESMemoryUsage bug 19787 ", dependsOnMethods = "TestCopyServerConfigs", alwaysRun = true)
    public void TestStartFESandFPS() {
        startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
        startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
        startSetup();
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStartFESandFPS", "TestFESMemoryUsage"));

    }

    @Test(groups = "FESMemoryUsage", description = "FESMemoryUsage bug 19787 ", dependsOnMethods = "TestStartFESandFPS", alwaysRun = true)
    public void TestDeployOfFESMemoryApplication() {

        System.out.println("running TestDeployofFESMemoryApplication");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDeployOfFESMemoryApplication", "TestFESMemoryUsage"));
        try {
            fesPerformanceStats = SPManager.getServerPerformanceStats();
            System.out.println("initial memory usage of fes server is " + fesPerformanceStats.getMemoryUsageInKB());
            System.out.println("initial free memory  is " + fesPerformanceStats.getFreeMemoryInKB());
            System.out.println("initial total memory is " + fesPerformanceStats.getTotalMemoryInKB());

        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDeployOfFESMemoryApplication", "TestFESMemoryUsage"));

        SampleEventProcessListener epListener = null;
        SampleEventProcessListener epListener1 = null;
        SampleEventProcessListener epListener123 = null;
        SampleEventProcessListener epListener3 = null;
        SampleEventProcessListener epListener4 = null;
        SampleEventProcessListener epListener5 = null;
        SampleEventProcessListener epListener6 = null;
        SampleEventProcessListener epListener7 = null;
        SampleEventProcessListener epListener8 = null;
        SampleEventProcessListener epListener9 = null;
        SampleEventProcessListener epListener10 = null;
        SampleEventProcessListener epListener11 = null;
        SampleEventProcessListener epListener12 = null;
        SampleEventProcessListener epListener13 = null;
        SampleEventProcessListener epListener14 = null;
        SampleEventProcessListener epListener15 = null;
        SampleEventProcessListener epListener16 = null;
        SampleEventProcessListener epListener17 = null;
        SampleEventProcessListener epListener18 = null;
        SampleEventProcessListener epListener19 = null;
        SampleEventProcessListener epListener20 = null;
        SampleEventProcessListener epListener21 = null;
        SampleEventProcessListener epListener22 = null;
        SampleEventProcessListener epListener23 = null;
        SampleEventProcessListener epListener24 = null;
        SampleEventProcessListener epListener25 = null;
        SampleEventProcessListener epListener26 = null;
        SampleEventProcessListener epListener27 = null;
        SampleEventProcessListener epListener28 = null;
        SampleEventProcessListener epListener29 = null;
        SampleEventProcessListener epListener30 = null;


        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
            epListener1 = new SampleEventProcessListener(appName1, eventStore);
            epListener123 = new SampleEventProcessListener(appName123, eventStore);
            epListener3 = new SampleEventProcessListener(appName3, eventStore);
            epListener4 = new SampleEventProcessListener(appName4, eventStore);
            epListener5 = new SampleEventProcessListener(appName5, eventStore);
            epListener6 = new SampleEventProcessListener(appName6, eventStore);
            epListener7 = new SampleEventProcessListener(appName7, eventStore);
            epListener8 = new SampleEventProcessListener(appName8, eventStore);
            epListener9 = new SampleEventProcessListener(appName9, eventStore);
            epListener10 = new SampleEventProcessListener(appName10, eventStore);
            epListener11 = new SampleEventProcessListener(appName11, eventStore);
            epListener12 = new SampleEventProcessListener(appName12, eventStore);
            epListener13 = new SampleEventProcessListener(appName13, eventStore);
            epListener14 = new SampleEventProcessListener(appName14, eventStore);
            epListener15 = new SampleEventProcessListener(appName15, eventStore);
            epListener16 = new SampleEventProcessListener(appName16, eventStore);
            epListener17 = new SampleEventProcessListener(appName17, eventStore);
            epListener18 = new SampleEventProcessListener(appName18, eventStore);
            epListener19 = new SampleEventProcessListener(appName19, eventStore);
            epListener20 = new SampleEventProcessListener(appName20, eventStore);
            epListener21 = new SampleEventProcessListener(appName21, eventStore);
            epListener22 = new SampleEventProcessListener(appName22, eventStore);
            epListener23 = new SampleEventProcessListener(appName23, eventStore);
            epListener24 = new SampleEventProcessListener(appName24, eventStore);
            epListener25 = new SampleEventProcessListener(appName25, eventStore);
            epListener26 = new SampleEventProcessListener(appName26, eventStore);
            epListener27 = new SampleEventProcessListener(appName27, eventStore);
            epListener28 = new SampleEventProcessListener(appName28, eventStore);
            epListener29 = new SampleEventProcessListener(appName29, eventStore);
            epListener30 = new SampleEventProcessListener(appName30, eventStore);

        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployOfFESMemoryApplication", "TestFESMemoryUsage"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
            eventProcessManager.addEventProcessListener(epListener1);
            eventProcessManager.addEventProcessListener(epListener123);
            eventProcessManager.addEventProcessListener(epListener3);
            eventProcessManager.addEventProcessListener(epListener4);
            eventProcessManager.addEventProcessListener(epListener5);
            eventProcessManager.addEventProcessListener(epListener6);
            eventProcessManager.addEventProcessListener(epListener7);
            eventProcessManager.addEventProcessListener(epListener8);
            eventProcessManager.addEventProcessListener(epListener9);
            eventProcessManager.addEventProcessListener(epListener10);
            eventProcessManager.addEventProcessListener(epListener11);
            eventProcessManager.addEventProcessListener(epListener12);
            eventProcessManager.addEventProcessListener(epListener13);
            eventProcessManager.addEventProcessListener(epListener14);
            eventProcessManager.addEventProcessListener(epListener15);
            eventProcessManager.addEventProcessListener(epListener16);
            eventProcessManager.addEventProcessListener(epListener17);
            eventProcessManager.addEventProcessListener(epListener18);
            eventProcessManager.addEventProcessListener(epListener19);
            eventProcessManager.addEventProcessListener(epListener20);
            eventProcessManager.addEventProcessListener(epListener21);
            eventProcessManager.addEventProcessListener(epListener22);
            eventProcessManager.addEventProcessListener(epListener23);
            eventProcessManager.addEventProcessListener(epListener24);
            eventProcessManager.addEventProcessListener(epListener25);
            eventProcessManager.addEventProcessListener(epListener26);
            eventProcessManager.addEventProcessListener(epListener27);
            eventProcessManager.addEventProcessListener(epListener28);
            eventProcessManager.addEventProcessListener(epListener29);
            eventProcessManager.addEventProcessListener(epListener30);


        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployOfFESMemoryApplication", "TestFESMemoryUsage"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployOfFESMemoryApplication", "TestFESMemoryUsagep"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("pyv_sf_not_in_mem_original-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original1-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original123-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original3-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original4-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original5-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original6-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original7-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original8-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original9-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original10-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original11-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original12-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original13-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original14-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original15-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original16-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original17-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original18-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original19-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original20-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original21-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original22-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original23-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original24-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original25-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original26-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original27-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original28-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original29-1.0@EnterpriseServer.zip");
            deployEventProcess("pyv_sf_not_in_mem_original30-1.0@EnterpriseServer.zip");


        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployOfFESMemoryApplication", "TestFESMemoryUsage"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployOfFESMemoryApplication", "TestFESMemoryUsage"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }


        try {
            ObjectName object1 = null;

            object1 = new ObjectName("java.lang:type=Memory");

            JMXClient jmxClient = JMXClient.getInstance();
            jmxClient.invoke(object1, "gc", new Object[]{}, new String[]{});
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }


            fesPerformanceStats1 = SPManager.getServerPerformanceStats();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        if(fesPerformanceStats1 != null){
        System.out.println("after deploying memory usage of fes server is " + fesPerformanceStats1.getMemoryUsageInKB());
        System.out.println("after deploying free memory  is " + fesPerformanceStats1.getFreeMemoryInKB());
        System.out.println("the total memory is " + fesPerformanceStats1.getTotalMemoryInKB());

        try {

            String[] strarray = fesPerformanceStats1.getMemoryUsageInKB().split("K");
            int a = Integer.parseInt(strarray[0]);
            int b = a / 1024;
            // System.out.println("after deploying memory usage of fes server is "+ b);
            if (b > 200) {
                throw new Exception("Failed because fes memory usage is more than 200mb");
            }
        } catch (Exception e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployOfFESMemoryApplication", "TestFESMemoryUsage"), e1);
            Assert.fail("Failed because fes memory usage is more than 200mb", e1);

        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStartFES", "TestFESMemoryUsage"));
    }
        else
            Assert.fail("FES Performance Stats is empty!");

    }

    @Test(groups = "FESMemoryUsage", dependsOnMethods = "TestDeployOfFESMemoryApplication", alwaysRun = true)
    public void stopAndDeleteApplication() {
        try {
            System.out.println("running stopAndDeleteApplication of TestFESMemoryUsage");
            Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestFESMemoryUsage"));
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName1, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName123,appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName3, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName4, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName5, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName6, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName7, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName8, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName9, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName10, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName11, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName12, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName13, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName14, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName15, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName16, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName17, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName18, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName19, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName20, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName21, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName22, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName23, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName24, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName25, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName26, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName27, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName28, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName29, appVersion, true, false);
            eventProcessManager.deleteEventProcess(appName30, appVersion, true, false);


        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestFESMemoryUsage"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestFESMemoryUsage"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopAndDeleteApplication", "TestFESMemoryUsage"));
    }

    @Test(groups = "FESMemoryUsage", description = "FESMemoryUsage bug 19787 ", dependsOnMethods = "stopAndDeleteApplication", alwaysRun = true)
    public void TestShutdownFES1andFPS1() {
        System.out.println("running TestShutdownFES1andFPS1");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestShutdownFES1andFPS1", "TestFESMemoryUsage"));
        try {
            rmiClient.getFpsManager().shutdownPeer("fps");
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(5000);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestShutdownFES1andFPS1", "TestFESMemoryUsage"), e);
            Assert.fail("testStopServers failed", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestShutdownFES1andFPS1", "TestFESMemoryUsage"));

    }

    @Test(groups = "FESMemoryUsage", description = "FESMemoryUsage bug 19787 ", dependsOnMethods = "TestShutdownFES1andFPS1", alwaysRun = true)
    public void TestModifyFesConf1() {
        System.out.println("running TestModifyFESConf1 of TestFESMemoryUsage");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestModifyFesConf1", "TestFESMemoryUsage"));
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        String serverConfigsPath = home + System.getProperty("file.separator") + "configuration" + System.getProperty("file.separator") + "serverConfigs" + System.getProperty("file.separator");
        try {


            File f1 = new File(serverConfigsPath + "fes.conf_backup");
            File f2 = new File(serverConfigsPath + "fes.conf");

            boolean deleted = f2.delete();
            if (deleted == false)
                Assert.fail("not able to delete modified fes.conf file");
            boolean renamed = f1.renameTo(new File(serverConfigsPath + "fes.conf"));
            if (deleted == false)
                Assert.fail("not able to rename original fes.conf file");

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Assert.fail("Failed to run TestModifyFesConf1", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestModifyFesConf1", "TestFESMemoryUsage"));
    }

    @Test(groups = "FESMemoryUsage", description = "FESMemoryUsage bug 19787 ", dependsOnMethods = "TestModifyFesConf1", alwaysRun = true)
    public void TestCopySeverConfigs1() {
        System.out.println("running TestCopyServerConfigs1 of TestFESMemoryUsage");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestCopyServerConfigs1", "TestFESMemoryUsage"));
        try {
            String serverConfigs = "serverConfigs.zip";
            executioncontroller.sendConfiguration(serverConfigs);
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Assert.fail("Failed to run TestCopySeverConfigs1", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestCopyServerConfigs1", "TestFESMemoryUsage"));

    }

    @Test(groups = "FESMemoryUsage", description = "FESMemoryUsage bug 19787 ", dependsOnMethods = "TestCopySeverConfigs1", alwaysRun = true)
    public void TestStartFES1andFPS1() {
        startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
        startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);

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


}
