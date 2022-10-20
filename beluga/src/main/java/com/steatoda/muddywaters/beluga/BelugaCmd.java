package com.steatoda.muddywaters.beluga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BelugaCmd {

	public static void main(String[] args) {

		Log.info("**************************************************");
		Log.info("Welcome to Beluga {}...", BelugaProperties.get().getVersion());
		Log.info("**************************************************");

		Log.debug("Beluga {} initializing...", BelugaProperties.get().getVersion());

		BelugaComponent belugaComponent = DaggerBelugaComponent.create();

		final Beluga beluga = belugaComponent.beluga();

		Log.info("Beluga {} initialized", BelugaProperties.get().getVersion());

		beluga.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			beluga.stop();
			beluga.destroy();
		}));

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(BelugaCmd.class);

}
