package com.steatoda.muddywaters.marlin;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonAttribute;

@CompiledJson
public class Payload {

	@JsonAttribute
	public String text;

	@JsonAttribute
	public Integer number;

}
