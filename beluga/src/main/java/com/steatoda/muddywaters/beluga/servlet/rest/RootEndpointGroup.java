package com.steatoda.muddywaters.beluga.servlet.rest;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

import javax.inject.Inject;

public class RootEndpointGroup implements EndpointGroup {

	@Inject
	public RootEndpointGroup(RestEndpointGroup restEndpointGroup) {
		this.restEndpointGroup = restEndpointGroup;
	}

	@Override
	public void addEndpoints() {

		ApiBuilder.get("bug", this::bug);

		ApiBuilder.get("hello", this::hello);

		ApiBuilder.path("rest", restEndpointGroup);

	}

	private void bug(Context context) {

		throw new RuntimeException("Bug!");

	}

	private void hello(Context context) {

		context.result("Yo!");

	}

	private final RestEndpointGroup restEndpointGroup;

}
