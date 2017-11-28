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
 * User: Ibrahim Shaik
 * Date: May 16, 2007
 * Time: 9:41:57 AM
 * To change this template use File | Settings | File Templates.
 */
public class TCServerElement extends TestElement {

    private String serverName;

    public String getServerMode() {
        return serverMode;
    }

    private String serverMode;

    protected void populate(StaxParser cursor) throws STFException {
        try {
            if (cursor.markCursor(TestEnvironmentConstants.SERVER)) {

                Hashtable attributes = cursor.getAttributes();
                serverName = (String) attributes.get(TestEnvironmentConstants.REF);
                serverMode = (String) attributes.get(TestEnvironmentConstants.MODE);
                if (serverName == null)
                    throwException("TestCase Server must define attribute ref.");
                if (serverMode == null)
                    throwException("TestCase Server must have attribute 'MODE'.");

                cursor.resetCursor();
            }
        } catch (XMLStreamException e) {
            throw new STFException();
        }
    }

    public String getName() {
        return serverName;
    }
}
