package com.steatoda.muddywaters.dolphin.vertx;

import io.vertx.core.Deployable;
import io.vertx.core.Promise;
import io.vertx.core.VerticleBase;
import io.vertx.core.spi.VerticleFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import java.util.Map;
import java.util.concurrent.Callable;

public class DaggerVerticleFactory implements VerticleFactory {

	public static final String Prefix = "dagger";
	public static final String Separator = ":";

	public static String prefix(String name) {
		return Prefix + Separator + name;
	}

	@Inject
	public DaggerVerticleFactory(Map<String, Provider<VerticleBase>> verticleMap) {
		this.verticleMap = verticleMap;
	}

	@Override
	public void createVerticle2(String verticleName, ClassLoader classLoader, Promise<Callable<? extends Deployable>> promise) {

		Provider<VerticleBase> provider = verticleMap.get(sanitizeVerticleClassName(verticleName));

		if (provider == null)
			promise.fail("No provider for verticle '" + verticleName + "'");
		else
			promise.complete(provider::get);

	}

	@Override
	public String prefix() {
		return Prefix;
	}

	private String sanitizeVerticleClassName(String verticleName) {
		return verticleName.substring(verticleName.lastIndexOf(Separator) + 1);
	}

	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(DaggerVerticleFactory.class);

	private final Map<String, Provider<VerticleBase>> verticleMap;

}
