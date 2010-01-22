package de.tum.in.flowgame.dao;

import java.util.List;

import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Score;

public interface GameSessionDAO extends GenericDAO<GameSession, Integer> {

	public List<Score> getPersonalScores(long personId);

}