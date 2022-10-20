package com.steatoda.muddywaters.orca;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrcaCmd {

	public static void main(String[] args) {

		Log.info("**************************************************");
		Log.info("Welcome to Orca {}...", OrcaProperties.get().getVersion());
		Log.info("**************************************************");

		Log.debug("Orca {} initializing...", OrcaProperties.get().getVersion());

		OrcaComponent orcaComponent = DaggerOrcaComponent.create();

		final Orca orca = orcaComponent.orca();

		Log.info("Orca {} initialized", OrcaProperties.get().getVersion());

		orca.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			orca.stop();
			orca.destroy();
		}));

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(OrcaCmd.class);

}
