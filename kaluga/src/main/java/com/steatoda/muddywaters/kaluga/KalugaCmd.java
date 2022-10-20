package com.steatoda.muddywaters.kaluga;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class KalugaCmd {

	public static void main(String[] args) {

		Log.info("**************************************************");
		Log.info("Welcome to Kaluga {}...", KalugaProperties.get().getVersion());
		Log.info("**************************************************");

		Log.debug("Kaluga {} initializing...", KalugaProperties.get().getVersion());

		KalugaComponent kalugaComponent = DaggerKalugaComponent.create();

		final Kaluga kaluga = kalugaComponent.kaluga();

		Log.info("Kaluga {} initialized", KalugaProperties.get().getVersion());

		kaluga.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			kaluga.stop();
			kaluga.destroy();
		}));

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(KalugaCmd.class);

}
