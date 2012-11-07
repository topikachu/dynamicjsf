package com.hp.spmaas.cdi.tenant;

import javax.faces.context.FacesContext;

public class TenantBean implements Tenant {
	
	String tenantName;
	public TenantBean (String tenantName){
		this.tenantName=tenantName;
	}
	
	
	
	
	@Override
	public String getTenantName() {
		return tenantName;
	}




	public void associate (){
		
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tenant", this);
	}
	public void deassociate(){
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("tenant");
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("SPMAAS_TENANT_NAME");
	}




	@Override
	public boolean hasTenant() {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	

}
