package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: srikanth
 * Date: Jan 10, 2011
 * Time: 5:25:13 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestReregisterCustomService extends AbstractTestNG {
    private String customComponentName = "NewCustomComponent";
    private TestEnvironmentConfig testenvconfig;
    private String fioranoHome = null;
    private String testingHome = null;
    private String customComponentPath = null;
    private String customComponentBuildPath = null;
    private String customComponentDeployPath = null;
    private String customComponentVersionPath = null;
    private String esbToComponent = null;
    private String outputLog = null;
    private ExecutionController remoteProxy = null;

    @Test(groups = "ReregisterCustomComponent", alwaysRun = true)
    public void startSetup() {
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        this.remoteProxy = ExecutionController.getInstance();
        fioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        testingHome = testenvconfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        esbToComponent = "esb" + File.separator + "tools" + File.separator + "templates" + File.separator + customComponentName;
        customComponentPath = testingHome + File.separator + "esb" + File.separator + "TestNG" + File.separator + "resources" + File.separator + customComponentName;
        customComponentBuildPath = fioranoHome + File.separator + esbToComponent;
        customComponentDeployPath = fioranoHome + File.separator + "runtimedata" + File.separator + "repository" + File.separator + "components" + File.separator + customComponentName;
        customComponentVersionPath = customComponentDeployPath + File.separator + "1.0";
        outputLog = "output.log";
    }

    @Test(groups = "ReregisterCustomComponent", description = "Re-registering a corrupted service fails :: 19962 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void createCustomComponent() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("createCustomComponent", "TestReregisterCustomService"));
        File file = new File(customComponentBuildPath);
        if (file.exists()) {
            boolean status = deleteDirectory(file);
            if (!status) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("createCustomComponent", "TestReregisterCustomService"));
                Assert.fail("Failed to delete pre existed custom component!");
            }
            copyDirectory(new File(customComponentPath), new File(customComponentBuildPath));
        } else {
            copyDirectory(new File(customComponentPath), new File(customComponentBuildPath));
        }

        Properties properties = new Properties();
        properties.load(new FileInputStream(customComponentBuildPath + File.separator + "build.properties"));
        properties.setProperty("installer.dir", fioranoHome);
        properties.setProperty("esb.user.dir", fioranoHome + File.separator + "runtimedata");
        properties.setProperty("server", "tsp_tcp://" + ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer() + ":1947");
        properties.store(new FileOutputStream(customComponentBuildPath + File.separator + "build.properties"), null);
        Properties properties1 = new Properties();
        properties1.load(new FileInputStream(customComponentBuildPath + File.separator + "etc" + File.separator + "resources.properties"));
        properties1.setProperty("icon32", fioranoHome + File.separator + "esb" + File.separator + "samples" + File.separator + "icons" + File.separator + "png_icons" + File.separator + "icon32.png");
        properties1.setProperty("icon", fioranoHome + File.separator + "esb" + File.separator + "samples" + File.separator + "icons" + File.separator + "png_icons" + File.separator + "icon.png");
        properties1.store(new FileOutputStream(customComponentBuildPath + File.separator + "etc" + File.separator + "resources.properties"), null);

        File buildFile = new File(customComponentDeployPath);
        if (buildFile.exists()) {
            remoteProxy.antunRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antunregister", esbToComponent, outputLog);
            Thread.sleep(5000);
        }
        File outLog = new File(outputLog);
        if (outLog.exists())
            outLog.delete();
        remoteProxy.antRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antregister", esbToComponent, outputLog);
        Thread.sleep(5000);
        boolean checkBuild = VerifyBuildProcess();
        if (!checkBuild) {
            Assert.fail("failed to register custom component");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("createCustomComponent", "TestReregisterCustomService"));

    }

    @Test(groups = "ReregisterCustomComponent", description = "Re-registering a corrupted service fails :: 19962 ", dependsOnMethods = "createCustomComponent", alwaysRun = true)
    public void createCorruptedService() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("createCorruptedService", "TestReregisterCustomService"));
        File file2 = new File(customComponentVersionPath);
        boolean status = deleteDirectory(file2);
        if (!status) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("createCorruptedService", "TestReregisterCustomService"));
            Assert.fail("Failed to make corrupted custom component");
        }
        File file1 = new File(customComponentVersionPath);
        if (!file1.exists())
            file1.mkdirs();
        File file = new File(customComponentVersionPath + File.separator + "ServiceDescriptor.xml");
        if (!file.exists())
            file.createNewFile();
        remoteProxy.antreRegister(ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer(), "antreregister", esbToComponent, outputLog);
        Thread.sleep(6000);
        boolean checkBuild = VerifyBuildProcess();
        if (!checkBuild) {
            Assert.fail("failed to re register custom component");
        } else
            System.out.println("TestCase Passes Successfully");


        Logger.Log(Level.INFO, Logger.getFinishMessage("createCorruptedService", "TestReregisterCustomService"));
    }

    @Test(enabled = false)
    private boolean deleteDirectory(File path) {
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
    private void copyDirectory(File srcPath, File dstPath) throws IOException {
        if (srcPath.isDirectory()) {
            if (!dstPath.exists()) {
                dstPath.mkdir();
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
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            new File(fioranoHome + File.separator + "output.log").delete();
        }
        return checkBuild;
    }


}
