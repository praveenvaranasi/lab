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
package com.fiorano.stf.misc;

import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.events.TifosiEvent;
import fiorano.tifosi.events.IEventListener;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Sep 8, 2006
 * Time: 11:25:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class JUnitEventListener implements IEventListener {
    private String m_ListenerType = "Generic Listener";

    public JUnitEventListener() {
    }

    public JUnitEventListener(String type)  //Give a type if u want to see which listener is getting the event
    {
        m_ListenerType = type;
    }

    public void onEvent(TifosiEvent event) throws TifosiException {
        System.out.println(m_ListenerType + ": [" + new Date().getTime() + "] EVENT::" + event + " With Status" + event.getEventStatus());
    }
}
