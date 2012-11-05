package com.hp.spmaas.dal;

import java.net.URL;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

public class EMFMetaData {

	EPackage contentPackage = null;
	@Produces
	@ApplicationScoped
	EPackage getContentPackage() {
		if (contentPackage != null)
			return contentPackage;
		synchronized (EMFMetaData.class) {
			if (contentPackage != null) {
				return contentPackage;
			} else {
				ResourceSetImpl resSet = new ResourceSetImpl();
				resSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
						.put("*", new XMIResourceFactoryImpl());
				URL url = Thread.currentThread().getContextClassLoader()
						.getResource("Contract.ecore");
				Resource resource = resSet.getResource(
						URI.createURI(url.toString()), true);
				resource.getContents();

				// get the first package
				contentPackage = (EPackage) resource.getContents().get(0);

				// register all EClass
				EPackage.Registry.INSTANCE.put(contentPackage.getNsURI(),
						contentPackage);
				return contentPackage;
			}
		}
	}
}
