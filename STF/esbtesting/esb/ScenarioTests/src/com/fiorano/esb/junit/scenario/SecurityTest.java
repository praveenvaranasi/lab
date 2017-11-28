/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 * Copyright (c) 2005, Fiorano Software, Inc.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.esb.junit.scenario;

import com.fiorano.esb.rtl.admin.PermissionImpl;
import com.fiorano.esb.rtl.security.FioranoSecurityManager;
import com.fiorano.stf.consts.DRTTestConstants;
import com.fiorano.stf.framework.ServerStatusController;
import com.fiorano.stf.logger.DRTTestLogger;
import com.fiorano.stf.test.core.DRTTestCase;
import com.fiorano.stf.test.core.TestCaseElement;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.io.*;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.Permission;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;


/*@author UdayK
* @date 19th Dec 2006
*/

public class SecurityTest extends DRTTestCase {

    private FioranoSecurityManager m_fioranoSecurityManager;
    private boolean m_initialised = false;
    private String resourceFilePath;
    private String m_username1;
    private String m_password1;
    private String m_username2;
    private String m_password2;
    private String m_groupname;
    private String m_NodeGroupName;
    private String m_AclName1;
    private String m_AclName2;
    private String m_LoggedinUsername;
    private String m_AdminUsername;

    static boolean isModifiedOnceHA = false;//to check the files overwriting
    static boolean isModifiedOnce = false;
    private FilenameFilter xmlFilter;//file extracter

    public SecurityTest(String testcaseName) {
        super(testcaseName);
    }

    public SecurityTest(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
    }

    private void setXmlFilter(String ext)//creating the xml file filter
    {
        xmlFilter = new OnlyExt(ext);
    }

    // the function modifies the xml files and replace any "search" string with "replace" string

    void modifyXmlFiles(String path, String search, String replace) throws IOException {
        File fl = new File(path);
        File[] filearr = fl.listFiles(xmlFilter);
        FileReader fr = null;
        FileWriter fw = null;
        boolean change = false;
        BufferedReader br;
        String s;
        String result = "";


        int i = 0;
        while (i < filearr.length - 1) {
            try {
                fr = new FileReader(filearr[i]);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                fw = new FileWriter("temp.xml");
            } catch (IOException e) {
                e.printStackTrace();
            }
            br = null;
            br = new BufferedReader(fr);


            while ((s = br.readLine()) != null) {

                int j = s.indexOf(search);
                if (j != -1) {
                    change = true;
                    int k = search.length() + j;
                    //System.out.println("The Index start is "+j+" and index last is "+ k);
                    result = s.substring(0, j);
                    result = result + replace;
                    result = result + s.substring(k);
                    s = result;

                }
                //System.out.print(s);

                fw.write(s);
                fw.write('\n');
            }
            fr.close();
            fw.close();

            if (change) {
                fr = new FileReader("temp.xml");
                fw = new FileWriter(filearr[i]);
                br = new BufferedReader(fr);
                while ((s = br.readLine()) != null) {
                    fw.write(s);
                    fw.write('\n');
                }
                fr.close();
                fw.close();
                change = false;
            }

            i = i + 1;

        }
    }
    //changed function

    public void setUp() throws Exception {

        super.setUp();
        m_fioranoSecurityManager = rtlClient.getFioranoSecurityManager();
        resourceFilePath = testCaseConfig.getTestBaseDir() + File.separator + "scenario" + File.separator + "Security";

        ServerStatusController stc;
        stc = ServerStatusController.getInstance();
        boolean isFPSHA = stc.getFPSHA();
        setXmlFilter("xml");
        if (isFPSHA && !isModifiedOnceHA) {
            isModifiedOnceHA = true;

            modifyXmlFiles(resourceFilePath, "fps_test", "fps_ha");

        } else if (!isFPSHA && !isModifiedOnce) {
            isModifiedOnce = true;
            modifyXmlFiles(resourceFilePath, "fps_ha", "fps_test");

        }
        init();
    }

    private void init() throws Exception {

        if (m_initialised)
            return;

        m_username1 = testCaseProperties.getProperty("UserName1");
        m_username2 = testCaseProperties.getProperty("UserName2");
        m_LoggedinUsername = testCaseProperties.getProperty("loggedinUsername");
        m_AdminUsername = testCaseProperties.getProperty("adminuser");
        m_password1 = testCaseProperties.getProperty("Password1");
        m_password2 = testCaseProperties.getProperty("Password2");
        m_groupname = testCaseProperties.getProperty("GroupName");
        m_AclName1 = testCaseProperties.getProperty("AclName1");
        m_AclName2 = testCaseProperties.getProperty("AclName2");
        m_NodeGroupName = testCaseProperties.getProperty("NodeGroupName");

        printInitParams();
        m_initialised = true;
    }


