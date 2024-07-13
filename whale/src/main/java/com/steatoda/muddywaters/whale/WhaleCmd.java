package com.steatoda.muddywaters.whale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WhaleCmd {

	public static void main(String[] args) throws Exception {

		Log.info(
			"""

			__        ___           _
			\\ \\      / / |__   __ _| | ___
			 \\ \\ /\\ / /| '_ \\ / _` | |/ _ \\
			  \\ V  V / | | | | (_| | |  __/
			   \\_/\\_/  |_| |_|\\__,_|_|\\___|
			"""
		);

		final Whale whale = WhaleInjector.get().getInstance(Whale.class);

		whale.start();

		Runtime.getRuntime().addShutdownHook(new Thread(whale::close));

		Thread.currentThread().join();

	}

	private static final Logger Log = LoggerFactory.getLogger(WhaleCmd.class);

}
