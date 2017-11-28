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

import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import fiorano.tifosi.dmi.service.Resource;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: amit
 * Date: Aug 4, 2008
 * Time: 4:46:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddResourcesTest extends DRTTestCase {

    private FioranoServiceRepository m_fioranoServiceRepository;
    private String resourceFilePath;
    private String m_DBResource;

    public AddResourcesTest(String name){
        super(name);
    }

    public AddResourcesTest(TestCaseElement testCaseConfig){
        super(testCaseConfig);
    }

    public void init() throws Exception{

        resourceFilePath = testCaseConfig.getTestBaseDir()+File.separator+"scenario"+File.separator+"AddResources";
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
        m_DBResource = resourceFilePath+File.separator+testCaseProperties.getProperty("DBResource");
        printInitParams();      

    }

    protected void printInitParams(){
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("The Resource File Path:: "+m_DBResource);
        System.out.println("...........................................................................");
    }

    public void setUp() throws Exception{
        super.setUp();
        init();
    }

    public static Test suite(){
        return new TestSuite(AddResourcesTest.class);
    }

    public void testDBResources() throws Exception{
        try{
            System.out.println("Started the Execution of the TestCase::"+getName());
            String resourceName = "com.ibm.mq.jar";
            Resource resource = new Resource();
            resource.setName(resourceName);
            m_fioranoServiceRepository.addResource("MQSeriesIn", "4.0", resource, m_DBResource);

        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(), false);

        }
    }
}
