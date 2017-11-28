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
import java.io.File;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 16, 2007
 * Time: 11:12:08 AM
 * To change this template use File | Settings | File Templates.
 */

public class FlowTestCaseElement extends TestCaseElement {


    private String instanceName;
    private String[] instanceNames;
    private String outputFile;
    private String startupDir;
    private String startupCommand;
    private String cleanupDir;
    private String cleanupCommand;
    private String appGUID;
    private String appVersion = "1.0";
    private Vector scenarios = new Vector();
    private boolean overWrite;
    private String applicationFilePath;
    private long applicationStatupTime = 20000l;


    protected void populate(StaxParser cursor) throws STFException {

        try {
            if (cursor.markCursor(TestEnvironmentConstants.FLOWTEST_CASE)) {

                Hashtable attributes = cursor.getAttributes();
                setName((String) attributes.get(TestEnvironmentConstants.NAME));
                setTestBaseDir((String) attributes.get(TestEnvironmentConstants.TESTCASE_BASE_DIR));

                validationEnabled = Boolean.valueOf((String) attributes.get(TestEnvironmentConstants.VALIDATION_ENABLED)).booleanValue();

                while (cursor.nextElement()) {
                    String localeName = cursor.getLocalName();

                    if (localeName.equalsIgnoreCase(TestEnvironmentConstants.STARTUP)) {
                        Hashtable cursorAttributes = cursor.getAttributes();
                        instanceName = (String) cursorAttributes.get(TestEnvironmentConstants.INSTANCE_NAME);
                        if (instanceName == null) {
                            instanceNames = ((String) cursorAttributes.get(TestEnvironmentConstants.INSTANCE_NAMES)).split(",");
                        }
                        startupDir = (String) cursorAttributes.get(TestEnvironmentConstants.STARTUP_DIR);

                        startupCommand = (String) cursorAttributes.get(TestEnvironmentConstants.COMMAND);

                    } else if (localeName.equalsIgnoreCase(TestEnvironmentConstants.EVENT_PROCESS)) {
                        Hashtable cursorAttributes = cursor.getAttributes();
                        appGUID = (String) cursorAttributes.get(TestEnvironmentConstants.APPLICATION_GUID);
                        appVersion = (String) cursorAttributes.get(TestEnvironmentConstants.APPLICATION_VERSION);
                        overWrite = Boolean.valueOf((String) cursorAttributes.get(TestEnvironmentConstants.OVERWRITE_APPLICATION)).booleanValue();
                        applicationStatupTime = Integer.parseInt((String) cursorAttributes.get(TestEnvironmentConstants.APPLICATION_STARTUP_TIME)) * 1000l;
                        String applicationRelPath = (String) cursorAttributes.get(TestEnvironmentConstants.APPLICATION_FILE_PATH);
                        if (applicationRelPath != null)
                            applicationFilePath = testCaseBaseDir + File.separator + applicationRelPath;
                    } else if (localeName.equalsIgnoreCase(TestEnvironmentConstants.TEST_SCENARIO)) {
                        cursor.markCursor(TestEnvironmentConstants.TEST_SCENARIO);
                        TestScenario testScenario = new TestScenario();
                        testScenario.populate(cursor);
                        cursor.resetCursor();
                        scenarios.add(testScenario);

                    } else if (localeName.equalsIgnoreCase(TestEnvironmentConstants.CLEANUP)) {
                        Hashtable cursorAttributes = cursor.getAttributes();
                        instanceName = (String) cursorAttributes.get(TestEnvironmentConstants.INSTANCE_NAME);
                        if (instanceName == null) {
                            instanceNames = ((String) cursorAttributes.get(TestEnvironmentConstants.INSTANCE_NAMES)).split(",");
                        }
                        cleanupDir = (String) cursorAttributes.get(TestEnvironmentConstants.CLEANUP_DIR);
                        cleanupCommand = (String) cursorAttributes.get(TestEnvironmentConstants.COMMAND);
                    }
                }

                cursor.resetCursor();
            }
        } catch (XMLStreamException e) {
            throwException("Failed to parse the TestSpecification file", e);
        }
    }


    public String getInstanceName() {
        return instanceName;
    }

    public String[] getInstanceNames() {
        return instanceNames;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }

    public String getStartupDir() {
        return startupDir;
    }

    public String getStartupCmd() {
        return startupCommand;
    }

    public String getCleanupDir() {
        return cleanupDir;
    }

    public String getCleanupCmd() {
        return cleanupCommand;
    }


    public String getAppGUID() {
        return appGUID;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getBaseDirectory() {
        return testCaseBaseDir;
    }

    public long getApplicationStartupTime() {
        return applicationStatupTime;
    }

    public Vector getScenarios() {
        return scenarios;
    }

    public String getTestCasepath() {
        return testCaseBaseDir;
    }

    public void setApplicationFilePath(String applicationFilePath) {
        this.applicationFilePath = applicationFilePath;
    }

    public void setStartUpDirPath(String startupDir) {
        this.startupDir = startupDir;
    }

    public void setcleanupDirPath(String cleanupDir) {
        this.cleanupDir = cleanupDir;
    }

    public String getApplicationFilePath() {
        return applicationFilePath;
    }

    public boolean isOverWrite() {
        return overWrite;
    }
}
