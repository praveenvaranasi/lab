package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.rmi.RemoteException;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 9/27/11
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSyncAfterStoppingComponent extends AbstractTestNG {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "passwd";


    private static IEventProcessManager eventManager;
    private static IRmiManager rmiManager;
    private static String rmiHandleID;
    private String resourceFilePath;

    private static String m_InMemAppGUID;
    private static String m_InMemAppFile;
    private static float m_version;
    private static String m_InMemSerInst;
    private float appVersion = 1.0f;
    @BeforeClass(groups = "SyncAfterStoppingComponent", description = "Bug 18228")
    public void startSetUp(){
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "TestSyncAfterStoppingComponent"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("bugs" + fsc + "TestInMemSync_Bug18228" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc +"tests" +  fsc + "bugs" + fsc + "TestInMemSync_Bug18228";
        m_InMemAppFile = testProperties.getProperty("InMemAppFile");
        m_InMemAppGUID = testProperties.getProperty("InMemAppGUID");
        m_InMemSerInst = testProperties.getProperty("InMemSerInst");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        rmiManager = rmiClient.getRmiManager();
        try {
            rmiHandleID = rmiManager.login(USERNAME, PASSWORD);
            eventManager = rmiManager.getEventProcessManager(rmiHandleID);
        } catch (RemoteException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ServiceException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Logger.Log(Level.INFO,Logger.getFinishMessage("startSetUp", "TestSyncAfterStoppingComponent"));
}

    @Test(groups = "SyncAfterStoppingComponent",dependsOnMethods = "startSetUp", alwaysRun = true, description = "Bug 18228")
    public void testInMemSyncBug18228() throws IOException {
        Logger.LogMethodOrder(Logger.getOrderMessage("testInMemSyncBug18228", "TestSyncAfterStoppingComponent"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            deployEventProcess(new File(resourceFilePath + fsc + m_InMemAppFile));
            System.out.println("deployed event process " + resourceFilePath + fsc + m_InMemAppFile);
            eventManager.checkResourcesAndConnectivity(m_InMemAppGUID, m_version);
            System.out.println("done with CRC");
            eventManager.startEventProcess(m_InMemAppGUID, m_version, false);
            System.out.println("started service instance");
            Thread.sleep(5000);
            eventManager.stopServiceInstance(m_InMemAppGUID,appVersion, m_InMemSerInst);
            System.out.println("stopped service instance");
            eventManager.synchronizeEventProcess(m_InMemAppGUID, m_version);
            eventManager.startAllServices(m_InMemAppGUID, m_version);
            eventManager.stopEventProcess(m_InMemAppGUID, m_version);
            eventManager.deleteEventProcess(m_InMemAppGUID, m_version, true, true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testInMemSyncBug18228","TestSyncAfterStoppingComponent"));
        } catch (ServiceException e) {
            Assert.fail("failed due to service exception");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testInMemSyncBug18228","TestSyncAfterStoppingComponent"));
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    private static void deployEventProcess(File eventProcessZipFile) throws IOException {
        boolean complete = false;
        byte[] contents;
        BufferedInputStream bis = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(eventProcessZipFile));
        } catch (FileNotFoundException e) {
            throw new IOException("Could not find event process file for deployment to fes: " + eventProcessZipFile.getAbsolutePath());
        }

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
                eventManager.deployEventProcess(contents, complete);

            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Could not deploy event process file :" + eventProcessZipFile.getAbsolutePath());
        } catch (ServiceException e) {
            e.printStackTrace();
            throw new IOException("Could not deploy event process file :" + eventProcessZipFile.getAbsolutePath());
        } finally {
            bis.close();
        }

    }

}