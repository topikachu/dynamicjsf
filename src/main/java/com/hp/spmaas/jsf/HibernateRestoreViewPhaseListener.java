package com.hp.spmaas.jsf;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("serial")
public class HibernateRestoreViewPhaseListener implements PhaseListener {
	private Logger logger = LoggerFactory
			.getLogger(HibernateRestoreViewPhaseListener.class);
	@Inject
	SessionFactory sessionFactory;

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	public void beforePhase(PhaseEvent event) {
		logger.debug("beforePhase begin transaction");
		sessionFactory.getCurrentSession().beginTransaction();
	}

	public void afterPhase(PhaseEvent event) {
	}
}
