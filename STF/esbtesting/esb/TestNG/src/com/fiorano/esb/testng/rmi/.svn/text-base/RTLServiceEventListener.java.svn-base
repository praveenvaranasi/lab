package com.fiorano.esb.testng.rmi;

import com.fiorano.esb.rtl.component.FioranoServiceRepository;
import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.rtl.server.FioranoServiceProviderFactory;
import fiorano.tifosi.common.TifosiException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: lokesh2
 * Date: Feb 4, 2011
 * Time: 4:00:05 PM
 * To change this template use File | Settings | File Templates.
 */
public class RTLServiceEventListener {

    private String providerURL = "tsp_tcp://localhost:1947";

    public RTLServiceEventListener() {
    }

    public RTLServiceEventListener(String providerURL) {
        this.providerURL = providerURL;
    }

    public FioranoServiceRepository startup() throws NamingException, TifosiException {
        Hashtable env = new Hashtable();

        env.put(Context.SECURITY_PRINCIPAL, "admin");
        env.put(Context.SECURITY_CREDENTIALS, "passwd");
        env.put(Context.PROVIDER_URL, providerURL);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.tifosi.jndi.InitialContextFactory");

        InitialContext ic = new InitialContext(env);

        // Lookup for Provider Factory
        FioranoServiceProviderFactory fioranoFactory = (FioranoServiceProviderFactory) ic.lookup("TifosiServiceProvider");

        // Create Fiorano Service Provider
        FioranoServiceProvider fsp = fioranoFactory.createServiceProvider("admin", "passwd");

        // Register an Event Listener
        FioranoServiceRepository fsr = fsp.getServiceRepository();

        return fsr;
    }

}
