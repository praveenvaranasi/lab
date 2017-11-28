package com.fiorano.esb.testng.rmi.sanity;

import com.fiorano.esb.server.api.*;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Feb 22, 2010
 * Time: 10:38:39 AM
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings(value = "all")
public class TestIRmiManager extends AbstractTestNG {

    public static IRmiManager rmiManager;
    public static String handleID;
    public static IEventProcessManager eventProcessManager;
    public static IConfigurationManager configurationManager;
    public static IServiceManager serviceManager;
    public static IFPSManager fpsManager;
    public static IServiceProviderManager serviceProviderManager;
    public static IDebugger debugger;

    public static Properties testProperties;
    public static int Thread_Sleep_Time;

    @BeforeClass
    public void setUp() {
        //Initialize the logger
//        initializeProperties();
//        initializeLogger();

    }

    @Test(groups = {"IRmiManager"}, alwaysRun = true)
    public static void start_rmiManager() {
        try {
            System.out.println(" Test cases will start in 2 minutes. just waiting for the servers to boot up.");
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {

            }
            // starting method of the rmiManager which will take registry
            Logger.LogMethodOrder(Logger.getOrderMessage("start_rmiManager", "TestIRmiManager"));
            ArrayList urlDetails = getActiveFESUrl();
            Registry reg = LocateRegistry.getRegistry((String) urlDetails.get(0), (Integer) urlDetails.get(1));
            rmiManager = (IRmiManager) reg.lookup(IRmiManager.class.toString());
            Logger.Log(Level.INFO, Logger.getFinishMessage("start_rmiManager", "TestIRmiManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("start_rmiManager", "TestIRmiManager"), e);
            Assert.fail();
        } catch (NotBoundException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("start_rmiManager", "TestIRmiManager"), e);
            Assert.fail();
        } catch (STFException e) {
            //exception already logged inside getActiveFESURL.
            Assert.fail();
        }

    }

    @Test(groups = {"IRmiManager"}, dependsOnMethods = "start_rmiManager", alwaysRun = true)
    public static void Testlogin() {
        try {
            // logs in to the server which provides a handleID
            Logger.LogMethodOrder(Logger.getOrderMessage("Testlogin", "TestIRmiManager"));
            handleID = rmiManager.login("admin", "passwd");
            Assert.assertNotNull(handleID);
            System.out.println("login successful with handleID = " + handleID);
            Logger.Log(Level.INFO, Logger.getFinishMessage("Testlogin", "TestIRmiManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogin", "TestIRmiManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testlogin", "TestIRmiManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = {"IRmiManager"}, dependsOnMethods = "Testlogin", alwaysRun = true)
    public static void TestIEventProcessManager() {
        try {
            // gets the eventprocessManager using the rmiManager and the handleID
            Logger.LogMethodOrder(Logger.getOrderMessage("TestIEventProcessManager", "TestIRmiManager"));
            Assert.assertNotNull(rmiManager);
            Assert.assertNotNull(handleID);

            eventProcessManager = rmiManager.getEventProcessManager(handleID);

            Assert.assertNotNull(eventProcessManager);
            Assert.assertEquals(eventProcessManager.toString(), "Event_Process_Manager");
            Assert.assertTrue(eventProcessManager.exists("SIMPLECHAT", 1.0f));

            Logger.Log(Level.INFO, Logger.getFinishMessage("TestIEventProcessManager", "TestIRmiManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIEventProcessManager", "TestIRmiManager"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIEventProcessManager", "TestIRmiManager"), e);
            Assert.fail();

        }

    }

    @Test(groups = {"IRmiManager"}, dependsOnMethods = "Testlogin", alwaysRun = true)
    public void TestRmiCallTimeOut() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestRmiCallTimeOut", "TestIRmiManager"));
        System.setProperty("FIORANO_RMI_CALL_TIMEOUT", "1");
        try {
            rmiManager.getEventProcessManager(handleID).getAllEventProcesses();
        } catch (RemoteException e) {
            if (e instanceof UnmarshalException) {
                if ((e.getCause().getMessage().indexOf("Read timed out") != -1))
                    Assert.assertTrue(true);
                else
                    e.printStackTrace();
                Assert.fail();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRmiCallTimeOut", "TestIRmiManager"), e);
            } else
                e.printStackTrace();
            Assert.fail();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRmiCallTimeOut", "TestIRmiManager"), e);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.fail();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRmiCallTimeOut", "TestIRmiManager"), e);

        } finally {
            System.setProperty("FIORANO_RMI_CALL_TIMEOUT", String.valueOf(Thread_Sleep_Time));//set it back to a healthy value
        }

    }

    @Test(groups = {"IRmiManager"}, dependsOnMethods = "Testlogin", alwaysRun = true)
    public void TestIServiceManager() {
        try {
            // gets the ServiceManager using the rmiManager and the handleID
            Logger.LogMethodOrder(Logger.getOrderMessage("TestIServiceManager", "TestIRmiManager"));
            Assert.assertNotNull(rmiManager);
            Assert.assertNotNull(handleID);

            serviceManager = rmiManager.getServiceManager(handleID);

            Assert.assertNotNull(serviceManager);
            Assert.assertEquals(serviceManager.toString(), "Service_Manager");

            Assert.assertTrue(serviceManager.exists("chat", 4.0f));

            Logger.Log(Level.INFO, Logger.getFinishMessage("TestIServiceManager", "TestIRmiManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIServiceManager", "TestIRmiManager"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIServiceManager", "TestIRmiManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IRmiManager"}, dependsOnMethods = "Testlogin", alwaysRun = true)
    public void TestIConfigurationManager() {
        try {
            // gets the ConfigurationManager using the rmiManager and the handleID
            Logger.LogMethodOrder(Logger.getOrderMessage("TestIConfigurationManager", "TestIRmiManager"));
            Assert.assertNotNull(rmiManager);
            Assert.assertNotNull(handleID);

            configurationManager = rmiManager.getConfigurationManager(handleID);

            Assert.assertNotNull(configurationManager);
            Assert.assertEquals(configurationManager.toString(), "CONFIGURATION_Manager");
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestIConfigurationManager", "TestIRmiManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIConfigurationManager", "TestIRmiManager"), e);
            Assert.fail();

        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIConfigurationManager", "TestIRmiManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IRmiManager"}, dependsOnMethods = "Testlogin", alwaysRun = true)
    public void TestfpsManager() {
        try {
            // gets the fpsManager from the rmiManager and the handleID
            Logger.LogMethodOrder(Logger.getOrderMessage("TestfpsManager", "TestIRmiManager"));
            Assert.assertNotNull(rmiManager);
            Assert.assertNotNull(handleID);

            fpsManager = rmiManager.getFPSManager(handleID);

            Assert.assertNotNull(fpsManager);
            Assert.assertEquals(fpsManager.toString(), "FPS_Manager");
            Assert.assertTrue(fpsManager.isPeerRunning("fps"));

            Logger.Log(Level.INFO, Logger.getFinishMessage("TestfpsManager", "TestIRmiManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestfpsManager", "TestIRmiManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestfpsManager", "TestIRmiManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = {"IRmiManager"}, dependsOnMethods = "Testlogin", alwaysRun = true)
    public void TestIServiceProviderManager() {
        try {
            //gets the ServiceProviderManager from the rmiManager and the handleID
            Logger.LogMethodOrder(Logger.getOrderMessage("TestIServiceProviderManager", "TestIRmiManager"));
            Assert.assertNotNull(rmiManager);
            Assert.assertNotNull(handleID);

            serviceProviderManager = rmiManager.getServiceProviderManager(handleID);

            Assert.assertNotNull(serviceProviderManager);
            Assert.assertEquals(serviceProviderManager.toString(), "Service_Provider_Manager");
            Assert.assertTrue(serviceProviderManager.getServerName().equals("fes"));

            Logger.Log(Level.INFO, Logger.getFinishMessage("TestIServiceProviderManager", "TestIRmiManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIServiceProviderManager", "TestIRmiManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIServiceProviderManager", "TestIRmiManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = {"IRmiManager"}, dependsOnMethods = "Testlogin", alwaysRun = true)
    public void TestIDebugger() {
        try {
            // gets the IDebugger using the rmiManager and the handleID
            Logger.LogMethodOrder(Logger.getOrderMessage("TestIDebugger", "TestIRmiManager"));
            Assert.assertNotNull(rmiManager);
            Assert.assertNotNull(handleID);

            debugger = rmiManager.getBreakpointManager(handleID);

            Assert.assertNotNull(debugger);
            Assert.assertEquals(debugger.toString(), "Breakpoint_Manager");
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestIDebugger", "TestIRmiManager"));

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIDebugger", "TestIRmiManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestIDebugger", "TestIRmiManager"), e);
            Assert.fail();
        }
    }

    @Test(enabled = false)
    public static IEventProcessManager geteventProcessManager() {
        return eventProcessManager;
    }

    @Test(enabled = false)
    public static IServiceManager getserviceManager() {
        return serviceManager;
    }

    @Test(enabled = false)
    public static IConfigurationManager getconfigurationManager() {
        return configurationManager;
    }

    @Test(enabled = false)
    public static IFPSManager getfpsManager() {
        return fpsManager;
    }

    @Test(enabled = false)
    public static IServiceProviderManager getserviceProviderManager() {
        return serviceProviderManager;
    }

    @Test(enabled = false)
    public static IDebugger getdebugger() {
        return debugger;
    }

    @Test(enabled = false)
    public synchronized static IRmiManager getrmiManager() {
        return rmiManager;
    }

    @Test(enabled = false)
    public static String getHandleID() {
        return handleID;
    }

    @Test(enabled = false)
    public static String getProperty(String key) {
        return testProperties.getProperty(key);
    }

    @Test(enabled = false)
    public static void updateIRmiManager() {
        try {
            //In case of fes restart, this method will update all the managers
            Thread.sleep(Thread_Sleep_Time);
            ArrayList urlDetails = getActiveFESUrl();
            Registry reg = LocateRegistry.getRegistry((String) urlDetails.get(0), (Integer) urlDetails.get(1));
            rmiManager = (IRmiManager) reg.lookup(IRmiManager.class.toString());
            handleID = rmiManager.login("admin", "passwd");
            // gets fpsManageranager object if its login correctly ; else gives a null;
            fpsManager = rmiManager.getFPSManager(handleID);
            // gets IServiceProviderManager object if its login correctly ; else gives a null;
            serviceProviderManager = rmiManager.getServiceProviderManager(handleID);
            serviceManager = rmiManager.getServiceManager(handleID);
            debugger = rmiManager.getBreakpointManager(handleID);
            eventProcessManager = rmiManager.getEventProcessManager(handleID);
            configurationManager = rmiManager.getConfigurationManager(handleID);

        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("updateIRmiManager", "TestIRmiManager"), e);

            Assert.fail();
        } catch (NotBoundException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("updateIRmiManager", "TestIRmiManager"), e);

            Assert.fail();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, "Unable to updateRMI Manager:", e);
            e.printStackTrace();
        }
    }

    /**
     * returns a arraylist of size 2.
     * which contains a string(ip of fes) followed by a int(rmi port of fes)
     */
    @Test(enabled = false)
    public static ArrayList getActiveFESUrl() throws STFException {
        ArrayList url = new ArrayList(2);//string followed by int.
        try {
            String s = ServerStatusController.getInstance().getURLOnFESActive();
            String rtlPort = s.substring(s.lastIndexOf(":") + 1);
            url.add(s.substring(s.lastIndexOf("//") + 2, s.lastIndexOf(":")));
            if (rtlPort.equals("1947")) {
                url.add(2047);
            } else
                url.add(2048);
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, "Unable to proceed with tests exception occured  while getting Active FES rmi port & url", e);
            throw e;
        }
        return url;
    }
}
