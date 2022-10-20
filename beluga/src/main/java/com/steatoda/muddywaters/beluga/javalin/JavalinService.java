package com.steatoda.muddywaters.beluga.javalin;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.steatoda.muddywaters.beluga.BelugaDestroyEvent;
import com.steatoda.muddywaters.beluga.BelugaPreStartEvent;
import com.steatoda.muddywaters.beluga.BelugaPreStopEvent;
import io.javalin.Javalin;
import io.javalin.json.JavalinJackson;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <p>Wraps {@link Javalin}, manages its lifecycle and makes sure it is closed on app shutdown.</p>
 */
@Singleton
public class JavalinService {

	@Inject
	public JavalinService(
		RootEndpointGroup rootEndpointGroup,
		JsonMapper jsonMapper,
		EventBus eventBus
	) {

		// TODO config file
		javalin = Javalin.create(config -> {
			config.jsonMapper(new JavalinJackson(jsonMapper));
		});
		javalin.jettyServer().setServerPort(16008);
		javalin.routes(rootEndpointGroup);

		eventBus.register(this);

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onBelugaPreStart(BelugaPreStartEvent event) {

		javalin.start();

		Log.info("Javalin started");

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onBelugaPreStop(BelugaPreStopEvent event) {

		javalin.stop();

		Log.info("Javalin stopped");

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onBelugaDestroy(BelugaDestroyEvent event) {

		javalin.close();

		Log.info("Javalin closed");

	}

	private static final Logger Log = LoggerFactory.getLogger(JavalinService.class);

	private final Javalin javalin;

}
