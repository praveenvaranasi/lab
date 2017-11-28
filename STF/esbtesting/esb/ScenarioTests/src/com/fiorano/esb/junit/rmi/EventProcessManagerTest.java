package com.fiorano.esb.junit.rmi;

import com.fiorano.esb.server.api.*;
import com.fiorano.stf.test.core.RMITestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import javax.naming.Context;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: lokesh
 */
public class EventProcessManagerTest extends RMITestCase {

    private static final String TEST_STARTED = "Started the Execution of the TestCase";
    private static final String TEST_SUCCESS = "TestCase succeded";
    private static final String TEST_FAILURE = "TestCase failed ";
    private static final String DOUBLE_COLON = ":: ";
    private static final String USERNAME = "admin";
    private static final String PASSWORD = "passwd";



    private static IEventProcessManager eventManager;
    private static IRmiManager rmiManager;
    private static String rmiHandleID;
    protected static String resourceFilePath;

    private static String m_appGUID;
    private static String m_wsdappGUID;
    private static String m_httpappGUID;
    private static float m_version;
    private static String m_serviceInstance;
    private static String m_wsstubserviceInstance;
    private static String m_httpstubserviceInstance;
    private static String m_portName;
    private static String m_routeName;
    private static String m_moduleName;
  //  private static String m_logLevel;


    private static String tempDirPath;
    private static String tempFilePath;
    public static String listenerFilePath;
    private static String deployZipPath;
    private static String deployWsdZipPath;
    private static String deployHttpZipPath;
    private static String repoPath;
    private static String serverRepoPath;
    private static String fioranoHome;

    Hashtable m_logLevel = new Hashtable();

    private static MyListener listener;

    public EventProcessManagerTest(String name) throws Exception {
        super(name);
        rmiManager = rmiClient.getRmiManager();
        rmiHandleID = rmiManager.login(USERNAME, PASSWORD);
        eventManager = rmiManager.getEventProcessManager(rmiHandleID);
        //todo stop using rmiClient, use serverStatusController to get addresses. Reason: servers may be non different machines
        //as  a bonus, fix rmiCLient to fulfill this task
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "EventProcessManager";
        setUp();
        createTempDir();
    }

    public EventProcessManagerTest(TestCaseElement testCaseConfig) throws Exception {
        super(testCaseConfig);
        rmiManager = rmiClient.getRmiManager();
        rmiHandleID = rmiManager.login(USERNAME, PASSWORD);
        eventManager = rmiManager.getEventProcessManager(rmiHandleID);

        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "rmi" + File.separator + "EventProcessManager";
        setUp();
        createTempDir();
    }

    public void setUp() throws Exception {
        super.setUp();
        init();
//        System.out.println(TEST_STARTED + DOUBLE_COLON + getName());

    }

