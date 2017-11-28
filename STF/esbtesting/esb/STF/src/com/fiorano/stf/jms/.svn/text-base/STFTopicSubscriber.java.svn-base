/**
 * Copyright (c) 2004 by Fiorano Software, Inc.,
 * Los Gatos, California, 95032, U.S.A.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software, Inc. ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */

package com.fiorano.stf.jms;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <p><strong> </strong> represents </p>
 *
 * @author FSIPL
 * @version 1.0
 * @created December 31, 2004
 */
public class STFTopicSubscriber implements ExceptionListener, MessageListener {
    private Vector m_messages = new Vector();
    private String message = null;
    private TopicConnection topicConnection = null;
    private TopicSession topicSession = null;
    String topicName = "primaryTopic";
    String tcf = "primaryTCF";
    String url = "http://localhost:1856";


    public STFTopicSubscriber(String topic, String tcf, String url) {
        // intialize the arguments
        initialize(topic, tcf, url);
        // run the test
        runTest();
    }

    /**
     * This method initialize the environment required
     * to run this sample.
     */
    public void initialize(String topic, String tcf, String url) {

        if (topic != null)
            this.topicName = topic;
        if (tcf != null)
            this.tcf = tcf;
        if (url != null)
            this.url = url;

        // print the configuration
        printConfiguration();
    }

    /**
     * This method prints the configuration parameters used
     * for running this sample.
     */
    public void printConfiguration() {
        System.out.println("Parameters used by the sample");
        System.out.println("***************************************** ");
        System.out.println("Topic Connection Factory::" + tcf);
        System.out.println("Provider Url::" + url);
        System.out.println("Topicname::" + topicName);
        System.out.println("*****************************************\n ");
    }

    /**
     * This method contains the complete logic of this sample.
     */
    public void runTest() {
        try {

            //  1. Create the InitialContext Object used for looking up
            //     JMS administered objects on the Fiorano/EMS
            //     located on the default host.
            //
            Hashtable env = new Hashtable();

            env.put(Context.SECURITY_PRINCIPAL, "anonymous");
            env.put(Context.SECURITY_CREDENTIALS, "anonymous");
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.jms.runtime.naming.FioranoInitialContextFactory");

            InitialContext ic = new InitialContext(env);

            // 1.1  Lookup Connection Factory and Topic names
            //
            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ic.lookup(tcf);
            Topic topic = (Topic) ic.lookup(topicName);

            topicConnection = topicConnectionFactory.createTopicConnection();

            // Register an Exception Listner
            topicConnection.setExceptionListener(this);
            topicConnection.start();

            topicSession = topicConnection.createTopicSession(false, 1);

            TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);

            // 5. install an asynchronous listener/callback on the subscriber object
            //    just created

            topicSubscriber.setMessageListener(this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanup() {
        try {
            topicSession.close();
            topicConnection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Vector getMessages() {
        return m_messages;
    }

    public String getMessage() {
        return message;
    }

    /**
     * @param e
     */
    public void onException(JMSException e) {
        //Report the Error and take necessary Error handling measures
        String error = e.getErrorCode();
        System.out.println(error);
    }

    public void onMessage(Message msg) {
        try {
            //  When a message is received, print it out to
            //  standard output
            //
            TextMessage textmsg2 = (TextMessage) msg;
            m_messages.add(msg);
            this.message = textmsg2.getText();

            System.out.println("Received : " + textmsg2.getText());
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

