package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.ExceptionListener;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.rtl.dm.FioranoDeploymentManager;
import com.fiorano.esb.rtl.events.FioranoEventsManager;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.rtl.sbw.FioranoSBWManager;
import com.fiorano.esb.rtl.security.FioranoSecurityManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.SystemInfo;
import fiorano.tifosi.dmi.TESPerformanceStats;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/21/11
 * Time: 6:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceProvider extends AbstractTestNG{
    private String resourceFilePath;
    private boolean m_initialised;

     static boolean isModifiedOnceHA=false;//to check the files overwriting
    static boolean isModifiedOnce=false;
    private FilenameFilter xmlFilter;//file extracter

    private void printInitParams() {
        System.out.println("_______________________The Initialization Parameters_______________________");
        System.out.println("______________________________No parameters________________________________");
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

    @BeforeClass(groups = "ServiceProviderTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "ServiceProvider" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "ServiceProvider";
        ServerStatusController stc;
        stc=ServerStatusController .getInstance();
        boolean isFPSHA=stc.getFPSHA();
        setXmlFilter("xml");
        try {
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
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetUp", "TestServiceProvider"));
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        m_initialised = true;
        printInitParams();
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testGetManagers() throws Exception{
        try
        {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetManagers", "ServiceProviderTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetManagers", "ServiceProviderTest"));
        }
        catch(Exception ex)
        {
            System.err.println("Exception in the Execution of test case::"+getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetManagers", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of "+ex.getMessage());

        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testGetManagers")
    public void testServerDetails() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testServerDetails", "ServiceProviderTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testServerDetails", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testServerDetails", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testServerDetails")
    public void testGetHandle() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetHandle", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            String handle = rtlClient.getHandleID();
            Assert.assertNotNull(handle);
            System.out.println("Server Handle is::" + handle);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetHandle", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetHandle", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testGetHandle")
    public void testLoadPropertyBundles() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testloadPropertyBundles", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            rtlClient.getFioranoServiceProvider().loadPropertyBundles();
            System.out.println("Loaded property bundle");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testloadPropertyBundles", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testloadPropertyBundles", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testLoadPropertyBundles")
    public void testIsConnectionClosed() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testIsConnectionClosed", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Assert.assertFalse(rtlClient.getFioranoServiceProvider().isConnectionCloseCalled());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testIsConnectionClosed", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testIsConnectionClosed", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testIsConnectionClosed")
    public void testGetUserPermissions() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetUserPermissions", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Vector permissionList = rtlClient.getFioranoServiceProvider().getUserPermissions();
            for(int i=0; i<permissionList.size(); i++)
                System.out.println(permissionList.get(i).toString());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetUserPermissions", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetUserPermissions", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testGetUserPermissions")
    public void testGetTESPerformanceStats() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetTESPerformanceStats", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            TESPerformanceStats tesps = rtlClient.getFioranoServiceProvider().getTESPerformanceStats();
            Assert.assertNotNull(tesps);
            System.out.println("Free Memory::"+tesps.getFreeMemory());
            System.out.println("Memory Usage::"+tesps.getMemoryUsage());
            System.out.println("Total Memory::"+tesps.getTotalMemory());
            System.out.println("Process count::"+tesps.getTotalProcessCount());
            System.out.println("Thread count::"+tesps.getTotalThreadCount());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetTESPerformanceStats", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTESPerformanceStats", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testGetTESPerformanceStats")
    public void testGetTESSystemInfo() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetTESSystemInfo", "ServiceProviderTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetTESSystemInfo", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetTESSystemInfo", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testGetTESSystemInfo")
    public void testSetTimeoutForServerCalls() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetTimeoutForServerCalls", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            rtlClient.getFioranoServiceProvider().setTimeoutForServerCalls(10000);
            System.out.println("Timeout set to 10000");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetTimeoutForServerCalls", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetTimeoutForServerCalls", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testSetTimeoutForServerCalls")
    public void testSetExceptionListener() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testSetExceptionListener", "ServiceProviderTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

             rtlClient.getFioranoServiceProvider().setExceptionListener(new ExceptionListener() {
                public void onException(Throwable thr) throws TifosiException {
                }
            });

            ExceptionListener exListener = rtlClient.getFioranoServiceProvider().getExceptionListener();
            Assert.assertNotNull(exListener);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testSetExceptionListener", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testSetExceptionListener", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "ServiceProviderTest", alwaysRun = true, dependsOnMethods = "testSetExceptionListener")
     public void testJMSStats() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testJMSStats", "ServiceProviderTest"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testJMSStats", "ServiceProviderTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testJMSStats", "ServiceProviderTest"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

}
