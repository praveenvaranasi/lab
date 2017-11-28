package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ServiceInstance;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 2/1/11
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
@Test(groups = "IRmiManager.*")
public class TestInMemory17763 extends AbstractTestNG {
    private String customComponentName = "test";
    private TestEnvironmentConfig testenvconfig;
    private String fioranoHome = null;
    private String testingHome = null;
    private String customComponentPath = null;
    private String customComponentBuildPath = null;
    private String customComponentDeployPath = null;
    private String appGUID = "INMEMORY17763";
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private IEventProcessManager eventProcessManager;
    private ServerStatusController serverStatusController;
    private FioranoApplicationController m_fioranoappcontroller;
    List<ServiceInstance> serviceInstanceList;
    private ExecutionController remoteProxy = null;
    private String outputLog = "output.log";
    private float appVersion = 1.0f;
    @Test(groups = "InMemory17763", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        remoteProxy = ExecutionController.getInstance();
        fioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        testingHome = testenvconfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        customComponentPath = testingHome + "/esb/TestNG/resources/" + customComponentName;
        customComponentBuildPath = fioranoHome + "/esb/tools/templates/test";
        customComponentDeployPath = fioranoHome + "/runtimedata/repository/components/" + customComponentName;
        try {
            serverStatusController = ServerStatusController.getInstance();
            m_fioranoappcontroller = serverStatusController.getServiceProvider().getApplicationController();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("starSetUp", "TestInMemory17763"));
            Assert.fail("Failed to get applicationController!");
        }
    }

    @Test(groups = "InMemory17763", description = "bug17763:Changed classes are not re-loaded when launched in-memory ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void createCustomComponent() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("createCustomComponent", "TestInMemory17763"));
        Process process = null;
        String javahome = System.getProperty("java.home");
        String java_home = javahome.substring(0, javahome.length() - 4);
        String env[] = {"JAVA_HOME=" + java_home};
        try {
            File file = new File(customComponentBuildPath);
            if (file.exists()) {
                boolean status = deleteDirectory(file);
                if (!status) {
                    Logger.Log(Level.SEVERE, Logger.getErrMessage("createCustomComponent", "TestInMemory17763"));
                    Assert.fail("Failed to delete pre existed custom component!");
                }
                copyDirectory(new File(customComponentPath), new File(customComponentBuildPath));
            } else {
                copyDirectory(new File(customComponentPath), new File(customComponentBuildPath));
            }
            //setting the properties in custom component
            Properties properties = new Properties();
            properties.load(new FileInputStream(customComponentBuildPath + "/build.properties"));
            properties.setProperty("installer.dir", fioranoHome);
            properties.setProperty("esb.user.dir", fioranoHome + "/runtimedata");
            properties.store(new FileOutputStream(customComponentBuildPath + "/build.properties"), null);
            properties = new Properties();
            properties.load(new FileInputStream(customComponentBuildPath + "/etc/resources.properties"));
            properties.setProperty("icon32", fioranoHome + "/esb/samples/icons/png_icons/icon32.png");
            properties.setProperty("icon", fioranoHome + "/esb/samples/icons/png_icons/icon.png");
            properties.store(new FileOutputStream(customComponentBuildPath + "/etc/resources.properties"), null);

            File buildFile = new File(customComponentDeployPath);
            if (buildFile.exists()) {
                remoteProxy.antunRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antunregister", "/esb/tools/templates/test", outputLog);
                Thread.sleep(5000);
            }
            File outLog = new File(outputLog);
            if (outLog.exists())
                outLog.delete();
            remoteProxy.antRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antregister", "/esb/tools/templates/test", outputLog);
            Thread.sleep(5000);
            boolean checkBuild = VerifyBuildProcess();
            if (!checkBuild) {
                Assert.fail("failed to register custom component");
            }
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createCustomComponent", "TestInMemory17763"), e);
            Assert.fail("Failed to copy the custom component folder", e);
        } finally {
            if (process != null)
                process.destroy();
        }

        SampleEventProcessListener epListener = null;
        epListener = new SampleEventProcessListener(appGUID, eventStore);
        try {
            eventProcessManager.addEventProcessListener(epListener);
            deployEventProcess("inmemory17763-1.0@EnterpriseServer.zip");
            Thread.sleep(5000);
            String checkTestComponentInPeer = fioranoHome + "/runtimedata/PeerServers/profile1/FPS/run/components/test";
            File cheFile = new File(checkTestComponentInPeer);
            if (cheFile.exists()) {
                boolean status = deleteDirectory(cheFile);
                if (!status) {
                    Logger.Log(Level.SEVERE, Logger.getErrMessage("createCustomComponent", "TestInMemory17763"));
                    Assert.fail("Failed to delete pre existed custom component in peer repository!");
                }
            }
            eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
            Thread.sleep(3000);
            eventProcessManager.startEventProcess(appGUID, appVersion, false);
            ;
            Thread.sleep(2000);
            eventProcessManager.startAllServices(appGUID, appVersion);
            Thread.sleep(5000);
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            Thread.sleep(2000);
            String testjava = fioranoHome + "/esb/tools/templates/test/src/com/fiorano/edbc/test/TestMessageListener.java";
            File testFile = new File(testjava);
            if (testFile.exists()) {
                testFile.delete();
                testFile.createNewFile();
                copyDirectory(new File(fioranoHome + "/esb/tools/templates/test/TestMessageListener.java"), testFile);
            }

            File outLog = new File(outputLog);
            if (outLog.exists())
                outLog.delete();
            remoteProxy.antreRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antreregister", "/esb/tools/templates/test", outputLog);
            Thread.sleep(5000);
            boolean checkBuild = VerifyBuildProcess();
            if (!checkBuild) {
                Assert.fail("failed to register custom component");
            }

            try {
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
                Thread.sleep(2000);
                eventProcessManager.startEventProcess(appGUID, appVersion, false);
                ;
                Thread.sleep(2000);
                eventProcessManager.startAllServices(appGUID, appVersion);
                Thread.sleep(4000);
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("createCustomComponent", "TestInMemory17763"), e);
                Assert.fail("Failed to start the application after reregister!", e);
            }
            createQueuePub("INMEMORY17763__1_0__TEST1__IN_PORT_1", "fps");

            if (!new File(new File(javahome).getAbsoluteFile() + "/hello").exists()) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("createCustomComponent", "TestInMemory17763"));
                Assert.fail("Failed as changes made in custom component are not applied!");
            } else {
                new File(new File(javahome).getAbsoluteFile() + "/hello").delete();
            }
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            Application application = m_fioranoappcontroller.getApplication(appGUID, appVersion);
            serviceInstanceList = application.getServiceInstances();
            serviceInstanceList.get(0).setLaunchType(0);
            m_fioranoappcontroller.saveApplication(application);
            try {
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
                Thread.sleep(2000);
                eventProcessManager.startEventProcess(appGUID, appVersion, false);
                Thread.sleep(2000);
                eventProcessManager.startAllServices(appGUID, appVersion);
                Thread.sleep(4000);
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("createCustomComponent", "TestInMemory17763"));
                Assert.fail("Failed to start ep when component launch type change to separate process!");
            }
            File buildFile = new File(customComponentDeployPath);
            if (buildFile.exists()) {
                remoteProxy.antunRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antunregister", "/esb/tools/templates/test", outputLog);
                Thread.sleep(5000);
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createCustomComponent", "TestInMemory17763"), e);
            Assert.fail("Failed to get the start event process!", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("createCustomComponent", "TestInMemory17763"));


    }

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
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        bis = new BufferedInputStream(new FileInputStream(testingHome + "/esb/TestNG/resources/" + zipName));
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
    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deleteDirectory(files[i]);
                } else {
                    files[i].delete();
                }
            }
        }
        return (path.delete());
    }

    @Test(enabled = false)
    public static void copyDirectory(File srcPath, File dstPath) throws IOException {
        if (srcPath.isDirectory()) {
            if (!dstPath.exists()) {
                dstPath.mkdirs();
            }
            String files[] = srcPath.list();
            for (int i = 0; i < files.length; i++) {
                copyDirectory(new File(srcPath, files[i]),
                        new File(dstPath, files[i]));
            }
        } else {
            if (!srcPath.exists()) {
                //Log(Level.SEVERE, Logger.getErrMessage("createCustomComponent", "TestInMemory17763"));
                Assert.fail("Failed as directory does not exist!");
            } else {
                InputStream in = new FileInputStream(srcPath);
                OutputStream out = new FileOutputStream(dstPath);
                // Transfer bytes from in to out
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            }
        }

    }

    @Test(groups = "InMemory17763", description = "bug17763:Changed classes are not re-loaded when launched in-memory ", dependsOnMethods = "createCustomComponent", alwaysRun = true)
    public void testStopDeleteApplication() {
        try {
            deleteDirectory(new File(fioranoHome + "/esb/tools/templates/test"));
            eventProcessManager.stopEventProcess(appGUID, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appGUID, appVersion, true, false);
        } catch (RemoteException e) {
            //Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            //Log(Level.SEVERE, Logger.getErrMessage("testComponentLaunchOnPeerRestartBug19057", "TestJapanese"), e);
            org.testng.Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testStopDeleteApplication", "TestInMemory17763"));

    }

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
        //String messageOnDestination = null;
        try {
            rec.sendMessage("fiorano");
        } catch (Exception e) {
            //Log(Level.SEVERE, Logger.getErrMessage("TestLaunchOfJapaneseApplication", "TestJapanese"), e);
            Assert.fail("Failed to do send message to destination " + queueName, e);
        } finally {
            rec.close();
        }
    }
}
