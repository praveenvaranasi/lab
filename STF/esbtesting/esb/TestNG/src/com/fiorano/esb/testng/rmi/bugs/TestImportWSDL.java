package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
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

/**
 * Created by IntelliJ IDEA.
 * User: ramesh
 * Date: Dec 15, 2010
 * Time: 3:33:23 PM
 * To change this template use File | Settings | File Templates.
 */


public class TestImportWSDL extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "IMPORTWSDL";
    private TestEnvironmentConfig testEnvConfig;
    private SampleEventProcessListener epListener = null;
    private float appVersion = 1.0f;
    @Test(groups = "Bugs", alwaysRun = true)
    public void initSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Bugs. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "Bugs", description = "WsStub with WSDL imports - bug 19996 ", dependsOnMethods = "initSetup", alwaysRun = true)
    public void TestImportWSDlLaunch() {

        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("TestImportWSDlLaunch", "TestImportWSDL"));
        Logger.LogMethodOrder(Logger.getOrderMessage("TestImportWSDlLaunch", "TestImportWSDL"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("ImportWSDL.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startServiceInstance(appName,appVersion, "WSStub2");
            try {
                Thread.sleep(75000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "1");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        }

        try {
            testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String home = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME) + File.separator + "esb" + File.separator + "server" + File.separator +
                    "jetty" + File.separator + "fps" + File.separator + "webapps" + File.separator + "bcwsgateway" + File.separator + "wsdls" + File.separator + "MathOperations";
            File WSDLHome = new File(home);
            boolean isDirectory = WSDLHome.isDirectory();

            if (isDirectory) {
                File WSDLfile = new File(WSDLHome.getAbsolutePath() + File.separator + "MathOperations.wsdl");
                File ImportedWsdl = new File(WSDLHome.getAbsolutePath() + File.separator + "imports" + File.separator + "tempconvert.asmx?WSDL");
                if (WSDLfile.exists()) {
                    if (!ImportedWsdl.exists()){
                        //Assert.fail("Imported WSDL files are not getting stored even after setting storeImports to true");
                    }
                } else {
                   // Assert.fail("WSDL files for the WebService are not getting stored when the WSStub is started");
                }
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
           // Assert.fail("WSStub Operation failed to run properly", e);
        }

        try {
            eventProcessManager.stopServiceInstance(appName,appVersion, "WSStub2");
            try {
                Thread.sleep(20000);
            } catch (InterruptedException e) {
            }
            // // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "0");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestImportWSDlLaunch", "TestImportWSDL"), e);
            //Assert.fail("Failed to do operation on service instance!", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestImportWSDlLaunch", "TestImportWSDL"));
    }

    @Test(groups = "Bugs", dependsOnMethods = "TestImportWSDlLaunch", alwaysRun = true)
    public void stopAndDeleteApplication() {
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestImportWSDL"));
        try {
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestImportWSDL"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestImportWSDL"), e);
            //Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "TestImportWSDL"));
    }

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" +
                File.separator + "resources" + File.separator + zipName));
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

