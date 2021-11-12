package com.steatoda.muddywaters.dolphin.vertx;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.core.spi.VerticleFactory;
import io.vertx.ext.web.Router;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Module
public interface VertxModule {

	@Binds
	Router router(DolphinRouter router);

	@Provides
	@Singleton
	static VertxOptions provideVertxOptions() {
		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("vertx.json")) {
			if (istream == null)
				throw new FileNotFoundException("Vert.x configuration (vertx.json) not found on classpath");
			String content = new String(istream.readAllBytes(), StandardCharsets.UTF_8);
			JsonObject json = new JsonObject(content);
			return new VertxOptions(json);
		} catch (Exception e) {
			throw new RuntimeException("Error loading Vert.x configuration", e);
		}
	}

	@Provides
	@Singleton
	static HttpServerOptions provideHttpServerOptions() {
		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("vertx-http-server.json")) {
			if (istream == null)
				throw new FileNotFoundException("Vert.x HTTP server configuration (vertx-http-server.json) not found on classpath");
			String content = new String(istream.readAllBytes(), StandardCharsets.UTF_8);
			JsonObject json = new JsonObject(content);
			return new HttpServerOptions(json);
		} catch (Exception e) {
			throw new RuntimeException("Error loading Vert.x HTTP server configuration", e);
		}
	}

	@Binds
	@Singleton
	VerticleFactory provideVerticleFactory(DaggerVerticleFactory factory);

	@Provides
	@Singleton
	static Vertx provideVertx(VertxOptions options, VerticleFactory verticleFactory) {
		Vertx vertx = Vertx.vertx(options);
		vertx.registerVerticleFactory(verticleFactory);
		return vertx;
	}

	@Provides
	static HttpServer provideHttpServer(Vertx vertx, HttpServerOptions options, Router router) {
		HttpServer server = vertx.createHttpServer(options);
		server.requestHandler(router);
		return server;
	}

	@Binds
	@IntoMap
	@StringKey(RestVerticle.Name)
	Verticle provideRestVerticle(RestVerticle verticle);

	Logger Log = LoggerFactory.getLogger(VertxModule.class);

}
