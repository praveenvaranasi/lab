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

import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.stf.remote.JMXClient;
import com.fiorano.stf.remote.RMIClient;
import com.fiorano.stf.remote.RTLClient;
import junit.framework.TestCase;

/**
 * @author Ibrahim Shaik
 * @Date: This class is a base class for all the test cases defined in this framework.
 */

public abstract class STFTestCase extends TestCase {

    //Test Case Configuration as specified in the test specification file
    protected TestCaseElement testCaseConfig;
    protected JMXClient jmxClient;
    protected RMIClient rmiClient;
    protected RTLClient rtlClient;
    protected STFTestSuite suite;

    public STFTestCase(String testCaseName) {
        super(testCaseName);
    }

    public STFTestCase(TestCaseElement testCaseConfig) {
        super(testCaseConfig.getName());
        this.testCaseConfig = testCaseConfig;
    }

    public TestCaseElement getTestConfig() {
        return testCaseConfig;
    }

    public void setJMXClient(JMXClient jmxClient) {
        this.jmxClient = jmxClient;
    }

    public void setRMIClient(RMIClient rmiClient) {
        this.rmiClient = rmiClient;
    }

    public void setRtlClient(RTLClient rtlClient) {
        this.rtlClient = rtlClient;
    }

    /**
     * @param suite - the suite  to which it belongs
     */
    public void setSuite(STFTestSuite suite) {
        this.suite = suite;
    }
}
