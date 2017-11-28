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

import com.fiorano.stf.framework.STFException;
import com.fiorano.stf.util.StaxParser;
import junit.framework.Assert;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 7, 2007
 * Time: 3:44:06 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This is the base class for all the data objects that represents test environment and test case specification.
 */
public abstract class TestElement {

    protected String testCaseName;
    protected String testCaseBaseDir;

    public String getName() {
        return testCaseName;
    }

    protected void setName(String testCaseName) {
        Assert.assertNotNull("TestCase name must not be null", testCaseName);
        this.testCaseName = testCaseName;
    }

    public String getTestBaseDir() {
        return testCaseBaseDir;
    }

    public void setTestBaseDir(String testCaseBaseDir) {
        if (testCaseBaseDir != null)
            this.testCaseBaseDir = testCaseBaseDir;
    }

    public final void parse(String fileName) throws STFException {
        File file = new File(fileName);
        if (!file.exists())
            throw new STFException("Given XML file doesn't exist: " + fileName);

        try {
            FileInputStream fis = new FileInputStream(file);
            this.parse(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public final void parse(InputStream is) throws STFException {
        try {
            StaxParser cursor = new StaxParser(is);
            this.parse(cursor);
        } catch (XMLStreamException e) {
            throw new STFException("Exception while parsing the inpustream: ", e);
        }
    }

    /**
     * This will parse the given xml file using STAX parser
     */
    public final void parse(StaxParser cursor) throws STFException {
        populate(cursor);
    }

    protected final void throwException(String message) throws STFException {
        throw new STFException(message);
    }

    protected final void throwException(String message, Throwable e) throws STFException {
        throw new STFException(message, e);
    }

    /**
     * This will initialize the test data object from the underlying XML file
     */
    protected abstract void populate(StaxParser cursor) throws STFException;


}
