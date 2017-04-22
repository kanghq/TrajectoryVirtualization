package com.hqkang.api.restserver.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.hqkang.api.restserver.model.Saying;


@Path("/hello")
public class HelloWorldResource {
	
	private static final String TEMPLATE = "Hello, %s!";
	
	@GET
	@Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Saying sayHello(@PathParam("name") String name) {		
		return new Saying(String.format(TEMPLATE, name));
    }
}
