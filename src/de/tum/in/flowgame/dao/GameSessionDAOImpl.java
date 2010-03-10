package de.tum.in.flowgame.dao;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Query;

import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Score;

public class GameSessionDAOImpl extends GenericJPADAO<GameSession, Integer> implements GameSessionDAO {

	public GameSessionDAOImpl() {
		super("IDP", GameSession.class);
	}

	public List<Score> getPersonalScores(long personId) {
		// HashMap<Integer, Long> result = new HashMap<Integer, Long>();
		List<Score> result = new LinkedList<Score>();
		Query q = getEntityManager().createQuery("SELECT gs FROM GameSession gs WHERE gs.player.id=:id");
		q.setParameter("id", personId);
		
		System.err.println("person " + personId);
		
		List<GameSession> sessionList = q.getResultList();
		for (GameSession session : sessionList) {
			System.err.println("session " + session.getId());
			System.err.println("rounds " + session.getRounds());
			for (GameRound round : session.getRounds()) {
				Long startTime = round.getStartTime();
				Long score = round.getScore();
				result.add(new Score(startTime, score));
				// System.out.println("ID: " + gr.getId());
				// System.out.println("Score: " + gr.getScore());
			}
		}

		return result;
	}

}