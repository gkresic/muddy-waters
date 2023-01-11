package com.steatoda.muddywaters.beluga;

import com.steatoda.muddywaters.beluga.servlet.JettyModule;
import com.steatoda.muddywaters.beluga.servlet.rest.JavalinModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
	BelugaModule.class,
	JettyModule.class,
	JavalinModule.class,
})
public interface BelugaComponent {

	@Singleton
	Beluga beluga();

}
