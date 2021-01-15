package com.steatoda.muddywaters.whale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhaleCmd {

	public static void main(String[] args) {

		final Whale whale = Whale.get();

		whale.init();
		whale.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				whale.stop();
				whale.destroy();
			}
		});

		try {
			Thread.currentThread().join();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(WhaleCmd.class);

}
