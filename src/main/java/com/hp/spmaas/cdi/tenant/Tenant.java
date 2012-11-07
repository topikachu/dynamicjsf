package com.hp.spmaas.cdi.tenant;

public interface Tenant {

	public abstract String getTenantName();
	public abstract boolean hasTenant();
}
