package com.fiorano.esb.testng.rmi.scenario.component;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.*;
import fiorano.tifosi.dmi.service.Service;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/13/11
 * Time: 12:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestAddComponent extends AbstractTestNG{
    private FioranoServiceRepository m_fioranoServiceRepository;
    private FioranoApplicationController m_fioranoApplicationController;
    private String resourceFilePath;
    private String m_appGUID;
    private Float m_version;
    private String m_appFile;

    public void init() throws Exception {
        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + getProperty("ApplicationFile");
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: " + m_appGUID + "\tVersion:: " + m_version);
        System.out.println("The Resource File Path:: " + resourceFilePath);
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "AddComponentTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Component" + fsc + "AddComponent" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Component" + fsc + "AddComponent";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        printInitParams();
    }

    @Test(groups = "AddComponentTest", alwaysRun = true, description = "Imports an application.")
    public void testImportApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testImportApplication", "TestAddComponent"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testImportApplication", "TestAddComponent"));
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testImportApplication", "TestAddComponent"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }
                                            
    @Test(groups = "AddComponentTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application.")
    public void testRunApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplication", "TestAddComponent"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(3000);
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplication", "TestAddComponent"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplication", "TestAddComponent"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }
    
    @Test(groups = "AddComponentTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "changes the cache property of a component and tests the change.")
     public void testAddComponent() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddcomponent", "TestAddComponent"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            // stopApplication(m_appGUID);
            System.out.println("Service instances before adding a new one");
            Iterator ir = application.getServiceInstances().iterator();
            while (ir.hasNext()) {
                ServiceInstance si = (ServiceInstance) ir.next();
                System.out.println("service instance::" + si.getName());
            }
           // Service service = rtlClient.getFioranoServiceRepository().getServiceInfo("Display", "4.0");
            ServiceInstance instance = new ServiceInstance();
            instance.setGUID("Display");
            instance.setName("Display123");
            instance.setVersion(4.0f);
            instance.setLaunchType(1);
            /*OutputPortInstance outputPortInstance= new OutputPortInstance();
            InputPortInstance inputPortInstance = new InputPortInstance();
            List inlist = new ArrayList();
            inlist.add(inputPortInstance);
            instance.setInputPortInstances(inlist);
            List outlist = new ArrayList();
            outlist.add(outputPortInstance);
            instance.setOutputPortInstances(outlist);*/
            String nodes[]={"fps"};
            instance.setNodes(nodes);
            List services = application.getServiceInstances();
            services.add(instance);
            application.setServiceInstances(services);
            m_fioranoApplicationController.saveApplication(application);

            m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID,m_version);
            Route route = new Route();
            route.setName("route2");
            route.setSourceServiceInstance("Feeder1");
            route.setSourcePortInstance("OUT_PORT");
            route.setTargetServiceInstance("Display123");
            route.setTargetPortInstance("IN_PORT");
            List routes = application.getRoutes();
            routes.add(route);
            application.setRoutes(routes);
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID,m_version);
            //m_fioranoApplicationController.stopAllServices(m_appGUID);
            System.out.println("Service instances after adding a new one");
            ir = application.getServiceInstances().iterator();
            while (ir.hasNext()) {
                ServiceInstance si = (ServiceInstance) ir.next();
                System.out.println("service instance::" + si.getName());
            }
            System.out.println("Routes after adding a new service instance");
            Iterator ir1 = application.getRoutes().iterator();
            while (ir1.hasNext()) {
                Route r = (Route) ir1.next();
                System.out.println("Route::" + r.getName());
            }
            m_fioranoApplicationController.startService(m_appGUID,m_version, "Display2");
            Thread.sleep(100000);
             Logger.Log(Level.INFO, Logger.getFinishMessage("testAddcomponent", "TestAddComponent"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            System.out.println("Exception message is ::" + ex.getMessage());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddComponent", "TestAddComponent"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "AddComponentTest", alwaysRun = true, dependsOnMethods = "testAddComponent", description = "stops a running application.")
    public void testKillApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "TestAddComponent"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application not killed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "TestAddComponent"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "TestAddComponent"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "AddComponentTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "Deletes the appication which is stopped.")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestAddComponent"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestAddComponent"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestAddComponent"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,m_version);
        } catch (TifosiException e) {
            if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1) {
                //never mind if the application is already running.
                return;
            }
            System.err.println("Error launching the application");
            e.printStackTrace();
            throw new FioranoException("Error launching the application", e);
        }
    }

    private void stopApplication(String appGUID) {
        try {
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);
        } catch (TifosiException e) {
            //ignore.
        }
    }
}
