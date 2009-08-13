package de.tum.in.flowgame.dao;

import de.tum.in.flowgame.model.GameRound;

public class GameRoundDAOImpl extends GenericJPADAO<GameRound, Integer> implements GameRoundDAO {

	public GameRoundDAOImpl() {
		super("IDP", GameRound.class);
	}

}