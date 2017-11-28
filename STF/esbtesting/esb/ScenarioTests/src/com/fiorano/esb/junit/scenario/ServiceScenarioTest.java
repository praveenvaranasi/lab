/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.esb.junit.scenario;

import com.fiorano.stf.consts.JUnitConstants;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.logger.DRTTestLogger;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.service.*;
import fiorano.tifosi.dmi.sps.ServiceSearchContext;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Level;

/**
 *@author Sandeep M
 * @date 28th Dec 2006
 */
public class ServiceScenarioTest extends DRTTestCase
{
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

    public ServiceScenarioTest(String name)
    {
        super(name);
    }

    public ServiceScenarioTest(TestCaseElement testCaseConfig){
        super(testCaseConfig);
    }

    public static Test suite(){
        return new TestSuite(ServiceScenarioTest.class);
    }

    public void init() throws Exception{

        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "ServiceScenario";
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
        m_serviceGUID=testCaseProperties.getProperty(JUnitConstants.COMPONENT_GUID);
        m_version=testCaseProperties.getProperty(JUnitConstants.COMPONENT_VERSION);
        m_iconFile=resourceFilePath+File.separator+testCaseProperties.getProperty(JUnitConstants.ICON_FILE);
        m_newVersion =testCaseProperties.getProperty(JUnitConstants.NEW_VERSION_NUMBER);
        m_resources = new StringTokenizer(testCaseProperties.getProperty(JUnitConstants.RESOURCES),",");
        m_resourceFile = resourceFilePath+File.separator+testCaseProperties.getProperty(JUnitConstants.RESOURCE_FILE);
		m_DBResource = resourceFilePath+File.separator+testCaseProperties.getProperty("DBResource");
        printInitParams();

    }

    protected void printInitParams(){
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Service:: "+m_serviceGUID+"\tVersion:: "+m_version);
        System.out.println("new Version:: "+m_newVersion);
        System.out.println("The Icon File Path:: "+m_iconFile);
        System.out.println("The Resource File Path:: "+m_resourceFile);
        System.out.println("...........................................................................");
    }


    public void setUp() throws Exception{
        super.setUp();
        init();
    }

    public void testGetAllServices() throws Exception
    {
        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetAllServices", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Enumeration services =m_fioranoServiceRepository.getAllServicesInRepository();
            Enumeration serviceGUIDsEnum=m_fioranoServiceRepository.getAllServiceGUIDs();
            Vector  serviceGUIDs = new Vector(50,15);
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetAllServices", "ServiceScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetAllServices", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }

    public void testGetAllNonLaunchableServices() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetAllNonLaunchableServices", "ServiceScenarioTest"));
            Enumeration nlServices = m_fioranoServiceRepository.getAllNonLaunchableServices();
            while (nlServices.hasMoreElements()) {
                Service sps = (Service) nlServices.nextElement();
                System.out.println("Fetched non-launchable service::" + sps.getDisplayName());
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetAllNonLaunchableServices", "ServiceScenarioTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetAllNonLaunchableServices", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testGetAllVersionsOfService() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetAllVersionsOfService", "ServiceScenarioTest"));
            Enumeration services = m_fioranoServiceRepository.getAllVersionsOfService(m_serviceGUID, false);
            Assert.assertTrue(services.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetAllVersionsOfService", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetAllVersionsOfService", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testSearchService() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSearchService", "ServiceScenarioTest"));
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID, m_version);
            ServiceSearchContext ssc = new ServiceSearchContext();
            ssc.setName(service.getDisplayName());
            Enumeration services = m_fioranoServiceRepository.searchServices(ssc);
            Assert.assertTrue(services.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSearchService", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSearchService", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

     public void testGetServiceInfo() throws Exception {
         System.out.println("Started the Execution of the TestCase::" + getName());

        try {
           DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetServiceInfo", "ServiceScenarioTest"));
           Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID, null);
           Assert.assertTrue(m_serviceGUID.equalsIgnoreCase(service.getGUID()));
           DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetServiceInfo", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetServiceInfo", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }

          try {
            m_fioranoServiceRepository.getServiceInfo(null, null);
            Assert.assertTrue("TestCase Failed - exception expected", false);
        }
        catch (Exception ex) {
        }

         try {
            m_fioranoServiceRepository.getServiceInfo("miskoozi", null);
            Assert.assertTrue("TestCase Failed - exception expected", false);
        }
        catch (Exception ex) {
        }

         try {
            m_fioranoServiceRepository.getServiceInfo("miskoozi", m_version);
            Assert.assertTrue("TestCase Failed - exception expected", false);
        }
        catch (Exception ex) {
        }
    }

