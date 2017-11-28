package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.dm.FioranoDeploymentManager;
import com.fiorano.esb.server.api.IEventProcessManager;
import com.fiorano.esb.server.api.ServiceException;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.dm.DeploymentRule;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: srikanth
 * Date: 8/8/11
 * Time: 4:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDMRuleswithLabels extends AbstractTestNG {
    private FioranoDeploymentManager fioranoDeploymentManager;
    private IEventProcessManager eventProcessManager;
    private String appGUID1 = "SIMPLECHAT";
    private String appGUID2 = "EPLABELTEST";
    private float appVersion = 1.0f;
    @Test(groups = "DMRuleswithLabels", alwaysRun = true)
    public void startSetup() {
        try {
            fioranoDeploymentManager = ServerStatusController.getInstance().getServiceProvider().getDeploymentManager();
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to run test,since we could not fioranoDeploymentManager!", e);
        }
        eventProcessManager = rmiClient.getEventProcessManager();
    }

    @Test(groups = "DMRuleswithLabels", description = "bug 20881 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestServiceLabelCheck() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestServiceLabelCheck", "TestDMRuleswithLabels"));
        try {
            DeploymentRule[] allRules = fioranoDeploymentManager.getDeploymentRules();
            for (int i = 0; i < allRules.length; i++)
                fioranoDeploymentManager.removeDeploymentRule(allRules[i].getRuleID());
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestServiceLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to clear rules DB!", e);
        }
        DeploymentRule deploymentRule1 = new DeploymentRule();
        deploymentRule1.setDisplayName("Rule1");
        deploymentRule1.setRuleID("1");
        deploymentRule1.setType(false);
        Set serviceset = new HashSet();
        serviceset.add("chat");
        Set serviceLabelset = new HashSet();
        serviceLabelset.add("Production");
        deploymentRule1.setServiceSet(serviceset);
        deploymentRule1.setServiceLabelSet(serviceLabelset);
        try {
            fioranoDeploymentManager.addDeploymentRule(deploymentRule1);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestServiceLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to add new deployment rule!", e);
        }
        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID1, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestServiceLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Assert.assertTrue(true, "DM Rule with service label is working fine");
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestServiceLabelCheck", "TestDMRuleswithLabels"));
    }

    @Test(groups = "DMRuleswithLabels", description = "bug 20881 ", dependsOnMethods = "TestServiceLabelCheck", alwaysRun = true)
    public void TestEPLabelCheck() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestEPLabelCheck", "TestDMRuleswithLabels"));
        try {
            DeploymentRule[] allRules = fioranoDeploymentManager.getDeploymentRules();
            for (int i = 0; i < allRules.length; i++)
                fioranoDeploymentManager.removeDeploymentRule(allRules[i].getRuleID());
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEPLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to clear rules DB!", e);
        }
        DeploymentRule deploymentRule2 = new DeploymentRule();
        deploymentRule2.setDisplayName("Rule2");
        deploymentRule2.setRuleID("2");
        deploymentRule2.setType(false);
        Set applicationset = new HashSet();
        applicationset.add(appGUID2);
        Set applicationLabelset = new HashSet();
        applicationLabelset.add("Staging");
        deploymentRule2.setApplicationSet(applicationset);
        deploymentRule2.setApplicationLabelSet(applicationLabelset);
        try {
            fioranoDeploymentManager.addDeploymentRule(deploymentRule2);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEPLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to add new deployment rule!", e);
        }
        try {
            deployEventProcess("EPLABELTEST.zip");
        } catch (IOException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEPLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to do SAVE!", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEPLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to do SAVE!", e);
        }

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
        }
        try {
            eventProcessManager.checkResourcesAndConnectivity(appGUID2, appVersion);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEPLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to do crc!", e);
        } catch (ServiceException e) {
            Assert.assertTrue(true, "DM Rule with Application label is working fine");
        }

        try {
            eventProcessManager.deleteEventProcess(appGUID2, appVersion, true, false);
        } catch (RemoteException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEPLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to do delete ep as part of cleanup", e);
        } catch (ServiceException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestEPLabelCheck", "TestDMRuleswithLabels"), e);
            Assert.fail("Failed to do delete ep as part of cleanup", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestEPLabelCheck", "TestDMRuleswithLabels"));
    }


    /**
     * deploys an event process which is located under $testing_home/esb/TestNG/resources/
     *
     * @param zipName - name of the event process zip located under $testing_home/esb/TestNG/resources/
     * @throws java.io.IOException - if file is not found or for any other IO error
     * @throws ServiceException
     */
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
