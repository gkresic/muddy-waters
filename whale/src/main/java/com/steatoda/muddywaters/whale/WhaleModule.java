package com.steatoda.muddywaters.whale;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class WhaleModule extends AbstractModule {

	@Override 
	protected void configure() {
		
		bind(EventBus.class).to(WhaleEventBus.class).asEagerSingleton();

	}

	@Provides
	@Singleton
    ObjectMapper provideObjectMapper() {
		// use custom factory that DOES NOT close underlying streams when reading/writing them
		// (allows to write several values to same stream e.g. when writing to ZIP stream)
		JsonFactory jsonFactory = new JsonFactory();
		jsonFactory.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
		jsonFactory.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, false);
        return JsonMapper.builder(jsonFactory)
            .configure(SerializationFeature.INDENT_OUTPUT, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(MapperFeature.AUTO_DETECT_CREATORS, false)
			.configure(MapperFeature.AUTO_DETECT_FIELDS, false)
			.configure(MapperFeature.AUTO_DETECT_GETTERS, false)
			.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false)
            .serializationInclusion(Include.NON_NULL)
                   .addModule(new JavaTimeModule())
                   .addModule(new Jdk8Module())
            .build();
	}

	@Provides
	WhaleStatus provideStatus() {
		WhaleStatus status = new WhaleStatus();
		status.setVersion(WhaleProperties.get().getVersion());
		return status;
	}

	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(WhaleModule.class);

}
