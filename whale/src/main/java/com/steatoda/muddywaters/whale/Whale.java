package com.steatoda.muddywaters.whale;

import dev.resteasy.guice.GuiceResteasyBootstrapServletContextListener;
import org.eclipse.jetty.ee10.webapp.WebAppContext;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.net.URL;
import java.util.concurrent.Executors;

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

		try (ResourceFactory.Closeable resourceFactory = ResourceFactory.closeable()) {
			Resource resource = resourceFactory.newResource(Thread.currentThread().getContextClassLoader().getResource("jetty.xml"));
			if (resource == null)
				throw new FileNotFoundException("Jetty configuration (jetty.xml) not found on classpath");
			XmlConfiguration jettyConf = new XmlConfiguration(resource);
			server = (Server) jettyConf.configure();
			// have to add ServletContextListener here (instead in web.xml) because otherwise it won't see our WhaleInjector as parent
			Handler handler = server.getHandler();
			while (handler instanceof Handler.Wrapper handlerWrapper && !(handler instanceof WebAppContext))
				handler = handlerWrapper.getHandler();
			if (handler == null)
				throw new RuntimeException("No WebAppContext handler configured in Jetty?!");
			assert handler instanceof WebAppContext;	// just to silence IntelliJ
			WebAppContext context = (WebAppContext) handler;
			URL webAppDir = Thread.currentThread().getContextClassLoader().getResource("war");
			if (webAppDir == null)
				throw new FileNotFoundException("Jetty root (war) not found on classpath");
			context.setWar(webAppDir.toURI().toString());
			context.addEventListener(WhaleInjector.get().getInstance(GuiceResteasyBootstrapServletContextListener.class));
			// enable virtual threads
			if (server.getThreadPool() instanceof QueuedThreadPool queuedThreadPool)
				queuedThreadPool.setVirtualThreadsExecutor(Executors.newVirtualThreadPerTaskExecutor());
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
