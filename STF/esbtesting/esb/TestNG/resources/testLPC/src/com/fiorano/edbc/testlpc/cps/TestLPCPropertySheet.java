package com.fiorano.edbc.testlpc.cps;

import com.fiorano.edbc.framework.service.configuration.IServiceConfiguration;
import com.fiorano.edbc.framework.service.cps.JMXPropertySheet;
import com.fiorano.edbc.testlpc.model.TestLPCPM;

/**
 * Execution class for TestLPC.
 * This class contains the main method which launches the component when started as an external process.
 * It creates the JMSHandler and new configuration object if needed.
 *
 * @author Fiorano Software Technologies Pvt. Ltd.
 */
public class TestLPCPropertySheet extends JMXPropertySheet {

    protected IServiceConfiguration getDefaultConfiguration() {
        return new TestLPCPM();
    }


}
