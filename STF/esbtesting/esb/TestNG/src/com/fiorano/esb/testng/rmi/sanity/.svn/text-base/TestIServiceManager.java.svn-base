/**
 * Created by IntelliJ IDEA.
 * User: gopi
 * Date: Feb 25, 2010
 * Time: 11:13:15 AM
 * To change this template use File | Settings | File Templates.
 */
package com.fiorano.esb.testng.rmi.sanity;

import com.fiorano.esb.server.api.IRepositoryEventListener;
import com.fiorano.esb.server.api.IServiceManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.server.api.ServiceReference;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.bugs.TestConstants;
import com.fiorano.esb.testng.rmi.bugs.TestIRepositoryEventListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

@Test(dependsOnGroups = {"THREE_IEventProcessManager.*"})
public class TestIServiceManager extends AbstractTestNG {

    public IServiceManager serviceManager;
    public ServiceReference[] sr;
    public static Hashtable<String, String> resultMap = new Hashtable<String, String>();


    @Test(groups = "IServiceManager", alwaysRun = true)
    public void start_IServiceManager() {
        Logger.LogMethodOrder(Logger.getOrderMessage("start_IServiceManager", "TestIServiceManager"));
        serviceManager = TestIRmiManager.getserviceManager();
        Logger.Log(Level.INFO, Logger.getFinishMessage("start_IServiceManager", "TestIServiceManager"));
    }

