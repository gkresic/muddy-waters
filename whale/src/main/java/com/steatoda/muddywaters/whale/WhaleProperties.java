package com.steatoda.muddywaters.whale;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Main Wahle properties.
 * Since it is used in configuring Guice DI, it can not be itself managed by Guice.
 */
public class WhaleProperties extends Properties {

	public static WhaleProperties get() {
		if (instance == null)
			instance = new WhaleProperties();
		return instance;
	}
	
	private WhaleProperties() {

		super();

		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/steatoda/muddywaters/whale/whale.properties")) {
			load(istream);
			version = getProperty("version");
		} catch (IOException e) {
			throw new RuntimeException("Could not load properties", e);
		}

	}

	public String getVersion() { return version; }

	private static final long serialVersionUID = 1L;
	private static WhaleProperties instance = null;
	
	private final String version;

}
