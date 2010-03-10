package de.tum.in.flowgame.model;

import java.io.Serializable;

public class Highscore implements Serializable {
	
	private final long personid;
	private final long score;
	private final Integer percentage;

	public Highscore(final long personid, final long score) {
		this(personid, score, null);
	}

	public Highscore(final long personid, final long score, final Integer percentage) {
		this.personid = personid;
		this.score = score;
		this.percentage = percentage;
	}

	public long getPersonid() {
		return personid;
	}

	public long getScore() {
		return score;
	}

	public Integer getPercentage() {
		return percentage;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Highscore) {
			if (personid == ((Highscore) obj).personid) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new Long(personid).hashCode();
	}

	@Override
	public String toString() {
		return "Highscore " + personid + " " + score + " " + percentage;
	}
}