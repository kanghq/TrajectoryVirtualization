package com.hqkang.api.restserver.rest;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.codehaus.jettison.json.JSONArray;

import com.hqkang.api.restserver.model.Trajectory;

@Path("/Trajectories/")
public class TrajectoriesResource {
	
	@GET
	@Produces("application/json")
	public String getTrajectoriesAsJsonArray() throws SQLException {
		return Trajectory.getTraList();
	}
	
	
	
	@GET
	@Path("{traid}/")
	@Produces("application/json")
	public String getTra(@PathParam("traid") String traid, @DefaultValue("non-gcj") @QueryParam("coo") String coo) throws SQLException {
		return new TrajectoryResource(traid).getTrajectoriesAsJsonArray(coo);
	}
	
	
	
	

}
