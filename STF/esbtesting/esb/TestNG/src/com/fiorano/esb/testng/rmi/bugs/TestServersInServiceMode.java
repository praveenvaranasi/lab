package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: Dec 10, 2010
 * Time: 3:17:41 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestServersInServiceMode extends AbstractTestNG {
    private TestEnvironmentConfig testenvconfig = ESBTestHarness.getTestEnvConfig();
    private ServerStatusController serverstatus;
    private TestEnvironment testenv;
    //public String FioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);

    @Test(groups = "ServerInService", description = "bug 19660[Ubuntu] unable to start servers in service mode", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestSpaceInFioranoHome"));
        try {
            serverstatus = ServerStatusController.getInstance();
            testenvconfig = ESBTestHarness.getTestEnvConfig();
            testenv = serverstatus.getTestEnvironment();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestSpaceInFioranoHome"), e);
            Assert.fail("testStopServers failed", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetUp", "testSpaceInFioranoHome"));
    }

    @Test(groups = "ServerInService", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testStopServers() {
        try {
            rmiClient.getFpsManager().shutdownPeer("fps");
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(5000);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopServers", "TestSpaceInFioranoHome"), e);
            Assert.fail("testStopServers failed", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testStopServers", "testSpaceInFioranoHome"));

    }

    @Test(groups = "ServerInService", dependsOnMethods = "testStopServers", description = "bug 19660[Ubuntu] unable to start servers in service mode", alwaysRun = true)
    public void testServersInServiceMode() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testServersInServiceMode", "TestServersInServiceMode"));
        try {
            if (!System.getProperty("os.name").startsWith("windows")) {
                String FioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
                //File th = new File(FioranoHome);
                String javahome = System.getProperty("java.home");
                String env[] = {"JAVA_HOME=" + javahome};
                String location1 = FioranoHome + File.separator + "esb/fes/bin/service";
                String location2 = FioranoHome + File.separator + "esb/fps/bin/service";
                Process process1 = null, process2 = null;
                try {
                    Runtime.getRuntime().exec("chmod +x " + location1 + "/fes-service.sh ");
                    Runtime.getRuntime().exec("chmod +x " + location2 + "/fps-service.sh ");
                    process1 = Runtime.getRuntime().exec(location1 + "/fes-service.sh " + "start", env, new File(location1));
                    process2 = Runtime.getRuntime().exec(location2 + "/fps-service.sh " + "start", env, new File(location2));
                } catch (IOException e) {
                    Assert.fail("can not execute linux script(fes/fps files");
                    e.printStackTrace();
                }
                InputStreamReader ins1 = new InputStreamReader(process1.getInputStream());
                InputStreamReader ins2 = new InputStreamReader(process2.getInputStream());
                BufferedReader bbr1 = new BufferedReader(ins1);
                BufferedReader bbr2 = new BufferedReader(ins2);
                //Thread.sleep(5000);
                //process.destroy();
                String str = "", str1 = null;
                while ((str1 = bbr1.readLine()) != null) {
                    str += str1;
                }
                //System.out.println(str);
                if (str.contains("bad substitution")) {
                    Assert.fail("bug 19660[Ubuntu] unable to start fes in service mode as ubuntu interpreter is not set");
                }
                str1 = null;
                str = "";
                while ((str1 = bbr2.readLine()) != null) {
                    str += str1;
                }
                //System.out.println(str);
                if (str.contains("bad substitution")) {
                    Assert.fail("bug 19660[Ubuntu] unable to start fps in service mode as ubuntu interpreter is not set");
                }
            Thread.sleep(30000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testServersInServiceMode", "TestServersInServiceMode"));

    }

    @Test(groups = "ServerInService", dependsOnMethods = "testServersInServiceMode", description = "bug20027unable to run server as linux service with non-root user when logged-in as root", alwaysRun = true)
    public void testServersInServiceModeAsUser() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testServersInServiceModeAsUser", "TestServersInServiceMode"));
        try {
            if (!System.getProperty("os.name").startsWith("windows")) {
                String FioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
                //File th = new File(FioranoHome);
                String javahome = System.getProperty("java.home");
                String env[] = {"JAVA_HOME=" + javahome};
                String location = "/etc/init.d";
                File file = new File(location);
                Process process;
                if (file.exists()) {
                    process = Runtime.getRuntime().exec(location + "/server-service.sh " + "start", env, file);
                    Thread.sleep(15000);
                    //exitValue is 1 when even fps is already running
                    if (process.exitValue() != 1) {
/*                        Runtime.getRuntime().exec("/sbin/chkconfig --del server-service.sh");
                        Runtime.getRuntime().exec("sudo -S rm -rf "+location+"/server-service.sh");*/
                        Assert.fail("unable to run server as linux service with non-root user when logged-in as root");
                    }
                }
/*                Runtime.getRuntime().exec("/sbin/chkconfig --del server-service.sh");
                Runtime.getRuntime().exec("sudo -S rm -rf etc/init.d/server-service.sh");*/
            }
        } catch (Exception e) {

        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("testServersInServiceModeAsUser", "TestServersInServiceMode"));


    }

    @Test(groups = "ServerInService", dependsOnMethods = "testServersInServiceModeAsUser", description = "bug 19660[Ubuntu] unable to start servers in service mode", alwaysRun = true)
    public void testCleanUp() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testCleanUp", "TestServersInServiceMode"));
        try {
            try{//shutdown if server running
                dupConstructor();
                rmiClient.getFpsManager().shutdownPeer("fps");
                rmiClient.shutdownEnterpriseServer();
                Thread.sleep(5000);
            } catch (Exception e){
                if(e.toString().contains("NullPointerException"))
                    Assert.fail("Case failed TestServersInServiceMode. Cause :The server was not started in service mode");

            }
            startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
            startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanUp", "TestServersInServiceMode"), e);
            Assert.fail("testCleanUp failed", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanUp", "TestServersInServiceMode"));

    }

}