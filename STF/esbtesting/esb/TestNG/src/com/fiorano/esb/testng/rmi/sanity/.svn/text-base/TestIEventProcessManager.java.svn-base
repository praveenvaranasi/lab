package com.fiorano.esb.testng.rmi.sanity;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.bugs.TestConstants;
import com.fiorano.esb.testng.rmi.bugs.TestIRepositoryEventListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.application.Route;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Feb 22, 2010
 * Time: 11:49:49 AM
 * To change this template use File | Settings | File Templates.
 */
@Test(dependsOnGroups = {"IRmiManager.*"})
public class TestIEventProcessManager extends AbstractTestNG {
    public IEventProcessManager eventProcessManager;
    public IEventProcessManagerListener eventProcessManagerListener;
    public EventProcessReference[] epr;
    private static Hashtable<String, String> resultMap = new Hashtable<String, String>();
    public int numberofServices; // gives the number of services available of an application. Here "SIMPLECHAT" is used.
    public String EventProcess_Name = "SIMPLECHAT";
    public float appVersion = 1.0f;
    public float Service_Version = 4.0f;

    @Test(groups = "IEventProcessManager", alwaysRun = true)
    public void start_IEventProcessManager() {
        Logger.LogMethodOrder(Logger.getOrderMessage("start_IEventProcessManager", "TestIEventProcessManager"));
        eventProcessManager = TestIRmiManager.geteventProcessManager();
        Logger.Log(Level.INFO, Logger.getFinishMessage("start_IEventProcessManager", "TestIEventProcessManager"));
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "start_IEventProcessManager", alwaysRun = true)
    public void TestaddEventProcessListener() {
        try {
            // adds the EventProcessListener to the eventprocessmanager
            Logger.LogMethodOrder(Logger.getOrderMessage("TestaddEventProcessListener", "TestIEventProcessManager"));
            eventProcessManagerListener = new TestIEventProcessManagerListener("SIMPLECHAT");
            eventProcessManager.addEventProcessListener(eventProcessManagerListener);

            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {

            }

            eventProcessManager.checkResourcesAndConnectivity("SIMPLECHAT", appVersion);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            eventProcessManager.startEventProcess("SIMPLECHAT", appVersion, false);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (resultMap.get(TestConstants.EVENTPROCESS_LAUNCH_SUCCESSFUL).equals("SIMPLECHAT")) {
                // Test passed Successfully
                eventProcessManager.stopEventProcess("SIMPLECHAT", appVersion);
                try {
                    Thread.sleep(Thread_Sleep_Time);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                Assert.fail();
            }
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestaddEventProcessListener", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddEventProcessListener", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddEventProcessListener", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TestaddEventProcessListener", alwaysRun = true)
    public void TestaddRepositoryEventListener() {
        try {
            // adds the repositoryeventListener to the eventprocessmanager
            Logger.LogMethodOrder(Logger.getOrderMessage("TestaddRepositoryEventListener", "TestIEventProcessManager"));
            IRepositoryEventListener irel = new TestIRepositoryEventListener();
            eventProcessManager.addRepositoryEventListener(irel);
            resultMap.clear();


            deployEventProcess("sample_event_process-soa922.zip");
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {

            }

            if (resultMap.get(TestConstants.EVENTPROCESS_DEPLOYED_SUCCESSFUL).equals("SAMPLE_EVENT_PROCESS")) {
                // test passed Successfully
            } else {
                Assert.fail();
            }
            resultMap.clear();

            Logger.Log(Level.INFO, Logger.getFinishMessage("TestaddRepositoryEventListener", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddRepositoryEventListener", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddRepositoryEventListener", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddRepositoryEventListener", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TestaddRepositoryEventListener", alwaysRun = true)
    public void TestcheckResourcesAndConnectivity() {
        try {
            // does CRC on an eventprocess
            Logger.LogMethodOrder(Logger.getOrderMessage("TestcheckResourcesAndConnectivity", "TestIEventProcessManager"));
            eventProcessManager.checkResourcesAndConnectivity("SIMPLECHAT", appVersion);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestcheckResourcesAndConnectivity", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestcheckResourcesAndConnectivity", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestcheckResourcesAndConnectivity", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TestcheckResourcesAndConnectivity", alwaysRun = true)
    public void TeststartEventProcess() {
        try {
            //starts an eventprocess of given name and version
            Logger.LogMethodOrder(Logger.getOrderMessage("TeststartEventProcess", "TestIEventProcessManager"));
            resultMap.clear();

            eventProcessManager.startEventProcess("SIMPLECHAT", appVersion, false);

            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {

            }

            int serviceCount = 0;
            EventProcessStateData epsd = eventProcessManager.getApplicationStateDetails("SIMPLECHAT",appVersion);
            Enumeration allServices = epsd.getAllServiceNames();
            while (allServices.hasMoreElements()) {
                String serviceName = (String) allServices.nextElement();
                serviceCount++;
            }
            numberofServices = serviceCount;

            eventProcessManager.startAllServices("SIMPLECHAT",appVersion);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {

            }
            int count = Integer.parseInt(resultMap.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + "SIMPLECHAT"));
            Assert.assertEquals(count, serviceCount);

            if (resultMap.get(TestConstants.EVENTPROCESS_LAUNCH_SUCCESSFUL).equals("SIMPLECHAT")) {
                // Test passed successfully
            } else {
                Assert.fail();
            }

            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TeststartEventProcess", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststartEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststartEventProcess", "TestIEventProcessManager"), e);

            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TeststartEventProcess", alwaysRun = true)
    public void TestDeleteRunningAppTest() {

        try {
            eventProcessManager.deleteEventProcess("SIMPLECHAT", appVersion, true, false);
        } catch (ServiceException e) {
            // Test passed Successfully
        } catch (Exception e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeleteRunningAppTest", "TestIEventProcessManager"), e);
            Assert.fail();
        }

        boolean statuscrc = false;

        try {
            eventProcessManager.checkResourcesAndConnectivity("SIMPLECHAT", appVersion);
        } catch (Exception e) {
            statuscrc = true;
        }
        Assert.assertEquals(statuscrc, false);
    }


    @Test(groups = "IEventProcessManager", dependsOnMethods = {"TestDeleteRunningAppTest"}, alwaysRun = true)
    public void TestgetApplicationStateDetails() {
        try {
            // returns applicationStateDetails of the given application
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetApplicationStateDetails", "TestIEventProcessManager"));
            Assert.assertEquals(eventProcessManager.getApplicationStateDetails("SIMPLECHAT",appVersion).getEventProcessID(), "SIMPLECHAT");
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetApplicationStateDetails", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetApplicationStateDetails", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetApplicationStateDetails", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TeststartEventProcess", alwaysRun = true)
    public void TestexportApplicationLogs() {
        try {
            // exports the application logs of the given application, version and given long index
            Logger.LogMethodOrder(Logger.getOrderMessage("TestexportApplicationLogs", "TestIEventProcessManager"));
            // exports the application logs into a byte array
            byte logs[] = eventProcessManager.exportApplicationLogs("SIMPLECHAT", appVersion, 0);
            Assert.assertEquals(logs.length > 0, true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestexportApplicationLogs", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestexportApplicationLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestexportApplicationLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TeststartEventProcess", alwaysRun = true)
    public void TestChangeRouteSelector() {
        try {
            //Changes the message selector for a route instance while the application is running.
            Logger.LogMethodOrder(Logger.getOrderMessage("TestChangeRouteSelector", "TestIEventProcessManager"));
            HashMap selectors = new HashMap();
            selectors.put(Route.SELECTOR_SENDER, "chat2");
            eventProcessManager.changeRouteSelector("SIMPLECHAT",appVersion, "route1", selectors);
            Thread.sleep(Thread_Sleep_Time);
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangeRouteSelector", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestChangeRouteSelector", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TeststartEventProcess", alwaysRun = true)
    public void TestexportServiceLogs() {
        try {
            // exxports the service logs of a service of a given application and of given version
            Logger.LogMethodOrder(Logger.getOrderMessage("TestexportServiceLogs", "TestIEventProcessManager"));
            // exports the service logs into a byte array
            byte byt2[] = eventProcessManager.exportServiceLogs("SIMPLECHAT", appVersion, "chat1", 0);
            Assert.assertEquals(byt2.length > 0, true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestexportServiceLogs", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestexportServiceLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestexportServiceLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TestexportApplicationLogs", alwaysRun = true)
    public void TestclearApplicationLogs() {
        try {
            // clears the application logs of the given application and version
            Logger.LogMethodOrder(Logger.getOrderMessage("TestclearApplicationLogs", "TestIEventProcessManager"));
            eventProcessManager.clearApplicationLogs("SIMPLECHAT", appVersion);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assert.assertEquals(eventProcessManager.getLastOutTrace(1, "chat1", "SIMPLECHAT", appVersion).length(), 0);
            Assert.assertEquals(eventProcessManager.getLastErrTrace(1, "chat1", "SIMPLECHAT", appVersion).length(), 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestclearApplicationLogs", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestclearApplicationLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestclearApplicationLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TestexportServiceLogs", alwaysRun = true)
    public void TestclearServiceErrLogs() {
        try {
            // clears the service error logs of the given service of a given application and version
            Logger.LogMethodOrder(Logger.getOrderMessage("TestclearServiceErrLogs", "TestIEventProcessManager"));
            String logs1 = eventProcessManager.getLastErrTrace(7, "chat1", "SIMPLECHAT", appVersion);
            if (logs1.length() > 0) {
                eventProcessManager.clearServiceErrLogs("chat1", "SIMPLECHAT", appVersion);
                Assert.assertEquals(eventProcessManager.getLastErrTrace(7, "chat1", "SIMPLECHAT", appVersion).length(), 0);
            }
            eventProcessManager.clearServiceErrLogs("chat1", "SIMPLECHAT", appVersion);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestclearServiceErrLogs", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestclearServiceErrLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestclearServiceErrLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TestexportServiceLogs", alwaysRun = true)
    public void TestclearServiceOutLogs() {
        try {
            // clears the service out logs of a given service of a given application and of a given version
            Logger.LogMethodOrder(Logger.getOrderMessage("TestclearServiceOutLogs", "TestIEventProcessManager"));
            String logs2 = eventProcessManager.getLastOutTrace(7, "chat1", "SIMPLECHAT", appVersion);
            if (logs2.length() > 0) {
                eventProcessManager.clearServiceOutLogs("chat1", "SIMPLECHAT", appVersion);
                Assert.assertEquals(eventProcessManager.getLastOutTrace(7, "chat1", "SIMPLECHAT", appVersion).length(), 0);
            }
            eventProcessManager.clearServiceOutLogs("chat1", "SIMPLECHAT", appVersion);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestclearServiceOutLogs", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestclearServiceOutLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestclearServiceOutLogs", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TestaddRepositoryEventListener", alwaysRun = true)
    public void TestdeleteEventProcess() {
        try {
            // deletes the event process from the repository
            Logger.LogMethodOrder(Logger.getOrderMessage("TestdeleteEventProcess", "TestIEventProcessManager"));
            resultMap.clear();

            eventProcessManager.deleteEventProcess("SAMPLE_EVENT_PROCESS", appVersion, true, false);
            Thread.sleep(Thread_Sleep_Time);

            if ((resultMap.get(TestConstants.EVENTPROCESS_DELETE_SUCCESSFUL)).equals("SAMPLE_EVENT_PROCESS")) {
                // Test passed successfully
            } else {
                Assert.fail();
            }

            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestdeleteEventProcess", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestdeleteEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            Assert.assertTrue(true);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestdeleteEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "start_IEventProcessManager", alwaysRun = true)
    public void TestgetAllEventProcesses() {
        try {
            // returns all the event processes in the repository
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetAllEventProcesses", "TestIEventProcessManager"));
            epr = eventProcessManager.getAllEventProcesses();
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assert.assertTrue(epr.length > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetAllEventProcesses", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetAllEventProcesses", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetAllEventProcesses", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = {"start_IEventProcessManager", "TestgetAllEventProcesses"}, alwaysRun = true)
    public void TestdependenciesExists() {
        try {
            // tests the dependency between service references and eventprocess references
            Logger.LogMethodOrder(Logger.getOrderMessage("TestdependenciesExists", "TestIEventProcessManager"));
            ServiceReference sr1 = new ServiceReference();
            ServiceReference sr2 = new ServiceReference();
            ServiceReference sr3 = new ServiceReference();
            ServiceReference sr4 = new ServiceReference();
            ServiceReference srr[] = {sr1, sr2, sr3, sr4};

            Assert.assertFalse(eventProcessManager.dependenciesExists(srr, epr));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestdependenciesExists", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestdependenciesExists", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestdependenciesExists", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "start_IEventProcessManager", alwaysRun = true)
    public void TestenableSBW() {
        try {
            // enables the SBW of a given application , service and of a given port
            Logger.LogMethodOrder(Logger.getOrderMessage("TestenableSBW", "TestIEventProcessManager"));
            eventProcessManager.startEventProcess("SIMPLECHAT", appVersion, false);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
            }

            eventProcessManager.enableSBW("chat1", "SIMPLECHAT",appVersion, "OUT_PORT", true, 2);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestenableSBW", "TestIEventProcessManager"));

            eventProcessManager.stopEventProcess("SIMPLECHAT", appVersion);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestenableSBW", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestenableSBW", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = {"start_IEventProcessManager", "TestenableSBW"}, alwaysRun = true)
    public void TestdisableSBW() {
        try {
            // disables the SBW of a given application , service and of the given port
            Logger.LogMethodOrder(Logger.getOrderMessage("TestdisableSBW", "TestIEventProcessManager"));
            eventProcessManager.startEventProcess("SIMPLECHAT", appVersion, false);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
            }

            eventProcessManager.disableSBW("chat1", "SIMPLECHAT",appVersion, "OUT_PORT");
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestdisableSBW", "TestIEventProcessManager"));

            eventProcessManager.stopEventProcess("SIMPLECHAT", appVersion);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
            }

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestdisableSBW", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestdisableSBW", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "start_IEventProcessManager", alwaysRun = true)
    public void Testexists() {
        try {
            // tests the existance of the eventprocess of given version in the repository
            Logger.LogMethodOrder(Logger.getOrderMessage("Testexists", "TestIEventProcessManager"));
            Assert.assertTrue(eventProcessManager.exists("SIMPLECHAT", appVersion));
            Logger.Log(Level.INFO, Logger.getFinishMessage("Testexists", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testexists", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testexists", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "start_IEventProcessManager", alwaysRun = true)
    public void TestgetEventProcess() {
        try {
            // return a byte array containing the eventprocess information of a given application and version
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetEventProcess", "TestIEventProcessManager"));
            byte eventprocessarray[] = eventProcessManager.getEventProcess("SIMPLECHAT", appVersion, 0);
            Assert.assertEquals(eventprocessarray.length > 0, true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetEventProcess", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "start_IEventProcessManager", alwaysRun = true)
    public void TestgetEventProcessIds() {
        try {
            // gets all the eventprocessIds exists in the repository
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetEventProcessIds", "TestIEventProcessManager"));
            Assert.assertTrue(eventProcessManager.getEventProcessIds().length > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetEventProcessIds", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetEventProcessIds", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetEventProcessIds", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TeststartEventProcess", alwaysRun = true)
    public void TestgetLastErrTrace() {
        try {
            // gets the last error trace of a given application,service and of given version. It  returns a string
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetLastErrTrace", "TestIEventProcessManager"));
            String gettrace = (eventProcessManager.getLastErrTrace(1, "chat1", "SIMPLECHAT", appVersion));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetLastErrTrace", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastErrTrace", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastErrTrace", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TeststartEventProcess", alwaysRun = true)
    public void TestgetLastOutTrace() {
        try {
            // returns the last out trace of a given application, service and of given version. It return a string
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetLastOutTrace", "TestIEventProcessManager"));
            String getOutTrace = (eventProcessManager.getLastOutTrace(1, "chat1", "SIMPLECHAT", appVersion));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetLastOutTrace", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastOutTrace", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetLastOutTrace", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = {"TeststartEventProcess"}, alwaysRun = true)
    public void TestgetRunningEventProcesses() {
        try {
            // gets all the currently running eventprocess references
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetRunningEventProcesses", "TestIEventProcessManager"));
            // gives all the running eventprocessesreferences
            EventProcessReference epr1[] = eventProcessManager.getRunningEventProcesses();
            Assert.assertTrue(epr1.length > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetRunningEventProcesses", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetRunningEventProcesses", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetRunningEventProcesses", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "start_IEventProcessManager", alwaysRun = true)
    public void TestgetVersions() {
        try {
            // return all the versions of a given application into a float array
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetVersions", "TestIEventProcessManager"));
            // gives all the versions of the given eventprocess
            float[] flt = eventProcessManager.getVersions("SIMPLECHAT");
            Assert.assertTrue(flt.length > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetVersions", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetVersions", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetVersions", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "start_IEventProcessManager", alwaysRun = true)
    public void TestsetLogLevel() {
        /*try {
            // set the log level of a given application, service
            Logger.LogMethodOrder(Logger.getOrderMessage("TestsetLogLevel", "TestIEventProcessManager"));
            Hashtable loglevel = new Hashtable();
            loglevel.put( "ChatService", "INFO");
            eventProcessManager.setLogLevel("SIMPLECHAT", "chat1",);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestsetLogLevel", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestsetLogLevel", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestsetLogLevel", "TestIEventProcessManager"), e);
            Assert.fail();
        }*/
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = {"TeststartEventProcess"}, alwaysRun = true)
    public void TeststartAllServices() {
        try {
            // Starts all the services of a given application and of given version.
            Logger.LogMethodOrder(Logger.getOrderMessage("TeststartAllServices", "TestIEventProcessManager"));
            resultMap.clear();
            int serviceCount = 0;
            EventProcessStateData epsd = eventProcessManager.getApplicationStateDetails("SIMPLECHAT",appVersion);
            Enumeration allServices = epsd.getAllServiceNames();
            while (allServices.hasMoreElements()) {
                String serviceName = (String) allServices.nextElement();
                serviceCount++;
                try {
                    eventProcessManager.stopServiceInstance("SIMPLECHAT",appVersion, serviceName);
                    Thread.sleep(Thread_Sleep_Time);
                } catch (Exception e) {
                    //Ignore
                }
            }

            eventProcessManager.startAllServices("SIMPLECHAT", appVersion);
            Thread.sleep(Thread_Sleep_Time);
            int count = Integer.parseInt(resultMap.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + "SIMPLECHAT"));
            Assert.assertEquals(count, serviceCount);
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TeststartAllServices", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststartAllServices", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststartAllServices", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststartAllServices", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }


    @Test(groups = "IEventProcessManager", dependsOnMethods = {"TeststartEventProcess"}, alwaysRun = true)
    public void TeststartServiceInstance() {
        try {
            // starts a given service instance of a given application
            Logger.LogMethodOrder(Logger.getOrderMessage("TeststartServiceInstance", "TestIEventProcessManager"));
            resultMap.clear();
            try {
                eventProcessManager.stopServiceInstance("SIMPLECHAT",appVersion, "chat2");
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Assert.fail();
            } catch (Exception e) {
                //Ignore
            }

            eventProcessManager.startServiceInstance("SIMPLECHAT",appVersion, "chat2");
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Assert.fail();
            }

            int count = Integer.parseInt(resultMap.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + "SIMPLECHAT"));
            Assert.assertEquals(count, 1);
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TeststartServiceInstance", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststartServiceInstance", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststartServiceInstance", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IEventProcessManager", dependsOnMethods = "TeststartEventProcess", alwaysRun = true)
    public void TestsetTrackedDataType() {
        try {
            // sets the TrackedDataType of a given application, service and given port
            Logger.LogMethodOrder(Logger.getOrderMessage("TestsetTrackedDataType", "TestIEventProcessManager"));
            eventProcessManager.setTrackedDataType("chat1", "SIMPLECHAT",appVersion, "OUT_PORT", 2);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestsetTrackedDataType", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestsetTrackedDataType", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestsetTrackedDataType", "TestIEventProcessManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = "TWO_IEventProcessManager", dependsOnGroups = {"IEventProcessManager.*"}, alwaysRun = true)
    public void TestrestartEventProcess() {
        try {
            // restarts the eventprocess which is currently in running state. If the given application is not in running state it will throw an error
            Logger.LogMethodOrder(Logger.getOrderMessage("TestrestartEventProcess", "TestIEventProcessManager"));
            resultMap.clear();
            // restarts the eventprocess
            eventProcessManager.restartEventProcess("SIMPLECHAT",appVersion);
            Thread.sleep(Thread_Sleep_Time);

            int count = Integer.parseInt(resultMap.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + "SIMPLECHAT"));
            Assert.assertEquals(count, numberofServices);
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestrestartEventProcess", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestrestartEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestrestartEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestrestartEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "TWO_IEventProcessManager", dependsOnMethods = {"TestrestartEventProcess"}, alwaysRun = true)
    public void TeststopServiceInstance() {
        try {
            // stops a service instance of a given application and of given service
            Logger.LogMethodOrder(Logger.getOrderMessage("TeststopServiceInstance", "TestIEventProcessManager"));
            resultMap.clear();
            try {
                eventProcessManager.startServiceInstance("SIMPLECHAT",appVersion, "chat1");
                Thread.sleep(Thread_Sleep_Time);
            } catch (Exception e) {
                //Ignore
            }
            //it will stop the given service instance in the running eventprocess; if not present gives an error
            eventProcessManager.stopServiceInstance("SIMPLECHAT",appVersion, "chat1");
            Thread.sleep(Thread_Sleep_Time);
            int count = Integer.parseInt(resultMap.get(TestConstants.SERVICEINSTANCE_STOP_SUCCESSFUL + "SIMPLECHAT"));
            Assert.assertEquals(count, 1);
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TeststopServiceInstance", "TestIEventProcessManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststopServiceInstance", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststopServiceInstance", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststopServiceInstance", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "TWO_IEventProcessManager", dependsOnMethods = {"TeststopServiceInstance"}, alwaysRun = true)
    public void TestsynchronizeEventProcess() {
        try {
            // synchronizes the eventprocess of a given application and of given version
            Logger.LogMethodOrder(Logger.getOrderMessage("TestsynchronizeEventProcess", "TestIEventProcessManager"));
            resultMap.clear();
            int serviceCount = 0;
            EventProcessStateData epsd = eventProcessManager.getApplicationStateDetails("SIMPLECHAT",appVersion);
            Enumeration allServices = epsd.getAllServiceNames();
            while (allServices.hasMoreElements()) {
                String serviceName = (String) allServices.nextElement();
                serviceCount++;
                try {
                    eventProcessManager.stopServiceInstance("SIMPLECHAT",appVersion, serviceName);
                    Thread.sleep(Thread_Sleep_Time);
                } catch (Exception e) {
                    //Ignore
                }
            }

            eventProcessManager.synchronizeEventProcess("SIMPLECHAT", appVersion);
            try {
                Thread.sleep(Thread_Sleep_Time);

            } catch (InterruptedException e) {
                e.printStackTrace();
                Assert.fail();
            }
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestsynchronizeEventProcess", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestsynchronizeEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestsynchronizeEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestsynchronizeEventProcess", "TestIEventProcessManager"), e);
            e.printStackTrace();
        }
    }

    @Test(groups = "THREE_IEventProcessManager", dependsOnGroups = {"TWO_IEventProcessManager.*"}, alwaysRun = true)
    public void TeststopEventProcess() {
        try {
            // stops the eventprocess which is currently running
            Logger.LogMethodOrder(Logger.getOrderMessage("TeststopEventProcess", "TestIEventProcessManager"));
            resultMap.clear();
            try {
                eventProcessManager.startAllServices("SIMPLECHAT", appVersion);
                Thread.sleep(Thread_Sleep_Time);
            } catch (Exception e) {
                // Ignore
            }
            //stops the eventprocess
            eventProcessManager.stopEventProcess("SIMPLECHAT", appVersion);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            int count = Integer.parseInt(resultMap.get(TestConstants.SERVICEINSTANCE_STOP_SUCCESSFUL + "SIMPLECHAT"));
            Assert.assertEquals(numberofServices, count);

            if (resultMap.get(TestConstants.EVENTPROCESS_STOP_SUCCESSFUL).equals("SIMPLECHAT")) {
                // Test passed successfully
            } else {
                Assert.fail();
            }
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TeststopEventProcess", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststopEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TeststopEventProcess", "TestIEventProcessManager"), e);
            Assert.fail();
        }

    }


    @Test(groups = "THREE_IEventProcessManager", dependsOnMethods = {"TeststopEventProcess"}, alwaysRun = true)
    public void TestremoveEventProcessListener() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("TestremoveEventProcessListener", "TestIEventProcessManager"));
            // removes the eventprocesslistener already attached to the given application
            resultMap.clear();
            eventProcessManager.removeEventProcessListener();
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eventProcessManager.startEventProcess("SIMPLECHAT", appVersion, false);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (resultMap.size() == 0) {
                // Test passed successfully
            } else {
                Assert.fail();
            }
            eventProcessManager.stopEventProcess("SIMPLECHAT", appVersion);

            Logger.Log(Level.INFO, Logger.getFinishMessage("TestremoveEventProcessListener", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveEventProcessListener", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveEventProcessListener", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "THREE_IEventProcessManager", dependsOnMethods = {"TeststopEventProcess"}, alwaysRun = true)
    public void TestremoveRepositoryEventListener() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("TestremoveRepositoryEventListener", "TestIEventProcessManager"));
            // removes the repositoryeventlistener already attached to the given application
            resultMap.clear();
            eventProcessManager.removeRepositoryEventListener();
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //mywork has been deployed in testDeploy Method.
            eventProcessManager.deleteEventProcess("MYWORK1", appVersion, true, false);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (resultMap.size() == 0) {
                // test passed successfully
            } else {
                Assert.fail();
            }

            Logger.Log(Level.INFO, Logger.getFinishMessage("TestremoveRepositoryEventListener", "TestIEventProcessManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveRepositoryEventListener", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveRepositoryEventListener", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }


    @Test(groups = "IEventProcessManager", dependsOnMethods = "TestaddRepositoryEventListener", alwaysRun = true)
    public void TestDeployeventprocess() {
        try {
            // deploys a user defined application by giving the path of a zip file which is here located in the resources folder
            Logger.LogMethodOrder(Logger.getOrderMessage("TestDeployeventprocess", "TestIEventProcessManager"));
            deployEventProcess("EventProcess.zip");
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Assert.assertTrue(eventProcessManager.exists("MYWORK1", appVersion));
            if (resultMap.get(TestConstants.EVENTPROCESS_DEPLOYED_SUCCESSFUL).equals("MYWORK1")) {
                // Test passed successfully
                resultMap.remove(TestConstants.EVENTPROCESS_DEPLOYED_SUCCESSFUL);
            } else {
                Assert.fail();
            }
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestDeployeventprocess", "TestIEventProcessManager"));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployeventprocess", "TestIEventProcessManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployeventprocess", "TestIEventProcessManager"), e);
            Assert.fail();
        }
    }

    @Test(enabled = false)
    public static synchronized void insertIntoResultMap(String key, String value) {
        resultMap.put(key, value);
    }

    @Test(enabled = false)
    public static synchronized boolean containsKey(String key) {
        return resultMap.containsKey(key);
    }

    @Test(enabled = false)
    public static synchronized String getValue(String key) {
        return resultMap.get(key);
    }

    /**
     * deploys an event process which is located under $testing_home/esb/TestNG/resources/
     *
     * @param zipName - name of the event process zip located under $testing_home/esb/TestNG/resources/
     * @throws IOException      - if file is not found or for any other IO error
     * @throws ServiceException
     */
    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + "/esb/TestNG/resources/" + zipName));
        boolean completed = false;
        byte result[];
        while (!completed) {
            byte[] temp = new byte[1024];
            int readcount = 0;
            readcount = bis.read(temp);

            if (readcount < 0) {
                completed = true;
                readcount = 0;
            }
            result = new byte[readcount];
            System.arraycopy(temp, 0, result, 0, readcount);

            // it will deploy the event process if the correct byte array is send; else gives an error
            this.eventProcessManager.deployEventProcess(result, completed);
        }
    }
}
