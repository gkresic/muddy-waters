package com.steatoda.muddywaters.kaluga.helidon;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.steatoda.muddywaters.kaluga.Payload;
import io.helidon.webserver.http.Handler;
import io.helidon.webserver.http.HttpRules;
import io.helidon.webserver.http.HttpService;
import io.helidon.webserver.http.ServerRequest;
import io.helidon.webserver.http.ServerResponse;

import javax.inject.Inject;

public class RootService implements HttpService {

	@Inject
	public RootService(RestService restService, JsonMapper jsonMapper) {
		this.restService = restService;
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void routing(HttpRules rules) {

		rules.get("bug", this::bug);

		rules.get("hello", this::hello);

		rules.register("rest", restService);

	}

	private void bug(ServerRequest request, ServerResponse response) {

		throw new RuntimeException("Bug!");

	}

	private void hello(ServerRequest request, ServerResponse response) {

		response.send("Yo!");

	}

	/** Buffered-decode using Jackson. */
	private void eat1(ServerRequest request, ServerResponse response) {

		try {

			String body = request.content().as(String.class);

			Payload[] payloads = jsonMapper.readValue(body, Payload[].class);

			Payload max = max(payloads);

			response.send(jsonMapper.writeValueAsString(max));

		} catch (Exception e) {
			throw new RuntimeException("Unable to read input stream", e);
		}

	}

	private String eat2(String body) {

		try {

			Payload[] payloads = jsonMapper.readValue(body, Payload[].class);

			Payload max = max(payloads);

			return jsonMapper.writeValueAsString(max);

		} catch (Exception e) {
			throw new RuntimeException("Unable to read input stream", e);
		}

	}

	private Payload eat3(Payload[] payloads) {

		try {

			return max(payloads);

		} catch (Exception e) {
			throw new RuntimeException("Unable to read input stream", e);
		}

	}

	private Payload max(Payload[] payloads) {

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		return max;

	}

	private final RestService restService;
	private final JsonMapper jsonMapper;

}
