package com.steatoda.muddywaters.kaluga.helidon;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.steatoda.muddywaters.kaluga.Payload;
import io.helidon.webserver.Handler;
import io.helidon.webserver.Routing;
import io.helidon.webserver.ServerRequest;
import io.helidon.webserver.ServerResponse;
import io.helidon.webserver.Service;

import javax.inject.Inject;

public class RootService implements Service {

	@Inject
	public RootService(JsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void update(Routing.Rules rules) {

		rules.get("/hello", this::hello);

		rules.post("/eat", Handler.create(String.class, this::eat1));

		rules.post("/eat1", Handler.create(String.class, this::eat1));

	}

	private void hello(ServerRequest request, ServerResponse response) {

		response.send("Yo!");

	}

	/** Buffered-decode using Jackson. */
	private void eat1(ServerRequest request, ServerResponse response, String body) {

		try {

			Payload[] payloads = jsonMapper.readValue(body, Payload[].class);

			Payload max = new Payload();

			for (Payload payload : payloads) {
				if (max.number == null || max.number < payload.number)
					max.number = payload.number;
				if (max.text == null || max.text.compareTo(payload.text) < 0)
					max.text = payload.text;
			}

			response.send(jsonMapper.writeValueAsString(max));

		} catch (Exception e) {
			throw new RuntimeException("Unable to read input stream", e);
		}

	}

	private final JsonMapper jsonMapper;

}
