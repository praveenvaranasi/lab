package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.remote.JMXClient;
import fiorano.tifosi.dmi.events.TifosiEvent;
import fiorano.tifosi.dmi.events.emailAlerts.AbstractEventAlert;
import fiorano.tifosi.dmi.events.emailAlerts.FPSStartStopAlert;
import fiorano.tifosi.events.TifosiEventTypes;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import fiorano.tifosi.dmi.events.emailAlerts.*;
import javax.management.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/19/11
 * Time: 2:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestEventDetailer extends AbstractTestNG{
    private ObjectName objName;
    private ObjectName objName2;
    private boolean isReady = false;
    private String handleId;
    private String appVersion="1.0";
    private ObjectName EventEmailNotifier;
    @BeforeClass(groups = "EventDetailerTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        if (isReady == true) return;
        try {
            objName = new ObjectName("Fiorano.Esb.Events:ServiceType=EventReciever,Name=ESBEventReciever");
            objName2 = new ObjectName("Fiorano.Esb.Events:ServiceType=EventEmailNotifier,Name=ESBEventEmailNotifier");
            isReady = true;
            JMXClient client =JMXClient.getInstance();
            handleId = client.getHandleId();
        }
        catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    @Test(groups = "EventDetailerTest", alwaysRun = true, dependsOnMethods = "startSetUp")
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
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "EventDetailerTest", alwaysRun = true, dependsOnMethods = "testSetMaxBufferedEvents")
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
            Object[] params = {handleId, noevents, eventType, TifosiEvent.NORMAL_CATEGORY, filter, appInstName,appVersion,servInstName, fpsName, source};
            String[] signatures = {String.class.getName(), int.class.getName(), int.class.getName(), String.class.getName(), int.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),String.class.getName()};
            ArrayList eventList = (ArrayList) jmxClient.invoke(objName, "getEvents", params, signatures);
            Assert.assertNotNull(eventList);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "EventDetailerTest", alwaysRun = true, dependsOnMethods = "testGetEventDetails")
    public void testGetArchivedEvents() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            String appInstName = "";
            String servInst = "";
            Integer eventTypeToSearchFrom = new Integer(1);
            Integer fromIndex = new Integer(0);
            Integer toIndex = new Integer(-1);
            Object[] params = {appInstName,appVersion,servInst,eventTypeToSearchFrom, fromIndex, toIndex};
            String[] signatures = {String.class.getName(), String.class.getName(),String.class.getName(), int.class.getName(), int.class.getName(), int.class.getName()};
            ArrayList eventList = (ArrayList) jmxClient.invoke(objName, "getArchivedEvents", params, signatures);
            Assert.assertNotNull(eventList);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "EventDetailerTest", alwaysRun = true, dependsOnMethods = "testGetArchivedEvents")
    public void testGetTotalNumberOfEvents2() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            Integer eventType = new Integer(1);
            String appInstName = "SIMPLECHAT";
            Object[] params = {eventType, appInstName,appVersion};
            String[] signatures = {int.class.getName(), String.class.getName(),String.class.getName()};
            int numEvents = ((Integer) jmxClient.invoke(objName, "getTotalNumberOfEvents", params, signatures)).intValue();
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "EventDetailerTest", alwaysRun = true, dependsOnMethods = "testGetTotalNumberOfEvents2")
    public void testGetTotalNumberOfEvents3() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            Integer eventType = new Integer(1);
            String appInstName = "SIMPLECHAT";
            String servInst = "chat1";
            Object[] params = {eventType, appInstName,appVersion,servInst};
            String[] signatures = {int.class.getName(), String.class.getName(),String.class.getName(),String.class.getName()};
            int numEvents = ((Integer) jmxClient.invoke(objName, "getTotalNumberOfEvents", params, signatures)).intValue();
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "EventDetailerTest", alwaysRun = true, dependsOnMethods = "testGetTotalNumberOfEvents3")
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
            Object[] params = {handleId,appInstName,appVersion,servInst, tpsName, source,eventType, TifosiEvent.NORMAL_CATEGORY, from, to, fromIndex, toIndex};
            String[] signatures = {String.class.getName(),String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),String.class.getName(), int.class.getName(), String.class.getName(), Date.class.getName(), Date.class.getName(), int.class.getName(), int.class.getName()};
            ArrayList eventList = (ArrayList) jmxClient.invoke(objName, "searchEvents", params, signatures);
            Assert.assertNotNull(eventList);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "EventDetailerTest", alwaysRun = true, dependsOnMethods = "testSearchEvents")
    public void testAddEmailID() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            String eventType = "FPS_LAUNCH_KILL";
            AbstractEventAlert.EventCategory eventCategory = AbstractEventAlert.EventCategory.WARNING;
            String toEmailID = "aravind.thangavel@in.fiorano.com";
            String fromName = "STF";
            String fromEmailID = "aravind.thangavel@in.fiorano.com";

            String params = "fps_test";

            ArrayList<String> emailDetails = new ArrayList<String>();
            emailDetails.add(0,toEmailID);
            emailDetails.add(1,fromName);
            emailDetails.add(2,fromEmailID);
            FPSStartStopAlert fpsStartStopAlert = new FPSStartStopAlert();
           // setParentParams(fpsStartStopAlert, "STFAlert", eventCategory, emailDetails);
            fpsStartStopAlert.setFpsName(params);
            AbstractEventAlert objabsEventAlert = (AbstractEventAlert)fpsStartStopAlert;
            Object[] parameters = new Object[]{objabsEventAlert, eventType, eventCategory+""};


            String[] signatures = {AbstractEventAlert.class.getName(), String.class.getName(), String.class.getName()};

            jmxClient.invoke(objName2, "addEmailID", parameters, signatures);
        }   catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("", ""), e);
            e.printStackTrace();
        }
    }

    @Test(groups = "EventDetailerTest", alwaysRun = true, dependsOnMethods = "testAddEmailID")
    public void testRemoveEmailID() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {  String eventType = "FPS_LAUNCH_KILL";
            AbstractEventAlert.EventCategory eventCategory = AbstractEventAlert.EventCategory.WARNING;
            String toEmailID = "aravind.thangavel@in.fiorano.com";
            String fromName = "STF";
            String fromEmailID = "aravind.thangavel@in.fiorano.com";

            String params = "fps_test";

            ArrayList<String> emailDetails = new ArrayList<String>();
            emailDetails.add(0,toEmailID);
            emailDetails.add(1,fromName);
            emailDetails.add(2,fromEmailID);
            FPSStartStopAlert fpsStartStopAlert = new FPSStartStopAlert();
            // setParentParams(fpsStartStopAlert, "STFAlert", eventCategory, emailDetails);

            fpsStartStopAlert.setFpsName(params);
            AbstractEventAlert objabsEventAlert = (AbstractEventAlert)fpsStartStopAlert;
            Object[] parameters = new Object[]{eventType, eventCategory+""};


            String[] signatures = {String.class.getName(), String.class.getName()};
            jmxClient.invoke(objName2, "removeEmailID", parameters, signatures);
        }  catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("", ""), e);
            e.printStackTrace();
        }

    }

    @Test(groups = "EventDetailerTest", alwaysRun = true, dependsOnMethods = "testRemoveEmailID")
    public void testGetAllEmailIDs() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
           ArrayList emails = (ArrayList) jmxClient.invoke(objName2, "getAllEmailIDs", new Object[]{}, new String[]{});
            Assert.assertNotNull(emails);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }
}
