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

import com.fiorano.esb.rtl.application.IDebugHandler;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.stf.remote.RTLClient;
import fiorano.tifosi.common.TifosiException;

import javax.jms.Message;
import java.util.Vector;

/**
 * @author Sandeep M
 * @date 7th September 2006
 */
public class JUnitDebuger implements IDebugHandler {

    private String m_appGUID;
    private String m_ownerRoute;
    private Vector m_messages = new Vector();
    private float appVersion = 1.0f;
    public JUnitDebuger(String appGUID, String route) {
        m_appGUID = appGUID;
        m_ownerRoute = route;

    }

    public void onMessageReceived(Message message) throws TifosiException {
        System.out.println("Message Received in the debugger " + message);

        m_messages.add(message);

    }

    public void forwardMessage(Message message) throws Exception {
       RTLClient.getInstance().getFioranoApplicationController().forwardDebugMessage(m_appGUID,appVersion, m_ownerRoute, message);
    }

    public void forwardAll() throws Exception {
        for (int i = 0; i < m_messages.size(); i++)
            forwardMessage((Message) m_messages.get(i));

    }

    public void discardMessage(int i) {
        m_messages.remove(i);
    }

    public void discardAll() {
        m_messages.clear();
    }

    public Message getMessage(int i) {
        return (Message) m_messages.remove(i);
    }

    public Vector getMessages() {
        return m_messages;
    }
}
