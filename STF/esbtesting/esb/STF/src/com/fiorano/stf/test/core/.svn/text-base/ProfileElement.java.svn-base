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
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: raja
 * Date: Jun 23, 2010
 * Time: 5:28:33 PM
 * To change this template use File | Settings | File Templates.
 */

public class ProfileElement extends TestElement {

    protected String profileName;
    protected boolean isWrapper = false;
    private String machineName;
    private String lockFilePath;

    protected void populate(StaxParser cursor) throws STFException {
        try {
            if (cursor.markCursor(TestEnvironmentConstants.PROFILE)) {

                Hashtable attributes = cursor.getAttributes();
                profileName = (String) attributes.get(TestEnvironmentConstants.NAME);
                isWrapper = Boolean.parseBoolean((String) attributes.get(TestEnvironmentConstants.IS_WRAPPER));

                if (profileName == null || profileName.trim().equalsIgnoreCase(""))
                    throw new STFException("Profile element must specify name attribute.");

                while (cursor.nextElement()) {

                    String element = cursor.getLocalName();
                    Hashtable childElemAttr = cursor.getAttributes();
                    // Handle FES Profile configuration element

                    if (element.equalsIgnoreCase(TestEnvironmentConstants.MACHINE)) {
                        machineName = (String) childElemAttr.get(TestEnvironmentConstants.REF);
                        lockFilePath = (String) childElemAttr.get(TestEnvironmentConstants.LOCK_FILE);
                    }


                    // Throw Exception
                    else
                        throw new STFException("Profile element doesn't support element: " + element);
                }

                cursor.resetCursor();
            }

        }
        catch (XMLStreamException e) {
            throw new STFException("Failed to parse TestEnvironment Configuration file", e);
        }
    }

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public boolean isWrapper() {
        return isWrapper;
    }

    public void setWrapper(boolean wrapper) {
        isWrapper = wrapper;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getLockFilePath() {
        return lockFilePath;
    }

    public void setLockFilePath(String lockFilePath) {
        this.lockFilePath = lockFilePath;
    }

}
