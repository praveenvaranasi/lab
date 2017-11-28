package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.application.controller.ApplicationControllerErrorCodes;
import com.fiorano.esb.rtl.TifosiConstants;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.ApplicationReference;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.aps.ApplicationStateDetails;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/19/11
 * Time: 10:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestApplicationController extends AbstractTestNG{

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

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testProperties.getProperty("ApplicationFile", "1.0.xml");
        m_version2 = Float.parseFloat(testProperties.getProperty("ApplicationVersion2"));
        m_appFile2 = resourceFilePath+ File.separator + testProperties.getProperty("ApplicationFile2", "2.0.xml");
        m_appFile3 = resourceFilePath+ File.separator + testProperties.getProperty("ApplicationFile3", "3.0.xml");
        m_startInstance = testProperties.getProperty("WorkFlowStartInstance");
        m_endInstance = testProperties.getProperty("WorkFlowEndInstance");
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    //picking up the xml extension files
    private void setXmlFilter(String ext){
        xmlFilter = new OnlyExt(ext);//creating filters
    }

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

    @BeforeClass(groups = "ApplicationControllerTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "ApplicationController" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc +"tests" +  fsc + "scenario" + fsc + "ApplicationController";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        //new change
        ServerStatusController stc;
        stc = ServerStatusController.getInstance();
        boolean isFPSHA = stc.getFPSHA();
        setXmlFilter("xml");

        try {
            if (isFPSHA && !isModifiedOnceHA) {
                isModifiedOnceHA = true;
                modifyXmlFiles(resourceFilePath, "fps_test", "fps_ha");
            }
            else if (!isFPSHA && !isModifiedOnce) {
                isModifiedOnce = true;
                modifyXmlFiles(resourceFilePath, "fps_ha", "fps_test");
            }
              init();
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testCreateApplication(){
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("The App File is::" + m_appFile);
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testCreateApplication", "TestApplicationController"));
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
                Assert.assertTrue(m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version), m_appGUID+" could not be started");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateApplication", "TestApplicationController"));
            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateApplication", "TestApplicationController"), e);
                   Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testCreateApplication")
    public void testGetAllApplicationRoutes(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetAllApplicationRoutes", "TestApplicationController"));
                System.out.println("Started the Execution of the TestCase::" + getName());


//            List list = (List)(jmxClient.invoke(objName, "getRoutesOfApplication", params, signatures));
                List list=m_fioranoApplicationController.getRoutesOfApplication(m_appGUID,m_version);
                Assert.assertNotNull(list);
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetAllApplicationRoutes", "TestApplicationController"));

            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetAllApplicationRoutes", "TestApplicationController"), e);
                   Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetAllApplicationRoutes")
    public void  testGetDeliverableMessageCount(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetDeliverableMessageCount", "TestApplicationController"));
                System.out.println("xxxStarted the Execution of the TestCase::" + getName());
                Hashtable pendingMsgs=new Hashtable();
                pendingMsgs=m_fioranoApplicationController.getDeliverableMessageCount(m_appGUID,m_version);
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetDeliverableMessageCount", "TestApplicationController"));
            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetDeliverableMessageCount", "TestApplicationController"), e);
                   Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetDeliverableMessageCount")
    public void testGetApplicationHeaders() {
            try {
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationHeaders", "TestApplicationController"));
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
                System.out.println("Finished fetching saved application headers");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetApplicationHeaders", "TestApplicationController"));
            }
            catch (Exception ex) {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationHeaders", "TestApplicationController"), ex);
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }
        }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationHeaders")
    public void testGetApplicationList(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationList", "TestApplicationController"));
                System.out.println("Started the Execution of the TestCase::" + getName());
                String appName;
                Enumeration en1 =m_fioranoApplicationController.getListOfRunningApplications();
                while(en1.hasMoreElements())
                {
                    appName=en1.nextElement().toString();
                    String[] appNameAndVersion = appName.split("__");
                    String appnew = appNameAndVersion[0];
                    String version = appNameAndVersion[1];
                    float verssion=Float.parseFloat(version);
                    Assert.assertTrue(m_fioranoApplicationController.isApplicationRunning(appnew,verssion));

                }
                System.out.println("Finished fetching list of running application");
                Enumeration en2 =m_fioranoApplicationController.getListOfSavedApplications();
                while(en2.hasMoreElements())
                {
                    appName=en2.nextElement().toString();
                    String[] appNameAndVersion = appName.split("__");
                    String appnew = appNameAndVersion[0];
                    String version = appNameAndVersion[1];
                    float verssion=Float.parseFloat(version);
                    m_fioranoApplicationController.getApplication(appnew,verssion);
                }
                System.out.println("Finished fetching list of saved application");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetApplicationList", "TestApplicationController"));
            }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationList", "TestApplicationController"), e);
                   Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationList")
    public void testGetApplicationsStateDetails(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationsStateDetails", "TestApplicationController"));
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
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetApplicationsStateDetails", "TestApplicationController"));

           }
            catch (Exception e) {
                   System.err.println("Exception in the Execution of test case::"+getName());
                   e.printStackTrace();
                   Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationsStateDetails", "TestApplicationController"), e);
                   Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationsStateDetails")
    public void testGetServiceInstanceStateDetails(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testGetServiceInstanceStateDetails", "TestApplicationController"));
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
                Logger.Log(Level.INFO, Logger.getFinishMessage("testGetServiceInstanceStateDetails", "TestApplicationController"));
            }
            catch (Exception ex) {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServiceInstanceStateDetails", "TestApplicationController"), ex);
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }
        }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetServiceInstanceStateDetails")
    public void testGetApplicationRoutes() {
        // This application is created form the SIMPLECHAT Application, and then setting the Workflow on ports. etc.
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationRoutes", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            List list = m_fioranoApplicationController.getRoutesOfApplication(m_appGUID,m_version);
            Assert.assertNotNull(list);
            Assert.assertTrue(list.size()>0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetApplicationRoutes", "TestApplicationController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationRoutes", "TestApplicationController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationRoutes")
    public void testSyncAppWithRunningVersion() {
           try {
               Logger.LogMethodOrder(Logger.getOrderMessage("testSyncAppWithRunningVersion", "TestApplicationController"));
               System.out.println("Started the Execution of the TestCase::" + getName());
               m_fioranoApplicationController.synchronizeApplicationToRunningVersion(m_appGUID,m_version);
               Logger.Log(Level.INFO, Logger.getFinishMessage("testSyncAppWithRunningVersion", "TestApplicationController"));
           }
           catch (Exception ex) {
               System.err.println("Exception in the Execution of test case::" + getName());
               ex.printStackTrace();
               Logger.Log(Level.SEVERE, Logger.getErrMessage("testSyncAppWithRunningVersion", "TestApplicationController"), ex);
               Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
           }
       }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testSyncAppWithRunningVersion")
    public void testGetApplicationStateDetails() {
           try {
               Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationStateDetails", "TestApplicationController"));
               System.out.println("Started the Execution of the TestCase::"+getName());
               ApplicationStateDetails asd1 = m_fioranoApplicationController.getCurrentStateOfApplication(m_appGUID,m_version);
               Assert.assertTrue(asd1.getAppGUID().equalsIgnoreCase(m_appGUID));
               System.out.println("Finished fetching current state of application");
               ApplicationStateDetails asd2 = m_fioranoApplicationController.getCurrentStateOfApplicationFromFPSs(m_appGUID,m_version);
               Assert.assertTrue(asd2.getAppGUID().equalsIgnoreCase(m_appGUID));
               System.out.println("Finished fetching current state of application from fps");
               Logger.Log(Level.INFO, Logger.getFinishMessage("testGetApplicationStateDetails", "TestApplicationController"));

           }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationStateDetails", "TestApplicationController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationStateDetails")
    public void testSetBreakPointConfig() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetBreakPointConfig", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Hashtable confTable = new Hashtable();
            confTable.put(TifosiConstants.DEBUGGER_BUFFER_SIZE, new Long(102400));
            confTable.put(TifosiConstants.DEBUGGER_WAIT_TIME, new Long(60000));
            m_fioranoApplicationController.setBreakPointConfig(confTable);
            Properties props = new Properties();
            props.put(TifosiConstants.DEBUGGER_BUFFER_SIZE, new Long(102401));
            props.put(TifosiConstants.DEBUGGER_WAIT_TIME, new Long(60001));
            m_fioranoApplicationController.setBreakPointConfig(props);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetBreakPointConfig", "TestApplicationController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetBreakPointConfig", "TestApplicationController"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testSetBreakPointConfig")
    public void testClearApplicationLogs(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearApplicationLogs", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.clearApplicationLogs(m_appGUID,m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearApplicationLogs", "TestApplicationController"));

        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.APS_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearApplicationLogs", "TestApplicationController"), e);
                Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }

        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testClearApplicationLogs")
    public void testGetLogLevel(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetLogLevel", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String[] modules={"ERR_HANDLER","OUT_HANDLER"};
            Hashtable logLevels = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version,m_startInstance,modules);
            Assert.assertNotNull(logLevels);
            Assert.assertTrue(logLevels.size()>0);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetLogLevel", "TestApplicationController"));

        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetLogLevel", "TestApplicationController"), e);
                Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetLogLevel")
    public void testSetLogLevel(){
        try{

            Logger.LogMethodOrder(Logger.getOrderMessage("testSetLogLevel", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            Hashtable modules = new Hashtable();
            modules.put("OUT_HANDLER","ALL");
            modules.put("ERR_HANDLER","ALL");
            m_fioranoApplicationController.setLogLevel(m_appGUID,m_version,m_startInstance,modules);
            String outLogLevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version,m_startInstance,"OUT_HANDLER");
            String errLogLevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_version,m_startInstance,"ERR_HANDLER");
            Assert.assertTrue(outLogLevel.equalsIgnoreCase("ALL") && errLogLevel.equalsIgnoreCase("ALL"));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetLogLevel", "TestApplicationController"));

        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetLogLevel", "TestApplicationController"), e);
                Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testSetLogLevel")
    public void testGetServiceStateDetails(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetServiceStateDetails", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            ServiceInstanceStateDetails details = m_fioranoApplicationController.getCurrentStateOfServiceFromFPSs(m_appGUID,m_version,m_startInstance);
            Assert.assertNotNull(details);
            Assert.assertTrue(details.getServiceInstanceName().equalsIgnoreCase(m_startInstance) && details.getServiceNodeName().equalsIgnoreCase("fps"));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetServiceStateDetails", "TestApplicationController"));

        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServiceStateDetails", "TestApplicationController"), e);
                Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetServiceStateDetails")
    public void testClearServiceErrLogs(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearServiceErrLogs", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.clearServiceErrLogs(m_startInstance,m_appGUID,m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearServiceErrLogs", "TestApplicationController"));
        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearServiceErrLogs", "TestApplicationController"), e);
                Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testClearServiceErrLogs")
    public void testClearServiceOutLogs(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearServiceOutLogs", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.clearServiceOutLogs(m_startInstance,m_appGUID,m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearServiceOutLogs", "TestApplicationController"));
        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearServiceOutLogs", "TestApplicationController"), e);
                Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testClearServiceOutLogs")
    public void testGetLastOutTrace(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetLastOutTrace", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.getLastOutTrace(50,m_startInstance,m_appGUID,m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetLastOutTrace", "TestApplicationController"));
        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetLastOutTrace", "TestApplicationController"), e);
                Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetLastOutTrace")
    public void testGetLastErrTrace(){
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetLastErrTrace", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::"+getName());
            m_fioranoApplicationController.getLastErrorTrace(50,m_startInstance,m_appGUID,m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetLastErrTrace", "TestApplicationController"));
        } catch(Exception e){
            boolean check = e.getMessage().indexOf(ApplicationControllerErrorCodes.SERVICE_HANDLE_NOT_PRESENT)!=-1;
            if(!check){
                System.err.println("Exception in the Execution of test case::"+getName());
                e.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetLastErrTrace", "TestApplicationController"), e);
                Assert.assertTrue(false, "TestCase Failed because of "+e.getMessage());
            }
        }
    }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testGetLastErrTrace")
    public void testStopAllServices(){
            try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testStopAllServices", "TestApplicationController"));
                System.out.println("Started the Execution of the TestCase::" + getName());
                  if(!(((m_fioranoApplicationController.getCurrentStateOfService(m_appGUID,m_version,m_startInstance))).getKillTime() < new Date().getTime()))
                    throw new Exception("Stop All Services Failed");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testStopAllServices", "TestApplicationController"));
            }catch(Exception ex){
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Logger.Log(Level.SEVERE, Logger.getErrMessage("testStopAllServices", "TestApplicationController"), ex);
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }
        }

    @Test(groups = "ApplicationControllerTest", alwaysRun = true, dependsOnMethods = "testStopAllServices")
    public void testDeleteApplication() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteApplication", "TestApplicationController"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteApplication", "TestApplicationController"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteApplication", "TestApplicationController"), ex);
            Assert.assertTrue(
                    false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @AfterClass(groups = "ApplicationControllerTest", alwaysRun = true)
    public void testCleanup() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCleanup", "ApplicationControllerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);

            m_fioranoApplicationController.close();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCleanup", "ApplicationControllerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanup", "ApplicationControllerTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
}
