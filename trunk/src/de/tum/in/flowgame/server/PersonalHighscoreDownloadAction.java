package de.tum.in.flowgame.server;

import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import de.tum.in.flowgame.client.HighscoreRequest;
import de.tum.in.flowgame.model.GameRound;
import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Score;

public class PersonalHighscoreDownloadAction extends GameDataAction<HighscoreRequest, SortedSet<Score>> {

	@Override
	protected SortedSet<Score> execute(final HighscoreRequest highscoreRequest) throws Exception {
		final long personId = highscoreRequest.getPersonId();
		final int numElements = highscoreRequest.getNumElements();
		return getPersonalScores(personId, numElements);
	}

	private SortedSet<Score> getPersonalScores(final long personId, int numElements) {
		final List<GameSession> sessions = em
			.createQuery("SELECT gs FROM GameSession gs WHERE gs.player.id=:id", GameSession.class)
			.setParameter("id", personId)
			.setMaxResults(numElements)
			.getResultList();
		
		final SortedSet<Score> result = new TreeSet<Score>(Collections.reverseOrder());
		for (final GameSession session : sessions) {
			for (final GameRound round : session.getRounds()) {
				result.add(round.getScore());
			}
		}
		return result;
	}
}
