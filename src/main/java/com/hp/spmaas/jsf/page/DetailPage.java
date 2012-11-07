package com.hp.spmaas.jsf.page;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.emf.ecore.EObject;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import com.hp.spmaas.metadata.ui.UIMetaDataProducer;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Named
@ConversationScoped
@URLMapping(id = "detailPage", pattern = "/page/#{detailPage.layout}/#{detailPage.objid}", viewId = "/faces/detail.xhtml")
public class DetailPage implements Serializable {
	String layout;
	String objid;
	@Inject
	SessionFactory sessionFactory;

	@Inject
	private Conversation conversation;

	@Inject
	UIMetaDataProducer uiMetaDataProducer;

	Object object;

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

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	@URLAction
	public void loadObject() {

		String objType = uiMetaDataProducer.getDataPage()
				.get(layout).getObjectType();
		Session session = sessionFactory.getCurrentSession();

		Criteria criteria = session.createCriteria(objType).add(
				Restrictions.eq("id", objid));

		List list = criteria.list();
		object = (EObject) list.get(0);
		if (conversation.isTransient()) {
			conversation.begin();
		}
		// System.out.println(contractFromDb.eGet(contractName));
		// EObject requester = (EObject) contractFromDb.eGet(contractRequester);
		// System.out.println(requester.eGet(personName));

	}

	public String save() {
		Session session = sessionFactory.getCurrentSession();
		session.save(object);
		
		return "pretty:detailPage";
	}
}
