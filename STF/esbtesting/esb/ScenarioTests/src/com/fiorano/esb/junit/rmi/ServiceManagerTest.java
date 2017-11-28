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
package com.fiorano.esb.junit.rmi;

import com.fiorano.stf.test.core.RMITestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.server.api.IServiceManager;
import com.fiorano.esb.server.api.ServiceReference;
import com.fiorano.esb.server.api.IRmiManager;

import java.rmi.RemoteException;
import java.io.*;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;

/**
 * Created by IntelliJ IDEA.
 * User: amit
 * Date: Jun 12, 2008
 * Time: 12:45:28 PM
 */
public class ServiceManagerTest extends RMITestCase {

    private IServiceManager serviceManager;
    private IRmiManager rmiManager;
    private String resourceFilePath;
    private String m_ComponentGUID;
    private float m_version;
    private String m_serviceDir;
    private String rmiHandleID;
    private String zipfile;

    public ServiceManagerTest(String name) {
        super(name);
    }

    public ServiceManagerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    public ServiceManagerTest(TestCaseElement testCaseConfig, ServerStatusController controller) {
        super(testCaseConfig);
    }

    public void setUp() throws Exception {
        super.setUp();
        System.out.println("Started the Execution of the TestCase::" + getName());
        rmiManager = rmiClient.getRmiManager();
        rmiHandleID = rmiManager.login("admin", "passwd");
        serviceManager = rmiManager.getServiceManager(rmiHandleID);
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "ServiceManager";
        init();
        // rmiManager.logout(rmiHandleID);
    }

    public void init() {
        m_ComponentGUID = testCaseProperties.getProperty("ComponentGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ComponentVersion"));
        m_serviceDir = resourceFilePath + File.separator + testCaseProperties.getProperty("ServiceDescriptorFile", "ServiceDescriptor.xml");
        zipfile = resourceFilePath + File.separator + testCaseProperties.getProperty("zipfile");
        printInitParams();
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Service GUID::       " + m_ComponentGUID + "  Version Number::" + m_version);
        System.out.println("Service Descriptor File" + m_serviceDir);
        System.out.println("_____________________________________________________________________________");
    }

    public static Test suite() {
        return new TestSuite(ServiceManagerTest.class);
    }


    public void testExists() {

        try {

            serviceManager.exists(m_ComponentGUID, m_version);
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
    }

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
            Assert.assertTrue("TestCase Failed because of " + e, false);
        }
    }

    public void testGetServiceIds() {
        try {

            serviceManager.getServiceIds();
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public void testdependenciesExists() {
        try {

            ServiceReference srref1 = new ServiceReference();
            srref1.setId("m_ComponentGUID");
            srref1.setVersion(IServiceManager.ANY_VERSION);
            serviceManager.dependenciesExists(new ServiceReference[]{srref1});
        } catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

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
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }

    }

    public void testRmilogout() {
        try {

            rmiManager.logout(rmiHandleID);

        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + e.getMessage(), false);
        }
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testExists");
//        methodsOrder.add("testGetAndDeployService");
        methodsOrder.add("testGetServiceIds");
        methodsOrder.add("testdependenciesExists");
//        methodsOrder.add("testDeleteService");
        methodsOrder.add("testRmilogout");

        return methodsOrder;
    }


}
