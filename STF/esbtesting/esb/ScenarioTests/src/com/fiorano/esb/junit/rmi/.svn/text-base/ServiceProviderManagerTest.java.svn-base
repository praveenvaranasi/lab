package com.fiorano.esb.junit.rmi;

import com.fiorano.stf.test.core.RMITestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.esb.server.api.*;
import com.fiorano.esb.application.controller.ApplicationControllerErrorCodes;

import java.io.File;
import java.rmi.RemoteException;
import java.util.HashMap;

import junit.framework.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: arun
 * Date: Oct 29, 2009
 * Time: 4:00:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceProviderManagerTest extends RMITestCase {


    private IServiceProviderManager SPManager;
    private IRmiManager rmiManager;
    private String resourceFilePath;
    private String m_ComponentGUID;
    private float m_version;
    private String m_serviceDir;
    private String rmiHandleID;

    public ServiceProviderManagerTest(String testCaseName) {

        super(testCaseName);
        try {
            rmiManager = rmiClient.getRmiManager();
            rmiHandleID = rmiManager.login("admin", "passwd");
            SPManager = rmiManager.getServiceProviderManager(rmiHandleID);
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "ServiceProviderManager";
            setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ServiceProviderManagerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
        try {
            rmiManager = rmiClient.getRmiManager();
            rmiHandleID = rmiManager.login("admin", "passwd");
            SPManager = rmiManager.getServiceProviderManager(rmiHandleID);
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "ServiceProviderManager";
            setUp();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setUp() throws Exception {
        super.setUp();
        System.out.println("Started the Execution of the TestCase::" + getName());
        //init();
    }

    public void init() {
//         m_ComponentGUID = testCaseProperties.getProperty("ComponentGUID");
//         m_version = Float.parseFloat(testCaseProperties.getProperty("ComponentVersion"));
//         m_serviceDir = resourceFilePath + File.separator + testCaseProperties.getProperty("ServiceDescriptorFile", "ServiceDescriptor.xml");
        //printInitParams();
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Service GUID::       " + m_ComponentGUID + "  Version Number::" + m_version);
        System.out.println("Service Descriptor File" + m_serviceDir);
        System.out.println("_____________________________________________________________________________");
    }

    /* public void testRestartServer()
    {
        try{
                SPManager.restartServer();
            }catch(RemoteException ignored){
            } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
         }
        while(looptillserveronline());

    }*/


    public void testGetLastOutLogs() {
        try {

            Assert.assertNotNull(SPManager.getLastErrLogs(50));
        } catch (Exception e) {
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT) != -1;
            if (!check) {
                System.err.println("Exception in the Execution of test case::" + getName());
                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            }
        }
    }

    public void testGetLastErrLogs() {
        try {

            Assert.assertNotNull(SPManager.getLastErrLogs(50));

        } catch (Exception e) {
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT) != -1;
            if (!check) {
                System.err.println("Exception in the Execution of test case::" + getName());
                e.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
            }
        }
    }

    public void testGetServername() {
        try {
            Assert.assertEquals(SPManager.getServerName(), "fes");
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testGetFESPerfomanceStats() {
        try {
            FESPerformanceStats perf = SPManager.getServerPerformanceStats();
            System.out.println("Processes:" + perf.getTotalProcessCount() + "\nMemory:" + perf.getMemoryUsageInKB());
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }

    }
    /*    public void testGetFESActiveSwitchTime()
        {
            try{
                long time1=SPManager.getFESActiveSwitchTime();
                try{
                SPManager.restartServer();
                }catch(RemoteException ignored){}
                while(looptillserveronline());
                long time2=SPManager.getFESActiveSwitchTime();
                Assert.assertTrue(time1<time2);
            } catch (ServiceException e) {
               System.err.println("Exception in the Execution of test case::"+getName());
                    e.printStackTrace();
                    Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            } catch (RemoteException e) {
                System.err.println("Exception in the Execution of test case::"+getName());
                    e.printStackTrace();
                    Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            }
        }*/


    private boolean looptillserveronline() {

        try {
            Thread.sleep(10000);
            rmiClient = RMIClient.getInstance();
            rmiManager = rmiClient.getRmiManager();
            rmiHandleID = rmiManager.login("admin", "passwd");
            SPManager = rmiManager.getServiceProviderManager(rmiHandleID);
            return false;
        } catch (Exception e) {
            looptillserveronline();
        }
        return false;
    }

    public void testGetServerDetails() {
        try {
            HashMap deets = SPManager.getServerDetails();
            Assert.assertEquals(deets.get("Name"), SPManager.getServerName());
            deets.get("Server URL");
            deets.get("Backup URL");
            deets.get("ProcessCount");
            deets.get("ThreadCount");
            Assert.assertTrue(((String) deets.get("OS")).toLowerCase().contains(System.getProperty("os.name").toLowerCase()));
        } catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        } catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }


    public void testshutdownServer() {
    }


    public void testGetServerSystemInfo() {
        try {
            SystemInfoReference info = SPManager.getServerSystemInfo();
            Assert.assertEquals(info.getOpSysName(), System.getProperty("os.name"));
        }
        catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
        catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testClearFESOutLogs() {
        try {
            SPManager.clearFESOutLogs();
            Assert.assertTrue(SPManager.getLastOutLogs(50).length() == 0);
        }
        catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
        catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testClearFESErrLogs() {
        try {
            SPManager.clearFESErrLogs();
            Assert.assertTrue(SPManager.getLastErrLogs(50).length() == 0);
        }
        catch (ServiceException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
        catch (RemoteException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }


}