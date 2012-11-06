package com.hp.spmaas.ui.metadata;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@ApplicationScoped
@Named("UIMetaDataProducer")
public class UIMetaDataProducer {

	private static final String UTF8 = "utf8";
	private Map<String, Page> dataPage = new HashMap<String, Page>();

	@PostConstruct
	public void onStartup() {
		try {
			loadPage();

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void loadPage() throws UnsupportedEncodingException {

		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("META-INF/layout/screenlayouts.json");
		loadPage(new InputStreamReader(is, UTF8), PageType.data);

	}

	public Map<String, Page> getDataPage() {
		return dataPage;
	}

	public void loadPage(Reader r, PageType type) {
		Gson gson = new Gson();
		Type collectionType = null;
		Map target = null;
		if (type == PageType.data) {
			collectionType = new TypeToken<Collection<Page>>() {
			}.getType();
			target = dataPage;
		}

		for (Page pd : gson.<Collection<Page>> fromJson(r, collectionType)) {
			target.put(pd.getLayoutName(), pd);
		}
	}

}
