package com.steatoda.muddywaters.whale.servlet.rest.provider;

import javax.inject.Inject;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	@Inject
	ObjectMapperProvider(ObjectMapper mapper) {
		this.mapper = mapper;
	}

	@Override
	public ObjectMapper getContext(Class<?> objectType) {
		return mapper;
	}

	private final ObjectMapper mapper;

}