package de.tum.in.flowgame.server;

import java.util.List;

import de.tum.in.flowgame.model.ScenarioSession;

public class ScenarioSessionDownloadAction extends GameDataAction<Long, ScenarioSession> {

	@Override
	public ScenarioSession execute(final Long playerId) throws Exception {
		// TODO select depending on player
		final List<ScenarioSession> scenarioSessions = list(ScenarioSession.class);
		final Integer id = 0;
		ScenarioSession session = null;
		if (id < scenarioSessions.size()) {
			session = scenarioSessions.get(id);
		}

		if (session == null) {
			throw new Exception("No valid session could be found for player " + playerId);
		} else {
			return session;
		}
	}

}
