package com.steatoda.muddywaters.narwhal;

import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

@Path("/")
public class PayloadResource {

	public PayloadResource(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}

	@GET
	@Path("hello")
	@Produces(MediaType.TEXT_PLAIN)
	public String hello() {

		return "Yo!";

	}

	@POST
	@Path("eat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Payload eat(List<Payload> payloads) {

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		return max;

	}

	/** Stream-decode using Jackson. */
	@POST
	@Path("eat1")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Payload eat1(InputStream istream) {

		Payload[] payloads;
		try {
			payloads = objectMapper.readValue(istream, Payload[].class);
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

		return max;

	}

	/** Stream-decode using DSL-JSON. */
	@POST
	@Path("eat2")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public byte[] eat2(InputStream istream) {

		Payload max = new Payload();

		Iterator<Payload> iter;
		try {
			iter = Json.iterateOver(Payload.class, istream);
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

		return ostream.toByteArray();

	}

	private static final DslJson<Payload> Json = new DslJson<>();

	private final ObjectMapper objectMapper;

}
