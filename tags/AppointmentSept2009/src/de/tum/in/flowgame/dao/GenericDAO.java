package de.tum.in.flowgame.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDAO<T, ID extends Serializable> {

	T find(ID id);

	List<T> findAll();

	List<T> findAll(int start, int length);

	T create(T entity);

	T update(T entity);

	void delete(T entity);
	
	void delete(ID id);

	Class<T> getEntityClass();

	T newEntityInstance() throws Exception;
}
