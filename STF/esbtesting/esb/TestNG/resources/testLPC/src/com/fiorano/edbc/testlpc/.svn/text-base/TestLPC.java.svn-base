package com.fiorano.edbc.testlpc;

import com.fiorano.edbc.framework.service.AbstractInmemoryService;
import com.fiorano.edbc.framework.service.exception.ServiceErrorID;
import com.fiorano.edbc.framework.service.exception.ServiceExecutionException;
import com.fiorano.edbc.framework.service.jms.IJMSObjects;
import com.fiorano.edbc.testlpc.model.TestLPCPM;

/**
 * Execution class for TestLPC.
 * This class contains the main method which launches the component when started as an external process.
 * It creates the JMSHandler and new configuration object if needed.
 *
 * @author Fiorano Software Technologies Pvt. Ltd.
 */
public class TestLPC extends AbstractInmemoryService {

    /**
     * Creates the service instance.
     */
    public TestLPC() {
    }


    /**
     * Creates a JMS Handler
     *
     * @return JMSObjects class which handles the jmsObjects.
     */
    protected IJMSObjects _createJMSObjects() {
        return new JMSObjects(this);
    }

    /**
     * Specifies whether the configuration is mandaory or not.
     *
     * @return whether the configuration is mandatory.
     */
    protected boolean isConfigurationMandatory() {
        return true;
    }

    /**
     * Creates a new Property Model object
     */
    protected void createDefaultServiceConfiguration() {
        configuration = new TestLPCPM();
    }


    /**
     * Main method starts the service in external process.
     *
     * @param args commandline arguments
     */
    public static void main(String args[]) {
        try {
            TestLPC service = new TestLPC();
            service.start(args);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


}
