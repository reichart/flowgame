package de.tum.in.flowgame.model;

import java.io.Serializable;

import javax.persistence.Entity;

@Entity
public class Answer extends AbstractEntity implements Serializable {
	Question question;
	Integer answer;
	
	public Answer() {
		//empty
	}
	
	public Answer(Question question, Integer answer) {
		this.question = question;
		this.answer = answer;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Integer getAnswer() {
		return answer;
	}

	public void setAnswer(Integer answer) {
		this.answer = answer;
	}

}
