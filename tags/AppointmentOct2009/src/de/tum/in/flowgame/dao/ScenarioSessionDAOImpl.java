package de.tum.in.flowgame.dao;

import de.tum.in.flowgame.model.ScenarioSession;

public class ScenarioSessionDAOImpl extends GenericJPADAO<ScenarioSession, Integer> implements ScenarioSessionDAO {

	public ScenarioSessionDAOImpl() {
		super("IDP", ScenarioSession.class);
	}

}