package com.fiorano.esb.testng.rmi.bugs;

import com.fiorano.esb.rtl.dm.FioranoDeploymentManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.ExecutionController;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.test.core.ProfileElement;
import com.fiorano.stf.test.core.ServerElement;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.TestEnvironmentConfig;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.dm.DeploymentRule;
import fiorano.tifosi.util.xmlutils.FioranoStaxParser;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: srikanth
 * Date: 8/8/11
 * Time: 12:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPersistDeploymentRules extends AbstractTestNG {
    private FioranoDeploymentManager fioranoDeploymentManager;
    private ExecutionController executioncontroller;
    private ServerStatusController serverstatus;
    private TestEnvironment testenv;
    private TestEnvironmentConfig testenvconfig;
    private String fioranohome;
    private String rulesDB;

    @Test(groups = "PersistDeploymentRules", alwaysRun = true)
    public void startSetup() {
        try {
            fioranoDeploymentManager = ServerStatusController.getInstance().getServiceProvider().getDeploymentManager();
            serverstatus = ServerStatusController.getInstance();
            testenv = serverstatus.getTestEnvironment();
            executioncontroller = ExecutionController.getInstance();
            testenvconfig = ESBTestHarness.getTestEnvConfig();
            TestEnvironmentConfig testEnvConfig = ESBTestHarness.getTestEnvConfig();
            fioranohome = testEnvConfig.getProperty(TestEnvironmentConstants.FIORANO_HOME);
            rulesDB = "esb" + File.separator + "server" + File.separator + "profiles" + File.separator + "profile1" + File.separator + "FES" + File.separator + "conf" + File.separator + "rules.xml";
        } catch (STFException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("startSetup", "TestPersistDeploymentRules"), e);
            Assert.fail("Failed to run test,since we could not fioranoDeploymentManager!", e);
        }
    }

    @Test(groups = "PersistDeploymentRules", description = "bug 20885 ", dependsOnMethods = "startSetup", alwaysRun = true)
    public void TestaddDeploymentRules() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestaddDeploymentRules", "TestPersistDeploymentRules"));
        try {
            DeploymentRule[] allRules = fioranoDeploymentManager.getDeploymentRules();
            for (int i = 0; i < allRules.length; i++)
                fioranoDeploymentManager.removeDeploymentRule(allRules[i].getRuleID());
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddDeploymentRules", "TestPersistDeploymentRules"), e);
            Assert.fail("Failed to clear rules DB!", e);
        }
        DeploymentRule deploymentRule1 = new DeploymentRule();
        deploymentRule1.setDisplayName("Rule1");
        deploymentRule1.setRuleID("1");
        deploymentRule1.setType(true);
        try {
            fioranoDeploymentManager.addDeploymentRule(deploymentRule1);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddDeploymentRules", "TestPersistDeploymentRules"), e);
            Assert.fail("Failed to add new deployment rule!", e);
        }
        DeploymentRule deploymentRule2 = new DeploymentRule();
        deploymentRule2.setDisplayName("Rule2");
        deploymentRule2.setRuleID("2");
        deploymentRule2.setType(false);
        try {
            fioranoDeploymentManager.addDeploymentRule(deploymentRule2);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddDeploymentRules", "TestPersistDeploymentRules"), e);
            Assert.fail("Failed to add new deployment rule!", e);
        }
        if (!(get_no_of_rules() == 2)) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddDeploymentRules", "TestPersistDeploymentRules"));
            Assert.fail("There is a mismatch in rule count!");
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestaddDeploymentRules", "TestPersistDeploymentRules"));
    }

    @Test(groups = "PersistDeploymentRules", description = "bug 20885 ", dependsOnMethods = "TestaddDeploymentRules", alwaysRun = true)
    public void TestRestartFES() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestRestartFES", "TestPersistDeploymentRules"));
        try {
            Map<String, ProfileElement> profileElements = null;
            ServerElement serverElement = testenv.getServer("fes");
            profileElements = serverElement.getProfileElements();
            rmiClient.shutdownEnterpriseServer();
            Thread.sleep(5000);
            startServer(testenv, testenvconfig, "fes", SERVER_TYPE.FES);
            //executioncontroller.startServerOnRemote(testenv.getMachine(profileElements.get("standalone").getMachineName()).getAddress(), serverElement.getMode(), "profile1", profileElements.get("standalone").isWrapper(), testenv.getServer("fes").isHA());
            Thread.sleep(120000);
            dupConstructor();
            startSetup();
        } catch (Exception e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestRestartFES", "TestPersistDeploymentRules"), e);
            Assert.fail("Failet to restart Enterprise Servers", e);
        }
        Logger.Log(Level.INFO, Logger.getFinishMessage("TestRestartFES", "TestPersistDeploymentRules"));

    }

    @Test(groups = "PersistDeploymentRules", description = "bug 20885 ", dependsOnMethods = "TestRestartFES", alwaysRun = true)
    public void TestaddDeploymentRulesafterRestart() {
        Logger.LogMethodOrder(Logger.getOrderMessage("TestaddDeploymentRulesafterRestart", "TestPersistDeploymentRules"));
        DeploymentRule deploymentRule1 = new DeploymentRule();
        deploymentRule1.setDisplayName("Rule3");
        deploymentRule1.setRuleID("3");
        deploymentRule1.setType(true);
        try {
            fioranoDeploymentManager.addDeploymentRule(deploymentRule1);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddDeploymentRulesafterRestart", "TestPersistDeploymentRules"), e);
            Assert.fail("Failed to add new deployment rule!", e);
        }
        DeploymentRule deploymentRule2 = new DeploymentRule();
        deploymentRule2.setDisplayName("Rule4");
        deploymentRule2.setRuleID("4");
        deploymentRule2.setType(false);
        try {
            fioranoDeploymentManager.addDeploymentRule(deploymentRule2);
        } catch (TifosiException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddDeploymentRulesafterRestart", "TestPersistDeploymentRules"), e);
            Assert.fail("Failed to add new deployment rule!", e);
        }
        if (!(get_no_of_rules() == 4)) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("TestaddDeploymentRulesafterRestart", "TestPersistDeploymentRules"));
            Assert.fail("Failed to persist deployment rules after FES Restart!");
        }

        Logger.Log(Level.INFO, Logger.getFinishMessage("TestaddDeploymentRules", "TestPersistDeploymentRules"));
    }


    private int get_no_of_rules() {
        int count = 0;
        FioranoStaxParser fioranoStaxParser;
        try {
            fioranoStaxParser = new FioranoStaxParser(new FileInputStream(new File(fioranohome + File.separator + rulesDB)));
            if (fioranoStaxParser.markCursor("rules"))
                count = Integer.parseInt(fioranoStaxParser.getAttributeValue(null, "count"));
            fioranoStaxParser.close();
        } catch (FileNotFoundException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("get_no_of_rules", "TestPersistDeploymentRules"), e);
            Assert.fail("Rules DB File not found", e);
        } catch (XMLStreamException e) {
            Logger.Log(Level.SEVERE, Logger.getErrMessage("get_no_of_rules", "TestPersistDeploymentRules"), e);
            Assert.fail("Failed to parse rules DB xml file", e);
        }
        return count;
    }
}
