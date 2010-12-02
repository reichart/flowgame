package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.List;

public class SocialCurrentHighscore implements Serializable {
	private List<Long> persons;
	private Highscore currentScore;
	
	public SocialCurrentHighscore(List<Long> persons, Long personid, long currentScore) {
		this.persons = persons;
		this.currentScore = new Highscore(personid, currentScore);
	}

	public List<Long> getPersons() {
		return persons;
	}
	
	public Highscore getCurrentScore() {
		return currentScore;
	}

}
