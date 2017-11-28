
package com.fiorano.edbc.test;

import com.fiorano.edbc.framework.service.engine.IRequestProcessor;
import com.fiorano.edbc.framework.service.exception.ServiceExecutionException;
import com.fiorano.edbc.framework.service.exception.ServiceErrorID;
import com.fiorano.edbc.framework.service.jms.AbstractMessageListener;
import com.fiorano.edbc.framework.service.jms.ServiceExceptionHandler;
import fiorano.esb.record.ESBRecordDefinition;
import fiorano.esb.utils.RBUtil;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * In order to receive message asynchronously as they are delivered to the message consumer, the client program needs to
 * create a message listener that implements the MessageListener interface.
 * MessageListener Listens to the JMSMessages, processes them and sends the result JMSMessage.
 * When a message listener is set on the consumer object, the session passes the incoming JMS messages to the
 * onMessage(..) method.
 * @author Fiorano Software Technologies Pvt. Ltd.
 * @created Thu, 22 Mar 2007
 */
public class TestMessageListener extends AbstractMessageListener {

    /**
     * Used to get the jms related objects like session, producer etc.
     */
    private JMSHandler jmsHandler;

    /**
     * Request processor object.
     */
    private RequestProcessor requestProcessor;

    /**
     * ServiceExceptionHandler which handles the exception
     */
    private ServiceExceptionHandler exceptionHandler;

    /**
     * Creates a Message Listener object.
     * @param jmsHandler jmshanlder object
     * @param exceptionHandler used in handling exceptions.
     */
    public TestMessageListener(JMSHandler jmsHandler, ServiceExceptionHandler exceptionHandler) {
        this.jmsHandler = jmsHandler;
        ESBRecordDefinition recordDefinition = jmsHandler.getInputPortHandler().getInputPortInstanceAdapter().getSchema();
        requestProcessor = new RequestProcessor(recordDefinition, jmsHandler.getLogger(), jmsHandler.getServiceConfiguration());
        this.exceptionHandler = exceptionHandler;
    }

    /**
     * Returns the request processor.
     * @return requestProcessor object.
     */
    protected IRequestProcessor getRequestProcessor() {
        return requestProcessor;
    }

    /**
     * Gets the session object
     * @return session object
     */
    protected Session getSession() {
        return jmsHandler.getSession();
    }

    /**
     * Gets the logger
     * @return logger used for logging
     */
    protected Logger getLogger() {
        return jmsHandler.getLogger();
    }

    /**
     * Sends the response message after processing the input message.
     * Currently the message is handled in the AbstractMessageListener class.
     * handleMessage(..) can be overidden to implement our own handler.
     * @param message output message
     * @throws com.fiorano.edbc.framework.service.exception.ServiceExecutionException if there is any exception in sending the message.
     */
    protected void sendResponse(Message message) throws ServiceExecutionException {
        try {
            jmsHandler.sendMessage(message);            
        } catch (JMSException e) {
            throw new ServiceExecutionException(e, ServiceErrorID.RESPONSE_GENERATION_ERROR);
        }
		getLogger().log(Level.INFO, RBUtil.getMessage(Bundle.class, Bundle.MESSAGE_SENT_SUCCESSFULLY, new Object[]{message}));
    }

    /**
     * Gets the exception handler.
     * @return exceptionHandler object.
     */
    protected ServiceExceptionHandler getExceptionHandler() {
        return exceptionHandler;
    }

}
