package com.hp.spmaas.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
@Path("/layout")
public class LayoutService {
	
	
	
	
	@GET
	@Path("/{param}")
	@Produces("text/json")
	public StreamingOutput getDataModel(@PathParam("param") String layout) {
 
		final InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("META-INF/layout/screenlayouts.json");
		return new StreamingOutput() {
			
			@Override
			public void write(OutputStream os) throws IOException,
					WebApplicationException {
				IOUtils.copy(is, os); 
				
			}
		};
		
 
	}
}
