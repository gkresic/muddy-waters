package com.steatoda.muddywaters.dolphin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DolphinCmd {

	public static void main(String[] args) {

		Log.info("**************************************************");
		Log.info("Welcome to Dolphin {}...", DolphinProperties.get().getVersion());
		Log.info("**************************************************");

		Log.debug("Dolphin {} initializing...", DolphinProperties.get().getVersion());

		DolphinComponent dolphinComponent = DaggerDolphinComponent.create();

		final Dolphin dolphin = dolphinComponent.dolphin();

		Log.info("Dolphin {} initialized", DolphinProperties.get().getVersion());

		dolphin.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			dolphin.stop();
			dolphin.destroy();
		}));

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(DolphinCmd.class);

}
