package com.fiorano.edbc.ccp;

import com.fiorano.edbc.ccp.Bundle;
import com.fiorano.edbc.framework.service.configuration.IServiceConfiguration;
import com.fiorano.edbc.framework.service.engine.AbstractRequestProcessor;
import com.fiorano.edbc.framework.service.exception.ServiceExecutionException;
import fiorano.esb.record.ESBRecordDefinition;
import fiorano.esb.utils.RBUtil;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Processes the input request.
 *
 * @author Fiorano Software Technologies Pvt. Ltd.
 */
public class RequestProcessor extends AbstractRequestProcessor {
    /**
     * Logger used for logging.
     */
    private Logger logger;

    /**
     * creates an instance of Request Processor.
     *
     * @param schema               If there's any schema on the input port to be validated.
     * @param logger               logger used for logging.
     * @param serviceConfiguration configuration object.
     */
    public RequestProcessor(ESBRecordDefinition schema, Logger logger,
                            IServiceConfiguration serviceConfiguration) {
        super(schema, logger, serviceConfiguration);
        this.logger = logger;
    }

    /**
     * Processes the input request. By default it returns the input message.
     *
     * @param request request string
     * @return response message.
     * @throws ServiceExecutionException if there is any exception in processing the request
     */
    public String process(String request) throws ServiceExecutionException {
        logger.log(Level.INFO, RBUtil.getMessage(Bundle.class, Bundle.REQUEST_PROCESSED, new Object[]{request}));
        return request;
    }

    /**
     * Processes the input request. By default it returns the input object.
     *
     * @param request request object
     * @return response object.
     * @throws ServiceExecutionException if there is any exception in processing the request.
     */
    public Object process(Object request) throws ServiceExecutionException {
        logger.log(Level.INFO, RBUtil.getMessage(Bundle.class, Bundle.REQUEST_PROCESSED, new Object[]{request}));
        return request;
    }
}
