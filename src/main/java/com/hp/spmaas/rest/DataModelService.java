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

@Path("/datamodel")
public class DataModelService {
	@GET
	@Path("/{param}")
	@Produces("text/xml")
	public StreamingOutput getDataModel(@PathParam("param") String modelName) {
 
		final InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("META-INF/model/Contract.ecore");
		return new StreamingOutput() {
			
			@Override
			public void write(OutputStream os) throws IOException,
					WebApplicationException {
				IOUtils.copy(is, os); 
				
			}
		};
		
 
	}
}
