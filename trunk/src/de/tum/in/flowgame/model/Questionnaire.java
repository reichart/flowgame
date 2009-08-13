package de.tum.in.flowgame.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Questionnaire extends AbstractEntity implements Serializable {

	@Column(length=50)
	String name;
	@Column(length=3000)
	String description;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}