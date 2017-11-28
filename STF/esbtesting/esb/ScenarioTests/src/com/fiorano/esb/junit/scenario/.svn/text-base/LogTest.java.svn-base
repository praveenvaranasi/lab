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

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.TestCaseElement;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.common.TifosiException;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.*;
import java.util.Hashtable;
import java.util.*;
import java.util.logging.Level;

/**
 * @author Uday K
 * @date 26th December 2006
 */
public class LogTest extends DRTTestCase {

    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised=false;
    private String m_appFile;
    private float m_appversion;
    private String m_appGUID;
    private String m_servinstance1;
    private String m_servinstance2;
    private int m_nooflines;
    private String m_loglevel1;
    private String m_loglevel2;
    private String m_modulename1;
    private String m_modulename2;
    private String resourceFilePath;

    static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter


    public LogTest(String name) {
        super(name);
    }

    public LogTest(TestCaseElement testCaseConfig) {
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
    }//changed function

    public void setUp() throws Exception {
        super.setUp();
        if(!m_initialised){
            m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
            resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Log";


        // new changes
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
        startApplication();
    }

    private void init() throws Exception{
        m_appFile = resourceFilePath+File.separator+testCaseProperties.getProperty("ApplicationXML","1.0.xml");
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_appversion = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_servinstance1 = testCaseProperties.getProperty("ServiceInstance1");
        m_servinstance2 = testCaseProperties.getProperty("ServiceInstance2");
        m_nooflines = Integer.parseInt(testCaseProperties.getProperty("NoOfLines"));
        m_modulename1 = testCaseProperties.getProperty("ModuleName1");
        m_modulename2 = testCaseProperties.getProperty("ModuleName2");
        m_loglevel1 = testCaseProperties.getProperty("LogLevel1");
        m_loglevel2 = testCaseProperties.getProperty("LogLevel2");
        printInitParams();
    }

    private void printInitParams(){
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       "+m_appGUID+"  Version Number::"+m_appversion);
        System.out.println("Service Instances::      "+m_servinstance1+ " and "+m_servinstance2);
        System.out.println("No Of Lines::      "+m_nooflines);
        System.out.println("..................................................................");
    }



    public void tearDown() throws Exception {
        super.tearDown();
        if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
            m_fioranoApplicationController.killApplication(m_appGUID,m_appversion);
    }

    private void startApplication() throws Exception
    {
        try {
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.compileApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.prepareLaunch(m_appGUID,m_appversion);
            m_fioranoApplicationController.launchApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_appversion);
            Thread.sleep(5000);
        } catch (Exception e) {
            throw new Exception("Couldn't launch application :"+m_appGUID);
        }
    }

