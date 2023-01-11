package com.steatoda.muddywaters.beluga.servlet;

import com.steatoda.muddywaters.beluga.BelugaDestroyEvent;
import com.steatoda.muddywaters.beluga.BelugaPreStartEvent;
import com.steatoda.muddywaters.beluga.BelugaPreStopEvent;
import org.eclipse.jetty.server.Server;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <p>Wraps Jetty {@link Server}, manages its lifecycle and makes sure it is closed on app shutdown.</p>
 */
@Singleton
public class JettyService {

	@Inject
	public JettyService(
		Server server,
		EventBus eventBus
	) {

		this.server = server;

		eventBus.register(this);

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onBelugaPreStart(BelugaPreStartEvent event) {

		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException("Error starting Jetty", e);
		}

		Log.info("Jetty started");

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onBelugaPreStop(BelugaPreStopEvent event) {

		try {
			server.stop();
			Log.info("Jetty stopped");
		} catch (Exception e) {
			Log.error("Error stopping jetty", e);
		}

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onBelugaDestroy(BelugaDestroyEvent event) {

		try {
			server.destroy();
			Log.info("Jetty shut down");
		} catch (Exception e) {
			Log.error("Unable to shutdown Jetty server", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(JettyService.class);

	private final Server server;

}
