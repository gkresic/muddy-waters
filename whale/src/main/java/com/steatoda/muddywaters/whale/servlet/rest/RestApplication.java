package com.steatoda.muddywaters.whale.servlet.rest;

import java.util.Collections;
import java.util.Map;

import javax.ws.rs.core.Application;

/**
 * Place to configure JAX-RS.
 */
public class RestApplication extends Application {

	/**
	 * <b>DO NOT</b> register resource classes here because that will circumvent Guice injections.
	 * Instead, register them in {@link com.steatoda.muddywaters.whale.servlet.rest.ResourceModule}
	 */

	@Override
	public Map<String, Object> getProperties() {
		return Collections.emptyMap();
	}

}