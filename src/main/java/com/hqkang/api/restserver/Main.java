package com.hqkang.api.restserver;

import java.util.EnumSet;

import javax.servlet.DispatcherType;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import com.hqkang.api.restserver.rest.HelloWorldResource;


public class Main {

	private static final int DEFAULT_PORT = 8080;
	
	private int serverPort;
	
	public Main(int serverPort) throws Exception {
		this.serverPort = serverPort;
	
		
		Server server = configureServer();	        
        server.start();
        server.join();
	}	

	private Server configureServer() {
		ResourceConfig resourceConfig = new ResourceConfig();		
		resourceConfig.packages(HelloWorldResource.class.getPackage().getName());
		resourceConfig.register(JacksonFeature.class);
		ServletContainer servletContainer = new ServletContainer(resourceConfig);
		ServletHolder sh = new ServletHolder(servletContainer);                
		Server server = new Server(serverPort);		
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(sh, "/*");
        FilterHolder cors = context.addFilter(CrossOriginFilter.class,"/*",EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");
		server.setHandler(context);
		return server;
	}
	
	public static void main(String[] args) throws Exception {
		
		int serverPort = DEFAULT_PORT;
		
		if(args.length >= 1) {
			try {
				serverPort = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
		}
		
		new Main(serverPort);	
	}

}
