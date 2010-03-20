package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class ScenarioSession extends AbstractEntity {
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private final List<ScenarioRound> rounds;

	public enum Type {
		INDIVIDUAL, SOCIAL, BASELINE
	}

	private final Type type;

	@Transient
	private int roundsPlayed;

	public ScenarioSession() {
		this(null);
	}

	public ScenarioSession(final Type type) {
		this.rounds = new ArrayList<ScenarioRound>();
		this.type = type;
	}

	public void add(final ScenarioRound round) {
		rounds.add(round);
	}

	// returns next round and counts how many rounds have been played
	public ScenarioRound getNextRound() {
		// TODO iterator?
		ScenarioRound round = null;
		roundsPlayed++;
		if (rounds.size() > roundsPlayed) {
			round = rounds.get(roundsPlayed);
		}
		return round;
	}

	public ScenarioRound getCurrentRound() {
		ScenarioRound round = null;
		if (rounds.size() > roundsPlayed) {
			round = rounds.get(roundsPlayed);
		}
		return round;
	}

	public Type getType() {
		return type;
	}

}