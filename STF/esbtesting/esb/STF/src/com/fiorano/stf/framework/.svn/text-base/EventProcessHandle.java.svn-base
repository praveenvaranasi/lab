package com.fiorano.stf.framework;

import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.stf.misc.OutputPortMessageListener;
import com.fiorano.stf.remote.RTLClient;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.application.Application;
import fiorano.tifosi.dmi.application.ApplicationParser;
import fiorano.tifosi.dmi.application.PortInstance;
import fiorano.tifosi.dmi.application.ServiceInstance;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Sandeep M
 * @Date:
 */
public class EventProcessHandle {
    private String applicationGuid;
    private float versionNumber;
    private String applicationFilePath;
    private Application application;
    private static final String PEER_INITIAL_CONTEXT_FACTORY = "fiorano.jms.runtime.naming.FioranoInitialContextFactory";
    private RTLClient rtlClient = RTLClient.getInstance();
    private float appVersion = 1.0f;
    public EventProcessHandle(String appGuid, float appVersion, String applicationFilePath) throws Exception {
        this.applicationGuid = appGuid;
        this.versionNumber = appVersion;
        this.applicationFilePath = applicationFilePath;
        try {
            application = RTLClient.getInstance().getFioranoApplicationController().getApplication(appGuid, appVersion);
        } catch (Exception e) {
            application = null;
        }
    }

    public boolean importApplication(boolean overwrite) throws Exception {
        if (overwrite || application == null) {
            File appFile = new File(applicationFilePath);
            if (appFile.isDirectory()) {
                application = ApplicationParser.readApplication(appFile, "development", true);
                rtlClient.getFioranoApplicationController().saveApplication(application);
            } else {
                application = ApplicationParser.readApplication(new File(applicationFilePath));
                rtlClient.getFioranoApplicationController().saveApplication(application);
            }
            return true;
        }
        return false;
    }

    public void startApplication() throws Exception {
        rtlClient.getFioranoApplicationController().prepareLaunch(applicationGuid, versionNumber);
        rtlClient.getFioranoApplicationController().launchApplication(applicationGuid, versionNumber);
        rtlClient.getFioranoApplicationController().startAllServices(applicationGuid,appVersion);
        Thread.sleep(20000);
    }

    public void addListener(String componnetName, String portName, OutputPortMessageListener messageLisener) throws Exception {
        ServiceInstance instance = application.getServiceInstance(componnetName);
        PortInstance port = instance.getPortInstance(portName);
        try {
            String destinationName;
            String fpsName = rtlClient.getFioranoApplicationController().getCurrentStateOfService(applicationGuid,appVersion, componnetName).getServiceNodeName();
            if (port.isSpecifiedDestinationUsed())
                destinationName = port.getDestination();
            else
                destinationName = applicationGuid + "__" + componnetName + "__" + portName;
            if (PortInstance.DESTINATION_TYPE_TOPIC == port.getDestinationType()) {
                createSubscriber(destinationName, applicationGuid, componnetName, fpsName, messageLisener);
            } else {
                createSubscriberonQueue(destinationName, applicationGuid, componnetName, fpsName, messageLisener);
            }

        } catch (Exception e) {
            throw e;
        }
    }

    public void sendMessage(String componentName, String portName, File messageFile) throws Exception {
        String message = getMessageAsString(messageFile);
        sendMessage(componentName, portName, message);
    }

    public void sendMessage(String componentName, String portName, String message) throws Exception {
        ServiceInstance instance = application.getServiceInstance(componentName);
        PortInstance port = instance.getPortInstance(portName);
        try {
            String destination;
            if (port.isSpecifiedDestinationUsed())
                destination = port.getDestination();
            else
                destination = applicationGuid + "__" + appVersion + "__" + componentName + "__" + portName;
            String fpsName = rtlClient.getFioranoApplicationController().getCurrentStateOfService(applicationGuid,appVersion, componentName).getServiceNodeName();
            if (PortInstance.DESTINATION_TYPE_QUEUE == port.getDestinationType()) {
                sendMessageOnQueue(destination, fpsName, message);
            } else
                sendMessageOnTopic(destination, fpsName, message);
        } catch (Exception e) {
            throw e;
        }


    }

