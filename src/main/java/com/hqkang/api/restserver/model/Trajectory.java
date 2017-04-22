package com.hqkang.api.restserver.model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.jettison.json.JSONArray;
import org.wololo.geojson.GeoJSON;
import org.wololo.geojson.GeoJSONFactory;
import org.wololo.geojson.Geometry;
import org.wololo.jts2geojson.GeoJSONReader;
import org.wololo.jts2geojson.GeoJSONWriter;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.hqkang.api.restserver.model.*;
import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;
import com.hqkang.api.restserver.Neo4jConnection;
import org.wololo.geojson.Feature;

public class Trajectory {
	
	public static String getTraList() {
		String jsonStr = "",traid;
		JSONArray jsr = new JSONArray();
		IdentityHashMap<String,String> reslist = new IdentityHashMap<String,String>();
		try {
		Connection con= new Neo4jConnection().getConnection();
		try (Statement stmt = con.createStatement()) {
		    ResultSet rs = stmt.executeQuery("MATCH (a)-[r]-(b) WHERE b.Seq = \"0\" RETURN b.TraID");
		    while (rs.next()) {
		        jsonStr = rs.getString(1);

            	
            	jsr.put(getFileNameNoEx(jsonStr));
		    }
		}
		con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return jsr.toString();
		
	}
	
	public static GeoJSON getTra(String string, String paramgcj) {
		String jsonStr = "";
		 String traid="";
    	List<Polygon> listPol  = new ArrayList<Polygon>();
    	List<LineString> listLS = new ArrayList<LineString>();
    	boolean gcj = false;
    	if(paramgcj.equals("gcj"))
    			gcj = true;
    	Map<String, Object> properties = new HashMap<String, Object>();
		try{
		Connection con=  new Neo4jConnection().getConnection();
		  
		 
		try (Statement stmt = con.createStatement()) {
		    ResultSet rs = stmt.executeQuery("match (a)-[r]-(b) where b.TraID  contains \"/"+string+".plt\"  return b");
			
		    while (rs.next()) {
		    	Polygon[] polEnt = new Polygon[1];
		        jsonStr = rs.getString(1);
		        jsonStr = jsonStr.replaceAll("\"\\{\"", "\\{\"");
            	jsonStr = jsonStr.replaceAll("\"\\}\"", "\"\\}");
            	Map<String,Object> node=(Map)JSON.parse(jsonStr);  
            	Map<String, Object> mbrMap = (Map<String, Object>) node.get("MBRJSON");
            	String mbrjson = JSON.toJSONString(mbrMap);
            	
            	
            	MBR iMBR = Gson.class.newInstance().fromJson(mbrjson,MBR.class);
            		
            	listLS.add(iMBR.getInsidePoints().getLineString(gcj));
            	listPol.add(iMBR.geoJsonShape());
            	traid = iMBR.getTraID();
		    }
		}
		properties.put("TraID", traid);
		con.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
		  List<Feature> features = new ArrayList<Feature>();


	  	 GeoJSONWriter writer = new GeoJSONWriter();
	  	 GeometryFactory gf = new GeometryFactory();
	  	Polygon[] Pol = new Polygon[listPol.size()];
	  	LineString[] LS = new LineString[listLS.size()];


	  	listPol.toArray(Pol);
	  	listLS.toArray(LS);
	  	
	  	
	  	
	  	 
	  	 MultiPolygon mpg = gf.createMultiPolygon(Pol);
	  	 MultiLineString mls = gf.createMultiLineString(LS);
	  	 GeoJSON json = writer.write(mpg);
	  	 GeoJSON multiLSJson = writer.write(mls);
	  	GeoJSONReader reader = new GeoJSONReader();
	  	Geometry mulPol = (Geometry) GeoJSONFactory.create(json.toString());
	  	Geometry mulLS = (Geometry) GeoJSONFactory.create(multiLSJson.toString());
		  features.add(new Feature(traid+"_pol",mulPol, properties));
		  features.add(new Feature(traid+"_ls",mulLS, properties));
		  

		  
		return writer.write(features);
	}
	public static String getFileNameNoEx(String filename) { 
        if ((filename != null) && (filename.length() > 0)) { 
            int dot = filename.lastIndexOf('.'); 
            int stt = filename.lastIndexOf("/")+1;
            if ((dot >-1) && (dot < (filename.length()))) { 
                return filename.substring(stt, dot); 
            } 
        } 
        return filename; 
    } 
	

}
