package com.hp.spmaas;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.view.facelets.Facelet;
import javax.faces.view.facelets.FaceletFactory;

@ManagedBean
public class ViewBuilder {
	
	private UIComponent binding=null;
	private Facelet resouceToFacet(String resource) {
		FacesContext fctx = FacesContext.getCurrentInstance();
		Application app = fctx.getApplication();
		FaceletFactory faceletFactory = (FaceletFactory) FactoryFinder
				.getFactory(FactoryFinder.FACELET_FACTORY);
		Resource res = app.getResourceHandler().createResource(resource);
		Facelet f;
		try {
			f = faceletFactory.getFacelet(res.getURL());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return f;

	}

	private UIComponent faceletToComponent() {
		  FacesContext fctx = FacesContext.getCurrentInstance();
	        Application app = fctx.getApplication();
		FaceletFactory faceletFactory = (FaceletFactory) FactoryFinder
				.getFactory(FactoryFinder.FACELET_FACTORY);
		 
	       
	  
		Map attributes=new HashMap();
		attributes.put("value","hello");
		UIComponent component= faceletFactory.createComponent("http://java.sun.com/jsf/composite/spmaas", "comp", attributes);
		
		return component;
		
	}
	
	public UIComponent getBind(){
		if (binding==null){
			binding=faceletToComponent();
		}
		return binding;
	}
	public void setBind(UIComponent u){
		this.binding=u;
	}

}
