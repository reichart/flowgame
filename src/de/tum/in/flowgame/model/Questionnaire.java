package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import de.tum.in.flowgame.client.ui.screens.QuestionnaireMessages;

@Entity
public class Questionnaire extends AbstractEntity {

	@Column(length = 50, nullable = false)
	private String name;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Question> questions;
	//instead of questions labels like happy<->unhappy are used
	@Column(nullable = false)
	private boolean labelDriven;

	@SuppressWarnings("unused")
	private Questionnaire() {
		this(null); // for JPA
	}
	
	public Questionnaire(final String name) {
		this(name, false);
	}
	
	public Questionnaire(final String name, final boolean labelDriven) {
		this(name, labelDriven, 0);
	}
	
	/**
	 * Constructor for use with DDLGenerator only to first create the Questionnaires in database
	 * @param name
	 * @param description
	 * @param labelDriven
	 * @param numberOfQuestions
	 */
	public Questionnaire(final String name, final boolean labelDriven, final int numberOfQuestions) {
		this.questions = new ArrayList<Question>();
		this.name = name;
		this.labelDriven = labelDriven;
		for (int i = 0; i < numberOfQuestions; i++) {
			questions.add(new Question(i+1));
		}
	}

	public String getName() {
		return name;
	}

	public List<Question> getQuestions() {
		for (Question q : questions) {
			q.setText(QuestionnaireMessages.getString(name + q.getNumber()));
		}
		return questions;
	}

	public String getDescription() {
		return QuestionnaireMessages.getString(name + ".desc");
	}
	
	public String getTitel() {
		return QuestionnaireMessages.getString(name + ".title");
	}

	public boolean isLabelDriven() {
		return labelDriven;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[" + getName() + "]";
	}
}