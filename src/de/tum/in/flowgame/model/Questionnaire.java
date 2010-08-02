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
	//instead of questions labels like happy<->unhappy are used
	@Column(nullable = false)
	private boolean labelDriven;

	@SuppressWarnings("unused")
	private Questionnaire() {
		this(null, null); // for JPA
	}
	
	public Questionnaire(final String name, final String description) {
		this(name, description, false);
	}
	
	public Questionnaire(final String name, final String description, final boolean labelDriven) {
		this.questions = new ArrayList<Question>();
		this.name = name;
		this.description = description;
		this.labelDriven = labelDriven;
	}

	public String getName() {
		return name;
	}

	public void addQuestion(final String text) {
		questions.add(new Question(text));
	}
	
	public void addLabelQuestion(final String label1, final String label2) {
		questions.add(new Question(label1 + Question.separator + label2));
	}
	
	public List<Question> getQuestions() {
		return questions;
	}

	public String getDescription() {
		return description;
	}

	public boolean isLabelDriven() {
		return labelDriven;
	}

}