package de.tum.in.flowgame.server;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import com.opensymphony.xwork2.inject.Inject;

public class PersistenceService {

	private final EntityManager em;

	@Inject("persistenceUnit")
	public PersistenceService(final String persistenceUnit) {
		this.em = Persistence.createEntityManagerFactory(persistenceUnit).createEntityManager();
	}

	public EntityManager getEntityManager() {
		return em;
	}
}
