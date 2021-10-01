package com.steatoda.muddywaters.sailfish;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;

import java.util.List;

@Controller
public class PayloadController {

	@Post(uri = "/eat", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
	public Payload eat(@Body List<Payload> payloads) {

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		return max;

	}

}
