package com.steatoda.muddywaters.beluga.servlet.rest;

import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Module;
import dagger.Provides;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Module
public interface JavalinModule {

	@Provides
	@Singleton
	static Javalin provideJavalin(RootEndpointGroup rootEndpointGroup, JsonMapper jsonMapper) {

		return Javalin.create(config -> {
			config.jetty.defaultPort = 16008;
			config.jetty.defaultHost = "0.0.0.0";
			config.jsonMapper(new JavalinJackson(jsonMapper, true));
			config.router.apiBuilder(rootEndpointGroup);
		});

	}

	Logger Log = LoggerFactory.getLogger(JavalinModule.class);

}
