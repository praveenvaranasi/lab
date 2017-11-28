package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.io.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: 1/20/11
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestFileDescriptor19637 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private TestEnvironmentConfig testenvconfig;

    @Test(groups = "FileDescriptorLeakInRMIModule", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run TestFileDescriptor , as eventProcessManager is not set.");
        }
    }

    @Test(groups = "FileDescriptorLeakInRMIModule", description = "FileDescriptorLeakInRMIModule bug 19637", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestFileDescriptorLeak() {

        if (!System.getProperty("os.name").startsWith("Windows")) {
            //This test is especially for servers having multiple interfaces
            Logger.LogMethodOrder(Logger.getOrderMessage("TestFileDescriptorLeak", "TestFileDescriptor19637"));
            String fioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
            File file = new File(fioranoHome);
            String location = file.getAbsolutePath() + File.separator + "esb/server/profiles/profile1/FES/pid.txt";
            String pid = null;
            String line = null;
            try {
                FileReader fr = new FileReader(location);
                BufferedReader br = new BufferedReader(fr);

                while ((line = br.readLine()) != null) {
                    pid = line.trim();
                }
                fr.close();
                br.close();
            } catch (IOException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFileDescriptorLeak", "TestFileDescriptor19637"), e);
                org.testng.Assert.fail("failed while getting fes pid", e);
            }

            File fmqfilepath = new File(file.getAbsolutePath() + "/fmq/bin/fmq.sh");
            Process process = null;
            String[] lsofcmd = new String[3];
            lsofcmd[0] = "/bin/sh";
            lsofcmd[1] = "-c";
            lsofcmd[2] = "/usr/sbin/lsof -p " + pid + " | wc -l";
            String fmqshutdownscript = file.getAbsolutePath() + "/fmq/bin/shutdown-fmq.sh";
            try {
                //adding execute permission for fmq.sh script
                Runtime.getRuntime().exec("chmod +x " + fmqfilepath.getAbsolutePath());
                Runtime.getRuntime().exec(fmqfilepath.getAbsolutePath());
                Thread.sleep(10000);
                //Runtime.getRuntime().exec("chmod +x " + lsofcmd);
                process = Runtime.getRuntime().exec(lsofcmd);

            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFileDescriptorLeak", "TestFileDescriptor19637"), e);
                try {
                    Runtime.getRuntime().exec("chmod +x " + fmqshutdownscript);
                    Runtime.getRuntime().exec(fmqshutdownscript);
                } catch (IOException e1) {
                    Assert.fail("failed to shutdown the fmqserver");
                }

                org.testng.Assert.fail("Failed to execute fmq.sh script", e);
            }

            String[] result = new String[5];
            for (int i = 0; i < result.length; i++) {
                try {
                    InputStreamReader ins = new InputStreamReader(process.getInputStream());
                    BufferedReader br = new BufferedReader(ins);
                    result[i] = br.readLine();
                    Thread.sleep(300000);
                    process = Runtime.getRuntime().exec(lsofcmd);
                } catch (Exception e) {
                    Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFileDescriptorLeak", "TestFileDescriptor19637"), e);
                    try {
                        Runtime.getRuntime().exec(fmqshutdownscript);
                    } catch (IOException e1) {
                        Assert.fail("failed to shutdown the fmqserver");
                    }
                    Assert.fail("failed while reading line");
                }
            }
            try {

                Runtime.getRuntime().exec(fmqshutdownscript);
                Thread.sleep(2000);
            } catch (Exception e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFileDescriptorLeak", "TestFileDescriptor19637"), e);
                org.testng.Assert.fail("failed while executing shutdown-fmq.sh script", e);
            }
/*            location =file.getAbsolutePath() + "/fmq/profiles/FioranoMQ/pid.txt";
            try {
                FileReader fr = new FileReader(location);
                BufferedReader br = new BufferedReader(fr);

                while ((line = br.readLine()) != null) {
                    pid = line.trim();
                }
                fr.close();
                br.close();
            }catch (IOException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFileDescriptorLeak", "TestFileDescriptor19637"), e);
                org.testng.Assert.fail("failed while getting fes pid", e);
            }*/
            for (int i = 1; i < result.length; i++) {
                if (!(Integer.parseInt(result[0]) >= Integer.parseInt(result[i]))) {
                    Logger.Log(Level.SEVERE, Logger.getErrMessage("TestFileDescriptorLeak", "TestFileDescriptor19637"));
                    Assert.fail("failed as file descriptor leak in rmi module");
                }
            }

        }
        Logger.Log(Level.SEVERE,Logger.getFinishMessage("TestFileDescriptorLeak", "TestFileDescriptor19637"));

    }

}
