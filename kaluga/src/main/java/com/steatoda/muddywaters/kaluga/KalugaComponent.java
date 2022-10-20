package com.steatoda.muddywaters.kaluga;

import com.steatoda.muddywaters.kaluga.helidon.HelidonModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
	KalugaModule.class,
	HelidonModule.class,
})
public interface KalugaComponent {

	@Singleton
	Kaluga kaluga();

}
