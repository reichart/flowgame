package de.tum.in.flowgame.server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.tum.in.flowgame.model.Highscore;

/**
 * Calculates the index of a specified score in relation to the global
 * highscores where the highest score is 1 and the lowest score is 0.
 */
public class PercentageDownloadAction extends GameDataAction<Highscore, Integer> {

	/**
	 * @return a value between 0 and 1 indicating the relative position in all
	 *         highscores
	 */
	@Override
	public Integer execute(final Highscore score) throws Exception {
		final List<Highscore> highscores = getGlobalHighscore();
		// remove highscore from same player
		for (final Iterator<Highscore> it = highscores.iterator(); it.hasNext();) {
			final Highscore hs = it.next();
			if (hs.getPersonid() == score.getPersonid()) {
				it.remove();
				break;
			}
		}

		// find relative index of score in global highscores
		int idx = 0;
		for (final Highscore highscore : highscores) {
			if (score.getScore() < highscore.getScore()) {
				break;
			}
			idx++;
		}

		final double percentage;
		if (highscores.size() == 0) {
			// you are the only person to play so far
			percentage = 100;
		} else {
			// calculate percentage of index to number of total highscores
			percentage = idx * 100.0 / highscores.size();
		}
		return (int) percentage;
	}

	@SuppressWarnings("unchecked")
	private List<Highscore> getGlobalHighscore() {
		return transform(em.createQuery(
				"SELECT p.id, MAX(r.score) AS highscore" +
				" FROM Person p, GameSession s, IN (s.rounds) r" +
				" WHERE s.player.id = p.id" +
				" GROUP BY p.id" +
				" ORDER BY highscore")
				.getResultList());
	}

	private List<Highscore> transform(final List<Object[]> highscore) {
		final List<Highscore> list = new ArrayList<Highscore>();
		for (final Object[] objects : highscore) {
			list.add(new Highscore((Long) objects[0], (Long) objects[1]));
		}
		return list;
	}
}
