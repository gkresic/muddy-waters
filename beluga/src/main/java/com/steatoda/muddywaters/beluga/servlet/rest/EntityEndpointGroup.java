package com.steatoda.muddywaters.beluga.servlet.rest;

import io.javalin.apibuilder.ApiBuilder;
import io.javalin.apibuilder.EndpointGroup;
import io.javalin.http.Context;

import javax.inject.Inject;

public class EntityEndpointGroup implements EndpointGroup {

	@Inject
	public EntityEndpointGroup() {}

	@Override
	public void addEndpoints() {

		ApiBuilder.get("foo", this::foo);

		ApiBuilder.get("bar", this::bar);

	}

	private void foo(Context context) {

		context.result("Foo!");

	}

	private void bar(Context context) {

		context.result("Bar!");

	}

}
