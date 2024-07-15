package com.steatoda.muddywaters.megalodon;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class PayloadController {

	@PostMapping("/eat")
	public Payload eat(@RequestBody List<Payload> payloads) {
		
		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		return max;
		
	}

	@PostMapping("/eat/async")
	public Mono<Payload> eatAsync(@RequestBody List<Payload> payloads) {
		return Mono.just(eat(payloads));
	}

}
