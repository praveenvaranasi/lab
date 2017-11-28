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
public class STFQueueSender implements ExceptionListener {
    // queue Name
    String queueName = "MyQueue";
    //	queu connection factory
    String qcf = "primaryQCF";
    // provider url
    String url = "http://localhost:1867";
    private QueueConnection qc;
    private QueueSession qs;
    private QueueSender sender;

    /**
     * This method initialize the environment required
     * to run this sample.
     */
    public void initialize(String queueName, String qcf, String url) {

        // fill in the hashtable with command line args
        if (queueName != null)
            this.queueName = queueName;
        // get the qcf
        if (qcf != null)
            this.qcf = qcf;
        // get the url
        if (url != null)
            this.url = url;

        // print the configuration
        printConfiguration();
    }

    /**
     * This method contains the complete logic of this sample.
     */
    public void send(String message, int count) {
        System.out.println("The Message ::" + message + " " + count + " times.");
        try {
            //  1. Create the InitialContext Object used for looking up
            //     JMS administered objects on the FioranoMQ
            //     located on the default host.
            //

            Hashtable env = new Hashtable();

            env.put(Context.SECURITY_PRINCIPAL, "anonymous");
            env.put(Context.SECURITY_CREDENTIALS, "anonymous");
            env.put(Context.PROVIDER_URL, url);
            env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.jms.runtime.naming.FioranoInitialContextFactory");

            InitialContext ic = new InitialContext(env);

            System.out.println("Created InitialContext :: " + ic);

            // 1.1  Lookup Connection Factory and Queue names
            //
            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ic.lookup(qcf);

            System.out.println("Looked up qcf " + queueConnectionFactory);

            javax.jms.Queue queue = (javax.jms.Queue) ic.lookup(queueName);

            System.out.println("Creating queue connection.");

            // 2. Create and start a queue connection
            //System.out.println("Creating queue connection");
            qc = queueConnectionFactory.createQueueConnection();

            // Set the ExceptionListener for this QueueConnection
            qc.setExceptionListener(new STFQueueSender());
            qc.start();

            System.out.println("Creating queue session");

            // 3. Create a Queue session on this connection
            //System.out.println("Creating Queue session: not transacted, auto ack");
            qs = qc.createQueueSession(false, 1);

            System.out.println("Creating queue sender");

            // 4. Create a publisher for this queue
            //System.out.println("Creating Queue ,publisher");
            sender = qs.createSender(queue);

            // 5. Create a text message for use in the while loop
            TextMessage textMessage = qs.createTextMessage();


            for (int i = 0; i < count; i++) {
                textMessage.setText(message);
                //To support userdocid based operations for sbw test
                textMessage.setStringProperty("ESBX__SYSTEM__USER_DEFINED_DOC_ID", "siren");
                sender.send(textMessage, DeliveryMode.PERSISTENT, 4, 0);
                //qs.commit();
                //Thread.sleep(100L);
            }

            System.out.println("Closing queue session and queue connection");
            qs.close();
            qc.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * If a JMS provider detects a serious problem with a
     * Connection this method is invoked passing it a JMSException
     * describing the problem.
     *
     * @param e JMSException raised by FMQ client libraries
     */
    public void onException(JMSException e) {
        //Report the Error and take necessary Error handling measures
        String error = e.getErrorCode();
        System.out.println(error);
    }

    /**
     * This method prints the configuration parameters used
     * for running this sample.
     */
    public void printConfiguration() {
        System.out.println("Parameters used by the sample");
        System.out.println("***************************************** ");
        System.out.println("Queue Connection Factory::" + qcf);
        System.out.println("Provider Url::" + url);
        System.out.println("Queuename::" + queueName);
        System.out.println("*****************************************\n ");
    }

    public void cleanup() {
        try {
            sender.close();
            qs.close();
            qc.close();
        } catch (Exception e) {
            e.printStackTrace();
            //ignore
        }
    }
}

