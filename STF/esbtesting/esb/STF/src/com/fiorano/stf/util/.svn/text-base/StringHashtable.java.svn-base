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

import com.fiorano.stf.framework.STFException;

import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: Ibrahim Shaik
 * Date: May 15, 2007
 * Time: 5:09:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class StringHashtable extends Hashtable {

    Hashtable hash = new Hashtable();

    /**
     * If the key is of type string, then it will change the key to lowercase and put that in the hashtable.
     * Will thow Exception if the element already exists in the hash table.
     *
     * @param key
     * @param value
     */
    public void addEntry(String key, Object value) throws STFException {
        if (containsKey(key))
            throw new STFException("Duplicate entry: " + key);

        key = key.toLowerCase();
        if (value instanceof String)
            value = ((String) value).toLowerCase();

        super.put(key, value);
    }

    public Object put(Object key, Object value) {

        if (key instanceof String)
            key = ((String) key).toLowerCase();
        if (value instanceof String)
            value = ((String) value).toLowerCase();
        return super.put(key, value);
    }

    public boolean containsKey(Object key) {
        if (key instanceof String)
            key = ((String) key).toLowerCase();
        return super.containsKey(key);
    }

    public Object get(Object key) {
        if (key instanceof String)
            key = ((String) key).toLowerCase();
        return super.get(key);
    }
}
