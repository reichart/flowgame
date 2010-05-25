package de.tum.in.flowgame.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ScenarioRound extends AbstractEntity {
	private Integer baselineModifier;
	private long expectedPlaytime;
	@OneToOne(cascade=CascadeType.PERSIST)
	private DifficultyFunction difficultyFunction;
	@OneToOne(cascade=CascadeType.PERSIST)
	private Questionnaire questionnaire;
	private boolean baseline;
	private int position;

	@SuppressWarnings("unused")
	private ScenarioRound() {
		// for JPA
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
			final DifficultyFunction f, final Questionnaire q) {
		this.baseline = baseline;
		this.baselineModifier = baselineModifier;
		this.expectedPlaytime = expectedPlaytime;
		this.difficultyFunction = f;
		this.questionnaire = q;
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

	public Questionnaire getQuestionnaire() {
		return questionnaire;
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