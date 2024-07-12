package com.steatoda.muddywaters.octopus.servlet;

import com.steatoda.muddywaters.octopus.Payload;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

import java.util.List;
import java.util.Map;

public class EatDataFetcher implements DataFetcher<Payload> {

	@Override
	public Payload get(DataFetchingEnvironment environment) throws Exception {

		List<Map<String, ?>> payloads = environment.getArgument("payloads");

		Payload max = new Payload();

		for (Map<String, ?> payload : payloads) {
			int number = (int) payload.get("number");
			String text = (String) payload.get("text");
			if (max.number == null || max.number < number)
				max.number = number;
			if (max.text == null || max.text.compareTo(text) < 0)
				max.text = text;
		}

		return max;

	}

}
