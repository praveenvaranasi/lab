package com.fiorano.esb.junit.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.rtl.server.FioranoServiceProviderFactory;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.service.Resource;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Abdullah
 * Date: Feb 18, 2011
 * Time: 2:49:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestResourceFetchforCacheEnabled_Bug19756 extends DRTTestCase {
    private static FioranoApplicationController fac;
    private static FioranoServiceRepository fsr;
    private static IEventProcessManager epm;
    private static TestEnvironmentConfig testEnvConfig;
    private static File  File_fioranohome;
    private static String handleID;
    private static File resource_File;
    private static File peer_resource, server_resource;
    private static String resource_url;
    private static String appGUID;
    private static String serviceGUID, serviceVersion;
    private static Float appVersion;
    private static Resource resource;
    private static long cTime1, cTime2;

    private static IRmiManager rmiManager;


    public TestResourceFetchforCacheEnabled_Bug19756(String name) throws Exception {
        super(name);
    }


    public TestResourceFetchforCacheEnabled_Bug19756(TestCaseElement testCaseConfig) throws Exception {
        super(testCaseConfig);
    }

    //Environment setup
    public void setUp() {

        try {
            super.setUp();
            rmiManager = rmiClient.getRmiManager();
            handleID = rmiManager.login("admin", "passwd");
            epm = rmiManager.getEventProcessManager(handleID);
            fac = rtlClient.getFioranoApplicationController();
            fsr = rtlClient.getFioranoServiceRepository();
            testEnvConfig = ESBTestHarness.getTestEnvConfig();
            String fioranohome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
            File_fioranohome = new File(fioranohome);
            init();

        }
        catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    //Case 1.. Check if added resource is present in peer repository
    public void testResourceFetch() throws Exception {
        setCache();
        addsResource();
        if (peer_resource.exists()) {
            Assert.assertTrue("New  resources are fetched to service after CRC", true);
        } else {
            Assert.fail("! Resources not fetched in peer with cache enabled !");
        }
    }

    //Case 2.. Resource is modified and check if changes are reflected in peer repository
    public void testResourceModified() throws Exception {
        addsResource();
        setCachetoNo();
        modifyResource();
        epm.checkResourcesAndConnectivity(appGUID, appVersion);
        cTime2 = peer_resource.lastModified();
        if (cTime1 != cTime2) {
            Assert.assertTrue("Changes in the resource are reflected in peer server repository after CRC", true);
        } else {
            Assert.fail("! Resources not modified in peer with cache enabled !");
        }
    }

    private void setCache() {
        try {
            Application app = fac.getApplication(appGUID, appVersion);
            app.setComponentCached(true);
            fac.saveApplication(app);
        }
        catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    private void setCachetoNo() {
        try {
            Application app = fac.getApplication(appGUID, appVersion);
            app.setComponentCached(false);
            fac.saveApplication(app);
        }
        catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void tearDown() throws Exception {
        fsr.removeResource(serviceGUID, serviceVersion, resource);
        epm.checkResourcesAndConnectivity(appGUID, appVersion);
        resource_File.delete();
        peer_resource.delete();
        server_resource.delete();
    }

    //New resource(text file-) is added to service(named newTextresource)
    private void addsResource() throws Exception {
        resourceInit();
        resource = new Resource();
        resource.setName("newTextresource.txt");
        fsr.addResource(serviceGUID, serviceVersion, resource, resource_url);
        epm.checkResourcesAndConnectivity(appGUID, appVersion);


        cTime1 = peer_resource.lastModified();
    }

    private void resourceInit() throws Exception {
        resource_url = File_fioranohome.getAbsolutePath() + File.separator + " newTextresource.txt";
        resource_File = new File(resource_url);
        if (!resource_File.exists()) {
            try {
                String temp = " ";
                byte buf[] = temp.getBytes();
                OutputStream f1 = new FileOutputStream(resource_File);
                f1.write(buf);
                f1.close();
            }
            catch (Exception fe) {
                System.out.print(fe.toString());
            }
        }
    }

    private void modifyResource() {
        String temp = "I write something";
        try {
            byte buf[] = temp.getBytes();

            OutputStream f1 = new FileOutputStream(server_resource);
            f1.write(buf);
            f1.close();
        }
        catch (Exception f) {
            System.out.print(f.toString());
        }

    }

    private void init() {
        appGUID = "SIMPLECHAT";
        appVersion = 1.0f;
        serviceGUID = "chat";
        serviceVersion = "4.0";
        peer_resource = new File(File_fioranohome.getAbsolutePath() + File.separator + "runtimedata" + File.separator +
                "PeerServers" + File.separator + "profile1" + File.separator + "FPS" + File.separator + "run" + File.separator +
                "components" + File.separator + "chat" + File.separator + "4.0" + File.separator + "newTextresource.txt");
        server_resource = new File(File_fioranohome.getAbsolutePath() + File.separator + "runtimedata" + File.separator +
                "repository" + File.separator + "components" + File.separator + "chat" + File.separator +
                "4.0" + File.separator + "newTextresource.txt");


    }

}
