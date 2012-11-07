package com.hp.spmaas.metadata;

import javax.inject.Inject;

import com.hp.spmaas.cdi.tenant.Tenant;
import com.hp.spmaas.cdi.tenant.TenantScoped;

@TenantScoped
public class MetadataUtil {
	@Inject Tenant tenant;
	public String getUIMetaDataPath(){
		return "META-INF/layout/"+tenant.getTenantName()+"/screenlayouts.json";
	}
	
	public String getDataModelMetaDataPath(){
		return "META-INF/model/"+tenant.getTenantName()+"/Contract.ecore";
	}
}
