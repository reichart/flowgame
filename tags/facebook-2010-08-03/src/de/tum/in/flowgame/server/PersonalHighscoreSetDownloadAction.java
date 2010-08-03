package de.tum.in.flowgame.server;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Score;

public class PersonalHighscoreSetDownloadAction extends GameDataAction<Long, SortedSet<Score>> {
	
	@Override
	protected SortedSet<Score> execute(Long personId) throws Exception {
		final List<GameSession> sessions = em
			.createQuery("SELECT gs FROM GameSession gs WHERE gs.player.id=:id", GameSession.class)
			.setParameter("id", personId)
			.getResultList();
		
		final SortedSet<Score> result = new TreeSet<Score>();
		for (final GameSession session : sessions) {
			for (final GameRound round : session.getRounds()) {
				result.add(round.getScore());
			}
		}
		return result;
	}
}
