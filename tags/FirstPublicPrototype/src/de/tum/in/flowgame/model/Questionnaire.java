package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Questionnaire extends AbstractEntity {

	@Column(length=50)
	String name;
	@OneToMany(cascade=CascadeType.ALL)
	List<Question> questions;
	
	public Questionnaire() {
		questions = new ArrayList<Question>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addQuestion(Question q) {
		questions.add(q);		
	}
	
	public List<Question> getQuestions() {
		return questions;
	}
}