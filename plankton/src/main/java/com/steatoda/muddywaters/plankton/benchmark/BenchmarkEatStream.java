package com.steatoda.muddywaters.plankton.benchmark;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.steatoda.muddywaters.plankton.proto.EatServiceGrpc;
import com.steatoda.muddywaters.plankton.proto.Payload;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class BenchmarkEatStream extends Benchmark {

	public BenchmarkEatStream(
		String host,
		int port,
		int numThreads,
		Duration duration,
		String jsonDataBaseName
	) throws IOException {

		super(host, port, numThreads, duration);

		Log.info("Benchmarking {}", jsonDataBaseName);

		Path jsonPath = Paths.get(jsonDataBaseName + ".json");

		if (!Files.exists(jsonPath))
			throw new IOException("JSON file " + jsonPath.toAbsolutePath() + " doesn't exist!");

		JsonMapper jsonMapper = JsonMapper.builder()
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true)
			.build();

		try (JsonParser jsonParser = jsonMapper.createParser(jsonPath.toFile())) {
			payloads = Arrays.stream(jsonParser.readValueAs(JsonPayload[].class))
				.map(jsonPayload -> Payload.newBuilder()
					.setText(jsonPayload.text)
					.setNumber(jsonPayload.number)
					.build()
				)
				.toArray(Payload[]::new);
		}

		Log.info("Loaded {} payloads", payloads.length);

		stub = EatServiceGrpc.newStub(channel());

	}

	protected void send() {

		// enqueue successor task
		submitRun();

		CountDownLatch finishLatch = new CountDownLatch(1);
		StreamObserver<Payload> responseObserver = new StreamObserver<>() {
			@Override
			public void onNext(Payload payload) {
				Log.trace("Got max {}/{}", payload.getNumber(), payload.getText());
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

		StreamObserver<Payload> requestObserver = stub.eatStream(responseObserver);
		try {
			for (Payload payload : payloads) {
				Log.trace("Sending {}/{}", payload.getNumber(), payload.getText());
				requestObserver.onNext(payload);
				if (finishLatch.getCount() == 0)
					return;
			}
		} catch (Exception e) {
			// cancel RPC
			requestObserver.onError(e);
			throw e;
		}

		requestObserver.onCompleted();

		// receiving happens asynchronously
		try {
			if (!finishLatch.await(1, TimeUnit.MINUTES))
				Log.error("Timeout waiting for response!");
		} catch (InterruptedException e) {
			Log.debug("Interrupted while waiting for response");
		}

		incTotalCount();

	}

	private static final Logger Log = LoggerFactory.getLogger(BenchmarkEatStream.class);

	private final EatServiceGrpc.EatServiceStub stub;
	private final Payload[] payloads;

}
