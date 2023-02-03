package com.steatoda.muddywaters.piranha;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Module;
import dagger.Provides;
import org.greenrobot.eventbus.EventBus;

import javax.inject.Singleton;

@Module
public interface PiranhaModule {

	@Provides
	@Singleton
	static EventBus provideEventBus() {

		EventBus.builder()
			.addIndex(new PiranhaEventBusIndex())
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
			.configure(SerializationFeature.INDENT_OUTPUT, false)
			.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, true)
			.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.serializationInclusion(JsonInclude.Include.NON_NULL)
			.build();
	}

	@Provides
	static PiranhaStatus provideStatus() {
		PiranhaStatus status = new PiranhaStatus();
		status.setVersion(PiranhaProperties.get().getVersion());
		return status;
	}

}
