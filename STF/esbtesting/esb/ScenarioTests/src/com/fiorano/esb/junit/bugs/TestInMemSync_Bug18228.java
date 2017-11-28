package com.fiorano.esb.junit.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.stf.test.core.RMITestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import junit.framework.Assert;

import java.io.*;

/**
 * Created by IntelliJ IDEA.
 * User: lokesh2
 * Date: Nov 9, 2009
 * Time: 3:58:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestInMemSync_Bug18228 extends RMITestCase {

    private static final String TEST_STARTED = "Started the Execution of the TestCase";
    private static final String TEST_SUCCESS = "TestCase succeded";
    private static final String TEST_FAILURE = "TestCase failed ";
    private static final String DOUBLE_COLON = ":: ";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "passwd";



    private static IEventProcessManager eventManager;
    private static IRmiManager rmiManager;
    private static String rmiHandleID;
    private String resourceFilePath;

    private static String m_InMemAppGUID;
    private static String m_wsdappGUID;
    private static String m_httpappGUID;
    private static float m_version;
    private static String m_InMemSerInst;
    private static String m_wsstubserviceInstance;
    private static String m_httpstubserviceInstance;
    private static String m_portName;
    private static String m_routeName;
    private static String m_moduleName;
    private static String m_logLevel;

    public TestInMemSync_Bug18228(String name) throws Exception {
        super(name);
        rmiManager = rmiClient.getRmiManager();
        rmiHandleID = rmiManager.login(USERNAME, PASSWORD);
        eventManager = rmiManager.getEventProcessManager(rmiHandleID);
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "bugs";

    }


    public TestInMemSync_Bug18228(TestCaseElement testCaseConfig) throws Exception {
        super(testCaseConfig);
        rmiManager = rmiClient.getRmiManager();
        rmiHandleID = rmiManager.login(USERNAME, PASSWORD);
        eventManager = rmiManager.getEventProcessManager(rmiHandleID);
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "bugs";


    }

    public void setUp() throws Exception {
        super.setUp();
        init();
        System.out.println(TEST_STARTED + DOUBLE_COLON + getName());

    }

    public void init() throws Exception {
        m_InMemAppGUID = testCaseProperties.getProperty("InMemAppGUID");
        m_InMemSerInst = testCaseProperties.getProperty("InMemSerInst");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
    }

    public void createTempDir() {

    }


    public void testInMemSyncBug18228() throws IOException {
        try {
            deployEventProcess(new File(resourceFilePath + File.separator + "inmem.zip"));
            eventManager.checkResourcesAndConnectivity(m_InMemAppGUID, m_version);
            eventManager.startEventProcess(m_InMemAppGUID, m_version, false);
            Thread.sleep(5000);
            eventManager.stopServiceInstance(m_InMemAppGUID,m_version, m_InMemSerInst);
            {
                eventManager.synchronizeEventProcess(m_InMemAppGUID, m_version);
                eventManager.startAllServices(m_InMemAppGUID, m_version);
                Assert.assertTrue(TEST_SUCCESS, true);
            }
        } catch (ServiceException e) {
            Assert.assertTrue(TEST_FAILURE + " due to " + e.getMessage(), false);
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
