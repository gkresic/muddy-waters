package com.steatoda.muddywaters.beluga.servlet.rest;

import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Module;
import dagger.Provides;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.webapp.WebAppContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Module
public interface JavalinModule {

	@Provides
	@Singleton
	static Javalin provideJavalin(WebAppContext webappContext, RootEndpointGroup rootEndpointGroup, JsonMapper jsonMapper) {

		Javalin javalin = Javalin.createStandalone(config -> {
			config.routing.contextPath = "/rest/";
			config.jsonMapper(new JavalinJackson(jsonMapper));
		});

		webappContext.addServlet(new ServletHolder(javalin.javalinServlet()), "/rest/*");

		javalin.routes(rootEndpointGroup);

		return javalin;

	}

	Logger Log = LoggerFactory.getLogger(JavalinModule.class);

}
