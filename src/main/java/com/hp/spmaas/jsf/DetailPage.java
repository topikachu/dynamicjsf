package com.hp.spmaas.jsf;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.emf.ecore.EObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Named
@RequestScoped

@URLMapping(id = "detailPage", pattern = "/detail/#{detailPage.layout}/#{detailPage.objid}", viewId = "/faces/detail.xhtml")
public class DetailPage {
	String layout;
	String objid;
	@Inject
	SessionFactory sessionFactory;
	EObject object;
	public String getLayout() {
		return layout;
	}
	public void setLayout(String layout) {
		this.layout = layout;
	}
	public String getObjid() {
		return objid;
	}
	public void setObjid(String objid) {
		this.objid = objid;
	}
	
	public EObject getObject() {
		return object;
	}
	public void setObject(EObject object) {
		this.object = object;
	}
	@URLAction
	public void loadObject(){
		Session session=sessionFactory.getCurrentSession();
		
		
		Query qry = session.createQuery("from Contract");
		List list = qry.list();
		object = (EObject) list.get(0);
		
//		System.out.println(contractFromDb.eGet(contractName));
//		EObject requester = (EObject) contractFromDb.eGet(contractRequester);		
//		System.out.println(requester.eGet(personName));
	
		
	}
	
}