    private void sendMessageOnQueue(String destination, String fpsName, String message) throws Exception {
        Vector backupUrls = null;
        InitialContext ic = null;
        //get the Topic Connection Factory.
        //The topic connection factory is defined as T__<APPLICATIONGUID>__<ComponentInstanceName>
        String qcf = "primaryQCF";
        try {
            String peerServerUrl = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
            backupUrls = rtlClient.getFioranoFPSManager().getBackupURLsForFPS(fpsName);
            Hashtable env = new Hashtable();

            env.put(Context.PROVIDER_URL, peerServerUrl);
            env.put(Context.INITIAL_CONTEXT_FACTORY, PEER_INITIAL_CONTEXT_FACTORY);

            String backupURLString = getBackupUrls(backupUrls);
            if (backupURLString != null)
                env.put("BackupConnectURLs", backupURLString);

            ic = new InitialContext(env);


            //ic = createInitialContext(peerServerUrl, backupUrls);

            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ic.lookup(qcf);


            javax.jms.Queue queue = (javax.jms.Queue) ic.lookup(destination);

            QueueConnection qc = queueConnectionFactory.createQueueConnection();

            qc.start();

            QueueSession qs = qc.createQueueSession(false, 1);

            QueueSender sender = qs.createSender(queue);

            TextMessage textMessage = qs.createTextMessage();

            // Set and Publish the message as Non-persistent Message
            textMessage.setText(message);
            sender.send(textMessage, DeliveryMode.PERSISTENT, 4, 0);

            qs.close();
            qc.close();
        } catch (Exception ex) {
            throw ex;
        }

    }

    public void sendMessageOnTopic(String componentName, String portName, File messageFile) throws Exception {

        String message = getMessageAsString(messageFile);
        ServiceInstance instance = application.getServiceInstance(componentName);
        PortInstance port = instance.getPortInstance(portName);
        try {
            String fpsName = rtlClient.getFioranoApplicationController().getCurrentStateOfService(applicationGuid,appVersion, componentName).getServiceNodeName();

            String topicName = applicationGuid + "__" + componentName + "__" + portName;
            Vector backupUrls = null;
            InitialContext ic = null;
            String tcf = "primaryTCF";

            String peerServerUrl = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
            backupUrls = rtlClient.getFioranoFPSManager().getBackupURLsForFPS(fpsName);
            Hashtable env = new Hashtable();

            env.put(Context.PROVIDER_URL, peerServerUrl);
            env.put(Context.INITIAL_CONTEXT_FACTORY, PEER_INITIAL_CONTEXT_FACTORY);

            String backupURLString = getBackupUrls(backupUrls);
            if (backupURLString != null)
                env.put("BackupConnectURLs", backupURLString);

            ic = new InitialContext(env);


            //ic = createInitialContext(peerServerUrl, backupUrls);

            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ic.lookup(tcf);


            javax.jms.Topic topic = (javax.jms.Topic) ic.lookup(topicName);

            TopicConnection tc = topicConnectionFactory.createTopicConnection();

            tc.start();

            TopicSession ts = tc.createTopicSession(false, 1);

            TopicPublisher sender = ts.createPublisher(topic);

            TextMessage textMessage = ts.createTextMessage();

            // Set and Publish the message as Non-persistent Message
            textMessage.setText(message);
            sender.send(textMessage, DeliveryMode.PERSISTENT, 4, 0);


            ts.close();
            tc.close();
        } catch (Exception ex) {
            throw ex;
        }

    }

    public void sendMessageOnTopic(String destination, String fpsName, String message) throws Exception {

        try {
            Vector backupUrls = null;
            InitialContext ic = null;
            String tcf = "primaryTCF";

            String peerServerUrl = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
            backupUrls = rtlClient.getFioranoFPSManager().getBackupURLsForFPS(fpsName);
            Hashtable env = new Hashtable();

            env.put(Context.PROVIDER_URL, peerServerUrl);
            env.put(Context.INITIAL_CONTEXT_FACTORY, PEER_INITIAL_CONTEXT_FACTORY);

            String backupURLString = getBackupUrls(backupUrls);
            if (backupURLString != null)
                env.put("BackupConnectURLs", backupURLString);

            ic = new InitialContext(env);

            //ic = createInitialContext(peerServerUrl, backupUrls);

            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ic.lookup(tcf);


            javax.jms.Topic topic = (javax.jms.Topic) ic.lookup(destination);

            TopicConnection tc = topicConnectionFactory.createTopicConnection();

            tc.start();

            TopicSession ts = tc.createTopicSession(false, 1);

            TopicPublisher sender = ts.createPublisher(topic);

            TextMessage textMessage = ts.createTextMessage();

            // Set and Publish the message as Non-persistent Message
            textMessage.setText(message);
            sender.send(textMessage, DeliveryMode.PERSISTENT, 4, 0);

            ts.close();
            tc.close();
        } catch (Exception ex) {
            throw ex;
        }

    }

