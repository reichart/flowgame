package de.tum.in.flowgame.dao;

import de.tum.in.flowgame.model.GameSession;

public class GameSessionDAOImpl extends GenericJPADAO<GameSession, Integer> implements GameSessionDAO {

	public GameSessionDAOImpl() {
		super("IDP", GameSession.class);
	}

}