package com.steatoda.muddywaters.sailfish;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

@Introspected
public class Payload {

	@JsonProperty
	public String getText() { return text; }
	public void setText(String text) { this.text = text; }

	@JsonProperty
	public Integer getNumber() { return number; }
	public void setNumber(Integer number) { this.number = number; }

	public String text;
	public Integer number;

}
