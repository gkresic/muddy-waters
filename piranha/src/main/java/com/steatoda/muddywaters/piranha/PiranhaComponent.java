package com.steatoda.muddywaters.piranha;

import com.steatoda.muddywaters.piranha.servlet.JettyModule;
import com.steatoda.muddywaters.piranha.servlet.rest.PippoModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
	PiranhaModule.class,
	JettyModule.class,
	PippoModule.class,
})
public interface PiranhaComponent {

	@Singleton
	Piranha piranha();

}
