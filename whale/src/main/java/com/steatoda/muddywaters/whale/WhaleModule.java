package com.steatoda.muddywaters.whale;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

public class WhaleModule extends AbstractModule {

	@Override 
	protected void configure() {
		bind(Whale.class).asEagerSingleton();
	}

	@Provides
	@Singleton
	ObjectMapper provideObjectMapper() {
		return JsonMapper.builder()
			.configure(SerializationFeature.INDENT_OUTPUT, false)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.configure(MapperFeature.AUTO_DETECT_CREATORS, false)
			.configure(MapperFeature.AUTO_DETECT_FIELDS, false)
			.configure(MapperFeature.AUTO_DETECT_GETTERS, false)
			.configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false)
			.build();
	}

	@SuppressWarnings("unused")
	private static final Logger Log = LoggerFactory.getLogger(WhaleModule.class);

}
