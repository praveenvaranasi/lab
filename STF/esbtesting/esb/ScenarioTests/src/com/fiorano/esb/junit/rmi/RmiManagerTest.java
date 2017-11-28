package com.fiorano.esb.junit.rmi;

import com.fiorano.esb.server.api.*;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.test.core.RMITestCase;
import junit.framework.Assert;

import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: lokesh
 */

public class RmiManagerTest extends RMITestCase {

    private static final String TEST_SUCCESS = "TestCase succeded";
    private static final String TEST_FAILURE = "TestCase failed ";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "passwd";


    IRmiManager rmiManager;
    String handleID;


    public RmiManagerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
        try {
            rmiManager = rmiClient.getRmiManager();
            handleID = rmiManager.login(USERNAME, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public RmiManagerTest(String name) throws Exception {
        super(name);
        rmiManager = rmiClient.getRmiManager();
        handleID = rmiManager.login(USERNAME, PASSWORD);
    }

    public void setUp() throws Exception {

        System.out.println("Started the Execution of the TestCase::" + getName());
    }

    public void testLogin() {

        try {
            handleID = rmiManager.login(USERNAME, PASSWORD);
        } catch (RemoteException e) {
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        }
    }

    public void testGetEventProcessManager() {

        try {
            IEventProcessManager eventProcessManager = rmiManager.getEventProcessManager(handleID);
            eventProcessManager.exists("SIMPLECHAT", 1.0f);
        } catch (RemoteException e) {
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        }
    }

    public void testRmiCallTimeOut() {
        System.setProperty("FIORANO_RMI_CALL_TIMEOUT", "1");
        try {
            rmiManager.getEventProcessManager(handleID).getAllEventProcesses();
        } catch (RemoteException e) {
            if (e instanceof UnmarshalException) {
                if ((e.getCause().getMessage().indexOf("Read timed out") != -1))
                    Assert.assertTrue(true);
                else
                    Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
            } else
                Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } finally {
            System.setProperty("FIORANO_RMI_CALL_TIMEOUT", "120000");//set it back to a healthy value = 2min
        }

    }

    public void testGetServiceManager() {

        try {
            IServiceManager serviceManager = rmiManager.getServiceManager(handleID);
            serviceManager.exists("chat", 4.0f);
        } catch (RemoteException e) {
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        }
    }

    public void testGetFPSManager() {

        try {
            IFPSManager fpsManager = rmiManager.getFPSManager(handleID);
            fpsManager.isPeerRunning("fps_test");
        } catch (RemoteException e) {
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        }
    }

    public void testGetServiceProviderManager() {

        try {
            IServiceProviderManager serviceProviderManager = rmiManager.getServiceProviderManager(handleID);
            serviceProviderManager.getServerName();
        } catch (RemoteException e) {
            System.err.println(TEST_FAILURE + ":: RemoteException : " + e.getMessage());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        }
    }

    public void testLogout() {

        try {
            rmiManager.logout(handleID);
            try {
                rmiManager.getServiceManager(handleID);
            } catch (Exception e) {
                Assert.assertTrue(TEST_SUCCESS, true);
            }
        } catch (RemoteException e) {
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (ServiceException e) {
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        }
        try {
            IEventProcessManager eventProcessManager = rmiManager.getEventProcessManager(handleID);
        } catch (Exception e) {
            Assert.assertTrue(TEST_SUCCESS, true);
        }
    }

    public void testMyFinally() {
        //TODO:use this to cleanup any temp files created
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();
        methodsOrder.add("testLogin");
        methodsOrder.add("testGetEventProcessManager");
        methodsOrder.add("testGetServiceManager");
        methodsOrder.add("testGetFPSManager");
        methodsOrder.add("testGetServiceProviderManager");
        methodsOrder.add("testLogout");
        methodsOrder.add("testRmiCallTimeOut");

        methodsOrder.add("testMyFinally");

        return methodsOrder;
    }

}