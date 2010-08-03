package de.tum.in.flowgame.model;

import javax.persistence.Entity;

@Entity
public class Question extends AbstractEntity {
	
	public static final String separator = "<->";
	
	private int number;

	@SuppressWarnings("unused")
	private Question() {
		// for JPA
	}
	
	public Question(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	
}