package de.tum.in.flowgame.model;

import javax.persistence.Entity;

@Entity
public class Answer extends AbstractEntity {
	Question question;
	Integer answer;
	
	public Answer() {
		//empty
	}
	
	public Answer(Question question, Integer answer) {
		this.question = question;
		this.answer = answer;
	}
}
