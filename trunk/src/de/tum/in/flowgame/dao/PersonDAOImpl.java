package de.tum.in.flowgame.dao;

import de.tum.in.flowgame.model.Person;

public class PersonDAOImpl extends GenericJPADAO<Person, Long> implements PersonDAO {

	public PersonDAOImpl() {
		super("IDP", Person.class);
	}

}