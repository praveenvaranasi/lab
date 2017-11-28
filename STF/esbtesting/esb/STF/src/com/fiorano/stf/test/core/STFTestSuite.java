/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.stf.test.core;

import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.remote.RTLClient;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.jndi.InitialContextFactory;
import junit.framework.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: May 22, 2007
 * Time: 4:29:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class STFTestSuite extends TestSuite {

    private List peerServerList;

    private JMXClient jmxClient;
    private RMIClient rmiClient;
    private RTLClient rtlClient;
    private final int RETRIES = 10;
    private STFTestCase currentRunningTestCase;


    public STFTestSuite(String name, List peerServerList) {
        setName(name);
        Assert.assertNotNull(peerServerList);
        this.peerServerList = peerServerList;
    }

    /**
     * This will add the tests from the given class to the suite
     */
    public void addTestSuite(TestCase testCase, ServerStatusController controller) {
        try {
            if (testCase instanceof DRTTestCase)
                addDRTTestSuite((DRTTestCase) testCase, controller);
            else
                throw new AssertionError("Found unsupported testcase type: " + testCase.getName());
        }
        catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
            throw new AssertionError("Failed to load TestSuite class: " + ((DRTTestCase) testCase).getTestCaseClassPath());
        }
    }

    private void addDRTTestSuite(DRTTestCase testCase, ServerStatusController controller) throws ClassNotFoundException {
        String className = ((DRTTestCase) testCase).getTestCaseClassPath();
        Class testCaseClass = Class.forName(className);
        try {
            testCaseClass.getConstructor(new Class[]{TestCaseElement.class, ServerStatusController.class});
        } catch (NoSuchMethodException e) {
            // fall through
        }

        if (!Modifier.isPublic(testCaseClass.getModifiers()))
            throw new AssertionError("Class " + testCaseClass.getName() + " is not public");

        Class superClass = testCaseClass;
        List names = new ArrayList();

        Method order;
        ArrayList methodsOrder;
        boolean orderExists;

        while (Test.class.isAssignableFrom(superClass)) {

            try {
                orderExists = true;
                order = superClass.getMethod("getOrder", null);
            }
            catch (NoSuchMethodException e) {
                orderExists = false;
            }

            if (orderExists == true) {
                try {
                    order = superClass.getMethod("getOrder", null);
                    methodsOrder = (ArrayList) order.invoke(null, null);

                    for (int index = 0; index < methodsOrder.size(); index++) {
                        try {
                            if (checkForTestMethod(superClass.getMethod(String.valueOf(methodsOrder.get(index)), null), names, testCaseClass)) {
                                Test test = createSTFTTest(testCaseClass, testCase.getTestConfig(), controller);
                                if (test instanceof TestCase)
                                    ((TestCase) test).setName(String.valueOf(methodsOrder.get(index)));
                                addTest(test);
                            }
                        }
                        catch (NoSuchMethodException e) {
                            exceptionToString(e);
                        }
                    }
                }
                catch (Exception e) {
                    System.out.println(e);
                    exceptionToString(e);
                }
            } else {
                Method[] methods = superClass.getDeclaredMethods();

                for (int index = 0; index < methods.length; index++) {
                    if (checkForTestMethod(methods[index], names, testCaseClass)) {
                        Test test = createSTFTTest(testCaseClass, testCase.getTestConfig(), controller);
                        if (test instanceof TestCase)
                            ((TestCase) test).setName(methods[index].getName());
                        addTest(test);
                    }
                }
            }
            superClass = superClass.getSuperclass();
        }

        if (names.size() == 0)
            new AssertionError("No tests found in " + testCaseClass.getName());
    }

    private boolean checkForTestMethod(Method m, List names, Class theClass) {
        String name = m.getName();
        if (names.contains(name))
            return false;
        if (!isPublicTestMethod(m)) {
            if (isTestMethod(m))
                throw new AssertionError("Test method isn't public: " + m.getName());
            return false;
        }
        names.add(name);
        return true;
    }

    private boolean isPublicTestMethod(Method m) {
        return isTestMethod(m) && Modifier.isPublic(m.getModifiers());
    }

    private boolean isTestMethod(Method m) {
        return
                m.getParameterTypes().length == 0 &&
                        m.getName().startsWith("test") &&
                        m.getReturnType().equals(Void.TYPE);
    }


    private Test createSTFTTest(Class theClass, TestCaseElement testCaseInfo, ServerStatusController controller) {
        Constructor constructor;
        try {
            constructor = getTestConstructor(theClass);

        } catch (NoSuchMethodException e) {
            throw new AssertionError("Class " + theClass.getName() + " has no public constructor with <TestCaseElement testCaseConfig>");
        }
        Object test;
        try {
            test = constructor.newInstance(new Object[]{testCaseInfo}); //breakthrough  (muahahahhaha)
        } catch (InstantiationException e) {
            throw new AssertionError("Cannot instantiate test case: " + testCaseInfo.getName() + " (" + exceptionToString(e) + ")");
        } catch (InvocationTargetException e) {
            throw new AssertionError("Exception in constructor: " + testCaseInfo.getName() + " (" + exceptionToString(e.getTargetException()) + ")");
        } catch (IllegalAccessException e) {
            throw new AssertionError("Cannot access test case: " + testCaseInfo.getName() + " (" + exceptionToString(e) + ")");
        }
        return (Test) test;
    }

    public static Constructor getTestConstructor(Class theClass) throws NoSuchMethodException {
        return theClass.getConstructor(new Class[]{TestCaseElement.class});             //breakthrough (muahahahha!!)
    }

    public void run(TestResult result) {
        try {
            setUp();
        } catch (STFException e) {
            e.printStackTrace();
            /*Error err = new AssertionError("Failed to setup STFTestSuite: ");
            err.initCause(e);
            throw err;*/
            result.addError(this, e);
            return;
        }
        for (Enumeration tests = tests(); tests.hasMoreElements();) {
            Test test = (Test) tests.nextElement();
            if (test instanceof STFTestCase) {
                ((STFTestCase) test).setJMXClient(jmxClient);
                ((STFTestCase) test).setRMIClient(rmiClient);
                ((STFTestCase) test).setRtlClient(rtlClient);
                ((STFTestCase) test).setSuite(this);
            }
            if (result.shouldStop())
                break;
            currentRunningTestCase = (STFTestCase) test;
            try {
                super.runTest(test, result);
            } finally {
                currentRunningTestCase = null;
            }
        }
        tearDown();
    }

    private void setUp() throws STFException {
        rtlClient = RTLClient.getInstance();
        rmiClient = RMIClient.getInstance();
        jmxClient = JMXClient.getInstance();

        for (Iterator peers = peerServerList.iterator(); peers.hasNext();) {
            String peer = (String) peers.next();
            waitForPeerStartUp(peer);
        }


    }

    public boolean waitForPeerStartUp(String peer) throws STFException {
        for (int i = 0; i < RETRIES; i++) {
            try {
                if (rtlClient.getFioranoFPSManager().isTPSRunning(peer))
                    return true;
            } catch (TifosiException e1) {
                throw new STFException("Failed to get get status of Peer Server: " + peer, e1);
            }
            try {
                synchronized (this) {
                    System.out.println("Waiting for the Peer Server " + peer + " to Start.");
                    this.wait(30000);
                }
            } catch (InterruptedException e1) {
            }

        }
        return false;
    }

    private void tearDown() {
        if(rtlClient!=null)
            rtlClient.cleanUp();
        if(rmiClient!=null)
            rmiClient.cleanUp();
        if(jmxClient!=null)
            jmxClient.cleanUp();
    }

    private static String exceptionToString(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        return stringWriter.toString();
    }


    public void updateFioranoServiceProvider() throws STFException {
        setUp();
        if (currentRunningTestCase != null) {
            //updating these for the currently eecuting test case
            (currentRunningTestCase).setRtlClient(rtlClient);
            (currentRunningTestCase).setRMIClient(rmiClient);
            (currentRunningTestCase).setJMXClient(jmxClient);
        }
    }

    public void updateRemoteClients() throws STFException {
        rtlClient = RTLClient.getInstance();
        rmiClient = RMIClient.getInstance();
        jmxClient = JMXClient.getInstance();

        if (currentRunningTestCase != null) {
            //updating these for the currently eecuting test case
            (currentRunningTestCase).setRtlClient(rtlClient);
            (currentRunningTestCase).setRMIClient(rmiClient);
            (currentRunningTestCase).setJMXClient(jmxClient);
        }
    }
}
