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

	// returns next round and counts how many rounds have been played
	public ScenarioRound getNextRound() {
		for (Iterator<ScenarioRound> iterator = rounds.iterator(); iterator.hasNext();) {
			ScenarioRound scenarioRound = iterator.next();
			if (scenarioRound.getPosition() == roundsPlayed) {
				roundsPlayed++;
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