package de.tum.in.flowgame.server.struts2;

import javax.persistence.EntityManager;

/**
 * Actions implementing this interface will receive an {@link EntityManager}
 * instance before invocation.
 */
public interface PersistenceAware {

	void setEntityManager(EntityManager em);
}
