package com.fiorano.esb.testng.rmi.scenario.sanity;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.remote.RTLClient;
import com.fiorano.stf.test.core.STFTestSuite;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/18/11
 * Time: 4:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestRestartServer extends AbstractTestNG{

    private String resourceFilePath;
    private STFTestSuite suite;
    private ExecutionController proxy;

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("...............................None...............................");
    }

    @BeforeClass(groups = "RestartServerTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Sanity" + fsc + "RestartServers" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Sanity" + fsc + "RestartServers";
        proxy = ExecutionController.getInstance();
        printInitParams();
    }

    @Test(groups = "RestartServerTest", alwaysRun = true, dependsOnMethods = "startSetup ", description = "Restarts the FPS server")
    public void testRestartFPS() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRestartFPS", "TestRestartServers"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.getFioranoFPSManager().restartTPS("fps");
            Thread.sleep(100000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRestartFPS", "TestRestartServers"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRestartFPS", "TestRestartServers"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "RestartServerTest", alwaysRun = true, dependsOnMethods = "testRestartFPS", description = "Restarts the FES server")
    public void testRestartFES() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRestartFES", "TestRestartServers"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.restartEnterpriseServer();

            System.out.println("Restarted FES");
            //todo
            // log saying that we have restarted the servers and hence we are updating fsp
            Thread.sleep(100000);
            rtlClient= RTLClient.getInstance();
            jmxClient= JMXClient.getInstance();
            rmiClient= RMIClient.getInstance();
            Logger.Log(Level.INFO, "FioranoServiceProvider updated by testRestartFES of RestartServersTest");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRestartFES", "TestRestartServers"));


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRestartFES", "TestRestartServers"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "RestartServerTest", alwaysRun = true, dependsOnMethods = "testRestartFES", description = "Shutdowns the FEs and FPS servers after restart")
    public void testShutdown() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testShutdown", "TestRestartServers"));
            Thread.sleep(5000);
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.getFioranoFPSManager().shutdownTPS("fps");
            rtlClient.shutdownEnterpriseServer();
            Thread.sleep(5000);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testShutdown", "TestRestartServers"));
            String fesURL, fesAddress = null;
            fesURL = ServerStatusController.getInstance().getURLOnFESActive();
            fesAddress = fesURL.substring(fesURL.lastIndexOf("//") + 2, fesURL.lastIndexOf(":")); //tsp_tcp
            proxy.startServerOnRemote(fesAddress, "fes", "profile1", false, false);
            Thread.sleep(100000);
            rtlClient = RTLClient.getInstance();
            rmiClient = RMIClient.getInstance();
            jmxClient = JMXClient.getInstance();
            proxy.startServerOnRemote(fesAddress, "fps", "profile1", false, false);
            Thread.sleep(50000);
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testShutdown", "TestRestartServers"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }
}
