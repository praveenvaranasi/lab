package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.IRmiManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.service.Resource;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 9/27/11
 * Time: 1:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestResourceFetchForCacheEnabled extends AbstractTestNG {
    private static FioranoApplicationController fac;
    private static FioranoServiceRepository fsr;
    private static IEventProcessManager epm;
    private static File File_fioranohome;
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

    private void init() throws Exception{
        appGUID = testProperties.getProperty("ApplicationGUID");
        appVersion = Float.parseFloat(testProperties.getProperty("ApplicationVersion"));
        serviceGUID = testProperties.getProperty("serviceGUID");
        serviceVersion = testProperties.getProperty("serviceVersion");
        peer_resource = new File(File_fioranohome.getAbsolutePath() + fsc + "runtimedata" + fsc +
                "PeerServers" + fsc + "profile1" + fsc + "FPS" + fsc + "run" + fsc +
                "components" + fsc + "chat" + fsc + "4.0" + fsc + "newTextResource.txt");
        server_resource = new File(File_fioranohome.getAbsolutePath() + fsc + "runtimedata" + fsc +
                "repository" + fsc + "components" + fsc + "chat" + fsc +
                "4.0" + fsc + "newTextResource.txt");
    }

    private void printInitParams() {
        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("Application GUID::       " + appGUID + "  Version Number::" + appVersion);
        System.out.println("Service GUID::       " + serviceGUID + "  Service Version::" + serviceVersion);
        System.out.println("..................................................................");
    }

    @Test(groups = "ResourceFetchForCacheEnabled", alwaysRun = true, description = "Bug 19756")
    public void startSetUp(){
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp", "ResourceFetchForCacheEnabled"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        try {
            initializeProperties("bugs"+fsc+"TestResourceFetchforCacheEnabled_Bug19756"+ fsc + "tests.properties");
            rmiManager = rmiClient.getRmiManager();
            handleID = rmiManager.login("admin", "passwd");
            epm = rmiManager.getEventProcessManager(handleID);
            fac = rtlClient.getFioranoApplicationController();
            fsr = rtlClient.getFioranoServiceRepository();
            String fioranohome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
            File_fioranohome = new File(fioranohome);
        init() ;
        }
        catch (Exception e) {
            Assert.fail("could not get rmiManager");
            System.out.print(e.toString());
        }
        printInitParams();

    }

    //Case 1.. Check if added resource is present in peer repository
    @Test(groups = "ResourceFetchForCacheEnabled", alwaysRun = true, dependsOnMethods = "startSetUp", description = "Bug 19756")
    public void testResourceFetch() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("testResourceFetch", "ResourceFetchForCacheEnabled"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        setCache();
        Logger.Log(Level.INFO, "set cache");
        addsResource();
        Logger.Log(Level.INFO, "added resource");
        if (peer_resource.exists()) {
            Assert.assertTrue( true,"New  resources are fetched to service after CRC");
        } else {
            Assert.fail("! Resources not fetched in peer with cache enabled !");
        }
    }

    //Case 2.. Resource is modified and check if changes are reflected in peer repository
    @Test(groups = "ResourceFetchForCacheEnabled", alwaysRun = true, dependsOnMethods = "testResourceFetch", description = "Bug 19756")
    public void testResourceModified() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("testResourceModified", "ResourceFetchForCacheEnabled"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        addsResource();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //ignore
        }
        setCacheToNo();
        modifyResource();
        epm.checkResourcesAndConnectivity(appGUID, appVersion);
        cTime2 = peer_resource.lastModified();
        if (cTime1 != cTime2) {
            Assert.assertTrue(true, "Changes in the resource are reflected in peer server repository after CRC");
        } else {
            Assert.fail("! Resources not modified in peer with cache enabled !");
        }
    }

    @Test(groups = "ResourceFetchForCacheEnabled", alwaysRun = true, dependsOnMethods = "testResourceModified", description = "Bug 19756")
    public void tearDown() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("tearDown", "ResourceFetchForCacheEnabled"));
        System.out.println("Started the Execution of the TestCase::" + getName());
        fsr.removeResource(serviceGUID, serviceVersion, resource);
        epm.checkResourcesAndConnectivity(appGUID, appVersion);
        //resource_File.delete();
        peer_resource.delete();
        server_resource.delete();
    }

    //New resource(text file-) is added to service(named newTextresource)
    private void addsResource() throws Exception {
        resourceInit();
        resource = new Resource();
        resource.setName("newTextResource.txt");
        fsr.addResource(serviceGUID, serviceVersion, resource, resource_url);
        epm.checkResourcesAndConnectivity(appGUID, appVersion);
        cTime1 = peer_resource.lastModified();
    }

    private void modifyResource() throws IOException {
        String temp = "I write something";
            byte buf[] = temp.getBytes();
            OutputStream f1 = new FileOutputStream(server_resource);
            f1.write(buf);
            f1.close();
    }

    private void resourceInit() throws Exception {
        resource_url = testResourcesHome + fsc + "temp" + fsc + "newTextResource.txt";
        System.out.println("resource url is "+resource_url);
        resource_File = new File(resource_url);
            resource_File.createNewFile();
            String temp = " ";
            byte buf[] = temp.getBytes();
            OutputStream f1 = new FileOutputStream(resource_File);
            f1.write(buf);
            f1.close();
    }

    private void setCache() throws TifosiException {
            Application app = fac.getApplication(appGUID, appVersion);
            app.setComponentCached(true);
            fac.saveApplication(app);
    }

    private void setCacheToNo() throws TifosiException {
            Application app = fac.getApplication(appGUID, appVersion);
            app.setComponentCached(false);
            fac.saveApplication(app);
    }
}

