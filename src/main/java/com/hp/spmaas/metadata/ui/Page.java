package com.hp.spmaas.metadata.ui;

import java.util.List;

public class Page {

	private String layoutName;

	private List<String> listProps ;
	private List<String> detailProps;

	private String objectType;
	
	
	public String getLayoutName() {
		return layoutName;
	}

	public void setLayoutName(String layoutName) {
		this.layoutName = layoutName;
	}

	public List<String> getListProps() {
		return listProps;
	}

	public void setListProps(List<String> listProperties) {
		this.listProps = listProperties;
	}

	public List<String> getDetailProps() {
		return detailProps;
	}

	public void setDetailProps(List<String> detailProperties) {
		this.detailProps = detailProperties;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	 
}
