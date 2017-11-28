package com.fiorano.edbc.ccp;

/**
 * This class contains the messages used by the service in logging
 * or events.
 *
 * @author Fiorano Software Technologies Pvt. Ltd.
 * @created Thu, 22 Mar 2007
 */
public interface Bundle {

    /**
     * @msg.message msg="Sent Message to destination: {0}. Message: {1}"
     */
    public final static String SENT_MESSAGE = "sent_message";

    /**
     * @msg.message msg=" Created producer."
     */
    public final static String PRODUCER_CREATED = "producer_created";

    /**
     * @msg.message msg=" Naming exception while trying to lookup. Reason {0}. "
     */
    public final static String NAMING_EXCEPTION = "naming_exception";

    /**
     * @msg.message msg=" Unable to send the message to destination: {0} <REASON>{1}</REASON>"
     */
    public final static String SEND_DEST_FAILED = "send_dest_failed";

    /**
     * @msg.message msg=" Unable to create jms object <REASON>{0}</REASON>"
     */
    public final static String JMS_OBJECT_CREATION_FAILED = "jms_object_creation_failed";

    /**
     * @msg.message msg=" Error in closing the connection  {0} <REASON>{1}</REASON>"
     */
    public final static String ERROR_IN_CLOSE_CONNECTION = "error_in_close_connection";

    /**
     * @msg.message msg="service configuration is invalid."
     */
    public final static String INVALID_CONFIGURATION = "invalid_configuration";

    /**
     * @msg.message msg="Processed the input request. {0}"
     */
    public final static String REQUEST_PROCESSED = "request_processed";

    /**
     * @msg.message msg="Message \" {0} \" sent successfully."
     */
    public final static String MESSAGE_SENT_SUCCESSFULLY = "message_sent_successfully";


}