package com.steatoda.muddywaters.plankton;

import io.grpc.InternalChannelz;
import io.grpc.InternalInstrumented;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class PlanktonCmd {

	public static void main(String[] args) throws IOException {

		Log.debug("Plankton starting...");

		Server server = ServerBuilder.forPort(17001)
			.addService(new PingService())
			.addService(new EatService())
			.build();

		Log.info("Plankton initialized");

		server.start();

		Log.info("Plankton started");

		Timer watchdog = new Timer(true);
		watchdog.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					InternalChannelz.ServerStats stats = ((InternalInstrumented<InternalChannelz.ServerStats>) server).getStats().get();
					Log.info("Succeeded={}, sockets={}", stats.callsSucceeded, stats.listenSockets.size());
				} catch (Exception e) {
					Log.error("Unable to fetch server stats", e);
				}
			}
		}, 0, Duration.ofSeconds(1).toMillis());

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			try {
				server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				Log.error("Error shutting down server", e);
			}
		}));

		try {
			server.awaitTermination();
		} catch (InterruptedException e) {
			throw new RuntimeException("Main thread interrupted?!", e);
		}

	}

	private static final Logger Log = LoggerFactory.getLogger(PlanktonCmd.class);

}
