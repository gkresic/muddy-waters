package com.steatoda.muddywaters.orca.spark;

import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.steatoda.muddywaters.orca.Payload;
import spark.Request;
import spark.Response;
import spark.RouteGroup;
import spark.Spark;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class OrcaRouteGroup implements RouteGroup {

	@Inject
	public OrcaRouteGroup(JsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void addRoutes() {

		Spark.get("/hello", this::hello);

		Spark.post("/eat", this::eat1);

		Spark.post("/eat1", this::eat1);

		Spark.post("/eat2", this::eat2);

		Spark.post("/eat3", this::eat3);

	}

	private Object hello(Request request, Response response) {

		return "Yo!";

	}

	/** Buffered-decode using Jackson. */
	private Object eat1(Request request, Response response) throws JsonProcessingException {

		Payload[] payloads = jsonMapper.readValue(request.body(), Payload[].class);

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		response.type("application/json");

		return jsonMapper.writeValueAsString(max);

	}

	/** Stream-decode using Jackson. */
	private Object eat2(Request request, Response response) throws IOException {

		Payload[] payloads = jsonMapper.readValue(request.raw().getInputStream(), Payload[].class);

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		response.type("application/json");

		return jsonMapper.writeValueAsString(max);

	}

	/** Stream-decode using DSL-JSON. */
	private Object eat3(Request request, Response response) throws IOException {

		Payload max = new Payload();

		Iterator<Payload> iter = Json.iterateOver(Payload.class, request.raw().getInputStream());

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

		response.type("application/json");

		return ostream.toByteArray();

	}

	private static final DslJson<Payload> Json = new DslJson<>();

	private final JsonMapper jsonMapper;

}
