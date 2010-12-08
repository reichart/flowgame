package de.tum.in.flowgame.server;

import java.util.ArrayList;
import java.util.List;

import de.tum.in.flowgame.model.Highscore;
import de.tum.in.flowgame.model.SocialCurrentHighscore;

public class SocialCurrentHighscoresDownloadAction extends GameDataAction<SocialCurrentHighscore, List<Highscore>> {

	@Override
	public List<Highscore> execute(final SocialCurrentHighscore socialCurrentHighscore) throws Exception {
		final List<Highscore> highscores = new ArrayList<Highscore>();

		List<Long> persons = socialCurrentHighscore.getPersons();
		if (persons != null && !persons.isEmpty()) {
			final List<Highscore> globalHighscore = getGlobalHighscore();
			for (Highscore highscore : globalHighscore) {
				if (highscore.getPersonid() == socialCurrentHighscore.getCurrentScore().getPersonid()) {
					globalHighscore.remove(highscore);
					break;
				}
			}
			
			final List<Highscore> personsHighscore = getPersonsHighscore(persons);
			
			insertSorted(socialCurrentHighscore, personsHighscore);
			insertSorted(socialCurrentHighscore, globalHighscore);
			
			
			for (final Highscore highscore : personsHighscore) {
				// if you are the only person to play
				final Integer percentage;
				if (globalHighscore.size() <= 1) {
					percentage = 100;
				} else {
					percentage = (int) (globalHighscore.indexOf(highscore) * 100.0 / (globalHighscore.size() - 1));
				}
				highscores.add(new Highscore(highscore.getPersonid(), highscore.getScore(), percentage));
			}
		}

		return highscores;
	}

	private void insertSorted(final SocialCurrentHighscore socialCurrentHighscore, final List<Highscore> highscores) {
		int index = 0;
		for (final Highscore highscore : highscores) {
			if (socialCurrentHighscore.getCurrentScore().getScore() < highscore.getScore()) {
				break;
			}
			index++;
		}
		System.err.println(index);
		highscores.add(index, socialCurrentHighscore.getCurrentScore());
	}

	@SuppressWarnings("unchecked")
	private List<Highscore> getGlobalHighscore() {
		return transform(em.createQuery(
				"SELECT p.id, MAX(r.score) AS highscore" + " FROM Person p, GameSession s, IN (s.rounds) r" + " WHERE s.player.id = p.id"
						+ " GROUP BY p.id" + " ORDER BY highscore").getResultList());
	}

	@SuppressWarnings("unchecked")
	private List<Highscore> getPersonsHighscore(final List<Long> persons) {
		return transform(em
				.createQuery(
						"SELECT p.id, MAX(r.score) AS highscore" + " FROM Person p, GameSession s, IN (s.rounds) r" + " WHERE s.player.id = p.id"
								+ " AND p.id IN :personids" + " GROUP BY p.id" + " ORDER BY highscore").setParameter("personids", persons)
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
