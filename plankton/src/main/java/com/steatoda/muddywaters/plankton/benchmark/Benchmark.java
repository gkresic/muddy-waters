package com.steatoda.muddywaters.plankton.benchmark;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public abstract class Benchmark implements AutoCloseable {

	public Benchmark(
		String host,
		int port,
		int numThreads,
		Duration duration
	) {

		this.numThreads =numThreads;
		this.duration = duration;

		channel = ManagedChannelBuilder
			.forAddress(host, port)
			.usePlaintext()
			.build();

		Log.info("[{}] Opened a channel to {}:{}", name(), host, port);

		executor = Executors.newFixedThreadPool(numThreads);

	}

	public void bench() throws InterruptedException {

		if (executor.isShutdown())
			throw new IllegalStateException("Benchmark is closed");

		Log.info(
			"""

			*************************
			*** {}
			*************************
			""",
			name()
		);

		Log.info("[{}] Running for {} using {} threads...", name(), duration, numThreads);

		start = System.currentTimeMillis();

		// initially submit numThreads jobs and let them submit new ones
		for (int i = 0; i < numThreads; ++i)
			submitRun();

		// start thread that terminates benchmark after timeout
		Timer timer = new Timer(true);
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				print();
				executor.shutdown();
				timer.cancel();
			}
		}, duration.toMillis());

		if (!executor.awaitTermination(2 * duration.toMillis(), TimeUnit.MILLISECONDS))
			Log.error("Timeout waiting for benchmark to finish!");

	}

	@Override
	public void close() throws Exception {
		executor.shutdownNow();
		channel.shutdownNow().awaitTermination(10, TimeUnit.SECONDS);
	}

	public Channel channel() { return channel; }

	protected abstract void send();

	protected String name() { return getClass().getSimpleName(); }

	protected Future<?> submitRun() {
		return executor.submit(this::send);
	}

	protected long incSuccessCount() {
		return successCount.incrementAndGet();
	}

	protected long incErrorCount() {
		return errorCount.incrementAndGet();
	}

	protected long incTotalCount() {
		return totalCount.incrementAndGet();
	}

	protected void print() {

		long totalCountFinal = totalCount.get();
		long successCountFinal = successCount.get();
		long errorCountFinal = errorCount.get();
		double durationSec = (System.currentTimeMillis() - start) / 1000.0;

		Log.info("Total: {} (success: {}, error: {})", totalCountFinal, successCountFinal, errorCountFinal);
		Log.info("Reqs/sec: {}", totalCountFinal / durationSec);

	}

	private static final Logger Log = LoggerFactory.getLogger(Benchmark.class);

	private final int numThreads;
	private final Duration duration;

	private final ManagedChannel channel;
	private final ExecutorService executor;

	private final AtomicLong totalCount = new AtomicLong(0L);
	private final AtomicLong successCount = new AtomicLong(0L);
	private final AtomicLong errorCount = new AtomicLong(0L);

	private long start = 0;

}
