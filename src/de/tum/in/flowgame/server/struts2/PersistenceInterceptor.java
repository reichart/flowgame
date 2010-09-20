package de.tum.in.flowgame.server.struts2;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

/**
 * Injects an {@link EntityManager} instance into actions implementing
 * {@link PersistenceAware} and closes it after invocation of the action.
 * <p>
 * Transaction handling (commit, rollback) is left to the action.
 * <p>
 * The {@link EntityManagerFactory} is closed upon undeploy/stop of the web
 * application.
 */
public class PersistenceInterceptor implements Interceptor {

	private String persistenceUnit;

	private EntityManagerFactory emf;

	public void init() {
		emf = Persistence.createEntityManagerFactory(persistenceUnit);
	}

	public String intercept(final ActionInvocation invocation) throws Exception {
		EntityManager em = null;

		final Object action = invocation.getAction();
		if (action instanceof PersistenceAware) {
			final PersistenceAware pa = (PersistenceAware) action;
			em = emf.createEntityManager();
			pa.setEntityManager(em);
		}

		try {
			return invocation.invoke();
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	public void setPersistenceUnit(final String persistenceUnit) {
		this.persistenceUnit = persistenceUnit;
	}

	public void destroy() {
		emf.close();
	}

}
