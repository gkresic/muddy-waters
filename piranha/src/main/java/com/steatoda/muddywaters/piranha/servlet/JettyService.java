package com.steatoda.muddywaters.piranha.servlet;

import com.steatoda.muddywaters.piranha.PiranhaDestroyEvent;
import com.steatoda.muddywaters.piranha.PiranhaPreStartEvent;
import com.steatoda.muddywaters.piranha.PiranhaPreStopEvent;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.FileNotFoundException;
import java.net.URL;

/**
 * <p>Wraps Jetty {@link Server}, manages its lifecycle and makes sure it is closed on app shutdown.</p>
 */
@Singleton
public class JettyService {

	@Inject
	public JettyService(
		Server server,
		WebAppContext webAppContext,
		EventBus eventBus
	) {

		this.server = server;

		try {

			URL webAppDir = Thread.currentThread().getContextClassLoader().getResource("war");

			if (webAppDir == null)
				throw new FileNotFoundException("Jetty root (war) not found on classpath");

			webAppContext.setResourceBase(webAppDir.toURI().toString());

		} catch (Exception e) {
			throw new RuntimeException("Error configuring Jetty webapp context", e);
		}

		eventBus.register(this);

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onPiranhaPreStart(PiranhaPreStartEvent event) {

		try {
			server.start();
		} catch (Exception e) {
			throw new RuntimeException("Error starting Jetty", e);
		}

		Log.info("Jetty started");

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onPiranhaPreStop(PiranhaPreStopEvent event) {

		try {
			server.stop();
			Log.info("Jetty stopped");
		} catch (Exception e) {
			Log.error("Error stopping Jetty", e);
		}

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onPiranhaDestroy(PiranhaDestroyEvent event) {

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
