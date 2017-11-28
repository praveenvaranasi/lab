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
package com.fiorano.stf.test.core;

import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.util.StaxParser;

import javax.xml.stream.XMLStreamException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 8, 2007
 * Time: 3:52:40 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class representd Server Element of TestEnvironment.
 */
public class ServerElement extends TestElement {

    protected String serverName;
    protected String machine;
    protected String profileName;
    protected String mode;
    protected boolean isHA;
    protected boolean ssl = false;
    protected boolean jetty = false;
    protected boolean basicAuth = false;

    private Map<String, ProfileElement> profileElements = new HashMap<String, ProfileElement>();

    protected boolean isHAServer = false;
    private boolean autoStartServer = true;

    protected String primaryFESUrl;

    protected String secondaryFESUrl;

    private String gatewayMachine;
    private String shareName;

    protected void populate(StaxParser cursor) throws STFException {
        try {
            if (cursor.markCursor(TestEnvironmentConstants.SERVER)) {

                Hashtable attributes = cursor.getAttributes();
                serverName = (String) attributes.get(TestEnvironmentConstants.NAME);
                isHA = Boolean.parseBoolean((String) attributes.get(TestEnvironmentConstants.IS_HA));
                mode = (String) attributes.get(TestEnvironmentConstants.MODE);

                Boolean SSL = Boolean.parseBoolean((String) attributes.get(TestEnvironmentConstants.ssl));
                Boolean JETTY = Boolean.parseBoolean((String) attributes.get(TestEnvironmentConstants.jetty));
                Boolean BasicAUTH = Boolean.parseBoolean((String) attributes.get(TestEnvironmentConstants.basicAuth));

                if (SSL != null)
                    this.ssl = SSL;
                if (JETTY != null)
                    this.jetty = JETTY;
                if (BasicAUTH != null)
                    this.basicAuth = BasicAUTH;
                // Check if autostart is disabeld. Default its enabled.
                String autoStartServerAttr = (String) attributes.get(TestEnvironmentConstants.AUTO_START);
                if (autoStartServerAttr != null)
                    autoStartServer = Boolean.getBoolean(autoStartServerAttr);

                if (serverName == null && serverName.trim().equalsIgnoreCase(""))
                    throw new STFException("Server element must specify name attribute.");
                while (cursor.nextElement()) {

                    Hashtable childAttributes = cursor.getAttributes();
                    String elemName = cursor.getLocalName();

                    if (mode.equalsIgnoreCase(TestEnvironmentConstants.FPS) && elemName.equalsIgnoreCase(TestEnvironmentConstants.PRIMARY_FESURL)) {
                        primaryFESUrl = (String) childAttributes.get(TestEnvironmentConstants.URL);
                    } else if (mode.equalsIgnoreCase(TestEnvironmentConstants.FPS) && elemName.equalsIgnoreCase(TestEnvironmentConstants.SECONDARY_FESURL)) {
                        secondaryFESUrl = (String) childAttributes.get(TestEnvironmentConstants.URL);
                    } else if (elemName.equalsIgnoreCase(TestEnvironmentConstants.PROFILE)) {
                        ProfileElement profile = new ProfileElement();
                        profile.populate(cursor);
                        if (profile.getProfileName().contains("primary"))
                            profileElements.put("primary", profile);
                        else if (profile.getProfileName().contains("secondary"))
                            profileElements.put("secondary", profile);
                        else
                            profileElements.put("standalone", profile);
                    } else if (elemName.equalsIgnoreCase(TestEnvironmentConstants.GATEWAY + TestEnvironmentConstants.MACHINE)) {
                        gatewayMachine = (String) childAttributes.get(TestEnvironmentConstants.REF);
                        shareName = (String) childAttributes.get(TestEnvironmentConstants.SHARE_NAME);
                    } else
                        throw new STFException("Invalid Test Environment Configuration file. Found unsupported element " + elemName + " as part of Server element.");
                }

                cursor.resetCursor();
            } else
                throw new STFException("Invalid TestEnvironment Configuration file. Missing Server configuration");

        } catch (XMLStreamException e) {
            throw new STFException("Failed to parse TestEnvironment Configuration file", e);
        }
    }


    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public Map<String, ProfileElement> getProfileElements() {
        return profileElements;
    }

    public void setProfileElements(Map<String, ProfileElement> profileElements) {
        this.profileElements = profileElements;
    }

    public boolean isAutoStartServer() {
        return autoStartServer;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isHA() {
        return isHA;
    }

    public boolean isSsl() {
        return ssl;
    }

    public boolean isJetty() {
        return jetty;
    }

    public boolean isbasicAuth() {
        return basicAuth;
    }

    public void setHA(boolean HA) {
        isHA = HA;
    }

    public String getGatewayMachine() {
        return gatewayMachine;
    }

    public void setGatewayMachine(String gatewayMachine) {
        this.gatewayMachine = gatewayMachine;
    }

    public String getShareName() {
        return shareName;
    }

    public void setShareName(String shareName) {
        this.shareName = shareName;
    }

    public String getPrimaryFESUrl() {
        return primaryFESUrl;
    }

    public void setPrimaryFESUrl(String primaryFESUrl) {
        this.primaryFESUrl = primaryFESUrl;
    }

    public String getSecondaryFESUrl() {
        return secondaryFESUrl;
    }

    public void setSecondaryFESUrl(String secondaryFESUrl) {
        this.secondaryFESUrl = secondaryFESUrl;
    }
}
