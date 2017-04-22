package com.hqkang.api.restserver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Neo4jConnection {
	private  Neo4jConnection instance;
	private  Connection dbConnection;
	public Neo4jConnection() throws SQLException {
		dbConnection = DriverManager.getConnection("jdbc:neo4j:bolt://localhost", "neo4j", "25519173");

		
	}

	 
	 public Connection getConnection() throws SQLException {
		 return this.dbConnection;
	 }

}
