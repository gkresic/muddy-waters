package com.steatoda.muddywaters.narwhal;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;

import javax.validation.constraints.NotEmpty;

public class NarwhalConfiguration extends Configuration {

	@JsonProperty
	public String getFoo() { return foo; }
	@JsonProperty
	public void setFoo(String foo) { this.foo = foo; }

	@NotEmpty
	private String foo;

}
