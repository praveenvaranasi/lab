package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.dm.FioranoDeploymentManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.dm.DeploymentLabel;
import fiorano.tifosi.dmi.dm.DeploymentRule;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Enumeration;
import java.util.Vector;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/18/11
 * Time: 6:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestDeploymentManager extends AbstractTestNG{
    //private String propFilePath;
    private boolean m_initialised;
    // since the rule IDs are created on the fly, we need to keep it static to use in diff functions
    private static String m_RuleID1;
    FioranoDeploymentManager m_DeploymentManager;
    private String resourceFilePath;

    public void init() throws Exception {
//        m_RuleID1=getProperty("ruleid1"); //No point reading ruleIds as they will be generated internally.
//        m_RuleID2=m_properties.getProperty("ruleid2");
//        m_version=m_properties.getProperty(JUnitConstants.COMPONENT_VERSION);
//        m_iconFile=propFilepath+File.separator+m_properties.getProperty(JUnitConstants.ICON_FILE);
//        m_fioranoServiceRepository = m_fioranoServiceProvider.getServiceRepository();
//        m_newVersion =m_properties.getProperty(JUnitConstants.NEW_VERSION_NUMBER);
//        m_resources = new StringTokenizer(m_properties.getProperty(JUnitConstants.RESOURCES),",");
//        m_resourceFile = propFilepath+File.separator+m_properties.getProperty(JUnitConstants.RESOURCE_FILE);
    }

    protected void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Rule ID1 : " + m_RuleID1);
//        System.out.println("Rule ID2" + m_RuleID2);
        //System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        //System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    @BeforeClass(groups = "DeploymentManagerTest", alwaysRun = true)
    public void startSetUp(){
        Logger.LogMethodOrder(Logger.getOrderMessage("startSetUp","TestDeploymentManager"));
        initializeProperties("scenario" + fsc + "DeploymentManager" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "DeploymentManager";
        m_DeploymentManager = rtlClient.getFioranoDeploymentManager();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveDeploymentRule", "TestDeploymentManager"));
        }
        printInitParams();
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "startSetUp")
    public void testIsAllowByDefault() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testIsAllowByDefault", "TestDeploymentManager"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            m_DeploymentManager.isAllowAllByDefault();  //Needs modification??
            Logger.Log(Level.INFO, Logger.getFinishMessage("testIsAllowByDefault", "TestDeploymentManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testIsAllowByDefault", "TestDeploymentManager"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testIsAllowByDefault")
    public void testAddDeploymentRule() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddDeploymentRule", "TestDeploymentManager"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            DeploymentRule dr = new DeploymentRule();
//            dr.setRuleID(m_RuleID1);
            dr.setDisplayName("testDisplayName");
            dr.setType(true);
            m_RuleID1 = m_DeploymentManager.addDeploymentRule(dr);

            DeploymentRule rule = m_DeploymentManager.getDeploymentRule(m_RuleID1);
            Assert.assertNotNull(rule);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testAddDeploymentRule", "TestDeploymentManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddDeploymentRule", "TestDeploymentManager"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testAddDeploymentRule")
    public void testUpdateDeploymentRule() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testUpdateDeploymentRule", "TestDeploymentManager"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            DeploymentRule rule = m_DeploymentManager.getDeploymentRule(m_RuleID1);
            Assert.assertNotNull(rule);

//            rule.setRuleID(m_RuleID2); // we cannot update rule id using the current API
            String newDisplayName = "newRandomName";
            rule.setDisplayName(newDisplayName);

            m_DeploymentManager.updateDeploymentRule(rule);

            DeploymentRule updatedRule;
            Assert.assertNotNull(updatedRule = m_DeploymentManager.getDeploymentRule(m_RuleID1));
            Assert.assertTrue(newDisplayName.equalsIgnoreCase(updatedRule.getDisplayName()));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testUpdateDeploymentRule", "TestDeploymentManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testUpdateDeploymentRule", "TestDeploymentManager"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testUpdateDeploymentRule")
    public void testRemoveDeploymentRule() {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveDeploymentRule", "TestDeploymentManager"));
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());
            DeploymentRule dr=null;
            m_DeploymentManager.removeDeploymentRule(m_RuleID1);
            try {
                dr = m_DeploymentManager.getDeploymentRule(m_RuleID1);
            } catch (TifosiException e) {
                //ignore
            }
            Assert.assertNull(dr);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveDeploymentRule", "TestDeploymentManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE,Logger.getErrMessage("testRemoveDeploymentRule","TestDeploymentManager"));
        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testRemoveDeploymentRule")
    public void testGetDeploymentRules() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetDeploymentRules", "TestDeploymentManager"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            DeploymentRule dr1 = new DeploymentRule();
//            dr1.setRuleID(m_RuleID1);
            String  RuleID1;
            dr1.setType(true);
            dr1.setDisplayName("rDbMs");
            RuleID1 = m_DeploymentManager.addDeploymentRule(dr1);

            DeploymentRule dr2 = new DeploymentRule();
