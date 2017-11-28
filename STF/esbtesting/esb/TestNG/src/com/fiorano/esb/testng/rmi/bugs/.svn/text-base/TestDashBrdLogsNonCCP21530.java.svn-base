package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 4/4/12
 * Time: 2:28 PM
 * To change this template use File | Settings | File Templates.
 * Bug No.21530
 */
public class TestDashBrdLogsNonCCP21530 extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private IFPSManager fpsManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private SampleEventProcessListener epListener = null;
    private String appName = "EVENT_PROCESS1";
    private float appVersion = 1.0f;
    @Test(groups= "Bugs",alwaysRun=true)
    public void initSetup(){
        this.eventProcessManager = rmiClient.getEventProcessManager();
        this.fpsManager = rmiClient.getFpsManager();

    }

    @Test(groups ="Bugs",dependsOnMethods = "initSetup",alwaysRun = true)
    public void TestNewNone() throws com.fiorano.esb.server.api.ServiceException,RemoteException{
        epListener = new SampleEventProcessListener(appName,eventStore);
        eventProcessManager.addEventProcessListener(epListener);

        try {
            deployEventProcess("EVENT_PROCESS1.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestNewNone","TestDashBrdLogsNonCCP"),e);
        }

        eventProcessManager.checkResourcesAndConnectivity(appName,appVersion);
        eventProcessManager.startEventProcess(appName,appVersion,false);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
        }
    }

    @Test(groups = "Bugs",dependsOnMethods = "TestNewNone",alwaysRun = true)
    public void getDashBrdLogs() throws IOException{

        ObjectName objName2 = null;
        try {
            objName2 = new ObjectName("Fiorano.Esb.Application.Controller:ServiceType=ApplicationController,Name=ApplicationController");
        } catch (MalformedObjectNameException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        String handleID = rmiClient.getHandleID();
               Object[] params = {appName, appVersion, handleID};
               String[] signatures = {String.class.getName(), float.class.getName(), String.class.getName()};
        try {
            jmxClient.invoke(objName2, "killApplication", params, signatures);
        } catch (ReflectionException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("getDashBrdLogs","TestDashBrdLogsNonCCP"),e);
        } catch (IOException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("getDashBrdLogs","TestDashBrdLogsNonCCP"),e);
        } catch (InstanceNotFoundException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("getDashBrdLogs","TestDashBrdLogsNonCCP"),e);
        } catch (MBeanException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("getDashBrdLogs","TestDashBrdLogsNonCCP"),e);
        }


        BufferedReader br1=null,br2=null;
        TestEnvironmentConfig testEnvConfig1 = ESBTestHarness.getTestEnvConfig();
        String test_home = testEnvConfig1.getProperty(TestEnvironmentConstants.TESTING_HOME);
        String Fiorano_home = testEnvConfig1.getProperty(TestEnvironmentConstants.FIORANO_HOME);
        String DashBrdLogs = Fiorano_home + File.separator + "runtimedata" + File.separator + "EnterpriseServers" + File.separator
                             +"profile1"+File.separator+"FES"+File.separator+"run"+File.separator+"logs"+File.separator+"Dashboard.log";
        String OutputLogs = test_home + File.separator+ "reports"+File.separator+"TestNG_Reports__testNG_Rmi_Cases"+File.separator+
                          "logs"+File.separator+"test-output.log";

        File f3 = new File("FileForTestDashBrdLogsNonCCP.txt");
        if(!f3.exists()){
            try {
                f3.createNewFile();
            } catch (IOException e) {
                Logger.Log(Level.SEVERE,Logger.getErrMessage("getDashBrdLogs","TestDashBrdLogsNonCCP"),e);
            }
        }

        FileWriter fWrit = new FileWriter(f3);
        BufferedWriter bufwrit = new BufferedWriter(fWrit);
        bufwrit.write("The class TestDashBrdLogsNonCCP produced following Dashboard Logs:");
        bufwrit.close();

        File f1 = new File(DashBrdLogs);
        File f2 = new File(OutputLogs);

        String str = null;
        char c='\n';
        StringBuffer buf1 = new StringBuffer();
        StringBuffer buf2 = new StringBuffer();

        try {
            br1 = new BufferedReader(new FileReader(f3));
            br2 = new BufferedReader(new FileReader(f1));
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        while(true){
            try {
                str = br1.readLine();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            if(str == null)
                break;
            buf1.append(str);
            buf1.append(c);
        }

        br1.close();

        FileWriter fw1 = new FileWriter(OutputLogs,true);
        BufferedWriter bw1 = new BufferedWriter(fw1);
        bw1.write(buf1.toString());
        bw1.close();

        while(true){
            try {
                str = br2.readLine();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

            if(str == null)
                break;
            buf2.append(str);
            buf2.append(c);
        }

        br2.close();

        FileWriter fw2 = new FileWriter(OutputLogs,true);
        BufferedWriter bw2 = new BufferedWriter(fw2);
        bw2.write(buf2.toString());
        bw2.close();
    }

    @Test(groups = "Bugs",dependsOnMethods = "getDashBrdLogs",alwaysRun = true)
    public void stopAndDeleteEP(){
        stopAndDeleteEP(eventProcessManager, appName , appVersion);
    }

    @Test(enabled=false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        File TestingHome = new File(home);
        bis = new BufferedInputStream(new FileInputStream(TestingHome.getAbsolutePath() + File.separator + "esb" + File.separator + "TestNG" +
                File.separator + "resources" + File.separator + zipName));
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
}
