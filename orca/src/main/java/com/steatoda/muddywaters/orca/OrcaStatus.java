package com.steatoda.muddywaters.orca;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(
	fieldVisibility = JsonAutoDetect.Visibility.NONE,
	getterVisibility = JsonAutoDetect.Visibility.NONE,
	isGetterVisibility = JsonAutoDetect.Visibility.NONE
)
public class OrcaStatus {

	@JsonProperty
	public String getVersion() { return version; }
	public void setVersion(String version) { this.version = version; }

	private String version;
	
}
