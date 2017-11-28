
package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.DeploymentUnit;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.*;
import com.fiorano.stf.util.StringHashtable;
import fiorano.tifosi.dmi.events.emailAlerts.AbstractEventAlert;
import fiorano.tifosi.dmi.events.emailAlerts.AppLaunchKillEventAlert;
import fiorano.tifosi.dmi.events.emailAlerts.FESStartStopAlert;
import fiorano.tifosi.dmi.events.emailAlerts.FPSStartStopAlert;
import org.testng.annotations.Test;

import javax.management.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.logging.Level;


/**
 * Created by IntelliJ IDEA.
 * User: santosh
 * Date: 8/21/12
 * Time: 2:11 PM
 * To change this template use File | Settings | File Templates.
 */

public class TestAddEventAlertsCleanDB21764 extends AbstractTestNG {
    private ArrayList<String> emailsDetails;
    private Object[] parameters = null;
    private ObjectName objName = null;
    private ObjectName objName1 = null;
    private String[] signatures;
    private TestSpecification testSpecification;
    private TestEnvironment testEnvironment;
    private static TestEnvironmentConfig config;
    private static Attribute attr1;
    private static Attribute attr2;
    private static MBeanServerConnection mbsc;
    private static String FioranoHome;

    @Test(groups = "Bugs",alwaysRun = true)
    public void initNewSetup() throws MalformedObjectNameException {
        this.emailsDetails = new ArrayList<String>();
        this.emailsDetails.add(0,"santosh.reddy@in.fiorano.com");
        this.emailsDetails.add(1,"dashboardsoa");
        this.emailsDetails.add(2,"dashboardsoa@fiorano.com");
        this.attr1 = new Attribute("MailServerName", "mail.in.fiorano.com");
        this.attr2 = new Attribute("MailServerPort",25);
        this.objName = new ObjectName("Fiorano.Esb.Events:ServiceType=EventEmailNotifier,Name=ESBEventEmailNotifier");
        this.objName1 = new ObjectName("Fiorano.Esb.Events:ServiceType=EventEmailNotifier,Name=ESBEventEmailNotifier,type = config");
        this.signatures = new String[]{AbstractEventAlert.class.getName(), String.class.getName(), String.class.getName()};
        this.jmxClient = JMXClient.getInstance();
        this.mbsc = JMXClient.getMBeanServerConnection();

    }

    @Test(groups = "Bugs",description = "adding alerts",dependsOnMethods = "initNewSetup",alwaysRun = true)
    public void TestAddAlert() throws InstanceNotFoundException, IOException, ReflectionException, MBeanException {


        /*try {
            mbsc.setAttribute(objName1,attr1);
            mbsc.setAttribute(objName1,attr2);
        } catch (AttributeNotFoundException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestAddaLERT_adding attri","TestAddEventAlertsCleanDB21764"));
        } catch (InvalidAttributeValueException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestAddaLERT_adding attri", "TestAddEventAlertsCleanDB21764"));
        }*/


         FPSStartStopAlert fpsStartStopAlert = new FPSStartStopAlert();
         setParentParams(fpsStartStopAlert, "fps", "ALL", this.emailsDetails);
         fpsStartStopAlert.setFpsName("fps");
         parameters = new Object[]{fpsStartStopAlert, "FPS", "ALL"};
         jmxClient.invoke(objName,"addEmailID",parameters,signatures);

         FESStartStopAlert fesStartStopAlert = new FESStartStopAlert();
         setParentParams(fesStartStopAlert, "fes", "ALL", this.emailsDetails);
         //fesStartStopAlert.setFes("fps");
         parameters = new Object[]{fesStartStopAlert, "FES", "ALL"};
         jmxClient.invoke(objName,"addEmailID",parameters,signatures);

         AppLaunchKillEventAlert appAlert = new AppLaunchKillEventAlert();
         setParentParams(appAlert, "Application", "ALL", this.emailsDetails);
         appAlert.setApplication("SIMPLECHAT");
         parameters = new Object[]{fesStartStopAlert, "APPLICATION", "ALL"};
         jmxClient.invoke(objName,"addEmailID",parameters,signatures);

    }

