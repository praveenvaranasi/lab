package com.fiorano.esb.testng.rmi.bugs;

//Installer Jars

import com.fiorano.esb.server.api.IServiceProviderManager;

//** Installer Jars **

import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.remote.RMIClient;
//import com.fiorano.stf.remote.RTLClient;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.framework.ExecutionController;
import java.lang.Process;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import com.fiorano.esb.testng.rmi.AbstractTestNG;

import java.io.IOException;

/**
 * Created by root on 11/17/17.
 */
public class TryServerShutDownAndStart extends AbstractTestNG{
    String fesBin = System.getProperty("FIORANO_HOME") + fsc + "esb" + fsc +"fes" + fsc + "bin";
    String fpsBin = System.getProperty("FIORANO_HOME") + fsc + "esb" + fsc + "fps" + fsc + "bin";
    private ServerStatusController serverStatusController;
    private ExecutionController executionController;
    private IServiceProviderManager iServiceProviderManager;
    private TestEnvironment testEnvironment;
    private TestEnvironmentConfig testEnvironmentConfig;

    @Test
    public void testVariables()
    {
        if (rmiClient == null)
        {
            rmiClient = RMIClient.getInstance();
        }
        if (jmxClient == null)
        {
            jmxClient = JMXClient.getInstance();
        }
        System.out.println("paths: \n"+fesBin+"\n"+fpsBin );
        System.out.println(rmiClient);
        System.out.println(jmxClient);
    }

    @Test(dependsOnMethods = "testVariables")
    public void startSetup()
    {
        this.executionController = ExecutionController.getInstance();
        this.serverStatusController = ServerStatusController.getInstance();
        this.iServiceProviderManager = rmiClient.getServiceProviderManager();
        this.testEnvironment = serverStatusController.getTestEnvironment();
        this.testEnvironmentConfig = ESBTestHarness.getTestEnvConfig();
        if (iServiceProviderManager == null)
        {
            Assert.fail("Can't run the Tests as serviceProviderManager is null");
        }
        else
        {
            System.out.println(iServiceProviderManager);
        }
    }

    @Test(dependsOnMethods = "startSetup")
    public void testShutdownServers() throws IOException
    {
        try
        {
            Process process = Runtime.getRuntime().exec(fpsBin+"/shutdown-fps.sh");
            cleanup();
            Thread.sleep(10000);
            System.out.println("FPS Shutdown");
        }
        catch (InterruptedException ie)
        {
            System.out.println(ie);
        }

    }

    @Test(dependsOnMethods = "testShutdownServers")
    private void startServer()
    {
        startServer(testEnvironment, testEnvironmentConfig, "fps", SERVER_TYPE.FPS);
        dupConstructor();
        System.out.println("Tests done :: FPSServerShutdown");
    }
}
