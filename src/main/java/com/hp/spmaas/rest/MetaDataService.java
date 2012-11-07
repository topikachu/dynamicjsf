package com.hp.spmaas.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;
import org.apache.wink.common.annotations.Scope;

import com.hp.spmaas.cdi.tenant.TenantScoped;
import com.hp.spmaas.metadata.MetadataUtil;

@Path("/metadata")
@TenantScoped
public class MetaDataService {
	public MetaDataService(){
		System.out.println();
	}
	
	@Inject
	MetadataUtil metadataUtil;

	@GET
	@Path("/{type}")
	@Produces("text/xml")
	public StreamingOutput getDataModel(@PathParam("type") String type) {

		String fileName;
		if (type.equals("datamodel")) {
			fileName = metadataUtil.getDataModelMetaDataPath();
		} else if (type.equals("layout")) {
			fileName = metadataUtil.getUIMetaDataPath();
		} else {
			return null;
		}
		final InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(fileName);
		return new StreamingOutput() {

			@Override
			public void write(OutputStream os) throws IOException,
					WebApplicationException {
				IOUtils.copy(is, os);

			}
		};

	}
}
