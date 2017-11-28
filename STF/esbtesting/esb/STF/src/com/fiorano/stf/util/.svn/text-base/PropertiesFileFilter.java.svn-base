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
package com.fiorano.stf.util;

import java.io.File;
import java.io.FileFilter;

public class PropertiesFileFilter implements FileFilter {

    public boolean accept(File pathname) {
        if (pathname.getName().equalsIgnoreCase("tests.properties"))
            return true;

        return false;
    }
}