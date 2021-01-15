package com.steatoda.muddywaters.whale.servlet.rest;

import java.util.List;

import javax.servlet.ServletContext;

import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import com.google.inject.Module;

public class WhaleGuiceResteasyBootstrapServletContextListener extends GuiceResteasyBootstrapServletContextListener {

	@Override
	protected List<? extends Module> getModules(ServletContext context) {

		@SuppressWarnings("unchecked")
		List<Module> modules = (List<Module>) super.getModules(context);
		
		modules.add(new ResourceModule());

		return modules;
		
	}

}
