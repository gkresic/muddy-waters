package com.steatoda.muddywaters.piranha.servlet;

import dagger.Module;
import dagger.Provides;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerWrapper;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.FileNotFoundException;

@Module
public interface JettyModule {

	@Provides
	@Singleton
	static XmlConfiguration provideConfiguration() {
		try {
			Resource resource = Resource.newResource(Thread.currentThread().getContextClassLoader().getResource("jetty.xml"));
			if (resource == null)
				throw new FileNotFoundException("Jetty configuration (jetty.xml) not found on classpath");
			return new XmlConfiguration(resource);
		} catch (Exception e) {
			throw new RuntimeException("Error loading Jetty configuration", e);
		}
	}

	@Provides
	@Singleton
	static Server provideServer(XmlConfiguration configuration) {
		try {
			return (Server) configuration.configure();
		} catch (Exception e) {
			throw new RuntimeException("Error constructing Jetty server", e);
		}
	}

	@Provides
	@Singleton
	static WebAppContext provideWebAppContext(Server server) {

		Handler handler = server.getHandler();
		while (handler instanceof HandlerWrapper && !(handler instanceof WebAppContext))
			handler = ((HandlerWrapper) handler).getHandler();

		if (handler == null)
			throw new RuntimeException("No WebAppContext handler configured in Jetty?!");

		assert handler instanceof WebAppContext;	// just to silence IntelliJ

		return (WebAppContext) handler;

	}

	Logger Log = LoggerFactory.getLogger(JettyModule.class);

}
