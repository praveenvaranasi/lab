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

import com.fiorano.stf.ESBTestHarness;
import com.fiorano.stf.consts.TestEnvironmentConstants;
import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.util.StaxParser;

import javax.xml.stream.XMLStreamException;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 16, 2007
 * Time: 9:06:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class TestSpecification extends TestElement {

    private LinkedHashMap testSuites = new LinkedHashMap();

    protected void populate(StaxParser cursor) throws STFException {

        try {
            if (cursor.markCursor(TestEnvironmentConstants.TEST_SPECIFICATION)) {
                while (cursor.nextElement()) {
                    String element = cursor.getLocalName();
                    if (element.equalsIgnoreCase(TestEnvironmentConstants.TEST_SUITE)) {
                        TestSuiteElement suite = new TestSuiteElement();
                        suite.parse(cursor);
                        testSuites.put(suite.getName(), suite);
                    } else
                        throwException("Found unsupported element " + element + " in " + TestEnvironmentConstants.TEST_SPECIFICATION + " element.");
                }

                cursor.resetCursor();
            } else
                throwException("Invalid TestCaseSpecification file");
        } catch (XMLStreamException e) {
            throwException("Failed to initialize the Test Specification file", e);
        }
    }

    public void initializeTestSpec(String testSpecFile) throws STFException {
        parse(testSpecFile);
        validateTestSpec();
    }

    private void validateTestSpec() throws STFException {
        for (Object item : testSuites.values()) {
            TestSuiteElement suiteElem = (TestSuiteElement) item;
            for (Object item1 : suiteElem.getTestCases().values()) {
                TestCaseElement caseElem = (TestCaseElement) item1;
                String basedir = caseElem.getTestBaseDir();
                caseElem.setTestBaseDir(basedir.replace("${TESTING_HOME}", ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.TESTING_HOME)));
                if (caseElem instanceof FlowTestCaseElement) {
                    FlowTestCaseElement flowCaseElem = (FlowTestCaseElement) caseElem;
                    String appPath = flowCaseElem.getApplicationFilePath();
                    String startupDir = flowCaseElem.getStartupDir();
                    for (Object scenario : flowCaseElem.getScenarios()) {
                        TestScenario testScenario = (TestScenario) scenario;
                        for (Object artifact : testScenario.getExpectedOutputs().values()) {
                            TestArtifact testArtifact = (TestArtifact) artifact;
                            String outputFile = testArtifact.getOutputFile();
                            if (outputFile != null) {
                                //TODO: add for all testenvconfig vars
                                flowCaseElem.setOutputFile(outputFile.replace("${FIORANO_HOME}", ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.FIORANO_HOME)));
                            }
                        }
                    }
                    if (startupDir != null)
                        flowCaseElem.setStartUpDirPath(startupDir.replace("${TESTING_HOME}", ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.TESTING_HOME)));
                    String cleanupDir = flowCaseElem.getCleanupDir();
                    if (cleanupDir != null)
                        flowCaseElem.setcleanupDirPath(cleanupDir.replace("${TESTING_HOME}", ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.TESTING_HOME)));
                    flowCaseElem.setApplicationFilePath(appPath.replace("${TESTING_HOME}", ESBTestHarness.getTestEnvConfig().getProperty(TestEnvironmentConstants.TESTING_HOME)));
                }
            }
        }
    }

    public LinkedHashMap getTestSuites() {
        return testSuites;
    }

}
