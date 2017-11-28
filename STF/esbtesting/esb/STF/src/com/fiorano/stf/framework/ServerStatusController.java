/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.stf.framework;

import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.rtl.server.FioranoServiceProviderFactory;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.test.core.ServerElement;
import com.fiorano.stf.test.core.TCServerElement;
import com.fiorano.stf.test.core.TestEnvironment;
import com.fiorano.stf.test.core.TestSuiteElement;
import fiorano.jms.ha.StatusPacket;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.jndi.InitialContextFactory;

import javax.management.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Dec 10, 2007
 * Time: 3:28:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class ServerStatusController {
    private TestSuiteElement currentSuiteElement;
    private TestEnvironment testEnvironment;
    public FioranoServiceProvider esp;
    private String primaryFESAddress;
    private String secondaryFESAddress;
    private String standaloneFESAddress;
    private String fesLockFileMachineAddress;
    private String fpsLockFileMachineAddress;
    private boolean isFESaHA = false;
    private ServerElement fesServerElem;

    /* the following fps variables willbe set instantly and will be for temporary usage */
    /* reason is there can be any number of fpss used for a test suite. so these will be set instantly and temporarily for the required profile */
    private boolean isFPSaHA = false;
    private String primaryFPSAddress;
    private String secondaryFPSAddress;
    private ServerElement fpsServerElem;
    /*--------------------------------------------------------------------------------- */

    private final int FES_PRIMARY_RMI_PORT = 2047;
    private final int FES_SECONDARY_RMI_PORT = 2048;
    private final int FPS_PRIMARY_RMI_PORT = 2067;
    private final int FPS_SECONDARY_RMI_PORT = 2068;
    private final int AMS_PRIMARY_RMI_PORT = 2347;
    private final int AGS_PRIMARY_RMI_PORT = 2367;


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MAKING SINGLETON starts ~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    private static ServerStatusController ref;

    private ServerStatusController() {
    }

    public synchronized static ServerStatusController getInstance() {
        if (ref == null) ref = new ServerStatusController();
        return ref;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ MAKING SINGLETON ends ~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public void setCurrentSuiteElement(TestSuiteElement currentSuiteElement) {
        this.currentSuiteElement = currentSuiteElement;
    }

    public TestSuiteElement getCurrentSuiteElement() {
        return currentSuiteElement;
    }

    public void setTestEnvironment(TestEnvironment testEnvironment) {
        this.testEnvironment = testEnvironment;
    }

    public TestEnvironment getTestEnvironment() {
        return testEnvironment;
    }

    public String getPrimaryFESAddress() {
        return primaryFESAddress;
    }

    public String getPrimaryFPSAddress() {
        return primaryFPSAddress;
    }

    public String getSecondaryFESAddress() {
        return secondaryFESAddress;
    }

    public String getSecondaryFPSAddress() {
        return secondaryFPSAddress;
    }

    public String getStandaloneFESAddress() {
        return standaloneFESAddress;
    }

    public void setStandaloneFESAddress(String standaloneFESAddress) {
        this.standaloneFESAddress = standaloneFESAddress;
    }

    public String getFesLockFileMachineAddress() {
        return fesLockFileMachineAddress;
    }

    public String getFpsLockFileMachineAddress() {
        return fpsLockFileMachineAddress;
    }


    public FioranoServiceProvider getServiceProvider() throws STFException {
        generateESP();
        return esp;
    }

    public void refresh() {
        currentSuiteElement = null;
        esp = null;
        primaryFESAddress = null;
        secondaryFESAddress = null;
        primaryFPSAddress = null;
        secondaryFPSAddress = null;
        isFESaHA = false;
        isFPSaHA = false;
        fesServerElem = null;
        fpsServerElem = null;
    }

    public void initiate() {
        try {
            setFESServerElem();
            setFESAddress();
            currentSuiteElement.setEnterpriseServer(getAddress(getURLOnFESActive()));
            System.out.println(currentSuiteElement.getEnterpriseServer());
            generateESP();
        } catch (STFException e) {
            e.printStackTrace();
        }
    }

    public void printParams() {
        System.out.println("****************************************");
        System.out.println("isFESaHA : " + isFESaHA);
        System.out.println("primary fes address: " + primaryFESAddress);
        System.out.println("secondary fes address: " + secondaryFESAddress);
        System.out.println("****************************************");
    }

    public void generateESP() throws STFException {
        esp = null;
        String fesurl = null;
        fesurl = getURLOnFESActive();
        if (fesurl != null){ esp = getEnterpriseServiceProvider(fesurl);
            System.out.println(esp);}
        else throw new STFException("fes is not active");
    }


    public String getPrimaryFESStatus() throws STFException {
        return getServerStatus(primaryFESAddress, FES_PRIMARY_RMI_PORT);
    }

    public String getSecondaryFESStatus() throws STFException {
        return getServerStatus(secondaryFESAddress, FES_SECONDARY_RMI_PORT);
    }

    public String getPrimaryFPSStatus(String profileName) throws STFException {
        setFPSAddress(profileName);
        return getServerStatus(primaryFPSAddress, FPS_PRIMARY_RMI_PORT);
    }

    public String getSecondaryFPSStatus(String profileName) throws STFException {
        setFPSAddress(profileName);
        return getServerStatus(secondaryFPSAddress, FPS_SECONDARY_RMI_PORT);
    }

    /* -------------------------------- needn't be exposed ---------------------------------------- STARTS */

    private void sleep(long interval) {
        try {
            Thread.sleep(interval);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean getServerExistence()
    {
        Set<String> apiServer = currentSuiteElement.getServers().keySet();
        if (apiServer.contains(TestEnvironmentConstants.AMS) || apiServer.contains(TestEnvironmentConstants.AGS))
            return true;
        else
            return false;
    }

    private String getUrl(String hostName, boolean isPrimary) {
        //todo on case port number is configurable, set that here.
        if (isPrimary && !getServerExistence())
            return "tsp_tcp://" + hostName + ":1947";
        else if (isPrimary && getServerExistence())
            return "tsp_tcp://" + hostName + ":2247";
        else if (!isPrimary && !getServerExistence())
            return "tsp_tcp://" + hostName + ":1948"; //this 1948 should be taken from FESHASecondary/conf/Configs.xml. this may change.
        else
            return "tsp_tcp://" + hostName + ":2248";


    }

    private String getAddress(String url) {
        if (url == null) return null;

        int protocolEnd = url.indexOf("//");
        int addBegin;
        if (protocolEnd == -1) addBegin = 0;
        else addBegin = protocolEnd + 2;

        int addEnd = url.indexOf(':', addBegin);
        if (addEnd == -1) addEnd = url.length();

        return url.substring(addBegin, addEnd);
    }

    private InitialContext createInitialContext(String url, Vector backupUrls) throws NamingException {
        Hashtable env = new Hashtable();
        env.put(Context.PROVIDER_URL, url);
        env.put(Context.INITIAL_CONTEXT_FACTORY, InitialContextFactory.class.getName());
        String backupURLString = getBackupUrls(backupUrls);
        if (backupURLString != null)
            env.put("BackupConnectURLs", backupURLString);

        InitialContext context;
        try {
            context = new InitialContext(env);
        }
        catch (NamingException ex) {
            throw ex;
        }

        return context;

    }

    private String getBackupUrls(Vector backupUrls) {
        if (backupUrls == null || backupUrls.isEmpty())
            return null;
        String toReturn = null;
        for (int i = 1; i <= backupUrls.size(); i++) {
            toReturn = (String) backupUrls.get(i - 1);
            if (!(i == backupUrls.size()))
                toReturn = toReturn + ":";

        }
        return toReturn;
    }

    private Enumeration getServerElements() throws STFException {
        Enumeration servers = currentSuiteElement.getServers().elements();
        Vector serverElements = new Vector();

        for (; servers.hasMoreElements();) {
            TCServerElement server = (TCServerElement) servers.nextElement();
            if (!testEnvironment.getServersList().containsKey(server.getName()))
                throw new STFException("Server " + server.getName() + " not definined in the TestEnvironment.");
            ServerElement serverElem = (ServerElement) testEnvironment.getServersList().get(server.getName());

            serverElements.add(serverElem);
        }

        return serverElements.elements();
    }

    private void setFESAddress() throws STFException {
        if (fesServerElem == null) setFESServerElem();
        if (fesServerElem == null) throw new STFException("fesServerElem not set");

        if (isFESaHA == true) {

            primaryFESAddress = testEnvironment.getMachine(fesServerElem.getProfileElements().get("primary").getMachineName()).getAddress();
            secondaryFESAddress = testEnvironment.getMachine(fesServerElem.getProfileElements().get("secondary").getMachineName()).getAddress();
            fesLockFileMachineAddress = testEnvironment.getMachine(fesServerElem.getGatewayMachine()).getAddress();
        } else {
            standaloneFESAddress = testEnvironment.getMachine(fesServerElem.getProfileElements().get("standalone").getMachineName()).getAddress(); //Gives IP
        }
    }

    public void setFPSAddress(String serverName) throws STFException {
        setFPSServerElem(serverName);
        if (fpsServerElem == null) throw new STFException("fpsServerElem not set");

        if (isFPSaHA == true) {
            primaryFPSAddress = testEnvironment.getMachine(fpsServerElem.getProfileElements().get("primary").getMachineName()).getAddress();
            secondaryFPSAddress = testEnvironment.getMachine((fpsServerElem.getProfileElements().get("secondary").getMachineName())).getAddress();
            fpsLockFileMachineAddress = testEnvironment.getMachine(fpsServerElem.getGatewayMachine()).getAddress();
        } else {
            primaryFPSAddress = testEnvironment.getMachine(fpsServerElem.getProfileElements().get("standalone").getMachineName()).getAddress();
        }
    }

    private void setFESServerElem() throws STFException {             //i changed to public
        Enumeration serverElements = getServerElements();

        for (; serverElements.hasMoreElements();) {
            ServerElement serverElem = (ServerElement) serverElements.nextElement();

            if (TestEnvironmentConstants.FES.equalsIgnoreCase(serverElem.getMode())) {
                fesServerElem = serverElem;
                if (serverElem.isHA()) isFESaHA = true;
                break;
            }
            if (TestEnvironmentConstants.AMS.equalsIgnoreCase(serverElem.getMode())){
                fesServerElem = serverElem;
                if (serverElem.isHA()) isFESaHA = true;
                break;
            }
        }
    }

    private void setFPSServerElem(String serverName) throws STFException {
        fpsServerElem = null;
        isFPSaHA = false;
        Enumeration serverElements = getServerElements();
        for (; serverElements.hasMoreElements();) {
            ServerElement serverElem = (ServerElement) serverElements.nextElement();

            if (TestEnvironmentConstants.FPS.equalsIgnoreCase(serverElem.getMode())) {
                if (serverElem.getServerName().equalsIgnoreCase(serverName)) {
                    fpsServerElem = serverElem;
                    if (serverElem.isHA()) isFPSaHA = true;
                    break;
                }
            }
            else if (TestEnvironmentConstants.AGS.equalsIgnoreCase(serverElem.getMode())){
                if(serverElem.getServerName().equalsIgnoreCase(serverName)){
                    fpsServerElem = serverElem;
                    if (serverElem.isHA()) isFPSaHA = true;
                    break;
                }
            }
        }
    }

    public String getServerStatus(String address, int port) throws STFException {

        //JMXClient jmxClient = JMXClient.getInstance();
        if (JMXClient.connect(address, port) == false) return null;

        try {
            MBeanServerConnection conn = JMXClient.getMBeanServerConnection();
            if (conn == null) throw new STFException("mbean connection couldn't be created");
            ObjectName obj = new ObjectName("Fiorano.HA.HAManager:ServiceType=RpManager,Name=FioranoHAManager");
            Object status = conn.getAttribute(obj, "Status");
            //JMXClient.cleanUp();
            return (String) status;
        }
        catch (MalformedObjectNameException e) {
            e.printStackTrace();
            throw new STFException("error while getting status :: " + e.getMessage());
        }
        catch (MBeanException e) {
            e.printStackTrace();
            throw new STFException("error while getting status :: " + e.getMessage());
        }
        catch (AttributeNotFoundException e) {
            e.printStackTrace();
            throw new STFException("error while getting status :: " + e.getMessage());
        }
        catch (InstanceNotFoundException e) {
            e.printStackTrace();
            throw new STFException("error while getting status :: " + e.getMessage());
        }
        catch (ReflectionException e) {
            e.printStackTrace();
            throw new STFException("error while getting status :: " + e);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new STFException("error while getting status :: " + e);
        }
        finally {
            //jmxClient.cleanUp();
        }
    }
    /* -------------------------------- needn't be exposed ---------------------------------------- ENDS */

    /* -------------------------------- needn't be exposed ---------------------------------------- STARTS */

    public FioranoServiceProvider getEnterpriseServiceProvider(String url) throws STFException {
        FioranoServiceProvider instance;
        try {
            InitialContext context = createInitialContext(url, null);
            FioranoServiceProviderFactory factory = (FioranoServiceProviderFactory) context.lookup("TifosiServiceProvider");
            instance = factory.createServiceProvider("admin", "passwd");

        } catch (Exception ex) {
            throw new STFException("Failed to get FioranoServiceProvider", ex);
        }
        return instance;
    }

    public String getAddressOnFPSActive(String fpsProfileName) throws STFException {
        try {
            String fesAddress = getURLOnFESActive();
            FioranoServiceProvider esp = getEnterpriseServiceProvider(fesAddress);
            FioranoFPSManager fpsman = esp.getFPSManager();
            return getAddress(fpsman.getConnectURLForFPS(fpsProfileName));
        }
        catch (TifosiException e) {
            e.printStackTrace();
            throw new STFException("error while getting active fps address :: " + e.getMessage());
        }
    }

    /* the following method should be implemented properly. Its almost dummy now */

    public String getURLOnFESActive() throws STFException {
        if (isFESaHA) {
            String sp1 = getPrimaryFESStatus();
            if (StatusPacket.active().toString().equals(sp1) || StatusPacket.standAlone().toString().equals(sp1))
                return getUrl(primaryFESAddress, true);

            String sp2 = getSecondaryFESStatus();
            if (StatusPacket.active().toString().equals(sp2) || StatusPacket.standAlone().toString().equals(sp2))
                return getUrl(secondaryFESAddress, false);
        }
        System.out.println(getUrl(standaloneFESAddress, true));
        return getUrl(standaloneFESAddress, true);
    }


    /* -------------------------------- needn't be exposed ---------------------------------------- ENDS */

    /* ---------------------------------shouldn't be exposed --------------------------------------- STARTS */

    //this function added on 08-04-08

    public boolean getFPSHA() {
        return isFPSaHA;
    }
    /* ---------------------------------shouldn't be exposed --------------------------------------- ENDS */
}
