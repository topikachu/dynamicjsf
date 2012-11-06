package com.hp.spmaas.jsf.page.dynamic;

import java.net.URL;

import freemarker.cache.URLTemplateLoader;



public class SPMaasTemplateLoader extends URLTemplateLoader {

	@Override
	protected URL getURL(String name) {
		return Thread.currentThread().getContextClassLoader().getResource(name);
	}

}
