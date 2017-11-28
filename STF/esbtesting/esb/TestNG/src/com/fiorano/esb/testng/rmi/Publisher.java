package com.fiorano.esb.testng.rmi;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: Nov 17, 2010
 * Time: 5:39:34 PM
 * To change this template use File | Settings | File Templates.
 */

import com.fiorano.stf.remote.RMIClient;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * the code has been picked up from FMQ/Samples/PubSUb/publisher
 */
public class Publisher implements ExceptionListener {

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String cf) {
        this.cf = cf;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    private String destinationName;
    private String cf;
    private String user;
    private String passwd;
    private MessageProducer sender;
    private Connection connection;
    private Session session;
    private Destination destination;
    private ArrayList arrlist;
    private RMIClient rmiClient = RMIClient.getInstance();


    public Session getSession() {
        return session;
    }

    /**
     * initializes the publisher. the publisher is created to the underlying mq of peer server specified by peerName.
     *
     * @param peerName
     */
    public void initialize(String peerName) throws Exception {
        rmiClient = RMIClient.getInstance();//assuming there can be only one rmi client

        Hashtable env = new Hashtable();
        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, passwd);
        String url = null;

        if ((peerName == "fps") || (peerName == "fps1")) {
            url = rmiClient.getFpsManager().getConnectURLForPeer(peerName);
        } else if (peerName.equals("hafps"))
            url = rmiClient.getFpsManager().getConnectURLForPeer(peerName);
        else {

            arrlist = AbstractTestNG.getActiveFESUrl();
            int port = 1847;
            if (arrlist.get(1).equals("2047")) {
                port = 1847;
            }
            url = "http://" + arrlist.get(0) + ":" + port;
        }

        env.put(Context.PROVIDER_URL, url);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.jms.runtime.naming.FioranoInitialContextFactory");
        env.put("BackupConnectURLs", "http://localhost:1956");

        InitialContext ic = null;

        ic = new InitialContext(env);
        // 1.1  Lookup Connection Factory and Destination names
        //
        ConnectionFactory connectionFactory = null;
        connectionFactory = (ConnectionFactory) ic.lookup(cf);
        destination = (Destination) ic.lookup(destinationName);
        // 2. Create and start a connection
        connection = connectionFactory.createConnection();
        // Set the ExceptionListener for this Connection
        connection.setExceptionListener(new Publisher());
        connection.start();
        // 3. Create a  session on this connection
        session = connection.createSession(false, 1);
        // 4. Create a publisher for this destination
        sender = session.createProducer(destination);


    }

    public void sendMessage(String mes) throws Exception {
        if (isClosed) {
            throw new Exception("Cannot send message as session/connection has been closed!");
        }
        if (sender == null) {
            throw new Exception("Cannot send message as this producer has not been initialized!");
        }
        TextMessage textmsg1 = session.createTextMessage();
        textmsg1.setText(mes);
        sender.send(textmsg1, DeliveryMode.PERSISTENT, 4, 0);
    }

    public void sendMessage(Message mes) throws Exception {
        if (isClosed) {
            throw new Exception("Cannot send message as session/connection has been closed!");
        }
        if (sender == null) {
            throw new Exception("Cannot send message as this producer has not been initialized!");
        }
        sender.send(mes, DeliveryMode.PERSISTENT, 4, 0);

    }


    private boolean isClosed = false;

    public void close() {

        try {
            session.close();
        } catch (Exception ignore) {

        }
        try {
            connection.close();
        } catch (Exception ignore) {

        }
        isClosed = true;
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
