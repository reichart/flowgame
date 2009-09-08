package de.tum.in.flowgame.server;

import java.util.List;

import de.tum.in.flowgame.dao.ScenarioSessionDAO;
import de.tum.in.flowgame.dao.ScenarioSessionDAOImpl;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.ScenarioSession;

public class ScenarioSessionDownloadAction extends GameDataAction {

	private final ScenarioSessionDAO dao;

	public ScenarioSessionDownloadAction() {
		this.dao = new ScenarioSessionDAOImpl();
	}

	@Override
	public Object execute(final Object o) throws Exception {
		if (o instanceof Person) {
			final Person p = (Person) o;

			// TODO select depending on player
			final List<ScenarioSession> scenarioSessions = dao.findAll();
			final Integer id = 0;
			ScenarioSession session = null;
			if (id < scenarioSessions.size()) {
				session = scenarioSessions.get(id);
			}

			if (session == null) {
				return "No valid session could be found for Person " + p.getName();
			} else {
				return session;
			}
		} else {
			return "The File did not refer to a valid Person. ScenarioSession could not be choosen";
		}
	}

}
