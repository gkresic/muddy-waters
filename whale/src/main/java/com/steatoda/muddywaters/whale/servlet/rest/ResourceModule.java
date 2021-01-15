package com.steatoda.muddywaters.whale.servlet.rest;

import com.google.inject.AbstractModule;

import com.steatoda.muddywaters.whale.servlet.rest.provider.ObjectMapperProvider;
import com.steatoda.muddywaters.whale.servlet.rest.resource.RootResource;

/**
 * Place to register all JAX-RS resources
 */
public class ResourceModule extends AbstractModule {

	@Override 
	protected void configure() {

		bind(ObjectMapperProvider.class);
		
		bind(RootResource.class);

	}

}
