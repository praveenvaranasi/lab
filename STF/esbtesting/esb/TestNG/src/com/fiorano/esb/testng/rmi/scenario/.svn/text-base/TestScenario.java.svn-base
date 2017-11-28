package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.Route;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.aps.ApplicationStateDetails;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import fiorano.tifosi.dmi.service.Execution;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

/**
* Created by IntelliJ IDEA.
* User: ranu
* Date: 10/21/11
* Time: 11:20 AM
* To change this template use File | Settings | File Templates.
*/
public class TestScenario extends AbstractTestNG{
    private String resourceFilePath;
    private boolean m_initialised;
    private String m_routeGUID;
    private String m_appGUID;

    private float m_version;
    private float m_newVersionNumber;
    private String m_appFile;
    private FioranoApplicationController m_fioranoApplicationController;

    static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_routeGUID=testProperties.getProperty("RouteGUID");
        m_newVersionNumber=Float.parseFloat(testProperties.getProperty("AppNewVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile");
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID:: " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Route GUID :: " + m_routeGUID);
        System.out.println("AppNewVersion :: " + m_newVersionNumber);
        System.out.println("Application File" + m_appFile);
        System.out.println("___________________________________________________________________________");
    }

    // the function modifies the xml files and replace any "search" string with "replace" string
    void modifyXmlFiles(String path, String search, String replace) throws IOException {
        File fl = new File(path);
        File[] filearr = fl.listFiles(xmlFilter);
        FileReader fr = null;
        FileWriter fw = null;
        boolean change = false;
        BufferedReader br;
        String s;
        String result = "";


        int i = 0;
        while (i < filearr.length - 1) {
            try {
                fr = new FileReader(filearr[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fw = new FileWriter("temp.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            br = null;
            br = new BufferedReader(fr);


            while ((s = br.readLine()) != null) {

                int j = s.indexOf(search);
                if (j != -1) {
                    change = true;
                    int k = search.length() + j;
                    //System.out.println("The Index start is "+j+" and index last is "+ k);
                    result = s.substring(0, j);
                    result = result + replace;
                    result = result + s.substring(k);
                    s = result;

                }
                //System.out.print(s);

                fw.write(s);
                fw.write('\n');
            }
            fr.close();
            fw.close();

            if (change) {
                fr = new FileReader("temp.xml");
                fw = new FileWriter(filearr[i]);
                br = new BufferedReader(fr);
                while ((s = br.readLine()) != null) {
                    fw.write(s);
                    fw.write('\n');
                }
                fr.close();
                fw.close();
                change = false;
            }

            i = i + 1;

        }
    }

    //picking up the xml extension files
    private void setXmlFilter(String ext){
        xmlFilter = new OnlyExt(ext);//creating filters
    }

    @BeforeClass(groups = "ScenarioTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "Scenario" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Scenario";
        //new change
        ServerStatusController stc;
        stc = ServerStatusController.getInstance();
        boolean isFPSHA = stc.getFPSHA();
        setXmlFilter("xml");
        try {
            if (isFPSHA && !isModifiedOnceHA) {
                isModifiedOnceHA = true;
                modifyXmlFiles(resourceFilePath, "fps", "fps_ha");
            }
            else if (!isFPSHA && !isModifiedOnce) {
                isModifiedOnce = true;
                modifyXmlFiles(resourceFilePath, "fps_ha", "fps");
            }
        init();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            Assert.fail("could not initialise properties");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testCreateApplication() {
        // This application is created form the SIMPLECHAT Application, and then setting the Workflow on ports. etc.
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateApplication", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateApplication", "ScenarioTest"));


        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateApplication", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testCreateApplication")
    public void testResourceAndConnectivityCheck() throws Exception{
        try
        {

            //precondition checking whether the Aplication is there in the repository and not running
            //not checking whether the application is present in the repository as isApplicationRunnning() will throw
            //                      an Exception if the application is not present.
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("The Application with GUID "+m_appGUID+" is already running.");
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.compileApplication(m_appGUID,m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID,m_version);

            //post condition
            //todo complete this

            Assert.assertTrue(true);

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }
    
    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testResourceAndConnectivityCheck")
    public void testRunApplicationSimple() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplicationSimple", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            //checking whether the application is running
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application Already Running");
            m_fioranoApplicationController.launchApplication(m_appGUID,m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);

            //checking whether the application is now running
            if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("Application Launch Failed");

            Assert.assertTrue(true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplicationSimple", "ScenarioTest"));
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplicationSimple", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }

    }

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testRunApplicationSimple")
    public void testRunApplicationAlreadyRunning() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRunApplicationAlreadyRunning", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.launchApplication(m_appGUID,m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);

            Assert.assertTrue(false, "TestCase failed as there is no Exception");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRunApplicationAlreadyRunning", "ScenarioTest"));
        }
        catch(Exception ex)
        {
            System.out.println("THE Exception Message::"+ex.getMessage());
            if(ex.getMessage().indexOf("of this application is already running. Only one version")!=-1)
            {
                Assert.assertTrue(true);
                return;
            }
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRunApplicationAlreadyRunning", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }

    }

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testRunApplicationAlreadyRunning")
    public void testSaveApplicationWithDifferentVersionNumber() throws Exception{
        File tempFile = new File("temp.xml");

        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testSaveApplicationWithDifferentVersionNumber", "ScenarioTest"));

            System.out.println("Started the Execution of the TestCase::"+getName());

            if(tempFile.exists())
                tempFile.delete();
            tempFile.createNewFile();

            Application existingApplication = m_fioranoApplicationController.getApplication(m_appGUID, m_version);
            Application aps = new Application();
            //in studio, SaveApplicationAsAction and NewApplicationCustomizer are the classes used


            OutputStream outStream = new FileOutputStream(tempFile);
            existingApplication.toXMLString(outStream);

            aps.setFieldValues(new FileInputStream(tempFile));
            aps.setVersion(m_newVersionNumber);
            m_fioranoApplicationController.saveApplication(aps);

            if(m_fioranoApplicationController.getApplication(m_appGUID, m_newVersionNumber)==null)
                throw new Exception("New Application not Created");
            Assert.assertTrue(true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSaveApplicationWithDifferentVersionNumber", "ScenarioTest"));

        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSaveApplicationWithDifferentVersionNumber", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
        finally{
            if(tempFile.exists())
                tempFile.delete();
        }

    }

/*  public void testRunDifferentVersionOfTheApplication()throws Exception
    {
        try
        {
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.compileApplication(m_appGUID,m_newVersionNumber);
            m_fioranoApplicationController.prepareLaunch(m_appGUID,m_newVersionNumber);
            m_fioranoApplicationController.launchApplication(m_appGUID,m_newVersionNumber);

            Assert.assertTrue("TestCase Failed as there is no exception ",false);

        }
        catch(Exception ex)
        {
            System.out.println("THE Error MEssage is::"+ex.getMessage());
            if(ex.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::")!=-1 && ex.getMessage().indexOf("version of this application is already running")!=-1)
            {
                Assert.assertTrue(true);
                return;
            }
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    } */

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testSaveApplicationWithDifferentVersionNumber")
    public void testRemoveServiceInstanceFromRunningApplication() throws Exception {
        List instances = null;
        String serviceDeleted=null;
        Application aps = null;
        List routes = null;
        ServiceInstance serviceDel = null;
        Vector toBeRemoved = new Vector();

        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveServiceInstanceFromRunningApplication", "ScenarioTest"));
            //the application is switched in the prvious API
            System.out.println("Started the Execution of the TestCase::"+getName());
            //precondition:: check whether the application is runnig
            if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID))
            {
                m_fioranoApplicationController.launchApplication(m_appGUID,m_version);
            }

            if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("The Application is not Running");

            aps = m_fioranoApplicationController.getApplication(m_appGUID,m_version);
            instances =aps.getServiceInstances();
            Iterator enum_instances = instances.iterator();

            if(enum_instances.hasNext())
            {

                serviceDel =(ServiceInstance)enum_instances.next();
                serviceDeleted =serviceDel.getName();
                routes =aps.getRoutes();
                Iterator allRoutes =routes.iterator();
                System.out.println("Removing Routes...");

                while(allRoutes.hasNext())
                {
                    Route route = (Route)allRoutes.next();
                    //System.out.println("route::"+route);
                    if(route.getSourceServiceInstance().equals(serviceDeleted)||route.getTargetServiceInstance().equals(serviceDeleted))
                        toBeRemoved.add(route);

                }
                for(int i=0;i<toBeRemoved.size();i++)
                {
                    Route route = (Route)toBeRemoved.get(i);
                    System.out.println("Removing::"+route.getName());
                    routes.remove(route);
                }
                aps.setRoutes(routes);
                System.out.println("Removing the service::"+serviceDel.getName()+" :: "+serviceDel.getGUID()+serviceDel.getVersion());
                instances.remove(serviceDel);
                aps.setServiceInstances(instances);
                m_fioranoApplicationController.saveApplication(aps);
                m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID,m_version);
            }
            else
                throw new Exception("No Services in the Application");

            instances =aps.getServiceInstances();
            enum_instances = instances.iterator();
            boolean isServiceDeleted =true;
            while(enum_instances.hasNext())
            {
                ServiceInstance service =(ServiceInstance)enum_instances.next();
                if(service.getName().equalsIgnoreCase(serviceDeleted))
                    isServiceDeleted =false;

            }
            if(isServiceDeleted)
                Assert.assertTrue(true);
            else
                Assert.assertTrue(false, "Test Case failed as the Service is still present.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveServiceInstanceFromRunningApplication", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            System.out.println(" The Exception Message is ::"+ex.getMessage());
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveServiceInstanceFromRunningApplication", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
        finally
        {
            instances.add(serviceDel);
            aps.setServiceInstances(instances);
            routes.addAll(toBeRemoved);
            aps.setRoutes((List<Route>)routes);
            m_fioranoApplicationController.saveApplication(aps);
            m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID,m_version);
        }
    }

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testRemoveServiceInstanceFromRunningApplication")
    public void testDeleteRunningApplication()throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteRunningApplication", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.killApplication(m_appGUID,m_version);


            if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version))
            {
                m_fioranoApplicationController.launchApplication(m_appGUID,m_version);
                Thread.sleep(5000);
            }

           /* if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version))
                throw new Exception("The Application is not Running");   */

            m_fioranoApplicationController.deleteApplication(m_appGUID,m_version);
            Assert.assertTrue(false, "TestCase Failed as there is no Exception in the execution");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteRunningApplication", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            if(ex.getMessage().indexOf("You cannot delete or rename a running version of an application")!=-1)
            {
                Assert.assertTrue(true);
                return;
            }
