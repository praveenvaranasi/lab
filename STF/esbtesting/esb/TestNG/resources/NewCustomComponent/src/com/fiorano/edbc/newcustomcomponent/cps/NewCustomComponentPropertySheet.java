
package com.fiorano.edbc.newcustomcomponent.cps;

import com.fiorano.edbc.newcustomcomponent.model.NewCustomComponentPM;
import com.fiorano.edbc.framework.service.configuration.IServiceConfiguration;
import com.fiorano.edbc.framework.service.cps.JMXPropertySheet;
/**
 * Execution class for NewCustomComponent.
 * This class contains the main method which launches the component when started as an external process.
 * It creates the JMSHandler and new configuration object if needed.
 *
 * @author Fiorano Software Technologies Pvt. Ltd.
 */
public class NewCustomComponentPropertySheet extends JMXPropertySheet {

     protected IServiceConfiguration getDefaultConfiguration() {
        return new NewCustomComponentPM();
    }


}
