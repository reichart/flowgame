package de.tum.in.flowgame.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ScenarioRound extends AbstractEntity implements Comparable<ScenarioRound> {
	private Integer baselineModifier;
	private long expectedPlaytime;
	@OneToOne(cascade=CascadeType.PERSIST)
	private DifficultyFunction difficultyFunction;
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
	public ScenarioRound(final boolean baseline, final Integer baselineModifier, final long expectedPlaytime, final DifficultyFunction f) {
		this.baseline = baseline;
		this.baselineModifier = baselineModifier;
		this.expectedPlaytime = expectedPlaytime;
		this.difficultyFunction = f;
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
	
	public boolean isBaselineRound() {
		return baseline;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public int getPosition() {
		return position;
	}

	/**
	 * allow for sorting on the basis of the position
	 */
	public int compareTo(ScenarioRound scenarioRound) {
		if (scenarioRound == null) {
			throw new NullPointerException();
		} else {
			return this.getPosition() - scenarioRound.getPosition();
		}
	}
	
}