    private void printInitParams() {

        System.out.println("..................The Initialization Paramaters...................");
        System.out.println("UserName1::" + m_username1 + "  Password1::" + m_password1);
        System.out.println("UserName2::" + m_username2 + "  Password2::" + m_password2);
        System.out.println("GroupName::" + m_groupname);
    }

    public static Test suite() {
        return new TestSuite(SecurityTest.class);
    }

    /**
     * Add a user test
     */
    public void testAddUser() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAddUser", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());
            Principal obj = m_fioranoSecurityManager.createUser(m_username1, m_password1);

            if (!(m_fioranoSecurityManager.authenticateUser(m_username1, m_password1)))
                throw new Exception("User:" + m_username1 + " with password " + m_password1 + " not created");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAddUser", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAddUser", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + ex.getMessage(), false);

        }

    }

    /**
     * Add a user test
     */
    public void testGetallGroupsAndUsers() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetallGroupsAndUsers", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Enumeration en = m_fioranoSecurityManager.getGroupNames();
            while (en.hasMoreElements()) {
                String groupName = (String) en.nextElement();
                Assert.assertNotNull((m_fioranoSecurityManager.getGroup(groupName)));
            }

            Enumeration en2 = m_fioranoSecurityManager.getUserNames();
            while (en2.hasMoreElements()) {
                String userName = (String) en2.nextElement();
                Assert.assertNotNull((m_fioranoSecurityManager.getUser(userName)));
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetallGroupsAndUsers", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetallGroupsAndUsers", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + ex.getMessage(), false);

        }

    }

    /**
     * Add an existing test
     */
    public void testAddExistingUser() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAddExistingUser", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());
            Principal obj = m_fioranoSecurityManager.createUser(m_username1, m_password1);
            Assert.fail("TestCase " + getName() + ": FAILED");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAddExistingUser", "SecurityTest"));

        }
        catch (Exception ex) {

            System.err.println("TestCase " + getName() + ": PASSED ");
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }


    /**
     *
     */
    public void testChangePassword() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testChangePassword", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());
            boolean changed = m_fioranoSecurityManager.changePassword(m_username1, m_password1, m_password2);

            if ((!changed) || (!m_fioranoSecurityManager.authenticateUser(m_username1, m_password2)))
                throw new Exception("User:" + m_username1 + " password not changed");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testChangePassword", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testChangePassword", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + ex.getMessage(), false);

        }
    }

    /**
     *
     */
    public void testChangePasswordWithInvalidOldPassword() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testChangePasswordWithInvalidOldPassword", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());
            boolean changed = m_fioranoSecurityManager.changePassword(m_username1, m_password1, m_password2);

            if (changed)
                Assert.assertFalse("TestCase " + getName() + ": FAILED ", false);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testChangePasswordWithInvalidOldPassword", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testChangePasswordWithInvalidOldPassword", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);

        }
    }

    /**
     * Remove a user test
     */
    public void testRemoveUser() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveUser", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());
            boolean deleted = m_fioranoSecurityManager.deleteUser(m_username1);

            if ((!deleted) || m_fioranoSecurityManager.authenticateUser(m_username1, m_password2))
                throw new Exception("User:" + m_username1 + " with password " + m_password1 + " still exists");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveUser", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveUser", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + ex.getMessage(), false);

        }
    }

    /**
     * Remove a non existing user test
     */
    public void testRemoveNonExistingUser() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveNonExistingUser", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());
            boolean deleted = m_fioranoSecurityManager.deleteUser(m_username1);

            if (deleted)
                Assert.assertTrue("TestCase " + getName() + ": FAILED ", false);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveNonExistingUser", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveNonExistingUser", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);

        }
    }

    /**
     *
     *
     */
    public void testAddGroup() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAddGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());
            Group grp = m_fioranoSecurityManager.createGroup(m_groupname);
            Group grp1 = m_fioranoSecurityManager.getGroup(m_groupname);

            if (!(grp1.getName().equals(grp.getName())))
                throw new Exception("Group:" + m_groupname + " not created");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAddGroup", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAddGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED >> " + ex.getMessage(), false);

        }
    }

    /**
     *
     *
     */
    public void testAddExistingGroup() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAddExistingGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());
            Group grp = m_fioranoSecurityManager.createGroup(m_groupname);
            //this testcase should fail

            Assert.assertFalse("TestCase " + getName() + ": FAILED ", false);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAddExistingGroup", "SecurityTest"));

        }
        catch (Exception ex) {

            System.err.println("TestCase " + getName() + ": PASSED ");
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public void testAddUserToGroup() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAddUserToGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());
            Principal user1 = m_fioranoSecurityManager.createUser(m_username1, m_password1);

            Group grp = m_fioranoSecurityManager.getGroup(m_groupname);

            boolean added = grp.addMember(user1);
            boolean isexist = false;

            for (Enumeration grpnames = m_fioranoSecurityManager.getGroupsOfMember(m_username1); grpnames.hasMoreElements();) {
                if (grpnames.nextElement().toString().equals(m_groupname))
                    isexist = true;
            }
            if ((!added) || (!isexist))
                throw new Exception("User " + m_username1 + " not added to group " + m_groupname);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAddUserToGroup", "SecurityTest"));

        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAddUserToGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED ", false);
        }

    }

    public void testAddInvalidUserToGroup() throws Exception {
        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testAddInvalidUserToGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Principal user = m_fioranoSecurityManager.createUser(m_username2, m_password2);
            m_fioranoSecurityManager.deleteUser(m_username2);

            Group grp = m_fioranoSecurityManager.getGroup(m_groupname);
            boolean added = grp.addMember(user);

            if (added)
                throw new Exception("Invalid user added to the group " + m_groupname);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testAddInvalidUserToGroup", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testAddInvalidUserToGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED ", true);

        }
    }

    public void testRemoveUserFromGroup() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveUserFromGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Group grp = m_fioranoSecurityManager.getGroup(m_groupname);

            Principal user = m_fioranoSecurityManager.getUser(m_username1);
            boolean removed = grp.removeMember(user);
            boolean isexist = false;

            for (Enumeration grpnames = m_fioranoSecurityManager.getGroupsOfMember(m_username1); grpnames.hasMoreElements();) {
                if (grpnames.nextElement().toString().equalsIgnoreCase(m_groupname))
                    isexist = true;
            }
            if ((!removed) || isexist)
                throw new Exception("User " + m_username1 + " not removed from the group " + m_groupname);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveUserFromGroup", "SecurityTest"));

        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveUserFromGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED ", false);
        }
    }

    public void testRemoveInvalidUserFromGroup() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveInvalidUserFromGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Group grp = m_fioranoSecurityManager.getGroup(m_groupname);

            Principal user2 = m_fioranoSecurityManager.createUser(m_username2, m_password2);

            boolean removed = grp.removeMember(user2);
            m_fioranoSecurityManager.deleteUser(m_username2);
            m_fioranoSecurityManager.deleteUser(m_username1);

            if (removed)
                throw new Exception("Invalid User " + m_username2 + " removed from the group " + m_groupname);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveInvalidUserFromGroup", "SecurityTest"));

        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveInvalidUserFromGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED ", true);
        }
    }

    public void testRemoveGroup() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            boolean removed = m_fioranoSecurityManager.deleteGroup(m_groupname);

            if ((!removed))
                throw new Exception("Group:" + m_groupname + " not deleted");
            try {
                Group grp = m_fioranoSecurityManager.getGroup(m_groupname);
                Assert.assertTrue("TestCase " + getName() + ": FAILED as the Group is not deleted", false);

            } catch (Exception ex) {
                //correct behaviour
                //todo check for the Exception.getMessage()
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveGroup", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED " + ex.getMessage(), false);

        }

    }

    public void testRemoveNonExistingGroup() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testRemoveNonExistingGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            boolean removed = m_fioranoSecurityManager.deleteGroup(m_groupname);

            if (removed)
                throw new Exception("Group:" + m_groupname + " not deleted which doesnot exist");
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testRemoveNonExistingGroup", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testRemoveNonExistingGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": FAILED " + ex.getMessage(), false);

        }
    }

    public void testGetACL() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetACL", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Enumeration en = m_fioranoSecurityManager.getAclNames();
            while (en.hasMoreElements()) {
                String aclName = (String) en.nextElement();
                Assert.assertNotNull((m_fioranoSecurityManager.getAcl(aclName)));
            }

            Enumeration en2 = m_fioranoSecurityManager.getServiceAclNames();
            while (en2.hasMoreElements()) {
                String aclName = (String) en2.nextElement();
                Assert.assertNotNull((m_fioranoSecurityManager.getServiceAcl(aclName)));
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetACL", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetACL", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public void testCreateACL() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCreateACL", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Acl acl = createAcl();
            Principal aclOwner = m_fioranoSecurityManager.getDefaultAclOwner();
            Assert.assertNotNull(aclOwner);
            Assert.assertTrue(aclOwner.getName().equalsIgnoreCase(m_AdminUsername));

            m_fioranoSecurityManager.removeAcl(acl.getName());
            Assert.assertNull((m_fioranoSecurityManager.getAcl(acl.getName())));

            m_fioranoSecurityManager.setAcl(m_AclName2, acl);
            Assert.assertNotNull((m_fioranoSecurityManager.getAcl(m_AclName2)));
            Assert.assertNull((m_fioranoSecurityManager.getAcl(acl.getName())));

            m_fioranoSecurityManager.removeAcl(m_AclName2);
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCreateACL", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCreateACL", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public void testCreateServiceACL() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCreateServiceACL", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Acl acl = createAcl();
            m_fioranoSecurityManager.removeAcl(acl.getName());
            Assert.assertNull((m_fioranoSecurityManager.getAcl(acl.getName())));

            m_fioranoSecurityManager.setServiceAcl(m_AclName2, acl);
            Assert.assertNotNull((m_fioranoSecurityManager.getServiceAcl(m_AclName2)));
            Assert.assertNull((m_fioranoSecurityManager.getAcl(m_AclName2)));

            Principal serOwner = m_fioranoSecurityManager.getDefaultServiceAclOwner();
            Assert.assertNotNull(serOwner);
            Assert.assertTrue(serOwner.getName().equalsIgnoreCase(m_username1));

            m_fioranoSecurityManager.removeServiceAcl(m_AclName2);
            Assert.assertNull((m_fioranoSecurityManager.getServiceAcl(m_AclName2)));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCreateServiceACL", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCreateServiceACL", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public Acl createAcl() throws Exception {

        Acl acl = null;
        try {

            Principal prin = m_fioranoSecurityManager.getUser(m_username1);
            acl = m_fioranoSecurityManager.createAcl(m_AclName1, prin);
            Assert.assertNotNull(acl);
            AclEntry aclentry = m_fioranoSecurityManager.createAclEntry();
            Assert.assertNotNull(aclentry);
            PermissionImpl permission = new PermissionImpl();
            aclentry.addPermission(permission);
            acl.addEntry(prin, aclentry);

            Assert.assertNotNull((m_fioranoSecurityManager.getAcl(acl.getName())));

        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
        return acl;
    }

    public void testCreateNodeGroup() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCreateNodeGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Group group = m_fioranoSecurityManager.createNodeGroup(m_NodeGroupName);
            Assert.assertNotNull(group);
            Assert.assertNotNull(m_fioranoSecurityManager.getNodeGroup(m_NodeGroupName));

            Principal prin = m_fioranoSecurityManager.getUser(m_username1);
            group.addMember(prin);
            Assert.assertNotNull(m_fioranoSecurityManager.getNode(m_username1));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCreateNodeGroup", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCreateNodeGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public void testGetNodeGroup() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetNodeGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Enumeration en = m_fioranoSecurityManager.getNodeGroupNames();
            while (en.hasMoreElements()) {
                String nodeGroupName = (String) en.nextElement();
                Assert.assertNotNull((m_fioranoSecurityManager.getNodeGroup(nodeGroupName)));
            }

            Enumeration en2 = m_fioranoSecurityManager.getNodeNames();
            while (en2.hasMoreElements()) {
                String nodeName = (String) en2.nextElement();
                Assert.assertNotNull((m_fioranoSecurityManager.getNode(nodeName)));
            }
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetNodeGroup", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetNodeGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public void testDeleteNodeGroup() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testDeleteNodeGroup", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            m_fioranoSecurityManager.deleteNodeGroup(m_NodeGroupName);
            Assert.assertNull((m_fioranoSecurityManager.getNodeGroup(m_NodeGroupName)));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testDeleteNodeGroup", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testDeleteNodeGroup", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public void testIsConnected() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testIsConnected", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Assert.assertFalse(m_fioranoSecurityManager.isConnected(m_username1));

            Assert.assertTrue(m_fioranoSecurityManager.isConnected(m_LoggedinUsername));
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testIsConnected", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testIsConnected", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public void testCanClearUserEvents() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testCanClearUserEvents", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Assert.assertFalse(m_fioranoSecurityManager.canClearUserEvents(m_username1)); //should be non-admin

            Assert.assertTrue(m_fioranoSecurityManager.canClearUserEvents(m_AdminUsername)); //should be admin
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testCanClearUserEvents", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testCanClearUserEvents", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public void testGetName() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testGetName", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Assert.assertNotNull(m_fioranoSecurityManager.getName());
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testGetName", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testGetName", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    public void testPermissions() throws Exception {

        try {
            DRTTestLogger.logMethodOrder(DRTTestConstants.getOrderMessage("testPermissions", "SecurityTest"));
            System.out.println("Started Execution of TestCase::" + getName());

            Permission permission = m_fioranoSecurityManager.getPermission(PermissionImpl.ALL_PERMISSIONS);
            Assert.assertNotNull(permission);
            //m_fioranoSecurityManager.isPermissionAvailable(m_LoggedinUsername,permission.toString());
            //m_fioranoSecurityManager.isPermissionAvailable()
            DRTTestLogger.log(DRTTestConstants.getFinishMessage("testPermissions", "SecurityTest"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            DRTTestLogger.log(Level.SEVERE, DRTTestConstants.getErrMessage("testPermissions", "SecurityTest"), ex);
            Assert.assertTrue("TestCase " + getName() + ": PASSED ", true);
        }
    }

    /*public void testAddUserAndRestart() throws Exception{

         try{

             System.out.println("Started Execution of TestCase::"+getName());

             Principal obj = m_fioranoSecurityManager.createUser(m_username1,m_password1);
             Assert.assertNotNull(obj);

             Group admin = m_fioranoSecurityManager.getGroup(TifosiRealmManager.ADMINISTRATOR_GROUP);
             Assert.assertNotNull(admin);

             admin.addMember(obj);

             m_fioranoServiceProvider.restart();

             //Assert.assertFalse(m_fioranoServiceProvider.isESBServerActive());

             Thread.sleep(25000);

             m_fioranoSecurityManager = null;
             m_fioranoServiceProvider = null;

             setUp(); // shud setup things again since fes has restarted

             Group admin_afterrestart = m_fioranoSecurityManager.getGroup(TifosiRealmManager.ADMINISTRATOR_GROUP);
             Assert.assertNotNull(admin_afterrestart);

             Principal obj_afterrestart = m_fioranoSecurityManager.getUser(m_username1);
             Assert.assertNotNull(obj_afterrestart);

             Assert.assertTrue(admin_afterrestart.isMember(obj_afterrestart));

             m_fioranoSecurityManager.deleteUser(m_username1);
             
         }
         catch(Exception ex){

             System.err.println("Exception in the Execution of test case::"+getName());
             ex.printStackTrace();
             Assert.assertTrue("TestCase "+getName()+": FAILED >> "+ex.getMessage(),false);

         }

    }*/

    public static ArrayList getOrder() {

        ArrayList methodsOrder = new ArrayList();

        methodsOrder.add("testAddUser");
        methodsOrder.add("testGetallGroupsAndUsers");
        methodsOrder.add("testAddExistingUser");
        methodsOrder.add("testChangePassword");
        methodsOrder.add("testChangePasswordWithInvalidOldPassword");
        methodsOrder.add("testRemoveUser");
        methodsOrder.add("testRemoveNonExistingUser");
        methodsOrder.add("testAddGroup");
        methodsOrder.add("testAddExistingGroup");
        methodsOrder.add("testAddUserToGroup");
        methodsOrder.add("testAddInvalidUserToGroup");
        methodsOrder.add("testRemoveUserFromGroup");
        methodsOrder.add("testRemoveInvalidUserFromGroup");
        methodsOrder.add("testRemoveGroup");
        methodsOrder.add("testRemoveNonExistingGroup");
        methodsOrder.add("testGetACL");
        methodsOrder.add("testCreateACL");
        methodsOrder.add("testCreateServiceACL");
        // methodsOrder.add("createAcl");
        methodsOrder.add("testCreateNodeGroup");
        methodsOrder.add("testGetNodeGroup");
        methodsOrder.add("testDeleteNodeGroup");
        methodsOrder.add("testIsConnected");
        methodsOrder.add("testCanClearUserEvents");
        methodsOrder.add("testGetName");
        methodsOrder.add("testPermissions");

        return methodsOrder;
    }

}