    public void testSetLogLevelForApplication() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            m_fioranoApplicationController.setLogLevel(m_appGUID,m_appversion,m_loglevel1);
            String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_appversion, m_servinstance1,m_modulename1);
            if(!loglevel.equalsIgnoreCase(m_loglevel1))
                    throw new Exception("Log Level not set@"+m_loglevel1+"@"+loglevel);

        }catch(Exception e){
            System.err.println("Exception in the Execution of test case::"+getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase "+getName()+": FAILED >> "+e.getMessage(),false);
        }
    }

    public void testSetLogLevelForServiceInstance() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
                m_fioranoApplicationController.setLogLevel(m_appGUID,m_appversion,m_servinstance1,m_loglevel2);
                String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_appversion, m_servinstance1,m_modulename1);

                if(!loglevel.equalsIgnoreCase(m_loglevel2))
                    throw new Exception("Log Level not set@"+m_loglevel2+"@"+loglevel);

        }catch(Exception e){
            System.err.println("Exception in the Execution of test case::"+getName());
            e.printStackTrace();
            Assert.assertTrue("TestCase "+getName()+": FAILED >> "+e.getMessage(),false);
        }
    }

    public void testSetLogLevelForModule() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetLogLevelForModule", "LogTest"));
                m_fioranoApplicationController.setLogLevel(m_appGUID,m_appversion, m_servinstance1, m_modulename2, m_loglevel1);
                String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_appversion, m_servinstance1, m_modulename2);
                if(!loglevel.equalsIgnoreCase(m_loglevel1))
                    throw new Exception("Log Level not set>>" + m_loglevel1 +"<<and>>"+loglevel+"<<");
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetLogLevelForModule", "LogTest"));
        }catch(Exception e){
            System.err.println("Exception in the Execution of test case::"+getName());
            e.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetLogLevelForModule", "LogTest"), e);
            Assert.assertTrue("TestCase "+getName()+": FAILED >> "+e.getMessage(),false);
        }
    }

    public void testSetLogLevelForModules() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
                DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testSetLogLevelForModules", "LogTest"));
                Hashtable m_hashtable = new Hashtable();
                m_hashtable.put(m_modulename1, m_loglevel1);
                m_hashtable.put(m_modulename2, m_loglevel2);

                m_fioranoApplicationController.setLogLevel(m_appGUID,m_appversion,m_servinstance2,m_hashtable);
                String[] m_modulenames = {m_modulename1, m_modulename2};
                Hashtable hashtable = m_fioranoApplicationController.getLogLevel(m_appGUID,m_appversion, m_servinstance2, m_modulenames);
                if(!( ((String)hashtable.get(m_modulename1)).equalsIgnoreCase(m_loglevel1) ||((String)hashtable.get(m_modulename2)).equalsIgnoreCase(m_loglevel2) ))
                    throw new Exception("Log Level not set");
                DRTTestLogger.log(DRTTestConstants.getFinishMessage("testSetLogLevelForModules", "LogTest"));
        }catch(Exception e){
            System.err.println("Exception in the Execution of test case::"+getName());
            e.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testSetLogLevelForModules", "LogTest"), e);
            Assert.assertTrue("TestCase "+getName()+": FAILED >> "+e.getMessage(),false);
        }
    }

    /**
     * Get Last Out Trace of a component in an application(running application or highest version)
     * m_nooflines indicates the no. of lines of logs to be fetched
     * @throws Exception
     */
    public void testGetLastOutTrace() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetLastOutTrace", "LogTest"));
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID);
            if( (outTrace1 == null)||(outTrace2 == null) )
                throw new Exception("Could not fetch Component Out Logs");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetLastOutTrace", "LogTest"));
        }
        catch( Exception e ){
             System.err.println("Exception in the Execution of test case::"+getName());
             e.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetLastOutTrace", "LogTest"), e);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+e.getMessage(),false);
        }

    }

    public void testGetLastErrTrace() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetLastErrTrace", "LogTest"));
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID);
            if( (errTrace1 == null)||(errTrace2 == null) )
                throw new Exception("Could not fetch Component Err Logs");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetLastErrTrace", "LogTest"));
         }
         catch( Exception e){
             System.err.println("Exception in the Execution of test case::"+getName());
             e.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetLastErrTrace", "LogTest"), e);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+e.getMessage(),false);
         }
    }

    public void testGetTESLogs() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetTESLogs", "LogTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String errLog = rtlClient.getFioranoServiceProvider().getTESLastErrLogs(1);
            if(errLog==null)
                Assert.assertNull(errLog);
            else
                Assert.assertNotNull(errLog);
            String outLog = rtlClient.getFioranoServiceProvider().getTESLastOutLogs(1);
            Assert.assertNotNull(outLog);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetTESLogs", "LogTest"));
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetTESLogs", "LogTest"), e);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + e.getMessage(), false);
        }
    }

     public void testClearTESLogs() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearTESLogs", "LogTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.getFioranoServiceProvider().clearTESOutLogs();
            rtlClient.getFioranoServiceProvider().clearTESErrLogs();
            String errLog = rtlClient.getFioranoServiceProvider().getTESLastErrLogs(1);
            Assert.assertNull(errLog);
            String outLog = rtlClient.getFioranoServiceProvider().getTESLastOutLogs(1);
            Assert.assertNotNull(outLog);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearTESLogs", "LogTest"));
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearTESLogs", "LogTest"), e);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + e.getMessage(), false);
        }
    }

    public void testFetchApplicationLog() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testFetchApplicationLog", "LogTest"));
            String zipDirPath =  resourceFilePath+File.separator+"zipapp";
            String tempDirPath = resourceFilePath+File.separator+"tempapp";
            File zipDir = new File(zipDirPath);
            File tempDir = new File(tempDirPath);
            zipDir.mkdirs();
            tempDir.mkdirs();
            String zipFilePath = zipDir.getAbsolutePath()+File.separator+"App.zip";
            m_fioranoApplicationController.fetchApplicationLog(m_appGUID,m_appversion,zipFilePath,tempDir.getAbsolutePath());
            System.out.println("the zip file is "+zipFilePath);
            File dir = new File(zipDirPath);
            if(dir.listFiles() == null){
                   deleteDir(zipDir.getAbsolutePath());
                   deleteDir(tempDir.getAbsolutePath());
                   throw new Exception("Could not export application Logs");
            }
            deleteDir(zipDir.getAbsolutePath());
            deleteDir(tempDir.getAbsolutePath());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testFetchApplicationLog", "LogTest"));
        }
        catch(Exception ex){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testFetchApplicationLog", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
        }
    }

    private void deleteDir(String DirPath) {
        File dir = new File(DirPath);
        File[] serviceInsts = dir.listFiles();
        for (int i=0; i<serviceInsts.length; i++) {
            if (serviceInsts[i].isDirectory())
                deleteDir(serviceInsts[i].getAbsolutePath());
            else
                serviceInsts[i].delete();
        }
        dir.delete();
    }

    public void testFetchApplicationLogOfService() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testFetchApplicationLog", "LogTest"));
            String zipDirPath =  resourceFilePath+File.separator+"zipserv";
            String tempDirPath = resourceFilePath+File.separator+"tempserv";
            File zipDir = new File(zipDirPath);
            File tempDir = new File(tempDirPath);
            zipDir.mkdirs();
            tempDir.mkdirs();
            String zipFilePath = zipDir.getAbsolutePath()+File.separator+"Serv.zip";
            m_fioranoApplicationController.fetchApplicationLogOfService(m_appGUID, m_appversion, m_servinstance1, zipFilePath, tempDir.getAbsolutePath());
            File dir = new File(zipDirPath);
            if(dir.listFiles() == null){
                   deleteDir(zipDir.getAbsolutePath());
                   deleteDir(tempDir.getAbsolutePath());
                   throw new Exception("Could not export application Logs");
            }
            deleteDir(zipDir.getAbsolutePath());
            deleteDir(tempDir.getAbsolutePath());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testFetchApplicationLog", "LogTest"));
        }
        catch(Exception ex){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testFetchApplicationLog", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
        }
   }

    public void testClearServiceOutLogs() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearServiceOutLogs", "LogTest"));
            m_fioranoApplicationController.clearServiceOutLogs(m_servinstance1, m_appGUID);
            m_fioranoApplicationController.clearServiceOutLogs(m_servinstance2, m_appGUID);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID);
            if( (outTrace1.length() != 0)||(outTrace2.length() != 0) )
                throw new Exception("Could not Clear Component Out Logs");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearServiceOutLogs", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearServiceOutLogs", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
        }
    }

    public void testClearServiceErrLogs() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearServiceErrLogs", "LogTest"));
            m_fioranoApplicationController.clearServiceErrLogs(m_servinstance1, m_appGUID);
            m_fioranoApplicationController.clearServiceErrLogs(m_servinstance2, m_appGUID);
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID);
            if( (errTrace1.length() != 0)||(errTrace2.length() != 0) )
                throw new Exception("Could not Clear Component Err Logs");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearServiceErrLogs", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearServiceErrLogs", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
        }
    }

    public void testClearApplicationLogs() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearApplicationLogs", "LogTest"));
