package com.hp.spmaas.metadata.dal;

import java.net.URL;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

import com.hp.spmaas.cdi.tenant.Tenant;
import com.hp.spmaas.cdi.tenant.TenantScoped;
import com.hp.spmaas.metadata.MetadataUtil;

@TenantScoped
public class EMFMetaDataProducer {

	
	@Inject
	private Tenant tenant;
	
	@Inject
	private MetadataUtil metadataUtil;
	
	EPackage contentPackage = null;
	@Produces
	@TenantScoped	
	EPackage getContentPackage() {
		if (contentPackage != null)
			return contentPackage;
		synchronized (EMFMetaDataProducer.class) {
			if (contentPackage != null) {
				return contentPackage;
			} else {
				ResourceSetImpl resSet = new ResourceSetImpl();
				resSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
						.put("*", new XMIResourceFactoryImpl());
				URL url = Thread.currentThread().getContextClassLoader()
						.getResource(metadataUtil.getDataModelMetaDataPath());
				Resource resource = resSet.getResource(
						URI.createURI(url.toString()), true);
				resource.getContents();

				// get the first package
				contentPackage = (EPackage) resource.getContents().get(0);
				EList<EClassifier> classes = contentPackage.getEClassifiers();
				
				// register all EClass
				EPackage.Registry.INSTANCE.put(contentPackage.getNsURI(),
						contentPackage);
				return contentPackage;
			}
		}
	}
}
