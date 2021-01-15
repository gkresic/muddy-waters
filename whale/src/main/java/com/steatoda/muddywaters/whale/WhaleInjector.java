package com.steatoda.muddywaters.whale;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.steatoda.muddywaters.whale.servlet.WhaleServletModule;

public class WhaleInjector {

	public static Injector get() {
		if (instance == null)
			instance = new WhaleInjector();
		return instance.getInjector();
	}
	
	private WhaleInjector() {
		injector = Guice.createInjector(
			new WhaleModule(),
			new WhaleServletModule()
		);
	}

	public Injector getInjector() { return injector; }

	private static WhaleInjector instance = null;
	private final Injector injector;

}