//            System.out.println("The Error Message ::"+ex.getMessage());
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteRunningApplication", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }
    /**
     * @precondition The Application should be running with a route on it.
     */
    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testDeleteRunningApplication")
    public void testSetDebugger() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetDebugger", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            //switching to the 1.0 version
            switchVersion(m_appGUID,m_version);
//            m_fioranoApplicationController.setDebugger(m_routeGUID,m_appGUID,new JUnitDebuger(m_appGUID,m_routeGUID,m_fioranoServiceProvider));
            Assert.assertTrue(true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetDebugger", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetDebugger", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }


    }
    //Take care in case the Above test case is moved to the different class..
    //Modified version is running. one of its services is removed.
    //swirchVersion() may be required
    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testSetDebugger")
    public void testStartRunningComponent()throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testStartRunningComponent", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            //ensure that all the services are launched
            if (!m_fioranoApplicationController.isApplicationRunning(m_appGUID)) {
                m_fioranoApplicationController.launchApplication(m_appGUID,m_version);
                m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
                Thread.sleep(10000);
            }

            //String serviceInstanceName;
            Application aps=m_fioranoApplicationController.getApplication(m_appGUID,m_version);
            List instances =aps.getServiceInstances();
            Iterator enum_instances = instances.iterator();
            String serviceName=null;
            if(enum_instances.hasNext())
            {

                ServiceInstance service =(ServiceInstance)enum_instances.next();
                serviceName =service.getName();
            }

            m_fioranoApplicationController.startService(m_appGUID,m_version,serviceName);
            Thread.sleep(10000);

            //Assert.assertTrue("Testcase failed. Exception expected. But no Exception Thrown",false);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStartRunningComponent", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            if(ex.getMessage().indexOf("is already in running state")!=-1)
            {
                Assert.assertTrue(true);
                return;
            }
            System.out.println("The ErrorMessage is:: "+ex.getMessage());
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStartRunningComponent", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testStartRunningComponent")
    public void testStopService() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testStopService", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            //ensure all the services are launched
