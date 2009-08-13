package de.tum.in.flowgame.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class GenericJPADAO<T, ID extends Serializable> implements GenericDAO<T, ID> {

	private static final Log log = LogFactory.getLog(GenericJPADAO.class);

	private EntityManager em;
	private final Class<T> entityClass;
	private final Query listQuery;

	public GenericJPADAO(final String persistenceUnitName, final Class<T> entityClass) {
		this(Persistence.createEntityManagerFactory(persistenceUnitName), entityClass);
	}

	public GenericJPADAO(final EntityManagerFactory emf, final Class<T> entityClass) {
		this(emf.createEntityManager(), entityClass);
	}

	public GenericJPADAO(final EntityManager em, final Class<T> entityClass) {
		this.em = em;
		this.entityClass = entityClass;

		this.listQuery = createFindQuery(entityClass);
	}
	
	@Override
	public Class<T> getEntityClass() {
		return entityClass;
	}

	@Override
	public T newEntityInstance() throws Exception {
		return getEntityClass().newInstance();
	}
	
	private Query createFindQuery(final Class<T> pEntityClass) {
		return em.createQuery("SELECT e FROM " + pEntityClass.getSimpleName() + " e");
	}

	/*
	 * For subclasses.
	 */
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public void setEntityManager(EntityManager em) {
		this.em = em;
	}
	
	@Override
	public T find(final ID id) {
		if (log.isDebugEnabled()) {
			log.debug("finding by id " + id);
		}
		return em.find(entityClass, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll() {
		if (log.isDebugEnabled()) {
			log.debug("finding all " + entityClass.getSimpleName());
		}
		final long start = System.currentTimeMillis();
		final List resultList = listQuery.getResultList();
		final long end = System.currentTimeMillis();
		log.info("findAll took " + (end-start) + "ms");
		
		return resultList;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<T> findAll(final int start, final int length) {
		if (log.isDebugEnabled()) {
			log.debug("finding " + length + " items from " + start + " for " + entityClass.getSimpleName());
		}
		return createFindQuery(entityClass).setFirstResult(start).setMaxResults(length).getResultList();
	}

	@Override
	public T create(final T entity) {
		if (log.isDebugEnabled()) {
			log.debug("creating " + entity);
		}

		em.getTransaction().begin();
		em.persist(entity);
		em.getTransaction().commit();
		
		em.isOpen();

		return entity;
	}

	@Override
	public T update(final T entity) {
		if (log.isDebugEnabled()) {
			log.debug("updating " + entity);
		}

		em.getTransaction().begin();
		em.merge(entity);
		em.getTransaction().commit();

		return entity;
	}

	@Override
	public void delete(final T entity) {
		if (log.isDebugEnabled()) {
			log.debug("deleting " + entity);
		}
		em.getTransaction().begin();
		em.remove(em.merge(entity));
		em.getTransaction().commit();
	}
	
	@Override
	public void delete(final ID id) {
		if (log.isDebugEnabled()) {
			log.debug("deleting Entity with ID " + id);
		}
		em.getTransaction().begin();
		T tempEntity = em.find(entityClass, id);
		em.remove(tempEntity);
		em.getTransaction().commit();
	}
}
