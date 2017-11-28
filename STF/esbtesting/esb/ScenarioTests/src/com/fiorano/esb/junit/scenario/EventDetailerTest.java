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
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.remote.JMXClient;

import javax.management.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import fiorano.tifosi.dmi.events.TifosiEvent;
import fiorano.tifosi.events.TifosiEventTypes;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Oct 15, 2007
 * Time: 3:45:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventDetailerTest extends RMITestCase {
    private ObjectName objName;
    private ObjectName objName2;
    private boolean isReady = false;
    private String handleId;

    public EventDetailerTest(String name) {
        super(name);
    }

    public EventDetailerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public void setUp() {
        if (isReady == true) return;
        try {
            objName = new ObjectName("Fiorano.Esb.Events:ServiceType=EventReciever,Name=ESBEventReciever");
            objName2 = new ObjectName("Fiorano.Esb.Events:ServiceType=EventEmailNotifier,Name=ESBEventEmailNotifier");
            isReady = true;
            JMXClient client =JMXClient.getInstance();
            handleId = client.getHandleId();
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        }
        isReady = true;
    }

    public static Test suite() {
        return new TestSuite(EventDetailerTest.class);
    }

    public void testSetMaxBufferedEvents() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            Integer noe = new Integer(10);
            Object[] params = {noe};
            String[] signatures = {int.class.getName()};
            jmxClient.invoke(objName, "setMaxBufferedEventsCount", params, signatures);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetEventDetails() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            Integer noevents = new Integer(10);
            Integer eventType = new Integer(TifosiEventTypes.APPLICATION_EVENT);
            Integer filter = new Integer(-1);
            String appInstName = "SIMPLECHAT";
            String servInstName = "chat1";
            String fpsName = "fps_test";
            String source = "chat1";
            Object[] params = {handleId, noevents, eventType, TifosiEvent.NORMAL_CATEGORY, filter, appInstName, servInstName, fpsName, source};
            String[] signatures = {String.class.getName(), int.class.getName(), int.class.getName(), String.class.getName(), int.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()};
            ArrayList eventList = (ArrayList) jmxClient.invoke(objName, "getEvents", params, signatures);
            Assert.assertNotNull(eventList);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetArchivedEvents() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            String appInstName = "";
            String servInst = "";
            Integer eventTypeToSearchFrom = new Integer(1);
            Integer fromIndex = new Integer(0);
            Integer toIndex = new Integer(-1);
            Object[] params = {appInstName, servInst, eventTypeToSearchFrom, fromIndex, toIndex};
            String[] signatures = {String.class.getName(), String.class.getName(), int.class.getName(), int.class.getName(), int.class.getName()};
            ArrayList eventList = (ArrayList) jmxClient.invoke(objName, "getArchivedEvents", params, signatures);
            Assert.assertNotNull(eventList);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetTotalNumberOfEvents2() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            Integer eventType = new Integer(1);
            String appInstName = "SIMPLECHAT";
            Object[] params = {eventType, appInstName};
            String[] signatures = {int.class.getName(), String.class.getName()};
            int numEvents = ((Integer) jmxClient.invoke(objName, "getTotalNumberOfEvents", params, signatures)).intValue();
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetTotalNumberOfEvents3() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            Integer eventType = new Integer(1);
            String appInstName = "SIMPLECHAT";
            String servInst = "chat1";
            Object[] params = {eventType, appInstName, servInst};
            String[] signatures = {int.class.getName(), String.class.getName(), String.class.getName()};
            int numEvents = ((Integer) jmxClient.invoke(objName, "getTotalNumberOfEvents", params, signatures)).intValue();
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testSearchEvents() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            String appInstName = "SIMPLECHAT";
            String servInst = "chat1";
            String tpsName = "fps_test";
            String source = "chat1";
            Integer eventType = new Integer(TifosiEventTypes.APPLICATION_EVENT);
            Date from = new Date();
            Date to = new Date();
            Integer fromIndex = new Integer(0);
            Integer toIndex = new Integer(-1);
            Object[] params = {handleId,appInstName, servInst, tpsName, source, eventType, TifosiEvent.NORMAL_CATEGORY, from, to, fromIndex, toIndex};
            String[] signatures = {String.class.getName(),String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), int.class.getName(), String.class.getName(), Date.class.getName(), Date.class.getName(), int.class.getName(), int.class.getName()};
            ArrayList eventList = (ArrayList) jmxClient.invoke(objName, "searchEvents", params, signatures);
            Assert.assertNotNull(eventList);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testAddEmailID() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            String toEmailID = "rajagopal.m@in.fiorano.com";
            String fromName = "Raja Gopal";
            String fromEmailID = "rajagopal.m@in.fiorano.com";
            String eventType = new String("FPS_LAUNCH_KILL");
            String params = "fps_test";
            ArrayList<String> emailDetails = new ArrayList<String>();
            emailDetails.add(0,toEmailID);
            emailDetails.add(1,fromName);
            emailDetails.add(2,fromEmailID);
            Object[] parameters = {emailDetails, eventType, TifosiEvent.NORMAL_CATEGORY, params};
            String[] signatures = {ArrayList.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "addEmailID", parameters, signatures);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testRemoveEmailID() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            String toEmailID = "krishna.aditya@fiorano.com";
            String fromName = "Raja Gopal";
            String fromEmailID = "rajagopal.m@in.fiorano.com";
            String params = "fps_test";
            String eventType = new String("FPS_LAUNCH_KILL");
            ArrayList<String> emailDetails = new ArrayList<String>();
            emailDetails.add(0,toEmailID);
            emailDetails.add(1,fromName);
            emailDetails.add(2,fromEmailID);
            Object[] parameters = {emailDetails, eventType, TifosiEvent.NORMAL_CATEGORY, params};
            String[] signatures = {ArrayList.class.getName(), String.class.getName(), String.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "removeEmailID", parameters, signatures);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();               
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetAllEmailIDs() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            Hashtable emails = (Hashtable) jmxClient.invoke(objName2, "getAllEmailIDs", new Object[]{}, new String[]{});
            Assert.assertNotNull(emails);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }
}
