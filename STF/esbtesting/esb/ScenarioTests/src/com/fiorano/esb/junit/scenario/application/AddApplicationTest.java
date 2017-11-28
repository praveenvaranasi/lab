package com.fiorano.esb.junit.scenario.application;

import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.esb.rtl.application.FioranoApplicationController;

import java.io.File;
import java.util.ArrayList;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;

/**
 * Created by IntelliJ IDEA.
 * User: root
 * Date: Mar 3, 2008
 * Time: 3:32:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddApplicationTest extends DRTTestCase {

    private String resourceFilePath;
    private boolean m_initialised;
    private String m_routeGUID;
    private String m_appGUID;
    private float m_version;
    private float m_newVersionNumber;
    private String m_appFile;
    private FioranoApplicationController m_fioranoApplicationController;

    public AddApplicationTest(String name) {
        super(name);
    }

    public AddApplicationTest(TestCaseElement testCaseConfig){
        super(testCaseConfig);
    }


    public void init() throws Exception {

        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));


        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile");
        printInitParams();

    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("Application GUID:: " + m_appGUID + "  Version Number::" + m_version);


        System.out.println("Application File" + m_appFile);
        System.out.println("...........................................................................");
    }

    public void setUp() throws Exception {
        super.setUp();
        if (!m_initialised) {
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Application"+File.separator+"CRC";
            init();
            m_initialised = true;
        }
    }
    public void tearDown() throws Exception
            {
                super.tearDown();
            }
    public void testImportApplication()
            {
                try
                {
                    System.out.println("Started the Execution of the TestCase::"+getName());
                    Application application = ApplicationParser.readApplication(new File(m_appFile));
                    m_fioranoApplicationController.saveApplication(application);
                    if(m_fioranoApplicationController.getApplication(m_appGUID,m_version)==null)//remember//
                     {
                     System.out.println("i am here");
                     throw new Exception("Application with GUID::"+m_appGUID+", version::"+m_version+" not created");
                      }
                }
                catch(Exception ex)
                {
                    System.out.println("Exception in the Execution of test case::"+getName());
                    System.err.println("Exception in the Execution of test case::"+getName());
                    ex.printStackTrace();
                    Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

                }
            }

            /**
             * Runs the application with transforamtion set.
             */


    public void testCRC(){
        try{
          //System.out.println("Started the Execution of the TestCase::"+getName());
                //                     an Exception if the application is not present.
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
                    Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

                }
}

    public void testRunApplication()
                {
                    try
                    {
                        System.out.println("Started the Execution of the TestCase::"+getName());
                        startApplication(m_appGUID,m_version);
                        if(!m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version))
                            throw new Exception("The Application is not started.");

                    }
                    catch(Exception ex)
                    {
                        System.err.println("Exception in the Execution of test case::"+getName());
                        ex.printStackTrace();
                        Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

                    }
                }


    public void testAddApplication(){
        try{
          System.out.println("Started the Execution of the TestCase::"+getName());
          Application application = ApplicationParser.readApplication(new File(m_appFile));
           

        }
        catch(Exception ex)
                    {
                        System.err.println("Exception in the Execution of test case::"+getName());
                        ex.printStackTrace();
                        Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

                    }
    }




        public void testKillApplication()
            {
                try
                {
                    System.out.println("Started the Execution of the TestCase::"+getName());
                    stopApplication(m_appGUID);
                    if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                        throw new Exception("Applciation not killed");

                }
                catch(Exception ex)
                {
                    System.err.println("Exception in the Execution of test case::"+getName());
                    ex.printStackTrace();
                    Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

                }
            }

            public void testDeleteApplication()
            {
                try
                {
                    System.out.println("Started the Execution of the TestCase::"+getName());
                    stopApplication(m_appGUID);
                    if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                        throw new Exception("Applciation not killed");

                    m_fioranoApplicationController.deleteApplication(m_appGUID,m_version);

                }
                catch(Exception ex)
                {
                    System.err.println("Exception in the Execution of test case::"+getName());
                    ex.printStackTrace();
                    Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

                }
            }

            private void startApplication(String appGuid, float appVersion) throws FioranoException
            {
                try
                {
                    m_fioranoApplicationController.prepareLaunch(appGuid,appVersion);
                    m_fioranoApplicationController.launchApplication(appGuid,appVersion);
                    m_fioranoApplicationController.startAllServices(appGuid,appVersion);
                }catch(TifosiException e){
                    if(e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::")!=-1 && e.getMessage().indexOf("version of this application is already running")!=-1)
                    {
                        //never mind if the application is already running.
                        return;
                    }
                    System.err.println("Error launching the application");
                    e.printStackTrace();
                    throw new FioranoException("Error launching the application",e);
                }
            }
            private void stopApplication(String appGUID)
            {
                try
                {
                    m_fioranoApplicationController.stopAllServices(appGUID,m_version);
                    m_fioranoApplicationController.killApplication(appGUID,m_version);
                } catch(TifosiException e){
                    //ignore.
                }
            }

              public static Test suite()
          {
                return new TestSuite(AddApplicationTest.class);
          }

            public static ArrayList getOrder(){

            ArrayList methodsOrder = new ArrayList();

            methodsOrder.add("testImportApplication");

            //methodsOrder.add("testChangeTransformationRuntime");
            methodsOrder.add("testCRC");
                 methodsOrder.add("testRunApplication");
            methodsOrder.add("testKillApplication");
            methodsOrder.add("testDeleteApplication");

            return methodsOrder;
            }

}