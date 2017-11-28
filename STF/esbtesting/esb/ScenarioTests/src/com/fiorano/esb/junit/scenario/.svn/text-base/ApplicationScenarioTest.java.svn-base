/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 * Copyright (c) 2005, Fiorano Software, Inc.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.esb.junit.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.xml.ClarkName;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationContext;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.service.Schema;
import fiorano.tifosi.dmi.service.Execution;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.events.EventStateConstants;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;
import java.io.*;
import java.util.ArrayList;                  
import java.util.Date;
import java.util.logging.Level;

/**
 * @author Sandeep M
 * @Date: 24th Dec 2006
 */
public class ApplicationScenarioTest extends DRTTestCase{

    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised =false;
    private String m_appGUID;
    private float m_version;
    private String m_applicationContextFile;
    private String m_routeElement;
    private String resourceFilePath;
    private String m_instance;
    private String m_routeElementNamespace;

    String m_appGUID2;
    String m_appFile2;
    float m_version2;
    String m_serviceInstance2;

     static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter
    //private static ModifyXML m_modifyFiles;
    

    public ApplicationScenarioTest(String name){
        super(name);
    }

    public ApplicationScenarioTest(TestCaseElement testCaseConfig){
        super(testCaseConfig);
    }

    private void setXmlFilter(String ext)//picking up the xml extension files
           {
               xmlFilter=new OnlyExt(ext);
           }


    // the function modifies the xml files and replace any "search" string with "replace" string
    void modifyXmlFiles(String path,String search,String replace) throws IOException{
            File fl=new File(path);
            File[]filearr=fl.listFiles(xmlFilter);
            FileReader fr=null;
            FileWriter fw=null;
           boolean change=false;
            BufferedReader br;
            String s;
            String result="";


            int i=0;
            while(i<filearr.length-1)
            {
                try{
                    fr=new FileReader(filearr[i]) ;
                } catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                try{
                    fw=new FileWriter("temp.xml");
                } catch(IOException e){
                    e.printStackTrace();
                }
                br=null;
                br=new BufferedReader(fr);


             while((s=br.readLine())!=null)
             {

                 int j=s.indexOf(search);
                 if(j!=-1)
                 {
                     change=true;
                     int k=search.length()+j;
                     //System.out.println("The Index start is "+j+" and index last is "+ k);
                     result=s.substring(0,j);
                     result=result+replace;
                     result=result+ s.substring(k);
                     s=result;

                  }
                    //System.out.print(s);

                    fw.write(s);
                   fw.write('\n');
            }
                fr.close();
                fw.close();

                if(change)
                {
                    fr=new FileReader("temp.xml");
                    fw=new FileWriter(filearr[i]);
                    br=new BufferedReader(fr);
                    while((s=br.readLine())!=null)
                    {
                        fw.write(s);
                        fw.write('\n');
                    }
                    fr.close();
                    fw.close();
                    change=false;
                }

                i=i+1;

            }
        }
        //changed function
    

    public void setUp() throws Exception{
        super.setUp();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "ApplicationScenario";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();

        ServerStatusController stc;
        stc=ServerStatusController .getInstance();
        boolean isFPSHA=stc.getFPSHA();

                   setXmlFilter("xml");
                if(isFPSHA && !isModifiedOnceHA)
               {
                   isModifiedOnceHA=true;

                   modifyXmlFiles(resourceFilePath,"fps_test","fps_ha");

               }
               else if(!isFPSHA && !isModifiedOnce)
               {
                   isModifiedOnce=true;
                   modifyXmlFiles(resourceFilePath,"fps_ha","fps_test");

               }
        
        init();
    }

    private void init() throws Exception{
        if(m_initialised)
            return;
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_applicationContextFile = resourceFilePath+File.separator+testCaseProperties.getProperty("ApplicationContext");
        m_routeElement = testCaseProperties.getProperty("RouteElement");
        m_routeElementNamespace  = testCaseProperties.getProperty("RouteElementNamespace");
        m_instance = resourceFilePath+File.separator+testCaseProperties.getProperty("ApplicationContextValue");

        m_appGUID2 = testCaseProperties.getProperty("ApplicationGUID2");
        m_appFile2 = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile2");
        m_version2 = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion2"));
        m_serviceInstance2 = testCaseProperties.getProperty("ServiceToBeChanged");

        printInitParams();
        m_initialised = true;
    }

