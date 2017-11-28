package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.BreakpointMetaData;
import com.fiorano.esb.server.api.IDebugger;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.Publisher;
import com.fiorano.esb.testng.rmi.Receiver;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ServiceInstance;
import org.testng.Assert;
import org.testng.annotations.Test;
import com.fiorano.esb.rtl.application.FioranoApplicationController;

import javax.jms.Message;
import javax.jms.TextMessage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.security.PublicKey;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: anurag
 * Date: 3/5/12
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDeleteRouteTransformation21397 extends AbstractTestNG{

    private IEventProcessManager eventProcessManager;
    private String appName = "BUG_21397";
    private TestEnvironmentConfig testEnvConfig;
    private ServerStatusController serverStatusController;
    private FioranoApplicationController m_fioranoappcontroller;
    private SampleEventProcessListener epListener = null;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private float appVersion = 1.0f;
    @Test(groups="Bugs" ,alwaysRun = true)
    public void InitSetup(){
        this.eventProcessManager= rmiClient.getEventProcessManager();
        this.testEnvConfig= ESBTestHarness.getTestEnvConfig();
        this.serverStatusController=ServerStatusController.getInstance();

        try {
            //this.m_fioranoappcontroller = serverStatusController.getServiceProvider().getapplicationController();
            this.m_fioranoappcontroller = serverStatusController.getServiceProvider().getApplicationController();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "Test_DeleteRouteTransformation"), e);
        }

        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Bugs. as eventProcessManager is not set.");
        }

    }

     @Test(groups="Bugs",description="Exception while deleting the route with transformation using NONE launch type", dependsOnMethods="InitSetup", alwaysRun = true)
    public void Test_DeleteRouteTransformationLaunch()
     {
        Logger.LogMethodOrder(Logger.getOrderMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"));
        Logger.Log(Level.SEVERE, Logger.getExecuteMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"));

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do ep listener!", e);
        }

        try {
            deployEventProcess("Bug_21397.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do CRC!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do CRC!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "2");
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("Test_DeleteRouteTransformationLaunch", "Test_DeleteRouteTransformation"), e);
            Assert.fail("Failed to do operation on service instance!", e);
        }
     }

    @Test(groups="Bugs",dependsOnMethods ="Test_DeleteRouteTransformationLaunch",alwaysRun=true)
    public void TestCase_Bug21397(){
       Logger.LogMethodOrder(Logger.getOrderMessage("TestCase_Bug21397","Test_DeleteRouteTransformation"));

        try {
            eventProcessManager.stopServiceInstance(appName,appVersion,"Feeder1");
        } catch (RemoteException e) {
           Logger.Log(Level.SEVERE,Logger.getErrMessage("TestCase_Bug21397","Test_DeleteRouteTransformation"));
            Assert.fail("Failed to stop feeder1",e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestCase_Bug21397","Test_DeleteRouteTransformation"));
        }

        try{
        Application application = m_fioranoappcontroller.getApplication(appName,appVersion);
        List<ServiceInstance> serviceInstanceList = application.getServiceInstances();
         for(ServiceInstance serviceInstanc: serviceInstanceList){
                if(serviceInstanc.getName().equalsIgnoreCase("Feeder1")){
                    serviceInstanc.setLaunchType(0);
                    System.out.print(serviceInstanc.getLaunchType());
                }
         }
        //serviceInstanceList.get(0).getOutputPortInstance("OUT_PORT").setPriority(2);
         application.setServiceInstances(serviceInstanceList);
            m_fioranoappcontroller.saveApplication(application);
        }catch(Exception e)
        {
            e.printStackTrace();
        }


        try {
            //eventProcessManager.deleteServiceInstance(appName,appVersion,"Feeder1");
            deployEventProcess("Bug_21397_AfterDeleteService.zip");
        } catch (Exception e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestCase_Bug21397","Test_DeleteRouteTransformation"));
            Assert.fail("Failed in testcase",e);
        }



    }
    @Test(groups = "Bugs", dependsOnMethods = "TestCase_Bug21397", alwaysRun = true)
        public void stopAndDeleteApplication() {
            Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "Test_ComponentPortChange"));
            try {
                eventProcessManager.stopEventProcess(appName, appVersion);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                }
                eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication","Test_ComponentPortChange"), e);
                Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "Test_ComponentPortChange"), e);
                Assert.fail("Failed to do stop/delete EP as part of cleanup", e);
            }
            Logger.Log(Level.SEVERE, Logger.getFinishMessage("stopAndDeleteApplication", "Test_ComponentPortChange"));
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