    @Test(groups = "Bugs",description = "Stopping and Starting Servers",dependsOnMethods = "TestAddAlert",alwaysRun = true)
    public void TestRestartServers() throws IOException, STFException {


        this.config = new TestEnvironmentConfig();
        config.load(new File("/home/santosh/EsbTest/Testing_Home/configuration/test.properties"));
        initializeTestSpecification(config.getProperty(TestEnvironmentConstants.TEST_SPECIFICATION));
        initializeTestEnvironment(config.getProperty(TestEnvironmentConstants.TEST_ENVIRONMENT));

        for (Object entries : testSpecification.getTestSuites().entrySet()) {
            Map.Entry entry = (Map.Entry) entries;
            TestSuiteElement testSuite = (TestSuiteElement) entry.getValue();

            shutdownServers(testSuite.getEnterpriseServer(), testSuite.getServers());

            deployProfiles(testSuite);

            startServers(testSuite);
        }


    }
    @Test(groups = "Bugs",description = "Stopping Servers",dependsOnMethods = "TestRestartServers",alwaysRun = true)
    public void TestShutServers() throws STFException, IOException {
        this.config = new TestEnvironmentConfig();
        config.load(new File("/home/santosh/Desktop/STF/esbtesting/configuration/test.properties"));
        try {
                initializeTestEnvironment(config.getProperty(TestEnvironmentConstants.TEST_ENVIRONMENT));
            }
            catch (STFException e) {
                System.out.println("ERROR while INITIALIZING 'TestEnvironment'. Not Running the tests.\n");
                e.printStackTrace();
            }
            try {
                initializeTestSpecification(config.getProperty(TestEnvironmentConstants.TEST_SPECIFICATION));
            }
            catch (STFException e) {
                System.out.println("ERROR while INITIALIZING 'Test Specifications'. Not Running the tests.\n");
                e.printStackTrace();
            }

        for (Object entries : testSpecification.getTestSuites().entrySet()) {
            Map.Entry entry = (Map.Entry) entries;
            TestSuiteElement testSuite = (TestSuiteElement) entry.getValue();

            shutdownServers(testSuite.getEnterpriseServer(), testSuite.getServers());

        }

    }

