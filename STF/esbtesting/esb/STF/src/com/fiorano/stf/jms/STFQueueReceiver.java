/**
 * Copyright (c) 1999-2017, Fiorano Software Inc. and affiliates.
 * Copyright (c) 2005, Fiorano Software, Inc.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
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
 *
 */
public class STFQueueReceiver extends Thread implements ExceptionListener {
    // queue Name
    String queueName = "primaryQueue";

    //	queu connection factory
    String qcf = "primaryQCF";
    private String url;
    private Vector m_messages = new Vector();
    public boolean stop = false;
    private QueueReceiver m_queueReceiver;
    private QueueSession queueSession = null;
    private QueueConnection queueConnection = null;


    /**
     * This method initialize the environment required
     * to run this sample.
     */
    public void initialize(String queueName, String qcf, String url) {
        if (queueName != null)
            this.queueName = queueName;
        if (qcf != null)
            this.qcf = qcf;
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
        System.out.println("Parameters used for creating a reciever");
        System.out.println("***************************************** ");
        System.out.println("Queue Connection Factory::" + qcf);
        System.out.println("Provider Url::" + url);
        System.out.println("Queuename::" + queueName);
        System.out.println("*****************************************\n ");

    }

    /**
     * This method contains the complete logic of this sample.
     */
    public void runTest() {
        try {
            //  1. Create the InitialContext Object used for looking up
            //     JMS administered objects on the FioranoMQ
            //     located on the default host.
            //
            Hashtable env = new Hashtable();

            env.put(Context.SECURITY_PRINCIPAL, "admin");
            env.put(Context.SECURITY_CREDENTIALS, "passwd");
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.jms.runtime.naming.FioranoInitialContextFactory");

            InitialContext ic = new InitialContext(env);

            // 1.1  Lookup Connection Factory and Queue names
            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ic.lookup(qcf);
            javax.jms.Queue queue = (javax.jms.Queue) ic.lookup(queueName);

            // 2. create and start a queue connection
            System.out.println("Creating queue connection");

            queueConnection = queueConnectionFactory.createQueueConnection();

            // Register an Exception Listner
            queueConnection.setExceptionListener(new STFQueueReceiver());
            queueConnection.start();

            // 3. create queue session on the connection just created
            System.out.println("Creating queue session: not transacted, auto ack");

            queueSession = queueConnection.createQueueSession(false, 1);

            // 4. create subscriber
            System.out.println("Creating queue, subscriber");

            m_queueReceiver = queueSession.createReceiver(queue);

            // 5. install an asynchronous listener/callback on the subscriber object
            //    just created
            System.out.println("Ready to listen for messages synchronously (blocking receive)");
            this.start();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param e
     */
    public void onException(JMSException e) {
        //Report the Error and take necessary Error handling measures
        String error = e.getErrorCode();
        System.out.println(error);
    }

    public synchronized Vector getMessages() {
        return m_messages;
    }

    public void run() {
        while (!stop) {
            System.out.println("Thead::" + this.getName() + " running.");
            try {
                TextMessage textmsg2 = (TextMessage) m_queueReceiver.receive();
//                System.out.println("Received : " + textmsg2.getText());
                this.addMessage(textmsg2);

            } catch (JMSException e) {
                e.printStackTrace();
            }
            synchronized (this) {
                try {
                    wait(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("CLOSING THE THREAD:::::: as stop is set to true.");
    }

    public synchronized void cleanup() {
        this.stop = true;
        notifyAll();
        try {
            m_queueReceiver.close();
            queueSession.close();
            queueConnection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }

    private synchronized void addMessage(TextMessage textmsg2) {
        m_messages.add(textmsg2);
        notifyAll();
        System.out.println("Notifying all threads.. and the variable stop of thread is " + stop);

    }

}
