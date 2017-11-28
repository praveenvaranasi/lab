/**
 * Copyright (c) 2005-2007, Fiorano Software Technologies Pvt. Ltd.,
 *
 * All rights reserved.
 *
 * This software is the confidential and proprietary information
 * of Fiorano Software ("Confidential Information").  You
 * shall not disclose such Confidential Information and shall use
 * it only in accordance with the terms of the license agreement
 * enclosed with this product or entered into with Fiorano.
 */
package com.fiorano.stf.remote;

import com.fiorano.esb.rtl.application.FioranoApplicationController;
import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.rtl.dm.FioranoDeploymentManager;
import com.fiorano.esb.rtl.events.FioranoEventsManager;
import com.fiorano.esb.rtl.fps.FioranoFPSManager;
import com.fiorano.esb.rtl.sbw.FioranoSBWManager;
import com.fiorano.esb.rtl.security.FioranoSecurityManager;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.rtl.server.FioranoServiceProviderFactory;
import com.fiorano.stf.framework.ServerStatusController;
import fiorano.tifosi.common.TifosiException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * @author lokesh
 */
public class RTLClient implements RemoteClient {

    private static RTLClient rtlClient;

    private String userName = "admin";
    private String password = "passwd";
    private String contextFactory = "fiorano.tifosi.jndi.InitialContextFactory";
    private String serviceProvider = "TifosiServiceProvider";

    private FioranoServiceProvider fioranoServiceProvider;

    /**
     * Application Controller RTL module
     */
    private FioranoApplicationController fioranoApplicationController;

    /**
     * Service Repository object
     */
    private FioranoServiceRepository fioranoServiceRepository;

    /**
     * Deployment manager object
     */
    private FioranoDeploymentManager fioranoDeploymentManager;

    /**
     * events manager object
     */
    private FioranoEventsManager fioranoEventsManager;

    /**
     * Peer Server Manager object
     */
    private FioranoFPSManager fioranoFPSManager;

    /**
     * SBW Manager object
     */
    private FioranoSBWManager fioranoSBWManager;

    /**
     * Security Manager object
     */
    private FioranoSecurityManager fioranoSecurityManager;


    public static RTLClient getInstance() {
        if (rtlClient == null)
            rtlClient = new RTLClient();
        return rtlClient;
    }

    public static RTLClient getInstance(String username, String passwd) {
        if (rtlClient == null)
            rtlClient = new RTLClient(username, passwd);
        return rtlClient;
    }

