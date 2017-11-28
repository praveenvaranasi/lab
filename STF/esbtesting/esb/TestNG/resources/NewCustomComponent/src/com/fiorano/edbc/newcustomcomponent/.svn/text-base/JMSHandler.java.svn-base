
package com.fiorano.edbc.newcustomcomponent;

import com.fiorano.edbc.framework.service.configuration.IServiceConfiguration;
import com.fiorano.edbc.framework.service.jms.AbstractJMSHandler;
import com.fiorano.edbc.framework.service.jms.ServiceExceptionHandler;
import com.fiorano.edbc.newcustomcomponent.ports.InputPortHandler;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.naming.NamingException;

/**
 * JMSHandler class holds the MessageProducer, MessageConsumer, InputPortHandler and session for every session
 * on the InputPort.
 * @author Fiorano Software Technologies Pvt. Ltd.
 * @created Thu, 22 Mar 2007
  */

public class JMSHandler extends AbstractJMSHandler {

    /**
     * Creates a JMSHandler object which is used in creating the JMS objects like Session, MessageProducer,
     * MessageConsumer etc to start the service.
     * @param inportHandler InputPortHandler object
     * @param index sessionIndex of an input port
     * @param connection connection object
     * @param serviceConfiguration service configuration object
     * @param exceptionHandler used to handle exceptions.
     * @throws JMSException if the JMSProvider fails to create JMS objects due to some internal error
     * @throws NamingException if a naming exception is encountered
     */
    public JMSHandler(InputPortHandler inportHandler, int index, Connection connection,
                      IServiceConfiguration serviceConfiguration, ServiceExceptionHandler exceptionHandler) throws JMSException, NamingException {
        super(inportHandler, index, connection, serviceConfiguration, exceptionHandler);
    }


    protected MessageListener createMessageListener(ServiceExceptionHandler serviceExceptionHandler) {
        return new NewCustomComponentMessageListener(this, serviceExceptionHandler);
    }
}

