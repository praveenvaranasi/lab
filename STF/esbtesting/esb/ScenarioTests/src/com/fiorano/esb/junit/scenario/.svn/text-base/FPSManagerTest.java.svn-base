package com.fiorano.esb.junit.scenario;

import com.fiorano.stf.consts.JUnitConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;

import java.util.*;
import java.io.*;
import java.util.Enumeration;
import java.util.Vector;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;
import fiorano.tifosi.dmi.*;
import fiorano.tifosi.dmi.service.ServiceReference;
import fiorano.tifosi.events.SystemEventInfo;

/**
 * Created by IntelliJ IDEA.
 * User: Prasanth
 * Date: Feb 26, 2007
 * Time: 11:26:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class FPSManagerTest extends DRTTestCase {
    private String resourceFilePath;
    private boolean m_initialised;
    FioranoFPSManager m_FPSManager;
    String  m_FPSName;
    String m_zipFilePath;

    static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter

    public FPSManagerTest(String name) {
        super(name);
    }

     public FPSManagerTest(TestCaseElement testCaseConfig){
        super(testCaseConfig);
    }

    public static Test suite() {
        return new TestSuite(FPSManagerTest.class);
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
    //c

    public void init() throws Exception {
        m_FPSName = testCaseProperties.getProperty(JUnitConstants.FPS_NAME);
        m_zipFilePath = resourceFilePath + File.separator + testCaseProperties.getProperty(JUnitConstants.ZIPFILE_NAME);
        printInitParams();
    }

    protected void printInitParams() {
        System.out.println("The Parameters Used For The Test Are::");
        System.out.println("FPS Name:: "+ m_FPSName);
        System.out.println("ZIP file Name:: "+ m_zipFilePath);
        System.out.println("...........................................................................");
    }

    public void setUp() throws Exception {
        super.setUp();
        if (!m_initialised) {
            m_FPSManager = rtlClient.getFioranoFPSManager();
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "FPSManager";

        //new changes            
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

    public void testGetTPS() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Enumeration tpsenum = m_FPSManager.getAllTPS();
            while(tpsenum.hasMoreElements()){
                TPSPropertySheet tpsp = (TPSPropertySheet)tpsenum.nextElement();
                System.out.println("tpsname::" + tpsp.getName());
            }
            Enumeration tpsnames = m_FPSManager.getAllTPSNamesFromRepository();
            while(tpsnames.hasMoreElements()){
                System.out.println(tpsnames.nextElement().getClass());
                //String name = (String)tpsnames.nextElement();
                //System.out.println("tpsnamefrom repository::" + name);
            }
            Enumeration tpsaliases = m_FPSManager.getAllTPSAliases();
            while(tpsaliases.hasMoreElements()){
                Object obj = tpsaliases.nextElement();
                Assert.assertNotNull(obj);
                System.out.println(obj.getClass());
                String[] alias = (String[])obj;
                //String alias = (String)tpsaliases.nextElement();
                //System.out.println(tpsaliases.nextElement().getClass());
                //String alias = (String)tpsaliases.nextElement(); \
                for(int i=0;i<alias.length;i++){
                System.out.println("tpsalias::" + alias[i]);
                }
            }
            Vector vec = m_FPSManager.getTPSAliases(m_FPSName);
            for(int i=0; i<vec.size(); i++){
                System.out.println((String)vec.get(i));        
            }
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


//    public void testCreateTPS() throws Exception {
//        try {
//            System.out.println("Started the Execution of the TestCase::" + getName());
//
//            Assert.assertTrue(m_FPSManager.getIsTPSExist(m_FPSName));
//            //m_FPSManager.shutdownTPS(m_FPSName);
//            TPSPropertySheet tpsps = m_FPSManager.getTPSProperties(m_FPSName);
//            Assert.assertNotNull(tpsps);         m_FPSManager.setTPSAlertModules(tpsps);
//
//            //tpsps.setTPSName("TestTPS");
//
//            //MessageBusConfiguration mbc = tpsps.getMessageBusConfiguration();
//            //Assert.assertNotNull(mbc);
//            //System.out.println("message bus config::" + mbc.toXMLString());
//
//            System.out.println("tpsconfs");
//            Properties props = tpsps.getTPSConfsProperties();
//            Assert.assertNotNull(props);
//            props.list(System.out);
//
//            //tpsps.getTPSConfiguration();
//
//
//        }
//        catch (Exception ex) {
//            System.err.println("Exception in the Execution of test case::" + getName());
//            ex.printStackTrace();
//            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
//        }
//    }

    public void testGetLogs() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            String outlogs = m_FPSManager.getTPSLastOutLogs(1,m_FPSName);
            String errlogs = m_FPSManager.getTPSLastErrLogs(1,m_FPSName);
            // the returned strings will be null if the log files are empty
            if(outlogs != null)
                System.out.println(m_FPSManager.getTPSLastErrLogs(1,m_FPSName));
            if(errlogs != null)
                System.out.println(m_FPSManager.getTPSLastOutLogs(1,m_FPSName));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testGetTraceInfo() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            TraceConfiguration tc = m_FPSManager.getTPSTraceConfiguration(m_FPSName);
            Enumeration en = tc.getTraceValues();
            while(en.hasMoreElements()){
                System.out.println(en.getClass());
            }
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testSetTraceInfo() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            TraceConfiguration tc = m_FPSManager.getTPSTraceConfiguration(m_FPSName);
            m_FPSManager.setTPSTraceConfiguration(m_FPSName,tc);
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testExportLogs() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            //System.out.println(m_zipFilePath);
            File file = new File(m_zipFilePath);
            Assert.assertNotNull(file);
            m_FPSManager.exportFPSLogs(m_FPSName,file.getAbsolutePath());
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testClearLogs() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_FPSManager.clearTPSErrLogs(m_FPSName);
            m_FPSManager.clearTPSOutLogs(m_FPSName);
            Assert.assertNull(m_FPSManager.getTPSLastOutLogs(1,m_FPSName));
            Assert.assertNull(m_FPSManager.getTPSLastErrLogs(1,m_FPSName));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testTPSURL() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("fps protocol::" + m_FPSManager.getConnectProtocolForFPS(m_FPSName));
            System.out.println("fps connect url::" + m_FPSManager.getConnectURLForFPS(m_FPSName));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testGetFPSSystemInfo() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            SystemInfo si = m_FPSManager.getTPSSystemInfo(m_FPSName);
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
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testGetAllInstalledServices() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Enumeration en = m_FPSManager.getAllInstalledServices(m_FPSName);
            while (en.hasMoreElements()) {
                ServiceInfo si = (ServiceInfo)en.nextElement();
                ServiceReference sr = si.getServiceHeader();
                System.out.println("service Name::"+sr.getDisplayName());
                //System.out.println(en.nextElement().getClass());
            }
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testGetTPSPerformanceStats() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            TPSPerformanceStats tesps = m_FPSManager.getTPSPerformanceStats(m_FPSName);
            Assert.assertNotNull(tesps);
            System.out.println("Free Memory::"+tesps.getFreeMemory());
            System.out.println("Memory Usage::"+tesps.getMemoryUsage());
            System.out.println("Total Memory::"+tesps.getTotalMemory());
            System.out.println("Process count::"+tesps.getTotalProcessCount());
            System.out.println("Thread count::"+tesps.getTotalThreadCount());
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testGetTPSAlertModules() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Vector vec = m_FPSManager.getTPSAlertModules(m_FPSName);
            Assert.assertNotNull(vec);
            for(int i=0; i<vec.size();i++){
                SystemEventInfo sei = (SystemEventInfo)vec.elementAt(i);
                String[] list = sei.getAlertHandlers();
                for (int j = 0; j < list.length; j++) {
                    System.out.println(list[j]);
                }
                //System.out.println(vec.elementAt(i).getClass());
            }
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testSetTPSAlertModules() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Vector vec = m_FPSManager.getTPSAlertModules(m_FPSName);
            m_FPSManager.setTPSAlertModules(m_FPSName,vec);
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

     public void testGetBackupURLs() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Vector vec = m_FPSManager.getBackupURLsForFPS(m_FPSName);
            for(int i=0; i<vec.size();i++){
                String url = (String)vec.elementAt(i);
                System.out.println("BACKUP URL is ::: "+url);
                //System.out.println(vec.elementAt(i).getClass());
            }
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testRemoveInstalledService() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            String s = m_FPSManager.removeInstalledService(m_FPSName,"chat","4.0",true);
            Assert.assertTrue(ServicesRemovalStatus.SERVICE_SUCCESSFULLY_REMOVED.equalsIgnoreCase(s) ||
                    ServicesRemovalStatus.SERVICE_NOT_PRESENT.equalsIgnoreCase(s));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testRemoveServiceFromNetwork() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_FPSManager.removeServiceFromNetwork("FileWriter","4.0",false);
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testRemoveAllInstalledServices() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            ServicesRemovalStatus srs = m_FPSManager.removeAllInstalledServices(m_FPSName,true);
            Assert.assertTrue(srs!=null);
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }


    public void testRestartTPS() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Assert.assertTrue(m_FPSManager.isTPSRunning(m_FPSName));
            m_FPSManager.restartTPS(m_FPSName);
            Thread.sleep(25000); //wait till server restarts - change if necessary
            //Assert.assertTrue(m_FPSManager.getIsTPSExist(m_FPSName));
            Assert.assertTrue(m_FPSManager.isTPSRunning(m_FPSName));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public void testShutDownTPS() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Assert.assertTrue(m_FPSManager.isTPSRunning(m_FPSName));
            m_FPSManager.shutdownTPS(m_FPSName);
            Thread.sleep(5000); //wait till server stops - change if necessary
            Assert.assertFalse(m_FPSManager.isTPSRunning(m_FPSName));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public static ArrayList getOrder(){
            ArrayList methodsOrder = new ArrayList();
            methodsOrder.add("testGetTPS");
            methodsOrder.add("testGetLogs");
            methodsOrder.add("testGetTraceInfo");
            methodsOrder.add("testSetTraceInfo");
            methodsOrder.add("testExportLogs");
            methodsOrder.add("testClearLogs");
            methodsOrder.add("testTPSURL");
            methodsOrder.add("testGetFPSSystemInfo");
            methodsOrder.add("testGetAllInstalledServices");
            methodsOrder.add("testGetTPSPerformanceStats");
            methodsOrder.add("testGetTPSAlertModules");
            methodsOrder.add("testSetTPSAlertModules");
            methodsOrder.add("testGetBackupURLs");
            methodsOrder.add("testRemoveInstalledService");
         //   methodsOrder.add("testRemoveServiceFromNetwork");
         //   methodsOrder.add("testRemoveAllInstalledServices");
         //   methodsOrder.add("testRestartTPS");
         //   methodsOrder.add("testShutDownTPS");

            return methodsOrder;
    }
}

