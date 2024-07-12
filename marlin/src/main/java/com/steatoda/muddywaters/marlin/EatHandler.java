package com.steatoda.muddywaters.marlin;

import com.dslplatform.json.DslJson;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http.HttpStatus;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.util.Callback;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Iterator;

public class EatHandler extends Handler.Abstract {

	@Override
	public boolean handle(Request request, Response response, Callback callback) throws Exception {

		if (!HttpMethod.POST.is(request.getMethod()))
			return false;

		if (!Request.getPathInContext(request).equals("/eat"))
			return false;

		Payload max = new Payload();

		try (InputStream istream = Content.Source.asInputStream(request)) {
			Iterator<Payload> iter = Json.iterateOver(Payload.class, istream);
			while (iter.hasNext()) {
				Payload payload = iter.next();
				if (max.number == null || max.number < payload.number)
					max.number = payload.number;
				if (max.text == null || max.text.compareTo(payload.text) < 0)
					max.text = payload.text;
			}
		}

		ByteArrayOutputStream ostream = new ByteArrayOutputStream(300);
		Json.serialize(max, ostream);

		response.setStatus(HttpStatus.OK_200);
		response.getHeaders().put(HttpHeader.CONTENT_TYPE, "application/json");
		response.write(true, ByteBuffer.wrap(ostream.toByteArray()), callback);

		return true;

	}

	private static final DslJson<Payload> Json = new DslJson<>();

}