    public void testReloadRepository() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testReloadRepository", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_fioranoServiceRepository.reloadRepository();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testReloadRepository", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testReloadRepository", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }



    public void testUpgradeServiceSimple()
    {
        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testUpgradeServiceSimple", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoServiceRepository.copyServiceVersion(m_serviceGUID,m_version,m_serviceGUID,m_newVersion);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testUpgradeServiceSimple", "ServiceScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testUpgradeServiceSimple", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }

    public void testGetAllUncommitedServices() throws Exception
    {
        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetAllUncommitedServices", "ServiceScenarioTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetAllUncommitedServices", "ServiceScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetAllUncommitedServices", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }


    public void testCommitInvalidService() throws Exception
    {
        try{

            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCommitInvalidService", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            float newVersion = Float.parseFloat(m_newVersion) + 2;

            //this testcase should fail
            m_fioranoServiceRepository.commitService(m_serviceGUID, Float.toString(newVersion));

            Assert.assertTrue("TestCase Failed",false);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCommitInvalidService", "ServiceScenarioTest"));

        }
        catch(Exception ex){

            System.err.println("TestCase Failed as it was expected - Continue with testing other cases");
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCommitInvalidService", "ServiceScenarioTest"), ex);
        }

    }


    public void testEditService() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testEditService", "ServiceScenarioTest"));
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID, m_newVersion);
            m_fioranoServiceRepository.editService(m_serviceGUID, m_newVersion, service);
            m_fioranoServiceRepository.saveService(service);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testEditService", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testEditService", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

