package com.steatoda.muddywaters.plankton.benchmark;

import com.steatoda.muddywaters.plankton.proto.PingServiceGrpc;
import com.steatoda.muddywaters.plankton.proto.Void;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BenchmarkPing extends Benchmark {

	public BenchmarkPing(
		String host,
		int port,
		int numThreads,
		Duration duration
	) {

		super(host, port, numThreads, duration);

		nothing = Void.newBuilder().build();

		stub = PingServiceGrpc.newStub(channel());

	}

	protected void send() {

		// enqueue successor task
		submitRun();

		CountDownLatch finishLatch = new CountDownLatch(1);
		StreamObserver<Void> responseObserver = new StreamObserver<>() {
			@Override
			public void onNext(Void nothing) {
				Log.trace("Got ping response");
			}
			@Override
			public void onError(Throwable t) {
				Log.error("Error during ping!", t);
				incErrorCount();
				finishLatch.countDown();
			}
			@Override
			public void onCompleted() {
				Log.trace("Finished ping");
				incSuccessCount();
				finishLatch.countDown();
			}
		};

		stub.ping(nothing, responseObserver);

		// receiving happens asynchronously
		try {
			if (!finishLatch.await(1, TimeUnit.MINUTES))
				Log.error("Timeout waiting for response!");
		} catch (InterruptedException e) {
			Log.debug("Interrupted while waiting for response");
		}

		incTotalCount();

	}

	private static final Logger Log = LoggerFactory.getLogger(BenchmarkPing.class);

	private final PingServiceGrpc.PingServiceStub stub;
	private final Void nothing;

}