    private RTLClient() {
        String providerURL = "tsp_tcp://" + ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer() + ":1947";
        FioranoServiceProviderFactory fioranoFactory = setupEnvironment(providerURL);
        try {
            fioranoServiceProvider = fioranoFactory.createServiceProvider(userName, password);
        } catch (TifosiException tifosiException) {
            if (tifosiException.getErrorCode().equals("UNABLE_TO_CONNECT")) {
                try {
                    providerURL = "tsp_tcp://" + ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer() + ":1948";
                    fioranoFactory = setupEnvironment(providerURL);
                    fioranoServiceProvider = fioranoFactory.createServiceProvider(userName, password);
                } catch (TifosiException tifosiException1) {
                    tifosiException1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        fioranoApplicationController = fioranoServiceProvider.getApplicationController();
        fioranoDeploymentManager = fioranoServiceProvider.getDeploymentManager();
        fioranoServiceRepository = fioranoServiceProvider.getServiceRepository();
        fioranoEventsManager = fioranoServiceProvider.getEventsManager();
        fioranoSBWManager = fioranoServiceProvider.getSBWManager();
        fioranoSecurityManager = fioranoServiceProvider.getSecurityManager();
        fioranoFPSManager = fioranoServiceProvider.getFPSManager();
    }

    private RTLClient(String username, String passwd) {
        String providerURL = "tsp_tcp://" + ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer() + ":1947";
        FioranoServiceProviderFactory fioranoFactory = setupEnvironment(providerURL);
        try {
            fioranoServiceProvider = fioranoFactory.createServiceProvider(username, passwd);
        } catch (TifosiException tifosiException) {
            if (tifosiException.getErrorCode().equals("UNABLE_TO_CONNECT")) {
                try {
                    providerURL = "tsp_tcp://" + ServerStatusController.getInstance().getCurrentSuiteElement().getEnterpriseServer() + ":1948";
                    fioranoFactory = setupEnvironment(providerURL);
                    fioranoServiceProvider = fioranoFactory.createServiceProvider(userName, password);
                } catch (TifosiException tifosiException1) {
                    tifosiException1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        fioranoApplicationController = fioranoServiceProvider.getApplicationController();
        fioranoDeploymentManager = fioranoServiceProvider.getDeploymentManager();
        fioranoServiceRepository = fioranoServiceProvider.getServiceRepository();
        fioranoEventsManager = fioranoServiceProvider.getEventsManager();
        fioranoSBWManager = fioranoServiceProvider.getSBWManager();
        fioranoSecurityManager = fioranoServiceProvider.getSecurityManager();
        fioranoFPSManager = fioranoServiceProvider.getFPSManager();
    }

    public static void setClientToNull() {
        rtlClient = null;
    }

    public void shutdownEnterpriseServer() {
        try {
            this.fioranoServiceProvider.shutdownTES();
        } catch (TifosiException tifosiException) {
            tifosiException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        setClientToNull();
        RMIClient.setClientToNull();
        JMXClient.setClientToNull();

    }

    public void restartEnterpriseServer() {
        try {
            this.fioranoServiceProvider.restart();
        } catch (TifosiException tifosiException) {
            tifosiException.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        setClientToNull();
        RMIClient.setClientToNull();
        JMXClient.setClientToNull();
    }

    public void cleanUp() {
        try {
            if (fioranoApplicationController != null)
                fioranoApplicationController.close();

            if (fioranoServiceRepository != null)
                fioranoServiceRepository.close();

            if (fioranoDeploymentManager != null)
                fioranoDeploymentManager.close();

            if (fioranoEventsManager != null)
                fioranoEventsManager.close();

            if (fioranoFPSManager != null)
                fioranoFPSManager.close();

            if (fioranoSBWManager != null)
                fioranoSBWManager.close();

            if (fioranoSecurityManager != null)
                fioranoSecurityManager.close();
        } catch (Exception e) {
            System.out.println("COULDN'T CLEANUP RTLCLIENT DUE TO :" + e.getMessage());
        }

        setClientToNull();
    }


    private FioranoServiceProviderFactory setupEnvironment(String providerURL) {
        Hashtable env = new Hashtable();
        env.put(Context.SECURITY_PRINCIPAL, userName);
        env.put(Context.SECURITY_CREDENTIALS, password);
        env.put(Context.PROVIDER_URL, providerURL);
        env.put(Context.INITIAL_CONTEXT_FACTORY, contextFactory);

        InitialContext ic = null;
        try {
            ic = new InitialContext(env);
        } catch (NamingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        // Lookup for Provider Factory
        FioranoServiceProviderFactory fioranoFactory = null;
        try {
            fioranoFactory = (FioranoServiceProviderFactory) ic.lookup(serviceProvider);
        } catch (NamingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return fioranoFactory;
    }

    public FioranoApplicationController getFioranoApplicationController() {
        return fioranoApplicationController;
    }

    public FioranoFPSManager getFioranoFPSManager() {
        return fioranoFPSManager;
    }

    public FioranoServiceRepository getFioranoServiceRepository() {
        return fioranoServiceRepository;
    }

    public FioranoDeploymentManager getFioranoDeploymentManager() {
        return fioranoDeploymentManager;
    }

    public FioranoEventsManager getFioranoEventsManager() {
        return fioranoEventsManager;
    }

    public FioranoSBWManager getFioranoSBWManager() {
        return fioranoSBWManager;
    }

    public FioranoSecurityManager getFioranoSecurityManager() {
        return fioranoSecurityManager;
    }

    public String getHandleID() {
        return fioranoServiceProvider.getHandleID();
    }

    public FioranoServiceProvider getFioranoServiceProvider() {
        return fioranoServiceProvider;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void printConfiguration() {
        System.out.println("Parameters used by the sample");
        System.out.println("***************************************** ");
//        System.out.println("Provider Url ::" + providerURL);
        System.out.println("User Name ::" + userName);
        System.out.println("Password ::" + password);
        System.out.println("*****************************************\n ");
    }

}
