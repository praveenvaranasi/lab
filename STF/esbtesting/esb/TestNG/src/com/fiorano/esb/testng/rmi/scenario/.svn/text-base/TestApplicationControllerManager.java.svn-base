package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.application.controller.ApplicationControllerManager;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.dmi.ServiceStatus;
import fiorano.tifosi.dmi.ServicesStatusPacket;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.aps.ApplicationStateDetails;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/18/11
 * Time: 5:27 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestApplicationControllerManager extends AbstractTestNG{
    private FioranoFPSManager m_fioranoFPSManager;
    private FioranoApplicationController m_fioranoApplicationController;
    private ApplicationControllerManager manager;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String m_servinstance1;
    private String m_servinstance2;
    private String m_route1;
    private String m_route2;
    private String resourceFilePath;
    boolean isModifiedOnceHA = false;//to check the files overwriting
    boolean isModifiedOnce = false;
    private FilenameFilter xmlFilter;//file extracter
    private String FPSPEER = "fps_test";

    public void init() throws Exception {
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationXML", "1.0.xml");
        m_servinstance1 = testProperties.getProperty("ServiceInstance1");
        m_servinstance2 = testProperties.getProperty("ServiceInstance2");
        m_route1 = testProperties.getProperty("RouteInstance1");
        m_route2 = testProperties.getProperty("RouteInstance1");
    }

    protected void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Service Instances::      " + m_servinstance1 + " and " + m_servinstance2);
        System.out.println("..................................................................");
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

    @BeforeClass(groups = "ApplicationControllerManagerTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "ApplicationControllerManager" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc +"tests" +  fsc + "scenario" + fsc + "ApplicationControllerManager";
        m_fioranoFPSManager = rtlClient.getFioranoFPSManager();
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
        }
        catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "ApplicationControllerManagerTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testCreateApplication() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);
            m_fioranoApplicationController.launchApplication(m_appGUID, m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
                                                                                                                                     
    @Test(groups = "ApplicationControllerManagerTest", alwaysRun = true, dependsOnMethods = "testCreateApplication")
    public void testClearErrLogsForRoute() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            List list = m_fioranoApplicationController.getRoutesOfApplication(m_appGUID, m_version);
            Assert.assertTrue(list.size() > 0);
            Iterator iter = list.iterator();
            System.out.println("Printing the " + m_appGUID + " Routes list.....");
            System.out.println("..............................................................");
            while (iter.hasNext()) {
                System.out.println(iter.next());
            }
            m_fioranoApplicationController.clearErrLogsForRoute(m_route1, m_appGUID, m_version);
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
                                                                                                                                     
    @Test(groups = "ApplicationControllerManagerTest", alwaysRun = true, dependsOnMethods = "testClearErrLogsForRoute")
    public void testClearOutLogsForRoute() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            List list = m_fioranoApplicationController.getRoutesOfApplication(m_appGUID, m_version);
            Assert.assertTrue(list.size() > 0);
            Iterator iter = list.iterator();
            System.out.println("Printing the " + m_appGUID + " Routes list.....");
            System.out.println("..............................................................");
            while (iter.hasNext()) {
                System.out.println(iter.next());
            }
            m_fioranoApplicationController.clearOutLogsForRoute(m_route1, m_appGUID, m_version);
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
                                                                                                                                     
    @Test(groups = "ApplicationControllerManagerTest", alwaysRun = true, dependsOnMethods = "testClearOutLogsForRoute")
    public void testGetLastErrorTraceForRoute() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            String trace = m_fioranoApplicationController.getLastErrorTraceForRoute(5, m_route1, m_appGUID, m_version);
            System.out.println(trace);
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
                                                                                                                                     
    @Test(groups = "ApplicationControllerManagerTest", alwaysRun = true, dependsOnMethods = "testGetLastErrorTraceForRoute")
    public void testGetLastOutTraceForRoute() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            String trace = m_fioranoApplicationController.getLastOutTraceForRoute(5, m_route1, m_appGUID, m_version);
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
                                                                                                                                     
    @Test(groups = "ApplicationControllerManagerTest", alwaysRun = true, dependsOnMethods = "testGetLastOutTraceForRoute")
    public void testGetNodeNameForService() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));
   try {
       System.out.println("Started the Execution of the TestCase::" + getName());
       String nodename = m_fioranoApplicationController.getNodeNameForService(m_appGUID,m_version, m_servinstance1);
       System.out.println("The node name for application " + m_appGUID+ " is "+nodename);
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
   }
   catch (Exception ex) {
       System.err.println("Exception in the Execution of test case::" + getName());
       ex.printStackTrace();
       Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
   }
}
                                                                                                                                     
    @Test(groups = "ApplicationControllerManagerTest", alwaysRun = true, dependsOnMethods = "testGetNodeNameForService")
    public void testGetServicesCurrentStatus() throws Exception{
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));
       System.out.println("Started the Execution of the TestCase::" + getName());
       try{
           ServicesStatusPacket servpacket = new ServicesStatusPacket();
           ServiceStatus srviceStatus = new ServiceStatus();
           srviceStatus.setAppInstName(m_appGUID);
           srviceStatus.setServInstName(m_servinstance1);
           srviceStatus.setNodeName("fps_test");
           servpacket.addServiceStatus(srviceStatus);
           Vector servstatus = servpacket.getServiceStates();
           Assert.assertNotNull(servstatus);
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
       }
       catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
   }
                                                                                                                                     
    @Test(groups = "ApplicationControllerManagerTest", alwaysRun = true, dependsOnMethods = "testGetServicesCurrentStatus")
    public void testGetUniqueRunningID() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));
      try {
          System.out.println("Started the Execution of the TestCase::" + getName());
          ApplicationStateDetails appstateDetails = m_fioranoApplicationController.getCurrentStateOfApplication(m_appGUID,m_version);
          ServiceInstanceStateDetails servdetails = appstateDetails.getServiceStatus(m_servinstance1);
          String uniquerunningID = servdetails.getUniqueRunningInstID();
          System.out.println("The unique running ID is "+uniquerunningID);
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
      }
      catch (Exception ex) {
          System.err.println("Exception in the Execution of test case::" + getName());
          ex.printStackTrace();
          Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
      }
   }
                                                                                                                                     
    @Test(groups = "ApplicationControllerManagerTest", alwaysRun = true, dependsOnMethods = "testGetUniqueRunningID")
    public void testSetTrackedDataType() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));

            try {
                System.out.println("Started the Execution of the TestCase::" + getName());
                String portName = "OUT_PORT";
                m_fioranoApplicationController.setTrackedDataType(m_servinstance1,m_appGUID,m_version,portName,1);
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
            }
            catch (Exception ex) {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
            }
         }
                                                                                                                                     
    @AfterClass(groups = "ApplicationControllerManagerTest", alwaysRun = true)
    public void testCleanup() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage(getName(),"TestApplicationControllerManager"));
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            m_fioranoApplicationController.close();
        Logger.LogMethodOrder(Logger.getFinishMessage(getName(),"TestApplicationControllerManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
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
