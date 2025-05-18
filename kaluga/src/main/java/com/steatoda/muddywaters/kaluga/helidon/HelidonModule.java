package com.steatoda.muddywaters.kaluga.helidon;

import com.fasterxml.jackson.databind.json.JsonMapper;
import dagger.Module;
import dagger.Provides;
import io.helidon.common.media.type.MediaType;
import io.helidon.common.media.type.MediaTypes;
import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.http.media.MediaContext;
import io.helidon.http.media.MediaContextConfig;
import io.helidon.http.media.jackson.JacksonSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Module
public interface HelidonModule {

	String ConfigFileName			= "helidon.yaml";
	MediaType ConfigFileMimeType	= MediaTypes.APPLICATION_X_YAML;

	@Provides
	@Singleton
	static Config provideConfig() {
		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream(ConfigFileName)) {
			if (istream == null)
				throw new FileNotFoundException("Helidon configuration (" + ConfigFileName + ") not found on classpath");
			return Config.create(ConfigSources.create(istream, ConfigFileMimeType));
		} catch (Exception e) {
			throw new RuntimeException("Error loading Helidon configuration", e);
		}
	}

	@Provides
	@Singleton
	static MediaContext provideMediaContext(JsonMapper jsonMapper) {
		return MediaContextConfig.builder()
			.addMediaSupport(JacksonSupport.create(jsonMapper))
			.build();
	}

	Logger Log = LoggerFactory.getLogger(HelidonModule.class);

}