    private String getMessageAsString(File messageFile) throws IOException {
        String toReturn = new String();
        try {

            FileInputStream fis = new FileInputStream(messageFile);
            byte[] buff = new byte[1024];
            int bytesRead = -1;
            while ((bytesRead = fis.read(buff)) != -1)
                toReturn = toReturn + new String(buff, 0, bytesRead);
        } catch (IOException e) {
            throw e;
        }
        return toReturn;
    }


    private void createSubscriber(String topicName, String applicationGUID,
                                  String componentName, String fpsName, OutputPortMessageListener messageLisener) throws Exception {


        Vector backupUrls = null;
        InitialContext ic = null;
        //get the Topic Connection Factory.
        //The topic connection factory is defined as T__<APPLICATIONGUID>__<ComponentInstanceName>
        String tcf = "primaryTCF";
        try {
            String peerServerUrl = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
            backupUrls = rtlClient.getFioranoFPSManager().getBackupURLsForFPS(fpsName);
            Hashtable env = new Hashtable();

            env.put(Context.PROVIDER_URL, peerServerUrl);
            env.put(Context.INITIAL_CONTEXT_FACTORY, PEER_INITIAL_CONTEXT_FACTORY);

            String backupURLString = getBackupUrls(backupUrls);
            if (backupURLString != null)
                env.put("BackupConnectURLs", backupURLString);

            ic = new InitialContext(env);
            //ic = createInitialContext(peerServerUrl, backupUrls);

            TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) ic.lookup(tcf);
            Topic topic = (Topic) ic.lookup(topicName);

            TopicConnection topicConnection = topicConnectionFactory.createTopicConnection();

            topicConnection.setExceptionListener(messageLisener);
            topicConnection.start();


            TopicSession topicSession = topicConnection.createTopicSession(false, 1);

            TopicSubscriber topicSubscriber = topicSession.createSubscriber(topic);

            // install an asynchronous listener/callback on the subscriber object
            //    just created
            topicSubscriber.setMessageListener(messageLisener);
        } catch (Exception e) {
            throw e;
        }
    }

    private void createSubscriberonQueue(String queueName, String applicationGUID,
                                         String componentName, String fpsName, OutputPortMessageListener messageLisener) throws Exception {


        Vector backupUrls = null;
        InitialContext ic = null;
        //get the Topic Connection Factory.
        //The topic connection factory is defined as T__<APPLICATIONGUID>__<ComponentInstanceName>
        String qcf = "primaryQCF";
        try {
            String peerServerUrl = rtlClient.getFioranoFPSManager().getConnectURLForFPS(fpsName);
            backupUrls = rtlClient.getFioranoFPSManager().getBackupURLsForFPS(fpsName);
            Hashtable env = new Hashtable();

            env.put(Context.PROVIDER_URL, peerServerUrl);
            env.put(Context.INITIAL_CONTEXT_FACTORY, PEER_INITIAL_CONTEXT_FACTORY);

            String backupURLString = getBackupUrls(backupUrls);
            if (backupURLString != null)
                env.put("BackupConnectURLs", backupURLString);

            ic = new InitialContext(env);
            //ic = createInitialContext(peerServerUrl, backupUrls);

            QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) ic.lookup(qcf);
            Queue queue = (Queue) ic.lookup(queueName);

            QueueConnection queueConnection = queueConnectionFactory.createQueueConnection();

            queueConnection.setExceptionListener(messageLisener);
            queueConnection.start();


            QueueSession queueSession = queueConnection.createQueueSession(false, 1);

            MessageConsumer queueReceiver = queueSession.createConsumer(queue);
            // install an asynchronous listener/callback on the subscriber object
            // just created
            queueReceiver.setMessageListener(messageLisener);
        } catch (Exception e) {
            throw e;
        }
    }

    private String getBackupUrls(Vector backupUrls) {
        if (backupUrls == null || backupUrls.isEmpty())
            return null;
        String toReturn = null;
        for (int i = 1; i <= backupUrls.size(); i++) {
            toReturn = (String) backupUrls.get(i - 1);
            if (!(i == backupUrls.size()))
                toReturn = toReturn + ":";

        }
        return toReturn;
    }


    public void killApplication() throws Exception {
        rtlClient.getFioranoApplicationController().killApplication(applicationGuid,appVersion);
    }

    public void deleteApplication() throws Exception {
        rtlClient.getFioranoApplicationController().deleteApplication(applicationGuid, versionNumber);
    }
}
