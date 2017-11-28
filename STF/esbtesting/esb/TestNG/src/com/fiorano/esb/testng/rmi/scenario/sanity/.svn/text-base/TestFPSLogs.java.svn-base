package com.fiorano.esb.testng.rmi.scenario.sanity;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.EventProcessHandle;
import com.fiorano.stf.misc.OutputPortMessageListener;
import fiorano.tifosi.common.FioranoException;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/17/11
 * Time: 6:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestFPSLogs extends AbstractTestNG{
    private FioranoApplicationController m_fioranoApplicationController;
    private boolean m_initialised = false;
    private String resourceFilePath;

    @BeforeClass(groups = "FPSLogsTest", alwaysRun = true)
    public void startSetUp(){
        initializeProperties("scenario" + fsc + "Sanity" + fsc + "FpsLogs" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Sanity" + fsc + "FpsLogs";
        m_fioranoApplicationController = rtlClient.getFioranoApplicationController();
    }

    @Test(groups = "FPSLogsTest", alwaysRun = true, description = "trying to import an application.")
    public void testFPSLogs() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testFPSLogs", "TestFPSLogs"));
            Boolean found = false;
            System.out.println("Started the Execution of the TestCase::" + getName());
            System.out.println("exporting the zip file to " + testResourcesHome+fsc+"temp" );
            rtlClient.getFioranoFPSManager().exportFPSLogs("fps", testResourcesHome+fsc+"temp"+fsc+"fps.zip");
            Thread.sleep(5000);
            found = new File(testResourcesHome+fsc+"temp"+fsc+"fps.zip").exists();
            if (found)
                System.out.println("zip file exported successfully to " + testResourcesHome+fsc+"temp");
            else throw new Exception();
            Logger.Log(Level.INFO, Logger.getFinishMessage("testFPSLogs", "TestFPSLogs"));
        }
        catch (Exception ex) {   //DRTTestLogger.initializeLogger();
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testFPSLogs", "TestFPSLogs"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());
        }
    }
}
