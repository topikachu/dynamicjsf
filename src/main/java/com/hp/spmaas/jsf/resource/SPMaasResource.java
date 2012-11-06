package com.hp.spmaas.jsf.resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.context.FacesContext;

import com.hp.spmaas.resource.url.SPMaasUrlHandler;

public final class SPMaasResource extends Resource {
	private final String resourceName;
	private final String libraryName;
	private final InputStream stream;
	private final String ctype;

	public SPMaasResource(String resourceName, String libraryName,
			InputStream stream, String ctype) {
		this.resourceName = resourceName;
		this.libraryName = libraryName;
		this.stream = stream;
		this.ctype = ctype;
	}

	@Override
	public boolean userAgentNeedsUpdate(FacesContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public URL getURL() {
		URL url=null;
		
			try {
				url=new URL(libraryName,"",-1,resourceName,new SPMaasUrlHandler(stream));
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return url;
		
	}

	@Override
	public Map<String, String> getResponseHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRequestPath() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		// TODO Auto-generated method stub
		return stream;
	}

	@Override
	public String getContentType() {
		return ctype;
	}

	@Override
	public String getLibraryName() {
		// TODO Auto-generated method stub
		return libraryName;
	}

	@Override
	public String getResourceName() {
		// TODO Auto-generated method stub
		return resourceName;
	}
}