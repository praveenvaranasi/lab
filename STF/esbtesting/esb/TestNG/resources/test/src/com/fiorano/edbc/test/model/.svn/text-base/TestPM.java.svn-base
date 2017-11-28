
package com.fiorano.edbc.test.model;

import com.fiorano.edbc.framework.service.configuration.AbstractErrorHandlingConfiguration;
import com.fiorano.edbc.framework.service.configuration.ConnectionlessServiceConfiguration;
import com.fiorano.edbc.framework.service.configuration.ConnectionlessErrorHandlingConfiguration;
import com.fiorano.edbc.framework.service.exception.ServiceConfigurationException;
import com.fiorano.edbc.framework.service.exception.ServiceErrorID;
import com.fiorano.edbc.framework.service.exception.ServiceException;
import com.fiorano.util.ErrorListener;
import com.fiorano.util.lang.ClassUtil;
import fiorano.esb.utils.RBUtil;
import com.fiorano.edbc.test.Bundle;
import java.net.URL;


/**
 * PropertyModel class captures the configuration information from the
 * Custom Property Sheet (CPS) of the component.
 * set methods set the property to the values provided in CPS and get methods
 * can be used to get those values.
 * JMX Naming conventions should be followed for these method names.
 *
 * @fiorano.xmbean
 * @jmx.mbean
 * @jboss.xmbean
 */
public class TestPM extends ConnectionlessServiceConfiguration {

    /**
     * Sample property of the component which is captured from the Custom Property Sheet.
     */
    private String myProperty="sample property";

    /**
     * Boolean specifying whether the input message validation should be done or not.
     */
    private boolean inputValidationEnabled;
	
    /**
     * Creates an instance of Property Model.
     */	
	public TestPM() {
	}

    /**
     * Returns the value of myProperty.
     *
     * @jmx.managed-attribute access="read-write" description="this is a sample property"
     * @jmx.descriptor name="displayName" value="My Property"
     * @jmx.descriptor name="defaultValue" value="sample property"
     * @jmx.descriptor name="index" value="0"
     */
    public String getMyProperty() {
        return myProperty;
    }

    /**
     * Sets the value of myProperty.
     *
     * @jmx.managed-attribute
     */
    public void setMyProperty(String val) {
        myProperty = val;
    }

    /**
     * Returns whether the validation of input is enabled or not.
     * Since there is no schema set on component's input port, here the validation is disabled.
     *
     * @jmx.managed-attribute access="read-write" description="The service tries to validate the input received if set to true. If this is set to false,
     * service will not validate the input and hence the performance increases. CAUTION: Setting this to false may cause undesired results if the input xml is
     * not valid"
     * @jmx.descriptor name="displayName" value="Validate input"
     * @jmx.descriptor name="defaultValue" value="true"
     * @jmx.descriptor name="index" value="-1"
     * @jmx.descriptor name="hidden" value="true"
     */
    public boolean isInputValidationEnabled() {
        return false;
    }

    /**
     * sets whether the validation of input is enabled or not
     *
     * @jmx.managed-attribute
     */
    public void setInputValidationEnabled(boolean inputValidationEnabled) {
        this.inputValidationEnabled = inputValidationEnabled;
    }

    /**
     * Validates the configuration parameters in the panel.
     * @param Listener error listener used to notify errors
     * @throws ServiceConfigurationException if there is any exception in validation
     */
    public void validate(ErrorListener Listener) throws ServiceConfigurationException {
        boolean errorOccured = false;
        if (myProperty == null) {
            if (Listener != null) {
                errorOccured = errorOccured | notifyErrorListener(Listener, new ServiceConfigurationException(
                        "Specify My Property", ServiceErrorID.INVALID_CONFIGURATION_ERROR));
            } else {
                throw new ServiceConfigurationException("Specify My Property",
                                                        ServiceErrorID.INVALID_CONFIGURATION_ERROR);
            }
        }

        if (errorOccured) {
            throw new ServiceConfigurationException(RBUtil.getMessage(Bundle.class, Bundle.INVALID_CONFIGURATION),
                                                    ServiceErrorID.INVALID_CONFIGURATION_ERROR);
        }
    }

    /**
    * Notifies error listener of an exception.
    * @param errorListener error listener
    * @param exceptionToNotify exception to be notified
    * @return boolean specifying whether the error listener is notified
    * @throws ServiceConfigurationException if there is any exception while notifying
    */
    protected final boolean notifyErrorListener(ErrorListener errorListener, Exception exceptionToNotify) throws ServiceConfigurationException {
        if (exceptionToNotify == null) {
            return false;
        }
        try {
            errorListener.error(exceptionToNotify);
        } catch (Exception e) {
            throw new ServiceConfigurationException(ServiceErrorID.INVALID_CONFIGURATION_ERROR);
        }
        return true;
    }

    /**
     * Overridden method which tests the component's logic.
     */
    public void test() throws ServiceException {
    }
	
    /**
     * Overridden method generally used for logging.
     */
    public String getAsFormattedString() {
        return "";
    }

    /**
     * @return help URL
     * @jmx.managed-operation description="Help set URL"
     */
    public URL fetchHelpSetURL() {
        return getClass().getResource(getHelpSetName());
    }

    /**
     * @return name of help set.
     */
    public String getHelpSetName() {
        return ClassUtil.getShortClassName(this.getClass()) + ".hs";
    }	

            
}
