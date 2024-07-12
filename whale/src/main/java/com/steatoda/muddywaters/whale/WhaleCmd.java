package com.steatoda.muddywaters.whale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhaleCmd {

	public static void main(String[] args) throws Exception {

		Log.info("**************************************************");
		Log.info("Welcome to Whale...");
		Log.info("**************************************************");

		final Whale whale = WhaleInjector.get().getInstance(Whale.class);

		whale.start();

		Runtime.getRuntime().addShutdownHook(new Thread(whale::close));

		Thread.currentThread().join();

	}

	private static final Logger Log = LoggerFactory.getLogger(WhaleCmd.class);

}
