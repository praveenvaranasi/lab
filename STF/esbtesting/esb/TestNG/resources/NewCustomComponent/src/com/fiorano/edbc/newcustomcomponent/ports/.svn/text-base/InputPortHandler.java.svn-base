
package com.fiorano.edbc.newcustomcomponent.ports;

import com.fiorano.edbc.framework.service.configuration.IServiceConfiguration;
import com.fiorano.edbc.framework.service.exception.ServiceErrorID;
import com.fiorano.edbc.framework.service.exception.ServiceExecutionException;
import com.fiorano.edbc.framework.service.jms.AbstractJMSHandler;
import com.fiorano.edbc.framework.service.jms.ServiceExceptionHandler;
import com.fiorano.edbc.framework.service.jms.ports.AbstractInputPortHandler;
import com.fiorano.edbc.newcustomcomponent.Bundle;
import com.fiorano.edbc.newcustomcomponent.JMSHandler;
import com.fiorano.esb.wrapper.InputPortInstanceAdapter;
import fiorano.esb.util.EventGenerator;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.naming.NamingException;
import java.util.Collection;
import java.util.logging.Level;


/**
 * JMS Message Processor which contains get and set methods for port properties.
 * @author Fiorano Software Technologies Pvt. Ltd.
 * @created Thu, 22 Mar 2007
 */
public class InputPortHandler extends AbstractInputPortHandler {

    /**
     * Creates an InputPortHandler object.
     * @param destination destination object
     * @param inputPortInstanceAdapter input port instance adapter
     * @param outputPortHandlers collection of output port names and their corresponding OutputPortHandlers
     * @param eventGenerator EventGenerator object
     * @param session session object
     */
    public InputPortHandler(Destination destination, InputPortInstanceAdapter inputPortInstanceAdapter, Collection outputPortHandlers,
                            EventGenerator eventGenerator, Session session) {
        super(destination, inputPortInstanceAdapter, outputPortHandlers, eventGenerator, session);
    }

    /**
     * This method creates JMSHandler for every session on an input port
     * @param i session index.
     * @param connection connection object
     * @param iServiceConfiguration configuration object
     * @param serviceExceptionHandler used in handling exceptions.
     * @throws ServiceExecutionException if there is any exception in creating the jmshandler.
     */
    protected AbstractJMSHandler createJMSHandler(int i, Connection connection,
                                                  IServiceConfiguration iServiceConfiguration,
                                                  ServiceExceptionHandler serviceExceptionHandler)
            throws ServiceExecutionException {
        JMSHandler jmsHandler;
        try {
            jmsHandler = new JMSHandler(this, i, connection, iServiceConfiguration, serviceExceptionHandler);
        } catch (JMSException e) {
            if (getLogger().isLoggable(Level.SEVERE)) {
                getLogger().log(Level.SEVERE, Bundle.JMS_OBJECT_CREATION_FAILED, e.getMessage());
            }
            throw new ServiceExecutionException(e, ServiceErrorID.TRANSPORT_ERROR);
        } catch (NamingException e) {
            if (getLogger().isLoggable(Level.SEVERE)) {
                getLogger().log(Level.SEVERE, Bundle.JMS_OBJECT_CREATION_FAILED, e.getMessage());
            }
            throw new ServiceExecutionException(e, ServiceErrorID.TRANSPORT_ERROR);
        }

        return jmsHandler;
    }
}
