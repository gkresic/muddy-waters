package com.steatoda.muddywaters.whale.servlet;

import javax.inject.Singleton;

import org.jboss.resteasy.plugins.server.servlet.HttpServlet30Dispatcher;

import com.google.inject.servlet.ServletModule;

public class WhaleServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		
		super.configureServlets();
		
		bind(HttpServlet30Dispatcher.class).in(Singleton.class);
		serve("/api/*").with(HttpServlet30Dispatcher.class);

	}

}
