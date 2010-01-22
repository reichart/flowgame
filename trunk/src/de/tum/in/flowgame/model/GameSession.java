package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class GameSession extends AbstractEntity implements Serializable {
	ScenarioSession scenarioSession;
	@ManyToOne(cascade=CascadeType.PERSIST)
	Person player;
	Difficulty baseline;
	@OneToMany(cascade=CascadeType.PERSIST)
	List<GameRound> rounds;

	public GameSession() {
		rounds = new ArrayList<GameRound>();
	}

	public Person getPlayer() {
		return player;
	}

	public void setPlayer(Person player) {
		this.player = player;
	}

	public Difficulty getBaseline() {
		return baseline;
	}

	public void setBaseline(Difficulty baseline) {
		this.baseline = baseline;
	}
	
	public List<GameRound> getRounds() {
		return rounds;
	}

	public void addRound(GameRound round) {
		this.rounds.add(round);
	}

	public ScenarioSession getScenarioSession() {
		return scenarioSession;
	}

	public void setScenarioSession(ScenarioSession scenarioSession) {
		this.scenarioSession = scenarioSession;
	}
	
}