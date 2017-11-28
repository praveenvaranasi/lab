package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/20/11
 * Time: 5:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestLog extends AbstractTestNG{
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

    private void init() throws Exception{
        m_appFile = resourceFilePath+ fsc+testProperties.getProperty("ApplicationXML","1.0.xml");
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_appversion = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_servinstance1 = testProperties.getProperty("ServiceInstance1");
        m_servinstance2 = testProperties.getProperty("ServiceInstance2");
        m_nooflines = Integer.parseInt(testProperties.getProperty("NoOfLines"));
        m_modulename1 = testProperties.getProperty("ModuleName1");
        m_modulename2 = testProperties.getProperty("ModuleName2");
        m_loglevel1 = testProperties.getProperty("LogLevel1");
        m_loglevel2 = testProperties.getProperty("LogLevel2");
    }

    private void printInitParams(){
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       "+m_appGUID+"  Version Number::"+m_appversion);
        System.out.println("Service Instances::      "+m_servinstance1+ " and "+m_servinstance2);
        System.out.println("No Of Lines::      "+m_nooflines);
        System.out.println("..................................................................");
    }

    // the function modifies the xml files and replace any "search" string with "replace" string
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

    //picking up the xml extension files
    private void setXmlFilter(String ext){
        xmlFilter = new OnlyExt(ext);//creating filters
    }

    @BeforeClass(groups = "LogTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "Log" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Log";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        //new change
        ServerStatusController stc;
        stc = ServerStatusController.getInstance();
        boolean isFPSHA = stc.getFPSHA();
        setXmlFilter("xml");
        try {
            if (isFPSHA && !isModifiedOnceHA) {
                isModifiedOnceHA = true;
                modifyXmlFiles(resourceFilePath, "fps", "fps_ha");
            }
            else if (!isFPSHA && !isModifiedOnce) {
                isModifiedOnce = true;
                modifyXmlFiles(resourceFilePath, "fps_ha", "fps");
            }
        init();
        startApplication();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            Assert.fail("could not initialise properties");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "startSetUp")
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
            Assert.assertTrue(false, "TestCase "+getName()+": FAILED >> "+e.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testSetLogLevelForApplication")
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
            Assert.assertTrue(false, "TestCase "+getName()+": FAILED >> "+e.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testSetLogLevelForServiceInstance")
    public void testSetLogLevelForModule() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testSetLogLevelForModule", "LogTest"));
                m_fioranoApplicationController.setLogLevel(m_appGUID,m_appversion, m_servinstance1, m_modulename2, m_loglevel1);
                String loglevel = m_fioranoApplicationController.getLogLevel(m_appGUID,m_appversion, m_servinstance1, m_modulename2);
                if(!loglevel.equalsIgnoreCase(m_loglevel1))
                    throw new Exception("Log Level not set>>" + m_loglevel1 +"<<and>>"+loglevel+"<<");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testSetLogLevelForModule", "LogTest"));
        }catch(Exception e){
            System.err.println("Exception in the Execution of test case::"+getName());
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetLogLevelForModule", "LogTest"), e);
            Assert.assertTrue(false, "TestCase "+getName()+": FAILED >> "+e.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testSetLogLevelForModule")
    public void testSetLogLevelForModules() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
                Logger.LogMethodOrder(Logger.getOrderMessage("testSetLogLevelForModules", "LogTest"));
                Hashtable m_hashtable = new Hashtable();
                m_hashtable.put(m_modulename1, m_loglevel1);
                m_hashtable.put(m_modulename2, m_loglevel2);

                m_fioranoApplicationController.setLogLevel(m_appGUID,m_appversion,m_servinstance2,m_hashtable);
                String[] m_modulenames = {m_modulename1, m_modulename2};
                Hashtable hashtable = m_fioranoApplicationController.getLogLevel(m_appGUID,m_appversion, m_servinstance2, m_modulenames);
                if(!( ((String)hashtable.get(m_modulename1)).equalsIgnoreCase(m_loglevel1) ||((String)hashtable.get(m_modulename2)).equalsIgnoreCase(m_loglevel2) ))
                    throw new Exception("Log Level not set");
                Logger.Log(Level.INFO, Logger.getFinishMessage("testSetLogLevelForModules", "LogTest"));
        }catch(Exception e){
            System.err.println("Exception in the Execution of test case::"+getName());
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetLogLevelForModules", "LogTest"), e);
            Assert.assertTrue(false, "TestCase "+getName()+": FAILED >> "+e.getMessage());
        }
    }
    /**
     * Get Last Out Trace of a component in an application(running application or highest version)
     * m_nooflines indicates the no. of lines of logs to be fetched
     * @throws Exception
     */
    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testSetLogLevelForModules")
    public void testGetLastOutTrace() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetLastOutTrace", "LogTest"));
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            if( (outTrace1 == null)||(outTrace2 == null) )
                throw new Exception("Could not fetch Component Out Logs");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetLastOutTrace", "LogTest"));
        }
        catch( Exception e ){
             System.err.println("Exception in the Execution of test case::"+getName());
             e.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetLastOutTrace", "LogTest"), e);
             Assert.assertTrue(false, "TestCase "+getName()+": FAILED >> "+e.getMessage());
        }

    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testGetLastOutTrace")
    public void testGetLastErrTrace() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetLastErrTrace", "LogTest"));
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            if( (errTrace1 == null)||(errTrace2 == null) )
                throw new Exception("Could not fetch Component Err Logs");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetLastErrTrace", "LogTest"));
         }
         catch( Exception e){
             System.err.println("Exception in the Execution of test case::"+getName());
             e.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetLastErrTrace", "LogTest"), e);
             Assert.assertTrue(false, "TestCase "+getName()+": FAILED >> "+e.getMessage());
         }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testGetLastErrTrace")
    public void testGetTESLogs() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetTESLogs", "LogTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            String errLog = rtlClient.getFioranoServiceProvider().getTESLastErrLogs(1);
            if(errLog==null)
                Assert.assertNull(errLog);
            else
                Assert.assertNotNull(errLog);
            String outLog = rtlClient.getFioranoServiceProvider().getTESLastOutLogs(1);
            Assert.assertNotNull(outLog);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetTESLogs", "LogTest"));
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTESLogs", "LogTest"), e);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + e.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testGetTESLogs")
    public void testClearTESLogs() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearTESLogs", "LogTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            rtlClient.getFioranoServiceProvider().clearTESOutLogs();
            rtlClient.getFioranoServiceProvider().clearTESErrLogs();
            String errLog = rtlClient.getFioranoServiceProvider().getTESLastErrLogs(1);
            Assert.assertNull(errLog);
            String outLog = rtlClient.getFioranoServiceProvider().getTESLastOutLogs(1);
            Assert.assertNotNull(outLog);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearTESLogs", "LogTest"));
        }
        catch (Exception e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearTESLogs", "LogTest"), e);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + e.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testClearTESLogs")
    public void testFetchApplicationLog() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testFetchApplicationLog", "LogTest"));
            String zipDirPath =  resourceFilePath+fsc+"zipapp";
            String tempDirPath = resourceFilePath+fsc+"tempapp";
            File zipDir = new File(zipDirPath);
            File tempDir = new File(tempDirPath);
            zipDir.mkdirs();
            tempDir.mkdirs();
            String zipFilePath = zipDir.getAbsolutePath()+fsc+"App.zip";
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testFetchApplicationLog", "LogTest"));
        }
        catch(Exception ex){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testFetchApplicationLog", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testFetchApplicationLog")
    public void testFetchApplicationLogOfService() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testFetchApplicationLog", "LogTest"));
            String zipDirPath =  resourceFilePath+fsc+"zipserv";
            String tempDirPath = resourceFilePath+fsc+"tempserv";
            File zipDir = new File(zipDirPath);
            File tempDir = new File(tempDirPath);
            zipDir.mkdirs();
            tempDir.mkdirs();
            String zipFilePath = zipDir.getAbsolutePath()+fsc+"Serv.zip";
            m_fioranoApplicationController.fetchApplicationLogOfService(m_appGUID, m_appversion, m_servinstance1, zipFilePath, tempDir.getAbsolutePath());
            File dir = new File(zipDirPath);
            if(dir.listFiles() == null){
                   deleteDir(zipDir.getAbsolutePath());
                   deleteDir(tempDir.getAbsolutePath());
                   throw new Exception("Could not export application Logs");
            }
            deleteDir(zipDir.getAbsolutePath());
            deleteDir(tempDir.getAbsolutePath());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testFetchApplicationLog", "LogTest"));
        }
        catch(Exception ex){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testFetchApplicationLog", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
   }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testFetchApplicationLogOfService")
    public void testClearServiceOutLogs() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearServiceOutLogs", "LogTest"));
            m_fioranoApplicationController.clearServiceOutLogs(m_servinstance1, m_appGUID, m_appversion);
            m_fioranoApplicationController.clearServiceOutLogs(m_servinstance2, m_appGUID, m_appversion);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID,m_appversion);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID,m_appversion);
            if( (outTrace1.length() != 0)||(outTrace2.length() != 0) )
                throw new Exception("Could not Clear Component Out Logs");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearServiceOutLogs", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearServiceOutLogs", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testClearServiceOutLogs")
    public void testClearServiceErrLogs() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearServiceErrLogs", "LogTest"));
            m_fioranoApplicationController.clearServiceErrLogs(m_servinstance1, m_appGUID, m_appversion);
            m_fioranoApplicationController.clearServiceErrLogs(m_servinstance2, m_appGUID, m_appversion);
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID,m_appversion);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID,m_appversion);
            if( (errTrace1.length() != 0)||(errTrace2.length() != 0) )
                throw new Exception("Could not Clear Component Err Logs");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearServiceErrLogs", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearServiceErrLogs", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testClearServiceErrLogs")
    public void testClearApplicationLogs() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearApplicationLogs", "LogTest"));
