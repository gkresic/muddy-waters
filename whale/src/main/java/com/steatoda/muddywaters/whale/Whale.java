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

import java.io.FileNotFoundException;
import java.util.EnumSet;
import java.util.concurrent.Executors;

public class Whale {

	@Inject
	public Whale(GuiceResteasyBootstrapServletContextListener guiceResteasyListener) {

		try (ResourceFactory.Closeable resourceFactory = ResourceFactory.closeable()) {

			// load config and initialize server
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

			Log.info("Jetty initialized");

		} catch (Exception e) {
			throw new RuntimeException("Error configuring Jetty", e);
		}

		Log.debug("Whale initialized");

	}

	public void start() {
		
		Log.debug("Whale starting...");
		
		try {
			server.start();
			Log.info("Jetty started");
		} catch (Exception e) {
			throw new RuntimeException("Error starting Jetty", e);
		}

		Log.info("Whale is up and runnin'");

	}
	
	public void close() {
		
		Log.debug("Whale stopping...");

		try {
			server.stop();
			Log.info("Jetty stopped");
		} catch (Exception e) {
			Log.error("Error stopping jetty", e);
		}

		try {
			server.destroy();
			Log.info("Jetty shut down");
		} catch (Exception e) {
			Log.error("Unable to shutdown Jetty server", e);
		}

		Log.info("Whale successfully shut down - bye!");

	}
	
	private static final Logger Log = LoggerFactory.getLogger(Whale.class);

	private final Server server;
	
}
