package com.hp.spmaas;

import java.io.InputStream;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;

public class TemplateResolver extends ResourceHandlerWrapper {
	private static final String DOT_XHTML = ".xhtml";
	private static final String SPMAAS_DYNAMICLAYOUT = "/spmaas/dynamiclayout/";
	private ResourceHandler wrapped;

	public TemplateResolver(ResourceHandler wrapped) {
		this.wrapped = wrapped;
	}

	@Override
	public ResourceHandler getWrapped() {
		// TODO Auto-generated method stub
		return wrapped;
	}



	public Resource createViewResource(String resourceName) {
		if (resourceName.startsWith(SPMAAS_DYNAMICLAYOUT) && resourceName.endsWith(DOT_XHTML))
		{
			String layout=resourceName.substring(SPMAAS_DYNAMICLAYOUT.length(),resourceName.lastIndexOf("."));		
			InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream(layout+DOT_XHTML);
			if (is!=null){
				String ctype=FacesContext.getCurrentInstance().getExternalContext().getMimeType(resourceName);
				Resource r=new SPMaasResource(resourceName, "spmaas", is, ctype);
				return r;
			}
			
		}
        return getWrapped().createViewResource(resourceName);
    }
	

}
