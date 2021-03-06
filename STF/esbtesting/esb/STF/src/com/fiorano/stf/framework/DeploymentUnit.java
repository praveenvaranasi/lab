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

import com.fiorano.stf.consts.TestEnvironmentConstants;

import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: May 16, 2007
 * Time: 2:52:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class DeploymentUnit {

    private String machineAddress;
    private String profileDir;
    private String sourceDir;
    private boolean isHAProfile;
    private boolean isWrapper;
    private String gatewayMachineAddress;
    private String serverConfigFilePath;

    private String lockFilePath;

    private int profileType;


    public static final int STAND_ALONE_FES = 0;
    public static final int STAND_ALONE_FPS = 1;
    public static final int HA_FES_PRIMARY = 2;
    public static final int HA_FES_SECONDARY = 3;
    public static final int HA_FPS_PRIMARY = 4;
    public static final int HA_FPS_SECONDARY = 5;
    public static final int STAND_ALONE_AMS = 6;
    public static final int STAND_ALONE_AGS = 7;

    private String primFESUrl;
    private String secFESUrl;

    private String primaryPeerURL;
    private String backupPeerURL;
    private boolean isSSL = false;
    private boolean isJetty = false;
    private boolean isbasicAuth = false;

    public String getPrimaryPeerURL() {
        return primaryPeerURL;
    }

    public void setPrimaryPeerURL(String primaryPeerURL) {
        this.primaryPeerURL = primaryPeerURL;
    }

    public String getBackupPeerURL() {
        return backupPeerURL;
    }

    public void setBackupPeerURL(String backupPeerURL) {
        this.backupPeerURL = backupPeerURL;
    }


    public void setMachineAddress(String machineAddress) {
        this.machineAddress = machineAddress;
    }

    public String getMachineAddress() {
        return machineAddress;
    }

    public String getProfileDir() {
        return profileDir;
    }

    public void setProfileDir(String profileDir) {
        this.profileDir = profileDir;
    }

    public String getSourceDir() {
        return sourceDir;
    }

    public void setSourceDir(String sourceDir) {
        this.sourceDir = sourceDir;
    }

    public boolean isHAProfile() {
        return isHAProfile;
    }

    public void setHAProfile(boolean isHAProfile) {
        this.isHAProfile = isHAProfile;
    }

    public int getProfileType() {
        return profileType;
    }


    public void setProfileType(String profileType, boolean isHA, boolean isPrimary) {
        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.FES)) {
            if (isHA)
                if (isPrimary)
                    this.profileType = DeploymentUnit.HA_FES_PRIMARY;
                else
                    this.profileType = DeploymentUnit.HA_FES_SECONDARY;
            else
                this.profileType = DeploymentUnit.STAND_ALONE_FES;

        } else {
            if (isHA)
                if (isPrimary)
                    this.profileType = DeploymentUnit.HA_FPS_PRIMARY;
                else
                    this.profileType = DeploymentUnit.HA_FPS_SECONDARY;
            else
                this.profileType = DeploymentUnit.STAND_ALONE_FPS;
        }
        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.AMS)){
            if(isHA)
                if (isPrimary)
                    this.profileType = DeploymentUnit.STAND_ALONE_AMS;
                else
                    this.profileType = DeploymentUnit.STAND_ALONE_AMS;
            else
                this.profileType = DeploymentUnit.STAND_ALONE_AMS;
        }
        if (profileType.equalsIgnoreCase(TestEnvironmentConstants.AGS)){
            if(isHA) {
                if (isPrimary)
                    this.profileType = DeploymentUnit.STAND_ALONE_AGS;
                else
                    this.profileType = DeploymentUnit.STAND_ALONE_AGS;
            }
            else
                this.profileType = DeploymentUnit.STAND_ALONE_AGS;
        }

    }

    public String getServerConfigFilePath() {
        return serverConfigFilePath;
    }

    public void setServerConfigFilePath(int profileCode, String profile) {

        if (profileCode == 0)
            serverConfigFilePath = TestEnvironmentConstants.PROFILES_DIR1 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR2 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR3 + File.separator
                    + profile + File.separator
                    + TestEnvironmentConstants.FES.toUpperCase() + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML1 + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML2;

        if (profileCode == 1)
            serverConfigFilePath = TestEnvironmentConstants.PROFILES_DIR1 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR2 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR3 + File.separator
                    + profile + File.separator
                    + TestEnvironmentConstants.FPS.toUpperCase() + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML1 + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML2;

        if (profileCode == 2)
            serverConfigFilePath = TestEnvironmentConstants.PROFILES_DIR1 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR2 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR3 + File.separator
                    + profile + File.separator
                    + TestEnvironmentConstants.FES.toUpperCase() + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML1 + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML2;
        if (profileCode == 3)
            serverConfigFilePath = TestEnvironmentConstants.PROFILES_DIR1 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR2 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR3 + File.separator
                    + profile + File.separator
                    + TestEnvironmentConstants.FES.toUpperCase() + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML1 + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML2;
        if (profileCode == 4)
            serverConfigFilePath = TestEnvironmentConstants.PROFILES_DIR1 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR2 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR3 + File.separator
                    + profile + File.separator
                    + TestEnvironmentConstants.FPS.toUpperCase() + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML1 + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML2;
        if (profileCode == 5)
            serverConfigFilePath = TestEnvironmentConstants.PROFILES_DIR1 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR2 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR3 + File.separator
                    + "FES" + File.separator
                    + TestEnvironmentConstants.FPS.toUpperCase() + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML1 + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML2;

        if (profileCode == 6)
            serverConfigFilePath = TestEnvironmentConstants.PROFILES_DIR1 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR2 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR3 + File.separator
                    + profile + File.separator
                    + TestEnvironmentConstants.AMS.toUpperCase() + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML1 + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML2;
        if (profileCode == 7)
            serverConfigFilePath = TestEnvironmentConstants.PROFILES_DIR1 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR2 + File.separator
                    + TestEnvironmentConstants.PROFILES_DIR3 + File.separator
                    + profile +  File.separator
                    + TestEnvironmentConstants.AGS.toUpperCase() + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML1 + File.separator
                    + TestEnvironmentConstants.CONFIGS_XML2;
    }

    public void setPrimaryFESUrl(String url) {
        this.primFESUrl = url;
    }

    public void setSecFESUrl(String secFESUrl) {
        this.secFESUrl = secFESUrl;
    }

    public String getPrimaryFESUrl() {
        return (this.primFESUrl);
    }

    public String getSecFESUrl() {
        return (this.secFESUrl);
    }

    public boolean isWrapper() {
        return isWrapper;
    }

    public void setWrapper(boolean wrapper) {
        isWrapper = wrapper;
    }

    public String getGatewayMachineAddress() {
        return gatewayMachineAddress;
    }

    public void setGatewayMachineAddress(String gatewayMachineAddress) {
        this.gatewayMachineAddress = gatewayMachineAddress;
    }

    public String getLockFilePath() {
        return lockFilePath;
    }

    public void setLockFilePath(String lockFilePath) {
        this.lockFilePath = lockFilePath;
    }

    public void setIsSSL(boolean isSSL) {
        this.isSSL = isSSL;
    }

    public boolean getIsSSL() {
        return this.isSSL;
    }

    public void setIsJetty(boolean isJetty) {
        this.isJetty = isJetty;
    }

    public boolean getIsJetty() {
        return this.isJetty;
    }

    public void setIsbasicAuth(boolean isbasicAuth) {
        this.isbasicAuth = isbasicAuth;
    }

    public boolean getIsbasicAuth() {
        return this.isbasicAuth;
    }

}
