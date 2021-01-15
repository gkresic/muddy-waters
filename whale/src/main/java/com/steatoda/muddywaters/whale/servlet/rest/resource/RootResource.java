package com.steatoda.muddywaters.whale.servlet.rest.resource;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.steatoda.muddywaters.whale.Payload;
import com.steatoda.muddywaters.whale.WhaleStatus;

@Path("")
public class RootResource {

	@Inject
	public RootResource(WhaleStatus status) {
		this.status = status;
	}

	@GET
	@Path("status")
	@Produces(MediaType.APPLICATION_JSON)
	public WhaleStatus status(@Context HttpServletRequest request) {
		
		Log.debug("Serving status to {}", request.getRemoteAddr());
		
		return status;
		
	}

	@POST
	@Path("eat")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Payload eat(List<Payload> payloads) {

		Payload max = new Payload();

		for (Payload payload : payloads) {
			if (max.number == null || max.number < payload.number)
				max.number = payload.number;
			if (max.text == null || max.text.compareTo(payload.text) < 0)
				max.text = payload.text;
		}

		Log.debug("Out of {} payloads, max was {}:{} ", payloads.size(), max.text, max.number);

		return max;
		
	}
	
	private static final Logger Log = LoggerFactory.getLogger(RootResource.class);

	private final WhaleStatus status;

}