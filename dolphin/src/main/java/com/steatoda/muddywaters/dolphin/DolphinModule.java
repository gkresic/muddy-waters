package com.steatoda.muddywaters.dolphin;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

@Module
public interface DolphinModule {

	@Provides
	@Singleton
	static EventBus provideEventBus() {

		EventBus.builder()
			.addIndex(new DolphinEventBusIndex())
			.logNoSubscriberMessages(false)
			.installDefaultEventBus();

		return EventBus.getDefault();

	}

	@Provides
	@Singleton
	static JsonMapper provideJsonMapper() {
		return JsonMapper.builder()
				   .configure(MapperFeature.AUTO_DETECT_CREATORS, false)
				   .configure(MapperFeature.AUTO_DETECT_FIELDS, false)
				   .configure(MapperFeature.AUTO_DETECT_GETTERS, false)
				   .configure(MapperFeature.AUTO_DETECT_IS_GETTERS, false)
				   .configure(SerializationFeature.INDENT_OUTPUT, true)
				   .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
				   .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
				   .serializationInclusion(JsonInclude.Include.NON_NULL)
				   .build();
	}

	@Provides
	static DolphinStatus provideStatus() {
		DolphinStatus status = new DolphinStatus();
		status.setVersion(DolphinProperties.get().getVersion());
		return status;
	}

}
