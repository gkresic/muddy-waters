package com.steatoda.muddywaters.beluga;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Main Beluga properties.</p>
 */
public class BelugaProperties extends Properties {

	public static BelugaProperties get() {
		if (instance == null)
			instance = new BelugaProperties();
		return instance;
	}

	private BelugaProperties() {

		super();

		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/steatoda/muddywaters/beluga/beluga.properties")) {
			load(istream);
			version = getProperty("version");
		} catch (IOException e) {
			throw new RuntimeException("Could not load properties", e);
		}

	}

	public String getVersion() { return version; }

	private static final long serialVersionUID = 1L;
	private static BelugaProperties instance = null;
	
	private final String version;

}
