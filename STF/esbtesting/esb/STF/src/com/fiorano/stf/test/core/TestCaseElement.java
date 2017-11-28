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

/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 16, 2007
 * Time: 10:08:36 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TestCaseElement extends TestElement {

    protected boolean validationEnabled;
    protected String enterpriseServerUrl;

    public boolean validationEnabled() {
        return validationEnabled;
    }

    public String getEnterpiseServerUrl() {
        return enterpriseServerUrl;
    }

    public String getEnterpriseServerUrl() {
        return enterpriseServerUrl;
    }

    public void setEnterpriseServerUrl(String enterpriseServerUrl) {
        this.enterpriseServerUrl = enterpriseServerUrl;
    }

}