    @Test(groups = "IServiceManager", dependsOnMethods = "start_IServiceManager", alwaysRun = true)
    public void TestaddServiceRepositoryEventListener() {
        try {
            // adds the service repository event listener to a service manager
            Logger.LogMethodOrder(Logger.getOrderMessage("TestaddServiceRepositoryEventListener", "TestIServiceManager"));
            IRepositoryEventListener irel = new TestIRepositoryEventListener();
            resultMap.clear();
            serviceManager.addServiceRepositoryEventListener(irel);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //serviceManager.delete("DB", 4.0f, true);
            this.deployService("COMP1_1.0.zip");
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Assert.assertTrue(serviceManager.exists("COMP1", 1.0f));
            if (resultMap.get(TestConstants.SERVICEINSTANCE_DEPLOY_SUCCESSFUL).equals("COMP1")) {
                // Test passed successfully
                resultMap.remove(TestConstants.SERVICEINSTANCE_DEPLOY_SUCCESSFUL);
            } else {
                Assert.fail();
            }
            resultMap.clear();
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestaddServiceRepositoryEventListener", "TestIServiceManager"));
        } catch (IOException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddServiceRepositoryEventListener", "TestIServiceManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddServiceRepositoryEventListener", "TestIServiceManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceManager", dependsOnMethods = "start_IServiceManager", alwaysRun = true)
    public void TestgetServiceIds() {
        try {
            // gets all the serviceIDs of a servicemanager. It returns a string array
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetServiceIds", "TestIServiceManager"));
            String[] id = serviceManager.getServiceIds();
            Assert.assertTrue(id.length > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetServiceIds", "TestIServiceManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetServiceIds", "TestIServiceManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetServiceIds", "TestIServiceManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceManager", dependsOnMethods = "start_IServiceManager", alwaysRun = true)
    public void TestExists() {
        try {
            // checks the existance of a given service in the repository
            Logger.LogMethodOrder(Logger.getOrderMessage("TestExists", "TestIServiceManager"));
            boolean check = serviceManager.exists("chat", 4.0f);
            Assert.assertTrue(check);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestExists", "TestIServiceManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestExists", "TestIServiceManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestExists", "TestIServiceManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceManager", dependsOnMethods = "start_IServiceManager", alwaysRun = true)
    public void TestgetVersions() {

        try {
            // gets all the versions of a given service in the repository. It returns a float array
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetVersions", "TestIServiceManager"));
            // get all the versions of a particular service into a float array
            float[] flt = serviceManager.getVersions("chat");
            Assert.assertTrue(flt.length > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetVersions", "TestIServiceManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetVersions", "TestIServiceManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetVersions", "TestIServiceManager"), e);
            Assert.fail();
        }
    }


    @Test(groups = "IServiceManager", dependsOnMethods = "start_IServiceManager", alwaysRun = true)
    public void TestgetAllServices() {
        try {
            // returns all the services in the repository to a service reference array
            Logger.LogMethodOrder(Logger.getOrderMessage("TestgetAllServices", "TestIServiceManager"));
            // get all the services in the fps into a ServiceReference array
            sr = serviceManager.getAllServices();
            Assert.assertTrue(sr.length > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestgetAllServices", "TestIServiceManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetAllServices", "TestIServiceManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestgetAllServices", "TestIServiceManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceManager", dependsOnMethods = "TestgetAllServices", alwaysRun = true)
    public void TestdependenciesExists() {
        try {
            // checks whether there exists a dependency among the service references
            Logger.LogMethodOrder(Logger.getOrderMessage("TestdependenciesExists", "TestIServiceManager"));
            Assert.assertTrue(serviceManager.dependenciesExists(sr));
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestdependenciesExists", "TestIServiceManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestdependenciesExists", "TestIServiceManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestdependenciesExists", "TestIServiceManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceManager", dependsOnMethods = {"start_IServiceManager"}, alwaysRun = true)
    public void TestfetchResourceForService() {
        try {
            // fetches resources for a given service of given version and of given resource of it. It returns a byte array
            Logger.LogMethodOrder(Logger.getOrderMessage("TestfetchResourceForService", "TestIServiceManager"));
            // fetches the Resources for a service which exists in the server with its service id,version,resource name
            byte k[] = serviceManager.fetchResourceForService("chat", 4.0f, "fesb-comp-chat.jar", 0);
            Assert.assertTrue(k.length > 0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestfetchResourceForService", "TestIServiceManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestfetchResourceForService", "TestIServiceManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestfetchResourceForService", "TestIServiceManager"), e);
            Assert.fail();
        }

    }

    @Test(groups = "TWO_IServiceManager", dependsOnGroups = "IServiceManager.*", alwaysRun = true)
    public void TestremoveServiceRepositoryEventListener() {
        try {
            // removes the service repository event listener already added to the service manager
            Logger.LogMethodOrder(Logger.getOrderMessage("TestremoveServiceRepositoryEventListener", "TestIServiceManager"));
            resultMap.remove(TestConstants.SERVICEINSTANCE_DELETE_SUCCESSFUL);
            int sizeBeforeRemoval = resultMap.size();
            serviceManager.removeServiceRepositoryEventListener();
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {

            }
            serviceManager.delete("COMP1", 1.0f, true);
            try {
                Thread.sleep(Thread_Sleep_Time);
            } catch (InterruptedException e) {

            }

            Assert.assertFalse(serviceManager.exists("COMP1", 1.0f));
            if (resultMap.size() == sizeBeforeRemoval) {
                // Test passed successfully
            } else {
                Assert.fail();
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestremoveServiceRepositoryEventListener", "TestIServiceManager"));
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveServiceRepositoryEventListener", "TestIServiceManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestremoveServiceRepositoryEventListener", "TestIServiceManager"), e);
            Assert.fail();
        }
    }

    @Test(groups = "IServiceManager", dependsOnMethods = "TestaddServiceRepositoryEventListener", alwaysRun = true)
    public void Testdelete() {

        try {
            // deletes the given service of given version from the service repository
            Logger.LogMethodOrder(Logger.getOrderMessage("Testdelete", "TestIServiceManager"));
            resultMap.clear();
            if (serviceManager.exists("COMP2", 1.0f)) {
                serviceManager.delete("COMP2", 1.0f, true);
            }
            Thread.sleep(Thread_Sleep_Time);

            Assert.assertFalse(serviceManager.exists("COMP2", 1.0f));
            if (resultMap.get(TestConstants.SERVICEINSTANCE_DELETE_SUCCESSFUL).equals("COMP2")) {
                // Test passed successfully
                resultMap.remove(TestConstants.SERVICEINSTANCE_DELETE_SUCCESSFUL);
            } else {
                Assert.fail();
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("Testdelete", "TestIServiceManager"));

        } catch (InterruptedException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testdelete", "TestIServiceManager"), e);
            Assert.fail();
        } catch (RemoteException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testdelete", "TestIServiceManager"), e);
            Assert.fail();
        } catch (ServiceException e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Testdelete", "TestIServiceManager"), e);
            Assert.fail();

        }
    }


    @Test(groups = "IServiceManager", dependsOnMethods = "TestaddServiceRepositoryEventListener", alwaysRun = true)
    public void TestDeployservice() {
        resultMap.clear();
        BufferedInputStream bis = null;
        try {
            // deploys a user defined service by giving the service zip path. Here it was taken from the resource folder
            Logger.LogMethodOrder(Logger.getOrderMessage("TestDeployservice", "TestIServiceManager"));
            //boolean complete = false;
            //byte[] contents;
            //serviceManager.delete("SMTP",4.0f,true);
            //Thread.sleep(Thread_Sleep_Time);
            //Assert.assertFalse(serviceManager.exists("SMTP", 4.0f));
            deployService("COMP2_1.0.zip");
            Thread.sleep(Thread_Sleep_Time);

            Assert.assertTrue(serviceManager.exists("COMP2", 1.0f));
            if (resultMap.get(TestConstants.SERVICEINSTANCE_DEPLOY_SUCCESSFUL).equals("COMP2")) {
                // Test passed successfully
                resultMap.remove(TestConstants.SERVICEINSTANCE_DEPLOY_SUCCESSFUL);
            } else {
                Assert.fail();
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("TestDeployservice", "TestIServiceManager"));

        }
        catch (Exception e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployservice", "TestIServiceManager"), e);
            Assert.fail();
        }
    }

    @Test(enabled = false)
    public static synchronized void insertIntoResultMap(String key, String value) {
        resultMap.put(key, value);
    }

    @Test(enabled = false)
    private void deployService(String zipName) throws IOException, ServiceException {
        boolean complete = false;
        BufferedInputStream bis = null;
        byte[] contents;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + "/esb/TestNG/resources/" + zipName));

        while (!complete) {
            byte[] tempContents = new byte[1024 * 40];
            int readCount = bis.read(tempContents);

            if (readCount < 0) {
                complete = true;
                readCount = 0;
            }
            contents = new byte[readCount];
            System.arraycopy(tempContents, 0, contents, 0, readCount);
            serviceManager.deployService(contents, complete);
        }
    }

}


