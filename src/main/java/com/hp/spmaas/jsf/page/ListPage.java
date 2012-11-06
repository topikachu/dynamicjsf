package com.hp.spmaas.jsf.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.emf.ecore.EObject;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.hp.spmaas.ui.metadata.Page;
import com.hp.spmaas.ui.metadata.UIMetaDataProducer;
import com.ocpsoft.pretty.faces.annotation.URLAction;
import com.ocpsoft.pretty.faces.annotation.URLMapping;

@Named
@ConversationScoped
@URLMapping(id = "listPage", pattern = "/page/#{listPage.layout}", viewId = "/faces/list.xhtml")
public class ListPage implements Serializable {
	@Inject
	SessionFactory sessionFactory;
	private String layout;

	@Inject
	UIMetaDataProducer uiMetaDataProducer;

	private List<ColumnModel> columns = new ArrayList<ColumnModel>();;
	private List list;
	
	private Object selectedObject;
	private String objType;
	
	
	@Inject private Conversation conversation;
	

	

	public String getObjType() {
		return objType;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;

	}

	public List<ColumnModel> getColumns() {
		return columns;
	}

	@URLAction
	public void loadObject() {
		Page page = uiMetaDataProducer.getDataPage().get(layout);
		objType = page.getObjectType();
		columns=new ArrayList<ListPage.ColumnModel>();
		for (String prop : page.getListProps()) {
			ColumnModel column = new ColumnModel(prop, prop);
			columns.add(column);
		}
		Session session = sessionFactory.getCurrentSession();

		Query qry = session.createQuery("from " + objType);
		list = qry.list();
		if (conversation.isTransient()) {
			conversation.begin();
		}
	}

	public List getList() {
		return list;
	}
	
	
	
	
	public void setSelectedObject(Object selectObject) {
		this.selectedObject = selectObject;
	}

	public Object getSelectedObject() {
		return selectedObject;
	}




	static public class ColumnModel implements Serializable {

		private String header;
		private String property;
		
		
		public ColumnModel(String header, String property) {
			this.header = header;
			this.property = property;
		}

		public String getHeader() {
			return header;
		}

		public String getProperty() {
			return property;
		}
	}
}
