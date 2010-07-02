package de.tum.in.flowgame.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import de.tum.in.flowgame.model.ScenarioRound;
import de.tum.in.flowgame.model.ScenarioSession;

public class ScenarioSessionDownloadAction extends
		GameDataAction<Long, ScenarioSession> {

	/**
	 * Download one of the ScenarioSessions, which one is chosen randomly
	 * the rounds within the Scenario are sorted in 50% of the cases otherwise they are shuffled (baseline rounds stay at the beginning)
	 */
	@Override
	public ScenarioSession execute(final Long playerId) throws Exception {
		Random rnd = new Random(System.currentTimeMillis());
		boolean sort = rnd.nextBoolean();
		System.err.println("sorted " + sort);

		final List<ScenarioSession> scenarioSessions = list(ScenarioSession.class);
		final Integer id = rnd.nextInt(scenarioSessions.size());
		System.err.println(id);
		ScenarioSession session = null;
		if (id < scenarioSessions.size()) {
			session = scenarioSessions.get(id);
			List<ScenarioRound> rounds = session.getRounds();
			if (sort) {
				Collections.sort(rounds);
			} else {
				//Make sure that baseline rounds are not shuffled
				//remove them from list
				List<ScenarioRound> baselineRounds = new ArrayList<ScenarioRound>();
				for (Iterator iterator = rounds.iterator(); iterator.hasNext();) {
					ScenarioRound round = (ScenarioRound) iterator.next();
					if (round.isBaselineRound()) {
						baselineRounds.add(round);
						iterator.remove();
					}
					
				}
				Collections.shuffle(rounds);
				//readd baseline Rounds at the beginning of the rounds list
				rounds.addAll(0, baselineRounds);
			}
		}

		if (session == null) {
			throw new Exception("No valid session could be found for player "
					+ playerId);
		} else {
			return session;
		}
	}

}
