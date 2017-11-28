package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.stf.remote.JMXClient;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.ApplicationReference;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/24/11
 * Time: 2:33 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestWSGatewayService extends AbstractTestNG{
    ObjectName appController;
    private ObjectName objName;
    boolean isReady = false;
    private String resourceFilePath;
    private FioranoApplicationController m_fioranoApplicationController;
    private String m_appGUID;
    private float m_version;
    private String m_appFile;
    private String handleId ;

    private void init() throws Exception{
        m_appGUID = testProperties.getProperty("ApplicationGUID");
        m_version = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        m_appFile = resourceFilePath + fsc + testProperties.getProperty("ApplicationFile");
    }

    private void printInitParams(){
        System.out.println("...................The Parameters Used For The Test Are....................");
        System.out.println("Application:: "+m_appGUID+"\tVersion:: "+m_version);
        System.out.println("Application File :: "+m_appFile);
        System.out.println("The Resource File Path:: "+resourceFilePath);
        System.out.println("...........................................................................");
    }

    @BeforeClass(groups = "WSGatewayServiceTest", alwaysRun = true)
    public void startSetUp(){
        if (isReady == true)
            return;
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "WSGatewayService" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "WSGatewayService";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
        try {
            init();
            appController = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
            objName = new ObjectName("Fiorano.Esb.Webconsole.Controller:ServiceType=WebconsoleController,Name=WebconsoleController");
            jmxClient =JMXClient.getInstance();
            handleId = jmxClient.getHandleId();
            isReady = true;
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("error in initialisation");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
    }

    @Test(groups = "WSGatewayServiceTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testGetWebEventProcessList() {
//        Thread.sleep(10000);
        launchApplication();
        try {
            Thread.sleep(10000);
        } catch(InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        int webinterfaceType = 1;
        Object[] args = {webinterfaceType, handleId};
        String[] sigs = {int.class.getName(), String.class.getName()};
        try {
            Vector list = (Vector) jmxClient.invoke(appController, "getWebEventProcessList", args, sigs);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execpasswdution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "WSGatewayServiceTest", alwaysRun = true, dependsOnMethods = "testGetWebEventProcessList")
    public void testGenerateWSDL() {

        String appGUID = "WSGATEWAYSERVICE";
        String servInstName = "WSStub1";
        Object[] args = {appGUID, servInstName, handleId};
        String[] sigs = {String.class.getName(), String.class.getName(), String.class.getName()};
        try {
            String wsdl = (String) jmxClient.invoke(appController, "showWSDL", args, sigs);
            Assert.assertNotNull(wsdl);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "WSGatewayServiceTest", alwaysRun = true, dependsOnMethods = "testGenerateWSDL")
    public void testGetReqSOAPmsg() {

        String appGUID = "WSGATEWAYSERVICE";
        String servinstnme = "WSStub1";
        boolean addrflag = false;
        boolean basicAuth = false;
        String user = "admin";
        String passwd = "passwd";
        boolean ignoreHostnameMismatch=false;
        boolean acceptServerCertificate=false;
        ArrayList<String> operation= new ArrayList();

        Object[] args1 = {appGUID, servinstnme, addrflag, basicAuth, user, passwd, ignoreHostnameMismatch, acceptServerCertificate, handleId};
        String[] sigs1 = {String.class.getName(), String.class.getName(), boolean.class.getName(), boolean.class.getName(), String.class.getName(), String.class.getName(), boolean.class.getName(), boolean.class.getName(), String.class.getName()};

        Map<String,Map<String,ArrayList<String>>> map1 = null;
        try{
        map1 = (Map<String,Map<String,ArrayList<String>>>)jmxClient.invoke(appController, "getOps", args1, sigs1);
        }
        catch (Exception e){
            e.printStackTrace();
            //ignore
        }
        try{
            Object key1 = map1.keySet().iterator().next();
            Map<String,ArrayList<String>> value1=map1.get(key1);
            Object key2 = value1.keySet().iterator().next();
            ArrayList<String> value2=value1.get(key2);
            String s1 = value2.get(0);
            operation.add(0,key1.toString());
            operation.add(0,key2.toString());
            operation.add(0,s1);
        }
        catch (Exception e){
            //ignore
        }

        Object[] args = {appGUID, servinstnme, addrflag, basicAuth, user, passwd, operation, ignoreHostnameMismatch, acceptServerCertificate, handleId};
        String[] sigs = {String.class.getName(), String.class.getName(), boolean.class.getName(), boolean.class.getName(), String.class.getName(), String.class.getName(), ArrayList.class.getName(), boolean.class.getName(), boolean.class.getName(), String.class.getName()};

        try {
            String reqSOAPmsg = (String) jmxClient.invoke(appController, "getReqSOAPMsg", args, sigs);
            Assert.assertNotNull(reqSOAPmsg);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "WSGatewayServiceTest", alwaysRun = true, dependsOnMethods = "testGetReqSOAPmsg")
    public void testGetResponseSOAPMsg() {

        String soapStr = "";
        String endPointURL = "FAdminService";
        String username = "admin";
        String password = "passwd";
        boolean basicAuth = true;
        String servInstName = "";
        String appGUID="";
        boolean ignoreHostnameMismatch=false;
        boolean acceptServerCertificate=true;
        ArrayList<String> operation= new ArrayList();
        operation.add("TEST")   ;
        Object[] args = {soapStr, endPointURL, appGUID,servInstName, basicAuth, username, password,operation,ignoreHostnameMismatch,acceptServerCertificate,handleId};
        String[] sigs = {String.class.getName(), String.class.getName(), String.class.getName(), String.class.getName(),boolean.class.getName(), String.class.getName(),String.class.getName(), ArrayList.class.getName(), boolean.class.getName(), boolean.class.getName(), String.class.getName()};
        try {
            String responseSOAPmsg = (String) jmxClient.invoke(appController, "getResponseSOAPMsg", args, sigs);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }
    }

    @Test(groups = "WSGatewayServiceTest", alwaysRun = true, dependsOnMethods = "testGetResponseSOAPMsg")
    public void testGetListOfApplications() throws Exception {


        try {
            Enumeration applist = m_fioranoApplicationController.getListOfApplications(ApplicationReference.WEB_SERVICE);
            Assert.assertNotNull(applist);

        } catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }


    }

    @Test(groups = "WSGatewayServiceTest", alwaysRun = true, dependsOnMethods = "testGetListOfApplications")
    public void testGetHeadersOfApplications() throws Exception {

        try {
            Enumeration headerlist = m_fioranoApplicationController.getHeadersOfApplications(ApplicationReference.WEB_SERVICE);
            Assert.assertNotNull(headerlist);

        } catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }

    @Test(groups = "WSGatewayServiceTest", alwaysRun = true, dependsOnMethods = "testGetHeadersOfApplications")
    public void testStopWebInstance() {

        String appGUID = "WSGATEWAYSERVICE";
        String servinstnme = "WSStub1";
        String user = null;
        Object[] args = {appGUID, servinstnme, user};
        String[] sigs = {String.class.getName(), String.class.getName(),String.class.getName()};
        try {
            jmxClient.invoke(appController, "stopWebInstance", args, sigs);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }

    }

    @Test(groups = "WSGatewayServiceTest", alwaysRun = true, dependsOnMethods = "testStopWebInstance")
    public void testStartWebInstance() {

        String appGUID = "WSGATEWAYSERVICE";
        String servinstnme = "WSStub1";
        String user = null;
        Object[] args = {appGUID, servinstnme, user };
        String[] sigs = {String.class.getName(), String.class.getName(),String.class.getName()};
        try {
            jmxClient.invoke(appController, "startWebInstance", args, sigs);
        } catch (ReflectionException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (InstanceNotFoundException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        } catch (MBeanException e) {
            System.err.println("Exception in the Execution of test case::" + getName());
            e.printStackTrace();
            Assert.assertTrue(false, "TestCase Failed because of " + e.getMessage());
        }finally {
            System.out.println("going to kill the application");
            stopApplication();
        }

    }

    private void launchApplication() {
        try {
            System.out.println("The App File is::" + m_appFile);
            Application application = ApplicationParser.readApplication(new File(m_appFile));
            m_fioranoApplicationController.saveApplication(application);
            m_fioranoApplicationController.prepareLaunch(m_appGUID, m_version);
            m_fioranoApplicationController.launchApplication(m_appGUID, m_version);
            m_fioranoApplicationController.startAllServices(m_appGUID,m_version);
            Thread.sleep(10000);
        } catch (TifosiException e) {
            System.out.println("error launching the application");
            e.printStackTrace();
        }
        catch (InterruptedException e) {
        }

        //kill the application in the end of the testcase
    }
    private void stopApplication() {
        try {
            if (m_fioranoApplicationController.isApplicationRunning(m_appGUID))
                m_fioranoApplicationController.killApplication(m_appGUID,m_version);
            m_fioranoApplicationController.deleteApplication(m_appGUID, m_version);
            Thread.sleep(10000);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
