package com.steatoda.muddywaters.piranha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PiranhaCmd {

	public static void main(String[] args) {

		Log.info("**************************************************");
		Log.info("Welcome to Piranha {}...", PiranhaProperties.get().getVersion());
		Log.info("**************************************************");

		Log.debug("Piranha {} initializing...", PiranhaProperties.get().getVersion());

		PiranhaComponent piranhaComponent = DaggerPiranhaComponent.create();

		final Piranha piranha = piranhaComponent.piranha();

		Log.info("Piranha {} initialized", PiranhaProperties.get().getVersion());

		piranha.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			piranha.stop();
			piranha.destroy();
		}));

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(PiranhaCmd.class);

}
