package de.tum.in.flowgame.model;

import javax.persistence.Entity;
import javax.persistence.Transient;

@Entity
public class Question extends AbstractEntity {
	
	public static final String separator = "<->";

	@Transient
	private String text;
	
	public Question() {
		// for JPA
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
}