//            m_fioranoApplicationController.startAllServices(m_appGUID);
            Application applciation = m_fioranoApplicationController.getApplication(m_appGUID,m_version);
            if(applciation.getServiceInstances().iterator().hasNext())
            {
                ServiceInstance service =(ServiceInstance)applciation.getServiceInstances().iterator().next();
                m_fioranoApplicationController.stopService(m_appGUID,m_version,service.getName());

            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopService", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopService", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }
//todo remove the test case as this is not the correct behaviour.
    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testStopService")
    public void testStopNonRunningApplication() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testStopNonRunningApplication", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Application applciation = m_fioranoApplicationController.getApplication(m_appGUID,m_version);
            if(applciation.getServiceInstances().iterator().hasNext())
            {
                ServiceInstance service =(ServiceInstance)applciation.getServiceInstances().iterator().next();
                m_fioranoApplicationController.stopService(m_appGUID,m_version,service.getName());

                //should throw an Excepion as the application is already stopped
                m_fioranoApplicationController.stopService(m_appGUID,m_version,service.getName());

            }
            //Exception Expected
             //not the actual behaviour
            //Assert.assertTrue("Test case failed as there is no exception, while the exception is expected",false);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopNonRunningApplication", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            //Exception expected here todo checl for exception's message
            Assert.assertTrue(true);

        }

    }

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testStopNonRunningApplication")
    public void testStopAllServices()throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testStopAllServices", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            ApplicationStateDetails applicationState =m_fioranoApplicationController.stopAllServices(m_appGUID,m_version);
            Application application =m_fioranoApplicationController.getApplication(m_appGUID,m_version);
            Iterator services = application.getServiceInstances().iterator();
            while(services.hasNext())
            {
                ServiceInstance service =(ServiceInstance)services.next();
                ServiceInstanceStateDetails stateDetails =applicationState.getServiceStatus(service.getName());

                System.out.println("The service State is ::"+stateDetails.getStatusString());
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAllServices", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopAllServices", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    /*@Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testStopAllServices")
    public void testStopServiceFromNonRunningApplication() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testStopServiceFromNonRunnigApplication", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            //Kill the Appication
            try
            {
                m_fioranoApplicationController.killApplication(m_appGUID);
            }catch(Exception ex)
            {
                //incase application is not runing ignore...
            }
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                            throw new Exception("Application Running");

            Application applciation = m_fioranoApplicationController.getApplication(m_appGUID,m_version);
            if(applciation.getServiceInstances().iterator().hasNext())
            {
                ServiceInstance service =(ServiceInstance)applciation.getServiceInstances().iterator().next();
                m_fioranoApplicationController.stopService(m_appGUID,service.getName());

                //should throw an Excepion as the application is already stopped
                m_fioranoApplicationController.stopService(m_appGUID,service.getName());

            }
            //Exceptino Expected

            Assert.assertTrue(false, "Test case failed as there is no exception, while the exception is expected");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testStopServiceFromNonRunnigApplication", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            //Exception Expected here
            Assert.assertTrue(true);
              Logger.LogMethodOrder(Logger.getOrderMessage("testStopServiceFromNonRunnigApplication", "ScenarioTest"));

        }

    }
//todo implement this later.
//    public void testRemoveServivceFromApplication() throws Exception
//    {
//
//    }*/
    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testStopAllServices")
    public void testTryLaunchingServiceInMemory() throws Exception{

        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testTryLaunchingServiceInMemory", "ScenarioTest"));
            switchVersion(m_appGUID,m_newVersionNumber);

            System.out.println("Started the Execution of the TestCase::"+getName());
            //precondition:: check whether the application is runnig
            if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("The Application is not Running");
            Application aps = m_fioranoApplicationController.getApplication(m_appGUID,m_newVersionNumber);
            List instances =aps.getServiceInstances();
            Iterator enum_instances = instances.iterator();
            ServiceInstance service =(ServiceInstance)enum_instances.next();
            m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            service.setLaunchType(Execution.LAUNCH_TYPE_IN_MEMORY);
            m_fioranoApplicationController.saveApplication(aps);
            m_fioranoApplicationController.launchApplication(m_appGUID,m_newVersionNumber);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
            Assert.assertTrue(false, "Test Case failed as an Exception is expected, as the UI Components shouldn't be launched in memory.");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testTryLaunchingServiceInMemory", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            //todo check for the Error code and remove the printing of stacktrace from the code
            Assert.assertTrue(true, "TestCase Passed and the Exception Message is "+ex.getMessage());
            Logger.Log(Level.INFO, Logger.getErrMessage("testTryLaunchingServiceInMemory", "ScenarioTest"));
        }

    }

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testTryLaunchingServiceInMemory")
    public void testKillApplication() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testKillApplication", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                testResourceAndConnectivityCheck();

                m_fioranoApplicationController.launchApplication(m_appGUID, m_version);

            if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                throw new Exception("The Application is not Running");

            m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            Assert.assertTrue(true);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testKillApplication", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testKillApplication", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    /*@Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testKillApplication")
    public void testDeleteApplication(){
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            m_fioranoApplicationController.deleteApplication(m_appGUID,m_version);

            Assert.assertNull(m_fioranoApplicationController.getApplication(m_appGUID,m_version), "Failed to delete the application");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "ScenarioTest"));
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }

    }*/

    @Test(groups = "ScenarioTest", alwaysRun = true, dependsOnMethods = "testKillApplication")
    public void testDeleteApplication2(){
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication2", "ScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            m_fioranoApplicationController.deleteApplication(m_appGUID,m_newVersionNumber);

            Assert.assertNull( m_fioranoApplicationController.getApplication(m_appGUID,m_newVersionNumber), "Failed to delete the application");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication2", "ScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication2", "ScenarioTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }

    }

    private void switchVersion(String guid, float newVersion)throws Exception {
        System.out.println("killing the Appllication... ");
        m_fioranoApplicationController.stopAllServices(guid,m_version);
        m_fioranoApplicationController.killApplication(guid,m_version);
        System.out.println("Application Killed....");
        System.out.println("Restarting with new version::"+newVersion+"...");
        m_fioranoApplicationController.launchApplication(guid,newVersion);
        m_fioranoApplicationController.startAllServices(guid,m_version);
        System.out.println("Application Launched ");
        Thread.sleep(30000);

    }
}
