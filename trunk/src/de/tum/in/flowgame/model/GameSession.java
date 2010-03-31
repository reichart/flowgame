package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class GameSession extends AbstractEntity {
	private ScenarioSession scenarioSession;
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Person player;
	private Difficulty baseline;
	@OneToMany(cascade = CascadeType.PERSIST)
	private final List<GameRound> rounds;
	@OneToMany(cascade = CascadeType.PERSIST)
	private List<Answer> answers;

	private GameSession() { // for JPA
		this.rounds = new ArrayList<GameRound>();
	}

	public GameSession(final long playerId, final ScenarioSession scenarioSession) {
		this();
		this.player = new Person(playerId, null);
		this.scenarioSession = scenarioSession;
	}

	public GameRound newRound(final ScenarioRound nextRound) {
		final GameRound round = new GameRound(nextRound);
		rounds.add(round);
		return round;
	}
	
	public Person getPlayer() {
		return player;
	}

	public Difficulty getBaseline() {
		return baseline;
	}

	public List<GameRound> getRounds() {
		return rounds;
	}

	public ScenarioSession getScenarioSession() {
		return scenarioSession;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}

}