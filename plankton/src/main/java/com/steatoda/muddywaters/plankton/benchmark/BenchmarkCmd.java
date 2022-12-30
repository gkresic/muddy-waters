package com.steatoda.muddywaters.plankton.benchmark;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class BenchmarkCmd {

	public static void main(String[] args) throws Exception {

		Log.debug("Running benchmarks...");

		for (int i = 0; i < Runs; ++i) {

			Log.info("Run #{}", i);

			try (Benchmark benchmark = new BenchmarkPing(Host, Port, ThreadCount, RunDuration)) {
				benchmark.bench();
			}

			try (Benchmark benchmark = new BenchmarkEatOne(Host, Port, ThreadCount, RunDuration)) {
				benchmark.bench();
			}

			try (Benchmark benchmark = new BenchmarkEatStream(Host, Port, ThreadCount, RunDuration, "payload-10")) {
				benchmark.bench();
			}

		}

	}

	private static final String Host = "localhost";
	private static final int Port = 17001;
	private static final int ThreadCount = 100;
	private static final Duration RunDuration = Duration.ofSeconds(10);
	private static final int Runs = 3;

	private static final Logger Log = LoggerFactory.getLogger(BenchmarkCmd.class);

}
