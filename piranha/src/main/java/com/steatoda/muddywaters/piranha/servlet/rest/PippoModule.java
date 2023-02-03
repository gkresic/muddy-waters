package com.steatoda.muddywaters.piranha.servlet.rest;

import dagger.Module;
import dagger.Provides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.pippo.core.Application;
import ro.pippo.core.PippoSettings;
import ro.pippo.core.RuntimeMode;

import javax.inject.Singleton;

@Module
public interface PippoModule {

	@Provides
	@Singleton
	static PippoSettings providePippoSettings() {
		return new PippoSettings(RuntimeMode.getCurrent());
	}

	@Provides
	@Singleton
	static Application provideApplication(PippoSettings pippoSettings, RootEndpointGroup.Factory rootEndpointGroupFactory, PiranhaJacksonJsonEngine jsonEngine) {

		Application application = new Application(pippoSettings);

		application.addRouteGroup(rootEndpointGroupFactory.create("/"));

		jsonEngine.init(application);
		application.getContentTypeEngines().setContentTypeEngine(jsonEngine);

		return application;

	}

	Logger Log = LoggerFactory.getLogger(PippoModule.class);

}
