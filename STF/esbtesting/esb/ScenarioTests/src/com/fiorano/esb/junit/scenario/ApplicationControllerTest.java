package com.fiorano.esb.junit.scenario;

import com.fiorano.esb.application.controller.ApplicationControllerErrorCodes;
import com.fiorano.esb.rtl.TifosiConstants;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.TestCaseElement;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.*;
import fiorano.tifosi.dmi.aps.ApplicationStateDetails;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: Prasanth
 * Date: Feb 22, 2007
 * Time: 5:05:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationControllerTest extends DRTTestCase {

    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String resourceFilePath;
    private String m_startInstance;
    private String m_endInstance;

    boolean isModifiedOnceHA=false;//to check the files overwriting
    boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter
    //private static ModifyXML m_modifyFiles;

    private float m_version2;
    private String m_appFile2;
    private String m_appFile3;
    //ModifyXML m_modifyFiles;//new change





    public ApplicationControllerTest(String name) {
        super(name);
    }

     public ApplicationControllerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }


    private void setXmlFilter(String ext)//picking up the xml extension files
       {
           xmlFilter=new OnlyExt(ext);//creating filters

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

    public void setUp() throws Exception {
        super.setUp();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "ApplicationController";

        //new change
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

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationFile", "1.0.xml");
        m_version2 = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion2"));
        m_appFile2 = resourceFilePath+ File.separator + testCaseProperties.getProperty("ApplicationFile2", "2.0.xml");
        m_appFile3 = resourceFilePath+ File.separator + testCaseProperties.getProperty("ApplicationFile3", "3.0.xml");
        m_startInstance = testCaseProperties.getProperty("WorkFlowStartInstance");
        m_endInstance = testCaseProperties.getProperty("WorkFlowEndInstance");
        printInitParams();
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    public static Test suite() {
        return new TestSuite(ApplicationControllerTest.class);
    }

       public void testCreateApplication(){
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("The App File is::" + m_appFile);
            try{
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCreateApplication", "ApplicationControllerTest"));
                Application application = ApplicationParser.readApplication(new File(m_appFile));
                m_fioranoApplicationController.saveApplication(application);

                if(m_fioranoApplicationController.getApplication(m_appGUID,m_version) == null)
		{
                    throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not creatnnections at tsp_tcp://0.0.0.0:19ed");
		}
		m_fioranoApplicationController.prepareLaunch(m_appGUID,m_version);
		m_fioranoApplicationController.launchApplication(m_appGUID,m_version);
		m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
		Thread.sleep(10000);
                Assert.assertTrue(m_appGUID+" could not be started", m_fioranoApplicationController.isApplicationRunning(m_appGUID));
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCreateApplication", "ApplicationControllerTest"));
            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCreateApplication", "ApplicationControllerTest"), e);
                   Assert.assertTrue("TestCase Failed because of "+e.getMessage(),false);
            }
        }

        private void startApplication(String appGUID, float version){
            try{
		System.out.println("IN start application");
                m_fioranoApplicationController.prepareLaunch(appGUID,version);
System.out.println("IN start application after prep");
                m_fioranoApplicationController.launchApplication(appGUID,version);
System.out.println("IN start application after launch");
                m_fioranoApplicationController.startAllServices(appGUID,version);
		System.out.println("IN start application2");
                /* wait for 20 sec so that the application gets started */
                Thread.sleep(10000);
		System.out.println("IN start application3");

            }
            catch (Exception e) {
                if (e.getMessage().indexOf("ERROR_APPLICATION_LAUNCH_ERROR ::") != -1 && e.getMessage().indexOf("version of this application is already running") != -1)
		e.printStackTrace();
                    //never mind if the application is already running.
                    return;
            }
        }


        public void testGetAllApplicationRoutes(){
            try{
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetAllApplicationRoutes", "ApplicationControllerTest"));
                System.out.println("Started the Execution of the TestCase::" + getName());


//            List list = (List)(jmxClient.invoke(objName, "getRoutesOfApplication", params, signatures));
                List list=m_fioranoApplicationController.getRoutesOfApplication(m_appGUID,m_version);
                Assert.assertNotNull(list);
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetAllApplicationRoutes", "ApplicationControllerTest"));

            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetAllApplicationRoutes", "ApplicationControllerTest"), e);
                   Assert.assertTrue("TestCase Failed because of "+e.getMessage(),false);
            }
        }

        public void  testModifyRoutes(){
            {
            //deprecated method
            }
        }

        public void  testGetDeliverableMessageCount(){
            try{
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetDeliverableMessageCount", "ApplicationControllerTest"));
                System.out.println("xxxStarted the Execution of the TestCase::" + getName());
                Hashtable pendingMsgs=new Hashtable();
                pendingMsgs=m_fioranoApplicationController.getDeliverableMessageCount(m_appGUID,m_version);
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetDeliverableMessageCount", "ApplicationControllerTest"));
            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetDeliverableMessageCount", "ApplicationControllerTest"), e);
                   Assert.assertTrue("TestCase Failed because of "+e.getMessage(),false);
            }
        }


    public void testGetApplicationHeaders() {
            try {
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetApplicationHeaders", "ApplicationControllerTest"));
                ApplicationReference AR1;
                System.out.println("Started the Execution of the TestCase::"+getName());
                ApplicationReference[] refArray1 = m_fioranoApplicationController.getApplicationHeaders(m_appGUID);
                ApplicationStateDetails ASD;
                System.out.println("Finished fetching applicationheaders");
                Enumeration en1 = m_fioranoApplicationController.getHeadersOfRunningApplications();
                while(en1.hasMoreElements())
                {
                    AR1= (ApplicationReference)en1.nextElement();
                    Assert.assertTrue(m_fioranoApplicationController.isApplicationRunning(AR1.getGUID()));

                }
                Enumeration en2 = m_fioranoApplicationController.getHeadersOfSavedApplications();
                while(en2.hasMoreElements())
                {
                    AR1= (ApplicationReference)en2.nextElement();
                  m_fioranoApplicationController.getApplication(AR1.getGUID(),AR1.getVersion());      //if its present, it must be gettable, insome pieces at least
                }
                System.out.println("Finished fetching saved applicationheaders");
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetApplicationHeaders", "ApplicationControllerTest"));
            }
            catch (Exception ex) {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetApplicationHeaders", "ApplicationControllerTest"), ex);
                Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
            }
        }

         public void testGetApplicatonList(){
            try{
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetApplicatonList", "ApplicationControllerTest"));
                System.out.println("Started the Execution of the TestCase::" + getName());
                String appName;
                Enumeration en1 =m_fioranoApplicationController.getListOfRunningApplications();
                while(en1.hasMoreElements())
                {
                    appName=en1.nextElement().toString();
                    Assert.assertTrue(m_fioranoApplicationController.isApplicationRunning(appName));

                }
                System.out.println("Finished fetching list of running application");
                Enumeration en2 =m_fioranoApplicationController.getListOfSavedApplications();
                while(en2.hasMoreElements())
                {
                    appName=en2.nextElement().toString();
                    m_fioranoApplicationController.getApplication(appName,1.0f);
                }
                System.out.println("Finished fetching list of saved application");
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetApplicatonList", "ApplicationControllerTest"));
            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetApplicatonList", "ApplicationControllerTest"), e);
                   Assert.assertTrue("TestCase Failed because of "+e.getMessage(),false);
            }
        }

        public void testGetApplicatonStateDetails(){
            try{
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetApplicatonStateDetails", "ApplicationControllerTest"));
                System.out.println("Started the Execution of the TestCase::" + getName());

                Object[] params = {m_appGUID};
                String[] signatures = {String.class.getName()};

                ApplicationStateDetails asd1 =(ApplicationStateDetails)m_fioranoApplicationController.getCurrentStateOfApplication(m_appGUID,m_version);

                    Assert.assertEquals(m_appGUID,asd1.getAppGUID());
                    Enumeration list=asd1.getAllServiceNames();
                    ApplicationStateDetails asd2 =m_fioranoApplicationController.getCurrentStateOfApplicationFromFPSs(m_appGUID,m_version);
                    Assert.assertTrue(asd2.getAppGUID().equalsIgnoreCase(m_appGUID));
                    Enumeration list2=asd2.getAllServiceNames();
                    ArrayList<String> aList=new ArrayList<String>();
                     while(list.hasMoreElements())
                    {
                        aList.add((String)list.nextElement());
                    }
                    while(list2.hasMoreElements())
                    {
                        Assert.assertTrue(aList.contains(list2.nextElement()));
                    }
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetApplicatonStateDetails", "ApplicationControllerTest"));

           }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetApplicatonStateDetails", "ApplicationControllerTest"), e);
                   Assert.assertTrue("TestCase Failed because of "+e.getMessage(),false);
            }
        }
//
            public void testGetServiceInstanceStateDetails(){
            try{
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetServiceInstanceStateDetails", "ApplicationControllerTest"));
                System.out.println("Started the Execution of the TestCase::"+getName());

                Application application = ApplicationParser.readApplication(new File(m_appFile));
                ServiceInstance startService = application.getServiceInstance(m_startInstance);
                String srvName = startService.getName();

                ServiceInstanceStateDetails sisd1 = m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version,m_startInstance);
                Assert.assertTrue(sisd1.getServiceInstanceName().equalsIgnoreCase(srvName));
                System.out.println("Finished fetching current state of service");
                ServiceInstanceStateDetails sisd2 = m_fioranoApplicationController.getCurrentStateOfServiceFromFPSs(m_appGUID,m_version,m_startInstance);
                Assert.assertTrue(sisd2.getServiceInstanceName().equalsIgnoreCase(srvName));
                System.out.println("Finished fetching current state of service from fps");
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetServiceInstanceStateDetails", "ApplicationControllerTest"));
            }
            catch (Exception ex) {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetServiceInstanceStateDetails", "ApplicationControllerTest"), ex);
                Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
            }
        }

     public void testGetApplicatonRoutes() {
        // This application is created form the SIMPLECHAT Application, and then setting the Workflow on ports. etc.
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetApplicatonRoutes", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            List list = m_fioranoApplicationController.getRoutesOfApplication(m_appGUID,m_version);
            Assert.assertNotNull(list);
            Assert.assertTrue(list.size()>0);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetApplicatonRoutes", "ApplicationControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetApplicatonRoutes", "ApplicationControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

   public void testSyncAppWithRunningVersion() {
           try {
               DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSyncAppWithRunningVersion", "ApplicationControllerTest"));
               System.out.println("Started the Execution of the TestCase::" + getName());
               m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID,m_version);
               DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSyncAppWithRunningVersion", "ApplicationControllerTest"));
           }
           catch (Exception ex) {
               System.err.println("Exception in the Execution of test case::" + getName());
               ex.printStackTrace();
               DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSyncAppWithRunningVersion", "ApplicationControllerTest"), ex);
               Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
           }
       }



    public void testGetApplicationStateDetails() {
           try {
               DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetApplicationStateDetails", "ApplicationControllerTest"));
               System.out.println("Started the Execution of the TestCase::"+getName());
               ApplicationStateDetails asd1 = m_fioranoApplicationController.getCurrentStateOfApplication(m_appGUID,m_version);
               Assert.assertTrue(asd1.getAppGUID().equalsIgnoreCase(m_appGUID));
               System.out.println("Finished fetching current state of application");
               ApplicationStateDetails asd2 = m_fioranoApplicationController.getCurrentStateOfApplicationFromFPSs(m_appGUID,m_version);
               Assert.assertTrue(asd2.getAppGUID().equalsIgnoreCase(m_appGUID));
               System.out.println("Finished fetching current state of application from fps");
               DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetApplicationStateDetails", "ApplicationControllerTest"));

           }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetApplicationStateDetails", "ApplicationControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }



    public void testSetBreakPointConfig() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetBreakPointConfig", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Hashtable confTable = new Hashtable();
            confTable.put(TifosiConstants.DEBUGGER_BUFFER_SIZE, new Long(102400));
            confTable.put(TifosiConstants.DEBUGGER_WAIT_TIME, new Long(60000));
            m_fioranoApplicationController.setBreakPointConfig(confTable);
            Properties props = new Properties();
            props.put(TifosiConstants.DEBUGGER_BUFFER_SIZE, new Long(102401));
            props.put(TifosiConstants.DEBUGGER_WAIT_TIME, new Long(60001));
            m_fioranoApplicationController.setBreakPointConfig(props);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetBreakPointConfig", "ApplicationControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetBreakPointConfig", "ApplicationControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testClearApplicationLogs(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearApplicationLogs", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.clearApplicationLogs(m_appGUID);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearApplicationLogs", "ApplicationControllerTest"));

        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.APS_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearApplicationLogs", "ApplicationControllerTest"), e);
                Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            }

        }
    }

    public void testGetLogLevel(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetLogLevel", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String[] modules={"ERR_HANDLER","OUT_HANDLER"};
            Hashtable logLevels = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version,m_startInstance,modules);
            Assert.assertNotNull(logLevels);
            Assert.assertTrue(logLevels.size()>0);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetLogLevel", "ApplicationControllerTest"));

        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetLogLevel", "ApplicationControllerTest"), e);
                Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            }
        }
    }


    public void testSetLogLevel(){
        try{

            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetLogLevel", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Hashtable modules = new Hashtable();
            modules.put("OUT_HANDLER","ALL");
            modules.put("ERR_HANDLER","ALL");
            m_fioranoApplicationController.setLogLevel(m_appGUID,m_version,m_startInstance,modules);
            String outLogLevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version,m_startInstance,"OUT_HANDLER");
            String errLogLevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version,m_startInstance,"ERR_HANDLER");
            Assert.assertTrue(outLogLevel.equalsIgnoreCase("ALL") && errLogLevel.equalsIgnoreCase("ALL"));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetLogLevel", "ApplicationControllerTest"));

        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetLogLevel", "ApplicationControllerTest"), e);
                Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            }
        }
    }

    public void testGetServiceStateDetails(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetServiceStateDetails", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            ServiceInstanceStateDetails details = m_fioranoApplicationController.getCurrentStateOfServiceFromFPSs(m_appGUID,m_version,m_startInstance);
            Assert.assertNotNull(details);
            Assert.assertTrue(details.getServiceInstanceName().equalsIgnoreCase(m_startInstance) && details.getServiceNodeName().equalsIgnoreCase("fps"));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetServiceStateDetails", "ApplicationControllerTest"));

        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetServiceStateDetails", "ApplicationControllerTest"), e);
                Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            }
        }
    }

    public void testClearServiceErrLogs(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearServiceErrLogs", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.clearServiceErrLogs(m_startInstance,m_appGUID);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearServiceErrLogs", "ApplicationControllerTest"));
        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearServiceErrLogs", "ApplicationControllerTest"), e);
                Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            }
        }
    }


    public void testClearServiceOutLogs(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearServiceOutLogs", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.clearServiceOutLogs(m_startInstance,m_appGUID);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearServiceOutLogs", "ApplicationControllerTest"));
        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearServiceOutLogs", "ApplicationControllerTest"), e);
                Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            }
        }
    }

    public void testGetLastOutTrace(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetLastOutTrace", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.getLastOutTrace(50,m_startInstance,m_appGUID);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetLastOutTrace", "ApplicationControllerTest"));
        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetLastOutTrace", "ApplicationControllerTest"), e);
                Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            }
        }
    }

    public void testGetLastErrTrace(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetLastErrTrace", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.getLastErrorTrace(50,m_startInstance,m_appGUID);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetLastErrTrace", "ApplicationControllerTest"));
        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetLastErrTrace", "ApplicationControllerTest"), e);
                Assert.assertTrue("TestCase Failed because of "+e.getMessage(), false);
            }
        }
    }

      public void testStopAllServices(){
            try{
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testStopAllServices", "ApplicationControllerTest"));
                System.out.println("Started the Execution of the TestCase::" + getName());
                  if(!(((m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version,m_startInstance))).getKillTime() < new Date().getTime()))
                    throw new Exception("Stop All Services Failed");
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testStopAllServices", "ApplicationControllerTest"));
            }catch(Exception ex){
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testStopAllServices", "ApplicationControllerTest"), ex);
                Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
            }
        }



    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "ApplicationControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "ApplicationControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }



    public void testCleanup() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCleanup", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);

            m_fioranoApplicationController.close();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCleanup", "ApplicationControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCleanup", "ApplicationControllerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }
     public static ArrayList getOrder() {
	System.out.println("Inside getOrder");
        ArrayList methodsOrder = new ArrayList();
         methodsOrder.add("testCreateApplication");
         methodsOrder.add("testGetAllApplicationRoutes");
//         methodsOrder.add("testModifyRoutes");                //deprecated method
         methodsOrder.add("testGetDeliverableMessageCount");
         methodsOrder.add("testGetApplicationHeaders");
         methodsOrder.add("testGetApplicatonList");
         methodsOrder.add("testGetApplicatonStateDetails");
         methodsOrder.add("testGetServiceInstanceStateDetails");
         methodsOrder.add("testGetApplicatonRoutes");
         methodsOrder.add("testSyncAppWithRunningVersion");
         methodsOrder.add("testGetApplicationStateDetails");
         methodsOrder.add("testSetBreakPointConfig");
         methodsOrder.add("testClearApplicationLogs");
         methodsOrder.add("testGetLogLevel");
         methodsOrder.add("testSetLogLevel");
         methodsOrder.add("testGetServiceStateDetails");
         methodsOrder.add("testClearServiceErrLogs");
         methodsOrder.add("testClearServiceOutLogs");
         methodsOrder.add("testGetLastOutTrace");
         methodsOrder.add("testGetLastErrTrace");
         methodsOrder.add("testStopAllServices");
         methodsOrder.add("testDeleteApplication");
         methodsOrder.add("testCleanup");
          return methodsOrder;
    }



}

class OnlyExt implements FilenameFilter{//extract the files of given extension

     String ext;

     public OnlyExt(String ext)
     {
         this.ext="."+ext;
     }
     public boolean accept(File dir,String name)
     {
         return name.endsWith(ext);
     }


}
