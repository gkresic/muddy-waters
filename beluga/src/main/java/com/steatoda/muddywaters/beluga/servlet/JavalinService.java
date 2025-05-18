package com.steatoda.muddywaters.beluga.servlet;

import com.steatoda.muddywaters.beluga.BelugaPreStartEvent;
import com.steatoda.muddywaters.beluga.BelugaPreStopEvent;
import io.javalin.Javalin;
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
		Javalin javalin,
		EventBus eventBus
	) {

		this.javalin = javalin;

		eventBus.register(this);

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onBelugaPreStart(BelugaPreStartEvent event) {

		try {
			javalin.start();
		} catch (Exception e) {
			throw new RuntimeException("Error starting Javalin", e);
		}

		Log.info("Javalin started");

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onBelugaPreStop(BelugaPreStopEvent event) {

		try {
			javalin.stop();
			Log.info("Javalin stopped");
		} catch (Exception e) {
			Log.error("Error stopping Javalin", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(JavalinService.class);

	private final Javalin javalin;

}
