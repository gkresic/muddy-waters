package com.steatoda.muddywaters.orca.spark;

import com.steatoda.muddywaters.orca.OrcaDestroyEvent;
import com.steatoda.muddywaters.orca.OrcaPreStartEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Spark;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * <p>Wraps Spark, manages its lifecycle and makes sure it is closed on app shutdown.</p>
 */
@Singleton
public class SparkService {

	@Inject
	public SparkService(OrcaRouteGroup rootRouteGroup, EventBus eventBus) {

		this.rootRouteGroup = rootRouteGroup;

		eventBus.register(this);

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onOrcaPreStart(OrcaPreStartEvent event) {

		try {

			// TODO move to config
			Spark.port(16007);
			Spark.threadPool(10);

			Spark.init();

			Spark.path("/", rootRouteGroup);

			Spark.awaitInitialization();

		} catch (Exception e) {
			throw new RuntimeException("Error deploying vertice(s)", e);
		}

		Log.info("Spark started");

	}

	@Subscribe
	@SuppressWarnings("unused")
	public void onOrcaDestroy(OrcaDestroyEvent event) {

		try {

			Spark.stop();

			Spark.awaitStop();

		} catch (Exception e) {
			Log.error("Error stopping Spark", e);
		}

		Log.info("Spark closed");

	}

	private static final Logger Log = LoggerFactory.getLogger(SparkService.class);

	private final OrcaRouteGroup rootRouteGroup;

}
