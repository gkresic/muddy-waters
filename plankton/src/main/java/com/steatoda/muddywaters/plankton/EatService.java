package com.steatoda.muddywaters.plankton;

import com.steatoda.muddywaters.plankton.proto.EatServiceGrpc;
import com.steatoda.muddywaters.plankton.proto.Payload;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EatService extends EatServiceGrpc.EatServiceImplBase {

	@Override
	public void eatOne(Payload payload, StreamObserver<Payload> responseObserver) {

		Log.trace("Got one payload {}/{}", payload.getNumber(), payload.getText());

		responseObserver.onNext(payload);

		responseObserver.onCompleted();

	}

	@Override
	public StreamObserver<Payload> eatStream(StreamObserver<Payload> responseObserver) {

		return new StreamObserver<>() {

			@Override
			public void onNext(Payload payload) {

				Log.trace("Got streamed payload {}/{}", payload.getNumber(), payload.getText());

				if (maxBuilder == null) {
					maxBuilder = Payload.newBuilder()
						.setNumber(payload.getNumber())
						.setText(payload.getText())
					;
					return;
				}

				if (maxBuilder.getNumber() < payload.getNumber())
					maxBuilder.setNumber(payload.getNumber());
				if (maxBuilder.getText().compareTo(payload.getText()) < 0)
					maxBuilder.setText(payload.getText());

			}

			@Override
			public void onError(Throwable t) {
				Log.error("Error during lunch!", t);
			}

			@Override
			public void onCompleted() {
				Payload max = maxBuilder.build();
				Log.trace("Returning max payload {}/{}", max.getNumber(), max.getText());
				responseObserver.onNext(max);
				responseObserver.onCompleted();
			}

			private Payload.Builder maxBuilder = null;

		};

	}

	private static final Logger Log = LoggerFactory.getLogger(EatService.class);

}
