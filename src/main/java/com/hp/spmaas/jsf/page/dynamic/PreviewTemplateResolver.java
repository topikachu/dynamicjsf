package com.hp.spmaas.jsf.page.dynamic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.hp.spmaas.jsf.resource.SPMaasResource;
import com.hp.spmaas.metadata.ui.Page;

import com.hp.spmaas.ui.metadata.annotation.DetailPageDefinations;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;


//TODO: need modify to support multitenancy
public class PreviewTemplateResolver extends AbstractTemplateResolver {

	public PreviewTemplateResolver(ResourceHandler wrapped) {
		super(wrapped);
		
	}

	private static final String SPMAAS_LIST_DYNAMIC_PREFIX = "/spmaas/preview/dynamic/";

	@Override
	protected String getLayoutPrefix() {
		return SPMAAS_LIST_DYNAMIC_PREFIX;
	}

	@Override
	protected String getPageTemplateName() {
		return "detail.ftl";
	}

	@Override
	protected Map<String, Object> createFacelteContent(Page page) {
		List<String> widgets = new ArrayList<String>();
		for (String string : page.getDetailProps()) {
			String widget = "<spmaas:editor var=\"#{var}\" property=\""
					+ string + "\"></spmaas:editor>\n";
			widgets.add(widget);
		}
		
		Map<String, Object> rootMap = new HashMap<String, Object>();
		rootMap.put("widgets", widgets);
		return rootMap;
	}

	
	
}
