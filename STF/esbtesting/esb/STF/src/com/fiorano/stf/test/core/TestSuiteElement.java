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
import com.fiorano.stf.util.StringHashtable;

import javax.xml.stream.XMLStreamException;
import java.util.Hashtable;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 16, 2007
 * Time: 9:25:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestSuiteElement extends TestElement {

    protected String testSuiteName;
    protected StringHashtable servers = new StringHashtable();
    protected LinkedHashMap testCases = new LinkedHashMap();
    protected String enterpriseServer = null;

    protected void populate(StaxParser cursor) throws STFException {
        try {
            if (cursor.markCursor(TestEnvironmentConstants.TEST_SUITE)) {
                Hashtable attributes = cursor.getAttributes();
                testSuiteName = (String) attributes.get(TestEnvironmentConstants.NAME);

                while (cursor.nextElement()) {
                    String element = cursor.getLocalName();
                    if (element.equalsIgnoreCase(TestEnvironmentConstants.SERVERS)) {
                        if (cursor.markCursor(TestEnvironmentConstants.SERVERS)) {

                            while (cursor.nextElement()) {
                                element = cursor.getLocalName();
                                if (element.equalsIgnoreCase(TestEnvironmentConstants.SERVER)) {
                                    TCServerElement server = new TCServerElement();
                                    server.parse(cursor);
                                    servers.addEntry(server.getName(), server);
                                } else
                                    throwException("Found unsuported element " + element + " in " + TestEnvironmentConstants.SERVERS + " element");
                            }

                            cursor.resetCursor();
                        }
                    } else if (element.equalsIgnoreCase(TestEnvironmentConstants.TEST_CASES)) {
                        if (cursor.markCursor(TestEnvironmentConstants.TEST_CASES)) {
                            while (cursor.nextElement()) {
                                element = cursor.getLocalName();

                                if (element.equalsIgnoreCase(TestEnvironmentConstants.FLOWTEST_CASE)) {
                                    TestCaseElement testCaseElement = new FlowTestCaseElement();
                                    testCaseElement.parse(cursor);
                                    testCases.put(testCaseElement.getName(), testCaseElement);
                                } else if (element.equalsIgnoreCase(TestEnvironmentConstants.DRTTEST_CASE)) {
                                    TestCaseElement testCaseElement = new DRTTestCaseElement();
                                    testCaseElement.parse(cursor);
                                    testCases.put(testCaseElement.getName(), testCaseElement);
                                } else if (element.equalsIgnoreCase(TestEnvironmentConstants.RMITEST_CASE)) {
                                    TestCaseElement testCaseElement = new RMITestCaseElement();
                                    testCaseElement.parse(cursor);
                                    testCases.put(testCaseElement.getName(), testCaseElement);
                                } else
                                    throwException("Found unsuported element " + element + " in " + TestEnvironmentConstants.TEST_CASES + " element");

                            }
                            cursor.resetCursor();
                        }
                    } else
                        throwException("Found unsuported element " + element + " in " + TestEnvironmentConstants.TEST_SUITE + " element");
                }
                cursor.resetCursor();
            } else
                throwException("Invalid TestSuite Element");
        } catch (XMLStreamException e) {
            throwException("Failed to parse TestCaseSpecification file", e);
        }
    }

    public String getName() {
        return testSuiteName;
    }

    public LinkedHashMap getTestCases() {
        return testCases;
    }


    public StringHashtable getServers() {
        return servers;
    }

    public String getEnterpriseServer() {
        return enterpriseServer;
    }

    public void setEnterpriseServer(String enterpriseServer) {
        this.enterpriseServer = enterpriseServer;
    }
}
