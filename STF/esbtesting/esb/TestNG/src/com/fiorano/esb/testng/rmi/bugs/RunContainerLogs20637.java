package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.ServiceException;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 9/15/11
 * Time: 12:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class RunContainerLogs20637 extends AbstractTestNG {


        char fsc = File.separatorChar;
        String pathUpToFesRun=System.getProperty("FIORANO_HOME") + fsc + "runtimedata" + fsc + "EnterpriseServers" +
                fsc + "profile1" + fsc + "FES" + fsc + "run" + fsc + "logs";
        String pathUpToFpsRun=System.getProperty("FIORANO_HOME") + fsc + "runtimedata" + fsc + "PeerServers" + fsc +
                "profile1" + fsc + "FPS" + fsc + "run" + fsc + "logs";
        String pathUpToFesBin = System.getProperty("FIORANO_HOME") + fsc + "esb" + fsc + "fes" + fsc + "bin" ;
        String pathUpToFpsBin = System.getProperty("FIORANO_HOME") + fsc + "esb" + fsc + "fps" + fsc + "bin" ;
        String runContainerErrorLog = "runContainer_error_log" ;
        String runContainerOutputLog = "runContainer_output_log" ;
        private TestEnvironmentConfig testenvconfig;
        private TestEnvironment testenv;
        private ServerStatusController serverstatus;



    @Test(groups = "RunContainerLogs", alwaysRun = true)
    public void startSetup(){
        if (rmiClient == null) {
            rmiClient = RMIClient.getInstance();
        }
        serverstatus = ServerStatusController.getInstance();
                    testenv = serverstatus.getTestEnvironment();

        testenvconfig = ESBTestHarness.getTestEnvConfig();

        //System.out.println(pathUpToFesRun);
        //System.out.println(pathUpToFesBin);


        Logger.LogMethodOrder(Logger.getOrderMessage("Deleting previous logs if any", "RunContainerLogs"));


    }

    @Test(groups = "RunContainerLogs", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testRunContainerLogs20637() throws IOException {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestLaunchOfRunContainerLogs", "RunContainerLogs"));

        try {
            File fesErrLogFile = new File(pathUpToFesRun+fsc+runContainerErrorLog);
            if(fesErrLogFile.exists()) {
                Runtime.getRuntime().exec("rm " + pathUpToFesRun+fsc+runContainerErrorLog);
            }


            File fpsErrLogFile = new File(pathUpToFpsRun+fsc+runContainerErrorLog);
            if(fpsErrLogFile.exists()) {
                Runtime.getRuntime().exec("rm " + pathUpToFpsRun + fsc + runContainerErrorLog);
            }

            File fesOutputLogFile = new File(pathUpToFesRun+fsc+runContainerOutputLog);
            if(fesOutputLogFile.exists()) {
                Runtime.getRuntime().exec("rm " + pathUpToFesRun + fsc + runContainerOutputLog);
            }

            File fpsOutputLogFile = new File(pathUpToFpsRun+fsc+runContainerOutputLog);
            if(fpsOutputLogFile.exists()) {
                Runtime.getRuntime().exec("rm " + pathUpToFpsRun + fsc + runContainerOutputLog);
            }


            Logger.LogMethodOrder(Logger.getOrderMessage("Setting up the servers", "RunContainerLogs"));
            try {
                rmiClient.getFpsManager().shutdownPeer("fps");
                System.out.println("stopped fps");
            } catch (RemoteException e) {
                Assert.fail("unable to stop fps");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (ServiceException e) {
                Assert.fail("unable to stop fps");
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            Logger.Log(Level.INFO, "Stopped fps");

            try{
                rmiClient.shutdownEnterpriseServer();
                System.out.println("stopped EnterPrise server");
            } catch (Exception e) {
                System.out.println("error shutting down fes");
            }
          Process p = Runtime.getRuntime().exec(pathUpToFpsBin+"/fps.sh");
             Thread.sleep(10000);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line1 = null;                         //to print the output of hidden console on terminal

                while ((line1 = in.readLine()) != null) {
                    System.out.println(line1);
                }
            System.out.println("Starting fps in background mode");
        } catch (IOException e) {
            Assert.fail("could not start the peer server FPS in background");
            e.printStackTrace();
        }  catch (Exception e){

        }
        Logger.Log(Level.INFO, "started fps in background");
        System.out.println("started fps in background");

        try {
          Process p = Runtime.getRuntime().exec(pathUpToFesBin+"/fes.sh");
             Thread.sleep(10000);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line1 = null;                         //to print the output of hidden console on terminal

                while ((line1 = in.readLine()) != null) {
                    System.out.println(line1);
                }
            Logger.Log(Level.INFO, "Starting fes in background mode");
            System.out.println("Starting fes in background mode");
        } catch (IOException e) {
            Assert.fail("could not start the enterprise server FPS in background");
            e.printStackTrace();
        }  catch (Exception e){

        }
        Logger.Log(Level.INFO, "started fes in background");
        System.out.println("started fes in background");



        try {
          Process p = Runtime.getRuntime().exec(pathUpToFpsBin+"/shutdown-fps.sh");
             Thread.sleep(10000);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line1 = null;                         //to print the output of hidden console on terminal

                while ((line1 = in.readLine()) != null) {
                    System.out.println(line1);
                }
            Logger.Log(Level.INFO, "Stopping fps in background mode");
            System.out.println("Stopping fps in background mode");
        } catch (IOException e) {
            Assert.fail("could not stop the peer server FPS in background");
            e.printStackTrace();
        }  catch (Exception e){

        }
        Logger.Log(Level.INFO, "stopped fps in background");
        System.out.println("stopped fps in background");

        try {
          Process p = Runtime.getRuntime().exec(pathUpToFesBin+"/shutdown-fes.sh");
             Thread.sleep(10000);

                BufferedReader in = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));
                String line1 = null;                   //to print the output of hidden console on terminal

                while ((line1 = in.readLine()) != null) {
                    System.out.println(line1);
                }
            Logger.Log(Level.INFO, "Stopping fes in background mode");
            System.out.println("Stopping fes in background mode");
        } catch (IOException e) {
            Assert.fail("could not stop the enterprise server FPS in background");
            e.printStackTrace();
        }  catch (Exception e){

        }
        Logger.Log(Level.INFO, "stopped fes in background");
        System.out.println("stopped fes in background");

        File fesErrLogFile = new File(pathUpToFesRun+fsc+runContainerErrorLog);
        if(fesErrLogFile.exists()) {
            System.out.println("Error Logs of fes are existing");
        }


        File fpsErrLogFile = new File(pathUpToFpsRun+fsc+runContainerErrorLog);
        if(fesErrLogFile.exists()) {
            System.out.println("Error Logs of fps are existing");
        }

        File fesOutputLogFile = new File(pathUpToFesRun+fsc+runContainerOutputLog);
        if(fesErrLogFile.exists()) {
            System.out.println("Output Logs of fes are existing");
        }

        File fpsOutputLogFile = new File(pathUpToFpsRun+fsc+runContainerOutputLog);
        if(fesErrLogFile.exists()) {
            System.out.println("Output Logs of fps are existing");
        }

    }

@Test(groups = "RunContainerLogs", dependsOnMethods = "testRunContainerLogs20637", alwaysRun = true)
    public void testCleanUp() {
    startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
    startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
    }

}
