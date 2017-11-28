package com.fiorano.esb.junit.scenario;

import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.application.controller.handler.ApplicationHandle;
import com.fiorano.esb.application.controller.ApplicationControllerMBean;
import com.fiorano.esb.application.controller.ApplicationControllerManager;


import java.io.*;
import java.util.*;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.ApplicationReference;
import fiorano.tifosi.dmi.application.ServiceInstance;
import fiorano.tifosi.dmi.aps.ApplicationStateDetails;
import fiorano.tifosi.dmi.aps.ServiceInstanceStateDetails;
import fiorano.tifosi.dmi.ServicesStatusPacket;
import fiorano.tifosi.dmi.ServiceStatus;


/**
 * Created by IntelliJ IDEA.
 * User: Nagesh V H
 * Date: Aug 15, 2008
 * Time: 12:42:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class ApplicationControllerManagerTest extends DRTTestCase {
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

    public ApplicationControllerManagerTest(String name) {
        super(name);
    }


    public ApplicationControllerManagerTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    private void setXmlFilter(String ext)//picking up the xml extension files
    {
        xmlFilter = new OnlyExt(ext);//creating filters

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
    //changed function


    public void setUp() throws Exception {
        super.setUp();
        m_fioranoFPSManager = rtlClient.getFioranoFPSManager();
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();

        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "ApplicationControllerManager";

        //new change
        ServerStatusController stc;
        stc = ServerStatusController.getInstance();
        boolean isFPSHA = stc.getFPSHA();
        setXmlFilter("xml");
        if (isFPSHA && !isModifiedOnceHA) {
            isModifiedOnceHA = true;

            modifyXmlFiles(resourceFilePath, "fps_test", "fps_ha");

        } else if (!isFPSHA && !isModifiedOnce) {
            isModifiedOnce = true;
            modifyXmlFiles(resourceFilePath, "fps_ha", "fps_test");

        }

        init();
    }

    public void init() {
        m_appGUID = testCaseProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testCaseProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + File.separator + testCaseProperties.getProperty("ApplicationXML", "1.0.xml");
        m_servinstance1 = testCaseProperties.getProperty("ServiceInstance1");
        m_servinstance2 = testCaseProperties.getProperty("ServiceInstance2");
        m_route1 = testCaseProperties.getProperty("RouteInstance1");
        m_route2 = testCaseProperties.getProperty("RouteInstance1");
        printInitParams();
    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        System.out.println("Service Instances::      " + m_servinstance1 + " and " + m_servinstance2);
        System.out.println("..................................................................");
    }


    public static Test suite() {
        return new TestSuite(FPSControllerTest.class);
    }

    public void testCreateApplication() throws Exception {

        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.compileApplication(m_appGUID, m_version);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);
            m_fioranoApplicationController.launchApplication(m_appGUID, m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testClearErrLogsForRoute() throws Exception {

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


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testClearOutLogsForRoute() throws Exception {

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


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testGetLastErrorTraceForRoute() throws Exception {

        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            String trace = m_fioranoApplicationController.getLastErrorTraceForRoute(5, m_route1, m_appGUID, m_version);

            System.out.println(trace);

        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testGetLastOutTraceForRoute() throws Exception {

        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            String trace = m_fioranoApplicationController.getLastOutTraceForRoute(5, m_route1, m_appGUID, m_version);


        }

        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }


    }

    public void testgetNodeNameForService() throws Exception {

   try {
       System.out.println("Started the Execution of the TestCase::" + getName());

       String nodename = m_fioranoApplicationController.getNodeNameForService(m_appGUID,m_version, m_servinstance1);
       System.out.println("The node name for application " + m_appGUID+ " is "+nodename);

   }

   catch (Exception ex) {
       System.err.println("Exception in the Execution of test case::" + getName());
       ex.printStackTrace();
       Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
   }


}

    


   public void testgetServicesCurrentStatus() throws Exception{
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
           
       }

       catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }

   }

     public void testgetUniqueRunningID() throws Exception {

      try {
          System.out.println("Started the Execution of the TestCase::" + getName());

          ApplicationStateDetails appstateDetails = m_fioranoApplicationController.getCurrentStateOfApplication(m_appGUID,m_version);
          ServiceInstanceStateDetails servdetails = appstateDetails.getServiceStatus(m_servinstance1);

          String uniquerunningID = servdetails.getUniqueRunningInstID();

          System.out.println("The unique running ID is "+uniquerunningID);


          
      }

      catch (Exception ex) {
          System.err.println("Exception in the Execution of test case::" + getName());
          ex.printStackTrace();
          Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
      }


   }


    public void testsetTrackedDataType() throws Exception {

            try {
                System.out.println("Started the Execution of the TestCase::" + getName());
                String portName = "OUT_PORT";

                m_fioranoApplicationController.setTrackedDataType(m_servinstance1,m_appGUID,m_version,portName,1);


            }

            catch (Exception ex) {
                System.err.println("Exception in the Execution of test case::" + getName());
                ex.printStackTrace();
                Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
            }


         }


    public void testCleanup() throws Exception {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            m_fioranoApplicationController.close();
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);
        }
    }

    public static ArrayList getOrder() {
        ArrayList methodsOrder = new ArrayList();
        methodsOrder.add("testCreateApplication");
        methodsOrder.add("testClearErrLogsForRoute");
        methodsOrder.add("testClearOutLogsForRoute");
        methodsOrder.add("testGetLastErrorTraceForRoute");
        methodsOrder.add("testGetLastOutTraceForRoute");
        methodsOrder.add("testgetNodeNameForService");
        methodsOrder.add("testgetServicesCurrentStatus");
        methodsOrder.add("testgetUniqueRunningID");
        methodsOrder.add("testsetTrackedDataType");
        methodsOrder.add("testCleanup");

        return methodsOrder;
    }


}
