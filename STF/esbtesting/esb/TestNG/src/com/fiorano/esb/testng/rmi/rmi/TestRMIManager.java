package com.fiorano.esb.testng.rmi.rmi;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/7/11
 * Time: 4:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestRMIManager extends AbstractTestNG{
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "passwd";
    IRmiManager rmiManager;
    String handleID;
    private static final String TEST_FAILURE = "test failed because of ";
    private static final String TEST_SUCCESS = "The test succeeded";

    @BeforeClass
    void startSetUp(){
        try {
            rmiManager = rmiClient.getRmiManager();
            handleID = rmiManager.login(USERNAME, PASSWORD);

        } catch (Exception e) {
            Assert.fail("Error in logging into RMI Manager");
            e.printStackTrace();
        }
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "TestRMIManager"));
        System.out.println("Finished the Execution of the TestCase::" + getName());
    }

    @Test(groups = "RMIManagerTest", alwaysRun = true)
    public void testLogin() {

            System.out.println("now in " + Logger.getErrMessage(getName(), getClass().toString()));
        try {
            handleID = rmiManager.login(USERNAME, PASSWORD);
        } catch (RemoteException e) {
            Assert.fail("remoteException in " + Logger.getErrMessage(getName(), getClass().toString()));
            Logger.Log(Level.SEVERE, "remoteException in " + Logger.getErrMessage(getName(), getClass().toString()));
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE,"ServiceException in " + Logger.getErrMessage(getName(), getClass().toString()));
            Assert.fail("ServiceException in " + Logger.getErrMessage(getName(), getClass().toString()));
            System.err.println(TEST_FAILURE + ":: ServiceException : " + e.getMessage());
        }
    }

    @Test(groups = "RMIManagerTest", alwaysRun = true, dependsOnMethods = "testLogin")
    public void testGetEventProcessManager() {

        try {
            IEventProcessManager eventProcessManager = rmiManager.getEventProcessManager(handleID);
            eventProcessManager.exists("SIMPLECHAT", 1.0f);
        } catch (RemoteException e) {
            Assert.fail("remoteException in " + Logger.getErrMessage(getName(), getClass().toString()));
            Logger.Log(Level.SEVERE, "remoteException in " + Logger.getErrMessage(getName(), getClass().toString()));
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println(TEST_FAILURE + ":: ServiceException : " + e.getMessage());
            Logger.Log(Level.SEVERE, "ServiceException in " + Logger.getErrMessage(getName(), getClass().toString()));
            Assert.fail("ServiceException in " + Logger.getErrMessage(getName(), getClass().toString()));
        }
    }

    @Test(groups = "RMIManagerTest", alwaysRun = true, dependsOnMethods = "testGetEventProcessManager")
    public void testRmiCallTimeOut() {
        System.setProperty("FIORANO_RMI_CALL_TIMEOUT", "1");
        try {
            rmiManager.getEventProcessManager(handleID).getAllEventProcesses();
        } catch (RemoteException e) {
            if (e instanceof UnmarshalException) {
                if ((e.getCause().getMessage().indexOf("Read timed out") != -1))
                    Assert.assertTrue(true);
                else
                    Assert.assertTrue(false, TEST_FAILURE + e.getMessage());
            } else
                Assert.assertTrue(false, TEST_FAILURE + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(false, TEST_FAILURE + e.getMessage());
        } finally {
            System.setProperty("FIORANO_RMI_CALL_TIMEOUT", "120000");//set it back to a healthy value = 2min
        }

    }

    @Test(groups = "RMIManagerTest", alwaysRun = true, dependsOnMethods = "testRmiCallTimeOut")
    public void testGetServiceManager() {

        try {
            IServiceManager serviceManager = rmiManager.getServiceManager(handleID);
            serviceManager.exists("chat", 4.0f);
        } catch (RemoteException e) {
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(false, TEST_FAILURE + e.getMessage());
        }
    }

    @Test(groups = "RMIManagerTest", alwaysRun = true, dependsOnMethods = "testGetServiceManager")
    public void testGetFPSManager() {

        try {
            IFPSManager fpsManager = rmiManager.getFPSManager(handleID);
            fpsManager.isPeerRunning("fps_test");
        } catch (RemoteException e) {
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(false, TEST_FAILURE + e.getMessage());
        }
    }

    @Test(groups = "RMIManagerTest", alwaysRun = true, dependsOnMethods = "testGetFPSManager")
    public void testGetServiceProviderManager() {

        try {
            IServiceProviderManager serviceProviderManager = rmiManager.getServiceProviderManager(handleID);
            serviceProviderManager.getServerName();
        } catch (RemoteException e) {
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(false, TEST_FAILURE + e.getMessage());
        }
    }

    @Test(groups = "RMIManagerTest", alwaysRun = true, dependsOnMethods = "testGetServiceProviderManager")
    public void testLogout() {

        try {
            rmiManager.logout(handleID);
            try {
                rmiManager.getServiceManager(handleID);
            } catch (Exception e) {
                Assert.assertTrue(true, TEST_SUCCESS);
            }
        } catch (RemoteException e) {
            Assert.assertTrue(false, TEST_FAILURE + e.getMessage());
        } catch (ServiceException e) {
            Assert.assertTrue(false, TEST_FAILURE + e.getMessage());
        }
        try {
            IEventProcessManager eventProcessManager = rmiManager.getEventProcessManager(handleID);
        } catch (Exception e) {
            Assert.assertTrue(true, TEST_SUCCESS);
        }
    }

}
