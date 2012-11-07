package com.hp.spmaas.dal.hibernate;

import java.util.Properties;
import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.teneo.PersistenceOptions;
import org.eclipse.emf.teneo.hibernate.HbDataStore;
import org.eclipse.emf.teneo.hibernate.HbHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Environment;

import com.hp.spmaas.cdi.tenant.TenantScoped;

@TenantScoped
public class HibernateSessionFactoryProducer {
	SessionFactory sessionFactory = null;

	@Inject
	EPackage contentPackage;

	@TenantScoped
	@Produces
	SessionFactory getSessionFactory() {
		// load ecore model
		if (sessionFactory != null)
			return sessionFactory;
		synchronized (HibernateSessionFactoryProducer.class) {
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
				props.setProperty("teneo.mapping.default_id_feature", "id");
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
				final HbDataStore hbds = new HbHelper()
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
		Session session = sessionFactory.openSession();
		Transaction tx = session.getTransaction();
		tx.begin();
		for (int i = 0; i < 10; i++) {
			EClass contractEClass = (EClass) contentPackage
					.getEClassifier("Contract");
			contractEClass.setName(contentPackage.getEClassifier("Contract")
					.getName());

			// retrieve all meta data of person
			EClass personEClass = (EClass) contentPackage
					.getEClassifier("Person");

			// create a dynamic person instance
			EObject person = EcoreUtil.create(personEClass);
			setValue(person, "name", "person" + String.valueOf(i));
			setValue(person, "id", genUUID());

			// create a dynamic contract instance
			EObject contract = EcoreUtil.create(contractEClass);
			setValue(contract, "name", "contract" + String.valueOf(i));
			setValue(contract, "requester", person);
			setValue(contract, "id", genUUID());
			// now persist them all

			session.save(person);
			session.save(contract);
		}
		tx.commit();
		session.close();

	}

	private String genUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
	}

	private void setValue(EObject o, String property, Object value) {
		EStructuralFeature propertyFeature = o.eClass().getEStructuralFeature(
				property);
		o.eSet(propertyFeature, value);

	}
}
