package com.hp.spmaas.jsf.hibernate;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.inject.Inject;

import org.apache.myfaces.extensions.cdi.jsf.api.listener.phase.AfterPhase;
import org.apache.myfaces.extensions.cdi.jsf.api.listener.phase.BeforePhase;
import org.apache.myfaces.extensions.cdi.jsf.api.listener.phase.JsfPhaseId;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.spmaas.cdi.tenant.TenantControl;

@SuppressWarnings("serial")
@RequestScoped
public class HibernateSessionContextListener {
	private Logger logger = LoggerFactory
			.getLogger(HibernateSessionContextListener.class);
	@Inject
	SessionFactory sessionFactory;

	@Inject
	TenantControl tenantControl;

	// @BeforePhase(JsfPhaseId.RESTORE_VIEW)
	public void beforePhase(
			@Observes @BeforePhase(JsfPhaseId.RESTORE_VIEW) PhaseEvent phaseEvent) {
		if (tenantControl.hasTenantActive()) {

			sessionFactory.getCurrentSession().beginTransaction();
		}
	}

	// @AfterPhase(JsfPhaseId.RENDER_RESPONSE)
	public void afterPhase(
			@Observes @AfterPhase(JsfPhaseId.RENDER_RESPONSE) PhaseEvent phaseEvent) {
		// the Hibernate commit or roll back transaction ...
		if (!tenantControl.hasTenantActive()) {
			return;
		}
		try {
			logger.debug("afterPhase is invoked. ");
			if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
				logger.debug("commit is going to be performed");
				sessionFactory.getCurrentSession().getTransaction().commit();
			}
		} catch (Throwable ex) {
			logger.error("commit exception", ex);
			if (sessionFactory.getCurrentSession().getTransaction().isActive()) {
				logger.error("rollback is going to be performed");
				sessionFactory.getCurrentSession().getTransaction().rollback();
			}
		}
	}
}
