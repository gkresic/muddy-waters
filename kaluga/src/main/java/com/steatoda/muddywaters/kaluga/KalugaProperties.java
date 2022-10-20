package com.steatoda.muddywaters.kaluga;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Main Kaluga properties.</p>
 */
public class KalugaProperties extends Properties {

	public static KalugaProperties get() {
		if (instance == null)
			instance = new KalugaProperties();
		return instance;
	}

	private KalugaProperties() {

		super();

		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/steatoda/muddywaters/kaluga/kaluga.properties")) {
			load(istream);
			version = getProperty("version");
		} catch (IOException e) {
			throw new RuntimeException("Could not load properties", e);
		}

	}

	public String getVersion() { return version; }

	private static final long serialVersionUID = 1L;
	private static KalugaProperties instance = null;
	
	private final String version;

}
