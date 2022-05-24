package com.steatoda.muddywaters.dolphin.vertx;

import com.dslplatform.json.DslJson;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.json.async.NonBlockingJsonParser;
import com.steatoda.muddywaters.dolphin.DolphinStatus;
import com.steatoda.muddywaters.dolphin.Payload;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.core.parsetools.JsonParser;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.impl.RouterImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;

public class DolphinRouter extends RouterImpl {

	@Inject
	public DolphinRouter(Vertx vertx) {

		super(vertx);

		get("/status").respond(this::status);

		post("/eat")
			.consumes("application/json")	// TODO use MediaType from Guava
			.produces("application/json")
			.handler(BodyHandler.create())
			.handler(this::eat1);

		post("/eat1")
			.consumes("application/json")
			.produces("application/json")
			.handler(BodyHandler.create())
			.handler(this::eat1);
		post("/eat1b")
			.consumes("application/json")
			.produces("application/json")
			.handler(BodyHandler.create())
			.blockingHandler(this::eat1);

		post("/eat2")
			.consumes("application/json")
			.produces("application/json")
			.handler(this::eat2);
		post("/eat2b")
			.consumes("application/json")
			.produces("application/json")
			.blockingHandler(this::eat2);

		post("/eat3")
			.consumes("application/json")
			.produces("application/json")
			.handler(BodyHandler.create())
			.handler(this::eat3);
		post("/eat3b")
			.consumes("application/json")
			.produces("application/json")
			.handler(BodyHandler.create())
			.blockingHandler(this::eat3);

		post("/eat9")
			.consumes("application/json")
			.produces("application/json")
			.handler(this::eat9);
		post("/eat9b")
			.consumes("application/json")
			.produces("application/json")
			.blockingHandler(this::eat9);

		post("/bench1")
			.consumes("application/json")
			.produces("application/json")
			.handler(BodyHandler.create())
			.handler(this::bench1);
		post("/bench2")
			.consumes("application/json")
			.produces("application/json")
			.handler(BodyHandler.create())
			.handler(this::bench2);
		post("/bench3")
			.consumes("application/json")
			.produces("application/json")
			.handler(this::bench3);

	}

	private Future<DolphinStatus> status(RoutingContext context) {
		return Future.succeededFuture(new DolphinStatus());	// TODO use Dagger
	}

	/**
	 * <p>Buffer decode using {@link Json#decodeValue} (defaults to Jackson).</p>
	 */
	private void eat1(RoutingContext context) {

		Payload[] payloads = Json.decodeValue(context.body().buffer(), Payload[].class);

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		context.json(max);

	}

	/**
	 * <p>Stream decode using {@link JsonParser} (uses Jackson's stream API)</p>
	 */
	private void eat2(RoutingContext context) {

		JsonParser parser = JsonParser.newParser(context.request());

		parser.objectValueMode();

		Payload max = new Payload();

		parser.handler(event -> {
			switch (event.type()) {
//				case START_ARRAY:
//					Log.info("Array START");
//					break;
				case END_ARRAY:
					context.json(max);
					break;
				case VALUE:
					Payload payload = event.mapTo(Payload.class);
					if (max.number == null || max.number < payload.number)
						max.number = payload.number;
					if (max.text == null || max.text.compareTo(payload.text) < 0)
						max.text = payload.text;
					break;
			}
		});

		context.request().resume();

	}

	/**
	 * <p>Buffer decode using DSL-JSON's streaming API.</p>
	 */
	private void eat3(RoutingContext context) {

		try {

			Payload max = new Payload();

			Iterator<Payload> iter = DslJsonPayload.iterateOver(Payload.class, new ByteBufInputStream(context.body().buffer().getByteBuf()));

			if (iter == null)
				throw new RuntimeException("Empty input?!");

			while (iter.hasNext()) {
				Payload payload = iter.next();
				if (max.number == null || max.number < payload.number)
					max.number = payload.number;
				if (max.text == null || max.text.compareTo(payload.text) < 0)
					max.text = payload.text;
			}

			ByteBufOutputStream ostream = new ByteBufOutputStream(Unpooled.buffer(300));
			DslJsonPayload.serialize(max, ostream);

			context.response().end(Buffer.buffer(ostream.buffer()));

		} catch (IOException e) {
			throw new RuntimeException("Unable to read input stream", e);
		}

	}

