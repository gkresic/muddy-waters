package com.steatoda.muddywaters.shark;

import org.rapidoid.http.Req;
import org.rapidoid.http.ReqRespHandler;
import org.rapidoid.http.Resp;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Handler implements ReqRespHandler {

	@Override
	public Object execute(Req req, Resp resp) throws Exception {
	
		Payload[] payloads = Mapper.readValue(req.body(), Payload[].class);
		
		Payload max = new Payload();
		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}
		
		return resp.code(200).body(Mapper.writeValueAsBytes(max));
		
	}

	private static final long serialVersionUID = 1L;

	private static final ObjectMapper Mapper = new ObjectMapper();
	
}
