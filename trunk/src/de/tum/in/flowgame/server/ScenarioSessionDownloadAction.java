package de.tum.in.flowgame.server;

import java.util.List;

import de.tum.in.flowgame.dao.ScenarioSessionDAO;
import de.tum.in.flowgame.dao.ScenarioSessionDAOImpl;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.ScenarioSession;

public class ScenarioSessionDownloadAction extends GameDataAction<Person, ScenarioSession> {

	@Override
	public ScenarioSession execute(final Person p) throws Exception {
		// TODO select depending on player
		final ScenarioSessionDAO dao = new ScenarioSessionDAOImpl();
		final List<ScenarioSession> scenarioSessions = dao.findAll();
		final Integer id = 0;
		ScenarioSession session = null;
		if (id < scenarioSessions.size()) {
			session = scenarioSessions.get(id);
		}

		if (session == null) {
			throw new Exception("No valid session could be found for Person " + p.getName());
		} else {
			return session;
		}
	}

}
