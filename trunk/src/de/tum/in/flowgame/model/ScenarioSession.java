package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class ScenarioSession extends AbstractEntity {
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ScenarioRound> rounds;
	
	@JoinColumn(nullable = false)
	@OneToOne(cascade=CascadeType.PERSIST)
	Questionnaire questionnaire;

	public enum Type {
		INDIVIDUAL, SOCIAL
	}

	private Type type;

	@Transient
	private int roundsPlayed;
	
	@SuppressWarnings("unused")
	private ScenarioSession() {
		this(null, null); // for JPA
	}

	public ScenarioSession(final Type type, final Questionnaire questionnaire) {
		this.rounds = new ArrayList<ScenarioRound>();
		this.type = type;
		this.questionnaire = questionnaire;
	}

	public void add(final ScenarioRound round) {
		round.setPosition(rounds.size());
		rounds.add(round);
	}

	/**
	 * Returns the next round to be played with a flag to control wether or not
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
		for (Iterator<ScenarioRound> iterator = rounds.iterator(); iterator.hasNext();) {
			ScenarioRound scenarioRound = iterator.next();
			if (scenarioRound.getPosition() == roundsPlayed) {
				// only actually change to the next round if told to do so: This
				// allows us to "peek" at the next round without activating it
				if (increment) {
					roundsPlayed++;
				}
				
				return scenarioRound;
			}
		}
		return null;
	}

	public Type getType() {
		return type;
	}
	
	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public List<ScenarioRound> getRounds() {
		return rounds;
	}
}