    private void printInitParams(){
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       "+m_appGUID+"  Version Number::"+m_version);
        System.out.println("_____________________________________________________________________________");
   }

    public static Test suite(){
        return new TestSuite(ApplicationScenarioTest.class);
    }

    public void testCreateApplication() {
        try{
            System.out.println("Started the Execution of the TestCase::"+getName());
            Application application = new Application();//m_fioranoApplicationController.getApplication(BASE_APPLICATION,1.0f);
            application.setServiceInstances(new ArrayList());
            application.setRoutes(new ArrayList());
            application.setGUID(m_appGUID);
            application.setDisplayName(m_appGUID);
            application.setVersion(m_version);
            ArrayList al = new ArrayList();
            al.add("CoreScenarioTests");
            application.setCategories(al);
            application.setCreationDate(new Date());
            ApplicationContext appContxt = getAppContext();
            setDefaultInstance(appContxt);
            application.setApplicationContext(appContxt);
            m_fioranoApplicationController.saveApplication(application);
   //         System.out.println("Creating the application with version no::"+m_fioranoApplicationController.createApplication(application,m_version));
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);
        }
    }

    public void testCheckApplicationContextValues(){
        try{
            System.out.println("Started the Execution of the TestCase::"+getName());
            System.out.println("The Application Is::\n"+m_fioranoApplicationController.getApplication(m_appGUID,m_version));
            System.out.println("AND The APP Context is \n"+m_fioranoApplicationController.getApplication(m_appGUID,m_version).getApplicationContext().getContent());
            System.out.println("AND The APP Context Default instance  is  \n"+m_fioranoApplicationController.getApplication(m_appGUID,m_version).getApplicationContext().getValue());
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);
        }
    }

    private ApplicationContext getAppContext() {
        ApplicationContext appContext = new ApplicationContext();
        String structure;
        StringBuffer buffer = new StringBuffer("");
        FileReader reader=null;
        BufferedReader bufferedReader=null;
        try{
            reader = new FileReader(m_applicationContextFile);
            bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            structure = "";
            while(line!=null){
                buffer.append(line);
                line = bufferedReader.readLine();
            }
        } catch(Exception e){
            e.printStackTrace();
            System.err.println("Unable To Read the message from the file. Sending the default message::testMessage");
            structure = "testMessage";
        }
        finally{
            try{
                bufferedReader.close();
                reader.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        structure = buffer.toString();
        appContext.setContent(structure);
        appContext.setRootElement(ClarkName.toClarkName(m_routeElementNamespace, m_routeElement));
        appContext.setType(Schema.TYPE_XSD);
        return appContext;
    }

    private void setDefaultInstance(ApplicationContext appContext) {
        StringBuffer appContextValue = new StringBuffer("");
        FileReader valueReader=null;
        BufferedReader valueBufferedReader=null;
        try{
            valueReader = new FileReader(m_instance);
            valueBufferedReader = new BufferedReader(valueReader);
            String value = valueBufferedReader.readLine();
            while(value!=null){
                appContextValue.append(value);
                value = valueBufferedReader.readLine();
            }
        } catch(IOException e){
            e.printStackTrace();
        }
        finally{
            try{
                valueBufferedReader.close();
                valueReader.close();
            } catch(IOException e){
                e.printStackTrace();
            }
        }
        System.out.println("The Default Instance being set is::"+appContextValue.toString());
        appContext.setValue(appContextValue.toString());
    }

    public void testDeleteApplication(){
        try{
            System.out.println("Started the Execution of the TestCase::"+getName());
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID,m_version);
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);
        }
    }

    /* written for the sake of bug 11788 STARTS */
    public void testChangeLaunchType(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testChangeLaunchType", "ApplicationScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            /* create the application with a file reader and a display */
            Application application = ApplicationParser.readApplication(new File(m_appFile2));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID2, m_version2) == null)
                throw new Exception("Application with GUID::" + m_appGUID2 + ", version::" + m_version2 + " not created");

            /* launch the application */
            startApplication(m_appGUID2,m_version2);

            /* kill the service and change the launch type */
            application = m_fioranoApplicationController.getApplication(m_appGUID2, m_version2);
            m_fioranoApplicationController.killService(m_appGUID2,m_version,m_serviceInstance2);
            application.getServiceInstance(m_serviceInstance2).setLaunchType(Execution.LAUNCH_TYPE_IN_MEMORY);
            m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID2,m_version);
            m_fioranoApplicationController.startService(m_appGUID2,m_version, m_serviceInstance2);

            /* kill the service and changed the launch type */
            application = m_fioranoApplicationController.getApplication(m_appGUID2, m_version2);
            m_fioranoApplicationController.killService(m_appGUID2,m_version,m_serviceInstance2);
            application.getServiceInstance(m_serviceInstance2).setLaunchType(Execution.LAUNCH_TYPE_SEPARATE_PROCESS);
            m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID2,m_version);
            m_fioranoApplicationController.startService(m_appGUID2,m_version, m_serviceInstance2);

            /* kill the application */
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID2))
                m_fioranoApplicationController.killApplication(m_appGUID2,m_version);
            /*delete the application in next test */
            //m_fioranoApplicationController.deleteApplication(m_appGUID2,m_version2);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testChangeLaunchType", "ApplicationScenarioTest"));
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testChangeLaunchType", "ApplicationScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

            /*kill the application if it wasn't killed previously due to some exceptions */
            try{
                if(m_fioranoApplicationController.isApplicationRunning(m_appGUID2))
                    m_fioranoApplicationController.killApplication(m_appGUID2,m_version);
                m_fioranoApplicationController.deleteApplication(m_appGUID2,m_version2);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    private void startApplication(String appGuid, float appVersion) throws FioranoException{
        try {
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
            throw new FioranoException("Error launching the application");
        }
    }
    /* written for the sake of bug 11788 ENDS */

    /* written for the sake of bug 12397 STARTS */
    public void testLaunchTypeNone(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testLaunchTypeNone", "ApplicationScenarioTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            /* application was already created in the previous test */
            /* change the application's launch type to none */
            Application application = m_fioranoApplicationController.getApplication(m_appGUID2, m_version2);
            application.getServiceInstance(m_serviceInstance2).setLaunchType(Execution.LAUNCH_TYPE_NONE);

            /* launch the application and check whether the component in running. if yes, raise exception so that the testcase fails */
            startApplication(m_appGUID2, m_version2);
            String componentStatus = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID2,m_version,m_serviceInstance2).getStatusString();
            Assert.assertNotNull(componentStatus);
            Assert.assertFalse(componentStatus.equals(EventStateConstants.SERVICE_HANDLE_BOUND));

            /*kill and delete the application */
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID2))
                m_fioranoApplicationController.killApplication(m_appGUID2,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID2,m_version2);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testLaunchTypeNone", "ApplicationScenarioTest"));
        }
        catch(Exception ex){
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testLaunchTypeNone", "ApplicationScenarioTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

            /*kill and delete the application if it wasn't deleted previously due to some exceptions */
            try{
                if(m_fioranoApplicationController.isApplicationRunning(m_appGUID2))
                    m_fioranoApplicationController.killApplication(m_appGUID2,m_version);
                m_fioranoApplicationController.deleteApplication(m_appGUID2,m_version2);
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    /* written for the sake of bug 12397 ENDS */


    public static ArrayList getOrder(){
	
	ArrayList methodsOrder = new ArrayList();

	//methodsOrder.add("testCreateApplication");
	//methodsOrder.add("testCheckApplicationContextValues");
	//methodsOrder.add("testDeleteApplication");
    methodsOrder.add("testChangeLaunchType");
    methodsOrder.add("testLaunchTypeNone");
        
    return methodsOrder;
    }	
}

