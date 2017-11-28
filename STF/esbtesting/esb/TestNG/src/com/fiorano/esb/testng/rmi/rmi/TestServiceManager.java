package com.fiorano.esb.testng.rmi.rmi;

import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.server.api.IServiceManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.server.api.ServiceReference;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.test.core.TestElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.rmi.RemoteException;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/10/11
 * Time: 6:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceManager extends AbstractTestNG{
    private IServiceManager serviceManager;
    private IRmiManager rmiManager;
    private String resourceFilePath;
    private String m_ComponentGUID;
    private float m_version;
    private String m_serviceDir;
    private String rmiHandleID;
    private String zipfile;
    private TestElement testCaseConfig;

    @BeforeClass(groups = "ServiceManagerTest", alwaysRun = true)
    public void startSetUp() throws ServiceException, RemoteException {
        Logger.Log(Level.INFO,Logger.getExecuteMessage("startSetUp","TestServiceManager"));
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "TestServiceManager"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        rmiManager = rmiClient.getRmiManager();
        rmiHandleID = rmiManager.login("admin", "passwd");
        serviceManager = rmiManager.getServiceManager(rmiHandleID);
        initializeProperties("rmi" + fsc + "ServiceManager" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "rmi" + fsc + "ServiceManager";
        init();
        printInitParams();
    }

    @Test(groups = "ServiceManagerTest", alwaysRun = true)
    public void testExists() {

        try {

            serviceManager.exists(m_ComponentGUID, m_version);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

    @Test(groups = "ServiceManagerTest", alwaysRun = true, dependsOnMethods = "testExists")
    public void testGetAndDeployService() {
        try {
            getService();

            serviceManager.delete(m_ComponentGUID, m_version, false);

            deployService();

            //now we assert true only if the component exists in repository
            Assert.assertTrue(serviceManager.exists(m_ComponentGUID, m_version));

        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e);
        }
    }

    @Test(groups = "ServiceManagerTest", alwaysRun = true, dependsOnMethods = "testGetAndDeployService")
    public void testGetServiceIds() {
        try {

            serviceManager.getServiceIds();
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceManagerTest", alwaysRun = true, dependsOnMethods = "testGetServiceIds")
    public void testDependenciesExist() {
        try {

            ServiceReference srref1 = new ServiceReference();
            srref1.setId("m_ComponentGUID");
            srref1.setVersion(IServiceManager.ANY_VERSION);
            serviceManager.dependenciesExists(new ServiceReference[]{srref1});
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "ServiceManagerTest", alwaysRun = true, dependsOnMethods = "testDependenciesExist")
    public void testDeleteService() {
        try {

            serviceManager.delete(m_ComponentGUID, m_version, true);

            Assert.assertFalse(serviceManager.exists(m_ComponentGUID, m_version));

            //now replace the service
            deployService();

        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }

    }

    @Test(groups = "ServiceManagerTest", alwaysRun = true, dependsOnMethods = "testDeleteService")
    public void testRMILogout() {
        try {

            rmiManager.logout(rmiHandleID);

        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }




    /*auxiliary functions*/

    private void getService() throws Exception {

        long index = 0;
        RandomAccessFile ras = new RandomAccessFile(zipfile, "rw"); //$NON-NLS-1$
        try {
            byte[] data = serviceManager.getService(m_ComponentGUID, m_version, index);
            while (data != null) {
                ras.write(data);
                index = index + data.length;
                data = serviceManager.getService(m_ComponentGUID, m_version, index);
            }
        }catch(RemoteException re){
            re.printStackTrace();
        }catch(ServiceException se){
            se.printStackTrace();
        }
        finally {
            ras.close();
        }
    }

    private void deployService() throws Exception {
        boolean successful = true;

        boolean complete = false;
        byte[] contents;


        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(zipfile));
        try {
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
        catch (Exception e) {
            successful = false;
        }
        finally {
            bis.close();
        }

    }

    public void init() {
        m_ComponentGUID = testProperties.getProperty("ComponentGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ComponentVersion"));
        m_serviceDir = resourceFilePath + File.separator + testProperties.getProperty("ServiceDescriptorFile", "ServiceDescriptor.xml");
        zipfile = resourceFilePath + File.separator + testProperties.getProperty("zipfile");
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Service GUID::       " + m_ComponentGUID + "  Version Number::" + m_version);
        System.out.println("Service Descriptor File" + m_serviceDir);
        System.out.println("_____________________________________________________________________________");
    }
}
