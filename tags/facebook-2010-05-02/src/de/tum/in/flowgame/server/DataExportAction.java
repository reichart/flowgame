package de.tum.in.flowgame.server;

import java.util.List;

import de.tum.in.flowgame.model.GameSession;
import de.tum.in.flowgame.model.Person;
import de.tum.in.flowgame.model.ScenarioSession;

public class DataExportAction extends DatabaseAction {

	private List<Person> players;
	private List<ScenarioSession> scenarios;
	private List<GameSession> sessions;

	@Override
	public String execute() throws Exception {
		this.players = list(Person.class);
		this.scenarios = list(ScenarioSession.class);
		this.sessions = list(GameSession.class);
		return SUCCESS;
	}
	
	public List<Person> getPlayers() {
		return players;
	}

	public List<ScenarioSession> getScenarios() {
		return scenarios;
	}
	
	public List<GameSession> getSessions() {
		return sessions;
	}
}
