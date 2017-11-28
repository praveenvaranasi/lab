package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IDebugger;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sagar
 * Date: Jan 19, 2011
 * Time: 6:58:20 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestSpaceInFioranoHome extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IDebugger debugger;
    private ExecutionController executioncontroller;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private TestEnvironmentConfig testenvconfig;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "SIMPLECHAT";
    private String oldFioranoHome;
    private String newFioranoHome;
    private float appVersion = 1.0f;
    @Test(groups = "SpacesInFioranoHome", description = "bug 19963:Unable to run servers if Fiorano-home directory has space in its name ", alwaysRun = true)
    public void startSetup() {
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetup", "TestSpaceInFioranoHome"));
        try {
            eventProcessManager = rmiClient.getEventProcessManager();
            serverstatus = ServerStatusController.getInstance();
            testenv = serverstatus.getTestEnvironment();
            debugger = rmiClient.getDebugger();

            testenvconfig = ESBTestHarness.getTestEnvConfig();
            //testLaunchOfSimpleChat();
            //testStopEventProcess();
            if (eventProcessManager == null) {
                Assert.fail("Cannot run test TestSpacesinFioranoHome. as eventProcessManager is not set.");
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestSpaceInFioranoHome"), e);
            Assert.fail("testStopServers failed", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("startSetUp", "testSpaceInFioranoHome"));
    }

    @Test(groups = "SpacesInFioranoHome", dependsOnMethods = "startSetup", alwaysRun = true)
    public void testStopServers() {
        try {
            rmiClient.getFpsManager().shutdownPeer("fps");
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(25000);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopServers", "TestSpaceInFioranoHome"), e);
            Assert.fail("testStopServers failed", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testStopServers", "testSpaceInFioranoHome"));

    }

    @Test(groups = "SpacesInFioranoHome", dependsOnMethods = "testStopServers", alwaysRun = true)
    public void testCreateNewFioranoHome() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStopServers", "TestSpaceInFioranoHome"));
        oldFioranoHome = testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        //String  loc = oldFioranoHome + File.separator + ".." ;
        File f_old = new File(oldFioranoHome);
        newFioranoHome = oldFioranoHome + File.separator + ".." + File.separator + "new Fiorano Home";
        File f_new = new File(newFioranoHome);
        //new FilePermission(newFioranoHome + "/-", "read,write,execute");
        f_new.mkdirs();
        copyDirectory(f_old, f_new);
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        String os_name = System.getProperty("os.name");

        if (os_name.startsWith("Linux")) {
            try {
                Runtime.getRuntime().exec(new String[]{"chmod", "-R", "777", newFioranoHome});
            } catch (IOException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateNewFioranoHome", "TestSpaceInFioranoHome"), e);
                Assert.fail("testCreateNewFioranoHome failed", e);
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateNewFioranoHome", "testSpaceInFioranoHome"));

        }
    }

    @Test(groups = "SpacesInFioranoHome", dependsOnMethods = "testCreateNewFioranoHome", alwaysRun = true)
    public void testStartServersWithNewFioranoHome() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStartServersWithNewFioranoHome", "TestSpaceInFioranoHome"));
        testenvconfig.setProperty(TestEnvironmentConstants.FIORANO_HOME, newFioranoHome);
        System.out.println(testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME));
        startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
        startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);

        Logger.Log(Level.INFO, Logger.getFinishMessage("testStartServersWithNewFioranoHome", "testSpaceInFioranoHome"));
    }

    @Test(groups = "SpacesInFioranoHome", dependsOnMethods = "testStartServersWithNewFioranoHome", alwaysRun = true)
    public void testLaunchOfSimpleChat() {

        Logger.LogMethodOrder(Logger.getOrderMessage("testLaunchOfSimpleChat", "TestSpaceInFioranoHome"));
        eventProcessManager = rmiClient.getEventProcessManager();

        SampleEventProcessListener epListener = null;
        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestSpaceInFioranoHome"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestSpaceInFioranoHome"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestSpaceInFioranoHome"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestSpaceInFioranoHome"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestSpaceInFioranoHome"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
            ;
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestSpaceInFioranoHome"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testLaunchOfSimpleChat", "TestSpaceInFioranoHome"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testLaunchOfSimpleChat", "TestSpaceInFioranoHome"));

    }

    @Test(groups = "SpacesInFioranoHome", dependsOnMethods = "testLaunchOfSimpleChat", alwaysRun = true)
    public void testStopEventProcess() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testStopEventProcess", "TestSpaceInFioranoHome"));
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopEventProcess", "testStopEventProcess"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopEventProcess", "testStopEventProcess"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testStopEventProcess", "testSpaceInFioranoHome"));
    }

    @Test(groups = "SpacesInFioranoHome", dependsOnMethods = "testStopEventProcess", alwaysRun = true)
    public void testCleanUp() {
        Logger.LogMethodOrder(Logger.getOrderMessage("testCleanUp", "TestSpaceInFioranoHome"));
        try {
            rmiClient.getFpsManager().shutdownPeer("fps");
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(5000);
            File f = new File(newFioranoHome);
            deleteDir(f);
            testenvconfig.setProperty(TestEnvironmentConstants.FIORANO_HOME, oldFioranoHome);
            startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
            startServer(testenv, testenvconfig, "fps", SERVER_TYPE.FPS);
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanUp", "TestSpaceInFioranoHome"), e);
            Assert.fail("testCleanUp failed", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanUp", "testSpaceInFioranoHome"));

    }


    @Test(enabled = false)
    private void copyDirectory(File sourceLocation, File targetLocation) {
        if (sourceLocation.isDirectory()) {
            if (!targetLocation.exists()) {
                targetLocation.mkdir();
            }

            String[] children = sourceLocation.list();
            for (int i = 0; i < children.length; i++) {
                copyDirectory(new File(sourceLocation, children[i]),
                        new File(targetLocation, children[i]));
            }
        } else {
            InputStream in = null;
            try {
                in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);
                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }

    @Test(enabled = false)
    private boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }


}




    