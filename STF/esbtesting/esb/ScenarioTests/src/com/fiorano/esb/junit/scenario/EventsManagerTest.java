package com.fiorano.esb.junit.scenario;

import com.fiorano.stf.misc.JUnitEventListener;
import com.fiorano.stf.jms.STFQueueSender;
import com.fiorano.stf.consts.JUnitConstants;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.misc.JUnitDebuger;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.esb.rtl.events.FioranoEventsManager;
import com.fiorano.esb.rtl.TifosiConstants;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.TestCaseElement;

import java.io.*;
import java.util.Enumeration;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.application.InputPortInstance;
import fiorano.tifosi.dmi.events.EventSearchContext;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.events.TifosiEventTypes;
import fiorano.tifosi.events.EventStateConstants;

/**
 * Created by IntelliJ IDEA.
 * User: Prasanth
 * Date: Feb 27, 2007
 * Time: 11:54:50 AM
 * To change this template use File | Settings | File Templates.
 */
public class EventsManagerTest extends DRTTestCase {

    private boolean m_initialised;
    FioranoEventsManager m_EventsManager;
    FioranoApplicationController m_fioranoApplicationController;
    private String resourceFilePath;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String m_startInstance;
    private String m_endInstance;

    static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter

    public EventsManagerTest(String name) {
        super(name);
    }

    public EventsManagerTest(TestCaseElement testCaseConfig){
        super(testCaseConfig);
    }


    private void setXmlFilter(String ext)//creating the xml file filter
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
    //

    public static Test suite() {
        return new TestSuite(EventsManagerTest.class);
    }

    public void init() throws Exception {

        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath+ File.separator + testCaseProperties.getProperty("ApplicationFile", "1.0.xml");
        m_startInstance = testCaseProperties.getProperty("WorkFlowStartInstance");
        m_endInstance = testCaseProperties.getProperty("WorkFlowEndInstance");

        printInitParams();

    }

    protected void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    public void setUp() throws Exception {
        super.setUp();
        if (!m_initialised) {
            m_EventsManager = rtlClient.getFioranoEventsManager();
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "EventsManager";

            ServerStatusController stc;
        stc= ServerStatusController.getInstance();
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
            m_initialised = true;
        }
    }

    public void testStartReceiving() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testStartReceiving", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_EventsManager.startReceiving();
            m_EventsManager.clearAllApplicationEvents(m_appGUID,"m_version",null,-1,0,-1);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testStartReceiving", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testStartReceiving", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testRegisterEventListener() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRegisterEventListener", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitEventListener eventListener = new JUnitEventListener("From Register Event Listener");
            m_EventsManager.registerEventListener(eventListener, TifosiConstants.ALL_EVENT_LISTENER,false);
            //m_fioranoServiceProvider.getSecurityManager().createUser("testUser","testpasswd");
            //m_fioranoServiceProvider.getSecurityManager().deleteUser("testUser");
            m_EventsManager.unRegisterEventListner(eventListener,TifosiConstants.ALL_EVENT_LISTENER);
             m_EventsManager.registerEventListener(eventListener, TifosiConstants.ALL_EVENT_LISTENER,false);
            m_EventsManager.cleanupEventListener(eventListener);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRegisterEventListener", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRegisterEventListener", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testRegisterAllEventListener() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRegisterAllEventListener", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitEventListener eventListener = new JUnitEventListener("From Register All Event Listener");
            m_EventsManager.registerAllEventListener(eventListener,false);
            //m_fioranoServiceProvider.getSecurityManager().createUser("testUser","testpasswd");
            // m_fioranoServiceProvider.getSecurityManager().deleteUser("testUser");
            m_EventsManager.unRegisterAllEventListener(eventListener);
            m_EventsManager.registerAllEventListener(eventListener);
            m_EventsManager.unRegisterAllEventListener(eventListener);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRegisterAllEventListener", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRegisterAllEventListener", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testRegisterApplicationEventListener() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRegisterApplicationEventListener", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitEventListener eventListener = new JUnitEventListener("From Register Application Event Listener");
            m_EventsManager.registerApplicationEventListener(eventListener,false);
            m_EventsManager.unRegisterApplicationEventListener(eventListener);
            m_EventsManager.registerApplicationEventListener(eventListener);
            m_EventsManager.unRegisterApplicationEventListener(eventListener);
            m_EventsManager.registerApplicationEventListener(eventListener,m_appGUID,false);
            m_EventsManager.unRegisterApplicationEventListener(m_appGUID);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRegisterApplicationEventListener", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRegisterApplicationEventListener", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testRegisterFPSEventListener() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRegisterFPSEventListener", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitEventListener eventListener = new JUnitEventListener("From Register FPS Event Listener");
            m_EventsManager.registerFPSEventListener(eventListener,false);
            m_EventsManager.unRegisterFPSEventListener(eventListener);
            m_EventsManager.registerFPSEventListener(eventListener);
            m_EventsManager.unRegisterFPSEventListener(eventListener);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRegisterFPSEventListener", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRegisterFPSEventListener", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testRegisterSecurityEventListener() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRegisterSecurityEventListener", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitEventListener eventListener = new JUnitEventListener("From Register Security Event Listener");
            m_EventsManager.registerSecurityEventListener(eventListener,false);
            m_EventsManager.unRegisterSecurityEventListener(eventListener);
            m_EventsManager.registerSecurityEventListener(eventListener);
            m_EventsManager.unRegisterSecurityEventListener(eventListener);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRegisterSecurityEventListener", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRegisterSecurityEventListener", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testRegisterComponentEventListener() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRegisterComponentEventListener", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            JUnitEventListener eventListener = new JUnitEventListener("From Register Component Event Listener");
            m_EventsManager.registerComponentEventListener(eventListener,false);
            m_EventsManager.unRegisterComponentEventListener(eventListener);
            m_EventsManager.registerComponentEventListener(eventListener);
            m_EventsManager.unRegisterComponentEventListener(eventListener);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRegisterComponentEventListener", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRegisterComponentEventListener", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testRegisterAllEventListener2() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRegisterAllEventListener2", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            /* clearing all events logged bofore */
            m_EventsManager.clearAllApplicationEvents(m_appGUID,"m_version",null,-1,0,-1);