     public void testSaveService() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSaveService", "ServiceScenarioTest"));
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID, m_newVersion);
            m_fioranoServiceRepository.editService(m_serviceGUID, m_newVersion, service);
            m_fioranoServiceRepository.saveService(service);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSaveService", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSaveService", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

     public void testPublishService() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testPublishService", "ServiceScenarioTest"));
            m_fioranoServiceRepository.publishService(m_serviceGUID, m_newVersion,null,null);
            Assert.assertTrue(m_fioranoServiceRepository.isServiceCommitted(m_serviceGUID, m_version));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testPublishService", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testPublishService", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }



    public void testUpdateIcon16() throws Exception
    {

        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testUpdateIcon16", "ServiceScenarioTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testUpdateIcon16", "ServiceScenarioTest"));

        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testUpdateIcon16", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(), false);

        }

    }

    /**
     * If the Icon File Dosent Exist.
     * @throws Exception
     */
    public void testUpdateIcon16Negitive() throws Exception
    {

        try
        {   DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testUpdateIcon16Negitive", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID,m_newVersion);
            String nonExistingIconFile = "NonExistingFile"+new Date().getTime();
            service.setIcon16(ICON_FILE_NAME);
            m_fioranoServiceRepository.editRegisteredService(m_serviceGUID,m_newVersion,service);
            m_fioranoServiceRepository.updateIcon(m_serviceGUID, m_newVersion, ICON_FILE_NAME, nonExistingIconFile);

            Assert.assertTrue("The Negitive Test Case Failed as there is no Exception in Execution",false);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testUpdateIcon16Negitive", "ServiceScenarioTest"));

        }
        catch(Exception ex)
        {
            System.out.println("The Error Message is:: "+ex.getMessage());
            //todo update the test case after the bug 10420 is fixed
            if(! (ex instanceof TifosiException ||  ex.getMessage().equals("ADDING_RESOURCE_TO_SERVICE_ERROR ::")))
            {
                System.err.println("Exception in the Execution of test case::"+getName());
                ex.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testUpdateIcon16Negitive", "ServiceScenarioTest"), ex);
                Assert.assertTrue("TestCase Failed because of "+ex.getMessage(), false);
            }
        }

    }
    public void testUpdateIcon32() throws Exception{

        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testUpdateIcon32", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            System.out.println("Updating the Service::"+m_serviceGUID+" Version: "+m_newVersion+" with "+ICON32_FILE_NAME);
            m_fioranoServiceRepository.updateIcon32(m_serviceGUID, m_newVersion, ICON32_FILE_NAME, m_iconFile);

            //check whether the Icon is updated or not.

            String largeIcon= "largeIcon_temp.png";
            m_fioranoServiceRepository.fetchResourceFromServiceRepository(m_serviceGUID,m_newVersion,ICON32_FILE_NAME,largeIcon);
            if( !compareBinaryFiles(m_iconFile,largeIcon))
                throw new Exception("The Icon File Updated to the Server dosent match with the file "+m_iconFile);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testUpdateIcon32", "ServiceScenarioTest"));


        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testUpdateIcon32", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(), false);

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
//            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(), false);
//
//        }
//
//    }

     public void testGetServiceIcon32() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetServiceIcon32", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            byte[] b = m_fioranoServiceRepository.getServiceIcon32(m_serviceGUID,m_version);
            Assert.assertNotNull(b);

            byte[] b2 = m_fioranoServiceRepository.getServiceIcon32(m_serviceGUID,null);
            Assert.assertNotNull(b2);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetServiceIcon32", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetServiceIcon32", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testGetServiceIcon16() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetServiceIcon16", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            byte[] b = m_fioranoServiceRepository.getServiceIcon(m_serviceGUID,m_version);
            Assert.assertNotNull(b);

            byte[] b2 = m_fioranoServiceRepository.getServiceIcon(m_serviceGUID,null);
            Assert.assertNotNull(b2);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetServiceIcon16", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetServiceIcon16", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    /**
     * If the Icon File Dosent Exist.
     * @throws Exception
     */
    public void testUpdateIcon32Negitive() throws Exception
    {

        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testUpdateIcon32Negitive", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Service service = m_fioranoServiceRepository.getServiceInfo(m_serviceGUID,m_newVersion);
            String nonExistingIconFile = "NonExistingFile"+new Date().getTime();
            service.setIcon32(ICON_FILE_NAME);
            m_fioranoServiceRepository.editRegisteredService(m_serviceGUID,m_newVersion,service);
            m_fioranoServiceRepository.updateIcon32(m_serviceGUID, m_newVersion, ICON_FILE_NAME, nonExistingIconFile);

            Assert.assertTrue("The Negitive Test Case Failed as there is no Exception in Execution",false);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testUpdateIcon32Negitive", "ServiceScenarioTest"));

        }
        catch(Exception ex)
        {
            System.out.println("The Error Message is:: "+ex.getMessage());

            //todo update the test case after the bug 10420 is fixed
            if(! (ex instanceof TifosiException ||  ex.getMessage().equals("ADDING_RESOURCE_TO_SERVICE_ERROR ::")))
            {
                System.err.println("Exception in the Execution of test case::"+getName());
                ex.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testUpdateIcon32Negitive", "ServiceScenarioTest"), ex);
                Assert.assertTrue("TestCase Failed because of "+ex.getMessage(), false);
            }
        }

    }

    public void testRemoveAllResources() throws Exception
    {
        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveAllResources", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Resource resource = new Resource();
            while(m_resources.hasMoreTokens())
            {
                String resourceName = m_resources.nextToken();
                resource.setName(resourceName);
                System.out.println("Removing the Resource::"+resourceName);
                m_fioranoServiceRepository.removeResource(m_serviceGUID,m_newVersion,resource);
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveAllResources", "ServiceScenarioTest"));


        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveAllResources", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }

    public void testAddResources() throws Exception
    {
        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAddResources", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            while(m_resources.hasMoreTokens())
            {
                String resourceName = m_resources.nextToken();
                Resource resource = new Resource();
                resource.setName(resourceName);
                m_fioranoServiceRepository.addResource(m_serviceGUID,m_newVersion,resource,m_resourceFile);
				System.out.println("TESTCASE");
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAddResources", "ServiceScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAddResources", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }

/*	 public void testDBResources() throws Exception
    {        
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
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }*/

     public void testGetResources() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetResources", "ServiceScenarioTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetResources", "ServiceScenarioTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetResources", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

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
//            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(), false);
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
//            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(), false);
//
//        }
//
//    }

     public void testDeleteIcon() throws Exception
    {

        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteIcon", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            m_fioranoServiceRepository.updateIcon(m_serviceGUID, m_newVersion, null, null);
            m_fioranoServiceRepository.updateIcon32(m_serviceGUID, m_newVersion, null, null);

            byte[] b = m_fioranoServiceRepository.getServiceIcon(m_serviceGUID,m_newVersion);
            Assert.assertTrue(b==null);
            byte[] b32 = m_fioranoServiceRepository.getServiceIcon32(m_serviceGUID,m_newVersion);
            Assert.assertTrue(b32==null);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteIcon", "ServiceScenarioTest"));
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteIcon", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(), false);
        }
    }

    public void testDeleteRegisteredService()throws Exception
    {
        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteRegisteredService", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Enumeration enum_services =m_fioranoServiceRepository.removeService(m_serviceGUID,m_newVersion);
            while(enum_services.hasMoreElements())System.out.println("ELEMNET::"+enum_services.nextElement());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteRegisteredService", "ServiceScenarioTest"));
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteRegisteredService", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }

    public void testDeleteWithInvalidData()throws Exception
    {
        DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteWithInvalidData", "ServiceScenarioTest"));
        System.out.println("Started the Execution of the TestCase::"+getName());

        try
        {
            m_fioranoServiceRepository.removeService("verdigrouz","4.0");
            Assert.assertTrue("TestCase Failed - exception expected",false);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteWithInvalidData", "ServiceScenarioTest"));

        }
        catch(Exception ex)
        {

            System.err.println("TestCase Failed as it was expected - Continue with testing other cases");
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteWithInvalidData", "ServiceScenarioTest"), ex);
        }

        try
        {
            m_fioranoServiceRepository.removeService("verdigrouz","random");
            Assert.assertTrue("TestCase Failed - exception expected",false);
        }
        catch(Exception ex)
        {
        }

        try
        {
            m_fioranoServiceRepository.removeService(null,"4.0");
            Assert.assertTrue("TestCase Failed - exception expected",false);
        }
        catch(Exception ex)
        {
        }

        try
        {
            m_fioranoServiceRepository.removeService(null,null);
            Assert.assertTrue("TestCase Failed - exception expected",false);
        }
        catch(Exception ex)
        {
        }

    }

     public void testDeleteAllVersionsOfService()throws Exception
    {
        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteAllVersionsOfService", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            String testGUID = "TESTSERVICE";
            
            m_fioranoServiceRepository.copyServiceVersion(m_serviceGUID,m_version,testGUID,"4.0");
//            m_fioranoServiceRepository.commitService(testGUID,"4.0");
            m_fioranoServiceRepository.copyServiceVersion(m_serviceGUID,m_version,testGUID,"4.1");
            m_fioranoServiceRepository.publishService(testGUID,"4.1", Deployment.PRODUCT,null);
            m_fioranoServiceRepository.copyServiceVersion(m_serviceGUID,m_version,testGUID,"4.2");
            m_fioranoServiceRepository.commitService(testGUID,"4.2");

            m_fioranoServiceRepository.removeService(testGUID,null);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteAllVersionsOfService", "ServiceScenarioTest"));
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteAllVersionsOfService", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }

    private boolean compareBinaryFiles(String fileA,String fileB) throws IOException
    {
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

     public void testCloseServiceRepository()throws Exception
    {
        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCloseServiceRepository", "ServiceScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoServiceRepository.close();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCloseServiceRepository", "ServiceScenarioTest"));

        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCloseServiceRepository", "ServiceScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }
 
    public static ArrayList getOrder(){

	ArrayList methodsOrder = new ArrayList();

   	methodsOrder.add("testGetAllServices");
	methodsOrder.add("testGetAllNonLaunchableServices");
   	methodsOrder.add("testGetAllVersionsOfService");
   	methodsOrder.add("testSearchService");
   	methodsOrder.add("testGetServiceInfo");
   	methodsOrder.add("testReloadRepository");
   	methodsOrder.add("testUpgradeServiceSimple");
   	methodsOrder.add("testGetAllUncommitedServices");
   	methodsOrder.add("testCommitInvalidService");
   	methodsOrder.add("testEditService");
   	methodsOrder.add("testSaveService");
   	methodsOrder.add("testPublishService");
   	methodsOrder.add("testUpdateIcon16");
   	methodsOrder.add("testUpdateIcon16Negitive");
    	methodsOrder.add("testUpdateIcon32");
    	methodsOrder.add("testGetServiceIcon32");
    	methodsOrder.add("testGetServiceIcon16");
    	methodsOrder.add("testUpdateIcon32Negitive");
    	methodsOrder.add("testRemoveAllResources");
    	methodsOrder.add("testAddResources");
    	methodsOrder.add("testGetResources");
    	methodsOrder.add("testDeleteIcon");
   	methodsOrder.add("testDeleteRegisteredService");
    	methodsOrder.add("testDeleteWithInvalidData");
    	methodsOrder.add("testDeleteAllVersionsOfService");
    	methodsOrder.add("testCloseServiceRepository");
		

	return methodsOrder;
    }
}
