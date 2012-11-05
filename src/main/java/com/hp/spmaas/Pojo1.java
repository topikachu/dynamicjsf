package com.hp.spmaas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;

@ManagedBean

public class Pojo1 {
	private Pojo2 pojo2=new Pojo2();
	private String name="pojo1";
	List <Integer> list= Arrays.asList(1,2,3);
	
	
	public List<Integer> getList() {
		return list;
	}
	public void setList(List<Integer> list) {
		this.list = list;
	}
	public Pojo2 getPojo2() {
		return pojo2;
	}
	public void setPojo2(Pojo2 pojo2) {
		this.pojo2 = pojo2;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
