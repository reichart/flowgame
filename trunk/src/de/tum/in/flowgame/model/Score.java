package de.tum.in.flowgame.model;

import java.io.Serializable;

public class Score implements Serializable {
	
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
}
