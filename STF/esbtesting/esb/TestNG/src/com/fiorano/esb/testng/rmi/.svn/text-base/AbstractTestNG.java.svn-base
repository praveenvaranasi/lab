package com.fiorano.esb.testng.rmi;

import com.fiorano.ant.tasks.ImportEStudioApplication;
import com.fiorano.ant.tasks.RMIConnectionManagerBase;
import com.fiorano.esb.server.api.EventProcessReference;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.bugs.TestConstants;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.remote.RTLClient;
import com.fiorano.stf.test.core.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: lokesh2
 * Date: Feb 3, 2011
 * Time: 11:25:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class AbstractTestNG {

    protected static RMIClient rmiClient;
    protected static RTLClient rtlClient;
    protected static JMXClient jmxClient;
    protected static Properties testProperties;
    protected char fsc =File.separatorChar;
    protected static String testResourcesHome;
    protected static String testingHome;
    protected static TestEnvironmentConfig testEnvConfig;
    protected static int Thread_Sleep_Time;
    private int attemptInstanceLevel = 0;
    private static String fioranoHome;
    protected enum SERVER_TYPE {FES, FPS};

    public AbstractTestNG() {
        rmiClient = RMIClient.getInstance();
        rtlClient = RTLClient.getInstance();
        jmxClient = JMXClient.getInstance();
        initializeProperties();
        Logger.initializeLogger();
    }
    protected void dupConstructor(){
        cleanup();
        rmiClient = RMIClient.getInstance();
        rtlClient = RTLClient.getInstance();
        jmxClient = JMXClient.getInstance();
        initializeProperties();
        Logger.initializeLogger();
    }

    public void initializeProperties() {
        if (testProperties != null)
            return;
        try {
            // initialize the properties file avaliable in the resource folder
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            testingHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            File th = new File(testingHome);
            FileInputStream fis = new FileInputStream(th.getAbsolutePath() + fsc + TestConstants.TestNG_Home + fsc + "resources" + fsc + "Test.properties");
            testProperties = new Properties();
            testProperties.load(fis);
            String temp = testProperties.getProperty("Thread_Sleep_Time");
            Thread_Sleep_Time = Integer.parseInt(temp);
//            System.out.println("Thread_Sleep_Time= " + Thread_Sleep_Time);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * sets default import options
     * @param zipName
     * @return
     * @throws Exception
     */
    public boolean importApplication(String zipName) throws Exception {
       return importApplication(zipName, true, true, null);
    }

    public boolean importApplication(String zipName, boolean overWrite, boolean overWriteNamedConfig, String label) throws Exception {
        RMIConnectionManagerBase rmiConnectionManagerBase = new RMIConnectionManagerBase(rmiClient.getRmiManager(), rmiClient.getHandleID(), fioranoHome);
        TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String testingHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
        String applicationFilePath = testingHome+ File.separator + "esb" + File.separator + "TestNG" + File.separator + "resources" + File.separator + zipName;
        File file = new File(applicationFilePath);
        ImportEStudioApplication importInstance= new ImportEStudioApplication();
        setImportApplicationParams(importInstance, file, label, overWrite, overWriteNamedConfig);

        importInstance.execute(rmiConnectionManagerBase);
        return true;
    }

    private void setImportApplicationParams(ImportEStudioApplication importInstance, File file,String label, boolean  overWrite, boolean overwriteNamedConfig) {
        importInstance.setImportLocation(file.getParentFile().getAbsolutePath());
        importInstance.setImportAppsList(file.getName());
        importInstance.setLabel(label);
        importInstance.setOverwrite(overWrite+"");
        importInstance.setOverWriteNamedConfig(overwriteNamedConfig+"");
        importInstance.setImportWholeAppChain("true");

    }

    /**
     * deprecated method to import application, Use importApplication
     * @param zipName
     * @param eventProcessManager
     * @throws IOException
     * @throws ServiceException
     */
    private void deployEventProcess(String zipName, IEventProcessManager eventProcessManager) throws IOException, ServiceException {
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
            eventProcessManager.deployEventProcess(result, completed);
        }
    }

    public void initializeProperties(String rel_path) {
        try {
            testEnvConfig = ESBTestHarness.getTestEnvConfig();
            testingHome = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
            testResourcesHome= testingHome + fsc + "esb" + fsc + "TestNG" + fsc + "resources";
            FileInputStream fis = new FileInputStream(testResourcesHome + fsc + "tests" + fsc + rel_path);
            testProperties = new Properties();
            testProperties.load(fis);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key) {
        return testProperties.getProperty(key);
    }

    /**
     * returns a arraylist of size 2.
     * which contains a string(ip of fes) followed by a int(rmi port of fes)
     */
    @Test(enabled = false)
    public static ArrayList getActiveFESUrl() throws STFException {
        ArrayList url = new ArrayList(2);//string followed by int.
        try {
            String s = ServerStatusController.getInstance().getURLOnFESActive();
            String rtlPort = s.substring(s.lastIndexOf(":") + 1);
            url.add(s.substring(s.lastIndexOf("//") + 2, s.lastIndexOf(":")));
            if (rtlPort.equals("1947")) {
                url.add(2047);
            } else
                url.add(2048);
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, "Unable to proceed with tests exception occured  while getting Active FES rmi port & url", e);
            throw e;
        }
        return url;
    }

   public static String getName() {
        //return Thread.currentThread().getStackTrace()[2].getMethodName().toString();
       return Thread.currentThread().getStackTrace()[2].toString();
    }

   public static String getOnlyMethodName() {
       return Thread.currentThread().getStackTrace()[2].getMethodName().toString();
    }

    protected static void cleanup(){
        RTLClient.setClientToNull();
        RMIClient.setClientToNull();
        JMXClient.setClientToNull();
    }


    /**
     * startServer
     *
     * This method will exit only if the given server is started
     *
     * @param testenv
     * @param testenvconfig
     * @param serverName
     * @param type
     */
    public void startServer(TestEnvironment testenv, TestEnvironmentConfig testenvconfig, String serverName, SERVER_TYPE type) {
        Logger.LogMethodOrder(Logger.getOrderMessage("Start Server", "Start Server"));
        if(attemptInstanceLevel > 2){
            attemptInstanceLevel = 0;
            Assert.fail("Could not able to start server because of some reasons");
            return;
        }
        attemptInstanceLevel++;
        try {
            if(type == SERVER_TYPE.FES)
                cleanup();
            if(testenvconfig != null)
                System.out.println(testenvconfig.getProperty(TestEnvironmentConstants.FIORANO_HOME));
            ExecutionController remoteProxy = ExecutionController.getInstance();
            ServerElement se = testenv.getServer(serverName);
            Map<String, ProfileElement> profileElements = se.getProfileElements();
            String str = new String("standalone");
            ProfileElement pm = profileElements.get(str);
            String machine = pm.getMachineName();
            MachineElement machine1 = testenv.getMachine(machine);
            String profileType = se.getMode();
            String profileName = pm.getProfileName();
            Boolean isWrapper = pm.isWrapper();
            Boolean isHA = se.isHA();
            remoteProxy.startServerOnRemote(machine1.getAddress(), profileType, profileName, isWrapper, isHA, testenvconfig);
            int attempt = 0; //waiting attempt for server to start
            if(type == SERVER_TYPE.FES) {
                while (true){
                    try{
                        rmiClient = RMIClient.getInstance();
                        rtlClient = RTLClient.getInstance();
                        jmxClient = JMXClient.getInstance();
                        break;
                    } catch (Exception e){
                        if(attempt > 4){
                            startServer(testenv, testenvconfig, serverName, type); //starting againg of server did not start even after 10 seconds
                            return;
                        }
                        Thread.sleep(2000);
                        attempt++;
                        continue;
                    }

                }
            } else {
                if(rmiClient == null)
                    Thread.sleep(10000);
                else
                    while ( !rmiClient.getFpsManager().isPeerRunning(serverName) )  {
                        Thread.sleep(1000);
                        if(attempt > 5){
                            startServer(testenv, testenvconfig, serverName, type);
                            return;
                        }
                        attempt++;

                    }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCleanUp", "TestSpaceInFioranoHome"), e);
            attemptInstanceLevel = 0;
        }
        attemptInstanceLevel = 0;
        Logger.Log(Level.INFO, Logger.getFinishMessage("Start Server", "Start Server"));
    }
    protected void stopAllEPs(IEventProcessManager eventProcessManager){
        try{
        for (EventProcessReference runningAppRef : eventProcessManager.getRunningEventProcesses()) {
            eventProcessManager.stopEventProcess(runningAppRef.getId(),runningAppRef.getVersion());
        }
        } catch (Exception e){
            //ignore
        }
    }

    protected void stopAndDeleteEP(IEventProcessManager eventProcessManager, String appName, Float appVersion){
        boolean exists = false;
                try{
                    exists = eventProcessManager.exists(appName, appVersion);
                } catch (Exception e) {}

        if (exists){
            boolean isRunnig = false;
            try{
                isRunnig = eventProcessManager.isRunning(appName, appVersion);
            } catch (Exception e) {}
            if(isRunnig){
            try{
                    eventProcessManager.stopEventProcess(appName, appVersion);
                Thread.sleep(1000);
            }catch (Exception e){}
            }
            try{
                eventProcessManager.deleteEventProcess(appName, appVersion, true, true);
                Thread.sleep(1000);
            }
            catch (Exception e){ }
        }
    }

}