	/**
	 * <p>Stream decode using blocking {@link VertxInputStream} and Jackson.</p>
	 *
	 * <p><b>Doesn't work!</b></p>
	 */
	private void eat9(RoutingContext context) {

		Log.info("Request received...");

		Log.info("Request ended 1: {}", context.request().isEnded());
		Log.info("Request ended 1.1: {}", context.request().isEnded());

		//		JsonFactory jsonFactory = new JsonFactory();
//		//jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
//		jsonFactory.configure(com.fasterxml.jackson.core.JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
//		ObjectMapper mapper = new ObjectMapper(/*jsonFactory*/)
//								  .configure(SerializationFeature.WRAP_ROOT_VALUE, false)
//								  .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
//								  .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
//								  .configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true)
//								  .disable(MapperFeature.AUTO_DETECT_CREATORS)
//								  .disable(MapperFeature.AUTO_DETECT_FIELDS)
//								  .disable(MapperFeature.AUTO_DETECT_GETTERS)
//								  .disable(MapperFeature.AUTO_DETECT_IS_GETTERS)
//								  .setSerializationInclusion(JsonInclude.Include.NON_NULL)
//								  .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
//			;

//		SimpleModule module = new SimpleModule();
//			// custom types
//			module.addSerializer(JsonObject.class, new JsonObjectSerializer());
//			module.addSerializer(JsonArray.class, new JsonArraySerializer());
//			// he have 2 extensions: RFC-7493
//			module.addSerializer(Instant.class, new InstantSerializer());
//			module.addDeserializer(Instant.class, new InstantDeserializer());
//			module.addSerializer(byte[].class, new ByteArraySerializer());
//			module.addDeserializer(byte[].class, new ByteArrayDeserializer());
//			module.addSerializer(Buffer.class, new BufferSerializer());
//			module.addDeserializer(Buffer.class, new BufferDeserializer());
//		mapper.registerModule(module);

		Payload max = new Payload();

		Log.info("Request ended 2: {}", context.request().isEnded());

		try (VertxInputStream istream = new VertxInputStream(context, Long.MAX_VALUE)) {

//			com.fasterxml.jackson.core.JsonParser jsonParser = mapper/*DatabindCodec.mapper()*/.getFactory().createParser(istream);
//
//			if (jsonParser.nextToken() != JsonToken.START_ARRAY) {
//				Log.error("Payload is NOT an array");
//				throw new RuntimeException("Payload is not an array");
//			} else {
//				Log.info("Payload IS an array");
//			}
//
//			while (jsonParser.nextToken() == JsonToken.START_OBJECT) {
//				Payload payload = mapper/*DatabindCodec.mapper()*/.readValue(jsonParser, Payload.class);
//				if (max.number == null || max.number < payload.number)
//					max.number = payload.number;
//				if (max.text == null || max.text.compareTo(payload.text) < 0)
//					max.text = payload.text;
//			}

			String text = new String(istream.readAllBytes(), StandardCharsets.UTF_8);
			Log.info("Got payload {}", text);

		} catch (IOException e) {
			throw new RuntimeException("Unable to read input stream", e);
		}

		Log.info("Sending response {} {}", max.text, max.number);

		context.json(max);

	}

	private void bench1(RoutingContext context) {

		long start = System.currentTimeMillis();

		Payload[] payloads = Json.decodeValue(context.body().buffer(), Payload[].class);

		Log.info("Parsed body 1 ({}) in {} ms", payloads.length, System.currentTimeMillis() - start);

		context.response().end("Done!");

	}

	private void bench2(RoutingContext context) {

		long start = System.currentTimeMillis();

		com.fasterxml.jackson.core.JsonParser jsonParser = DatabindCodec.createParser(context.body().buffer());

		Payload[] payloads = DatabindCodec.fromParser(jsonParser, Payload[].class);

		Log.info("Parsed body 2 ({}) in {} ms", payloads.length, System.currentTimeMillis() - start);

		context.response().end("Done!");

	}

	// doesn't work
	private void bench3(RoutingContext context) {

		try {

			long start = System.currentTimeMillis();

			JsonFactory factory = new JsonFactory();
			NonBlockingJsonParser jsonParser = (NonBlockingJsonParser) factory.createNonBlockingByteArrayParser();

			Handler<Buffer> dataHandler = data -> {
				try {
					byte[] bytes = data.getBytes();
					jsonParser.feedInput(bytes, 0, bytes.length);
					context.request().handler(null);	// remove itself as handler
				} catch (IOException e) {
					throw new DecodeException("Could not feed input to JSON parser", e);
				}
			};

			// start handler
			context.request().handler(dataHandler);

			context.request().endHandler(nothing -> {
				jsonParser.endOfInput();
			});

			Payload[] payloads = DatabindCodec.fromParser(jsonParser, Payload[].class);

			Log.info("Parsed body 4 ({}) in {} ms", payloads.length, System.currentTimeMillis() - start);

		} catch (IOException e) {
			throw new RuntimeException("Unable to read input stream", e);
		}

		context.response().end("Done!");

	}

	private static final Logger Log = LoggerFactory.getLogger(DolphinRouter.class);

	private static final DslJson<Payload> DslJsonPayload = new DslJson<>();

}
