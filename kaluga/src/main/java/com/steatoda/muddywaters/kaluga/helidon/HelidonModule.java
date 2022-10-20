package com.steatoda.muddywaters.kaluga.helidon;

import dagger.Module;
import dagger.Provides;
import io.helidon.common.http.MediaType;
import io.helidon.config.Config;
import io.helidon.config.ConfigSources;
import io.helidon.media.common.MediaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.FileNotFoundException;
import java.io.InputStream;

@Module
public interface HelidonModule {

	String ConfigFileName		= "helidon.yaml";
	String ConfigFileMimeType	= MediaType.APPLICATION_X_YAML.toString();

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
	static MediaContext provideMediaContext() {
		return MediaContext.create();
	}

	Logger Log = LoggerFactory.getLogger(HelidonModule.class);

}
