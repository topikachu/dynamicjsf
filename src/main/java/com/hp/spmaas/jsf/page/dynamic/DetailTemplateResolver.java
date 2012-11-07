package com.hp.spmaas.jsf.page.dynamic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.ResourceHandler;

import com.hp.spmaas.metadata.ui.Page;

//TODO: need modify to support multitenancy
public class DetailTemplateResolver extends AbstractTemplateResolver {
	private static final String SPMAAS_DETAIL_DYNAMIC_PREFIX = "/spmaas/detail/dynamic/";

	public DetailTemplateResolver(ResourceHandler wrapped) {
		super(wrapped);
	}

	@Override
	protected String getLayoutPrefix() {
		return SPMAAS_DETAIL_DYNAMIC_PREFIX;
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

	@Override
	protected String getPageTemplateName() {
		return "detail.ftl";
	}

}
