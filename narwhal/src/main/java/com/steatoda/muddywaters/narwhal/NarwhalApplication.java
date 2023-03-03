package com.steatoda.muddywaters.narwhal;

import io.dropwizard.Application;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NarwhalApplication extends Application<NarwhalConfiguration> {

	public static void main(String[] args) throws Exception {
		new NarwhalApplication().run(args);
	}

	@Override
	public String getName() { return "narwhal"; }

	@Override
	public void initialize(Bootstrap<NarwhalConfiguration> bootstrap) {

		bootstrap.setConfigurationSourceProvider(new ResourceConfigurationSourceProvider());

	}

	@Override
	public void run(NarwhalConfiguration configuration, Environment environment) throws Exception {

		Log.info("Foo: {}", configuration.getFoo());

		environment.jersey().register(new PayloadResource(environment.getObjectMapper()));

	}

	private static final Logger Log = LoggerFactory.getLogger(NarwhalApplication.class);

}
