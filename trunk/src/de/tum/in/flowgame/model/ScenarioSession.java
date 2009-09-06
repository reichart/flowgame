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
public class ScenarioSession extends AbstractEntity implements Serializable{
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	List<ScenarioRound> rounds;
	String name;
	
	@Transient
	int roundsPlayed;
	
	public ScenarioSession() {
		rounds = new ArrayList<ScenarioRound>();
		roundsPlayed = 0;
	}

	public List<ScenarioRound> getRounds() {
		return rounds;
	}
	
	// returns next round and counts how many rounds have been played
	public ScenarioRound getNextRound() {
		ScenarioRound round = null;
		if (rounds.size() > roundsPlayed) {
			round = rounds.get(roundsPlayed);
		}
		roundsPlayed++;
		return round;
	}
	
}
