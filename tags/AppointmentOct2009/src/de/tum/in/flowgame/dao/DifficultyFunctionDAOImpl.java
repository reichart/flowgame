package de.tum.in.flowgame.dao;

import de.tum.in.flowgame.model.DifficultyFunction;

public class DifficultyFunctionDAOImpl extends GenericJPADAO<DifficultyFunction, Integer> implements DifficultyFunctionDAO {

	public DifficultyFunctionDAOImpl() {
		super("IDP", DifficultyFunction.class);
	}

}