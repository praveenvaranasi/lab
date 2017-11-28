package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.annotations.Test;

import javax.management.ObjectName;
import java.io.*;
import java.rmi.RemoteException;
import java.security.PrivateKey;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: janardhan
 * Date: 13/9/11
 * Time: 4:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestRestStubIncorrectXML_21054 extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appName = "TEST_REST_POST";
    private SampleEventProcessListener epListener = null;
    private float appVersion = 1.0f;
    @Test(groups = "RestStubIncorrectXML", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            //Assert.fail("Cannot run Group RestStubIncorrectXML. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "RestStubIncorrectXML", description = "RestStubIncorrectXML bug 21054 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestAddSchemaToRepository() {
        System.out.println("running TestAddSchemaToRepository of TestRestStubIncorrectXML_21054");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestAddSchemaToRepository", "TestRestStubIncorrectXML_21054"));
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String fiorano_home = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        String xmlcatalogPath = fiorano_home + System.getProperty("file.separator") + "xml-catalog" + System.getProperty("file.separator") + "user" + System.getProperty("file.separator");
        String testing_home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        String testng_resource_xmlcatalog = testing_home + System.getProperty("file.separator") + "esb" + System.getProperty("file.separator") + "TestNG" + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "schema_xmlcatalog" + System.getProperty("file.separator");
        try {


            File f1 = new File(xmlcatalogPath + "xml-catalog.xml");
            boolean renamed1 = f1.renameTo(new File(xmlcatalogPath + "xml-catalog.xml_backup"));
            String souFile1 = testng_resource_xmlcatalog + "xml-catalog.xml";
            String detFile1 = xmlcatalogPath + "xml-catalog.xml";
            copyfile(souFile1, detFile1);
            String souFile2 = testng_resource_xmlcatalog + "db_emp_detail.xsd";
            String detFile2 = xmlcatalogPath + "db_emp_detail.xsd";
            copyfile(souFile2, detFile2);

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
           // Assert.fail("Failed to copy files to xml-catalog", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestAddSchemaToRepository", "TestRestStubIncorrectXML_21054"));
    }

    @Test(groups = "RestStubIncorrectXML", description = "RestStubIncorrectXML bug 21054 ", dependsOnMethods = "TestAddSchemaToRepository", alwaysRun = true)
    public void TestRestStubIncorrectXML() {

        System.out.println("running TestRestStubIncorrectXML of TestRestStubIncorrectXML_21054");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML"));

        //SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML"), e1);
           // Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML"), e);
           // Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML"), e);
            //Assert.fail("Failed to do ep listener!", e);
        }
        // testEnvConfig = ESBTestHarness.getTestEnvConfig();
        // String currentos=testEnvConfig.getProperty(TestEnvironmentConstants.MACHINE);

        try {
            deployEventProcess("test_rest_post-1.0@EnterpriseServer.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
           // Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
           // Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }

        try {
            eventProcessManager.checkResourcesAndConnectivity(appName, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
           // Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
          //  Assert.fail("Failed to do crc!", e);
        }
        try {
            eventProcessManager.startEventProcess(appName, appVersion, false);
            ;
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
            //Assert.fail("Failed to do start application!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
           // Assert.fail("Failed to do start application!", e);
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
        //clear map
        eventStore.clear();
        try {
            //eventProcessManager.startAllServices(appName, appVersion);
            eventProcessManager.startAllServices(appName, appVersion);
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
            }
            // Assert.assertEquals(eventStore.get(TestConstants.SERVICEINSTANCE_LAUNCH_SUCCESSFUL + appName), "4");

        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
           // Assert.fail("Failed to do operation on service instance!", e);
        }
        try {

            ObjectName appController = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");

            Object[] args = {appName,appVersion, "RESTStub1", false, "", "", false, false, jmxClient.getHandleId()};
            String[] sigs = {String.class.getName(),"float", String.class.getName(), "boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName()};
            Map<String, Vector<String>> maps = (Map<String, Vector<String>>) jmxClient.invoke(appController, "getRESTServiceMethodDetails", args, sigs);
            // String resource= maps.keySet().toString();
            String resource = "";
            for (String resourceName : maps.keySet()) {
                resource = resourceName;
                break;
            }
            System.out.println("resource Name is :" + resource);
            String method = "";
            for (String methodName : maps.get(resource)) {
                method = methodName;
                break;
            }

            System.out.println("method Name is :" + method);
            Object[] args1 = {appName,appVersion, "RESTStub1", resource, method, false, "", "", false, false, jmxClient.getHandleId()};
            String[] sigs1 = {String.class.getName(),"float", String.class.getName(), String.class.getName(), String.class.getName(), "boolean", String.class.getName(), String.class.getName(), "boolean", "boolean", String.class.getName()};
            String dashboard_input = (String) jmxClient.invoke(appController, "getRESTRequestMsg", args1, sigs1);
            //System.out.println("Dashboard input is: "+dashboard_input);
            if (dashboard_input.contains("<ns1:Request xmlns:ns1=\"http://www.fiorano.com/services/rest\">") && dashboard_input.contains("<ns2:Employee xmlns:ns2=\"http://samples.abhi.rest.db/emp\">")) {
                System.out.println("Dashboard generates correct XML if request has representation schema configured in RestStub");
            } else {
                System.out.println("<ns1:Request xmlns:ns1=\"http://www.fiorano.com/services/rest\">");
                System.out.println("<ns2:Employee xmlns:ns2=\"http://samples.abhi.rest.db/emp\">");
                throw new Exception();
            }

        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
           // Assert.fail("Failed to do check dashboard input xml", e);

        }


    }

    @Test(groups = "RestStubIncorrectXML", dependsOnMethods = "TestRestStubIncorrectXML", alwaysRun = true)
    public void stopAndDeleteApplication() {
        try {
            //eventProcessManager.stopEventProcess(appName, appVersion);
            eventProcessManager.stopEventProcess(appName, appVersion);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
            }
            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestStubIncorrectXML", "TestRestStubIncorrectXML_21054"), e);
           // Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
    }


    @Test(groups = "RestStubIncorrectXML", dependsOnMethods = "stopAndDeleteApplication", alwaysRun = true)
    public void removeSchemaFromRepository() {

        System.out.println("running removeSchemaFromRepository of TestRestStubIncorrectXML_21054");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestAddSchemaToRepository", "TestRestStubIncorrectXML_21054"));
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String fiorano_home = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        String xmlcatalogPath = fiorano_home + System.getProperty("file.separator") + "xml-catalog" + System.getProperty("file.separator") + "user" + System.getProperty("file.separator");

        File file1 = new File(xmlcatalogPath + "xml-catalog.xml");
        file1.delete();

        File file2 = new File(xmlcatalogPath + "db_emp_detail.xsd");
        file2.delete();

        File f1 = new File(xmlcatalogPath + "xml-catalog.xml_backup");

        boolean renamed1 = f1.renameTo(new File(xmlcatalogPath + "xml-catalog.xml"));
        Logger.Log(Level.INFO, Logger.getFinishMessage("removeSchemaFromRepository", "TestRestStubIncorrectXML_21054"));

    }


    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + "/esb/TestNG/resources/" + zipName));
        boolean completed = false;
        byte result[];
        while (!completed) {
            byte[] temp = new byte[1024];
            int readcount = 0;
            readcount = bis.read(temp);

            if (readcount < 0) {
                completed = true;
                readcount = 0;
            }
            result = new byte[readcount];
            System.arraycopy(temp, 0, result, 0, readcount);

            // it will deploy the event process if the correct byte array is send; else gives an error
            this.eventProcessManager.deployEventProcess(result, completed);
        }
    }

    @Test(enabled = false)
    private void copyfile(String srFile, String dtFile) throws IOException {
        try {
            File f1 = new File(srFile);
            File f2 = new File(dtFile);
            InputStream in = new FileInputStream(f1);
            OutputStream out = new FileOutputStream(f2);

            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

}
