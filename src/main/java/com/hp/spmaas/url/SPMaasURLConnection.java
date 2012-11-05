package com.hp.spmaas.url;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class SPMaasURLConnection extends URLConnection {

	private InputStream inputStream;
	protected SPMaasURLConnection(URL url, InputStream inputStream) {
		super(url);
		this.inputStream = inputStream;
	}

	@Override
	public void connect() throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return inputStream;
		
	}

}
