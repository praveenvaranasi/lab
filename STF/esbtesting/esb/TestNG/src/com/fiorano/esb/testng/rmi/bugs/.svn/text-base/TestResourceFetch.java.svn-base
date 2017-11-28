package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.RTLServiceEventListener;
import com.fiorano.esb.testng.rmi.SampleEventProcessListener;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.service.Resource;
import junit.framework.Assert;
import org.testng.annotations.Test;

import javax.naming.NamingException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: Jan 17, 2011
 * Time: 11:28:10 AM
 * To change this template use File | Settings | File Templates.
 */


public class TestResourceFetch extends AbstractTestNG {
    private IEventProcessManager eventProcessManager;
    private static Hashtable<String, String> eventStore = new Hashtable<String, String>();
    private String appGUID = "EVENT_PROCESS1";
    private float appVersion = 1.0f;

    @Test(groups = "ResourceFetch", alwaysRun = true)
    public void startSetup() {
        this.eventProcessManager = rmiClient.getEventProcessManager();
        if (eventProcessManager == null) {
            Assert.fail("Cannot run Group Resource Fetch. as eventProcessManager is not set.");
        }
    }

    @Test(groups = "ResourceFetch", description = " Resource not fetched if its present in installation drive", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestResourceFetch19543() {

        if (System.getProperty("os.name").startsWith("Windows")) {
            Logger.LogMethodOrder(Logger.getOrderMessage("TestResourceFetch", "ResourceFetch"));
            SampleEventProcessListener epListener = null;
/*            String soaversion = testEnvConfig.getProperty(TestEnvironmentConstants.SOA_VERSION);
            String[] strarray = null;
            strarray = soaversion.split("\\\\");
            soaversion = strarray[strarray.length - 1];
            System.out.println(soaversion);*/
            try {
                epListener = new SampleEventProcessListener(appGUID, eventStore);
            } catch (RemoteException e1) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetch", "ResourceFetch"), e1);
                org.testng.Assert.fail("Failed to create ep listener!", e1);
            }

            try {
                eventProcessManager.addEventProcessListener(epListener);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetch", "ResourceFetch"), e);
                org.testng.Assert.fail("Failed to add ep listener!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetch", "ResourceFetch"), e);
                org.testng.Assert.fail("Failed to do ep listener!", e);
            }


            HashMap serverdetails = null;
            try {
                serverdetails = rmiClient.getServiceProviderManager().getServerDetails();
            } catch (RemoteException e) {
                Assert.fail("Fail to get server details");
            } catch (ServiceException e) {
                Assert.fail("Fail to get server details");
            }
            String url = (String) serverdetails.get("Server URL");

            RTLServiceEventListener rtlServiceEventListener = new RTLServiceEventListener(url);
            FioranoServiceRepository fsr = null;
            try {
                fsr = rtlServiceEventListener.startup();
            } catch (NamingException e) {
                org.testng.Assert.fail("NamingException while creating rtlServiceEventSubscriber");
            } catch (TifosiException e) {
                org.testng.Assert.fail("TifosiException while creating rtlServiceEventSubcriber");
            }

            Resource resource = new Resource();
            resource.setName("ojdbc14.jar");
            //resource.
            //resource.
            String resourceLocation = TestingHome.getAbsolutePath() + "/esb/TestNG/resources/ojdbc14.jar";
            //TODO: resource to be added here
            try {
                fsr.addResource("jdbc", "4.0", resource, resourceLocation);
            } catch (TifosiException e) {
                Assert.fail("Fail to add resource to service");
            }

            //get the file property into a string
            try {
                deployEventProcess("ResourceFetch@EnterpriseServer.zip");
            } catch (IOException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetch", "ResourceFetch"), e);
                org.testng.Assert.fail("Failed to do SAVE!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetch", "ResourceFetch"), e);
                org.testng.Assert.fail("Failed to do SAVE!", e);
            }

            try {
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetch", "ResourceFetch"), e);
                org.testng.Assert.fail("Failed to do crc!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetch", "ResourceFetch"), e);
                org.testng.Assert.fail("Failed to do crc!", e);
            }
            String ps = File.separator;
            //expecting the default installation to be c:\program Files\....
            String str = fiorano_home.getAbsolutePath() + ps + "runtimedata" + ps + "PeerServers" + ps + "profile1" + ps + "FPS" + ps + "run" + ps + "components" + ps + "jdbc" + ps + "4.0" + ps + "ojdbc14.jar";
            File file = new File(str);
            long firstlymodifiedtime = 0l;
            if (file.exists()) {
                firstlymodifiedtime = file.lastModified();
            }

            try {
                fsr.addResource("jdbc", "4.0", resource, resourceLocation);
            } catch (TifosiException e) {
                Assert.fail("Fail to add resource to service");
            }

            //get the ojdbc14.jar file property and check with above add resource property
            try {
                eventProcessManager.checkResourcesAndConnectivity(appGUID, appVersion);
            } catch (RemoteException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetch", "ResourceFetch"), e);
                org.testng.Assert.fail("Failed to do crc!", e);
            } catch (ServiceException e) {
                Logger.Log(Level.SEVERE, Logger.getErrMessage("TestResourceFetch", "ResourceFetch"), e);
                org.testng.Assert.fail("Failed to do crc!", e);
            }

            long lastlymodifiedtime = 0l;
            if (file.exists()) {
                lastlymodifiedtime = file.lastModified();
            }

            if (firstlymodifiedtime != 0l && lastlymodifiedtime != 0l && firstlymodifiedtime == lastlymodifiedtime) {
                Assert.fail("fail to fetch resource");
            }

            //removing the added resource.
            try {
                fsr.removeResource("jdbc", "4.0", resource);
            } catch (TifosiException e) {
                Assert.fail("Failed to remove resource");
            }
            stopAndDeleteEP(eventProcessManager, appGUID, appVersion);
        }
        Logger.Log(Level.SEVERE, Logger.getFinishMessage("TestResourceFetch", "ResourceFetch"));

    }

    TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
    String home = testEnvConfig.getProperty(TestEnvironmentConstants.TESTING_HOME);
    String fioranohome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
    File TestingHome = new File(home);
    File fiorano_home = new File(fioranohome);

    @Test(enabled = false)
    private void deployEventProcess(String zipName) throws IOException, ServiceException {
        BufferedInputStream bis = null;
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
