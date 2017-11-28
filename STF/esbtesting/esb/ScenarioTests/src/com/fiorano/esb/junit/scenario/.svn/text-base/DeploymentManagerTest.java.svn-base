package com.fiorano.esb.junit.scenario;

import com.fiorano.esb.rtl.dm.FioranoDeploymentManager;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.TestCaseElement;
import com.fiorano.stf.test.core.DRTTestCase;

import java.util.Enumeration;
import java.util.Vector;
import java.util.ArrayList;
import java.util.logging.Level;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.Assert;
import fiorano.tifosi.dmi.dm.DeploymentRule;
import fiorano.tifosi.dmi.dm.DeploymentLabel;

/**
 * Created by IntelliJ IDEA.
 * User: Prasanth
 * Date: Feb 27, 2007
 * Time: 3:20:29 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeploymentManagerTest extends DRTTestCase {
    //private String propFilePath;
    private boolean m_initialised;
    // since the rule IDs are created on the fly, we need to keep it static to use in diff functions
    private static String m_RuleID1;
    FioranoDeploymentManager m_DeploymentManager;

    public DeploymentManagerTest(String name) {
        super(name);
    }

    public DeploymentManagerTest(TestCaseElement testCaseConfig){
        super(testCaseConfig);
    }

    public static Test suite() {
        return new TestSuite(DeploymentManagerTest.class);
    }

    public void init() throws Exception {

//        m_RuleID1=m_properties.getProperty("ruleid1"); //No point reading ruleIds as they will be generated internally.
//        m_RuleID2=m_properties.getProperty("ruleid2");
//        m_version=m_properties.getProperty(JUnitConstants.COMPONENT_VERSION);
//        m_iconFile=propFilepath+File.separator+m_properties.getProperty(JUnitConstants.ICON_FILE);
//        m_fioranoServiceRepository = m_fioranoServiceProvider.getServiceRepository();
//        m_newVersion =m_properties.getProperty(JUnitConstants.NEW_VERSION_NUMBER);
//        m_resources = new StringTokenizer(m_properties.getProperty(JUnitConstants.RESOURCES),",");
//        m_resourceFile = propFilepath+File.separator+m_properties.getProperty(JUnitConstants.RESOURCE_FILE);
        printInitParams();

    }

    protected void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("Rule ID1 : " + m_RuleID1);
//        System.out.println("Rule ID2" + m_RuleID2);
        //System.out.println("Application GUID::       " + m_appGUID + "  Version Number::" + m_version);
        //System.out.println("Application File" + m_appFile);
        System.out.println("_____________________________________________________________________________");
    }

    public void setUp() throws Exception {
        super.setUp();
        if (!m_initialised) {
            m_DeploymentManager = rtlClient.getFioranoDeploymentManager();
            //propFilePath = directory.getAbsolutePath() + File.separator + "tests" + File.separator + "scenario" + File.separator + "LogTest";
            init();
            m_initialised = true;
        }
    }

     public void testIsAllowByDefault() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testIsAllowByDefault", "DeploymentManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            m_DeploymentManager.isAllowAllByDefault();  //Needs modification??
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testIsAllowByDefault", "DeploymentManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testIsAllowByDefault", "DeploymentManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

     public void testAddDeploymentRule() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAddDeploymentRule", "DeploymentManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            DeploymentRule dr = new DeploymentRule();
//            dr.setRuleID(m_RuleID1);
            dr.setDisplayName("testDisplayName");
            dr.setType(true);
            m_RuleID1 = m_DeploymentManager.addDeploymentRule(dr);

            DeploymentRule rule = m_DeploymentManager.getDeploymentRule(m_RuleID1);
            Assert.assertNotNull(rule);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAddDeploymentRule", "DeploymentManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAddDeploymentRule", "DeploymentManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testUpdateDeploymentRule() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testUpdateDeploymentRule", "DeploymentManagerTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testUpdateDeploymentRule", "DeploymentManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testUpdateDeploymentRule", "DeploymentManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

     public void RemoveDeploymentRuleTest() {
        try {
            System.out.println("Started the Execution of the TestCase::" + getName());

            m_DeploymentManager.removeDeploymentRule(m_RuleID1);
            Assert.assertNull(m_DeploymentManager.getDeploymentRule(m_RuleID1));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testGetDeploymentRules() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetDeploymentRules", "DeploymentManagerTest"));
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
                System.out.println("Fetched deploymentrule with id::"+drList[i].getRuleID());
            }

            m_DeploymentManager.removeDeploymentRule(RuleID1);
            m_DeploymentManager.removeDeploymentRule(RuleID2);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetDeploymentRules", "DeploymentManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetDeploymentRules", "DeploymentManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

     public void testGetServiceLabels() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetServiceLabels", "DeploymentManagerTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetServiceLabels", "DeploymentManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetServiceLabels", "DeploymentManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testGetApplicationLabels() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetApplicationLabels", "DeploymentManagerTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetApplicationLabels", "DeploymentManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetApplicationLabels", "DeploymentManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testGetPeerLabels() {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetPeerLabels", "DeploymentManagerTest"));
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
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetPeerLabels", "DeploymentManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetPeerLabels", "DeploymentManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testUpdateDeploymentOrderRules() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testUpdateDeploymentOrderRules", "DeploymentManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            Vector list = new Vector();

            System.out.println("Before update order");
            DeploymentRule[] drList1 = m_DeploymentManager.getDeploymentRules();
            for(int i=0; i<drList1.length; i++)
            {
                System.out.println("Fetched deploymentrule with id::"+drList1[i].getRuleID());
                list.add(drList1[i].getRuleID());
            }

            String[] newList = new String[list.size()];
            newList[0] =  (String)list.lastElement();
            newList[newList.length-1] =  (String)list.firstElement();
            for(int i=1; i<newList.length-1;i++)
            {
               newList[i] = (String)list.get(i);
            }
            m_DeploymentManager.updateDeploymentRuleOrders(newList);

            System.out.println("After update order");
            DeploymentRule[] drList2 = m_DeploymentManager.getDeploymentRules();
            Assert.assertTrue(drList2.length>0);
            for (int i = 0; i < drList2.length; i++) {
                System.out.println("Fetched deploymentrule with id::" + drList2[i].getRuleID());
            }

            Assert.assertTrue(drList1[0].getRuleID().equalsIgnoreCase(drList2[drList2.length-1].getRuleID()));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testUpdateDeploymentOrderRules", "DeploymentManagerTest"));

        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testUpdateDeploymentOrderRules", "DeploymentManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public void testClearDeploymentRules() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testClearDeploymentRules", "DeploymentManagerTest"));
            System.out.println("Started the Execution of the TestCase::" + getName());

            DeploymentRule[] drList1 = m_DeploymentManager.getDeploymentRules();
            for (int i = 0; i < drList1.length; i++) {
                m_DeploymentManager.removeDeploymentRule(drList1[i].getRuleID());
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testClearDeploymentRules", "DeploymentManagerTest"));
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testClearDeploymentRules", "DeploymentManagerTest"), ex);
            Assert.assertTrue("TestCase Failed because of " + ex.getMessage(), false);

        }
    }

    public static ArrayList getOrder(){
	
	ArrayList methodsOrder = new ArrayList();

	methodsOrder.add("testIsAllowByDefault");
	methodsOrder.add("testAddDeploymentRule");
	methodsOrder.add("testUpdateDeploymentRule");
	methodsOrder.add("testGetDeploymentRules");
	methodsOrder.add("testGetServiceLabels");
	methodsOrder.add("testGetApplicationLabels");
	methodsOrder.add("testGetPeerLabels");
	methodsOrder.add("testUpdateDeploymentOrderRules");
	methodsOrder.add("testClearDeploymentRules");

	return methodsOrder;
    }
}
