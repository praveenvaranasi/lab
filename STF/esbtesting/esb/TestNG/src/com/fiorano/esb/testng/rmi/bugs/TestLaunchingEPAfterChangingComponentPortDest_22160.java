
package com.fiorano.esb.testng.rmi.bugs;

/**
 * Created with IntelliJ IDEA.
 * User: ashhar
 * Date: 02/25/13
 * Time: 7:20 PM
 * To `change this template use File | Settings | File Templates.
 */

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.FESPerformanceStats;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.PortInstanceMetaData;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.server.api.SystemInfoReference;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationReference;
import fiorano.tifosi.dmi.application.PortInstance;
import org.testng.Assert;
import org.testng.annotations.Test;
//import org.w3c.dom.*;
//import org.xml.sax.SAXException;
//
//import javax.management.ObjectName;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

public class TestLaunchingEPAfterChangingComponentPortDest_22160 extends  AbstractTestNG
{
    private IServiceProviderManager serviceProviderManager;
    private IEventProcessManager eventProcessManager;
    //private IEventProcessManager eventProcessManager2;
    private IFPSManager ifpsManager;
    private FioranoApplicationController m_fioranoApplicationController;
    private SampleEventProcessListener epListener = null;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();

    private void print(String s)
    {
        System.out.println(s);
    }


    @Test(groups = "Bugs", alwaysRun = true, description = "initialise Managers")
    public void startSetup()
    {
        //FES Test
        this.serviceProviderManager = rmiClient.getServiceProviderManager();
        if (serviceProviderManager == null)
        {
            Assert.fail("Cannot run group 'Bugs' as serviceProviderManager is not set.");
        }

        //FPS Test
        this.ifpsManager = rmiClient.getFpsManager();
        if (ifpsManager == null)
        {
            Assert.fail("Cannot run group 'Bugs' as ifpsManager is not set.");
        }


        // initialise eventProcessManager
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null)
        {
            Assert.fail("Cannot run as eventProcessManager is not set.");
        }

        // initialise m_fioranoApplicationController

        try
        {
            m_fioranoApplicationController = ServerStatusController.getInstance().getServiceProvider().getApplicationController();
        } catch (STFException e)
        {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestLaunchingEPAfterChangingComponentPortDest_22160"), e);
            Assert.fail("Failed to run test,since we could not fioranoapplicationcontroller!", e);
        }

    }


    @Test(groups = "Bugs", alwaysRun = true, description = "Deploy EP", dependsOnMethods = "startSetup")
    public void deployEP() throws IOException, ServiceException
    {

        try
        {
            epListener = new SampleEventProcessListener("EP_TEST", eventStore);
            System.out.println("----------- eplistener1 created -------------");
        } catch (RemoteException e1)
        {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deployEP", "TestLaunchingEPAfterChangingComponentPortDest_22160"), e1);
            // Assert.fail("Failed to create ep listener!", e1);
        }

        Map<String, String> map = new TreeMap<String, String>(eventStore);
        System.out.println(map);


        try
        {
            eventProcessManager.addEventProcessListener(epListener);
            System.out.println("----------- eplistener1 added -------------");
        } catch (RemoteException e)
        {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deployEP", "TestLaunchingEPAfterChangingComponentPortDest_22160"), e);
            // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e)
        {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("deployEP", "TestLaunchingEPAfterChangingComponentPortDest_22160"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }


        deployEventProcess("ep_test-1.0@EnterpriseServer.zip");/*, eventProcessManager1);*/

         print("------deployed------");


    }

    @Test(groups = "Bugs", alwaysRun = true, description = "Change Port Destination Type", dependsOnMethods = "deployEP")
    public void changeEPDisplayPortDestination() throws TifosiException
    {
        //m_fioranoApplicationController = rtlClient.getFioranoApplicationController();

        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }


        Application application = m_fioranoApplicationController.getApplication("EP_TEST", 1.0f);
//
//        if(application.getServiceInstance("Display1").getInputPortInstance("IN_PORT")==null)
//            print("----- application.getServiceInstance(\"Display1\").getInputPortInstance(\"IN_PORT\") = null");

        application.getServiceInstance("Display1").getOutputPortInstance("OUT_PORT").setDestinationType(0); // 0 for queue
    }


    @Test(groups = "Bugs", alwaysRun = true, description = "Run EP", dependsOnMethods = "changeEPDisplayPortDestination")
    public void runEP() throws TifosiException, RemoteException, ServiceException
    {
        //m_fioranoApplicationController = rtlClient.getFioranoApplicationController();

        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        eventProcessManager.checkResourcesAndConnectivity("EP_TEST", 1.0f);


        eventProcessManager.startEventProcess("EP_TEST", 1.0f, false);

    }

    @Test(groups = "Bugs", alwaysRun = true, description = "Kill EP.Display", dependsOnMethods = "runEP")
    public void killEPDisplay() throws RemoteException, ServiceException
    {

//        System.out.println("isEP1 running = " + eventProcessManager.isRunning("BUG_23026_EP1"));
//        System.out.println("isEP2 running = " + eventProcessManager.isRunning("BUG_23026_EP2"));

        try
        {
            Thread.sleep(8000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        eventProcessManager.stopServiceInstance("EP_TEST", 1.0f, "Display1");

    }

    @Test(groups = "Bugs", alwaysRun = true, description = "Change Port Destination Type to Topic", dependsOnMethods = "killEPDisplay")
    public void changeEPDisplayPortDestinationToTopic() throws TifosiException
    {
        //m_fioranoApplicationController = rtlClient.getFioranoApplicationController();

        try
        {
            Thread.sleep(5000);
        } catch (InterruptedException e)
        {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Application application = m_fioranoApplicationController.getApplication("EP_TEST", 1.0f);
//
//        if(application.getServiceInstance("Display1").getInputPortInstance("IN_PORT")==null)
//            print("----- application.getServiceInstance(\"Display1\").getInputPortInstance(\"IN_PORT\") = null");

        application.getServiceInstance("Display1").getOutputPortInstance("OUT_PORT").setDestinationType(1); // 1 for topic
    }

    @Test(groups = "Bugs", alwaysRun = true, description = "Synchronise EP", dependsOnMethods = "changeEPDisplayPortDestinationToTopic")
    public void synchroniseEP1() throws RemoteException, ServiceException
    {

        eventProcessManager.synchronizeEventProcess("EP_TEST", 1.0f);

    }




    /*
       * deploys an event process which is located under $testing_home/esb/TestNG/resources/
       *
       * @param zipName - name of the event process zip located under $testing_home/esb/TestNG/resources/
       * @throws java.io.IOException - if file is not found or for any other IO error
   * @throws ServiceException
   */
    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException
    {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" +
                File.separator + "resources" + File.separator + zipName));
        boolean completed = false;
        byte result[];
        while (!completed)
        {
            byte[] temp = new byte[1024];
            int readcount = 0;
            readcount = bis.read(temp);

            if (readcount < 0)
            {
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