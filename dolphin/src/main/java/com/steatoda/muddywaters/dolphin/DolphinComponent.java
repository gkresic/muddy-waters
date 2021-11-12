package com.steatoda.muddywaters.dolphin;

import com.steatoda.muddywaters.dolphin.vertx.VertxModule;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {
	DolphinModule.class,
	VertxModule.class,
})
public interface DolphinComponent {

	@Singleton
	Dolphin dolphin();

}
