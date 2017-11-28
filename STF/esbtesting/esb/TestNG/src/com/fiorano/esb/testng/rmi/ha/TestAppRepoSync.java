package com.fiorano.esb.testng.rmi.ha;

import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IFPSManager;
import com.fiorano.esb.server.api.IServiceProviderManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.*;
import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;


/**
 * Created by IntelliJ IDEA.
 * User: janardhan
 * Date: Jan 21, 2011
 * Time: 4:21:35 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestAppRepoSync extends AbstractTestNG {

    private IEventProcessManager eventProcessManager;
    private IServiceProviderManager serviceprovidermanager;
    private IFPSManager fpsManager;
    private ExecutionController executioncontroller;
    private TestEnvironment testenv;
    private ServerStatusController serverstatus;
    private TestEnvironmentConfig testenvconfig;
    private MachineElement machine1;
    private ServerElement serverElement;
    private ProfileElement profileElement;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private Map<String, ProfileElement> profileElements = null;
    private TestEnvironmentConfig testEnvConfig;
    private String appName = "APPREPOSYNC";
    private String profile = null;
    private String machine = null;
    private boolean isWrapper = false;
    private boolean isHA = true;
    private float appVersion = 1.0f;
    @Test(groups = "AppRepoSync", alwaysRun = true)
    public void startSetup() {
        System.out.println("running startSetup");
        this.serverstatus = ServerStatusController.getInstance();
        this.fpsManager = rmiClient.getFpsManager();
        this.testenv = serverstatus.getTestEnvironment();
        this.executioncontroller = ExecutionController.getInstance();
        this.serviceprovidermanager = rmiClient.getServiceProviderManager();
        this.testenvconfig = ESBTestHarness.getTestEnvConfig();
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group AppRepoSync. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "AppRepoSync", description = "bug 19880 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestKillActiveHAFES() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestKillActiveHAFES", "TestAppRepoSync"));
        ArrayList arrlist = null;
        try {
            arrlist = AbstractTestNG.getActiveFESUrl();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillActiveHAFES", "TestAppRepoSync"), e);
            Assert.fail("Failed to get active FES url", e);
        }
        if (arrlist.get(1).toString().equals("2047"))
            profile = "primary";
        else
            profile = "secondary";
        serverElement = testenv.getServer("hafes");
        profileElements = serverElement.getProfileElements();


        profileElement = profileElements.get(new String(profile));
        machine = profileElement.getMachineName();
        machine1 = testenv.getMachine(machine);
        isWrapper = profileElement.isWrapper();
        isHA = serverElement.isHA();
        try {
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(120000);
            rmiClient = RMIClient.getInstance();
            startSetup();

        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestKillActiveHAFES", "TestAppRepoSync"), e);
            Assert.fail("Failed to Restart Primary FES", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestKillActiveHAFES", "TestAppRepoSync"));
    }

    @Test(groups = "AppRepoSync", description = "bug 19880 ", dependsOnMethods = "TestKillActiveHAFES", alwaysRun = true)
    public void TestDeployApplication() {
        System.out.println("running TestDeployApplication");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDeployApplication", "TestAppRepoSync"));
        SampleEventProcessListener epListener = null;

        try {
            epListener = new SampleEventProcessListener(appName, eventStore);
        } catch (RemoteException e1) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployApplication", "TestAppRepoSync"), e1);
            Assert.fail("Failed to create ep listener!", e1);
        }

        try {
            eventProcessManager.addEventProcessListener(epListener);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployApplication", "TestAppRepoSync"), e);
            Assert.fail("Failed to add ep listener!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployApplication", "TestAppRepoSync"), e);
            Assert.fail("Failed to do ep listener!", e);
        }


        try {
            deployEventProcess("appreposync-1.0@EnterpriseServer.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployApplication", "TestAppRepoSync"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeployApplication", "TestAppRepoSync"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestDeployApplication", "TestAppRepoSync"));

    }

    @Test(groups = "AppRepoSync", description = "bug 19880 ", dependsOnMethods = "TestDeployApplication", alwaysRun = true)
    public void TestEditAppxml() {
        System.out.println("running TestEditAppxml");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestEditAppxml", "TestAppRepoSync"));
        testEnvConfig = ESBTestHarness.getTestEnvConfig();
        String Appxml = testenvconfig.getProperty("FIORANO_HOME") + System.getProperty("file.separator") + "runtimedata" + System.getProperty("file.separator") + "repository" + System.getProperty("file.separator") + "META-INF" + System.getProperty("file.separator") + "Applications.xml";

        try {


            File file = new File(Appxml);

            //Create instance of DocumentBuilderFactory
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            //Get the DocumentBuilder
            DocumentBuilder docBuilder = factory.newDocumentBuilder();

            //Using existing XML Document
            Document doc = docBuilder.parse(file);

            //create the root element
            Element root = doc.getDocumentElement();

            //create child element
            Element childElement = doc.createElement("Application");

            //Add the attribute to the child
            childElement.setAttribute("GUID", "abcd12345");
            childElement.setAttribute("version", "1.0");
            childElement.setAttribute("counter", "0");
            root.appendChild(childElement);

            //set up a transformer
            //TransformerFactory transfac = TransformerFactory.newInstance();
            //Transformer trans = transfac.newTransformer();

            //create string from xml tree
            //StringWriter sw = new StringWriter();
            //StreamResult result = new StreamResult(sw);
            //DOMSource source = new DOMSource(doc);
            //Serialize DOM
            OutputFormat format    = new OutputFormat(doc);
            // as a String
            StringWriter stringOut = new StringWriter ();
            XMLSerializer serial   = new XMLSerializer (stringOut, format);
            serial.serialize(doc);
            //trans.transform(source, result);
            String xmlString = stringOut.toString();

            OutputStream f0;
            byte buf[] = xmlString.getBytes();
            f0 = new FileOutputStream(Appxml);
            for (int i = 0; i < buf.length; i++) {
                f0.write(buf[i]);
            }
            f0.close();
            buf = null;

        }
        catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEditAppxml", "TestAppRepoSync"), e);
            Assert.fail("Failed to edit applications.xml!", e);
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestEditAppxml", "TestAppRepoSync"));
    }

    @Test(groups = "AppRepoSync", description = "bug 19880 ", dependsOnMethods = "TestEditAppxml", alwaysRun = true)
    public void TestStartSecondaryHAFES() {
        System.out.println("running TestStartSecondaryHAFES");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestStartSecondaryHAFES", "TestAppRepoSync"));
        try {
            executioncontroller.startServerOnRemote(machine1.getAddress(), serverElement.getMode(), "haprofile1/" + profile, isWrapper, isHA);
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestStartSecondaryHAFES", "TestaddNewConfiguration"), e);
            Assert.fail("Failed to Restart Primary FES", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestStartSecondaryHAFES", "TestAppRepoSync"));
    }

    @Test(groups = "AppRepoSync", description = "bug 19880 ", dependsOnMethods = "TestStartSecondaryHAFES", alwaysRun = true)
    public void TestSwitchActivePassive() {
        System.out.println("running TestSwitchActivePassive");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestSwitchActivePassive", "TestAppRepoSync"));
        ArrayList arrlist = null;
        String profile = null;
        try {
            arrlist = AbstractTestNG.getActiveFESUrl();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSwitchActivePassive", "TestAppRepoSync"), e);
            Assert.fail("Failed to get active FES url", e);
        }
        if (arrlist.get(1).toString().equals("2047"))
            profile = "primary";
        else
            profile = "secondary";
        serverElement = testenv.getServer("hafes");
        profileElements = serverElement.getProfileElements();
        profileElement = profileElements.get(new String(profile));
        machine = profileElement.getMachineName();
        machine1 = testenv.getMachine(machine);
        isWrapper = profileElement.isWrapper();
        isHA = serverElement.isHA();
        try {
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(60000);
            executioncontroller.startServerOnRemote(machine1.getAddress(), serverElement.getMode(), "haprofile1/" + profile, isWrapper, isHA);
            Thread.sleep(60000);
            rmiClient = RMIClient.getInstance();
            startSetup();

        } catch (InterruptedException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestSwitchActivePassive", "TestAppRepoSync"), e);
            //Assert.fail("Failed to Restart Primary FES", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestSwitchActivePassive", "TestAppRepoSync"));
    }

    @Test(groups = "AppRepoSync", description = "bug 19880 ", dependsOnMethods = "TestSwitchActivePassive", alwaysRun = true)
    public void TestDeleteTempFolder() {
        System.out.println("running TestDeleteTempFolder");
        Logger.LogMethodOrder(Logger.getOrderMessage("TestDeleteTempfolder", "TestAppRepoSync"));
        try {
            testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String tempdir = testenvconfig.getProperty("FIORANO_HOME") + System.getProperty("file.separator") + "runtimedata" + System.getProperty("file.separator") + "repository" + System.getProperty("file.separator") + "applications" + System.getProperty("file.separator") + "applications_temp";
            File f = new File(tempdir);
            if (f.exists()) {
                System.out.println("$FIORANO_HOME/runtimedata/repository/applications/applications_temp folder exists");
                if (!f.delete()) {
                    throw new Exception("Failed to delete the folder $FIORANO_HOME/runtimedata/repository/applications/applications_temp");
                }
            }
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestDeleteTempFolder", "TestAppRepoSync"), e);
            Assert.fail("Failed to delete the folder $FIORANO_HOME/runtimedata/repository/applications/applications_temp");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestDeleteTempFolder", "TestAppRepoSync"));
    }

    @Test(groups = "AppRepoSync", description = "bug 19880 ", dependsOnMethods = "TestDeleteTempFolder", alwaysRun = true)
    public void stopAndDeleteApplication() {
        System.out.println("running stopAndDeleteApplication");
        Logger.LogMethodOrder(Logger.getOrderMessage("stopAndDeleteApplication", "TestAppRepoSync"));
        try {

            eventProcessManager.deleteEventProcess(appName, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestAppRepoSync"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("stopAndDeleteApplication", "TestAppRepoSync"), e);
            Assert.fail("Failed to do stop/delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("stopAndDeleteApplication", "TestAppRepoSync"));
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
}
