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

	@Transient
	private int roundsPlayed;

	public ScenarioSession() {
		this.rounds = new ArrayList<ScenarioRound>();
	}

	public void add(final ScenarioRound round) {
		round.setPosition(rounds.size());
		rounds.add(round);
	}

	/**
	 * @return the next round to be played or <code>null</code> if there are no
	 *         more rounds
	 */
	public ScenarioRound getNextRound() {
		ScenarioRound round;
		try {
			round = rounds.get(roundsPlayed);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
		
		roundsPlayed++;
		
		return round;
	}

	public List<ScenarioRound> getRounds() {
		return rounds;
	}
}