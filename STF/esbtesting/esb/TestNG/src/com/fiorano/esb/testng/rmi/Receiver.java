package com.fiorano.esb.testng.rmi;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: Nov 17, 2010
 * Time: 3:23:37 PM
 * To change this template use File | Settings | File Templates.
 */


import com.fiorano.stf.remote.RMIClient;
import fiorano.jms.services.msg.def.FioranoTextMessage;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * the code has been picked up from FMQ/Samples/PTP/QRecieiver
 */
public class Receiver implements ExceptionListener {
    private FioranoTextMessage msg;
    private int count = 0;

    public String getDestinationName() {
        return destinationName;
    }

    public void setDestinationName(String destinationName) {
        this.destinationName = destinationName;
    }

    public String getCf() {
        return cf;
    }

    public void setCf(String qcf) {
        this.cf = qcf;
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
    private Object recFile;
    private ArrayList arrlist;
    private RMIClient rmiClient = RMIClient.getInstance();


    private InitialContext ic;
    private MessageConsumer consumer;
    private Connection connection;
    private Session session;
    private Destination destination;
    private boolean blockingReceive = false;
    private boolean saveMessages = false;

    private ArrayList<Message> messages;

    public Receiver() {
    }

    public Receiver(boolean blockingReceive, boolean saveMessages) {
        this.blockingReceive = blockingReceive;
        this.saveMessages = saveMessages;
    }

    /**
     * initializes the receiver. the receiver is created to the underlying mq of peer server specified by peerName.
     *
     * @param peerName
     */

    public void initialize(String peerName) throws Exception {


        //  1. Create the InitialContext Object used for looking up
        //     JMS administered objects on the FioranoMQ
        //     located on the default host.
        //
        rmiClient = RMIClient.getInstance(); //assuming there can be only one rmi client
        Hashtable env = new Hashtable();

        env.put(Context.SECURITY_PRINCIPAL, user);
        env.put(Context.SECURITY_CREDENTIALS, passwd);
        String url = null;
        if ((peerName.equals("fps") || peerName.equals("fps1"))) {
            url = rmiClient.getFpsManager().getConnectURLForPeer(peerName);
            if (url.endsWith("2010"))
                url = "http://0.0.0.0:1877";
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

        ic = new InitialContext(env);


        // 1.1  Lookup Connection Factory and destination names
        //
        ConnectionFactory connectionFactory = null;
        connectionFactory = (ConnectionFactory) ic.lookup(cf);
        destination = (Destination) ic.lookup(destinationName);


        // 2. create and start a  connection
        connection = connectionFactory.createConnection();

        // Register an Exception Listner
        connection.setExceptionListener(new Receiver());
        connection.start();

        // 3. create  session on the connection just created
        session = connection.createSession(false, 1);

        // 4. create subscriber
        if (!blockingReceive)
            consumer = session.createConsumer(destination);
        else {
            consumer = session.createConsumer(destination);
            consumer.setMessageListener(new MyMessageListener());
//            session.setMessageListener(new MyMessageListener());
        }

        Thread.sleep(2000);

    }

    public class MyMessageListener implements MessageListener {
        public void onMessage(Message message) {
            count++;

            if (saveMessages) {
                if (messages == null)
                    messages = new ArrayList<Message>();

                messages.add(message);
            }
        }
    }

    public int getCount() {
        return count;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public String getMessageOnDestination() throws Exception {
        TextMessage textmsg2 = (TextMessage) consumer.receive();
        return (textmsg2.getText());
    }

    //this function will wait for given time and returns message if the message is received else returns null
    public String getMessageOnDestination(long time) throws Exception {
        TextMessage textmsg2 = (TextMessage) consumer.receive(time);
        return (textmsg2.getText());
    }

    public Message getMessage() throws Exception {

        return consumer.receive();
    }


    public Object getMsgObj() {
        try {
            Message msg = consumer.receive();
            recFile = msg.getObjectProperty("Attachment");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recFile;

    }
    public Object getMsgObj(Object temp) {
        try {
            ObjectMessage msg = (ObjectMessage)consumer.receive();
            recFile = msg.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return recFile;

    }


    /**
     * @param e
     */
    public void onException(JMSException e) {
        //Report the Error and take necessary Error handling measures
        String error = e.getErrorCode();

        System.out.println(error);
    }

    public void close() {
        try {
            session.close();
        } catch (Exception e) {

        }
        try {
            connection.close();
        } catch (Exception e) {

        }
    }

}
