package com.hqkang.api.restserver.rest;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonValue;

import com.hqkang.api.restserver.model.Trajectory;

public class TrajectoryResource {
	
	String traid;

	public TrajectoryResource(String traid) {
		// TODO Auto-generated constructor stub
		this.traid = traid;
	}

	
	@GET
	@Produces("application/json")
	@JsonValue
	public String getTrajectoriesAsJsonArray(String coo) throws SQLException {
		
		
		
		
		return Trajectory.getTra(traid, coo).toString();
		
	}
	
	

}
