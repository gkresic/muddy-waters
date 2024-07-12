package com.steatoda.muddywaters.whale;

import com.google.inject.servlet.GuiceFilter;
import dev.resteasy.guice.GuiceResteasyBootstrapServletContextListener;
import jakarta.inject.Inject;
import jakarta.servlet.DispatcherType;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.EventBus;

import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.concurrent.Executors;

public class Whale {

	@Inject
	public Whale(EventBus eventBus, GuiceResteasyBootstrapServletContextListener guiceResteasyListener) {
		this.eventBus = eventBus;
		this.guiceResteasyListener = guiceResteasyListener;
	}

	public void	init() {

		eventBus.post(new WhalePreInitEvent(this));

		try (ResourceFactory.Closeable resourceFactory = ResourceFactory.closeable()) {
			Resource resource = resourceFactory.newResource(Thread.currentThread().getContextClassLoader().getResource("jetty.xml"));
			if (resource == null)
				throw new FileNotFoundException("Jetty configuration (jetty.xml) not found on classpath");
			XmlConfiguration jettyConf = new XmlConfiguration(resource);
			server = (Server) jettyConf.configure();
			ServletContextHandler handler = new ServletContextHandler();
			// NOTE: register only GuiceFilter here and all the other filters/servlets in com.steatoda.muddywaters.whale.servlet.WhaleServletModule
			handler.addFilter(GuiceFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
			// have to add ServletContextListener here (instead in WhaleServletModule) because otherwise it won't see our WhaleInjector as parent
			handler.addEventListener(guiceResteasyListener);
			server.setHandler(handler);
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

	private final EventBus eventBus;
	private final GuiceResteasyBootstrapServletContextListener guiceResteasyListener;

	private Server server = null;
	
}
