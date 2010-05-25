package de.tum.in.flowgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class ScenarioRound extends AbstractEntity {
	private Integer baselineModifier;
	private long expectedPlaytime;
	@OneToOne(cascade=CascadeType.PERSIST)
	private DifficultyFunction difficultyFunction;
	@OneToMany(cascade=CascadeType.PERSIST, fetch=FetchType.EAGER)
	private List<Questionnaire> questionnaires;
	private boolean baseline;
	private int position;

	@SuppressWarnings("unused")
	private ScenarioRound() {
		// for JPA
		questionnaires = new ArrayList<Questionnaire>();
	}

	/**
	 * @param baseline
	 *            <code>true</code> if this round is a
	 * @param baselineModifier
	 *            the baseline modifier
	 * @param expectedPlaytime
	 *            the expected play time
	 * @param f
	 *            the difficulty function
	 * @param q
	 *            the questionnaire to display after this round
	 */
	public ScenarioRound(final boolean baseline, final Integer baselineModifier, final long expectedPlaytime,
			final DifficultyFunction f) {
		this.baseline = baseline;
		this.baselineModifier = baselineModifier;
		this.expectedPlaytime = expectedPlaytime;
		this.difficultyFunction = f;
		questionnaires = new ArrayList<Questionnaire>();
	}

	public Integer getBaselineModifier() {
		return baselineModifier;
	}

	public long getExpectedPlaytime() {
		return expectedPlaytime;
	}

	public DifficultyFunction getDifficultyFunction() {
		return difficultyFunction;
	}
	
	public void addQuestionnaire(Questionnaire q) {
		questionnaires.add(q);
	}

	public Questionnaire getQuestionnaire(int i) {
		return questionnaires.get(i);
	}
	
	public int getNumberOfQuestionnaires() {
		return questionnaires.size();
	}
	
	public List<Questionnaire> getQuestionnaires() {
		return questionnaires;
	}

	public boolean isBaselineRound() {
		return baseline;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}
	
}