package com.hp.spmaas.jsf.page;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.hp.spmaas.cdi.tenant.TenantControl;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@RequestScoped
@Named
@URLMapping(id = "loginPage", pattern = "/login/#{loginPage.userName}", viewId = "#{loginPage.getNextPage}")
public class LoginPage {
	@Inject
	private TenantControl tenantControl;
	private String userName;

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getNextPage(){
		return login();
	}
	private String login() {
		if (userName.equals("Tommy")) {
			tenantControl.associate("CompanyABC");
			return "/faces/index.xhtml";
		} else if (userName.equals("Harry")) {
			tenantControl.associate("CompanyXYZ");
			return "/faces/index.xhtml";
		}
		return "";
	}
}
