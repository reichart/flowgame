package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Entity
public class Questionnaire extends AbstractEntity {

	@Column(length = 50, nullable = false)
	private String name;
	@Column(length = 3000, nullable = false)
	private String description;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Question> questions;

	@SuppressWarnings("unused")
	private Questionnaire() {
		// for JPA
	}
	
	public Questionnaire(final String name, final String description) {
		this.questions = new ArrayList<Question>();
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void addQuestion(final String text) {
		questions.add(new Question(text));
	}

	public List<Question> getQuestions() {
		return questions;
	}

	public String getDescription() {
		return description;
	}

}