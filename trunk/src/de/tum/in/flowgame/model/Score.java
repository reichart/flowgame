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
		if (obj instanceof Score) {
			return startTime == ((Score) obj).startTime;
		}
		return false;
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

	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + startTime + ": " + score + "]";
	}
}
