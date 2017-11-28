package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IUserSecurityManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.annotations.Test;

import java.rmi.RemoteException;
import java.security.Principal;
import java.util.logging.Level;

/**
 * Created with IntelliJ IDEA.
 * User: phani
 * Date: 2/21/13
 * Time: 4:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestShutting_down_server_22833 extends AbstractTestNG{
    private IUserSecurityManager iusersecuritymanager;
    private TestEnvironmentConfig testenvconfig;
    private TestEnvironment testenv;
    private String username = "user1";
    private String password = "user1";
    private Principal  user;

    //Runtime.getRuntime().exec("/esb/fes/bin/shutdown-server.sh");
    @Test(groups = "Bugs", alwaysRun = true, description = "Initialise testing of bug 22833")
    public void initSetup(){
        testenv = ServerStatusController.getInstance().getTestEnvironment();
        testenvconfig = ESBTestHarness.getTestEnvConfig();
        try{
            iusersecuritymanager = rmiClient.getUserSecurityManager();
            user = iusersecuritymanager.createUser(username,password);
        }catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("initSetup", "TestShutting_down_server_22833"), e);
        }catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("initSetup", "TestShutting_down_server_22833"), e);
        }
    }
    @Test(groups = "Bugs", description = "NPE while shutting down server with user credential", dependsOnMethods = "initSetup", alwaysRun = true)
    public void run_shutdown_script_22833(){
        try{
            Runtime.getRuntime().exec(System.getProperty("FIORANO_HOME")+"/esb/fes/bin/shutdown-fes.sh");
            Thread.sleep(10000);
            startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
            cleanup();
            dupConstructor();
            Logger.Log(Level.INFO,Logger.getFinishMessage("run_shutdown_script_22833","TestShutting_down_server_22833"));
        }catch(Exception e){
            Logger.Log(Level.SEVERE, Logger.getErrMessage("run_shutdown_script_22833", "TestShutting_down_server_22833"), e);
        }
    }

}

