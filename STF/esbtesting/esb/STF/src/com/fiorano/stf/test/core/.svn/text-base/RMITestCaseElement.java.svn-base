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
import junit.framework.Assert;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Oct 15, 2007
 * Time: 1:39:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class RMITestCaseElement extends DRTTestCaseElement {

    private String propertyFilePath;
    private String testCaseClass;

    protected void populate(StaxParser cursor) throws STFException {
        try {
            if (cursor.markCursor(TestEnvironmentConstants.RMITEST_CASE)) {
                Hashtable attributes = cursor.getAttributes();

                setName((String) attributes.get(TestEnvironmentConstants.NAME));
                setTestBaseDir((String) attributes.get(TestEnvironmentConstants.TESTCASE_BASE_DIR));
                testCaseClass = (String) attributes.get(TestEnvironmentConstants.TESTCASE_CLASS);
                Assert.assertNotNull("DRTTest case must specify TestCase Class ", testCaseClass);

                while (cursor.nextElement()) {
                    String element = cursor.getLocalName();
                    if (element.equalsIgnoreCase(TestEnvironmentConstants.PROPERTY)) {
                        Hashtable propertyAttributes = cursor.getAttributes();
                        propertyFilePath = (String) propertyAttributes.get(TestEnvironmentConstants.FILE);

                    } else
                        throwException("Found unsuppoerted element" + element + " in DRTTestCase " + testCaseName);
                }
                cursor.resetCursor();
            }
        }
        catch (XMLStreamException e) {
            throwException("Failed to parse the TestSpecification file", e);
        }
    }

    public String getPropertyFilePath() {
        return getTestBaseDir() + File.separator + propertyFilePath;
    }

    public String getTestCaseClass() {
        return testCaseClass;
    }
}
