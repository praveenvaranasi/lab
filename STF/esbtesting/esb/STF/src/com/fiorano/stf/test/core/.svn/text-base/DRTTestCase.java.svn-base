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

import com.fiorano.stf.framework.ServerStatusController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: user
 * Date: May 22, 2007
 * Time: 4:46:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class DRTTestCase extends STFTestCase {
    public final static String DRTTest_Logs = "DRTTEST_Logs";//relative to TESTING_HOME
    protected DRTTestCaseElement drtTestCaseConfig;
    protected Properties testCaseProperties;
    private String testCaseClassPath;
    protected ServerStatusController serverStatusController;

    public DRTTestCase(String testCaseName) {
        super(testCaseName);
    }

    public DRTTestCase(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
        drtTestCaseConfig = (DRTTestCaseElement) testCaseConfig;
        testCaseClassPath = drtTestCaseConfig.getTestCaseClass();
    }

    public DRTTestCase(TestCaseElement testCaseConfig, ServerStatusController serverStatusController) {
        super(testCaseConfig);
        drtTestCaseConfig = (DRTTestCaseElement) testCaseConfig;
        testCaseClassPath = drtTestCaseConfig.getTestCaseClass();
        this.serverStatusController = serverStatusController;

    }

    public String getTestCaseClassPath() {
        return testCaseClassPath;
    }

    protected void setUp() throws Exception {
        testCaseProperties = loadPropertiesFile();
    }

    private Properties loadPropertiesFile() {
        Properties properties = null;
        File propFile = null;
        try {
            properties = new Properties();
            propFile = new File(drtTestCaseConfig.getPropertyFilePath());
            properties.load(new FileInputStream(propFile));
        } catch (IOException ioe) {
            fail("DRTTestCase " + testCaseConfig.getName() + " properties file " + propFile.getAbsolutePath() + " doesnot exists.");
        }
        return properties;
    }


}
