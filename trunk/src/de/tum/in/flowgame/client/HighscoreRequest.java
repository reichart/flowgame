package de.tum.in.flowgame.client;

import java.io.Serializable;

public class HighscoreRequest implements Serializable {

	private final long personId;
	private final int numElements;

	public HighscoreRequest(final long personId, final int numElements) {
		this.personId = personId;
		this.numElements = numElements;
	}

	public long getPersonId() {
		return personId;
	}

	public int getNumElements() {
		return numElements;
	}
}