    public void init() throws Exception {
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_wsdappGUID = testCaseProperties.getProperty("WsdApplicationGUID");
        m_httpappGUID = testCaseProperties.getProperty("HttpApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_serviceInstance = testCaseProperties.getProperty("ServiceInstance");
        m_wsstubserviceInstance = testCaseProperties.getProperty("WSStubServiceInstance");
        m_httpstubserviceInstance = testCaseProperties.getProperty("HttpStubServiceInstance");
        m_portName = testCaseProperties.getProperty("PortName");
        m_routeName = testCaseProperties.getProperty("RouteName");
        m_moduleName = testCaseProperties.getProperty("ModuleName");
        //m_logLevel = testCaseProperties.getProperty("LogLevel");

        fioranoHome = testCaseProperties.getProperty("fioranoHome");
        tempDirPath = resourceFilePath + File.separator + "temp";
        listenerFilePath = resourceFilePath + File.separator + "temp" + File.separator + "listener.log";
        deployZipPath = resourceFilePath + File.separator + "testing.zip";
        deployWsdZipPath = resourceFilePath + File.separator + "wsdprocess.zip";
        deployHttpZipPath = resourceFilePath + File.separator + "httpprocess.zip";
        repoPath = testCaseProperties.getProperty("repoPath");
        serverRepoPath = testCaseProperties.getProperty("serverRepoPath");

        listener = new MyListener();
    }

    public void createTempDir() {
        File f = new File(tempDirPath);
        if (!f.exists()) f.mkdir();
    }

    public static Test suite() {
        return new TestSuite(EventProcessManagerTest.class);
    }

    public void testDeployEventProcess() {

        try {
            deployEventProcess(new File(deployZipPath));
            Assert.assertTrue(eventManager.exists(m_appGUID, m_version));
        } catch (ServiceException e) {
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }

    }

    //@org.junit.Test
    public void testExists() {
        tempFilePath = fioranoHome + repoPath;
        try {
            boolean existence, existFlag = false;
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            existence = eventManager.exists(m_appGUID, m_version);
            File files[] = new File(tempFilePath).listFiles();
            for (File file : files) {
                if (file.isDirectory() && file.getName().equals(m_appGUID))
                    existFlag = true;
            }
            Assert.assertEquals(TEST_SUCCESS, existence, existFlag);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    public void testDependenciesExists() {
//        Assert.assertTrue("Test does not exist", false);
        ServiceReference serRef = new ServiceReference();
        serRef.setId(m_serviceInstance);
        ServiceReference serRefs[] = new ServiceReference[]{serRef};
        EventProcessReference eveRef = new EventProcessReference();
        eveRef.setId(m_appGUID);
        EventProcessReference eveRefs[] = new EventProcessReference[]{eveRef};
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.dependenciesExists(serRefs, eveRefs);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }

    }

    //@org.junit.Test
    public void testCheckResourcesAndConnectivity() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.checkResourcesAndConnectivity(m_appGUID, m_version);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testStartEventProcess() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.startEventProcess(m_appGUID, m_version, false);
            Thread.sleep(5000);
            try {
                eventManager.startEventProcess(m_appGUID, m_version, false);
            } catch (RemoteException e) {
            } catch (ServiceException e) {
                Assert.assertTrue(TEST_SUCCESS, true);
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testStopServiceInstance() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.stopServiceInstance(m_appGUID,m_version, m_serviceInstance);
            Thread.sleep(5000);
            try {
                eventManager.stopServiceInstance(m_appGUID,m_version, m_serviceInstance);
            } catch (RemoteException e) {
            } catch (ServiceException e) {
                Assert.assertTrue(TEST_SUCCESS, true);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testStartServiceInstance() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.startServiceInstance(m_appGUID,m_version, m_serviceInstance);
            Thread.sleep(5000);
            try {
                eventManager.startServiceInstance(m_appGUID,m_version, m_serviceInstance);
            } catch (RemoteException e) {
            } catch (ServiceException e) {
                Assert.assertTrue(TEST_SUCCESS, true);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testSynchronizeEventProcess() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.stopServiceInstance(m_appGUID,m_version, m_serviceInstance);
            Thread.sleep(5000);
            eventManager.synchronizeEventProcess(m_appGUID, m_version);
            Thread.sleep(5000);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //String appGuid ,String emptyEventProcess , String eventProcessWith1Chat ,String eventProcessWith2Chat,String checkFileLocation
    public void testSync() {

        String appGuid = "SYNC_TEST";
        IEventProcessManagerListener listener = null;
        ArrayList<String> launchedComponentsAppGuid1 = new ArrayList<String>();
        try {
            String emptyEventProcess = resourceFilePath + File.separator + testCaseProperties.getProperty("SyncTest0");
            String eventProcessWith1Chat = resourceFilePath + File.separator + testCaseProperties.getProperty("SyncTest1");
            String eventProcessWith2Chat = resourceFilePath + File.separator + testCaseProperties.getProperty("SyncTest2");
            String checkFileLocation = resourceFilePath + File.separator;
            boolean success = false;
            File emptyEventProcessZipFile = new File(emptyEventProcess);

            //launching empty
            deployEventProcess(emptyEventProcessZipFile);
            eventManager.checkResourcesAndConnectivity(appGuid, 1.0f);
            eventManager.startEventProcess(appGuid, 1.0f, false);
            Thread.sleep(5000);
            System.out.println("Empty event process lauch successful");

            /*add listener to listen for component launch event*/
            listener = new EventProcessManagerListener(launchedComponentsAppGuid1);
            eventManager.addEventProcessListener(listener);

            /* Synchronize after adding 1 chat component */
            File eventProcessZipFileWith1Chat = new File(eventProcessWith1Chat);

            deployEventProcess(eventProcessZipFileWith1Chat);
            eventManager.synchronizeEventProcess(appGuid, 1.0f);
            eventManager.startAllServices(appGuid, 1.0f);
            Thread.sleep(5000);

            if (!isLaunchSuccessful(appGuid, launchedComponentsAppGuid1))
                Assert.assertTrue(success);

            System.out.println("Synchronize after adding first chat component successful");

            /* Synchronize after adding second chat component */
            File eventProcessZipFileWith2Chat = new File(eventProcessWith2Chat);

            deployEventProcess(eventProcessZipFileWith2Chat);
            eventManager.synchronizeEventProcess(appGuid, 1.0f);
            eventManager.startAllServices(appGuid, 1.0f);
            Thread.sleep(5000);

            if (!isLaunchSuccessful(appGuid, launchedComponentsAppGuid1))
                Assert.assertTrue(success);

            System.out.println("Synchronize after adding second chat component successful");

            eventManager.removeEventProcessListener();
            success = true;
            Assert.assertTrue(success);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.assertTrue("Test failed because of:" + e.getMessage(), false);
        } finally {
            try {
                eventManager.stopEventProcess(appGuid, 1.0f);
                eventManager.removeEventProcessListener();
            } catch (RemoteException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean isSynchronizeSuccessful(String checkFile) {
        File f = new File(checkFile);
        if (f.exists())
            return true;
        else
            return false;
    }


    private void cleanCheckFiles(String checkFileList[]) {
        int i = 0;
        for (i = 0; i < checkFileList.length; i++) {
            File f = new File(checkFileList[i]);
            f.delete();
        }

    }

    //@org.junit.Test
    public void testStartAllServices() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.startAllServices(m_appGUID, m_version);
            Thread.sleep(5000);
            try {
                eventManager.startServiceInstance(m_appGUID,m_version, m_serviceInstance);
            } catch (RemoteException e) {
            } catch (ServiceException e) {
                Assert.assertTrue(TEST_SUCCESS, true);
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testRestartEventProcess() {
        System.out.println("--RestartEventProcess method does not work as expected--");
        System.out.println("--see bug # 17876--");

        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.stopEventProcess(m_appGUID, 1.0f);
            Thread.sleep(5000);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }

        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.startEventProcess(m_appGUID, m_version, false);
            Thread.sleep(10000);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }


//        Assert.assertTrue("TestCase failed because method does not work as expected",false);

    //@org.junit.Test
    public void testChangeRouteSelector() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            HashMap selectors = new HashMap();
//            eventManager.stopEventProcess(m_appGUID);
//            Thread.sleep(5000);
            ClassLoader loader = new URLClassLoader(new URL[]{new URL("file:" + fioranoHome + File.separator + "esb/shared/lib/all/esb-shared.jar")}, null);
            Class Rou = loader.loadClass("fiorano.tifosi.dmi.application.Route");
            Field f = Rou.getField("SELECTOR_SENDER");
            selectors.put(f.get(null), m_serviceInstance);
            eventManager.changeRouteSelector(m_appGUID,m_version, m_routeName, selectors);
            Assert.assertTrue(TEST_SUCCESS, true);
//            try {
//                eventManager.startEventProcess(m_appGUID, m_version);
//            }
//            catch (ServiceException se) {
//                se.printStackTrace();
//            }
//            Thread.sleep(5000);

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testSetLogLevel() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.setLogLevel(m_appGUID,m_version, m_serviceInstance, m_logLevel);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testGetLastErrTrace() {
//        String errTrace;
        int noOfLines = 10;
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.getLastErrTrace(noOfLines, m_serviceInstance, m_appGUID, m_version);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testGetLastOutTrace() {
//        String outTrace;
        int noOfLines = 10;
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.getLastOutTrace(noOfLines, m_serviceInstance, m_appGUID, m_version);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testExportServiceLogs() {
        tempFilePath = tempDirPath + File.separator + "ServiceLogs.zip";
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            byte buffer[] = eventManager.exportServiceLogs(m_appGUID, m_version, m_serviceInstance, 0);
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tempFilePath));
            output.write(buffer);
            output.close();
            File f = new File(tempFilePath);
            if (f.exists()) {
                Assert.assertTrue(TEST_SUCCESS, true);
            } else Assert.assertTrue(TEST_FAILURE, false);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testExportApplicationLogs() {
        tempFilePath = tempDirPath + File.separator + "AppLogs.zip";
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            byte buffer[] = eventManager.exportApplicationLogs(m_appGUID, m_version, 0);
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tempFilePath));
            output.write(buffer);
            output.close();
            File f = new File(tempFilePath);
            if (f.exists()) {
                Assert.assertTrue(TEST_SUCCESS, true);
            } else Assert.assertTrue(TEST_FAILURE, false);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testClearServiceErrLogs() {
        tempFilePath = fioranoHome + File.separator + "runtimedata/PeerServers/profile1/FPS/run/logs/";
        boolean errFlag = false;
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.clearServiceErrLogs(m_serviceInstance, m_appGUID, m_version);
            Thread.sleep(2000);
            File f = new File(tempFilePath + m_appGUID + File.separator + m_serviceInstance);
            if (f.exists() && f.isDirectory()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    if (file.getName().matches("^.*\\.err\\.\\d$")) {
                        errFlag = true;
                    }
                }
                if (errFlag) {
                    Assert.assertTrue(TEST_FAILURE, false);
                } else Assert.assertTrue(TEST_SUCCESS, true);
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testClearServiceOutLogs() {
        tempFilePath = fioranoHome + File.separator + "runtimedata/PeerServers/profile1/FPS/run/logs/";
        boolean outFlag = false;
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.clearServiceOutLogs(m_serviceInstance, m_appGUID, m_version);
            Thread.sleep(2000);
            File f = new File(tempFilePath + m_appGUID + File.separator + m_serviceInstance);
            if (f.exists() && f.isDirectory()) {
                File[] files = f.listFiles();
                for (File file : files) {
                    if (file.getName().matches("^.*\\.out\\.\\d$")) {
                        outFlag = true;
                    }
                }
                if (outFlag) {
                    Assert.assertTrue(TEST_FAILURE, false);
                } else Assert.assertTrue(TEST_SUCCESS, true);
            }
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testClearApplicationLogs() {
        tempFilePath = fioranoHome + File.separator + "runtimedata/PeerServers/profile1/FPS/run/logs/";

        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.clearApplicationLogs(m_appGUID, m_version);
            Thread.sleep(2000);
            if (listFiles(tempFilePath + m_appGUID)) {
                Assert.assertTrue(TEST_FAILURE, false);
            } else Assert.assertTrue(TEST_SUCCESS, true);

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    private static boolean listFiles(String pathname) {
        boolean fileFlag = false;
        File dir = new File(pathname);
        if (!dir.exists()) return false;
        File[] list = dir.listFiles();
        for (File f : list) {
            if (f.isFile()) {
                fileFlag = true;
            } else listFiles(f.getPath());
        }
        return fileFlag;
    }

    /**
     * TODO: find a better test, do something with subscribers
     */
    //@org.junit.Test
    public void testEnableSBW() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.enableSBW(m_serviceInstance, m_appGUID,m_version, m_portName, false, 0);
            Thread.sleep(5000);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testSetTrackedDataType() {
        int trackType = 4; //refer javadocs 4:Attachment
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.setTrackedDataType(m_serviceInstance, m_appGUID,m_version, m_portName, trackType);
            Thread.sleep(5000);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    /**
     * TODO: find a better test, do something with subscribers
     */
    //@org.junit.Test
    public void testDisableSBW() {
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.disableSBW(m_serviceInstance, m_appGUID,m_version, m_portName);
            Thread.sleep(5000);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    /* -----------------------------------------------------------------------*/

    /*

       public void testDocTrackersampletest() {

           try {
               File docTrackEventProcessZipFile = new File("/home/raja/fsoa_test/fioranodev/head/test.zip");
               deployEventProcess(docTrackEventProcessZipFile);
               eventManager.checkResourcesAndConnectivity("TEST", 1.0f);
               eventManager.startEventProcess("TEST", 1.0f);
               Thread.sleep(20000);
               eventManager.stopEventProcess("TEST");
               int count = countAllWorkFlowInstancesForApp("TEST");
               System.out.println("**************"+ count);
               Thread.sleep(20000);

               assertEquals(200, count);
           }
           catch (IOException e) {
               e.printStackTrace();
               Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
           } catch (InterruptedException e) {
               e.printStackTrace();
               Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
           } catch (ServiceException e) {
               e.printStackTrace();
               Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
           }
       }

    */
    public void testSample() {

        try {
            File docTrackEventProcessZipFile = new File("/home/raja/fsoa_test/fioranodev/head/test.zip");
            deployEventProcess(docTrackEventProcessZipFile);
            eventManager.checkResourcesAndConnectivity("TEST", 1.0f);
            eventManager.startEventProcess("TEST", 1.0f, false);
            Thread.sleep(15000);
            eventManager.stopEventProcess("TEST", 1.0f);
            int count = countAllWorkFlowInstancesForApp("TEST");
            System.out.println("**************" + count);
            Thread.sleep(5000);

            eventManager.startEventProcess("TEST", 1.0f, false);
            Thread.sleep(15000);
            eventManager.stopEventProcess("TEST", 1.0f);

        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        }
    }


    public void testDocTrack() {

        try {
            String docTrackEventProcess = resourceFilePath + File.separator + testCaseProperties.getProperty("doctracker");
            String _appGUID1 = testCaseProperties.getProperty("docTrackApp");
            File docTrackEventProcessZipFile = new File(docTrackEventProcess);
            deployEventProcess(docTrackEventProcessZipFile);
            eventManager.checkResourcesAndConnectivity(_appGUID1, 1.0f);
            eventManager.startEventProcess(_appGUID1, 1.0f, false);
            Thread.sleep(10000);
            eventManager.stopEventProcess(_appGUID1, 1.0f);
            int count = countAllWorkFlowInstancesForApp(_appGUID1);
            Thread.sleep(5000);

//            assertEquals(1000, count);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        }
//        finally {
//            String appGUID = testCaseProperties.getProperty("docTrackApp");
//            try {
//                eventManager.stopEventProcess(appGUID);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//            catch (ServiceException e) {
//                e.printStackTrace();
//            }
//        }
    }


    public static int countAllWorkFlowInstancesForApp(String appGUID) {
        try {

            /////// Connecting to RMI Connector Server////////////////
            JMXServiceURL address = new JMXServiceURL("rmi", "localhost", 0,
                    "/jndi/fmq");
            Map environment = new HashMap();

            environment.put(Context.INITIAL_CONTEXT_FACTORY,
                    "com.sun.jndi.rmi.registry.RegistryContextFactory");
            environment.put(Context.PROVIDER_URL, "rmi://" + "localhost" + ":" + 2047);

            String[] credentials = new String[]
                    {
                            "admin", "passwd"};

            environment.put("jmx.remote.credentials", credentials);

            JMXConnector connector = JMXConnectorFactory.newJMXConnector(address,
                    environment);

            connector.connect(environment);

            MBeanServerConnection conn = connector.getMBeanServerConnection();

            System.out.println("Connection Established");

            ObjectName object1 = new ObjectName(
                    "Fiorano.Esb.Sbw:ServiceType=SBWManager,Name=SBWManager");
            Object[] params = {"TEST"};
            String[] signatures = {String.class.getName()};
            Integer count = (Integer) conn.invoke(object1, "countAllWorkFlowInstancesForApp", params, signatures);

            connector.close();

            return count;


        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;
        }
    }


    /* Function to Deploy the event process */
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
            /*Keep calling deployEventProcess until all the contents of the event process has been deployed */
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

//    //@org.junit.Test
//            eventManager.getDebuggerKey(m_appGUID), this is method is not there within latest esb source. So its commented

//    public void testGetDebuggerKey() {
////        String debugKey;
//        try {
//            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
//            eventManager.getDebuggerKey(m_appGUID);
//            Assert.assertTrue(TEST_SUCCESS, true);
//        }
//        catch (ServiceException e) {
//            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
//        }
//        catch (Exception e) {
//            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
//        }
//
//    }

    //@org.junit.Test
    public void testGetEventProcess() {
        tempFilePath = tempDirPath + File.separator + "EventProcess.zip";
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            byte buffer[];
            ArrayList<byte[]> bufs = new ArrayList<byte[]>();
//            int i=0
            int lenght = 0;
            do {
                buffer = eventManager.getEventProcess(m_appGUID, m_version, lenght);
                bufs.add(buffer);
                lenght += buffer.length;
//                i+=buffer.length;
            }
            while (buffer.length >= 40960);

            byte buffer2[] = new byte[lenght];
            int i = 0;
            for (byte[] b : bufs) {
                System.arraycopy(b, 0, buffer2, i, b.length);
                i += b.length;
            }
            BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tempFilePath));
            output.write(buffer2);
            output.close();

            try {
                new ZipFile(tempFilePath);
                Assert.assertTrue(TEST_SUCCESS, true);
            } catch (ZipException e) {
                e.printStackTrace();
                Assert.assertTrue(TEST_FAILURE + " due to " + e.getMessage(), false);
            }

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    /**
     * TODO: better test, check in estudio runtime repo
     */
    //@org.junit.Test
    public void testGetAllEventProcesses() {
//       EventProcessReference references[];
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.getAllEventProcesses();
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testGetApplicationStateDetails() {
        try {
//            EventProcessStateData epsd;
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.getApplicationStateDetails(m_appGUID,m_version);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    /**
     * TODO: better test, check in estudio runtime repo
     */
    //@org.junit.Test
    public void testGetEventProcessIds() {
//       String Ids[];
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.getEventProcessIds();
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testGetRunningEventProcesses() {
//       EventProcessReference references[];
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.getRunningEventProcesses();
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testGetVersions() {
//       float versions[];
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.getVersions(m_appGUID);
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testStopEventProcess() {
        /* written smaller test
EventProcessReference refs[];
       boolean startFlag=false;
       try
       {
           System.out.println(TEST_STARTED+DOUBLE_COLON + getName());
           refs=eventManager.getRunningEventProcesses(rmiHandleID);
           for(EventProcessReference ref : refs)
           {
               if(ref.getId().equals(m_appGUID))
               {
                   startFlag=true;
                   break;
               }
           }
           if(!startFlag)
           {
               try
               {  eventManager.startEventProcess(m_appGUID,m_version,IEventProcessManager.Environment.DEVELOPMENT,
                                                           rmiHandleID);
               }
               catch(Exception e)
               {
                  System.err.println( "TestCase Failed because of "+e.getMessage());
               }
           }
           eventManager.stopEventProcess(m_appGUID,rmiHandleID);
           Thread.sleep(2000);
           refs=eventManager.getRunningEventProcesses(rmiHandleID);
           startFlag=false;
           for(EventProcessReference ref : refs)
           {
               if(ref.getId().equals(m_appGUID))
               {
                   startFlag=true;
                   break;
               }
           }
           if(!startFlag)  Assert.assertTrue(TEST_SUCCESS,true);
           else Assert.assertTrue(TEST_FAILURE,false);
       }
       catch(ServiceException e)
        {
            Assert.assertTrue(TEST_FAILURE+e.getMessage(),false);
        }
        catch(Exception e)
        {
            System.err.println(TEST_FAILURE+DOUBLE_COLON+e.getClass().getSimpleName());
        }*/
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            eventManager.stopEventProcess(m_appGUID, 1.0f);
            Thread.sleep(4000);
//            try {
//                eventManager.stopEventProcess(m_appGUID);
//            } catch (RemoteException e) {
//            }
//            catch (ServiceException e) {
//                Assert.assertTrue(TEST_SUCCESS, true);
//            }

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    /**
     * this test requires starting an event process
     * class MyListener created for this test
     */
    //@org.junit.Test
    public void testAddEventProcessListener() {
        try {
            //MyListener listener = new MyListener();
//            System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
//            eventManager.stopEventProcess(m_appGUID);

            eventManager.addEventProcessListener(listener);
            try {
                eventManager.startEventProcess(m_appGUID, m_version, false);
                eventManager.startAllServices(m_appGUID, m_version);
            } catch (ServiceException se) {
                se.printStackTrace();
            }
            Thread.sleep(5000);
            File f = new File(listenerFilePath);
            FileReader reader = new FileReader(f);
            char c[] = new char[(int) f.length()];
            reader.read(c);
            if (new String(c).equals("ProcessStarted")) {
                Assert.assertTrue(TEST_SUCCESS, true);
            } else Assert.assertTrue(TEST_FAILURE, false);

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    /**
     * this test requires deleting an event process
     * class MyRepoListener created for this test
     */
    //@org.junit.Test
    public void testAddRepositoryEventListener() {
        try {
            MyRepoListener listener = new MyRepoListener();
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            try {
                eventManager.stopEventProcess(m_appGUID, 1.0f);
            } catch (ServiceException se) {
                se.printStackTrace();
            }

            Thread.sleep(2000);

            eventManager.addRepositoryEventListener(listener);
            eventManager.deleteEventProcess(m_appGUID, m_version, true, false);
            Thread.sleep(2000);
            File f = new File(listenerFilePath);
            FileReader reader = new FileReader(f);
            char c[] = new char[(int) f.length()];
            reader.read(c);

            if (new String(c).equals("ProcessDeleted")) {
                Assert.assertTrue(TEST_SUCCESS, true);
            } else Assert.assertTrue(TEST_FAILURE, false);

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testRemoveEventProcessListener() {
        long before, after;
        try {
            // MyListener listener = new MyListener();
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            File f = new File(listenerFilePath);
            before = f.length();
            deployEventProcess(new File(deployZipPath));
            testCheckResourcesAndConnectivity();
            Thread.sleep(5000);
            eventManager.removeEventProcessListener();
            eventManager.startEventProcess(m_appGUID, m_version, false);
            Thread.sleep(5000);
            after = f.length();

            if (before == after) {
                Assert.assertTrue(TEST_SUCCESS, true);
            } else Assert.assertTrue(TEST_FAILURE, false);

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    //@org.junit.Test
    public void testDeleteEventProcess() {
        boolean delFlag = false;
        tempFilePath = fioranoHome + repoPath;
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            try {
                eventManager.stopEventProcess(m_appGUID, 1.0f);
            } catch (ServiceException se) {
                se.printStackTrace();
            }
            Thread.sleep(3000);
            eventManager.deleteEventProcess(m_appGUID, m_version, true, false);
            Thread.sleep(3000);
            File files[] = new File(tempFilePath).listFiles();
            for (File file : files) {
                if (file.isDirectory() && file.getName().equals(m_appGUID)) {
                    delFlag = true;
                }
            }
            if (!delFlag) {
                Assert.assertTrue(TEST_SUCCESS, true);
            } else Assert.assertTrue(TEST_FAILURE, false);

        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    public void testAppWithRemoteServInst() throws ServiceException, IOException {

        String baseEventProcess = resourceFilePath + File.separator + testCaseProperties.getProperty("baseEventProcess");
        String remoteInstEventProcess = resourceFilePath + File.separator + testCaseProperties.getProperty("remoteEventProcess");
        String appGuid1 = "BASE";
        String appGuid2 = "REMOTE";
        boolean success = false;
        File baseEventProcessZipFile = new File(baseEventProcess);
        File remoteInsEventProcessZipFile = new File(remoteInstEventProcess);

        ArrayList<String> launchedComponentsAppGuid1 = new ArrayList<String>();
        ArrayList<String> launchedComponentsAppGuid2 = new ArrayList<String>();

        /*add listener to listen for component launch event*/

        IEventProcessManagerListener listenerAppGuid1 = new EventProcessManagerListener(launchedComponentsAppGuid1);
        eventManager.addEventProcessListener(listenerAppGuid1);

        IEventProcessManagerListener listenerAppGuid2 = new EventProcessManagerListener(launchedComponentsAppGuid2);
        eventManager.addEventProcessListener(listenerAppGuid2);


        // CRC and Launch of base event process
        try {
            deployEventProcess(baseEventProcessZipFile);
            eventManager.checkResourcesAndConnectivity(appGuid1, 1.0f);
            eventManager.startEventProcess(appGuid1, 1.0f, false);
            eventManager.startAllServices(appGuid1, 1.0f);

        } catch (ServiceException e) {
            e.printStackTrace();

        }


        System.out.println("Base event process lauch successful ***** ");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }

        assertEquals(isLaunchSuccessful(appGuid1, launchedComponentsAppGuid1), true);


        // CRC and Launch of Application with remote instance
        try {
            deployEventProcess(remoteInsEventProcessZipFile);
            eventManager.checkResourcesAndConnectivity(appGuid2, 1.0f);
            eventManager.startEventProcess(appGuid2, 1.0f, false);
            eventManager.startAllServices(appGuid2, 1.0f);


        } catch (ServiceException e) {
            e.printStackTrace();
        }


        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertEquals(isLaunchSuccessful(appGuid2, launchedComponentsAppGuid2), true);


        System.out.println("Event process with Remote Service Instance Launch successful *****");

        try {
            System.out.println("Stoping remote EP first ...... ");
            eventManager.stopEventProcess(appGuid2, 1.0f);
            Thread.sleep(3000);
            System.out.println("Stoping local EP...");
            eventManager.stopEventProcess(appGuid1, 1.0f);
        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }

        eventManager.removeEventProcessListener();
        eventManager.removeEventProcessListener();
    }


    private static boolean isLaunchSuccessful(String appGuid, ArrayList<String> launchedComponents) throws ServiceException, RemoteException {
        Enumeration<String> list = eventManager.getApplicationStateDetails(appGuid,m_version).getAllServiceNames();

        while (list.hasMoreElements()) {
            String currentInst = list.nextElement();
            if (currentInst.lastIndexOf('.') != -1)             // dont check for remote service instance launch as they are checked by their actuall application
                continue;

            if (!launchedComponents.contains(currentInst))
                return false;
        }

        return true;
    }


    //@org.junit.Test
    public void testRemoveRepositoryEventListener() {
        long before, after;
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            File f = new File(listenerFilePath);
            before = f.length();
            eventManager.removeRepositoryEventListener();
//               eventManager.deleteEventProcess(m_appGUID,m_version,rmiHandleID);
            after = f.length();
            if (before == after) {
                Assert.assertTrue(TEST_SUCCESS, true);
            } else Assert.assertTrue(TEST_FAILURE, false);

        } catch (ServiceException e) {
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    public void testViewWSDL() {
        String url;
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            File tempZipFile = new File(deployWsdZipPath);
            byte buffer[] = new byte[(int) tempZipFile.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(tempZipFile));
            input.read(buffer, 0, buffer.length);
            input.close();
            eventManager.deployEventProcess(buffer, true);
            eventManager.checkResourcesAndConnectivity(m_wsdappGUID, m_version);
            eventManager.startEventProcess(m_wsdappGUID, m_version, false);
            Thread.sleep(5000);
            url = eventManager.viewWSDL(m_wsdappGUID,m_version, m_wsstubserviceInstance);
            Assert.assertTrue(TEST_SUCCESS + url, true);
        } catch (ServiceException e) {
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    public void testViewHttpContext() {
        String url;
        try {
            //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());
            File tempZipFile = new File(deployHttpZipPath);
            byte buffer[] = new byte[(int) tempZipFile.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(tempZipFile));
            input.read(buffer, 0, buffer.length);
            input.close();
            eventManager.deployEventProcess(buffer, true);
            eventManager.checkResourcesAndConnectivity(m_httpappGUID, m_version);
            eventManager.startEventProcess(m_httpappGUID, m_version, false);
            Thread.sleep(5000);
            url = eventManager.viewHttpContext(m_httpappGUID,m_version, m_httpstubserviceInstance);
            Assert.assertTrue(TEST_SUCCESS + url, true);
        } catch (ServiceException e) {
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    public void testMyFinally() {
        removeFile(tempDirPath);
    }

    private static void removeFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            File files[] = f.listFiles();
            for (File file : files) {
                if (!file.isDirectory()) file.delete();
                else {
                    removeFile(file.getPath());
                    file.delete();
                }
            }
            f.delete();
        }
    }


    public void deleteRunningAppTest() {
        //System.out.println(TEST_STARTED + DOUBLE_COLON + getName());

        try {
            deployEventProcess(new File(deployZipPath));
            testStartEventProcess();
            eventManager.deleteEventProcess(m_appGUID, m_version, true, false);
        } catch (ServiceException e) {
            Assert.assertTrue(TEST_SUCCESS, true);
        } catch (Exception e) {
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }

    }

    public void cacheDisableTest() {
        try {
            Application app = ApplicationParser.readApplication(new File(fioranoHome + serverRepoPath + "/SIMPLECHAT/1.0"), Application.Label.development.toString(), true);
            app.setComponentCached(false);
            ApplicationParser.writeApplication(app, new File(tempDirPath + "/temp_simplechat"));
            File directory = new File(tempDirPath + "/temp_simplechat");
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempDirPath + "/temp_simplechat/temp_simplechat.zip"));
            zip(directory, directory, zos);
            zos.close();
            File tempZipFile = new File(tempDirPath + "/temp_simplechat/temp_simplechat.zip");
            byte buffer[] = new byte[(int) tempZipFile.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(tempZipFile));
            input.read(buffer, 0, buffer.length);
            input.close();
            eventManager.deployEventProcess(buffer, true);
            eventManager.checkResourcesAndConnectivity("SIMPLECHAT", 1.0f);
            eventManager.startEventProcess("SIMPLECHAT", 1.0f, false);
        } catch (ServiceException e) {
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    public void emptyProcessTest() {
        try {
            File f = new File(tempDirPath + "/EMPTYPROCESS/1.0/env");
            f.mkdirs();
            f = new File(tempDirPath + "/EMPTYPROCESS/1.0/env/development.xml");
            f.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(f));
            String dev_content = "<?xml version='1.0' encoding='UTF-8'?>" +
                    "<target:target xmlns:target=\"http://fiorano.com/environment/1.0\"" +
                    " xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" " +
                    "xsi:schemaLocation=\"http://fiorano.com/environment/1.0 env.xsd\" />";
            writer.write(dev_content);
            writer.flush();
            writer.close();
            File fi = new File(tempDirPath + "/EMPTYPROCESS/1.0/EventProcess.xml");
            fi.createNewFile();
            String proc_content = "<application guid=\"EMPTYPROCESS\" version=\"1.0\">" +
                    "<display name=\"EMPTYPROCESS\" categories=\"User Processes\" />" +
                    "<creation date=\"05-11-2009 05:01:57\" /><deployment label=\"Development\" />" +
                    "</application>";
            writer = new BufferedWriter(new FileWriter(fi));
            writer.write(proc_content);
            writer.flush();
            writer.close();


            File directory = new File(tempDirPath + "/EMPTYPROCESS/1.0");
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(tempDirPath + "/EMPTYPROCESS/emptyproc.zip"));
            zip(directory, directory, zos);
            zos.close();

            File tempZipFile = new File(tempDirPath + "/EMPTYPROCESS/emptyproc.zip");
            byte buffer[] = new byte[(int) tempZipFile.length()];
            BufferedInputStream input = new BufferedInputStream(new FileInputStream(tempZipFile));
            input.read(buffer, 0, buffer.length);
            input.close();
            eventManager.deployEventProcess(buffer, true);
            eventManager.checkResourcesAndConnectivity("EMPTYPROCESS", 1.0f);
            eventManager.startEventProcess("EMPTYPROCESS", 1.0f, false);
            Thread.sleep(4000);


        } catch (ServiceException e) {
            e.printStackTrace();
            Assert.assertTrue(TEST_FAILURE + e.getMessage(), false);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(TEST_FAILURE + DOUBLE_COLON + e.getClass().getSimpleName());
        }
    }

    private static final void zip(File directory, File base,
                                  ZipOutputStream zos) throws IOException {
        File[] files = directory.listFiles();
        byte[] buffer = new byte[8192];
        int read = 0;
        for (int i = 0, n = files.length; i < n; i++) {
            if (files[i].isDirectory()) {
                zip(files[i], base, zos);
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                ZipEntry entry = new ZipEntry(files[i].getPath().substring(
                        base.getPath().length() + 1));
                zos.putNextEntry(entry);
                while (-1 != (read = in.read(buffer))) {
                    zos.write(buffer, 0, read);
                }
                in.close();
            }
        }
    }

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();
        methodsOrder.add("testDeployEventProcess");
        methodsOrder.add("testExists");
        methodsOrder.add("testDependenciesExists");
        methodsOrder.add("testCheckResourcesAndConnectivity");
        methodsOrder.add("testStartEventProcess");
        methodsOrder.add("testStartAllServices");
        methodsOrder.add("testStopServiceInstance");
        methodsOrder.add("testStartServiceInstance");
        methodsOrder.add("testSynchronizeEventProcess");
        methodsOrder.add("testSync");

//        methodsOrder.add("testRestartEventProcess");       //broken

        methodsOrder.add("testSetLogLevel");
        methodsOrder.add("testGetLastErrTrace");
        methodsOrder.add("testGetLastOutTrace");
        methodsOrder.add("testExportServiceLogs");
        methodsOrder.add("testExportApplicationLogs");
        methodsOrder.add("testClearServiceErrLogs");
        methodsOrder.add("testClearServiceOutLogs");
        methodsOrder.add("testClearApplicationLogs");
        methodsOrder.add("testEnableSBW");
        methodsOrder.add("testSetTrackedDataType");
        methodsOrder.add("testDisableSBW");
//        methodsOrder.add("testDocTrackersampletest");
//        methodsOrder.add("testSample");
        methodsOrder.add("testChangeRouteSelector");
        methodsOrder.add("testAppWithRemoteServInst");

        methodsOrder.add("testGetEventProcess");
        methodsOrder.add("testGetAllEventProcesses");
        methodsOrder.add("testGetApplicationStateDetails");
        methodsOrder.add("testGetEventProcessIds");
        methodsOrder.add("testGetRunningEventProcesses");
        methodsOrder.add("testGetVersions");
        methodsOrder.add("testStopEventProcess");
        methodsOrder.add("testAddEventProcessListener");
        methodsOrder.add("testAddRepositoryEventListener");
        methodsOrder.add("testRemoveEventProcessListener");
        methodsOrder.add("testRemoveRepositoryEventListener");
        methodsOrder.add("testDeleteEventProcess");
        methodsOrder.add("testViewWSDL");
        methodsOrder.add("testViewHttpContext");
        methodsOrder.add("deleteRunningAppTest");
        methodsOrder.add("cacheDisableTest");
        methodsOrder.add("emptyProcessTest");
        methodsOrder.add("testMyFinally");

        return methodsOrder;
    }

}

// TODO:use it for removing dependency on Thread.sleep
class TestListener extends UnicastRemoteObject implements IEventProcessManagerListener, Serializable {
    public TestListener() throws RemoteException {
        super();
    }

    /**
     * @deprecated
     */
    public void eventProcessDeleted(float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    /**
     * @deprecated
     */
    public void eventProcessDeployed(float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void serviceInstanceStarted(String s, String s1, String s2, float v, String s3) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void serviceInstanceStopped(String s, String s1, String s2, float v, String s3) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void routeBreakPointAdded(String s, float v, String s1) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void routeBreakPointRemoved(String s, float v, String s1) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void eventProcessStarted(String s, float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void eventProcessStarting(String s, float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void eventProcessStopped(String s, float v) throws RemoteException {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    public void serviceInstanceStarting(String s, String s1, String s2, float v, String s3) throws java.rmi.RemoteException {

    }
    public void eventProcessSynchronized(String var1, float var2) throws RemoteException{

    }
}