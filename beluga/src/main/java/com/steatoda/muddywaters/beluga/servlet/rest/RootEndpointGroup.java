package com.steatoda.muddywaters.beluga.servlet.rest;

import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.steatoda.muddywaters.beluga.Payload;
import com.steatoda.muddywaters.beluga.proto.PayloadRequest;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class RootEndpointGroup implements EndpointGroup {

	@Inject
	public RootEndpointGroup(EntityEndpointGroup entityEndpointGroup, JsonMapper jsonMapper) {
		this.entityEndpointGroup = entityEndpointGroup;
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void addEndpoints() {

		ApiBuilder.get("/bug", this::hello);

		ApiBuilder.get("/rest/hello", this::hello);

		ApiBuilder.post("/rest/eat", this::eat1);

		ApiBuilder.post("/rest/eat1", this::eat1);

		ApiBuilder.post("/rest/eat2", this::eat2);

		ApiBuilder.post("/rest/eat3", this::eat3);

		ApiBuilder.post("/rest/eatProtobuf", this::eatProtobuf);

		ApiBuilder.path("/rest/entity", entityEndpointGroup);

	}

	private void hello(Context context) {

		context.result("Yo!");

	}

	/** Buffered-decode using Jackson. */
	private void eat1(Context context) throws JsonProcessingException {

		Payload[] payloads = jsonMapper.readValue(context.body(), Payload[].class);

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		context
			.contentType("application/json")
			.result(jsonMapper.writeValueAsString(max))
		;

	}

	/** Stream-decode using Jackson. */
	private void eat2(Context context) throws IOException {

		Payload[] payloads = jsonMapper.readValue(context.req().getInputStream(), Payload[].class);

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		context
			.contentType("application/json")
			.result(jsonMapper.writeValueAsString(max))
		;

	}

	/** Stream-decode using DSL-JSON. */
	private void eat3(Context context) throws IOException {

		Payload max = new Payload();

		Iterator<Payload> iter = Json.iterateOver(Payload.class, context.req().getInputStream());

		if (iter == null)
			throw new RuntimeException("Empty input?!");

		while (iter.hasNext()) {
			Payload payload = iter.next();
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		ByteArrayOutputStream ostream = new ByteArrayOutputStream(300);
		Json.serialize(max, ostream);

		context
			.contentType("application/json")
			.result(ostream.toByteArray())
		;

	}

	/** Protobuf payload. */
	private void eatProtobuf(Context context) throws IOException {

		PayloadRequest protoRequest = PayloadRequest.parseFrom(context.req().getInputStream());

		Payload max = new Payload();

		for (int index = 0; index < protoRequest.getPayloadsCount(); ++index) {
			com.steatoda.muddywaters.beluga.proto.Payload protoPayload = protoRequest.getPayloads(index);
			if (max.number == null || max.number < protoPayload.getNumber())
				max.number = protoPayload.getNumber();
			if (max.text == null || max.text.compareTo(protoPayload.getText()) < 0)
				max.text = protoPayload.getText();
		}

		context
			.contentType("application/json")
			.result(jsonMapper.writeValueAsString(max))
		;

	}

	private static final DslJson<Payload> Json = new DslJson<>();

	private final EntityEndpointGroup entityEndpointGroup;
	private final JsonMapper jsonMapper;

}
