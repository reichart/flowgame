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
	private List<ScenarioRound> rounds;

	public enum Type {
		INDIVIDUAL, SOCIAL
	}

	private Type type;

	@Transient
	private int roundsPlayed;

	@SuppressWarnings("unused")
	private ScenarioSession() {
		this(null); // for JPA
	}

	public ScenarioSession(final Type type) {
		this.rounds = new ArrayList<ScenarioRound>();
		this.type = type;
	}

	public void add(final ScenarioRound round) {
		round.setPosition(rounds.size());
		rounds.add(round);
	}

	/**
	 * to just peek at the next round (without changing any internal state) or
	 * actually getting the next round and incrementing the internal round
	 * counter.
	 * 
	 * @param increment
	 *            if <code>true</code>, increments the rounds played counter, if
	 *            <code>false</code>, the next call will return the same (next)
	 *            round
	 * @return the next round to be played or <code>null</code> if there are no
	 *         more rounds
	 */
	public ScenarioRound getNextRound(final boolean increment) {
		ScenarioRound round;
		try {
			round = rounds.get(roundsPlayed);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
		
		// only actually change to the next round if told to do so: This
		// allows us to "peek" at the next round without activating it
		if (increment) {
			roundsPlayed++;
		}
		
		int i = 0;
		for (ScenarioRound r : rounds) {
			System.err.println("number " + i + " Position " + r.getPosition() + " Baseline " + r.isBaselineRound());
			i++;
		}
		

		return round;
	}

	public Type getType() {
		return type;
	}

	public List<ScenarioRound> getRounds() {
		return rounds;
	}
}