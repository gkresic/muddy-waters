package com.steatoda.muddywaters.octopus;

import com.steatoda.muddywaters.octopus.servlet.OctopusGraphQLHttpServlet;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.resource.ResourceFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.util.concurrent.Executors;

public class OctopusCmd {

	public static void main(String[] args) throws Exception {

		Log.info(
			"""

			  ___       _
			 / _ \\  ___| |_ ___  _ __  _   _ ___
			| | | |/ __| __/ _ \\| '_ \\| | | / __|
			| |_| | (__| || (_) | |_) | |_| \\__ \\
			 \\___/ \\___|\\__\\___/| .__/ \\__,_|___/
			                    |_|
			"""
		);

        Server server;

        try (ResourceFactory.Closeable resourceFactory = ResourceFactory.closeable()) {
            Resource resource = resourceFactory.newResource(Thread.currentThread().getContextClassLoader().getResource("jetty.xml"));
            if (resource == null)
                throw new FileNotFoundException("Jetty configuration (jetty.xml) not found on classpath");
            XmlConfiguration jettyConf = new XmlConfiguration(resource);
            server = (Server) jettyConf.configure();
			// add GraphQL servlet
            ServletContextHandler handler = new ServletContextHandler();
			OctopusGraphQLHttpServlet graphQLServlet = new OctopusGraphQLHttpServlet();
			handler.addServlet(graphQLServlet, "/graphql/*");
            server.setHandler(handler);
			// enable virtual threads
			if (server.getThreadPool() instanceof QueuedThreadPool queuedThreadPool)
				queuedThreadPool.setVirtualThreadsExecutor(Executors.newVirtualThreadPerTaskExecutor());
        } catch (Exception e) {
            throw new RuntimeException("Error configuring Jetty", e);
        }
        Log.info("Jetty initialized");

        server.start();

        Log.info("Jetty started");

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
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
        }));

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException("Main thread interrupted?!", e);
        }

	}

	private static final Logger Log = LoggerFactory.getLogger(OctopusCmd.class);

}
