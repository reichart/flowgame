package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Entity
public class ScenarioSession extends AbstractEntity implements Serializable {
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	List<ScenarioRound> rounds;
	String name;

	public enum Type {
		INDIVIDUAL, SOCIAL, BASELINE
	}

	Type type;

	@Transient
	int roundsPlayed;

	public ScenarioSession() {
		rounds = new ArrayList<ScenarioRound>();
		roundsPlayed = 0;
	}

	public ScenarioSession(final Type type) {
		this();
		this.type = type;
	}

	public List<ScenarioRound> getRounds() {
		return rounds;
	}

	// returns next round and counts how many rounds have been played
	public ScenarioRound getNextRound() {
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