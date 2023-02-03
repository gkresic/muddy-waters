package com.steatoda.muddywaters.piranha.servlet.rest;

import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.steatoda.muddywaters.piranha.Payload;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteGroup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class RootEndpointGroup extends RouteGroup {

	@AssistedFactory
	public interface Factory {
		RootEndpointGroup create(String uriPattern);
	}

	@AssistedInject
	public RootEndpointGroup(
		@Assisted String uriPattern,
		EntityEndpointGroup.Factory entityEndpointGroupFactory,
		JsonMapper jsonMapper
	) {

		super(uriPattern);

		this.jsonMapper = jsonMapper;

		GET("/hello", this::hello);

		POST("/eat", this::eat1);

		POST("/eat1", this::eat1);

		POST("/eat2", this::eat2);

		POST("/eat3", this::eat3);

		addRouteGroup(entityEndpointGroupFactory.create("/entity"));

	}

	private void hello(RouteContext context) {

		context.send("Yo!");

	}

	/** Buffered-decode using Jackson. */
	private void eat1(RouteContext context) {

		Payload[] payloads;
		try {
			payloads = jsonMapper.readValue(context.getRequest().getBody(), Payload[].class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Unable to read request body", e);
		}

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		try {
			context
				.json()
				.send(jsonMapper.writeValueAsString(max))
			;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Unable to serialize response", e);
		}

	}

	/** Stream-decode using Jackson. */
	private void eat2(RouteContext context) {

		Payload[] payloads;
		try {
			payloads = jsonMapper.readValue(context.getRequest().getHttpServletRequest().getInputStream(), Payload[].class);
		} catch (IOException e) {
			throw new RuntimeException("Unable to read request body", e);
		}

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		try {
			context
				.json()
				.send(jsonMapper.writeValueAsString(max))
			;
		} catch (JsonProcessingException e) {
			throw new RuntimeException("Unable to serialize response", e);
		}

	}

	/** Stream-decode using DSL-JSON. */
	private void eat3(RouteContext context) {

		Payload max = new Payload();

		Iterator<Payload> iter;
		try {
			iter = Json.iterateOver(Payload.class, context.getRequest().getHttpServletRequest().getInputStream());
		} catch (IOException e) {
			throw new RuntimeException("Unable to read request body", e);
		}

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
		try {
			Json.serialize(max, ostream);
		} catch (IOException e) {
			throw new RuntimeException("Unable to serialize response", e);
		}

		context
			.json()
			.send(ostream.toByteArray())
		;

	}

	private static final DslJson<Payload> Json = new DslJson<>();

	private final JsonMapper jsonMapper;

}
