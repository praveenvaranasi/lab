package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import fiorano.tifosi.dmi.service.Resource;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/18/11
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestAddResources extends AbstractTestNG{

    private FioranoServiceRepository m_fioranoServiceRepository;
    private String resourceFilePath;
    private String m_DBResource;

    public void init() throws Exception {
        m_fioranoServiceRepository = rtlClient.getFioranoServiceRepository();
        m_DBResource = resourceFilePath+fsc+testProperties.getProperty("DBResource");
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("The Resource File Path:: "+m_DBResource);
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "AddResourcesTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "AddResources" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc +"tests" +  fsc + "scenario" + fsc + "AddResources";
            try {
                init();
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        printInitParams();
    }

    @Test(groups = "AddResourcesTest", alwaysRun = true, description = "trying to import an application.")
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
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }
}
