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
package com.fiorano.stf;

import com.fiorano.stf.framework.STFException;

/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 7, 2007
 * Time: 3:33:18 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * This interface declare the base methods that constitute life cycle of test execution.
 */
public interface TestHarness {

    /**
     * This is the main method for test esecution, which will invoke other lifecycle methods as defined in specific test harness
     *
     * @throws com.fiorano.stf.framework.STFException
     *
     */
    public void execute() throws STFException;
}
