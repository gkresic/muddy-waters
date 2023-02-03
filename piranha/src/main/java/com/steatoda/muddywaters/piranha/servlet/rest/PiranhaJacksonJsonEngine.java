package com.steatoda.muddywaters.piranha.servlet.rest;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import ro.pippo.core.Application;
import ro.pippo.core.ContentTypeEngine;
import ro.pippo.core.HttpConstants;
import ro.pippo.core.PippoRuntimeException;

import javax.inject.Inject;
import java.io.IOException;

// based on ro.pippo.jackson.JacksonJsonEngine and superclass
// https://github.com/pippo-java/pippo/tree/master/pippo-content-type-parent/pippo-jackson
public class PiranhaJacksonJsonEngine implements ContentTypeEngine {

	@Inject
	public PiranhaJacksonJsonEngine(JsonMapper jsonMapper) {
		this.jsonMapper = jsonMapper;
	}

	@Override
	public void init(Application application) {}

	@Override
	public String getContentType() {
		return HttpConstants.ContentType.APPLICATION_JSON;
	}

	@Override
	public String toString(Object object) {
		try {
			return jsonMapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new PippoRuntimeException(e, "Error serializing object to {}", getContentType());
		}
	}

	@Override
	public <T> T fromString(String content, Class<T> classOfT) {
		try {
			return jsonMapper.readValue(content, classOfT);
		} catch (JsonParseException | JsonMappingException e) {
			throw new PippoRuntimeException(e, "Error deserializing {}", getContentType());
		} catch (IOException e) {
			throw new PippoRuntimeException(e, "Invalid {} document", getContentType());
		}
	}

	private final JsonMapper jsonMapper;

}
