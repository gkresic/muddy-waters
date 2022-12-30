package com.steatoda.muddywaters.plankton;

import com.steatoda.muddywaters.plankton.proto.PingServiceGrpc;
import com.steatoda.muddywaters.plankton.proto.Void;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PingService extends PingServiceGrpc.PingServiceImplBase {

	@Override
	public void ping(Void nothing, StreamObserver<Void> responseObserver) {

		Log.trace("Ping!");

		responseObserver.onNext(nothing);

		responseObserver.onCompleted();

	}

	private static final Logger Log = LoggerFactory.getLogger(PingService.class);

}
