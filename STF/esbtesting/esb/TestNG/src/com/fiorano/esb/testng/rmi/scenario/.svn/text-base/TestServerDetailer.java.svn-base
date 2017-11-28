package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.remote.JMXClient;
import org.testng.Assert;
//import com.fiorano.dashboard.client.ui.data.ServerDetails;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/24/11
 * Time: 3:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServerDetailer extends AbstractTestNG{
    private boolean isReady = false;
    private ObjectName objName1;
    private ObjectName objName2;
    private ObjectName objName3;
    private ObjectName objName4;
    private ObjectName objName5;

    @BeforeClass(groups = "ServiceScenarioTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "TestServiceScenario"));
        if (isReady == true) return;
        try {
            objName1 = new ObjectName("Fiorano.etc:ServiceType=MemoryManager,Name=MemoryManager");
            objName2 = new ObjectName("Fiorano.Esb.Fps.Controller:ServiceType=FPSController,Name=FPSController");
            objName3 = new ObjectName("Fiorano.Esb.Server:ServiceType=FESServer,Name=FESServer");
            objName4 = new ObjectName("Fiorano.Esb.Log:ServiceType=LogManager,Name=ESBLogManager");
            objName5 = new ObjectName("Fiorano.Esb.Webconsole.Controller:ServiceType=WebconsoleController,Name=WebconsoleController");
            isReady = true;
            JMXClient.connect("admin", "passwd");
            Logger.Log(Level.INFO, Logger.getFinishMessage("startSetUp", "TestServiceScenario"));
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetUp", "TestServiceScenario"));
        }
        isReady = true;
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testGetFESServerDetails() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetFESServerDetails", "TestServiceScenario"));

        try {
            //ServerDetails sdFES = new ServerDetails();
            long memoryused, memoryMax;
            memoryused = ((Long) jmxClient.invoke(objName1, "getUsedMemory", new Object[]{}, new String[]{})).longValue();
            memoryMax = ((Long) jmxClient.invoke(objName1, "getMaxMemory", new Object[]{}, new String[]{})).longValue();
            //sdFES.setMemoryUsage((memoryused / 1024) + "K" + "/" + (memoryMax / 1024) + "K");
            String spName = (String) jmxClient.invoke(objName3, "getServerName", new Object[]{}, new String[]{});
            Assert.assertNotNull(spName);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetFESServerDetails", "TestServiceScenario"));
        } catch (ReflectionException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerDetails", "TestServiceScenario"));
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerDetails", "TestServiceScenario"));
        } catch (InstanceNotFoundException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerDetails", "TestServiceScenario"));
        } catch (MBeanException e) {
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerDetails", "TestServiceScenario"));
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetFESServerDetails")
    public void testGetFPSServerDetails() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetFPSServerDetails","TestServiceScenario"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetFPSServerDetails", "TestServiceScenario"));
        } catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSServerDetails", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSServerDetails", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSServerDetails", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSServerDetails", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetFPSServerDetails")
    public void testGetServerDetails() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetServerDetails","TestServiceScenario"));
        String serverName = "fps";
        Object[] params = {serverName};
        String[] signatures = {String.class.getName()};
        try {
            HashMap serverDetails = (HashMap) jmxClient.invoke(objName5, "getServerDetails", params, signatures);
            Assert.assertNotNull(serverDetails);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetServerDetails", "TestServiceScenario"));
        } catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServerDetails", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServerDetails", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServerDetails", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServerDetails", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetServerDetails")
    public void testGetFESServerErrLogs() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetFESServerErrLogs","TestServiceScenario"));
        Object[] params = {new Integer(100)};
        String[] signatures = {int.class.getName()};
        try {
            String logs = (String) jmxClient.invoke(objName4, "getTESLastErrLogs", params, signatures);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetFESServerErrLogs", "TestServiceScenario"));
        } catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerErrLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerErrLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerErrLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerErrLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetFESServerErrLogs")
    public void testGetFPSServerErrLogs() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetFPSServerErrLogs","TestServiceScenario"));
        String fpsName = "fps";
        try {
            if (isFPSRunning(fpsName)) {
                Object[] params = {new Integer(100), fpsName};
                String[] signatures = {int.class.getName(), String.class.getName()};
                String logs = (String) jmxClient.invoke(objName2, "getLastOutLogs", params, signatures);
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetFPSServerErrLogs", "TestServiceScenario"));
        } catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSServerErrLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSServerErrLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSServerErrLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFPSServerErrLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetFPSServerErrLogs")
    public void testGetFESServerOutLogs() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetFESServerOutLogs","TestServiceScenario"));
        try {
            Object[] params = {new Integer(100)};
            String[] signatures = {int.class.getName()};
            String logs = (String) jmxClient.invoke(objName4, "getTESLastOutLogs", params, signatures);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetFESServerOutLogs", "TestServiceScenario"));
        }
        catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerOutLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerOutLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerOutLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetFESServerOutLogs", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetFESServerOutLogs")
    public void testGetQueues() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetQueues","TestServiceScenario"));
        String serverName = "EnterpriseServer";
        try {
            Object[] params = {serverName};
            String[] signatures = {String.class.getName()};
            HashMap queues = (HashMap) jmxClient.invoke(objName5, "getQueues", params, signatures);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetQueues", "TestServiceScenario"));
        }
        catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetQueues", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetQueues", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetQueues", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetQueues", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetQueues")
    public void testGetTopics() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetTopics","TestServiceScenario"));
        String serverName = "EnterpriseServer";
        try {
            Object[] params = {serverName};
            String[] signatures = {String.class.getName()};
            HashMap topics = (HashMap) jmxClient.invoke(objName5, "getTopics", params, signatures);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetTopics", "TestServiceScenario"));
        } catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTopics", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTopics", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTopics", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTopics", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetTopics")
    public void testGetConnections() {
        System.out.println("Started the Execution of the TestCase::" + getName());
        Logger.LogMethodOrder(Logger.getOrderMessage("testGetConnections","TestServiceScenario"));
        String serverName = "EnterpriseServer";
        try {
            Object[] params = {serverName};
            String[] signatures = {String.class.getName()};
            HashMap connections = (HashMap) jmxClient.invoke(objName5, "getConnections", params, signatures);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetConnections", "TestServiceScenario"));
        } catch (ReflectionException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetConnections", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetConnections", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetConnections", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetConnections", "TestServiceScenario"));
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    public ArrayList getAllFPSNames() {

        if (isReady) {
            try {
                return (ArrayList) jmxClient.invoke(objName2, "getAllTPSNamesList", new Object[]{}, new String[]{});
            } catch (ReflectionException e) {

                e.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            } catch (IOException e) {


                e.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            } catch (InstanceNotFoundException e) {

                e.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            } catch (MBeanException e) {

                e.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
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
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            } catch (IOException e) {

                e.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            } catch (InstanceNotFoundException e) {

                e.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            } catch (MBeanException e) {

                e.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
            }
        }
        return false;
    }
}
