package com.steatoda.muddywaters.whale;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import com.steatoda.muddywaters.whale.servlet.rest.WhaleGuiceResteasyBootstrapServletContextListener;

public class Whale {

	public static Whale get() {
		if (instance == null)
			instance = new Whale();
		return instance;
	}

	private Whale() {}

	public void	init() {
        
		Log.info("**************************************************");
		Log.info("Welcome to Whale {}...", WhaleProperties.get().getVersion());
		Log.info("**************************************************");

		eventBus = WhaleInjector.get().getInstance(EventBus.class);

		eventBus.post(new WhalePreInitEvent(this));

		try {
			XmlConfiguration jettyConf = new XmlConfiguration(Resource.newResource(Thread.currentThread().getContextClassLoader().getResource("jetty.xml")));
			server = Server.class.cast(jettyConf.configure());
			// have to add ServletContextListener here (instead in web.xml) because otherwise it won't see our WhaleInjector as parent
			Handler handler = server.getHandler();
			while (handler != null && !WebAppContext.class.isInstance(handler) && HandlerWrapper.class.isInstance(handler))
				handler = HandlerWrapper.class.cast(handler).getHandler();
			if (handler == null)
				throw new RuntimeException("No WebAppContext handler configured in Jetty?!");
			WebAppContext.class.cast(handler).addEventListener(WhaleInjector.get().getInstance(WhaleGuiceResteasyBootstrapServletContextListener.class));
		} catch (Exception e) {
			throw new RuntimeException("Error configuring Jetty", e);
		}
		Log.info("Jetty initialized");

		eventBus.post(new WhaleInitEvent(this));

	}
	
	public void start() {
		
		Log.debug("Whale {} starting...", WhaleProperties.get().getVersion());
		
		eventBus.post(new WhalePreStartEvent(this));
		
		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException("Error starting Jetty", e);
		}
		Log.info("Jetty started");

		eventBus.post(new WhaleStartEvent(this));
		
		Log.info("Whale {} is up and runnin'", WhaleProperties.get().getVersion());

	}
	
	public void stop() {
		
		Log.debug("Whale {} stopping...", WhaleProperties.get().getVersion());

		eventBus.post(new WhalePreStopEvent(this));
		
		try {
			server.stop();
			Log.info("Jetty stopped");
		} catch (Exception e) {
			Log.error("Error stopping jetty", e);
		}
		
		eventBus.post(new WhaleStopEvent(this));
		
		Log.info("Whale {} stopped", WhaleProperties.get().getVersion());

	}
	
	public void destroy() {

		Log.debug("Whale {} is initializing shutdown...", WhaleProperties.get().getVersion());

		try {
			server.destroy();
			Log.info("Jetty shut down");
		} catch (Exception e) {
			Log.error("Unable to shutdown Jetty server", e);
		}

		eventBus.post(new WhaleDestroyEvent(this));

		Log.info("Whale {} successfully shut down - bye!", WhaleProperties.get().getVersion());
		
	}

	private static final Logger Log = LoggerFactory.getLogger(Whale.class);
	private static Whale instance = null;

	private EventBus eventBus = null;
	private Server server = null;
	
}
