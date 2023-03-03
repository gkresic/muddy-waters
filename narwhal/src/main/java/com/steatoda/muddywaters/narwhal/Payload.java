package com.steatoda.muddywaters.narwhal;

import com.dslplatform.json.CompiledJson;
import com.fasterxml.jackson.annotation.JsonProperty;

@CompiledJson
public class Payload {

	@JsonProperty
	public String text;

	@JsonProperty
	public Integer number;

}
