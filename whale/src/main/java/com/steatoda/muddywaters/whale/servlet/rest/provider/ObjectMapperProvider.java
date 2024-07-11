package com.steatoda.muddywaters.whale.servlet.rest.provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.ws.rs.ext.ContextResolver;
import jakarta.ws.rs.ext.Provider;

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