//            m_fioranoApplicationController.killApplication(m_appGUID);
//            m_fioranoApplicationController.launchApplication(m_appGUID,m_appversion);
//            m_fioranoApplicationController.startAllServices(m_appGUID);
//            Thread.sleep(5000);
            m_fioranoApplicationController.killApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.clearApplicationLogs(m_appGUID);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID);
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID);
            if( (outTrace1.length() != 0)||(outTrace2.length() != 0)||(errTrace1.length() != 0)||(errTrace2.length() != 0) )
                throw new Exception("Could not Clear Application Logs with appversion");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearApplicationLogs", "LogTest"));
         }
         catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearApplicationLogs", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
        }
   }

    public void testGetLastOutTraceWithVersion() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetLastOutTraceWithVersion", "LogTest"));
//            m_fioranoApplicationController.launchApplication(m_appGUID,m_appversion);
//            m_fioranoApplicationController.startAllServices(m_appGUID);
//            Thread.sleep(5000);
            m_fioranoApplicationController.killApplication(m_appGUID,m_appversion);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            if( (outTrace1 == null)||(outTrace2 == null) )
                throw new Exception("Could not fetch Component Out Logs with appversion");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetLastOutTraceWithVersion", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetLastOutTraceWithVersion", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
        }
    }

    public void testGetLastErrTraceWithVersion() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetLastErrTraceWithVersion", "LogTest"));
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID,m_appversion);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID,m_appversion);
            if( (errTrace1 == null)||(errTrace2 == null) )
                throw new Exception("Could not fetch Component Err Logs with appversion");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetLastErrTraceWithVersion", "LogTest"));
         }
         catch( Exception ex){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetLastErrTraceWithVersion", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
         }
    }

    public void testClearServiceOutLogsWithVersion() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearServiceOutLogsWithVersion", "LogTest"));
            m_fioranoApplicationController.clearServiceOutLogs(m_servinstance1, m_appGUID, m_appversion);
            m_fioranoApplicationController.clearServiceOutLogs(m_servinstance2, m_appGUID, m_appversion);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            if( (outTrace1.length() != 0)||(outTrace2.length() != 0) )
                throw new Exception("Could not Clear Component Out Logs with appversion");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearServiceOutLogsWithVersion", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearServiceOutLogsWithVersion", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
        }
    }

    public void testClearServiceErrLogsWithVersion() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
           DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearServiceErrLogsWithVersion", "LogTest"));
            m_fioranoApplicationController.clearServiceErrLogs(m_servinstance1, m_appGUID, m_appversion);
            m_fioranoApplicationController.clearServiceErrLogs(m_servinstance2, m_appGUID, m_appversion);
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            if( (errTrace1.length() != 0)||(errTrace2.length() != 0) )
                throw new Exception("Could not Clear Component Err Logs with appversion");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearServiceErrLogsWithVersion", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearServiceErrLogsWithVersion", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
        }
     }

    /**
     * Clear logs of an application(of a particular version)
     * ie. Logs of all the components in that application
     * @throws Exception
     */
    public void testClearApplicationLogsWithVersion() throws Exception{
         System.out.println("Started the Execution of the TestCase::"+getName());
         try{
           DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearApplicationLogsWithVersion", "LogTest"));
//            m_fioranoApplicationController.launchApplication(m_appGUID,m_appversion);
//            m_fioranoApplicationController.startAllServices(m_appGUID);
//            Thread.sleep(5000);
            m_fioranoApplicationController.killApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.clearApplicationLogs(m_appGUID, m_appversion);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            m_fioranoApplicationController.deleteApplication(m_appGUID,m_appversion);
            if( (outTrace1.length() != 0)||(outTrace2.length() != 0)||(errTrace1.length() != 0)||(errTrace2.length() != 0) )
                throw new Exception("Could not Clear Application Logs with appversion");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearApplicationLogsWithVersion", "LogTest"));
         }
         catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearApplicationLogsWithVersion", "LogTest"), ex);
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);
        }
     }

    public static Test suite(){
        return new TestSuite(LogTest.class);
    }

    public static ArrayList getOrder(){

        ArrayList methodsOrder = new ArrayList();

//        methodsOrder.add("testSetLogLevelForApplication");    // deprecated
//        methodsOrder.add("testSetLogLevelForServiceInstance");    // deprecated
        methodsOrder.add("testSetLogLevelForModule");
        methodsOrder.add("testSetLogLevelForModules");
        methodsOrder.add("testGetLastOutTrace");
        methodsOrder.add("testGetLastErrTrace");
        methodsOrder.add("testGetTESLogs");
        methodsOrder.add("testClearTESLogs");
        methodsOrder.add("testFetchApplicationLog");
        methodsOrder.add("testFetchApplicationLogOfService");
        methodsOrder.add("testClearServiceOutLogs");
        methodsOrder.add("testClearServiceErrLogs");
        methodsOrder.add("testClearApplicationLogs");
        methodsOrder.add("testGetLastOutTraceWithVersion");
        methodsOrder.add("testGetLastErrTraceWithVersion");
        methodsOrder.add("testClearServiceOutLogsWithVersion");
        methodsOrder.add("testClearServiceErrLogsWithVersion");
        methodsOrder.add("testClearApplicationLogsWithVersion");

        return methodsOrder;
    }
}