    @Test(groups = "Bugs",description = "Cleaning DB",dependsOnMethods = "TestShutServers",alwaysRun = true)
    public void TestCleanDB(){
        this.FioranoHome = config.getProperty(TestEnvironmentConstants.FIORANO_HOME);

        try {
            Process P1 = Runtime.getRuntime().exec(FioranoHome + "/esb/server/bin/clearDBServer.sh -mode fes -profile haprofile1/primary -q 2");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCleanDB1", "TestAddEventAlertsCleanDB21764"),e);
        }

        try {
            Process P4 = Runtime.getRuntime().exec(FioranoHome + "/esb/server/bin/clearDBServer.sh -mode fes -profile haprofile1/secondary -q 2");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestCleanDB1", "TestAddEventAlertsCleanDB21764"),e);
        }

        try {
            Process P2 = Runtime.getRuntime().exec(FioranoHome + "/esb/server/bin/clearDBServer.sh -mode fps -q 2");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestCleanDB1","TestAddEventAlertsCleanDB21764"),e);
        }

        try {
            Process P3 = Runtime.getRuntime().exec(FioranoHome + "/esb/server/bin/clearDBServer.sh -mode fps -profile profile2 -q 2");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestCleanDB1","TestAddEventAlertsCleanDB21764"),e);
        }

        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
        }
    }

    @Test(groups = "Bugs",description = "Starting Servers",dependsOnMethods = "TestCleanDB",alwaysRun = true)
    public void TestStartServers() throws STFException {

        for (Object entries : testSpecification.getTestSuites().entrySet()) {
            Map.Entry entry = (Map.Entry) entries;
            TestSuiteElement testSuite = (TestSuiteElement) entry.getValue();

            deployProfiles(testSuite);

            startServers(testSuite);
        }

    }

    @Test(groups = "Bugs",description = "checking FES Logs",dependsOnMethods = "TestStartServers",alwaysRun = true)
    public void TestCheckLogs(){
        String filename1 = FioranoHome +  File.separator + "runtimedata" + File.separator
                           + "repository" + File.separator + "EventAlerts" + File.separator + "FES" + File.separator+ "fes.xml";
        String filename2 = FioranoHome +  File.separator + "runtimedata" + File.separator
                           + "repository" + File.separator + "EventAlerts" + File.separator + "FPS" + File.separator+ "fps.xml";
        File file1 = new File(filename1);
        boolean b1 = file1.exists();

        File file2 = new File(filename2);
        boolean b2 = file2.exists();

        if(!(b1&b2)){
            Logger.Log(Level.SEVERE,Logger.getErrMessage("TestCheckLogs","TestAddEventAlertsCleanDB21764"));
        }
        else{
            Logger.Log(Level.INFO,Logger.getFinishMessage("TestCheckLogs","TestAddEventAlertsCleanDB21764"));
        }


    }

    @Test(enabled = false)
    private void setParentParams(AbstractEventAlert eventAlert, String alertId, String eventCategory, ArrayList<String> emailDetails) {
        eventAlert.setAlertId(alertId);
        eventAlert.setReceipientAddress(emailDetails.get(0));
        eventAlert.setSenderName(emailDetails.get(1));
        eventAlert.setSenderAddress(emailDetails.get(2));
        eventAlert.setEventCategory(AbstractEventAlert.EventCategory.valueOf(eventCategory));
    }

    @Test(enabled = false)
    private void initializeTestSpecification(String testSpecFile) throws STFException {
        this.testSpecification = new TestSpecification();
        testSpecification.initializeTestSpec(testSpecFile);
    }

    @Test(enabled = false)
    private void initializeTestEnvironment(String testEnvFile) throws STFException {
        this.testEnvironment = new TestEnvironment();
        testEnvironment.initializeTestEnvironment(testEnvFile);

    }

    @Test(enabled = false)
    private void shutdownServers(String enterpriseServerName, StringHashtable serversList) {
        ExecutionController remoteProxy = ExecutionController.getInstance();
        for (Enumeration servers = serversList.elements(); servers.hasMoreElements();) {
            TCServerElement tcServer = (TCServerElement) servers.nextElement();
            ServerElement server = testEnvironment.getServer(tcServer.getName());
            Map<String, ProfileElement> profiles = server.getProfileElements();
            for (String profileType : profiles.keySet()) {
                ProfileElement profileElement = profiles.get(profileType);
                remoteProxy.shutdownServerOnRemote(testEnvironment.getMachine(profileElement.getMachineName()).getAddress(),
                        server.getMode(), profileElement.getProfileName(), profileElement.isWrapper(), server.isHA());
            }
        }
    }

    @Test(enabled = false)
    private void deployProfiles(TestSuiteElement testSuite) throws STFException {

        String backupProfilesPath = ESBTestHarness.getTestEnvConfig().get(TestEnvironmentConstants.FIORANO_HOME) + File.separator + "esb" + File.separator
                + "server" + File.separator + "profiles_backup";
        String profilesPath = ESBTestHarness.getTestEnvConfig().get(TestEnvironmentConstants.FIORANO_HOME) + File.separator + "esb" + File.separator
                + "server" + File.separator + "profiles";
        try {
            ExecutionController remoteProxy = ExecutionController.getInstance();
            for (Enumeration servers = testSuite.getServers().elements(); servers.hasMoreElements();) {
                TCServerElement tcServer = (TCServerElement) servers.nextElement();
                ServerElement server = testEnvironment.getServer(tcServer.getName());
                // This will not deploy the server profiles if autostart is disabled
                if (!server.isAutoStartServer())
                    continue;
                //load properteis file -- this contains urls of prim /sec FES SERVERS.

                Map<String, ProfileElement> profiles = server.getProfileElements();
                ProfileElement profileElem;

                if (server.isHA()) {
                    //Create Primary Deployment Unit
                    profileElem = profiles.get("primary");
                    DeploymentUnit primaryDeploymentUnit = new DeploymentUnit();
                    primaryDeploymentUnit.setHAProfile(true);
                    primaryDeploymentUnit.setProfileDir(profileElem.getProfileName());
                    primaryDeploymentUnit.setMachineAddress(testEnvironment.getMachine(profileElem.getMachineName()).getAddress());
                    if (server.getMode().equalsIgnoreCase(TestEnvironmentConstants.FPS)) {
                        primaryDeploymentUnit.setPrimaryFESUrl(testEnvironment.getMachine(server.getPrimaryFESUrl()).getAddress());
                        primaryDeploymentUnit.setSecFESUrl(testEnvironment.getMachine(server.getSecondaryFESUrl()).getAddress());
                        primaryDeploymentUnit.setBackupPeerURL(testEnvironment.getMachine(profiles.get("secondary").getMachineName()).getAddress());
                    } else {
                        primaryDeploymentUnit.setSecFESUrl(testEnvironment.getMachine(profiles.get("secondary").getMachineName()).getAddress());
                    }
                    primaryDeploymentUnit.setLockFilePath(profileElem.getLockFilePath());
                    primaryDeploymentUnit.setProfileType(server.getMode(), true, true);
                    primaryDeploymentUnit.setServerConfigFilePath(primaryDeploymentUnit.getProfileType(), profileElem.getProfileName());
                    primaryDeploymentUnit.setGatewayMachineAddress(testEnvironment.getMachine(server.getGatewayMachine()).getAddress());
                    remoteProxy.deployProfile(primaryDeploymentUnit, this.getTestEnvConfig());

                    //Create secondary Deployment Unit
                    profileElem = profiles.get("secondary");
                    DeploymentUnit secondaryDeploymentUnit = new DeploymentUnit();
                    secondaryDeploymentUnit.setHAProfile(true);
                    secondaryDeploymentUnit.setProfileDir(profileElem.getProfileName());
                    secondaryDeploymentUnit.setMachineAddress(testEnvironment.getMachine(profileElem.getMachineName()).getAddress());
                    if (server.getMode().equalsIgnoreCase(TestEnvironmentConstants.FPS)) {
                        secondaryDeploymentUnit.setPrimaryFESUrl(testEnvironment.getMachine(server.getPrimaryFESUrl()).getAddress());
                        secondaryDeploymentUnit.setSecFESUrl(testEnvironment.getMachine(server.getSecondaryFESUrl()).getAddress());
                        secondaryDeploymentUnit.setBackupPeerURL(testEnvironment.getMachine(profiles.get("primary").getMachineName()).getAddress());
                    } else {
                        secondaryDeploymentUnit.setPrimaryFESUrl(testEnvironment.getMachine(profiles.get("primary").getMachineName()).getAddress());
                    }
                    secondaryDeploymentUnit.setLockFilePath(profileElem.getLockFilePath());
                    secondaryDeploymentUnit.setProfileType(server.getMode(), true, false);
                    secondaryDeploymentUnit.setServerConfigFilePath(secondaryDeploymentUnit.getProfileType(), profileElem.getProfileName());
                    secondaryDeploymentUnit.setGatewayMachineAddress(testEnvironment.getMachine(server.getGatewayMachine()).getAddress());
                    remoteProxy.deployProfile(secondaryDeploymentUnit, this.getTestEnvConfig());

                } else {
                    profileElem = profiles.get("standalone");
                    //Create Deployment Unit
                    DeploymentUnit deploymentUnit = new DeploymentUnit();
                    deploymentUnit.setHAProfile(false);
                    deploymentUnit.setProfileDir(profileElem.getProfileName());
                    deploymentUnit.setMachineAddress(testEnvironment.getMachine(profileElem.getMachineName()).getAddress());
                    deploymentUnit.setProfileType(server.getMode(), false, false);
                    deploymentUnit.setServerConfigFilePath(deploymentUnit.getProfileType(), profileElem.getProfileName());
                    deploymentUnit.setIsSSL(server.isSsl());
                    deploymentUnit.setIsJetty(server.isJetty());
                    deploymentUnit.setIsbasicAuth(server.isbasicAuth());
                    if (server.getMode().equalsIgnoreCase(TestEnvironmentConstants.FPS)) {
                        deploymentUnit.setPrimaryFESUrl(testEnvironment.getMachine(server.getPrimaryFESUrl()).getAddress());
                        deploymentUnit.setSecFESUrl(testEnvironment.getMachine(server.getSecondaryFESUrl()).getAddress());
                    }
                    remoteProxy.deployProfile(deploymentUnit, this.getTestEnvConfig());
                }
            }

            String profilesZipName = "profiles.zip";
            String serverConfigs = "serverConfigs.zip";
            remoteProxy.sendProfiles(profilesZipName);
            remoteProxy.sendConfiguration(serverConfigs);

        } catch (STFException e) {
            File profilesDir = new File(profilesPath);
            deleteDir(profilesDir);
            File backupProfilesDir = new File(backupProfilesPath);
            if (!backupProfilesDir.renameTo(profilesDir))
                System.out.println("ALERT !!! COULDN'T REVERT BACKUP PROFILES");
            e.printStackTrace();
        }
    }

    @Test(enabled = false)
    public static TestEnvironmentConfig getTestEnvConfig() {
        return config;
    }

    @Test(enabled = false)
    private void deleteDir(File dirName) {
        for (File file : dirName.listFiles()) {
            if (file.isDirectory())
                deleteDir(file);
            else file.delete();
        }
        dirName.delete();
    }

    @Test(enabled = false)
    private void startServers(TestSuiteElement testSuite) {
        StringHashtable peerServersList = testSuite.getServers();
        for (Enumeration servers = peerServersList.elements(); servers.hasMoreElements();) {
            TCServerElement tcServer = (TCServerElement) servers.nextElement();
            startServer(tcServer, TestEnvironmentConstants.FES);
        }
        for (Enumeration servers = peerServersList.elements(); servers.hasMoreElements();) {
            TCServerElement tcServer = (TCServerElement) servers.nextElement();
            startServer(tcServer, TestEnvironmentConstants.FPS);
        }
    }

    @Test(enabled = false)
    private void startServer(TCServerElement tcServer, String serverType) {
        ServerElement server = testEnvironment.getServer(tcServer.getName());
        if (!server.isAutoStartServer())
            return;
        if (!(server.getMode().equalsIgnoreCase(serverType)))
            return;
        ExecutionController remoteProxy = ExecutionController.getInstance();
        Map<String, ProfileElement> profiles = server.getProfileElements();
        ProfileElement profileElem;
        if (server.isHA()) {
            profileElem = profiles.get("primary");
            remoteProxy.startServerOnRemote(testEnvironment.getMachine(profileElem.getMachineName()).getAddress(),
                    server.getMode(), profileElem.getProfileName(), profileElem.isWrapper(), true);

            profileElem = profiles.get("secondary");
            remoteProxy.startServerOnRemote(testEnvironment.getMachine(profileElem.getMachineName()).getAddress(),
                    server.getMode(), profileElem.getProfileName(), profileElem.isWrapper(), true);
        } else {
            profileElem = profiles.get("standalone");
            remoteProxy.startServerOnRemote(testEnvironment.getMachine(profileElem.getMachineName()).getAddress(),
            server.getMode(), profileElem.getProfileName(), profileElem.isWrapper(), false);
        }
    }

}


