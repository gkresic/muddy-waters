package com.steatoda.muddywaters.shark;

import java.util.Iterator;

import org.rapidoid.http.MediaType;
import org.rapidoid.http.Req;
import org.rapidoid.http.ReqRespHandler;
import org.rapidoid.http.Resp;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Handler implements ReqRespHandler {

	@Override
	public Object execute(Req req, Resp resp) throws Exception {

		Payload max = new Payload();

		Iterator<Payload> iter = Mapper.readerFor(PayloadRef).readValues(req.body());

		while (iter.hasNext()) {
			Payload payload = iter.next();
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}
		
		return resp
			.code(200)
			.contentType(MediaType.JSON)
			.body(Mapper.writeValueAsBytes(max));
		
	}

	private static final long serialVersionUID = 1L;

	private static final ObjectMapper Mapper = new ObjectMapper();
	private static final TypeReference<Payload> PayloadRef = new TypeReference<Payload>() {};
	
}
