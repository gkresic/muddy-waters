package com.steatoda.muddywaters.whale.servlet;

import com.steatoda.muddywaters.whale.servlet.rest.RestApplication;
import jakarta.inject.Singleton;
import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;

import com.google.inject.servlet.ServletModule;

import java.util.Map;

public class WhaleServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		
		super.configureServlets();

		bind(HttpServlet30Dispatcher.class).in(Singleton.class);
		Map<String, String> params = Map.of(
			"resteasy.servlet.mapping.prefix", "/api",
			"resteasy.logger.type", "SLF4J",
			"javax.ws.rs.Application", RestApplication.class.getName()
		);
		serve("/api/*").with(HttpServlet30Dispatcher.class, params);

	}

}
