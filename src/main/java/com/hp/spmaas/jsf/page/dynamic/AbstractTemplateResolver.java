package com.hp.spmaas.jsf.page.dynamic;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;

import com.hp.spmaas.jsf.resource.SPMaasResource;
import com.hp.spmaas.ui.metadata.Page;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public abstract class AbstractTemplateResolver extends ResourceHandlerWrapper {

	private static final String UTF8 = "utf8";
	private static final String DOT_XHTML = ".xhtml";
	private ResourceHandler wrapped;
	private Map<String, Page> pages;
	private Map<String, InputStream> facelets = new HashMap<String, InputStream>();

	public AbstractTemplateResolver(ResourceHandler wrapped) {
		this.wrapped=wrapped;
	}

	protected abstract String getLayoutPrefix();

	@Override
	public ResourceHandler getWrapped() {
		return wrapped;
	}

	public Resource createViewResource(String resourceName) {
	
		String layoutprefix = getLayoutPrefix();
		if (resourceName.startsWith(layoutprefix)
				&& resourceName.endsWith(DOT_XHTML)) {
			if (pages == null) {
				loadPageDefinations();
			}
	
			String layout = resourceName.substring(layoutprefix.length(),
					resourceName.lastIndexOf("."));
	
			InputStream is = getFacelet(layout);
	
			if (is != null) {
				String ctype = FacesContext.getCurrentInstance()
						.getExternalContext().getMimeType(resourceName);
				Resource r = new SPMaasResource(resourceName, "spmaas", is,
						ctype);
				return r;
			}
	
		}
		return getWrapped().createViewResource(resourceName);
	}

	private void loadPageDefinations() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		final ELContext elContext = facesContext.getELContext();
		ExpressionFactory expressionFactory = facesContext.getApplication()
				.getExpressionFactory();
	
		ValueExpression exp = expressionFactory.createValueExpression(
				elContext, "#{UIMetaDataProducer.dataPage}",
				Object.class);
		pages = (Map<String, Page>) exp.getValue(elContext);
	}

	private InputStream getFacelet(String layout) {
	
		InputStream is = facelets.get(layout);
		if (is == null) {
	
			is = createFacelet(layout);
			facelets.put(layout, is);
		}
		return is;
	}

	private InputStream createFacelet(String layout) {
		InputStream is = null;
		try {
	
			SPMaasTemplateLoader mtl = new SPMaasTemplateLoader();
			Configuration cfg = new Configuration();
			cfg.setTemplateLoader(mtl);
			cfg.setLocalizedLookup(false);
			Template detailTemplate = cfg
					.getTemplate("META-INF/template/"+getPageTemplateName());
			
	
			StringWriter strwriter = new StringWriter();
			Page page=pages.get(layout);
			detailTemplate.process( createFacelteContent(page), strwriter);
			is = new ByteArrayInputStream(strwriter.toString()
					.getBytes(UTF8));
	
		} catch (IOException e) {
	
		} catch (TemplateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return is;
	}

	protected abstract String getPageTemplateName() ;

	
	
	protected abstract Map<String, Object> createFacelteContent(Page page);

}
