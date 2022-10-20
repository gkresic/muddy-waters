package com.steatoda.muddywaters.orca;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Main Orca properties.</p>
 */
public class OrcaProperties extends Properties {

	public static OrcaProperties get() {
		if (instance == null)
			instance = new OrcaProperties();
		return instance;
	}

	private OrcaProperties() {

		super();

		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/steatoda/muddywaters/orca/orca.properties")) {
			load(istream);
			version = getProperty("version");
		} catch (IOException e) {
			throw new RuntimeException("Could not load properties", e);
		}

	}

	public String getVersion() { return version; }

	private static final long serialVersionUID = 1L;
	private static OrcaProperties instance = null;
	
	private final String version;

}
