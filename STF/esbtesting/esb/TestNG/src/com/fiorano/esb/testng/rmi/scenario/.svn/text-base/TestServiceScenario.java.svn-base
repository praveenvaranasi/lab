package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.service.Deployment;
import fiorano.tifosi.dmi.service.Resource;
import fiorano.tifosi.dmi.service.Service;
import fiorano.tifosi.dmi.sps.ServiceSearchContext;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;

/**
* Created by IntelliJ IDEA.
* User: ranu
* Date: 10/24/11
* Time: 11:52 AM
* To change this template use File | Settings | File Templates.
*/
public class TestServiceScenario extends AbstractTestNG{
    private FioranoServiceRepository m_fioranoServiceRepository;
    private String resourceFilePath;
    private String m_serviceGUID;
    private String m_version;
    private static final String ICON_FILE_NAME = "JUNIT_SERVICE_SCENARIO.png";
    private String m_iconFile;
    private String m_newVersion;
    private StringTokenizer m_resources;
    private String m_resourceFile;
	private String m_DBResource;
    private static final String ICON32_FILE_NAME = "JUNIT_SERVICE_SCENARIO_LARGE_ICON.png";
                               
    public void init() throws Exception{
        m_serviceGUID=testProperties.getProperty("ComponentGUID");
        m_version=testProperties.getProperty("Version");
        m_iconFile=resourceFilePath+File.separator+testProperties.getProperty("IconFile");
        m_newVersion =testProperties.getProperty("NewVersionNumber");
        m_resources = new StringTokenizer(testProperties.getProperty("Resources"),",");
        m_resourceFile = resourceFilePath+File.separator+testProperties.getProperty("ResourceFile");
		m_DBResource = resourceFilePath+File.separator+testProperties.getProperty("DBResource");
    }
    protected void printInitParams(){
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: "+m_serviceGUID+"\tVersion:: "+m_version);
        System.out.println("new Version:: "+m_newVersion);
        System.out.println("The Icon File Path:: "+m_iconFile);
        System.out.println("The Resource File Path:: "+m_resourceFile);
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "ServiceScenarioTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "ServiceScenario" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "ServiceScenario";
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
        try {
            init();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetUp", "TestServiceScenario"));
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testGetAllServices() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetAllServices", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Enumeration services =m_fioranoServiceRepository.getAllServicesInRepository();
            Enumeration serviceGUIDsEnum=m_fioranoServiceRepository.getAllServiceGUIDs();
            Vector serviceGUIDs = new Vector(50,15);
            while(serviceGUIDsEnum.hasMoreElements())
                    serviceGUIDs.add(serviceGUIDsEnum.nextElement());

            while(services.hasMoreElements())
            {
                Service sps =(Service)services.nextElement();
                String serviceGUID = sps.getGUID();
                serviceGUIDs.remove(serviceGUID);
            }

            if(!serviceGUIDs.isEmpty())
                throw new Exception("All The Services are not returned. The Services not returned yet are::"+serviceGUIDs);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetAllServices", "TestServiceScenario"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetAllServices", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetAllServices")
    public void testGetAllNonLaunchableServices() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetAllNonLaunchableServices", "TestServiceScenario"));
            Enumeration nlServices = m_fioranoServiceRepository.getAllNonLaunchableServices();
            while (nlServices.hasMoreElements()) {
                Service sps = (Service) nlServices.nextElement();
                System.out.println("Fetched non-launchable service::" + sps.getDisplayName());
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetAllNonLaunchableServices", "TestServiceScenario"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetAllNonLaunchableServices", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetAllNonLaunchableServices")
    public void testGetAllVersionsOfService() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetAllVersionsOfService", "TestServiceScenario"));
            Enumeration services = m_fioranoServiceRepository.getAllVersionsOfService(m_serviceGUID, false);
            Assert.assertTrue(services.hasMoreElements());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetAllVersionsOfService", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetAllVersionsOfService", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetAllVersionsOfService")
    public void testSearchService() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Logger.LogMethodOrder(Logger.getOrderMessage("testSearchService", "TestServiceScenario"));
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID, m_version);
            ServiceSearchContext ssc = new ServiceSearchContext();
            ssc.setName(service.getDisplayName());
            Enumeration services = m_fioranoServiceRepository.searchServices(ssc);
            Assert.assertTrue(services.hasMoreElements());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSearchService", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSearchService", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testSearchService")
    public void testGetServiceInfo() throws Exception {
         System.out.println("Started the Execution of the TestCase::" + getName());

        try {
           Logger.LogMethodOrder(Logger.getOrderMessage("testGetServiceInfo", "TestServiceScenario"));
           Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID, null);
           Assert.assertTrue(m_serviceGUID.equalsIgnoreCase(service.getGUID()));
           Logger.Log(Level.INFO, Logger.getFinishMessage("testGetServiceInfo", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServiceInfo", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }

          try {
            m_fioranoServiceRepository.getServiceInfo(null, null);
            Assert.assertTrue(false, "TestCase Failed - exception expected");
        }
        catch (Exception ex) {
        }

         try {
            m_fioranoServiceRepository.getServiceInfo("miskoozi", null);
            Assert.assertTrue(false, "TestCase Failed - exception expected");
        }
        catch (Exception ex) {
        }

         try {
            m_fioranoServiceRepository.getServiceInfo("miskoozi", m_version);
            Assert.assertTrue(false, "TestCase Failed - exception expected");
        }
        catch (Exception ex) {
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetServiceInfo")
    public void testReloadRepository() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testReloadRepository", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoServiceRepository.reloadRepository();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testReloadRepository", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testReloadRepository", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testReloadRepository")
    public void testUpgradeServiceSimple(){
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testUpgradeServiceSimple", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoServiceRepository.copyServiceVersion(m_serviceGUID,m_version,m_serviceGUID,m_newVersion);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testUpgradeServiceSimple", "TestServiceScenario"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testUpgradeServiceSimple", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testUpgradeServiceSimple")
    public void testGetAllUncommitedServices() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetAllUncommitedServices", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Enumeration services =m_fioranoServiceRepository.getAllUnCommittedServices();
            Enumeration serviceGUIDsEnum=m_fioranoServiceRepository.getAllUnCommittedServiceGUIDs();
            Vector  serviceGUIDs = new Vector();
            while(serviceGUIDsEnum.hasMoreElements())
            {
                serviceGUIDs.add(serviceGUIDsEnum.nextElement());
            }

            while(services.hasMoreElements())
            {
                Service sps =(Service)services.nextElement();
                String serviceGUID = sps.getGUID();
                String version = String.valueOf(sps.getVersion());
                Assert.assertFalse(m_fioranoServiceRepository.isServiceCommitted(serviceGUID,version));
                serviceGUIDs.remove(serviceGUID);
            }

            if(!serviceGUIDs.isEmpty())
                throw new Exception("All The Services are not returned. The Services not returned yet are::"+serviceGUIDs);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetAllUncommitedServices", "TestServiceScenario"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetAllUncommitedServices", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetAllUncommitedServices")
    public void testCommitInvalidService() throws Exception{
        try{

            Logger.LogMethodOrder(Logger.getOrderMessage("testCommitInvalidService", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            float newVersion = Float.parseFloat(m_newVersion) + 2;

            //this testcase should fail
            m_fioranoServiceRepository.commitService(m_serviceGUID, Float.toString(newVersion));

            Assert.assertTrue(false, "TestCase Failed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCommitInvalidService", "TestServiceScenario"));

        }
        catch(Exception ex){

            System.err.println("TestCase Failed as it was expected - Continue with testing other cases");
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCommitInvalidService", "TestServiceScenario"), ex);
        }

    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testCommitInvalidService")
    public void testEditService() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Logger.LogMethodOrder(Logger.getOrderMessage("testEditService", "TestServiceScenario"));
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID, m_newVersion);
            m_fioranoServiceRepository.editService(m_serviceGUID, m_newVersion, service);
            m_fioranoServiceRepository.saveService(service);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testEditService", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testEditService", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testEditService")
    public void testSaveService() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Logger.LogMethodOrder(Logger.getOrderMessage("testSaveService", "TestServiceScenario"));
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID, m_newVersion);
            m_fioranoServiceRepository.editService(m_serviceGUID, m_newVersion, service);
            m_fioranoServiceRepository.saveService(service);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSaveService", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSaveService", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testSaveService")
    public void testPublishService() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Logger.LogMethodOrder(Logger.getOrderMessage("testPublishService", "TestServiceScenario"));
            m_fioranoServiceRepository.publishService(m_serviceGUID, m_newVersion,null,null);
            Assert.assertTrue(m_fioranoServiceRepository.isServiceCommitted(m_serviceGUID, m_version));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testPublishService", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testPublishService", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testPublishService")
    public void testUpdateIcon16() throws Exception{

        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testUpdateIcon16", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID,m_newVersion);
            service.setIcon16(ICON_FILE_NAME);
            m_fioranoServiceRepository.editRegisteredService(m_serviceGUID,m_newVersion,service);
            m_fioranoServiceRepository.updateIcon(m_serviceGUID, m_newVersion, ICON_FILE_NAME, m_iconFile);

            //check whether the Icon is updated or not.

            String smallIcon= "smallIcon_temp.png";
            m_fioranoServiceRepository.fetchResourceFromServiceRepository(m_serviceGUID,m_newVersion,ICON_FILE_NAME,smallIcon);
            if( !compareBinaryFiles(m_iconFile,smallIcon))
                throw new Exception("The Icon File Updated to the Server dosent match with the file "+m_iconFile);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testUpdateIcon16", "TestServiceScenario"));

        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testUpdateIcon16", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }

    }

    /**
     * If the Icon File Dosent Exist.
     * @throws Exception
     */
    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testUpdateIcon16")
    public void testUpdateIcon16Negative() throws Exception {

        try
        {   Logger.LogMethodOrder(Logger.getOrderMessage("testUpdateIcon16Negitive", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID,m_newVersion);
            String nonExistingIconFile = "NonExistingFile"+new Date().getTime();
            service.setIcon16(ICON_FILE_NAME);
            m_fioranoServiceRepository.editRegisteredService(m_serviceGUID,m_newVersion,service);
            m_fioranoServiceRepository.updateIcon(m_serviceGUID, m_newVersion, ICON_FILE_NAME, nonExistingIconFile);

            Assert.assertTrue(false, "The Negitive Test Case Failed as there is no Exception in Execution");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testUpdateIcon16Negitive", "TestServiceScenario"));

        }
        catch(Exception ex)
        {
            System.out.println("The Error Message is:: "+ex.getMessage());
            //todo update the test case after the bug 10420 is fixed
            if(! (ex instanceof TifosiException ||  ex.getMessage().equals("ADDING_RESOURCE_TO_SERVICE_ERROR ::")))
            {
                System.err.println("Exception in the Execution of test case::"+getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testUpdateIcon16Negitive", "TestServiceScenario"), ex);
                Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
            }
        }

    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testUpdateIcon16Negative")
    public void testUpdateIcon32() throws Exception{

        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testUpdateIcon32", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            System.out.println("Updating the Service::"+m_serviceGUID+" Version: "+m_newVersion+" with "+ICON32_FILE_NAME);
            m_fioranoServiceRepository.updateIcon32(m_serviceGUID, m_newVersion, ICON32_FILE_NAME, m_iconFile);

            //check whether the Icon is updated or not.

            String largeIcon= "largeIcon_temp.png";
            m_fioranoServiceRepository.fetchResourceFromServiceRepository(m_serviceGUID,m_newVersion,ICON32_FILE_NAME,largeIcon);
            if( !compareBinaryFiles(m_iconFile,largeIcon))
                throw new Exception("The Icon File Updated to the Server dosent match with the file "+m_iconFile);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testUpdateIcon32", "TestServiceScenario"));


        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testUpdateIcon32", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }

    }

    // api deprecated
//    public void testAddIcon32() throws Exception{
//
//        try
//        {
//            System.out.println("Started the Execution of the TestCase::"+getName());
//
//            System.out.println("Updating the Service::"+m_serviceGUID+" Version: "+m_newVersion+" with new icon"+ICON32_FILE_NAME);
//            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID,m_newVersion);
//            service.setIcon32(ICON_FILE_NAME);
//            m_fioranoServiceRepository.editRegisteredService(m_serviceGUID,m_newVersion,service);
//            m_fioranoServiceRepository.updateIcon32(m_serviceGUID, m_newVersion, ICON32_FILE_NAME, m_iconFile);
//
//            m_fioranoServiceRepository.close();
//
//            //check whether the Icon is updated or not.
//
//            String largeIcon= "largeIcon_temp.png";
//            m_fioranoServiceRepository.fetchResourceFromServiceRepository(m_serviceGUID,m_newVersion,ICON32_FILE_NAME,largeIcon);
//            if( !compareBinaryFiles(m_iconFile,largeIcon))
//                throw new Exception("The Icon File Updated to the Server dosent match with the file "+m_iconFile);
//
//
//
//        }
//        catch(Exception ex){
//            System.err.println("Exception in the Execution of test case::"+getName());
//            ex.printStackTrace();
//            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
//
//        }
//
//    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testUpdateIcon32")
     public void testGetServiceIcon32() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetServiceIcon32", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            byte[] b = m_fioranoServiceRepository.getServiceIcon32(m_serviceGUID,m_version);
            Assert.assertNotNull(b);

            byte[] b2 = m_fioranoServiceRepository.getServiceIcon32(m_serviceGUID,null);
            Assert.assertNotNull(b2);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetServiceIcon32", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServiceIcon32", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetServiceIcon32")
    public void testGetServiceIcon16() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetServiceIcon16", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            byte[] b = m_fioranoServiceRepository.getServiceIcon(m_serviceGUID,m_version);
            Assert.assertNotNull(b);

            byte[] b2 = m_fioranoServiceRepository.getServiceIcon(m_serviceGUID,null);
            Assert.assertNotNull(b2);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetServiceIcon16", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServiceIcon16", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    /**
     * If the Icon File Dosent Exist.
     * @throws Exception
     */
    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetServiceIcon16")
    public void testUpdateIcon32Negative() throws Exception{

        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testUpdateIcon32Negitive", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID,m_newVersion);
            String nonExistingIconFile = "NonExistingFile"+new Date().getTime();
            service.setIcon32(ICON_FILE_NAME);
            m_fioranoServiceRepository.editRegisteredService(m_serviceGUID,m_newVersion,service);
            m_fioranoServiceRepository.updateIcon32(m_serviceGUID, m_newVersion, ICON_FILE_NAME, nonExistingIconFile);

            Assert.assertTrue(false, "The Negitive Test Case Failed as there is no Exception in Execution");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testUpdateIcon32Negitive", "TestServiceScenario"));

        }
        catch(Exception ex)
        {
            System.out.println("The Error Message is:: "+ex.getMessage());

            //todo update the test case after the bug 10420 is fixed
            if(! (ex instanceof TifosiException ||  ex.getMessage().equals("ADDING_RESOURCE_TO_SERVICE_ERROR ::")))
            {
                System.err.println("Exception in the Execution of test case::"+getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testUpdateIcon32Negitive", "TestServiceScenario"), ex);
                Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
            }
        }

    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testUpdateIcon32Negative")
    public void testRemoveAllResources() throws Exception {
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveAllResources", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Resource resource = new Resource();
            while(m_resources.hasMoreTokens())
            {
                String resourceName = m_resources.nextToken();
                resource.setName(resourceName);
                System.out.println("Removing the Resource::"+resourceName);
                m_fioranoServiceRepository.removeResource(m_serviceGUID,m_newVersion,resource);
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveAllResources", "TestServiceScenario"));


        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveAllResources", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testRemoveAllResources")
    public void testAddResources() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddResources", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            while(m_resources.hasMoreTokens())
            {
                String resourceName = m_resources.nextToken();
                Resource resource = new Resource();
                resource.setName(resourceName);
                m_fioranoServiceRepository.addResource(m_serviceGUID,m_newVersion,resource,m_resourceFile);
				System.out.println("TESTCASE");
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testAddResources", "TestServiceScenario"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddResources", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }
                                                       
    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testAddResources")
	public void testDBResources() throws Exception {
		try
        {
				System.out.println("Started the Execution of the TestCase::"+getName());
                String resourceName = "ojdbc14.jar";
                Resource resource = new Resource();
                resource.setName(resourceName);
                m_fioranoServiceRepository.addResource("jdbc","4.0",resource,m_DBResource);

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testDBResources")
    public void testGetResources() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetResources", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Enumeration en = m_fioranoServiceRepository.getAllResourcesForService(m_serviceGUID,m_version);
            Assert.assertTrue(en.hasMoreElements());
            while(en.hasMoreElements())
            {
                Resource res = (Resource)en.nextElement();
                Assert.assertTrue(m_fioranoServiceRepository.hasResource(m_serviceGUID,m_version,res.getName()));
                long modified = m_fioranoServiceRepository.getLastModified(m_serviceGUID,m_version,res.getName());
                Assert.assertTrue(modified>0);
                long size = m_fioranoServiceRepository.getSize(m_serviceGUID,m_version,res.getName());
                Assert.assertTrue(size>0);
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetResources", "TestServiceScenario"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetResources", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }
//todo add the test cases after the Bug::10419 is fixed
//    public void testDeleteIcon() throws Exception
//    {
//
//        try
//        {
//            System.out.println("Started the Execution of the TestCase::"+getName());
//            m_fioranoServiceRepository.updateIcon(m_serviceGUID, m_newVersion, ICON_FILE_NAME, null);
//
//            //check whether the Icon is Deleted or not.
//
//            try{
//                String smallIcon= "smallIcon_temp.png";
//                m_fioranoServiceRepository.fetchResourceFromServiceRepository(m_serviceGUID,m_newVersion,ICON_FILE_NAME,smallIcon);
//            } catch(TifosiException e){
//                //Exception Expected here
//                //todo Check for the Error Message
//                Assert.assertTrue(true);
//                return;
//            }
//
//            throw new Exception("The Icon File Updated to the Server dosent match with the file "+m_iconFile);
//
//        }
//        catch(Exception ex){
//            System.err.println("Exception in the Execution of test case::"+getName());
//            ex.printStackTrace();
//            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
//
//        }
//
//    }
//
//    public void testDeleteIcon32() throws Exception
//    {
//
//        try
//        {
//            System.out.println("Started the Execution of the TestCase::"+getName());
//            m_fioranoServiceRepository.updateIcon32(m_serviceGUID, m_newVersion, ICON_FILE_NAME, null);
//
//            //check whether the Icon is Deleted or not.
//
//            try{
//                String smallIcon= "smallIcon_temp.png";
//                m_fioranoServiceRepository.fetchResourceFromServiceRepository(m_serviceGUID,m_newVersion,ICON_FILE_NAME,smallIcon);
//            } catch(TifosiException e){
//                //Exception Expected here
//                //todo Check for the Error Message
//                Assert.assertTrue(true);
//                return;
//            }
//
//            throw new Exception("The Icon File Updated to the Server dosent match with the file "+m_iconFile);
//
//        }
//        catch(Exception ex){
//            System.err.println("Exception in the Execution of test case::"+getName());
//            ex.printStackTrace();
//            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
//
//        }
//
//    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testGetResources")
    public void testDeleteIcon() throws Exception {

        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteIcon", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            m_fioranoServiceRepository.updateIcon(m_serviceGUID, m_newVersion, null, null);
            m_fioranoServiceRepository.updateIcon32(m_serviceGUID, m_newVersion, null, null);

            byte[] b = m_fioranoServiceRepository.getServiceIcon(m_serviceGUID,m_newVersion);
            Assert.assertTrue(b==null);
            byte[] b32 = m_fioranoServiceRepository.getServiceIcon32(m_serviceGUID,m_newVersion);
            Assert.assertTrue(b32==null);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteIcon", "TestServiceScenario"));
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteIcon", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());
        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testDeleteIcon")
    public void testDeleteRegisteredService()throws Exception {
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteRegisteredService", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Enumeration enum_services =m_fioranoServiceRepository.removeService(m_serviceGUID,m_newVersion);
            while(enum_services.hasMoreElements())System.out.println("ELEMNET::"+enum_services.nextElement());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteRegisteredService", "TestServiceScenario"));
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteRegisteredService", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testDeleteRegisteredService")
    public void testDeleteWithInvalidData()throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteWithInvalidData", "TestServiceScenario"));
        System.out.println("Started the Execution of the TestCase::"+getName());

        try
        {
            m_fioranoServiceRepository.removeService("verdigrouz","4.0");
            Assert.assertTrue(false, "TestCase Failed - exception expected");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteWithInvalidData", "TestServiceScenario"));

        }
        catch(Exception ex)
        {

            System.err.println("TestCase Failed as it was expected - Continue with testing other cases");
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteWithInvalidData", "TestServiceScenario"), ex);
        }

        try
        {
            m_fioranoServiceRepository.removeService("verdigrouz","random");
            Assert.assertTrue(false, "TestCase Failed - exception expected");
        }
        catch(Exception ex)
        {
        }

        try
        {
            m_fioranoServiceRepository.removeService(null,"4.0");
            Assert.assertTrue(false, "TestCase Failed - exception expected");
        }
        catch(Exception ex)
        {
        }

        try
        {
            m_fioranoServiceRepository.removeService(null,null);
            Assert.assertTrue(false, "TestCase Failed - exception expected");
        }
        catch(Exception ex)
        {
        }

    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testDeleteWithInvalidData")
    public void testDeleteAllVersionsOfService()throws Exception {
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteAllVersionsOfService", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            String testGUID = "TESTSERVICE";

            m_fioranoServiceRepository.copyServiceVersion(m_serviceGUID,m_version,testGUID,"4.0");
//            m_fioranoServiceRepository.commitService(testGUID,"4.0");
            m_fioranoServiceRepository.copyServiceVersion(m_serviceGUID,m_version,testGUID,"4.1");
            m_fioranoServiceRepository.publishService(testGUID,"4.1", Deployment.PRODUCT,null);
            m_fioranoServiceRepository.copyServiceVersion(m_serviceGUID,m_version,testGUID,"4.2");
            m_fioranoServiceRepository.commitService(testGUID,"4.2");

            m_fioranoServiceRepository.removeService(testGUID,null);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteAllVersionsOfService", "TestServiceScenario"));
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteAllVersionsOfService", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    @Test(groups = "ServiceScenarioTest", alwaysRun = true, dependsOnMethods = "testDeleteAllVersionsOfService")
    public void testCloseServiceRepository()throws Exception {
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCloseServiceRepository", "TestServiceScenario"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoServiceRepository.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCloseServiceRepository", "TestServiceScenario"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCloseServiceRepository", "TestServiceScenario"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    private boolean compareBinaryFiles(String fileA,String fileB) throws IOException {
        boolean match;
        FileInputStream inputStreamB=null;
        FileInputStream inputStreamA=null;
        try{
            final int BLOCK_SIZE = 65536; //64 bytes
            byte[] streamABlock = new byte[BLOCK_SIZE];
            byte[] streamBBlock = new byte[BLOCK_SIZE];
            inputStreamA = new FileInputStream(fileA);
            inputStreamB = new FileInputStream(fileB);

            int bytesReadA;
            do{
                bytesReadA = inputStreamA.read(streamABlock);
                int bytesReadB = inputStreamB.read(streamBBlock);
                match = (bytesReadA==bytesReadB);
            } while(match && (bytesReadA>-1));

        } catch(IOException e){
            e.printStackTrace();
            match = false;
        }
        finally
        {
            inputStreamA.close();
            inputStreamB.close();
        }
        return match;
    }
}
