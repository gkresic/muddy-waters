package com.steatoda.muddywaters.beluga;

import com.steatoda.muddywaters.beluga.javalin.JavalinModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
	BelugaModule.class,
	JavalinModule.class,
})
public interface BelugaComponent {

	@Singleton
	Beluga beluga();

}
