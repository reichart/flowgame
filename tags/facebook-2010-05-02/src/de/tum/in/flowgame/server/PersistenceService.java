package de.tum.in.flowgame.server;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.opensymphony.xwork2.inject.Inject;

public class PersistenceService {

	private final static Log log = LogFactory.getLog(PersistenceService.class);
	
	private final EntityManager em;

	@Inject("persistenceUnit")
	public PersistenceService(final String persistenceUnit) {
		try {
			this.em = Persistence.createEntityManagerFactory(persistenceUnit).createEntityManager();
		} catch (final RuntimeException ex) {
			log.error("failed to create entity manager", ex);
			throw ex;
		}
	}

	public EntityManager getEntityManager() {
		return em;
	}
}
