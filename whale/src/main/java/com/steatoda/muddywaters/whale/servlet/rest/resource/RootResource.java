package com.steatoda.muddywaters.whale.servlet.rest.resource;

import java.util.List;

import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
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

		//Log.debug("Out of {} payloads, max was {}:{} ", payloads.size(), max.text, max.number);

		return max;
		
	}
	
	private static final Logger Log = LoggerFactory.getLogger(RootResource.class);

	private final WhaleStatus status;

}