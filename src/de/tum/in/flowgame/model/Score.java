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
