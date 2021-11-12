package com.steatoda.muddywaters.dolphin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * <p>Main Dolphin properties.</p>
 */
public class DolphinProperties extends Properties {

	public static DolphinProperties get() {
		if (instance == null)
			instance = new DolphinProperties();
		return instance;
	}

	private DolphinProperties() {

		super();

		try (InputStream istream = Thread.currentThread().getContextClassLoader().getResourceAsStream("com/steatoda/muddywaters/dolphin/dolphin.properties")) {
			load(istream);
			version = getProperty("version");
		} catch (IOException e) {
			throw new RuntimeException("Could not load properties", e);
		}

	}

	public String getVersion() { return version; }

	private static final long serialVersionUID = 1L;
	private static DolphinProperties instance = null;
	
	private final String version;

}
