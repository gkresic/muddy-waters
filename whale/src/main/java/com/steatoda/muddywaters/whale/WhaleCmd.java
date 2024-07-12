package com.steatoda.muddywaters.whale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhaleCmd {

	public static void main(String[] args) {

		Log.info("**************************************************");
		Log.info("Welcome to Whale {}...", WhaleProperties.get().getVersion());
		Log.info("**************************************************");

		final Whale whale = WhaleInjector.get().getInstance(Whale.class);

		whale.init();
		whale.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			whale.stop();
			whale.destroy();
		}));

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(WhaleCmd.class);

}
