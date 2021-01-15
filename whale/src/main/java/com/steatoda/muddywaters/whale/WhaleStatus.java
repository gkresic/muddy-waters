package com.steatoda.muddywaters.whale;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WhaleStatus {

	@JsonProperty
	public String getVersion() { return version; }
	public void setVersion(String version) { this.version = version; }

	private String version;
	
}
