
package com.fiorano.edbc.newcustomcomponent;

import com.fiorano.edbc.framework.service.jms.AbstractJMSObjects;
import com.fiorano.edbc.framework.service.jms.ports.AbstractInputPortHandler;
import com.fiorano.edbc.newcustomcomponent.ports.InputPortHandler;
import com.fiorano.esb.wrapper.InputPortInstanceAdapter;
import fiorano.esb.util.EventGenerator;

import javax.jms.Destination;
import javax.jms.Session;
import java.util.Collection;

/**
 * JMSObjects handles the creation of connection parameters, does the lookup and creates the JMSHandler.
 *
 * @author Fiorano Software Technologies Pvt. Ltd.
 */
public class JMSObjects extends AbstractJMSObjects {

    /**
     * Creates an instance of JMSObjects.
     * @param service service class object.
     */
    public JMSObjects(NewCustomComponent service) {
        super(service);
    }

	/**
     *
     * @param destination receive destination.
     * @param inputPortInstanceAdapter InputPortInstanceAdapter is an adapter (wrapper class) for InputPortInstance.
     * @param outputPortHandlers output port handlers collection.
     * @param eventGenerator used in raising events.
     * @param eventSession session object.
     * @return input port handler object.
     */
    protected AbstractInputPortHandler createInputPortHandler(Destination destination,
                                                              InputPortInstanceAdapter inputPortInstanceAdapter,
                                                              Collection outputPortHandlers,
                                                              EventGenerator eventGenerator, Session eventSession) {
        return new InputPortHandler(destination, inputPortInstanceAdapter, outputPortHandlers, eventGenerator, eventSession);
    }
}