//            dr2.setRuleID(m_RuleID2);
            String  RuleID2;
            dr1.setDisplayName("RdBmS");
            RuleID2 = m_DeploymentManager.addDeploymentRule(dr2);


            DeploymentRule[] drList = m_DeploymentManager.getDeploymentRules();
            for(int i=0; i<drList.length; i++)
            {
                Assert.assertNotNull(m_DeploymentManager.getDeploymentRule(drList[i].getRuleID()));
                System.out.println("Fetched deployment rule with id::"+drList[i].getRuleID());
            }

        //    m_DeploymentManager.removeDeploymentRule(RuleID1);
        //    m_DeploymentManager.removeDeploymentRule(RuleID2);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetDeploymentRules", "TestDeploymentManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetDeploymentRules", "TestDeploymentManager"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testGetDeploymentRules")
    public void testGetServiceLabels() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetServiceLabels", "TestDeploymentManager"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Enumeration en = m_DeploymentManager.getAllServiceLabels();
            while(en.hasMoreElements())
            {
                DeploymentLabel label = (DeploymentLabel)en.nextElement();
                Assert.assertNotNull(m_DeploymentManager.getServiceLabel(label.getName()));
                System.out.println("Fetched service label::"+label.getName());
            }

            DeploymentLabel defLabel = m_DeploymentManager.getDefaultServiceLabel();
            Assert.assertNotNull(defLabel);
            System.out.println("Default service label::"+defLabel.getName());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetServiceLabels", "TestDeploymentManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetServiceLabels", "TestDeploymentManager"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testGetServiceLabels")
    public void testGetApplicationLabels() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetApplicationLabels", "TestDeploymentManager"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Enumeration en = m_DeploymentManager.getAllApplicationLabels();
            while(en.hasMoreElements())
            {
                DeploymentLabel label = (DeploymentLabel)en.nextElement();
                Assert.assertNotNull(m_DeploymentManager.getApplicationLabel(label.getName()));
                System.out.println("Fetched application label::"+label.getName());
            }

            DeploymentLabel defLabel = m_DeploymentManager.getDefaultApplicationLabel();
            Assert.assertNotNull(defLabel);
            System.out.println("Default application label::"+defLabel.getName());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetApplicationLabels", "TestDeploymentManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetApplicationLabels", "TestDeploymentManager"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testGetApplicationLabels")
    public void testGetPeerLabels() {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetPeerLabels", "TestDeploymentManager"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Enumeration en = m_DeploymentManager.getAllPeerLabels();
            while(en.hasMoreElements())
            {
                DeploymentLabel label = (DeploymentLabel)en.nextElement();
                Assert.assertNotNull(m_DeploymentManager.getPeerLabel(label.getName()));
                System.out.println("Fetched peer label::"+label.getName());
            }

            DeploymentLabel defLabel = m_DeploymentManager.getDefaultPeerLabel();
            Assert.assertNotNull(defLabel);
            System.out.println("Default peer label::"+defLabel.getName());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetPeerLabels", "TestDeploymentManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetPeerLabels", "TestDeploymentManager"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @Test(groups = "DeploymentManagerTest", alwaysRun = true, dependsOnMethods = "testGetPeerLabels")
    public void testUpdateDeploymentOrderRules() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testUpdateDeploymentOrderRules", "TestDeploymentManager"));
            System.out.println("Started the Execution of the TestCase::" + getName());
            Vector list = new Vector();
            System.out.println("Before update order");
            DeploymentRule[] drList1 = m_DeploymentManager.getDeploymentRules();
            int i;
            for(i=0; i<drList1.length; i++)
            {
                System.out.println("Fetched deployment rule with id::"+drList1[i].getRuleID());
                list.add(drList1[i].getRuleID());
            }

            String[] newList = new String[list.size()];
            newList[0] =  (String)list.lastElement();
            newList[list.size()-1] =  (String)list.firstElement();
            for(i=1; i<list.size()-1;i++)
            {
               newList[i] = (String)list.get(i);
            }
            m_DeploymentManager.updateDeploymentRuleOrders(newList);

            System.out.println("After update order");
            DeploymentRule[] drList2 = m_DeploymentManager.getDeploymentRules();
            Assert.assertTrue(drList2.length>0);
            for (i = 0; i < drList2.length; i++) {
                System.out.println("Fetched deployment rule with id::" + drList2[i].getRuleID());
            }

            Assert.assertTrue(drList1[0].getRuleID().equalsIgnoreCase(drList2[drList2.length-1].getRuleID()));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testUpdateDeploymentOrderRules", "TestDeploymentManager"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testUpdateDeploymentOrderRules", "TestDeploymentManager"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }

    @AfterClass(groups = "DeploymentManagerTest", alwaysRun = true)
    public void testClearDeploymentRules() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testClearDeploymentRules", "TestDeploymentManager"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            DeploymentRule[] drList1 = m_DeploymentManager.getDeploymentRules();
            for (int i = 0; i < drList1.length; i++) {
                m_DeploymentManager.removeDeploymentRule(drList1[i].getRuleID());
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testClearDeploymentRules", "TestDeploymentManager"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testClearDeploymentRules", "TestDeploymentManager"), ex);
            Assert.assertTrue(false, "TestCase Failed because of " + ex.getMessage());

        }
    }
}
