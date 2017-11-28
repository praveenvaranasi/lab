package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.*;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: malarvizhi
 * Date: 8/15/12
 * Time: 7:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestBackupTakenInClearDB21808 extends AbstractTestNG {

    String pathUpToFesBin = System.getProperty("FIORANO_HOME") + fsc + "esb" + fsc + "fes" + fsc + "bin" ;
    String pathUpToFpsBin = System.getProperty("FIORANO_HOME") + fsc + "esb" + fsc + "fps" + fsc + "bin" ;
    String space=" ";
    private TestEnvironmentConfig testenvconfig;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    Boolean checkFES=true;
    Boolean checkFPS=true;

    @Test(groups = "bugs", alwaysRun = true)
    public void startSetup() throws IOException, InterruptedException {
        if (rmiClient == null) {
            rmiClient = RMIClient.getInstance();
        }
        serverstatus = ServerStatusController.getInstance();
        testenv = serverstatus.getTestEnvironment();
        testenvconfig = ESBTestHarness.getTestEnvConfig();
    }

    @Test(groups = "bugs",dependsOnMethods = "startSetup", alwaysRun = true)
    public void shutdownServers()  {
        Logger.LogMethodOrder(Logger.getOrderMessage("ShutdownServers", "TestBackupTakenInClearDB21808"));
        try {
            rmiClient.getFpsManager().shutdownPeer("fps");
            Thread.sleep(10000);
            System.out.println("\n"+"FPS Shutdown");
        } catch (Exception e) {
            System.out.println("\n"+"error shutting down fps"+"\n"+e);
        }
        Logger.Log(Level.INFO, "Stopped fps");
        try{
          rmiClient.shutdownEnterpriseServer();
          Thread.sleep(10000);
          System.out.println("\n"+"FES Shutdown");
        } catch (Exception e) {
            System.out.println("\n"+"Error shutting down fes");
            e.printStackTrace();
        }
    }

    @Test(groups = "bugs", dependsOnMethods = "shutdownServers", alwaysRun = true)
    public void testClearFESDB() throws InterruptedException {
        Logger.LogMethodOrder(Logger.getOrderMessage("testClearFESDB", "TestBackupTakenInClearDB21808"));
        //Thread.sleep(50000);
       for(int fesOption=1;fesOption<=9;fesOption++)
       {
       try {
            Process p = Runtime.getRuntime().exec(pathUpToFesBin+"/clearDB.sh"+space+"-backup"+space+"-q"+space+fesOption);
            Thread.sleep(10000);

            BufferedReader in = new BufferedReader(
                  new InputStreamReader(p.getInputStream()));
            String line1 = null;                         //to print the output of hidden console on terminal

            while ((line1 = in.readLine()) != null) {
             System.out.println(line1);
            }

       } catch (IOException e) {
           checkFES=false;
            Assert.fail("FES ClearDB with Backup Failed");
            e.printStackTrace();
       }  catch (Exception e){
           e.printStackTrace();
       }
        Logger.Log(Level.INFO, "Cleared FES DB and Backup taken");
       }
    }

    @Test(groups = "bugs", dependsOnMethods = "testClearFESDB", alwaysRun = true)
    public void testClearFPSDB() throws IOException {
        Logger.LogMethodOrder(Logger.getOrderMessage("testClearFPSDB", "TestBackupTakenInClearDB21808"));
        for(int fpsOption=1;fpsOption<=4;fpsOption++)
        {
        try {
            Process p = Runtime.getRuntime().exec(pathUpToFpsBin+"/clearDB.sh"+space+"-backup"+space+"-q"+space+fpsOption);
            Thread.sleep(10000);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line1 = null;                         //to print the output of hidden console on terminal

            while ((line1 = in.readLine()) != null) {
                System.out.println(line1);
            }

        } catch (IOException e) {
            checkFPS=false;
            Assert.fail("FPS ClearDB with Backup Failed");
            e.printStackTrace();
        }  catch (Exception e){

        }
        Logger.Log(Level.INFO, "Cleared FPS DB and Backup taken");

    }
        if((checkFES && checkFPS)==true)
        {
            System.out.println("\n"+"Cleared FES and FPS DB with Backup");
        }
    }

    @Test(groups = "bugs", dependsOnMethods = "testClearFPSDB", alwaysRun = true)
    public void testCleanUp() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testCleanUp", "TestSpaceInFioranoHome"));
        try {
            startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
            startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
        }
        catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanUp", "TestSpaceInFioranoHome"), e);
            Assert.fail("testCleanUp failed", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanUp", "testSpaceInFioranoHome"));

    }
}

