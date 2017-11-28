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
package com.fiorano.esb.junit.scenario;

import com.fiorano.stf.test.core.RMITestCase;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.dashboard.client.ui.data.ServerDetails;

import javax.management.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Oct 15, 2007
 * Time: 4:03:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerDetailerTest extends RMITestCase {
    private boolean isReady = false;
    private ObjectName objName1;
    private ObjectName objName2;
    private ObjectName objName3;
    private ObjectName objName4;
    private ObjectName objName5;

    public ServerDetailerTest(String name) {
        super(name);
    }

    public ServerDetailerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() {
        if (isReady == true) return;
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            objName1 = new ObjectName("Fiorano.etc:ServiceType=MemoryManager,Name=MemoryManager");
            objName2 = new ObjectName("Fiorano.Esb.Fps.Controller:ServiceType=FPSController,Name=FPSController");
            objName3 = new ObjectName("Fiorano.Esb.Server:ServiceType=FESServer,Name=FESServer");
            objName4 = new ObjectName("Fiorano.Esb.Log:ServiceType=LogManager,Name=ESBLogManager");
            objName5 = new ObjectName("Fiorano.Esb.Webconsole.Controller:ServiceType=WebconsoleController,Name=WebconsoleController");
            isReady = true;
            JMXClient.connect("admin", "passwd");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        isReady = true;
    }

    public static Test suite() {
        return new TestSuite(ServerDetailerTest.class);
    }

    public void testGetFESServerDetails() {

        try {
            ServerDetails sdFES = new ServerDetails();
            long memoryused, memoryMax;
            memoryused = ((Long) jmxClient.invoke(objName1, "getUsedMemory", new Object[]{}, new String[]{})).longValue();
            memoryMax = ((Long) jmxClient.invoke(objName1, "getMaxMemory", new Object[]{}, new String[]{})).longValue();
            sdFES.setMemoryUsage((memoryused / 1024) + "K" + "/" + (memoryMax / 1024) + "K");
            String spName = (String) jmxClient.invoke(objName3, "getServerName", new Object[]{}, new String[]{});
            Assert.assertNotNull(spName);
        } catch (ReflectionException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetFPSServerDetails() {

        try {
            ArrayList fpsNames = getAllFPSNames();
            String memusage;// = null;
            for (int i = 0; i < fpsNames.size(); i++) {
                Object[] params = {(String) fpsNames.get(i)};
                String[] signatures = {String.class.getName()};
                if (isFPSRunning((String) fpsNames.get(i))) {
                    memusage = (String) jmxClient.invoke(objName2, "getFPSMemoryUsage", params, signatures);
                }
            }
        } catch (ReflectionException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public ArrayList getAllFPSNames() {

        if (isReady) {
            try {
                return (ArrayList) jmxClient.invoke(objName2, "getAllTPSNamesList", new Object[]{}, new String[]{});
            } catch (ReflectionException e) {

                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            } catch (IOException e) {


                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            } catch (InstanceNotFoundException e) {

                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            } catch (MBeanException e) {

                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            }
        }
        return null;
    }

    public boolean isFPSRunning(String fpsName) {

        if (isReady) {
            try {
                Object[] params = {fpsName};
                String[] signatures = {String.class.getName()};
                return ((Boolean) jmxClient.invoke(objName2, "isTPSRunning", params, signatures)).booleanValue();
            } catch (ReflectionException e) {

                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            } catch (IOException e) {

                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            } catch (InstanceNotFoundException e) {

                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            } catch (MBeanException e) {

                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            }
        }
        return false;
    }

    public void testGetServerDetails() {

        String serverName = "fps_test";
        Object[] params = {serverName};
        String[] signatures = {String.class.getName()};
        try {
            HashMap serverDetails = (HashMap) jmxClient.invoke(objName5, "getServerDetails", params, signatures);
            Assert.assertNotNull(serverDetails);
        } catch (ReflectionException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetFESServerErrLogs() {

        Object[] params = {new Integer(100)};
        String[] signatures = {int.class.getName()};
        try {
            String logs = (String) jmxClient.invoke(objName4, "getTESLastErrLogs", params, signatures);
        } catch (ReflectionException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetFPSServerErrLogs() {

        String fpsName = "fps_test";
        try {
            if (isFPSRunning(fpsName)) {
                Object[] params = {new Integer(100), fpsName};
                String[] signatures = {int.class.getName(), String.class.getName()};
                String logs = (String) jmxClient.invoke(objName2, "getLastOutLogs", params, signatures);
            }
        } catch (ReflectionException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetFESServerOutLogs() {

        try {
            Object[] params = {new Integer(100)};
            String[] signatures = {int.class.getName()};
            String logs = (String) jmxClient.invoke(objName4, "getTESLastOutLogs", params, signatures);
        }
        catch (ReflectionException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetQueues() {

        String serverName = "EnterpriseServer";
        try {
            Object[] params = {serverName};
            String[] signatures = {String.class.getName()};
            HashMap queues = (HashMap) jmxClient.invoke(objName5, "getQueues", params, signatures);
        }
        catch (ReflectionException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetTopics() {

        String serverName = "EnterpriseServer";
        try {
            Object[] params = {serverName};
            String[] signatures = {String.class.getName()};
            HashMap topics = (HashMap) jmxClient.invoke(objName5, "getTopics", params, signatures);
        } catch (ReflectionException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetConnections() {

        String serverName = "EnterpriseServer";
        try {
            Object[] params = {serverName};
            String[] signatures = {String.class.getName()};
            HashMap connections = (HashMap) jmxClient.invoke(objName5, "getConnections", params, signatures);
        } catch (ReflectionException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {

            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }


}