            /* register new all event listener, upgrade if present */
            JUnitEventListener eventListener = new JUnitEventListener("From Register All Event Listener");
            m_EventsManager.registerAllEventListener(eventListener);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRegisterAllEventListener2", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRegisterAllEventListener2", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testCreateApplication() {
        // This application is created form the SIMPLECHAT Application, and then setting the Workflow on ports. etc.
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCreateApplication", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("The App File is::" + m_appFile);
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application with GUID::" + m_appGUID + ", version::" + m_version + " not created");
            startApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCreateApplication", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCreateApplication", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }
     private void startApplication(String appGuid, float appVersion) throws FioranoException {
        try {
            m_fioranoApplicationController.compileApplication(appGuid,appVersion);
            m_fioranoApplicationController.prepareLaunch(appGuid, appVersion);
            m_fioranoApplicationController.launchApplication(appGuid, appVersion);
            m_fioranoApplicationController.startAllServices(appGuid,appVersion);
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

    // written for the sake of bug 11775
    public void testAPPLICATION_SAVED(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAPPLICATION_SAVED", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.APPLICATION_EVENT); //event type is must for search
            esc.setEventStatus(EventStateConstants.APPLICATION_SAVED);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAPPLICATION_SAVED", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAPPLICATION_SAVED", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testAPPLICATION_CREATED(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAPPLICATION_CREATED", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.APPLICATION_EVENT); //event type is must for search
            esc.setEventStatus(EventStateConstants.APPLICATION_REPOSITORY_UPDATION);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAPPLICATION_CREATED", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAPPLICATION_CREATED", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testAPPLICATION_LAUNCHED(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAPPLICATION_LAUNCHED", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.APPLICATION_EVENT);  //event type is must for search
            esc.setEventStatus(EventStateConstants.APPLICATION_LAUNCHED);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAPPLICATION_LAUNCHED", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAPPLICATION_LAUNCHED", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testROUTE_DEBUGGER_SET(){
       try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testROUTE_DEBUGGER_SET", "EventsManagerTest"));
            System.out.println("Started the ESBWDetailerTestxecution of the TestCase::" + getName());
            String m_routeGUID = testCaseProperties.getProperty("RouteGUID");
            JUnitDebuger interceptor = new JUnitDebuger(m_appGUID,m_routeGUID);
            m_fioranoApplicationController.setDebugger(m_routeGUID,m_appGUID,m_version,interceptor);

            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.ROUTE_EVENT);   //event type is must for search
            esc.setEventStatus(EventStateConstants.ROUTE_DEBUGGER_SET);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,-1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testROUTE_DEBUGGER_SET", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testROUTE_DEBUGGER_SET", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testROUTE_DEBUGGER_REMOVED(){
       try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testROUTE_DEBUGGER_REMOVED", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String m_routeGUID = testCaseProperties.getProperty("RouteGUID");
            m_fioranoApplicationController.removeDebugger(m_routeGUID,m_appGUID,m_version,true,false,10000L);

            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.ROUTE_EVENT);      //event type is must for search
            esc.setEventStatus(EventStateConstants.ROUTE_DEBUGGER_REMOVED);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,-1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testROUTE_DEBUGGER_REMOVED", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testROUTE_DEBUGGER_REMOVED", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testGetAllApplicationEvents() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetAllApplicationEvents", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Enumeration en = m_EventsManager.getAllApplicationEvents(m_appGUID,"m_version",m_startInstance, TifosiEventTypes.APPLICATION_EVENT,0,1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetAllApplicationEvents", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetAllApplicationEvents", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testSearchEvents() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSearchEvents", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.APPLICATION_EVENT); //event type is must for search
            esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSearchEvents", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSearchEvents", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testGetSystemEventModules() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetSystemEventModules", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Hashtable ht = m_EventsManager.getSystemEventModules();
            Enumeration en = ht.keys();
            Assert.assertTrue(en.hasMoreElements());
            while(en.hasMoreElements()){
                String key = (String)en.nextElement();
                System.out.println(key+"::"+ht.get(key));
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetSystemEventModules", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetSystemEventModules", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testGetNumberofEvents() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetNumberofEvents", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Assert.assertTrue(m_EventsManager.getTotalNumberOfEvents(TifosiEventTypes.APPLICATION_EVENT,m_appGUID,"m_version")>0);
     //       Assert.assertTrue(m_EventsManager.getTotalNumberOfEvents(TifosiEventTypes.APPLICATION_EVENT,m_appGUID,m_startInstance)>0);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetNumberofEvents", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetNumberofEvents", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testSERVICE_KILLED_ON_TPS_SHUTDOWN(){
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSERVICE_KILLED_ON_TPS_SHUTDOWN", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            FioranoFPSManager m_FPSManager = rtlClient.getFioranoFPSManager();
            String m_FPSName = testCaseProperties.getProperty(JUnitConstants.FPS_NAME);
            System.out.println(m_FPSName);
            Assert.assertTrue(m_FPSManager.isTPSRunning(m_FPSName));
            m_FPSManager.restartTPS(m_FPSName);
            Thread.sleep(25000); //wait till server restarts - change if necessary
            //Assert.assertTrue(m_FPSManager.getIsTPSExist(m_FPSName));
            Assert.assertTrue(m_FPSManager.isTPSRunning(m_FPSName));

            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.SERVICE_EVENT); //event type is must for search
            esc.setEventStatus(EventStateConstants.SERVICE_KILLED_ON_TPS_SHUTDOWN);
            //esc.setAppInstanceName(m_appGUID);
            esc.setTPSName(m_FPSName);
            Enumeration en = m_EventsManager.searchEvents(esc,0,1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSERVICE_KILLED_ON_TPS_SHUTDOWN", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSERVICE_KILLED_ON_TPS_SHUTDOWN", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testAPPLICATION_RENAMED(){
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAPPLICATION_RENAMED", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
           // Assert.assertTrue(m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version));
            String newGUID = "NEWTESTAPPID";
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version)){
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            }
            m_fioranoApplicationController.renameApplication(m_appGUID,m_version, newGUID, m_version);
            if (m_fioranoApplicationController.getApplication(newGUID, m_version) == null)
                throw new Exception("Application name not changed");
            m_fioranoApplicationController.renameApplication(newGUID,m_version, m_appGUID, m_version);
            if (m_fioranoApplicationController.getApplication(m_appGUID, m_version) == null)
                throw new Exception("Application name not changed");

            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.APPLICATION_EVENT);  //event type is must for search
            esc.setEventStatus(EventStateConstants.APPLICATION_RENAMED);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAPPLICATION_RENAMED", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAPPLICATION_RENAMED", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testDeleteApplication() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteApplication", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            if(m_fioranoApplicationController.isApplicationRunning(m_appGUID,m_version)){
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            }
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteApplication", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteApplication", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }



    // written for the sake of bug 11775
    public void testAPPLICATION_KILLED(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAPPLICATION_KILLED", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.APPLICATION_EVENT); //event type is must for search
            esc.setEventStatus(EventStateConstants.APPLICATION_KILLED);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,2);
            Assert.assertFalse(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAPPLICATION_KILLED", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAPPLICATION_KILLED", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testAPPLICATION_DELETED(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAPPLICATION_DELETED", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.APPLICATION_EVENT); //event type is must for search
            esc.setEventStatus(EventStateConstants.APPLICATION_DELETED);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,2);
            Assert.assertFalse(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAPPLICATION_DELETED", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAPPLICATION_DELETED", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testSERVICE_HANDLE_BOUND(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSERVICE_HANDLE_BOUND", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Thread.sleep(30000);
            //as the application is lauched, services have to be launched, and so this event can be expected
            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.SERVICE_EVENT); //event type is must for search
            esc.setEventStatus(EventStateConstants.SERVICE_HANDLE_BOUND);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,-1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSERVICE_HANDLE_BOUND", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSERVICE_HANDLE_BOUND", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    // written for the sake of bug 11775
    public void testSERVICE_HANDLE_UNBOUND(){
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSERVICE_HANDLE_UNBOUND", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            //as the application is killed, services have to be killed, and so this event can be expected
            EventSearchContext esc = new EventSearchContext();
            esc.addEventType(TifosiEventTypes.SERVICE_EVENT); //event type is must for search
            esc.setEventStatus(EventStateConstants.SERVICE_HANDLE_UNBOUND);
            //esc.setAppInstanceName(m_appGUID);
            Enumeration en = m_EventsManager.searchEvents(esc,0,1);
            Assert.assertTrue(en.hasMoreElements());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSERVICE_HANDLE_UNBOUND", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSERVICE_HANDLE_UNBOUND", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    private void sendMessageOnInputPort(String m_appGUID, String serviceInstName, String portName, String message, int count) throws Exception {
        STFQueueSender publisher = null;
        System.out.println("Sending the message \n" + message);
        publisher = new STFQueueSender();
        String fpsName = (String) m_fioranoApplicationController.getApplication(m_appGUID, m_version).getServiceInstance(serviceInstName).getNodes()[0];
        if (fpsName == null || fpsName.trim().equals(""))
            fpsName = "fps";
        String url = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
        String queueName = m_appGUID + "__" + serviceInstName + "__" + portName;
        publisher.initialize(queueName, null, url);
        publisher.send(message, count);
        try {
            Thread.sleep(200L);
        } catch (InterruptedException e) {
            //ignore
        }

        if (publisher != null)
            publisher.cleanup();
        System.out.println("Messages Published");
    }

    public void testStopReceiving() {
        try {
           DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testStopReceiving", "EventsManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_EventsManager.clearAllApplicationEvents(m_appGUID,"m_version",m_startInstance,TifosiEventTypes.APPLICATION_EVENT,0,1);
            EventSearchContext esc = new EventSearchContext();
            esc.setAppInstanceName(m_appGUID);
            m_EventsManager.clearEventLogs(esc);
            m_EventsManager.stopReceiving();
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testStopReceiving", "EventsManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testStopReceiving", "EventsManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public static ArrayList getOrder(){

	ArrayList methodsOrder = new ArrayList();

	methodsOrder.add("testStartReceiving");
	methodsOrder.add("testRegisterEventListener");
	methodsOrder.add("testRegisterAllEventListener");
	methodsOrder.add("testRegisterApplicationEventListener");
	methodsOrder.add("testRegisterFPSEventListener");
	methodsOrder.add("testRegisterSecurityEventListener");
	methodsOrder.add("testRegisterComponentEventListener");
    methodsOrder.add("testRegisterAllEventListener2");
	methodsOrder.add("testCreateApplication");
  //methodsOrder.add("testAPPLICATION_SAVED");
    methodsOrder.add("testAPPLICATION_CREATED");
    methodsOrder.add("testSERVICE_HANDLE_BOUND");
  //methodsOrder.add("testAPPLICATION_LAUNCHED");
  //  methodsOrder.add("testROUTE_DEBUGGER_SET");
  //  methodsOrder.add("testROUTE_DEBUGGER_REMOVED");
    methodsOrder.add("testGetAllApplicationEvents");
	methodsOrder.add("testSearchEvents");
	methodsOrder.add("testGetSystemEventModules");
	methodsOrder.add("testGetNumberofEvents");
  //methodsOrder.add("testSERVICE_KILLED_ON_TPS_SHUTDOWN");
 // methodsOrder.add("testAPPLICATION_RENAMED");
    methodsOrder.add("testAPPLICATION_KILLED");
    methodsOrder.add("testDeleteApplication");
    methodsOrder.add("testAPPLICATION_DELETED");
    methodsOrder.add("testSERVICE_HANDLE_UNBOUND");
    methodsOrder.add("testStopReceiving");

	return methodsOrder;
    }
}
