package com.hp.spmaas.cdi.tenant;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.spi.Context;
import javax.enterprise.context.spi.Contextual;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.faces.context.FacesContext;

public class TenantScope implements Context {

	public static final String SPMAAS_TENANT_NAME = "SPMAAS_TENANT_NAME";

	@Override
	public Class<? extends Annotation> getScope() {
		return TenantScoped.class;
	}

	@Override
	public <T> T get(Contextual<T> contextual,
			CreationalContext<T> creationalContext) {
		Bean bean = (Bean) contextual;
		Map tenantMap = getTenantMap();
		
		String beanId = getId(bean);
		if (tenantMap.containsKey(beanId)) {
			return (T) tenantMap.get(beanId);
		} else {
			T 
			t = (T) bean.create(creationalContext);
			
			tenantMap.put(getId(bean), t);
			return t;
		}
	}

	private String getId(Bean bean) {
		if ( bean.getName()!=null)
			return bean.getName();
		else
			return bean.getBeanClass().getName();
	}

	private Map getTenantMap() {
		FacesContext fctx = FacesContext.getCurrentInstance();
		String tenantName = getTenantName();
		assert tenantName != null && !tenantName.equals("") : "tenant name is empty";
		Map tenantMap = (Map) fctx.getExternalContext().getApplicationMap()
				.get(tenantName);
		if (tenantMap == null)
			tenantMap = new HashMap();
		fctx.getExternalContext().getApplicationMap()
				.put(tenantName, tenantMap);
		return tenantMap;
	}

	private String getTenantName() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.get(SPMAAS_TENANT_NAME);
	}

	@Override
	public <T> T get(Contextual<T> contextual) {
		Bean bean = (Bean) contextual;
        Map viewMap = getTenantMap();
        if(viewMap.containsKey(getId(bean))) {
            return (T) viewMap.get(getId(bean));
        } else {
            return null;
        }
	}

	@Override
	public boolean isActive() {
		return true;
	}

}
