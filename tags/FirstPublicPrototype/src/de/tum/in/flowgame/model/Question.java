package de.tum.in.flowgame.model;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Question extends AbstractEntity {
	@Column(length = 500)
	String text;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}