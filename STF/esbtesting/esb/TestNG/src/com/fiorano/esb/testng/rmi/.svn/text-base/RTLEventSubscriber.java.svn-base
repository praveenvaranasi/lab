package com.fiorano.esb.testng.rmi;

import com.fiorano.esb.rtl.server.FioranoServiceProvider;
import com.fiorano.esb.rtl.server.FioranoServiceProviderFactory;
import fiorano.tifosi.common.TifosiException;
import fiorano.tifosi.dmi.events.SBWEvent;
import fiorano.tifosi.dmi.events.ServiceEvent;
import fiorano.tifosi.dmi.events.TifosiEvent;
import fiorano.tifosi.events.IEventListener;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: sudharshan
 * Date: Jan 11, 2011
 * Time: 11:19:23 AM
 * To change this template use File | Settings | File Templates.
 */

public class RTLEventSubscriber {
    private String providerURL = "tsp_tcp://localhost:1947";
    private ArrayList<TifosiEvent> events = new ArrayList<TifosiEvent>();

    public RTLEventSubscriber() {
    }

    public RTLEventSubscriber(String providerURL) {
        this.providerURL = providerURL;
    }

    public void startup() throws NamingException, TifosiException {
        Hashtable env = new Hashtable();

        env.put(Context.SECURITY_PRINCIPAL, "admin");
        env.put(Context.SECURITY_CREDENTIALS, "passwd");
        env.put(Context.PROVIDER_URL, providerURL);
        env.put(Context.INITIAL_CONTEXT_FACTORY, "fiorano.tifosi.jndi.InitialContextFactory");

        InitialContext ic = new InitialContext(env);

        // Lookup for Provider Factory
        FioranoServiceProviderFactory fioranoFactory = (FioranoServiceProviderFactory) ic.lookup("TifosiServiceProvider");

        // Create Fiorano Service Provider
        FioranoServiceProvider fsp = fioranoFactory.createServiceProvider("admin", "passwd", true);

        // Register an Event Listener
        EventListener eventListener = new EventListener();
        fsp.getEventsManager().registerAllEventListener(eventListener);
    }

    private class EventListener implements IEventListener {
        public void onEvent(TifosiEvent event) throws TifosiException {
            if (event instanceof ServiceEvent) {
                events.add(event);
            } else if (event instanceof SBWEvent)
                events.add(event);
        }
    }

    public ArrayList<TifosiEvent> getEvents() {
        return events;
    }
}
