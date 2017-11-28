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


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 9, 2007
 * Time: 6:03:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestEnvironmentConfig extends Properties {

    /**
     * Default constructor
     */
    public TestEnvironmentConfig() {
    }

    /**
     * This will initialize the test environment with properties passed as argument.
     *
     * @param configProperties
     */
    public TestEnvironmentConfig(Properties configProperties) {
        super(configProperties);
    }

    public void load(File file) throws IOException {

        super.load(new FileInputStream(file));
        Enumeration keys = this.keys();
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            String value = this.getProperty(key);
            if (!new File(value).isAbsolute()) {
                this.put(key, new File(file.getParent(), value).getAbsolutePath());
            }

        }
    }
}
