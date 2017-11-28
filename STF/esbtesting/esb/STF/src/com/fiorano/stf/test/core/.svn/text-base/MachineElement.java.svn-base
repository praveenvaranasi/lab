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
 * Date: May 8, 2007
 * Time: 3:52:40 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This class represents Machine Element of TestEnvironment.
 */
public class MachineElement extends TestElement {

    private String machine;
    private String address;
    private boolean isWin;

    protected void populate(StaxParser cursor) throws STFException {
        try {
            if (cursor.markCursor(TestEnvironmentConstants.MACHINE)) {

                Hashtable attributes = cursor.getAttributes();
                machine = (String) attributes.get(TestEnvironmentConstants.NAME);
                if (machine == null || machine.trim().equalsIgnoreCase(""))
                    throw new STFException("Machine element msut specify name attribute.");

                address = (String) attributes.get(TestEnvironmentConstants.ADDRESS);
                if (machine == null || machine.trim().equalsIgnoreCase(""))
                    throw new STFException("Machine element msut specify address attribute.");

                isWin = Boolean.parseBoolean(((String) attributes.get(TestEnvironmentConstants.IS_WINDOWS)));
                if (machine == null || machine.trim().equalsIgnoreCase(""))
                    throw new STFException("Machine element msut specify os attribute.");
                cursor.resetCursor();
            } else
                throw new STFException("Invalid TestEnvironment Configuration file. No machine details specified");
        } catch (XMLStreamException e) {
            throw new STFException("Failed to parse TestEnvironment Configuration file", e);
        }
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMachineName() {
        return machine;
    }

    public void setMachineName(String machineName) {
        machine = machineName;
    }

    public boolean isWindows() {
        return isWin;
    }

    public void setWindows(boolean win) {
        isWin = win;
    }
}
