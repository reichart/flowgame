package de.tum.in.flowgame.model;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Question extends AbstractEntity {
	
	public static final String separator = "<->";
	
	private int number;

	@Transient
	private String text;
	
	@SuppressWarnings("unused")
	private Question() {
		// for JPA
	}
	
	public Question(int number) {
		this.number = number;
	}
	
	/**
	 * Only for {@link Questionnaire}.
	 */
	protected void setText(final String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public int getNumber() {
		return number;
	}
	
}