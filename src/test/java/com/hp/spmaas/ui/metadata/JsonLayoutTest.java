package com.hp.spmaas.ui.metadata;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.gson.Gson;
import com.hp.spmaas.metadata.ui.Page;
import com.hp.spmaas.metadata.ui.PageType;
import com.hp.spmaas.metadata.ui.UIMetaDataProducer;

public class JsonLayoutTest {

	
	@Test
	public void testLoadComplexLayoutJson() {
		
		List<Page> pages=new ArrayList<Page>();
		{
		Page page1=new Page();
		page1.setLayoutName("layout1");
		page1.setObjectType("Contract");
		List<String>listProps=Arrays.asList("name");
		List<String>detailProps=Arrays.asList("name");
		page1.setListProps(listProps);
		page1.setDetailProps(detailProps);
		pages.add(page1);
		}
		{
			Page page1=new Page();
			page1.setLayoutName("layout2");
			page1.setObjectType("Contract");
			List<String>listProps=Arrays.asList("name","requester.name");
			List<String>detailProps=Arrays.asList("name","requester.name");
			page1.setListProps(listProps);
			page1.setDetailProps(detailProps);
			pages.add(page1);
			}
		
		
//		Page page1=new Page();
//		page1.setLayoutName("layout1");
//		page1.setObjectType("Contract");
//		List<String>listProps=Arrays.asList("name");
//		List<String>detailProps=Arrays.asList("name");
//		page1.setListProps(listProps);
//		page1.setDetailProps(detailProps);
//		
//		DetailPageDefination dpd2=new DetailPageDefination();
//		dpd2.setLayoutName("layout2");
//		dpd2.setObjectType("Contract");
//		dpd2.addProperty("name");
//		dpd2.addProperty("requester.name");
//		List<DetailPageDefination>dpds = new ArrayList<DetailPageDefination>();
//		dpds.add(dpd1);
//		dpds.add(dpd2);
		Gson gson = new Gson();
		String dpdJson=gson.toJson(pages);
		System.out.println(dpdJson);
		//InputStream is=Thread.currentThread().getContextClassLoader().getResourceAsStream("layout.json");
		UIMetaDataProducer mdp = new UIMetaDataProducer();
		mdp.loadPage(new StringReader(dpdJson),PageType.data);
		assertEquals(2, mdp.getDataPage().size());
		
		assertEquals(1, mdp.getDataPage().get("layout1").getDetailProps().size());
				
	}
}
