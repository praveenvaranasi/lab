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
package com.fiorano.stf.consts;

/**
 * Created by IntelliJ IDEA.
 * User: aditya
 * Date: Aug 10, 2007
 * Time: 2:12:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface XMLConstants {
    /**
     * the testsuites element for the aggregate document
     */
    String TESTSUITES = "testsuites";

    /**
     * the testsuite element
     */
    String TESTSUITE = "testsuite";

    /**
     * the testcase element
     */
    String TESTCASE = "testcase";

    /**
     * the error element
     */
    String ERROR = "error";

    /**
     * the failure element
     */
    String FAILURE = "failure";

    /**
     * the system-err element
     */
    String SYSTEM_ERR = "system-err";

    /**
     * the system-out element
     */
    String SYSTEM_OUT = "system-out";

    /**
     * package attribute for the aggregate document
     */
    String ATTR_PACKAGE = "package";

    /**
     * name attribute for property, testcase and testsuite elements
     */
    String ATTR_NAME = "name";

    /**
     * time attribute for testcase and testsuite elements
     */
    String ATTR_TIME = "time";

    /**
     * errors attribute for testsuite elements
     */
    String ATTR_ERRORS = "errors";

    /**
     * failures attribute for testsuite elements
     */
    String ATTR_FAILURES = "failures";

    /**
     * tests attribute for testsuite elements
     */
    String ATTR_TESTS = "tests";

    /**
     * type attribute for failure and error elements
     */
    String ATTR_TYPE = "type";

    /**
     * message attribute for failure elements
     */
    String ATTR_MESSAGE = "message";

    /**
     * the properties element
     */
    String PROPERTIES = "properties";

    /**
     * the property element
     */
    String PROPERTY = "property";

    /**
     * value attribute for property elements
     */
    String ATTR_VALUE = "value";

    /**
     * classname attribute for testcase elements
     */
    String ATTR_CLASSNAME = "classname";

    /**
     * id attribute
     */
    String ATTR_ID = "id";

    /**
     * timestamp of test cases
     */
    String TIMESTAMP = "timestamp";

    /**
     * name of host running the tests
     */
    String HOSTNAME = "hostname";
}
