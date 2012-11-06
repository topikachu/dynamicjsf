package com.hp.spmaas.resource.url;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class SPMaasUrlHandler extends URLStreamHandler  {

	InputStream inputStream;
	
	
	
	
	public SPMaasUrlHandler(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
	}




	public InputStream getInputStream() {
		return inputStream;
	}




	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}




	@Override
	protected URLConnection openConnection(URL u) throws IOException {
		
		return new SPMaasURLConnection(u,inputStream);
	}

}
