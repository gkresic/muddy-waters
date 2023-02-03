package com.steatoda.muddywaters.piranha;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.util.Properties;

/**
 * <p>Main Piranha properties.</p>
 */
public class PiranhaProperties extends Properties {

	public static PiranhaProperties get() {
		if (instance == null)
			instance = new PiranhaProperties();
		return instance;
	}

	private PiranhaProperties() {

		super();

		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/steatoda/muddywaters/piranha/piranha.properties")) {
			load(istream);
			version = getProperty("version");
		} catch (IOException e) {
			throw new RuntimeException("Could not load properties", e);
		}

	}

	public String getVersion() { return version; }

	@Serial
	private static final long serialVersionUID = 1L;

	private static PiranhaProperties instance = null;
	
	private final String version;

}
