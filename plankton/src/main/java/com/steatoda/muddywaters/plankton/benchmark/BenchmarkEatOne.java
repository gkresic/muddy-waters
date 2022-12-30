package com.steatoda.muddywaters.plankton.benchmark;

import com.steatoda.muddywaters.plankton.proto.EatServiceGrpc;
import com.steatoda.muddywaters.plankton.proto.Payload;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BenchmarkEatOne extends Benchmark {

	public BenchmarkEatOne(
		String host,
		int port,
		int numThreads,
		Duration duration
	) {

		super(host, port, numThreads, duration);

		payload = Payload.newBuilder()
			.setText("foo")
			.setNumber(42)
			.build();

		Log.info("Using payload {}/{}", payload.getNumber(), payload.getText());

		stub = EatServiceGrpc.newStub(channel());

	}

	protected void send() {

		// enqueue successor task
		submitRun();

		CountDownLatch finishLatch = new CountDownLatch(1);
		StreamObserver<Payload> responseObserver = new StreamObserver<>() {
			@Override
			public void onNext(Payload payload) {
				Log.trace("Got {}/{}", payload.getNumber(), payload.getText());
			}
			@Override
			public void onError(Throwable t) {
				Log.error("Error during lunch!", t);
				incErrorCount();
				finishLatch.countDown();
			}
			@Override
			public void onCompleted() {
				Log.trace("Finished lunch");
				incSuccessCount();
				finishLatch.countDown();
			}
		};

		stub.eatOne(payload, responseObserver);

		// receiving happens asynchronously
		try {
			if (!finishLatch.await(1, TimeUnit.MINUTES))
				Log.error("Timeout waiting for response!");
		} catch (InterruptedException e) {
			Log.debug("Interrupted while waiting for response");
		}

		incTotalCount();

	}

	private static final Logger Log = LoggerFactory.getLogger(BenchmarkEatOne.class);

	private final EatServiceGrpc.EatServiceStub stub;
	private final Payload payload;

}
