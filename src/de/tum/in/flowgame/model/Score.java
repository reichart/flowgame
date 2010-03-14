package de.tum.in.flowgame.model;

import java.io.Serializable;

public class Score implements Serializable, Comparable<Score> {

	private final long startTime;
	private final long score;

	public Score(final long startTime, final long score) {
		this.startTime = startTime;
		this.score = score;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getScore() {
		return score;
	}

	@Override
	public int hashCode() {
		return (int) startTime;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final Score other = (Score) obj;
		if (startTime != other.startTime)
			return false;
		return true;
	}

	/**
	 * Sorts by {@link #startTime}.
	 */
	public int compareTo(final Score other) {
		if (this.startTime == other.startTime)
			return 0;
		else if (this.startTime > other.startTime)
			return 1;
		else
			return -1;
	}
}
