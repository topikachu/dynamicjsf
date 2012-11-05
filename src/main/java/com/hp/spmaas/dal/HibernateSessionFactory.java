package com.hp.spmaas.dal;

import java.util.Properties;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.teneo.PersistenceOptions;
import org.eclipse.emf.teneo.hibernate.HbDataStore;
import org.eclipse.emf.teneo.hibernate.HbHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Environment;

public class HibernateSessionFactory {
	SessionFactory sessionFactory = null;

	@Inject
	EPackage contentPackage;
	@ApplicationScoped
	@Produces
	SessionFactory getSessionFactory() {
		// load ecore model
		if (sessionFactory != null)
			return sessionFactory;
		synchronized (HibernateSessionFactory.class) {
			if (sessionFactory != null) {
				return sessionFactory;
			} else {
				
				final Properties props = new Properties();

				// the jdbc
				props.setProperty(Environment.DRIVER, "org.hsqldb.jdbcDriver");
				props.setProperty(Environment.USER, "sa");
				props.setProperty(Environment.URL, "jdbc:hsqldb:mem:library");
				props.setProperty(Environment.PASS, "");
				props.setProperty(Environment.DIALECT,
						org.hibernate.dialect.HSQLDialect.class.getName());

				// set a specific option
				// see this page
				// http://wiki.eclipse.org/Teneo/Hibernate/Configuration_Options
				// for all the available options
				props.setProperty(
						PersistenceOptions.CASCADE_POLICY_ON_NON_CONTAINMENT,
						"REFRESH,PERSIST,MERGE");

				// the name of the session factory
				String packageName = contentPackage.getName();
				// create the HbDataStore using the name
				final HbDataStore hbds = HbHelper.INSTANCE
						.createRegisterDataStore(packageName);

				// set the properties
				hbds.setDataStoreProperties(props);
				// Now reset the epackages in the datastore
				hbds.setEPackages(new EPackage[] { contentPackage });

				// recreate the database
				hbds.initialize();

				// print the hibernate.hbm.xml for demo purposes
				System.err.println(hbds.getMappingXML());
				sessionFactory = hbds.getSessionFactory();
				prepareData();
				return sessionFactory;
			}
		}
	}

	private void prepareData() {
		// retrieve all meta data of contract
				EClass contractEClass = (EClass) contentPackage.getEClassifier("Contract");		
				contractEClass.setName(contentPackage.getEClassifier("Contract").getName());
				EAttribute contractName = (EAttribute) contractEClass.getEStructuralFeature("name");
				EReference contractRequester=(EReference) contractEClass.getEStructuralFeature("requester");
				
				// retrieve all meta data of person 
				EClass personEClass = (EClass) contentPackage.getEClassifier("Person");		
				EAttribute personName = (EAttribute) personEClass.getEStructuralFeature("name");		
				
				// create a dynamic person instance
				EObject person=EcoreUtil.create(personEClass);
				person.eSet(personName, "person1");
				
				// create a dynamic contract instance
				EObject contract = EcoreUtil.create(contractEClass);
				contract.eSet(contractName, "contract one");
				contract.eSet(contractRequester, person);
				
				// now persist them all
				Session session = sessionFactory.openSession();
				Transaction tx = session.getTransaction();
				tx.begin();
				session.save(person);
				session.save(contract);
			
				tx.commit();
				session.close();
		
	}
}
