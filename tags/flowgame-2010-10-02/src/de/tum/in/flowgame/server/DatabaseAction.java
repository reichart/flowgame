package de.tum.in.flowgame.server;

import java.util.List;

import javax.persistence.EntityManager;

import com.opensymphony.xwork2.ActionSupport;

import de.tum.in.flowgame.server.struts2.PersistenceAware;

/**
 * Provides subclasses an {@link EntityManager} instance to access the database.
 */
public abstract class DatabaseAction extends ActionSupport implements PersistenceAware {

	protected EntityManager em;

	public void setEntityManager(final EntityManager em) {
		this.em = em;
	}

	public <T> List<T> list(final Class<T> clazz) {
		return em.createQuery("SELECT t FROM " + clazz.getSimpleName() + " t", clazz).getResultList();
	}
}