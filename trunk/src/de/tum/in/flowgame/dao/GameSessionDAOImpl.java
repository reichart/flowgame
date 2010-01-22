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
		List<GameSession> sessionList = q.getResultList();
		for (GameSession session : sessionList) {
			for (GameRound round : session.getRounds()) {
				result.add(new Score(round.getId(), round.getScore()));
				// System.out.println("ID: " + gr.getId());
				// System.out.println("Score: " + gr.getScore());
			}
		}

		return result;
	}

}