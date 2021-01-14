package com.steatoda.muddywaters.shark;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Iterator;

import org.rapidoid.http.MediaType;
import org.rapidoid.http.Req;
import org.rapidoid.http.ReqRespHandler;
import org.rapidoid.http.Resp;

import com.dslplatform.json.DslJson;

public class Handler implements ReqRespHandler {

	@Override
	public Object execute(Req req, Resp resp) throws Exception {

		Payload max = new Payload();

		Iterator<Payload> iter = Json.iterateOver(Payload.class, new ByteArrayInputStream(req.body()));

		while (iter.hasNext()) {
			Payload payload = iter.next();
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}
		
		ByteArrayOutputStream ostream = new ByteArrayOutputStream(300);
		Json.serialize(max, ostream);
		
		return resp
			.code(200)
			.contentType(MediaType.JSON)
			.body(ostream.toByteArray())
		;
		
	}

	private static final long serialVersionUID = 1L;

	private static final DslJson<Payload> Json = new DslJson<>();
	
}
