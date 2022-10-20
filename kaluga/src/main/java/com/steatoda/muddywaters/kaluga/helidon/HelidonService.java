package com.steatoda.muddywaters.kaluga.helidon;

import com.steatoda.muddywaters.kaluga.KalugaPreStartEvent;
import com.steatoda.muddywaters.kaluga.KalugaPreStopEvent;
import io.helidon.config.Config;
import io.helidon.media.common.MediaContext;
import io.helidon.webserver.Routing;
import io.helidon.webserver.WebServer;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <p>Wraps Helidon's {@link WebServer}, manages its lifecycle and makes sure it is closed on app shutdown.</p>
 */
@Singleton
public class HelidonService {

	@Inject
	public HelidonService(
		RootService rootService,
		Config config,
		MediaContext mediaContext,
		EventBus eventBus
	) {

		webServer = WebServer.builder(Routing.builder().register(rootService))
			.config(config.get("server"))
			.mediaContext(mediaContext)
			.build();

		Log.info("Configured port is {}", config.get("server.port").asInt());
		Log.info("WebServer listening on {}", webServer.port());

		eventBus.register(this);

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onKalugaPreStart(KalugaPreStartEvent event) {

		webServer.start().await();

		Log.info("Javalin started");

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onKalugaPreStop(KalugaPreStopEvent event) {

		webServer.shutdown().await();

		Log.info("Javalin stopped");

	}

	private static final Logger Log = LoggerFactory.getLogger(HelidonService.class);

	private final WebServer webServer;

}
