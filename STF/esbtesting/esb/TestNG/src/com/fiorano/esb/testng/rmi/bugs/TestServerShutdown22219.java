package com.fiorano.esb.testng.rmi.bugs;


import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.remote.RTLClient;
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
 * Date: 8/8/12
 * Time: 4:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServerShutdown22219 extends AbstractTestNG {

    String pathUpToFesBin = System.getProperty("FIORANO_HOME") + fsc + "esb" + fsc + "fes" + fsc + "bin" ;
    String pathUpToFpsBin = System.getProperty("FIORANO_HOME") + fsc + "esb" + fsc + "fps" + fsc + "bin" ;
    private String space=" ";
    private TestEnvironmentConfig testenvconfig;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private IServiceProviderManager serviceProviderManager;
    private ExecutionController executioncontroller;

    @Test(groups = "bugs", alwaysRun = true)
    public void startSetup(){
        if (rmiClient == null) {
           rmiClient = RMIClient.getInstance();
        }
        if (jmxClient == null) {
           jmxClient =JMXClient.getInstance();
        }
        this.executioncontroller = ExecutionController.getInstance();
        this.serverstatus = ServerStatusController.getInstance();
        this.testenv = serverstatus.getTestEnvironment();
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        this.serviceProviderManager=rmiClient.getServiceProviderManager();
        if (serviceProviderManager == null) {
            Assert.fail("Cannot run Group Bugs as serviceProviderManager is not set.");
        }
       Logger.LogMethodOrder(Logger.getOrderMessage("Setting up the servers", "RunContainerLogs"));
    }

    @Test(groups = "bugs",dependsOnMethods = "startSetup", alwaysRun = true)
    public void shutdownServers(){
        try {
            rmiClient.getFpsManager().shutdownPeer("fps");
            Thread.sleep(20000);
            System.out.println("FPS shutdown ");
        } catch (Exception e) {
            System.out.println("\n"+"error shutting down fps"+"\n"+e);
        }
        Logger.Log(Level.INFO, "shutdown fps");
        try{
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(20000);
            rmiClient = null;
            jmxClient=null;
            rtlClient=null;
            System.out.println("FES Shutdown");
        } catch (Exception e) {
            System.out.println("\n"+"error shutting down fes");
        }
    }

     boolean check=true;
    @Test(groups = "bugs", dependsOnMethods = "shutdownServers", alwaysRun = true)
    public void testMutilpleServerShutdown() throws IOException {
        Logger.LogMethodOrder(Logger.getOrderMessage("testMutilpleServerShutdown", "TestServerShutdown22219"));
       for(int multipleRun=0; multipleRun<=1;multipleRun++){
           startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
           startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
           rmiClient = RMIClient.getInstance();
           rtlClient = RTLClient.getInstance();
           jmxClient = JMXClient.getInstance();
           Logger.Log(Level.INFO, "Starting fes in nobackground mode");
        try {
            Process p = Runtime.getRuntime().exec(pathUpToFpsBin+"/shutdown-fps.sh");
            Thread.sleep(10000);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
            String line1 = null;                   //to print the output of hidden console on terminal

            while ((line1 = in.readLine()) != null) {
             System.out.println(line1);
            }
            Logger.Log(Level.INFO, "shutdown fps ");

        } catch (IOException e) {
            Assert.fail("could not shutdown fps");
            e.printStackTrace();
        }  catch (Exception e){

        }
        Logger.Log(Level.INFO, "shutdown fps");

        try {
            Process p = Runtime.getRuntime().exec(pathUpToFesBin+"/shutdown-fes.sh");
            Thread.sleep(20000);
           BufferedReader in = new BufferedReader(
                    new InputStreamReader(p.getInputStream()));
           String line1 = null;                   //to print the output of hidden console on terminal

           while ((line1 = in.readLine()) != null) {
              System.out.println(line1);
              if(line1.contains(":: ERROR_SHUTDOWN_SERVER ::"))
              check=false;
           }
           Logger.Log(Level.INFO, "shutdown fes ");

        } catch (IOException e) {
            Assert.fail("could not shutdown  the enterprise server");
            e.printStackTrace();
        } catch (InterruptedException e) {
        e.printStackTrace();
        }

       }
        cleanup();
        startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
        startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
        dupConstructor();
       if(!check)
       {
          System.out.println("\n"+"Error Thrown in FES Shutdown"+"\n"+"\n"+"Test failed ::: TestServerShutdown22219");
          Assert.fail("Error Thrown in FES Shutdown");
       }
       else
        System.out.println("\n"+"Test passed :::TestServerShutdown22219");
    }
  }

