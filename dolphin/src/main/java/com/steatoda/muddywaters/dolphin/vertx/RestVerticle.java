package com.steatoda.muddywaters.dolphin.vertx;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.VerticleBase;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.impl.cpu.CpuCoreSensor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

public class RestVerticle extends VerticleBase {

	public static final String Name = "RestVerticle";

	public static Future<String> deploy(Vertx vertx) {
		DeploymentOptions options = new DeploymentOptions();
		options.setInstances(2 * CpuCoreSensor.availableProcessors());
		return vertx.deployVerticle(DaggerVerticleFactory.prefix(Name), options);
	}

	@Inject
	public RestVerticle(HttpServer server) {
		this.server = server;
	}

	@Override
	public Future<?> start() {
		return server.listen();
	}

	@Override
	public Future<?> stop() {
		return server.close();
	}

	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(RestVerticle.class);

	private final HttpServer server;

}
