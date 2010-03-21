package de.tum.in.flowgame.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Question extends AbstractEntity {

	@Column(length = 500, nullable = false)
	private String text;

	@SuppressWarnings("unused")
	private Question() {
		// for JPA
	}

	protected Question(final String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}