package com.fiorano.esb.junit.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.dm.FioranoDeploymentManager;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.rtl.sbw.FioranoSBWManager;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.rtl.security.FioranoSecurityManager;
import com.fiorano.esb.rtl.events.FioranoEventsManager;
import com.fiorano.esb.rtl.ExceptionListener;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.TestCaseElement;

import java.util.ArrayList;
import java.io.*;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;
import fiorano.tifosi.tr.uc.IUCClientSession;
import fiorano.tifosi.cfg.IConfigurationManager;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.TESPerformanceStats;
import fiorano.tifosi.dmi.SystemInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Prasanth
 * Date: Feb 23, 2007
 * Time: 2:09:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServiceProviderTest extends DRTTestCase {

    private String resourceFilePath;
    private boolean m_initialised;

     static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter


    public ServiceProviderTest(String name) {
        super(name);
    }

    public ServiceProviderTest(TestCaseElement testCaseConfig){
        super(testCaseConfig);
    }

    public static Test suite(){
        return new TestSuite(ServiceProviderTest.class);
    }

    public void init() throws Exception{
        printInitParams();
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
    //changed function

    protected void printInitParams(){
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("...........................................................................");
    }
    
    public void setUp() throws Exception {
        super.setUp();
        if(!m_initialised)
        {
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "ServiceProvider";


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
            m_initialised = true;
        }
    }

    public void testGetManagers() throws Exception
    {
        try
        {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetManagers", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::"+getName());

            FioranoApplicationController fac = rtlClient.getFioranoApplicationController();
            Assert.assertNotNull(fac);
            FioranoDeploymentManager fdm = rtlClient.getFioranoDeploymentManager();
            Assert.assertNotNull(fdm);
            FioranoServiceRepository fsr = rtlClient.getFioranoServiceRepository();
            Assert.assertNotNull(fsr);
            FioranoSBWManager fsbwm = rtlClient.getFioranoSBWManager();
            Assert.assertNotNull(fsbwm);
            FioranoFPSManager ffpsm = rtlClient.getFioranoFPSManager();
            Assert.assertNotNull(ffpsm);
            FioranoSecurityManager fsm = rtlClient.getFioranoSecurityManager();
            Assert.assertNotNull(fsm);
            FioranoEventsManager fem = rtlClient.getFioranoEventsManager();
            Assert.assertNotNull(fem);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetManagers", "ServiceProviderTest"));
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetManagers", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of "+ex.getMessage(),false);

        }
    }

    public void testServerDetails() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testServerDetails", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Assert.assertTrue(rtlClient.getFioranoServiceProvider().isESBServerActive());
            System.out.println("ESB Server Active");
            long l = rtlClient.getFioranoServiceProvider().getFESActiveTime();
            Assert.assertTrue(l > 0);
            System.out.println("ESB Server Active for::" + l);

            String url = rtlClient.getFioranoServiceProvider().getServerURL();
            System.out.println("Server URL::" + url);

            int version = rtlClient.getFioranoServiceProvider().getCommunicationVersion();
            Assert.assertTrue(version > 0);

            boolean secEnabled = rtlClient.getFioranoServiceProvider().getIsSecurityEnabled();
            Assert.assertTrue(secEnabled);

            Vector urlList = rtlClient.getFioranoServiceProvider().getBackupURLs();
		 if(urlList!=null)
	            Assert.assertTrue(urlList.size()>0);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testServerDetails", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testServerDetails", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }



     public void testGetHandle() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetHandle", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            String handle = rtlClient.getHandleID();
            Assert.assertNotNull(handle);
            System.out.println("Server Handle is::" + handle);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetHandle", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetHandle", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testloadPropertyBundles() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testloadPropertyBundles", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            rtlClient.getFioranoServiceProvider().loadPropertyBundles();
            System.out.println("Loaded property bundle");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testloadPropertyBundles", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testloadPropertyBundles", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testIsConnectionClosed() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testIsConnectionClosed", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Assert.assertFalse(rtlClient.getFioranoServiceProvider().isConnectionCloseCalled());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testIsConnectionClosed", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testIsConnectionClosed", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testGetUserPermissions() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetUserPermissions", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Vector permissionList = rtlClient.getFioranoServiceProvider().getUserPermissions();
            for(int i=0; i<permissionList.size(); i++)
                System.out.println(permissionList.get(i).toString());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetUserPermissions", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetUserPermissions", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testGetTESPerformanceStats() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetTESPerformanceStats", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            TESPerformanceStats tesps = rtlClient.getFioranoServiceProvider().getTESPerformanceStats();
            Assert.assertNotNull(tesps);
            System.out.println("Free Memory::"+tesps.getFreeMemory());
            System.out.println("Memory Usage::"+tesps.getMemoryUsage());
            System.out.println("Total Memory::"+tesps.getTotalMemory());
            System.out.println("Process count::"+tesps.getTotalProcessCount());
            System.out.println("Thread count::"+tesps.getTotalThreadCount());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetTESPerformanceStats", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetTESPerformanceStats", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testGetTESSystemInfo() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetTESSystemInfo", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            SystemInfo si = rtlClient.getFioranoServiceProvider().getTESSystemInfo();
            Assert.assertNotNull(si);
            System.out.println("JRE Impl vendor::"+si.getJREImplVendor());
            System.out.println("JRE Impl version::"+si.getJREImplVersion());
            System.out.println("JRE Spec version::"+si.getJRESpecVersion());
            System.out.println("JVM Impl name::"+si.getJVMImplName());
            System.out.println("JVM Impl vendor::"+si.getJVMImplVendor());
            System.out.println("JVM Impl version::"+si.getJVMImplVersion());
            System.out.println("JVM Spec version::"+si.getJVMSpecVersion());
            System.out.println("OS name::"+si.getOSName());
            System.out.println("OS version::"+si.getOSVersion());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetTESSystemInfo", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetTESSystemInfo", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testSetTimeoutForServerCalls() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetTimeoutForServerCalls", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            rtlClient.getFioranoServiceProvider().setTimeoutForServerCalls(10000);
            System.out.println("Timeout set to 10000");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetTimeoutForServerCalls", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetTimeoutForServerCalls", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testSetExceptionListener() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetExceptionListener", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

             rtlClient.getFioranoServiceProvider().setExceptionListener(new ExceptionListener() {
                public void onException(Throwable thr) throws TifosiException {
                }
            });
            
            ExceptionListener exListener = rtlClient.getFioranoServiceProvider().getExceptionListener();
            Assert.assertNotNull(exListener);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetExceptionListener", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetExceptionListener", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

     public void testJMSStats() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testJMSStats", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            String mq = rtlClient.getFioranoServiceProvider().getActiveMQ();
            System.out.println("MQ server::"+mq);

            Hashtable ht = rtlClient.getFioranoServiceProvider().getStateOfJMS();
            System.out.println("JMS State");
            Enumeration keys = ht.keys();
            while(keys.hasMoreElements())
            {
                Object key = keys.nextElement();
                System.out.println(key.toString() + "::" + ht.get(key));
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testJMSStats", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testJMSStats", "ServiceProviderTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }


    //covers server close and stop also?
    /*public void testServerRestart() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            m_fioranoServiceProvider.restart();

            Thread.sleep(20000); //wait till server restarts - change if necessary
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }*/



//    public void testShutdown() throws Exception {
//        try {
//            System.out.println("Started the Execution of the TestCase::" + getName());
//
//            String mq = m_fioranoServiceProvider.getActiveMQ();
//            m_fioranoServiceProvider.shutdownMQ(mq);  //Is this possible? :)
//
//            m_fioranoServiceProvider.shutdown();
//        }
//        catch (Exception ex) {
//            System.err.println("Exception in the Execution of test case::" + getName());
//            ex.printStackTrace();
//            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
//
//        }
//    }

    public static ArrayList getOrder(){
	
	ArrayList methodsOrder = new ArrayList();

	methodsOrder.add("testGetManagers");
   	methodsOrder.add("testServerDetails");
   	methodsOrder.add("testGetHandle");
   	methodsOrder.add("testloadPropertyBundles");
   	methodsOrder.add("testIsConnectionClosed");
   	methodsOrder.add("testGetUserPermissions");
   	methodsOrder.add("testGetTESPerformanceStats");
   	methodsOrder.add("testGetTESSystemInfo"); 
   	methodsOrder.add("testSetTimeoutForServerCalls");
   	methodsOrder.add("testSetExceptionListener");
   	methodsOrder.add("testJMSStats");

	return methodsOrder;
    }	
}
