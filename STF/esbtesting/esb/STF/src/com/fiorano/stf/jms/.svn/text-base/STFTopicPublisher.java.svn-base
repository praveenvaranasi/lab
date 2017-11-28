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

/**
 * <p><strong> </strong> represents </p>
 *
 * @author FSIPL
 * @version 1.0
 * @created December 31, 2004
 */
public class STFTopicPublisher implements ExceptionListener {
    String topicName = "primaryTopic";
    String tcf = "primaryTCF";

    // provider url
    String url = "http://localhost:1867";
    private String m_message = null;
    private boolean m_initialised = false;
    private TopicSession ts = null;
    private TopicPublisher tp = null;
    private TopicConnection tc = null;


    public void publish(String topicName, String tcf, String url, String message) {
        // create an instance of topic publisher
        STFTopicPublisher publisher = new STFTopicPublisher();

        // intialize the arguments
        publisher.initialize(topicName, null, url, message);

        // run the test
        publisher.runTest();

    }

    /**
     * This method initialize the environment required
     * to run this sample.
     */
    public void initialize(String topicName, String tcf, String url, String message) {
        if (topicName != null)
            this.topicName = topicName;
        if (tcf != null)
            this.tcf = tcf;
        if (url != null)
            this.url = url;

        this.m_message = message;
        printConfiguration();
    }

    /**
     * This method contains the complete logic of this sample.
     */
    public void runTest() {
        if (m_initialised) {
            try {
                TextMessage textmsg1 = ts.createTextMessage();
                // Set and Publish the message as Non-persistent Message
                textmsg1.setText(m_message);
                tp.publish(textmsg1, DeliveryMode.NON_PERSISTENT, 4, 0);
            } catch (JMSException e) {
                e.printStackTrace();
            }
            return;
        }
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

            tc = topicConnectionFactory.createTopicConnection();

            // Set the ExceptionListener for this TopicConnection
            tc.setExceptionListener(new STFTopicPublisher());
            tc.start();

            ts = tc.createTopicSession(false, 1);

            tp = ts.createPublisher(topic);

            // 5. Create a text message for use in the while loop
            TextMessage textmsg1 = ts.createTextMessage();
            // Set and Publish the message as Non-persistent Message
            textmsg1.setText(m_message);
            tp.publish(textmsg1, DeliveryMode.NON_PERSISTENT, 4, 0);

            m_initialised = true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cleanup() {
        try {
            ts.close();
            tc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // main

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
     * If a JMS provider detects a serious problem with a
     * Connection this method is invoked passing it a JMSException
     * describing the problem.
     *
     * @param e
     */
    public void onException(JMSException e) {
        //Report the Error and take necessary Error handling measures
        String message = e.getMessage();

        System.out.println(message);
    }
}

