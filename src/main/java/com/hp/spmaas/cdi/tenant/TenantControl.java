package com.hp.spmaas.cdi.tenant;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;

@ApplicationScoped
public class TenantControl {

	Map<String, Tenant> tenantBeans = new HashMap<String, Tenant>();

	public void associate(String tenantName) {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.put(TenantScope.SPMAAS_TENANT_NAME, tenantName);
		Tenant tenantBean = new TenantBean(tenantName);
		tenantBean = tenantBeans.get(tenantBean);
		if (tenantBean == null) {
			tenantBean=new TenantBean(tenantName);
			tenantBeans.put(tenantName, tenantBean);
		}
	}

	public void deassociate() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
				.remove(TenantScope.SPMAAS_TENANT_NAME);
	}

	public boolean hasTenantActive() {
		return FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get(TenantScope.SPMAAS_TENANT_NAME) != null;
	}
	
	@Produces
	public Tenant getTenant(){
		 String tenantName = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap()
		.get(TenantScope.SPMAAS_TENANT_NAME);
		 return tenantBeans.get(tenantName);
	}
}
