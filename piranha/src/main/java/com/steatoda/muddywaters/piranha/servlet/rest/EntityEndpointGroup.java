package com.steatoda.muddywaters.piranha.servlet.rest;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import ro.pippo.core.route.RouteContext;
import ro.pippo.core.route.RouteGroup;

public class EntityEndpointGroup extends RouteGroup {

	@AssistedFactory
	public interface Factory {
		EntityEndpointGroup create(String uriPattern);
	}

	@AssistedInject
	public EntityEndpointGroup(@Assisted String uriPattern) {

		super(uriPattern);

		GET("/foo", this::foo);

		GET("/bar", this::bar);

	}

	private void foo(RouteContext context) {

		context.send("Foo!");

	}

	private void bar(RouteContext context) {

		context.send("Bar!");

	}

}
