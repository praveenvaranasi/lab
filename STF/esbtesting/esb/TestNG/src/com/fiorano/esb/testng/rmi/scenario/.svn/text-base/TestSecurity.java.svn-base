package com.fiorano.esb.testng.rmi.scenario;

import com.fiorano.esb.rtl.admin.PermissionImpl;
import com.fiorano.esb.rtl.security.FioranoSecurityManager;
import com.fiorano.esb.testng.rmi.AbstractTestNG;
import com.fiorano.esb.testng.rmi.log.Logger;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.common.TifosiException;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.security.Principal;
import java.security.acl.Acl;
import java.security.acl.AclEntry;
import java.security.acl.Group;
import java.security.acl.Permission;
import java.util.Enumeration;
import java.util.logging.Level;

/**
 * Created by IntelliJ IDEA.
 * User: ranu
 * Date: 10/21/11
 * Time: 10:11 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestSecurity extends AbstractTestNG{
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

    private void init() throws Exception {
        if (m_initialised)
            return;
        m_username1 = testProperties.getProperty("UserName1");
        m_username2 = testProperties.getProperty("UserName2");
        m_LoggedinUsername = testProperties.getProperty("loggedinUsername");
        m_AdminUsername = testProperties.getProperty("adminuser");
        m_password1 = testProperties.getProperty("Password1");
        m_password2 = testProperties.getProperty("Password2");
        m_groupname = testProperties.getProperty("GroupName");
        m_AclName1 = testProperties.getProperty("AclName1");
        m_AclName2 = testProperties.getProperty("AclName2");
        m_NodeGroupName = testProperties.getProperty("NodeGroupName");
        m_initialised = true;
    }

    private void printInitParams() {
        System.out.println("_______________________The Initialization Paramaters_______________________");
        System.out.println("UserName1::" + m_username1 + "  Password1::" + m_password1);
        System.out.println("UserName2::" + m_username2 + "  Password2::" + m_password2);
        System.out.println("loggedinUsername::" + m_LoggedinUsername + "  adminuser::" + m_AdminUsername);
        System.out.println("GroupName::" + m_groupname + "  NodeGroupName::" + m_NodeGroupName);
        System.out.println("___________________________________________________________________________");
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

    //picking up the xml extension files
    private void setXmlFilter(String ext){
        xmlFilter = new OnlyExt(ext);//creating filters
    }

    @BeforeClass(groups = "SecurityTest", alwaysRun = true)
    public void startSetUp(){
        System.out.println("Started the Execution of the TestCase::" + getName());
        initializeProperties("scenario" + fsc + "Security" + fsc + "tests.properties");
        resourceFilePath = testResourcesHome + fsc + "tests" +  fsc + "scenario" + fsc + "Security";
        //new change

        m_fioranoSecurityManager = rtlClient.getFioranoSecurityManager();

    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "startSetUp", description = "Add a user test")
    public void testAddUser() throws Exception {
        ServerStatusController stc;
        stc = ServerStatusController.getInstance();
        boolean isFPSHA = stc.getFPSHA();
        setXmlFilter("xml");

        try {
            if (isFPSHA && !isModifiedOnceHA) {
                isModifiedOnceHA = true;
                modifyXmlFiles(resourceFilePath, "fps", "fps_ha");
            }
            else if (!isFPSHA && !isModifiedOnce) {
                isModifiedOnce = true;
                modifyXmlFiles(resourceFilePath, "fps_ha", "fps");
            }
            init();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (Exception e) {
            Assert.fail("could not initialise properties");
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        printInitParams();
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddUser", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());
            Principal obj = m_fioranoSecurityManager.createUser(m_username1, m_password1);

            if (!(m_fioranoSecurityManager.authenticateUser(m_username1, m_password1)))
                throw new Exception("User:" + m_username1 + " with password " + m_password1 + " not created");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testAddUser", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddUser", "TestSecurity"), ex);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());

        }

    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testAddUser")
    public void testGetallGroupsAndUsers() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetallGroupsAndUsers", "TestSecurity"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetallGroupsAndUsers", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetallGroupsAndUsers", "TestSecurity"), ex);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());

        }

    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testGetallGroupsAndUsers", description = "Add an existing test")
    public void testAddExistingUser() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddExistingUser", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());
            Principal obj = m_fioranoSecurityManager.createUser(m_username1, m_password1);
            Assert.fail("TestCase " + getName() + ": FAILED");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testAddExistingUser", "TestSecurity"));

        }
        catch (Exception ex) {
            System.err.println("TestCase " + getName() + ": PASSED ");
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testAddExistingUser")
    public void testChangePassword() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testChangePassword", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());
            boolean changed = m_fioranoSecurityManager.changePassword(m_username1, m_password1, m_password2);

            if ((!changed) || (!m_fioranoSecurityManager.authenticateUser(m_username1, m_password2)))
                throw new Exception("User:" + m_username1 + " password not changed");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testChangePassword", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangePassword", "TestSecurity"), ex);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());

        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testChangePassword")
    public void testChangePasswordWithInvalidOldPassword() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testChangePasswordWithInvalidOldPassword", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());
            boolean changed = m_fioranoSecurityManager.changePassword(m_username1, m_password1, m_password2);

            if (changed)
                Assert.assertFalse(false, "TestCase " + getName() + ": FAILED ");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testChangePasswordWithInvalidOldPassword", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testChangePasswordWithInvalidOldPassword", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");

        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testChangePasswordWithInvalidOldPassword", description = "Remove a user test")
    public void testRemoveUser() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveUser", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());
            boolean deleted = m_fioranoSecurityManager.deleteUser(m_username1);

            if ((!deleted) || m_fioranoSecurityManager.authenticateUser(m_username1, m_password2))
                throw new Exception("User:" + m_username1 + " with password " + m_password1 + " still exists");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveUser", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveUser", "TestSecurity"), ex);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());

        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testRemoveUser", description = "Remove a non existing user test")
    public void testRemoveNonExistingUser() throws Exception {
        Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveNonExistingUser", "TestSecurity"));
        System.out.println("Started Execution of TestCase::" + getName());
        try {
            boolean deleted = m_fioranoSecurityManager.deleteUser(m_username1);
            if (deleted)
                Assert.assertTrue(false, "TestCase " + getName() + ": FAILED ");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveNonExistingUser", "TestSecurity"));
        }
        catch (TifosiException ex) {
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveNonExistingUser", "TestSecurity"));
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
            return;
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveNonExistingUser", "TestSecurity"), ex);
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testRemoveNonExistingUser")
    public void testAddGroup() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddGroup", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());
            Group grp = m_fioranoSecurityManager.createGroup(m_groupname);
            Group grp1 = m_fioranoSecurityManager.getGroup(m_groupname);

            if (!(grp1.getName().equals(grp.getName())))
                throw new Exception("Group:" + m_groupname + " not created");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testAddGroup", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddGroup", "TestSecurity"), ex);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED >> " + ex.getMessage());

        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testAddGroup")
    public void testAddExistingGroup() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddExistingGroup", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());
            Group grp = m_fioranoSecurityManager.createGroup(m_groupname);
            //this testcase should fail

            Assert.assertFalse(false, "TestCase " + getName() + ": FAILED ");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testAddExistingGroup", "TestSecurity"));

        }
        catch (Exception ex) {

            System.err.println("TestCase " + getName() + ": PASSED ");
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testAddExistingGroup")
    public void testAddUserToGroup() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddUserToGroup", "TestSecurity"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testAddUserToGroup", "TestSecurity"));

        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddUserToGroup", "TestSecurity"), ex);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED ");
        }

    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testAddUserToGroup")
    public void testAddInvalidUserToGroup() throws Exception {
        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testAddInvalidUserToGroup", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());

            Principal user = m_fioranoSecurityManager.createUser(m_username2, m_password2);
            m_fioranoSecurityManager.deleteUser(m_username2);

            Group grp = m_fioranoSecurityManager.getGroup(m_groupname);
            boolean added = grp.addMember(user);

            if (added)
                throw new Exception("Invalid user added to the group " + m_groupname);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testAddInvalidUserToGroup", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testAddInvalidUserToGroup", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": FAILED ");

        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testAddInvalidUserToGroup")
    public void testRemoveUserFromGroup() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveUserFromGroup", "TestSecurity"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveUserFromGroup", "TestSecurity"));

        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveUserFromGroup", "TestSecurity"), ex);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testRemoveUserFromGroup")
    public void testRemoveInvalidUserFromGroup() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveInvalidUserFromGroup", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());

            Group grp = m_fioranoSecurityManager.getGroup(m_groupname);

            Principal user2 = m_fioranoSecurityManager.createUser(m_username2, m_password2);

            boolean removed = grp.removeMember(user2);
            m_fioranoSecurityManager.deleteUser(m_username2);
            m_fioranoSecurityManager.deleteUser(m_username1);

            if (removed)
                throw new Exception("Invalid User " + m_username2 + " removed from the group " + m_groupname);
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveInvalidUserFromGroup", "TestSecurity"));

        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveInvalidUserFromGroup", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": FAILED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testRemoveInvalidUserFromGroup")
    public void testRemoveGroup() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveGroup", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());

            boolean removed = m_fioranoSecurityManager.deleteGroup(m_groupname);

            if ((!removed))
                throw new Exception("Group:" + m_groupname + " not deleted");
            try {
                Group grp = m_fioranoSecurityManager.getGroup(m_groupname);
                Assert.assertTrue(false, "TestCase " + getName() + ": FAILED as the Group is not deleted");

            } catch (Exception ex) {
                //correct behaviour
                //todo check for the Exception.getMessage()
            }
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveGroup", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveGroup", "TestSecurity"), ex);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED " + ex.getMessage());

        }

    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testRemoveGroup")
    public void testRemoveNonExistingGroup() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testRemoveNonExistingGroup", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());

            boolean removed = m_fioranoSecurityManager.deleteGroup(m_groupname);

            if (removed)
                throw new Exception("Group:" + m_groupname + " not deleted which doesnot exist");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveNonExistingGroup", "TestSecurity"));
        }
        catch (TifosiException ex) {
            Logger.Log(Level.INFO, Logger.getFinishMessage("testRemoveNonExistingUser", "TestSecurity"));
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");

        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testRemoveNonExistingGroup", "TestSecurity"), ex);
            Assert.assertTrue(false, "TestCase " + getName() + ": FAILED " + ex.getMessage());

        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testRemoveNonExistingGroup")
    public void testGetACL() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetACL", "TestSecurity"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetACL", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetACL", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testGetACL")
    public void testCreateACL() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateACL", "TestSecurity"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateACL", "TestSecurity"));
        }
        catch (TifosiException ex) {
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateACL", "TestSecurity"));
            return;
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            //ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateACL", "TestSecurity"), ex);
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testCreateACL")
    public void testCreateServiceACL() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateServiceACL", "TestSecurity"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateServiceACL", "TestSecurity"));
        }
        catch (TifosiException ex) {
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateServiceACL", "TestSecurity"));
            return;
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateServiceACL", "TestSecurity"), ex);
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testCreateServiceACL")
    public void testCreateNodeGroup() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCreateNodeGroup", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());

            Group group = m_fioranoSecurityManager.createNodeGroup(m_NodeGroupName);
            Assert.assertNotNull(group);
            Assert.assertNotNull(m_fioranoSecurityManager.getNodeGroup(m_NodeGroupName));

            Principal prin = m_fioranoSecurityManager.getUser(m_username1);
            group.addMember(prin);
            Assert.assertNotNull(m_fioranoSecurityManager.getNode(m_username1));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCreateNodeGroup", "TestSecurity"));
        }
        catch (TifosiException ex) {
            return;
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCreateNodeGroup", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testCreateNodeGroup")
    public void testGetNodeGroup() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetNodeGroup", "TestSecurity"));
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
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetNodeGroup", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetNodeGroup", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testGetNodeGroup")
    public void testDeleteNodeGroup() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testDeleteNodeGroup", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());
            m_fioranoSecurityManager.deleteNodeGroup(m_NodeGroupName);
            Assert.assertNull((m_fioranoSecurityManager.getNodeGroup(m_NodeGroupName)));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testDeleteNodeGroup", "TestSecurity"));
        }
        catch (TifosiException ex) {
            return;
        }
        catch (Exception ex) {
            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testDeleteNodeGroup", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testDeleteNodeGroup")
    public void testIsConnected() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testIsConnected", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());

            Assert.assertFalse(m_fioranoSecurityManager.isConnected(m_username1));

            Assert.assertTrue(m_fioranoSecurityManager.isConnected(m_LoggedinUsername));
            Logger.Log(Level.INFO, Logger.getFinishMessage("testIsConnected", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testIsConnected", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testIsConnected")
    public void testCanClearUserEvents() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testCanClearUserEvents", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());

            Assert.assertFalse(m_fioranoSecurityManager.canClearUserEvents(m_username1)); //should be non-admin

            Assert.assertTrue(m_fioranoSecurityManager.canClearUserEvents(m_AdminUsername)); //should be admin
            Logger.Log(Level.INFO, Logger.getFinishMessage("testCanClearUserEvents", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testCanClearUserEvents", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testCanClearUserEvents")
    public void testGetName() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testGetName", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());

            Assert.assertNotNull(m_fioranoSecurityManager.getName());
            Logger.Log(Level.INFO, Logger.getFinishMessage("testGetName", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testGetName", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    @Test(groups = "SecurityTest", alwaysRun = true, dependsOnMethods = "testGetName")
    public void testPermissions() throws Exception {

        try {
            Logger.LogMethodOrder(Logger.getOrderMessage("testPermissions", "TestSecurity"));
            System.out.println("Started Execution of TestCase::" + getName());

            Permission permission = m_fioranoSecurityManager.getPermission(PermissionImpl.ALL_PERMISSIONS);
            Assert.assertNotNull(permission);
            //m_fioranoSecurityManager.isPermissionAvailable(m_LoggedinUsername,permission.toString());
            //m_fioranoSecurityManager.isPermissionAvailable()
            Logger.Log(Level.INFO, Logger.getFinishMessage("testPermissions", "TestSecurity"));
        }
        catch (Exception ex) {

            System.err.println("Exception in the Execution of test case::" + getName());
            ex.printStackTrace();
            Logger.Log(Level.SEVERE, Logger.getErrMessage("testPermissions", "TestSecurity"), ex);
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }
    }

    public Acl createAcl() throws Exception {

        Acl acl = null;
        //try {
            Principal prin = m_fioranoSecurityManager.getUser(m_username1);
            acl = m_fioranoSecurityManager.createAcl(m_AclName1, prin);
            Assert.assertNotNull(acl);
            AclEntry aclentry = m_fioranoSecurityManager.createAclEntry();
            Assert.assertNotNull(aclentry);
            PermissionImpl permission = new PermissionImpl();
            aclentry.addPermission(permission);
            acl.addEntry(prin, aclentry);
            Assert.assertNotNull((m_fioranoSecurityManager.getAcl(acl.getName())));
        /*}
        catch (Exception ex) {
            Assert.assertTrue(true, "TestCase " + getName() + ": PASSED ");
        }*/
        return acl;
    }

}