//            m_fioranoApplicationController.killApplication(m_appGUID);
//            m_fioranoApplicationController.launchApplication(m_appGUID,m_appversion);
//            m_fioranoApplicationController.startAllServices(m_appGUID);
//            Thread.sleep(5000);
            m_fioranoApplicationController.killApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.clearApplicationLogs(m_appGUID,m_appversion);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID,m_appversion);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID,m_appversion);
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID,m_appversion);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID,m_appversion);
            if( (outTrace1.length() != 0)||(outTrace2.length() != 0)||(errTrace1.length() != 0)||(errTrace2.length() != 0) )
                throw new Exception("Could not Clear Application Logs with appversion");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearApplicationLogs", "LogTest"));
         }
         catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearApplicationLogs", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
   }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testClearApplicationLogs")
    public void testGetLastOutTraceWithVersion() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetLastOutTraceWithVersion", "LogTest"));
            m_fioranoApplicationController.launchApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_appversion);
            Thread.sleep(5000);
            m_fioranoApplicationController.killApplication(m_appGUID,m_appversion);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            if( (outTrace1 == null)||(outTrace2 == null) )
                throw new Exception("Could not fetch Component Out Logs with appversion");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetLastOutTraceWithVersion", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetLastOutTraceWithVersion", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testGetLastOutTraceWithVersion")
    public void testGetLastErrTraceWithVersion() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetLastErrTraceWithVersion", "LogTest"));
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            if( (errTrace1 == null)||(errTrace2 == null) )
                throw new Exception("Could not fetch Component Err Logs with appversion");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetLastErrTraceWithVersion", "LogTest"));
         }
         catch( Exception ex){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetLastErrTraceWithVersion", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
         }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testGetLastErrTraceWithVersion")
    public void testClearServiceOutLogsWithVersion() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearServiceOutLogsWithVersion", "LogTest"));
            m_fioranoApplicationController.clearServiceOutLogs(m_servinstance1, m_appGUID, m_appversion);
            m_fioranoApplicationController.clearServiceOutLogs(m_servinstance2, m_appGUID, m_appversion);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            if( (outTrace1.length() != 0)||(outTrace2.length() != 0) )
                throw new Exception("Could not Clear Component Out Logs with appversion");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearServiceOutLogsWithVersion", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearServiceOutLogsWithVersion", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
    }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testClearServiceOutLogsWithVersion")
    public void testClearServiceErrLogsWithVersion() throws Exception{
        System.out.println("Started the Execution of the TestCase::"+getName());
        try{
           Logger.LogMethodOrder(Logger.getOrderMessage("testClearServiceErrLogsWithVersion", "LogTest"));
            m_fioranoApplicationController.clearServiceErrLogs(m_servinstance1, m_appGUID, m_appversion);
            m_fioranoApplicationController.clearServiceErrLogs(m_servinstance2, m_appGUID, m_appversion);
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            if( (errTrace1.length() != 0)||(errTrace2.length() != 0) )
                throw new Exception("Could not Clear Component Err Logs with appversion");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearServiceErrLogsWithVersion", "LogTest"));
        }
        catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearServiceErrLogsWithVersion", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
     }

    /**
     * Clear logs of an application(of a particular version)
     * ie. Logs of all the components in that application
     * @throws Exception
     */
    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testClearServiceErrLogsWithVersion")
    public void testClearApplicationLogsWithVersion() throws Exception{
         System.out.println("Started the Execution of the TestCase::"+getName());
         try{
           Logger.LogMethodOrder(Logger.getOrderMessage("testClearApplicationLogsWithVersion", "LogTest"));
            m_fioranoApplicationController.launchApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_appversion);
            Thread.sleep(5000);
            m_fioranoApplicationController.killApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.clearApplicationLogs(m_appGUID, m_appversion);
            String outTrace1 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String outTrace2 = m_fioranoApplicationController.getLastOutTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            String errTrace1 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance1, m_appGUID, m_appversion);
            String errTrace2 = m_fioranoApplicationController.getLastErrorTrace(m_nooflines, m_servinstance2, m_appGUID, m_appversion);
            m_fioranoApplicationController.deleteApplication(m_appGUID,m_appversion);
            if( (outTrace1.length() != 0)||(outTrace2.length() != 0)||(errTrace1.length() != 0)||(errTrace2.length() != 0) )
                throw new Exception("Could not Clear Application Logs with appversion");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearApplicationLogsWithVersion", "LogTest"));
         }
         catch( Exception ex ){
             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearApplicationLogsWithVersion", "LogTest"), ex);
             Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());
        }
     }

    @Test(groups = "LogTest", alwaysRun = true, dependsOnMethods = "testClearApplicationLogsWithVersion")
    public void tearDown() throws Exception {
        if(m_fioranoApplicationController.isApplicationRunning(m_appGUID))
            m_fioranoApplicationController.killApplication(m_appGUID,m_appversion);
    }

    private void startApplication() throws Exception{
        try {
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.compileApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.prepareLaunch(m_appGUID,m_appversion);
            m_fioranoApplicationController.launchApplication(m_appGUID,m_appversion);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_appversion);
            Thread.sleep(5000);
        } catch (Exception e) {
            throw new Exception("Couldn't launch application :"+m_appGUID+m_appversion);
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
}
