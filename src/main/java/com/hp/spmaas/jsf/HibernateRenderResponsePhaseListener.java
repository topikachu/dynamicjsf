package com.hp.spmaas.jsf;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.inject.Inject;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.spmaas.dal.HibernateSessionFactory;

@SuppressWarnings("serial")
//@JsfPhaseListene
public class HibernateRenderResponsePhaseListener implements PhaseListener {
        private Logger logger = LoggerFactory.getLogger(HibernateRenderResponsePhaseListener.class);
        @Inject
        SessionFactory sessionFactory;
    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }
 
    public void beforePhase(PhaseEvent event) {
    }
 
    public void afterPhase(PhaseEvent event) {
        // the Hibernate commit or roll back transaction ...
            try
            {
                logger.debug("afterPhase is invoked. ");
                        if( sessionFactory.getCurrentSession().getTransaction().isActive() )
                        {
                        logger.debug("commit is going to be performed" );
                                sessionFactory.getCurrentSession().getTransaction().commit();
                        }
                }
            catch (Throwable ex)
            {
                logger.error("commit exception", ex);
                        if( sessionFactory.getCurrentSession().getTransaction().isActive() )
                        {
                        logger.error("rollback is going to be performed" );
                                sessionFactory.getCurrentSession().getTransaction().rollback();
                        }
            }
    }
}
