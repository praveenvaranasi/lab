package com.fiorano.esb.testng.rmi.scenario.application;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.Route;
import fiorano.tifosi.dmi.application.ServiceInstance;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/13/11
 * Time: 10:29 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestAddComponent extends AbstractTestNG{

    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String componentName = "SMTP1";


    private void init() throws Exception {
        if (m_initialised)
            return;

        m_appGUID = getProperty("ApplicationGUID");
        m_version = Float.parseFloat(getProperty("ApplicationVersion"));
        //m_routeGUID=testCaseProperties.getProperty("RouteGUID");
        m_appFile = resourceFilePath + File.separator + testProperties.getProperty("ApplicationFile", "1.0.xml");
        //m_newTransformation = resourceFilePath+File.separator+testCaseProperties.getProperty("NewTransformation",null);
        printInitParams();
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        //System.out.println("Route GUID:: "+m_routeGUID);
        System.out.println("The Application File is::" + m_appFile);
        //System.out.println("The New Transformation File::"+m_newTransformation);
        System.out.println("..................................................................");

    }

    @BeforeClass(groups = "AddComponentTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario"+ fsc +"Application" + fsc + "AddComponent" + fsc + "tests.properties");
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Application" + fsc +  "AddComponent";
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    @Test(groups = "AddComponentTest", alwaysRun = true, dependsOnMethods = "startSetUp", description = "trying to import an application.")
        public void testImportApplication() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)//remember//
            {
                System.out.println("i am here");
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            }
        }
        catch (Exception ex) {
            System.out.println("Exception in the Execution of test case::" + getName());
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "AddComponentTest", alwaysRun = true, dependsOnMethods = "testImportApplication", description = "Runs the application.")
    public void testRunApplication() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            startApplication(m_appGUID, m_version);
            Thread.sleep(100000);

            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID, m_version))
                throw new Exception("The Application is not started.");

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "AddComponentTest", alwaysRun = true, dependsOnMethods = "testRunApplication", description = "Editing an application.")
    public void testEditApplication(){
        try{
           System.out.println("Started the Execution of the TestCase::" + getName());
           Application application = ApplicationParser.readApplication(new File(m_appFile));
            ServiceInstance instance=new ServiceInstance();
            instance.setName("Display2");

            Iterator ir=application.getRoutes().iterator();
            while(ir.hasNext())
            {
            Route route1=(Route)ir.next();
            System.out.println("Route is::"+ir.next());
            }
            application.addServiceInstance(instance);
            Route route=new Route();
            route.setName("route3");
            route.setSourceServiceInstance("SMTP1");
            route.setTargetServiceInstance("Display2");
            application.addRoute(route);
            System.out.println(route.getName());

            m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID,m_version);


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "AddComponentTest", alwaysRun = true, dependsOnMethods = "testEditApplication", description = "Killing an application.")
    public void testKillApplication() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "AddComponentTest", alwaysRun = true, dependsOnMethods = "testKillApplication", description = "deleting an application.")
    public void testDeleteApplication() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            stopApplication(m_appGUID);
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Applciation not killed");

            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            System.out.println("started application");
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,m_version);

            return;
        } catch (TifosiException e) {
            if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1)
            {
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
            System.out.println("Stopped the application");
            m_fioranoApplicationController.stopAllServices(appGUID,m_version);
            m_fioranoApplicationController.killApplication(appGUID,m_version);

        } catch (TifosiException e) {
            //ignore.
        }
    }
}
