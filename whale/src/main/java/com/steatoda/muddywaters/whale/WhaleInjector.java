package com.steatoda.muddywaters.whale;

import com.google.inject.Guice;
import com.google.inject.Injector;

import com.steatoda.muddywaters.whale.servlet.WhaleServletModule;
import com.steatoda.muddywaters.whale.servlet.rest.ResourceModule;

public class WhaleInjector {

	public static Injector get() {
		if (instance == null)
			instance = new WhaleInjector();
		return instance.getInjector();
	}
	
	private WhaleInjector() {
		injector = Guice.createInjector(
			new WhaleModule(),
			new WhaleServletModule(),
			new ResourceModule()
		);
	}

	public Injector getInjector() { return injector; }

	private static WhaleInjector instance = null;
	private final Injector injector;

}
