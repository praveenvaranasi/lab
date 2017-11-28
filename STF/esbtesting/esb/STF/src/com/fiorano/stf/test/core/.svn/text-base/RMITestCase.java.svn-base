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

import com.fiorano.stf.remote.RMIClient;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Oct 15, 2007
 * Time: 1:46:12 PM
 * To change this template use File | Settings | File Templates.
 */
public class RMITestCase extends DRTTestCase {
    protected RMITestCaseElement rmiTestCaseConfig;
    protected Properties testCaseProperties;
    private String testCaseClassPath;
    protected static RMIClient rmiClient = RMIClient.getInstance();

    public RMITestCase(String testCaseName) {
        super(testCaseName);
    }

    public RMITestCase(TestCaseElement testCaseConfig) {
        super(testCaseConfig);
        rmiTestCaseConfig = (RMITestCaseElement) testCaseConfig;
        testCaseClassPath = rmiTestCaseConfig.getTestCaseClass();
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
            propFile = new File(rmiTestCaseConfig.getPropertyFilePath());
            properties.load(new FileInputStream(propFile));
        } catch (IOException ioe) {
            fail("RMITestCase " + testCaseConfig.getName() + " properties file " + propFile.getAbsolutePath() + " doesnot exists.");
        }
        return properties;
    }

}
