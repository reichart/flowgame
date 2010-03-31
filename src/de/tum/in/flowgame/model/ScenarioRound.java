package de.tum.in.flowgame.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ScenarioRound extends AbstractEntity {
	private Integer baselineModifier;
	private Long expectedPlaytime;
	@OneToOne(cascade=CascadeType.PERSIST)
	private DifficultyFunction difficultyFunction;
	@OneToOne(cascade=CascadeType.PERSIST)
	private Questionnaire questionnaire;
	private boolean baseline;

	@SuppressWarnings("unused")
	private ScenarioRound() {
		// for JPA
	}

	public ScenarioRound(final boolean baseline, final Integer baselineModifier, final Long expectedPlaytime,
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

	public Long getExpectedPlaytime() {
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
}