package com.steatoda.muddywaters.whale;

import java.text.SimpleDateFormat;

import javax.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
		ObjectMapper mapper = new ObjectMapper(jsonFactory)
			.configure(SerializationFeature.WRAP_ROOT_VALUE, false)
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(MapperFeature.USE_WRAPPER_NAME_AS_PROPERTY_NAME, true)
			.disable(MapperFeature.AUTO_DETECT_CREATORS)
			.disable(MapperFeature.AUTO_DETECT_FIELDS)
			.disable(MapperFeature.AUTO_DETECT_GETTERS)
			.disable(MapperFeature.AUTO_DETECT_IS_GETTERS)
			.setSerializationInclusion(Include.NON_NULL)
			.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"))
		;
		mapper.setConfig(mapper.getSerializationConfig().withView(Object.class));
		mapper.setConfig(mapper.getDeserializationConfig().withView(Object.class));
		return mapper;
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

