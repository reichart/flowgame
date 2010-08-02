package de.tum.in.flowgame.server;

import java.util.List;

import javax.persistence.EntityManager;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.inject.Inject;

/**
 * Provides subclasses an {@link EntityManager} instance to access the database.
 */
public abstract class DatabaseAction extends ActionSupport {

	protected EntityManager em;

	@Inject("persistence")
	public void setPersistence(final PersistenceService service) {
		this.em = service.getEntityManager();
	}

	public <T> List<T> list(final Class<T> clazz) {
		return em.createQuery("SELECT t FROM " + clazz.getSimpleName() + " t", clazz).getResultList();
	}
}